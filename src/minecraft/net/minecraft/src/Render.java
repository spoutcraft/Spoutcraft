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

	public abstract void doRender(Entity entity, double d, double d1, double d2, float f, float f1);

	protected void loadTexture(String par1Str) {
		RenderEngine renderengine = renderManager.renderEngine;
		renderengine.bindTexture(renderengine.getTexture(par1Str));
	}

	public boolean loadDownloadableImageTexture(String par1Str, String par2Str) { //Spout: changed protected to public
		RenderEngine renderengine = renderManager.renderEngine;
		int i = renderengine.getTextureForDownloadableImage(par1Str, par2Str);

		if (i >= 0) {
			renderengine.bindTexture(i);
			return true;
		} else {
			return false;
		}
	}

	private void renderEntityOnFire(Entity par1Entity, double par2, double par4, double par6, float par8) {
		GL11.glDisable(GL11.GL_LIGHTING);
		int i = Block.fire.blockIndexInTexture;
		int j = (i & 0xf) << 4;
		int k = i & 0xf0;
		float f = (float)j / 256F;
		float f2 = ((float)j + 15.99F) / 256F;
		float f4 = (float)k / 256F;
		float f6 = ((float)k + 15.99F) / 256F;
		GL11.glPushMatrix();
		GL11.glTranslatef((float)par2, (float)par4, (float)par6);
		float f8 = par1Entity.width * 1.4F;
		GL11.glScalef(f8, f8, f8);
		loadTexture("/terrain.png");
		Tessellator tessellator = Tessellator.instance;
		float f9 = 0.5F;
		float f10 = 0.0F;
		float f11 = par1Entity.height / f8;
		float f12 = (float)(par1Entity.posY - par1Entity.boundingBox.minY);
		GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(0.0F, 0.0F, -0.3F + (float)(int)f11 * 0.02F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float f13 = 0.0F;
		int l = 0;
		tessellator.startDrawingQuads();

		while (f11 > 0.0F) {
			float f1;
			float f3;
			float f5;
			float f7;

			if (l % 2 == 0) {
				f1 = (float)j / 256F;
				f3 = ((float)j + 15.99F) / 256F;
				f5 = (float)k / 256F;
				f7 = ((float)k + 15.99F) / 256F;
			} else {
				f1 = (float)j / 256F;
				f3 = ((float)j + 15.99F) / 256F;
				f5 = (float)(k + 16) / 256F;
				f7 = ((float)(k + 16) + 15.99F) / 256F;
			}

			if ((l / 2) % 2 == 0) {
				float f14 = f3;
				f3 = f1;
				f1 = f14;
			}

			tessellator.addVertexWithUV(f9 - f10, 0.0F - f12, f13, f3, f7);
			tessellator.addVertexWithUV(-f9 - f10, 0.0F - f12, f13, f1, f7);
			tessellator.addVertexWithUV(-f9 - f10, 1.4F - f12, f13, f1, f5);
			tessellator.addVertexWithUV(f9 - f10, 1.4F - f12, f13, f3, f5);
			f11 -= 0.45F;
			f12 -= 0.45F;
			f9 *= 0.9F;
			f13 += 0.03F;
			l++;
		}

		tessellator.draw();
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_LIGHTING);
	}

	private void renderShadow(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		RenderEngine renderengine = renderManager.renderEngine;
		renderengine.bindTexture(renderengine.getTexture("%clamp%/misc/shadow.png"));
		World world = getWorldFromRenderManager();
		GL11.glDepthMask(false);
		float f = shadowSize;

		if (par1Entity instanceof EntityLiving) {
			EntityLiving entityliving = (EntityLiving)par1Entity;
			f *= entityliving.getRenderSizeModifier();

			if (entityliving instanceof EntityAnimal) {
				EntityAnimal entityanimal = (EntityAnimal)entityliving;

				if (entityanimal.isChild()) {
					f *= 0.5F;
				}
			}
		}

		double d = par1Entity.lastTickPosX + (par1Entity.posX - par1Entity.lastTickPosX) * (double)par9;
		double d1 = par1Entity.lastTickPosY + (par1Entity.posY - par1Entity.lastTickPosY) * (double)par9 + (double)par1Entity.getShadowSize();
		double d2 = par1Entity.lastTickPosZ + (par1Entity.posZ - par1Entity.lastTickPosZ) * (double)par9;
		int i = MathHelper.floor_double(d - (double)f);
		int j = MathHelper.floor_double(d + (double)f);
		int k = MathHelper.floor_double(d1 - (double)f);
		int l = MathHelper.floor_double(d1);
		int i1 = MathHelper.floor_double(d2 - (double)f);
		int j1 = MathHelper.floor_double(d2 + (double)f);
		double d3 = par2 - d;
		double d4 = par4 - d1;
		double d5 = par6 - d2;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();

		for (int k1 = i; k1 <= j; k1++) {
			for (int l1 = k; l1 <= l; l1++) {
				for (int i2 = i1; i2 <= j1; i2++) {
					int j2 = world.getBlockId(k1, l1 - 1, i2);

					if (j2 > 0 && world.getBlockLightValue(k1, l1, i2) > 3) {
						renderShadowOnBlock(Block.blocksList[j2], par2, par4 + (double)par1Entity.getShadowSize(), par6, k1, l1, i2, par8, f, d3, d4 + (double)par1Entity.getShadowSize(), d5);
					}
				}
			}
		}

		tessellator.draw();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);
	}

	private World getWorldFromRenderManager() {
		return renderManager.worldObj;
	}

	private void renderShadowOnBlock(Block par1Block, double par2, double par4, double par6, int par8, int par9, int par10, float par11, float par12, double par13, double par15, double par17) {
		Tessellator tessellator = Tessellator.instance;

		if (!par1Block.renderAsNormalBlock()) {
			return;
		}

		double d = ((double)par11 - (par4 - ((double)par9 + par15)) / 2D) * 0.5D * (double)getWorldFromRenderManager().getLightBrightness(par8, par9, par10);

		if (d < 0.0D) {
			return;
		}

		if (d > 1.0D) {
			d = 1.0D;
		}

		tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, (float)d);
		double d1 = (double)par8 + par1Block.minX + par13;
		double d2 = (double)par8 + par1Block.maxX + par13;
		double d3 = (double)par9 + par1Block.minY + par15 + 0.015625D;
		double d4 = (double)par10 + par1Block.minZ + par17;
		double d5 = (double)par10 + par1Block.maxZ + par17;
		float f = (float)((par2 - d1) / 2D / (double)par12 + 0.5D);
		float f1 = (float)((par2 - d2) / 2D / (double)par12 + 0.5D);
		float f2 = (float)((par6 - d4) / 2D / (double)par12 + 0.5D);
		float f3 = (float)((par6 - d5) / 2D / (double)par12 + 0.5D);
		tessellator.addVertexWithUV(d1, d3, d4, f, f2);
		tessellator.addVertexWithUV(d1, d3, d5, f, f3);
		tessellator.addVertexWithUV(d2, d3, d5, f1, f3);
		tessellator.addVertexWithUV(d2, d3, d4, f1, f2);
	}

	public static void renderOffsetAABB(AxisAlignedBB par0AxisAlignedBB, double par1, double par3, double par5) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		Tessellator tessellator = Tessellator.instance;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		tessellator.startDrawingQuads();
		tessellator.setTranslation(par1, par3, par5);
		tessellator.setNormal(0.0F, 0.0F, -1F);
		tessellator.addVertex(par0AxisAlignedBB.minX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.minZ);
		tessellator.addVertex(par0AxisAlignedBB.maxX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.minZ);
		tessellator.addVertex(par0AxisAlignedBB.maxX, par0AxisAlignedBB.minY, par0AxisAlignedBB.minZ);
		tessellator.addVertex(par0AxisAlignedBB.minX, par0AxisAlignedBB.minY, par0AxisAlignedBB.minZ);
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		tessellator.addVertex(par0AxisAlignedBB.minX, par0AxisAlignedBB.minY, par0AxisAlignedBB.maxZ);
		tessellator.addVertex(par0AxisAlignedBB.maxX, par0AxisAlignedBB.minY, par0AxisAlignedBB.maxZ);
		tessellator.addVertex(par0AxisAlignedBB.maxX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.maxZ);
		tessellator.addVertex(par0AxisAlignedBB.minX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.maxZ);
		tessellator.setNormal(0.0F, -1F, 0.0F);
		tessellator.addVertex(par0AxisAlignedBB.minX, par0AxisAlignedBB.minY, par0AxisAlignedBB.minZ);
		tessellator.addVertex(par0AxisAlignedBB.maxX, par0AxisAlignedBB.minY, par0AxisAlignedBB.minZ);
		tessellator.addVertex(par0AxisAlignedBB.maxX, par0AxisAlignedBB.minY, par0AxisAlignedBB.maxZ);
		tessellator.addVertex(par0AxisAlignedBB.minX, par0AxisAlignedBB.minY, par0AxisAlignedBB.maxZ);
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		tessellator.addVertex(par0AxisAlignedBB.minX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.maxZ);
		tessellator.addVertex(par0AxisAlignedBB.maxX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.maxZ);
		tessellator.addVertex(par0AxisAlignedBB.maxX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.minZ);
		tessellator.addVertex(par0AxisAlignedBB.minX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.minZ);
		tessellator.setNormal(-1F, 0.0F, 0.0F);
		tessellator.addVertex(par0AxisAlignedBB.minX, par0AxisAlignedBB.minY, par0AxisAlignedBB.maxZ);
		tessellator.addVertex(par0AxisAlignedBB.minX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.maxZ);
		tessellator.addVertex(par0AxisAlignedBB.minX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.minZ);
		tessellator.addVertex(par0AxisAlignedBB.minX, par0AxisAlignedBB.minY, par0AxisAlignedBB.minZ);
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		tessellator.addVertex(par0AxisAlignedBB.maxX, par0AxisAlignedBB.minY, par0AxisAlignedBB.minZ);
		tessellator.addVertex(par0AxisAlignedBB.maxX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.minZ);
		tessellator.addVertex(par0AxisAlignedBB.maxX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.maxZ);
		tessellator.addVertex(par0AxisAlignedBB.maxX, par0AxisAlignedBB.minY, par0AxisAlignedBB.maxZ);
		tessellator.setTranslation(0.0D, 0.0D, 0.0D);
		tessellator.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public static void renderAABB(AxisAlignedBB par0AxisAlignedBB) {
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertex(par0AxisAlignedBB.minX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.minZ);
		tessellator.addVertex(par0AxisAlignedBB.maxX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.minZ);
		tessellator.addVertex(par0AxisAlignedBB.maxX, par0AxisAlignedBB.minY, par0AxisAlignedBB.minZ);
		tessellator.addVertex(par0AxisAlignedBB.minX, par0AxisAlignedBB.minY, par0AxisAlignedBB.minZ);
		tessellator.addVertex(par0AxisAlignedBB.minX, par0AxisAlignedBB.minY, par0AxisAlignedBB.maxZ);
		tessellator.addVertex(par0AxisAlignedBB.maxX, par0AxisAlignedBB.minY, par0AxisAlignedBB.maxZ);
		tessellator.addVertex(par0AxisAlignedBB.maxX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.maxZ);
		tessellator.addVertex(par0AxisAlignedBB.minX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.maxZ);
		tessellator.addVertex(par0AxisAlignedBB.minX, par0AxisAlignedBB.minY, par0AxisAlignedBB.minZ);
		tessellator.addVertex(par0AxisAlignedBB.maxX, par0AxisAlignedBB.minY, par0AxisAlignedBB.minZ);
		tessellator.addVertex(par0AxisAlignedBB.maxX, par0AxisAlignedBB.minY, par0AxisAlignedBB.maxZ);
		tessellator.addVertex(par0AxisAlignedBB.minX, par0AxisAlignedBB.minY, par0AxisAlignedBB.maxZ);
		tessellator.addVertex(par0AxisAlignedBB.minX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.maxZ);
		tessellator.addVertex(par0AxisAlignedBB.maxX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.maxZ);
		tessellator.addVertex(par0AxisAlignedBB.maxX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.minZ);
		tessellator.addVertex(par0AxisAlignedBB.minX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.minZ);
		tessellator.addVertex(par0AxisAlignedBB.minX, par0AxisAlignedBB.minY, par0AxisAlignedBB.maxZ);
		tessellator.addVertex(par0AxisAlignedBB.minX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.maxZ);
		tessellator.addVertex(par0AxisAlignedBB.minX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.minZ);
		tessellator.addVertex(par0AxisAlignedBB.minX, par0AxisAlignedBB.minY, par0AxisAlignedBB.minZ);
		tessellator.addVertex(par0AxisAlignedBB.maxX, par0AxisAlignedBB.minY, par0AxisAlignedBB.minZ);
		tessellator.addVertex(par0AxisAlignedBB.maxX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.minZ);
		tessellator.addVertex(par0AxisAlignedBB.maxX, par0AxisAlignedBB.maxY, par0AxisAlignedBB.maxZ);
		tessellator.addVertex(par0AxisAlignedBB.maxX, par0AxisAlignedBB.minY, par0AxisAlignedBB.maxZ);
		tessellator.draw();
	}

	public void setRenderManager(RenderManager par1RenderManager) {
		renderManager = par1RenderManager;
	}

	public void doRenderShadowAndFire(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		if (renderManager.options.fancyGraphics && shadowSize > 0.0F) {
			double d = renderManager.getDistanceToCamera(par1Entity.posX, par1Entity.posY, par1Entity.posZ);
			float f = (float)((1.0D - d / 256D) * (double)shadowOpaque);

			if (f > 0.0F) {
				renderShadow(par1Entity, par2, par4, par6, f, par9);
			}
		}

		if (par1Entity.isBurning()) {
			renderEntityOnFire(par1Entity, par2, par4, par6, par9);
		}
	}

	public FontRenderer getFontRendererFromRenderManager() {
		return renderManager.getFontRenderer();
	}
}
