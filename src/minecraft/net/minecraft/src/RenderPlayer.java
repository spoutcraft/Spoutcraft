package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.EnumAction;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemArmor;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.RenderLiving;
import net.minecraft.src.Tessellator;

//Spout Start
import org.bukkit.ChatColor;
import org.spoutcraft.client.special.EasterEggs;
import org.spoutcraft.spoutcraftapi.material.CustomItem;
import org.spoutcraft.spoutcraftapi.material.MaterialData;
//spout End
import org.lwjgl.opengl.GL11;

public class RenderPlayer extends RenderLiving {

	private ModelBiped modelBipedMain;
	private ModelBiped modelArmorChestplate;
	private ModelBiped modelArmor;
	private static final String[] armorFilenamePrefix = new String[]{"cloth", "chain", "iron", "diamond", "gold"};

	public RenderPlayer() {
		super(new ModelBiped(0.0F), 0.5F);
		this.modelBipedMain = (ModelBiped)this.mainModel;
		this.modelArmorChestplate = new ModelBiped(1.0F);
		this.modelArmor = new ModelBiped(0.5F);
	}

	protected int setArmorModel(EntityPlayer var1, int var2, float var3) {
		ItemStack var4 = var1.inventory.armorItemInSlot(3 - var2);
		if(var4 != null) {
			Item var5 = var4.getItem();
			if(var5 instanceof ItemArmor) {
				ItemArmor var6 = (ItemArmor)var5;
				this.loadTexture("/armor/" + armorFilenamePrefix[var6.renderIndex] + "_" + (var2 == 2?2:1) + ".png");
				ModelBiped var7 = var2 == 2?this.modelArmor:this.modelArmorChestplate;
				var7.bipedHead.showModel = var2 == 0;
				var7.bipedHeadwear.showModel = var2 == 0;
				var7.bipedBody.showModel = var2 == 1 || var2 == 2;
				var7.bipedRightArm.showModel = var2 == 1;
				var7.bipedLeftArm.showModel = var2 == 1;
				var7.bipedRightLeg.showModel = var2 == 2 || var2 == 3;
				var7.bipedLeftLeg.showModel = var2 == 2 || var2 == 3;
				this.setRenderPassModel(var7);
				if (var4.isItemEnchanted()) {
					return 15;
				}

				return 1;
			}
		}

		return -1;
	}

	public void renderPlayer(EntityPlayer var1, double var2, double var4, double var6, float var8, float var9) {
		ItemStack var10 = var1.inventory.getCurrentItem();
		this.modelArmorChestplate.heldItemRight = this.modelArmor.heldItemRight = this.modelBipedMain.heldItemRight = var10 != null ? 1 : 0;
		if(var10 != null && var1.func_35205_Y() > 0) {
			EnumAction var11 = var10.getItemUseAction();
			if(var11 == EnumAction.block) {
				this.modelArmorChestplate.heldItemRight = this.modelArmor.heldItemRight = this.modelBipedMain.heldItemRight = 3;
			}
			else if (var11 == EnumAction.bow) {
				this.modelArmorChestplate.aimedBow = this.modelArmor.aimedBow = this.modelBipedMain.aimedBow = true;
			}
		}

		this.modelArmorChestplate.isSneak = this.modelArmor.isSneak = this.modelBipedMain.isSneak = var1.isSneaking();
		double var13 = var4 - (double)var1.yOffset;
		if(var1.isSneaking() && !(var1 instanceof EntityPlayerSP)) {
			var13 -= 0.125D;
		}

		super.doRenderLiving(var1, var2, var13, var6, var8, var9);
		this.modelArmorChestplate.aimedBow = this.modelArmor.aimedBow = this.modelBipedMain.aimedBow = false;
		this.modelArmorChestplate.isSneak = this.modelArmor.isSneak = this.modelBipedMain.isSneak = false;
		this.modelArmorChestplate.heldItemRight = this.modelArmor.heldItemRight = this.modelBipedMain.heldItemRight = 0;
	}

