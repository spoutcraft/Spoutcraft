package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.lwjgl.opengl.GL11;
// Spout Start
import java.util.LinkedList;

import org.newdawn.slick.opengl.Texture;
import org.spoutcraft.client.io.CustomTextureManager;
import org.spoutcraft.client.item.CustomEntityDiggingFX;
import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.block.design.GenericBlockDesign;
import org.spoutcraft.api.material.CustomBlock;
import org.spoutcraft.api.material.MaterialData;

// Spout End

public class EffectRenderer {

	/** Reference to the World object. */
	protected World worldObj;
	private List[] fxLayers = new List[4];
	private List field_90038_c = new ArrayList();
	private RenderEngine renderer;

	/** RNG. */
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
		this.field_90038_c.add(par1EntityFX);
	}

	private void func_90037_b(EntityFX par1EntityFX) {
		int var2 = par1EntityFX.getFXLayer();

		if (this.fxLayers[var2].size() >= 4000) {
			this.fxLayers[var2].remove(0);
		}

		this.fxLayers[var2].add(par1EntityFX);
	}

	public void updateEffects() {
		for (int var1 = 0; var1 < 4; ++var1) {
			EntityFX var2 = null;

			try {
				for (int var3 = 0; var3 < this.fxLayers[var1].size(); ++var3) {
					var2 = (EntityFX)this.fxLayers[var1].get(var3);
					var2.onUpdate();

					if (var2.isDead) {
						this.fxLayers[var1].remove(var3--);
					}
				}
			} catch (Throwable var7) {
				CrashReport var4 = CrashReport.func_85055_a(var7, "Uncaught exception while ticking particles");
				CrashReportCategory var5 = var4.func_85058_a("Particle engine details");
				var5.addCrashSectionCallable("Last ticked particle", new CallableLastTickedParticle(this, var2));
				var5.addCrashSection("Texture index", Integer.valueOf(var1));
				throw new ReportedException(var4);
			}
		}

		Iterator var8 = this.field_90038_c.iterator();

		while (var8.hasNext()) {
			this.func_90037_b((EntityFX)var8.next());
		}

		this.field_90038_c.clear();
	}

	/**
	 * Renders all current particles. Args player, partialTickTime
	 */
	public void renderParticles(Entity par1Entity, float par2) {
		float var3 = ActiveRenderInfo.rotationX;
		float var4 = ActiveRenderInfo.rotationZ;
		float var5 = ActiveRenderInfo.rotationYZ;
		float var6 = ActiveRenderInfo.rotationXY;
		float var7 = ActiveRenderInfo.rotationXZ;
		EntityFX.interpPosX = par1Entity.lastTickPosX + (par1Entity.posX - par1Entity.lastTickPosX) * (double)par2;
		EntityFX.interpPosY = par1Entity.lastTickPosY + (par1Entity.posY - par1Entity.lastTickPosY) * (double)par2;
		EntityFX.interpPosZ = par1Entity.lastTickPosZ + (par1Entity.posZ - par1Entity.lastTickPosZ) * (double)par2;

		// Spout Start
		LinkedList<CustomEntityDiggingFX> specialParticles = new LinkedList<CustomEntityDiggingFX>();
		// Spout End
		for (int var8 = 0; var8 < 3; ++var8) {
			if (!this.fxLayers[var8].isEmpty()) {
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
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				var10.startDrawingQuads();

				for (int var11 = 0; var11 < this.fxLayers[var8].size(); ++var11) {
					EntityFX var12 = (EntityFX)this.fxLayers[var8].get(var11);
					// Spout Start
					if (var12 instanceof CustomEntityDiggingFX) {
						specialParticles.add((CustomEntityDiggingFX) var12);
						continue;
					}
					// Spout End
					var10.setBrightness(var12.getBrightnessForRender(par2));
					var12.renderParticle(var10, par2, var3, var7, var4, var5, var6);
				}

				var10.draw();
				GL11.glDisable(GL11.GL_BLEND);
			}
		}
		// Spout Start
		for (CustomEntityDiggingFX customFX : specialParticles) {
			customFX.renderParticle(Tessellator.instance, par2, var3, var7, var4, var5, var6);
		}
		// Spout End
	}

	public void renderLitParticles(Entity par1Entity, float par2) {
		float var4 = MathHelper.cos(par1Entity.rotationYaw * 0.017453292F);
		float var5 = MathHelper.sin(par1Entity.rotationYaw * 0.017453292F);
		float var6 = -var5 * MathHelper.sin(par1Entity.rotationPitch * 0.017453292F);
		float var7 = var4 * MathHelper.sin(par1Entity.rotationPitch * 0.017453292F);
		float var8 = MathHelper.cos(par1Entity.rotationPitch * 0.017453292F);
		byte var9 = 3;

		if (!this.fxLayers[var9].isEmpty()) {
			Tessellator var10 = Tessellator.instance;

			for (int var11 = 0; var11 < this.fxLayers[var9].size(); ++var11) {
				EntityFX var12 = (EntityFX)this.fxLayers[var9].get(var11);
				var10.setBrightness(var12.getBrightnessForRender(par2));
				var12.renderParticle(var10, par2, var4, var8, var5, var6, var7);
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

			// Spout Start
			// TODO This needs to be moved into BlockDesign's API
			boolean custom = false;
			GenericBlockDesign design = null;
			Texture customTexture = null;
			CustomBlock block = MaterialData.getCustomBlock(worldObj.world.getChunkAt(par1, par2, par3).getCustomBlockId(par1, par2, par3));
			if (block != null) {
				design = (GenericBlockDesign) block.getBlockDesign();
			}

			if (design != null) {
				customTexture = CustomTextureManager.getTextureFromUrl(design.getTextureAddon(), design.getTexureURL());
				if (customTexture != null) {
					custom = true;
				}
			}
			// Spout End

			for (int var8 = 0; var8 < var7; ++var8) {
				for (int var9 = 0; var9 < var7; ++var9) {
					for (int var10 = 0; var10 < var7; ++var10) {
						double var11 = (double)par1 + ((double)var8 + 0.5D) / (double)var7;
						double var13 = (double)par2 + ((double)var9 + 0.5D) / (double)var7;
						double var15 = (double)par3 + ((double)var10 + 0.5D) / (double)var7;
						int var17 = this.rand.nextInt(6);
						// Spout Start
						if (custom) {
							this.addEffect((new CustomEntityDiggingFX(this.worldObj, var11, var13, var15, var11 - (double)par1 - 0.5D, var13 - (double)par2 - 0.5D, var15 - (double)par3 - 0.5D, var6, var17, par5, customTexture, design)).func_70596_a(par1, par2, par3));
						}
						else
						// Spout End
						this.addEffect((new EntityDiggingFX(this.worldObj, var11, var13, var15, var11 - (double)par1 - 0.5D, var13 - (double)par2 - 0.5D, var15 - (double)par3 - 0.5D, var6, var17, par5)).func_70596_a(par1, par2, par3));
					}
				}
			}
		}
	}

	/**
	 * Adds block hit particles for the specified block. Args: x, y, z, sideHit
	 */
	public void addBlockHitEffects(int par1, int par2, int par3, int par4) {
		int var5 = this.worldObj.getBlockId(par1, par2, par3);

		if (var5 != 0) {
			Block var6 = Block.blocksList[var5];
			float var7 = 0.1F;
			double var8 = (double)par1 + this.rand.nextDouble() * (var6.getBlockBoundsMaxX() - var6.getBlockBoundsMinX() - (double)(var7 * 2.0F)) + (double)var7 + var6.getBlockBoundsMinX();
			double var10 = (double)par2 + this.rand.nextDouble() * (var6.getBlockBoundsMaxY() - var6.getBlockBoundsMinY() - (double)(var7 * 2.0F)) + (double)var7 + var6.getBlockBoundsMinY();
			double var12 = (double)par3 + this.rand.nextDouble() * (var6.getBlockBoundsMaxZ() - var6.getBlockBoundsMinZ() - (double)(var7 * 2.0F)) + (double)var7 + var6.getBlockBoundsMinZ();

			if (par4 == 0) {
				var10 = (double)par2 + var6.getBlockBoundsMinY() - (double)var7;
			}

			if (par4 == 1) {
				var10 = (double)par2 + var6.getBlockBoundsMaxY() + (double)var7;
			}

			if (par4 == 2) {
				var12 = (double)par3 + var6.getBlockBoundsMinZ() - (double)var7;
			}

			if (par4 == 3) {
				var12 = (double)par3 + var6.getBlockBoundsMaxZ() + (double)var7;
			}

			if (par4 == 4) {
				var8 = (double)par1 + var6.getBlockBoundsMinX() - (double)var7;
			}

			if (par4 == 5) {
				var8 = (double)par1 + var6.getBlockBoundsMaxX() + (double)var7;
			}

			// Spout Start
			boolean custom = false;
			GenericBlockDesign design = null;
			int data = this.worldObj.getBlockMetadata(par1, par2, par3);
			CustomBlock block = MaterialData.getCustomBlock(worldObj.world.getChunkAt(par1, par2, par3).getCustomBlockId(par1, par2, par3));
			if (block != null) {
				design = (GenericBlockDesign) block.getBlockDesign();
			}

			if (design != null) {
				Texture customTexture = CustomTextureManager.getTextureFromUrl(design.getTextureAddon(), design.getTexureURL());
				if (customTexture != null) {
					custom = true;
					this.addEffect((new CustomEntityDiggingFX(this.worldObj, var8, var10, var12, 0.0D, 0.0D, 0.0D, var6, par4, data, customTexture, design)).func_70596_a(par1, par2, par3).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
				}
			}
			if (!custom) {
				this.addEffect((new EntityDiggingFX(this.worldObj, var8, var10, var12, 0.0D, 0.0D, 0.0D, var6, par4, data)).func_70596_a(par1, par2, par3).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
			}
			// Spout End
		}
	}

	public String getStatistics() {
		return "" + (this.fxLayers[0].size() + this.fxLayers[1].size() + this.fxLayers[2].size());
	}
}
