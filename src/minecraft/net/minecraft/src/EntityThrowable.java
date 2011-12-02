package net.minecraft.src;

import java.util.List;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Vec3D;
import net.minecraft.src.World;

public abstract class EntityThrowable extends Entity {

	private int field_40079_d = -1;
	private int field_40080_e = -1;
	private int field_40082_ao = -1;
	private int field_40084_ap = 0;
	protected boolean field_40085_a = false;
	public int field_40081_b = 0;
	public EntityLiving field_40083_c; //Spout protected -> public
    private int field_40087_aq;
	private int field_40086_ar = 0;


	public EntityThrowable(World var1) {
		super(var1);
		this.setSize(0.25F, 0.25F);
	}

	protected void entityInit() {}

	public boolean isInRangeToRenderDist(double var1) {
		double var3 = this.boundingBox.getAverageEdgeLength() * 4.0D;
		var3 *= 64.0D;
		return var1 < var3 * var3;
	}

	public EntityThrowable(World var1, EntityLiving var2) {
		super(var1);
		this.field_40083_c = var2;
		this.setSize(0.25F, 0.25F);
		this.setLocationAndAngles(var2.posX, var2.posY + (double)var2.getEyeHeight(), var2.posZ, var2.rotationYaw, var2.rotationPitch);
		this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
		this.posY -= 0.10000000149011612D;
		this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.yOffset = 0.0F;
		float var3 = 0.4F;
		this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * var3);
		this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * var3);
		this.motionY = (double)(-MathHelper.sin((this.rotationPitch + this.func_40074_d()) / 180.0F * 3.1415927F) * var3);
		this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, this.func_40077_c(), 1.0F);
	}

	public EntityThrowable(World var1, double var2, double var4, double var6) {
		super(var1);
		this.field_40087_aq = 0;
		this.setSize(0.25F, 0.25F);
		this.setPosition(var2, var4, var6);
		this.yOffset = 0.0F;
    }

	protected float func_40077_c() {
        return 1.5F;
    }

	protected float func_40074_d() {
        return 0.0F;
    }

	public void setThrowableHeading(double var1, double var3, double var5, float var7, float var8) {
		float var9 = MathHelper.sqrt_double(var1 * var1 + var3 * var3 + var5 * var5);
		var1 /= (double)var9;
		var3 /= (double)var9;
		var5 /= (double)var9;
		var1 += this.rand.nextGaussian() * 0.007499999832361937D * (double)var8;
		var3 += this.rand.nextGaussian() * 0.007499999832361937D * (double)var8;
		var5 += this.rand.nextGaussian() * 0.007499999832361937D * (double)var8;
		var1 *= (double)var7;
		var3 *= (double)var7;
		var5 *= (double)var7;
		this.motionX = var1;
		this.motionY = var3;
		this.motionZ = var5;
		float var10 = MathHelper.sqrt_double(var1 * var1 + var5 * var5);
		this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(var1, var5) * 180.0D / 3.1415927410125732D);
		this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(var3, (double)var10) * 180.0D / 3.1415927410125732D);
		this.field_40087_aq = 0;
    }

	public void setVelocity(double var1, double var3, double var5) {
		this.motionX = var1;
		this.motionY = var3;
		this.motionZ = var5;
		if(this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			float var7 = MathHelper.sqrt_double(var1 * var1 + var5 * var5);
			this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(var1, var5) * 180.0D / 3.1415927410125732D);
			this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(var3, (double)var7) * 180.0D / 3.1415927410125732D);
        }

    }

	public void onUpdate() {
		this.lastTickPosX = this.posX;
		this.lastTickPosY = this.posY;
		this.lastTickPosZ = this.posZ;
        super.onUpdate();
		if(this.field_40081_b > 0) {
			--this.field_40081_b;
        }

		if(this.field_40085_a) {
			int var1 = this.worldObj.getBlockId(this.field_40079_d, this.field_40080_e, this.field_40082_ao);
			if(var1 == this.field_40084_ap) {
				++this.field_40087_aq;
				if(this.field_40087_aq == 1200) {
					this.setEntityDead();
                }

                return;
            }

			this.field_40085_a = false;
			this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
			this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
			this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
			this.field_40087_aq = 0;
			this.field_40086_ar = 0;
		} else {
			++this.field_40086_ar;
		}

		Vec3D var15 = Vec3D.createVector(this.posX, this.posY, this.posZ);
		Vec3D var2 = Vec3D.createVector(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		MovingObjectPosition var3 = this.worldObj.rayTraceBlocks(var15, var2);
		var15 = Vec3D.createVector(this.posX, this.posY, this.posZ);
		var2 = Vec3D.createVector(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
		if(var3 != null) {
			var2 = Vec3D.createVector(var3.hitVec.xCoord, var3.hitVec.yCoord, var3.hitVec.zCoord);
		}

		if(!this.worldObj.multiplayerWorld) {
			Entity var4 = null;
			List var5 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
			double var6 = 0.0D;

			for(int var8 = 0; var8 < var5.size(); ++var8) {
				Entity var9 = (Entity)var5.get(var8);
				if(var9.canBeCollidedWith() && (var9 != this.field_40083_c || this.field_40086_ar >= 5)) {
					float var10 = 0.3F;
					AxisAlignedBB var11 = var9.boundingBox.expand((double)var10, (double)var10, (double)var10);
					MovingObjectPosition var12 = var11.func_1169_a(var15, var2);
					if(var12 != null) {
						double var13 = var15.distanceTo(var12.hitVec);
						if(var13 < var6 || var6 == 0.0D) {
							var4 = var9;
							var6 = var13;
						}
					}
				}
			}

			if(var4 != null) {
				var3 = new MovingObjectPosition(var4);
			}
		}

		if(var3 != null) {
			this.func_40078_a(var3);
		}

		this.posX += this.motionX;
		this.posY += this.motionY;
		this.posZ += this.motionZ;
		float var16 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / 3.1415927410125732D);

		for(this.rotationPitch = (float)(Math.atan2(this.motionY, (double)var16) * 180.0D / 3.1415927410125732D); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
			;
		}

		while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
			this.prevRotationPitch += 360.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
			this.prevRotationYaw -= 360.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
			this.prevRotationYaw += 360.0F;
        }

		this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
		this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
		float var17 = 0.99F;
		float var18 = this.func_40075_e();
		if(this.isInWater()) {
			for(int var7 = 0; var7 < 4; ++var7) {
				float var19 = 0.25F;
				this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double)var19, this.posY - this.motionY * (double)var19, this.posZ - this.motionZ * (double)var19, this.motionX, this.motionY, this.motionZ);
    }

			var17 = 0.8F;
		}

		this.motionX *= (double)var17;
		this.motionY *= (double)var17;
		this.motionZ *= (double)var17;
		this.motionY -= (double)var18;
		this.setPosition(this.posX, this.posY, this.posZ);
	}

	protected float func_40075_e() {
        return 0.03F;
    }

	protected abstract void func_40078_a(MovingObjectPosition var1);

	public void writeEntityToNBT(NBTTagCompound var1) {
		var1.setShort("xTile", (short)this.field_40079_d);
		var1.setShort("yTile", (short)this.field_40080_e);
		var1.setShort("zTile", (short)this.field_40082_ao);
		var1.setByte("inTile", (byte)this.field_40084_ap);
		var1.setByte("shake", (byte)this.field_40081_b);
		var1.setByte("inGround", (byte)(this.field_40085_a?1:0));
    }

	public void readEntityFromNBT(NBTTagCompound var1) {
		this.field_40079_d = var1.getShort("xTile");
		this.field_40080_e = var1.getShort("yTile");
		this.field_40082_ao = var1.getShort("zTile");
		this.field_40084_ap = var1.getByte("inTile") & 255;
		this.field_40081_b = var1.getByte("shake") & 255;
		this.field_40085_a = var1.getByte("inGround") == 1;
    }

	public void onCollideWithPlayer(EntityPlayer var1) {}

	public float getShadowSize() {
        return 0.0F;
    }
}
