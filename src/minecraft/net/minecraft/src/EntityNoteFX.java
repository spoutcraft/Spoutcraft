package net.minecraft.src;

public class EntityNoteFX extends EntityFX {
	float noteParticleScale;

	public EntityNoteFX(World world, double d, double d1, double d2,
	        double d3, double d4, double d5) {
		this(world, d, d1, d2, d3, d4, d5, 2.0F);
	}

	public EntityNoteFX(World world, double d, double d1, double d2,
	        double d3, double d4, double d5, float f) {
		super(world, d, d1, d2, 0.0D, 0.0D, 0.0D);
		motionX *= 0.0099999997764825821D;
		motionY *= 0.0099999997764825821D;
		motionZ *= 0.0099999997764825821D;
		motionY += 0.20000000000000001D;
		particleRed = MathHelper.sin(((float)d3 + 0.0F) * 3.141593F * 2.0F) * 0.65F + 0.35F;
		particleGreen = MathHelper.sin(((float)d3 + 0.3333333F) * 3.141593F * 2.0F) * 0.65F + 0.35F;
		particleBlue = MathHelper.sin(((float)d3 + 0.6666667F) * 3.141593F * 2.0F) * 0.65F + 0.35F;
		particleScale *= 0.75F;
		particleScale *= f;
		noteParticleScale = particleScale;
		particleMaxAge = 6;
		noClip = false;
		setParticleTextureIndex(64);
	}

	public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
		float f6 = (((float)particleAge + f) / (float)particleMaxAge) * 32F;
		if (f6 < 0.0F) {
			f6 = 0.0F;
		}
		if (f6 > 1.0F) {
			f6 = 1.0F;
		}
		particleScale = noteParticleScale * f6;
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
		motionX *= 0.6600000262260437D;
		motionY *= 0.6600000262260437D;
		motionZ *= 0.6600000262260437D;
		if (onGround) {
			motionX *= 0.69999998807907104D;
			motionZ *= 0.69999998807907104D;
		}
	}
}
