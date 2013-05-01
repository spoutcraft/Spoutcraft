package net.minecraft.src;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.ARBOcclusionQuery;
import org.lwjgl.opengl.GL11;
// MCPatcher Start
import com.prupe.mcpatcher.mod.ColorizeWorld;
import com.prupe.mcpatcher.mod.RenderPass;
import com.prupe.mcpatcher.mod.SkyRenderer;
// MCPatcher End
// Spout Start
import org.spoutcraft.api.gui.Color;
import org.spoutcraft.client.HDImageBufferDownload;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.TileEntityComparator;
import org.spoutcraft.client.config.Configuration;
import org.spoutcraft.client.io.CustomTextureManager;
import org.spoutcraft.client.spoutworth.SpoutWorth;
// Spout End

public class RenderGlobal implements IWorldAccess {
	public List tileEntities = new ArrayList();
	private WorldClient theWorld;

	/** The RenderEngine instance used by RenderGlobal */
	private final RenderEngine renderEngine;
	private List worldRenderersToUpdate = new ArrayList();
	private WorldRenderer[] sortedWorldRenderers;
	// Spout Start - private to public
	public WorldRenderer[] worldRenderers;
	// Spout End
	private int renderChunksWide;
	private int renderChunksTall;
	private int renderChunksDeep;

	/** OpenGL render lists base */
	private int glRenderListBase;

	/** A reference to the Minecraft object. */
	private Minecraft mc;

	/** Global render blocks */
	private RenderBlocks globalRenderBlocks;

	/** OpenGL occlusion query base */
	private IntBuffer glOcclusionQueryBase;

	/** Is occlusion testing enabled */
	private boolean occlusionEnabled = false;

	/**
	 * counts the cloud render updates. Used with mod to stagger some updates
	 */
	private int cloudTickCounter = 0;

	/** The star GL Call list */
	private int starGLCallList;

	/** OpenGL sky list */
	private int glSkyList;

	/** OpenGL sky list 2 */
	private int glSkyList2;

	/** Minimum block X */
	private int minBlockX;

	/** Minimum block Y */
	private int minBlockY;

	/** Minimum block Z */
	private int minBlockZ;

	/** Maximum block X */
	private int maxBlockX;

	/** Maximum block Y */
	private int maxBlockY;

	/** Maximum block Z */
	private int maxBlockZ;

	/**
	 * Stores blocks currently being broken. Key is entity ID of the thing doing the breaking. Value is a
	 * DestroyBlockProgress
	 */
	private Map damagedBlocks = new HashMap();
	private Icon[] destroyBlockIcons;
	private int renderDistance = -1;

	/** Render entities startup counter (init value=2) */
	private int renderEntitiesStartupCounter = 2;

	/** Count entities total */
	private int countEntitiesTotal;

	/** Count entities rendered */
	private int countEntitiesRendered;

	/** Count entities hidden */
	private int countEntitiesHidden;

	/** Dummy buffer (50k) not used */
	int[] dummyBuf50k = new int[50000];

	/** Occlusion query result */
	IntBuffer occlusionResult = GLAllocation.createDirectIntBuffer(64);

	/** How many renderers are loaded this frame that try to be rendered */
	private int renderersLoaded;

	/** How many renderers are being clipped by the frustrum this frame */
	private int renderersBeingClipped;

	/** How many renderers are being occluded this frame */
	private int renderersBeingOccluded;

	/** How many renderers are actually being rendered this frame */
	private int renderersBeingRendered;

	/**
	 * How many renderers are skipping rendering due to not having a render pass this frame
	 */
	private int renderersSkippingRenderPass;

	/** Dummy render int */
	private int dummyRenderInt;

	/** World renderers check index */
	private int worldRenderersCheckIndex;

	/** List of OpenGL lists for the current render pass */
	private List glRenderLists = new ArrayList();

	/** All render lists (fixed length 4) */
	private RenderList[] allRenderLists = new RenderList[] {new RenderList(), new RenderList(), new RenderList(), new RenderList()};

	/**
	 * Previous x position when the renderers were sorted. (Once the distance moves more than 4 units they will be
	 * resorted)
	 */
	double prevSortX = -9999.0D;

	/**
	 * Previous y position when the renderers were sorted. (Once the distance moves more than 4 units they will be
	 * resorted)
	 */
	double prevSortY = -9999.0D;

	/**
	 * Previous Z position when the renderers were sorted. (Once the distance moves more than 4 units they will be
	 * resorted)
	 */
	double prevSortZ = -9999.0D;

	/**
	 * The offset used to determine if a renderer is one of the sixteenth that are being updated this frame
	 */
	int frustumCheckOffset = 0;

	// Spout Start
	private long lastMovedTime = System.currentTimeMillis();
	private long frameCount = 0;
	public static int renderersToUpdateLastTick = 0;
	// Spout End

	public RenderGlobal(Minecraft par1Minecraft, RenderEngine par2RenderEngine) {
		this.mc = par1Minecraft;
		this.renderEngine = par2RenderEngine;
		// Spout Start
		byte var3 = 64;
		byte var4 = 64;
		this.glRenderListBase = GLAllocation.generateDisplayLists(var3 * var3 * var4 * 5);
		// Spout End
		this.occlusionEnabled = OpenGlCapsChecker.checkARBOcclusion();
		// Spout Start
		if (this.occlusionEnabled && Configuration.ambientOcclusion) {
		// Spout End
			this.occlusionResult.clear();
			this.glOcclusionQueryBase = GLAllocation.createDirectIntBuffer(var3 * var3 * var4);
			this.glOcclusionQueryBase.clear();
			this.glOcclusionQueryBase.position(0);
			this.glOcclusionQueryBase.limit(var3 * var3 * var4);
			ARBOcclusionQuery.glGenQueriesARB(this.glOcclusionQueryBase);
		}

		// Spout Start
		refreshStars();
		// Spout End

		this.starGLCallList = GLAllocation.generateDisplayLists(3);
		GL11.glPushMatrix();
		GL11.glNewList(this.starGLCallList, GL11.GL_COMPILE);
		this.renderStars();
		GL11.glEndList();
		GL11.glPopMatrix();
		Tessellator var5 = Tessellator.instance;
		this.glSkyList = this.starGLCallList + 1;
		GL11.glNewList(this.glSkyList, GL11.GL_COMPILE);
		byte var7 = 64;
		int var8 = 256 / var7 + 2;
		float var6 = 16.0F;
		int var9;
		int var10;

		for (var9 = -var7 * var8; var9 <= var7 * var8; var9 += var7) {
			for (var10 = -var7 * var8; var10 <= var7 * var8; var10 += var7) {
				var5.startDrawingQuads();
				var5.addVertex((double)(var9 + 0), (double)var6, (double)(var10 + 0));
				var5.addVertex((double)(var9 + var7), (double)var6, (double)(var10 + 0));
				var5.addVertex((double)(var9 + var7), (double)var6, (double)(var10 + var7));
				var5.addVertex((double)(var9 + 0), (double)var6, (double)(var10 + var7));
				var5.draw();
			}
		}

		GL11.glEndList();
		this.glSkyList2 = this.starGLCallList + 2;
		GL11.glNewList(this.glSkyList2, GL11.GL_COMPILE);
		var6 = -16.0F;
		var5.startDrawingQuads();

		for (var9 = -var7 * var8; var9 <= var7 * var8; var9 += var7) {
			for (var10 = -var7 * var8; var10 <= var7 * var8; var10 += var7) {
				var5.addVertex((double)(var9 + var7), (double)var6, (double)(var10 + 0));
				var5.addVertex((double)(var9 + 0), (double)var6, (double)(var10 + 0));
				var5.addVertex((double)(var9 + 0), (double)var6, (double)(var10 + var7));
				var5.addVertex((double)(var9 + var7), (double)var6, (double)(var10 + var7));
			}
		}

		var5.draw();
		GL11.glEndList();
	}

	// Spout Start
	public void refreshStars() {
		this.starGLCallList = GLAllocation.generateDisplayLists(3);
		GL11.glPushMatrix();
		GL11.glNewList(this.starGLCallList, GL11.GL_COMPILE);
		this.renderStars();
		GL11.glEndList();
		GL11.glPopMatrix();
		Tessellator var5 = Tessellator.instance;
		this.glSkyList = this.starGLCallList + 1;
		GL11.glNewList(this.glSkyList, GL11.GL_COMPILE);
		byte var7 = 64;
		int var8 = 256 / var7 + 2;
		float var6 = 16.0F;
		int var9;
		int var10;

		for (var9 = -var7 * var8; var9 <= var7 * var8; var9 += var7) {
			for (var10 = -var7 * var8; var10 <= var7 * var8; var10 += var7) {
				var5.startDrawingQuads();
				var5.addVertex((double)(var9 + 0), (double)var6, (double)(var10 + 0));
				var5.addVertex((double)(var9 + var7), (double)var6, (double)(var10 + 0));
				var5.addVertex((double)(var9 + var7), (double)var6, (double)(var10 + var7));
				var5.addVertex((double)(var9 + 0), (double)var6, (double)(var10 + var7));
				var5.draw();
			}
		}

		GL11.glEndList();
		this.glSkyList2 = this.starGLCallList + 2;
		GL11.glNewList(this.glSkyList2, GL11.GL_COMPILE);
		var6 = -16.0F;
		var5.startDrawingQuads();

		for (var9 = -var7 * var8; var9 <= var7 * var8; var9 += var7) {
			for (var10 = -var7 * var8; var10 <= var7 * var8; var10 += var7) {
				var5.addVertex((double)(var9 + var7), (double)var6, (double)(var10 + 0));
				var5.addVertex((double)(var9 + 0), (double)var6, (double)(var10 + 0));
				var5.addVertex((double)(var9 + 0), (double)var6, (double)(var10 + var7));
				var5.addVertex((double)(var9 + var7), (double)var6, (double)(var10 + var7));
			}
		}

		var5.draw();
		GL11.glEndList();
	}
	// Spout End
	private void renderStars() {
		// Spout Start
		if (!SpoutClient.getInstance().getSkyManager().isStarsVisible()) {
			return;
		}
		// Spout End
		Random var1 = new Random(10842L);
		Tessellator var2 = Tessellator.instance;
		var2.startDrawingQuads();

		// Spout Start
		for (int var3 = 0; var3 < SpoutClient.getInstance().getSkyManager().getStarFrequency(); ++var3) {
		// Spout End
			double var4 = (double)(var1.nextFloat() * 2.0F - 1.0F);
			double var6 = (double)(var1.nextFloat() * 2.0F - 1.0F);
			double var8 = (double)(var1.nextFloat() * 2.0F - 1.0F);
			double var10 = (double)(0.15F + var1.nextFloat() * 0.1F);
			double var12 = var4 * var4 + var6 * var6 + var8 * var8;

			if (var12 < 1.0D && var12 > 0.01D) {
				var12 = 1.0D / Math.sqrt(var12);
				var4 *= var12;
				var6 *= var12;
				var8 *= var12;
				double var14 = var4 * 100.0D;
				double var16 = var6 * 100.0D;
				double var18 = var8 * 100.0D;
				double var20 = Math.atan2(var4, var8);
				double var22 = Math.sin(var20);
				double var24 = Math.cos(var20);
				double var26 = Math.atan2(Math.sqrt(var4 * var4 + var8 * var8), var6);
				double var28 = Math.sin(var26);
				double var30 = Math.cos(var26);
				double var32 = var1.nextDouble() * Math.PI * 2.0D;
				double var34 = Math.sin(var32);
				double var36 = Math.cos(var32);

				for (int var38 = 0; var38 < 4; ++var38) {
					double var39 = 0.0D;
					double var41 = (double)((var38 & 2) - 1) * var10;
					double var43 = (double)((var38 + 1 & 2) - 1) * var10;
					double var47 = var41 * var36 - var43 * var34;
					double var49 = var43 * var36 + var41 * var34;
					double var53 = var47 * var28 + var39 * var30;
					double var55 = var39 * var28 - var47 * var30;
					double var57 = var55 * var22 - var49 * var24;
					double var61 = var49 * var22 + var55 * var24;
					var2.addVertex(var14 + var57, var16 + var53, var18 + var61);
				}
			}
		}

		var2.draw();
	}

	/**
	 * set null to clear
	 */
	public void setWorldAndLoadRenderers(WorldClient par1WorldClient) {
		if (this.theWorld != null) {
			this.theWorld.removeWorldAccess(this);
		}

		this.prevSortX = -9999.0D;
		this.prevSortY = -9999.0D;
		this.prevSortZ = -9999.0D;
		RenderManager.instance.set(par1WorldClient);
		this.theWorld = par1WorldClient;
		this.globalRenderBlocks = new RenderBlocks(par1WorldClient);

		if (par1WorldClient != null) {
			par1WorldClient.addWorldAccess(this);
			this.loadRenderers();
		}
	}

