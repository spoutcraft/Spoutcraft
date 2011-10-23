package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.Chunk;
import net.minecraft.src.ChunkCache;
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
import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.config.ConfigReader;
import org.getspout.spout.io.CustomTextureManager;
import org.getspout.spout.item.SpoutCustomBlockDesign;
import org.getspout.spout.item.SpoutItem;
import org.newdawn.slick.opengl.Texture;
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
	public int sizeWidth;
	public int sizeHeight;
	public int sizeDepth;
	public int posXMinus;
	public int posYMinus;
	public int posZMinus;
	public int posXClip;
	public int posYClip;
	public int posZClip;
	public boolean isInFrustum = false;
	public boolean[] skipRenderPass = new boolean[3]; //Spout
	public int posXPlus;
	public int posYPlus;
	public int posZPlus;
	public float rendererRadius;
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
	//Spout Start
	public boolean isVisibleFromPosition = false;
	public double visibleFromX;
	public double visibleFromY;
	public double visibleFromZ;
	private boolean needsBoxUpdate = false;
	public boolean isInFrustrumFully = false;
	//Spout End

	public WorldRenderer(World var1, List var2, int var3, int var4, int var5, int var6, int var7) {
		this.worldObj = var1;
		this.tileEntities = var2;
		this.sizeWidth = this.sizeHeight = this.sizeDepth = var6;
		this.rendererRadius = MathHelper.sqrt_float((float)(this.sizeWidth * this.sizeWidth + this.sizeHeight * this.sizeHeight + this.sizeDepth * this.sizeDepth)) / 2.0F;
		this.glRenderList = var7;
		this.posX = -999;
		this.setPosition(var3, var4, var5);
		this.needsUpdate = false;
	}

	public void setPosition(int var1, int var2, int var3) {
		if(var1 != this.posX || var2 != this.posY || var3 != this.posZ) {
			this.setDontDraw();
			this.posX = var1;
			this.posY = var2;
			this.posZ = var3;
			this.posXPlus = var1 + this.sizeWidth / 2;
			this.posYPlus = var2 + this.sizeHeight / 2;
			this.posZPlus = var3 + this.sizeDepth / 2;
			this.posXClip = var1 & 1023;
			this.posYClip = var2;
			this.posZClip = var3 & 1023;
			this.posXMinus = var1 - this.posXClip;
			this.posYMinus = var2 - this.posYClip;
			this.posZMinus = var3 - this.posZClip;
			//Spout Start
			float var4 = 6.0F;
			this.rendererBoundingBox = AxisAlignedBB.getBoundingBox((double)((float)var1 - var4), (double)((float)var2 - var4), (double)((float)var3 - var4), (double)((float)(var1 + this.sizeWidth) + var4), (double)((float)(var2 + this.sizeHeight) + var4), (double)((float)(var3 + this.sizeDepth) + var4));
			this.needsBoxUpdate = true;
			this.markDirty();
			this.isVisibleFromPosition = false;
			//Spout End
		}
	}

	private void setupGLTranslation() {
		GL11.glTranslatef((float)this.posXClip, (float)this.posYClip, (float)this.posZClip);
	}

	public void updateRenderer() {
		//Spout Start
		if(this.needsUpdate) {
			++chunksUpdated;
			if(this.needsBoxUpdate) {
				float var1 = 6.0F;
				GL11.glNewList(this.glRenderList + 3, 4864 /*GL_COMPILE*/); //Spout
				RenderItem.renderAABB(AxisAlignedBB.getBoundingBoxFromPool((double)((float)this.posXClip - var1), (double)((float)this.posYClip - var1), (double)((float)this.posZClip - var1), (double)((float)(this.posXClip + this.sizeWidth) + var1), (double)((float)(this.posYClip + this.sizeHeight) + var1), (double)((float)(this.posZClip + this.sizeDepth) + var1)));
				GL11.glEndList();
				this.needsBoxUpdate = false;
			}

			this.isVisible = true;
			this.isVisibleFromPosition = false;
			int x = this.posX;
			int y = this.posY;
			int z = this.posZ;
			int sizeXOffset = this.posX + this.sizeWidth;
			int sizeYOffset = this.posY + this.sizeHeight;
			int sizeZOffset = this.posZ + this.sizeDepth;

			for(int renderPass = 0; renderPass < 3; ++renderPass) { //Spout
				this.skipRenderPass[renderPass] = true;
			}

			Chunk.isLit = false;
			HashSet tileRenderers = new HashSet();
			tileRenderers.addAll(this.tileEntityRenderers);
			this.tileEntityRenderers.clear();
			ChunkCache chunkCache = new ChunkCache(this.worldObj, x - 1, y - 1, z - 1, sizeXOffset + 1, sizeYOffset + 1, sizeZOffset + 1);
			RenderBlocks blockRenderer = new RenderBlocks(chunkCache);
			
			List<String> hitTextures = new ArrayList<String>();
			List<String> hitTexturesPlugins = new ArrayList<String>();
			int currentTexture = 0;
			Minecraft game = SpoutClient.getHandle();

			hitTextures.add("/terrain.png");
			hitTexturesPlugins.add("");
			int defaultTexture = game.renderEngine.getTexture("/terrain.png");
			for (int renderPass = 0; renderPass < 3; ++renderPass) { //Spout
				boolean skipRenderPass = false;
				boolean rendered = false;
				boolean drawBlock = false;

				game.renderEngine.bindTexture(defaultTexture);
				for (currentTexture = 0; currentTexture < hitTextures.size(); currentTexture++) {
					boolean uvIgnore = false;
					int texture = defaultTexture;
					if(currentTexture > 0){
						Texture customTexture = CustomTextureManager.getTextureFromUrl(hitTexturesPlugins.get(currentTexture), hitTextures.get(currentTexture));
						if (customTexture != null) {
							texture = customTexture.getTextureID();
							game.renderEngine.bindTexture(texture);
							if(texture <= 0)
								texture = defaultTexture;
						}
					}
					
					//for (int dy = y; dy < sizeYOffset; ++dy) {
					for (int dy = sizeYOffset; dy > y; --dy) {
						for (int dz = z; dz < sizeZOffset; ++dz) {
							for (int dx = x; dx < sizeXOffset; ++dx) {
								int var19 = chunkCache.getBlockId(dx, dy, dz);
								
								if (var19 > 0) {
									String customTexture = null; 
									String customTexturePlugin = null;
									SpoutCustomBlockDesign design = null;
									if (SpoutItem.isBlockOverride(dx, dy, dz)) {
										design = SpoutItem.getCustomBlockDesign(dx, dy, dz);
									} else {
										int data = chunkCache.getBlockMetadata(dx, dy, dz);
										design = SpoutItem.getCustomBlockDesign(var19, data);
									}
									if (design != null) {
										customTexture = design.getTexureURL();
										customTexturePlugin = design.getTextureAddon();
									}
									if(customTexture != null){
										boolean found = false;
										for(int i=0;i<hitTextures.size();i++){
											if(hitTextures.get(i).equals(customTexture) && hitTexturesPlugins.get(i).equals(customTexturePlugin))
												found = true;
										}
										if(!found) {
											hitTextures.add(customTexture);
											hitTexturesPlugins.add(customTexturePlugin);
										}
										if(!hitTextures.get(currentTexture).equals(customTexture) || !hitTexturesPlugins.get(currentTexture).equals(customTexturePlugin))
											continue;
									} else if(currentTexture != 0) { 
										continue;
									}
								

									if (!drawBlock) {
										drawBlock = true;
										GL11.glNewList(this.glRenderList + renderPass, 4864 /* GL_COMPILE */);
										GL11.glPushMatrix();
										this.setupGLTranslation();
										GL11.glTranslatef((float)(-this.sizeDepth) / 2.0F, (float)(-this.sizeHeight) / 2.0F, (float)(-this.sizeDepth) / 2.0F);
										GL11.glScalef(1F, 1F, 1F);
										GL11.glTranslatef((float)this.sizeDepth / 2.0F, (float)this.sizeHeight / 2.0F, (float)this.sizeDepth / 2.0F);
										tessellator.startDrawingQuads();
										tessellator.setTranslationD((double)(-this.posX), (double)(-this.posY), (double)(-this.posZ));
									}
									
									if(tessellator.textureOverride != texture){
										tessellator.draw();
										tessellator.textureOverride = texture;
										tessellator.startDrawingQuads();
									}
									

									if (renderPass == 0 && Block.isBlockContainer[var19]) {
										TileEntity var20 = chunkCache.getBlockTileEntity(dx, dy, dz);
										if (TileEntityRenderer.instance.hasSpecialRenderer(var20)) {
											this.tileEntityRenderers.add(var20);
										}
									}

									Block var25 = Block.blocksList[var19];
									int blockRenderPass = var25.getRenderBlockPass();
									if (design != null) {
										if (renderPass == design.getRenderPass()) {
											rendered |= SpoutItem.renderCustomBlock(blockRenderer, design, var25, dx, dy, dz);
										} else {
											skipRenderPass = true;
										}
									} else if (blockRenderPass != renderPass) {
										skipRenderPass = true;
									} else {
										rendered |= blockRenderer.renderBlockByRenderType(var25, dx, dy, dz);
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
		}
	}

	public float distanceToEntitySquared(Entity var1) {
		float var2 = (float)(var1.posX - (double)this.posXPlus);
		float var3 = (float)(var1.posY - (double)this.posYPlus);
		float var4 = (float)(var1.posZ - (double)this.posZPlus);
		return var2 * var2 + var3 * var3 + var4 * var4;
	}

	public void setDontDraw() {
		for(int var1 = 0; var1 < 3; ++var1) { //Spout
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
		//Spout Start
		if(this.isInFrustum && ConfigReader.advancedOpenGL == 2) {
			this.isInFrustrumFully = var1.isBoundingBoxInFrustumFully(this.rendererBoundingBox);
		} else {
			this.isInFrustrumFully = false;
		}
		//Spout End
	}

	public void callOcclusionQueryList() {
		GL11.glCallList(this.glRenderList + 3); //Spout
	}

	public boolean skipAllRenderPasses() {
		return !this.isInitialized?false:this.skipRenderPass[0] && this.skipRenderPass[1] && this.skipRenderPass[2]; //Spout
	}

	public void markDirty() {
		this.needsUpdate = true;
	}

}
