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
//Spout start
import org.lwjgl.opengl.GL13;
import org.spoutcraft.client.SpoutClient;
//Spout end

public class RenderLiving extends Render {

	protected ModelBase mainModel;
	protected ModelBase renderPassModel;

	public RenderLiving(ModelBase par1ModelBase, float par2) {
		this.mainModel = par1ModelBase;
		this.shadowSize = par2;
	}

	public void setRenderPassModel(ModelBase par1ModelBase) {
		this.renderPassModel = par1ModelBase;
	}

	private float func_48418_a(float par1, float par2, float par3) {
		float var4;
		for (var4 = par2 - par1; var4 < -180.0F; var4 += 360.0F) {
			;
		}

		while (var4 >= 180.0F) {
			var4 -= 360.0F;
		}

		return par1 + par3 * var4;
	}

	public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
		GL11.glPushMatrix();
		GL11.glDisable(2884);
		this.mainModel.onGround = this.renderSwingProgress(par1EntityLiving, par9);
		if (this.renderPassModel != null) {
			this.renderPassModel.onGround = this.mainModel.onGround;
		}

		this.mainModel.isRiding = par1EntityLiving.isRiding();
		if (this.renderPassModel != null) {
			this.renderPassModel.isRiding = this.mainModel.isRiding;
		}

		this.mainModel.isChild = par1EntityLiving.isChild();
		if (this.renderPassModel != null) {
			this.renderPassModel.isChild = this.mainModel.isChild;
		}

