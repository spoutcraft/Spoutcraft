package net.minecraft.src;

import net.minecraft.src.EntityCreature;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import org.spoutcraft.client.entity.CraftWaterMob; //Spout

public abstract class EntityWaterMob extends EntityCreature {

	public EntityWaterMob(World var1) {
		super(var1);
		//Spout start
		this.spoutEntity = new CraftWaterMob(this);
		//Spout end
	}

	public boolean canBreatheUnderwater() {
		return true;
	}

	public void writeEntityToNBT(NBTTagCompound var1) {
		super.writeEntityToNBT(var1);
	}

	public void readEntityFromNBT(NBTTagCompound var1) {
		super.readEntityFromNBT(var1);
	}

	public boolean getCanSpawnHere() {
		return this.worldObj.checkIfAABBIsClear(this.boundingBox);
	}

	public int getTalkInterval() {
		return 120;
	}

	protected boolean canDespawn() {
		return true;
	}

	protected int func_36001_a(EntityPlayer var1) {
		return 1 + this.worldObj.rand.nextInt(3);
	}
}
