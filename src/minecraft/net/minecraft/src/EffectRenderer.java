package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityDiggingFX;
import net.minecraft.src.EntityFX;
import net.minecraft.src.MathHelper;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;

//Spout start
import java.util.LinkedList;

import org.getspout.spout.io.CustomTextureManager;
import org.getspout.spout.item.CustomEntityDiggingFX;
import org.getspout.spout.item.SpoutCustomBlockDesign;
import org.getspout.spout.item.SpoutItem;
//Spout end
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

public class EffectRenderer {

	protected World worldObj;
	private List[] fxLayers = new List[4];
	private RenderEngine renderer;
	private Random rand = new Random();


	public EffectRenderer(World var1, RenderEngine var2) {
		if(var1 != null) {
			this.worldObj = var1;
		}

		this.renderer = var2;

		for(int var3 = 0; var3 < 4; ++var3) {
			this.fxLayers[var3] = new ArrayList();
		}

	}

	public void addEffect(EntityFX var1) {
		int var2 = var1.getFXLayer();
		if(this.fxLayers[var2].size() >= 4000) {
			this.fxLayers[var2].remove(0);
		}

		this.fxLayers[var2].add(var1);
	}

	public void updateEffects() {
		for(int var1 = 0; var1 < 4; ++var1) {
			for(int var2 = 0; var2 < this.fxLayers[var1].size(); ++var2) {
				EntityFX var3 = (EntityFX)this.fxLayers[var1].get(var2);
				var3.onUpdate();
				if(var3.isDead) {
					this.fxLayers[var1].remove(var2--);
				}
			}
		}

	}

	public void renderParticles(Entity var1, float var2) {
		float var3 = MathHelper.cos(var1.rotationYaw * 3.1415927F / 180.0F);
		float var4 = MathHelper.sin(var1.rotationYaw * 3.1415927F / 180.0F);
		float var5 = -var4 * MathHelper.sin(var1.rotationPitch * 3.1415927F / 180.0F);
		float var6 = var3 * MathHelper.sin(var1.rotationPitch * 3.1415927F / 180.0F);
		float var7 = MathHelper.cos(var1.rotationPitch * 3.1415927F / 180.0F);
		EntityFX.interpPosX = var1.lastTickPosX + (var1.posX - var1.lastTickPosX) * (double)var2;
		EntityFX.interpPosY = var1.lastTickPosY + (var1.posY - var1.lastTickPosY) * (double)var2;
		EntityFX.interpPosZ = var1.lastTickPosZ + (var1.posZ - var1.lastTickPosZ) * (double)var2;

		//Spout start
		LinkedList<CustomEntityDiggingFX> specialParticles = new LinkedList<CustomEntityDiggingFX>();
		//Spout end
		for(int var8 = 0; var8 < 3; ++var8) {
			if(this.fxLayers[var8].size() != 0) {
				int var9 = 0;
				if(var8 == 0) {
					var9 = this.renderer.getTexture("/particles.png");
				}

				if(var8 == 1) {
					var9 = this.renderer.getTexture("/terrain.png");
				}

				if(var8 == 2) {
					var9 = this.renderer.getTexture("/gui/items.png");
				}

				GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, var9);
				Tessellator var10 = Tessellator.instance;
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				var10.startDrawingQuads();

				for(int var11 = 0; var11 < this.fxLayers[var8].size(); ++var11) {
					EntityFX var12 = (EntityFX)this.fxLayers[var8].get(var11);
					
					//Spout start
					if (var12 instanceof CustomEntityDiggingFX){
						specialParticles.add((CustomEntityDiggingFX)var12);
						continue;
					}
					//Spout end
					
					var10.func_35835_b(var12.func_35115_a(var2));
					var12.renderParticle(var10, var2, var3, var7, var4, var5, var6);
				}

				var10.draw();
			}
		}
		
