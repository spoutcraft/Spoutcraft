package net.minecraft.src;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID; // Spout

import org.spoutcraft.client.SpoutClient; // Spout

public abstract class Entity {

	private static int nextEntityID = 0;
	//Spout start
	public static List<Entity> toProcess = new LinkedList<Entity>();
	//Spout end
	public int entityId;
	public double renderDistanceWeight;
	public boolean preventEntitySpawning;
	public Entity riddenByEntity;
	public Entity ridingEntity;
	public World worldObj;
	public double prevPosX;
	public double prevPosY;
	public double prevPosZ;
	public double posX;
	public double posY;
	public double posZ;
	public double motionX;
	public double motionY;
	public double motionZ;
	public float rotationYaw;
	public float rotationPitch;
	public float prevRotationYaw;
	public float prevRotationPitch;
	public final AxisAlignedBB boundingBox;
	public boolean onGround;
	public boolean isCollidedHorizontally;
	public boolean isCollidedVertically;
	public boolean isCollided;
	public boolean velocityChanged;
	protected boolean isInWeb;
	public boolean field_70135_K;
	public boolean isDead;
	public float yOffset;
	public float width;
	public float height;
	public float prevDistanceWalkedModified;
	public float distanceWalkedModified;
	public float fallDistance;
	private int nextStepDistance;
	public double lastTickPosX;
	public double lastTickPosY;
	public double lastTickPosZ;
	public float ySize;
	public float stepHeight;
	public boolean noClip;
	public float entityCollisionReduction;
	protected Random rand;
	public int ticksExisted;
	public int fireResistance;
	public int fire; // Spout private -> public
	protected boolean inWater;
	public int hurtResistantTime;
	private boolean firstUpdate;
	public String skinUrl;
	public String cloakUrl;
	protected boolean isImmuneToFire;
	protected DataWatcher dataWatcher;
	private double entityRiderPitchDelta;
	private double entityRiderYawDelta;
	public boolean addedToChunk;
	public int chunkCoordX;
	public int chunkCoordY;
	public int chunkCoordZ;
	public int serverPosX;
	public int serverPosY;
	public int serverPosZ;
	public boolean ignoreFrustumCheck;
	public boolean isAirBorne;
	//Spout start
	public boolean partiallyInWater = false;
	public org.spoutcraft.spoutcraftapi.entity.Entity spoutEntity;
	public UUID uniqueId = UUID.randomUUID();
	public boolean wasOnGround;
	public boolean clientonly = false;
	//Spout end
	public EnumEntitySize myEntitySize;

	public Entity(World par1World) {
		this.entityId = nextEntityID++;
		this.renderDistanceWeight = 1.0D;
		this.preventEntitySpawning = false;
		this.boundingBox = AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		this.onGround = false;
		this.isCollided = false;
		this.velocityChanged = false;
		this.field_70135_K = true;
		this.isDead = false;
		this.yOffset = 0.0F;
		this.width = 0.6F;
		this.height = 1.8F;
		this.prevDistanceWalkedModified = 0.0F;
		this.distanceWalkedModified = 0.0F;
		this.fallDistance = 0.0F;
		this.nextStepDistance = 1;
		this.ySize = 0.0F;
		this.stepHeight = 0.0F;
		this.noClip = false;
		this.entityCollisionReduction = 0.0F;
		this.rand = new Random();
		this.ticksExisted = 0;
		this.fireResistance = 1;
		this.fire = 0;
		this.inWater = false;
		this.hurtResistantTime = 0;
		this.firstUpdate = true;
		this.isImmuneToFire = false;
		this.dataWatcher = new DataWatcher();
		this.addedToChunk = false;
		this.myEntitySize = EnumEntitySize.SIZE_2;
		this.worldObj = par1World;
		this.setPosition(0.0D, 0.0D, 0.0D);
		this.dataWatcher.addObject(0, Byte.valueOf((byte)0));
		this.dataWatcher.addObject(1, Short.valueOf((short)300));
		this.entityInit();
	}

	protected abstract void entityInit();

	public DataWatcher getDataWatcher() {
		return this.dataWatcher;
	}

	public boolean equals(Object par1Obj) {
		return par1Obj instanceof Entity ? ((Entity)par1Obj).entityId == this.entityId : false;
	}

	public int hashCode() {
		return this.entityId;
	}

	protected void preparePlayerToSpawn() {
		if(this.worldObj != null) {
			while(this.posY > 0.0D) {
				this.setPosition(this.posX, this.posY, this.posZ);
				if(this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty()) {
					break;
				}

				++this.posY;
			}

			this.motionX = this.motionY = this.motionZ = 0.0D;
			this.rotationPitch = 0.0F;
		}
	}

	public void setDead() {
		this.isDead = true;
	}

	protected void setSize(float par1, float par2) {
		this.width = par1;
		this.height = par2;
		float var3 = par1 % 2.0F;

		if ((double)var3 < 0.375D) {
			this.myEntitySize = EnumEntitySize.SIZE_1;
		} else if ((double)var3 < 0.75D) {
			this.myEntitySize = EnumEntitySize.SIZE_2;
		} else if ((double)var3 < 1.0D) {
			this.myEntitySize = EnumEntitySize.SIZE_3;
		} else if ((double)var3 < 1.375D) {
			this.myEntitySize = EnumEntitySize.SIZE_4;
		} else if ((double)var3 < 1.75D) {
			this.myEntitySize = EnumEntitySize.SIZE_5;
		} else {
			this.myEntitySize = EnumEntitySize.SIZE_6;
		}
	}

	protected void setRotation(float par1, float par2) {
		this.rotationYaw = par1 % 360.0F;
		this.rotationPitch = par2 % 360.0F;
	}

	public void setPosition(double par1, double par3, double par5) {
		this.posX = par1;
		this.posY = par3;
		this.posZ = par5;
		float var7 = this.width / 2.0F;
		float var8 = this.height;
		this.boundingBox.setBounds(par1 - (double)var7, par3 - (double)this.yOffset + (double)this.ySize, par5 - (double)var7, par1 + (double)var7, par3 - (double)this.yOffset + (double)this.ySize + (double)var8, par5 + (double)var7);
	}

