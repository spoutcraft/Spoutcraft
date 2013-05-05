package net.minecraft.src;

import java.util.List;

public class EntityBoat extends Entity {
	private boolean field_70279_a;
	private double speedMultiplier;
	private int boatPosRotationIncrements;
	private double boatX;
	private double boatY;
	private double boatZ;
	private double boatYaw;
	private double boatPitch;
	private double velocityX;
	private double velocityY;
	private double velocityZ;

	public EntityBoat(World par1World) {
		super(par1World);
		this.field_70279_a = true;
		this.speedMultiplier = 0.07D;
		this.preventEntitySpawning = true;
		this.setSize(1.5F, 0.6F);
		this.yOffset = this.height / 2.0F;
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
	 * prevent them from trampling crops
	 */
	protected boolean canTriggerWalking() {
		return false;
	}

	protected void entityInit() {
		this.dataWatcher.addObject(17, new Integer(0));
		this.dataWatcher.addObject(18, new Integer(1));
		this.dataWatcher.addObject(19, new Integer(0));
	}

	/**
	 * Returns a boundingBox used to collide the entity with other entities and blocks. This enables the entity to be
	 * pushable on contact, like boats or minecarts.
	 */
	public AxisAlignedBB getCollisionBox(Entity par1Entity) {
		return par1Entity.boundingBox;
	}

	/**
	 * returns the bounding box for this entity
	 */
	public AxisAlignedBB getBoundingBox() {
		return this.boundingBox;
	}

	/**
	 * Returns true if this entity should push and be pushed by other entities when colliding.
	 */
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

	/**
	 * Returns the Y offset from the entity's position for any entity riding this one.
	 */
	public double getMountedYOffset() {
		return (double)this.height * 0.0D - 0.30000001192092896D;
	}

	/**
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
		if (this.isEntityInvulnerable()) {
			return false;
		} else if (!this.worldObj.isRemote && !this.isDead) {
			this.setForwardDirection(-this.getForwardDirection());
			this.setTimeSinceHit(10);
			this.setDamageTaken(this.getDamageTaken() + par2 * 10);
			this.setBeenAttacked();
			boolean var3 = par1DamageSource.getEntity() instanceof EntityPlayer && ((EntityPlayer)par1DamageSource.getEntity()).capabilities.isCreativeMode;

			if (var3 || this.getDamageTaken() > 40) {
				if (this.riddenByEntity != null) {
					this.riddenByEntity.mountEntity(this);
				}

				if (!var3) {
					this.dropItemWithOffset(Item.boat.itemID, 1, 0.0F);
				}

				this.setDead();
			}

			return true;
		} else {
			return true;
		}
	}

	/**
	 * Setups the entity to do the hurt animation. Only used by packets in multiplayer.
	 */
	public void performHurtAnimation() {
		this.setForwardDirection(-this.getForwardDirection());
		this.setTimeSinceHit(10);
		this.setDamageTaken(this.getDamageTaken() * 11);
	}

	/**
	 * Returns true if other Entities should be prevented from moving through this Entity.
	 */
	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	/**
	 * Sets the position and rotation. Only difference from the other one is no bounding on the rotation. Args: posX, posY,
	 * posZ, yaw, pitch
	 */
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

	/**
	 * Sets the velocity to the args. Args: x, y, z
	 */
	public void setVelocity(double par1, double par3, double par5) {
		this.velocityX = this.motionX = par1;
		this.velocityY = this.motionY = par3;
		this.velocityZ = this.motionZ = par5;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		super.onUpdate();

		if (this.getTimeSinceHit() > 0) {
			this.setTimeSinceHit(this.getTimeSinceHit() - 1);
		}

		if (this.getDamageTaken() > 0) {
			this.setDamageTaken(this.getDamageTaken() - 1);
		}

		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		byte var1 = 5;
		double var2 = 0.0D;

		for (int var4 = 0; var4 < var1; ++var4) {
			double var5 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (double)(var4 + 0) / (double)var1 - 0.125D;
			double var7 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (double)(var4 + 1) / (double)var1 - 0.125D;
			AxisAlignedBB var9 = AxisAlignedBB.getAABBPool().getAABB(this.boundingBox.minX, var5, this.boundingBox.minZ, this.boundingBox.maxX, var7, this.boundingBox.maxZ);

			if (this.worldObj.isAABBInMaterial(var9, Material.water)) {
				var2 += 1.0D / (double)var1;
			}
		}

		double var23 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
		double var6;
		double var8;

		if (var23 > 0.26249999999999996D) {
			var6 = Math.cos((double)this.rotationYaw * Math.PI / 180.0D);
			var8 = Math.sin((double)this.rotationYaw * Math.PI / 180.0D);

			for (int var10 = 0; (double)var10 < 1.0D + var23 * 60.0D; ++var10) {
				double var11 = (double)(this.rand.nextFloat() * 2.0F - 1.0F);
				double var13 = (double)(this.rand.nextInt(2) * 2 - 1) * 0.7D;
				double var15;
				double var17;

				if (this.rand.nextBoolean()) {
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
		double var25;

		if (this.worldObj.isRemote && this.field_70279_a) {
			if (this.boatPosRotationIncrements > 0) {
				var6 = this.posX + (this.boatX - this.posX) / (double)this.boatPosRotationIncrements;
				var8 = this.posY + (this.boatY - this.posY) / (double)this.boatPosRotationIncrements;
				var25 = this.posZ + (this.boatZ - this.posZ) / (double)this.boatPosRotationIncrements;
				var12 = MathHelper.wrapAngleTo180_double(this.boatYaw - (double)this.rotationYaw);
				this.rotationYaw = (float)((double)this.rotationYaw + var12 / (double)this.boatPosRotationIncrements);
				this.rotationPitch = (float)((double)this.rotationPitch + (this.boatPitch - (double)this.rotationPitch) / (double)this.boatPosRotationIncrements);
				--this.boatPosRotationIncrements;
				this.setPosition(var6, var8, var25);
				this.setRotation(this.rotationYaw, this.rotationPitch);
			} else {
				var6 = this.posX + this.motionX;
				var8 = this.posY + this.motionY;
				var25 = this.posZ + this.motionZ;
				this.setPosition(var6, var8, var25);

				if (this.onGround) {
					this.motionX *= 0.5D;
					this.motionY *= 0.5D;
					this.motionZ *= 0.5D;
				}

				this.motionX *= 0.9900000095367432D;
				this.motionY *= 0.949999988079071D;
				this.motionZ *= 0.9900000095367432D;
			}
		} else {
			if (var2 < 1.0D) {
				var6 = var2 * 2.0D - 1.0D;
				this.motionY += 0.03999999910593033D * var6;
			} else {
				if (this.motionY < 0.0D) {
					this.motionY /= 2.0D;
				}

				this.motionY += 0.007000000216066837D;
			}

			if (this.riddenByEntity != null) {
				this.motionX += this.riddenByEntity.motionX * this.speedMultiplier;
				this.motionZ += this.riddenByEntity.motionZ * this.speedMultiplier;
			}

			var6 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

			if (var6 > 0.35D) {
				var8 = 0.35D / var6;
				this.motionX *= var8;
				this.motionZ *= var8;
				var6 = 0.35D;
			}

			if (var6 > var23 && this.speedMultiplier < 0.35D) {
				this.speedMultiplier += (0.35D - this.speedMultiplier) / 35.0D;

				if (this.speedMultiplier > 0.35D) {
					this.speedMultiplier = 0.35D;
				}
			} else {
				this.speedMultiplier -= (this.speedMultiplier - 0.07D) / 35.0D;

				if (this.speedMultiplier < 0.07D) {
					this.speedMultiplier = 0.07D;
				}
			}

			if (this.onGround) {
				this.motionX *= 0.5D;
				this.motionY *= 0.5D;
				this.motionZ *= 0.5D;
			}

			this.moveEntity(this.motionX, this.motionY, this.motionZ);

			if (this.isCollidedHorizontally && var23 > 0.2D) {
				if (!this.worldObj.isRemote && !this.isDead) {
					this.setDead();
					int var24;

					for (var24 = 0; var24 < 3; ++var24) {
						this.dropItemWithOffset(Block.planks.blockID, 1, 0.0F);
					}

					for (var24 = 0; var24 < 2; ++var24) {
						this.dropItemWithOffset(Item.stick.itemID, 1, 0.0F);
					}
				}
			} else {
				this.motionX *= 0.9900000095367432D;
				this.motionY *= 0.949999988079071D;
				this.motionZ *= 0.9900000095367432D;
			}

			this.rotationPitch = 0.0F;
			var8 = (double)this.rotationYaw;
			var25 = this.prevPosX - this.posX;
			var12 = this.prevPosZ - this.posZ;

			if (var25 * var25 + var12 * var12 > 0.001D) {
				var8 = (double)((float)(Math.atan2(var12, var25) * 180.0D / Math.PI));
			}

			double var14 = MathHelper.wrapAngleTo180_double(var8 - (double)this.rotationYaw);

			if (var14 > 20.0D) {
				var14 = 20.0D;
			}

			if (var14 < -20.0D) {
				var14 = -20.0D;
			}

			this.rotationYaw = (float)((double)this.rotationYaw + var14);
			this.setRotation(this.rotationYaw, this.rotationPitch);

			if (!this.worldObj.isRemote) {
				List var16 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
				int var26;

				if (var16 != null && !var16.isEmpty()) {
					for (var26 = 0; var26 < var16.size(); ++var26) {
						Entity var18 = (Entity)var16.get(var26);

						if (var18 != this.riddenByEntity && var18.canBePushed() && var18 instanceof EntityBoat) {
							var18.applyEntityCollision(this);
						}
					}
				}

				for (var26 = 0; var26 < 4; ++var26) {
					int var27 = MathHelper.floor_double(this.posX + ((double)(var26 % 2) - 0.5D) * 0.8D);
					int var19 = MathHelper.floor_double(this.posZ + ((double)(var26 / 2) - 0.5D) * 0.8D);

					for (int var20 = 0; var20 < 2; ++var20) {
						int var21 = MathHelper.floor_double(this.posY) + var20;
						int var22 = this.worldObj.getBlockId(var27, var21, var19);

						if (var22 == Block.snow.blockID) {
							this.worldObj.setBlockToAir(var27, var21, var19);
						} else if (var22 == Block.waterlily.blockID) {
							this.worldObj.destroyBlock(var27, var21, var19, true);
						}
					}
				}

				if (this.riddenByEntity != null && this.riddenByEntity.isDead) {
					this.riddenByEntity = null;
				}
			}
		}
	}

	public void updateRiderPosition() {
		if (this.riddenByEntity != null) {
			double var1 = Math.cos((double)this.rotationYaw * Math.PI / 180.0D) * 0.4D;
			double var3 = Math.sin((double)this.rotationYaw * Math.PI / 180.0D) * 0.4D;
			this.riddenByEntity.setPosition(this.posX + var1, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ + var3);
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {}

	public float getShadowSize() {
		return 0.0F;
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
	 */
	public boolean interact(EntityPlayer par1EntityPlayer) {
		if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && this.riddenByEntity != par1EntityPlayer) {
			return true;
		} else {
			if (!this.worldObj.isRemote) {
				par1EntityPlayer.mountEntity(this);
			}

			return true;
		}
	}

	/**
	 * Sets the damage taken from the last hit.
	 */
	public void setDamageTaken(int par1) {
		this.dataWatcher.updateObject(19, Integer.valueOf(par1));
	}

	/**
	 * Gets the damage taken from the last hit.
	 */
	public int getDamageTaken() {
		return this.dataWatcher.getWatchableObjectInt(19);
	}

	/**
	 * Sets the time to count down from since the last time entity was hit.
	 */
	public void setTimeSinceHit(int par1) {
		this.dataWatcher.updateObject(17, Integer.valueOf(par1));
	}

	/**
	 * Gets the time since the last hit.
	 */
	public int getTimeSinceHit() {
		return this.dataWatcher.getWatchableObjectInt(17);
	}

	/**
	 * Sets the forward direction of the entity.
	 */
	public void setForwardDirection(int par1) {
		this.dataWatcher.updateObject(18, Integer.valueOf(par1));
	}

	/**
	 * Gets the forward direction of the entity.
	 */
	public int getForwardDirection() {
		return this.dataWatcher.getWatchableObjectInt(18);
	}

	public void func_70270_d(boolean par1) {
		this.field_70279_a = par1;
	}
}
