package net.minecraft.src;

public class EntitySpellParticleFX extends EntityFX {
	private int field_40111_a;

	public EntitySpellParticleFX(World world, double d, double d1, double d2,
	        double d3, double d4, double d5) {
		super(world, d, d1, d2, d3, d4, d5);
		field_40111_a = 128;
		motionY *= 0.20000000298023224D;
		if (d3 == 0.0D && d5 == 0.0D) {
			motionX *= 0.10000000149011612D;
			motionZ *= 0.10000000149011612D;
		}
		particleScale *= 0.75F;
		particleMaxAge = (int)(8D / (Math.random() * 0.80000000000000004D + 0.20000000000000001D));
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
		super.renderParticle(tessellator, f, f1, f2, f3, f4, f5);
	}

	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		if (particleAge++ >= particleMaxAge) {
			setEntityDead();
		}
		setParticleTextureIndex(field_40111_a + (7 - (particleAge * 8) / particleMaxAge));
		motionY += 0.0040000000000000001D;
		moveEntity(motionX, motionY, motionZ);
		if (posY == prevPosY) {
			motionX *= 1.1000000000000001D;
			motionZ *= 1.1000000000000001D;
		}
		motionX *= 0.95999997854232788D;
		motionY *= 0.95999997854232788D;
		motionZ *= 0.95999997854232788D;
		if (onGround) {
			motionX *= 0.69999998807907104D;
			motionZ *= 0.69999998807907104D;
		}
	}

	public void func_40110_b(int i) {
		field_40111_a = i;
	}
}
