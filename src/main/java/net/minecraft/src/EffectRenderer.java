package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.prupe.mcpatcher.sky.FireworksHelper;

public class EffectRenderer {
	private static final ResourceLocation particleTextures = new ResourceLocation("textures/particle/particles.png");
	/** Reference to the World object. */
	// MCPatcher Start - protected to public
	public World worldObj;
	// MCPatcher End
	private List[] fxLayers = new List[5];
	private TextureManager renderer;

	/** RNG. */
	private Random rand = new Random();

	public EffectRenderer(World par1World, TextureManager par2TextureManager) {
		if (par1World != null) {
			this.worldObj = par1World;
		}

		this.renderer = par2TextureManager;

		for (int var3 = 0; var3 < 5; ++var3) {
			this.fxLayers[var3] = new ArrayList();
		}
	}
	
	public void addEffect(EntityFX par1EntityFX) {
		int var2 = FireworksHelper.getFXLayer(par1EntityFX);

		if (this.fxLayers[var2].size() >= 4000) {
			this.fxLayers[var2].remove(0);
		}

		this.fxLayers[var2].add(par1EntityFX);
	}

	public void updateEffects() {
		for (int var1 = 0; var1 < 5; ++var1) {
			for (int var2 = 0; var2 < this.fxLayers[var1].size(); ++var2) {
				EntityFX var3 = (EntityFX)this.fxLayers[var1].get(var2);
				var3.onUpdate();

				if (var3.isDead) {
					this.fxLayers[var1].remove(var2--);
				}
			}
		}
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

		for (int var8 = 0; var8 < 5; ++var8) {
			if (!FireworksHelper.skipThisLayer(this.fxLayers[var8].isEmpty(), var8)) {
				switch (var8) {
					case 0:
					default:
						this.renderer.bindTexture(particleTextures);
						break;

					case 1:
						this.renderer.bindTexture(TextureMap.locationBlocksTexture);
						break;

					case 2:
						this.renderer.bindTexture(TextureMap.locationItemsTexture);
				}

				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glDepthMask(false);
				GL11.glEnable(GL11.GL_BLEND);
				FireworksHelper.setParticleBlendMethod(var8);
				GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
				Tessellator var9 = Tessellator.instance;
				var9.startDrawingQuads();

				for (int var10 = 0; var10 < this.fxLayers[var8].size(); ++var10) {
					EntityFX var11 = (EntityFX)this.fxLayers[var8].get(var10);
					var9.setBrightness(var11.getBrightnessForRender(par2));
					var11.renderParticle(var9, par2, var3, var7, var4, var5, var6);
				}

				var9.draw();
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glDepthMask(true);
				GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
			}
		}
	}

	public void renderLitParticles(Entity par1Entity, float par2) {
		float var3 = 0.017453292F;
		float var4 = MathHelper.cos(par1Entity.rotationYaw * 0.017453292F);
		float var5 = MathHelper.sin(par1Entity.rotationYaw * 0.017453292F);
		float var6 = -var5 * MathHelper.sin(par1Entity.rotationPitch * 0.017453292F);
		float var7 = var4 * MathHelper.sin(par1Entity.rotationPitch * 0.017453292F);
		float var8 = MathHelper.cos(par1Entity.rotationPitch * 0.017453292F);
		byte var9 = 3;
		List var10 = this.fxLayers[var9];

		if (!var10.isEmpty()) {
			Tessellator var11 = Tessellator.instance;

			for (int var12 = 0; var12 < var10.size(); ++var12) {
				EntityFX var13 = (EntityFX)var10.get(var12);
				var11.setBrightness(var13.getBrightnessForRender(par2));
				var13.renderParticle(var11, par2, var4, var8, var5, var6, var7);
			}
		}
	}

	public void clearEffects(World par1World) {
		this.worldObj = par1World;

		for (int var2 = 0; var2 < 5; ++var2) {
			this.fxLayers[var2].clear();
		}
	}

	public void addBlockDestroyEffects(int par1, int par2, int par3, int par4, int par5) {
		if (par4 != 0) {
			Block var6 = Block.blocksList[par4];
			byte var7 = 4;

			for (int var8 = 0; var8 < var7; ++var8) {
				for (int var9 = 0; var9 < var7; ++var9) {
					for (int var10 = 0; var10 < var7; ++var10) {
						double var11 = (double)par1 + ((double)var8 + 0.5D) / (double)var7;
						double var13 = (double)par2 + ((double)var9 + 0.5D) / (double)var7;
						double var15 = (double)par3 + ((double)var10 + 0.5D) / (double)var7;
						this.addEffect((new EntityDiggingFX(this.worldObj, var11, var13, var15, var11 - (double)par1 - 0.5D, var13 - (double)par2 - 0.5D, var15 - (double)par3 - 0.5D, var6, par5)).applyColourMultiplier(par1, par2, par3));
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

			this.addEffect((new EntityDiggingFX(this.worldObj, var8, var10, var12, 0.0D, 0.0D, 0.0D, var6, this.worldObj.getBlockMetadata(par1, par2, par3))).applyColourMultiplier(par1, par2, par3).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
		}
	}

	public String getStatistics() {
		return "" + (this.fxLayers[0].size() + this.fxLayers[1].size() + this.fxLayers[2].size());
	}
}
