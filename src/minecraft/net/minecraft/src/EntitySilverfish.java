package net.minecraft.src;

import org.spoutcraft.client.entity.CraftSilverfish;

import net.minecraft.src.Block;
import net.minecraft.src.BlockSilverfish;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityDamageSource;
import net.minecraft.src.EntityMob;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumCreatureAttribute;
import net.minecraft.src.Facing;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

public class EntitySilverfish extends EntityMob {

	private int allySummonCooldown;

	public EntitySilverfish(World par1World) {
		super(par1World);
		this.texture = "/mob/silverfish.png";
		this.setSize(0.3F, 0.7F);
		this.moveSpeed = 0.6F;
		this.attackStrength = 1;
		//Spout start
		this.spoutEntity = new CraftSilverfish(this);
		//Spout end
	}

	public int getMaxHealth() {
		return 8;
	}

	protected boolean canTriggerWalking() {
		return false;
	}

	protected Entity findPlayerToAttack() {
		double var1 = 8.0D;
		return this.worldObj.getClosestVulnerablePlayerToEntity(this, var1);
	}

	protected String getLivingSound() {
		return "mob.silverfish.say";
	}

	protected String getHurtSound() {
		return "mob.silverfish.hit";
	}

	protected String getDeathSound() {
		return "mob.silverfish.kill";
	}

	public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
		if (this.allySummonCooldown <= 0 && par1DamageSource instanceof EntityDamageSource) {
			this.allySummonCooldown = 20;
		}

		return super.attackEntityFrom(par1DamageSource, par2);
	}

	protected void attackEntity(Entity par1Entity, float par2) {
		if (this.attackTime <= 0 && par2 < 1.2F && par1Entity.boundingBox.maxY > this.boundingBox.minY && par1Entity.boundingBox.minY < this.boundingBox.maxY) {
			this.attackTime = 20;
			par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), this.attackStrength);
		}

	}

	protected void playStepSound(int par1, int par2, int par3, int par4) {
		this.worldObj.playSoundAtEntity(this, "mob.silverfish.step", 1.0F, 1.0F);
	}

	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);
	}

	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);
	}

	protected int getDropItemId() {
		return 0;
	}

	public void onUpdate() {
		this.renderYawOffset = this.rotationYaw;
		super.onUpdate();
	}

	protected void updateEntityActionState() {
		super.updateEntityActionState();
		if (!this.worldObj.isRemote) {
			int var1;
			int var2;
			int var3;
			int var5;
			if (this.allySummonCooldown > 0) {
				--this.allySummonCooldown;
				if (this.allySummonCooldown == 0) {
					var1 = MathHelper.floor_double(this.posX);
					var2 = MathHelper.floor_double(this.posY);
					var3 = MathHelper.floor_double(this.posZ);
					boolean var4 = false;

					for (var5 = 0; !var4 && var5 <= 5 && var5 >= -5; var5 = var5 <= 0?1 - var5:0 - var5) {
						for (int var6 = 0; !var4 && var6 <= 10 && var6 >= -10; var6 = var6 <= 0?1 - var6:0 - var6) {
							for (int var7 = 0; !var4 && var7 <= 10 && var7 >= -10; var7 = var7 <= 0?1 - var7:0 - var7) {
								int var8 = this.worldObj.getBlockId(var1 + var6, var2 + var5, var3 + var7);
								if (var8 == Block.silverfish.blockID) {
									this.worldObj.playAuxSFX(2001, var1 + var6, var2 + var5, var3 + var7, Block.silverfish.blockID + (this.worldObj.getBlockMetadata(var1 + var6, var2 + var5, var3 + var7) << 12));
									this.worldObj.setBlockWithNotify(var1 + var6, var2 + var5, var3 + var7, 0);
									Block.silverfish.onBlockDestroyedByPlayer(this.worldObj, var1 + var6, var2 + var5, var3 + var7, 0);
									if (this.rand.nextBoolean()) {
										var4 = true;
										break;
									}
								}
							}
						}
					}
				}
			}

			if (this.entityToAttack == null && !this.hasPath()) {
				var1 = MathHelper.floor_double(this.posX);
				var2 = MathHelper.floor_double(this.posY + 0.5D);
				var3 = MathHelper.floor_double(this.posZ);
				int var9 = this.rand.nextInt(6);
				var5 = this.worldObj.getBlockId(var1 + Facing.offsetsXForSide[var9], var2 + Facing.offsetsYForSide[var9], var3 + Facing.offsetsZForSide[var9]);
				if (BlockSilverfish.getPosingIdByMetadata(var5)) {
					this.worldObj.setBlockAndMetadataWithNotify(var1 + Facing.offsetsXForSide[var9], var2 + Facing.offsetsYForSide[var9], var3 + Facing.offsetsZForSide[var9], Block.silverfish.blockID, BlockSilverfish.getMetadataForBlockType(var5));
					this.spawnExplosionParticle();
					this.setEntityDead();
				} else {
					this.updateWanderPath();
				}
			} else if (this.entityToAttack != null && !this.hasPath()) {
				this.entityToAttack = null;
			}

		}
	}

	public float getBlockPathWeight(int par1, int par2, int par3) {
		return this.worldObj.getBlockId(par1, par2 - 1, par3) == Block.stone.blockID?10.0F:super.getBlockPathWeight(par1, par2, par3);
	}

	protected boolean isValidLightLevel() {
		return true;
	}

	public boolean getCanSpawnHere() {
		if (super.getCanSpawnHere()) {
			EntityPlayer var1 = this.worldObj.getClosestPlayerToEntity(this, 5.0D);
			return var1 == null;
		} else {
			return false;
		}
	}

	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.ARTHROPOD;
	}
}
