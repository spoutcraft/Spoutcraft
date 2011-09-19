package net.minecraft.src;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityArrow;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModelBase;
import net.minecraft.src.ModelRenderer;
import net.minecraft.src.Render;
import net.minecraft.src.Tessellator;

import org.getspout.spout.client.SpoutClient; //Spout
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class RenderLiving extends Render {

	protected ModelBase mainModel;
	protected ModelBase renderPassModel;


	public RenderLiving(ModelBase var1, float var2) {
		this.mainModel = var1;
		this.shadowSize = var2;
	}

	public void setRenderPassModel(ModelBase var1) {
		this.renderPassModel = var1;
	}

	public void doRenderLiving(EntityLiving var1, double var2, double var4, double var6, float var8, float var9) {
		GL11.glPushMatrix();
		GL11.glDisable(2884 /*GL_CULL_FACE*/);
		this.mainModel.onGround = this.renderSwingProgress(var1, var9);
		if(this.renderPassModel != null) {
			this.renderPassModel.onGround = this.mainModel.onGround;
		}

		this.mainModel.isRiding = var1.isRiding();
		if(this.renderPassModel != null) {
			this.renderPassModel.isRiding = this.mainModel.isRiding;
		}

		try {
			float var10 = var1.prevRenderYawOffset + (var1.renderYawOffset - var1.prevRenderYawOffset) * var9;
			float var11 = var1.prevRotationYaw + (var1.rotationYaw - var1.prevRotationYaw) * var9;
			float var12 = var1.prevRotationPitch + (var1.rotationPitch - var1.prevRotationPitch) * var9;
			this.renderLivingAt(var1, var2, var4, var6);
			float var13 = this.handleRotationFloat(var1, var9);
			this.rotateCorpse(var1, var13, var10, var9);
			float var14 = 0.0625F;
			GL11.glEnable('\u803a');
			GL11.glScalef(-1.0F, -1.0F, 1.0F);
			this.preRenderCallback(var1, var9);
			GL11.glTranslatef(0.0F, -24.0F * var14 - 0.0078125F, 0.0F);
			float var15 = var1.field_705_Q + (var1.field_704_R - var1.field_705_Q) * var9;
			float var16 = var1.field_703_S - var1.field_704_R * (1.0F - var9);
			if(var15 > 1.0F) {
				var15 = 1.0F;
			}

			this.loadDownloadableImageTexture(var1.skinUrl, var1.getEntityTexture());
			GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
			this.mainModel.setLivingAnimations(var1, var16, var15, var9);
			this.mainModel.render(var1, var16, var15, var13, var11 - var10, var12, var14);

			for(int var17 = 0; var17 < 4; ++var17) {
				if(this.shouldRenderPass(var1, var17, var9)) {
					this.renderPassModel.render(var1, var16, var15, var13, var11 - var10, var12, var14);
					GL11.glDisable(3042 /*GL_BLEND*/);
					GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
				}
			}

			this.renderEquippedItems(var1, var9);
			float var25 = var1.getEntityBrightness(var9);
			int var18 = this.getColorMultiplier(var1, var25, var9);
			GL13.glClientActiveTexture('\u84c1');
			GL13.glActiveTexture('\u84c1');
			GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
			GL13.glClientActiveTexture('\u84c0');
			GL13.glActiveTexture('\u84c0');
			if((var18 >> 24 & 255) > 0 || var1.hurtTime > 0 || var1.deathTime > 0) {
				GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
				GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
				GL11.glEnable(3042 /*GL_BLEND*/);
				GL11.glBlendFunc(770, 771);
				GL11.glDepthFunc(514);
				if(var1.hurtTime > 0 || var1.deathTime > 0) {
					GL11.glColor4f(var25, 0.0F, 0.0F, 0.4F);
					this.mainModel.render(var1, var16, var15, var13, var11 - var10, var12, var14);

					for(int var19 = 0; var19 < 4; ++var19) {
						if(this.inheritRenderPass(var1, var19, var9)) {
							GL11.glColor4f(var25, 0.0F, 0.0F, 0.4F);
							this.renderPassModel.render(var1, var16, var15, var13, var11 - var10, var12, var14);
						}
					}
				}

				if((var18 >> 24 & 255) > 0) {
					float var26 = (float)(var18 >> 16 & 255) / 255.0F;
					float var20 = (float)(var18 >> 8 & 255) / 255.0F;
					float var21 = (float)(var18 & 255) / 255.0F;
					float var22 = (float)(var18 >> 24 & 255) / 255.0F;
					GL11.glColor4f(var26, var20, var21, var22);
					this.mainModel.render(var1, var16, var15, var13, var11 - var10, var12, var14);

					for(int var23 = 0; var23 < 4; ++var23) {
						if(this.inheritRenderPass(var1, var23, var9)) {
							GL11.glColor4f(var26, var20, var21, var22);
							this.renderPassModel.render(var1, var16, var15, var13, var11 - var10, var12, var14);
						}
					}
				}

				GL11.glDepthFunc(515);
				GL11.glDisable(3042 /*GL_BLEND*/);
				GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
				GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
			}

			GL11.glDisable('\u803a');
		} catch (Exception var24) {
			var24.printStackTrace();
		}

		GL13.glClientActiveTexture('\u84c1');
		GL13.glActiveTexture('\u84c1');
		GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
		GL13.glClientActiveTexture('\u84c0');
		GL13.glActiveTexture('\u84c0');
		GL11.glEnable(2884 /*GL_CULL_FACE*/);
		GL11.glPopMatrix();
		this.passSpecialRender(var1, var2, var4, var6);
	}

	protected void renderLivingAt(EntityLiving var1, double var2, double var4, double var6) {
		GL11.glTranslatef((float)var2, (float)var4, (float)var6);
	}

	protected void rotateCorpse(EntityLiving var1, float var2, float var3, float var4) {
		GL11.glRotatef(180.0F - var3, 0.0F, 1.0F, 0.0F);
		if(var1.deathTime > 0) {
			float var5 = ((float)var1.deathTime + var4 - 1.0F) / 20.0F * 1.6F;
			var5 = MathHelper.sqrt_float(var5);
			if(var5 > 1.0F) {
				var5 = 1.0F;
			}

			GL11.glRotatef(var5 * this.getDeathMaxRotation(var1), 0.0F, 0.0F, 1.0F);
		}

	}

	protected float renderSwingProgress(EntityLiving var1, float var2) {
		return var1.getSwingProgress(var2);
	}

	protected float handleRotationFloat(EntityLiving var1, float var2) {
		return (float)var1.ticksExisted + var2;
	}

	protected void renderEquippedItems(EntityLiving var1, float var2) {
		if(var1.field_35172_bP > 0) {
			EntityArrow var3 = new EntityArrow(var1.worldObj, var1.posX, var1.posY, var1.posZ);
			Random var4 = new Random((long)var1.entityId);

			for(int var5 = 0; var5 < var1.field_35172_bP; ++var5) {
				GL11.glPushMatrix();
				ModelRenderer var6 = this.mainModel.func_35393_a(var4);
				var6.postRender(0.0625F);
				float var7 = var4.nextFloat();
				float var8 = var4.nextFloat();
				float var9 = var4.nextFloat();
				float var10 = (var6.field_35977_i + (var6.field_35973_l - var6.field_35977_i) * var7) / 16.0F;
				float var11 = (var6.field_35975_j + (var6.field_35974_m - var6.field_35975_j) * var8) / 16.0F;
				float var12 = (var6.field_35976_k + (var6.field_35972_n - var6.field_35976_k) * var9) / 16.0F;
				GL11.glTranslatef(var10, var11, var12);
				var7 = var7 * 2.0F - 1.0F;
				var8 = var8 * 2.0F - 1.0F;
				var9 = var9 * 2.0F - 1.0F;
				var7 *= -1.0F;
				var8 *= -1.0F;
				var9 *= -1.0F;
				float var13 = MathHelper.sqrt_float(var7 * var7 + var9 * var9);
				var3.prevRotationYaw = var3.rotationYaw = (float)(Math.atan2((double)var7, (double)var9) * 180.0D / 3.1415927410125732D);
				var3.prevRotationPitch = var3.rotationPitch = (float)(Math.atan2((double)var8, (double)var13) * 180.0D / 3.1415927410125732D);
				double var14 = 0.0D;
				double var16 = 0.0D;
				double var18 = 0.0D;
				float var20 = 0.0F;
				this.renderManager.renderEntityWithPosYaw(var3, var14, var16, var18, var20, var2);
				GL11.glPopMatrix();
			}
		}

	}

	protected boolean inheritRenderPass(EntityLiving var1, int var2, float var3) {
		return this.shouldRenderPass(var1, var2, var3);
	}

	protected boolean shouldRenderPass(EntityLiving var1, int var2, float var3) {
		return false;
	}

	protected float getDeathMaxRotation(EntityLiving var1) {
		return 90.0F;
	}

	protected int getColorMultiplier(EntityLiving var1, float var2, float var3) {
		return 0;
	}

	protected void preRenderCallback(EntityLiving var1, float var2) {}

	protected void passSpecialRender(EntityLiving var1, double var2, double var4, double var6) {
		//Spout Start
		if(Minecraft.isDebugInfoEnabled() && !SpoutClient.getHandle().isMultiplayerWorld()) {
			this.renderLivingLabel(var1, Integer.toString(var1.entityId), var2, var4, var6, 64);
		}
		else {
			String title = SpoutClient.getInstance().getActivePlayer().getEntityTitle(var1.entityId);
			if (title != null && !title.equals("[hide]")) {
				String lines[] = title.split("\\n");
				for (int i = 0; i < lines.length; i++){
					renderLivingLabel(var1, lines[i], var2, var4 + (0.275D * (lines.length - i - 1)), var6, 64);
				}
			}
		}
		//Spout End
	}

	protected void renderLivingLabel(EntityLiving var1, String var2, double var3, double var5, double var7, int var9) {
		float var10 = var1.getDistanceToEntity(this.renderManager.livingPlayer);
		if(var10 <= (float)var9) {
			FontRenderer var11 = this.getFontRendererFromRenderManager();
			float var12 = 1.6F;
			float var13 = 0.016666668F * var12;
			GL11.glPushMatrix();
			GL11.glTranslatef((float)var3 + 0.0F, (float)var5 + 2.3F, (float)var7);
			GL11.glNormal3f(0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
			GL11.glScalef(-var13, -var13, var13);
			GL11.glDisable(2896 /*GL_LIGHTING*/);
			GL11.glDepthMask(false);
			GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
			GL11.glEnable(3042 /*GL_BLEND*/);
			GL11.glBlendFunc(770, 771);
			Tessellator var14 = Tessellator.instance;
			byte var15 = 0;
			if(var2.equals("deadmau5")) {
				var15 = -10;
			}

			GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
			var14.startDrawingQuads();
			int var16 = var11.getStringWidth(var2) / 2;
			var14.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
			var14.addVertex((double)(-var16 - 1), (double)(-1 + var15), 0.0D);
			var14.addVertex((double)(-var16 - 1), (double)(8 + var15), 0.0D);
			var14.addVertex((double)(var16 + 1), (double)(8 + var15), 0.0D);
			var14.addVertex((double)(var16 + 1), (double)(-1 + var15), 0.0D);
			var14.draw();
			GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
			var11.drawString(var2, -var11.getStringWidth(var2) / 2, var15, 553648127);
			GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
			GL11.glDepthMask(true);
			var11.drawString(var2, -var11.getStringWidth(var2) / 2, var15, -1);
			GL11.glEnable(2896 /*GL_LIGHTING*/);
			GL11.glDisable(3042 /*GL_BLEND*/);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glPopMatrix();
		}
	}

	// $FF: synthetic method
	// $FF: bridge method
	public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9) {
		this.doRenderLiving((EntityLiving)var1, var2, var4, var6, var8, var9);
	}
}
