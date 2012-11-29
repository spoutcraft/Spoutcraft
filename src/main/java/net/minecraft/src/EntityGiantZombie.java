package net.minecraft.src;

import org.spoutcraft.client.entity.CraftGiant;

public class EntityGiantZombie extends EntityMob {
	public EntityGiantZombie(World par1World) {
		super(par1World);
		this.texture = "/mob/zombie.png";
		this.moveSpeed = 0.5F;
		this.yOffset *= 6.0F;
		this.setSize(this.width * 6.0F, this.height * 6.0F);
		// Spout Start
		this.spoutEntity = new CraftGiant(this);
		// Spout End
	}

	public int getMaxHealth() {
		return 100;
	}

	/**
	 * Takes a coordinate in and returns a weight to determine how likely this creature will try to path to the block.
	 * Args: x, y, z
	 */
	public float getBlockPathWeight(int par1, int par2, int par3) {
		return this.worldObj.getLightBrightness(par1, par2, par3) - 0.5F;
	}

	/**
	 * Returns the amount of damage a mob should deal.
	 */
	public int getAttackStrength(Entity par1Entity) {
		return 50;
	}
}
