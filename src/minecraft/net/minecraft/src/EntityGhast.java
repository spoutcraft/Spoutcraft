package net.minecraft.src;

import net.minecraft.src.AchievementList;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityFireball;
import net.minecraft.src.EntityFlying;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IMob;
import net.minecraft.src.Item;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Vec3D;
import net.minecraft.src.World;
//Spout Start
import org.spoutcraft.client.entity.CraftGhast;
import org.spoutcraft.spoutcraftapi.entity.EntitySkinType;
//Spout End

public class EntityGhast extends EntityFlying implements IMob {

	public int courseChangeCooldown = 0;
	public double waypointX;
	public double waypointY;
	public double waypointZ;
	private Entity targetedEntity = null;
	private int aggroCooldown = 0;
	public int prevAttackCounter = 0;
	public int attackCounter = 0;

	public EntityGhast(World par1World) {
		super(par1World);
		this.texture = "/mob/ghast.png";
		this.setSize(4.0F, 4.0F);
		this.isImmuneToFire = true;
		this.experienceValue = 5;
		//Spout start
		this.spoutEntity = new CraftGhast(this);
		//Spout end
	}

	public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
		if ("fireball".equals(par1DamageSource.getDamageType()) && par1DamageSource.getEntity() instanceof EntityPlayer) {
			super.attackEntityFrom(par1DamageSource, 1000);
			((EntityPlayer)par1DamageSource.getEntity()).triggerAchievement(AchievementList.ghast);
			return true;
		} else {
			return super.attackEntityFrom(par1DamageSource, par2);
		}
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
	}

	public int getMaxHealth() {
		return 10;
	}

	public void onUpdate() {
		super.onUpdate();
		byte var1 = this.dataWatcher.getWatchableObjectByte(16);
		this.texture = var1 == 1?"/mob/ghast_fire.png":"/mob/ghast.png";
		//Spout Start
		setTextureToRender((byte) (var1 == 1? EntitySkinType.GHAST_MOUTH.getId() : 0));
		//Spout End
	}

	protected void updateEntityActionState() {
		if (!this.worldObj.isRemote && this.worldObj.difficultySetting == 0) {
			this.setEntityDead();
		}

		this.despawnEntity();
		this.prevAttackCounter = this.attackCounter;
		double var1 = this.waypointX - this.posX;
		double var3 = this.waypointY - this.posY;
		double var5 = this.waypointZ - this.posZ;
		double var7 = (double)MathHelper.sqrt_double(var1 * var1 + var3 * var3 + var5 * var5);
		if (var7 < 1.0D || var7 > 60.0D) {
			this.waypointX = this.posX + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
			this.waypointY = this.posY + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
			this.waypointZ = this.posZ + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
		}

		if (this.courseChangeCooldown-- <= 0) {
			this.courseChangeCooldown += this.rand.nextInt(5) + 2;
			if (this.isCourseTraversable(this.waypointX, this.waypointY, this.waypointZ, var7)) {
				this.motionX += var1 / var7 * 0.1D;
				this.motionY += var3 / var7 * 0.1D;
				this.motionZ += var5 / var7 * 0.1D;
			} else {
				this.waypointX = this.posX;
				this.waypointY = this.posY;
				this.waypointZ = this.posZ;
			}
		}

		if (this.targetedEntity != null && this.targetedEntity.isDead) {
			this.targetedEntity = null;
		}

		if (this.targetedEntity == null || this.aggroCooldown-- <= 0) {
			this.targetedEntity = this.worldObj.getClosestVulnerablePlayerToEntity(this, 100.0D);
			if (this.targetedEntity != null) {
				this.aggroCooldown = 20;
			}
		}

		double var9 = 64.0D;
		if (this.targetedEntity != null && this.targetedEntity.getDistanceSqToEntity(this) < var9 * var9) {
			double var11 = this.targetedEntity.posX - this.posX;
			double var13 = this.targetedEntity.boundingBox.minY + (double)(this.targetedEntity.height / 2.0F) - (this.posY + (double)(this.height / 2.0F));
			double var15 = this.targetedEntity.posZ - this.posZ;
			this.renderYawOffset = this.rotationYaw = -((float)Math.atan2(var11, var15)) * 180.0F / 3.1415927F;
			if (this.canEntityBeSeen(this.targetedEntity)) {
				if (this.attackCounter == 10) {
					this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1007, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
				}

				++this.attackCounter;
				if (this.attackCounter == 20) {
					this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1008, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
					EntityFireball var17 = new EntityFireball(this.worldObj, this, var11, var13, var15);
					double var18 = 4.0D;
					Vec3D var20 = this.getLook(1.0F);
					var17.posX = this.posX + var20.xCoord * var18;
					var17.posY = this.posY + (double)(this.height / 2.0F) + 0.5D;
					var17.posZ = this.posZ + var20.zCoord * var18;
					this.worldObj.spawnEntityInWorld(var17);
					this.attackCounter = -40;
				}
			} else if (this.attackCounter > 0) {
				--this.attackCounter;
			}
		} else {
			this.renderYawOffset = this.rotationYaw = -((float)Math.atan2(this.motionX, this.motionZ)) * 180.0F / 3.1415927F;
			if (this.attackCounter > 0) {
				--this.attackCounter;
			}
		}

		if (!this.worldObj.isRemote) {
			byte var21 = this.dataWatcher.getWatchableObjectByte(16);
			byte var12 = (byte)(this.attackCounter > 10?1:0);
			if (var21 != var12) {
				this.dataWatcher.updateObject(16, Byte.valueOf(var12));
			}
		}

	}

	private boolean isCourseTraversable(double par1, double par3, double par5, double par7) {
		double var9 = (this.waypointX - this.posX) / par7;
		double var11 = (this.waypointY - this.posY) / par7;
		double var13 = (this.waypointZ - this.posZ) / par7;
		AxisAlignedBB var15 = this.boundingBox.copy();

		for(int var16 = 1; (double)var16 < par7; ++var16) {
			var15.offset(var9, var11, var13);
			if (this.worldObj.getCollidingBoundingBoxes(this, var15).size() > 0) {
				return false;
			}
		}

		return true;
	}

	protected String getLivingSound() {
		return "mob.ghast.moan";
	}

	protected String getHurtSound() {
		return "mob.ghast.scream";
	}

	protected String getDeathSound() {
		return "mob.ghast.death";
	}

	protected int getDropItemId() {
		return Item.gunpowder.shiftedIndex;
	}

	protected void dropFewItems(boolean par1, int par2) {
		int var3 = this.rand.nextInt(2) + this.rand.nextInt(1 + par2);

		int var4;
		for(var4 = 0; var4 < var3; ++var4) {
			this.dropItem(Item.ghastTear.shiftedIndex, 1);
		}

		var3 = this.rand.nextInt(3) + this.rand.nextInt(1 + par2);

		for(var4 = 0; var4 < var3; ++var4) {
			this.dropItem(Item.gunpowder.shiftedIndex, 1);
		}

	}

	protected float getSoundVolume() {
		return 10.0F;
	}

	public boolean getCanSpawnHere() {
		return this.rand.nextInt(20) == 0 && super.getCanSpawnHere() && this.worldObj.difficultySetting > 0;
	}

	public int getMaxSpawnedInChunk() {
		return 1;
	}
}
