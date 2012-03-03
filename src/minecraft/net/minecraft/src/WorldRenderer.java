package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.Chunk;
import net.minecraft.src.Entity;
import net.minecraft.src.ICamera;
import net.minecraft.src.MathHelper;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.RenderItem;
import net.minecraft.src.Tessellator;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityRenderer;
import net.minecraft.src.World;
import org.lwjgl.opengl.GL11;
//Spout start
import org.newdawn.slick.opengl.Texture;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.io.CustomTextureManager;
import org.spoutcraft.client.item.SpoutItem;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.block.design.GenericBlockDesign;
import org.spoutcraft.spoutcraftapi.material.CustomBlock;
import org.spoutcraft.spoutcraftapi.material.MaterialData;

import net.minecraft.client.Minecraft;
//Spout end

public class WorldRenderer {

	public World worldObj;
	private int glRenderList = -1;
	private static Tessellator tessellator = Tessellator.instance;
	public static int chunksUpdated = 0;
	public int posX;
	public int posY;
	public int posZ;
	public int posXMinus;
	public int posYMinus;
	public int posZMinus;
	public int posXClip;
	public int posYClip;
	public int posZClip;
	public boolean isInFrustum = false;
	public boolean[] skipRenderPass = new boolean[2];
	public int posXPlus;
	public int posYPlus;
	public int posZPlus;
	public boolean needsUpdate;
	public AxisAlignedBB rendererBoundingBox;
	public int chunkIndex;
	public boolean isVisible = true;
	public boolean isWaitingOnOcclusionQuery;
	public int glOcclusionQuery;
	public boolean isChunkLit;
	private boolean isInitialized = false;
	public List tileEntityRenderers = new ArrayList();
	private List tileEntities;

	public WorldRenderer(World par1World, List par2List, int par3, int par4, int par5, int par6) {
		this.worldObj = par1World;
		this.tileEntities = par2List;
		this.glRenderList = par6;
		this.posX = -999;
		this.setPosition(par3, par4, par5);
		this.needsUpdate = false;
	}

	public void setPosition(int par1, int par2, int par3) {
		if (par1 != this.posX || par2 != this.posY || par3 != this.posZ) {
			this.setDontDraw();
			this.posX = par1;
			this.posY = par2;
			this.posZ = par3;
			this.posXPlus = par1 + 8;
			this.posYPlus = par2 + 8;
			this.posZPlus = par3 + 8;
			this.posXClip = par1 & 1023;
			this.posYClip = par2;
			this.posZClip = par3 & 1023;
			this.posXMinus = par1 - this.posXClip;
			this.posYMinus = par2 - this.posYClip;
			this.posZMinus = par3 - this.posZClip;
			float var4 = 6.0F;
			this.rendererBoundingBox = AxisAlignedBB.getBoundingBox((double)((float)par1 - var4), (double)((float)par2 - var4), (double)((float)par3 - var4), (double)((float)(par1 + 16) + var4), (double)((float)(par2 + 16) + var4), (double)((float)(par3 + 16) + var4));
			GL11.glNewList(this.glRenderList + 2, GL11.GL_COMPILE);
			RenderItem.renderAABB(AxisAlignedBB.getBoundingBoxFromPool((double)((float)this.posXClip - var4), (double)((float)this.posYClip - var4), (double)((float)this.posZClip - var4), (double)((float)(this.posXClip + 16) + var4), (double)((float)(this.posYClip + 16) + var4), (double)((float)(this.posZClip + 16) + var4)));
			GL11.glEndList();
			this.markDirty();
		}
	}

	private void setupGLTranslation() {
		GL11.glTranslatef((float)this.posXClip, (float)this.posYClip, (float)this.posZClip);
	}

