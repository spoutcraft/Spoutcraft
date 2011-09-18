package net.minecraft.src;

import java.util.Iterator;
import java.util.List;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityArrow;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntitySheep;
import net.minecraft.src.Item;
import net.minecraft.src.ItemFood;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.PathEntity;
import net.minecraft.src.World;

public class EntityWolf extends EntityAnimal {

	private boolean looksWithInterest = false;
	private float field_25048_b;
	private float field_25054_c;
	private boolean isWolfShaking;
	private boolean field_25052_g;
	private float timeWolfIsShaking;
	private float prevTimeWolfIsShaking;


	public EntityWolf(World var1) {
		super(var1);
		this.texture = "/mob/wolf.png";
		this.setSize(0.8F, 0.8F);
		this.moveSpeed = 1.1F;
		this.health = 8;
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
		this.dataWatcher.addObject(17, "");
		this.dataWatcher.addObject(18, new Integer(this.health));
	}

	protected boolean canTriggerWalking() {
		return false;
	}

	public String getEntityTexture() {
		//Spout Start
		if(isWolfTamed()) {

			return EntitySkinType.getTexture(EntitySkinType.WOLF_TAMED, this, "/mob/wolf_tame.png");
		}
		if(isWolfAngry()){
			return EntitySkinType.getTexture(EntitySkinType.WOLF_ANGRY, this, "/mob/wolf_angry.png");
		} else {
			return super.getEntityTexture();
		}
		//Spout End
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
		var1.setBoolean("Angry", this.isWolfAngry());
		var1.setBoolean("Sitting", this.isWolfSitting());
		if(this.getWolfOwner() == null) {
			var1.setString("Owner", "");
		} else {
			var1.setString("Owner", this.getWolfOwner());
		}

	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
		this.setWolfAngry(var1.getBoolean("Angry"));
		this.setIsSitting(var1.getBoolean("Sitting"));
		String var2 = var1.getString("Owner");
		if(var2.length() > 0) {
			this.setOwner(var2);
			this.setIsTamed(true);
		}

	}

	protected boolean canDespawn() {
		return !this.isWolfTamed();
	}

	protected String getLivingSound() {
		return this.isWolfAngry()?"mob.wolf.growl":(this.rand.nextInt(3) == 0?(this.isWolfTamed() && this.dataWatcher.getWatchableObjectInt(18) < 10?"mob.wolf.whine":"mob.wolf.panting"):"mob.wolf.bark");
	}

	protected String getHurtSound() {
		return "mob.wolf.hurt";
	}

	protected String getDeathSound() {
		return "mob.wolf.death";
	}

	protected float getSoundVolume() {
		return 0.4F;
	}

	protected int getDropItemId() {
		return -1;
	}

	protected void updateEntityActionState() {
		super.updateEntityActionState();
		if(!this.hasAttacked && !this.hasPath() && this.isWolfTamed() && this.ridingEntity == null) {
			EntityPlayer var3 = this.worldObj.getPlayerEntityByName(this.getWolfOwner());
			if(var3 != null) {
				float var2 = var3.getDistanceToEntity(this);
				if(var2 > 5.0F) {
					this.getPathOrWalkableBlock(var3, var2);
				}
			} else if(!this.isInWater()) {
				this.setIsSitting(true);
			}
		} else if(this.entityToAttack == null && !this.hasPath() && !this.isWolfTamed() && this.worldObj.rand.nextInt(100) == 0) {
			List var1 = this.worldObj.getEntitiesWithinAABB(EntitySheep.class, AxisAlignedBB.getBoundingBoxFromPool(this.posX, this.posY, this.posZ, this.posX + 1.0D, this.posY + 1.0D, this.posZ + 1.0D).expand(16.0D, 4.0D, 16.0D));
			if(!var1.isEmpty()) {
				this.setEntityToAttack((Entity)var1.get(this.worldObj.rand.nextInt(var1.size())));
			}
		}

		if(this.isInWater()) {
			this.setIsSitting(false);
		}

		if(!this.worldObj.multiplayerWorld) {
			this.dataWatcher.updateObject(18, Integer.valueOf(this.health));
		}

	}

