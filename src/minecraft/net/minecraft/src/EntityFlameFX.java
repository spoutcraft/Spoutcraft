package net.minecraft.src;

import java.util.Random;

public class EntityFlameFX extends EntityFX {
	private float field_672_a;

	public EntityFlameFX(World world, double d, double d1, double d2,
	        double d3, double d4, double d5) {
		super(world, d, d1, d2, d3, d4, d5);
		motionX = motionX * 0.0099999997764825821D + d3;
		motionY = motionY * 0.0099999997764825821D + d4;
		motionZ = motionZ * 0.0099999997764825821D + d5;
		d += (rand.nextFloat() - rand.nextFloat()) * 0.05F;
		d1 += (rand.nextFloat() - rand.nextFloat()) * 0.05F;
		d2 += (rand.nextFloat() - rand.nextFloat()) * 0.05F;
		field_672_a = particleScale;
		particleRed = particleGreen = particleBlue = 1.0F;
		particleMaxAge = (int)(8D / (Math.random() * 0.80000000000000004D + 0.20000000000000001D)) + 4;
		noClip = true;
		setParticleTextureIndex(48);
	}

	public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
		float f6 = ((float)particleAge + f) / (float)particleMaxAge;
		particleScale = field_672_a * (1.0F - f6 * f6 * 0.5F);
		super.renderParticle(tessellator, f, f1, f2, f3, f4, f5);
	}

	public int getEntityBrightnessForRender(float f) {
		float f1 = ((float)particleAge + f) / (float)particleMaxAge;
		if (f1 < 0.0F) {
			f1 = 0.0F;
		}
		if (f1 > 1.0F) {
			f1 = 1.0F;
		}
		int i = super.getEntityBrightnessForRender(f);
		int j = i & 0xff;
		int k = i >> 16 & 0xff;
		j += (int)(f1 * 15F * 16F);
		if (j > 240) {
			j = 240;
		}
		return j | k << 16;
	}

	public float getEntityBrightness(float f) {
		float f1 = ((float)particleAge + f) / (float)particleMaxAge;
		if (f1 < 0.0F) {
			f1 = 0.0F;
		}
		if (f1 > 1.0F) {
			f1 = 1.0F;
		}
		float f2 = super.getEntityBrightness(f);
		return f2 * f1 + (1.0F - f1);
	}

	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		if (particleAge++ >= particleMaxAge) {
			setEntityDead();
		}
		moveEntity(motionX, motionY, motionZ);
		motionX *= 0.95999997854232788D;
		motionY *= 0.95999997854232788D;
		motionZ *= 0.95999997854232788D;
		if (onGround) {
			motionX *= 0.69999998807907104D;
			motionZ *= 0.69999998807907104D;
		}
	}
}
