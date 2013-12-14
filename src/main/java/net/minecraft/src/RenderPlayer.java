package net.minecraft.src;

import org.lwjgl.opengl.GL11;

import org.bukkit.ChatColor;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.player.accessories.AccessoryHandler;
import org.spoutcraft.client.player.accessories.AccessoryType;
import org.spoutcraft.client.special.VIP;
import org.spoutcraft.client.special.Resources;

public class RenderPlayer extends RenderLiving {
	private static final ResourceLocation steveTextures = new ResourceLocation("textures/entity/steve.png");
	
	// Spout Start - private to public
	public ModelBiped modelBipedMain;
	// Spout End
	private ModelBiped modelArmorChestplate;
	private ModelBiped modelArmor;	

	public RenderPlayer() {
		super(new ModelBiped(0.0F), 0.5F);
		this.modelBipedMain = (ModelBiped)this.mainModel;
		this.modelArmorChestplate = new ModelBiped(1.0F);
		this.modelArmor = new ModelBiped(0.5F);
	}

	/**
	 * Set the specified armor model as the player model. Args: player, armorSlot, partialTick
	 */
	//ToDo: Missing Spoutcraft API Custom Armor API code
	protected int setArmorModel(AbstractClientPlayer par1AbstractClientPlayer, int par2, float par3) {
		ItemStack var4 = par1AbstractClientPlayer.inventory.armorItemInSlot(3 - par2);

		if (var4 != null) {
			Item var5 = var4.getItem();

			if (var5 instanceof ItemArmor) {
				ItemArmor var6 = (ItemArmor)var5;
				this.bindTexture(RenderBiped.func_110857_a(var6, par2));
				ModelBiped var7 = par2 == 2 ? this.modelArmor : this.modelArmorChestplate;
				var7.bipedHead.showModel = par2 == 0;
				var7.bipedHeadwear.showModel = par2 == 0;
				var7.bipedBody.showModel = par2 == 1 || par2 == 2;
				var7.bipedRightArm.showModel = par2 == 1;
				var7.bipedLeftArm.showModel = par2 == 1;
				var7.bipedRightLeg.showModel = par2 == 2 || par2 == 3;
				var7.bipedLeftLeg.showModel = par2 == 2 || par2 == 3;
				this.setRenderPassModel(var7);
				var7.onGround = this.mainModel.onGround;
				var7.isRiding = this.mainModel.isRiding;
				var7.isChild = this.mainModel.isChild;
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

	protected void func_130220_b(AbstractClientPlayer par1AbstractClientPlayer, int par2, float par3) {
		ItemStack var4 = par1AbstractClientPlayer.inventory.armorItemInSlot(3 - par2);

		if (var4 != null) {
			Item var5 = var4.getItem();

			if (var5 instanceof ItemArmor) {
				this.bindTexture(RenderBiped.func_110858_a((ItemArmor)var5, par2, "overlay"));
				float var6 = 1.0F;
				GL11.glColor3f(var6, var6, var6);
			}
		}
	}

	//ToDo: Missing from this method (renderPlayer) is the Accessorize Code
	public void func_130009_a(AbstractClientPlayer par1AbstractClientPlayer, double par2, double par4, double par6, float par8, float par9) {
		float var10 = 1.0F;
		GL11.glColor3f(var10, var10, var10);
		ItemStack var11 = par1AbstractClientPlayer.inventory.getCurrentItem();
		this.modelArmorChestplate.heldItemRight = this.modelArmor.heldItemRight = this.modelBipedMain.heldItemRight = var11 != null ? 1 : 0;

		if (var11 != null && par1AbstractClientPlayer.getItemInUseCount() > 0) {
			EnumAction var12 = var11.getItemUseAction();

			if (var12 == EnumAction.block) {
				this.modelArmorChestplate.heldItemRight = this.modelArmor.heldItemRight = this.modelBipedMain.heldItemRight = 3;
			} else if (var12 == EnumAction.bow) {
				this.modelArmorChestplate.aimedBow = this.modelArmor.aimedBow = this.modelBipedMain.aimedBow = true;
			}
		}

		this.modelArmorChestplate.isSneak = this.modelArmor.isSneak = this.modelBipedMain.isSneak = par1AbstractClientPlayer.isSneaking();
		double var14 = par4 - (double)par1AbstractClientPlayer.yOffset;

		if (par1AbstractClientPlayer.isSneaking() && !(par1AbstractClientPlayer instanceof EntityPlayerSP)) {
			var14 -= 0.125D;
		}
		
		// Spout Start - VIP
		if (!AccessoryHandler.isHandled(par1AbstractClientPlayer.username)) {
			AccessoryHandler.addVIPAccessoriesFor(par1AbstractClientPlayer);
		}

		super.doRenderLiving(par1AbstractClientPlayer, par2, var14, par6, par8, par9);
		this.modelArmorChestplate.aimedBow = this.modelArmor.aimedBow = this.modelBipedMain.aimedBow = false;
		this.modelArmorChestplate.isSneak = this.modelArmor.isSneak = this.modelBipedMain.isSneak = false;
		this.modelArmorChestplate.heldItemRight = this.modelArmor.heldItemRight = this.modelBipedMain.heldItemRight = 0;
	}

	protected ResourceLocation func_110817_a(AbstractClientPlayer par1AbstractClientPlayer) {
		return par1AbstractClientPlayer.getLocationSkin();
	}
	
	/**
	 * Method for adding special render rules
	 */
	//ToDo: missing Accessorize API code and CustomMaterial code
	protected void renderSpecials(AbstractClientPlayer par1AbstractClientPlayer, float par2) {
		float var3 = 1.0F;
		GL11.glColor3f(var3, var3, var3);
		super.renderEquippedItems(par1AbstractClientPlayer, par2);
		super.renderArrowsStuckInEntity(par1AbstractClientPlayer, par2);
		ItemStack var4 = par1AbstractClientPlayer.inventory.armorItemInSlot(3);

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

				this.renderManager.itemRenderer.renderItem(par1AbstractClientPlayer, var4, 0);
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
		if (!par1AbstractClientPlayer.isInvisible()){
			AccessoryHandler.renderAllAccessories(par1AbstractClientPlayer, 0.0625F, par2);
		}
		// Spout End

		if (par1AbstractClientPlayer.getCommandSenderName().equals("deadmau5") && par1AbstractClientPlayer.getTextureSkin().isTextureUploaded()) {
			this.bindTexture(par1AbstractClientPlayer.getLocationSkin());

			for (int var23 = 0; var23 < 2; ++var23) {
				float var27 = par1AbstractClientPlayer.prevRotationYaw + (par1AbstractClientPlayer.rotationYaw - par1AbstractClientPlayer.prevRotationYaw) * par2 - (par1AbstractClientPlayer.prevRenderYawOffset + (par1AbstractClientPlayer.renderYawOffset - par1AbstractClientPlayer.prevRenderYawOffset) * par2);
				float var7 = par1AbstractClientPlayer.prevRotationPitch + (par1AbstractClientPlayer.rotationPitch - par1AbstractClientPlayer.prevRotationPitch) * par2;
				GL11.glPushMatrix();
				GL11.glRotatef(var27, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(var7, 1.0F, 0.0F, 0.0F);
				GL11.glTranslatef(0.375F * (float)(var23 * 2 - 1), 0.0F, 0.0F);
				GL11.glTranslatef(0.0F, -0.375F, 0.0F);
				GL11.glRotatef(-var7, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(-var27, 0.0F, 1.0F, 0.0F);
				float var8 = 1.3333334F;
				GL11.glScalef(var8, var8, var8);
				this.modelBipedMain.renderEars(0.0625F);
				GL11.glPopMatrix();
			}
		}

		boolean var24 = par1AbstractClientPlayer.getTextureCape().isTextureUploaded();
		boolean var25 = !par1AbstractClientPlayer.isInvisible();
		boolean var26 = !par1AbstractClientPlayer.getHideCape();
		float var14;

		if (var24 && var25 && var26) {
			this.bindTexture(par1AbstractClientPlayer.getLocationCape());
			GL11.glPushMatrix();
			GL11.glTranslatef(0.0F, 0.0F, 0.125F);
			double var29 = par1AbstractClientPlayer.field_71091_bM + (par1AbstractClientPlayer.field_71094_bP - par1AbstractClientPlayer.field_71091_bM) * (double)par2 - (par1AbstractClientPlayer.prevPosX + (par1AbstractClientPlayer.posX - par1AbstractClientPlayer.prevPosX) * (double)par2);
			double var10 = par1AbstractClientPlayer.field_71096_bN + (par1AbstractClientPlayer.field_71095_bQ - par1AbstractClientPlayer.field_71096_bN) * (double)par2 - (par1AbstractClientPlayer.prevPosY + (par1AbstractClientPlayer.posY - par1AbstractClientPlayer.prevPosY) * (double)par2);
			double var12 = par1AbstractClientPlayer.field_71097_bO + (par1AbstractClientPlayer.field_71085_bR - par1AbstractClientPlayer.field_71097_bO) * (double)par2 - (par1AbstractClientPlayer.prevPosZ + (par1AbstractClientPlayer.posZ - par1AbstractClientPlayer.prevPosZ) * (double)par2);
			var14 = par1AbstractClientPlayer.prevRenderYawOffset + (par1AbstractClientPlayer.renderYawOffset - par1AbstractClientPlayer.prevRenderYawOffset) * par2;
			double var15 = (double)MathHelper.sin(var14 * (float)Math.PI / 180.0F);
			double var17 = (double)(-MathHelper.cos(var14 * (float)Math.PI / 180.0F));
			float var19 = (float)var10 * 10.0F;

			if (var19 < -6.0F) {
				var19 = -6.0F;
			}

			if (var19 > 32.0F) {
				var19 = 32.0F;
			}

			float var20 = (float)(var29 * var15 + var12 * var17) * 100.0F;
			float var21 = (float)(var29 * var17 - var12 * var15) * 100.0F;

			if (var20 < 0.0F) {
				var20 = 0.0F;
			}

			float var22 = par1AbstractClientPlayer.prevCameraYaw + (par1AbstractClientPlayer.cameraYaw - par1AbstractClientPlayer.prevCameraYaw) * par2;
			var19 += MathHelper.sin((par1AbstractClientPlayer.prevDistanceWalkedModified + (par1AbstractClientPlayer.distanceWalkedModified - par1AbstractClientPlayer.prevDistanceWalkedModified) * par2) * 6.0F) * 32.0F * var22;

			if (par1AbstractClientPlayer.isSneaking()) {
				var19 += 25.0F;
			}

			GL11.glRotatef(6.0F + var20 / 2.0F + var19, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(var21 / 2.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(-var21 / 2.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
			this.modelBipedMain.renderCloak(0.0625F);
			GL11.glPopMatrix();
		}

		ItemStack var28 = par1AbstractClientPlayer.inventory.getCurrentItem();

		if (var28 != null) {
			GL11.glPushMatrix();
			this.modelBipedMain.bipedRightArm.postRender(0.0625F);
			GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);

			if (par1AbstractClientPlayer.fishEntity != null) {
				var28 = new ItemStack(Item.stick);
			}

			EnumAction var9 = null;

			if (par1AbstractClientPlayer.getItemInUseCount() > 0) {
				var9 = var28.getItemUseAction();
			}

			float var31;

			if (var28.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[var28.itemID].getRenderType())) {
				var31 = 0.5F;
				GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
				var31 *= 0.75F;
				GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(-var31, -var31, var31);
			} else if (var28.itemID == Item.bow.itemID) {
				var31 = 0.625F;
				GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
				GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(var31, -var31, var31);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			} else if (Item.itemsList[var28.itemID].isFull3D()) {
				var31 = 0.625F;

				if (Item.itemsList[var28.itemID].shouldRotateAroundWhenRendering()) {
					GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
					GL11.glTranslatef(0.0F, -0.125F, 0.0F);
				}

				if (par1AbstractClientPlayer.getItemInUseCount() > 0 && var9 == EnumAction.block) {
					GL11.glTranslatef(0.05F, 0.0F, -0.1F);
					GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(-10.0F, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(-60.0F, 0.0F, 0.0F, 1.0F);
				}

				GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
				GL11.glScalef(var31, -var31, var31);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			} else {
				var31 = 0.375F;
				GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
				GL11.glScalef(var31, var31, var31);
				GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
			}

			float var13;
			float var32;
			int var33;

			if (var28.getItem().requiresMultipleRenderPasses()) {
				for (var33 = 0; var33 <= 1; ++var33) {
					int var11 = var28.getItem().getColorFromItemStack(var28, var33);
					var32 = (float)(var11 >> 16 & 255) / 255.0F;
					var13 = (float)(var11 >> 8 & 255) / 255.0F;
					var14 = (float)(var11 & 255) / 255.0F;
					GL11.glColor4f(var32, var13, var14, 1.0F);
					this.renderManager.itemRenderer.renderItem(par1AbstractClientPlayer, var28, var33);
				}
			} else {
				var33 = var28.getItem().getColorFromItemStack(var28, 0);
				float var30 = (float)(var33 >> 16 & 255) / 255.0F;
				var32 = (float)(var33 >> 8 & 255) / 255.0F;
				var13 = (float)(var33 & 255) / 255.0F;
				GL11.glColor4f(var30, var32, var13, 1.0F);
				this.renderManager.itemRenderer.renderItem(par1AbstractClientPlayer, var28, 0);
			}

			GL11.glPopMatrix();
		}
	}

	protected void renderPlayerScale(AbstractClientPlayer par1AbstractClientPlayer, float par2) {
		float var3 = 0.9375F;
		// Spout Start
		String cleanUserName = ChatColor.stripColor(par1AbstractClientPlayer.getEntityName());
		VIP vip = Resources.getVIP(cleanUserName);
		
		if (vip != null) {
			var3 = vip.getScale();
		}
		// Spout End
		GL11.glScalef(var3, var3, var3);
	}

	protected void func_96450_a(AbstractClientPlayer par1AbstractClientPlayer, double par2, double par4, double par6, String par8Str, float par9, double par10) {
		if (par10 < 100.0D) {
			Scoreboard var12 = par1AbstractClientPlayer.getWorldScoreboard();
			ScoreObjective var13 = var12.func_96539_a(2);

			if (var13 != null) {
				Score var14 = var12.func_96529_a(par1AbstractClientPlayer.getEntityName(), var13);

				if (par1AbstractClientPlayer.isPlayerSleeping()) {
					this.renderLivingLabel(par1AbstractClientPlayer, var14.getScorePoints() + " " + var13.getDisplayName(), par2, par4 - 1.5D, par6, 64);
				} else {
					this.renderLivingLabel(par1AbstractClientPlayer, var14.getScorePoints() + " " + var13.getDisplayName(), par2, par4, par6, 64);
				}

				par4 += (double)((float)this.getFontRendererFromRenderManager().FONT_HEIGHT * 1.15F * par9);
			}
		}

		super.func_96449_a(par1AbstractClientPlayer, par2, par4, par6, par8Str, par9, par10);
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
	protected void renderPlayerSleep(AbstractClientPlayer par1AbstractClientPlayer, double par2, double par4, double par6) {
		if (par1AbstractClientPlayer.isEntityAlive() && par1AbstractClientPlayer.isPlayerSleeping()) {
			super.renderLivingAt(par1AbstractClientPlayer, par2 + (double)par1AbstractClientPlayer.field_71079_bU, par4 + (double)par1AbstractClientPlayer.field_71082_cx, par6 + (double)par1AbstractClientPlayer.field_71089_bV);
		} else {
			super.renderLivingAt(par1AbstractClientPlayer, par2, par4, par6);
		}
	}

	/**
	 * Rotates the player if the player is sleeping. This method is called in rotateCorpse.
	 */
	protected void rotatePlayer(AbstractClientPlayer par1AbstractClientPlayer, float par2, float par3, float par4) {
		if (par1AbstractClientPlayer.isEntityAlive() && par1AbstractClientPlayer.isPlayerSleeping()) {
			GL11.glRotatef(par1AbstractClientPlayer.getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(this.getDeathMaxRotation(par1AbstractClientPlayer), 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
		} else {
			super.rotateCorpse(par1AbstractClientPlayer, par2, par3, par4);
		}
	}

	protected void func_96449_a(EntityLivingBase par1EntityLivingBase, double par2, double par4, double par6, String par8Str, float par9, double par10) {
		this.func_96450_a((AbstractClientPlayer)par1EntityLivingBase, par2, par4, par6, par8Str, par9, par10);
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args: entityLiving,
	 * partialTickTime
	 */
	protected void preRenderCallback(EntityLivingBase par1EntityLivingBase, float par2) {
		this.renderPlayerScale((AbstractClientPlayer)par1EntityLivingBase, par2);
	}

	protected void func_82408_c(EntityLivingBase par1EntityLivingBase, int par2, float par3) {
		this.func_130220_b((AbstractClientPlayer)par1EntityLivingBase, par2, par3);
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	protected int shouldRenderPass(EntityLivingBase par1EntityLivingBase, int par2, float par3) {
		return this.setArmorModel((AbstractClientPlayer)par1EntityLivingBase, par2, par3);
	}

	protected void renderEquippedItems(EntityLivingBase par1EntityLivingBase, float par2) {
		this.renderSpecials((AbstractClientPlayer)par1EntityLivingBase, par2);
	}

	protected void rotateCorpse(EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4) {
		this.rotatePlayer((AbstractClientPlayer)par1EntityLivingBase, par2, par3, par4);
	}

	/**
	 * Sets a simple glTranslate on a LivingEntity.
	 */
	protected void renderLivingAt(EntityLivingBase par1EntityLivingBase, double par2, double par4, double par6) {
		this.renderPlayerSleep((AbstractClientPlayer)par1EntityLivingBase, par2, par4, par6);
	}

	public void doRenderLiving(EntityLivingBase par1EntityLivingBase, double par2, double par4, double par6, float par8, float par9) {
		this.func_130009_a((AbstractClientPlayer)par1EntityLivingBase, par2, par4, par6, par8, par9);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(Entity par1Entity) {
		return this.func_110817_a((AbstractClientPlayer)par1Entity);
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
	 * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
	 * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
	 * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
	 */
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		this.func_130009_a((AbstractClientPlayer)par1Entity, par2, par4, par6, par8, par9);
	}
}
