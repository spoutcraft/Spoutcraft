package net.minecraft.src;

import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
// MCPatcher Start
import com.prupe.mcpatcher.mod.MobRandomizer;
import com.prupe.mcpatcher.mod.CITUtils;
// MCPatcher End
// Spout Start
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.player.accessories.AccessoryHandler;
import org.spoutcraft.client.player.accessories.AccessoryType;
import org.spoutcraft.client.special.VIP;
// Spout End

public class RenderLiving extends Render {
	protected ModelBase mainModel;

	/** The model to be used during the render passes. */
	protected ModelBase renderPassModel;

	public RenderLiving(ModelBase par1ModelBase, float par2) {
		this.mainModel = par1ModelBase;
		this.shadowSize = par2;
	}

	/**
	 * Sets the model to be used in the current render pass (the first render pass is done after the primary model is
	 * rendered) Args: model
	 */
	public void setRenderPassModel(ModelBase par1ModelBase) {
		this.renderPassModel = par1ModelBase;
	}

	/**
	 * Returns a rotation angle that is inbetween two other rotation angles. par1 and par2 are the angles between which to
	 * interpolate, par3 is probably a float between 0.0 and 1.0 that tells us where "between" the two angles we are.
	 * Example: par1 = 30, par2 = 50, par3 = 0.5, then return = 40
	 */
	private float interpolateRotation(float par1, float par2, float par3) {
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
		GL11.glDisable(GL11.GL_CULL_FACE);
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
			float var10 = this.interpolateRotation(par1EntityLiving.prevRenderYawOffset, par1EntityLiving.renderYawOffset, par9);
			float var11 = this.interpolateRotation(par1EntityLiving.prevRotationYawHead, par1EntityLiving.rotationYawHead, par9);
			float var12 = par1EntityLiving.prevRotationPitch + (par1EntityLiving.rotationPitch - par1EntityLiving.prevRotationPitch) * par9;
			this.renderLivingAt(par1EntityLiving, par2, par4, par6);
			float var13 = this.handleRotationFloat(par1EntityLiving, par9);
			this.rotateCorpse(par1EntityLiving, var13, var10, par9);
			float var14 = 0.0625F;
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glScalef(-1.0F, -1.0F, 1.0F);
			this.preRenderCallback(par1EntityLiving, par9);
			GL11.glTranslatef(0.0F, -24.0F * var14 - 0.0078125F, 0.0F);
			float var15 = par1EntityLiving.prevLimbYaw + (par1EntityLiving.limbYaw - par1EntityLiving.prevLimbYaw) * par9;
			float var16 = par1EntityLiving.limbSwing - par1EntityLiving.limbYaw * (1.0F - par9);

			if (par1EntityLiving.isChild()) {
				var16 *= 3.0F;
			}

			if (var15 > 1.0F) {
				var15 = 1.0F;
			}

			GL11.glEnable(GL11.GL_ALPHA_TEST);
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

					if ((var18 & 240) == 16) {
						this.func_82408_c(par1EntityLiving, var17, par9);
						this.renderPassModel.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);
					}

