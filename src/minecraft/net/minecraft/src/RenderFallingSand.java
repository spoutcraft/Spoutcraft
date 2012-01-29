package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderFallingSand extends Render {
	private RenderBlocks renderBlocks;

	public RenderFallingSand() {
		renderBlocks = new RenderBlocks();
		shadowSize = 0.5F;
	}

	public void doRenderFallingSand(EntityFallingSand entityfallingsand, double d, double d1, double d2,
	        float f, float f1) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float)d, (float)d1, (float)d2);
		loadTexture("/terrain.png");
		Block block = Block.blocksList[entityfallingsand.blockID];
		World world = entityfallingsand.getWorld();
		GL11.glDisable(2896 /*GL_LIGHTING*/);
		if (block == Block.dragonEgg) {
			renderBlocks.blockAccess = world;
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.setTranslationD((float)(-MathHelper.floor_double(entityfallingsand.posX)) - 0.5F, (float)(-MathHelper.floor_double(entityfallingsand.posY)) - 0.5F, (float)(-MathHelper.floor_double(entityfallingsand.posZ)) - 0.5F);
			renderBlocks.renderBlockByRenderType(block, MathHelper.floor_double(entityfallingsand.posX), MathHelper.floor_double(entityfallingsand.posY), MathHelper.floor_double(entityfallingsand.posZ));
			tessellator.setTranslationD(0.0D, 0.0D, 0.0D);
			tessellator.draw();
		}
		else {
			renderBlocks.renderBlockFallingSand(block, world, MathHelper.floor_double(entityfallingsand.posX), MathHelper.floor_double(entityfallingsand.posY), MathHelper.floor_double(entityfallingsand.posZ));
		}
		GL11.glEnable(2896 /*GL_LIGHTING*/);
		GL11.glPopMatrix();
	}

	public void doRender(Entity entity, double d, double d1, double d2,
	        float f, float f1) {
		doRenderFallingSand((EntityFallingSand)entity, d, d1, d2, f, f1);
	}
}
