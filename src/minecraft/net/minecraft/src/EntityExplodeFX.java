package net.minecraft.src;

import java.util.Random;

public class EntityExplodeFX extends EntityFX {
	public EntityExplodeFX(World world, double d, double d1, double d2,
	        double d3, double d4, double d5) {
		super(world, d, d1, d2, d3, d4, d5);
		motionX = d3 + (double)((float)(Math.random() * 2D - 1.0D) * 0.05F);
		motionY = d4 + (double)((float)(Math.random() * 2D - 1.0D) * 0.05F);
		motionZ = d5 + (double)((float)(Math.random() * 2D - 1.0D) * 0.05F);
		particleRed = particleGreen = particleBlue = rand.nextFloat() * 0.3F + 0.7F;
		particleScale = rand.nextFloat() * rand.nextFloat() * 6F + 1.0F;
		particleMaxAge = (int)(16D / ((double)rand.nextFloat() * 0.80000000000000004D + 0.20000000000000001D)) + 2;
	}

	public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
		super.renderParticle(tessellator, f, f1, f2, f3, f4, f5);
	}

	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		if (particleAge++ >= particleMaxAge) {
			setEntityDead();
		}
		setParticleTextureIndex(7 - (particleAge * 8) / particleMaxAge);
		motionY += 0.0040000000000000001D;
		moveEntity(motionX, motionY, motionZ);
		motionX *= 0.89999997615814209D;
		motionY *= 0.89999997615814209D;
		motionZ *= 0.89999997615814209D;
		if (onGround) {
			motionX *= 0.69999998807907104D;
			motionZ *= 0.69999998807907104D;
		}
	}
}