	/**
	 * Loads all the renderers and sets up the basic settings usage
	 */
	public void loadRenderers() {
		if (this.theWorld != null) {
			// Spout Start
			Block.leaves.setGraphicsLevel(Configuration.isFancyTrees());
			// Spout End
			this.renderDistance = this.mc.gameSettings.renderDistance;
			int var1;

			if (this.worldRenderers != null) {
				for (var1 = 0; var1 < this.worldRenderers.length; ++var1) {
					this.worldRenderers[var1].stopRendering();
				}
			}

			var1 = 64 << 3 - this.renderDistance;

			// Spout Start
			if (Configuration.isFarView()) {
				var1 = 512;
			} else if (var1 > 400) {
			// Spout End
				var1 = 400;
			}

			this.renderChunksWide = var1 / 16 + 1;
			this.renderChunksTall = 16;
			this.renderChunksDeep = var1 / 16 + 1;
			this.worldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
			this.sortedWorldRenderers = new WorldRenderer[this.renderChunksWide * this.renderChunksTall * this.renderChunksDeep];
			int var2 = 0;
			int var3 = 0;
			this.minBlockX = 0;
			this.minBlockY = 0;
			this.minBlockZ = 0;
			this.maxBlockX = this.renderChunksWide;
			this.maxBlockY = this.renderChunksTall;
			this.maxBlockZ = this.renderChunksDeep;
			int var4;

			for (var4 = 0; var4 < this.worldRenderersToUpdate.size(); ++var4) {
				((WorldRenderer)this.worldRenderersToUpdate.get(var4)).needsUpdate = false;
			}

			this.worldRenderersToUpdate.clear();
			this.tileEntities.clear();

			for (var4 = 0; var4 < this.renderChunksWide; ++var4) {
				for (int var5 = 0; var5 < this.renderChunksTall; ++var5) {
					for (int var6 = 0; var6 < this.renderChunksDeep; ++var6) {
						this.worldRenderers[(var6 * this.renderChunksTall + var5) * this.renderChunksWide + var4] = new WorldRenderer(this.theWorld, this.tileEntities, var4 * 16, var5 * 16, var6 * 16, this.glRenderListBase + var2);

						// Spout Start
						if (this.occlusionEnabled && Configuration.ambientOcclusion && this.glOcclusionQueryBase != null) {
							this.worldRenderers[(var6 * this.renderChunksTall + var5) * this.renderChunksWide + var4].glOcclusionQuery = this.glOcclusionQueryBase.get(var3);
						}
						// Spout End
						this.worldRenderers[(var6 * this.renderChunksTall + var5) * this.renderChunksWide + var4].isWaitingOnOcclusionQuery = false;
						this.worldRenderers[(var6 * this.renderChunksTall + var5) * this.renderChunksWide + var4].isVisible = true;
						this.worldRenderers[(var6 * this.renderChunksTall + var5) * this.renderChunksWide + var4].isInFrustum = true;
						this.worldRenderers[(var6 * this.renderChunksTall + var5) * this.renderChunksWide + var4].chunkIndex = var3++;
						this.worldRenderers[(var6 * this.renderChunksTall + var5) * this.renderChunksWide + var4].markDirty();
						this.sortedWorldRenderers[(var6 * this.renderChunksTall + var5) * this.renderChunksWide + var4] = this.worldRenderers[(var6 * this.renderChunksTall + var5) * this.renderChunksWide + var4];
						this.worldRenderersToUpdate.add(this.worldRenderers[(var6 * this.renderChunksTall + var5) * this.renderChunksWide + var4]);
						// Spout Start - 3 to 5
						var2 += 5;
						// Spout End
					}
				}
			}

			if (this.theWorld != null) {
				EntityLiving var7 = this.mc.renderViewEntity;

				if (var7 != null) {
					this.markRenderersForNewPosition(MathHelper.floor_double(var7.posX), MathHelper.floor_double(var7.posY), MathHelper.floor_double(var7.posZ));
					Arrays.sort(this.sortedWorldRenderers, new EntitySorter(var7));
				}
			}

			this.renderEntitiesStartupCounter = 2;
		}
	}

	/**
	 * Renders all entities within range and within the frustrum. Args: pos, frustrum, partialTickTime
	 */
	public void renderEntities(Vec3 par1Vec3, ICamera par2ICamera, float par3) {
		if (this.renderEntitiesStartupCounter > 0) {
			--this.renderEntitiesStartupCounter;
		} else {
			this.theWorld.theProfiler.startSection("prepare");
			TileEntityRenderer.instance.cacheActiveRenderInfo(this.theWorld, this.renderEngine, this.mc.fontRenderer, this.mc.renderViewEntity, par3);
			RenderManager.instance.cacheActiveRenderInfo(this.theWorld, this.renderEngine, this.mc.fontRenderer, this.mc.renderViewEntity, this.mc.pointedEntityLiving, this.mc.gameSettings, par3);
			this.countEntitiesTotal = 0;
			this.countEntitiesRendered = 0;
			this.countEntitiesHidden = 0;
			EntityLiving var4 = this.mc.renderViewEntity;
			RenderManager.renderPosX = var4.lastTickPosX + (var4.posX - var4.lastTickPosX) * (double)par3;
			RenderManager.renderPosY = var4.lastTickPosY + (var4.posY - var4.lastTickPosY) * (double)par3;
			RenderManager.renderPosZ = var4.lastTickPosZ + (var4.posZ - var4.lastTickPosZ) * (double)par3;
			TileEntityRenderer.staticPlayerX = var4.lastTickPosX + (var4.posX - var4.lastTickPosX) * (double)par3;
			TileEntityRenderer.staticPlayerY = var4.lastTickPosY + (var4.posY - var4.lastTickPosY) * (double)par3;
			TileEntityRenderer.staticPlayerZ = var4.lastTickPosZ + (var4.posZ - var4.lastTickPosZ) * (double)par3;
			this.mc.entityRenderer.enableLightmap((double)par3);
			this.theWorld.theProfiler.endStartSection("global");
			List var5 = this.theWorld.getLoadedEntityList();
			this.countEntitiesTotal = var5.size();
			int var6;
			Entity var7;

			for (var6 = 0; var6 < this.theWorld.weatherEffects.size(); ++var6) {
				var7 = (Entity)this.theWorld.weatherEffects.get(var6);
				++this.countEntitiesRendered;

				if (var7.isInRangeToRenderVec3D(par1Vec3)) {
					RenderManager.instance.renderEntity(var7, par3);
				}
			}

			this.theWorld.theProfiler.endStartSection("entities");

			for (var6 = 0; var6 < var5.size(); ++var6) {
				var7 = (Entity)var5.get(var6);

				if (var7.isInRangeToRenderVec3D(par1Vec3) && (var7.ignoreFrustumCheck || par2ICamera.isBoundingBoxInFrustum(var7.boundingBox) || var7.riddenByEntity == this.mc.thePlayer) && (var7 != this.mc.renderViewEntity || this.mc.gameSettings.thirdPersonView != 0 || this.mc.renderViewEntity.isPlayerSleeping()) && this.theWorld.blockExists(MathHelper.floor_double(var7.posX), 0, MathHelper.floor_double(var7.posZ))) {
					++this.countEntitiesRendered;
					RenderManager.instance.renderEntity(var7, par3);
				}
			}

			this.theWorld.theProfiler.endStartSection("tileentities");
			RenderHelper.enableStandardItemLighting();

			// Spout Start
			int max = tileEntities.size();
			int threshold = (int) Math.min(1000, (Math.max(100, (SpoutWorth.getInstance().getAverageFPS() * 8))));
			if (tileEntities.size() > threshold) {
				Collections.sort((List<TileEntity>)tileEntities, new TileEntityComparator());
				max = threshold / 2;
			}
			Iterator var8 = this.tileEntities.iterator();

			int i = 0;
			while (var8.hasNext() && (i < max)) {
				i++;
				TileEntity var9 = (TileEntity)var8.next();
				if (!var9.isInvalid()) {
					TileEntityRenderer.instance.renderTileEntity(var9, par3);
				}
			}
			// Spout End

			for (var6 = 0; var6 < this.tileEntities.size(); ++var6) {
				TileEntityRenderer.instance.renderTileEntity((TileEntity)this.tileEntities.get(var6), par3);
			}

			this.mc.entityRenderer.disableLightmap((double)par3);
			this.theWorld.theProfiler.endSection();
		}
	}

	/**
	 * Gets the render info for use on the Debug screen
	 */
	public String getDebugInfoRenders() {
		return "C: " + this.renderersBeingRendered + "/" + this.renderersLoaded + ". F: " + this.renderersBeingClipped + ", O: " + this.renderersBeingOccluded + ", E: " + this.renderersSkippingRenderPass;
	}

	/**
	 * Gets the entities info for use on the Debug screen
	 */
	public String getDebugInfoEntities() {
		return "E: " + this.countEntitiesRendered + "/" + this.countEntitiesTotal + ". B: " + this.countEntitiesHidden + ", I: " + (this.countEntitiesTotal - this.countEntitiesHidden - this.countEntitiesRendered);
	}

	/**
	 * Goes through all the renderers setting new positions on them and those that have their position changed are adding
	 * to be updated
	 */
	private void markRenderersForNewPosition(int par1, int par2, int par3) {
		par1 -= 8;
		par2 -= 8;
		par3 -= 8;
		this.minBlockX = Integer.MAX_VALUE;
		this.minBlockY = Integer.MAX_VALUE;
		this.minBlockZ = Integer.MAX_VALUE;
		this.maxBlockX = Integer.MIN_VALUE;
		this.maxBlockY = Integer.MIN_VALUE;
		this.maxBlockZ = Integer.MIN_VALUE;
		int var4 = this.renderChunksWide * 16;
		int var5 = var4 / 2;

		for (int var6 = 0; var6 < this.renderChunksWide; ++var6) {
			int var7 = var6 * 16;
			int var8 = var7 + var5 - par1;

			if (var8 < 0) {
				var8 -= var4 - 1;
			}

			var8 /= var4;
			var7 -= var8 * var4;

			if (var7 < this.minBlockX) {
				this.minBlockX = var7;
			}

			if (var7 > this.maxBlockX) {
				this.maxBlockX = var7;
			}

			for (int var9 = 0; var9 < this.renderChunksDeep; ++var9) {
				int var10 = var9 * 16;
				int var11 = var10 + var5 - par3;

				if (var11 < 0) {
					var11 -= var4 - 1;
				}

				var11 /= var4;
				var10 -= var11 * var4;

				if (var10 < this.minBlockZ) {
					this.minBlockZ = var10;
				}

				if (var10 > this.maxBlockZ) {
					this.maxBlockZ = var10;
				}

				for (int var12 = 0; var12 < this.renderChunksTall; ++var12) {
					int var13 = var12 * 16;

					if (var13 < this.minBlockY) {
						this.minBlockY = var13;
					}

					if (var13 > this.maxBlockY) {
						this.maxBlockY = var13;
					}

					WorldRenderer var14 = this.worldRenderers[(var9 * this.renderChunksTall + var12) * this.renderChunksWide + var6];
					boolean var15 = var14.needsUpdate;
					var14.setPosition(var7, var13, var10);

					if (!var15 && var14.needsUpdate) {
						this.worldRenderersToUpdate.add(var14);
					}
				}
			}
		}
	}

