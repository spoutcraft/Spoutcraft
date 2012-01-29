package net.minecraft.src;

import java.util.Random;

public class EntitySuspendFX extends EntityFX {
	public EntitySuspendFX(World world, double d, double d1, double d2,
	        double d3, double d4, double d5) {
		super(world, d, d1 - 0.125D, d2, d3, d4, d5);
		particleRed = 0.4F;
		particleGreen = 0.4F;
		particleBlue = 0.7F;
		setParticleTextureIndex(0);
		setSize(0.01F, 0.01F);
		particleScale = particleScale * (rand.nextFloat() * 0.6F + 0.2F);
		motionX = d3 * 0.0D;
		motionY = d4 * 0.0D;
		motionZ = d5 * 0.0D;
		particleMaxAge = (int)(16D / (Math.random() * 0.80000000000000004D + 0.20000000000000001D));
	}

	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		moveEntity(motionX, motionY, motionZ);
		if (worldObj.getBlockMaterial(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ)) != Material.water) {
			setEntityDead();
		}
		if (particleMaxAge-- <= 0) {
			setEntityDead();
		}
	}
}
