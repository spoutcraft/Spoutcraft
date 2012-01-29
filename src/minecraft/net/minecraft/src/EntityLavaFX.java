package net.minecraft.src;

import java.util.Random;

public class EntityLavaFX extends EntityFX {
	private float lavaParticleScale;

	public EntityLavaFX(World world, double d, double d1, double d2) {
		super(world, d, d1, d2, 0.0D, 0.0D, 0.0D);
		motionX *= 0.80000001192092896D;
		motionY *= 0.80000001192092896D;
		motionZ *= 0.80000001192092896D;
		motionY = rand.nextFloat() * 0.4F + 0.05F;
		particleRed = particleGreen = particleBlue = 1.0F;
		particleScale *= rand.nextFloat() * 2.0F + 0.2F;
		lavaParticleScale = particleScale;
		particleMaxAge = (int)(16D / (Math.random() * 0.80000000000000004D + 0.20000000000000001D));
		noClip = false;
		setParticleTextureIndex(49);
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
		char c = '\360';
		int j = i >> 16 & 0xff;
		return c | j << 16;
	}

	public float getEntityBrightness(float f) {
		return 1.0F;
	}

	public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
		float f6 = ((float)particleAge + f) / (float)particleMaxAge;
		particleScale = lavaParticleScale * (1.0F - f6 * f6);
		super.renderParticle(tessellator, f, f1, f2, f3, f4, f5);
	}

	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		if (particleAge++ >= particleMaxAge) {
			setEntityDead();
		}
		float f = (float)particleAge / (float)particleMaxAge;
		if (rand.nextFloat() > f) {
			worldObj.spawnParticle("smoke", posX, posY, posZ, motionX, motionY, motionZ);
		}
		motionY -= 0.029999999999999999D;
		moveEntity(motionX, motionY, motionZ);
		motionX *= 0.99900001287460327D;
		motionY *= 0.99900001287460327D;
		motionZ *= 0.99900001287460327D;
		if (onGround) {
			motionX *= 0.69999998807907104D;
			motionZ *= 0.69999998807907104D;
		}
	}
}