	/**
	 * Sorts all renderers based on the passed in entity. Args: entityLiving, renderPass, partialTickTime
	 */
	public int sortAndRender(EntityLiving par1EntityLiving, int par2, double par3) {
		this.theWorld.theProfiler.startSection("sortchunks");

		for (int var5 = 0; var5 < 10; ++var5) {
			this.worldRenderersCheckIndex = (this.worldRenderersCheckIndex + 1) % this.worldRenderers.length;
			WorldRenderer var6 = this.worldRenderers[this.worldRenderersCheckIndex];

			if (var6.needsUpdate && !this.worldRenderersToUpdate.contains(var6)) {
				this.worldRenderersToUpdate.add(var6);
			}
		}

		if (this.mc.gameSettings.renderDistance != this.renderDistance) {
			this.loadRenderers();
		}

		if (par2 == 0) {
			this.renderersLoaded = 0;
			this.dummyRenderInt = 0;
			this.renderersBeingClipped = 0;
			this.renderersBeingOccluded = 0;
			this.renderersBeingRendered = 0;
			this.renderersSkippingRenderPass = 0;
		}

		double var33 = par1EntityLiving.lastTickPosX + (par1EntityLiving.posX - par1EntityLiving.lastTickPosX) * par3;
		double var7 = par1EntityLiving.lastTickPosY + (par1EntityLiving.posY - par1EntityLiving.lastTickPosY) * par3;
		double var9 = par1EntityLiving.lastTickPosZ + (par1EntityLiving.posZ - par1EntityLiving.lastTickPosZ) * par3;
		double var11 = par1EntityLiving.posX - this.prevSortX;
		double var13 = par1EntityLiving.posY - this.prevSortY;
		double var15 = par1EntityLiving.posZ - this.prevSortZ;

		if (var11 * var11 + var13 * var13 + var15 * var15 > 16.0D) {
			this.prevSortX = par1EntityLiving.posX;
			this.prevSortY = par1EntityLiving.posY;
			this.prevSortZ = par1EntityLiving.posZ;
			this.markRenderersForNewPosition(MathHelper.floor_double(par1EntityLiving.posX), MathHelper.floor_double(par1EntityLiving.posY), MathHelper.floor_double(par1EntityLiving.posZ));
			Arrays.sort(this.sortedWorldRenderers, new EntitySorter(par1EntityLiving));
		}

		RenderHelper.disableStandardItemLighting();
		byte var17 = 0;
		int var34;

		// Spout Start
		if (this.occlusionEnabled && Configuration.ambientOcclusion && this.mc.gameSettings.advancedOpengl && !this.mc.gameSettings.anaglyph && par2 == 0) {
		// Spout End
			byte var18 = 0;
			int var19 = 16;
			this.checkOcclusionQueryResult(var18, var19);

			for (int var20 = var18; var20 < var19; ++var20) {
				this.sortedWorldRenderers[var20].isVisible = true;
			}

			this.theWorld.theProfiler.endStartSection("render");
			var34 = var17 + this.renderSortedRenderers(var18, var19, par2, par3);

			do {
				this.theWorld.theProfiler.endStartSection("occ");
				int var35 = var19;
				var19 *= 2;

				if (var19 > this.sortedWorldRenderers.length) {
					var19 = this.sortedWorldRenderers.length;
				}

				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glDisable(GL11.GL_FOG);
				GL11.glColorMask(false, false, false, false);
				GL11.glDepthMask(false);
				this.theWorld.theProfiler.startSection("check");
				this.checkOcclusionQueryResult(var35, var19);
				this.theWorld.theProfiler.endSection();
				GL11.glPushMatrix();
				float var36 = 0.0F;
				float var21 = 0.0F;
				float var22 = 0.0F;

				for (int var23 = var35; var23 < var19; ++var23) {
					if (this.sortedWorldRenderers[var23].skipAllRenderPasses()) {
						this.sortedWorldRenderers[var23].isInFrustum = false;
					} else {
						if (!this.sortedWorldRenderers[var23].isInFrustum) {
							this.sortedWorldRenderers[var23].isVisible = true;
						}

						if (this.sortedWorldRenderers[var23].isInFrustum && !this.sortedWorldRenderers[var23].isWaitingOnOcclusionQuery) {
							float var24 = MathHelper.sqrt_float(this.sortedWorldRenderers[var23].distanceToEntitySquared(par1EntityLiving));
							int var25 = (int)(1.0F + var24 / 128.0F);

							if (this.cloudTickCounter % var25 == var23 % var25) {
								WorldRenderer var26 = this.sortedWorldRenderers[var23];
								float var27 = (float)((double)var26.posXMinus - var33);
								float var28 = (float)((double)var26.posYMinus - var7);
								float var29 = (float)((double)var26.posZMinus - var9);
								float var30 = var27 - var36;
								float var31 = var28 - var21;
								float var32 = var29 - var22;

								if (var30 != 0.0F || var31 != 0.0F || var32 != 0.0F) {
									GL11.glTranslatef(var30, var31, var32);
									var36 += var30;
									var21 += var31;
									var22 += var32;
								}

								this.theWorld.theProfiler.startSection("bb");
								ARBOcclusionQuery.glBeginQueryARB(ARBOcclusionQuery.GL_SAMPLES_PASSED_ARB, this.sortedWorldRenderers[var23].glOcclusionQuery);
								this.sortedWorldRenderers[var23].callOcclusionQueryList();
								ARBOcclusionQuery.glEndQueryARB(ARBOcclusionQuery.GL_SAMPLES_PASSED_ARB);
								this.theWorld.theProfiler.endSection();
								this.sortedWorldRenderers[var23].isWaitingOnOcclusionQuery = true;
							}
						}
					}
				}

				GL11.glPopMatrix();

				if (this.mc.gameSettings.anaglyph) {
					if (EntityRenderer.anaglyphField == 0) {
						GL11.glColorMask(false, true, true, true);
					} else {
						GL11.glColorMask(true, false, false, true);
					}
				} else {
					GL11.glColorMask(true, true, true, true);
				}

				GL11.glDepthMask(true);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_FOG);
				this.theWorld.theProfiler.endStartSection("render");
				var34 += this.renderSortedRenderers(var35, var19, par2, par3);
			} while (var19 < this.sortedWorldRenderers.length);
		} else {
			this.theWorld.theProfiler.endStartSection("render");
			var34 = var17 + this.renderSortedRenderers(0, this.sortedWorldRenderers.length, par2, par3);
		}

