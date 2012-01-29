package net.minecraft.src;

public class EntityBreakingFX extends EntityFX {
	public EntityBreakingFX(World world, double d, double d1, double d2,
	        Item item) {
		super(world, d, d1, d2, 0.0D, 0.0D, 0.0D);
		setParticleTextureIndex(item.getIconFromDamage(0));
		particleRed = particleGreen = particleBlue = 1.0F;
		particleGravity = Block.blockSnow.blockParticleGravity;
		particleScale /= 2.0F;
	}

	public EntityBreakingFX(World world, double d, double d1, double d2,
	        double d3, double d4, double d5, Item item) {
		this(world, d, d1, d2, item);
		motionX *= 0.10000000149011612D;
		motionY *= 0.10000000149011612D;
		motionZ *= 0.10000000149011612D;
		motionX += d3;
		motionY += d4;
		motionZ += d5;
	}

	public int getFXLayer() {
		return 2;
	}

	public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
		float f6 = ((float)(getParticleTextureIndex() % 16) + particleTextureJitterX / 4F) / 16F;
		float f7 = f6 + 0.01560938F;
		float f8 = ((float)(getParticleTextureIndex() / 16) + particleTextureJitterY / 4F) / 16F;
		float f9 = f8 + 0.01560938F;
		float f10 = 0.1F * particleScale;
		float f11 = (float)((prevPosX + (posX - prevPosX) * (double)f) - interpPosX);
		float f12 = (float)((prevPosY + (posY - prevPosY) * (double)f) - interpPosY);
		float f13 = (float)((prevPosZ + (posZ - prevPosZ) * (double)f) - interpPosZ);
		float f14 = 1.0F;
		tessellator.setColorOpaque_F(f14 * particleRed, f14 * particleGreen, f14 * particleBlue);
		tessellator.addVertexWithUV(f11 - f1 * f10 - f4 * f10, f12 - f2 * f10, f13 - f3 * f10 - f5 * f10, f6, f9);
		tessellator.addVertexWithUV((f11 - f1 * f10) + f4 * f10, f12 + f2 * f10, (f13 - f3 * f10) + f5 * f10, f6, f8);
		tessellator.addVertexWithUV(f11 + f1 * f10 + f4 * f10, f12 + f2 * f10, f13 + f3 * f10 + f5 * f10, f7, f8);
		tessellator.addVertexWithUV((f11 + f1 * f10) - f4 * f10, f12 - f2 * f10, (f13 + f3 * f10) - f5 * f10, f7, f9);
	}
}
