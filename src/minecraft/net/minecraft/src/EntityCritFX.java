package net.minecraft.src;

public class EntityCritFX extends EntityFX {
	private boolean field_35136_ay;
	float field_35137_a;

	public EntityCritFX(World world, double d, double d1, double d2,
	        double d3, double d4, double d5) {
		this(world, d, d1, d2, d3, d4, d5, 1.0F);
	}

	public EntityCritFX(World world, double d, double d1, double d2,
	        double d3, double d4, double d5, float f) {
		super(world, d, d1, d2, 0.0D, 0.0D, 0.0D);
		field_35136_ay = true;
		motionX *= 0.10000000149011612D;
		motionY *= 0.10000000149011612D;
		motionZ *= 0.10000000149011612D;
		motionX += d3 * 0.40000000000000002D;
		motionY += d4 * 0.40000000000000002D;
		motionZ += d5 * 0.40000000000000002D;
		particleRed = particleGreen = particleBlue = (float)(Math.random() * 0.30000001192092896D + 0.60000002384185791D);
		particleScale *= 0.75F;
		particleScale *= f;
		field_35137_a = particleScale;
		particleMaxAge = (int)(6D / (Math.random() * 0.80000000000000004D + 0.59999999999999998D));
		particleMaxAge *= f;
		noClip = false;
		setParticleTextureIndex(65);
		onUpdate();
	}

	public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
		if (!field_35136_ay) {
			return;
		}
		float f6 = (((float)particleAge + f) / (float)particleMaxAge) * 32F;
		if (f6 < 0.0F) {
			f6 = 0.0F;
		}
		if (f6 > 1.0F) {
			f6 = 1.0F;
		}
		particleScale = field_35137_a * f6;
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
		particleGreen *= 0.95999999999999996D;
		particleBlue *= 0.90000000000000002D;
		motionX *= 0.69999998807907104D;
		motionY *= 0.69999998807907104D;
		motionZ *= 0.69999998807907104D;
		motionY -= 0.019999999552965164D;
		if (onGround) {
			motionX *= 0.69999998807907104D;
			motionZ *= 0.69999998807907104D;
		}
	}
}
