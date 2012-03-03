package net.minecraft.src;

import net.minecraft.src.EntityCreature;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import org.spoutcraft.client.entity.CraftWaterMob; //Spout

public abstract class EntityWaterMob extends EntityCreature {

	public EntityWaterMob(World par1World) {
		super(par1World);
		//Spout start
		this.spoutEntity = new CraftWaterMob(this);
		//Spout end
	}

	public boolean canBreatheUnderwater() {
		return true;
	}

	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);
	}

	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);
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

	protected int getExperiencePoints(EntityPlayer par1EntityPlayer) {
		return 1 + this.worldObj.rand.nextInt(3);
	}
}
