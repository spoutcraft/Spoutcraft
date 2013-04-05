package net.minecraft.src;

import java.util.List;

public abstract class EntityFireball extends Entity {
	private int xTile = -1;
	private int yTile = -1;
	private int zTile = -1;
	private int inTile = 0;
	private boolean inGround = false;
	public EntityLiving shootingEntity;
	private int ticksAlive;
	private int ticksInAir = 0;
	public double accelerationX;
	public double accelerationY;
	public double accelerationZ;
	// Spout Start
	public float yield = 1F;
	public boolean incendiary = true;
	// Spout End

	public EntityFireball(World par1World) {
		super(par1World);
		this.setSize(1.0F, 1.0F);
	}

	protected void entityInit() {}

	/**
	 * Checks if the entity is in range to render by using the past in distance and comparing it to its average edge length
	 * * 64 * renderDistanceWeight Args: distance
	 */
	public boolean isInRangeToRenderDist(double par1) {
		double var3 = this.boundingBox.getAverageEdgeLength() * 4.0D;
		var3 *= 64.0D;
		return par1 < var3 * var3;
	}

	public EntityFireball(World par1World, double par2, double par4, double par6, double par8, double par10, double par12) {
		super(par1World);
		this.setSize(1.0F, 1.0F);
		this.setLocationAndAngles(par2, par4, par6, this.rotationYaw, this.rotationPitch);
		this.setPosition(par2, par4, par6);
		double var14 = (double)MathHelper.sqrt_double(par8 * par8 + par10 * par10 + par12 * par12);
		this.accelerationX = par8 / var14 * 0.1D;
		this.accelerationY = par10 / var14 * 0.1D;
		this.accelerationZ = par12 / var14 * 0.1D;
	}

	public EntityFireball(World par1World, EntityLiving par2EntityLiving, double par3, double par5, double par7) {
		super(par1World);
		this.shootingEntity = par2EntityLiving;
		this.setSize(1.0F, 1.0F);
		this.setLocationAndAngles(par2EntityLiving.posX, par2EntityLiving.posY, par2EntityLiving.posZ, par2EntityLiving.rotationYaw, par2EntityLiving.rotationPitch);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.yOffset = 0.0F;
		this.motionX = this.motionY = this.motionZ = 0.0D;
		par3 += this.rand.nextGaussian() * 0.4D;
		par5 += this.rand.nextGaussian() * 0.4D;
		par7 += this.rand.nextGaussian() * 0.4D;
		double var9 = (double)MathHelper.sqrt_double(par3 * par3 + par5 * par5 + par7 * par7);
		this.accelerationX = par3 / var9 * 0.1D;
		this.accelerationY = par5 / var9 * 0.1D;
		this.accelerationZ = par7 / var9 * 0.1D;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		if (!this.worldObj.isRemote && (this.shootingEntity != null && this.shootingEntity.isDead || !this.worldObj.blockExists((int)this.posX, (int)this.posY, (int)this.posZ))) {
			this.setDead();
		} else {
			super.onUpdate();
			this.setFire(1);

			if (this.inGround) {
				int var1 = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);

				if (var1 == this.inTile) {
					++this.ticksAlive;

					if (this.ticksAlive == 600) {
						this.setDead();
					}

					return;
				}

				this.inGround = false;
				this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
				this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
				this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
				this.ticksAlive = 0;
				this.ticksInAir = 0;
			} else {
				++this.ticksInAir;
			}

			Vec3 var19 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
			Vec3 var2 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			MovingObjectPosition var3 = this.worldObj.rayTraceBlocks(var19, var2);
			var19 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
			var2 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

			if (var3 != null) {
				var2 = this.worldObj.getWorldVec3Pool().getVecFromPool(var3.hitVec.xCoord, var3.hitVec.yCoord, var3.hitVec.zCoord);
			}

			Entity var4 = null;
			List var5 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
			double var6 = 0.0D;

			for (int var8 = 0; var8 < var5.size(); ++var8) {
				Entity var9 = (Entity)var5.get(var8);

				if (var9.canBeCollidedWith() && (!var9.isEntityEqual(this.shootingEntity) || this.ticksInAir >= 25)) {
					float var10 = 0.3F;
					AxisAlignedBB var11 = var9.boundingBox.expand((double)var10, (double)var10, (double)var10);
					MovingObjectPosition var12 = var11.calculateIntercept(var19, var2);

					if (var12 != null) {
						double var13 = var19.distanceTo(var12.hitVec);

						if (var13 < var6 || var6 == 0.0D) {
							var4 = var9;
							var6 = var13;
						}
					}
				}
			}

			if (var4 != null) {
				var3 = new MovingObjectPosition(var4);
			}

			if (var3 != null) {
				this.onImpact(var3);
			}

			this.posX += this.motionX;
			this.posY += this.motionY;
			this.posZ += this.motionZ;
			float var17 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.rotationYaw = (float)(Math.atan2(this.motionZ, this.motionX) * 180.0D / Math.PI) + 90.0F;

			for (this.rotationPitch = (float)(Math.atan2((double)var17, this.motionY) * 180.0D / Math.PI) - 90.0F; this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
				;
			}

			while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
				this.prevRotationPitch += 360.0F;
			}