		try {
			float var10 = this.func_48418_a(par1EntityLiving.prevRenderYawOffset, par1EntityLiving.renderYawOffset, par9);
			float var11 = this.func_48418_a(par1EntityLiving.prevRotationYaw3, par1EntityLiving.prevRotationYaw2, par9);
			float var12 = par1EntityLiving.prevRotationPitch + (par1EntityLiving.rotationPitch - par1EntityLiving.prevRotationPitch) * par9;
			this.renderLivingAt(par1EntityLiving, par2, par4, par6);
			float var13 = this.handleRotationFloat(par1EntityLiving, par9);
			this.rotateCorpse(par1EntityLiving, var13, var10, par9);
			float var14 = 0.0625F;
			GL11.glEnable('\u803a');
			GL11.glScalef(-1.0F, -1.0F, 1.0F);
			this.preRenderCallback(par1EntityLiving, par9);
			GL11.glTranslatef(0.0F, -24.0F * var14 - 0.0078125F, 0.0F);
			float var15 = par1EntityLiving.field_705_Q + (par1EntityLiving.field_704_R - par1EntityLiving.field_705_Q) * par9;
			float var16 = par1EntityLiving.field_703_S - par1EntityLiving.field_704_R * (1.0F - par9);
			if (par1EntityLiving.isChild()) {
				var16 *= 3.0F;
			}

			if (var15 > 1.0F) {
				var15 = 1.0F;
			}

			GL11.glEnable(3008);
			this.mainModel.setLivingAnimations(par1EntityLiving, var16, var15, par9);
			this.renderModel(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);

			float var19;
			int var18;
			float var20;
			float var22;
			for (int var17 = 0; var17 < 4; ++var17) {
				var18 = this.shouldRenderPass(par1EntityLiving, var17, par9);
				if (var18 > 0) {
					this.renderPassModel.setLivingAnimations(par1EntityLiving, var16, var15, par9);
					this.renderPassModel.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);
					if (var18 == 15) {
						var19 = (float)par1EntityLiving.ticksExisted + par9;
						this.loadTexture("%blur%/misc/glint.png");
						GL11.glEnable(3042);
						var20 = 0.5F;
						GL11.glColor4f(var20, var20, var20, 1.0F);
						GL11.glDepthFunc(514);
						GL11.glDepthMask(false);

						for (int var21 = 0; var21 < 2; ++var21) {
							GL11.glDisable(2896);
							var22 = 0.76F;
							GL11.glColor4f(0.5F * var22, 0.25F * var22, 0.8F * var22, 1.0F);
							GL11.glBlendFunc(768, 1);
							GL11.glMatrixMode(5890);
							GL11.glLoadIdentity();
							float var23 = var19 * (0.0010F + (float)var21 * 0.0030F) * 20.0F;
							float var24 = 0.33333334F;
							GL11.glScalef(var24, var24, var24);
							GL11.glRotatef(30.0F - (float)var21 * 60.0F, 0.0F, 0.0F, 1.0F);
							GL11.glTranslatef(0.0F, var23, 0.0F);
							GL11.glMatrixMode(5888);
							this.renderPassModel.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);
						}

						GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
						GL11.glMatrixMode(5890);
						GL11.glDepthMask(true);
						GL11.glLoadIdentity();
						GL11.glMatrixMode(5888);
						GL11.glEnable(2896);
						GL11.glDisable(3042);
						GL11.glDepthFunc(515);
					}

					GL11.glDisable(3042);
					GL11.glEnable(3008);
				}
			}

			this.renderEquippedItems(par1EntityLiving, par9);
			float var26 = par1EntityLiving.getEntityBrightness(par9);
			var18 = this.getColorMultiplier(par1EntityLiving, var26, par9);
			OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapEnabled);
			GL11.glDisable(3553);
			OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapDisabled);
			if ((var18 >> 24 & 255) > 0 || par1EntityLiving.hurtTime > 0 || par1EntityLiving.deathTime > 0) {
				GL11.glDisable(3553);
				GL11.glDisable(3008);
				GL11.glEnable(3042);
				GL11.glBlendFunc(770, 771);
				GL11.glDepthFunc(514);
				if (par1EntityLiving.hurtTime > 0 || par1EntityLiving.deathTime > 0) {
					GL11.glColor4f(var26, 0.0F, 0.0F, 0.4F);
					this.mainModel.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);

					for (int var27 = 0; var27 < 4; ++var27) {
						if (this.inheritRenderPass(par1EntityLiving, var27, par9) >= 0) {
							GL11.glColor4f(var26, 0.0F, 0.0F, 0.4F);
							this.renderPassModel.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);
						}
					}
				}

				if ((var18 >> 24 & 255) > 0) {
					var19 = (float)(var18 >> 16 & 255) / 255.0F;
					var20 = (float)(var18 >> 8 & 255) / 255.0F;
					float var29 = (float)(var18 & 255) / 255.0F;
					var22 = (float)(var18 >> 24 & 255) / 255.0F;
					GL11.glColor4f(var19, var20, var29, var22);
					this.mainModel.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);

					for (int var28 = 0; var28 < 4; ++var28) {
						if (this.inheritRenderPass(par1EntityLiving, var28, par9) >= 0) {
							GL11.glColor4f(var19, var20, var29, var22);
							this.renderPassModel.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);
						}
					}
				}

				GL11.glDepthFunc(515);
				GL11.glDisable(3042);
				GL11.glEnable(3008);
				GL11.glEnable(3553);
			}

			GL11.glDisable('\u803a');
		} catch (Exception var25) {
			var25.printStackTrace();
		}

		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapEnabled);
		GL11.glEnable(3553);
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapDisabled);
		GL11.glEnable(2884);
		GL11.glPopMatrix();
		this.passSpecialRender(par1EntityLiving, par2, par4, par6);
	}

	protected void renderModel(EntityLiving par1EntityLiving, float par2, float par3, float par4, float par5, float par6, float par7) {
		this.loadDownloadableImageTexture(par1EntityLiving.skinUrl, par1EntityLiving.getEntityTexture());
		this.mainModel.render(par1EntityLiving, par2, par3, par4, par5, par6, par7);
	}

	protected void renderLivingAt(EntityLiving par1EntityLiving, double par2, double par4, double par6) {
		GL11.glTranslatef((float)par2, (float)par4, (float)par6);
	}

	protected void rotateCorpse(EntityLiving par1EntityLiving, float par2, float par3, float par4) {
		GL11.glRotatef(180.0F - par3, 0.0F, 1.0F, 0.0F);
		if (par1EntityLiving.deathTime > 0) {
			float var5 = ((float)par1EntityLiving.deathTime + par4 - 1.0F) / 20.0F * 1.6F;
			var5 = MathHelper.sqrt_float(var5);
			if (var5 > 1.0F) {
				var5 = 1.0F;
			}

			GL11.glRotatef(var5 * this.getDeathMaxRotation(par1EntityLiving), 0.0F, 0.0F, 1.0F);
		}

	}

	protected float renderSwingProgress(EntityLiving par1EntityLiving, float par2) {
		return par1EntityLiving.getSwingProgress(par2);
	}

	protected float handleRotationFloat(EntityLiving par1EntityLiving, float par2) {
		return (float)par1EntityLiving.ticksExisted + par2;
	}

	protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2) {}

	protected int inheritRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
		return this.shouldRenderPass(par1EntityLiving, par2, par3);
	}

	protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
		return -1;
	}

	protected float getDeathMaxRotation(EntityLiving par1EntityLiving) {
		return 90.0F;
	}

	protected int getColorMultiplier(EntityLiving par1EntityLiving, float par2, float par3) {
		return 0;
	}

	protected void preRenderCallback(EntityLiving par1EntityLiving, float par2) {}

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

	protected void renderLivingLabel(EntityLiving par1EntityLiving, String par2Str, double par3, double par5, double par7, int par9, int color, int color2) {
	//Spout end
		float var10 = par1EntityLiving.getDistanceToEntity(this.renderManager.livingPlayer);
		if (var10 <= (float)par9) {
			FontRenderer var11 = this.getFontRendererFromRenderManager();
			float var12 = 1.6F;
			float var13 = 0.016666668F * var12;
			GL11.glPushMatrix();
			GL11.glTranslatef((float)par3 + 0.0F, (float)par5 + 2.3F, (float)par7);
			GL11.glNormal3f(0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
			GL11.glScalef(-var13, -var13, var13);
			GL11.glDisable(2896);
			GL11.glDepthMask(false);
			GL11.glDisable(2929);
			GL11.glEnable(3042);
			GL11.glBlendFunc(770, 771);
			Tessellator var14 = Tessellator.instance;
			byte var15 = 0;
			if (par2Str.equals("deadmau5")) {
				var15 = -10;
			}

			GL11.glDisable(3553);
			var14.startDrawingQuads();
			int var16 = var11.getStringWidth(par2Str) / 2;
			var14.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
			var14.addVertex((double)(-var16 - 1), (double)(-1 + var15), 0.0D);
			var14.addVertex((double)(-var16 - 1), (double)(8 + var15), 0.0D);
			var14.addVertex((double)(var16 + 1), (double)(8 + var15), 0.0D);
			var14.addVertex((double)(var16 + 1), (double)(-1 + var15), 0.0D);
			var14.draw();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			var11.drawString(par2Str, -var11.getStringWidth(par2Str) / 2, var15, color);  //Spout (changed to color var)
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(true);
			var11.drawString(par2Str, -var11.getStringWidth(par2Str) / 2, var15, color2);  //Spout (changed to color2 var)
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glPopMatrix();
		}
	}

	
	
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		this.doRenderLiving((EntityLiving)par1Entity, par2, par4, par6, par8, par9);
	}
}
