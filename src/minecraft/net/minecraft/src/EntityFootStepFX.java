package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class EntityFootStepFX extends EntityFX {
	private int field_27018_a;
	private int field_27020_o;
	private RenderEngine currentFootSteps;

	public EntityFootStepFX(RenderEngine renderengine, World world, double d, double d1, double d2) {
		super(world, d, d1, d2, 0.0D, 0.0D, 0.0D);
		field_27018_a = 0;
		field_27020_o = 0;
		currentFootSteps = renderengine;
		motionX = motionY = motionZ = 0.0D;
		field_27020_o = 200;
	}

	public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
		float f6 = ((float)field_27018_a + f) / (float)field_27020_o;
		f6 *= f6;
		float f7 = 2.0F - f6 * 2.0F;
		if (f7 > 1.0F) {
			f7 = 1.0F;
		}
		f7 *= 0.2F;
		GL11.glDisable(2896 /*GL_LIGHTING*/);
		float f8 = 0.125F;
		float f9 = (float)(posX - interpPosX);
		float f10 = (float)(posY - interpPosY);
		float f11 = (float)(posZ - interpPosZ);
		float f12 = worldObj.getLightBrightness(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ));
		currentFootSteps.bindTexture(currentFootSteps.getTexture("/misc/footprint.png"));
		GL11.glEnable(3042 /*GL_BLEND*/);
		GL11.glBlendFunc(770, 771);
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_F(f12, f12, f12, f7);
		tessellator.addVertexWithUV(f9 - f8, f10, f11 + f8, 0.0D, 1.0D);
		tessellator.addVertexWithUV(f9 + f8, f10, f11 + f8, 1.0D, 1.0D);
		tessellator.addVertexWithUV(f9 + f8, f10, f11 - f8, 1.0D, 0.0D);
		tessellator.addVertexWithUV(f9 - f8, f10, f11 - f8, 0.0D, 0.0D);
		tessellator.draw();
		GL11.glDisable(3042 /*GL_BLEND*/);
		GL11.glEnable(2896 /*GL_LIGHTING*/);
	}

	public void onUpdate() {
		field_27018_a++;
		if (field_27018_a == field_27020_o) {
			setEntityDead();
		}
	}

	public int getFXLayer() {
		return 3;
	}
}