	public void setAngles(float par1, float par2) {
		float var3 = this.rotationPitch;
		float var4 = this.rotationYaw;
		this.rotationYaw = (float)((double)this.rotationYaw + (double)par1 * 0.15D);
		this.rotationPitch = (float)((double)this.rotationPitch - (double)par2 * 0.15D);
		if(this.rotationPitch < -90.0F) {
			this.rotationPitch = -90.0F;
		}

		if(this.rotationPitch > 90.0F) {
			this.rotationPitch = 90.0F;
		}

		this.prevRotationPitch += this.rotationPitch - var3;
		this.prevRotationYaw += this.rotationYaw - var4;
	}

	public void onUpdate() {
		this.onEntityUpdate();
	}

	public void onEntityUpdate() {
		this.worldObj.theProfiler.startSection("entityBaseTick");
		if(this.ridingEntity != null && this.ridingEntity.isDead) {
			this.ridingEntity = null;
		}
		//Spout start
		partiallyInWater = isInsideOfMaterial(Material.water, -1);
		//Spout end

		++this.ticksExisted;
		this.prevDistanceWalkedModified = this.distanceWalkedModified;
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.prevRotationPitch = this.rotationPitch;
		this.prevRotationYaw = this.rotationYaw;
		int var3;
		if(this.isSprinting() && !this.isInWater()) {
			int var1 = MathHelper.floor_double(this.posX);
			int var2 = MathHelper.floor_double(this.posY - 0.20000000298023224D - (double)this.yOffset);
			var3 = MathHelper.floor_double(this.posZ);
			int var4 = this.worldObj.getBlockId(var1, var2, var3);
			if(var4 > 0) {
				this.worldObj.spawnParticle("tilecrack_" + var4, this.posX + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, this.boundingBox.minY + 0.1D, this.posZ + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, -this.motionX * 4.0D, 1.5D, -this.motionZ * 4.0D);
			}
		}

		if(this.handleWaterMovement()) {
			if(!this.inWater && !this.firstUpdate) {
				float var6 = MathHelper.sqrt_double(this.motionX * this.motionX * 0.20000000298023224D + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.20000000298023224D) * 0.2F;
				if(var6 > 1.0F) {
					var6 = 1.0F;
				}

				this.worldObj.playSoundAtEntity(this, "random.splash", var6, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
				float var7 = (float)MathHelper.floor_double(this.boundingBox.minY);

				float var5;
				float var8;
				for(var3 = 0; (float)var3 < 1.0F + this.width * 20.0F; ++var3) {
					var8 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					var5 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					this.worldObj.spawnParticle("bubble", this.posX + (double)var8, (double)(var7 + 1.0F), this.posZ + (double)var5, this.motionX, this.motionY - (double)(this.rand.nextFloat() * 0.2F), this.motionZ);
				}

				for(var3 = 0; (float)var3 < 1.0F + this.width * 20.0F; ++var3) {
					var8 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					var5 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
					this.worldObj.spawnParticle("splash", this.posX + (double)var8, (double)(var7 + 1.0F), this.posZ + (double)var5, this.motionX, this.motionY, this.motionZ);
				}
			}

			this.fallDistance = 0.0F;
			this.inWater = true;
			this.fire = 0;
		} else {
			this.inWater = false;
		}

		if(this.worldObj.isRemote) {
			this.fire = 0;
		} else if(this.fire > 0) {
			if(this.isImmuneToFire) {
				this.fire -= 4;
				if(this.fire < 0) {
					this.fire = 0;
				}
			} else {
				if(this.fire % 20 == 0) {
					this.attackEntityFrom(DamageSource.onFire, 1);
				}

				--this.fire;
			}
		}

		if(this.handleLavaMovement()) {
			this.setOnFireFromLava();
			this.fallDistance *= 0.5F;
		}

		if(this.posY < -64.0D) {
			this.kill();
		}

		if(!this.worldObj.isRemote) {
			this.setFlag(0, this.fire > 0);
			this.setFlag(2, this.ridingEntity != null);
		}

		this.firstUpdate = false;
		this.worldObj.theProfiler.endSection();
	}

	protected void setOnFireFromLava() {
		if(!this.isImmuneToFire) {
			this.attackEntityFrom(DamageSource.lava, 4);
			this.setFire(15);
		}
	}

	public void setFire(int par1) {
		int var2 = par1 * 20;
		if(this.fire < var2) {
			this.fire = var2;
		}
	}

	public void extinguish() {
		this.fire = 0;
	}

	protected void kill() {
		this.setDead();
	}

	public boolean isOffsetPositionInLiquid(double par1, double par3, double par5) {
		AxisAlignedBB var7 = this.boundingBox.getOffsetBoundingBox(par1, par3, par5);
		List var8 = this.worldObj.getCollidingBoundingBoxes(this, var7);
		return var8.size() > 0 ? false : !this.worldObj.isAnyLiquid(var7);
	}

	public void moveEntity(double par1, double par3, double par5) {
		if(this.noClip) {
			this.boundingBox.offset(par1, par3, par5);
			this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D;
			this.posY = this.boundingBox.minY + (double)this.yOffset - (double)this.ySize;
			this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D;
		} else {
			this.worldObj.theProfiler.startSection("move");
			this.ySize *= 0.4F;
			double var7 = this.posX;
			double var9 = this.posZ;
			if(this.isInWeb) {
				this.isInWeb = false;
				par1 *= 0.25D;
				par3 *= 0.05000000074505806D;
				par5 *= 0.25D;
				this.motionX = 0.0D;
				this.motionY = 0.0D;
				this.motionZ = 0.0D;
			}

			double var11 = par1;
			double var13 = par3;
			double var15 = par5;
			AxisAlignedBB var17 = this.boundingBox.copy();
			boolean var18 = this.onGround && this.isSneaking() && this instanceof EntityPlayer;
			if(var18) {
				double var19;
				for(var19 = 0.05D; par1 != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.getOffsetBoundingBox(par1, -1.0D, 0.0D)).isEmpty(); var11 = par1) {
					if(par1 < var19 && par1 >= -var19) {
						par1 = 0.0D;
					} else if(par1 > 0.0D) {
						par1 -= var19;
					} else {
						par1 += var19;
					}
				}

				for(; par5 != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.getOffsetBoundingBox(0.0D, -1.0D, par5)).isEmpty(); var15 = par5) {
					if(par5 < var19 && par5 >= -var19) {
						par5 = 0.0D;
					} else if(par5 > 0.0D) {
						par5 -= var19;
					} else {
						par5 += var19;
					}
				}

				while (par1 != 0.0D && par5 != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.getOffsetBoundingBox(par1, -1.0D, par5)).isEmpty()) {
					if (par1 < var19 && par1 >= -var19) {
						par1 = 0.0D;
					} else if (par1 > 0.0D) {
						par1 -= var19;
					} else {
						par1 += var19;
					}

					if (par5 < var19 && par5 >= -var19) {
						par5 = 0.0D;
					} else if (par5 > 0.0D) {
						par5 -= var19;
					} else {
						par5 += var19;
					}

					var11 = par1;
					var15 = par5;
				}
			}