	protected void renderName(EntityPlayer var1, double var2, double var4, double var6) {
		if(Minecraft.isGuiEnabled() && var1 != this.renderManager.livingPlayer) {
			float var8 = 1.6F;
			float var9 = 0.016666668F * var8;
			float var10 = var1.getDistanceToEntity(this.renderManager.livingPlayer);
			float var11 = var1.isSneaking()?32.0F:64.0F;
			if(var10 < var11) {
				//Spout Start
				String title = var1.displayName;
				//int color = EasterEggs.getEasterEggTitleColor();
				float alpha = 0.25F;
				//if a plugin hasn't set a title, use the easter egg title (if one exists)
				//if (EasterEggs.getEasterEggTitle(var1.username) != null && color == -1) {
				//	title = EasterEggs.getEasterEggTitle(var1.username);
				//	alpha = 0.0F;
				//}
				if (!title.equals("[hide]")) {
					String lines[] = title.split("\\n");
					double y = var4;
					for (int line = 0; line < lines.length; line++) {
						title = lines[line];
						var4 = y + (0.275D * (lines.length - line - 1));
						if(!var1.isSneaking()) {
							if(var1.isPlayerSleeping()) {
								this.renderLivingLabel(var1, title, var2, var4 - 1.5D, var6, 64);
							} else {
								//if (color != -1) {
								//	this.renderLivingLabel(var1, title, var2, var4, var6, 64, color, color);
								//}
								//else {
									this.renderLivingLabel(var1, title, var2, var4, var6, 64);
								//}
							}
						} else {
							title = ChatColor.stripColor(title); //strip colors when sneaking
							
							FontRenderer var13 = this.getFontRendererFromRenderManager();
							GL11.glPushMatrix();
							GL11.glTranslatef((float)var2 + 0.0F, (float)var4 + 2.3F, (float)var6);
							GL11.glNormal3f(0.0F, 1.0F, 0.0F);
							GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
							GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
							GL11.glScalef(-var9, -var9, var9);
							GL11.glDisable(GL11.GL_LIGHTING);
							GL11.glTranslatef(0.0F, 0.25F / var9, 0.0F);
							GL11.glDepthMask(false);
							GL11.glEnable(GL11.GL_BLEND);
							GL11.glBlendFunc(770, 771);
							Tessellator var14 = Tessellator.instance;
							GL11.glDisable(GL11.GL_TEXTURE_2D);
							var14.startDrawingQuads();
							int var15 = var13.getStringWidth(title) / 2;
							var14.setColorRGBA_F(0.0F, 0.0F, 0.0F, alpha);
							var14.addVertex((double)(-var15 - 1), -1.0D, 0.0D);
							var14.addVertex((double)(-var15 - 1), 8.0D, 0.0D);
							var14.addVertex((double)(var15 + 1), 8.0D, 0.0D);
							var14.addVertex((double)(var15 + 1), -1.0D, 0.0D);
							var14.draw();
							GL11.glEnable(GL11.GL_TEXTURE_2D);
							GL11.glDepthMask(true);
							var13.drawString(title, -var13.getStringWidth(title) / 2, 0, 553648127);
							GL11.glEnable(GL11.GL_LIGHTING);
							GL11.glDisable(GL11.GL_BLEND);
							GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
							GL11.glPopMatrix();
						}
					}
				}
				//Spout End
			}
		}

	}