	public void onLivingUpdate() {
		super.onLivingUpdate();
		this.looksWithInterest = false;
		if(this.hasCurrentTarget() && !this.hasPath() && !this.isWolfAngry()) {
			Entity var1 = this.getCurrentTarget();
			if(var1 instanceof EntityPlayer) {
				EntityPlayer var2 = (EntityPlayer)var1;
				ItemStack var3 = var2.inventory.getCurrentItem();
				if(var3 != null) {
					if(!this.isWolfTamed() && var3.itemID == Item.bone.shiftedIndex) {
						this.looksWithInterest = true;
					} else if(this.isWolfTamed() && Item.itemsList[var3.itemID] instanceof ItemFood) {
						this.looksWithInterest = ((ItemFood)Item.itemsList[var3.itemID]).getIsWolfsFavoriteMeat();
					}
				}
			}
		}

		if(!this.isMultiplayerEntity && this.isWolfShaking && !this.field_25052_g && !this.hasPath() && this.onGround) {
			this.field_25052_g = true;
			this.timeWolfIsShaking = 0.0F;
			this.prevTimeWolfIsShaking = 0.0F;
			this.worldObj.setEntityState(this, (byte)8);
		}

	}

	public void onUpdate() {
		super.onUpdate();
		this.field_25054_c = this.field_25048_b;
		if(this.looksWithInterest) {
			this.field_25048_b += (1.0F - this.field_25048_b) * 0.4F;
		} else {
			this.field_25048_b += (0.0F - this.field_25048_b) * 0.4F;
		}

		if(this.looksWithInterest) {
			this.numTicksToChaseTarget = 10;
		}

		if(this.isWet()) {
			this.isWolfShaking = true;
			this.field_25052_g = false;
			this.timeWolfIsShaking = 0.0F;
			this.prevTimeWolfIsShaking = 0.0F;
		} else if((this.isWolfShaking || this.field_25052_g) && this.field_25052_g) {
			if(this.timeWolfIsShaking == 0.0F) {
				this.worldObj.playSoundAtEntity(this, "mob.wolf.shake", this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			}

			this.prevTimeWolfIsShaking = this.timeWolfIsShaking;
			this.timeWolfIsShaking += 0.05F;
			if(this.prevTimeWolfIsShaking >= 2.0F) {
				this.isWolfShaking = false;
				this.field_25052_g = false;
				this.prevTimeWolfIsShaking = 0.0F;
				this.timeWolfIsShaking = 0.0F;
			}

			if(this.timeWolfIsShaking > 0.4F) {
				float var1 = (float)this.boundingBox.minY;
				int var2 = (int)(MathHelper.sin((this.timeWolfIsShaking - 0.4F) * 3.1415927F) * 7.0F);

				for(int var3 = 0; var3 < var2; ++var3) {
					float var4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
					float var5 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
					this.worldObj.spawnParticle("splash", this.posX + (double)var4, (double)(var1 + 0.8F), this.posZ + (double)var5, this.motionX, this.motionY, this.motionZ);
				}
			}
		}

	}

	public boolean getWolfShaking() {
		return this.isWolfShaking;
	}

	public float getShadingWhileShaking(float var1) {
		return 0.75F + (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * var1) / 2.0F * 0.25F;
	}

	public float getShakeAngle(float var1, float var2) {
		float var3 = (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * var1 + var2) / 1.8F;
		if(var3 < 0.0F) {
			var3 = 0.0F;
		} else if(var3 > 1.0F) {
			var3 = 1.0F;
		}

		return MathHelper.sin(var3 * 3.1415927F) * MathHelper.sin(var3 * 3.1415927F * 11.0F) * 0.15F * 3.1415927F;
	}

	public float getInterestedAngle(float var1) {
		return (this.field_25054_c + (this.field_25048_b - this.field_25054_c) * var1) * 0.15F * 3.1415927F;
	}

	public float getEyeHeight() {
		return this.height * 0.8F;
	}

	protected int getVerticalFaceSpeed() {
		return this.isWolfSitting()?20:super.getVerticalFaceSpeed();
	}

	private void getPathOrWalkableBlock(Entity var1, float var2) {
		PathEntity var3 = this.worldObj.getPathToEntity(this, var1, 16.0F);
		if(var3 == null && var2 > 12.0F) {
			int var4 = MathHelper.floor_double(var1.posX) - 2;
			int var5 = MathHelper.floor_double(var1.posZ) - 2;
			int var6 = MathHelper.floor_double(var1.boundingBox.minY);

			for(int var7 = 0; var7 <= 4; ++var7) {
				for(int var8 = 0; var8 <= 4; ++var8) {
					if((var7 < 1 || var8 < 1 || var7 > 3 || var8 > 3) && this.worldObj.isBlockNormalCube(var4 + var7, var6 - 1, var5 + var8) && !this.worldObj.isBlockNormalCube(var4 + var7, var6, var5 + var8) && !this.worldObj.isBlockNormalCube(var4 + var7, var6 + 1, var5 + var8)) {
						this.setLocationAndAngles((double)((float)(var4 + var7) + 0.5F), (double)var6, (double)((float)(var5 + var8) + 0.5F), this.rotationYaw, this.rotationPitch);
						return;
					}
				}
			}
		} else {
			this.setPathToEntity(var3);
		}

	}

	protected boolean isMovementCeased() {
		return this.isWolfSitting() || this.field_25052_g;
	}

	public boolean attackEntityFrom(DamageSource var1, int var2) {
		Entity var3 = var1.func_35532_a();
		this.setIsSitting(false);
		if(var3 != null && !(var3 instanceof EntityPlayer) && !(var3 instanceof EntityArrow)) {
			var2 = (var2 + 1) / 2;
		}

		if(!super.attackEntityFrom(var1, var2)) {
			return false;
		} else {
			if(!this.isWolfTamed() && !this.isWolfAngry()) {
				if(var3 instanceof EntityPlayer) {
					this.setWolfAngry(true);
					this.entityToAttack = var3;
				}

				if(var3 instanceof EntityArrow && ((EntityArrow)var3).shootingEntity != null) {
					var3 = ((EntityArrow)var3).shootingEntity;
				}

				if(var3 instanceof EntityLiving) {
					List var4 = this.worldObj.getEntitiesWithinAABB(EntityWolf.class, AxisAlignedBB.getBoundingBoxFromPool(this.posX, this.posY, this.posZ, this.posX + 1.0D, this.posY + 1.0D, this.posZ + 1.0D).expand(16.0D, 4.0D, 16.0D));
					Iterator var5 = var4.iterator();

					while(var5.hasNext()) {
						Entity var6 = (Entity)var5.next();
						EntityWolf var7 = (EntityWolf)var6;
						if(!var7.isWolfTamed() && var7.entityToAttack == null) {
							var7.entityToAttack = var3;
							if(var3 instanceof EntityPlayer) {
								var7.setWolfAngry(true);
							}
						}
					}
				}
			} else if(var3 != this && var3 != null) {
				if(this.isWolfTamed() && var3 instanceof EntityPlayer && ((EntityPlayer)var3).username.equalsIgnoreCase(this.getWolfOwner())) {
					return true;
				}

				this.entityToAttack = var3;
			}

			return true;
		}
	}

	protected Entity findPlayerToAttack() {
		return this.isWolfAngry()?this.worldObj.getClosestPlayerToEntity(this, 16.0D):null;
	}

	protected void attackEntity(Entity var1, float var2) {
		if(var2 > 2.0F && var2 < 6.0F && this.rand.nextInt(10) == 0) {
			if(this.onGround) {
				double var8 = var1.posX - this.posX;
				double var5 = var1.posZ - this.posZ;
				float var7 = MathHelper.sqrt_double(var8 * var8 + var5 * var5);
				this.motionX = var8 / (double)var7 * 0.5D * 0.800000011920929D + this.motionX * 0.20000000298023224D;
				this.motionZ = var5 / (double)var7 * 0.5D * 0.800000011920929D + this.motionZ * 0.20000000298023224D;
				this.motionY = 0.4000000059604645D;
			}
		} else if((double)var2 < 1.5D && var1.boundingBox.maxY > this.boundingBox.minY && var1.boundingBox.minY < this.boundingBox.maxY) {
			this.attackTime = 20;
			byte var3 = 2;
			if(this.isWolfTamed()) {
				var3 = 4;
			}

			var1.attackEntityFrom(DamageSource.func_35525_a(this), var3);
		}

	}

	public boolean interact(EntityPlayer var1) {
		ItemStack var2 = var1.inventory.getCurrentItem();
		if(!this.isWolfTamed()) {
			if(var2 != null && var2.itemID == Item.bone.shiftedIndex && !this.isWolfAngry()) {
				--var2.stackSize;
				if(var2.stackSize <= 0) {
					var1.inventory.setInventorySlotContents(var1.inventory.currentItem, (ItemStack)null);
				}

				if(!this.worldObj.multiplayerWorld) {
					if(this.rand.nextInt(3) == 0) {
						this.setIsTamed(true);
						this.setPathToEntity((PathEntity)null);
						this.setIsSitting(true);
						this.health = 20;
						this.setOwner(var1.username);
						this.showHeartsOrSmokeFX(true);
						this.worldObj.setEntityState(this, (byte)7);
					} else {
						this.showHeartsOrSmokeFX(false);
						this.worldObj.setEntityState(this, (byte)6);
					}
				}

				return true;
			}
		} else {
			if(var2 != null && Item.itemsList[var2.itemID] instanceof ItemFood) {
				ItemFood var3 = (ItemFood)Item.itemsList[var2.itemID];
				if(var3.getIsWolfsFavoriteMeat() && this.dataWatcher.getWatchableObjectInt(18) < 20) {
					--var2.stackSize;
					this.heal(var3.getHealAmount());
					if(var2.stackSize <= 0) {
						var1.inventory.setInventorySlotContents(var1.inventory.currentItem, (ItemStack)null);
					}

					return true;
				}
			}

			if(var1.username.equalsIgnoreCase(this.getWolfOwner())) {
				if(!this.worldObj.multiplayerWorld) {
					this.setIsSitting(!this.isWolfSitting());
					this.isJumping = false;
					this.setPathToEntity((PathEntity)null);
				}

				return true;
			}
		}

		return false;
	}

	void showHeartsOrSmokeFX(boolean var1) {
		String var2 = "heart";
		if(!var1) {
			var2 = "smoke";
		}

		for(int var3 = 0; var3 < 7; ++var3) {
			double var4 = this.rand.nextGaussian() * 0.02D;
			double var6 = this.rand.nextGaussian() * 0.02D;
			double var8 = this.rand.nextGaussian() * 0.02D;
			this.worldObj.spawnParticle(var2, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 0.5D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, var4, var6, var8);
		}

	}

	public void handleHealthUpdate(byte var1) {
		if(var1 == 7) {
			this.showHeartsOrSmokeFX(true);
		} else if(var1 == 6) {
			this.showHeartsOrSmokeFX(false);
		} else if(var1 == 8) {
			this.field_25052_g = true;
			this.timeWolfIsShaking = 0.0F;
			this.prevTimeWolfIsShaking = 0.0F;
		} else {
			super.handleHealthUpdate(var1);
		}

	}

	public float setTailRotation() {
		return this.isWolfAngry()?1.5393804F:(this.isWolfTamed()?(0.55F - (float)(20 - this.dataWatcher.getWatchableObjectInt(18)) * 0.02F) * 3.1415927F:0.62831855F);
	}

	public int getMaxSpawnedInChunk() {
		return 8;
	}

	public String getWolfOwner() {
		return this.dataWatcher.getWatchableObjectString(17);
	}

	public void setOwner(String var1) {
		this.dataWatcher.updateObject(17, var1);
	}

	public boolean isWolfSitting() {
		return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	public void setIsSitting(boolean var1) {
		byte var2 = this.dataWatcher.getWatchableObjectByte(16);
		if(var1) {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte)(var2 | 1)));
		} else {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte)(var2 & -2)));
		}

	}

	public boolean isWolfAngry() {
		return (this.dataWatcher.getWatchableObjectByte(16) & 2) != 0;
	}

	public void setWolfAngry(boolean var1) {
		byte var2 = this.dataWatcher.getWatchableObjectByte(16);
		if(var1) {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte)(var2 | 2)));
		} else {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte)(var2 & -3)));
		}

	}

	public boolean isWolfTamed() {
		return (this.dataWatcher.getWatchableObjectByte(16) & 4) != 0;
	}

	public void setIsTamed(boolean var1) {
		byte var2 = this.dataWatcher.getWatchableObjectByte(16);
		if(var1) {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte)(var2 | 4)));
		} else {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte)(var2 & -5)));
		}

	}
}