					if (CITUtils.setupArmorOverlays(par1EntityLiving, var17)) {
						while (CITUtils.preRenderArmorOverlay()) {
							this.renderPassModel.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);
							CITUtils.postRenderArmorOverlay();
						}
					} else if ((var18 & 15) == 15) {
						var19 = (float)par1EntityLiving.ticksExisted + par9;
						this.loadTexture("%blur%/misc/glint.png");
						GL11.glEnable(GL11.GL_BLEND);
						var20 = 0.5F;
						GL11.glColor4f(var20, var20, var20, 1.0F);
						GL11.glDepthFunc(GL11.GL_EQUAL);
						GL11.glDepthMask(false);

						for (int var21 = 0; var21 < 2; ++var21) {
							GL11.glDisable(GL11.GL_LIGHTING);
							var22 = 0.76F;
							GL11.glColor4f(0.5F * var22, 0.25F * var22, 0.8F * var22, 1.0F);
							GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
							GL11.glMatrixMode(GL11.GL_TEXTURE);
							GL11.glLoadIdentity();
							float var23 = var19 * (0.001F + (float)var21 * 0.003F) * 20.0F;
							float var24 = 0.33333334F;
							GL11.glScalef(var24, var24, var24);
							GL11.glRotatef(30.0F - (float)var21 * 60.0F, 0.0F, 0.0F, 1.0F);
							GL11.glTranslatef(0.0F, var23, 0.0F);
							GL11.glMatrixMode(GL11.GL_MODELVIEW);
							this.renderPassModel.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);
						}

						GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
						GL11.glMatrixMode(GL11.GL_TEXTURE);
						GL11.glDepthMask(true);
						GL11.glLoadIdentity();
						GL11.glMatrixMode(GL11.GL_MODELVIEW);
						GL11.glEnable(GL11.GL_LIGHTING);
						GL11.glDisable(GL11.GL_BLEND);
						GL11.glDepthFunc(GL11.GL_LEQUAL);
					}

					GL11.glDisable(GL11.GL_BLEND);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
				}
			}

			GL11.glDepthMask(true);
			this.renderEquippedItems(par1EntityLiving, par9);
			float var26 = par1EntityLiving.getBrightness(par9);
			var18 = this.getColorMultiplier(par1EntityLiving, var26, par9);
			OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);

			if ((var18 >> 24 & 255) > 0 || par1EntityLiving.hurtTime > 0 || par1EntityLiving.deathTime > 0) {
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glDepthFunc(GL11.GL_EQUAL);

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

				GL11.glDepthFunc(GL11.GL_LEQUAL);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}

			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		} catch (Exception var25) {
			var25.printStackTrace();
		}

		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
		this.passSpecialRender(par1EntityLiving, par2, par4, par6);
	}

	/**
	 * Renders the model in RenderLiving
	 */
	protected void renderModel(EntityLiving par1EntityLiving, float par2, float par3, float par4, float par5, float par6, float par7) {
		this.func_98190_a(par1EntityLiving);

		if (!par1EntityLiving.isInvisible()) {
			this.mainModel.render(par1EntityLiving, par2, par3, par4, par5, par6, par7);
		} else if (!par1EntityLiving.func_98034_c(Minecraft.getMinecraft().thePlayer)) {
			GL11.glPushMatrix();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.15F);
			GL11.glDepthMask(false);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
			this.mainModel.render(par1EntityLiving, par2, par3, par4, par5, par6, par7);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
			GL11.glPopMatrix();
			GL11.glDepthMask(true);
		} else {
			this.mainModel.setRotationAngles(par2, par3, par4, par5, par6, par7, par1EntityLiving);
		}
	}

	protected void func_98190_a(EntityLiving par1EntityLiving) {
		this.loadTexture(MobRandomizer.randomTexture(par1EntityLiving));
	}

	/**
	 * Sets a simple glTranslate on a LivingEntity.
	 */
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

	/**
	 * Defines what float the third param in setRotationAngles of ModelBase is
	 */
	protected float handleRotationFloat(EntityLiving par1EntityLiving, float par2) {
		return (float)par1EntityLiving.ticksExisted + par2;
	}

	protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2) {}

	/**
	 * renders arrows the Entity has been attacked with, attached to it
	 */
	protected void renderArrowsStuckInEntity(EntityLiving par1EntityLiving, float par2) {
		int var3 = par1EntityLiving.getArrowCountInEntity();

		if (var3 > 0) {
			EntityArrow var4 = new EntityArrow(par1EntityLiving.worldObj, par1EntityLiving.posX, par1EntityLiving.posY, par1EntityLiving.posZ);
			Random var5 = new Random((long)par1EntityLiving.entityId);
			RenderHelper.disableStandardItemLighting();

			for (int var6 = 0; var6 < var3; ++var6) {
				GL11.glPushMatrix();
				ModelRenderer var7 = this.mainModel.getRandomModelBox(var5);
				ModelBox var8 = (ModelBox)var7.cubeList.get(var5.nextInt(var7.cubeList.size()));
				var7.postRender(0.0625F);
				float var9 = var5.nextFloat();
				float var10 = var5.nextFloat();
				float var11 = var5.nextFloat();
				float var12 = (var8.posX1 + (var8.posX2 - var8.posX1) * var9) / 16.0F;
				float var13 = (var8.posY1 + (var8.posY2 - var8.posY1) * var10) / 16.0F;
				float var14 = (var8.posZ1 + (var8.posZ2 - var8.posZ1) * var11) / 16.0F;
				GL11.glTranslatef(var12, var13, var14);
				var9 = var9 * 2.0F - 1.0F;
				var10 = var10 * 2.0F - 1.0F;
				var11 = var11 * 2.0F - 1.0F;
				var9 *= -1.0F;
				var10 *= -1.0F;
				var11 *= -1.0F;
				float var15 = MathHelper.sqrt_float(var9 * var9 + var11 * var11);
				var4.prevRotationYaw = var4.rotationYaw = (float)(Math.atan2((double)var9, (double)var11) * 180.0D / Math.PI);
				var4.prevRotationPitch = var4.rotationPitch = (float)(Math.atan2((double)var10, (double)var15) * 180.0D / Math.PI);
				double var16 = 0.0D;
				double var18 = 0.0D;
				double var20 = 0.0D;
				float var22 = 0.0F;
				this.renderManager.renderEntityWithPosYaw(var4, var16, var18, var20, var22, par2);
				GL11.glPopMatrix();
			}

			RenderHelper.enableStandardItemLighting();
		}
	}

	protected int inheritRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
		return this.shouldRenderPass(par1EntityLiving, par2, par3);
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
		return -1;
	}

	protected void func_82408_c(EntityLiving par1EntityLiving, int par2, float par3) {}

	protected float getDeathMaxRotation(EntityLiving par1EntityLiving) {
		return 90.0F;
	}

	/**
	 * Returns an ARGB int color back. Args: entityLiving, lightBrightness, partialTickTime
	 */
	protected int getColorMultiplier(EntityLiving par1EntityLiving, float par2, float par3) {
		return 0;
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args: entityLiving,
	 * partialTickTime
	 */
	protected void preRenderCallback(EntityLiving par1EntityLiving, float par2) {}

	/**
	 * Passes the specialRender and renders it
	 */
	protected void passSpecialRender(EntityLiving par1EntityLiving, double par2, double par4, double par6) {
		// Spout Start - Include renderName method here previously located in RenderPlayer.
		if (!(par1EntityLiving instanceof EntityPlayer)) { // Renderer for Entities since they can have a custom name (wolves for example)
			if (Minecraft.isDebugInfoEnabled() && SpoutClient.getInstance().isEntityLabelCheat()) {
				this.renderLivingLabel(par1EntityLiving, Integer.toString(par1EntityLiving.entityId), par2, par4, par6, 64);
			} else if (par1EntityLiving.func_94059_bO() || par1EntityLiving.func_94056_bM() && par1EntityLiving == this.renderManager.field_96451_i) {
				String title = par1EntityLiving.getTranslatedEntityName();
				if (title != null && !title.equals("[hide]")) {
					String lines[] = title.split("\\n");
					for (int i = 0; i < lines.length; i++){
						renderLivingLabel(par1EntityLiving, lines[i], par2, par4 + (0.275D * (lines.length - i - 1)), par6, 64);
					}
				}
			}
		} else if (par1EntityLiving instanceof EntityPlayer) {
			EntityPlayer par1EntityPlayer = (EntityPlayer) par1EntityLiving;
			if (!par1EntityPlayer.isInvisible()) {
				if(Minecraft.isGuiEnabled() && (par1EntityPlayer != this.renderManager.livingPlayer || (Minecraft.theMinecraft.gameSettings.thirdPersonView != 0 && Minecraft.theMinecraft.currentScreen == null))) {
					float var8 = 1.6F;
					float var9 = 0.016666668F * var8;
					double var10 = par1EntityPlayer.getDistanceSqToEntity(this.renderManager.livingPlayer);
					float var12 = par1EntityPlayer.isSneaking() ? 32.0F : 64.0F;

					if (var10 < (double)(var12 * var12)) {
						String title = null;
						VIP vip = par1EntityPlayer.vip;
						float var92 = 0.0F;
						if (vip != null) {
							title = vip.getTitle();
							var92 = vip.getScale();
						} else {
							title = par1EntityPlayer.displayName;
						}
						float alpha = 0.25F;
						
						if (!title.equals("[hide]")) {
							String lines[] = title.split("\\n");
							double y = par4;
							for (int line = 0; line < lines.length; line++) {
								title = lines[line];
								par4 = y + (0.275D * (lines.length - line - 1));

								if (AccessoryHandler.hasAccessory(par1EntityPlayer.username, AccessoryType.NOTCHHAT)) {
									par4 = par4 + 0.275d;
								} else if (AccessoryHandler.hasAccessory(par1EntityPlayer.username, AccessoryType.TOPHAT)) {
									par4 = par4 + 0.5d;
								}
								
								if (var92 > 0.9375F) {
									par4 = par4 + 0.5D;
								} else if (var92 < 0.86F && var92 != 0.0F) {
									par4 = par4 - 0.25D;
								}

								if (!par1EntityPlayer.isSneaking()) {
									if (par1EntityPlayer.isPlayerSleeping()) {
										this.renderLivingLabel(par1EntityPlayer, title, par2, par4 - 1.5D, par6, 64);
									} else {
										this.renderLivingLabel(par1EntityPlayer, title, par2, par4, par6, 64);
									}
								} else {
									title = org.bukkit.ChatColor.stripColor(title); //strip colors when sneaking
									FontRenderer var14 = this.getFontRendererFromRenderManager();
									GL11.glPushMatrix();
									GL11.glTranslatef((float)par2 + 0.0F, (float)par4 + 2.3F, (float)par6);
									GL11.glNormal3f(0.0F, 1.0F, 0.0F);
									GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
									GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
									GL11.glScalef(-var9, -var9, var9);
									GL11.glDisable(GL11.GL_LIGHTING);
									GL11.glTranslatef(0.0F, 0.25F / var9, 0.0F);
									GL11.glDepthMask(false);

									GL11.glEnable(GL11.GL_BLEND);
									GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
									Tessellator var15 = Tessellator.instance;
									GL11.glDisable(GL11.GL_TEXTURE_2D);
									var15.startDrawingQuads();
									int var16 = var14.getStringWidth(title) / 2;
									var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
									var15.addVertex((double)(-var16 - 1), -1.0D, 0.0D);
									var15.addVertex((double)(-var16 - 1), 8.0D, 0.0D);
									var15.addVertex((double)(var16 + 1), 8.0D, 0.0D);
									var15.addVertex((double)(var16 + 1), -1.0D, 0.0D);
									var15.draw();
									GL11.glEnable(GL11.GL_TEXTURE_2D);
									GL11.glDepthMask(true);
									var14.drawString(title, -var14.getStringWidth(title) / 2, 0, 553648127);
									GL11.glEnable(GL11.GL_LIGHTING);
									GL11.glDisable(GL11.GL_BLEND);
									GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
									GL11.glPopMatrix();
								}
							}
						}
					}
				}
			}
		}
		// Spout End
	}

	protected void func_96449_a(EntityLiving par1EntityLiving, double par2, double par4, double par6, String par8Str, float par9, double par10) {
		if (par1EntityLiving.isPlayerSleeping()) {
			this.renderLivingLabel(par1EntityLiving, par8Str, par2, par4 - 1.5D, par6, 64);
		} else {
			this.renderLivingLabel(par1EntityLiving, par8Str, par2, par4, par6, 64);
		}
	}

	/**
	 * Draws the debug or playername text above a living
	 */
	protected void renderLivingLabel(EntityLiving par1EntityLiving, String par2Str, double par3, double par5, double par7, int par9) {
		double var10 = par1EntityLiving.getDistanceSqToEntity(this.renderManager.livingPlayer);

		if (var10 <= (double)(par9 * par9)) {
			FontRenderer var12 = this.getFontRendererFromRenderManager();
			float var13 = 1.6F;
			float var14 = 0.016666668F * var13;
			GL11.glPushMatrix();
			GL11.glTranslatef((float)par3 + 0.0F, (float)par5 + par1EntityLiving.height + 0.5F, (float)par7);
			GL11.glNormal3f(0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
			GL11.glScalef(-var14, -var14, var14);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDepthMask(false);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			Tessellator var15 = Tessellator.instance;
			byte var16 = 0;

			if (par2Str.equals("deadmau5")) {
				var16 = -10;
			}

			GL11.glDisable(GL11.GL_TEXTURE_2D);
			var15.startDrawingQuads();
			int var17 = var12.getStringWidth(par2Str) / 2;
			var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
			var15.addVertex((double)(-var17 - 1), (double)(-1 + var16), 0.0D);
			var15.addVertex((double)(-var17 - 1), (double)(8 + var16), 0.0D);
			var15.addVertex((double)(var17 + 1), (double)(8 + var16), 0.0D);
			var15.addVertex((double)(var17 + 1), (double)(-1 + var16), 0.0D);
			var15.draw();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			var12.drawString(par2Str, -var12.getStringWidth(par2Str) / 2, var16, 553648127);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(true);
			var12.drawString(par2Str, -var12.getStringWidth(par2Str) / 2, var16, -1);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glPopMatrix();
		}
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
	 * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
	 * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
	 * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
	 */
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		this.doRenderLiving((EntityLiving)par1Entity, par2, par4, par6, par8, par9);
	}
}
