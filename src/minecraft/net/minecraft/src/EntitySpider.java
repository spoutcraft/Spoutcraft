package net.minecraft.src;

import org.getspout.spout.entity.CraftSpider;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityMob;
import net.minecraft.src.Item;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

public class EntitySpider extends EntityMob {

	public EntitySpider(World var1) {
		super(var1);
		this.texture = "/mob/spider.png";
		this.setSize(1.4F, 0.9F);
		this.moveSpeed = 0.8F;
		//Spout start
		this.spoutEntity = new CraftSpider(this);
		//Spout end
	}

	public double getMountedYOffset() {
		return (double)this.height * 0.75D - 0.5D;
	}

	protected boolean canTriggerWalking() {
		return false;
	}

	protected Entity findPlayerToAttack() {
		float var1 = this.getEntityBrightness(1.0F);
		if(var1 < 0.5F) {
			double var2 = 16.0D;
			return this.worldObj.getClosestPlayerToEntity(this, var2);
		} else {
			return null;
		}
	}

	protected String getLivingSound() {
		return "mob.spider";
	}

	protected String getHurtSound() {
		return "mob.spider";
	}

	protected String getDeathSound() {
		return "mob.spiderdeath";
	}

	protected void attackEntity(Entity var1, float var2) {
		float var3 = this.getEntityBrightness(1.0F);
		if(var3 > 0.5F && this.rand.nextInt(100) == 0) {
			this.entityToAttack = null;
		} else {
			if(var2 > 2.0F && var2 < 6.0F && this.rand.nextInt(10) == 0) {
				if(this.onGround) {
					double var4 = var1.posX - this.posX;
					double var6 = var1.posZ - this.posZ;
					float var8 = MathHelper.sqrt_double(var4 * var4 + var6 * var6);
					this.motionX = var4 / (double)var8 * 0.5D * 0.800000011920929D + this.motionX * 0.20000000298023224D;
					this.motionZ = var6 / (double)var8 * 0.5D * 0.800000011920929D + this.motionZ * 0.20000000298023224D;
					this.motionY = 0.4000000059604645D;
				}
			} else {
				super.attackEntity(var1, var2);
			}

		}
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
	}

	protected int getDropItemId() {
		return Item.silk.shiftedIndex;
	}

	public boolean isOnLadder() {
		return this.isCollidedHorizontally;
	}

	public void func_35112_o() {}

	public float func_35188_k_() {
		return 1.0F;
	}
}
