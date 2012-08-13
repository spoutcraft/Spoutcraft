package net.minecraft.src;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

// Spout start
import org.spoutcraft.client.SpoutcraftWorld;
import org.spoutcraft.client.config.ConfigReader;
import org.spoutcraft.client.entity.EntityText;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.material.CustomBlock;
import org.spoutcraft.spoutcraftapi.material.MaterialData;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;

//Spout End
public abstract class World implements IBlockAccess {

	/**
	 * boolean; if true updates scheduled by scheduleBlockUpdate happen immediately
	 */
	public boolean scheduledUpdatesAreImmediate = false;

	/** A list of all Entities in all currently-loaded chunks */
	public List loadedEntityList = new ArrayList();
	protected List unloadedEntityList = new ArrayList();

	/** A list of all TileEntities in all currently-loaded chunks */
	public List loadedTileEntityList = new ArrayList();
	private List addedTileEntityList = new ArrayList();

	/** Entities marked for removal. */
	private List entityRemoval = new ArrayList();

	/** Array list of players in the world. */
	public List playerEntities = new ArrayList();

	/** a list of all the lightning entities */
	public List weatherEffects = new ArrayList();
	private long cloudColour = 16777215L;

	/** How much light is subtracted from full daylight */
	public int skylightSubtracted = 0;

	/**
	 * Contains the current Linear Congruential Generator seed for block updates. Used with an A value of 3 and a C value
	 * of 0x3c6ef35f, producing a highly planar series of values ill-suited for choosing random blocks in a 16x128x16
	 * field.
	 */
	protected int updateLCG = (new Random()).nextInt();

	/**
	 * magic number used to generate fast random numbers for 3d distribution within a chunk
	 */
	protected final int DIST_HASH_MAGIC = 1013904223;
	protected float prevRainingStrength;
	protected float rainingStrength;
	protected float prevThunderingStrength;
	protected float thunderingStrength;

	/**
	 * Set to 2 whenever a lightning bolt is generated in SSP. Decrements if > 0 in updateWeather(). Value appears to be
	 * unused.
	 */
	protected int lastLightningBolt = 0;

	/**
	 * If > 0, the sky and skylight colors are illuminated by a lightning flash
	 */
	public int lightningFlash = 0;

	/** true while the world is editing blocks */
	public boolean editingBlocks = false;

	/** Option > Difficulty setting (0 - 3) */
	public int difficultySetting;

	/** RNG for World. */
	public Random rand = new Random();

	/** The WorldProvider instance that World uses. */
	public final WorldProvider worldProvider;
	protected List worldAccesses = new ArrayList();
	public IChunkProvider chunkProvider; // Spout protected -> public
	protected final ISaveHandler saveHandler;
	public WorldInfo worldInfo; // Spout protected -> public
	public boolean findingSpawnPoint;
	public MapStorage mapStorage;
	public final VillageCollection villageCollectionObj = new VillageCollection(this);
	protected final VillageSiege villageSiegeObj = new VillageSiege(this);
	public final Profiler field_72984_F;
	private ArrayList collidingBoundingBoxes = new ArrayList();
	private boolean scanningTileEntities;
	public boolean spawnHostileMobs = true; // Spout private -> public
	public boolean spawnPeacefulMobs = true; // Spout private -> public
	protected Set activeChunkSet = new HashSet();
	private int ambientTickCountdown;
	int[] lightUpdateBlockList;
	private List entitiesWithinAABBExcludingEntity;
	public boolean isRemote;
	//Spout start
	public SpoutcraftWorld world;
	public TIntObjectHashMap<String> customTitles = new TIntObjectHashMap<String>(200);
	//Spout end

	public BiomeGenBase getBiomeGenForCoords(int par1, int par2) {
		if (this.blockExists(par1, 0, par2)) {
			Chunk var3 = this.getChunkFromBlockCoords(par1, par2);
			if (var3 != null) {
				return var3.getBiomeGenForWorldCoords(par1 & 15, par2 & 15, this.worldProvider.worldChunkMgr);
			}
		}

		return this.worldProvider.worldChunkMgr.getBiomeGenAt(par1, par2);
	}

	public WorldChunkManager getWorldChunkManager() {
		return this.worldProvider.worldChunkMgr;
	}

	public World(ISaveHandler par1ISaveHandler, String par2Str, WorldSettings par3WorldSettings, WorldProvider par4WorldProvider, Profiler par5Profiler) {
		this.ambientTickCountdown = this.rand.nextInt(12000);
		this.lightUpdateBlockList = new int[32768];
		this.entitiesWithinAABBExcludingEntity = new ArrayList();
		this.isRemote = false;
		this.saveHandler = par1ISaveHandler;
		this.mapStorage = new MapStorage(par1ISaveHandler);
		this.worldInfo = par1ISaveHandler.loadWorldInfo();
		this.field_72984_F = par5Profiler;
		if (par4WorldProvider != null) {
			this.worldProvider = par4WorldProvider;
		} else if (this.worldInfo != null && this.worldInfo.getDimension() != 0) {
			this.worldProvider = WorldProvider.getProviderForDimension(this.worldInfo.getDimension());
		} else {
			this.worldProvider = WorldProvider.getProviderForDimension(0);
		}
		if (this.worldInfo == null) {
			this.worldInfo = new WorldInfo(par3WorldSettings, par2Str);
		} else {
			this.worldInfo.setWorldName(par2Str);
		}

		this.worldProvider.registerWorld(this);
		this.chunkProvider = this.createChunkProvider();

		if (!this.worldInfo.func_76070_v()) {
			this.func_72963_a(par3WorldSettings);
			this.worldInfo.func_76091_d(true);
		}

		this.calculateInitialSkylight();
		this.calculateInitialWeather();
		//Spout start
		world = new SpoutcraftWorld(this);
		//Spout end
	}

	protected abstract IChunkProvider createChunkProvider();

	protected void func_72963_a(WorldSettings par1WorldSettings) {
		this.worldInfo.func_76091_d(true);
	}

	public void setSpawnLocation() {
		this.func_72950_A(8, 64, 8);
	}

	public int getFirstUncoveredBlock(int par1, int par2) {
		int var3;
		for (var3 = 63; !this.isAirBlock(par1, var3 + 1, par2); ++var3) {
			;
		}

		return this.getBlockId(par1, var3, par2);
	}

	public int getBlockId(int par1, int par2, int par3) {
		return par1 >= -30000000 && par3 >= -30000000 && par1 < 30000000 && par3 < 30000000 ? (par2 < 0 ? 0 : (par2 >= 256 ? 0 : this.getChunkFromChunkCoords(par1 >> 4, par3 >> 4).getBlockID(par1 & 15, par2, par3 & 15))) : 0;
	}

	public int getBlockLightOpacity(int par1, int par2, int par3) {
		return par1 >= -30000000 && par3 >= -30000000 && par1 < 30000000 && par3 < 30000000 ? (par2 < 0 ? 0 : (par2 >= 256 ? 0 : this.getChunkFromChunkCoords(par1 >> 4, par3 >> 4).getBlockLightOpacity(par1 & 15, par2, par3 & 15))) : 0;
	}

	public boolean isAirBlock(int par1, int par2, int par3) {
		return this.getBlockId(par1, par2, par3) == 0;
	}

	public boolean func_72927_d(int par1, int par2, int par3) {
		int var4 = this.getBlockId(par1, par2, par3);
		return Block.blocksList[var4] != null && Block.blocksList[var4].hasTileEntity();
	}

	public boolean blockExists(int par1, int par2, int par3) {
		return par2 >= 0 && par2 < 256 ? this.chunkExists(par1 >> 4, par3 >> 4) : false;
	}

	public boolean doChunksNearChunkExist(int par1, int par2, int par3, int par4) {
		return this.checkChunksExist(par1 - par4, par2 - par4, par3 - par4, par1 + par4, par2 + par4, par3 + par4);
	}

	public boolean checkChunksExist(int par1, int par2, int par3, int par4, int par5, int par6) {
		if (par5 >= 0 && par2 < 256) {
			par1 >>= 4;
			par3 >>= 4;
			par4 >>= 4;
			par6 >>= 4;

			for (int var7 = par1; var7 <= par4; ++var7) {
				for (int var8 = par3; var8 <= par6; ++var8) {
					if (!this.chunkExists(var7, var8)) {
						return false;
					}
				}
			}

			return true;
		} else {
			return false;
		}
	}

	protected boolean chunkExists(int par1, int par2) {
		return this.chunkProvider.chunkExists(par1, par2);
	}

	public Chunk getChunkFromBlockCoords(int par1, int par2) {
		return this.getChunkFromChunkCoords(par1 >> 4, par2 >> 4);
	}

	public Chunk getChunkFromChunkCoords(int par1, int par2) {
		return this.chunkProvider.provideChunk(par1, par2);
	}

	public boolean setBlockAndMetadata(int par1, int par2, int par3, int par4, int par5) {
		return this.func_72930_a(par1, par2, par3, par4, par5, true);
	}

	public boolean func_72930_a(int par1, int par2, int par3, int par4, int par5, boolean par6) {
		if (par1 >= -30000000 && par3 >= -30000000 && par1 < 30000000 && par3 < 30000000) {
			if (par2 < 0) {
				return false;
			} else if (par2 >= 256) {
				return false;
			} else {
				Chunk var7 = this.getChunkFromChunkCoords(par1 >> 4, par3 >> 4);
				boolean var8 = var7.setBlockIDWithMetadata(par1 & 15, par2, par3 & 15, par4, par5);
				this.field_72984_F.startSection("checkLight");
				this.updateAllLightTypes(par1, par2, par3);
				this.field_72984_F.endSection();

				if (par6 && var8 && (this.isRemote || var7.deferRender)) {
					this.markBlockNeedsUpdate(par1, par2, par3);
				}

				return var8;
			}
		} else {
			return false;
		}
	}

	public boolean setBlock(int par1, int par2, int par3, int par4) {
		if (par1 >= -30000000 && par3 >= -30000000 && par1 < 30000000 && par3 < 30000000) {
			if (par2 < 0) {
				return false;
			} else if (par2 >= 256) {
				return false;
			} else {
				Chunk var5 = this.getChunkFromChunkCoords(par1 >> 4, par3 >> 4);
				boolean var6 = var5.setBlockID(par1 & 15, par2, par3 & 15, par4);
				this.field_72984_F.startSection("checkLight");
				this.updateAllLightTypes(par1, par2, par3);
				this.field_72984_F.endSection();

				if (var6 && (this.isRemote || var5.deferRender)) {
					this.markBlockNeedsUpdate(par1, par2, par3);
				}

				return var6;
			}
		} else {
			return false;
		}
	}

	public Material getBlockMaterial(int par1, int par2, int par3) {
		int var4 = this.getBlockId(par1, par2, par3);
		return var4 == 0 ? Material.air : Block.blocksList[var4].blockMaterial;
	}

	public int getBlockMetadata(int par1, int par2, int par3) {
		if (par1 >= -30000000 && par3 >= -30000000 && par1 < 30000000 && par3 < 30000000) {
			if (par2 < 0) {
				return 0;
			} else if (par2 >= 256) {
				return 0;
			} else {
				Chunk var4 = this.getChunkFromChunkCoords(par1 >> 4, par3 >> 4);
				par1 &= 15;
				par3 &= 15;
				return var4.getBlockMetadata(par1, par2, par3);
			}
		} else {
			return 0;
		}
	}

	public void setBlockMetadataWithNotify(int par1, int par2, int par3, int par4) {
		if (this.setBlockMetadata(par1, par2, par3, par4)) {
			this.notifyBlockChange(par1, par2, par3, this.getBlockId(par1, par2, par3));
		}
	}

	public boolean setBlockMetadata(int par1, int par2, int par3, int par4) {
		if (par1 >= -30000000 && par3 >= -30000000 && par1 < 30000000 && par3 < 30000000) {
			if (par2 < 0) {
				return false;
			} else if (par2 >= 256) {
				return false;
			} else {
				Chunk var5 = this.getChunkFromChunkCoords(par1 >> 4, par3 >> 4);
				int var6 = par1 & 15;
				int var7 = par3 & 15;
				boolean var8 = var5.setBlockMetadata(var6, par2, var7, par4);

				if (var8 && (this.isRemote || var5.deferRender && Block.requiresSelfNotify[var5.getBlockID(var6, par2, var7) & 4095])) {
					this.markBlockNeedsUpdate(par1, par2, par3);
				}

				return var8;
			}
		} else {
			return false;
		}
	}

	public boolean setBlockWithNotify(int par1, int par2, int par3, int par4) {
		if (this.setBlock(par1, par2, par3, par4)) {
			this.notifyBlockChange(par1, par2, par3, par4);
			return true;
		} else {
			return false;
		}
	}

	public boolean setBlockAndMetadataWithNotify(int par1, int par2, int par3, int par4, int par5) {
		if (this.setBlockAndMetadata(par1, par2, par3, par4, par5)) {
			this.notifyBlockChange(par1, par2, par3, par4);
			return true;
		} else {
			return false;
		}
	}

