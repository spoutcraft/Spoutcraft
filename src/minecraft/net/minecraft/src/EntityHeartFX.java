package net.minecraft.src;

public class EntityHeartFX extends EntityFX {
	float particleScaleOverTime;

	public EntityHeartFX(World world, double d, double d1, double d2,
	        double d3, double d4, double d5) {
		this(world, d, d1, d2, d3, d4, d5, 2.0F);
	}

	public EntityHeartFX(World world, double d, double d1, double d2,
	        double d3, double d4, double d5, float f) {
		super(world, d, d1, d2, 0.0D, 0.0D, 0.0D);
		motionX *= 0.0099999997764825821D;
		motionY *= 0.0099999997764825821D;
		motionZ *= 0.0099999997764825821D;
		motionY += 0.10000000000000001D;
		particleScale *= 0.75F;
		particleScale *= f;
		particleScaleOverTime = particleScale;
		particleMaxAge = 16;
		noClip = false;
		setParticleTextureIndex(80);
	}

	public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
		float f6 = (((float)particleAge + f) / (float)particleMaxAge) * 32F;
		if (f6 < 0.0F) {
			f6 = 0.0F;
		}
		if (f6 > 1.0F) {
			f6 = 1.0F;
		}
		particleScale = particleScaleOverTime * f6;
		super.renderParticle(tessellator, f, f1, f2, f3, f4, f5);
	}

	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		if (particleAge++ >= particleMaxAge) {
			setEntityDead();
		}
		moveEntity(motionX, motionY, motionZ);
		if (posY == prevPosY) {
			motionX *= 1.1000000000000001D;
			motionZ *= 1.1000000000000001D;
		}
		motionX *= 0.86000001430511475D;
		motionY *= 0.86000001430511475D;
		motionZ *= 0.86000001430511475D;
		if (onGround) {
			motionX *= 0.69999998807907104D;
			motionZ *= 0.69999998807907104D;
		}
	}
}
