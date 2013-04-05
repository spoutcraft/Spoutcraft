package net.minecraft.src;

import org.lwjgl.opengl.GL11;
// Spout Start
import net.minecraft.client.Minecraft;
import org.spoutcraft.api.material.MaterialData;
import org.spoutcraft.client.HDImageBufferDownload;
import org.spoutcraft.client.player.accessories.AccessoryHandler;
import org.spoutcraft.client.player.accessories.AccessoryType;
import org.spoutcraft.client.special.VIP;
// Spout End

public class RenderPlayer extends RenderLiving {
	// Spout Start - private to public
	public ModelBiped modelBipedMain;
	// Spout End
	private ModelBiped modelArmorChestplate;
	private ModelBiped modelArmor;
	private static final String[] armorFilenamePrefix = new String[] {"cloth", "chain", "iron", "diamond", "gold"};

	public RenderPlayer() {
		super(new ModelBiped(0.0F), 0.5F);
		this.modelBipedMain = (ModelBiped)this.mainModel;
		this.modelArmorChestplate = new ModelBiped(1.0F);
		this.modelArmor = new ModelBiped(0.5F);
	}

	protected void func_98191_a(EntityPlayer par1EntityPlayer) {
		this.loadDownloadableImageTexture(par1EntityPlayer.skinUrl, par1EntityPlayer.getTexture());
	}

	/**
	 * Set the specified armor model as the player model. Args: player, armorSlot, partialTick
	 */
	protected int setArmorModel(EntityPlayer par1EntityPlayer, int par2, float par3) {
		ItemStack var4 = par1EntityPlayer.inventory.armorItemInSlot(3 - par2);

		if (var4 != null) {
			Item var5 = var4.getItem();

			if (var5 instanceof ItemArmor) {
				ItemArmor var6 = (ItemArmor)var5;
				this.loadTexture("/armor/" + armorFilenamePrefix[var6.renderIndex] + "_" + (par2 == 2 ? 2 : 1) + ".png");
				// Spout Start
				VIP vip = par1EntityPlayer.vip;
				int armorId = (par2 == 2 ? 2 : 1);
				if (vip != null && vip.getArmor(armorId) != null) {
					String url = vip.getArmor(armorId);
					if (!this.loadDownloadableImageTexture(url, (String) null)) {
						Minecraft.theMinecraft.renderEngine.obtainImageData(url, new HDImageBufferDownload());
					}
				}
				// Spout End
				ModelBiped var7 = par2 == 2 ? this.modelArmor : this.modelArmorChestplate;
				var7.bipedHead.showModel = par2 == 0;
				var7.bipedHeadwear.showModel = par2 == 0;
				var7.bipedBody.showModel = par2 == 1 || par2 == 2;
				var7.bipedRightArm.showModel = par2 == 1;
				var7.bipedLeftArm.showModel = par2 == 1;
				var7.bipedRightLeg.showModel = par2 == 2 || par2 == 3;
				var7.bipedLeftLeg.showModel = par2 == 2 || par2 == 3;
				this.setRenderPassModel(var7);

				if (var7 != null) {
					var7.onGround = this.mainModel.onGround;
				}

				if (var7 != null) {
					var7.isRiding = this.mainModel.isRiding;
				}

				if (var7 != null) {
					var7.isChild = this.mainModel.isChild;
				}

				float var8 = 1.0F;

				if (var6.getArmorMaterial() == EnumArmorMaterial.CLOTH) {
					int var9 = var6.getColor(var4);
					float var10 = (float)(var9 >> 16 & 255) / 255.0F;
					float var11 = (float)(var9 >> 8 & 255) / 255.0F;
					float var12 = (float)(var9 & 255) / 255.0F;
					GL11.glColor3f(var8 * var10, var8 * var11, var8 * var12);

					if (var4.isItemEnchanted()) {
						return 31;
					}

					return 16;
				}

				GL11.glColor3f(var8, var8, var8);

				if (var4.isItemEnchanted()) {
					return 15;
				}

				return 1;
			}
		}

		return -1;
	}

	protected void func_82439_b(EntityPlayer par1EntityPlayer, int par2, float par3) {
		ItemStack var4 = par1EntityPlayer.inventory.armorItemInSlot(3 - par2);

		if (var4 != null) {
			Item var5 = var4.getItem();

			if (var5 instanceof ItemArmor) {
				ItemArmor var6 = (ItemArmor)var5;
				this.loadTexture("/armor/" + armorFilenamePrefix[var6.renderIndex] + "_" + (par2 == 2 ? 2 : 1) + "_b.png");
				float var7 = 1.0F;
				GL11.glColor3f(var7, var7, var7);
			}
		}
	}

