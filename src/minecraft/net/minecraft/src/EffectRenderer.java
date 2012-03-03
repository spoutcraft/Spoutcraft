package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.src.ActiveRenderInfo;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityDiggingFX;
import net.minecraft.src.EntityFX;
import net.minecraft.src.MathHelper;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;
import org.lwjgl.opengl.GL11;
//Spout start
import java.util.LinkedList;

import org.newdawn.slick.opengl.Texture;
import org.spoutcraft.client.io.CustomTextureManager;
import org.spoutcraft.client.item.CustomEntityDiggingFX;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.block.design.GenericBlockDesign;
import org.spoutcraft.spoutcraftapi.material.CustomBlock;
import org.spoutcraft.spoutcraftapi.material.MaterialData;

//Spout end

public class EffectRenderer {

	protected World worldObj;
	private List[] fxLayers = new List[4];
	private RenderEngine renderer;
	private Random rand = new Random();

	public EffectRenderer(World par1World, RenderEngine par2RenderEngine) {
		if (par1World != null) {
			this.worldObj = par1World;
		}

		this.renderer = par2RenderEngine;

		for (int var3 = 0; var3 < 4; ++var3) {
			this.fxLayers[var3] = new ArrayList();
		}

	}

	public void addEffect(EntityFX par1EntityFX) {
		int var2 = par1EntityFX.getFXLayer();
		if (this.fxLayers[var2].size() >= 4000) {
			this.fxLayers[var2].remove(0);
		}

		this.fxLayers[var2].add(par1EntityFX);
	}

	public void updateEffects() {
		for (int var1 = 0; var1 < 4; ++var1) {
			for (int var2 = 0; var2 < this.fxLayers[var1].size(); ++var2) {
				EntityFX var3 = (EntityFX)this.fxLayers[var1].get(var2);
				var3.onUpdate();
				if (var3.isDead) {
					this.fxLayers[var1].remove(var2--);
				}
			}
		}

	}

	public void renderParticles(Entity par1Entity, float par2) {
		float var3 = ActiveRenderInfo.rotationX;
		float var4 = ActiveRenderInfo.rotationZ;
		float var5 = ActiveRenderInfo.rotationYZ;
		float var6 = ActiveRenderInfo.rotationXY;
		float var7 = ActiveRenderInfo.rotationXZ;
		EntityFX.interpPosX = par1Entity.lastTickPosX + (par1Entity.posX - par1Entity.lastTickPosX) * (double)par2;
		EntityFX.interpPosY = par1Entity.lastTickPosY + (par1Entity.posY - par1Entity.lastTickPosY) * (double)par2;
		EntityFX.interpPosZ = par1Entity.lastTickPosZ + (par1Entity.posZ - par1Entity.lastTickPosZ) * (double)par2;

		// Spout start
		LinkedList<CustomEntityDiggingFX> specialParticles = new LinkedList<CustomEntityDiggingFX>();
		// Spout end
		for (int var8 = 0; var8 < 3; ++var8) {
			if (this.fxLayers[var8].size() != 0) {
				int var9 = 0;
				if (var8 == 0) {
					var9 = this.renderer.getTexture("/particles.png");
				}

				if (var8 == 1) {
					var9 = this.renderer.getTexture("/terrain.png");
				}

				if (var8 == 2) {
					var9 = this.renderer.getTexture("/gui/items.png");
				}

				GL11.glBindTexture(GL11.GL_TEXTURE_2D, var9);
				Tessellator var10 = Tessellator.instance;
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				var10.startDrawingQuads();

				for (int var11 = 0; var11 < this.fxLayers[var8].size(); ++var11) {
					EntityFX var12 = (EntityFX)this.fxLayers[var8].get(var11);
					// Spout start
					if (var12 instanceof CustomEntityDiggingFX) {
						specialParticles.add((CustomEntityDiggingFX) var12);
						continue;
					}
					// Spout end
					var10.setBrightness(var12.getEntityBrightnessForRender(par2));
					var12.renderParticle(var10, par2, var3, var7, var4, var5, var6);
				}

				var10.draw();
			}
		}
		// Spout start
		for (CustomEntityDiggingFX customFX : specialParticles) {
			customFX.renderParticle(Tessellator.instance, par2, var3, var7, var4, var5, var6);
		}
		// Spout end
	}

	public void func_1187_b(Entity par1Entity, float par2) {
		float var3 = MathHelper.cos(par1Entity.rotationYaw * 3.1415927F / 180.0F);
		float var4 = MathHelper.sin(par1Entity.rotationYaw * 3.1415927F / 180.0F);
		float var5 = -var4 * MathHelper.sin(par1Entity.rotationPitch * 3.1415927F / 180.0F);
		float var6 = var3 * MathHelper.sin(par1Entity.rotationPitch * 3.1415927F / 180.0F);
		float var7 = MathHelper.cos(par1Entity.rotationPitch * 3.1415927F / 180.0F);
		byte var8 = 3;
		if (this.fxLayers[var8].size() != 0) {
			Tessellator var9 = Tessellator.instance;

			for (int var10 = 0; var10 < this.fxLayers[var8].size(); ++var10) {
				EntityFX var11 = (EntityFX)this.fxLayers[var8].get(var10);
				var9.setBrightness(var11.getEntityBrightnessForRender(par2));
				var11.renderParticle(var9, par2, var3, var7, var4, var5, var6);
			}

		}
	}

