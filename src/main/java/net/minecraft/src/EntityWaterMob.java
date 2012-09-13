package net.minecraft.src;

import org.spoutcraft.client.entity.CraftWaterMob; // Spout

public abstract class EntityWaterMob extends EntityCreature implements IAnimals {
	public EntityWaterMob(World par1World) {
		super(par1World);
		// Spout Start
		this.spoutEntity = new CraftWaterMob(this);
		// Spout End
	}

	public boolean canBreatheUnderwater() {
		return true;
	}

	/**
	 * Checks if the entity's current position is a valid location to spawn this entity.
	 */
	public boolean getCanSpawnHere() {
		return this.worldObj.checkIfAABBIsClear(this.boundingBox);
	}

	/**
	 * Get number of ticks, at least during which the living entity will be silent.
	 */
	public int getTalkInterval() {
		return 120;
	}

	/**
	 * Determines if an entity can be despawned, used on idle far away entities
	 */
	protected boolean canDespawn() {
		return true;
	}

	/**
	 * Get the experience points the entity currently has.
	 */
	protected int getExperiencePoints(EntityPlayer par1EntityPlayer) {
		return 1 + this.worldObj.rand.nextInt(3);
	}
}
