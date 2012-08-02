package net.minecraft.src;

import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityMob;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntitySmallFireball;
import net.minecraft.src.Item;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

import org.spoutcraft.client.entity.CraftBlaze; //Spout

public class EntityBlaze extends EntityMob {

	private float heightOffset = 0.5F;
	private int heightOffsetUpdateTime;
	private int field_70846_g;

	public EntityBlaze(World par1World) {
		super(par1World);
		this.texture = "/mob/fire.png";
		this.isImmuneToFire = true;
		this.attackStrength = 6;
		this.experienceValue = 10;
		//Spout start
		this.spoutEntity = new CraftBlaze(this);
		//Spout end
	}

	public int getMaxHealth() {
		return 20;
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, new Byte((byte)0));
	}

	protected String getLivingSound() {
		return "mob.blaze.breathe";
	}

	protected String getHurtSound() {
		return "mob.blaze.hit";
	}

	protected String getDeathSound() {
		return "mob.blaze.death";
	}

	public int getBrightnessForRender(float par1) {
		return 15728880;
	}

	public float getBrightness(float par1) {
		return 1.0F;
	}

	public void onLivingUpdate() {
		if(!this.worldObj.isRemote) {
			if(this.isWet()) {
				this.attackEntityFrom(DamageSource.drown, 1);
			}

			--this.heightOffsetUpdateTime;
			if(this.heightOffsetUpdateTime <= 0) {
				this.heightOffsetUpdateTime = 100;
				this.heightOffset = 0.5F + (float)this.rand.nextGaussian() * 3.0F;
			}

			if(this.getEntityToAttack() != null && this.getEntityToAttack().posY + (double)this.getEntityToAttack().getEyeHeight() > this.posY + (double)this.getEyeHeight() + (double)this.heightOffset) {
				this.motionY += (0.30000001192092896D - this.motionY) * 0.30000001192092896D;
			}
		}

		if(this.rand.nextInt(24) == 0) {
			this.worldObj.playSoundEffect(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "fire.fire", 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F);
		}

		if(!this.onGround && this.motionY < 0.0D) {
			this.motionY *= 0.6D;
		}

		for(int var1 = 0; var1 < 2; ++var1) {
			this.worldObj.spawnParticle("largesmoke", this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, 0.0D, 0.0D, 0.0D);
		}

		super.onLivingUpdate();
	}

	protected void attackEntity(Entity par1Entity, float par2) {
		if(this.attackTime <= 0 && par2 < 2.0F && par1Entity.boundingBox.maxY > this.boundingBox.minY && par1Entity.boundingBox.minY < this.boundingBox.maxY) {
			this.attackTime = 20;
			this.attackEntityAsMob(par1Entity);
		} else if(par2 < 30.0F) {
			double var3 = par1Entity.posX - this.posX;
			double var5 = par1Entity.boundingBox.minY + (double)(par1Entity.height / 2.0F) - (this.posY + (double)(this.height / 2.0F));
			double var7 = par1Entity.posZ - this.posZ;
			if(this.attackTime == 0) {
				++this.field_70846_g;
				if(this.field_70846_g == 1) {
					this.attackTime = 60;
					this.func_70844_e(true);
				} else if(this.field_70846_g <= 4) {
					this.attackTime = 6;
				} else {
					this.attackTime = 100;
					this.field_70846_g = 0;
					this.func_70844_e(false);
				}

				if(this.field_70846_g > 1) {
					float var9 = MathHelper.sqrt_float(par2) * 0.5F;
					this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1009, (int)this.posX, (int)this.posY, (int)this.posZ, 0);

					for(int var10 = 0; var10 < 1; ++var10) {
						EntitySmallFireball var11 = new EntitySmallFireball(this.worldObj, this, var3 + this.rand.nextGaussian() * (double)var9, var5, var7 + this.rand.nextGaussian() * (double)var9);
						var11.posY = this.posY + (double)(this.height / 2.0F) + 0.5D;
						this.worldObj.spawnEntityInWorld(var11);
					}
				}
			}

			this.rotationYaw = (float)(Math.atan2(var7, var3) * 180.0D / Math.PI) - 90.0F;
			this.hasAttacked = true;
		}
	}

	protected void fall(float par1) {}

	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);
	}

	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);
	}

	protected int getDropItemId() {
		return Item.blazeRod.shiftedIndex;
	}

	public boolean isBurning() {
		return this.func_70845_n();
	}

	protected void dropFewItems(boolean par1, int par2) {
		if(par1) {
			int var3 = this.rand.nextInt(2 + par2);

			for(int var4 = 0; var4 < var3; ++var4) {
				this.dropItem(Item.blazeRod.shiftedIndex, 1);
			}
		}
	}

	public boolean func_70845_n() {
		return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	public void func_70844_e(boolean par1) {
		byte var2 = this.dataWatcher.getWatchableObjectByte(16);
		if(par1) {
			var2 = (byte)(var2 | 1);
		} else {
			var2 &= -2;
		}

		this.dataWatcher.updateObject(16, Byte.valueOf(var2));
	}

	protected boolean isValidLightLevel() {
		return true;
	}
}
