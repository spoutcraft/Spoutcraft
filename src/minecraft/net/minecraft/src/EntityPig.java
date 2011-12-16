package net.minecraft.src;

import org.spoutcraft.client.entity.CraftPig;

import net.minecraft.src.AchievementList;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityLightningBolt;
import net.minecraft.src.EntityPigZombie;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

public class EntityPig extends EntityAnimal {

	public EntityPig(World var1) {
		super(var1);
		this.texture = "/mob/pig.png";
		this.setSize(0.9F, 0.9F);
		//Spout start
		this.spoutEntity = new CraftPig(this);
		//Spout end
	}

	public int getMaxHealth() {
		return 10;
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
		var1.setBoolean("Saddle", this.getSaddled());
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
		this.setSaddled(var1.getBoolean("Saddle"));
	}

	protected String getLivingSound() {
		return "mob.pig";
	}

	protected String getHurtSound() {
		return "mob.pig";
	}

	protected String getDeathSound() {
		return "mob.pigdeath";
	}

	public boolean interact(EntityPlayer var1) {
		if(super.interact(var1)) {
			return true;
		} else if(this.getSaddled() && !this.worldObj.multiplayerWorld && (this.riddenByEntity == null || this.riddenByEntity == var1)) {
			var1.mountEntity(this);
			return true;
		} else {
			return false;
		}
	}

	protected int getDropItemId() {
		return this.isBurning()?Item.porkCooked.shiftedIndex:Item.porkRaw.shiftedIndex;
	}

	public boolean getSaddled() {
		return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	public void setSaddled(boolean var1) {
		if(var1) {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte)1));
		} else {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte)0));
		}

	}

	public void onStruckByLightning(EntityLightningBolt var1) {
		if(!this.worldObj.multiplayerWorld) {
			EntityPigZombie var2 = new EntityPigZombie(this.worldObj);
			var2.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
			this.worldObj.entityJoinedWorld(var2);
			this.setEntityDead();
		}
	}

	protected void fall(float var1) {
		super.fall(var1);
		if(var1 > 5.0F && this.riddenByEntity instanceof EntityPlayer) {
			((EntityPlayer)this.riddenByEntity).triggerAchievement(AchievementList.flyPig);
		}

	}

	protected EntityAnimal func_40145_a(EntityAnimal var1) {
		return new EntityPig(this.worldObj);
	}
}