	public void clearEffects(World par1World) {
		this.worldObj = par1World;

		for (int var2 = 0; var2 < 4; ++var2) {
			this.fxLayers[var2].clear();
		}

	}

	public void addBlockDestroyEffects(int par1, int par2, int par3, int par4, int par5) {
		if (par4 != 0) {
			Block var6 = Block.blocksList[par4];
			byte var7 = 4;

			// Spout start
			//TODO this needs to be moved into BlockDesign's API
			boolean custom = false;
			GenericBlockDesign design = null;
			Texture customTexture = null;
			CustomBlock block = MaterialData.getCustomBlock(Spoutcraft.getWorld().getChunkAt(par1, par2, par3).getCustomBlockId(par1, par2, par3));
			if (block != null) {
				design = (GenericBlockDesign) block.getBlockDesign();
			}

			if (design != null) {
				customTexture = CustomTextureManager.getTextureFromUrl(design.getTextureAddon(), design.getTexureURL());
				if (customTexture != null) {
					custom = true;
				}
			}
			// Spout end

			for (int var8 = 0; var8 < var7; ++var8) {
				for (int var9 = 0; var9 < var7; ++var9) {
					for (int var10 = 0; var10 < var7; ++var10) {
						double var11 = (double)par1 + ((double)var8 + 0.5D) / (double)var7;
						double var13 = (double)par2 + ((double)var9 + 0.5D) / (double)var7;
						double var15 = (double)par3 + ((double)var10 + 0.5D) / (double)var7;
						int var17 = this.rand.nextInt(6);
						//Spout start
						if (custom) {
							this.addEffect((new CustomEntityDiggingFX(this.worldObj, var11, var13, var15, var11 - (double)par1 - 0.5D, var13 - (double)par2 - 0.5D, var15 - (double)par3 - 0.5D, var6, var17, par5, customTexture, design)).func_4041_a(par1, par2, par3));
						}
						else
						//Spout end
						this.addEffect((new EntityDiggingFX(this.worldObj, var11, var13, var15, var11 - (double)par1 - 0.5D, var13 - (double)par2 - 0.5D, var15 - (double)par3 - 0.5D, var6, var17, par5)).func_4041_a(par1, par2, par3));
					}
				}
			}

		}
	}

	public void addBlockHitEffects(int par1, int par2, int par3, int par4) {
		int var5 = this.worldObj.getBlockId(par1, par2, par3);
		if (var5 != 0) {
			Block var6 = Block.blocksList[var5];
			float var7 = 0.1F;
			double var8 = (double)par1 + this.rand.nextDouble() * (var6.maxX - var6.minX - (double)(var7 * 2.0F)) + (double)var7 + var6.minX;
			double var10 = (double)par2 + this.rand.nextDouble() * (var6.maxY - var6.minY - (double)(var7 * 2.0F)) + (double)var7 + var6.minY;
			double var12 = (double)par3 + this.rand.nextDouble() * (var6.maxZ - var6.minZ - (double)(var7 * 2.0F)) + (double)var7 + var6.minZ;
			if (par4 == 0) {
				var10 = (double)par2 + var6.minY - (double)var7;
			}

			if (par4 == 1) {
				var10 = (double)par2 + var6.maxY + (double)var7;
			}

			if (par4 == 2) {
				var12 = (double)par3 + var6.minZ - (double)var7;
			}

			if (par4 == 3) {
				var12 = (double)par3 + var6.maxZ + (double)var7;
			}

			if (par4 == 4) {
				var8 = (double)par1 + var6.minX - (double)var7;
			}

			if (par4 == 5) {
				var8 = (double)par1 + var6.maxX + (double)var7;
			}

			// Spout start
			boolean custom = false;
			GenericBlockDesign design = null;
			int data = this.worldObj.getBlockMetadata(par1, par2, par3);
			CustomBlock block = MaterialData.getCustomBlock(Spoutcraft.getWorld().getChunkAt(par1, par2, par3).getCustomBlockId(par1, par2, par3));
			if (block != null) {
				design = (GenericBlockDesign) block.getBlockDesign();
			}

			if (design != null) {
				Texture customTexture = CustomTextureManager.getTextureFromUrl(design.getTextureAddon(), design.getTexureURL());
				if (customTexture != null) {
					custom = true;
					this.addEffect((new CustomEntityDiggingFX(this.worldObj, var8, var10, var12, 0.0D, 0.0D, 0.0D, var6, par4, data, customTexture, design)).func_4041_a(par1, par2, par3).multiplyVelocity(0.2F).func_405_d(0.6F));
				}
			}
			if (!custom) {
				this.addEffect((new EntityDiggingFX(this.worldObj, var8, var10, var12, 0.0D, 0.0D, 0.0D, var6, par4, data)).func_4041_a(par1, par2, par3).multiplyVelocity(0.2F).func_405_d(0.6F));
			}
			// Spout end
		}
	}

	public String getStatistics() {
		return "" + (this.fxLayers[0].size() + this.fxLayers[1].size() + this.fxLayers[2].size());
	}
}