			while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
				this.prevRotationYaw -= 360.0F;
			}

			while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
				this.prevRotationYaw += 360.0F;
			}

			this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
			this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
			float var18 = this.getMotionFactor();

			if (this.isInWater()) {
				for (int var16 = 0; var16 < 4; ++var16) {
					float var15 = 0.25F;
					this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double)var15, this.posY - this.motionY * (double)var15, this.posZ - this.motionZ * (double)var15, this.motionX, this.motionY, this.motionZ);
				}

				var18 = 0.8F;
			}

			this.motionX += this.accelerationX;
			this.motionY += this.accelerationY;
			this.motionZ += this.accelerationZ;
			this.motionX *= (double)var18;
			this.motionY *= (double)var18;
			this.motionZ *= (double)var18;
			this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
			this.setPosition(this.posX, this.posY, this.posZ);
		}
	}

	/**
	 * Return the motion factor for this projectile. The factor is multiplied by the original motion.
	 */
	protected float getMotionFactor() {
		return 0.95F;
	}

	/**
	 * Called when this EntityFireball hits a block or entity.
	 */
	protected abstract void onImpact(MovingObjectPosition var1);

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		par1NBTTagCompound.setShort("xTile", (short)this.xTile);
		par1NBTTagCompound.setShort("yTile", (short)this.yTile);
		par1NBTTagCompound.setShort("zTile", (short)this.zTile);
		par1NBTTagCompound.setByte("inTile", (byte)this.inTile);
		par1NBTTagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
		par1NBTTagCompound.setTag("direction", this.newDoubleNBTList(new double[] {this.motionX, this.motionY, this.motionZ}));
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		this.xTile = par1NBTTagCompound.getShort("xTile");
		this.yTile = par1NBTTagCompound.getShort("yTile");
		this.zTile = par1NBTTagCompound.getShort("zTile");
		this.inTile = par1NBTTagCompound.getByte("inTile") & 255;
		this.inGround = par1NBTTagCompound.getByte("inGround") == 1;

		if (par1NBTTagCompound.hasKey("direction")) {
			NBTTagList var2 = par1NBTTagCompound.getTagList("direction");
			this.motionX = ((NBTTagDouble)var2.tagAt(0)).data;
			this.motionY = ((NBTTagDouble)var2.tagAt(1)).data;
			this.motionZ = ((NBTTagDouble)var2.tagAt(2)).data;
		} else {
			this.setDead();
		}
	}

	/**
	 * Returns true if other Entities should be prevented from moving through this Entity.
	 */
	public boolean canBeCollidedWith() {
		return true;
	}

	public float getCollisionBorderSize() {
		return 1.0F;
	}

	/**
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
		if (this.isEntityInvulnerable()) {
			return false;
		} else {
			this.setBeenAttacked();

			if (par1DamageSource.getEntity() != null) {
				Vec3 var3 = par1DamageSource.getEntity().getLookVec();

				if (var3 != null) {
					this.motionX = var3.xCoord;
					this.motionY = var3.yCoord;
					this.motionZ = var3.zCoord;
					this.accelerationX = this.motionX * 0.1D;
					this.accelerationY = this.motionY * 0.1D;
					this.accelerationZ = this.motionZ * 0.1D;
				}

				if (par1DamageSource.getEntity() instanceof EntityLiving) {
					this.shootingEntity = (EntityLiving)par1DamageSource.getEntity();
				}

				return true;
			} else {
				return false;
			}
		}
	}

	public float getShadowSize() {
		return 0.0F;
	}

	/**
	 * Gets how bright this entity is.
	 */
	public float getBrightness(float par1) {
		return 1.0F;
	}

	public int getBrightnessForRender(float par1) {
		return 15728880;
	}
}