	public void renderPlayer(EntityPlayer par1EntityPlayer, double par2, double par4, double par6, float par8, float par9) {
		float var10 = 1.0F;
		GL11.glColor3f(var10, var10, var10);
		ItemStack var11 = par1EntityPlayer.inventory.getCurrentItem();
		this.modelArmorChestplate.heldItemRight = this.modelArmor.heldItemRight = this.modelBipedMain.heldItemRight = var11 != null ? 1 : 0;

		if (var11 != null && par1EntityPlayer.getItemInUseCount() > 0) {
			EnumAction var12 = var11.getItemUseAction();

			if (var12 == EnumAction.block) {
				this.modelArmorChestplate.heldItemRight = this.modelArmor.heldItemRight = this.modelBipedMain.heldItemRight = 3;
			} else if (var12 == EnumAction.bow) {
				this.modelArmorChestplate.aimedBow = this.modelArmor.aimedBow = this.modelBipedMain.aimedBow = true;
			}
		}

		this.modelArmorChestplate.isSneak = this.modelArmor.isSneak = this.modelBipedMain.isSneak = par1EntityPlayer.isSneaking();
		double var14 = par4 - (double)par1EntityPlayer.yOffset;

		if (par1EntityPlayer.isSneaking() && !(par1EntityPlayer instanceof EntityPlayerSP)) {
			var14 -= 0.125D;
		}
		// Spout Start - VIP
		if (!AccessoryHandler.isHandled(par1EntityPlayer.username)) {
			AccessoryHandler.addVIPAccessoriesFor(par1EntityPlayer);
		}

		VIP vip = par1EntityPlayer.vip;
		if (vip != null) {
			float s = vip.getScale();
			GL11.glPushMatrix();
			GL11.glTranslated(0, (s - 1) * 1.6, 0);
			GL11.glScalef(s, s, s);
			super.doRenderLiving(par1EntityPlayer, par2, var14, par6, par8, par9);
			GL11.glPopMatrix();
		} else {
		super.doRenderLiving(par1EntityPlayer, par2, var14, par6, par8, par9);
		}
		// Spout End
		this.modelArmorChestplate.aimedBow = this.modelArmor.aimedBow = this.modelBipedMain.aimedBow = false;
		this.modelArmorChestplate.isSneak = this.modelArmor.isSneak = this.modelBipedMain.isSneak = false;
		this.modelArmorChestplate.heldItemRight = this.modelArmor.heldItemRight = this.modelBipedMain.heldItemRight = 0;
	}