	protected void renderSpecials(EntityPlayer var1, float var2) {
		super.renderEquippedItems(var1, var2);
		ItemStack var3 = var1.inventory.armorItemInSlot(3);
		if(var3 != null && var3.getItem().shiftedIndex < 256) {
			GL11.glPushMatrix();
			this.modelBipedMain.bipedHead.postRender(0.0625F);
			if(RenderBlocks.renderItemIn3d(Block.blocksList[var3.itemID].getRenderType())) {
				float var4 = 0.625F;
				GL11.glTranslatef(0.0F, -0.25F, 0.0F);
				GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(var4, -var4, var4);
			}

			this.renderManager.itemRenderer.renderItem(var1, var3, 0);
			GL11.glPopMatrix();
		}

		float var6;
		if(var1.username.equals("deadmau5") && this.loadDownloadableImageTexture(var1.skinUrl, (String)null)) {
			for(int var19 = 0; var19 < 2; ++var19) {
				float var5 = var1.prevRotationYaw + (var1.rotationYaw - var1.prevRotationYaw) * var2 - (var1.prevRenderYawOffset + (var1.renderYawOffset - var1.prevRenderYawOffset) * var2);
				var6 = var1.prevRotationPitch + (var1.rotationPitch - var1.prevRotationPitch) * var2;
				GL11.glPushMatrix();
				GL11.glRotatef(var5, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(var6, 1.0F, 0.0F, 0.0F);
				GL11.glTranslatef(0.375F * (float)(var19 * 2 - 1), 0.0F, 0.0F);
				GL11.glTranslatef(0.0F, -0.375F, 0.0F);
				GL11.glRotatef(-var6, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(-var5, 0.0F, 1.0F, 0.0F);
				float var7 = 1.3333334F;
				GL11.glScalef(var7, var7, var7);
				this.modelBipedMain.renderEars(0.0625F);
				GL11.glPopMatrix();
			}
		}

		float var10;
		if(this.loadDownloadableImageTexture(var1.playerCloakUrl, (String)null)) {
			GL11.glPushMatrix();
			GL11.glTranslatef(0.0F, 0.0F, 0.125F);
			double var22 = var1.field_20066_r + (var1.field_20063_u - var1.field_20066_r) * (double)var2 - (var1.prevPosX + (var1.posX - var1.prevPosX) * (double)var2);
			double var23 = var1.field_20065_s + (var1.field_20062_v - var1.field_20065_s) * (double)var2 - (var1.prevPosY + (var1.posY - var1.prevPosY) * (double)var2);
			double var8 = var1.field_20064_t + (var1.field_20061_w - var1.field_20064_t) * (double)var2 - (var1.prevPosZ + (var1.posZ - var1.prevPosZ) * (double)var2);
			var10 = var1.prevRenderYawOffset + (var1.renderYawOffset - var1.prevRenderYawOffset) * var2;
			double var11 = (double)MathHelper.sin(var10 * 3.1415927F / 180.0F);
			double var13 = (double)(-MathHelper.cos(var10 * 3.1415927F / 180.0F));
			float var15 = (float)var23 * 10.0F;
			if(var15 < -6.0F) {
				var15 = -6.0F;
			}

			if(var15 > 32.0F) {
				var15 = 32.0F;
			}

			float var16 = (float)(var22 * var11 + var8 * var13) * 100.0F;
			float var17 = (float)(var22 * var13 - var8 * var11) * 100.0F;
			if(var16 < 0.0F) {
				var16 = 0.0F;
			}

			float var18 = var1.prevCameraYaw + (var1.cameraYaw - var1.prevCameraYaw) * var2;
			var15 += MathHelper.sin((var1.prevDistanceWalkedModified + (var1.distanceWalkedModified - var1.prevDistanceWalkedModified) * var2) * 6.0F) * 32.0F * var18;
			if(var1.isSneaking()) {
				var15 += 25.0F;
			}

			GL11.glRotatef(6.0F + var16 / 2.0F + var15, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(var17 / 2.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(-var17 / 2.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
			this.modelBipedMain.renderCloak(0.0625F);
			GL11.glPopMatrix();
		}

		ItemStack var21 = var1.inventory.getCurrentItem();
		if(var21 != null) {
			GL11.glPushMatrix();
			this.modelBipedMain.bipedRightArm.postRender(0.0625F);
			GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);
			if(var1.fishEntity != null) {
				var21 = new ItemStack(Item.stick);
			}

			EnumAction var20 = null;
			if(var1.func_35205_Y() > 0) {
				var20 = var21.getItemUseAction();
			}

			if(var21.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[var21.itemID].getRenderType())) {
				var6 = 0.5F;
				GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
				var6 *= 0.75F;
				GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(var6, -var6, var6);
			}
			else if (var21.itemID == Item.bow.shiftedIndex) {
				var6 = 0.625F;
				GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
				GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(var6, -var6, var6);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			}
			//spout start
			else if (Item.itemsList[var21.itemID].isFull3D() || var21.itemID == Item.flint.shiftedIndex && MaterialData.getCustomItem(var21.getItemDamage()) instanceof org.spoutcraft.spoutcraftapi.material.Tool) {
			//spout end
				var6 = 0.625F;
				if(Item.itemsList[var21.itemID].shouldRotateAroundWhenRendering()) {
					GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
					GL11.glTranslatef(0.0F, -0.125F, 0.0F);
				}

				if(var1.func_35205_Y() > 0 && var20 == EnumAction.block) {
					GL11.glTranslatef(0.05F, 0.0F, -0.1F);
					GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(-10.0F, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(-60.0F, 0.0F, 0.0F, 1.0F);
				}

				GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
				GL11.glScalef(var6, -var6, var6);
				GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			}
			else {
				var6 = 0.375F;
				GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
				GL11.glScalef(var6, var6, var6);
				GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
			}

			if (var21.getItem().func_46058_c()) {
				for (int var25 = 0; var25 <= 1; ++var25) {
					int var24 = var21.getItem().getColorFromDamage(var21.getItemDamage(), var25);
					float var26 = (float)(var24 >> 16 & 255) / 255.0F;
					float var9 = (float)(var24 >> 8 & 255) / 255.0F;
					var10 = (float)(var24 & 255) / 255.0F;
					GL11.glColor4f(var26, var9, var10, 1.0F);
					this.renderManager.itemRenderer.renderItem(var1, var21, var25);
				}
			}
			else {
				this.renderManager.itemRenderer.renderItem(var1, var21, 0);
			}

			GL11.glPopMatrix();
		}

	}

	protected void renderPlayerScale(EntityPlayer var1, float var2) {
		float var3 = 0.9375F;
		GL11.glScalef(var3, var3, var3);
	}

	public void drawFirstPersonHand() {
		this.modelBipedMain.onGround = 0.0F;
		this.modelBipedMain.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		this.modelBipedMain.bipedRightArm.render(0.0625F);
	}

	protected void renderPlayerSleep(EntityPlayer var1, double var2, double var4, double var6) {
		if(var1.isEntityAlive() && var1.isPlayerSleeping()) {
			super.renderLivingAt(var1, var2 + (double)var1.field_22063_x, var4 + (double)var1.field_22062_y, var6 + (double)var1.field_22061_z);
		}
		else {
			super.renderLivingAt(var1, var2, var4, var6);
		}

	}

	protected void rotatePlayer(EntityPlayer var1, float var2, float var3, float var4) {
		if(var1.isEntityAlive() && var1.isPlayerSleeping()) {
			GL11.glRotatef(var1.getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(this.getDeathMaxRotation(var1), 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
		}
		else {
			super.rotateCorpse(var1, var2, var3, var4);
		}

	}

	protected void passSpecialRender(EntityLiving var1, double var2, double var4, double var6) {
		this.renderName((EntityPlayer)var1, var2, var4, var6);
	}

	protected void preRenderCallback(EntityLiving var1, float var2) {
		this.renderPlayerScale((EntityPlayer)var1, var2);
	}

	protected int shouldRenderPass(EntityLiving var1, int var2, float var3) {
		return this.setArmorModel((EntityPlayer)var1, var2, var3);
	}

	protected void renderEquippedItems(EntityLiving var1, float var2) {
		this.renderSpecials((EntityPlayer)var1, var2);
	}

	protected void rotateCorpse(EntityLiving var1, float var2, float var3, float var4) {
		this.rotatePlayer((EntityPlayer)var1, var2, var3, var4);
	}

	protected void renderLivingAt(EntityLiving var1, double var2, double var4, double var6) {
		this.renderPlayerSleep((EntityPlayer)var1, var2, var4, var6);
	}

	public void doRenderLiving(EntityLiving var1, double var2, double var4, double var6, float var8, float var9) {
		this.renderPlayer((EntityPlayer)var1, var2, var4, var6, var8, var9);
	}

	public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9) {
		this.renderPlayer((EntityPlayer)var1, var2, var4, var6, var8, var9);
	}

}