	public void markBlockNeedsUpdate(int par1, int par2, int par3) {
		Iterator var4 = this.worldAccesses.iterator();

		while (var4.hasNext()) {
			IWorldAccess var5 = (IWorldAccess)var4.next();
			var5.markBlockNeedsUpdate(par1, par2, par3);
		}
	}

	public void notifyBlockChange(int par1, int par2, int par3, int par4) {
		this.markBlockNeedsUpdate(par1, par2, par3);
		this.notifyBlocksOfNeighborChange(par1, par2, par3, par4);
	}

	public void markBlocksDirtyVertical(int par1, int par2, int par3, int par4) {
		int var5;
		if (par3 > par4) {
			var5 = par4;
			par4 = par3;
			par3 = var5;
		}

		if (!this.worldProvider.hasNoSky) {
			for (var5 = par3; var5 <= par4; ++var5) {
				this.updateLightByType(EnumSkyBlock.Sky, par1, var5, par2);
			}
		}

		this.markBlocksDirty(par1, par3, par2, par1, par4, par2);
	}

	public void markBlockAsNeedsUpdate(int par1, int par2, int par3) {
		Iterator var4 = this.worldAccesses.iterator();

		while (var4.hasNext()) {
			IWorldAccess var5 = (IWorldAccess)var4.next();
			var5.markBlockRangeNeedsUpdate(par1, par2, par3, par1, par2, par3);
		}
	}

	public void markBlocksDirty(int par1, int par2, int par3, int par4, int par5, int par6) {
		Iterator var7 = this.worldAccesses.iterator();

		while (var7.hasNext()) {
			IWorldAccess var8 = (IWorldAccess)var7.next();
			var8.markBlockRangeNeedsUpdate(par1, par2, par3, par4, par5, par6);
		}

	}

	public void notifyBlocksOfNeighborChange(int par1, int par2, int par3, int par4) {
		this.notifyBlockOfNeighborChange(par1 - 1, par2, par3, par4);
		this.notifyBlockOfNeighborChange(par1 + 1, par2, par3, par4);
		this.notifyBlockOfNeighborChange(par1, par2 - 1, par3, par4);
		this.notifyBlockOfNeighborChange(par1, par2 + 1, par3, par4);
		this.notifyBlockOfNeighborChange(par1, par2, par3 - 1, par4);
		this.notifyBlockOfNeighborChange(par1, par2, par3 + 1, par4);
	}

	private void notifyBlockOfNeighborChange(int par1, int par2, int par3, int par4) {
		if (!this.editingBlocks && !this.isRemote) {
			Block var5 = Block.blocksList[this.getBlockId(par1, par2, par3)];
			if (var5 != null) {
				var5.onNeighborBlockChange(this, par1, par2, par3, par4);
			}
		}
	}

	public boolean canBlockSeeTheSky(int par1, int par2, int par3) {
		return this.getChunkFromChunkCoords(par1 >> 4, par3 >> 4).canBlockSeeTheSky(par1 & 15, par2, par3 & 15);
	}

	public int getFullBlockLightValue(int par1, int par2, int par3) {
		if (par2 < 0) {
			return 0;
		} else {
			if (par2 >= 256) {
				par2 = 255;
			}

			return this.getChunkFromChunkCoords(par1 >> 4, par3 >> 4).getBlockLightValue(par1 & 15, par2, par3 & 15, 0);
		}
	}

	public int getBlockLightValue(int par1, int par2, int par3) {
		return this.getBlockLightValue_do(par1, par2, par3, true);
	}

	public int getBlockLightValue_do(int par1, int par2, int par3, boolean par4) {
		if (par1 >= -30000000 && par3 >= -30000000 && par1 < 30000000 && par3 < 30000000) {
			if (par4) {
				int var5 = this.getBlockId(par1, par2, par3);
				if (var5 == Block.field_72079_ak.blockID || var5 == Block.field_72092_bO.blockID || var5 == Block.tilledField.blockID || var5 == Block.stairCompactCobblestone.blockID || var5 == Block.stairCompactPlanks.blockID) {
					int var6 = this.getBlockLightValue_do(par1, par2 + 1, par3, false);
					int var7 = this.getBlockLightValue_do(par1 + 1, par2, par3, false);
					int var8 = this.getBlockLightValue_do(par1 - 1, par2, par3, false);
					int var9 = this.getBlockLightValue_do(par1, par2, par3 + 1, false);
					int var10 = this.getBlockLightValue_do(par1, par2, par3 - 1, false);
					if (var7 > var6) {
						var6 = var7;
					}

					if (var8 > var6) {
						var6 = var8;
					}

					if (var9 > var6) {
						var6 = var9;
					}

					if (var10 > var6) {
						var6 = var10;
					}

					return var6;
				}
			}

			if (par2 < 0) {
				return 0;
			} else {
				if (par2 >= 256) {
					par2 = 255;
				}

				Chunk var11 = this.getChunkFromChunkCoords(par1 >> 4, par3 >> 4);
				par1 &= 15;
				par3 &= 15;
				return var11.getBlockLightValue(par1, par2, par3, this.skylightSubtracted);
			}
		} else {
			return 15;
		}
	}

	public int getHeightValue(int par1, int par2) {
		if (par1 >= -30000000 && par2 >= -30000000 && par1 < 30000000 && par2 < 30000000) {
			if (!this.chunkExists(par1 >> 4, par2 >> 4)) {
				return 0;
			} else {
				Chunk var3 = this.getChunkFromChunkCoords(par1 >> 4, par2 >> 4);
				return var3.getHeightValue(par1 & 15, par2 & 15);
			}
		} else {
			return 0;
		}
	}

	public int getSkyBlockTypeBrightness(EnumSkyBlock par1EnumSkyBlock, int par2, int par3, int par4) {
		if (this.worldProvider.hasNoSky && par1EnumSkyBlock == EnumSkyBlock.Sky) {
			return 0;
		} else {
			if (par3 < 0) {
				par3 = 0;
			}

			if (par3 >= 256) {
				return par1EnumSkyBlock.defaultLightValue;
			} else if (par2 >= -30000000 && par4 >= -30000000 && par2 < 30000000 && par4 < 30000000) {
				int var5 = par2 >> 4;
				int var6 = par4 >> 4;
				if (!this.chunkExists(var5, var6)) {
					return par1EnumSkyBlock.defaultLightValue;
				} else if (Block.useNeighborBrightness[this.getBlockId(par2, par3, par4)]) {
					int var12 = this.getSavedLightValue(par1EnumSkyBlock, par2, par3 + 1, par4);
					int var8 = this.getSavedLightValue(par1EnumSkyBlock, par2 + 1, par3, par4);
					int var9 = this.getSavedLightValue(par1EnumSkyBlock, par2 - 1, par3, par4);
					int var10 = this.getSavedLightValue(par1EnumSkyBlock, par2, par3, par4 + 1);
					int var11 = this.getSavedLightValue(par1EnumSkyBlock, par2, par3, par4 - 1);
					if (var8 > var12) {
						var12 = var8;
					}

					if (var9 > var12) {
						var12 = var9;
					}

					if (var10 > var12) {
						var12 = var10;
					}

					if (var11 > var12) {
						var12 = var11;
					}

					return var12;
				} else {
					Chunk var7 = this.getChunkFromChunkCoords(var5, var6);
					return var7.getSavedLightValue(par1EnumSkyBlock, par2 & 15, par3, par4 & 15);
				}
			} else {
				return par1EnumSkyBlock.defaultLightValue;
			}
		}
	}

	public int getSavedLightValue(EnumSkyBlock par1EnumSkyBlock, int par2, int par3, int par4) {
		if (par3 < 0) {
			par3 = 0;
		}

		if (par3 >= 256) {
			par3 = 255;
		}

		if (par2 >= -30000000 && par4 >= -30000000 && par2 < 30000000 && par4 < 30000000) {
			int var5 = par2 >> 4;
			int var6 = par4 >> 4;
			if (!this.chunkExists(var5, var6)) {
				return par1EnumSkyBlock.defaultLightValue;
			} else {
				Chunk var7 = this.getChunkFromChunkCoords(var5, var6);
				return var7.getSavedLightValue(par1EnumSkyBlock, par2 & 15, par3, par4 & 15);
			}
		} else {
			return par1EnumSkyBlock.defaultLightValue;
		}
	}

	public void setLightValue(EnumSkyBlock par1EnumSkyBlock, int par2, int par3, int par4, int par5) {
		if (par2 >= -30000000 && par4 >= -30000000 && par2 < 30000000 && par4 < 30000000) {
			if (par3 >= 0) {
				if (par3 < 256) {
					if (this.chunkExists(par2 >> 4, par4 >> 4)) {
						Chunk var6 = this.getChunkFromChunkCoords(par2 >> 4, par4 >> 4);
						var6.setLightValue(par1EnumSkyBlock, par2 & 15, par3, par4 & 15, par5);
						Iterator var7 = this.worldAccesses.iterator();

						while (var7.hasNext()) {
							IWorldAccess var8 = (IWorldAccess)var7.next();
							var8.markBlockNeedsUpdate2(par2, par3, par4);
						}
					}
				}
			}
		}
	}

	public void func_72902_n(int par1, int par2, int par3) {
		Iterator var4 = this.worldAccesses.iterator();

		while (var4.hasNext()) {
			IWorldAccess var5 = (IWorldAccess)var4.next();
			var5.markBlockNeedsUpdate2(par1, par2, par3);
		}
	}

	public int getLightBrightnessForSkyBlocks(int par1, int par2, int par3, int par4) {
		int var5 = this.getSkyBlockTypeBrightness(EnumSkyBlock.Sky, par1, par2, par3);
		int var6 = this.getSkyBlockTypeBrightness(EnumSkyBlock.Block, par1, par2, par3);
		if (var6 < par4) {
			var6 = par4;
		}

		return var5 << 20 | var6 << 4;
	}

	public float getBrightness(int par1, int par2, int par3, int par4) {
		int var5 = this.getBlockLightValue(par1, par2, par3);
		if (var5 < par4) {
			var5 = par4;
		}

		return this.worldProvider.lightBrightnessTable[var5];
	}

	public float getLightBrightness(int par1, int par2, int par3) {
		return this.worldProvider.lightBrightnessTable[this.getBlockLightValue(par1, par2, par3)];
	}

	public boolean isDaytime() {
		return this.skylightSubtracted < 4;
	}

	public MovingObjectPosition rayTraceBlocks(Vec3 par1Vec3, Vec3 par2Vec3) {
		return this.rayTraceBlocks_do_do(par1Vec3, par2Vec3, false, false);
	}

	public MovingObjectPosition rayTraceBlocks_do(Vec3 par1Vec3, Vec3 par2Vec3, boolean par3) {
		return this.rayTraceBlocks_do_do(par1Vec3, par2Vec3, par3, false);
	}