		//Spout start
		for (CustomEntityDiggingFX customFX : specialParticles) {
			customFX.renderParticle(Tessellator.instance, var2, var3, var7, var4, var5, var6);
		}
		//Spout end

	}

	public void func_1187_b(Entity var1, float var2) {
		float var3 = MathHelper.cos(var1.rotationYaw * 3.1415927F / 180.0F);
		float var4 = MathHelper.sin(var1.rotationYaw * 3.1415927F / 180.0F);
		float var5 = -var4 * MathHelper.sin(var1.rotationPitch * 3.1415927F / 180.0F);
		float var6 = var3 * MathHelper.sin(var1.rotationPitch * 3.1415927F / 180.0F);
		float var7 = MathHelper.cos(var1.rotationPitch * 3.1415927F / 180.0F);
		byte var8 = 3;
		if(this.fxLayers[var8].size() != 0) {
			Tessellator var9 = Tessellator.instance;

			for(int var10 = 0; var10 < this.fxLayers[var8].size(); ++var10) {
				EntityFX var11 = (EntityFX)this.fxLayers[var8].get(var10);
				var9.func_35835_b(var11.func_35115_a(var2));
				var11.renderParticle(var9, var2, var3, var7, var4, var5, var6);
			}

		}
	}

	public void clearEffects(World var1) {
		this.worldObj = var1;

		for(int var2 = 0; var2 < 4; ++var2) {
			this.fxLayers[var2].clear();
		}

	}

	public void addBlockDestroyEffects(int var1, int var2, int var3, int var4, int var5) {
		if(var4 != 0) {
			Block var6 = Block.blocksList[var4];
			byte var7 = 4;
			
			//Spout start
			boolean custom = false;
			SpoutCustomBlockDesign design = null;
			Texture customTexture = null;
			int data = worldObj.getBlockMetadata(var1, var2, var3);
			if (SpoutItem.isBlockOverride(var1, var2, var3)) {
				design = SpoutItem.getCustomBlockDesign(var1, var2, var3);
			} else {
				design = SpoutItem.getCustomBlockDesign(var5, data);
			}
			if (design != null) {
				customTexture = CustomTextureManager.getTextureFromUrl(design.getTextureAddon(), design.getTexureURL());
				if (customTexture != null) {
					custom = true;
				}
			}
			//Spout end

			for(int var8 = 0; var8 < var7; ++var8) {
				for(int var9 = 0; var9 < var7; ++var9) {
					for(int var10 = 0; var10 < var7; ++var10) {
						double var11 = (double)var1 + ((double)var8 + 0.5D) / (double)var7;
						double var13 = (double)var2 + ((double)var9 + 0.5D) / (double)var7;
						double var15 = (double)var3 + ((double)var10 + 0.5D) / (double)var7;
						int var17 = this.rand.nextInt(6);
						
						//Spout start
						if (custom) {
							this.addEffect((new CustomEntityDiggingFX(this.worldObj, var11, var13, var15, var11 - (double)var1 - 0.5D, var13 - (double)var2 - 0.5D, var15 - (double)var3 - 0.5D, var6, var17, var5, customTexture, design)).func_4041_a(var1, var2, var3));
						}
						else {
							this.addEffect((new EntityDiggingFX(this.worldObj, var11, var13, var15, var11 - (double)var1 - 0.5D, var13 - (double)var2 - 0.5D, var15 - (double)var3 - 0.5D, var6, var17, var5)).func_4041_a(var1, var2, var3));
						}
						//Spout end
					
					}
				}
			}

		}
	}

	public void addBlockHitEffects(int var1, int var2, int var3, int var4) {
		int var5 = this.worldObj.getBlockId(var1, var2, var3);
		if(var5 != 0) {
			Block var6 = Block.blocksList[var5];
			float var7 = 0.1F;
			double var8 = (double)var1 + this.rand.nextDouble() * (var6.maxX - var6.minX - (double)(var7 * 2.0F)) + (double)var7 + var6.minX;
			double var10 = (double)var2 + this.rand.nextDouble() * (var6.maxY - var6.minY - (double)(var7 * 2.0F)) + (double)var7 + var6.minY;
			double var12 = (double)var3 + this.rand.nextDouble() * (var6.maxZ - var6.minZ - (double)(var7 * 2.0F)) + (double)var7 + var6.minZ;
			if(var4 == 0) {
				var10 = (double)var2 + var6.minY - (double)var7;
			}

			if(var4 == 1) {
				var10 = (double)var2 + var6.maxY + (double)var7;
			}

			if(var4 == 2) {
				var12 = (double)var3 + var6.minZ - (double)var7;
			}

			if(var4 == 3) {
				var12 = (double)var3 + var6.maxZ + (double)var7;
			}

			if(var4 == 4) {
				var8 = (double)var1 + var6.minX - (double)var7;
			}

			if(var4 == 5) {
				var8 = (double)var1 + var6.maxX + (double)var7;
			}
			
			//Spout start
			boolean custom = false;
			SpoutCustomBlockDesign design = null;
			int data = worldObj.getBlockMetadata(var1, var2, var3);
			if (SpoutItem.isBlockOverride(var1, var2, var3)) {
				design = SpoutItem.getCustomBlockDesign(var1, var2, var3);
			} else {
				design = SpoutItem.getCustomBlockDesign(var5, data);
			}
			if (design != null) {
				Texture customTexture = CustomTextureManager.getTextureFromUrl(design.getTextureAddon(), design.getTexureURL());
				if (customTexture != null) {
					custom = true;
					this.addEffect((new CustomEntityDiggingFX(this.worldObj, var8, var10, var12, 0.0D, 0.0D, 0.0D, var6, var4, data, customTexture, design)).func_4041_a(var1, var2, var3).func_407_b(0.2F).func_405_d(0.6F));
				}
			}
			if (!custom) {
				this.addEffect((new EntityDiggingFX(this.worldObj, var8, var10, var12, 0.0D, 0.0D, 0.0D, var6, var4, data)).func_4041_a(var1, var2, var3).func_407_b(0.2F).func_405_d(0.6F));
			}
			//Spout end
		}
	}

	public String getStatistics() {
		return "" + (this.fxLayers[0].size() + this.fxLayers[1].size() + this.fxLayers[2].size());
	}
}
