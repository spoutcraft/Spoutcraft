package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderFireball extends Render {
	private float field_40269_a;

	public RenderFireball(float f) {
		field_40269_a = f;
	}

	public void doRenderFireball(EntityFireball entityfireball, double d, double d1, double d2,
	        float f, float f1) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d, (float)d1, (float)d2);
		GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
		float f2 = field_40269_a;
		GL11.glScalef(f2 / 1.0F, f2 / 1.0F, f2 / 1.0F);
		byte byte0 = 46;
		loadTexture("/gui/items.png");
		Tessellator tessellator = Tessellator.instance;
		float f3 = (float)((byte0 % 16) * 16 + 0) / 256F;
		float f4 = (float)((byte0 % 16) * 16 + 16) / 256F;
		float f5 = (float)((byte0 / 16) * 16 + 0) / 256F;
		float f6 = (float)((byte0 / 16) * 16 + 16) / 256F;
		float f7 = 1.0F;
		float f8 = 0.5F;
		float f9 = 0.25F;
		GL11.glRotatef(180F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		tessellator.addVertexWithUV(0.0F - f8, 0.0F - f9, 0.0D, f3, f6);
		tessellator.addVertexWithUV(f7 - f8, 0.0F - f9, 0.0D, f4, f6);
		tessellator.addVertexWithUV(f7 - f8, 1.0F - f9, 0.0D, f4, f5);
		tessellator.addVertexWithUV(0.0F - f8, 1.0F - f9, 0.0D, f3, f5);
		tessellator.draw();
		GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
		GL11.glPopMatrix();
	}

	public void doRender(Entity entity, double d, double d1, double d2,
	        float f, float f1) {
		doRenderFireball((EntityFireball)entity, d, d1, d2, f, f1);
	}
}