	public MovingObjectPosition rayTraceBlocks_do_do(Vec3 par1Vec3, Vec3 par2Vec3, boolean par3, boolean par4) {
		if (!Double.isNaN(par1Vec3.xCoord) && !Double.isNaN(par1Vec3.yCoord) && !Double.isNaN(par1Vec3.zCoord)) {
			if (!Double.isNaN(par2Vec3.xCoord) && !Double.isNaN(par2Vec3.yCoord) && !Double.isNaN(par2Vec3.zCoord)) {
				int var5 = MathHelper.floor_double(par2Vec3.xCoord);
				int var6 = MathHelper.floor_double(par2Vec3.yCoord);
				int var7 = MathHelper.floor_double(par2Vec3.zCoord);
				int var8 = MathHelper.floor_double(par1Vec3.xCoord);
				int var9 = MathHelper.floor_double(par1Vec3.yCoord);
				int var10 = MathHelper.floor_double(par1Vec3.zCoord);
				int var11 = this.getBlockId(var8, var9, var10);
				int var12 = this.getBlockMetadata(var8, var9, var10);
				Block var13 = Block.blocksList[var11];
				if ((!par4 || var13 == null || var13.getCollisionBoundingBoxFromPool(this, var8, var9, var10) != null) && var11 > 0 && var13.canCollideCheck(var12, par3)) {
					MovingObjectPosition var14 = var13.collisionRayTrace(this, var8, var9, var10, par1Vec3, par2Vec3);
					if (var14 != null) {
						return var14;
					}
				}

				var11 = 200;

				while (var11-- >= 0) {
					if (Double.isNaN(par1Vec3.xCoord) || Double.isNaN(par1Vec3.yCoord) || Double.isNaN(par1Vec3.zCoord)) {
						return null;
					}

					if (var8 == var5 && var9 == var6 && var10 == var7) {
						return null;
					}

					boolean var39 = true;
					boolean var40 = true;
					boolean var41 = true;
					double var15 = 999.0D;
					double var17 = 999.0D;
					double var19 = 999.0D;
					if (var5 > var8) {
						var15 = (double)var8 + 1.0D;
					} else if (var5 < var8) {
						var15 = (double)var8 + 0.0D;
					} else {
						var39 = false;
					}

					if (var6 > var9) {
						var17 = (double)var9 + 1.0D;
					} else if (var6 < var9) {
						var17 = (double)var9 + 0.0D;
					} else {
						var40 = false;
					}

					if (var7 > var10) {
						var19 = (double)var10 + 1.0D;
					} else if (var7 < var10) {
						var19 = (double)var10 + 0.0D;
					} else {
						var41 = false;
					}

					double var21 = 999.0D;
					double var23 = 999.0D;
					double var25 = 999.0D;
					double var27 = par2Vec3.xCoord - par1Vec3.xCoord;
					double var29 = par2Vec3.yCoord - par1Vec3.yCoord;
					double var31 = par2Vec3.zCoord - par1Vec3.zCoord;
					if (var39) {
						var21 = (var15 - par1Vec3.xCoord) / var27;
					}

					if (var40) {
						var23 = (var17 - par1Vec3.yCoord) / var29;
					}

					if (var41) {
						var25 = (var19 - par1Vec3.zCoord) / var31;
					}

					boolean var33 = false;
					byte var42;
					if (var21 < var23 && var21 < var25) {
						if (var5 > var8) {
							var42 = 4;
						} else {
							var42 = 5;
						}

						par1Vec3.xCoord = var15;
						par1Vec3.yCoord += var29 * var21;
						par1Vec3.zCoord += var31 * var21;
					} else if (var23 < var25) {
						if (var6 > var9) {
							var42 = 0;
						} else {
							var42 = 1;
						}

						par1Vec3.xCoord += var27 * var23;
						par1Vec3.yCoord = var17;
						par1Vec3.zCoord += var31 * var23;
					} else {
						if (var7 > var10) {
							var42 = 2;
						} else {
							var42 = 3;
						}

						par1Vec3.xCoord += var27 * var25;
						par1Vec3.yCoord += var29 * var25;
						par1Vec3.zCoord = var19;
					}

					Vec3 var34 = Vec3.func_72437_a().func_72345_a(par1Vec3.xCoord, par1Vec3.yCoord, par1Vec3.zCoord);
					var8 = (int)(var34.xCoord = (double)MathHelper.floor_double(par1Vec3.xCoord));
					if (var42 == 5) {
						--var8;
						++var34.xCoord;
					}

					var9 = (int)(var34.yCoord = (double)MathHelper.floor_double(par1Vec3.yCoord));
					if (var42 == 1) {
						--var9;
						++var34.yCoord;
					}

					var10 = (int)(var34.zCoord = (double)MathHelper.floor_double(par1Vec3.zCoord));
					if (var42 == 3) {
						--var10;
						++var34.zCoord;
					}

					int var35 = this.getBlockId(var8, var9, var10);
					int var36 = this.getBlockMetadata(var8, var9, var10);
					Block var37 = Block.blocksList[var35];
					if ((!par4 || var37 == null || var37.getCollisionBoundingBoxFromPool(this, var8, var9, var10) != null) && var35 > 0 && var37.canCollideCheck(var36, par3)) {
						MovingObjectPosition var38 = var37.collisionRayTrace(this, var8, var9, var10, par1Vec3, par2Vec3);
						if (var38 != null) {
							return var38;
						}
					}
				}

				return null;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public void playSoundAtEntity(Entity par1Entity, String par2Str, float par3, float par4) {
		if (par1Entity != null && par2Str != null) {
			Iterator var5 = this.worldAccesses.iterator();

			while (var5.hasNext()) {
				IWorldAccess var6 = (IWorldAccess)var5.next();
				var6.playSound(par2Str, par1Entity.posX, par1Entity.posY - (double)par1Entity.yOffset, par1Entity.posZ, par3, par4);
			}
		}
	}

	public void func_72980_b(double par1, double par3, double par5, String par7Str, float par8, float par9) {
		if (par7Str != null) {
			Iterator var10 = this.worldAccesses.iterator();

			while (var10.hasNext()) {
				IWorldAccess var11 = (IWorldAccess)var10.next();
				var11.playSound(par7Str, par1, par3, par5, par8, par9);
			}
		}
	}

	public void playRecord(String par1Str, int par2, int par3, int par4) {
		Iterator var5 = this.worldAccesses.iterator();

		while (var5.hasNext()) {
			IWorldAccess var6 = (IWorldAccess)var5.next();
			var6.playRecord(par1Str, par2, par3, par4);
		}
	}

	public void spawnParticle(String par1Str, double par2, double par4, double par6, double par8, double par10, double par12) {
		Iterator var14 = this.worldAccesses.iterator();

		while (var14.hasNext()) {
			IWorldAccess var15 = (IWorldAccess)var14.next();
			var15.spawnParticle(par1Str, par2, par4, par6, par8, par10, par12);
		}
	}

	public boolean addWeatherEffect(Entity par1Entity) {
		this.weatherEffects.add(par1Entity);
		return true;
	}

	public boolean spawnEntityInWorld(Entity par1Entity) {
		int var2 = MathHelper.floor_double(par1Entity.posX / 16.0D);
		int var3 = MathHelper.floor_double(par1Entity.posZ / 16.0D);
		boolean var4 = false;
		if (par1Entity instanceof EntityPlayer) {
			var4 = true;
		}

		if (!var4 && !this.chunkExists(var2, var3)) {
			return false;
		} else {
			if (par1Entity instanceof EntityPlayer) {
				EntityPlayer var5 = (EntityPlayer)par1Entity;
				this.playerEntities.add(var5);
				this.updateAllPlayersSleepingFlag();
			}

			this.getChunkFromChunkCoords(var2, var3).addEntity(par1Entity);
			this.loadedEntityList.add(par1Entity);
			this.obtainEntitySkin(par1Entity);
			return true;
		}
	}

	public void obtainEntitySkin(Entity par1Entity) { //Spout protected -> public
		Iterator var2 = this.worldAccesses.iterator();

		while (var2.hasNext()) {
			IWorldAccess var3 = (IWorldAccess)var2.next();
			var3.obtainEntitySkin(par1Entity);
		}
	}

	public void releaseEntitySkin(Entity par1Entity) { //Spout protected -> public
		Iterator var2 = this.worldAccesses.iterator();

		while (var2.hasNext()) {
			IWorldAccess var3 = (IWorldAccess)var2.next();
			var3.releaseEntitySkin(par1Entity);
		}
	}

	public void setEntityDead(Entity par1Entity) {
		if (par1Entity.riddenByEntity != null) {
			par1Entity.riddenByEntity.mountEntity((Entity)null);
		}

		if (par1Entity.ridingEntity != null) {
			par1Entity.mountEntity((Entity)null);
		}

		par1Entity.setDead();
		if (par1Entity instanceof EntityPlayer) {
			this.playerEntities.remove((EntityPlayer)par1Entity);
			this.updateAllPlayersSleepingFlag();
		}
	}

	public void func_72973_f(Entity par1Entity) {
		par1Entity.setDead();

		if (par1Entity instanceof EntityPlayer) {
			this.playerEntities.remove(par1Entity);
			this.updateAllPlayersSleepingFlag();
		}

		int var2 = par1Entity.chunkCoordX;
		int var3 = par1Entity.chunkCoordZ;

		if (par1Entity.addedToChunk && this.chunkExists(var2, var3)) {
			this.getChunkFromChunkCoords(var2, var3).removeEntity(par1Entity);
		}

		this.loadedEntityList.remove(par1Entity);
		this.releaseEntitySkin(par1Entity);
	}

	public void addWorldAccess(IWorldAccess par1IWorldAccess) {
		this.worldAccesses.add(par1IWorldAccess);
	}

	public void removeWorldAccess(IWorldAccess par1IWorldAccess) {
		this.worldAccesses.remove(par1IWorldAccess);
	}

	public List getCollidingBoundingBoxes(Entity par1Entity, AxisAlignedBB par2AxisAlignedBB) {
		this.collidingBoundingBoxes.clear();
		int var3 = MathHelper.floor_double(par2AxisAlignedBB.minX);
		int var4 = MathHelper.floor_double(par2AxisAlignedBB.maxX + 1.0D);
		int var5 = MathHelper.floor_double(par2AxisAlignedBB.minY);
		int var6 = MathHelper.floor_double(par2AxisAlignedBB.maxY + 1.0D);
		int var7 = MathHelper.floor_double(par2AxisAlignedBB.minZ);
		int var8 = MathHelper.floor_double(par2AxisAlignedBB.maxZ + 1.0D);

		for (int var9 = var3; var9 < var4; ++var9) {
			for (int var10 = var7; var10 < var8; ++var10) {
				if (this.blockExists(var9, 64, var10)) {
					for (int var11 = var5 - 1; var11 < var6; ++var11) {
						Block var12 = Block.blocksList[this.getBlockId(var9, var11, var10)];
						if (var12 != null) {
							var12.func_71871_a(this, var9, var11, var10, par2AxisAlignedBB, this.collidingBoundingBoxes, par1Entity);
						}
					}
				}
			}
		}

		double var15 = 0.25D;
		List var17 = this.getEntitiesWithinAABBExcludingEntity(par1Entity, par2AxisAlignedBB.expand(var15, var15, var15));
		Iterator var16 = var17.iterator();

		while (var16.hasNext()) {
			Entity var13 = (Entity)var16.next();
			AxisAlignedBB var14 = var13.getBoundingBox();

			if (var14 != null && var14.intersectsWith(par2AxisAlignedBB)) {
				this.collidingBoundingBoxes.add(var14);
			}

			var14 = par1Entity.getCollisionBox(var13);

			if (var14 != null && var14.intersectsWith(par2AxisAlignedBB)) {
				this.collidingBoundingBoxes.add(var14);
			}
		}

		return this.collidingBoundingBoxes;
	}

	public List func_72840_a(AxisAlignedBB par1AxisAlignedBB) {
		this.collidingBoundingBoxes.clear();
		int var2 = MathHelper.floor_double(par1AxisAlignedBB.minX);
		int var3 = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0D);
		int var4 = MathHelper.floor_double(par1AxisAlignedBB.minY);
		int var5 = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0D);
		int var6 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
		int var7 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0D);

		for (int var8 = var2; var8 < var3; ++var8) {
			for (int var9 = var6; var9 < var7; ++var9) {
				if (this.blockExists(var8, 64, var9)) {
					for (int var10 = var4 - 1; var10 < var5; ++var10) {
						Block var11 = Block.blocksList[this.getBlockId(var8, var10, var9)];

						if (var11 != null) {
							var11.func_71871_a(this, var8, var10, var9, par1AxisAlignedBB, this.collidingBoundingBoxes, (Entity)null);
						}
					}
				}
			}
		}

		return this.collidingBoundingBoxes;
	}


	public int calculateSkylightSubtracted(float par1) {
		float var2 = this.getCelestialAngle(par1);
		float var3 = 1.0F - (MathHelper.cos(var2 * (float)Math.PI * 2.0F) * 2.0F + 0.5F);
		if (var3 < 0.0F) {
			var3 = 0.0F;
		}

		if (var3 > 1.0F) {
			var3 = 1.0F;
		}

		var3 = 1.0F - var3;
		var3 = (float)((double)var3 * (1.0D - (double)(this.getRainStrength(par1) * 5.0F) / 16.0D));
		var3 = (float)((double)var3 * (1.0D - (double)(this.getWeightedThunderStrength(par1) * 5.0F) / 16.0D));
		var3 = 1.0F - var3;
		return (int)(var3 * 11.0F);
	}

	public float func_72971_b(float par1) {
		float var2 = this.getCelestialAngle(par1);
		float var3 = 1.0F - (MathHelper.cos(var2 * (float)Math.PI * 2.0F) * 2.0F + 0.2F);
		if (var3 < 0.0F) {
			var3 = 0.0F;
		}

		if (var3 > 1.0F) {
			var3 = 1.0F;
		}

		var3 = 1.0F - var3;
		var3 = (float)((double)var3 * (1.0D - (double)(this.getRainStrength(par1) * 5.0F) / 16.0D));
		var3 = (float)((double)var3 * (1.0D - (double)(this.getWeightedThunderStrength(par1) * 5.0F) / 16.0D));
		return var3 * 0.8F + 0.2F;
	}

