package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModelBase;
import net.minecraft.src.OpenGlHelper;
import net.minecraft.src.Render;
import net.minecraft.src.Tessellator;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.spoutcraft.client.SpoutClient;

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
		GL11.glDisable(GL11.GL_CULL_FACE);
		this.mainModel.onGround = this.renderSwingProgress(var1, var9);
		if(this.renderPassModel != null) {
			this.renderPassModel.onGround = this.mainModel.onGround;
		}

		this.mainModel.isRiding = var1.isRiding();
		if(this.renderPassModel != null) {
			this.renderPassModel.isRiding = this.mainModel.isRiding;
		}

		this.mainModel.field_40301_k = var1.isChild();
		if(this.renderPassModel != null) {
			this.renderPassModel.field_40301_k = this.mainModel.field_40301_k;
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
			if (var1.isChild()) {
				var16 *= 3.0F;
			}

			if(var15 > 1.0F) {
				var15 = 1.0F;
			}

			GL11.glEnable(GL11.GL_ALPHA_TEST);
			this.mainModel.setLivingAnimations(var1, var16, var15, var9);
			this.renderModel(var1, var16, var15, var13, var11 - var10, var12, var14);

			float var19;
			int var18;
			float var20;
			float var22;
			for(int var17 = 0; var17 < 4; ++var17) {
				var18 = this.shouldRenderPass(var1, var17, var9);
				if(var18 > 0) {
					this.renderPassModel.setLivingAnimations(var1, var16, var15, var9);
					this.renderPassModel.render(var1, var16, var15, var13, var11 - var10, var12, var14);
					if(var18 == 15) {
						var19 = (float)var1.ticksExisted + var9;
						this.loadTexture("%blur%/misc/glint.png");
						GL11.glEnable(GL11.GL_BLEND);
						var20 = 0.5F;
						GL11.glColor4f(var20, var20, var20, 1.0F);
						GL11.glDepthFunc(514);
						GL11.glDepthMask(false);

						for(int var21 = 0; var21 < 2; ++var21) {
							GL11.glDisable(GL11.GL_LIGHTING);
							var22 = 0.76F;
							GL11.glColor4f(0.5F * var22, 0.25F * var22, 0.8F * var22, 1.0F);
							GL11.glBlendFunc(768, 1);
							GL11.glMatrixMode(GL11.GL_TEXTURE);
							GL11.glLoadIdentity();
							float var23 = var19 * (0.0010F + (float)var21 * 0.0030F) * 20.0F;
							float var24 = 0.33333334F;
							GL11.glScalef(var24, var24, var24);
							GL11.glRotatef(30.0F - (float)var21 * 60.0F, 0.0F, 0.0F, 1.0F);
							GL11.glTranslatef(0.0F, var23, 0.0F);
							GL11.glMatrixMode(org.lwjgl.opengl.ARBVertexBlend.GL_MODELVIEW0_ARB);
							this.renderPassModel.render(var1, var16, var15, var13, var11 - var10, var12, var14);
						}

						GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
						GL11.glMatrixMode(GL11.GL_TEXTURE);
						GL11.glDepthMask(true);
						GL11.glLoadIdentity();
						GL11.glMatrixMode(org.lwjgl.opengl.ARBVertexBlend.GL_MODELVIEW0_ARB);
						GL11.glEnable(GL11.GL_LIGHTING);
						GL11.glDisable(GL11.GL_BLEND);
						GL11.glDepthFunc(515);
					}

					GL11.glDisable(GL11.GL_BLEND);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
				}
			}

			this.renderEquippedItems(var1, var9);
			float var26 = var1.getEntityBrightness(var9);
			var18 = this.getColorMultiplier(var1, var26, var9);
			OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapEnabled);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapDisabled);
			if((var18 >> 24 & 255) > 0 || var1.hurtTime > 0 || var1.deathTime > 0) {
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(770, 771);
				GL11.glDepthFunc(514);
				if(var1.hurtTime > 0 || var1.deathTime > 0) {
					GL11.glColor4f(var26, 0.0F, 0.0F, 0.4F);
					this.mainModel.render(var1, var16, var15, var13, var11 - var10, var12, var14);

					for(int var27 = 0; var27 < 4; ++var27) {
						if(this.inheritRenderPass(var1, var27, var9) >= 0) {
							GL11.glColor4f(var26, 0.0F, 0.0F, 0.4F);
							this.renderPassModel.render(var1, var16, var15, var13, var11 - var10, var12, var14);
						}
					}
				}

				if((var18 >> 24 & 255) > 0) {
					var19 = (float)(var18 >> 16 & 255) / 255.0F;
					var20 = (float)(var18 >> 8 & 255) / 255.0F;
					float var29 = (float)(var18 & 255) / 255.0F;
					var22 = (float)(var18 >> 24 & 255) / 255.0F;
					GL11.glColor4f(var19, var20, var29, var22);
					this.mainModel.render(var1, var16, var15, var13, var11 - var10, var12, var14);

					for(int var28 = 0; var28 < 4; ++var28) {
						if(this.inheritRenderPass(var1, var28, var9) >= 0) {
							GL11.glColor4f(var19, var20, var29, var22);
							this.renderPassModel.render(var1, var16, var15, var13, var11 - var10, var12, var14);
						}
					}
				}

				GL11.glDepthFunc(515);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}

			GL11.glDisable('\u803a');
		}
		catch (Exception var25) {
			var25.printStackTrace();
		}

		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapEnabled);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapDisabled);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
		this.passSpecialRender(var1, var2, var4, var6);
	}

	protected void renderModel(EntityLiving var1, float var2, float var3, float var4, float var5, float var6, float var7) {
		this.loadDownloadableImageTexture(var1.skinUrl, var1.getEntityTexture());
		this.mainModel.render(var1, var2, var3, var4, var5, var6, var7);
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

	protected void renderEquippedItems(EntityLiving var1, float var2) {}

	protected int inheritRenderPass(EntityLiving var1, int var2, float var3) {
		return this.shouldRenderPass(var1, var2, var3);
	}

	protected int shouldRenderPass(EntityLiving var1, int var2, float var3) {
		return -1;
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
		if(Minecraft.isDebugInfoEnabled() && SpoutClient.getInstance().isEntityLabelCheat()) {
			this.renderLivingLabel(var1, Integer.toString(var1.entityId), var2, var4, var6, 64);
		}
		else {
			String title = var1.displayName;
			if (title != null && !title.equals("[hide]")) {
				String lines[] = title.split("\\n");
				for (int i = 0; i < lines.length; i++){
					renderLivingLabel(var1, lines[i], var2, var4 + (0.275D * (lines.length - i - 1)), var6, 64);
				}
			}
		}
		//Spout End
	}

	//Spout start
	protected void renderLivingLabel(EntityLiving var1, String var2, double var3, double var5, double var7, int var9) {
		renderLivingLabel(var1, var2, var3, var5, var7, var9, 0xFFFFFF, -1);
	}

	protected void renderLivingLabel(EntityLiving var1, String var2, double var3, double var5, double var7, int var9, int color, int color2) {
	//Spout end
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
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDepthMask(false);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(770, 771);
			Tessellator var14 = Tessellator.instance;
			byte var15 = 0;
			if(var2.equals("deadmau5")) {
				var15 = -10;
			}

			GL11.glDisable(GL11.GL_TEXTURE_2D);
			var14.startDrawingQuads();
			int var16 = var11.getStringWidth(var2) / 2;
			var14.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
			var14.addVertex((double)(-var16 - 1), (double)(-1 + var15), 0.0D);
			var14.addVertex((double)(-var16 - 1), (double)(8 + var15), 0.0D);
			var14.addVertex((double)(var16 + 1), (double)(8 + var15), 0.0D);
			var14.addVertex((double)(var16 + 1), (double)(-1 + var15), 0.0D);
			var14.draw();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			var11.drawString(var2, -var11.getStringWidth(var2) / 2, var15, color);  //Spout (changed to color var)
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(true);
			var11.drawString(var2, -var11.getStringWidth(var2) / 2, var15, color2);  //Spout (changed to color2 var)
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_BLEND);
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
