package net.minecraft.src;

import java.util.Random;

public class EntityEnchantmentTableParticleFX extends EntityFX {
	private float field_40107_a;
	private double field_40109_aw;
	private double field_40108_ax;
	private double field_40106_ay;

	public EntityEnchantmentTableParticleFX(World world, double d, double d1, double d2,
	        double d3, double d4, double d5) {
		super(world, d, d1, d2, d3, d4, d5);
		motionX = d3;
		motionY = d4;
		motionZ = d5;
		field_40109_aw = posX = d;
		field_40108_ax = posY = d1;
		field_40106_ay = posZ = d2;
		float f = rand.nextFloat() * 0.6F + 0.4F;
		field_40107_a = particleScale = rand.nextFloat() * 0.5F + 0.2F;
		particleRed = particleGreen = particleBlue = 1.0F * f;
		particleGreen *= 0.9F;
		particleRed *= 0.9F;
		particleMaxAge = (int)(Math.random() * 10D) + 30;
		noClip = true;
		setParticleTextureIndex((int)(Math.random() * 26D + 1.0D + 224D));
	}

	public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
		super.renderParticle(tessellator, f, f1, f2, f3, f4, f5);
	}

	public int getEntityBrightnessForRender(float f) {
		int i = super.getEntityBrightnessForRender(f);
		float f1 = (float)particleAge / (float)particleMaxAge;
		f1 *= f1;
		f1 *= f1;
		int j = i & 0xff;
		int k = i >> 16 & 0xff;
		k += (int)(f1 * 15F * 16F);
		if (k > 240) {
			k = 240;
		}
		return j | k << 16;
	}

	public float getEntityBrightness(float f) {
		float f1 = super.getEntityBrightness(f);
		float f2 = (float)particleAge / (float)particleMaxAge;
		f2 *= f2;
		f2 *= f2;
		return f1 * (1.0F - f2) + f2;
	}

	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		float f = (float)particleAge / (float)particleMaxAge;
		f = 1.0F - f;
		float f1 = 1.0F - f;
		f1 *= f1;
		f1 *= f1;
		posX = field_40109_aw + motionX * (double)f;
		posY = (field_40108_ax + motionY * (double)f) - (double)(f1 * 1.2F);
		posZ = field_40106_ay + motionZ * (double)f;
		if (particleAge++ >= particleMaxAge) {
			setEntityDead();
		}
	}
}
