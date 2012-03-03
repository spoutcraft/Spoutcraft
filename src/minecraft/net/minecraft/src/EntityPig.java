package net.minecraft.src;

import org.spoutcraft.client.entity.CraftPig;

import net.minecraft.src.AchievementList;
import net.minecraft.src.EntityAIFollowParent;
import net.minecraft.src.EntityAILookIdle;
import net.minecraft.src.EntityAIMate;
import net.minecraft.src.EntityAIPanic;
import net.minecraft.src.EntityAISwimming;
import net.minecraft.src.EntityAITempt;
import net.minecraft.src.EntityAIWander;
import net.minecraft.src.EntityAIWatchClosest;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityLightningBolt;
import net.minecraft.src.EntityPigZombie;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

public class EntityPig extends EntityAnimal {

	public EntityPig(World par1World) {
		super(par1World);
		this.texture = "/mob/pig.png";
		this.setSize(0.9F, 0.9F);
		this.func_48084_aL().func_48664_a(true);
		float var2 = 0.25F;
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIPanic(this, 0.38F));
		this.tasks.addTask(2, new EntityAIMate(this, var2));
		this.tasks.addTask(3, new EntityAITempt(this, 0.25F, Item.wheat.shiftedIndex, false));
		this.tasks.addTask(4, new EntityAIFollowParent(this, 0.28F));
		this.tasks.addTask(5, new EntityAIWander(this, var2));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(7, new EntityAILookIdle(this));
		//Spout start
		this.spoutEntity = new CraftPig(this);
		//Spout end
	}

	public boolean isAIEnabled() {
		return true;
	}

	public int getMaxHealth() {
		return 10;
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
	}

	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setBoolean("Saddle", this.getSaddled());
	}

	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);
		this.setSaddled(par1NBTTagCompound.getBoolean("Saddle"));
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

	public boolean interact(EntityPlayer par1EntityPlayer) {
		if (super.interact(par1EntityPlayer)) {
			return true;
		} else if (this.getSaddled() && !this.worldObj.isRemote && (this.riddenByEntity == null || this.riddenByEntity == par1EntityPlayer)) {
			par1EntityPlayer.mountEntity(this);
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

	public void setSaddled(boolean par1) {
		if (par1) {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte)1));
		} else {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte)0));
		}

	}

	public void onStruckByLightning(EntityLightningBolt par1EntityLightningBolt) {
		if (!this.worldObj.isRemote) {
			EntityPigZombie var2 = new EntityPigZombie(this.worldObj);
			var2.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
			this.worldObj.spawnEntityInWorld(var2);
			this.setEntityDead();
		}
	}

	protected void fall(float par1) {
		super.fall(par1);
		if (par1 > 5.0F && this.riddenByEntity instanceof EntityPlayer) {
			((EntityPlayer)this.riddenByEntity).triggerAchievement(AchievementList.flyPig);
		}

	}

	public EntityAnimal spawnBabyAnimal(EntityAnimal par1EntityAnimal) {
		return new EntityPig(this.worldObj);
	}
}
