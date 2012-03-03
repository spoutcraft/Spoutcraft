package net.minecraft.src;

import org.spoutcraft.client.entity.CraftGiant;

import net.minecraft.src.EntityMob;
import net.minecraft.src.World;

public class EntityGiantZombie extends EntityMob {

	public EntityGiantZombie(World par1World) {
		super(par1World);
		this.texture = "/mob/zombie.png";
		this.moveSpeed = 0.5F;
		this.attackStrength = 50;
		this.yOffset *= 6.0F;
		this.setSize(this.width * 6.0F, this.height * 6.0F);
		//Spout start
		this.spoutEntity = new CraftGiant(this);
		//Spout end
	}

	public int getMaxHealth() {
		return 100;
	}

	public float getBlockPathWeight(int par1, int par2, int par3) {
		return this.worldObj.getLightBrightness(par1, par2, par3) - 0.5F;
	}
}