	public Vec3 getSkyColor(Entity par1Entity, float par2) {
		float var3 = this.getCelestialAngle(par2);
		float var4 = MathHelper.cos(var3 * (float)Math.PI * 2.0F) * 2.0F + 0.5F;

		if (var4 < 0.0F) {
			var4 = 0.0F;
		}

		if (var4 > 1.0F) {
			var4 = 1.0F;
		}

		int var5 = MathHelper.floor_double(par1Entity.posX);
		int var6 = MathHelper.floor_double(par1Entity.posZ);
		BiomeGenBase var7 = this.getBiomeGenForCoords(var5, var6);
		float var8 = var7.getFloatTemperature();
		int var9 = var7.getSkyColorByTemp(var8);
		float var10 = (float)(var9 >> 16 & 255) / 255.0F;
		float var11 = (float)(var9 >> 8 & 255) / 255.0F;
		float var12 = (float)(var9 & 255) / 255.0F;
		var10 *= var4;
		var11 *= var4;
		var12 *= var4;
		float var13 = this.getRainStrength(par2);
		float var14;
		float var15;

		if (var13 > 0.0F) {
			var14 = (var10 * 0.3F + var11 * 0.59F + var12 * 0.11F) * 0.6F;
			var15 = 1.0F - var13 * 0.75F;
			var10 = var10 * var15 + var14 * (1.0F - var15);
			var11 = var11 * var15 + var14 * (1.0F - var15);
			var12 = var12 * var15 + var14 * (1.0F - var15);
		}

		var14 = this.getWeightedThunderStrength(par2);

		if (var14 > 0.0F) {
			var15 = (var10 * 0.3F + var11 * 0.59F + var12 * 0.11F) * 0.2F;
			float var16 = 1.0F - var14 * 0.75F;
			var10 = var10 * var16 + var15 * (1.0F - var16);
			var11 = var11 * var16 + var15 * (1.0F - var16);
			var12 = var12 * var16 + var15 * (1.0F - var16);
		}

		if (this.lightningFlash > 0) {
			var15 = (float)this.lightningFlash - par2;

			if (var15 > 1.0F) {
				var15 = 1.0F;
			}

			var15 *= 0.45F;
			var10 = var10 * (1.0F - var15) + 0.8F * var15;
			var11 = var11 * (1.0F - var15) + 0.8F * var15;
			var12 = var12 * (1.0F - var15) + 1.0F * var15;
		}

		return Vec3.func_72437_a().func_72345_a((double)var10, (double)var11, (double)var12);
	}

	public float getCelestialAngle(float par1) {
		return this.worldProvider.calculateCelestialAngle(this.worldInfo.getWorldTime(), par1);
	}

	public int getMoonPhase(float par1) {
		return this.worldProvider.getMoonPhase(this.worldInfo.getWorldTime(), par1);
	}

	public float getCelestialAngleRadians(float par1) {
		float var2 = this.getCelestialAngle(par1);
		return var2 * (float)Math.PI * 2.0F;
	}

	public Vec3 drawClouds(float par1) {
		float var2 = this.getCelestialAngle(par1);
		float var3 = MathHelper.cos(var2 * (float)Math.PI * 2.0F) * 2.0F + 0.5F;
		if (var3 < 0.0F) {
			var3 = 0.0F;
		}

		if (var3 > 1.0F) {
			var3 = 1.0F;
		}

		float var4 = (float)(this.cloudColour >> 16 & 255L) / 255.0F;
		float var5 = (float)(this.cloudColour >> 8 & 255L) / 255.0F;
		float var6 = (float)(this.cloudColour & 255L) / 255.0F;
		float var7 = this.getRainStrength(par1);
		float var8;
		float var9;
		if (var7 > 0.0F) {
			var8 = (var4 * 0.3F + var5 * 0.59F + var6 * 0.11F) * 0.6F;
			var9 = 1.0F - var7 * 0.95F;
			var4 = var4 * var9 + var8 * (1.0F - var9);
			var5 = var5 * var9 + var8 * (1.0F - var9);
			var6 = var6 * var9 + var8 * (1.0F - var9);
		}

		var4 *= var3 * 0.9F + 0.1F;
		var5 *= var3 * 0.9F + 0.1F;
		var6 *= var3 * 0.85F + 0.15F;
		var8 = this.getWeightedThunderStrength(par1);
		if (var8 > 0.0F) {
			var9 = (var4 * 0.3F + var5 * 0.59F + var6 * 0.11F) * 0.2F;
			float var10 = 1.0F - var8 * 0.95F;
			var4 = var4 * var10 + var9 * (1.0F - var10);
			var5 = var5 * var10 + var9 * (1.0F - var10);
			var6 = var6 * var10 + var9 * (1.0F - var10);
		}

		return Vec3.func_72437_a().func_72345_a((double)var4, (double)var5, (double)var6);
	}

	public Vec3 getFogColor(float par1) {
		float var2 = this.getCelestialAngle(par1);
		return this.worldProvider.getFogColor(var2, par1);
	}

	public int getPrecipitationHeight(int par1, int par2) {
		return this.getChunkFromBlockCoords(par1, par2).getPrecipitationHeight(par1 & 15, par2 & 15);
	}

	public int getTopSolidOrLiquidBlock(int par1, int par2) {
		Chunk var3 = this.getChunkFromBlockCoords(par1, par2);
		int var4 = var3.getTopFilledSegment() + 15;
		par1 &= 15;

		for (par2 &= 15; var4 > 0; --var4) {
			int var5 = var3.getBlockID(par1, var4, par2);
			if (var5 != 0 && Block.blocksList[var5].blockMaterial.blocksMovement() && Block.blocksList[var5].blockMaterial != Material.leaves) {
				return var4 + 1;
			}
		}

		return -1;
	}

	public float getStarBrightness(float par1) {
		float var2 = this.getCelestialAngle(par1);
		float var3 = 1.0F - (MathHelper.cos(var2 * (float)Math.PI * 2.0F) * 2.0F + 0.25F);
		if (var3 < 0.0F) {
			var3 = 0.0F;
		}

		if (var3 > 1.0F) {
			var3 = 1.0F;
		}

		return var3 * var3 * 0.5F;
	}

	public void scheduleBlockUpdate(int par1, int par2, int par3, int par4, int par5) {}

	public void scheduleBlockUpdateFromLoad(int par1, int par2, int par3, int par4, int par5) {}

	public void updateEntities() {
		this.field_72984_F.startSection("entities");
		this.field_72984_F.startSection("global");

		int var1;
		Entity var2;
		for (var1 = 0; var1 < this.weatherEffects.size(); ++var1) {
			var2 = (Entity)this.weatherEffects.get(var1);
			var2.onUpdate();
			if (var2.isDead) {
				this.weatherEffects.remove(var1--);
			}
		}

		this.field_72984_F.endStartSection("remove");
		this.loadedEntityList.removeAll(this.unloadedEntityList);
		Iterator var5 = this.unloadedEntityList.iterator();
		int var3;
		int var4;

		while (var5.hasNext()) {
			var2 = (Entity)var5.next();
			var3 = var2.chunkCoordX;
			var4 = var2.chunkCoordZ;
			if (var2.addedToChunk && this.chunkExists(var3, var4)) {
				this.getChunkFromChunkCoords(var3, var4).removeEntity(var2);
			}
		}

		var5 = this.unloadedEntityList.iterator();

		while (var5.hasNext()) {
			var2 = (Entity)var5.next();
			this.releaseEntitySkin(var2);
		}

		this.unloadedEntityList.clear();
		this.field_72984_F.endStartSection("regular");

		for (var1 = 0; var1 < this.loadedEntityList.size(); ++var1) {
			var2 = (Entity)this.loadedEntityList.get(var1);
			if(var2 instanceof EntityText) {
			}
			if (var2.ridingEntity != null) {
				if (!var2.ridingEntity.isDead && var2.ridingEntity.riddenByEntity == var2) {
					continue;
				}

				var2.ridingEntity.riddenByEntity = null;
				var2.ridingEntity = null;
			}
			this.field_72984_F.startSection("tick");

			if (!var2.isDead) {
				this.updateEntity(var2);
			}

			this.field_72984_F.endSection();
			this.field_72984_F.startSection("remove");

			if (var2.isDead) {
				var3 = var2.chunkCoordX;
				var4 = var2.chunkCoordZ;
				if (var2.addedToChunk && this.chunkExists(var3, var4)) {
					this.getChunkFromChunkCoords(var3, var4).removeEntity(var2);
				}

				this.loadedEntityList.remove(var1--);
				this.releaseEntitySkin(var2);
			}

			this.field_72984_F.endSection();
		}

		this.field_72984_F.endStartSection("tileEntities");
		this.scanningTileEntities = true;
		var5 = this.loadedTileEntityList.iterator();

		while (var5.hasNext()) {
			TileEntity var6 = (TileEntity)var5.next();
			if (!var6.isInvalid() && var6.worldObj != null && this.blockExists(var6.xCoord, var6.yCoord, var6.zCoord)) {
				var6.updateEntity();
			}

			if (var6.isInvalid()) {
				var5.remove();

				if (this.chunkExists(var6.xCoord >> 4, var6.zCoord >> 4)) {
					Chunk var8 = this.getChunkFromChunkCoords(var6.xCoord >> 4, var6.zCoord >> 4);

					if (var8 != null) {
						var8.removeChunkBlockTileEntity(var6.xCoord & 15, var6.yCoord, var6.zCoord & 15);
					}
				}
			}
		}

		this.scanningTileEntities = false;
		if (!this.entityRemoval.isEmpty()) {
			this.loadedTileEntityList.removeAll(this.entityRemoval);
			this.entityRemoval.clear();
		}

		this.field_72984_F.endStartSection("pendingTileEntities");
		if (!this.addedTileEntityList.isEmpty()) {
			Iterator var7 = this.addedTileEntityList.iterator();

			while (var7.hasNext()) {
				TileEntity var9 = (TileEntity)var7.next();
				if (!var9.isInvalid()) {
					if (!this.loadedTileEntityList.contains(var9)) {
						this.loadedTileEntityList.add(var9);
					}


					if (this.chunkExists(var9.xCoord >> 4, var9.zCoord >> 4)) {
						Chunk var10 = this.getChunkFromChunkCoords(var9.xCoord >> 4, var9.zCoord >> 4);

						if (var10 != null) {
							var10.setChunkBlockTileEntity(var9.xCoord & 15, var9.yCoord, var9.zCoord & 15, var9);
						}
					}

					this.markBlockNeedsUpdate(var9.xCoord, var9.yCoord, var9.zCoord);
				}
			}

			this.addedTileEntityList.clear();
		}

		this.field_72984_F.endSection();
		this.field_72984_F.endSection();
	}

	public void addTileEntity(Collection par1Collection) {
		if (this.scanningTileEntities) {
			this.addedTileEntityList.addAll(par1Collection);
		} else {
			this.loadedTileEntityList.addAll(par1Collection);
		}
	}

	public void updateEntity(Entity par1Entity) {
		this.updateEntityWithOptionalForce(par1Entity, true);
	}

	public void updateEntityWithOptionalForce(Entity par1Entity, boolean par2) {
		int var3 = MathHelper.floor_double(par1Entity.posX);
		int var4 = MathHelper.floor_double(par1Entity.posZ);
		byte var5 = 32;
		if (!par2 || this.checkChunksExist(var3 - var5, 0, var4 - var5, var3 + var5, 0, var4 + var5)) {
			par1Entity.lastTickPosX = par1Entity.posX;
			par1Entity.lastTickPosY = par1Entity.posY;
			par1Entity.lastTickPosZ = par1Entity.posZ;
			par1Entity.prevRotationYaw = par1Entity.rotationYaw;
			par1Entity.prevRotationPitch = par1Entity.rotationPitch;
			if (par2 && par1Entity.addedToChunk) {
				if (par1Entity.ridingEntity != null) {
					par1Entity.updateRidden();
				} else {
					par1Entity.onUpdate();
				}
			}

			this.field_72984_F.startSection("chunkCheck");
			if (Double.isNaN(par1Entity.posX) || Double.isInfinite(par1Entity.posX)) {
				par1Entity.posX = par1Entity.lastTickPosX;
			}

			if (Double.isNaN(par1Entity.posY) || Double.isInfinite(par1Entity.posY)) {
				par1Entity.posY = par1Entity.lastTickPosY;
			}

			if (Double.isNaN(par1Entity.posZ) || Double.isInfinite(par1Entity.posZ)) {
				par1Entity.posZ = par1Entity.lastTickPosZ;
			}

			if (Double.isNaN((double)par1Entity.rotationPitch) || Double.isInfinite((double)par1Entity.rotationPitch)) {
				par1Entity.rotationPitch = par1Entity.prevRotationPitch;
			}

			if (Double.isNaN((double)par1Entity.rotationYaw) || Double.isInfinite((double)par1Entity.rotationYaw)) {
				par1Entity.rotationYaw = par1Entity.prevRotationYaw;
			}

			int var6 = MathHelper.floor_double(par1Entity.posX / 16.0D);
			int var7 = MathHelper.floor_double(par1Entity.posY / 16.0D);
			int var8 = MathHelper.floor_double(par1Entity.posZ / 16.0D);
			if (!par1Entity.addedToChunk || par1Entity.chunkCoordX != var6 || par1Entity.chunkCoordY != var7 || par1Entity.chunkCoordZ != var8) {
				if (par1Entity.addedToChunk && this.chunkExists(par1Entity.chunkCoordX, par1Entity.chunkCoordZ)) {
					this.getChunkFromChunkCoords(par1Entity.chunkCoordX, par1Entity.chunkCoordZ).removeEntityAtIndex(par1Entity, par1Entity.chunkCoordY);
				}

				if (this.chunkExists(var6, var8)) {
					par1Entity.addedToChunk = true;
					this.getChunkFromChunkCoords(var6, var8).addEntity(par1Entity);
				} else {
					par1Entity.addedToChunk = false;
				}
			}

			this.field_72984_F.endSection();
			if (par2 && par1Entity.addedToChunk && par1Entity.riddenByEntity != null) {
				if (!par1Entity.riddenByEntity.isDead && par1Entity.riddenByEntity.ridingEntity == par1Entity) {
					this.updateEntity(par1Entity.riddenByEntity);
				} else {
					par1Entity.riddenByEntity.ridingEntity = null;
					par1Entity.riddenByEntity = null;
				}
			}
		}
	}

