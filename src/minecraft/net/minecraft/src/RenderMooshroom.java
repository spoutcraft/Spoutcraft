package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderMooshroom extends RenderLiving {
	public RenderMooshroom(ModelBase modelbase, float f) {
		super(modelbase, f);
	}

	public void func_40273_a(EntityMooshroom entitymooshroom, double d, double d1, double d2,
	        float f, float f1) {
		super.doRenderLiving(entitymooshroom, d, d1, d2, f, f1);
	}

	protected void func_40272_a(EntityMooshroom entitymooshroom, float f) {
		super.renderEquippedItems(entitymooshroom, f);
		if (entitymooshroom.isChild()) {
			return;
		}
		else {
			loadTexture("/terrain.png");
			GL11.glEnable(2884 /*GL_CULL_FACE*/);
			GL11.glPushMatrix();
			GL11.glScalef(1.0F, -1F, 1.0F);
			GL11.glTranslatef(0.2F, 0.4F, 0.5F);
			GL11.glRotatef(42F, 0.0F, 1.0F, 0.0F);
			renderBlocks.renderBlockAsItem(Block.mushroomRed, 0, 1.0F);
			GL11.glTranslatef(0.1F, 0.0F, -0.6F);
			GL11.glRotatef(42F, 0.0F, 1.0F, 0.0F);
			renderBlocks.renderBlockAsItem(Block.mushroomRed, 0, 1.0F);
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			((ModelQuadruped)mainModel).head.postRender(0.0625F);
			GL11.glScalef(1.0F, -1F, 1.0F);
			GL11.glTranslatef(0.0F, 0.75F, -0.2F);
			GL11.glRotatef(12F, 0.0F, 1.0F, 0.0F);
			renderBlocks.renderBlockAsItem(Block.mushroomRed, 0, 1.0F);
			GL11.glPopMatrix();
			GL11.glDisable(2884 /*GL_CULL_FACE*/);
			return;
		}
	}

	protected void renderEquippedItems(EntityLiving entityliving, float f) {
		func_40272_a((EntityMooshroom)entityliving, f);
	}

	public void doRenderLiving(EntityLiving entityliving, double d, double d1, double d2,
	        float f, float f1) {
		func_40273_a((EntityMooshroom)entityliving, d, d1, d2, f, f1);
	}

	public void doRender(Entity entity, double d, double d1, double d2,
	        float f, float f1) {
		func_40273_a((EntityMooshroom)entity, d, d1, d2, f, f1);
	}
}
