package net.minecraft.src;

import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityCreature;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumSkyBlock;
import net.minecraft.src.IMob;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Potion;
import net.minecraft.src.World;
import org.spoutcraft.client.entity.CraftMonster; //Spout

public abstract class EntityMob extends EntityCreature implements IMob {

	protected int attackStrength = 2;

	public EntityMob(World par1World) {
		super(par1World);
		this.experienceValue = 5;
		//Spout start
		this.spoutEntity = new CraftMonster(this);
		//Spout end
	}

	public void onLivingUpdate() {
		float var1 = this.getEntityBrightness(1.0F);
		if (var1 > 0.5F) {
			this.entityAge += 2;
		}

		super.onLivingUpdate();
	}

	public void onUpdate() {
		super.onUpdate();
		if (!this.worldObj.isRemote && this.worldObj.difficultySetting == 0) {
			this.setEntityDead();
		}

	}

	protected Entity findPlayerToAttack() {
		EntityPlayer var1 = this.worldObj.getClosestVulnerablePlayerToEntity(this, 16.0D);
		return var1 != null && this.canEntityBeSeen(var1)?var1:null;
	}

	public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
		if (super.attackEntityFrom(par1DamageSource, par2)) {
			Entity var3 = par1DamageSource.getEntity();
			if (this.riddenByEntity != var3 && this.ridingEntity != var3) {
				if (var3 != this) {
					this.entityToAttack = var3;
				}

				return true;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	public boolean attackEntityAsMob(Entity par1Entity) {
		int var2 = this.attackStrength;
		if (this.isPotionActive(Potion.damageBoost)) {
			var2 += 3 << this.getActivePotionEffect(Potion.damageBoost).getAmplifier();
		}

		if (this.isPotionActive(Potion.weakness)) {
			var2 -= 2 << this.getActivePotionEffect(Potion.weakness).getAmplifier();
		}

		return par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), var2);
	}

	protected void attackEntity(Entity par1Entity, float par2) {
		if (this.attackTime <= 0 && par2 < 2.0F && par1Entity.boundingBox.maxY > this.boundingBox.minY && par1Entity.boundingBox.minY < this.boundingBox.maxY) {
			this.attackTime = 20;
			this.attackEntityAsMob(par1Entity);
		}

	}

	public float getBlockPathWeight(int par1, int par2, int par3) {
		return 0.5F - this.worldObj.getLightBrightness(par1, par2, par3);
	}

	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);
	}

	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);
	}

	protected boolean isValidLightLevel() {
		int var1 = MathHelper.floor_double(this.posX);
		int var2 = MathHelper.floor_double(this.boundingBox.minY);
		int var3 = MathHelper.floor_double(this.posZ);
		if (this.worldObj.getSavedLightValue(EnumSkyBlock.Sky, var1, var2, var3) > this.rand.nextInt(32)) {
			return false;
		} else {
			int var4 = this.worldObj.getBlockLightValue(var1, var2, var3);
			if (this.worldObj.isThundering()) {
				int var5 = this.worldObj.skylightSubtracted;
				this.worldObj.skylightSubtracted = 10;
				var4 = this.worldObj.getBlockLightValue(var1, var2, var3);
				this.worldObj.skylightSubtracted = var5;
			}

			return var4 <= this.rand.nextInt(8);
		}
	}

	public boolean getCanSpawnHere() {
		return this.isValidLightLevel() && super.getCanSpawnHere();
	}
}