	public void updateRenderer() {
		//Spout Start
		if(this.needsUpdate) {
			++chunksUpdated;
			int x = this.posX;
			int y = this.posY;
			int z = this.posZ;
			int sizeXOffset = this.posX + 16;
			int sizeYOffset = this.posY + 16;
			int sizeZOffset = this.posZ + 16;

			for(int renderPass = 0; renderPass < 2; ++renderPass) {
				this.skipRenderPass[renderPass] = true;
			}

			Chunk.isLit = false;
			HashSet tileRenderers = new HashSet();
			tileRenderers.addAll(this.tileEntityRenderers);
			this.tileEntityRenderers.clear();
			//ChunkCache chunkCache = new ChunkCache(this.worldObj, x - 1, y - 1, z - 1, sizeXOffset + 1, sizeYOffset + 1, sizeZOffset + 1);
			RenderBlocks blockRenderer = new RenderBlocks(worldObj);
			
			List<String> hitTextures = new ArrayList<String>();
			List<String> hitTexturesPlugins = new ArrayList<String>();
			int currentTexture = 0;
			Minecraft game = SpoutClient.getHandle();

			hitTextures.add("/terrain.png");
			hitTexturesPlugins.add("");
			int defaultTexture = game.renderEngine.getTexture("/terrain.png");
			game.renderEngine.bindTexture(defaultTexture);
			
			short[] customBlockIds = Spoutcraft.getWorld().getChunkAt(posX, posY, posZ).getCustomBlockIds();

			blockRenderer.customIds = customBlockIds;

			for (int renderPass = 0; renderPass < 2; ++renderPass) {
				
				boolean skipRenderPass = false;
				boolean rendered = false;
				boolean drawBlock = false;
				
				if (!drawBlock) {
					drawBlock = true;
					GL11.glNewList(this.glRenderList + renderPass, GL11.GL_COMPILE);
					GL11.glPushMatrix();
					this.setupGLTranslation();
					GL11.glTranslatef(-8F, -8F, -8F);
					GL11.glScalef(1F, 1F, 1F);
					GL11.glTranslatef(8F, 8F, 8F);
					tessellator.startDrawingQuads();
					tessellator.setTranslationD((double)(-this.posX), (double)(-this.posY), (double)(-this.posZ));
				}
				
				game.renderEngine.bindTexture(defaultTexture);
				for (currentTexture = 0; currentTexture < hitTextures.size(); currentTexture++) {	
					int texture = defaultTexture;
					//First pass (currentTexture == 0) is for the default terrain.png texture. Subsquent passes are for custom textures
					//This design is to avoid unnessecary glBindTexture calls.
					if(currentTexture > 0) {
						Texture customTexture = CustomTextureManager.getTextureFromUrl(hitTexturesPlugins.get(currentTexture), hitTextures.get(currentTexture));
						if (customTexture != null) {
							texture = customTexture.getTextureID();
							game.renderEngine.bindTexture(texture);
							if(texture <= 0) {
								texture = defaultTexture;
							}
						}
					}

					if(tessellator.textureOverride != texture){
						tessellator.draw();
						tessellator.textureOverride = texture;
						tessellator.startDrawingQuads();
					}
					
					float[] oldBounds = new float[6];
					
					//The x,y,z order is important, don't change!
					for (int dx = x; dx < sizeXOffset; ++dx) {
						for (int dz = z; dz < sizeZOffset; ++dz) {
							for (int dy = y; dy < sizeYOffset; ++dy) {
								int id = worldObj.getBlockId(dx, dy, dz);
								if (id > 0) {
									String customTexture = null; 
									String customTextureAddon = null;
									GenericBlockDesign design = null;

									CustomBlock mat = null;
									if (customBlockIds != null) {
										int key = ((dx & 0xF) << 11) | ((dz & 0xF) << 7) | (dy & 0x7F);
										if (customBlockIds[key] != 0) {
											mat = MaterialData.getCustomBlock(customBlockIds[key]);
											if (mat != null) {
												design = (GenericBlockDesign) mat.getBlockDesign();
											}
										}
									}
									
									if (design != null) {
										customTexture = design.getTexureURL();
										customTextureAddon = design.getTextureAddon();
									}
									
									
									if(customTexture != null){
										boolean found = false;
										
										//Search for the custom texture in our list
										for(int i = 0; i < hitTextures.size(); i++){
											if(hitTextures.get(i).equals(customTexture) && hitTexturesPlugins.get(i).equals(customTextureAddon)) {
												found = true;
												break;
											}
										}
										//If it is not already accounted for, add it so we do an additional pass for it.
										if(!found) {
											hitTextures.add(customTexture);
											hitTexturesPlugins.add(customTextureAddon);
										}
										
										//Do not render if we are using a different texture than the current one
										if(!hitTextures.get(currentTexture).equals(customTexture) || !hitTexturesPlugins.get(currentTexture).equals(customTextureAddon)) {
											continue;
										}
									}
									//Do not render if we are not using the terrain.png and can't find a valid texture for this custom block
									else if (currentTexture != 0) { 
										continue;
									}
									
									Block block = Block.blocksList[id];
									if (renderPass == 0 && block.func_48205_p()) {
										TileEntity var20 = worldObj.getBlockTileEntity(dx, dy, dz);
										if (TileEntityRenderer.instance.hasSpecialRenderer(var20)) {
											this.tileEntityRenderers.add(var20);
										}
									}

									//Determine which pass this block needs to be rendered on
									int blockRenderPass = block.getRenderBlockPass();
									if (design != null) {
										blockRenderPass = design.getRenderPass();
									}
									
									if (blockRenderPass != renderPass) {
										skipRenderPass = true;
									}
									else {
										if (design != null) {
											oldBounds[0] = (float) block.minX;
											oldBounds[1] = (float) block.minY;
											oldBounds[2] = (float) block.minZ;
											oldBounds[3] = (float) block.maxX;
											oldBounds[4] = (float) block.maxY;
											oldBounds[5] = (float) block.maxZ;
											block.setBlockBounds(design.getLowXBound(), design.getLowYBound(), design.getLowZBound(), design.getHighXBound(), design.getHighYBound(), design.getHighZBound());
											rendered |= design.renderBlock(mat, dx, dy, dz);
											block.setBlockBounds(oldBounds[0], oldBounds[1], oldBounds[2], oldBounds[3], oldBounds[4], oldBounds[5]);
										}
										else {
											rendered |= blockRenderer.renderBlockByRenderType(block, dx, dy, dz);
										}
									}
								}
							}
						}
					}
				}

				if (drawBlock) {
					tessellator.draw();
					tessellator.textureOverride = 0;
					GL11.glPopMatrix();
					GL11.glEndList();
					game.renderEngine.bindTexture(defaultTexture);
					tessellator.setTranslationD(0.0D, 0.0D, 0.0D);
				} else {
					rendered = false;
				}

				if (rendered) {
					this.skipRenderPass[renderPass] = false;
				}

				if (!skipRenderPass) {
					break;
				}
			}
			
			HashSet var24 = new HashSet();
			var24.addAll(this.tileEntityRenderers);
			var24.removeAll(tileRenderers);
			this.tileEntities.addAll(var24);
			tileRenderers.removeAll(this.tileEntityRenderers);
			//Spout End
			this.tileEntities.removeAll(tileRenderers);
			this.isChunkLit = Chunk.isLit;
			this.isInitialized = true;
			
			blockRenderer.customIds = null;
		}
	}

