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
import org.getspout.spout.io.CustomTextureManager;
import org.getspout.spout.item.SpoutCustomBlockDesign;
import org.getspout.spout.item.SpoutItemBlock;
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
			float var4 = 0.0F;
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
				float var1 = 0.0F;
				GL11.glNewList(this.glRenderList + 3, 4864 /*GL_COMPILE*/); //Spout
				RenderItem.renderAABB(AxisAlignedBB.getBoundingBoxFromPool((double)((float)this.posXClip - var1), (double)((float)this.posYClip - var1), (double)((float)this.posZClip - var1), (double)((float)(this.posXClip + this.sizeWidth) + var1), (double)((float)(this.posYClip + this.sizeHeight) + var1), (double)((float)(this.posZClip + this.sizeDepth) + var1)));
				GL11.glEndList();
				this.needsBoxUpdate = false;
			}

			this.isVisible = true;
			this.isVisibleFromPosition = false;
			int var22 = this.posX;
			int var2 = this.posY;
			int var3 = this.posZ;
			int var4 = this.posX + this.sizeWidth;
			int var5 = this.posY + this.sizeHeight;
			int var6 = this.posZ + this.sizeDepth;

			for(int var7 = 0; var7 < 3; ++var7) { //Spout
				this.skipRenderPass[var7] = true;
			}

			Chunk.isLit = false;
			HashSet var8 = new HashSet();
			var8.addAll(this.tileEntityRenderers);
			this.tileEntityRenderers.clear();
			byte var9 = 1;
			ChunkCache var10 = new ChunkCache(this.worldObj, var22 - var9, var2 - var9, var3 - var9, var4 + var9, var5 + var9, var6 + var9);
			RenderBlocks var11 = new RenderBlocks(var10);
			
			List<String> hitTextures = new ArrayList<String>();
			List<String> hitTexturesPlugins = new ArrayList<String>();
			int currentTexture = 0;
			Minecraft game = SpoutClient.getHandle();

			hitTextures.add("/terrain.png");
			hitTexturesPlugins.add("");
			int defaultTexture = game.renderEngine.getTexture("/terrain.png");
			for (int var12 = 0; var12 < 3; ++var12) { //Spout
				boolean var13 = false;
				boolean var14 = false;
				boolean var15 = false;

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
					
					for (int var16 = var2; var16 < var5; ++var16) {
						for (int var17 = var3; var17 < var6; ++var17) {
							for (int var18 = var22; var18 < var4; ++var18) {
								int var19 = var10.getBlockId(var18, var16, var17);
								String customTexture = null; 
								String customTexturePlugin = null;
								SpoutCustomBlockDesign design = null;
								if (SpoutItemBlock.isBlockOverride(var18, var16, var17)) {
									design = SpoutItemBlock.getCustomBlockDesign(var18, var16, var17);
								} else {
									int data = var10.getBlockMetadata(var18, var16, var17);
									design = SpoutItemBlock.getCustomBlockDesign(var19, data);
								}
								if (design != null) {
									customTexture = design.getTexureURL();
									customTexturePlugin = design.getTexturePlugin();
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
								if (var19 > 0) {

									if (!var15) {
										var15 = true;
										GL11.glNewList(this.glRenderList + var12, 4864 /* GL_COMPILE */);
										tessellator.isLoadingChunk = true;
										tessellator.startDrawingQuads();
									}
									
									if(tessellator.textureOverride != texture){
										tessellator.draw();
										tessellator.textureOverride = texture;
										tessellator.startDrawingQuads();
									}
									

									if (var12 == 0 && Block.isBlockContainer[var19]) {
										TileEntity var20 = var10.getBlockTileEntity(var18, var16, var17);
										if (TileEntityRenderer.instance.hasSpecialRenderer(var20)) {
											this.tileEntityRenderers.add(var20);
										}
									}

									Block var25 = Block.blocksList[var19];
									int var21 = var25.getRenderBlockPass();
									if (design != null) {
										if (var12 == design.getRenderPass()) {
											var14 |= SpoutItemBlock.renderCustomBlock(this, var11, design, var25, var18, var16, var17);
										} else {
											var13 = true;
										}
									} else if (var21 != var12) {
										var13 = true;
									} else {
										var14 |= var11.renderBlockByRenderType(var25, var18, var16, var17);
									}
								}
							}
						}
					}
				}

				if (var15) {
					tessellator.draw();
					tessellator.textureOverride = 0;
					GL11.glEndList();
					tessellator.isLoadingChunk = false;
					game.renderEngine.bindTexture(defaultTexture);
				} else {
					var14 = false;
				}

				if (var14) {
					this.skipRenderPass[var12] = false;
				}

				if (!var13) {
					break;
				}
			}

			HashSet var24 = new HashSet();
			var24.addAll(this.tileEntityRenderers);
			var24.removeAll(var8);
			this.tileEntities.addAll(var24);
			var8.removeAll(this.tileEntityRenderers);
			//Spout End
			this.tileEntities.removeAll(var8);
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
		if(this.isInFrustum && Config.isOcclusionEnabled() && Config.isOcclusionFancy()) {
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
