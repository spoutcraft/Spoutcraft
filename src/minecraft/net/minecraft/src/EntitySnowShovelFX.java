package net.minecraft.src;

public class EntitySnowShovelFX extends EntityFX {
	float snowDigParticleScale;

	public EntitySnowShovelFX(World world, double d, double d1, double d2,
	        double d3, double d4, double d5) {
		this(world, d, d1, d2, d3, d4, d5, 1.0F);
	}

	public EntitySnowShovelFX(World world, double d, double d1, double d2,
	        double d3, double d4, double d5, float f) {
		super(world, d, d1, d2, d3, d4, d5);
		motionX *= 0.10000000149011612D;
		motionY *= 0.10000000149011612D;
		motionZ *= 0.10000000149011612D;
		motionX += d3;
		motionY += d4;
		motionZ += d5;
		particleRed = particleGreen = particleBlue = 1.0F - (float)(Math.random() * 0.30000001192092896D);
		particleScale *= 0.75F;
		particleScale *= f;
		snowDigParticleScale = particleScale;
		particleMaxAge = (int)(8D / (Math.random() * 0.80000000000000004D + 0.20000000000000001D));
		particleMaxAge *= f;
		noClip = false;
	}

	public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
		float f6 = (((float)particleAge + f) / (float)particleMaxAge) * 32F;
		if (f6 < 0.0F) {
			f6 = 0.0F;
		}
		if (f6 > 1.0F) {
			f6 = 1.0F;
		}
		particleScale = snowDigParticleScale * f6;
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
		motionY -= 0.029999999999999999D;
		moveEntity(motionX, motionY, motionZ);
		motionX *= 0.99000000953674316D;
		motionY *= 0.99000000953674316D;
		motionZ *= 0.99000000953674316D;
		if (onGround) {
			motionX *= 0.69999998807907104D;
			motionZ *= 0.69999998807907104D;
		}
	}
}
