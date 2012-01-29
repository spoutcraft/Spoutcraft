package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderCreeper extends RenderLiving {
	private ModelBase field_27008_a;

	public RenderCreeper() {
		super(new ModelCreeper(), 0.5F);
		field_27008_a = new ModelCreeper(2.0F);
	}

	protected void updateCreeperScale(EntityCreeper entitycreeper, float f) {
		EntityCreeper entitycreeper1 = entitycreeper;
		float f1 = entitycreeper1.setCreeperFlashTime(f);
		float f2 = 1.0F + MathHelper.sin(f1 * 100F) * f1 * 0.01F;
		if (f1 < 0.0F) {
			f1 = 0.0F;
		}
		if (f1 > 1.0F) {
			f1 = 1.0F;
		}
		f1 *= f1;
		f1 *= f1;
		float f3 = (1.0F + f1 * 0.4F) * f2;
		float f4 = (1.0F + f1 * 0.1F) / f2;
		GL11.glScalef(f3, f4, f3);
	}

	protected int updateCreeperColorMultiplier(EntityCreeper entitycreeper, float f, float f1) {
		EntityCreeper entitycreeper1 = entitycreeper;
		float f2 = entitycreeper1.setCreeperFlashTime(f1);
		if ((int)(f2 * 10F) % 2 == 0) {
			return 0;
		}
		int i = (int)(f2 * 0.2F * 255F);
		if (i < 0) {
			i = 0;
		}
		if (i > 255) {
			i = 255;
		}
		char c = '\377';
		char c1 = '\377';
		char c2 = '\377';
		return i << 24 | c << 16 | c1 << 8 | c2;
	}

	protected int func_27006_a(EntityCreeper entitycreeper, int i, float f) {
		if (entitycreeper.getPowered()) {
			if (i == 1) {
				float f1 = (float)entitycreeper.ticksExisted + f;
				loadTexture("/armor/power.png");
				GL11.glMatrixMode(5890 /*GL_TEXTURE*/);
				GL11.glLoadIdentity();
				float f2 = f1 * 0.01F;
				float f3 = f1 * 0.01F;
				GL11.glTranslatef(f2, f3, 0.0F);
				setRenderPassModel(field_27008_a);
				GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/);
				GL11.glEnable(3042 /*GL_BLEND*/);
				float f4 = 0.5F;
				GL11.glColor4f(f4, f4, f4, 1.0F);
				GL11.glDisable(2896 /*GL_LIGHTING*/);
				GL11.glBlendFunc(1, 1);
				return 1;
			}
			if (i == 2) {
				GL11.glMatrixMode(5890 /*GL_TEXTURE*/);
				GL11.glLoadIdentity();
				GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/);
				GL11.glEnable(2896 /*GL_LIGHTING*/);
				GL11.glDisable(3042 /*GL_BLEND*/);
			}
		}
		return -1;
	}

	protected int func_27007_b(EntityCreeper entitycreeper, int i, float f) {
		return -1;
	}

	protected void preRenderCallback(EntityLiving entityliving, float f) {
		updateCreeperScale((EntityCreeper)entityliving, f);
	}

	protected int getColorMultiplier(EntityLiving entityliving, float f, float f1) {
		return updateCreeperColorMultiplier((EntityCreeper)entityliving, f, f1);
	}

	protected int shouldRenderPass(EntityLiving entityliving, int i, float f) {
		return func_27006_a((EntityCreeper)entityliving, i, f);
	}

	protected int inheritRenderPass(EntityLiving entityliving, int i, float f) {
		return func_27007_b((EntityCreeper)entityliving, i, f);
	}
}
