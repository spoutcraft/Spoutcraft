package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public abstract class Render {
	protected RenderManager renderManager;
	private ModelBase modelBase;
	protected RenderBlocks renderBlocks;
	protected float shadowSize;
	protected float shadowOpaque;

	public Render() {
		modelBase = new ModelBiped();
		renderBlocks = new RenderBlocks();
		shadowSize = 0.0F;
		shadowOpaque = 1.0F;
	}

	public abstract void doRender(Entity entity, double d, double d1, double d2,
	        float f, float f1);

	protected void loadTexture(String s) {
		RenderEngine renderengine = renderManager.renderEngine;
		renderengine.bindTexture(renderengine.getTexture(s));
	}

	protected boolean loadDownloadableImageTexture(String s, String s1) {
		RenderEngine renderengine = renderManager.renderEngine;
		int i = renderengine.getTextureForDownloadableImage(s, s1);
		if (i >= 0) {
			renderengine.bindTexture(i);
			return true;
		}
		else {
			return false;
		}
	}

	private void renderEntityOnFire(Entity entity, double d, double d1, double d2,
	        float f) {
		GL11.glDisable(2896 /*GL_LIGHTING*/);
		int i = Block.fire.blockIndexInTexture;
		int j = (i & 0xf) << 4;
		int k = i & 0xf0;
		float f1 = (float)j / 256F;
		float f3 = ((float)j + 15.99F) / 256F;
		float f5 = (float)k / 256F;
		float f7 = ((float)k + 15.99F) / 256F;
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d, (float)d1, (float)d2);
		float f9 = entity.width * 1.4F;
		GL11.glScalef(f9, f9, f9);
		loadTexture("/terrain.png");
		Tessellator tessellator = Tessellator.instance;
		float f10 = 0.5F;
		float f11 = 0.0F;
		float f12 = entity.height / f9;
		float f13 = (float)(entity.posY - entity.boundingBox.minY);
		GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(0.0F, 0.0F, -0.3F + (float)(int)f12 * 0.02F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float f14 = 0.0F;
		int l = 0;
		tessellator.startDrawingQuads();
		while (f12 > 0.0F) {
			float f2;
			float f4;
			float f6;
			float f8;
			if (l % 2 == 0) {
				f2 = (float)j / 256F;
				f4 = ((float)j + 15.99F) / 256F;
				f6 = (float)k / 256F;
				f8 = ((float)k + 15.99F) / 256F;
			}
			else {
				f2 = (float)j / 256F;
				f4 = ((float)j + 15.99F) / 256F;
				f6 = (float)(k + 16) / 256F;
				f8 = ((float)(k + 16) + 15.99F) / 256F;
			}
			if ((l / 2) % 2 == 0) {
				float f15 = f4;
				f4 = f2;
				f2 = f15;
			}
			tessellator.addVertexWithUV(f10 - f11, 0.0F - f13, f14, f4, f8);
			tessellator.addVertexWithUV(-f10 - f11, 0.0F - f13, f14, f2, f8);
			tessellator.addVertexWithUV(-f10 - f11, 1.4F - f13, f14, f2, f6);
			tessellator.addVertexWithUV(f10 - f11, 1.4F - f13, f14, f4, f6);
			f12 -= 0.45F;
			f13 -= 0.45F;
			f10 *= 0.9F;
			f14 += 0.03F;
			l++;
		}
		tessellator.draw();
		GL11.glPopMatrix();
		GL11.glEnable(2896 /*GL_LIGHTING*/);
	}

	private void renderShadow(Entity entity, double d, double d1, double d2,
	        float f, float f1) {
		GL11.glEnable(3042 /*GL_BLEND*/);
		GL11.glBlendFunc(770, 771);
		RenderEngine renderengine = renderManager.renderEngine;
		renderengine.bindTexture(renderengine.getTexture("%clamp%/misc/shadow.png"));
		World world = getWorldFromRenderManager();
		GL11.glDepthMask(false);
		float f2 = shadowSize;
		if (entity instanceof EntityLiving) {
			EntityLiving entityliving = (EntityLiving)entity;
			f2 *= entityliving.func_35159_aC();
			if (entityliving instanceof EntityAnimal) {
				EntityAnimal entityanimal = (EntityAnimal)entityliving;
				if (entityanimal.isChild()) {
					f2 *= 0.5F;
				}
			}
		}
		double d3 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)f1;
		double d4 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)f1 + (double)entity.getShadowSize();
		double d5 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)f1;
		int i = MathHelper.floor_double(d3 - (double)f2);
		int j = MathHelper.floor_double(d3 + (double)f2);
		int k = MathHelper.floor_double(d4 - (double)f2);
		int l = MathHelper.floor_double(d4);
		int i1 = MathHelper.floor_double(d5 - (double)f2);
		int j1 = MathHelper.floor_double(d5 + (double)f2);
		double d6 = d - d3;
		double d7 = d1 - d4;
		double d8 = d2 - d5;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		for (int k1 = i; k1 <= j; k1++) {
			for (int l1 = k; l1 <= l; l1++) {
				for (int i2 = i1; i2 <= j1; i2++) {
					int j2 = world.getBlockId(k1, l1 - 1, i2);
					if (j2 > 0 && world.getBlockLightValue(k1, l1, i2) > 3) {
						renderShadowOnBlock(Block.blocksList[j2], d, d1 + (double)entity.getShadowSize(), d2, k1, l1, i2, f, f2, d6, d7 + (double)entity.getShadowSize(), d8);
					}
				}
			}
		}

		tessellator.draw();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(3042 /*GL_BLEND*/);
		GL11.glDepthMask(true);
	}

	private World getWorldFromRenderManager() {
		return renderManager.worldObj;
	}

	private void renderShadowOnBlock(Block block, double d, double d1, double d2,
	        int i, int j, int k, float f, float f1, double d3,
	        double d4, double d5) {
		Tessellator tessellator = Tessellator.instance;
		if (!block.renderAsNormalBlock()) {
			return;
		}
		double d6 = ((double)f - (d1 - ((double)j + d4)) / 2D) * 0.5D * (double)getWorldFromRenderManager().getLightBrightness(i, j, k);
		if (d6 < 0.0D) {
			return;
		}
		if (d6 > 1.0D) {
			d6 = 1.0D;
		}
		tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, (float)d6);
		double d7 = (double)i + block.minX + d3;
		double d8 = (double)i + block.maxX + d3;
		double d9 = (double)j + block.minY + d4 + 0.015625D;
		double d10 = (double)k + block.minZ + d5;
		double d11 = (double)k + block.maxZ + d5;
		float f2 = (float)((d - d7) / 2D / (double)f1 + 0.5D);
		float f3 = (float)((d - d8) / 2D / (double)f1 + 0.5D);
		float f4 = (float)((d2 - d10) / 2D / (double)f1 + 0.5D);
		float f5 = (float)((d2 - d11) / 2D / (double)f1 + 0.5D);
		tessellator.addVertexWithUV(d7, d9, d10, f2, f4);
		tessellator.addVertexWithUV(d7, d9, d11, f2, f5);
		tessellator.addVertexWithUV(d8, d9, d11, f3, f5);
		tessellator.addVertexWithUV(d8, d9, d10, f3, f4);
	}

	public static void renderOffsetAABB(AxisAlignedBB axisalignedbb, double d, double d1, double d2) {
		GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
		Tessellator tessellator = Tessellator.instance;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		tessellator.startDrawingQuads();
		tessellator.setTranslationD(d, d1, d2);
		tessellator.setNormal(0.0F, 0.0F, -1F);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.setNormal(0.0F, -1F, 0.0F);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.setNormal(-1F, 0.0F, 0.0F);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.setTranslationD(0.0D, 0.0D, 0.0D);
		tessellator.draw();
		GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
	}

	public static void renderAABB(AxisAlignedBB axisalignedbb) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
		tessellator.addVertex(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
		tessellator.draw();
	}

	public void setRenderManager(RenderManager rendermanager) {
		renderManager = rendermanager;
	}

	public void doRenderShadowAndFire(Entity entity, double d, double d1, double d2,
	        float f, float f1) {
		if (renderManager.options.fancyGraphics && shadowSize > 0.0F) {
			double d3 = renderManager.getDistanceToCamera(entity.posX, entity.posY, entity.posZ);
			float f2 = (float)((1.0D - d3 / 256D) * (double)shadowOpaque);
			if (f2 > 0.0F) {
				renderShadow(entity, d, d1, d2, f2, f1);
			}
		}
		if (entity.isBurning()) {
			renderEntityOnFire(entity, d, d1, d2, f1);
		}
	}

	public FontRenderer getFontRendererFromRenderManager() {
		return renderManager.getFontRenderer();
	}
}
