package net.minecraft.src;

import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.newdawn.slick.opengl.Texture;
import org.spoutcraft.api.block.design.BlockDesign;
import org.spoutcraft.api.material.MaterialData;
import org.spoutcraft.client.io.CustomTextureManager;

public class RenderItem extends Render {

	private RenderBlocks renderBlocks = new RenderBlocks();

	/** The RNG used in RenderItem (for bobbing itemstacks on the ground) */
	private Random random = new Random();
	public boolean field_77024_a = true;

	/** Defines the zLevel of rendering of item on GUI. */
	public float zLevel = 0.0F;
	public static boolean field_82407_g = false;

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
		// Spout Start
		// Sanity Checks
		if (par1EntityItem == null || par1EntityItem.item == null) {
			return;
		}
		// Spout End
		this.random.setSeed(187L);
		ItemStack var10 = par1EntityItem.item;
		if (var10.getItem() != null) {
			// GL11.glPushMatrix(); // Spout delate to later, if no custom design given
			float var11 = MathHelper.sin(((float)par1EntityItem.age + par9) / 10.0F + par1EntityItem.hoverStart) * 0.1F + 0.1F;
			float var12 = (((float)par1EntityItem.age + par9) / 20.0F + par1EntityItem.hoverStart) * (180F / (float)Math.PI);
			byte var13 = 1;

			if (par1EntityItem.item.stackSize > 1) {
				var13 = 2;
			}

			if (par1EntityItem.item.stackSize > 5) {
				var13 = 3;
			}

			if (par1EntityItem.item.stackSize > 20) {
				var13 = 4;
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
						Texture texture = CustomTextureManager.getTextureFromUrl(item.getAddon().getDescription().getName(), textureURI);
						if (texture != null) {
							GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
							custom = true;
						}
					}
				}