		this.theWorld.theProfiler.endSection();
		return var34;
	}

	private void checkOcclusionQueryResult(int par1, int par2) {
		for (int var3 = par1; var3 < par2; ++var3) {
			if (this.sortedWorldRenderers[var3].isWaitingOnOcclusionQuery) {
				this.occlusionResult.clear();
				ARBOcclusionQuery.glGetQueryObjectuARB(this.sortedWorldRenderers[var3].glOcclusionQuery, ARBOcclusionQuery.GL_QUERY_RESULT_AVAILABLE_ARB, this.occlusionResult);

				if (this.occlusionResult.get(0) != 0) {
					this.sortedWorldRenderers[var3].isWaitingOnOcclusionQuery = false;
					this.occlusionResult.clear();
					ARBOcclusionQuery.glGetQueryObjectuARB(this.sortedWorldRenderers[var3].glOcclusionQuery, ARBOcclusionQuery.GL_QUERY_RESULT_ARB, this.occlusionResult);
					this.sortedWorldRenderers[var3].isVisible = this.occlusionResult.get(0) != 0;
				}
			}
		}
	}

	/**
	 * Renders the sorted renders for the specified render pass. Args: startRenderer, numRenderers, renderPass,
	 * partialTickTime
	 */
	private int renderSortedRenderers(int par1, int par2, int par3, double par4) {
		this.glRenderLists.clear();
		int var6 = 0;

		for (int var7 = par1; var7 < par2; ++var7) {
			if (par3 == 0) {
				++this.renderersLoaded;

				if (this.sortedWorldRenderers[var7].skipRenderPass[par3]) {
					++this.renderersSkippingRenderPass;
				} else if (!this.sortedWorldRenderers[var7].isInFrustum) {
					++this.renderersBeingClipped;
				// Spout Start
				} else if (this.occlusionEnabled && Configuration.ambientOcclusion && !this.sortedWorldRenderers[var7].isVisible) {
				// Spout End
					++this.renderersBeingOccluded;
				} else {
					++this.renderersBeingRendered;
				}
			}

			// Spout Start
			if (!this.sortedWorldRenderers[var7].skipRenderPass[par3] && this.sortedWorldRenderers[var7].isInFrustum && (!this.occlusionEnabled && !Configuration.ambientOcclusion || this.sortedWorldRenderers[var7].isVisible)) {
			// Spout End
				int var8 = this.sortedWorldRenderers[var7].getGLCallListForPass(par3);

				if (var8 >= 0) {
					this.glRenderLists.add(this.sortedWorldRenderers[var7]);
					++var6;
				}
			}
		}

		EntityLiving var19 = this.mc.renderViewEntity;
		double var20 = var19.lastTickPosX + (var19.posX - var19.lastTickPosX) * par4;
		double var10 = var19.lastTickPosY + (var19.posY - var19.lastTickPosY) * par4;
		double var12 = var19.lastTickPosZ + (var19.posZ - var19.lastTickPosZ) * par4;
		int var14 = 0;
		int var15;

		for (var15 = 0; var15 < this.allRenderLists.length; ++var15) {
			this.allRenderLists[var15].func_78421_b();
		}

		for (var15 = 0; var15 < this.glRenderLists.size(); ++var15) {
			WorldRenderer var16 = (WorldRenderer)this.glRenderLists.get(var15);
			int var17 = -1;

			for (int var18 = 0; var18 < var14; ++var18) {
				if (this.allRenderLists[var18].func_78418_a(var16.posXMinus, var16.posYMinus, var16.posZMinus)) {
					var17 = var18;
				}
			}

			if (var17 < 0) {
				var17 = var14++;
				this.allRenderLists[var17].func_78422_a(var16.posXMinus, var16.posYMinus, var16.posZMinus, var20, var10, var12);
			}

			this.allRenderLists[var17].func_78420_a(var16.getGLCallListForPass(par3));
		}

		this.renderAllRenderLists(par3, par4);
		return var6;
	}

	/**
	 * Render all render lists
	 */
	public void renderAllRenderLists(int par1, double par2) {
		// Spout Start
		RenderPass.enableDisableLightmap(this.mc.entityRenderer, par2, par1);
		// Spout End

		for (int var4 = 0; var4 < this.allRenderLists.length; ++var4) {
			this.allRenderLists[var4].func_78419_a();
		}

		this.mc.entityRenderer.disableLightmap(par2);
	}

	public void updateClouds() {
		++this.cloudTickCounter;

		if (this.cloudTickCounter % 20 == 0) {
			Iterator var1 = this.damagedBlocks.values().iterator();

			while (var1.hasNext()) {
				DestroyBlockProgress var2 = (DestroyBlockProgress)var1.next();
				int var3 = var2.getCreationCloudUpdateTick();

				if (this.cloudTickCounter - var3 > 400) {
					var1.remove();
				}
			}
		}
	}

	/**
	 * Renders the sky with the partial tick time. Args: partialTickTime
	 */
	public void renderSky(float par1) {
		// Spout Start
		SkyRenderer.setup(this.theWorld, this.renderEngine, par1, this.theWorld.getCelestialAngle(par1));
		// Spout End

		if (this.mc.theWorld.provider.dimensionId == 1) {
			GL11.glDisable(GL11.GL_FOG);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			RenderHelper.disableStandardItemLighting();
			GL11.glDepthMask(false);
			this.renderEngine.bindTexture("/misc/tunnel.png");
			Tessellator var21 = Tessellator.instance;

			for (int var22 = 0; var22 < 6; ++var22) {
				GL11.glPushMatrix();

				if (var22 == 1) {
					GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
				}

				if (var22 == 2) {
					GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				}

				if (var22 == 3) {
					GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
				}

				if (var22 == 4) {
					GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
				}

				if (var22 == 5) {
					GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
				}

				var21.startDrawingQuads();
				// MCPatcher Start
				var21.setColorOpaque_I(ColorizeWorld.endSkyColor);
				// MCPatcher End
				var21.addVertexWithUV(-100.0D, -100.0D, -100.0D, 0.0D, 0.0D);
				var21.addVertexWithUV(-100.0D, -100.0D, 100.0D, 0.0D, 16.0D);
				var21.addVertexWithUV(100.0D, -100.0D, 100.0D, 16.0D, 16.0D);
				var21.addVertexWithUV(100.0D, -100.0D, -100.0D, 16.0D, 0.0D);
				var21.draw();
				GL11.glPopMatrix();
			}

			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
		} else if (this.mc.theWorld.provider.isSurfaceWorld()) {
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			Vec3 var2 = this.theWorld.getSkyColor(this.mc.renderViewEntity, par1);
			float var3 = (float)var2.xCoord;
			float var4 = (float)var2.yCoord;
			float var5 = (float)var2.zCoord;

			// Spout Start
			Color skyColor = SpoutClient.getInstance().getSkyManager().getSkyColor();
			if (skyColor != null) {
				var3 = skyColor.getRedF();
				var4 = skyColor.getGreenF();
				var5 = skyColor.getBlueF();
			}
			// Spout End

			float var8;

			if (this.mc.gameSettings.anaglyph) {
				float var6 = (var3 * 30.0F + var4 * 59.0F + var5 * 11.0F) / 100.0F;
				float var7 = (var3 * 30.0F + var4 * 70.0F) / 100.0F;
				var8 = (var3 * 30.0F + var5 * 70.0F) / 100.0F;
				var3 = var6;
				var4 = var7;
				var5 = var8;
			}

			GL11.glColor3f(var3, var4, var5);
			Tessellator var23 = Tessellator.instance;
			GL11.glDepthMask(false);
			GL11.glEnable(GL11.GL_FOG);
			GL11.glColor3f(var3, var4, var5);
			// Spout Start
			if (Configuration.isSky()) {
			GL11.glCallList(this.glSkyList);
			}
			// Spout End
			GL11.glDisable(GL11.GL_FOG);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			RenderHelper.disableStandardItemLighting();
			float[] var24 = this.theWorld.provider.calcSunriseSunsetColors(this.theWorld.getCelestialAngle(par1), par1);
			float var9;
			float var10;
			float var11;
			float var12;

			// MCPatcher Start
			if (var24 != null && Configuration.isSky()) {
			// MCPatcher End
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glShadeModel(GL11.GL_SMOOTH);
				GL11.glPushMatrix();
				GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(MathHelper.sin(this.theWorld.getCelestialAngleRadians(par1)) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
				GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
				var8 = var24[0];
				var9 = var24[1];
				var10 = var24[2];
				float var13;

				if (this.mc.gameSettings.anaglyph) {
					var11 = (var8 * 30.0F + var9 * 59.0F + var10 * 11.0F) / 100.0F;
					var12 = (var8 * 30.0F + var9 * 70.0F) / 100.0F;
					var13 = (var8 * 30.0F + var10 * 70.0F) / 100.0F;
					var8 = var11;
					var9 = var12;
					var10 = var13;
				}

				var23.startDrawing(6);
				var23.setColorRGBA_F(var8, var9, var10, var24[3]);
				var23.addVertex(0.0D, 100.0D, 0.0D);
				byte var26 = 16;
				var23.setColorRGBA_F(var24[0], var24[1], var24[2], 0.0F);

				for (int var27 = 0; var27 <= var26; ++var27) {
					var13 = (float)var27 * (float)Math.PI * 2.0F / (float)var26;
					float var14 = MathHelper.sin(var13);
					float var15 = MathHelper.cos(var13);
					var23.addVertex((double)(var14 * 120.0F), (double)(var15 * 120.0F), (double)(-var15 * 40.0F * var24[3]));
				}

				var23.draw();
				GL11.glPopMatrix();
				GL11.glShadeModel(GL11.GL_FLAT);
			}

			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			GL11.glPushMatrix();
			var8 = 1.0F - this.theWorld.getRainStrength(par1);
			var9 = 0.0F;
			var10 = 0.0F;
			var11 = 0.0F;
			GL11.glColor4f(1.0F, 1.0F, 1.0F, var8);
			GL11.glTranslatef(var9, var10, var11);
			GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
			// MCPatcher Start
			SkyRenderer.renderAll();
			// MCPatcher End
			GL11.glRotatef(this.theWorld.getCelestialAngle(par1) * 360.0F, 1.0F, 0.0F, 0.0F);
			var12 = 30.0F;
			// Spout Start
			if (SpoutClient.getInstance().getSkyManager().isSunVisible()) {
				if (SpoutClient.getInstance().getSkyManager().getSunTextureUrl() == null || CustomTextureManager.getTexturePathFromUrl(SpoutClient.getInstance().getSkyManager().getSunTextureUrl()) == null) {
				this.renderEngine.bindTexture(SkyRenderer.setupCelestialObject("/environment/sun.png"));
			} else {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture(CustomTextureManager.getTexturePathFromUrl(SpoutClient.getInstance().getSkyManager().getSunTextureUrl())));
			}
			double multiplier = (SpoutClient.getInstance().getSkyManager().getSunSizePercent() / 100D);
			// Spout End
			var23.startDrawingQuads();
			// Spout Start
			var23.addVertexWithUV((double)(-var12), 100.0D / multiplier, (double)(-var12), 0.0D, 0.0D);
			var23.addVertexWithUV((double)var12, 100.0D / multiplier, (double)(-var12), 1.0D, 0.0D);
			var23.addVertexWithUV((double)var12, 100.0D / multiplier, (double)var12, 1.0D, 1.0D);
			var23.addVertexWithUV((double)(-var12), 100.0D / multiplier, (double)var12, 0.0D, 1.0D);
			var23.draw();
			}
			// Spout End
			var12 = 20.0F;
			// Spout Start
			if (SpoutClient.getInstance().getSkyManager().isMoonVisible()) {
			if (SpoutClient.getInstance().getSkyManager().getMoonTextureUrl() == null || CustomTextureManager.getTexturePathFromUrl(SpoutClient.getInstance().getSkyManager().getMoonTextureUrl()) == null) {
				this.renderEngine.bindTexture(SkyRenderer.setupCelestialObject("/environment/moon_phases.png"));
			} else {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.renderEngine.getTexture(CustomTextureManager.getTexturePathFromUrl(SpoutClient.getInstance().getSkyManager().getMoonTextureUrl())));
			}
			int var26 = this.theWorld.getMoonPhase();
			int var27 = var26 % 4;
			int var28 = var26 / 4 % 2;
			float var16 = (float)(var27 + 0) / 4.0F;
			float var17 = (float)(var28 + 0) / 2.0F;
			float var18 = (float)(var27 + 1) / 4.0F;
			float var19 = (float)(var28 + 1) / 2.0F;
			var23.startDrawingQuads();
			double multiplier = (SpoutClient.getInstance().getSkyManager().getMoonSizePercent() / 100D);
			var23.addVertexWithUV((double)(-var12), -100.0D, (double)var12, (double)var18, (double)var19);
			var23.addVertexWithUV((double)var12, -100.0D, (double)var12, (double)var16, (double)var19);
			var23.addVertexWithUV((double)var12, -100.0D, (double)(-var12), (double)var16, (double)var17);
			var23.addVertexWithUV((double)(-var12), -100.0D, (double)(-var12), (double)var18, (double)var17);
			var23.draw();
			}
			// Spout End
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			float var20 = this.theWorld.getStarBrightness(par1) * var8;

			if (var20 > 0.0F && !SkyRenderer.active) {
				GL11.glColor4f(var20, var20, var20, var20);
				GL11.glCallList(this.starGLCallList);
			}

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_FOG);
			GL11.glPopMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glColor3f(0.0F, 0.0F, 0.0F);
			double var25 = this.mc.thePlayer.getPosition(par1).yCoord - this.theWorld.getHorizon();

			// Spout Start - Added stars condition
			if (var25 < 0.0D && Configuration.isStars()) {
			// Spout End
				GL11.glPushMatrix();
				GL11.glTranslatef(0.0F, 12.0F, 0.0F);
				GL11.glCallList(this.glSkyList2);
				GL11.glPopMatrix();
				var10 = 1.0F;
				var11 = -((float)(var25 + 65.0D));
				var12 = -var10;
				var23.startDrawingQuads();
				var23.setColorRGBA_I(0, 255);
				var23.addVertex((double)(-var10), (double)var11, (double)var10);
				var23.addVertex((double)var10, (double)var11, (double)var10);
				var23.addVertex((double)var10, (double)var12, (double)var10);
				var23.addVertex((double)(-var10), (double)var12, (double)var10);
				var23.addVertex((double)(-var10), (double)var12, (double)(-var10));
				var23.addVertex((double)var10, (double)var12, (double)(-var10));
				var23.addVertex((double)var10, (double)var11, (double)(-var10));
				var23.addVertex((double)(-var10), (double)var11, (double)(-var10));
				var23.addVertex((double)var10, (double)var12, (double)(-var10));
				var23.addVertex((double)var10, (double)var12, (double)var10);
				var23.addVertex((double)var10, (double)var11, (double)var10);
				var23.addVertex((double)var10, (double)var11, (double)(-var10));
				var23.addVertex((double)(-var10), (double)var11, (double)(-var10));
				var23.addVertex((double)(-var10), (double)var11, (double)var10);
				var23.addVertex((double)(-var10), (double)var12, (double)var10);
				var23.addVertex((double)(-var10), (double)var12, (double)(-var10));
				var23.addVertex((double)(-var10), (double)var12, (double)(-var10));
				var23.addVertex((double)(-var10), (double)var12, (double)var10);
				var23.addVertex((double)var10, (double)var12, (double)var10);
				var23.addVertex((double)var10, (double)var12, (double)(-var10));
				var23.draw();
			}

			if (this.theWorld.provider.isSkyColored()) {
				GL11.glColor3f(var3 * 0.2F + 0.04F, var4 * 0.2F + 0.04F, var5 * 0.6F + 0.1F);
			} else {
				GL11.glColor3f(var3, var4, var5);
			}

			GL11.glPushMatrix();
			GL11.glTranslatef(0.0F, -((float)(var25 - 16.0D)), 0.0F);
			// Spout Start
			if (Configuration.isSky()) {
			GL11.glCallList(this.glSkyList2);
			}
			// Spout End
			GL11.glPopMatrix();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDepthMask(true);
		}
	}

	public void renderClouds(float par1) {
		// Spout Start
		if (!SpoutClient.getInstance().getSkyManager().isCloudsVisible() || !Configuration.isSky()) {
			return;
		}
		if (this.mc.theWorld.provider.isSurfaceWorld()) {
			if (ColorizeWorld.drawFancyClouds(this.mc.gameSettings.fancyGraphics)) {
		// Spout End
				this.renderCloudsFancy(par1);
			} else {
				GL11.glDisable(GL11.GL_CULL_FACE);
				float var2 = (float)(this.mc.renderViewEntity.lastTickPosY + (this.mc.renderViewEntity.posY - this.mc.renderViewEntity.lastTickPosY) * (double)par1);
				byte var3 = 32;
				int var4 = 256 / var3;
				Tessellator var5 = Tessellator.instance;
				this.renderEngine.bindTexture("/environment/clouds.png");
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				Vec3 var6 = this.theWorld.getCloudColour(par1);
				float var7 = (float)var6.xCoord;
				float var8 = (float)var6.yCoord;
				float var9 = (float)var6.zCoord;
				float var10;

				if (this.mc.gameSettings.anaglyph) {
					var10 = (var7 * 30.0F + var8 * 59.0F + var9 * 11.0F) / 100.0F;
					float var11 = (var7 * 30.0F + var8 * 70.0F) / 100.0F;
					float var12 = (var7 * 30.0F + var9 * 70.0F) / 100.0F;
					var7 = var10;
					var8 = var11;
					var9 = var12;
				}

				var10 = 4.8828125E-4F;
				double var24 = (double)((float)this.cloudTickCounter + par1);
				double var13 = this.mc.renderViewEntity.prevPosX + (this.mc.renderViewEntity.posX - this.mc.renderViewEntity.prevPosX) * (double)par1 + var24 * 0.029999999329447746D;
				double var15 = this.mc.renderViewEntity.prevPosZ + (this.mc.renderViewEntity.posZ - this.mc.renderViewEntity.prevPosZ) * (double)par1;
				int var17 = MathHelper.floor_double(var13 / 2048.0D);
				int var18 = MathHelper.floor_double(var15 / 2048.0D);
				var13 -= (double)(var17 * 2048);
				var15 -= (double)(var18 * 2048);
				// Spout Start
				float var19 = SpoutClient.getInstance().getSkyManager().getCloudHeight() - var2 + 0.33F;
				// Spout End
				float var20 = (float)(var13 * (double)var10);
				float var21 = (float)(var15 * (double)var10);
				var5.startDrawingQuads();
				var5.setColorRGBA_F(var7, var8, var9, 0.8F);

				for (int var22 = -var3 * var4; var22 < var3 * var4; var22 += var3) {
					for (int var23 = -var3 * var4; var23 < var3 * var4; var23 += var3) {
						var5.addVertexWithUV((double)(var22 + 0), (double)var19, (double)(var23 + var3), (double)((float)(var22 + 0) * var10 + var20), (double)((float)(var23 + var3) * var10 + var21));
						var5.addVertexWithUV((double)(var22 + var3), (double)var19, (double)(var23 + var3), (double)((float)(var22 + var3) * var10 + var20), (double)((float)(var23 + var3) * var10 + var21));
						var5.addVertexWithUV((double)(var22 + var3), (double)var19, (double)(var23 + 0), (double)((float)(var22 + var3) * var10 + var20), (double)((float)(var23 + 0) * var10 + var21));
						var5.addVertexWithUV((double)(var22 + 0), (double)var19, (double)(var23 + 0), (double)((float)(var22 + 0) * var10 + var20), (double)((float)(var23 + 0) * var10 + var21));
					}
				}

				var5.draw();
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_CULL_FACE);
			}
		}
	}

	/**
	 * Checks if the given position is to be rendered with cloud fog
	 */
	public boolean hasCloudFog(double par1, double par3, double par5, float par7) {
		return false;
	}

	/**
	 * Renders the 3d fancy clouds
	 */
	public void renderCloudsFancy(float par1) {
		GL11.glDisable(GL11.GL_CULL_FACE);
		float var2 = (float)(this.mc.renderViewEntity.lastTickPosY + (this.mc.renderViewEntity.posY - this.mc.renderViewEntity.lastTickPosY) * (double)par1);
		Tessellator var3 = Tessellator.instance;
		float var4 = 12.0F;
		float var5 = 4.0F;
		double var6 = (double)((float)this.cloudTickCounter + par1);
		double var8 = (this.mc.renderViewEntity.prevPosX + (this.mc.renderViewEntity.posX - this.mc.renderViewEntity.prevPosX) * (double)par1 + var6 * 0.029999999329447746D) / (double)var4;
		double var10 = (this.mc.renderViewEntity.prevPosZ + (this.mc.renderViewEntity.posZ - this.mc.renderViewEntity.prevPosZ) * (double)par1) / (double)var4 + 0.33000001311302185D;
		// Spout Start
		float var12 = SpoutClient.getInstance().getSkyManager().getCloudHeight() - var2 + 0.33F;
		// Spout End
		int var13 = MathHelper.floor_double(var8 / 2048.0D);
		int var14 = MathHelper.floor_double(var10 / 2048.0D);
		var8 -= (double)(var13 * 2048);
		var10 -= (double)(var14 * 2048);
		this.renderEngine.bindTexture("/environment/clouds.png");
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Vec3 var15 = this.theWorld.getCloudColour(par1);
		float var16 = (float)var15.xCoord;
		float var17 = (float)var15.yCoord;
		float var18 = (float)var15.zCoord;
		// Spout Start
		Color cloudColor = SpoutClient.getInstance().getSkyManager().getCloudColor();
		if (cloudColor != null) {
			var16 = cloudColor.getRedF();
			var17 = cloudColor.getGreenF();
			var18 = cloudColor.getBlueF();
		}
		// Spout End
		float var19;
		float var21;
		float var20;

		if (this.mc.gameSettings.anaglyph) {
			var19 = (var16 * 30.0F + var17 * 59.0F + var18 * 11.0F) / 100.0F;
			var20 = (var16 * 30.0F + var17 * 70.0F) / 100.0F;
			var21 = (var16 * 30.0F + var18 * 70.0F) / 100.0F;
			var16 = var19;
			var17 = var20;
			var18 = var21;
		}

		var19 = (float)(var8 * 0.0D);
		var20 = (float)(var10 * 0.0D);
		var21 = 0.00390625F;
		var19 = (float)MathHelper.floor_double(var8) * var21;
		var20 = (float)MathHelper.floor_double(var10) * var21;
		float var22 = (float)(var8 - (double)MathHelper.floor_double(var8));
		float var23 = (float)(var10 - (double)MathHelper.floor_double(var10));
		byte var24 = 8;
		byte var25 = 4;
		float var26 = 9.765625E-4F;
		GL11.glScalef(var4, 1.0F, var4);

		for (int var27 = 0; var27 < 2; ++var27) {
			if (var27 == 0) {
				GL11.glColorMask(false, false, false, false);
			} else if (this.mc.gameSettings.anaglyph) {
				if (EntityRenderer.anaglyphField == 0) {
					GL11.glColorMask(false, true, true, true);
				} else {
					GL11.glColorMask(true, false, false, true);
				}
			} else {
				GL11.glColorMask(true, true, true, true);
			}

			for (int var28 = -var25 + 1; var28 <= var25; ++var28) {
				for (int var29 = -var25 + 1; var29 <= var25; ++var29) {
					var3.startDrawingQuads();
					float var30 = (float)(var28 * var24);
					float var31 = (float)(var29 * var24);
					float var32 = var30 - var22;
					float var33 = var31 - var23;

					if (var12 > -var5 - 1.0F) {
						var3.setColorRGBA_F(var16 * 0.7F, var17 * 0.7F, var18 * 0.7F, 0.8F);
						var3.setNormal(0.0F, -1.0F, 0.0F);
						var3.addVertexWithUV((double)(var32 + 0.0F), (double)(var12 + 0.0F), (double)(var33 + (float)var24), (double)((var30 + 0.0F) * var21 + var19), (double)((var31 + (float)var24) * var21 + var20));
						var3.addVertexWithUV((double)(var32 + (float)var24), (double)(var12 + 0.0F), (double)(var33 + (float)var24), (double)((var30 + (float)var24) * var21 + var19), (double)((var31 + (float)var24) * var21 + var20));
						var3.addVertexWithUV((double)(var32 + (float)var24), (double)(var12 + 0.0F), (double)(var33 + 0.0F), (double)((var30 + (float)var24) * var21 + var19), (double)((var31 + 0.0F) * var21 + var20));
						var3.addVertexWithUV((double)(var32 + 0.0F), (double)(var12 + 0.0F), (double)(var33 + 0.0F), (double)((var30 + 0.0F) * var21 + var19), (double)((var31 + 0.0F) * var21 + var20));
					}

					if (var12 <= var5 + 1.0F) {
						var3.setColorRGBA_F(var16, var17, var18, 0.8F);
						var3.setNormal(0.0F, 1.0F, 0.0F);
						var3.addVertexWithUV((double)(var32 + 0.0F), (double)(var12 + var5 - var26), (double)(var33 + (float)var24), (double)((var30 + 0.0F) * var21 + var19), (double)((var31 + (float)var24) * var21 + var20));
						var3.addVertexWithUV((double)(var32 + (float)var24), (double)(var12 + var5 - var26), (double)(var33 + (float)var24), (double)((var30 + (float)var24) * var21 + var19), (double)((var31 + (float)var24) * var21 + var20));
						var3.addVertexWithUV((double)(var32 + (float)var24), (double)(var12 + var5 - var26), (double)(var33 + 0.0F), (double)((var30 + (float)var24) * var21 + var19), (double)((var31 + 0.0F) * var21 + var20));
						var3.addVertexWithUV((double)(var32 + 0.0F), (double)(var12 + var5 - var26), (double)(var33 + 0.0F), (double)((var30 + 0.0F) * var21 + var19), (double)((var31 + 0.0F) * var21 + var20));
					}

					var3.setColorRGBA_F(var16 * 0.9F, var17 * 0.9F, var18 * 0.9F, 0.8F);
					int var34;

					if (var28 > -1) {
						var3.setNormal(-1.0F, 0.0F, 0.0F);

						for (var34 = 0; var34 < var24; ++var34) {
							var3.addVertexWithUV((double)(var32 + (float)var34 + 0.0F), (double)(var12 + 0.0F), (double)(var33 + (float)var24), (double)((var30 + (float)var34 + 0.5F) * var21 + var19), (double)((var31 + (float)var24) * var21 + var20));
							var3.addVertexWithUV((double)(var32 + (float)var34 + 0.0F), (double)(var12 + var5), (double)(var33 + (float)var24), (double)((var30 + (float)var34 + 0.5F) * var21 + var19), (double)((var31 + (float)var24) * var21 + var20));
							var3.addVertexWithUV((double)(var32 + (float)var34 + 0.0F), (double)(var12 + var5), (double)(var33 + 0.0F), (double)((var30 + (float)var34 + 0.5F) * var21 + var19), (double)((var31 + 0.0F) * var21 + var20));
							var3.addVertexWithUV((double)(var32 + (float)var34 + 0.0F), (double)(var12 + 0.0F), (double)(var33 + 0.0F), (double)((var30 + (float)var34 + 0.5F) * var21 + var19), (double)((var31 + 0.0F) * var21 + var20));
						}
					}

					if (var28 <= 1) {
						var3.setNormal(1.0F, 0.0F, 0.0F);

						for (var34 = 0; var34 < var24; ++var34) {
							var3.addVertexWithUV((double)(var32 + (float)var34 + 1.0F - var26), (double)(var12 + 0.0F), (double)(var33 + (float)var24), (double)((var30 + (float)var34 + 0.5F) * var21 + var19), (double)((var31 + (float)var24) * var21 + var20));
							var3.addVertexWithUV((double)(var32 + (float)var34 + 1.0F - var26), (double)(var12 + var5), (double)(var33 + (float)var24), (double)((var30 + (float)var34 + 0.5F) * var21 + var19), (double)((var31 + (float)var24) * var21 + var20));
							var3.addVertexWithUV((double)(var32 + (float)var34 + 1.0F - var26), (double)(var12 + var5), (double)(var33 + 0.0F), (double)((var30 + (float)var34 + 0.5F) * var21 + var19), (double)((var31 + 0.0F) * var21 + var20));
							var3.addVertexWithUV((double)(var32 + (float)var34 + 1.0F - var26), (double)(var12 + 0.0F), (double)(var33 + 0.0F), (double)((var30 + (float)var34 + 0.5F) * var21 + var19), (double)((var31 + 0.0F) * var21 + var20));
						}
					}

					var3.setColorRGBA_F(var16 * 0.8F, var17 * 0.8F, var18 * 0.8F, 0.8F);

					if (var29 > -1) {
						var3.setNormal(0.0F, 0.0F, -1.0F);

						for (var34 = 0; var34 < var24; ++var34) {
							var3.addVertexWithUV((double)(var32 + 0.0F), (double)(var12 + var5), (double)(var33 + (float)var34 + 0.0F), (double)((var30 + 0.0F) * var21 + var19), (double)((var31 + (float)var34 + 0.5F) * var21 + var20));
							var3.addVertexWithUV((double)(var32 + (float)var24), (double)(var12 + var5), (double)(var33 + (float)var34 + 0.0F), (double)((var30 + (float)var24) * var21 + var19), (double)((var31 + (float)var34 + 0.5F) * var21 + var20));
							var3.addVertexWithUV((double)(var32 + (float)var24), (double)(var12 + 0.0F), (double)(var33 + (float)var34 + 0.0F), (double)((var30 + (float)var24) * var21 + var19), (double)((var31 + (float)var34 + 0.5F) * var21 + var20));
							var3.addVertexWithUV((double)(var32 + 0.0F), (double)(var12 + 0.0F), (double)(var33 + (float)var34 + 0.0F), (double)((var30 + 0.0F) * var21 + var19), (double)((var31 + (float)var34 + 0.5F) * var21 + var20));
						}
					}

					if (var29 <= 1) {
						var3.setNormal(0.0F, 0.0F, 1.0F);

						for (var34 = 0; var34 < var24; ++var34) {
							var3.addVertexWithUV((double)(var32 + 0.0F), (double)(var12 + var5), (double)(var33 + (float)var34 + 1.0F - var26), (double)((var30 + 0.0F) * var21 + var19), (double)((var31 + (float)var34 + 0.5F) * var21 + var20));
							var3.addVertexWithUV((double)(var32 + (float)var24), (double)(var12 + var5), (double)(var33 + (float)var34 + 1.0F - var26), (double)((var30 + (float)var24) * var21 + var19), (double)((var31 + (float)var34 + 0.5F) * var21 + var20));
							var3.addVertexWithUV((double)(var32 + (float)var24), (double)(var12 + 0.0F), (double)(var33 + (float)var34 + 1.0F - var26), (double)((var30 + (float)var24) * var21 + var19), (double)((var31 + (float)var34 + 0.5F) * var21 + var20));
							var3.addVertexWithUV((double)(var32 + 0.0F), (double)(var12 + 0.0F), (double)(var33 + (float)var34 + 1.0F - var26), (double)((var30 + 0.0F) * var21 + var19), (double)((var31 + (float)var34 + 0.5F) * var21 + var20));
						}
					}

					var3.draw();
				}
			}
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_CULL_FACE);
	}

	/**
	 * Updates some of the renderers sorted by distance from the player
	 */
	public boolean updateRenderers(EntityLiving par1EntityLiving, boolean par2) {
		byte var3 = 2;
		RenderSorter var4 = new RenderSorter(par1EntityLiving);
		WorldRenderer[] var5 = new WorldRenderer[var3];
		ArrayList var6 = null;
		int var7 = this.worldRenderersToUpdate.size();
		int var8 = 0;
		this.theWorld.theProfiler.startSection("nearChunksSearch");
		int var9;
		WorldRenderer var10;
		int var11;
		int var12;
		label136:

		for (var9 = 0; var9 < var7; ++var9) {
			var10 = (WorldRenderer)this.worldRenderersToUpdate.get(var9);

			if (var10 != null) {
				if (!par2) {
					if (var10.distanceToEntitySquared(par1EntityLiving) > 256.0F) {
						for (var11 = 0; var11 < var3 && (var5[var11] == null || var4.doCompare(var5[var11], var10) <= 0); ++var11) {
							;
						}

						--var11;

						if (var11 > 0) {
							var12 = var11;

							while (true) {
								--var12;

								if (var12 == 0) {
									var5[var11] = var10;
									continue label136;
								}

								var5[var12 - 1] = var5[var12];
							}
						}

						continue;
					}
				} else if (!var10.isInFrustum) {
					continue;
				}

				if (var6 == null) {
					var6 = new ArrayList();
				}

				++var8;
				var6.add(var10);
				this.worldRenderersToUpdate.set(var9, (Object)null);
			}
		}

		this.theWorld.theProfiler.endSection();
		this.theWorld.theProfiler.startSection("sort");

		if (var6 != null) {
			if (var6.size() > 1) {
				Collections.sort(var6, var4);
			}

			for (var9 = var6.size() - 1; var9 >= 0; --var9) {
				var10 = (WorldRenderer)var6.get(var9);
				var10.updateRenderer();
				var10.needsUpdate = false;
			}
		}

		this.theWorld.theProfiler.endSection();
		var9 = 0;
		this.theWorld.theProfiler.startSection("rebuild");
		int var16;

		for (var16 = var3 - 1; var16 >= 0; --var16) {
			WorldRenderer var17 = var5[var16];

			if (var17 != null) {
				if (!var17.isInFrustum && var16 != var3 - 1) {
					var5[var16] = null;
					var5[0] = null;
					break;
				}

				var5[var16].updateRenderer();
				var5[var16].needsUpdate = false;
				++var9;
			}
		}

		this.theWorld.theProfiler.endSection();
		this.theWorld.theProfiler.startSection("cleanup");
		var16 = 0;
		var11 = 0;

		for (var12 = this.worldRenderersToUpdate.size(); var16 != var12; ++var16) {
			WorldRenderer var13 = (WorldRenderer)this.worldRenderersToUpdate.get(var16);

			if (var13 != null) {
				boolean var14 = false;

				for (int var15 = 0; var15 < var3 && !var14; ++var15) {
					if (var13 == var5[var15]) {
						var14 = true;
					}
				}

				if (!var14) {
					if (var11 != var16) {
						this.worldRenderersToUpdate.set(var11, var13);
					}

					++var11;
				}
			}
		}

		this.theWorld.theProfiler.endSection();
		this.theWorld.theProfiler.startSection("trim");

		while (true) {
			--var16;

			if (var16 < var11) {
				this.theWorld.theProfiler.endSection();
				return var7 == var8 + var9;
			}

			this.worldRenderersToUpdate.remove(var16);
		}
	}

	public void drawBlockBreaking(EntityPlayer par1EntityPlayer, MovingObjectPosition par2MovingObjectPosition, int par3, ItemStack par4ItemStack, float par5) {
		Tessellator var6 = Tessellator.instance;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, (MathHelper.sin((float)Minecraft.getSystemTime() / 100.0F) * 0.2F + 0.4F) * 0.5F);

		if (par3 != 0 && par4ItemStack != null) {
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			float var7 = MathHelper.sin((float)Minecraft.getSystemTime() / 100.0F) * 0.2F + 0.8F;
			GL11.glColor4f(var7, var7, var7, MathHelper.sin((float)Minecraft.getSystemTime() / 200.0F) * 0.2F + 0.5F);
			this.renderEngine.bindTexture("/terrain.png");
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
	}

	public void drawBlockDamageTexture(Tessellator par1Tessellator, EntityPlayer par2EntityPlayer, float par3) {
		double var4 = par2EntityPlayer.lastTickPosX + (par2EntityPlayer.posX - par2EntityPlayer.lastTickPosX) * (double)par3;
		double var6 = par2EntityPlayer.lastTickPosY + (par2EntityPlayer.posY - par2EntityPlayer.lastTickPosY) * (double)par3;
		double var8 = par2EntityPlayer.lastTickPosZ + (par2EntityPlayer.posZ - par2EntityPlayer.lastTickPosZ) * (double)par3;

		if (!this.damagedBlocks.isEmpty()) {
			GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR);
			this.renderEngine.bindTexture("/terrain.png");
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glPolygonOffset(-3.0F, -3.0F);
			GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			par1Tessellator.startDrawingQuads();
			par1Tessellator.setTranslation(-var4, -var6, -var8);
			par1Tessellator.disableColor();
			Iterator var10 = this.damagedBlocks.values().iterator();

			while (var10.hasNext()) {
				DestroyBlockProgress var11 = (DestroyBlockProgress)var10.next();
				double var12 = (double)var11.getPartialBlockX() - var4;
				double var14 = (double)var11.getPartialBlockY() - var6;
				double var16 = (double)var11.getPartialBlockZ() - var8;

				if (var12 * var12 + var14 * var14 + var16 * var16 > 1024.0D) {
					var10.remove();
				} else {
					int var18 = this.theWorld.getBlockId(var11.getPartialBlockX(), var11.getPartialBlockY(), var11.getPartialBlockZ());
					Block var19 = var18 > 0 ? Block.blocksList[var18] : null;

					if (var19 == null) {
						var19 = Block.stone;
					}

					this.globalRenderBlocks.renderBlockUsingTexture(var19, var11.getPartialBlockX(), var11.getPartialBlockY(), var11.getPartialBlockZ(), this.destroyBlockIcons[var11.getPartialBlockDamage()]);
				}
			}

			par1Tessellator.draw();
			par1Tessellator.setTranslation(0.0D, 0.0D, 0.0D);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glPolygonOffset(0.0F, 0.0F);
			GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glDepthMask(true);
			GL11.glPopMatrix();
		}
	}

	/**
	 * Draws the selection box for the player. Args: entityPlayer, rayTraceHit, i, itemStack, partialTickTime
	 */
	public void drawSelectionBox(EntityPlayer par1EntityPlayer, MovingObjectPosition par2MovingObjectPosition, int par3, ItemStack par4ItemStack, float par5) {
		if (par3 == 0 && par2MovingObjectPosition.typeOfHit == EnumMovingObjectType.TILE) {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
			GL11.glLineWidth(2.0F);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDepthMask(false);
			float var6 = 0.002F;
			int var7 = this.theWorld.getBlockId(par2MovingObjectPosition.blockX, par2MovingObjectPosition.blockY, par2MovingObjectPosition.blockZ);

			if (var7 > 0) {
				Block.blocksList[var7].setBlockBoundsBasedOnState(this.theWorld, par2MovingObjectPosition.blockX, par2MovingObjectPosition.blockY, par2MovingObjectPosition.blockZ);
				double var8 = par1EntityPlayer.lastTickPosX + (par1EntityPlayer.posX - par1EntityPlayer.lastTickPosX) * (double)par5;
				double var10 = par1EntityPlayer.lastTickPosY + (par1EntityPlayer.posY - par1EntityPlayer.lastTickPosY) * (double)par5;
				double var12 = par1EntityPlayer.lastTickPosZ + (par1EntityPlayer.posZ - par1EntityPlayer.lastTickPosZ) * (double)par5;
				this.drawOutlinedBoundingBox(Block.blocksList[var7].getSelectedBoundingBoxFromPool(this.theWorld, par2MovingObjectPosition.blockX, par2MovingObjectPosition.blockY, par2MovingObjectPosition.blockZ).expand((double)var6, (double)var6, (double)var6).getOffsetBoundingBox(-var8, -var10, -var12));
			}

			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);
		}
	}

	/**
	 * Draws lines for the edges of the bounding box.
	 */
	private void drawOutlinedBoundingBox(AxisAlignedBB par1AxisAlignedBB) {
		Tessellator var2 = Tessellator.instance;
		var2.startDrawing(3);
		var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		var2.draw();
		var2.startDrawing(3);
		var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		var2.draw();
		var2.startDrawing(1);
		var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		var2.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		var2.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		var2.draw();
	}

	/**
	 * Marks the blocks in the given range for update
	 */
	public void markBlocksForUpdate(int par1, int par2, int par3, int par4, int par5, int par6) {
		int var7 = MathHelper.bucketInt(par1, 16);
		int var8 = MathHelper.bucketInt(par2, 16);
		int var9 = MathHelper.bucketInt(par3, 16);
		int var10 = MathHelper.bucketInt(par4, 16);
		int var11 = MathHelper.bucketInt(par5, 16);
		int var12 = MathHelper.bucketInt(par6, 16);

		for (int var13 = var7; var13 <= var10; ++var13) {
			int var14 = var13 % this.renderChunksWide;

			if (var14 < 0) {
				var14 += this.renderChunksWide;
			}

			for (int var15 = var8; var15 <= var11; ++var15) {
				int var16 = var15 % this.renderChunksTall;

				if (var16 < 0) {
					var16 += this.renderChunksTall;
				}

				for (int var17 = var9; var17 <= var12; ++var17) {
					int var18 = var17 % this.renderChunksDeep;

					if (var18 < 0) {
						var18 += this.renderChunksDeep;
					}

					int var19 = (var18 * this.renderChunksTall + var16) * this.renderChunksWide + var14;
					WorldRenderer var20 = this.worldRenderers[var19];

					if (var20 != null && !var20.needsUpdate) {
						this.worldRenderersToUpdate.add(var20);
						var20.markDirty();
					}
				}
			}
		}
	}

	/**
	 * On the client, re-renders the block. On the server, sends the block to the client (which will re-render it),
	 * including the tile entity description packet if applicable. Args: x, y, z
	 */
	public void markBlockForUpdate(int par1, int par2, int par3) {
		this.markBlocksForUpdate(par1 - 1, par2 - 1, par3 - 1, par1 + 1, par2 + 1, par3 + 1);
	}

	/**
	 * On the client, re-renders this block. On the server, does nothing. Used for lighting updates.
	 */
	public void markBlockForRenderUpdate(int par1, int par2, int par3) {
		this.markBlocksForUpdate(par1 - 1, par2 - 1, par3 - 1, par1 + 1, par2 + 1, par3 + 1);
	}

	/**
	 * On the client, re-renders all blocks in this range, inclusive. On the server, does nothing. Args: min x, min y, min
	 * z, max x, max y, max z
	 */
	public void markBlockRangeForRenderUpdate(int par1, int par2, int par3, int par4, int par5, int par6) {
		this.markBlocksForUpdate(par1 - 1, par2 - 1, par3 - 1, par4 + 1, par5 + 1, par6 + 1);
	}

	/**
	 * Checks all renderers that previously weren't in the frustum and 1/16th of those that previously were in the frustum
	 * for frustum clipping Args: frustum, partialTickTime
	 */
	public void clipRenderersByFrustum(ICamera par1ICamera, float par2) {
		for (int var3 = 0; var3 < this.worldRenderers.length; ++var3) {
			if (!this.worldRenderers[var3].skipAllRenderPasses() && (!this.worldRenderers[var3].isInFrustum || (var3 + this.frustumCheckOffset & 15) == 0)) {
				this.worldRenderers[var3].updateInFrustum(par1ICamera);
			}
		}

		++this.frustumCheckOffset;
	}

	/**
	 * Plays the specified record. Arg: recordName, x, y, z
	 */
	public void playRecord(String par1Str, int par2, int par3, int par4) {
		ItemRecord var5 = ItemRecord.getRecord(par1Str);

		if (par1Str != null && var5 != null) {
			this.mc.ingameGUI.setRecordPlayingMessage(var5.getRecordTitle());
		}

		this.mc.sndManager.playStreaming(par1Str, (float)par2, (float)par3, (float)par4);
	}

	/**
	 * Plays the specified sound. Arg: soundName, x, y, z, volume, pitch
	 */
	public void playSound(String par1Str, double par2, double par4, double par6, float par8, float par9) {}

	/**
	 * Plays sound to all near players except the player reference given
	 */
	public void playSoundToNearExcept(EntityPlayer par1EntityPlayer, String par2Str, double par3, double par5, double par7, float par9, float par10) {}

	/**
	 * Spawns a particle. Arg: particleType, x, y, z, velX, velY, velZ
	 */
	public void spawnParticle(String par1Str, double par2, double par4, double par6, double par8, double par10, double par12) {
		// Spout Start
		if (mc == null || theWorld == null || mc.renderViewEntity == null) {
			return;
		}
		// Spout End
		try {
			this.doSpawnParticle(par1Str, par2, par4, par6, par8, par10, par12);
		} catch (Throwable var17) {
			CrashReport var15 = CrashReport.makeCrashReport(var17, "Exception while adding particle");
			CrashReportCategory var16 = var15.makeCategory("Particle being added");
			var16.addCrashSection("Name", par1Str);
			var16.addCrashSectionCallable("Position", new CallableParticlePositionInfo(this, par2, par4, par6));
			throw new ReportedException(var15);
		}
	}

	/**
	 * Spawns a particle. Arg: particleType, x, y, z, velX, velY, velZ
	 */
	public EntityFX doSpawnParticle(String par1Str, double par2, double par4, double par6, double par8, double par10, double par12) {
		if (this.mc != null && this.mc.renderViewEntity != null && this.mc.effectRenderer != null) {
			int var14 = this.mc.gameSettings.particleSetting;

			if (var14 == 1 && this.theWorld.rand.nextInt(3) == 0) {
				var14 = 2;
			}

			double var15 = this.mc.renderViewEntity.posX - par2;
			double var17 = this.mc.renderViewEntity.posY - par4;
			double var19 = this.mc.renderViewEntity.posZ - par6;
			EntityFX var21 = null;

			if (par1Str.equals("hugeexplosion")) {
				this.mc.effectRenderer.addEffect(var21 = new EntityHugeExplodeFX(this.theWorld, par2, par4, par6, par8, par10, par12));
			} else if (par1Str.equals("largeexplode")) {
				this.mc.effectRenderer.addEffect(var21 = new EntityLargeExplodeFX(this.renderEngine, this.theWorld, par2, par4, par6, par8, par10, par12));
			} else if (par1Str.equals("fireworksSpark")) {
				this.mc.effectRenderer.addEffect(var21 = new EntityFireworkSparkFX(this.theWorld, par2, par4, par6, par8, par10, par12, this.mc.effectRenderer));
			}

			if (var21 != null) {
				return (EntityFX)var21;
			} else {
				// Spout Start
				double var22 = 6D;
				if (!org.spoutcraft.client.config.Configuration.isFancyParticles()) {
					var22 = 16.0D;
				}
				// Spout End

				if (var15 * var15 + var17 * var17 + var19 * var19 > var22 * var22) {
					return null;
				} else if (var14 > 1) {
					return null;
				} else {
					if (par1Str.equals("bubble")) {
						var21 = new EntityBubbleFX(this.theWorld, par2, par4, par6, par8, par10, par12);
					} else if (par1Str.equals("suspended")) {
						var21 = new EntitySuspendFX(this.theWorld, par2, par4, par6, par8, par10, par12);
					} else if (par1Str.equals("depthsuspend")) {
						var21 = new EntityAuraFX(this.theWorld, par2, par4, par6, par8, par10, par12);
					} else if (par1Str.equals("townaura")) {
						// MCPatcher Start
						var21 = (new EntityAuraFX(this.theWorld, par2, par4, par6, par8, par10, par12)).colorize();
						// MCPatcher End
					} else if (par1Str.equals("crit")) {
						var21 = new EntityCritFX(this.theWorld, par2, par4, par6, par8, par10, par12);
					} else if (par1Str.equals("magicCrit")) {
						var21 = new EntityCritFX(this.theWorld, par2, par4, par6, par8, par10, par12);
						((EntityFX)var21).setRBGColorF(((EntityFX)var21).getRedColorF() * 0.3F, ((EntityFX)var21).getGreenColorF() * 0.8F, ((EntityFX)var21).getBlueColorF());
						((EntityFX)var21).nextTextureIndexX();
					} else if (par1Str.equals("smoke")) {
						var21 = new EntitySmokeFX(this.theWorld, par2, par4, par6, par8, par10, par12);
					} else if (par1Str.equals("mobSpell")) {
						var21 = new EntitySpellParticleFX(this.theWorld, par2, par4, par6, 0.0D, 0.0D, 0.0D);
						((EntityFX)var21).setRBGColorF((float)par8, (float)par10, (float)par12);
					} else if (par1Str.equals("mobSpellAmbient")) {
						var21 = new EntitySpellParticleFX(this.theWorld, par2, par4, par6, 0.0D, 0.0D, 0.0D);
						((EntityFX)var21).setAlphaF(0.15F);
						((EntityFX)var21).setRBGColorF((float)par8, (float)par10, (float)par12);
					} else if (par1Str.equals("spell")) {
						var21 = new EntitySpellParticleFX(this.theWorld, par2, par4, par6, par8, par10, par12);
					} else if (par1Str.equals("instantSpell")) {
						var21 = new EntitySpellParticleFX(this.theWorld, par2, par4, par6, par8, par10, par12);
						((EntitySpellParticleFX)var21).setBaseSpellTextureIndex(144);
					} else if (par1Str.equals("witchMagic")) {
						var21 = new EntitySpellParticleFX(this.theWorld, par2, par4, par6, par8, par10, par12);
						((EntitySpellParticleFX)var21).setBaseSpellTextureIndex(144);
						float var24 = this.theWorld.rand.nextFloat() * 0.5F + 0.35F;
						((EntityFX)var21).setRBGColorF(1.0F * var24, 0.0F * var24, 1.0F * var24);
					} else if (par1Str.equals("note")) {
						var21 = new EntityNoteFX(this.theWorld, par2, par4, par6, par8, par10, par12);
					} else if (par1Str.equals("portal")) {
						var21 = new EntityPortalFX(this.theWorld, par2, par4, par6, par8, par10, par12);
					} else if (par1Str.equals("enchantmenttable")) {
						var21 = new EntityEnchantmentTableParticleFX(this.theWorld, par2, par4, par6, par8, par10, par12);
					} else if (par1Str.equals("explode")) {
						var21 = new EntityExplodeFX(this.theWorld, par2, par4, par6, par8, par10, par12);
					} else if (par1Str.equals("flame")) {
						var21 = new EntityFlameFX(this.theWorld, par2, par4, par6, par8, par10, par12);
					} else if (par1Str.equals("lava")) {
						var21 = new EntityLavaFX(this.theWorld, par2, par4, par6);
					} else if (par1Str.equals("footstep")) {
						var21 = new EntityFootStepFX(this.renderEngine, this.theWorld, par2, par4, par6);
					} else if (par1Str.equals("splash")) {
						var21 = new EntitySplashFX(this.theWorld, par2, par4, par6, par8, par10, par12);
					} else if (par1Str.equals("largesmoke")) {
						var21 = new EntitySmokeFX(this.theWorld, par2, par4, par6, par8, par10, par12, 2.5F);
					} else if (par1Str.equals("cloud")) {
						var21 = new EntityCloudFX(this.theWorld, par2, par4, par6, par8, par10, par12);
					} else if (par1Str.equals("reddust")) {
						var21 = new EntityReddustFX(this.theWorld, par2, par4, par6, (float)par8, (float)par10, (float)par12);
					} else if (par1Str.equals("snowballpoof")) {
						var21 = new EntityBreakingFX(this.theWorld, par2, par4, par6, Item.snowball, this.renderEngine);
					} else if (par1Str.equals("dripWater")) {
						var21 = new EntityDropParticleFX(this.theWorld, par2, par4, par6, Material.water);
					} else if (par1Str.equals("dripLava")) {
						var21 = new EntityDropParticleFX(this.theWorld, par2, par4, par6, Material.lava);
					} else if (par1Str.equals("snowshovel")) {
						var21 = new EntitySnowShovelFX(this.theWorld, par2, par4, par6, par8, par10, par12);
					} else if (par1Str.equals("slime")) {
						var21 = new EntityBreakingFX(this.theWorld, par2, par4, par6, Item.slimeBall, this.renderEngine);
					} else if (par1Str.equals("heart")) {
						var21 = new EntityHeartFX(this.theWorld, par2, par4, par6, par8, par10, par12);
					} else if (par1Str.equals("angryVillager")) {
						var21 = new EntityHeartFX(this.theWorld, par2, par4 + 0.5D, par6, par8, par10, par12);
						((EntityFX)var21).setParticleTextureIndex(81);
						((EntityFX)var21).setRBGColorF(1.0F, 1.0F, 1.0F);
					} else if (par1Str.equals("happyVillager")) {
						var21 = new EntityAuraFX(this.theWorld, par2, par4, par6, par8, par10, par12);
						((EntityFX)var21).setParticleTextureIndex(82);
						((EntityFX)var21).setRBGColorF(1.0F, 1.0F, 1.0F);
					} else if (par1Str.startsWith("iconcrack_")) {
						int var27 = Integer.parseInt(par1Str.substring(par1Str.indexOf("_") + 1));
						var21 = new EntityBreakingFX(this.theWorld, par2, par4, par6, par8, par10, par12, Item.itemsList[var27], this.renderEngine);
					} else if (par1Str.startsWith("tilecrack_")) {
						String[] var28 = par1Str.split("_", 3);
						int var25 = Integer.parseInt(var28[1]);
						int var26 = Integer.parseInt(var28[2]);
						var21 = (new EntityDiggingFX(this.theWorld, par2, par4, par6, par8, par10, par12, Block.blocksList[var25], 0, var26, this.renderEngine)).applyRenderColor(var26);
					}

					if (var21 != null) {
						this.mc.effectRenderer.addEffect((EntityFX)var21);
					}

					return (EntityFX)var21;
				}
			}
		} else {
			return null;
		}
	}

	/**
	 * Called on all IWorldAccesses when an entity is created or loaded. On client worlds, starts downloading any necessary
	 * textures. On server worlds, adds the entity to the entity tracker.
	 */
	public void onEntityCreate(Entity par1Entity) {
		par1Entity.updateCloak();

		if (par1Entity.skinUrl != null) {
			// Spout Start
			this.renderEngine.obtainImageData(par1Entity.skinUrl, new HDImageBufferDownload());
			// Spout End
		}

		if (par1Entity.cloakUrl != null) {
			// Spout Start
			this.renderEngine.obtainImageData(par1Entity.cloakUrl, new HDImageBufferDownload());
			// Spout End
		}
	}

	/**
	 * Called on all IWorldAccesses when an entity is unloaded or destroyed. On client worlds, releases any downloaded
	 * textures. On server worlds, removes the entity from the entity tracker.
	 */
	public void onEntityDestroy(Entity par1Entity) {
		if (par1Entity.skinUrl != null) {
			this.renderEngine.releaseImageData(par1Entity.skinUrl);
		}

		if (par1Entity.cloakUrl != null) {
			this.renderEngine.releaseImageData(par1Entity.cloakUrl);
		}
	}

	/**
	 * Deletes all display lists
	 */
	public void deleteAllDisplayLists() {
		GLAllocation.deleteDisplayLists(this.glRenderListBase);
	}

	public void broadcastSound(int par1, int par2, int par3, int par4, int par5) {
		Random var6 = this.theWorld.rand;

		switch (par1) {
			case 1013:
			case 1018:
				if (this.mc.renderViewEntity != null) {
					double var7 = (double)par2 - this.mc.renderViewEntity.posX;
					double var9 = (double)par3 - this.mc.renderViewEntity.posY;
					double var11 = (double)par4 - this.mc.renderViewEntity.posZ;
					double var13 = Math.sqrt(var7 * var7 + var9 * var9 + var11 * var11);
					double var15 = this.mc.renderViewEntity.posX;
					double var17 = this.mc.renderViewEntity.posY;
					double var19 = this.mc.renderViewEntity.posZ;

					if (var13 > 0.0D) {
						var15 += var7 / var13 * 2.0D;
						var17 += var9 / var13 * 2.0D;
						var19 += var11 / var13 * 2.0D;
					}

					if (par1 == 1013) {
						this.theWorld.playSound(var15, var17, var19, "mob.wither.spawn", 1.0F, 1.0F, false);
					} else if (par1 == 1018) {
						this.theWorld.playSound(var15, var17, var19, "mob.enderdragon.end", 5.0F, 1.0F, false);
					}
				}

			default:
		}
	}

	/**
	 * Plays a pre-canned sound effect along with potentially auxiliary data-driven one-shot behaviour (particles, etc).
	 */
	public void playAuxSFX(EntityPlayer par1EntityPlayer, int par2, int par3, int par4, int par5, int par6) {
		// Spout Start
		if (mc == null || theWorld == null || mc.renderViewEntity == null) {
			return;
		}
		// Spout End
		Random var7 = this.theWorld.rand;
		double var8;
		double var10;
		double var12;
		String var14;
		int var15;
		int var20;
		double var23;
		double var25;
		double var27;
		double var29;
		double var39;

		switch (par2) {
			case 1000:
				this.theWorld.playSound((double)par3, (double)par4, (double)par5, "random.click", 1.0F, 1.0F, false);
				break;

			case 1001:
				this.theWorld.playSound((double)par3, (double)par4, (double)par5, "random.click", 1.0F, 1.2F, false);
				break;

			case 1002:
				this.theWorld.playSound((double)par3, (double)par4, (double)par5, "random.bow", 1.0F, 1.2F, false);
				break;

			case 1003:
				if (Math.random() < 0.5D) {
					this.theWorld.playSound((double)par3 + 0.5D, (double)par4 + 0.5D, (double)par5 + 0.5D, "random.door_open", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
				} else {
					this.theWorld.playSound((double)par3 + 0.5D, (double)par4 + 0.5D, (double)par5 + 0.5D, "random.door_close", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
				}

				break;

			case 1004:
				this.theWorld.playSound((double)((float)par3 + 0.5F), (double)((float)par4 + 0.5F), (double)((float)par5 + 0.5F), "random.fizz", 0.5F, 2.6F + (var7.nextFloat() - var7.nextFloat()) * 0.8F, false);
				break;

			case 1005:
				if (Item.itemsList[par6] instanceof ItemRecord) {
					this.theWorld.playRecord(((ItemRecord)Item.itemsList[par6]).recordName, par3, par4, par5);
				} else {
					this.theWorld.playRecord((String)null, par3, par4, par5);
				}

				break;

			case 1007:
				this.theWorld.playSound((double)par3 + 0.5D, (double)par4 + 0.5D, (double)par5 + 0.5D, "mob.ghast.charge", 10.0F, (var7.nextFloat() - var7.nextFloat()) * 0.2F + 1.0F, false);
				break;

			case 1008:
				this.theWorld.playSound((double)par3 + 0.5D, (double)par4 + 0.5D, (double)par5 + 0.5D, "mob.ghast.fireball", 10.0F, (var7.nextFloat() - var7.nextFloat()) * 0.2F + 1.0F, false);
				break;

			case 1009:
				this.theWorld.playSound((double)par3 + 0.5D, (double)par4 + 0.5D, (double)par5 + 0.5D, "mob.ghast.fireball", 2.0F, (var7.nextFloat() - var7.nextFloat()) * 0.2F + 1.0F, false);
				break;

			case 1010:
				this.theWorld.playSound((double)par3 + 0.5D, (double)par4 + 0.5D, (double)par5 + 0.5D, "mob.zombie.wood", 2.0F, (var7.nextFloat() - var7.nextFloat()) * 0.2F + 1.0F, false);
				break;

			case 1011:
				this.theWorld.playSound((double)par3 + 0.5D, (double)par4 + 0.5D, (double)par5 + 0.5D, "mob.zombie.metal", 2.0F, (var7.nextFloat() - var7.nextFloat()) * 0.2F + 1.0F, false);
				break;

			case 1012:
				this.theWorld.playSound((double)par3 + 0.5D, (double)par4 + 0.5D, (double)par5 + 0.5D, "mob.zombie.woodbreak", 2.0F, (var7.nextFloat() - var7.nextFloat()) * 0.2F + 1.0F, false);
				break;

			case 1014:
				this.theWorld.playSound((double)par3 + 0.5D, (double)par4 + 0.5D, (double)par5 + 0.5D, "mob.wither.shoot", 2.0F, (var7.nextFloat() - var7.nextFloat()) * 0.2F + 1.0F, false);
				break;

			case 1015:
				this.theWorld.playSound((double)par3 + 0.5D, (double)par4 + 0.5D, (double)par5 + 0.5D, "mob.bat.takeoff", 0.05F, (var7.nextFloat() - var7.nextFloat()) * 0.2F + 1.0F, false);
				break;

			case 1016:
				this.theWorld.playSound((double)par3 + 0.5D, (double)par4 + 0.5D, (double)par5 + 0.5D, "mob.zombie.infect", 2.0F, (var7.nextFloat() - var7.nextFloat()) * 0.2F + 1.0F, false);
				break;

			case 1017:
				this.theWorld.playSound((double)par3 + 0.5D, (double)par4 + 0.5D, (double)par5 + 0.5D, "mob.zombie.unfect", 2.0F, (var7.nextFloat() - var7.nextFloat()) * 0.2F + 1.0F, false);
				break;

			case 1020:
				this.theWorld.playSound((double)((float)par3 + 0.5F), (double)((float)par4 + 0.5F), (double)((float)par5 + 0.5F), "random.anvil_break", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
				break;

			case 1021:
				this.theWorld.playSound((double)((float)par3 + 0.5F), (double)((float)par4 + 0.5F), (double)((float)par5 + 0.5F), "random.anvil_use", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
				break;

			case 1022:
				this.theWorld.playSound((double)((float)par3 + 0.5F), (double)((float)par4 + 0.5F), (double)((float)par5 + 0.5F), "random.anvil_land", 0.3F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
				break;

			case 2000:
				int var33 = par6 % 3 - 1;
				int var9 = par6 / 3 % 3 - 1;
				var10 = (double)par3 + (double)var33 * 0.6D + 0.5D;
				var12 = (double)par4 + 0.5D;
				double var34 = (double)par5 + (double)var9 * 0.6D + 0.5D;

				for (int var35 = 0; var35 < 10; ++var35) {
					double var37 = var7.nextDouble() * 0.2D + 0.01D;
					double var38 = var10 + (double)var33 * 0.01D + (var7.nextDouble() - 0.5D) * (double)var9 * 0.5D;
					var39 = var12 + (var7.nextDouble() - 0.5D) * 0.5D;
					var23 = var34 + (double)var9 * 0.01D + (var7.nextDouble() - 0.5D) * (double)var33 * 0.5D;
					var25 = (double)var33 * var37 + var7.nextGaussian() * 0.01D;
					var27 = -0.03D + var7.nextGaussian() * 0.01D;
					var29 = (double)var9 * var37 + var7.nextGaussian() * 0.01D;
					this.spawnParticle("smoke", var38, var39, var23, var25, var27, var29);
				}

				return;

			case 2001:
				var20 = par6 & 4095;

				if (var20 > 0) {
					Block var40 = Block.blocksList[var20];
					this.mc.sndManager.playSound(var40.stepSound.getBreakSound(), (float)par3 + 0.5F, (float)par4 + 0.5F, (float)par5 + 0.5F, (var40.stepSound.getVolume() + 1.0F) / 2.0F, var40.stepSound.getPitch() * 0.8F);
				}

				this.mc.effectRenderer.addBlockDestroyEffects(par3, par4, par5, par6 & 4095, par6 >> 12 & 255);
				break;

			case 2002:
				var8 = (double)par3;
				var10 = (double)par4;
				var12 = (double)par5;
				var14 = "iconcrack_" + Item.potion.itemID;

				for (var15 = 0; var15 < 8; ++var15) {
					this.spawnParticle(var14, var8, var10, var12, var7.nextGaussian() * 0.15D, var7.nextDouble() * 0.2D, var7.nextGaussian() * 0.15D);
				}

				var15 = Item.potion.getColorFromDamage(par6);
				float var16 = (float)(var15 >> 16 & 255) / 255.0F;
				float var17 = (float)(var15 >> 8 & 255) / 255.0F;
				float var18 = (float)(var15 >> 0 & 255) / 255.0F;
				String var19 = "spell";

				if (Item.potion.isEffectInstant(par6)) {
					var19 = "instantSpell";
				}

				for (var20 = 0; var20 < 100; ++var20) {
					var39 = var7.nextDouble() * 4.0D;
					var23 = var7.nextDouble() * Math.PI * 2.0D;
					var25 = Math.cos(var23) * var39;
					var27 = 0.01D + var7.nextDouble() * 0.5D;
					var29 = Math.sin(var23) * var39;
					EntityFX var31 = this.doSpawnParticle(var19, var8 + var25 * 0.1D, var10 + 0.3D, var12 + var29 * 0.1D, var25, var27, var29);

					if (var31 != null) {
						float var32 = 0.75F + var7.nextFloat() * 0.25F;
						var31.setRBGColorF(var16 * var32, var17 * var32, var18 * var32);
						var31.multiplyVelocity((float)var39);
					}
				}

				this.theWorld.playSound((double)par3 + 0.5D, (double)par4 + 0.5D, (double)par5 + 0.5D, "random.glass", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
				break;

			case 2003:
				var8 = (double)par3 + 0.5D;
				var10 = (double)par4;
				var12 = (double)par5 + 0.5D;
				var14 = "iconcrack_" + Item.eyeOfEnder.itemID;

				for (var15 = 0; var15 < 8; ++var15) {
					this.spawnParticle(var14, var8, var10, var12, var7.nextGaussian() * 0.15D, var7.nextDouble() * 0.2D, var7.nextGaussian() * 0.15D);
				}

				for (double var36 = 0.0D; var36 < (Math.PI * 2D); var36 += 0.15707963267948966D) {
					this.spawnParticle("portal", var8 + Math.cos(var36) * 5.0D, var10 - 0.4D, var12 + Math.sin(var36) * 5.0D, Math.cos(var36) * -5.0D, 0.0D, Math.sin(var36) * -5.0D);
					this.spawnParticle("portal", var8 + Math.cos(var36) * 5.0D, var10 - 0.4D, var12 + Math.sin(var36) * 5.0D, Math.cos(var36) * -7.0D, 0.0D, Math.sin(var36) * -7.0D);
				}

				return;

			case 2004:
				for (int var21 = 0; var21 < 20; ++var21) {
					double var22 = (double)par3 + 0.5D + ((double)this.theWorld.rand.nextFloat() - 0.5D) * 2.0D;
					double var24 = (double)par4 + 0.5D + ((double)this.theWorld.rand.nextFloat() - 0.5D) * 2.0D;
					double var26 = (double)par5 + 0.5D + ((double)this.theWorld.rand.nextFloat() - 0.5D) * 2.0D;
					this.theWorld.spawnParticle("smoke", var22, var24, var26, 0.0D, 0.0D, 0.0D);
					this.theWorld.spawnParticle("flame", var22, var24, var26, 0.0D, 0.0D, 0.0D);
				}

				return;

			case 2005:
				ItemDye.func_96603_a(this.theWorld, par3, par4, par5, par6);
		}
	}

	/**
	 * Starts (or continues) destroying a block with given ID at the given coordinates for the given partially destroyed
	 * value
	 */
	public void destroyBlockPartially(int par1, int par2, int par3, int par4, int par5) {
		if (par5 >= 0 && par5 < 10) {
			DestroyBlockProgress var6 = (DestroyBlockProgress)this.damagedBlocks.get(Integer.valueOf(par1));

			if (var6 == null || var6.getPartialBlockX() != par2 || var6.getPartialBlockY() != par3 || var6.getPartialBlockZ() != par4) {
				var6 = new DestroyBlockProgress(par1, par2, par3, par4);
				this.damagedBlocks.put(Integer.valueOf(par1), var6);
			}

			var6.setPartialBlockDamage(par5);
			var6.setCloudUpdateTick(this.cloudTickCounter);
		} else {
			this.damagedBlocks.remove(Integer.valueOf(par1));
		}
	}

	public void registerDestroyBlockIcons(IconRegister par1IconRegister) {
		this.destroyBlockIcons = new Icon[10];

		for (int var2 = 0; var2 < this.destroyBlockIcons.length; ++var2) {
			this.destroyBlockIcons[var2] = par1IconRegister.registerIcon("destroy_" + var2);
		}
	}

	// Spout Start
	// TODO: Some methods here may not be called.
	public int renderAllSortedRenderers(int var1, double var2) {
		return this.renderSortedRenderers(0, this.sortedWorldRenderers.length, var1, var2);
	}

	public void updateAllRenderers() {
		if (this.worldRenderers != null) {
			for (int var1 = 0; var1 < this.worldRenderers.length; ++var1) {
				if (/*this.worldRenderers[var1].isChunkLit && */!this.worldRenderers[var1].needsUpdate) {
					this.worldRenderersToUpdate.add(this.worldRenderers[var1]);
					this.worldRenderers[var1].markDirty();
				}
			}
		}
	}

	public void setAllRenderesVisible() {
		if (this.worldRenderers != null) {
			for (int var1 = 0; var1 < this.worldRenderers.length; ++var1) {
				this.worldRenderers[var1].isVisible = true;
			}

		}
	}

	private boolean isMoving(EntityLiving entity, long time) {
		if (this.isMovingNow(entity)) {
			this.lastMovedTime = System.currentTimeMillis();
			return true;
		} else {
			return System.currentTimeMillis() - this.lastMovedTime < time;
		}
	}

	private boolean isMovingNow(EntityLiving var1) {
		double var2 = 0.0010D;
		return var1.isJumping?true:(var1.isSneaking()?true:((double)var1.prevSwingProgress > var2?true:(this.mc.mouseHelper.deltaX != 0?true:(this.mc.mouseHelper.deltaY != 0?true:(Math.abs(var1.posX - var1.prevPosX) > var2?true:(Math.abs(var1.posY - var1.prevPosY) > var2?true:Math.abs(var1.posZ - var1.prevPosZ) > var2))))));
	}

	public void updateRenderer(int x, int y, int z) {
		int chunkX = MathHelper.bucketInt(x, 16);
		int chunkY = MathHelper.bucketInt(y, 16);
		int chunkZ = MathHelper.bucketInt(z, 16);

		int cx = chunkX % this.renderChunksWide;
		if (cx < 0) {
			cx += this.renderChunksWide;
		}

		int cy = chunkY % this.renderChunksTall;
		if (cy < 0) {
			cy += this.renderChunksTall;
		}

		int cz = chunkZ % this.renderChunksDeep;
		if (cz < 0) {
			cz += this.renderChunksDeep;
		}

		int index = (cz * this.renderChunksTall + cy) * this.renderChunksWide + cx;
		WorldRenderer renderer = this.worldRenderers[index];
		if (renderer.needsUpdate) {
			renderer.updateRenderer();
			renderer.needsUpdate = false;
		}

	}

	public void prepareAO() {
		if (this.occlusionEnabled && Configuration.ambientOcclusion) {
			byte var3 = 64;
			byte var4 = 64;
			this.occlusionResult.clear();
			this.glOcclusionQueryBase = GLAllocation.createDirectIntBuffer(var3 * var3 * var4);
			this.glOcclusionQueryBase.clear();
			this.glOcclusionQueryBase.position(0);
			this.glOcclusionQueryBase.limit(var3 * var3 * var4);
			ARBOcclusionQuery.glGenQueriesARB(this.glOcclusionQueryBase);
		}
	}
	// Spout End
}
