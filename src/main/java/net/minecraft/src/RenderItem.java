package net.minecraft.src;

import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
//Spout Start
import org.newdawn.slick.opengl.Texture;
import org.spoutcraft.api.block.design.BlockDesign;
import org.spoutcraft.api.material.MaterialData;
import org.spoutcraft.client.io.CustomTextureManager;
import org.spoutcraft.client.SpoutClient;
//Spout End

public class RenderItem extends Render {
	private RenderBlocks itemRenderBlocks = new RenderBlocks();

	/** The RNG used in RenderItem (for bobbing itemstacks on the ground) */
	private Random random = new Random();
	public boolean renderWithColor = true;

	/** Defines the zLevel of rendering of item on GUI. */
	public float zLevel = 0.0F;
	public static boolean renderInFrame = false;

	public RenderItem() {
		this.shadowSize = 0.15F;
		this.shadowOpaque = 0.75F;
	}

	// Spout Start
	@Override
	protected void loadTexture(String texture) {
		if (this.renderManager != null && this.renderManager.renderEngine != null) {
			int textureId = this.renderManager.renderEngine.getTexture(texture);
			if (textureId >= 0) {
				this.renderManager.renderEngine.bindTexture(textureId);
			}
		}
	}
	// Spout End

	/**
	 * Renders the item
	 */
	public void doRenderItem(EntityItem par1EntityItem, double par2, double par4, double par6, float par8, float par9) {
		this.random.setSeed(187L);
		ItemStack var10 = par1EntityItem.getEntityItem();

		if (var10.getItem() != null) {
			// Spout Start - Removed
			//GL11.glPushMatrix();
			// Spout End
			float var11 = MathHelper.sin(((float)par1EntityItem.age + par9) / 10.0F + par1EntityItem.hoverStart) * 0.1F + 0.1F;
			float var12 = (((float)par1EntityItem.age + par9) / 20.0F + par1EntityItem.hoverStart) * (180F / (float)Math.PI);
			byte var13 = 1;

			if (par1EntityItem.getEntityItem().stackSize > 1) {
				var13 = 2;
			}

			if (par1EntityItem.getEntityItem().stackSize > 5) {
				var13 = 3;
			}

			if (par1EntityItem.getEntityItem().stackSize > 20) {
				var13 = 4;
			}

			if (par1EntityItem.getEntityItem().stackSize > 40) {
				var13 = 5;
			}

			// Spout Start
			boolean custom = false;
			BlockDesign design = null;
			if (var10.itemID == 318) {
				org.spoutcraft.api.material.CustomItem item = MaterialData.getCustomItem(var10.getItemDamage());
				if (item != null) {
					String textureURI = item.getTexture();
					if (textureURI == null) {
						org.spoutcraft.api.material.CustomBlock block = MaterialData.getCustomBlock(var10.getItemDamage());
						design = block != null ? block.getBlockDesign() : null;
						textureURI = design != null ? design.getTexureURL() : null;
					}
					if (textureURI != null) {
						Texture texture = CustomTextureManager.getTextureFromUrl(item.getAddon(), textureURI);
						if (texture != null) {
							SpoutClient.getHandle().renderEngine.bindTexture(texture.getTextureID());
							custom = true;
						}
					}
				}
			}
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);

			if (design != null && custom) {
				//GL11.glScalef(0.25F, 0.25F, 0.25F);
				design.renderItemstack(par1EntityItem.getEntityItem(), (float)par2, (float)(par4 + var11), (float)par6, var12, 0.25F, random);
			} else {
				GL11.glPushMatrix(); // the push from above
				if (!custom) {
					if (var10.itemID < 256) {
						this.loadTexture("/terrain.png");
					} else {
						this.loadTexture("/gui/items.png");
					}
				}
				// Spout End

				GL11.glTranslatef((float)par2, (float)par4 + var11, (float)par6);
				GL11.glEnable(GL12.GL_RESCALE_NORMAL);
				int var17;
				float var19;
				float var18;
				float var20;

				if (var10.getItemSpriteNumber() == 0 && Block.blocksList[var10.itemID] != null && RenderBlocks.renderItemIn3d(Block.blocksList[var10.itemID].getRenderType())) {
					Block var21 = Block.blocksList[var10.itemID];
					GL11.glRotatef(var12, 0.0F, 1.0F, 0.0F);

					if (renderInFrame) {
						GL11.glScalef(1.25F, 1.25F, 1.25F);
						GL11.glTranslatef(0.0F, 0.05F, 0.0F);
						GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
					}

					this.loadTexture("/terrain.png");
					float var25 = 0.25F;
					int var24 = var21.getRenderType();

					if (var24 == 1 || var24 == 19 || var24 == 12 || var24 == 2) {
						var25 = 0.5F;
					}

					GL11.glScalef(var25, var25, var25);

					for (var17 = 0; var17 < var13; ++var17) {
						GL11.glPushMatrix();

						if (var17 > 0) {
							var18 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var25;
							var19 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var25;
							var20 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var25;
							GL11.glTranslatef(var18, var19, var20);
						}

						var18 = 1.0F;
						this.itemRenderBlocks.renderBlockAsItem(var21, var10.getItemDamage(), var18);
						GL11.glPopMatrix();
					}
				} else {
					float var16;

					if (var10.getItem().requiresMultipleRenderPasses()) {
						if (renderInFrame) {
							GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
							GL11.glTranslatef(0.0F, -0.05F, 0.0F);
						} else {
							GL11.glScalef(0.5F, 0.5F, 0.5F);
						}

						this.loadTexture("/gui/items.png");

						for (int var14 = 0; var14 <= 1; ++var14) {
							this.random.setSeed(187L);
							Icon var15 = var10.getItem().getIconFromDamageForRenderPass(var10.getItemDamage(), var14);
							var16 = 1.0F;

							if (this.renderWithColor) {
								var17 = Item.itemsList[var10.itemID].getColorFromItemStack(var10, var14);
								var18 = (float)(var17 >> 16 & 255) / 255.0F;
								var19 = (float)(var17 >> 8 & 255) / 255.0F;
								var20 = (float)(var17 & 255) / 255.0F;
								GL11.glColor4f(var18 * var16, var19 * var16, var20 * var16, 1.0F);
								this.renderDroppedItem(par1EntityItem, var15, var13, par9, var18 * var16, var19 * var16, var20 * var16, custom);
							} else {
								this.renderDroppedItem(par1EntityItem, var15, var13, par9, 1.0F, 1.0F, 1.0F, custom);
							}
						}
					} else {
						if (renderInFrame) {
							GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
							GL11.glTranslatef(0.0F, -0.05F, 0.0F);
						} else {
							GL11.glScalef(0.5F, 0.5F, 0.5F);
						}

						Icon var23 = var10.getIconIndex();

						if (var10.getItemSpriteNumber() == 0) {
							this.loadTexture("/terrain.png");
						} else {
							if (!custom) {
								this.loadTexture("/gui/items.png");
							}
						}

						if (this.renderWithColor) {
							int var22 = Item.itemsList[var10.itemID].getColorFromItemStack(var10, 0);
							var16 = (float)(var22 >> 16 & 255) / 255.0F;
							float var26 = (float)(var22 >> 8 & 255) / 255.0F;
							var18 = (float)(var22 & 255) / 255.0F;
							var19 = 1.0F;
							this.renderDroppedItem(par1EntityItem, var23, var13, par9, var16 * var19, var26 * var19, var18 * var19, custom);
						} else {
							this.renderDroppedItem(par1EntityItem, var23, var13, par9, 1.0F, 1.0F, 1.0F, custom);
						}
					}
				}

			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			GL11.glPopMatrix();
			}
		}
	}

	/**
	 * Renders a dropped item
	 */
	private void renderDroppedItem(EntityItem par1EntityItem, Icon par2Icon, int par3, float par4, float par5, float par6, float par7, boolean customTexture) {
		Tessellator var8 = Tessellator.instance;

		if (par2Icon == null) {
			par2Icon = this.renderManager.renderEngine.getMissingIcon(par1EntityItem.getEntityItem().getItemSpriteNumber());
		}

		float var9 = par2Icon.getMinU();
		float var10 = par2Icon.getMaxU();
		float var11 = par2Icon.getMinV();
		float var12 = par2Icon.getMaxV();
		float var13 = 1.0F;
		float var14 = 0.5F;
		float var15 = 0.25F;
		float var17;
		// Spout Start
		if (customTexture) {
			var9 = 0F;
			var10 = 1F;
			var11 = 1F;
			var12 = 0F;
		}
		// Spout End

		if (this.renderManager.options.fancyGraphics) {
			GL11.glPushMatrix();

			if (renderInFrame) {
				GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
			} else {
				GL11.glRotatef((((float)par1EntityItem.age + par4) / 20.0F + par1EntityItem.hoverStart) * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
			}

			float var16 = 0.0625F;
			var17 = 0.021875F;
			ItemStack var18 = par1EntityItem.getEntityItem();
			int var19 = var18.stackSize;
			byte var27;

			if (var19 < 2) {
				var27 = 1;
			} else if (var19 < 16) {
				var27 = 2;
			} else if (var19 < 32) {
				var27 = 3;
			} else {
				var27 = 4;
			}

			GL11.glTranslatef(-var14, -var15, -((var16 + var17) * (float)var27 / 2.0F));

			for (int var20 = 0; var20 < var27; ++var20) {
				GL11.glTranslatef(0.0F, 0.0F, var16 + var17);

				// Spout Removed
				/*
				if (Block.blocksList[var18.itemID] != null) {
					this.loadTexture("/terrain.png");
				} else {
					this.loadTexture("/gui/items.png");
				}
				*/
				// Spout End

				GL11.glColor4f(par5, par6, par7, 1.0F);
				ItemRenderer.renderItemIn2D(var8, var10, var11, var9, var12, par2Icon.getSheetWidth(), par2Icon.getSheetHeight(), var16);

				if (var18 != null && var18.hasEffect()) {
					GL11.glDepthFunc(GL11.GL_EQUAL);
					GL11.glDisable(GL11.GL_LIGHTING);
					this.renderManager.renderEngine.bindTexture("%blur%/misc/glint.png");
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
					float var21 = 0.76F;
					GL11.glColor4f(0.5F * var21, 0.25F * var21, 0.8F * var21, 1.0F);
					GL11.glMatrixMode(GL11.GL_TEXTURE);
					GL11.glPushMatrix();
					float var22 = 0.125F;
					GL11.glScalef(var22, var22, var22);
					float var23 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
					GL11.glTranslatef(var23, 0.0F, 0.0F);
					GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
					ItemRenderer.renderItemIn2D(var8, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, var16);
					GL11.glPopMatrix();
					GL11.glPushMatrix();
					GL11.glScalef(var22, var22, var22);
					var23 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
					GL11.glTranslatef(-var23, 0.0F, 0.0F);
					GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
					ItemRenderer.renderItemIn2D(var8, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, var16);
					GL11.glPopMatrix();
					GL11.glMatrixMode(GL11.GL_MODELVIEW);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glEnable(GL11.GL_LIGHTING);
					GL11.glDepthFunc(GL11.GL_LEQUAL);
				}
			}

			GL11.glPopMatrix();
		} else {
			for (int var26 = 0; var26 < par3; ++var26) {
				GL11.glPushMatrix();

				if (var26 > 0) {
					var17 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
					float var24 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
					float var25 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
					GL11.glTranslatef(var17, var24, var25);
				}

				if (!renderInFrame) {
					GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
				}

				GL11.glColor4f(par5, par6, par7, 1.0F);
				var8.startDrawingQuads();
				var8.setNormal(0.0F, 1.0F, 0.0F);
				var8.addVertexWithUV((double)(0.0F - var14), (double)(0.0F - var15), 0.0D, (double)var9, (double)var12);
				var8.addVertexWithUV((double)(var13 - var14), (double)(0.0F - var15), 0.0D, (double)var10, (double)var12);
				var8.addVertexWithUV((double)(var13 - var14), (double)(1.0F - var15), 0.0D, (double)var10, (double)var11);
				var8.addVertexWithUV((double)(0.0F - var14), (double)(1.0F - var15), 0.0D, (double)var9, (double)var11);
				var8.draw();
				GL11.glPopMatrix();
			}
		}
	}

	/**
	 * Renders the item's icon or block into the UI at the specified position.
	 */
	public void renderItemIntoGUI(FontRenderer par1FontRenderer, RenderEngine par2RenderEngine, ItemStack par3ItemStack, int par4, int par5) {
		int var6 = par3ItemStack.itemID;
		int var7 = par3ItemStack.getItemDamage();
		Icon var8 = par3ItemStack.getIconIndex();
		float var12;
		float var13;
		float var18;

		// Spout Start
		boolean custom = false;
		BlockDesign design = null;
		if (var6 == 318) {
			org.spoutcraft.api.material.CustomItem item = MaterialData.getCustomItem(var7);
			if (item != null) {
				String textureURI = item.getTexture();
				if (textureURI == null) {
					org.spoutcraft.api.material.CustomBlock block = MaterialData.getCustomBlock(var7);
					design = block != null ? block.getBlockDesign() : null;
					textureURI = design != null ? design.getTexureURL() : null;
				}
				if (textureURI != null) {
					Texture texture = CustomTextureManager.getTextureFromUrl(item.getAddon(), textureURI);
					if (texture != null) {
						SpoutClient.getHandle().renderEngine.bindTexture(texture.getTextureID());
						custom = true;
					}
				}
			}
		}
		if (!custom) {
			if (var6 < 256) {
				loadTexture("/terrain.png");
			} else {
				loadTexture("/gui/items.png");
			}
		}

		if (design != null && custom) {
			design.renderItemOnHUD((float)(par4 - 2), (float)(par5 + 3), -3.0F + this.zLevel);
		} else if (var6 < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[var6].getRenderType())) {
			par2RenderEngine.bindTexture("/terrain.png");
			Block var15 = Block.blocksList[var6];
			GL11.glPushMatrix();
			GL11.glTranslatef((float)(par4 - 2), (float)(par5 + 3), -3.0F + this.zLevel);
			GL11.glScalef(10.0F, 10.0F, 10.0F);
			GL11.glTranslatef(1.0F, 0.5F, 1.0F);
			GL11.glScalef(1.0F, 1.0F, -1.0F);
			GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			int var16 = Item.itemsList[var6].getColorFromItemStack(par3ItemStack, 0);
			var18 = (float)(var16 >> 16 & 255) / 255.0F;
			var12 = (float)(var16 >> 8 & 255) / 255.0F;
			var13 = (float)(var16 & 255) / 255.0F;

			if (this.renderWithColor) {
				GL11.glColor4f(var18, var12, var13, 1.0F);
			}

			//this.renderIcon(par4, par5, var10, 16, 16); //TODO:

			GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
			this.itemRenderBlocks.useInventoryTint = this.renderWithColor;
			this.itemRenderBlocks.renderBlockAsItem(var15, var7, 1.0F);
			this.itemRenderBlocks.useInventoryTint = true;
			GL11.glPopMatrix();
		} else {
			int var9;

			if (Item.itemsList[var6].requiresMultipleRenderPasses()) {
				GL11.glDisable(GL11.GL_LIGHTING);
				if (!custom) {
					par2RenderEngine.bindTexture("/gui/items.png");
				}

				for (var9 = 0; var9 <= 1; ++var9) {
					Icon var10 = Item.itemsList[var6].getIconFromDamageForRenderPass(var7, var9);
					int var11 = Item.itemsList[var6].getColorFromItemStack(par3ItemStack, var9);
					var12 = (float)(var11 >> 16 & 255) / 255.0F;
					var13 = (float)(var11 >> 8 & 255) / 255.0F;
					float var14 = (float)(var11 & 255) / 255.0F;

					if (this.renderWithColor) {
						GL11.glColor4f(var12, var13, var14, 1.0F);
					}

					this.renderIcon(par4, par5, var10, 16, 16);
				}

				GL11.glEnable(GL11.GL_LIGHTING);
			} else {
				GL11.glDisable(GL11.GL_LIGHTING);

				if (var6 < 256) {
					par2RenderEngine.bindTexture("/terrain.png");
				} else {
					if (!custom) {
						par2RenderEngine.bindTexture("/gui/items.png");
					}
				}

				var9 = Item.itemsList[var6].getColorFromItemStack(par3ItemStack, 0);
				float var17 = (float)(var9 >> 16 & 255) / 255.0F;
				var18 = (float)(var9 >> 8 & 255) / 255.0F;
				var12 = (float)(var9 & 255) / 255.0F;

				if (this.renderWithColor) {
					GL11.glColor4f(var17, var18, var12, 1.0F);
				}

				// Spout Start
				if (custom) {
				Tessellator tes = Tessellator.instance;
				tes.startDrawingQuads();
				tes.addVertexWithUV((double)(par4 + 0), (double)(par5 + 16), (double)0, 0, 0);
				tes.addVertexWithUV((double)(par4 + 16), (double)(par5 + 16), (double)0, 1, 0);
				tes.addVertexWithUV((double)(par4 + 16), (double)(par5 + 0), (double)0, 1, 1);
				tes.addVertexWithUV((double)(par4 + 0), (double)(par5 + 0), (double)0, 0, 1);
				tes.draw();
				} else
					this.renderIcon(par4, par5, var8, 16, 16);
				// Spout End
				GL11.glEnable(GL11.GL_LIGHTING);
			}
		}

		GL11.glEnable(GL11.GL_CULL_FACE);
	}

	/**
	 * Render the item's icon or block into the GUI, including the glint effect.
	 */
	public void renderItemAndEffectIntoGUI(FontRenderer par1FontRenderer, RenderEngine par2RenderEngine, ItemStack par3ItemStack, int par4, int par5) {
		if (par3ItemStack != null) {
			this.renderItemIntoGUI(par1FontRenderer, par2RenderEngine, par3ItemStack, par4, par5);

			if (par3ItemStack.hasEffect()) {
				GL11.glDepthFunc(GL11.GL_GREATER);
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDepthMask(false);
				par2RenderEngine.bindTexture("%blur%/misc/glint.png");
				this.zLevel -= 50.0F;
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_DST_COLOR);
				GL11.glColor4f(0.5F, 0.25F, 0.8F, 1.0F);
				this.renderGlint(par4 * 431278612 + par5 * 32178161, par4 - 2, par5 - 2, 20, 20);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glDepthMask(true);
				this.zLevel += 50.0F;
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glDepthFunc(GL11.GL_LEQUAL);
			}
		}
	}

	private void renderGlint(int par1, int par2, int par3, int par4, int par5) {
		for (int var6 = 0; var6 < 2; ++var6) {
			if (var6 == 0) {
				GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
			}

			if (var6 == 1) {
				GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
			}

			float var7 = 0.00390625F;
			float var8 = 0.00390625F;
			float var9 = (float)(Minecraft.getSystemTime() % (long)(3000 + var6 * 1873)) / (3000.0F + (float)(var6 * 1873)) * 256.0F;
			float var10 = 0.0F;
			Tessellator var11 = Tessellator.instance;
			float var12 = 4.0F;

			if (var6 == 1) {
				var12 = -1.0F;
			}

			var11.startDrawingQuads();
			var11.addVertexWithUV((double)(par2 + 0), (double)(par3 + par5), (double)this.zLevel, (double)((var9 + (float)par5 * var12) * var7), (double)((var10 + (float)par5) * var8));
			var11.addVertexWithUV((double)(par2 + par4), (double)(par3 + par5), (double)this.zLevel, (double)((var9 + (float)par4 + (float)par5 * var12) * var7), (double)((var10 + (float)par5) * var8));
			var11.addVertexWithUV((double)(par2 + par4), (double)(par3 + 0), (double)this.zLevel, (double)((var9 + (float)par4) * var7), (double)((var10 + 0.0F) * var8));
			var11.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), (double)this.zLevel, (double)((var9 + 0.0F) * var7), (double)((var10 + 0.0F) * var8));
			var11.draw();
		}
	}

	/**
	 * Renders the item's overlay information. Examples being stack count or damage on top of the item's image at the
	 * specified position.
	 */
	public void renderItemOverlayIntoGUI(FontRenderer par1FontRenderer, RenderEngine par2RenderEngine, ItemStack par3ItemStack, int par4, int par5) {
		this.renderItemStack(par1FontRenderer, par2RenderEngine, par3ItemStack, par4, par5, (String)null);
	}

	public void renderItemStack(FontRenderer par1FontRenderer, RenderEngine par2RenderEngine, ItemStack par3ItemStack, int par4, int par5, String par6Str) {
		if (par3ItemStack != null) {
			if (par3ItemStack.stackSize > 1 || par6Str != null) {
				String var7 = par6Str == null ? String.valueOf(par3ItemStack.stackSize) : par6Str;
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				par1FontRenderer.drawStringWithShadow(var7, par4 + 19 - 2 - par1FontRenderer.getStringWidth(var7), par5 + 6 + 3, 16777215);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}

			// Spout Start
			NBTTagList list = par3ItemStack.getAllEnchantmentTagList();
			short max = -1;
			short amnt = -1;
			if (list != null) {
				for (int i = 0; i < list.tagCount(); i++) {
					NBTBase tag = list.tagAt(i);
					if (tag instanceof NBTTagCompound) {
						NBTTagCompound ench = (NBTTagCompound)tag;
						short id = ench.getShort("id");
						short lvl = ench.getShort("lvl");
						if (id == 254)
							amnt = lvl; //Enchantment DURABILITY = new SpoutEnchantment(254);
						if (id == 255)
							max = lvl; //Enchantment MAX_DURABILITY = new SpoutEnchantment(255);
					}
				}
			}
			boolean override = max > 0 && amnt > 0 && amnt < max;
			// Spout End
			if (par3ItemStack.isItemDamaged()) {
				int var12 = (int)Math.round(13.0D - (double)par3ItemStack.getItemDamageForDisplay() * 13.0D / (double)par3ItemStack.getMaxDamage());
				int var8 = (int)Math.round(255.0D - (double)par3ItemStack.getItemDamageForDisplay() * 255.0D / (double)par3ItemStack.getMaxDamage());
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				Tessellator var9 = Tessellator.instance;
				int var10 = 255 - var8 << 16 | var8 << 8;
				int var11 = (255 - var8) / 4 << 16 | 16128;
				this.renderQuad(var9, par4 + 2, par5 + 13, 13, 2, 0);
				this.renderQuad(var9, par4 + 2, par5 + 13, 12, 1, var11);
				this.renderQuad(var9, par4 + 2, par5 + 13, var12, 1, var10);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			}
		}
	}

	/**
	 * Adds a quad to the tesselator at the specified position with the set width and height and color.  Args: tessellator,
	 * x, y, width, height, color
	 */
	private void renderQuad(Tessellator par1Tessellator, int par2, int par3, int par4, int par5, int par6) {
		par1Tessellator.startDrawingQuads();
		par1Tessellator.setColorOpaque_I(par6);
		par1Tessellator.addVertex((double)(par2 + 0), (double)(par3 + 0), 0.0D);
		par1Tessellator.addVertex((double)(par2 + 0), (double)(par3 + par5), 0.0D);
		par1Tessellator.addVertex((double)(par2 + par4), (double)(par3 + par5), 0.0D);
		par1Tessellator.addVertex((double)(par2 + par4), (double)(par3 + 0), 0.0D);
		par1Tessellator.draw();
	}

	public void renderIcon(int par1, int par2, Icon par3Icon, int par4, int par5) {
		Tessellator var6 = Tessellator.instance;
		var6.startDrawingQuads();
		var6.addVertexWithUV((double)(par1 + 0), (double)(par2 + par5), (double)this.zLevel, (double)par3Icon.getMinU(), (double)par3Icon.getMaxV());
		var6.addVertexWithUV((double)(par1 + par4), (double)(par2 + par5), (double)this.zLevel, (double)par3Icon.getMaxU(), (double)par3Icon.getMaxV());
		var6.addVertexWithUV((double)(par1 + par4), (double)(par2 + 0), (double)this.zLevel, (double)par3Icon.getMaxU(), (double)par3Icon.getMinV());
		var6.addVertexWithUV((double)(par1 + 0), (double)(par2 + 0), (double)this.zLevel, (double)par3Icon.getMinU(), (double)par3Icon.getMinV());
		var6.draw();
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
	 * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
	 * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
	 * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
	 */
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		this.doRenderItem((EntityItem)par1Entity, par2, par4, par6, par8, par9);
	}
}