			List var30 = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.addCoord(par1, par3, par5));
			AxisAlignedBB var21;

			for (Iterator var20 = var30.iterator(); var20.hasNext(); par3 = var21.calculateYOffset(this.boundingBox, par3)) {
				var21 = (AxisAlignedBB)var20.next();
			}


			this.boundingBox.offset(0.0D, par3, 0.0D);

			if(!this.field_70135_K && var13 != par3) {
				par5 = 0.0D;
				par3 = 0.0D;
				par1 = 0.0D;
			}

			boolean var31 = this.onGround || var13 != par3 && var13 < 0.0D;
			AxisAlignedBB var22;
			Iterator var32;

			for (var32 = var30.iterator(); var32.hasNext(); par1 = var22.calculateXOffset(this.boundingBox, par1)) {
				var22 = (AxisAlignedBB)var32.next();
			}

			this.boundingBox.offset(par1, 0.0D, 0.0D);

			if(!this.field_70135_K && var11 != par1) {
				par5 = 0.0D;
				par3 = 0.0D;
				par1 = 0.0D;
			}

			for (var32 = var30.iterator(); var32.hasNext(); par5 = var22.calculateZOffset(this.boundingBox, par5)) {
				var22 = (AxisAlignedBB)var32.next();
			}

			this.boundingBox.offset(0.0D, 0.0D, par5);

			if(!this.field_70135_K && var15 != par5) {
				par5 = 0.0D;
				par3 = 0.0D;
				par1 = 0.0D;
			}

			double var23;
			double var33;
			if(this.stepHeight > 0.0F && var31 && (var18 || this.ySize < 0.05F) && (var11 != par1 || var15 != par5)) {
				var33 = par1;
				var23 = par3;
				double var25 = par5;
				par1 = var11;
				par3 = (double)this.stepHeight;
				par5 = var15;
				AxisAlignedBB var27 = this.boundingBox.copy();
				this.boundingBox.setBB(var17);
				var30 = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.addCoord(var11, par3, var15));
				AxisAlignedBB var29;
				Iterator var28;

				for (var28 = var30.iterator(); var28.hasNext(); par3 = var29.calculateYOffset(this.boundingBox, par3)) {
					var29 = (AxisAlignedBB)var28.next();
				}

				this.boundingBox.offset(0.0D, par3, 0.0D);
				if(!this.field_70135_K && var13 != par3) {
					par5 = 0.0D;
					par3 = 0.0D;
					par1 = 0.0D;
				}

				for (var32 = var30.iterator(); var32.hasNext(); par1 = var22.calculateXOffset(this.boundingBox, par1)) {
					var22 = (AxisAlignedBB)var32.next();
				}

				this.boundingBox.offset(par1, 0.0D, 0.0D);

				if(!this.field_70135_K && var11 != par1) {
					par5 = 0.0D;
					par3 = 0.0D;
					par1 = 0.0D;
				}

				for (var32 = var30.iterator(); var32.hasNext(); par5 = var22.calculateZOffset(this.boundingBox, par5)) {
					var22 = (AxisAlignedBB)var32.next();
				}

				this.boundingBox.offset(0.0D, 0.0D, par5);

				if(!this.field_70135_K && var15 != par5) {
					par5 = 0.0D;
					par3 = 0.0D;
					par1 = 0.0D;
				}

				if(!this.field_70135_K && var13 != par3) {
					par5 = 0.0D;
					par3 = 0.0D;
					par1 = 0.0D;
				} else {
					par3 = (double)(-this.stepHeight);

					for (var28 = var30.iterator(); var28.hasNext(); par3 = var29.calculateYOffset(this.boundingBox, par3)) {
						var29 = (AxisAlignedBB)var28.next();
					}

					this.boundingBox.offset(0.0D, par3, 0.0D);
				}

				if(var33 * var33 + var25 * var25 >= par1 * par1 + par5 * par5) {
					par1 = var33;
					par3 = var23;
					par5 = var25;
					this.boundingBox.setBB(var27);
				} else {
					double var37 = this.boundingBox.minY - (double)((int)this.boundingBox.minY);
					if(var37 > 0.0D) {
						this.ySize = (float)((double)this.ySize + var37 + 0.01D);
					}
				}
			}

			this.worldObj.theProfiler.endSection();
			this.worldObj.theProfiler.startSection("rest");
			this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D;
			this.posY = this.boundingBox.minY + (double)this.yOffset - (double)this.ySize;
			this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D;
			this.isCollidedHorizontally = var11 != par1 || var15 != par5;
			this.isCollidedVertically = var13 != par3;
			this.onGround = var13 != par3 && var13 < 0.0D;
			this.isCollided = this.isCollidedHorizontally || this.isCollidedVertically;
			this.updateFallState(par3, this.onGround);
			if(var11 != par1) {
				this.motionX = 0.0D;
			}

			if(var13 != par3) {
				this.motionY = 0.0D;
			}

			if(var15 != par5) {
				this.motionZ = 0.0D;
			}

			var33 = this.posX - var7;
			var23 = this.posZ - var9;

			if(this.canTriggerWalking() && !var18 && this.ridingEntity == null) {
				this.distanceWalkedModified = (float)((double)this.distanceWalkedModified + (double)MathHelper.sqrt_double(var33 * var33 + var23 * var23) * 0.6D);
				int var34 = MathHelper.floor_double(this.posX);
				int var26 = MathHelper.floor_double(this.posY - 0.20000000298023224D - (double)this.yOffset);
				int var36 = MathHelper.floor_double(this.posZ);
				int var38 = this.worldObj.getBlockId(var34, var26, var36);
				if(var38 == 0 && this.worldObj.getBlockId(var34, var26 - 1, var38) == Block.fence.blockID) {
					var38 = this.worldObj.getBlockId(var34, var26 - 1, var38);
				}

				if(this.distanceWalkedModified > (float)this.nextStepDistance && var38 > 0) {
					this.nextStepDistance = (int)this.distanceWalkedModified + 1;
					this.playStepSound(var34, var26, var36, var38);
					Block.blocksList[var38].onEntityWalking(this.worldObj, var34, var26, var36, this);
				}
			}

			this.doBlockCollisions();
			boolean var35 = this.isWet();

			if (this.worldObj.isBoundingBoxBurning(this.boundingBox.contract(0.001D, 0.001D, 0.001D))) {
				this.dealFireDamage(1);
				if(!var35) {
					++this.fire;
					if(this.fire == 0) {
						this.setFire(8);
					}
				}
			} else if(this.fire <= 0) {
				this.fire = -this.fireResistance;
			}

			if(var35 && this.fire > 0) {
				this.worldObj.playSoundAtEntity(this, "random.fizz", 0.7F, 1.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
				this.fire = -this.fireResistance;
			}

			this.worldObj.theProfiler.endSection();
		}
	}

	protected void doBlockCollisions() {
		int var1 = MathHelper.floor_double(this.boundingBox.minX + 0.001D);
		int var2 = MathHelper.floor_double(this.boundingBox.minY + 0.001D);
		int var3 = MathHelper.floor_double(this.boundingBox.minZ + 0.001D);
		int var4 = MathHelper.floor_double(this.boundingBox.maxX - 0.001D);
		int var5 = MathHelper.floor_double(this.boundingBox.maxY - 0.001D);
		int var6 = MathHelper.floor_double(this.boundingBox.maxZ - 0.001D);

		if (this.worldObj.checkChunksExist(var1, var2, var3, var4, var5, var6)) {
			for (int var7 = var1; var7 <= var4; ++var7) {
				for (int var8 = var2; var8 <= var5; ++var8) {
					for (int var9 = var3; var9 <= var6; ++var9) {
						int var10 = this.worldObj.getBlockId(var7, var8, var9);

						if (var10 > 0) {
							Block.blocksList[var10].onEntityCollidedWithBlock(this.worldObj, var7, var8, var9, this);
						}
					}
				}
			}
		}
	}

	protected void playStepSound(int par1, int par2, int par3, int par4) {
		StepSound var5 = Block.blocksList[par4].stepSound;
		if(this.worldObj.getBlockId(par1, par2 + 1, par3) == Block.snow.blockID) {
			var5 = Block.snow.stepSound;
			this.worldObj.playSoundAtEntity(this, var5.getStepSound(), var5.getVolume() * 0.15F, var5.getPitch());
		} else if(!Block.blocksList[par4].blockMaterial.isLiquid()) {
			this.worldObj.playSoundAtEntity(this, var5.getStepSound(), var5.getVolume() * 0.15F, var5.getPitch());
		}
	}

	protected boolean canTriggerWalking() {
		return true;
	}

	protected void updateFallState(double par1, boolean par3) {
		if(par3) {
			if(this.fallDistance > 0.0F) {
				if(this instanceof EntityLiving) {
					int var4 = MathHelper.floor_double(this.posX);
					int var5 = MathHelper.floor_double(this.posY - 0.20000000298023224D - (double)this.yOffset);
					int var6 = MathHelper.floor_double(this.posZ);
					int var7 = this.worldObj.getBlockId(var4, var5, var6);
					if(var7 == 0 && this.worldObj.getBlockId(var4, var5 - 1, var6) == Block.fence.blockID) {
						var7 = this.worldObj.getBlockId(var4, var5 - 1, var6);
					}

					if(var7 > 0) {
						Block.blocksList[var7].onFallenUpon(this.worldObj, var4, var5, var6, this, this.fallDistance);
					}
				}

				this.fall(this.fallDistance);
				this.fallDistance = 0.0F;
			}
		} else if(par1 < 0.0D) {
			this.fallDistance = (float)((double)this.fallDistance - par1);
		}
	}

	public AxisAlignedBB getBoundingBox() {
		return null;
	}

	protected void dealFireDamage(int par1) {
		if(!this.isImmuneToFire) {
			this.attackEntityFrom(DamageSource.inFire, par1);
		}
	}

	public final boolean isImmuneToFire() {
		return this.isImmuneToFire;
	}

	protected void fall(float par1) {
		if(this.riddenByEntity != null) {
			this.riddenByEntity.fall(par1);
		}
	}

	public boolean isWet() {
		return this.inWater || this.worldObj.canLightningStrikeAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
	}

	public boolean isInWater() {
		return this.inWater;
	}

	public boolean handleWaterMovement() {
		return this.worldObj.handleMaterialAcceleration(this.boundingBox.expand(0.0D, -0.4000000059604645D, 0.0D).contract(0.001D, 0.001D, 0.001D), Material.water, this);
	}
	
	//Spout start
	public boolean isInsideOfMaterial(Material material) {
		return isInsideOfMaterial(material, 0);
	}

	public boolean isInsideOfMaterial(Material material, float offset) {
		double var2 = this.posY + (double)this.getEyeHeight() + offset;
		int var4 = MathHelper.floor_double(this.posX);
		int var5 = MathHelper.floor_float((float)MathHelper.floor_double(var2));
		int var6 = MathHelper.floor_double(this.posZ);
		int var7 = this.worldObj.getBlockId(var4, var5, var6);
		if(var7 != 0 && Block.blocksList[var7].blockMaterial == material) {
			float var8 = BlockFluid.getFluidHeightPercent(this.worldObj.getBlockMetadata(var4, var5, var6)) - 0.11111111F;
			float var9 = (float)(var5 + 1) - var8;
			return var2 < (double)var9;
		}
		return false;
	}
	//Spout end

	public float getEyeHeight() {
		return 0.0F;
	}

	public boolean handleLavaMovement() {
		return this.worldObj.isMaterialInBB(this.boundingBox.expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.lava);
	}

	public void moveFlying(float par1, float par2, float par3) {
		float var4 = par1 * par1 + par2 * par2;

		if (var4 >= 1.0E-4F) {
			var4 = MathHelper.sqrt_float(var4);
			if(var4 < 1.0F) {
				var4 = 1.0F;
			}

			var4 = par3 / var4;
			par1 *= var4;
			par2 *= var4;
			float var5 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F);
			float var6 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F);
			this.motionX += (double)(par1 * var6 - par2 * var5);
			this.motionZ += (double)(par2 * var6 + par1 * var5);
		}
	}

	public int getBrightnessForRender(float par1) {
		int var2 = MathHelper.floor_double(this.posX);
		int var3 = MathHelper.floor_double(this.posZ);
		if(this.worldObj.blockExists(var2, 0, var3)) {
			double var4 = (this.boundingBox.maxY - this.boundingBox.minY) * 0.66D;
			int var6 = MathHelper.floor_double(this.posY - (double)this.yOffset + var4);
			return this.worldObj.getLightBrightnessForSkyBlocks(var2, var6, var3, 0);
		} else {
			return 0;
		}
	}

	public float getBrightness(float par1) {
		int var2 = MathHelper.floor_double(this.posX);
		int var3 = MathHelper.floor_double(this.posZ);
		if(this.worldObj.blockExists(var2, 0, var3)) {
			double var4 = (this.boundingBox.maxY - this.boundingBox.minY) * 0.66D;
			int var6 = MathHelper.floor_double(this.posY - (double)this.yOffset + var4);
			return this.worldObj.getLightBrightness(var2, var6, var3);
		} else {
			return 0.0F;
		}
	}

	public void setWorld(World par1World) {
		this.worldObj = par1World;
	}

	public void setPositionAndRotation(double par1, double par3, double par5, float par7, float par8) {
		this.prevPosX = this.posX = par1;
		this.prevPosY = this.posY = par3;
		this.prevPosZ = this.posZ = par5;
		this.prevRotationYaw = this.rotationYaw = par7;
		this.prevRotationPitch = this.rotationPitch = par8;
		this.ySize = 0.0F;
		double var9 = (double)(this.prevRotationYaw - par7);
		if(var9 < -180.0D) {
			this.prevRotationYaw += 360.0F;
		}

		if(var9 >= 180.0D) {
			this.prevRotationYaw -= 360.0F;
		}

		this.setPosition(this.posX, this.posY, this.posZ);
		this.setRotation(par7, par8);
	}

	public void setLocationAndAngles(double par1, double par3, double par5, float par7, float par8) {
		this.lastTickPosX = this.prevPosX = this.posX = par1;
		this.lastTickPosY = this.prevPosY = this.posY = par3 + (double)this.yOffset;
		this.lastTickPosZ = this.prevPosZ = this.posZ = par5;
		this.rotationYaw = par7;
		this.rotationPitch = par8;
		this.setPosition(this.posX, this.posY, this.posZ);
	}

	public float getDistanceToEntity(Entity par1Entity) {
		float var2 = (float)(this.posX - par1Entity.posX);
		float var3 = (float)(this.posY - par1Entity.posY);
		float var4 = (float)(this.posZ - par1Entity.posZ);
		return MathHelper.sqrt_float(var2 * var2 + var3 * var3 + var4 * var4);
	}

	public double getDistanceSq(double par1, double par3, double par5) {
		double var7 = this.posX - par1;
		double var9 = this.posY - par3;
		double var11 = this.posZ - par5;
		return var7 * var7 + var9 * var9 + var11 * var11;
	}

	public double getDistance(double par1, double par3, double par5) {
		double var7 = this.posX - par1;
		double var9 = this.posY - par3;
		double var11 = this.posZ - par5;
		return (double)MathHelper.sqrt_double(var7 * var7 + var9 * var9 + var11 * var11);
	}

	public double getDistanceSqToEntity(Entity par1Entity) {
		double var2 = this.posX - par1Entity.posX;
		double var4 = this.posY - par1Entity.posY;
		double var6 = this.posZ - par1Entity.posZ;
		return var2 * var2 + var4 * var4 + var6 * var6;
	}

	public void onCollideWithPlayer(EntityPlayer par1EntityPlayer) {}

	public void applyEntityCollision(Entity par1Entity) {
		if(par1Entity.riddenByEntity != this && par1Entity.ridingEntity != this) {
			double var2 = par1Entity.posX - this.posX;
			double var4 = par1Entity.posZ - this.posZ;
			double var6 = MathHelper.abs_max(var2, var4);
			if(var6 >= 0.009999999776482582D) {
				var6 = (double)MathHelper.sqrt_double(var6);
				var2 /= var6;
				var4 /= var6;
				double var8 = 1.0D / var6;
				if(var8 > 1.0D) {
					var8 = 1.0D;
				}

				var2 *= var8;
				var4 *= var8;
				var2 *= 0.05000000074505806D;
				var4 *= 0.05000000074505806D;
				var2 *= (double)(1.0F - this.entityCollisionReduction);
				var4 *= (double)(1.0F - this.entityCollisionReduction);
				this.addVelocity(-var2, 0.0D, -var4);
				par1Entity.addVelocity(var2, 0.0D, var4);
			}
		}
	}

	public void addVelocity(double par1, double par3, double par5) {
		this.motionX += par1;
		this.motionY += par3;
		this.motionZ += par5;
		this.isAirBorne = true;
	}

	protected void setBeenAttacked() {
		this.velocityChanged = true;
	}

	public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
		this.setBeenAttacked();
		return false;
	}

	public boolean canBeCollidedWith() {
		return false;
	}

	public boolean canBePushed() {
		return false;
	}

	public void addToPlayerScore(Entity par1Entity, int par2) {}

	public boolean isInRangeToRenderVec3D(Vec3 par1Vec3D) {
		double var2 = this.posX - par1Vec3D.xCoord;
		double var4 = this.posY - par1Vec3D.yCoord;
		double var6 = this.posZ - par1Vec3D.zCoord;
		double var8 = var2 * var2 + var4 * var4 + var6 * var6;
		return this.isInRangeToRenderDist(var8);
	}

	public boolean isInRangeToRenderDist(double par1) {
		double var3 = this.boundingBox.getAverageEdgeLength();
		var3 *= 64.0D * this.renderDistanceWeight;
		return par1 < var3 * var3;
	}

	public String getTexture() {
		return null;
	}

	public boolean addEntityID(NBTTagCompound par1NBTTagCompound) {
		String var2 = this.getEntityString();
		if(!this.isDead && var2 != null) {
			par1NBTTagCompound.setString("id", var2);
			this.writeToNBT(par1NBTTagCompound);
			return true;
		} else {
			return false;
		}
	}

	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		par1NBTTagCompound.setTag("Pos", this.newDoubleNBTList(new double[]{this.posX, this.posY + (double)this.ySize, this.posZ}));
		par1NBTTagCompound.setTag("Motion", this.newDoubleNBTList(new double[]{this.motionX, this.motionY, this.motionZ}));
		par1NBTTagCompound.setTag("Rotation", this.newFloatNBTList(new float[]{this.rotationYaw, this.rotationPitch}));
		par1NBTTagCompound.setFloat("FallDistance", this.fallDistance);
		par1NBTTagCompound.setShort("Fire", (short)this.fire);
		par1NBTTagCompound.setShort("Air", (short)this.getAir());
		par1NBTTagCompound.setBoolean("OnGround", this.onGround);
		//Spout start
		par1NBTTagCompound.setLong("ID_LSB", uniqueId.getLeastSignificantBits());
		par1NBTTagCompound.setLong("ID_MSB", uniqueId.getMostSignificantBits());
		//Spout end
		this.writeEntityToNBT(par1NBTTagCompound);
	}

	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		NBTTagList var2 = par1NBTTagCompound.getTagList("Pos");
		NBTTagList var3 = par1NBTTagCompound.getTagList("Motion");
		NBTTagList var4 = par1NBTTagCompound.getTagList("Rotation");
		this.motionX = ((NBTTagDouble)var3.tagAt(0)).data;
		this.motionY = ((NBTTagDouble)var3.tagAt(1)).data;
		this.motionZ = ((NBTTagDouble)var3.tagAt(2)).data;
		if(Math.abs(this.motionX) > 10.0D) {
			this.motionX = 0.0D;
		}

		if(Math.abs(this.motionY) > 10.0D) {
			this.motionY = 0.0D;
		}

		if(Math.abs(this.motionZ) > 10.0D) {
			this.motionZ = 0.0D;
		}

		this.prevPosX = this.lastTickPosX = this.posX = ((NBTTagDouble)var2.tagAt(0)).data;
		this.prevPosY = this.lastTickPosY = this.posY = ((NBTTagDouble)var2.tagAt(1)).data;
		this.prevPosZ = this.lastTickPosZ = this.posZ = ((NBTTagDouble)var2.tagAt(2)).data;
		this.prevRotationYaw = this.rotationYaw = ((NBTTagFloat)var4.tagAt(0)).data;
		this.prevRotationPitch = this.rotationPitch = ((NBTTagFloat)var4.tagAt(1)).data;
		this.fallDistance = par1NBTTagCompound.getFloat("FallDistance");
		this.fire = par1NBTTagCompound.getShort("Fire");
		this.setAir(par1NBTTagCompound.getShort("Air"));
		this.onGround = par1NBTTagCompound.getBoolean("OnGround");
		this.setPosition(this.posX, this.posY, this.posZ);
		this.setRotation(this.rotationYaw, this.rotationPitch);
		//Spout Start
		long lsb = par1NBTTagCompound.getLong("ID_LSB");
		long msb = par1NBTTagCompound.getLong("ID_MSB");
		UUID id = new UUID(msb, lsb);
		if (!id.equals(new UUID(0, 0))) {
			uniqueId = id;
		}
		//Spout End
		this.readEntityFromNBT(par1NBTTagCompound);
	}

	protected final String getEntityString() {
		return EntityList.getEntityString(this);
	}

	protected abstract void readEntityFromNBT(NBTTagCompound var1);

	protected abstract void writeEntityToNBT(NBTTagCompound var1);

	protected NBTTagList newDoubleNBTList(double ... par1ArrayOfDouble) {
		NBTTagList var2 = new NBTTagList();
		double[] var3 = par1ArrayOfDouble;
		int var4 = par1ArrayOfDouble.length;

		for(int var5 = 0; var5 < var4; ++var5) {
			double var6 = var3[var5];
			var2.appendTag(new NBTTagDouble((String)null, var6));
		}

		return var2;
	}

	protected NBTTagList newFloatNBTList(float ... par1ArrayOfFloat) {
		NBTTagList var2 = new NBTTagList();
		float[] var3 = par1ArrayOfFloat;
		int var4 = par1ArrayOfFloat.length;

		for(int var5 = 0; var5 < var4; ++var5) {
			float var6 = var3[var5];
			var2.appendTag(new NBTTagFloat((String)null, var6));
		}

		return var2;
	}

	public float getShadowSize() {
		return this.height / 2.0F;
	}

	public EntityItem dropItem(int par1, int par2) {
		return this.dropItemWithOffset(par1, par2, 0.0F);
	}

	public EntityItem dropItemWithOffset(int par1, int par2, float par3) {
		return this.entityDropItem(new ItemStack(par1, par2, 0), par3);
	}

	public EntityItem entityDropItem(ItemStack par1ItemStack, float par2) {
		EntityItem var3 = new EntityItem(this.worldObj, this.posX, this.posY + (double)par2, this.posZ, par1ItemStack);
		var3.delayBeforeCanPickup = 10;
		this.worldObj.spawnEntityInWorld(var3);
		return var3;
	}

	public boolean isEntityAlive() {
		return !this.isDead;
	}

	public boolean isEntityInsideOpaqueBlock() {
		for(int var1 = 0; var1 < 8; ++var1) {
			float var2 = ((float)((var1 >> 0) % 2) - 0.5F) * this.width * 0.8F;
			float var3 = ((float)((var1 >> 1) % 2) - 0.5F) * 0.1F;
			float var4 = ((float)((var1 >> 2) % 2) - 0.5F) * this.width * 0.8F;
			int var5 = MathHelper.floor_double(this.posX + (double)var2);
			int var6 = MathHelper.floor_double(this.posY + (double)this.getEyeHeight() + (double)var3);
			int var7 = MathHelper.floor_double(this.posZ + (double)var4);
			if(this.worldObj.isBlockNormalCube(var5, var6, var7)) {
				return true;
			}
		}

		return false;
	}

	public boolean interact(EntityPlayer par1EntityPlayer) {
		return false;
	}

	public AxisAlignedBB getCollisionBox(Entity par1Entity) {
		return null;
	}

	public void updateRidden() {
		if(this.ridingEntity.isDead) {
			this.ridingEntity = null;
		} else {
			this.motionX = 0.0D;
			this.motionY = 0.0D;
			this.motionZ = 0.0D;
			this.onUpdate();
			if(this.ridingEntity != null) {
				this.ridingEntity.updateRiderPosition();
				this.entityRiderYawDelta += (double)(this.ridingEntity.rotationYaw - this.ridingEntity.prevRotationYaw);

				for(this.entityRiderPitchDelta += (double)(this.ridingEntity.rotationPitch - this.ridingEntity.prevRotationPitch); this.entityRiderYawDelta >= 180.0D; this.entityRiderYawDelta -= 360.0D) {
					;
				}

				while(this.entityRiderYawDelta < -180.0D) {
					this.entityRiderYawDelta += 360.0D;
				}

				while(this.entityRiderPitchDelta >= 180.0D) {
					this.entityRiderPitchDelta -= 360.0D;
				}

				while(this.entityRiderPitchDelta < -180.0D) {
					this.entityRiderPitchDelta += 360.0D;
				}

				double var1 = this.entityRiderYawDelta * 0.5D;
				double var3 = this.entityRiderPitchDelta * 0.5D;
				float var5 = 10.0F;
				if(var1 > (double)var5) {
					var1 = (double)var5;
				}

				if(var1 < (double)(-var5)) {
					var1 = (double)(-var5);
				}

				if(var3 > (double)var5) {
					var3 = (double)var5;
				}

				if(var3 < (double)(-var5)) {
					var3 = (double)(-var5);
				}

				this.entityRiderYawDelta -= var1;
				this.entityRiderPitchDelta -= var3;
				this.rotationYaw = (float)((double)this.rotationYaw + var1);
				this.rotationPitch = (float)((double)this.rotationPitch + var3);
			}
		}
	}

	public void updateRiderPosition() {
		if (!(this.riddenByEntity instanceof EntityPlayer) || !((EntityPlayer)this.riddenByEntity).func_71066_bF()) {
			this.riddenByEntity.lastTickPosX = this.riddenByEntity.posX;
			this.riddenByEntity.lastTickPosY = this.riddenByEntity.posY;
			this.riddenByEntity.lastTickPosZ = this.riddenByEntity.posZ;
		}

		this.riddenByEntity.setPosition(this.posX, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ);
	}

	public double getYOffset() {
		return (double)this.yOffset;
	}

	public double getMountedYOffset() {
		return (double)this.height * 0.75D;
	}

	public void mountEntity(Entity par1Entity) {
		this.entityRiderPitchDelta = 0.0D;
		this.entityRiderYawDelta = 0.0D;
		if (par1Entity == null) {
			if(this.ridingEntity != null) {
				this.ridingEntity.riddenByEntity = null;
			}

			this.ridingEntity = null;
		} else if (this.ridingEntity == par1Entity) {
			this.unmountEntity(par1Entity);
			this.ridingEntity.riddenByEntity = null;
			this.ridingEntity = null;
			this.setLocationAndAngles(par1Entity.posX, par1Entity.boundingBox.minY + (double)par1Entity.height, par1Entity.posZ, this.rotationYaw, this.rotationPitch);
		} else {
			if(this.ridingEntity != null) {
				this.ridingEntity.riddenByEntity = null;
			}

			if (par1Entity.riddenByEntity != null) {
				par1Entity.riddenByEntity.ridingEntity = null;
			}

			this.ridingEntity = par1Entity;
			par1Entity.riddenByEntity = this;
		}
	}

	public void unmountEntity(Entity par1Entity) {
		double var3 = par1Entity.posX;
		double var5 = par1Entity.boundingBox.minY + (double)par1Entity.height;
		double var7 = par1Entity.posZ;

		for (double var9 = -1.5D; var9 < 2.0D; ++var9) {
			for (double var11 = -1.5D; var11 < 2.0D; ++var11) {
				if (var9 != 0.0D || var11 != 0.0D) {
					int var13 = (int)(this.posX + var9);
					int var14 = (int)(this.posZ + var11);
					AxisAlignedBB var2 = this.boundingBox.getOffsetBoundingBox(var9, 1.0D, var11);

					if (this.worldObj.getAllCollidingBoundingBoxes(var2).isEmpty()) {
						if (this.worldObj.doesBlockHaveSolidTopSurface(var13, (int)this.posY, var14)) {
							this.setLocationAndAngles(this.posX + var9, this.posY + 1.0D, this.posZ + var11, this.rotationYaw, this.rotationPitch);
							return;
						}

						if (this.worldObj.func_72797_t(var13, (int)this.posY - 1, var14) || this.worldObj.getBlockMaterial(var13, (int)this.posY - 1, var14) == Material.water) {
							var3 = this.posX + var9;
							var5 = this.posY + 1.0D;
							var7 = this.posZ + var11;
						}
					}
				}
			}
		}

		this.setLocationAndAngles(var3, var5, var7, this.rotationYaw, this.rotationPitch);
	}

	public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
		this.setPosition(par1, par3, par5);
		this.setRotation(par7, par8);
		List var10 = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.contract(0.03125D, 0.0D, 0.03125D));
		if(!var10.isEmpty()) {
			double var11 = 0.0D;
			Iterator var13 = var10.iterator();

			while (var13.hasNext()) {
				AxisAlignedBB var14 = (AxisAlignedBB)var13.next();
				if(var14.maxY > var11) {
					var11 = var14.maxY;
				}
			}

			par3 += var11 - this.boundingBox.minY;
			this.setPosition(par1, par3, par5);
		}
	}

	public float getCollisionBorderSize() {
		return 0.1F;
	}

	public Vec3 getLookVec() {
		return null;
	}

	public void setInPortal() {}

	public void setVelocity(double par1, double par3, double par5) {
		this.motionX = par1;
		this.motionY = par3;
		this.motionZ = par5;
	}

	public void handleHealthUpdate(byte par1) {}

	public void performHurtAnimation() {}

	public void updateCloak() {}

	public ItemStack[] getLastActiveItems() {
		return null;
	}

	public void func_70062_b(int par1, ItemStack par2ItemStack) {}

	public boolean isBurning() {
		return this.fire > 0 || this.getFlag(0);
	}

	public boolean isRiding() {
		return this.ridingEntity != null || this.getFlag(2);
	}

	public boolean isSneaking() {
		return this.getFlag(1);
	}
	
	public void setSneaking(boolean par1) {
		this.setFlag(1, par1);
	}

	public boolean isSprinting() {
		return this.getFlag(3);
	}

	public void setSprinting(boolean par1) {
		this.setFlag(3, par1);
	}

	public boolean isEating() {
		return this.getFlag(4);
	}

	public void setEating(boolean par1) {
		this.setFlag(4, par1);
	}

	protected boolean getFlag(int par1) {
		return (this.dataWatcher.getWatchableObjectByte(0) & 1 << par1) != 0;
	}

	public void setFlag(int var1, boolean var2) { // Spout protected -> public
		byte var3 = this.dataWatcher.getWatchableObjectByte(0);
		if(var2) {
			this.dataWatcher.updateObject(0, Byte.valueOf((byte)(var3 | 1 << var1)));
		} else {
			this.dataWatcher.updateObject(0, Byte.valueOf((byte)(var3 & ~(1 << var1))));
		}
	}

	public int getAir() {
		return this.dataWatcher.getWatchableObjectShort(1);
	}

	public void setAir(int par1) {
		this.dataWatcher.updateObject(1, Short.valueOf((short)par1));
	}

	public void onStruckByLightning(EntityLightningBolt par1EntityLightningBolt) {
		this.dealFireDamage(5);
		++this.fire;
		if(this.fire == 0) {
			this.setFire(8);
		}
	}

	public void onKillEntity(EntityLiving par1EntityLiving) {}

	protected boolean pushOutOfBlocks(double par1, double par3, double par5) {
		int var7 = MathHelper.floor_double(par1);
		int var8 = MathHelper.floor_double(par3);
		int var9 = MathHelper.floor_double(par5);
		double var10 = par1 - (double)var7;
		double var12 = par3 - (double)var8;
		double var14 = par5 - (double)var9;
		if(this.worldObj.isBlockNormalCube(var7, var8, var9)) {
			boolean var16 = !this.worldObj.isBlockNormalCube(var7 - 1, var8, var9);
			boolean var17 = !this.worldObj.isBlockNormalCube(var7 + 1, var8, var9);
			boolean var18 = !this.worldObj.isBlockNormalCube(var7, var8 - 1, var9);
			boolean var19 = !this.worldObj.isBlockNormalCube(var7, var8 + 1, var9);
			boolean var20 = !this.worldObj.isBlockNormalCube(var7, var8, var9 - 1);
			boolean var21 = !this.worldObj.isBlockNormalCube(var7, var8, var9 + 1);
			byte var22 = -1;
			double var23 = 9999.0D;
			if(var16 && var10 < var23) {
				var23 = var10;
				var22 = 0;
			}

			if(var17 && 1.0D - var10 < var23) {
				var23 = 1.0D - var10;
				var22 = 1;
			}

			if(var18 && var12 < var23) {
				var23 = var12;
				var22 = 2;
			}

			if(var19 && 1.0D - var12 < var23) {
				var23 = 1.0D - var12;
				var22 = 3;
			}

			if(var20 && var14 < var23) {
				var23 = var14;
				var22 = 4;
			}

			if(var21 && 1.0D - var14 < var23) {
				var23 = 1.0D - var14;
				var22 = 5;
			}

			float var25 = this.rand.nextFloat() * 0.2F + 0.1F;
			if(var22 == 0) {
				this.motionX = (double)(-var25);
			}

			if(var22 == 1) {
				this.motionX = (double)var25;
			}

			if(var22 == 2) {
				this.motionY = (double)(-var25);
			}

			if(var22 == 3) {
				this.motionY = (double)var25;
			}

			if(var22 == 4) {
				this.motionZ = (double)(-var25);
			}

			if(var22 == 5) {
				this.motionZ = (double)var25;
			}

			return true;
		} else {
			return false;
		}
	}

	public void setInWeb() {
		this.isInWeb = true;
		this.fallDistance = 0.0F;
	}

	public String getEntityName() {
		String var1 = EntityList.getEntityString(this);

		if (var1 == null) {
			var1 = "generic";
		}

		return StatCollector.translateToLocal("entity." + var1 + ".name");
	}

	public Entity[] getParts() {
		return null;
	}

	public boolean isEntityEqual(Entity par1Entity) {
		return this == par1Entity;
	}

	public float func_70079_am() {
		return 0.0F;
	}

	public void setHeadRotationYaw(float par1) {}

	public boolean canAttackWithItem() {
		return true;
	}

	public String toString() {
		return String.format("%s[\'%s\'/%d, l=\'%s\', x=%.2f, y=%.2f, z=%.2f]", new Object[] {this.getClass().getSimpleName(), this.getEntityName(), Integer.valueOf(this.entityId), this.worldObj == null ? "~NULL~" : this.worldObj.getWorldInfo().getWorldName(), Double.valueOf(this.posX), Double.valueOf(this.posY), Double.valueOf(this.posZ)});
	}
}