				/*
				 * org.spoutcraft.api.material.CustomBlock block = MaterialData.getCustomBlock(var10.getItemDamage()); design = block != null ? block.getBlockDesign() : null; if (design != null &&
				 * design.getTextureAddon() != null && design.getTexureURL() != null) { Texture texture = CustomTextureManager.getTextureFromUrl(design.getTextureAddon(), design.getTexureURL()); if
				 * (texture != null) { this.renderManager.renderEngine.bindTexture(texture.getTextureID()); custom = true; } }
				 */
			}
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);

			if (design != null && custom) {
				//GL11.glScalef(0.25F, 0.25F, 0.25F);
				design.renderItemstack((org.spoutcraft.api.entity.Item)par1EntityItem.spoutEntity, (float)par2, (float)(par4 + var11), (float)par6, var12, 0.25F, random);
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
			Block var14 = Block.blocksList[var10.itemID];
			int var16;
			float var19;
			float var20;
			float var24;

			if (var14 != null && RenderBlocks.renderItemIn3d(var14.getRenderType())) {
				GL11.glRotatef(var12, 0.0F, 1.0F, 0.0F);

				if (field_82407_g) {
					GL11.glScalef(1.25F, 1.25F, 1.25F);
					GL11.glTranslatef(0.0F, 0.05F, 0.0F);
					GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
				}

				this.loadTexture("/terrain.png");
				float var22 = 0.25F;
				var16 = var14.getRenderType();

				if (var16 == 1 || var16 == 19 || var16 == 12 || var16 == 2) {
					var22 = 0.5F;
				}

				GL11.glScalef(var22, var22, var22);

				for (int var23 = 0; var23 < var13; ++var23) {
					GL11.glPushMatrix();

					if (var23 > 0) {
						var24 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var22;
						var19 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var22;
						var20 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var22;
						GL11.glTranslatef(var24, var19, var20);
					}

					var24 = 1.0F;
					this.renderBlocks.renderBlockAsItem(var14, var10.getItemDamage(), var24);
					GL11.glPopMatrix();
				}
			} else {
				int var15;
				float var17;

				if (var10.getItem().requiresMultipleRenderPasses()) {
					if (field_82407_g) {
						GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
						GL11.glTranslatef(0.0F, -0.05F, 0.0F);
						GL11.glDisable(GL11.GL_LIGHTING);
					} else {
						GL11.glScalef(0.5F, 0.5F, 0.5F);
					}

					this.loadTexture("/gui/items.png");

					for (var15 = 0; var15 <= 1; ++var15) {
						var16 = var10.getItem().getIconFromDamageForRenderPass(var10.getItemDamage(), var15);
						var17 = 1.0F;

						if (this.field_77024_a) {
							int var18 = Item.itemsList[var10.itemID].getColorFromItemStack(var10, var15);
							var19 = (float)(var18 >> 16 & 255) / 255.0F;
							var20 = (float)(var18 >> 8 & 255) / 255.0F;
							float var21 = (float)(var18 & 255) / 255.0F;
							GL11.glColor4f(var19 * var17, var20 * var17, var21 * var17, 1.0F);
						}

						this.func_77020_a(var16, var13);
					}
				} else {
					if (field_82407_g) {
						GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
						GL11.glTranslatef(0.0F, -0.05F, 0.0F);
						GL11.glDisable(GL11.GL_LIGHTING);
					} else {
						GL11.glScalef(0.5F, 0.5F, 0.5F);
					}

					var15 = var10.getIconIndex();

					if (var14 != null) {
						this.loadTexture("/terrain.png");
					} else {
						this.loadTexture("/gui/items.png");
					}

					if (this.field_77024_a) {
						var16 = Item.itemsList[var10.itemID].getColorFromItemStack(var10, 0);
						var17 = (float)(var16 >> 16 & 255) / 255.0F;
						var24 = (float)(var16 >> 8 & 255) / 255.0F;
						var19 = (float)(var16 & 255) / 255.0F;
						var20 = 1.0F;
						GL11.glColor4f(var17 * var20, var24 * var20, var19 * var20, 1.0F);
					}
					
					// Spout Start
					this.renderItemBillboard(var15, var13, custom);
					// Spout End
				}
			}

			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			GL11.glPopMatrix();
			} // Spout
		}
	}

	// Spout Start
	private void func_77020_a(int var1, int var2) {
		renderItemBillboard(var1, var2, false);
	}

	private void renderItemBillboard(int par1, int par2, boolean customTexture) {
		// Spout End
		Tessellator var3 = Tessellator.instance;
		float var4 = (float)(par1 % 16 * 16 + 0) / 256.0F;
		float var5 = (float)(par1 % 16 * 16 + 16) / 256.0F;
		float var6 = (float)(par1 / 16 * 16 + 0) / 256.0F;
		float var7 = (float)(par1 / 16 * 16 + 16) / 256.0F;
		float var8 = 1.0F;
		float var9 = 0.5F;
		float var10 = 0.25F;
		// Spout Start
		if (customTexture) {
			var4 = 0F;
			var5 = 1F;
			var6 = 1F;
			var7 = 0F;
		}
		// Spout End

		for (int var11 = 0; var11 < par2; ++var11) {
			GL11.glPushMatrix();

			if (var11 > 0) {
				float var12 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
				float var13 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
				float var14 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
				GL11.glTranslatef(var12, var13, var14);
			}

			GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
			var3.startDrawingQuads();
			var3.setNormal(0.0F, 1.0F, 0.0F);
			var3.addVertexWithUV((double)(0.0F - var9), (double)(0.0F - var10), 0.0D, (double)var4, (double)var7);
			var3.addVertexWithUV((double)(var8 - var9), (double)(0.0F - var10), 0.0D, (double)var5, (double)var7);
			var3.addVertexWithUV((double)(var8 - var9), (double)(1.0F - var10), 0.0D, (double)var5, (double)var6);
			var3.addVertexWithUV((double)(0.0F - var9), (double)(1.0F - var10), 0.0D, (double)var4, (double)var6);
			var3.draw();
			GL11.glPopMatrix();
		}
	}

	/**
	 * Renders the item's icon or block into the UI at the specified position.
	 */
	public void renderItemIntoGUI(FontRenderer par1FontRenderer, RenderEngine par2RenderEngine, ItemStack par3ItemStack, int par4, int par5) {
		int var6 = par3ItemStack.itemID;
		int var7 = par3ItemStack.getItemDamage();
		int var8 = par3ItemStack.getIconIndex();
		int var10;
		float var12;
		float var13;
		float var16;

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
					Texture texture = CustomTextureManager.getTextureFromUrl(item.getAddon().getDescription().getName(), textureURI);
					if (texture != null) {
						GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
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
			design.renderItemOnHUD((float)(var6 - 2), (float)(var7 + 3), -3.0F + this.zLevel);
		} else if (var6 < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[var6].getRenderType())) {
			// Spout End
			Block var15 = Block.blocksList[var6];
			GL11.glPushMatrix();
			GL11.glTranslatef((float)(par4 - 2), (float)(par5 + 3), -3.0F + this.zLevel);
			GL11.glScalef(10.0F, 10.0F, 10.0F);
			GL11.glTranslatef(1.0F, 0.5F, 1.0F);
			GL11.glScalef(1.0F, 1.0F, -1.0F);
			GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			var10 = Item.itemsList[var6].getColorFromItemStack(par3ItemStack, 0);
			var16 = (float)(var10 >> 16 & 255) / 255.0F;
			var12 = (float)(var10 >> 8 & 255) / 255.0F;
			var13 = (float)(var10 & 255) / 255.0F;

			if (this.field_77024_a) {
				GL11.glColor4f(var16, var12, var13, 1.0F);
			}

			GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
			this.renderBlocks.useInventoryTint = this.field_77024_a;
			this.renderBlocks.renderBlockAsItem(var15, var7, 1.0F);
			this.renderBlocks.useInventoryTint = true;
			GL11.glPopMatrix();
		} else {
			int var9;

			if (Item.itemsList[var6].requiresMultipleRenderPasses()) {
				GL11.glDisable(GL11.GL_LIGHTING);
				par2RenderEngine.bindTexture(par2RenderEngine.getTexture("/gui/items.png"));

				for (var9 = 0; var9 <= 1; ++var9) {
					var10 = Item.itemsList[var6].getIconFromDamageForRenderPass(var7, var9);
					int var11 = Item.itemsList[var6].getColorFromItemStack(par3ItemStack, var9);
					var12 = (float)(var11 >> 16 & 255) / 255.0F;
					var13 = (float)(var11 >> 8 & 255) / 255.0F;
					float var14 = (float)(var11 & 255) / 255.0F;

					if (this.field_77024_a) {
						GL11.glColor4f(var12, var13, var14, 1.0F);
					}

					this.renderTexturedQuad(par4, par5, var10 % 16 * 16, var10 / 16 * 16, 16, 16);
				}

				GL11.glEnable(GL11.GL_LIGHTING);
			} else if (var8 >= 0) {
				GL11.glDisable(GL11.GL_LIGHTING);

				if (var6 < 256) {
					par2RenderEngine.bindTexture(par2RenderEngine.getTexture("/terrain.png"));
				} else {
					par2RenderEngine.bindTexture(par2RenderEngine.getTexture("/gui/items.png"));
				}

				var9 = Item.itemsList[var6].getColorFromItemStack(par3ItemStack, 0);
				float var17 = (float)(var9 >> 16 & 255) / 255.0F;
				var16 = (float)(var9 >> 8 & 255) / 255.0F;
				var12 = (float)(var9 & 255) / 255.0F;

				if (this.field_77024_a) {
					GL11.glColor4f(var17, var16, var12, 1.0F);
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
					this.renderTexturedQuad(par4, par5, var8 % 16 * 16, var8 / 16 * 16, 16, 16);
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

			if (par3ItemStack != null && par3ItemStack.hasEffect()) {
				GL11.glDepthFunc(GL11.GL_GREATER);
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDepthMask(false);
				par2RenderEngine.bindTexture(par2RenderEngine.getTexture("%blur%/misc/glint.png"));
				this.zLevel -= 50.0F;
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_DST_COLOR);
				GL11.glColor4f(0.5F, 0.25F, 0.8F, 1.0F);
				this.func_77018_a(par4 * 431278612 + par5 * 32178161, par4 - 2, par5 - 2, 20, 20);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glDepthMask(true);
				this.zLevel += 50.0F;
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glDepthFunc(GL11.GL_LEQUAL);
			}
		}
	}

	private void func_77018_a(int par1, int par2, int par3, int par4, int par5) {
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
	 * Renders the item's overlay information. Examples being stack count or damage on top of the item's image at the specified position.
	 */
	public void renderItemOverlayIntoGUI(FontRenderer par1FontRenderer, RenderEngine par2RenderEngine, ItemStack par3ItemStack, int par4, int par5) {
		if (par3ItemStack != null) {
			if (par3ItemStack.stackSize > 1) {
				String var6 = "" + par3ItemStack.stackSize;
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				par1FontRenderer.drawStringWithShadow(var6, par4 + 19 - 2 - par1FontRenderer.getStringWidth(var6), par5 + 6 + 3, 16777215);
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
			if (par3ItemStack.isItemDamaged() || override) {
				int var11 = (int)Math.round(13.0D - (double)(override ? amnt : par3ItemStack.getItemDamageForDisplay()) * 13.0D / (double)(override ? max : par3ItemStack.getMaxDamage()));
				int var7 = (int)Math.round(255.0D - (double)(override ? amnt : par3ItemStack.getItemDamageForDisplay()) * 255.0D / (double)(override ? max : par3ItemStack.getMaxDamage()));
				// Spout End
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				Tessellator var8 = Tessellator.instance;
				int var9 = 255 - var7 << 16 | var7 << 8;
				int var10 = (255 - var7) / 4 << 16 | 16128;
				this.renderQuad(var8, par4 + 2, par5 + 13, 13, 2, 0);
				this.renderQuad(var8, par4 + 2, par5 + 13, 12, 1, var10);
				this.renderQuad(var8, par4 + 2, par5 + 13, var11, 1, var9);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			}
		}
	}

	/**
	 * Adds a quad to the tesselator at the specified position with the set width and height and color. Args: tessellator, x, y, width, height, color
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

	/**
	 * Adds a textured quad to the tesselator at the specified position with the specified texture coords, width and height. Args: x, y, u, v, width, height
	 */
	public void renderTexturedQuad(int par1, int par2, int par3, int par4, int par5, int par6) {
		float var7 = 0.00390625F;
		float var8 = 0.00390625F;
		Tessellator var9 = Tessellator.instance;
		var9.startDrawingQuads();
		var9.addVertexWithUV((double)(par1 + 0), (double)(par2 + par6), (double)this.zLevel, (double)((float)(par3 + 0) * var7), (double)((float)(par4 + par6) * var8));
		var9.addVertexWithUV((double)(par1 + par5), (double)(par2 + par6), (double)this.zLevel, (double)((float)(par3 + par5) * var7), (double)((float)(par4 + par6) * var8));
		var9.addVertexWithUV((double)(par1 + par5), (double)(par2 + 0), (double)this.zLevel, (double)((float)(par3 + par5) * var7), (double)((float)(par4 + 0) * var8));
		var9.addVertexWithUV((double)(par1 + 0), (double)(par2 + 0), (double)this.zLevel, (double)((float)(par3 + 0) * var7), (double)((float)(par4 + 0) * var8));
		var9.draw();
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then handing it off to a worker function which does the actual work. In all
	 * probabilty, the class Render is generic (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1, double d2, float f, float f1). But JAD is pre
	 * 1.5 so doesn't do that.
	 */
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		this.doRenderItem((EntityItem)par1Entity, par2, par4, par6, par8, par9);
	}
}
