package net.minecraft.src;

import java.util.Random;
import org.lwjgl.opengl.GL11;

public class RenderDragon extends RenderLiving {
	public static EntityDragon entityDragon;
	private static int field_40284_d = 0;
	protected ModelDragon modelDragon;

	public RenderDragon() {
		super(new ModelDragon(0.0F), 0.5F);
		modelDragon = (ModelDragon)mainModel;
		setRenderPassModel(mainModel);
	}

	protected void rotateDragonBody(EntityDragon entitydragon, float f, float f1, float f2) {
		float f3 = (float)entitydragon.func_40160_a(7, f2)[0];
		float f4 = (float)(entitydragon.func_40160_a(5, f2)[1] - entitydragon.func_40160_a(10, f2)[1]);
		GL11.glRotatef(-f3, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(f4 * 10F, 1.0F, 0.0F, 0.0F);
		GL11.glTranslatef(0.0F, 0.0F, 1.0F);
		if (entitydragon.deathTime > 0) {
			float f5 = ((((float)entitydragon.deathTime + f2) - 1.0F) / 20F) * 1.6F;
			f5 = MathHelper.sqrt_float(f5);
			if (f5 > 1.0F) {
				f5 = 1.0F;
			}
			GL11.glRotatef(f5 * getDeathMaxRotation(entitydragon), 0.0F, 0.0F, 1.0F);
		}
	}

	protected void func_40280_a(EntityDragon entitydragon, float f, float f1, float f2, float f3, float f4, float f5) {
		if (entitydragon.field_40178_aA > 0) {
			float f6 = (float)entitydragon.field_40178_aA / 200F;
			GL11.glDepthFunc(515);
			GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
			GL11.glAlphaFunc(516, f6);
			loadDownloadableImageTexture(entitydragon.skinUrl, "/mob/enderdragon/shuffle.png");
			mainModel.render(entitydragon, f, f1, f2, f3, f4, f5);
			GL11.glAlphaFunc(516, 0.1F);
			GL11.glDepthFunc(514);
		}
		loadDownloadableImageTexture(entitydragon.skinUrl, entitydragon.getEntityTexture());
		mainModel.render(entitydragon, f, f1, f2, f3, f4, f5);
		if (entitydragon.hurtTime > 0) {
			GL11.glDepthFunc(514);
			GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
			GL11.glEnable(3042 /*GL_BLEND*/);
			GL11.glBlendFunc(770, 771);
			GL11.glColor4f(1.0F, 0.0F, 0.0F, 0.5F);
			mainModel.render(entitydragon, f, f1, f2, f3, f4, f5);
			GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
			GL11.glDisable(3042 /*GL_BLEND*/);
			GL11.glDepthFunc(515);
		}
	}

	public void renderDragon(EntityDragon entitydragon, double d, double d1, double d2,
	        float f, float f1) {
		entityDragon = entitydragon;
		if (field_40284_d != 4) {
			mainModel = new ModelDragon(0.0F);
			field_40284_d = 4;
		}
		super.doRenderLiving(entitydragon, d, d1, d2, f, f1);
		if (entitydragon.healingEnderCrystal != null) {
			float f2 = (float)entitydragon.healingEnderCrystal.field_41032_a + f1;
			float f3 = MathHelper.sin(f2 * 0.2F) / 2.0F + 0.5F;
			f3 = (f3 * f3 + f3) * 0.2F;
			float f4 = (float)(entitydragon.healingEnderCrystal.posX - entitydragon.posX - (entitydragon.prevPosX - entitydragon.posX) * (double)(1.0F - f1));
			float f5 = (float)(((double)f3 + entitydragon.healingEnderCrystal.posY) - 1.0D - entitydragon.posY - (entitydragon.prevPosY - entitydragon.posY) * (double)(1.0F - f1));
			float f6 = (float)(entitydragon.healingEnderCrystal.posZ - entitydragon.posZ - (entitydragon.prevPosZ - entitydragon.posZ) * (double)(1.0F - f1));
			float f7 = MathHelper.sqrt_float(f4 * f4 + f6 * f6);
			float f8 = MathHelper.sqrt_float(f4 * f4 + f5 * f5 + f6 * f6);
			GL11.glPushMatrix();
			GL11.glTranslatef((float)d, (float)d1 + 2.0F, (float)d2);
			GL11.glRotatef(((float)(-Math.atan2(f6, f4)) * 180F) / 3.141593F - 90F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(((float)(-Math.atan2(f7, f5)) * 180F) / 3.141593F - 90F, 1.0F, 0.0F, 0.0F);
			Tessellator tessellator = Tessellator.instance;
			RenderHelper.disableStandardItemLighting();
			GL11.glDisable(2884 /*GL_CULL_FACE*/);
			loadTexture("/mob/enderdragon/beam.png");
			GL11.glShadeModel(7425 /*GL_SMOOTH*/);
			float f9 = 0.0F - ((float)entitydragon.ticksExisted + f1) * 0.01F;
			float f10 = MathHelper.sqrt_float(f4 * f4 + f5 * f5 + f6 * f6) / 32F - ((float)entitydragon.ticksExisted + f1) * 0.01F;
			tessellator.startDrawing(5);
			int i = 8;
			for (int j = 0; j <= i; j++) {
				float f11 = MathHelper.sin(((float)(j % i) * 3.141593F * 2.0F) / (float)i) * 0.75F;
				float f12 = MathHelper.cos(((float)(j % i) * 3.141593F * 2.0F) / (float)i) * 0.75F;
				float f13 = ((float)(j % i) * 1.0F) / (float)i;
				tessellator.setColorOpaque_I(0);
				tessellator.addVertexWithUV(f11 * 0.2F, f12 * 0.2F, 0.0D, f13, f10);
				tessellator.setColorOpaque_I(0xffffff);
				tessellator.addVertexWithUV(f11, f12, f8, f13, f9);
			}

			tessellator.draw();
			GL11.glEnable(2884 /*GL_CULL_FACE*/);
			GL11.glShadeModel(7424 /*GL_FLAT*/);
			RenderHelper.enableStandardItemLighting();
			GL11.glPopMatrix();
		}
	}

	protected void renderDragonDying(EntityDragon entitydragon, float f) {
		super.renderEquippedItems(entitydragon, f);
		Tessellator tessellator = Tessellator.instance;
		if (entitydragon.field_40178_aA > 0) {
			RenderHelper.disableStandardItemLighting();
			float f1 = ((float)entitydragon.field_40178_aA + f) / 200F;
			float f2 = 0.0F;
			if (f1 > 0.8F) {
				f2 = (f1 - 0.8F) / 0.2F;
			}
			Random random = new Random(432L);
			GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
			GL11.glShadeModel(7425 /*GL_SMOOTH*/);
			GL11.glEnable(3042 /*GL_BLEND*/);
			GL11.glBlendFunc(770, 1);
			GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
			GL11.glEnable(2884 /*GL_CULL_FACE*/);
			GL11.glDepthMask(false);
			GL11.glPushMatrix();
			GL11.glTranslatef(0.0F, -1F, -2F);
			for (int i = 0; (float)i < ((f1 + f1 * f1) / 2.0F) * 60F; i++) {
				GL11.glRotatef(random.nextFloat() * 360F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(random.nextFloat() * 360F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(random.nextFloat() * 360F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(random.nextFloat() * 360F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(random.nextFloat() * 360F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(random.nextFloat() * 360F + f1 * 90F, 0.0F, 0.0F, 1.0F);
				tessellator.startDrawing(6);
				float f3 = random.nextFloat() * 20F + 5F + f2 * 10F;
				float f4 = random.nextFloat() * 2.0F + 1.0F + f2 * 2.0F;
				tessellator.setColorRGBA_I(0xffffff, (int)(255F * (1.0F - f2)));
				tessellator.addVertex(0.0D, 0.0D, 0.0D);
				tessellator.setColorRGBA_I(0xff00ff, 0);
				tessellator.addVertex(-0.86599999999999999D * (double)f4, f3, -0.5F * f4);
				tessellator.addVertex(0.86599999999999999D * (double)f4, f3, -0.5F * f4);
				tessellator.addVertex(0.0D, f3, 1.0F * f4);
				tessellator.addVertex(-0.86599999999999999D * (double)f4, f3, -0.5F * f4);
				tessellator.draw();
			}

			GL11.glPopMatrix();
			GL11.glDepthMask(true);
			GL11.glDisable(2884 /*GL_CULL_FACE*/);
			GL11.glDisable(3042 /*GL_BLEND*/);
			GL11.glShadeModel(7424 /*GL_FLAT*/);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
			GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
			RenderHelper.enableStandardItemLighting();
		}
	}

	protected int func_40283_a(EntityDragon entitydragon, int i, float f) {
		if (i == 1) {
			GL11.glDepthFunc(515);
		}
		if (i != 0) {
			return -1;
		}
		else {
			loadTexture("/mob/enderdragon/ender_eyes.png");
			float f1 = 1.0F;
			GL11.glEnable(3042 /*GL_BLEND*/);
			GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
			GL11.glBlendFunc(1, 1);
			GL11.glDisable(2896 /*GL_LIGHTING*/);
			GL11.glDepthFunc(514);
			int j = 61680;
			int k = j % 0x10000;
			int l = j / 0x10000;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapEnabled, (float)k / 1.0F, (float)l / 1.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glEnable(2896 /*GL_LIGHTING*/);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, f1);
			return 1;
		}
	}

	protected int shouldRenderPass(EntityLiving entityliving, int i, float f) {
		return func_40283_a((EntityDragon)entityliving, i, f);
	}

	protected void renderEquippedItems(EntityLiving entityliving, float f) {
		renderDragonDying((EntityDragon)entityliving, f);
	}

	protected void rotateCorpse(EntityLiving entityliving, float f, float f1, float f2) {
		rotateDragonBody((EntityDragon)entityliving, f, f1, f2);
	}

	protected void renderModel(EntityLiving entityliving, float f, float f1, float f2, float f3, float f4, float f5) {
		func_40280_a((EntityDragon)entityliving, f, f1, f2, f3, f4, f5);
	}

	public void doRenderLiving(EntityLiving entityliving, double d, double d1, double d2,
	        float f, float f1) {
		renderDragon((EntityDragon)entityliving, d, d1, d2, f, f1);
	}

	public void doRender(Entity entity, double d, double d1, double d2,
	        float f, float f1) {
		renderDragon((EntityDragon)entity, d, d1, d2, f, f1);
	}
}