	public boolean checkIfAABBIsClear(AxisAlignedBB par1AxisAlignedBB) {
		return this.func_72917_a(par1AxisAlignedBB, (Entity)null);
	}

	public boolean func_72917_a(AxisAlignedBB par1AxisAlignedBB, Entity par2Entity) {
		List var3 = this.getEntitiesWithinAABBExcludingEntity((Entity) null, par1AxisAlignedBB);
		Iterator var4 = var3.iterator();
		Entity var5;

		do {
			if (!var4.hasNext()) {
				return true;
			}

			var5 = (Entity)var4.next();
		} while (var5.isDead || !var5.preventEntitySpawning || var5 == par2Entity);

		return false;
	}

	public boolean func_72829_c(AxisAlignedBB par1AxisAlignedBB) {
		int var2 = MathHelper.floor_double(par1AxisAlignedBB.minX);
		int var3 = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0D);
		int var4 = MathHelper.floor_double(par1AxisAlignedBB.minY);
		int var5 = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0D);
		int var6 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
		int var7 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0D);

		if (par1AxisAlignedBB.minX < 0.0D) {
			--var2;
		}

		if (par1AxisAlignedBB.minY < 0.0D) {
			--var4;
		}

		if (par1AxisAlignedBB.minZ < 0.0D) {
			--var6;
		}

		for (int var8 = var2; var8 < var3; ++var8) {
			for (int var9 = var4; var9 < var5; ++var9) {
				for (int var10 = var6; var10 < var7; ++var10) {
					Block var11 = Block.blocksList[this.getBlockId(var8, var9, var10)];

					if (var11 != null) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public boolean isAnyLiquid(AxisAlignedBB par1AxisAlignedBB) {
		int var2 = MathHelper.floor_double(par1AxisAlignedBB.minX);
		int var3 = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0D);
		int var4 = MathHelper.floor_double(par1AxisAlignedBB.minY);
		int var5 = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0D);
		int var6 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
		int var7 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0D);
		if (par1AxisAlignedBB.minX < 0.0D) {
			--var2;
		}

		if (par1AxisAlignedBB.minY < 0.0D) {
			--var4;
		}

		if (par1AxisAlignedBB.minZ < 0.0D) {
			--var6;
		}

		for (int var8 = var2; var8 < var3; ++var8) {
			for (int var9 = var4; var9 < var5; ++var9) {
				for (int var10 = var6; var10 < var7; ++var10) {
					Block var11 = Block.blocksList[this.getBlockId(var8, var9, var10)];
					if (var11 != null && var11.blockMaterial.isLiquid()) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public boolean isBoundingBoxBurning(AxisAlignedBB par1AxisAlignedBB) {
		int var2 = MathHelper.floor_double(par1AxisAlignedBB.minX);
		int var3 = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0D);
		int var4 = MathHelper.floor_double(par1AxisAlignedBB.minY);
		int var5 = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0D);
		int var6 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
		int var7 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0D);
		if (this.checkChunksExist(var2, var4, var6, var3, var5, var7)) {
			for (int var8 = var2; var8 < var3; ++var8) {
				for (int var9 = var4; var9 < var5; ++var9) {
					for (int var10 = var6; var10 < var7; ++var10) {
						int var11 = this.getBlockId(var8, var9, var10);
						if (var11 == Block.fire.blockID || var11 == Block.lavaMoving.blockID || var11 == Block.lavaStill.blockID) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	public boolean handleMaterialAcceleration(AxisAlignedBB par1AxisAlignedBB, Material par2Material, Entity par3Entity) {
		int var4 = MathHelper.floor_double(par1AxisAlignedBB.minX);
		int var5 = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0D);
		int var6 = MathHelper.floor_double(par1AxisAlignedBB.minY);
		int var7 = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0D);
		int var8 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
		int var9 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0D);
		if (!this.checkChunksExist(var4, var6, var8, var5, var7, var9)) {
			return false;
		} else {
			boolean var10 = false;
			Vec3 var11 = Vec3.func_72437_a().func_72345_a(0.0D, 0.0D, 0.0D);

			for (int var12 = var4; var12 < var5; ++var12) {
				for (int var13 = var6; var13 < var7; ++var13) {
					for (int var14 = var8; var14 < var9; ++var14) {
						Block var15 = Block.blocksList[this.getBlockId(var12, var13, var14)];
						if (var15 != null && var15.blockMaterial == par2Material) {
							double var16 = (double)((float)(var13 + 1) - BlockFluid.getFluidHeightPercent(this.getBlockMetadata(var12, var13, var14)));
							if ((double)var7 >= var16) {
								var10 = true;
								var15.velocityToAddToEntity(this, var12, var13, var14, par3Entity, var11);
							}
						}
					}
				}
			}

			if (var11.lengthVector() > 0.0D) {
				var11 = var11.normalize();
				double var18 = 0.014D;
				par3Entity.motionX += var11.xCoord * var18;
				par3Entity.motionY += var11.yCoord * var18;
				par3Entity.motionZ += var11.zCoord * var18;
			}

			return var10;
		}
	}

	public boolean isMaterialInBB(AxisAlignedBB par1AxisAlignedBB, Material par2Material) {
		int var3 = MathHelper.floor_double(par1AxisAlignedBB.minX);
		int var4 = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0D);
		int var5 = MathHelper.floor_double(par1AxisAlignedBB.minY);
		int var6 = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0D);
		int var7 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
		int var8 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0D);

		for (int var9 = var3; var9 < var4; ++var9) {
			for (int var10 = var5; var10 < var6; ++var10) {
				for (int var11 = var7; var11 < var8; ++var11) {
					Block var12 = Block.blocksList[this.getBlockId(var9, var10, var11)];
					if (var12 != null && var12.blockMaterial == par2Material) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public boolean isAABBInMaterial(AxisAlignedBB par1AxisAlignedBB, Material par2Material) {
		int var3 = MathHelper.floor_double(par1AxisAlignedBB.minX);
		int var4 = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0D);
		int var5 = MathHelper.floor_double(par1AxisAlignedBB.minY);
		int var6 = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0D);
		int var7 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
		int var8 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0D);

		for (int var9 = var3; var9 < var4; ++var9) {
			for (int var10 = var5; var10 < var6; ++var10) {
				for (int var11 = var7; var11 < var8; ++var11) {
					Block var12 = Block.blocksList[this.getBlockId(var9, var10, var11)];
					if (var12 != null && var12.blockMaterial == par2Material) {
						int var13 = this.getBlockMetadata(var9, var10, var11);
						double var14 = (double)(var10 + 1);
						if (var13 < 8) {
							var14 = (double)(var10 + 1) - (double)var13 / 8.0D;
						}

						if (var14 >= par1AxisAlignedBB.minY) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	public Explosion createExplosion(Entity par1Entity, double par2, double par4, double par6, float par8) {
		return this.newExplosion(par1Entity, par2, par4, par6, par8, false);
	}

	public Explosion newExplosion(Entity par1Entity, double par2, double par4, double par6, float par8, boolean par9) {
		Explosion var10 = new Explosion(this, par1Entity, par2, par4, par6, par8);
		var10.isFlaming = par9;
		var10.doExplosionA();
		var10.doExplosionB(true);
		return var10;
	}

	public float getBlockDensity(Vec3 par1Vec3, AxisAlignedBB par2AxisAlignedBB) {
		double var3 = 1.0D / ((par2AxisAlignedBB.maxX - par2AxisAlignedBB.minX) * 2.0D + 1.0D);
		double var5 = 1.0D / ((par2AxisAlignedBB.maxY - par2AxisAlignedBB.minY) * 2.0D + 1.0D);
		double var7 = 1.0D / ((par2AxisAlignedBB.maxZ - par2AxisAlignedBB.minZ) * 2.0D + 1.0D);
		int var9 = 0;
		int var10 = 0;

		for (float var11 = 0.0F; var11 <= 1.0F; var11 = (float)((double)var11 + var3)) {
			for (float var12 = 0.0F; var12 <= 1.0F; var12 = (float)((double)var12 + var5)) {
				for (float var13 = 0.0F; var13 <= 1.0F; var13 = (float)((double)var13 + var7)) {
					double var14 = par2AxisAlignedBB.minX + (par2AxisAlignedBB.maxX - par2AxisAlignedBB.minX) * (double)var11;
					double var16 = par2AxisAlignedBB.minY + (par2AxisAlignedBB.maxY - par2AxisAlignedBB.minY) * (double)var12;
					double var18 = par2AxisAlignedBB.minZ + (par2AxisAlignedBB.maxZ - par2AxisAlignedBB.minZ) * (double)var13;
					if (this.rayTraceBlocks(Vec3.func_72437_a().func_72345_a(var14, var16, var18), par1Vec3) == null) {
						++var9;
					}

					++var10;
				}
			}
		}

		return (float)var9 / (float)var10;
	}

	public boolean func_72886_a(EntityPlayer par1EntityPlayer, int par2, int par3, int par4, int par5) {
		if (par5 == 0) {
			--par3;
		}

		if (par5 == 1) {
			++par3;
		}

		if (par5 == 2) {
			--par4;
		}

		if (par5 == 3) {
			++par4;
		}

		if (par5 == 4) {
			--par2;
		}

		if (par5 == 5) {
			++par2;
		}

		if (this.getBlockId(par2, par3, par4) == Block.fire.blockID) {
			this.playAuxSFXAtEntity(par1EntityPlayer, 1004, par2, par3, par4, 0);
			this.setBlockWithNotify(par2, par3, par4, 0);
			return true;
		} else {
			return false;
		}
	}

	public Entity func_4085_a(Class par1Class) {
		return null;
	}

	public String getDebugLoadedEntities() {
		return "All: " + this.loadedEntityList.size();
	}

	public String getProviderName() {
		return this.chunkProvider.makeString();
	}

	public TileEntity getBlockTileEntity(int par1, int par2, int par3) {
		if (par2 >= 256) {
			return null;
		} else {
			Chunk var4 = this.getChunkFromChunkCoords(par1 >> 4, par3 >> 4);
			if (var4 == null) {
				return null;
			} else {
				TileEntity var5 = var4.getChunkBlockTileEntity(par1 & 15, par2, par3 & 15);
				if (var5 == null) {
					Iterator var6 = this.addedTileEntityList.iterator();

					while (var6.hasNext()) {
						TileEntity var7 = (TileEntity)var6.next();
						if (!var7.isInvalid() && var7.xCoord == par1 && var7.yCoord == par2 && var7.zCoord == par3) {
							var5 = var7;
							break;
						}
					}
				}

				return var5;
			}
		}
	}

	public void setBlockTileEntity(int par1, int par2, int par3, TileEntity par4TileEntity) {
		if (par4TileEntity != null && !par4TileEntity.isInvalid()) {
			if (this.scanningTileEntities) {
				par4TileEntity.xCoord = par1;
				par4TileEntity.yCoord = par2;
				par4TileEntity.zCoord = par3;
				this.addedTileEntityList.add(par4TileEntity);
			} else {
				this.loadedTileEntityList.add(par4TileEntity);
				Chunk var5 = this.getChunkFromChunkCoords(par1 >> 4, par3 >> 4);
				if (var5 != null) {
					var5.setChunkBlockTileEntity(par1 & 15, par2, par3 & 15, par4TileEntity);
				}
			}
		}
	}

	public void removeBlockTileEntity(int par1, int par2, int par3) {
		TileEntity var4 = this.getBlockTileEntity(par1, par2, par3);
		if (var4 != null && this.scanningTileEntities) {
			var4.invalidate();
			this.addedTileEntityList.remove(var4);
		} else {
			if (var4 != null) {
				this.addedTileEntityList.remove(var4);
				this.loadedTileEntityList.remove(var4);
			}

			Chunk var5 = this.getChunkFromChunkCoords(par1 >> 4, par3 >> 4);
			if (var5 != null) {
				var5.removeChunkBlockTileEntity(par1 & 15, par2, par3 & 15);
			}
		}
	}

	public void markTileEntityForDespawn(TileEntity par1TileEntity) {
		this.entityRemoval.add(par1TileEntity);
	}

	public boolean isBlockOpaqueCube(int par1, int par2, int par3) {
		Block var4 = Block.blocksList[this.getBlockId(par1, par2, par3)];
		return var4 == null ? false : var4.isOpaqueCube();
	}

	public boolean isBlockNormalCube(int par1, int par2, int par3) {
		return Block.isNormalCube(this.getBlockId(par1, par2, par3));
	}

	public boolean func_72797_t(int par1, int par2, int par3) {
		Block var4 = Block.blocksList[this.getBlockId(par1, par2, par3)];
		return var4 == null ? false : (var4.blockMaterial.isOpaque() && var4.renderAsNormalBlock() ? true : (var4 instanceof BlockStairs ? (this.getBlockMetadata(par1, par2, par3) & 4) == 4 : (var4 instanceof BlockHalfSlab ? (this.getBlockMetadata(par1, par2, par3) & 8) == 8 : false)));
	}

	public boolean isBlockNormalCubeDefault(int par1, int par2, int par3, boolean par4) {
		if (par1 >= -30000000 && par3 >= -30000000 && par1 < 30000000 && par3 < 30000000) {
			Chunk var5 = this.chunkProvider.provideChunk(par1 >> 4, par3 >> 4);
			if (var5 != null && !var5.isEmpty()) {
				Block var6 = Block.blocksList[this.getBlockId(par1, par2, par3)];
				return var6 == null ? false : var6.blockMaterial.isOpaque() && var6.renderAsNormalBlock();
			} else {
				return par4;
			}
		} else {
			return par4;
		}
	}

	public void calculateInitialSkylight() {
		int var1 = this.calculateSkylightSubtracted(1.0F);
		if (var1 != this.skylightSubtracted) {
			this.skylightSubtracted = var1;
		}
	}

	public void setAllowedSpawnTypes(boolean par1, boolean par2) {
		this.spawnHostileMobs = par1;
		this.spawnPeacefulMobs = par2;
	}

	public void tick() {
		this.updateWeather();
	}

	private void calculateInitialWeather() {
		if (this.worldInfo.isRaining()) {
			this.rainingStrength = 1.0F;
			if (this.worldInfo.isThundering()) {
				this.thunderingStrength = 1.0F;
			}
		}
	}

	protected void updateWeather() {
		if (!this.worldProvider.hasNoSky) {
			if (this.lastLightningBolt > 0) {
				--this.lastLightningBolt;
			}

			int var1 = this.worldInfo.getThunderTime();
			if (var1 <= 0) {
				if (this.worldInfo.isThundering()) {
					this.worldInfo.setThunderTime(this.rand.nextInt(12000) + 3600);
				} else {
					this.worldInfo.setThunderTime(this.rand.nextInt(168000) + 12000);
				}
			} else {
				--var1;
				this.worldInfo.setThunderTime(var1);
				if (var1 <= 0) {
					this.worldInfo.setThundering(!this.worldInfo.isThundering());
				}
			}

			int var2 = this.worldInfo.getRainTime();
			if (var2 <= 0) {
				if (this.worldInfo.isRaining()) {
					this.worldInfo.setRainTime(this.rand.nextInt(12000) + 12000);
				} else {
					this.worldInfo.setRainTime(this.rand.nextInt(168000) + 12000);
				}
			} else {
				--var2;
				this.worldInfo.setRainTime(var2);
				if (var2 <= 0) {
					this.worldInfo.setRaining(!this.worldInfo.isRaining());
				}
			}

			this.prevRainingStrength = this.rainingStrength;
			if (this.worldInfo.isRaining()) {
				this.rainingStrength = (float)((double)this.rainingStrength + 0.01D);
			} else {
				this.rainingStrength = (float)((double)this.rainingStrength - 0.01D);
			}

			if (this.rainingStrength < 0.0F) {
				this.rainingStrength = 0.0F;
			}

			if (this.rainingStrength > 1.0F) {
				this.rainingStrength = 1.0F;
			}

			this.prevThunderingStrength = this.thunderingStrength;
			if (this.worldInfo.isThundering()) {
				this.thunderingStrength = (float)((double)this.thunderingStrength + 0.01D);
			} else {
				this.thunderingStrength = (float)((double)this.thunderingStrength - 0.01D);
			}

			if (this.thunderingStrength < 0.0F) {
				this.thunderingStrength = 0.0F;
			}

			if (this.thunderingStrength > 1.0F) {
				this.thunderingStrength = 1.0F;
			}
		}
	}

	public void func_72913_w() {
		this.worldInfo.setRainTime(1);
	}

	protected void func_72903_x() {
		this.activeChunkSet.clear();
		this.field_72984_F.startSection("buildList");

		int var1;
		EntityPlayer var2;
		int var3;
		int var4;
		for (var1 = 0; var1 < this.playerEntities.size(); ++var1) {
			var2 = (EntityPlayer)this.playerEntities.get(var1);
			var3 = MathHelper.floor_double(var2.posX / 16.0D);
			var4 = MathHelper.floor_double(var2.posZ / 16.0D);
			byte var5 = 7;

			for (int var6 = -var5; var6 <= var5; ++var6) {
				for (int var7 = -var5; var7 <= var5; ++var7) {
					this.activeChunkSet.add(new ChunkCoordIntPair(var6 + var3, var7 + var4));
				}
			}
		}

		this.field_72984_F.endSection();
		if (this.ambientTickCountdown > 0) {
			--this.ambientTickCountdown;
		}

		this.field_72984_F.startSection("playerCheckLight");
		if (!this.playerEntities.isEmpty() && ConfigReader.clientLight) { //Spout
			var1 = this.rand.nextInt(this.playerEntities.size());
			var2 = (EntityPlayer)this.playerEntities.get(var1);
			var3 = MathHelper.floor_double(var2.posX) + this.rand.nextInt(11) - 5;
			var4 = MathHelper.floor_double(var2.posY) + this.rand.nextInt(11) - 5;
			int var8 = MathHelper.floor_double(var2.posZ) + this.rand.nextInt(11) - 5;
			this.updateAllLightTypes(var3, var4, var8);
		}

		this.field_72984_F.endSection();
	}

	protected void func_48458_a(int par1, int par2, Chunk par3Chunk) {
		this.field_72984_F.endStartSection("moodSound");
		if (this.ambientTickCountdown == 0) {
			this.updateLCG = this.updateLCG * 3 + 1013904223;
			int var4 = this.updateLCG >> 2;
			int var5 = var4 & 15;
			int var6 = var4 >> 8 & 15;
			int var7 = var4 >> 16 & 127;
			int var8 = par3Chunk.getBlockID(var5, var7, var6);
			var5 += par1;
			var6 += par2;
			if (var8 == 0 && this.getFullBlockLightValue(var5, var7, var6) <= this.rand.nextInt(8) && this.getSavedLightValue(EnumSkyBlock.Sky, var5, var7, var6) <= 0) {
				EntityPlayer var9 = this.getClosestPlayer((double)var5 + 0.5D, (double)var7 + 0.5D, (double)var6 + 0.5D, 8.0D);
				if (var9 != null && var9.getDistanceSq((double)var5 + 0.5D, (double)var7 + 0.5D, (double)var6 + 0.5D) > 4.0D) {
					this.func_72980_b((double)var5 + 0.5D, (double)var7 + 0.5D, (double)var6 + 0.5D, "ambient.cave.cave", 0.7F, 0.8F + this.rand.nextFloat() * 0.2F);
					this.ambientTickCountdown = this.rand.nextInt(12000) + 6000;
				}
			}
		}

		this.field_72984_F.endStartSection("checkLight");
		par3Chunk.enqueueRelightChecks();
	}

	protected void tickBlocksAndAmbiance() {
		this.func_72903_x();
	}

	public boolean isBlockFreezable(int par1, int par2, int par3) {
		return this.canBlockFreeze(par1, par2, par3, false);
	}

	public boolean isBlockFreezableNaturally(int par1, int par2, int par3) {
		return this.canBlockFreeze(par1, par2, par3, true);
	}

	public boolean canBlockFreeze(int par1, int par2, int par3, boolean par4) {
		BiomeGenBase var5 = this.getBiomeGenForCoords(par1, par3);
		float var6 = var5.getFloatTemperature();
		if (var6 > 0.15F) {
			return false;
		} else {
			if (par2 >= 0 && par2 < 256 && this.getSavedLightValue(EnumSkyBlock.Block, par1, par2, par3) < 10) {
				int var7 = this.getBlockId(par1, par2, par3);
				if ((var7 == Block.waterStill.blockID || var7 == Block.waterMoving.blockID) && this.getBlockMetadata(par1, par2, par3) == 0) {
					if (!par4) {
						return true;
					}

					boolean var8 = true;
					if (var8 && this.getBlockMaterial(par1 - 1, par2, par3) != Material.water) {
						var8 = false;
					}

					if (var8 && this.getBlockMaterial(par1 + 1, par2, par3) != Material.water) {
						var8 = false;
					}

					if (var8 && this.getBlockMaterial(par1, par2, par3 - 1) != Material.water) {
						var8 = false;
					}

					if (var8 && this.getBlockMaterial(par1, par2, par3 + 1) != Material.water) {
						var8 = false;
					}

					if (!var8) {
						return true;
					}
				}
			}

			return false;
		}
	}

	public boolean canSnowAt(int par1, int par2, int par3) {
		BiomeGenBase var4 = this.getBiomeGenForCoords(par1, par3);
		float var5 = var4.getFloatTemperature();
		if (var5 > 0.15F) {
			return false;
		} else {
			if (par2 >= 0 && par2 < 256 && this.getSavedLightValue(EnumSkyBlock.Block, par1, par2, par3) < 10) {
				int var6 = this.getBlockId(par1, par2 - 1, par3);
				int var7 = this.getBlockId(par1, par2, par3);
				if (var7 == 0 && Block.snow.canPlaceBlockAt(this, par1, par2, par3) && var6 != 0 && var6 != Block.ice.blockID && Block.blocksList[var6].blockMaterial.blocksMovement()) {
					return true;
				}
			}

			return false;
		}
	}

	public void updateAllLightTypes(int par1, int par2, int par3) {
		if (!this.worldProvider.hasNoSky) {
			this.updateLightByType(EnumSkyBlock.Sky, par1, par2, par3);
		}

		this.updateLightByType(EnumSkyBlock.Block, par1, par2, par3);
	}

	private int computeSkyLightValue(int par1, int par2, int par3, int par4, int par5, int par6) {
		int var7 = 0;
		if (this.canBlockSeeTheSky(par2, par3, par4)) {
			var7 = 15;
		} else {
			if (par6 == 0) {
				par6 = 1;
			}

			int var8 = this.getSavedLightValue(EnumSkyBlock.Sky, par2 - 1, par3, par4) - par6;
			int var9 = this.getSavedLightValue(EnumSkyBlock.Sky, par2 + 1, par3, par4) - par6;
			int var10 = this.getSavedLightValue(EnumSkyBlock.Sky, par2, par3 - 1, par4) - par6;
			int var11 = this.getSavedLightValue(EnumSkyBlock.Sky, par2, par3 + 1, par4) - par6;
			int var12 = this.getSavedLightValue(EnumSkyBlock.Sky, par2, par3, par4 - 1) - par6;
			int var13 = this.getSavedLightValue(EnumSkyBlock.Sky, par2, par3, par4 + 1) - par6;
			if (var8 > var7) {
				var7 = var8;
			}

			if (var9 > var7) {
				var7 = var9;
			}

			if (var10 > var7) {
				var7 = var10;
			}

			if (var11 > var7) {
				var7 = var11;
			}

			if (var12 > var7) {
				var7 = var12;
			}

			if (var13 > var7) {
				var7 = var13;
			}
		}

		return var7;
	}

	private int computeBlockLightValue(int par1, int par2, int par3, int par4, int par5, int par6) {
		//Spout start
		int light = Block.lightValue[par5];
		
		//Fix for generation-time accessing
		org.spoutcraft.spoutcraftapi.World world = Spoutcraft.getWorld();
		short customId = 0;
		if(world != null) {
			customId = world.getChunkAt(par2, par3, par4).getCustomBlockId(par2, par3, par4);
		}
		if (customId > 0) {
			CustomBlock block = MaterialData.getCustomBlock(customId);
			if (block != null) {
				light = block.getLightLevel();
			}
		}
		int var7 = light;
		//Spout end
		int var8 = this.getSavedLightValue(EnumSkyBlock.Block, par2 - 1, par3, par4) - par6;
		int var9 = this.getSavedLightValue(EnumSkyBlock.Block, par2 + 1, par3, par4) - par6;
		int var10 = this.getSavedLightValue(EnumSkyBlock.Block, par2, par3 - 1, par4) - par6;
		int var11 = this.getSavedLightValue(EnumSkyBlock.Block, par2, par3 + 1, par4) - par6;
		int var12 = this.getSavedLightValue(EnumSkyBlock.Block, par2, par3, par4 - 1) - par6;
		int var13 = this.getSavedLightValue(EnumSkyBlock.Block, par2, par3, par4 + 1) - par6;
		if (var8 > var7) {
			var7 = var8;
		}

		if (var9 > var7) {
			var7 = var9;
		}

		if (var10 > var7) {
			var7 = var10;
		}

		if (var11 > var7) {
			var7 = var11;
		}

		if (var12 > var7) {
			var7 = var12;
		}

		if (var13 > var7) {
			var7 = var13;
		}

		return var7;
	}

	public void updateLightByType(EnumSkyBlock par1EnumSkyBlock, int par2, int par3, int par4) {
		if (this.doChunksNearChunkExist(par2, par3, par4, 17)) {
			int var5 = 0;
			int var6 = 0;
			this.field_72984_F.startSection("getBrightness");
			int var7 = this.getSavedLightValue(par1EnumSkyBlock, par2, par3, par4);
			boolean var8 = false;
			int var9 = this.getBlockId(par2, par3, par4);
			int var10 = this.getBlockLightOpacity(par2, par3, par4);
			if (var10 == 0) {
				var10 = 1;
			}

			boolean var11 = false;
			int var24;
			if (par1EnumSkyBlock == EnumSkyBlock.Sky) {
				var24 = this.computeSkyLightValue(var7, par2, par3, par4, var9, var10);
			} else {
				var24 = this.computeBlockLightValue(var7, par2, par3, par4, var9, var10);
			}

			int var12;
			int var13;
			int var14;
			int var15;
			int var17;
			int var16;
			int var19;
			int var18;
			if (var24 > var7) {
				this.lightUpdateBlockList[var6++] = 133152;
			} else if (var24 < var7) {
				if (par1EnumSkyBlock != EnumSkyBlock.Block) {
					;
				}

				this.lightUpdateBlockList[var6++] = 133152 + (var7 << 18);

				while (var5 < var6) {
					var9 = this.lightUpdateBlockList[var5++];
					var10 = (var9 & 63) - 32 + par2;
					var24 = (var9 >> 6 & 63) - 32 + par3;
					var12 = (var9 >> 12 & 63) - 32 + par4;
					var13 = var9 >> 18 & 15;
					var14 = this.getSavedLightValue(par1EnumSkyBlock, var10, var24, var12);
					if (var14 == var13) {
						this.setLightValue(par1EnumSkyBlock, var10, var24, var12, 0);

						if (var13 > 0) {
							var15 = var10 - par2;
							var16 = var24 - par3;
							var17 = var12 - par4;

							if (var15 < 0) {
								var15 = -var15;
							}

							if (var16 < 0) {
								var16 = -var16;
							}

							if (var17 < 0) {
								var17 = -var17;
							}

							if (var15 + var16 + var17 < 17) {
								for (var18 = 0; var18 < 6; ++var18) {
									var19 = var18 % 2 * 2 - 1;
									int var20 = var10 + var18 / 2 % 3 / 2 * var19;
									int var21 = var24 + (var18 / 2 + 1) % 3 / 2 * var19;
									int var22 = var12 + (var18 / 2 + 2) % 3 / 2 * var19;
									var14 = this.getSavedLightValue(par1EnumSkyBlock, var20, var21, var22);
									int var23 = Block.lightOpacity[this.getBlockId(var20, var21, var22)];

									if (var23 == 0) {
										var23 = 1;
									}

									if (var14 == var13 - var23 && var6 < this.lightUpdateBlockList.length) {
										this.lightUpdateBlockList[var6++] = var20 - par2 + 32 + (var21 - par3 + 32 << 6) + (var22 - par4 + 32 << 12) + (var13 - var23 << 18);
									}
								}
							}
						}
					}
				}

				var5 = 0;
			}

			this.field_72984_F.endSection();
			this.field_72984_F.startSection("tcp < tcc");

			while (var5 < var6) {
				var9 = this.lightUpdateBlockList[var5++];
				var10 = (var9 & 63) - 32 + par2;
				var24 = (var9 >> 6 & 63) - 32 + par3;
				var12 = (var9 >> 12 & 63) - 32 + par4;
				var13 = this.getSavedLightValue(par1EnumSkyBlock, var10, var24, var12);
				var14 = this.getBlockId(var10, var24, var12);
				var15 = Block.lightOpacity[var14];

				if (var15 == 0) {
					var15 = 1;
				}

				boolean var25 = false;

				if (par1EnumSkyBlock == EnumSkyBlock.Sky) {
					var16 = this.computeSkyLightValue(var13, var10, var24, var12, var14, var15);
				} else {
					var16 = this.computeBlockLightValue(var13, var10, var24, var12, var14, var15);
				}

				if (var16 != var13) {
					this.setLightValue(par1EnumSkyBlock, var10, var24, var12, var16);

					if (var16 > var13) {
						var17 = var10 - par2;
						var18 = var24 - par3;
						var19 = var12 - par4;

						if (var17 < 0) {
							var17 = -var17;
						}

						if (var18 < 0) {
							var18 = -var18;
						}

						if (var19 < 0) {
							var19 = -var19;
						}

						if (var17 + var18 + var19 < 17 && var6 < this.lightUpdateBlockList.length - 6) {
							if (this.getSavedLightValue(par1EnumSkyBlock, var10 - 1, var24, var12) < var16) {
								this.lightUpdateBlockList[var6++] = var10 - 1 - par2 + 32 + (var24 - par3 + 32 << 6) + (var12 - par4 + 32 << 12);
							}

							if (this.getSavedLightValue(par1EnumSkyBlock, var10 + 1, var24, var12) < var16) {
								this.lightUpdateBlockList[var6++] = var10 + 1 - par2 + 32 + (var24 - par3 + 32 << 6) + (var12 - par4 + 32 << 12);
							}

							if (this.getSavedLightValue(par1EnumSkyBlock, var10, var24 - 1, var12) < var16) {
								this.lightUpdateBlockList[var6++] = var10 - par2 + 32 + (var24 - 1 - par3 + 32 << 6) + (var12 - par4 + 32 << 12);
							}

							if (this.getSavedLightValue(par1EnumSkyBlock, var10, var24 + 1, var12) < var16) {
								this.lightUpdateBlockList[var6++] = var10 - par2 + 32 + (var24 + 1 - par3 + 32 << 6) + (var12 - par4 + 32 << 12);
							}

							if (this.getSavedLightValue(par1EnumSkyBlock, var10, var24, var12 - 1) < var16) {
								this.lightUpdateBlockList[var6++] = var10 - par2 + 32 + (var24 - par3 + 32 << 6) + (var12 - 1 - par4 + 32 << 12);
							}

							if (this.getSavedLightValue(par1EnumSkyBlock, var10, var24, var12 + 1) < var16) {
								this.lightUpdateBlockList[var6++] = var10 - par2 + 32 + (var24 - par3 + 32 << 6) + (var12 + 1 - par4 + 32 << 12);
							}
						}
					}
				}
			}

			this.field_72984_F.endSection();
		}
	}

	public boolean tickUpdates(boolean par1) {
		return false;
	}

	public List getEntitiesWithinAABBExcludingEntity(Entity par1Entity, AxisAlignedBB par2AxisAlignedBB) {
		this.entitiesWithinAABBExcludingEntity.clear();
		int var3 = MathHelper.floor_double((par2AxisAlignedBB.minX - 2.0D) / 16.0D);
		int var4 = MathHelper.floor_double((par2AxisAlignedBB.maxX + 2.0D) / 16.0D);
		int var5 = MathHelper.floor_double((par2AxisAlignedBB.minZ - 2.0D) / 16.0D);
		int var6 = MathHelper.floor_double((par2AxisAlignedBB.maxZ + 2.0D) / 16.0D);

		for (int var7 = var3; var7 <= var4; ++var7) {
			for (int var8 = var5; var8 <= var6; ++var8) {
				if (this.chunkExists(var7, var8)) {
					this.getChunkFromChunkCoords(var7, var8).getEntitiesWithinAABBForEntity(par1Entity, par2AxisAlignedBB, this.entitiesWithinAABBExcludingEntity);
				}
			}
		}

		return this.entitiesWithinAABBExcludingEntity;
	}

	public List getEntitiesWithinAABB(Class par1Class, AxisAlignedBB par2AxisAlignedBB) {
		int var3 = MathHelper.floor_double((par2AxisAlignedBB.minX - 2.0D) / 16.0D);
		int var4 = MathHelper.floor_double((par2AxisAlignedBB.maxX + 2.0D) / 16.0D);
		int var5 = MathHelper.floor_double((par2AxisAlignedBB.minZ - 2.0D) / 16.0D);
		int var6 = MathHelper.floor_double((par2AxisAlignedBB.maxZ + 2.0D) / 16.0D);
		ArrayList var7 = new ArrayList();

		for (int var8 = var3; var8 <= var4; ++var8) {
			for (int var9 = var5; var9 <= var6; ++var9) {
				if (this.chunkExists(var8, var9)) {
					this.getChunkFromChunkCoords(var8, var9).getEntitiesOfTypeWithinAAAB(par1Class, par2AxisAlignedBB, var7);
				}
			}
		}

		return var7;
	}

	public Entity findNearestEntityWithinAABB(Class par1Class, AxisAlignedBB par2AxisAlignedBB, Entity par3Entity) {
		List var4 = this.getEntitiesWithinAABB(par1Class, par2AxisAlignedBB);
		Entity var5 = null;
		double var6 = Double.MAX_VALUE;
		Iterator var8 = var4.iterator();

		while (var8.hasNext()) {
			Entity var9 = (Entity)var8.next();
			if (var9 != par3Entity) {
				double var10 = par3Entity.getDistanceSqToEntity(var9);
				if (var10 <= var6) {
					var5 = var9;
					var6 = var10;
				}
			}
		}

		return var5;
	}

	public List getLoadedEntityList() {
		return this.loadedEntityList;
	}

	public void updateTileEntityChunkAndDoNothing(int par1, int par2, int par3, TileEntity par4TileEntity) {
		if (this.blockExists(par1, par2, par3)) {
			this.getChunkFromBlockCoords(par1, par3).setChunkModified();
		}
	}

	public int countEntities(Class par1Class) {
		int var2 = 0;

		for (int var3 = 0; var3 < this.loadedEntityList.size(); ++var3) {
			Entity var4 = (Entity)this.loadedEntityList.get(var3);
			if (par1Class.isAssignableFrom(var4.getClass())) {
				++var2;
			}
		}

		return var2;
	}

	public void addLoadedEntities(List par1List) {
		this.loadedEntityList.addAll(par1List);

		for (int var2 = 0; var2 < par1List.size(); ++var2) {
			this.obtainEntitySkin((Entity)par1List.get(var2));
		}
	}

	public void unloadEntities(List par1List) {
		this.unloadedEntityList.addAll(par1List);
	}

	public boolean func_72931_a(int par1, int par2, int par3, int par4, boolean par5, int par6, Entity par7Entity) {
		int var8 = this.getBlockId(par2, par3, par4);
		Block var9 = Block.blocksList[var8];
		Block var10 = Block.blocksList[par1];
		AxisAlignedBB var11 = var10.getCollisionBoundingBoxFromPool(this, par2, par3, par4);

		if (par5) {
			var11 = null;
		}

		if (var11 != null && !this.func_72917_a(var11, par7Entity)) {
			return false;
		} else {
			if (var9 != null && (var9 == Block.waterMoving || var9 == Block.waterStill || var9 == Block.lavaMoving || var9 == Block.lavaStill || var9 == Block.fire || var9.blockMaterial.isGroundCover())) {
				var9 = null;
			}

			return par1 > 0 && var9 == null && var10.canPlaceBlockOnSide(this, par2, par3, par4, par6);
		}
	}

	public PathEntity getPathEntityToEntity(Entity par1Entity, Entity par2Entity, float par3, boolean par4, boolean par5, boolean par6, boolean par7) {
		this.field_72984_F.startSection("pathfind");
		int var8 = MathHelper.floor_double(par1Entity.posX);
		int var9 = MathHelper.floor_double(par1Entity.posY + 1.0D);
		int var10 = MathHelper.floor_double(par1Entity.posZ);
		int var11 = (int)(par3 + 16.0F);
		int var12 = var8 - var11;
		int var13 = var9 - var11;
		int var14 = var10 - var11;
		int var15 = var8 + var11;
		int var16 = var9 + var11;
		int var17 = var10 + var11;
		ChunkCache var18 = new ChunkCache(this, var12, var13, var14, var15, var16, var17);
		PathEntity var19 = (new PathFinder(var18, par4, par5, par6, par7)).createEntityPathTo(par1Entity, par2Entity, par3);
		this.field_72984_F.endSection();
		return var19;
	}

	public PathEntity getEntityPathToXYZ(Entity par1Entity, int par2, int par3, int par4, float par5, boolean par6, boolean par7, boolean par8, boolean par9) {
		this.field_72984_F.startSection("pathfind");
		int var10 = MathHelper.floor_double(par1Entity.posX);
		int var11 = MathHelper.floor_double(par1Entity.posY);
		int var12 = MathHelper.floor_double(par1Entity.posZ);
		int var13 = (int)(par5 + 8.0F);
		int var14 = var10 - var13;
		int var15 = var11 - var13;
		int var16 = var12 - var13;
		int var17 = var10 + var13;
		int var18 = var11 + var13;
		int var19 = var12 + var13;
		ChunkCache var20 = new ChunkCache(this, var14, var15, var16, var17, var18, var19);
		PathEntity var21 = (new PathFinder(var20, par6, par7, par8, par9)).createEntityPathTo(par1Entity, par2, par3, par4, par5);
		this.field_72984_F.endSection();
		return var21;
	}

	public boolean isBlockProvidingPowerTo(int par1, int par2, int par3, int par4) {
		int var5 = this.getBlockId(par1, par2, par3);
		return var5 == 0 ? false : Block.blocksList[var5].isIndirectlyPoweringTo(this, par1, par2, par3, par4);
	}

	public boolean isBlockGettingPowered(int par1, int par2, int par3) {
		return this.isBlockProvidingPowerTo(par1, par2 - 1, par3, 0) ? true : (this.isBlockProvidingPowerTo(par1, par2 + 1, par3, 1) ? true : (this.isBlockProvidingPowerTo(par1, par2, par3 - 1, 2) ? true : (this.isBlockProvidingPowerTo(par1, par2, par3 + 1, 3) ? true : (this.isBlockProvidingPowerTo(par1 - 1, par2, par3, 4) ? true : this.isBlockProvidingPowerTo(par1 + 1, par2, par3, 5)))));
	}

	public boolean isBlockIndirectlyProvidingPowerTo(int par1, int par2, int par3, int par4) {
		if (this.isBlockNormalCube(par1, par2, par3)) {
			return this.isBlockGettingPowered(par1, par2, par3);
		} else {
			int var5 = this.getBlockId(par1, par2, par3);
			return var5 == 0 ? false : Block.blocksList[var5].isPoweringTo(this, par1, par2, par3, par4);
		}
	}

	public boolean isBlockIndirectlyGettingPowered(int par1, int par2, int par3) {
		return this.isBlockIndirectlyProvidingPowerTo(par1, par2 - 1, par3, 0) ? true : (this.isBlockIndirectlyProvidingPowerTo(par1, par2 + 1, par3, 1) ? true : (this.isBlockIndirectlyProvidingPowerTo(par1, par2, par3 - 1, 2) ? true : (this.isBlockIndirectlyProvidingPowerTo(par1, par2, par3 + 1, 3) ? true : (this.isBlockIndirectlyProvidingPowerTo(par1 - 1, par2, par3, 4) ? true : this.isBlockIndirectlyProvidingPowerTo(par1 + 1, par2, par3, 5)))));
	}

	public EntityPlayer getClosestPlayerToEntity(Entity par1Entity, double par2) {
		return this.getClosestPlayer(par1Entity.posX, par1Entity.posY, par1Entity.posZ, par2);
	}

	public EntityPlayer getClosestPlayer(double par1, double par3, double par5, double par7) {
		double var9 = -1.0D;
		EntityPlayer var11 = null;

		for (int var12 = 0; var12 < this.playerEntities.size(); ++var12) {
			EntityPlayer var13 = (EntityPlayer)this.playerEntities.get(var12);
			double var14 = var13.getDistanceSq(par1, par3, par5);
			if ((par7 < 0.0D || var14 < par7 * par7) && (var9 == -1.0D || var14 < var9)) {
				var9 = var14;
				var11 = var13;
			}
		}

		return var11;
	}

	public EntityPlayer getClosestVulnerablePlayerToEntity(Entity par1Entity, double par2) {
		return this.getClosestVulnerablePlayer(par1Entity.posX, par1Entity.posY, par1Entity.posZ, par2);
	}

	public EntityPlayer getClosestVulnerablePlayer(double par1, double par3, double par5, double par7) {
		double var9 = -1.0D;
		EntityPlayer var11 = null;

		for (int var12 = 0; var12 < this.playerEntities.size(); ++var12) {
			EntityPlayer var13 = (EntityPlayer)this.playerEntities.get(var12);
			if (!var13.capabilities.disableDamage) {
				double var14 = var13.getDistanceSq(par1, par3, par5);
				if ((par7 < 0.0D || var14 < par7 * par7) && (var9 == -1.0D || var14 < var9)) {
					var9 = var14;
					var11 = var13;
				}
			}
		}

		return var11;
	}

	public EntityPlayer getPlayerEntityByName(String par1Str) {
		for (int var2 = 0; var2 < this.playerEntities.size(); ++var2) {
			if (par1Str.equals(((EntityPlayer)this.playerEntities.get(var2)).username)) {
				return (EntityPlayer)this.playerEntities.get(var2);
			}
		}

		return null;
	}

	public void sendQuittingDisconnectingPacket() {}

	public void checkSessionLock() throws MinecraftException {
		this.saveHandler.checkSessionLock();
	}

	public void setWorldTime(long var1) {
		//Spout start
		if (ConfigReader.time != 0) {
			return;
		}
		//Spout end
		this.worldInfo.setWorldTime(var1);
	}

	public long getSeed() {
		return this.worldInfo.getSeed();
	}

	public long getWorldTime() {
		return this.worldInfo.getWorldTime();
	}

	public ChunkCoordinates getSpawnPoint() {
		return new ChunkCoordinates(this.worldInfo.getSpawnX(), this.worldInfo.getSpawnY(), this.worldInfo.getSpawnZ());
	}

	public void func_72950_A(int par1, int par2, int par3) {
		this.worldInfo.setSpawnPosition(par1, par2, par3);
	}

	public void joinEntityInSurroundings(Entity par1Entity) {
		int var2 = MathHelper.floor_double(par1Entity.posX / 16.0D);
		int var3 = MathHelper.floor_double(par1Entity.posZ / 16.0D);
		byte var4 = 2;

		for (int var5 = var2 - var4; var5 <= var2 + var4; ++var5) {
			for (int var6 = var3 - var4; var6 <= var3 + var4; ++var6) {
				this.getChunkFromChunkCoords(var5, var6);
			}
		}

		if (!this.loadedEntityList.contains(par1Entity)) {
			this.loadedEntityList.add(par1Entity);
		}
	}

	public boolean canMineBlock(EntityPlayer par1EntityPlayer, int par2, int par3, int par4) {
		return true;
	}

	public void setEntityState(Entity par1Entity, byte par2) {}

	public IChunkProvider getChunkProvider() {
		return this.chunkProvider;
	}

	public void sendClientEvent(int par1, int par2, int par3, int par4, int par5, int par6) {
		if (par4 > 0) {
			Block.blocksList[par4].receiveClientEvent(this, par1, par2, par3, par5, par6);
		}
	}

	public ISaveHandler getSaveHandler() {
		return this.saveHandler;
	}

	public WorldInfo getWorldInfo() {
		return this.worldInfo;
	}

	public void updateAllPlayersSleepingFlag() {}

	public float getWeightedThunderStrength(float par1) {
		return (this.prevThunderingStrength + (this.thunderingStrength - this.prevThunderingStrength) * par1) * this.getRainStrength(par1);
	}

	public float getRainStrength(float par1) {
		return this.prevRainingStrength + (this.rainingStrength - this.prevRainingStrength) * par1;
	}

	public void setRainStrength(float par1) {
		this.prevRainingStrength = par1;
		this.rainingStrength = par1;
	}

	public boolean isThundering() {
		return (double)this.getWeightedThunderStrength(1.0F) > 0.9D;
	}

	public boolean isRaining() {
		return (double)this.getRainStrength(1.0F) > 0.2D;
	}

	public boolean canLightningStrikeAt(int par1, int par2, int par3) {
		if (!this.isRaining()) {
			return false;
		} else if (!this.canBlockSeeTheSky(par1, par2, par3)) {
			return false;
		} else if (this.getPrecipitationHeight(par1, par3) > par2) {
			return false;
		} else {
			BiomeGenBase var4 = this.getBiomeGenForCoords(par1, par3);
			return var4.getEnableSnow() ? false : var4.canSpawnLightningBolt();
		}
	}

	public boolean isBlockHighHumidity(int par1, int par2, int par3) {
		BiomeGenBase var4 = this.getBiomeGenForCoords(par1, par3);
		return var4.isHighHumidity();
	}

	public void setItemData(String par1Str, WorldSavedData par2WorldSavedData) {
		this.mapStorage.setData(par1Str, par2WorldSavedData);
	}

	public WorldSavedData loadItemData(Class par1Class, String par2Str) {
		return this.mapStorage.loadData(par1Class, par2Str);
	}

	public int getUniqueDataId(String par1Str) {
		return this.mapStorage.getUniqueDataId(par1Str);
	}

	public void playAuxSFX(int par1, int par2, int par3, int par4, int par5) {
		this.playAuxSFXAtEntity((EntityPlayer)null, par1, par2, par3, par4, par5);
	}

	public void playAuxSFXAtEntity(EntityPlayer par1EntityPlayer, int par2, int par3, int par4, int par5, int par6) {
		for (int var7 = 0; var7 < this.worldAccesses.size(); ++var7) {
			((IWorldAccess)this.worldAccesses.get(var7)).playAuxSFX(par1EntityPlayer, par2, par3, par4, par5, par6);
		}
	}

	public int getHeight() {
		return 256;
	}

	public int func_72940_L() {
		return this.worldProvider.hasNoSky ? 128 : 256;
	}

	public Random setRandomSeed(int par1, int par2, int par3) {
		long var4 = (long)par1 * 341873128712L + (long)par2 * 132897987541L + this.getWorldInfo().getSeed() + (long)par3;
		this.rand.setSeed(var4);
		return this.rand;
	}

	public boolean updatingLighting() {
		return false;
	}

	public SpawnListEntry getRandomMob(EnumCreatureType par1EnumCreatureType, int par2, int par3, int par4) {
		List var5 = this.getChunkProvider().getPossibleCreatures(par1EnumCreatureType, par2, par3, par4);
		return var5 != null && !var5.isEmpty()?(SpawnListEntry)WeightedRandom.getRandomItem(this.rand, var5):null;
	}

	public ChunkPosition findClosestStructure(String par1Str, int par2, int par3, int par4) {
		return this.getChunkProvider().findClosestStructure(this, par1Str, par2, par3, par4);
	}

	public boolean func_72806_N() {
		return false;
	}

	public double getHorizon() {
		return this.worldInfo.getTerrainType() == WorldType.FLAT ? 0.0D : 63.0D;
	}

	public CrashReport func_72914_a(CrashReport par1CrashReport) {
		par1CrashReport.func_71500_a("World " + this.worldInfo.getWorldName() + " Entities", new CallableLvl1(this));
		par1CrashReport.func_71500_a("World " + this.worldInfo.getWorldName() + " Players", new CallableLvl2(this));
		par1CrashReport.func_71500_a("World " + this.worldInfo.getWorldName() + " Chunk Stats", new CallableLvl3(this));
		return par1CrashReport;
	}

	public void func_72888_f(int par1, int par2, int par3, int par4, int par5) {
		Iterator var6 = this.worldAccesses.iterator();

		while (var6.hasNext()) {
			IWorldAccess var7 = (IWorldAccess)var6.next();
			var7.func_72705_a(par1, par2, par3, par4, par5);
		}
	}

	//Spout Start
	public void doColorfulStuff() {
		for(int i = 0; i < this.playerEntities.size(); ++i) {
			EntityPlayer ep = (EntityPlayer)this.playerEntities.get(i);
			if (ep != Minecraft.theMinecraft.field_71439_g || Minecraft.theMinecraft.gameSettings.thirdPersonView != 0) {
				ep.doFancyStuff();
			}
		}
	}

	public int getGrassColorCache(int x, int y, int z) {
		Chunk chunk = getChunkFromBlockCoords(x, z);
		if (chunk != null) {
			return chunk.grassColorCache;
		}
		return 0xffffff;
	}

	public void setGrassColorCache(int x, int y, int z, int color) {
		Chunk chunk = getChunkFromBlockCoords(x, z);
		if (chunk != null) {
			chunk.grassColorCache = color;
		}
	}

	public int getWaterColorCache(int x, int y, int z) {
		Chunk chunk = getChunkFromBlockCoords(x, z);
		if (chunk != null) {
			return chunk.waterColorCache;
		}
		return 0xffffff;
	}

	public void setWaterColorCache(int x, int y, int z, int color) {
		Chunk chunk = getChunkFromBlockCoords(x, z);
		if (chunk != null) {
			chunk.waterColorCache = color;
		}
	}
	//Spout End
	
	//Spout start
	public void checkEntityTile(TileEntity tileentity)
	{
		if(tileentity instanceof TileEntityPiston)
			return;

		switch(getBlockId(tileentity.xCoord, tileentity.yCoord, tileentity.zCoord))
		{
			case 63:
			case 68:
				if(!(tileentity instanceof TileEntitySign))
					tileentity.invalidate();
				break;
			case 61:
			case 62:
				if(!(tileentity instanceof TileEntityFurnace))
					tileentity.invalidate();
				break;
			case 84:
				if(!(tileentity instanceof TileEntityRecordPlayer))
					tileentity.invalidate();
				break;
			case 23:
				if(!(tileentity instanceof TileEntityDispenser))
					tileentity.invalidate();
				break;
			case 54:
				if(!(tileentity instanceof TileEntityChest))
					tileentity.invalidate();
				break;
			case 52:
				if(!(tileentity instanceof TileEntityMobSpawner))
					tileentity.invalidate();
				break;
			case 25:
				if(!(tileentity instanceof TileEntityNote))
					tileentity.invalidate();
				break;
			case 116:
				if(!(tileentity instanceof TileEntityEnchantmentTable))
					tileentity.invalidate();
				break;
			case 117:
				if(!(tileentity instanceof TileEntityBrewingStand))
					tileentity.invalidate();
				break;
			default:
				tileentity.invalidate();
				break;
		}
	}
	//Spout end
}
