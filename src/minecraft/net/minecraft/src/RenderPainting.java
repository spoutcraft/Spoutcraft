package net.minecraft.src;

import java.util.Random;
import org.lwjgl.opengl.GL11;

public class RenderPainting extends Render {
	private Random rand;

	public RenderPainting() {
		rand = new Random();
	}

	public void func_158_a(EntityPainting entitypainting, double d, double d1, double d2,
	        float f, float f1) {
		rand.setSeed(187L);
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d, (float)d1, (float)d2);
		GL11.glRotatef(f, 0.0F, 1.0F, 0.0F);
		GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
		loadTexture("/art/kz.png");
		EnumArt enumart = entitypainting.art;
		float f2 = 0.0625F;
		GL11.glScalef(f2, f2, f2);
		func_159_a(entitypainting, enumart.sizeX, enumart.sizeY, enumart.offsetX, enumart.offsetY);
		GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
		GL11.glPopMatrix();
	}

	private void func_159_a(EntityPainting entitypainting, int i, int j, int k, int l) {
		float f = (float)(-i) / 2.0F;
		float f1 = (float)(-j) / 2.0F;
		float f2 = -0.5F;
		float f3 = 0.5F;
		for (int i1 = 0; i1 < i / 16; i1++) {
			for (int j1 = 0; j1 < j / 16; j1++) {
				float f4 = f + (float)((i1 + 1) * 16);
				float f5 = f + (float)(i1 * 16);
				float f6 = f1 + (float)((j1 + 1) * 16);
				float f7 = f1 + (float)(j1 * 16);
				func_160_a(entitypainting, (f4 + f5) / 2.0F, (f6 + f7) / 2.0F);
				float f8 = (float)((k + i) - i1 * 16) / 256F;
				float f9 = (float)((k + i) - (i1 + 1) * 16) / 256F;
				float f10 = (float)((l + j) - j1 * 16) / 256F;
				float f11 = (float)((l + j) - (j1 + 1) * 16) / 256F;
				float f12 = 0.75F;
				float f13 = 0.8125F;
				float f14 = 0.0F;
				float f15 = 0.0625F;
				float f16 = 0.75F;
				float f17 = 0.8125F;
				float f18 = 0.001953125F;
				float f19 = 0.001953125F;
				float f20 = 0.7519531F;
				float f21 = 0.7519531F;
				float f22 = 0.0F;
				float f23 = 0.0625F;
				Tessellator tessellator = Tessellator.instance;
				tessellator.startDrawingQuads();
				tessellator.setNormal(0.0F, 0.0F, -1F);
				tessellator.addVertexWithUV(f4, f7, f2, f9, f10);
				tessellator.addVertexWithUV(f5, f7, f2, f8, f10);
				tessellator.addVertexWithUV(f5, f6, f2, f8, f11);
				tessellator.addVertexWithUV(f4, f6, f2, f9, f11);
				tessellator.setNormal(0.0F, 0.0F, 1.0F);
				tessellator.addVertexWithUV(f4, f6, f3, f12, f14);
				tessellator.addVertexWithUV(f5, f6, f3, f13, f14);
				tessellator.addVertexWithUV(f5, f7, f3, f13, f15);
				tessellator.addVertexWithUV(f4, f7, f3, f12, f15);
				tessellator.setNormal(0.0F, 1.0F, 0.0F);
				tessellator.addVertexWithUV(f4, f6, f2, f16, f18);
				tessellator.addVertexWithUV(f5, f6, f2, f17, f18);
				tessellator.addVertexWithUV(f5, f6, f3, f17, f19);
				tessellator.addVertexWithUV(f4, f6, f3, f16, f19);
				tessellator.setNormal(0.0F, -1F, 0.0F);
				tessellator.addVertexWithUV(f4, f7, f3, f16, f18);
				tessellator.addVertexWithUV(f5, f7, f3, f17, f18);
				tessellator.addVertexWithUV(f5, f7, f2, f17, f19);
				tessellator.addVertexWithUV(f4, f7, f2, f16, f19);
				tessellator.setNormal(-1F, 0.0F, 0.0F);
				tessellator.addVertexWithUV(f4, f6, f3, f21, f22);
				tessellator.addVertexWithUV(f4, f7, f3, f21, f23);
				tessellator.addVertexWithUV(f4, f7, f2, f20, f23);
				tessellator.addVertexWithUV(f4, f6, f2, f20, f22);
				tessellator.setNormal(1.0F, 0.0F, 0.0F);
				tessellator.addVertexWithUV(f5, f6, f2, f21, f22);
				tessellator.addVertexWithUV(f5, f7, f2, f21, f23);
				tessellator.addVertexWithUV(f5, f7, f3, f20, f23);
				tessellator.addVertexWithUV(f5, f6, f3, f20, f22);
				tessellator.draw();
			}
		}
	}

	private void func_160_a(EntityPainting entitypainting, float f, float f1) {
		int i = MathHelper.floor_double(entitypainting.posX);
		int j = MathHelper.floor_double(entitypainting.posY + (double)(f1 / 16F));
		int k = MathHelper.floor_double(entitypainting.posZ);
		if (entitypainting.direction == 0) {
			i = MathHelper.floor_double(entitypainting.posX + (double)(f / 16F));
		}
		if (entitypainting.direction == 1) {
			k = MathHelper.floor_double(entitypainting.posZ - (double)(f / 16F));
		}
		if (entitypainting.direction == 2) {
			i = MathHelper.floor_double(entitypainting.posX - (double)(f / 16F));
		}
		if (entitypainting.direction == 3) {
			k = MathHelper.floor_double(entitypainting.posZ + (double)(f / 16F));
		}
		int l = renderManager.worldObj.getLightBrightnessForSkyBlocks(i, j, k, 0);
		int i1 = l % 0x10000;
		int j1 = l / 0x10000;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapEnabled, i1, j1);
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
	}

	public void doRender(Entity entity, double d, double d1, double d2,
	        float f, float f1) {
		func_158_a((EntityPainting)entity, d, d1, d2, f, f1);
	}
}
