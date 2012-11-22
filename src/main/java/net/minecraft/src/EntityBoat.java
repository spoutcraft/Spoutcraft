package net.minecraft.src;

import java.util.List;
// Spout Start
import org.spoutcraft.client.entity.CraftBoat;
// Spout End

public class EntityBoat extends Entity {
	private boolean field_70279_a;
	private double field_70276_b;
	private int boatPosRotationIncrements;
	private double boatX;
	private double boatY;
	private double boatZ;
	private double boatYaw;
	private double boatPitch;
	private double velocityX;
	private double velocityY;
	private double velocityZ;

	// Spout Start
	public double maxSpeed = 0.4D;
	public double occupiedDeceleration = 0.2D;
	public double unoccupiedDeceleration = -1;
	public boolean landBoats = false;
	// Spout Ends

	public EntityBoat(World par1World) {
		super(par1World);
		this.field_70279_a = true;
		this.field_70276_b = 0.07D;
		this.preventEntitySpawning = true;
		this.setSize(1.5F, 0.6F);
		this.yOffset = this.height / 2.0F;

		// Spout Start
		this.spoutEntity = new CraftBoat(this);
		// Spout End
	}

	protected boolean canTriggerWalking() {
		return false;
	}

	protected void entityInit() {
		this.dataWatcher.addObject(17, new Integer(0));
		this.dataWatcher.addObject(18, new Integer(1));
		this.dataWatcher.addObject(19, new Integer(0));
	}

	public AxisAlignedBB getCollisionBox(Entity par1Entity) {
		return par1Entity.boundingBox;
	}

	public AxisAlignedBB getBoundingBox() {
		return this.boundingBox;
	}

	public boolean canBePushed() {
		return true;
	}

	public EntityBoat(World par1World, double par2, double par4, double par6) {
		this(par1World);
		this.setPosition(par2, par4 + (double)this.yOffset, par6);
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		this.prevPosX = par2;
		this.prevPosY = par4;
		this.prevPosZ = par6;
	}

	public double getMountedYOffset() {
		return (double)this.height * 0.0D - 0.30000001192092896D;
	}

