package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderGiantZombie extends RenderLiving {
	private float scale;

	public RenderGiantZombie(ModelBase modelbase, float f, float f1) {
		super(modelbase, f * f1);
		scale = f1;
	}

	protected void preRenderScale(EntityGiantZombie entitygiantzombie, float f) {
		GL11.glScalef(scale, scale, scale);
	}

	protected void preRenderCallback(EntityLiving entityliving, float f) {
		preRenderScale((EntityGiantZombie)entityliving, f);
	}
}
