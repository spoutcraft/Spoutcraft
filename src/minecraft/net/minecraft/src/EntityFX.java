package net.minecraft.src;

import java.util.Random;

public class EntityFX extends Entity {
	private int particleTextureIndex;
	protected float particleTextureJitterX;
	protected float particleTextureJitterY;
	protected int particleAge;
	protected int particleMaxAge;
	protected float particleScale;
	protected float particleGravity;
	protected float particleRed;
	protected float particleGreen;
	protected float particleBlue;
	public static double interpPosX;
	public static double interpPosY;
	public static double interpPosZ;

	public EntityFX(World world, double d, double d1, double d2,
	        double d3, double d4, double d5) {
		super(world);
		particleAge = 0;
		particleMaxAge = 0;
		setSize(0.2F, 0.2F);
		yOffset = height / 2.0F;
		setPosition(d, d1, d2);
		particleRed = particleGreen = particleBlue = 1.0F;
		motionX = d3 + (double)((float)(Math.random() * 2D - 1.0D) * 0.4F);
		motionY = d4 + (double)((float)(Math.random() * 2D - 1.0D) * 0.4F);
		motionZ = d5 + (double)((float)(Math.random() * 2D - 1.0D) * 0.4F);
		float f = (float)(Math.random() + Math.random() + 1.0D) * 0.15F;
		float f1 = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
		motionX = (motionX / (double)f1) * (double)f * 0.40000000596046448D;
		motionY = (motionY / (double)f1) * (double)f * 0.40000000596046448D + 0.10000000149011612D;
		motionZ = (motionZ / (double)f1) * (double)f * 0.40000000596046448D;
		particleTextureJitterX = rand.nextFloat() * 3F;
		particleTextureJitterY = rand.nextFloat() * 3F;
		particleScale = (rand.nextFloat() * 0.5F + 0.5F) * 2.0F;
		particleMaxAge = (int)(4F / (rand.nextFloat() * 0.9F + 0.1F));
		particleAge = 0;
	}

	public EntityFX multiplyVelocity(float f) {
		motionX *= f;
		motionY = (motionY - 0.10000000149011612D) * (double)f + 0.10000000149011612D;
		motionZ *= f;
		return this;
	}

	public EntityFX func_405_d(float f) {
		setSize(0.2F * f, 0.2F * f);
		particleScale *= f;
		return this;
	}

	public void func_40097_b(float f, float f1, float f2) {
		particleRed = f;
		particleGreen = f1;
		particleBlue = f2;
	}

	public float func_40098_n() {
		return particleRed;
	}

	public float func_40101_o() {
		return particleGreen;
	}

	public float func_40102_p() {
		return particleBlue;
	}

	protected boolean canTriggerWalking() {
		return false;
	}

	protected void entityInit() {
	}

	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		if (particleAge++ >= particleMaxAge) {
			setEntityDead();
		}
		motionY -= 0.040000000000000001D * (double)particleGravity;
		moveEntity(motionX, motionY, motionZ);
		motionX *= 0.98000001907348633D;
		motionY *= 0.98000001907348633D;
		motionZ *= 0.98000001907348633D;
		if (onGround) {
			motionX *= 0.69999998807907104D;
			motionZ *= 0.69999998807907104D;
		}
	}

	public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
		float f6 = (float)(particleTextureIndex % 16) / 16F;
		float f7 = f6 + 0.0624375F;
		float f8 = (float)(particleTextureIndex / 16) / 16F;
		float f9 = f8 + 0.0624375F;
		float f10 = 0.1F * particleScale;
		float f11 = (float)((prevPosX + (posX - prevPosX) * (double)f) - interpPosX);
		float f12 = (float)((prevPosY + (posY - prevPosY) * (double)f) - interpPosY);
		float f13 = (float)((prevPosZ + (posZ - prevPosZ) * (double)f) - interpPosZ);
		float f14 = 1.0F;
		tessellator.setColorOpaque_F(particleRed * f14, particleGreen * f14, particleBlue * f14);
		tessellator.addVertexWithUV(f11 - f1 * f10 - f4 * f10, f12 - f2 * f10, f13 - f3 * f10 - f5 * f10, f7, f9);
		tessellator.addVertexWithUV((f11 - f1 * f10) + f4 * f10, f12 + f2 * f10, (f13 - f3 * f10) + f5 * f10, f7, f8);
		tessellator.addVertexWithUV(f11 + f1 * f10 + f4 * f10, f12 + f2 * f10, f13 + f3 * f10 + f5 * f10, f6, f8);
		tessellator.addVertexWithUV((f11 + f1 * f10) - f4 * f10, f12 - f2 * f10, (f13 + f3 * f10) - f5 * f10, f6, f9);
	}

	public int getFXLayer() {
		return 0;
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
	}

	public void setParticleTextureIndex(int i) {
		particleTextureIndex = i;
	}

	public int getParticleTextureIndex() {
		return particleTextureIndex;
	}
}
