package net.minecraft.src;

import net.minecraft.src.Item;
import net.minecraft.src.Minecraft;
import net.minecraft.src.World;

import org.newdawn.slick.opengl.Texture;

import org.spoutcraft.client.io.CustomTextureManager;
import org.spoutcraft.client.SpoutClient;

public class EntityBreakingFX extends EntityFX {

	public Texture currentTexture = null;

	// Spout > Lots of things changed in this class.
	public EntityBreakingFX(World par1World, double par2, double par4, double par6, Item par8Item) {
		this(par1World, par2, par4, par6, par8Item, 0, null);				
	}

	public EntityBreakingFX(World par1World, double par2, double par4, double par6, Item par8Item, int par9) {
		this(par1World, par2, par4, par6, par8Item, par9, null);				
	}

	public EntityBreakingFX(World par1World, double par2, double par4, double par6, Item par8Item, int par9, Texture texture) {			
		super(par1World, par2, par4, par6, 0.0D, 0.0D, 0.0D);		
		this.currentTexture = texture;		
		this.setParticleIcon(par8Item.getIconFromDamage(par9));		
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
		this.particleGravity = Block.blockSnow.blockParticleGravity;
		this.particleScale /= 2.0F;

	}

	public EntityBreakingFX(World par1World, double par2, double par4, double par6, double par8, double par10, double par12, Item par14Item, int par15) {		
		this(par1World, par2, par4, par6, par8, par10, par12, par14Item, par15, null);
	}

	public EntityBreakingFX(World par1World, double par2, double par4, double par6, double par8, double par10, double par12, Item par14Item, int par15, Texture texture) {
		this(par1World, par2, par4, par6, par14Item, par15, texture);		
		this.motionX *= 0.10000000149011612D;
		this.motionY *= 0.10000000149011612D;
		this.motionZ *= 0.10000000149011612D;
		this.motionX += par8;
		this.motionY += par10;
		this.motionZ += par12;
	}

	public int getFXLayer() {
		return 2;
	}

	public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7) {		
		if (currentTexture != null) {
			SpoutClient.getHandle().renderEngine.bindTexture(currentTexture.getTextureID());		
		}
		float var8 = ((float)this.particleTextureIndexX + this.particleTextureJitterX / 4.0F) / 16.0F;
		float var9 = var8 + 0.015609375F;
		float var10 = ((float)this.particleTextureIndexY + this.particleTextureJitterY / 4.0F) / 16.0F;
		float var11 = var10 + 0.015609375F;
		float var12 = 0.1F * this.particleScale;

		if (this.particleIcon != null) {
			var8 = this.particleIcon.getInterpolatedU((double)(this.particleTextureJitterX / 4.0F * 16.0F));
			var9 = this.particleIcon.getInterpolatedU((double)((this.particleTextureJitterX + 1.0F) / 4.0F * 16.0F));
			var10 = this.particleIcon.getInterpolatedV((double)(this.particleTextureJitterY / 4.0F * 16.0F));
			var11 = this.particleIcon.getInterpolatedV((double)((this.particleTextureJitterY + 1.0F) / 4.0F * 16.0F));
		}
	
		// ToDo: More work needed here to set the custom X/Y coords of the vertex calculations since we are not using icons for custom blocks.
		float var13 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)par2 - interpPosX);
		float var14 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)par2 - interpPosY);
		float var15 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)par2 - interpPosZ);
		float var16 = 1.0F;
		par1Tessellator.setColorOpaque_F(var16 * this.particleRed, var16 * this.particleGreen, var16 * this.particleBlue);
		par1Tessellator.addVertexWithUV((double)(var13 - par3 * var12 - par6 * var12), (double)(var14 - par4 * var12), (double)(var15 - par5 * var12 - par7 * var12), (double)var8, (double)var11);
		par1Tessellator.addVertexWithUV((double)(var13 - par3 * var12 + par6 * var12), (double)(var14 + par4 * var12), (double)(var15 - par5 * var12 + par7 * var12), (double)var8, (double)var10);
		par1Tessellator.addVertexWithUV((double)(var13 + par3 * var12 + par6 * var12), (double)(var14 + par4 * var12), (double)(var15 + par5 * var12 + par7 * var12), (double)var9, (double)var10);
		par1Tessellator.addVertexWithUV((double)(var13 + par3 * var12 - par6 * var12), (double)(var14 - par4 * var12), (double)(var15 + par5 * var12 - par7 * var12), (double)var9, (double)var11);
	}
}
