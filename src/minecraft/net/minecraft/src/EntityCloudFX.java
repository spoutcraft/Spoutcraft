package net.minecraft.src;

public class EntityCloudFX extends EntityFX {
	float field_35135_a;

	public EntityCloudFX(World world, double d, double d1, double d2,
	        double d3, double d4, double d5) {
		super(world, d, d1, d2, 0.0D, 0.0D, 0.0D);
		float f = 2.5F;
		motionX *= 0.10000000149011612D;
		motionY *= 0.10000000149011612D;
		motionZ *= 0.10000000149011612D;
		motionX += d3;
		motionY += d4;
		motionZ += d5;
		particleRed = particleGreen = particleBlue = 1.0F - (float)(Math.random() * 0.30000001192092896D);
		particleScale *= 0.75F;
		particleScale *= f;
		field_35135_a = particleScale;
		particleMaxAge = (int)(8D / (Math.random() * 0.80000000000000004D + 0.29999999999999999D));
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
		particleScale = field_35135_a * f6;
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
		moveEntity(motionX, motionY, motionZ);
		motionX *= 0.95999997854232788D;
		motionY *= 0.95999997854232788D;
		motionZ *= 0.95999997854232788D;
		EntityPlayer entityplayer = worldObj.getClosestPlayerToEntity(this, 2D);
		if (entityplayer != null && posY > entityplayer.boundingBox.minY) {
			posY += (entityplayer.boundingBox.minY - posY) * 0.20000000000000001D;
			motionY += (entityplayer.motionY - motionY) * 0.20000000000000001D;
			setPosition(posX, posY, posZ);
		}
		if (onGround) {
			motionX *= 0.69999998807907104D;
			motionZ *= 0.69999998807907104D;
		}
	}
}