	public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
		if (this.func_85032_ar()) {
			return false;
		} else if (!this.worldObj.isRemote && !this.isDead) {
			this.setForwardDirection(-this.getForwardDirection());
			this.setTimeSinceHit(10);
			this.setDamageTaken(this.getDamageTaken() + par2 * 10);
			this.setBeenAttacked();

			if (par1DamageSource.getEntity() instanceof EntityPlayer && ((EntityPlayer)par1DamageSource.getEntity()).capabilities.isCreativeMode) {
				this.setDamageTaken(100);
			}

			if(this.getDamageTaken() > 40) {
				if(this.riddenByEntity != null) {
					this.riddenByEntity.mountEntity(this);
				}

				this.dropItemWithOffset(Item.boat.shiftedIndex, 1, 0.0F);

				this.setDead();
			}

			return true;
		} else {
			return true;
		}
	}

	public void performHurtAnimation() {
		this.setForwardDirection(-this.getForwardDirection());
		this.setTimeSinceHit(10);
		this.setDamageTaken(this.getDamageTaken() * 11);
	}

	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
		if (this.field_70279_a) {
			this.boatPosRotationIncrements = par9 + 5;
		} else {
			double var10 = par1 - this.posX;
			double var12 = par3 - this.posY;
			double var14 = par5 - this.posZ;
			double var16 = var10 * var10 + var12 * var12 + var14 * var14;

			if (var16 <= 1.0D) {
				return;
			}

			this.boatPosRotationIncrements = 3;
		}
		this.boatX = par1;
		this.boatY = par3;
		this.boatZ = par5;
		this.boatYaw = (double)par7;
		this.boatPitch = (double)par8;
		this.motionX = this.velocityX;
		this.motionY = this.velocityY;
		this.motionZ = this.velocityZ;
	}

	public void setVelocity(double par1, double par3, double par5) {
		this.velocityX = this.motionX = par1;
		this.velocityY = this.motionY = par3;
		this.velocityZ = this.motionZ = par5;
	}

	public void onUpdate() {
		super.onUpdate();
		if(this.getTimeSinceHit() > 0) {
			this.setTimeSinceHit(this.getTimeSinceHit() - 1);
		}

		if(this.getDamageTaken() > 0) {
			this.setDamageTaken(this.getDamageTaken() - 1);
		}

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		byte var1 = 5;
		double var2 = 0.0D;

		for(int var4 = 0; var4 < var1; ++var4) {
			double var5 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (double)(var4 + 0) / (double)var1 - 0.125D;
			double var7 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (double)(var4 + 1) / (double)var1 - 0.125D;
			AxisAlignedBB var9 = AxisAlignedBB.getAABBPool().addOrModifyAABBInPool(this.boundingBox.minX, var5, this.boundingBox.minZ, this.boundingBox.maxX, var7, this.boundingBox.maxZ);
			if(this.worldObj.isAABBInMaterial(var9, Material.water)) {
				var2 += 1.0D / (double)var1;
			}
		}

		double var24 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
		double var6;
		double var8;

		if(var24 > 0.26249999999999996D) {
			var6 = Math.cos((double)this.rotationYaw * Math.PI / 180.0D);
			var8 = Math.sin((double)this.rotationYaw * Math.PI / 180.0D);

			for(int var10 = 0; (double)var10 < 1.0D + var24 * 60.0D; ++var10) {
				double var11 = (double)(this.rand.nextFloat() * 2.0F - 1.0F);
				double var13 = (double)(this.rand.nextInt(2) * 2 - 1) * 0.7D;
				double var15;
				double var17;
				if(this.rand.nextBoolean()) {
					var15 = this.posX - var6 * var11 * 0.8D + var8 * var13;
					var17 = this.posZ - var8 * var11 * 0.8D - var6 * var13;
					this.worldObj.spawnParticle("splash", var15, this.posY - 0.125D, var17, this.motionX, this.motionY, this.motionZ);
				} else {
					var15 = this.posX + var6 + var8 * var11 * 0.7D;
					var17 = this.posZ + var8 - var6 * var11 * 0.7D;
					this.worldObj.spawnParticle("splash", var15, this.posY - 0.125D, var17, this.motionX, this.motionY, this.motionZ);
				}
			}
		}

		double var12;
		double var26;

		if(this.worldObj.isRemote && this.field_70279_a) {
			if(this.boatPosRotationIncrements > 0) {
				var6 = this.posX + (this.boatX - this.posX) / (double)this.boatPosRotationIncrements;
				var8 = this.posY + (this.boatY - this.posY) / (double)this.boatPosRotationIncrements;
				var26 = this.posZ + (this.boatZ - this.posZ) / (double)this.boatPosRotationIncrements;

				var12 = MathHelper.wrapAngleTo180_double(this.boatYaw - (double)this.rotationYaw);

				this.rotationYaw = (float)((double)this.rotationYaw + var12 / (double)this.boatPosRotationIncrements);
				this.rotationPitch = (float)((double)this.rotationPitch + (this.boatPitch - (double)this.rotationPitch) / (double)this.boatPosRotationIncrements);
				--this.boatPosRotationIncrements;
				this.setPosition(var6, var8, var26);
				this.setRotation(this.rotationYaw, this.rotationPitch);
			} else {
				var6 = this.posX + this.motionX;
				var8 = this.posY + this.motionY;
				var26 = this.posZ + this.motionZ;
				this.setPosition(var6, var8, var26);
				if(this.onGround) {
					this.motionX *= 0.5D;
					this.motionY *= 0.5D;
					this.motionZ *= 0.5D;
				}

				this.motionX *= 0.9900000095367432D;
				this.motionY *= 0.949999988079071D;
				this.motionZ *= 0.9900000095367432D;
			}
		} else {
			if(var2 < 1.0D) {
				var6 = var2 * 2.0D - 1.0D;
				this.motionY += 0.03999999910593033D * var6;
			} else {
				if(this.motionY < 0.0D) {
					this.motionY /= 2.0D;
				}

				this.motionY += 0.007000000216066837D;
			}

			if(this.riddenByEntity != null) {
				this.motionX += this.riddenByEntity.motionX * this.field_70276_b;
				this.motionZ += this.riddenByEntity.motionZ * this.field_70276_b;
			}
			// Spout Start
			else if (unoccupiedDeceleration >= 0) {
				 this.motionX *= unoccupiedDeceleration;
				 this.motionZ *= unoccupiedDeceleration;
				 // Kill lingering speed
				 if (motionX <= 0.00001) {
					  motionX = 0;
				 }
				 if (motionZ <= 0.00001) {
					  motionZ = 0;
				 }
			}
			
			var24 = this.maxSpeed;
			// Spout End

			var6 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

			if (var6 > 0.35D) {
				var8 = 0.35D / var6;
				this.motionX *= var8;
				this.motionZ *= var8;
				var6 = 0.35D;
			}

			if (var6 > var24 && this.field_70276_b < 0.35D) {
				this.field_70276_b += (0.35D - this.field_70276_b) / 35.0D;

				if (this.field_70276_b > 0.35D) {
					this.field_70276_b = 0.35D;
				}
			} else {
				this.field_70276_b -= (this.field_70276_b - 0.07D) / 35.0D;

				if (this.field_70276_b < 0.07D) {
					this.field_70276_b = 0.07D;
				}
			}

			if(this.onGround) {
				this.motionX *= 0.5D;
				this.motionY *= 0.5D;
				this.motionZ *= 0.5D;
			}

			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			if(this.isCollidedHorizontally && var24 > 0.2D) {
				if(!this.worldObj.isRemote) {
					this.setDead();
					int var25;

					for(var25 = 0; var25 < 3; ++var25) {
						this.dropItemWithOffset(Block.planks.blockID, 1, 0.0F);
					}

					for(var25 = 0; var25 < 2; ++var25) {
						this.dropItemWithOffset(Item.stick.shiftedIndex, 1, 0.0F);
					}
				}
			} else {
				this.motionX *= 0.9900000095367432D;
				this.motionY *= 0.949999988079071D;
				this.motionZ *= 0.9900000095367432D;
			}

			this.rotationPitch = 0.0F;
			var8 = (double)this.rotationYaw;
			var26 = this.prevPosX - this.posX;
			var12 = this.prevPosZ - this.posZ;

			if (var26 * var26 + var12 * var12 > 0.001D) {
				var8 = (double)((float)(Math.atan2(var12, var26) * 180.0D / Math.PI));
			}

			double var14 = MathHelper.wrapAngleTo180_double(var8 - (double)this.rotationYaw);

			if(var14 > 20.0D) {
				var14 = 20.0D;
			}

			if(var14 < -20.0D) {
				var14 = -20.0D;
			}

			this.rotationYaw = (float)((double)this.rotationYaw + var14);
			this.setRotation(this.rotationYaw, this.rotationPitch);
			if (!this.worldObj.isRemote) {
				List var16 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
				int var27;

				if (var16 != null && !var16.isEmpty()) {
					for (var27 = 0; var27 < var16.size(); ++var27) {
						Entity var18 = (Entity)var16.get(var27);

						if (var18 != this.riddenByEntity && var18.canBePushed() && var18 instanceof EntityBoat) {
							var18.applyEntityCollision(this);
						}
					}
				}

				for (var27 = 0; var27 < 4; ++var27) {
					int var28 = MathHelper.floor_double(this.posX + ((double)(var27 % 2) - 0.5D) * 0.8D);
					int var19 = MathHelper.floor_double(this.posZ + ((double)(var27 / 2) - 0.5D) * 0.8D);

					for (int var20 = 0; var20 < 2; ++var20) {
						int var21 = MathHelper.floor_double(this.posY) + var20;
						int var22 = this.worldObj.getBlockId(var28, var21, var19);
						int var23 = this.worldObj.getBlockMetadata(var28, var21, var19);

						if (var22 == Block.snow.blockID) {
							this.worldObj.setBlockWithNotify(var28, var21, var19, 0);
						} else if (var22 == Block.waterlily.blockID) {
							Block.waterlily.dropBlockAsItemWithChance(this.worldObj, var28, var21, var19, var23, 0.3F, 0);
							this.worldObj.setBlockWithNotify(var28, var21, var19, 0);
						}
					}
				}

				if (this.riddenByEntity != null && this.riddenByEntity.isDead) {

					riddenByEntity.ridingEntity = null; // Spout
					this.riddenByEntity = null;
				}
			}
		}
	}

	public void updateRiderPosition() {
		if(this.riddenByEntity != null) {
			double var1 = Math.cos((double)this.rotationYaw * Math.PI / 180.0D) * 0.4D;
			double var3 = Math.sin((double)this.rotationYaw * Math.PI / 180.0D) * 0.4D;
			this.riddenByEntity.setPosition(this.posX + var1, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ + var3);
		}
	}

	protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {}

	protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {}

	public float getShadowSize() {
		return 0.0F;
	}

	public boolean interact(EntityPlayer par1EntityPlayer) {
		if(this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && this.riddenByEntity != par1EntityPlayer) {
			return true;
		} else {
			if(!this.worldObj.isRemote) {
				par1EntityPlayer.mountEntity(this);
			}

			return true;
		}
	}

	public void setDamageTaken(int par1) {
		this.dataWatcher.updateObject(19, Integer.valueOf(par1));
	}

	public int getDamageTaken() {
		return this.dataWatcher.getWatchableObjectInt(19);
	}

	public void setTimeSinceHit(int par1) {
		this.dataWatcher.updateObject(17, Integer.valueOf(par1));
	}

	public int getTimeSinceHit() {
		return this.dataWatcher.getWatchableObjectInt(17);
	}

	public void setForwardDirection(int par1) {
		this.dataWatcher.updateObject(18, Integer.valueOf(par1));
	}

	public int getForwardDirection() {
		return this.dataWatcher.getWatchableObjectInt(18);
	}

	public void func_70270_d(boolean par1) {
		this.field_70279_a = par1;
	}
}