	// Spout Start
	/**
	 * Used to render a player's name above their head
	 */
	protected void renderName(EntityPlayer par1EntityPlayer, double par2, double par4, double par6) {
		if (!par1EntityPlayer.getHasActivePotion()) {
			if(Minecraft.isGuiEnabled() && (par1EntityPlayer != this.renderManager.livingPlayer || (Minecraft.theMinecraft.gameSettings.thirdPersonView != 0 && Minecraft.theMinecraft.currentScreen == null))) {
				float var8 = 1.6F;
				float var9 = 0.016666668F * var8;
				double var10 = par1EntityPlayer.getDistanceSqToEntity(this.renderManager.livingPlayer);
				float var12 = par1EntityPlayer.isSneaking() ? 32.0F : 64.0F;

				if (var10 < (double)(var12 * var12)) {
					String title = null;
					VIP vip = par1EntityPlayer.vip;
					if (vip != null) {
						title = vip.getTitle();
					} else {
						title = par1EntityPlayer.displayName;
					}
					float alpha = 0.25F;
					// If a plugin hasn't set a title, use the easter egg title (if one exists)
					/*if (EasterEggs.getEasterEggTitle(var1.username) != null && color == -1) {
					title = EasterEggs.getEasterEggTitle(var1.username);
					alpha = 0.0F;
				}*/
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

							if (!par1EntityPlayer.isSneaking()) {
								if (par1EntityPlayer.isPlayerSleeping()) {
									this.renderLivingLabel(par1EntityPlayer, title, par2, par4 - 1.5D, par6, 64);
								} else {
									this.renderLivingLabel(par1EntityPlayer, title, par2, par4, par6, 64);
									// TODO: Adapation needed.
									/*if (color != -1) {
									this.renderLivingLabel(var1, title, var2, var4, var6, 64, color, color);
								} else {
									this.renderLivingLabel(par1EntityPlayer, title, par2, par4, par6, 64);
								}*/
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

	/**
	 * Method for adding special render rules
	 */
	protected void renderSpecials(EntityPlayer par1EntityPlayer, float par2) {
		float var3 = 1.0F;
		GL11.glColor3f(var3, var3, var3);
		super.renderEquippedItems(par1EntityPlayer, par2);
		super.renderArrowsStuckInEntity(par1EntityPlayer, par2);
		ItemStack var4 = par1EntityPlayer.inventory.armorItemInSlot(3);

		if (var4 != null) {
			GL11.glPushMatrix();
			this.modelBipedMain.bipedHead.postRender(0.0625F);
			float var5;

			if (var4.getItem().itemID < 256) {
				if (RenderBlocks.renderItemIn3d(Block.blocksList[var4.itemID].getRenderType())) {
					var5 = 0.625F;
					GL11.glTranslatef(0.0F, -0.25F, 0.0F);
					GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
					GL11.glScalef(var5, -var5, -var5);
				}

				this.renderManager.itemRenderer.renderItem(par1EntityPlayer, var4, 0);
			} else if (var4.getItem().itemID == Item.skull.itemID) {
				var5 = 1.0625F;
				GL11.glScalef(var5, -var5, -var5);
				String var6 = "";

				if (var4.hasTagCompound() && var4.getTagCompound().hasKey("SkullOwner")) {
					var6 = var4.getTagCompound().getString("SkullOwner");
				}

				TileEntitySkullRenderer.skullRenderer.func_82393_a(-0.5F, 0.0F, -0.5F, 1, 180.0F, var4.getItemDamage(), var6);
			}

			GL11.glPopMatrix();
		}

		// Spout Start
		if (!par1EntityPlayer.getHasActivePotion()){
			AccessoryHandler.renderAllAccessories(par1EntityPlayer, 0.0625F, par2);
		}
		// Spout End

		float var8;
		float var7;

		if (par1EntityPlayer.username.equals("deadmau5") && this.loadDownloadableImageTexture(par1EntityPlayer.skinUrl, (String)null)) {
			for (int var20 = 0; var20 < 2; ++var20) {
				float var25 = par1EntityPlayer.prevRotationYaw + (par1EntityPlayer.rotationYaw - par1EntityPlayer.prevRotationYaw) * par2 - (par1EntityPlayer.prevRenderYawOffset + (par1EntityPlayer.renderYawOffset - par1EntityPlayer.prevRenderYawOffset) * par2);
				var7 = par1EntityPlayer.prevRotationPitch + (par1EntityPlayer.rotationPitch - par1EntityPlayer.prevRotationPitch) * par2;
				GL11.glPushMatrix();
				GL11.glRotatef(var25, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(var7, 1.0F, 0.0F, 0.0F);
				GL11.glTranslatef(0.375F * (float)(var20 * 2 - 1), 0.0F, 0.0F);
				GL11.glTranslatef(0.0F, -0.375F, 0.0F);
				GL11.glRotatef(-var7, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(-var25, 0.0F, 1.0F, 0.0F);
				var8 = 1.3333334F;
				GL11.glScalef(var8, var8, var8);
				this.modelBipedMain.renderEars(0.0625F);
				GL11.glPopMatrix();
			}
		}

		float var11;

		if (this.loadDownloadableImageTexture(par1EntityPlayer.cloakUrl, (String)null) && !par1EntityPlayer.getHasActivePotion() && !par1EntityPlayer.getHideCape()) {
			GL11.glPushMatrix();
			GL11.glTranslatef(0.0F, 0.0F, 0.125F);
			double var22 = par1EntityPlayer.field_71091_bM + (par1EntityPlayer.field_71094_bP - par1EntityPlayer.field_71091_bM) * (double)par2 - (par1EntityPlayer.prevPosX + (par1EntityPlayer.posX - par1EntityPlayer.prevPosX) * (double)par2);
			double var24 = par1EntityPlayer.field_71096_bN + (par1EntityPlayer.field_71095_bQ - par1EntityPlayer.field_71096_bN) * (double)par2 - (par1EntityPlayer.prevPosY + (par1EntityPlayer.posY - par1EntityPlayer.prevPosY) * (double)par2);
			double var9 = par1EntityPlayer.field_71097_bO + (par1EntityPlayer.field_71085_bR - par1EntityPlayer.field_71097_bO) * (double)par2 - (par1EntityPlayer.prevPosZ + (par1EntityPlayer.posZ - par1EntityPlayer.prevPosZ) * (double)par2);
			var11 = par1EntityPlayer.prevRenderYawOffset + (par1EntityPlayer.renderYawOffset - par1EntityPlayer.prevRenderYawOffset) * par2;
			double var12 = (double)MathHelper.sin(var11 * (float)Math.PI / 180.0F);
			double var14 = (double)(-MathHelper.cos(var11 * (float)Math.PI / 180.0F));
			float var16 = (float)var24 * 10.0F;

			if (var16 < -6.0F) {
				var16 = -6.0F;
			}

			if (var16 > 32.0F) {
				var16 = 32.0F;
			}

			float var17 = (float)(var22 * var12 + var9 * var14) * 100.0F;
			float var18 = (float)(var22 * var14 - var9 * var12) * 100.0F;

			if (var17 < 0.0F) {
				var17 = 0.0F;
			}

			float var19 = par1EntityPlayer.prevCameraYaw + (par1EntityPlayer.cameraYaw - par1EntityPlayer.prevCameraYaw) * par2;
			var16 += MathHelper.sin((par1EntityPlayer.prevDistanceWalkedModified + (par1EntityPlayer.distanceWalkedModified - par1EntityPlayer.prevDistanceWalkedModified) * par2) * 6.0F) * 32.0F * var19;

			if (par1EntityPlayer.isSneaking()) {
				var16 += 25.0F;
			}

			GL11.glRotatef(6.0F + var17 / 2.0F + var16, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(var18 / 2.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(-var18 / 2.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
			this.modelBipedMain.renderCloak(0.0625F);
			GL11.glPopMatrix();
		}

		ItemStack var21 = par1EntityPlayer.inventory.getCurrentItem();

		if (var21 != null) {
			GL11.glPushMatrix();
			this.modelBipedMain.bipedRightArm.postRender(0.0625F);
			GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);

			if (par1EntityPlayer.fishEntity != null) {
				var21 = new ItemStack(Item.stick);
			}

			EnumAction var23 = null;

			if (par1EntityPlayer.getItemInUseCount() > 0) {
				var23 = var21.getItemUseAction();
			}

			if (var21.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[var21.itemID].getRenderType())) {
				var7 = 0.5F;
				GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
				var7 *= 0.75F;
				GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(-var7, -var7, var7);
			} else if (var21.itemID == Item.bow.itemID) {
				var7 = 0.625F;
				GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
				GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(var7, -var7, var7);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				// Spout Start
			} else if (Item.itemsList[var21.itemID].isFull3D() || var21.itemID == Item.flint.itemID && MaterialData.getCustomItem(var21.getItemDamage()) instanceof org.spoutcraft.api.material.Tool) {
				// Spout End
				var7 = 0.625F;

				if (Item.itemsList[var21.itemID].shouldRotateAroundWhenRendering()) {
					GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
					GL11.glTranslatef(0.0F, -0.125F, 0.0F);
				}

				if (par1EntityPlayer.getItemInUseCount() > 0 && var23 == EnumAction.block) {
					GL11.glTranslatef(0.05F, 0.0F, -0.1F);
					GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(-10.0F, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(-60.0F, 0.0F, 0.0F, 1.0F);
				}

				GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
				GL11.glScalef(var7, -var7, var7);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			} else {
				var7 = 0.375F;
				GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
				GL11.glScalef(var7, var7, var7);
				GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
			}

			float var10;
			int var27;
			float var28;

			if (var21.getItem().requiresMultipleRenderPasses()) {
				for (var27 = 0; var27 <= 1; ++var27) {
					int var26 = var21.getItem().getColorFromItemStack(var21, var27);
					var28 = (float)(var26 >> 16 & 255) / 255.0F;
					var10 = (float)(var26 >> 8 & 255) / 255.0F;
					var11 = (float)(var26 & 255) / 255.0F;
					GL11.glColor4f(var28, var10, var11, 1.0F);
					this.renderManager.itemRenderer.renderItem(par1EntityPlayer, var21, var27);
				}
			} else {
				var27 = var21.getItem().getColorFromItemStack(var21, 0);
				var8 = (float)(var27 >> 16 & 255) / 255.0F;
				var28 = (float)(var27 >> 8 & 255) / 255.0F;
				var10 = (float)(var27 & 255) / 255.0F;
				GL11.glColor4f(var8, var28, var10, 1.0F);
				this.renderManager.itemRenderer.renderItem(par1EntityPlayer, var21, 0);
			}

			GL11.glPopMatrix();
		}
	}

	protected void renderPlayerScale(EntityPlayer par1EntityPlayer, float par2) {
		float var3 = 0.9375F;
		GL11.glScalef(var3, var3, var3);
	}

	protected void func_96450_a(EntityPlayer par1EntityPlayer, double par2, double par4, double par6, String par8Str, float par9, double par10) {
		if (par10 < 100.0D) {
			Scoreboard var12 = par1EntityPlayer.func_96123_co();
			ScoreObjective var13 = var12.func_96539_a(2);

			if (var13 != null) {
				Score var14 = var12.func_96529_a(par1EntityPlayer.getEntityName(), var13);

				if (par1EntityPlayer.isPlayerSleeping()) {
					this.renderLivingLabel(par1EntityPlayer, var14.func_96652_c() + " " + var13.func_96678_d(), par2, par4 - 1.5D, par6, 64);
				} else {
					this.renderLivingLabel(par1EntityPlayer, var14.func_96652_c() + " " + var13.func_96678_d(), par2, par4, par6, 64);
				}

				par4 += (double)((float)this.getFontRendererFromRenderManager().FONT_HEIGHT * 1.15F * par9);
			}
		}

		super.func_96449_a(par1EntityPlayer, par2, par4, par6, par8Str, par9, par10);
	}

	public void renderFirstPersonArm(EntityPlayer par1EntityPlayer) {
		float var2 = 1.0F;
		GL11.glColor3f(var2, var2, var2);
		this.modelBipedMain.onGround = 0.0F;
		this.modelBipedMain.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, par1EntityPlayer);
		this.modelBipedMain.bipedRightArm.render(0.0625F);
	}

	/**
	 * Renders player with sleeping offset if sleeping
	 */
	protected void renderPlayerSleep(EntityPlayer par1EntityPlayer, double par2, double par4, double par6) {
		if (par1EntityPlayer.isEntityAlive() && par1EntityPlayer.isPlayerSleeping()) {
			super.renderLivingAt(par1EntityPlayer, par2 + (double)par1EntityPlayer.field_71079_bU, par4 + (double)par1EntityPlayer.field_71082_cx, par6 + (double)par1EntityPlayer.field_71089_bV);
		} else {
			super.renderLivingAt(par1EntityPlayer, par2, par4, par6);
		}
	}

	/**
	 * Rotates the player if the player is sleeping. This method is called in rotateCorpse.
	 */
	protected void rotatePlayer(EntityPlayer par1EntityPlayer, float par2, float par3, float par4) {
		if (par1EntityPlayer.isEntityAlive() && par1EntityPlayer.isPlayerSleeping()) {
			GL11.glRotatef(par1EntityPlayer.getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(this.getDeathMaxRotation(par1EntityPlayer), 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
		} else {
			super.rotateCorpse(par1EntityPlayer, par2, par3, par4);
		}
	}

	protected void func_96449_a(EntityLiving par1EntityLiving, double par2, double par4, double par6, String par8Str, float par9, double par10) {
		this.func_96450_a((EntityPlayer)par1EntityLiving, par2, par4, par6, par8Str, par9, par10);
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args: entityLiving,
	 * partialTickTime
	 */
	protected void preRenderCallback(EntityLiving par1EntityLiving, float par2) {
		this.renderPlayerScale((EntityPlayer)par1EntityLiving, par2);
	}

	protected void func_82408_c(EntityLiving par1EntityLiving, int par2, float par3) {
		this.func_82439_b((EntityPlayer)par1EntityLiving, par2, par3);
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
		return this.setArmorModel((EntityPlayer)par1EntityLiving, par2, par3);
	}

	protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2) {
		this.renderSpecials((EntityPlayer)par1EntityLiving, par2);
	}

	protected void rotateCorpse(EntityLiving par1EntityLiving, float par2, float par3, float par4) {
		this.rotatePlayer((EntityPlayer)par1EntityLiving, par2, par3, par4);
	}

	/**
	 * Sets a simple glTranslate on a LivingEntity.
	 */
	protected void renderLivingAt(EntityLiving par1EntityLiving, double par2, double par4, double par6) {
		this.renderPlayerSleep((EntityPlayer)par1EntityLiving, par2, par4, par6);
	}

	protected void func_98190_a(EntityLiving par1EntityLiving) {
		this.func_98191_a((EntityPlayer)par1EntityLiving);
	}

	public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
		this.renderPlayer((EntityPlayer)par1EntityLiving, par2, par4, par6, par8, par9);
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
	 * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
	 * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
	 * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
	 */
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		this.renderPlayer((EntityPlayer)par1Entity, par2, par4, par6, par8, par9);
	}
}