	public float distanceToEntitySquared(Entity var1) {
		float var2 = (float)(var1.posX - (double)this.posXPlus);
		float var3 = (float)(var1.posY - (double)this.posYPlus);
		float var4 = (float)(var1.posZ - (double)this.posZPlus);
		return var2 * var2 + var3 * var3 + var4 * var4;
	}

	public void setDontDraw() {
		for(int var1 = 0; var1 < 2; ++var1) {
			this.skipRenderPass[var1] = true;
		}

		this.isInFrustum = false;
		this.isInitialized = false;
	}

	public void stopRendering() {
		this.setDontDraw();
		this.worldObj = null;
	}

	public int getGLCallListForPass(int var1) {
		return !this.isInFrustum?-1:(!this.skipRenderPass[var1]?this.glRenderList + var1:-1);
	}

	public void updateInFrustrum(ICamera var1) {
		this.isInFrustum = var1.isBoundingBoxInFrustum(this.rendererBoundingBox);
	}

	public void callOcclusionQueryList() {
		GL11.glCallList(this.glRenderList + 2);
	}

	public boolean skipAllRenderPasses() {
		return !this.isInitialized?false:this.skipRenderPass[0] && this.skipRenderPass[1];
	}

	public void markDirty() {
		this.needsUpdate = true;
	}

}
