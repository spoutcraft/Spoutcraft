package net.minecraft.src;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Block;
import net.minecraft.src.BlockFluid;
import net.minecraft.src.Chunk;
import net.minecraft.src.ChunkCoordIntPair;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.ChunkPosition;
import net.minecraft.src.ChunkProvider;
import net.minecraft.src.ChunkProviderLoadOrGenerate;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLightningBolt;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumCreatureType;
import net.minecraft.src.EnumSkyBlock;
import net.minecraft.src.EnumWorldType;
import net.minecraft.src.Explosion;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.IChunkLoader;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.IProgressUpdate;
import net.minecraft.src.ISaveHandler;
import net.minecraft.src.IWorldAccess;
import net.minecraft.src.MapStorage;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NextTickListEntry;
import net.minecraft.src.PathEntity;
import net.minecraft.src.PathFinder;
import net.minecraft.src.Profiler;
import net.minecraft.src.SpawnListEntry;
import net.minecraft.src.SpawnerAnimals;
import net.minecraft.src.ThreadedFileIOBase;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Vec3D;
import net.minecraft.src.WeightedRandom;
import net.minecraft.src.WorldChunkManager;
import net.minecraft.src.WorldInfo;
import net.minecraft.src.WorldProvider;
import net.minecraft.src.WorldSavedData;
import net.minecraft.src.WorldSettings;
//Spout Start
import org.spoutcraft.client.SpoutcraftWorld;
import org.spoutcraft.client.config.ConfigReader;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.material.CustomBlock;
import org.spoutcraft.spoutcraftapi.material.MaterialData;

import net.minecraft.client.Minecraft;

//Spout End
public class World implements IBlockAccess {

	public int heightShift;
	public int xShift;
	public int worldHeight;
	public int worldMaxY;
	public int seaLevel;
	public boolean scheduledUpdatesAreImmediate;
	public List loadedEntityList;
	private List unloadedEntityList;
	private TreeSet scheduledTickTreeSet;
	private Set scheduledTickSet;
	public List loadedTileEntityList;
	private List addedTileEntityList;
	private List entityRemoval;
	public List playerEntities;
	public List weatherEffects;
	private long cloudColour;
	public int skylightSubtracted;
	protected int updateLCG;
	protected final int DIST_HASH_MAGIC;
	protected float prevRainingStrength;
	protected float rainingStrength;
	protected float prevThunderingStrength;
	protected float thunderingStrength;
	protected int lastLightningBolt;
	public int lightningFlash;
	public boolean editingBlocks;
	private long lockTimestamp;
	public int autosavePeriod = org.spoutcraft.client.gui.settings.AutosaveButton.getAutosaveTicks(); // Spout protected -> public
	public int difficultySetting;
	public Random rand;
	public boolean isNewWorld;
	public final WorldProvider worldProvider;
	protected List worldAccesses;
	public IChunkProvider chunkProvider; // Spout protected -> public
	protected final ISaveHandler saveHandler;
	public WorldInfo worldInfo; // Spout protected -> public
	public boolean findingSpawnPoint;
	private boolean allPlayersSleeping;
	public MapStorage mapStorage;
	private ArrayList collidingBoundingBoxes;
	private boolean scanningTileEntities;
	public boolean spawnHostileMobs; // Spout private -> public
	public boolean spawnPeacefulMobs; // Spout private -> public
	private Set positionsToUpdate;
	private int soundCounter;
	int[] lightUpdateBlockList;
	private List entitiesWithinAABBExcludingEntity;
	public boolean multiplayerWorld;
	// Spout start
	public final SpoutcraftWorld world;

	// Spout end

	public WorldChunkManager getWorldChunkManager() {
		return this.worldProvider.worldChunkMgr;
	}

	public World(ISaveHandler var1, String var2, WorldProvider var3, WorldSettings var4) {
		this.heightShift = 7;
		this.xShift = this.heightShift + 4;
		this.worldHeight = 1 << this.heightShift;
		this.worldMaxY = this.worldHeight - 1;
		this.seaLevel = this.worldHeight / 2 - 1;
		this.scheduledUpdatesAreImmediate = false;
		this.loadedEntityList = new ArrayList();
		this.unloadedEntityList = new ArrayList();
		this.scheduledTickTreeSet = new TreeSet();
		this.scheduledTickSet = new HashSet();
		this.loadedTileEntityList = new ArrayList();
		this.addedTileEntityList = new ArrayList();
		this.entityRemoval = new ArrayList();
		this.playerEntities = new ArrayList();
		this.weatherEffects = new ArrayList();
		this.cloudColour = 16777215L;
		this.skylightSubtracted = 0;
		this.updateLCG = (new Random()).nextInt();
		this.DIST_HASH_MAGIC = 1013904223;
		this.lastLightningBolt = 0;
		this.lightningFlash = 0;
		this.editingBlocks = false;
		this.lockTimestamp = System.currentTimeMillis();
		// this.autosavePeriod = 40; //Spout
		this.rand = new Random();
		this.isNewWorld = false;
		this.worldAccesses = new ArrayList();
		this.collidingBoundingBoxes = new ArrayList();
		this.spawnHostileMobs = true;
		this.spawnPeacefulMobs = true;
		this.positionsToUpdate = new HashSet();
		this.soundCounter = this.rand.nextInt(12000);
		this.lightUpdateBlockList = new int['\u8000'];
		this.entitiesWithinAABBExcludingEntity = new ArrayList();
		this.multiplayerWorld = false;
		this.saveHandler = var1;
		this.worldInfo = new WorldInfo(var4, var2);
		this.worldProvider = var3;
		this.mapStorage = new MapStorage(var1);
		var3.registerWorld(this);
		this.chunkProvider = this.getChunkProvider();
		this.calculateInitialSkylight();
		this.calculateInitialWeather();
		// Spout start
		world = new SpoutcraftWorld(this);
		// Spout end
	}

	public World(World var1, WorldProvider var2) {
		this.heightShift = 7;
		this.xShift = this.heightShift + 4;
		this.worldHeight = 1 << this.heightShift;
		this.worldMaxY = this.worldHeight - 1;
		this.seaLevel = this.worldHeight / 2 - 1;
		this.scheduledUpdatesAreImmediate = false;
		this.loadedEntityList = new ArrayList();
		this.unloadedEntityList = new ArrayList();
		this.scheduledTickTreeSet = new TreeSet();
		this.scheduledTickSet = new HashSet();
		this.loadedTileEntityList = new ArrayList();
		this.addedTileEntityList = new ArrayList();
		this.entityRemoval = new ArrayList();
		this.playerEntities = new ArrayList();
		this.weatherEffects = new ArrayList();
		this.cloudColour = 16777215L;
		this.skylightSubtracted = 0;
		this.updateLCG = (new Random()).nextInt();
		this.DIST_HASH_MAGIC = 1013904223;
		this.lastLightningBolt = 0;
		this.lightningFlash = 0;
		this.editingBlocks = false;
		this.lockTimestamp = System.currentTimeMillis();
		// this.autosavePeriod = 40; //Spout
		this.rand = new Random();
		this.isNewWorld = false;
		this.worldAccesses = new ArrayList();
		this.collidingBoundingBoxes = new ArrayList();
		this.spawnHostileMobs = true;
		this.spawnPeacefulMobs = true;
		this.positionsToUpdate = new HashSet();
		this.soundCounter = this.rand.nextInt(12000);
		this.lightUpdateBlockList = new int['\u8000'];
		this.entitiesWithinAABBExcludingEntity = new ArrayList();
		this.multiplayerWorld = false;
		this.lockTimestamp = var1.lockTimestamp;
		this.saveHandler = var1.saveHandler;
		this.worldInfo = new WorldInfo(var1.worldInfo);
		
		//Spout start
		setMapHeight(worldInfo.getMapHeight());
		//Spout end
		
		this.mapStorage = new MapStorage(this.saveHandler);
		this.worldProvider = var2;
		var2.registerWorld(this);
		this.chunkProvider = this.getChunkProvider();
		this.calculateInitialSkylight();
		this.calculateInitialWeather();
		// Spout start
		world = new SpoutcraftWorld(this);
		// Spout end
	}

	//Spout start
	public World(ISaveHandler var1, String var2, WorldSettings var3, int height) {
		this(var1, var2, var3, (WorldProvider) null, height);
	}

	public World(ISaveHandler var1, String var2, WorldSettings var3, WorldProvider var4, int height) {
	//Spout end
		this.heightShift = 7;
		this.xShift = this.heightShift + 4;
		this.worldHeight = 1 << this.heightShift;
		this.worldMaxY = this.worldHeight - 1;
		this.seaLevel = this.worldHeight / 2 - 1;
		this.scheduledUpdatesAreImmediate = false;
		this.loadedEntityList = new ArrayList();
		this.unloadedEntityList = new ArrayList();
		this.scheduledTickTreeSet = new TreeSet();
		this.scheduledTickSet = new HashSet();
		this.loadedTileEntityList = new ArrayList();
		this.addedTileEntityList = new ArrayList();
		this.entityRemoval = new ArrayList();
		this.playerEntities = new ArrayList();
		this.weatherEffects = new ArrayList();
		this.cloudColour = 16777215L;
		this.skylightSubtracted = 0;
		this.updateLCG = (new Random()).nextInt();
		this.DIST_HASH_MAGIC = 1013904223;
		this.lastLightningBolt = 0;
		this.lightningFlash = 0;
		this.editingBlocks = false;
		this.lockTimestamp = System.currentTimeMillis();
		//this.autosavePeriod = 40; //Spout
		this.rand = new Random();
		this.isNewWorld = false;
		this.worldAccesses = new ArrayList();
		this.collidingBoundingBoxes = new ArrayList();
		this.spawnHostileMobs = true;
		this.spawnPeacefulMobs = true;
		this.positionsToUpdate = new HashSet();
		this.soundCounter = this.rand.nextInt(12000);
		this.lightUpdateBlockList = new int['\u8000'];
		this.entitiesWithinAABBExcludingEntity = new ArrayList();
		this.multiplayerWorld = false;
		this.saveHandler = var1;
		this.mapStorage = new MapStorage(var1);
		this.worldInfo = var1.loadWorldInfo();
		this.isNewWorld = this.worldInfo == null;
		if (var4 != null) {
			this.worldProvider = var4;
		}
		else if (this.worldInfo != null && this.worldInfo.getDimension() != 0) {
			this.worldProvider = WorldProvider.getProviderForDimension(this.worldInfo.getDimension());
		}
		else {
			this.worldProvider = WorldProvider.getProviderForDimension(0);
		}

		boolean var5 = false;
		if (this.worldInfo == null) {
			this.worldInfo = new WorldInfo(var3, var2);
			var5 = true;
			//Spout start
			worldInfo.setMapHeight(height);
			//Spout end
		} else {
			this.worldInfo.setWorldName(var2);
		}
		
		//Spout start
		setMapHeight(worldInfo.getMapHeight());
		//Spout end

		this.worldProvider.registerWorld(this);
		this.chunkProvider = this.getChunkProvider();
		if (var5) {
			this.getInitialSpawnLocation();
		}

		this.calculateInitialSkylight();
		this.calculateInitialWeather();
		//Spout start
		world = new SpoutcraftWorld(this);
		//Spout end
	}

	protected IChunkProvider getChunkProvider() {
		IChunkLoader var1 = this.saveHandler.getChunkLoader(this.worldProvider);
		return new ChunkProvider(this, var1, this.worldProvider.getChunkProvider());
	}

	protected void getInitialSpawnLocation() {
		if (!this.worldProvider.canRespawnHere()) {
			this.worldInfo.setSpawn(0, this.worldProvider.func_46066_g(), 0);
		}
		else {
		this.findingSpawnPoint = true;
		WorldChunkManager var1 = this.getWorldChunkManager();
		List var2 = var1.getBiomesToSpawnIn();
		Random var3 = new Random(this.getWorldSeed());
		ChunkPosition var4 = var1.func_35556_a(0, 0, 256, var2, var3);
		int var5 = 0;
			int var6 = this.worldProvider.func_46066_g();
		int var7 = 0;
		if (var4 != null) {
			var5 = var4.x;
			var7 = var4.z;
			}
			else {
			System.out.println("Unable to find spawn biome");
		}

		int var8 = 0;

		while (!this.worldProvider.canCoordinateBeSpawn(var5, var7)) {
			var5 += var3.nextInt(64) - var3.nextInt(64);
			var7 += var3.nextInt(64) - var3.nextInt(64);
			++var8;
			if (var8 == 1000) {
				break;
			}
		}

		this.worldInfo.setSpawn(var5, var6, var7);
		this.findingSpawnPoint = false;
	}
	}

	public ChunkCoordinates getEntrancePortalLocation() {
		return this.worldProvider.getEntrancePortalLocation();
	}

	public void setSpawnLocation() {
		if (this.worldInfo.getSpawnY() <= 0) {
			this.worldInfo.setSpawnY(this.worldHeight / 2);
		}

		int var1 = this.worldInfo.getSpawnX();
		int var2 = this.worldInfo.getSpawnZ();
		int var3 = 0;

		while (this.getFirstUncoveredBlock(var1, var2) == 0) {
			var1 += this.rand.nextInt(8) - this.rand.nextInt(8);
			var2 += this.rand.nextInt(8) - this.rand.nextInt(8);
			++var3;
			if (var3 == 10000) {
				break;
			}
		}

		this.worldInfo.setSpawnX(var1);
		this.worldInfo.setSpawnZ(var2);
	}

	public int getFirstUncoveredBlock(int var1, int var2) {
		int var3;
		for (var3 = this.seaLevel; !this.isAirBlock(var1, var3 + 1, var2); ++var3) {
			;
		}

		return this.getBlockId(var1, var3, var2);
	}

	public void emptyMethod1() {}

	public void spawnPlayerWithLoadedChunks(EntityPlayer var1) {
		try {
			NBTTagCompound var2 = this.worldInfo.getPlayerNBTTagCompound();
			if (var2 != null) {
				var1.readFromNBT(var2);
				this.worldInfo.setPlayerNBTTagCompound((NBTTagCompound) null);
			}

			if (this.chunkProvider instanceof ChunkProviderLoadOrGenerate) {
				ChunkProviderLoadOrGenerate var3 = (ChunkProviderLoadOrGenerate) this.chunkProvider;
				int var4 = MathHelper.floor_float((float) ((int) var1.posX)) >> 4;
				int var5 = MathHelper.floor_float((float) ((int) var1.posZ)) >> 4;
				var3.setCurrentChunkOver(var4, var5);
			}

			this.spawnEntityInWorld(var1);
		}
		catch (Exception var6) {
			var6.printStackTrace();
		}

	}

	public void saveWorld(boolean var1, IProgressUpdate var2) {
		if (this.chunkProvider.canSave()) {
			if (var2 != null) {
				var2.displaySavingString("Saving level");
			}

			this.saveLevel();
			if (var2 != null) {
				var2.displayLoadingString("Saving chunks");
			}

			this.chunkProvider.saveChunks(var1, var2);
		}
	}

	private void saveLevel() {
		this.checkSessionLock();
		this.saveHandler.saveWorldInfoAndPlayer(this.worldInfo, this.playerEntities);
		this.mapStorage.saveAllData();
	}

	public boolean quickSaveWorld(int var1) {
		if (!this.chunkProvider.canSave()) {
			return true;
		}
		else {
			if (var1 == 0) {
				this.saveLevel();
			}

			return this.chunkProvider.saveChunks(false, (IProgressUpdate) null);
		}
	}

	public int getBlockId(int var1, int var2, int var3) {
		return var1 >= -30000000 && var3 >= -30000000 && var1 < 30000000 && var3 < 30000000 ? (var2 < 0 ? 0 : (var2 >= this.worldHeight ? 0 : this.getChunkFromChunkCoords(var1 >> 4, var3 >> 4).getBlockID(var1 & 15, var2, var3 & 15))) : 0;
	}

	public boolean isAirBlock(int var1, int var2, int var3) {
		return this.getBlockId(var1, var2, var3) == 0;
	}

	public boolean blockExists(int var1, int var2, int var3) {
		return var2 >= 0 && var2 < this.worldHeight ? this.chunkExists(var1 >> 4, var3 >> 4) : false;
	}

	public boolean doChunksNearChunkExist(int var1, int var2, int var3, int var4) {
		return this.checkChunksExist(var1 - var4, var2 - var4, var3 - var4, var1 + var4, var2 + var4, var3 + var4);
	}

	public boolean checkChunksExist(int var1, int var2, int var3, int var4, int var5, int var6) {
		if (var5 >= 0 && var2 < this.worldHeight) {
			var1 >>= 4;
			var2 >>= 4;
			var3 >>= 4;
			var4 >>= 4;
			var5 >>= 4;
			var6 >>= 4;

			for (int var7 = var1; var7 <= var4; ++var7) {
				for (int var8 = var3; var8 <= var6; ++var8) {
					if (!this.chunkExists(var7, var8)) {
						return false;
					}
				}
			}

			return true;
		}
		else {
			return false;
		}
	}

	private boolean chunkExists(int var1, int var2) {
		return this.chunkProvider.chunkExists(var1, var2);
	}

	public Chunk getChunkFromBlockCoords(int var1, int var2) {
		return this.getChunkFromChunkCoords(var1 >> 4, var2 >> 4);
	}

	public Chunk getChunkFromChunkCoords(int var1, int var2) {
		return this.chunkProvider.provideChunk(var1, var2);
	}

	public boolean setBlockAndMetadata(int var1, int var2, int var3, int var4, int var5) {
		if (var1 >= -30000000 && var3 >= -30000000 && var1 < 30000000 && var3 < 30000000) {
			if (var2 < 0) {
				return false;
			}
			else if (var2 >= this.worldHeight) {
				return false;
			}
			else {
				Chunk var6 = this.getChunkFromChunkCoords(var1 >> 4, var3 >> 4);
				boolean var7 = var6.setBlockIDWithMetadata(var1 & 15, var2, var3 & 15, var4, var5);
				this.updateAllLightTypes(var1, var2, var3);
				return var7;
			}
		}
		else {
			return false;
		}
	}

	public boolean setBlock(int var1, int var2, int var3, int var4) {
		if (var1 >= -30000000 && var3 >= -30000000 && var1 < 30000000 && var3 < 30000000) {
			if (var2 < 0) {
				return false;
			}
			else if (var2 >= this.worldHeight) {
				return false;
			}
			else {
				Chunk var5 = this.getChunkFromChunkCoords(var1 >> 4, var3 >> 4);
				boolean var6 = var5.setBlockID(var1 & 15, var2, var3 & 15, var4);
				this.updateAllLightTypes(var1, var2, var3);
				return var6;
			}
		}
		else {
			return false;
		}
	}

	public Material getBlockMaterial(int var1, int var2, int var3) {
		int var4 = this.getBlockId(var1, var2, var3);
		return var4 == 0 ? Material.air : Block.blocksList[var4].blockMaterial;
	}

	public int getBlockMetadata(int var1, int var2, int var3) {
		if (var1 >= -30000000 && var3 >= -30000000 && var1 < 30000000 && var3 < 30000000) {
			if (var2 < 0) {
				return 0;
			}
			else if (var2 >= this.worldHeight) {
				return 0;
			}
			else {
				Chunk var4 = this.getChunkFromChunkCoords(var1 >> 4, var3 >> 4);
				var1 &= 15;
				var3 &= 15;
				return var4.getBlockMetadata(var1, var2, var3);
			}
		}
		else {
			return 0;
		}
	}

	public void setBlockMetadataWithNotify(int var1, int var2, int var3, int var4) {
		if (this.setBlockMetadata(var1, var2, var3, var4)) {
			int var5 = this.getBlockId(var1, var2, var3);
			if (Block.requiresSelfNotify[var5 & 255]) {
				this.notifyBlockChange(var1, var2, var3, var5);
			}
			else {
				this.notifyBlocksOfNeighborChange(var1, var2, var3, var5);
			}
		}

	}

	public boolean setBlockMetadata(int var1, int var2, int var3, int var4) {
		if (var1 >= -30000000 && var3 >= -30000000 && var1 < 30000000 && var3 < 30000000) {
			if (var2 < 0) {
				return false;
			}
			else if (var2 >= this.worldHeight) {
				return false;
			}
			else {
				Chunk var5 = this.getChunkFromChunkCoords(var1 >> 4, var3 >> 4);
				var1 &= 15;
				var3 &= 15;
				return var5.setBlockMetadata(var1, var2, var3, var4);
			}
		}
		else {
			return false;
		}
	}

	public boolean setBlockWithNotify(int var1, int var2, int var3, int var4) {
		if (this.setBlock(var1, var2, var3, var4)) {
			this.notifyBlockChange(var1, var2, var3, var4);
			return true;
		}
		else {
			return false;
		}
	}

	public boolean setBlockAndMetadataWithNotify(int var1, int var2, int var3, int var4, int var5) {
		if (this.setBlockAndMetadata(var1, var2, var3, var4, var5)) {
			this.notifyBlockChange(var1, var2, var3, var4);
			return true;
		}
		else {
			return false;
		}
	}

	public void markBlockNeedsUpdate(int var1, int var2, int var3) {
		for (int var4 = 0; var4 < this.worldAccesses.size(); ++var4) {
			((IWorldAccess) this.worldAccesses.get(var4)).markBlockAndNeighborsNeedsUpdate(var1, var2, var3);
		}

	}

	protected void notifyBlockChange(int var1, int var2, int var3, int var4) {
		this.markBlockNeedsUpdate(var1, var2, var3);
		this.notifyBlocksOfNeighborChange(var1, var2, var3, var4);
	}

	public void markBlocksDirtyVertical(int var1, int var2, int var3, int var4) {
		int var5;
		if (var3 > var4) {
			var5 = var4;
			var4 = var3;
			var3 = var5;
		}

		if (!this.worldProvider.hasNoSky) {
			for (var5 = var3; var5 <= var4; ++var5) {
				this.updateLightByType(EnumSkyBlock.Sky, var1, var5, var2);
			}
		}

		this.markBlocksDirty(var1, var3, var2, var1, var4, var2);
	}

	public void markBlockAsNeedsUpdate(int var1, int var2, int var3) {
		for (int var4 = 0; var4 < this.worldAccesses.size(); ++var4) {
			((IWorldAccess) this.worldAccesses.get(var4)).markBlockRangeNeedsUpdate(var1, var2, var3, var1, var2, var3);
		}

	}

	public void markBlocksDirty(int var1, int var2, int var3, int var4, int var5, int var6) {
		for (int var7 = 0; var7 < this.worldAccesses.size(); ++var7) {
			((IWorldAccess) this.worldAccesses.get(var7)).markBlockRangeNeedsUpdate(var1, var2, var3, var4, var5, var6);
		}

	}

	public void notifyBlocksOfNeighborChange(int var1, int var2, int var3, int var4) {
		this.notifyBlockOfNeighborChange(var1 - 1, var2, var3, var4);
		this.notifyBlockOfNeighborChange(var1 + 1, var2, var3, var4);
		this.notifyBlockOfNeighborChange(var1, var2 - 1, var3, var4);
		this.notifyBlockOfNeighborChange(var1, var2 + 1, var3, var4);
		this.notifyBlockOfNeighborChange(var1, var2, var3 - 1, var4);
		this.notifyBlockOfNeighborChange(var1, var2, var3 + 1, var4);
	}

	private void notifyBlockOfNeighborChange(int var1, int var2, int var3, int var4) {
		if (!this.editingBlocks && !this.multiplayerWorld) {
			Block var5 = Block.blocksList[this.getBlockId(var1, var2, var3)];
			if (var5 != null) {
				var5.onNeighborBlockChange(this, var1, var2, var3, var4);
			}

		}
	}

	public boolean canBlockSeeTheSky(int var1, int var2, int var3) {
		return this.getChunkFromChunkCoords(var1 >> 4, var3 >> 4).canBlockSeeTheSky(var1 & 15, var2, var3 & 15);
	}

	public int getFullBlockLightValue(int var1, int var2, int var3) {
		if (var2 < 0) {
			return 0;
		}
		else {
			if (var2 >= this.worldHeight) {
				var2 = this.worldHeight - 1;
			}

			return this.getChunkFromChunkCoords(var1 >> 4, var3 >> 4).getBlockLightValue(var1 & 15, var2, var3 & 15, 0);
		}
	}

	public int getBlockLightValue(int var1, int var2, int var3) {
		return this.getBlockLightValue_do(var1, var2, var3, true);
	}

	public int getBlockLightValue_do(int var1, int var2, int var3, boolean var4) {
		if (var1 >= -30000000 && var3 >= -30000000 && var1 < 30000000 && var3 < 30000000) {
			if (var4) {
				int var5 = this.getBlockId(var1, var2, var3);
				if (var5 == Block.stairSingle.blockID || var5 == Block.tilledField.blockID || var5 == Block.stairCompactCobblestone.blockID || var5 == Block.stairCompactPlanks.blockID) {
					int var6 = this.getBlockLightValue_do(var1, var2 + 1, var3, false);
					int var7 = this.getBlockLightValue_do(var1 + 1, var2, var3, false);
					int var8 = this.getBlockLightValue_do(var1 - 1, var2, var3, false);
					int var9 = this.getBlockLightValue_do(var1, var2, var3 + 1, false);
					int var10 = this.getBlockLightValue_do(var1, var2, var3 - 1, false);
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

			if (var2 < 0) {
				return 0;
			}
			else {
				if (var2 >= this.worldHeight) {
					var2 = this.worldHeight - 1;
				}

				Chunk var11 = this.getChunkFromChunkCoords(var1 >> 4, var3 >> 4);
				var1 &= 15;
				var3 &= 15;
				return var11.getBlockLightValue(var1, var2, var3, this.skylightSubtracted);
			}
		}
		else {
			return 15;
		}
	}

	public int getHeightValue(int var1, int var2) {
		if (var1 >= -30000000 && var2 >= -30000000 && var1 < 30000000 && var2 < 30000000) {
			if (!this.chunkExists(var1 >> 4, var2 >> 4)) {
				return 0;
			}
			else {
				Chunk var3 = this.getChunkFromChunkCoords(var1 >> 4, var2 >> 4);
				return var3.getHeightValue(var1 & 15, var2 & 15);
			}
		}
		else {
			return 0;
		}
	}

	public int getSkyBlockTypeBrightness(EnumSkyBlock var1, int var2, int var3, int var4) {
		if (this.worldProvider.hasNoSky && var1 == EnumSkyBlock.Sky) {
			return 0;
		}
		else {
			if (var3 < 0) {
				var3 = 0;
			}

			if (var3 >= this.worldHeight && var1 == EnumSkyBlock.Sky) {
				return 15;
			}
			else if (var3 >= 0 && var3 < this.worldHeight && var2 >= -30000000 && var4 >= -30000000 && var2 < 30000000 && var4 < 30000000) {
				int var5 = var2 >> 4;
				int var6 = var4 >> 4;
				if (!this.chunkExists(var5, var6)) {
					return 0;
				}
				else if (Block.useNeighborBrightness[this.getBlockId(var2, var3, var4)]) {
					int var12 = this.getSavedLightValue(var1, var2, var3 + 1, var4);
					int var8 = this.getSavedLightValue(var1, var2 + 1, var3, var4);
					int var9 = this.getSavedLightValue(var1, var2 - 1, var3, var4);
					int var10 = this.getSavedLightValue(var1, var2, var3, var4 + 1);
					int var11 = this.getSavedLightValue(var1, var2, var3, var4 - 1);
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
				}
				else {
					Chunk var7 = this.getChunkFromChunkCoords(var5, var6);
					return var7.getSavedLightValue(var1, var2 & 15, var3, var4 & 15);
				}
			}
			else {
				return var1.defaultLightValue;
			}
		}
	}

	public int getSavedLightValue(EnumSkyBlock var1, int var2, int var3, int var4) {
		if (var3 < 0) {
			var3 = 0;
		}

		if (var3 >= this.worldHeight) {
			var3 = this.worldHeight - 1;
		}

		if (var3 >= 0 && var3 < this.worldHeight && var2 >= -30000000 && var4 >= -30000000 && var2 < 30000000 && var4 < 30000000) {
			int var5 = var2 >> 4;
			int var6 = var4 >> 4;
			if (!this.chunkExists(var5, var6)) {
				return 0;
			}
			else {
				Chunk var7 = this.getChunkFromChunkCoords(var5, var6);
				return var7.getSavedLightValue(var1, var2 & 15, var3, var4 & 15);
			}
		}
		else {
			return var1.defaultLightValue;
		}
	}

	public void setLightValue(EnumSkyBlock var1, int var2, int var3, int var4, int var5) {
		if (var2 >= -30000000 && var4 >= -30000000 && var2 < 30000000 && var4 < 30000000) {
			if (var3 >= 0) {
				if (var3 < this.worldHeight) {
					if (this.chunkExists(var2 >> 4, var4 >> 4)) {
						Chunk var6 = this.getChunkFromChunkCoords(var2 >> 4, var4 >> 4);
						var6.setLightValue(var1, var2 & 15, var3, var4 & 15, var5);

						for (int var7 = 0; var7 < this.worldAccesses.size(); ++var7) {
							((IWorldAccess) this.worldAccesses.get(var7)).markBlockAndNeighborsNeedsUpdate(var2, var3, var4);
						}

					}
				}
			}
		}
	}

	public int getLightBrightnessForSkyBlocks(int var1, int var2, int var3, int var4) {
		int var5 = this.getSkyBlockTypeBrightness(EnumSkyBlock.Sky, var1, var2, var3);
		int var6 = this.getSkyBlockTypeBrightness(EnumSkyBlock.Block, var1, var2, var3);
		if (var6 < var4) {
			var6 = var4;
		}

		return var5 << 20 | var6 << 4;
	}

	public float getBrightness(int var1, int var2, int var3, int var4) {
		int var5 = this.getBlockLightValue(var1, var2, var3);
		if (var5 < var4) {
			var5 = var4;
		}

		return this.worldProvider.lightBrightnessTable[var5];
	}

	public float getLightBrightness(int var1, int var2, int var3) {
		return this.worldProvider.lightBrightnessTable[this.getBlockLightValue(var1, var2, var3)];
	}

	public boolean isDaytime() {
		return this.skylightSubtracted < 4;
	}

	public MovingObjectPosition rayTraceBlocks(Vec3D var1, Vec3D var2) {
		return this.rayTraceBlocks_do_do(var1, var2, false, false);
	}

	public MovingObjectPosition rayTraceBlocks_do(Vec3D var1, Vec3D var2, boolean var3) {
		return this.rayTraceBlocks_do_do(var1, var2, var3, false);
	}

	public MovingObjectPosition rayTraceBlocks_do_do(Vec3D var1, Vec3D var2, boolean var3, boolean var4) {
		if (!Double.isNaN(var1.xCoord) && !Double.isNaN(var1.yCoord) && !Double.isNaN(var1.zCoord)) {
			if (!Double.isNaN(var2.xCoord) && !Double.isNaN(var2.yCoord) && !Double.isNaN(var2.zCoord)) {
				int var5 = MathHelper.floor_double(var2.xCoord);
				int var6 = MathHelper.floor_double(var2.yCoord);
				int var7 = MathHelper.floor_double(var2.zCoord);
				int var8 = MathHelper.floor_double(var1.xCoord);
				int var9 = MathHelper.floor_double(var1.yCoord);
				int var10 = MathHelper.floor_double(var1.zCoord);
				int var11 = this.getBlockId(var8, var9, var10);
				int var12 = this.getBlockMetadata(var8, var9, var10);
				Block var13 = Block.blocksList[var11];
				if ((!var4 || var13 == null || var13.getCollisionBoundingBoxFromPool(this, var8, var9, var10) != null) && var11 > 0 && var13.canCollideCheck(var12, var3)) {
					MovingObjectPosition var14 = var13.collisionRayTrace(this, var8, var9, var10, var1, var2);
					if (var14 != null) {
						return var14;
					}
				}

				var11 = 200;

				while (var11-- >= 0) {
					if (Double.isNaN(var1.xCoord) || Double.isNaN(var1.yCoord) || Double.isNaN(var1.zCoord)) {
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
						var15 = (double) var8 + 1.0D;
					}
					else if (var5 < var8) {
						var15 = (double) var8 + 0.0D;
					}
					else {
						var39 = false;
					}

					if (var6 > var9) {
						var17 = (double) var9 + 1.0D;
					}
					else if (var6 < var9) {
						var17 = (double) var9 + 0.0D;
					}
					else {
						var40 = false;
					}

					if (var7 > var10) {
						var19 = (double) var10 + 1.0D;
					}
					else if (var7 < var10) {
						var19 = (double) var10 + 0.0D;
					}
					else {
						var41 = false;
					}

					double var21 = 999.0D;
					double var23 = 999.0D;
					double var25 = 999.0D;
					double var27 = var2.xCoord - var1.xCoord;
					double var29 = var2.yCoord - var1.yCoord;
					double var31 = var2.zCoord - var1.zCoord;
					if (var39) {
						var21 = (var15 - var1.xCoord) / var27;
					}

					if (var40) {
						var23 = (var17 - var1.yCoord) / var29;
					}

					if (var41) {
						var25 = (var19 - var1.zCoord) / var31;
					}

					boolean var33 = false;
					byte var42;
					if (var21 < var23 && var21 < var25) {
						if (var5 > var8) {
							var42 = 4;
						}
						else {
							var42 = 5;
						}

						var1.xCoord = var15;
						var1.yCoord += var29 * var21;
						var1.zCoord += var31 * var21;
					}
					else if (var23 < var25) {
						if (var6 > var9) {
							var42 = 0;
						}
						else {
							var42 = 1;
						}

						var1.xCoord += var27 * var23;
						var1.yCoord = var17;
						var1.zCoord += var31 * var23;
					}
					else {
						if (var7 > var10) {
							var42 = 2;
						}
						else {
							var42 = 3;
						}

						var1.xCoord += var27 * var25;
						var1.yCoord += var29 * var25;
						var1.zCoord = var19;
					}

					Vec3D var34 = Vec3D.createVector(var1.xCoord, var1.yCoord, var1.zCoord);
					var8 = (int) (var34.xCoord = (double) MathHelper.floor_double(var1.xCoord));
					if (var42 == 5) {
						--var8;
						++var34.xCoord;
					}

					var9 = (int) (var34.yCoord = (double) MathHelper.floor_double(var1.yCoord));
					if (var42 == 1) {
						--var9;
						++var34.yCoord;
					}

					var10 = (int) (var34.zCoord = (double) MathHelper.floor_double(var1.zCoord));
					if (var42 == 3) {
						--var10;
						++var34.zCoord;
					}

					int var35 = this.getBlockId(var8, var9, var10);
					int var36 = this.getBlockMetadata(var8, var9, var10);
					Block var37 = Block.blocksList[var35];
					if ((!var4 || var37 == null || var37.getCollisionBoundingBoxFromPool(this, var8, var9, var10) != null) && var35 > 0 && var37.canCollideCheck(var36, var3)) {
						MovingObjectPosition var38 = var37.collisionRayTrace(this, var8, var9, var10, var1, var2);
						if (var38 != null) {
							return var38;
						}
					}
				}

				return null;
			}
			else {
				return null;
			}
		}
		else {
			return null;
		}
	}

	public void playSoundAtEntity(Entity var1, String var2, float var3, float var4) {
		for (int var5 = 0; var5 < this.worldAccesses.size(); ++var5) {
			((IWorldAccess) this.worldAccesses.get(var5)).playSound(var2, var1.posX, var1.posY - (double) var1.yOffset, var1.posZ, var3, var4);
		}

	}

	public void playSoundEffect(double var1, double var3, double var5, String var7, float var8, float var9) {
		for (int var10 = 0; var10 < this.worldAccesses.size(); ++var10) {
			((IWorldAccess) this.worldAccesses.get(var10)).playSound(var7, var1, var3, var5, var8, var9);
		}

	}

	public void playRecord(String var1, int var2, int var3, int var4) {
		for (int var5 = 0; var5 < this.worldAccesses.size(); ++var5) {
			((IWorldAccess) this.worldAccesses.get(var5)).playRecord(var1, var2, var3, var4);
		}

	}

	public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12) {
		for (int var14 = 0; var14 < this.worldAccesses.size(); ++var14) {
			((IWorldAccess) this.worldAccesses.get(var14)).spawnParticle(var1, var2, var4, var6, var8, var10, var12);
		}

	}

	public boolean addWeatherEffect(Entity var1) {
		this.weatherEffects.add(var1);
		return true;
	}

	public boolean spawnEntityInWorld(Entity var1) {
		int var2 = MathHelper.floor_double(var1.posX / 16.0D);
		int var3 = MathHelper.floor_double(var1.posZ / 16.0D);
		boolean var4 = false;
		if (var1 instanceof EntityPlayer) {
			var4 = true;
		}

		if (!var4 && !this.chunkExists(var2, var3)) {
			return false;
		}
		else {
			if (var1 instanceof EntityPlayer) {
				EntityPlayer var5 = (EntityPlayer) var1;
				this.playerEntities.add(var5);
				this.updateAllPlayersSleepingFlag();
			}

			this.getChunkFromChunkCoords(var2, var3).addEntity(var1);
			this.loadedEntityList.add(var1);
			this.obtainEntitySkin(var1);
			return true;
		}
	}

	public void obtainEntitySkin(Entity var1) { // Spout protected -> public
		for (int var2 = 0; var2 < this.worldAccesses.size(); ++var2) {
			((IWorldAccess) this.worldAccesses.get(var2)).obtainEntitySkin(var1);
		}

	}

	public void releaseEntitySkin(Entity var1) { // Spout protected -> public
		for (int var2 = 0; var2 < this.worldAccesses.size(); ++var2) {
			((IWorldAccess) this.worldAccesses.get(var2)).releaseEntitySkin(var1);
		}

	}

	public void setEntityDead(Entity var1) {
		if (var1.riddenByEntity != null) {
			var1.riddenByEntity.mountEntity((Entity) null);
		}

		if (var1.ridingEntity != null) {
			var1.mountEntity((Entity) null);
		}

		var1.setEntityDead();
		if (var1 instanceof EntityPlayer) {
			this.playerEntities.remove((EntityPlayer) var1);
			this.updateAllPlayersSleepingFlag();
		}

	}

	public void addWorldAccess(IWorldAccess var1) {
		this.worldAccesses.add(var1);
	}

	public void removeWorldAccess(IWorldAccess var1) {
		this.worldAccesses.remove(var1);
	}

	public List getCollidingBoundingBoxes(Entity var1, AxisAlignedBB var2) {
		this.collidingBoundingBoxes.clear();
		int var3 = MathHelper.floor_double(var2.minX);
		int var4 = MathHelper.floor_double(var2.maxX + 1.0D);
		int var5 = MathHelper.floor_double(var2.minY);
		int var6 = MathHelper.floor_double(var2.maxY + 1.0D);
		int var7 = MathHelper.floor_double(var2.minZ);
		int var8 = MathHelper.floor_double(var2.maxZ + 1.0D);

		for (int var9 = var3; var9 < var4; ++var9) {
			for (int var10 = var7; var10 < var8; ++var10) {
				if (this.blockExists(var9, this.worldHeight / 2, var10)) {
					for (int var11 = var5 - 1; var11 < var6; ++var11) {
						Block var12 = Block.blocksList[this.getBlockId(var9, var11, var10)];
						if (var12 != null) {
							var12.getCollidingBoundingBoxes(this, var9, var11, var10, var2, this.collidingBoundingBoxes);
						}
					}
				}
			}
		}

		double var14 = 0.25D;
		List var16 = this.getEntitiesWithinAABBExcludingEntity(var1, var2.expand(var14, var14, var14));

		for (int var15 = 0; var15 < var16.size(); ++var15) {
			AxisAlignedBB var13 = ((Entity) var16.get(var15)).getBoundingBox();
			if (var13 != null && var13.intersectsWith(var2)) {
				this.collidingBoundingBoxes.add(var13);
			}

			var13 = var1.getCollisionBox((Entity) var16.get(var15));
			if (var13 != null && var13.intersectsWith(var2)) {
				this.collidingBoundingBoxes.add(var13);
			}
		}

		return this.collidingBoundingBoxes;
	}

	public int calculateSkylightSubtracted(float var1) {
		float var2 = this.getCelestialAngle(var1);
		float var3 = 1.0F - (MathHelper.cos(var2 * 3.1415927F * 2.0F) * 2.0F + 0.5F);
		if (var3 < 0.0F) {
			var3 = 0.0F;
		}

		if (var3 > 1.0F) {
			var3 = 1.0F;
		}

		var3 = 1.0F - var3;
		var3 = (float) ((double) var3 * (1.0D - (double) (this.getRainStrength(var1) * 5.0F) / 16.0D));
		var3 = (float) ((double) var3 * (1.0D - (double) (this.getWeightedThunderStrength(var1) * 5.0F) / 16.0D));
		var3 = 1.0F - var3;
		return (int) (var3 * 11.0F);
	}

	public float func_35464_b(float var1) {
		float var2 = this.getCelestialAngle(var1);
		float var3 = 1.0F - (MathHelper.cos(var2 * 3.1415927F * 2.0F) * 2.0F + 0.2F);
		if (var3 < 0.0F) {
			var3 = 0.0F;
		}

		if (var3 > 1.0F) {
			var3 = 1.0F;
		}

		var3 = 1.0F - var3;
		var3 = (float) ((double) var3 * (1.0D - (double) (this.getRainStrength(var1) * 5.0F) / 16.0D));
		var3 = (float) ((double) var3 * (1.0D - (double) (this.getWeightedThunderStrength(var1) * 5.0F) / 16.0D));
		return var3 * 0.8F + 0.2F;
	}

	public Vec3D getSkyColor(Entity var1, float var2) {
		float var3 = this.getCelestialAngle(var2);
		float var4 = MathHelper.cos(var3 * 3.1415927F * 2.0F) * 2.0F + 0.5F;
		if (var4 < 0.0F) {
			var4 = 0.0F;
		}

		if (var4 > 1.0F) {
			var4 = 1.0F;
		}

		int var5 = MathHelper.floor_double(var1.posX);
		int var6 = MathHelper.floor_double(var1.posZ);
		float var7 = this.getWorldChunkManager().getTemperature(var5, 64, var6);
		int var8 = this.getWorldChunkManager().getBiomeGenAt(var5, var6).getSkyColorByTemp(var7);
		float var9 = (float) (var8 >> 16 & 255) / 255.0F;
		float var10 = (float) (var8 >> 8 & 255) / 255.0F;
		float var11 = (float) (var8 & 255) / 255.0F;
		var9 *= var4;
		var10 *= var4;
		var11 *= var4;
		float var12 = this.getRainStrength(var2);
		float var13;
		float var14;
		if (var12 > 0.0F) {
			var13 = (var9 * 0.3F + var10 * 0.59F + var11 * 0.11F) * 0.6F;
			var14 = 1.0F - var12 * 0.75F;
			var9 = var9 * var14 + var13 * (1.0F - var14);
			var10 = var10 * var14 + var13 * (1.0F - var14);
			var11 = var11 * var14 + var13 * (1.0F - var14);
		}

		var13 = this.getWeightedThunderStrength(var2);
		if (var13 > 0.0F) {
			var14 = (var9 * 0.3F + var10 * 0.59F + var11 * 0.11F) * 0.2F;
			float var15 = 1.0F - var13 * 0.75F;
			var9 = var9 * var15 + var14 * (1.0F - var15);
			var10 = var10 * var15 + var14 * (1.0F - var15);
			var11 = var11 * var15 + var14 * (1.0F - var15);
		}

		if (this.lightningFlash > 0) {
			var14 = (float) this.lightningFlash - var2;
			if (var14 > 1.0F) {
				var14 = 1.0F;
			}

			var14 *= 0.45F;
			var9 = var9 * (1.0F - var14) + 0.8F * var14;
			var10 = var10 * (1.0F - var14) + 0.8F * var14;
			var11 = var11 * (1.0F - var14) + 1.0F * var14;
		}

		return Vec3D.createVector((double) var9, (double) var10, (double) var11);
	}

	public float getCelestialAngle(float var1) {
		return this.worldProvider.calculateCelestialAngle(this.worldInfo.getWorldTime(), var1);
	}

	public int func_40475_d(float var1) {
		return this.worldProvider.func_40470_b(this.worldInfo.getWorldTime(), var1);
	}

	public float getCelestialAngleRadians(float var1) {
		float var2 = this.getCelestialAngle(var1);
		return var2 * 3.1415927F * 2.0F;
	}

	public Vec3D drawClouds(float var1) {
		float var2 = this.getCelestialAngle(var1);
		float var3 = MathHelper.cos(var2 * 3.1415927F * 2.0F) * 2.0F + 0.5F;
		if (var3 < 0.0F) {
			var3 = 0.0F;
		}

		if (var3 > 1.0F) {
			var3 = 1.0F;
		}

		float var4 = (float) (this.cloudColour >> 16 & 255L) / 255.0F;
		float var5 = (float) (this.cloudColour >> 8 & 255L) / 255.0F;
		float var6 = (float) (this.cloudColour & 255L) / 255.0F;
		float var7 = this.getRainStrength(var1);
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
		var8 = this.getWeightedThunderStrength(var1);
		if (var8 > 0.0F) {
			var9 = (var4 * 0.3F + var5 * 0.59F + var6 * 0.11F) * 0.2F;
			float var10 = 1.0F - var8 * 0.95F;
			var4 = var4 * var10 + var9 * (1.0F - var10);
			var5 = var5 * var10 + var9 * (1.0F - var10);
			var6 = var6 * var10 + var9 * (1.0F - var10);
		}

		return Vec3D.createVector((double) var4, (double) var5, (double) var6);
	}

	public Vec3D getFogColor(float var1) {
		float var2 = this.getCelestialAngle(var1);
		return this.worldProvider.getFogColor(var2, var1);
	}

	public int getPrecipitationHeight(int var1, int var2) {
		return this.getChunkFromBlockCoords(var1, var2).getPrecipitationHeight(var1 & 15, var2 & 15);
	}

	public int getTopSolidOrLiquidBlock(int var1, int var2) {
		Chunk var3 = this.getChunkFromBlockCoords(var1, var2);
		int var4 = this.worldHeight - 1;
		var1 &= 15;

		for (var2 &= 15; var4 > 0; --var4) {
			int var5 = var3.getBlockID(var1, var4, var2);
			if (var5 != 0 && Block.blocksList[var5].blockMaterial.getIsSolid() && Block.blocksList[var5].blockMaterial != Material.leaves) {
				return var4 + 1;
			}
		}

		return -1;
	}

	public float getStarBrightness(float var1) {
		float var2 = this.getCelestialAngle(var1);
		float var3 = 1.0F - (MathHelper.cos(var2 * 3.1415927F * 2.0F) * 2.0F + 0.75F);
		if (var3 < 0.0F) {
			var3 = 0.0F;
		}

		if (var3 > 1.0F) {
			var3 = 1.0F;
		}

		return var3 * var3 * 0.5F;
	}

	public void scheduleBlockUpdate(int var1, int var2, int var3, int var4, int var5) {
		NextTickListEntry var6 = new NextTickListEntry(var1, var2, var3, var4);
		byte var7 = 8;
		if (this.scheduledUpdatesAreImmediate) {
			if (this.checkChunksExist(var6.xCoord - var7, var6.yCoord - var7, var6.zCoord - var7, var6.xCoord + var7, var6.yCoord + var7, var6.zCoord + var7)) {
				int var8 = this.getBlockId(var6.xCoord, var6.yCoord, var6.zCoord);
				if (var8 == var6.blockID && var8 > 0) {
					Block.blocksList[var8].updateTick(this, var6.xCoord, var6.yCoord, var6.zCoord, this.rand);
				}
			}

		}
		else {
			if (this.checkChunksExist(var1 - var7, var2 - var7, var3 - var7, var1 + var7, var2 + var7, var3 + var7)) {
				if (var4 > 0) {
					var6.setScheduledTime((long) var5 + this.worldInfo.getWorldTime());
				}

				if (!this.scheduledTickSet.contains(var6)) {
					this.scheduledTickSet.add(var6);
					this.scheduledTickTreeSet.add(var6);
				}
			}

		}
	}
	
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
			default:
				tileentity.invalidate();
				break;
		}
	}
	//Spout end

	public void func_41083_e(int var1, int var2, int var3, int var4, int var5) {
		NextTickListEntry var6 = new NextTickListEntry(var1, var2, var3, var4);
		if (var4 > 0) {
			var6.setScheduledTime((long) var5 + this.worldInfo.getWorldTime());
		}

		if (!this.scheduledTickSet.contains(var6)) {
			this.scheduledTickSet.add(var6);
			this.scheduledTickTreeSet.add(var6);
		}

	}

	public void updateEntities() {
		Profiler.startSection("entities");
		Profiler.startSection("global");

		int var1;
		Entity var2;
		for (var1 = 0; var1 < this.weatherEffects.size(); ++var1) {
			var2 = (Entity) this.weatherEffects.get(var1);
			var2.onUpdate();
			if (var2.isDead) {
				this.weatherEffects.remove(var1--);
			}
		}

		Profiler.endStartSection("remove");
		this.loadedEntityList.removeAll(this.unloadedEntityList);

		int var3;
		int var4;
		for (var1 = 0; var1 < this.unloadedEntityList.size(); ++var1) {
			var2 = (Entity) this.unloadedEntityList.get(var1);
			var3 = var2.chunkCoordX;
			var4 = var2.chunkCoordZ;
			if (var2.addedToChunk && this.chunkExists(var3, var4)) {
				this.getChunkFromChunkCoords(var3, var4).removeEntity(var2);
			}
		}

		for (var1 = 0; var1 < this.unloadedEntityList.size(); ++var1) {
			this.releaseEntitySkin((Entity) this.unloadedEntityList.get(var1));
		}

		this.unloadedEntityList.clear();
		Profiler.endStartSection("regular");

		for (var1 = 0; var1 < this.loadedEntityList.size(); ++var1) {
			var2 = (Entity) this.loadedEntityList.get(var1);
			if (var2.ridingEntity != null) {
				if (!var2.ridingEntity.isDead && var2.ridingEntity.riddenByEntity == var2) {
					continue;
				}

				var2.ridingEntity.riddenByEntity = null;
				var2.ridingEntity = null;
			}

			if (!var2.isDead) {
				this.updateEntity(var2);
			}

			Profiler.startSection("remove");
			if (var2.isDead) {
				var3 = var2.chunkCoordX;
				var4 = var2.chunkCoordZ;
				if (var2.addedToChunk && this.chunkExists(var3, var4)) {
					this.getChunkFromChunkCoords(var3, var4).removeEntity(var2);
				}

				this.loadedEntityList.remove(var1--);
				this.releaseEntitySkin(var2);
			}

			Profiler.endSection();
		}

		Profiler.endStartSection("tileEntities");
		this.scanningTileEntities = true;
		Iterator var10 = this.loadedTileEntityList.iterator();

		while (var10.hasNext()) {
			TileEntity var5 = (TileEntity) var10.next();
			if (!var5.isInvalid() && var5.worldObj != null && this.blockExists(var5.xCoord, var5.yCoord, var5.zCoord)) {
				//Spout start
				checkEntityTile(var5);
				if(!var5.isInvalid())
				{
					var5.updateEntity();
				}
				//Spout end
			}

			if (var5.isInvalid()) {
				var10.remove();
				if (this.chunkExists(var5.xCoord >> 4, var5.zCoord >> 4)) {
					Chunk var7 = this.getChunkFromChunkCoords(var5.xCoord >> 4, var5.zCoord >> 4);
					if (var7 != null) {
						var7.removeChunkBlockTileEntity(var5.xCoord & 15, var5.yCoord, var5.zCoord & 15);
					}
				}
			}
		}

		this.scanningTileEntities = false;
		if (!this.entityRemoval.isEmpty()) {
			this.loadedTileEntityList.removeAll(this.entityRemoval);
			this.entityRemoval.clear();
		}

		Profiler.endStartSection("pendingTileEntities");
		if (!this.addedTileEntityList.isEmpty()) {
			Iterator var6 = this.addedTileEntityList.iterator();

			while (var6.hasNext()) {
				TileEntity var8 = (TileEntity) var6.next();
				if (!var8.isInvalid()) {
					if (!this.loadedTileEntityList.contains(var8)) {
						this.loadedTileEntityList.add(var8);
					}

					if (this.chunkExists(var8.xCoord >> 4, var8.zCoord >> 4)) {
						Chunk var9 = this.getChunkFromChunkCoords(var8.xCoord >> 4, var8.zCoord >> 4);
						if (var9 != null) {
							var9.setChunkBlockTileEntity(var8.xCoord & 15, var8.yCoord, var8.zCoord & 15, var8);
						}
					}

					this.markBlockNeedsUpdate(var8.xCoord, var8.yCoord, var8.zCoord);
				}
			}

			this.addedTileEntityList.clear();
		}

		Profiler.endSection();
		Profiler.endSection();
	}

	public void addTileEntity(Collection var1) {
		if (this.scanningTileEntities) {
			this.addedTileEntityList.addAll(var1);
		}
		else {
			this.loadedTileEntityList.addAll(var1);
		}

	}

	public void updateEntity(Entity var1) {
		this.updateEntityWithOptionalForce(var1, true);
	}

	public void updateEntityWithOptionalForce(Entity var1, boolean var2) {
		int var3 = MathHelper.floor_double(var1.posX);
		int var4 = MathHelper.floor_double(var1.posZ);
		byte var5 = 32;
		if (!var2 || this.checkChunksExist(var3 - var5, 0, var4 - var5, var3 + var5, this.worldHeight, var4 + var5)) {
			var1.lastTickPosX = var1.posX;
			var1.lastTickPosY = var1.posY;
			var1.lastTickPosZ = var1.posZ;
			var1.prevRotationYaw = var1.rotationYaw;
			var1.prevRotationPitch = var1.rotationPitch;
			if (var2 && var1.addedToChunk) {
				if (var1.ridingEntity != null) {
					var1.updateRidden();
				}
				else {
					var1.onUpdate();
				}
			}

			Profiler.startSection("chunkCheck");
			if (Double.isNaN(var1.posX) || Double.isInfinite(var1.posX)) {
				var1.posX = var1.lastTickPosX;
			}

			if (Double.isNaN(var1.posY) || Double.isInfinite(var1.posY)) {
				var1.posY = var1.lastTickPosY;
			}

			if (Double.isNaN(var1.posZ) || Double.isInfinite(var1.posZ)) {
				var1.posZ = var1.lastTickPosZ;
			}

			if (Double.isNaN((double) var1.rotationPitch) || Double.isInfinite((double) var1.rotationPitch)) {
				var1.rotationPitch = var1.prevRotationPitch;
			}

			if (Double.isNaN((double) var1.rotationYaw) || Double.isInfinite((double) var1.rotationYaw)) {
				var1.rotationYaw = var1.prevRotationYaw;
			}

			int var6 = MathHelper.floor_double(var1.posX / 16.0D);
			int var7 = MathHelper.floor_double(var1.posY / 16.0D);
			int var8 = MathHelper.floor_double(var1.posZ / 16.0D);
			if (!var1.addedToChunk || var1.chunkCoordX != var6 || var1.chunkCoordY != var7 || var1.chunkCoordZ != var8) {
				if (var1.addedToChunk && this.chunkExists(var1.chunkCoordX, var1.chunkCoordZ)) {
					this.getChunkFromChunkCoords(var1.chunkCoordX, var1.chunkCoordZ).removeEntityAtIndex(var1, var1.chunkCoordY);
				}

				if (this.chunkExists(var6, var8)) {
					var1.addedToChunk = true;
					this.getChunkFromChunkCoords(var6, var8).addEntity(var1);
				}
				else {
					var1.addedToChunk = false;
				}
			}

			Profiler.endSection();
			if (var2 && var1.addedToChunk && var1.riddenByEntity != null) {
				if (!var1.riddenByEntity.isDead && var1.riddenByEntity.ridingEntity == var1) {
					this.updateEntity(var1.riddenByEntity);
				}
				else {
					var1.riddenByEntity.ridingEntity = null;
					var1.riddenByEntity = null;
				}
			}

		}
	}

	public boolean checkIfAABBIsClear(AxisAlignedBB var1) {
		List var2 = this.getEntitiesWithinAABBExcludingEntity((Entity) null, var1);

		for (int var3 = 0; var3 < var2.size(); ++var3) {
			Entity var4 = (Entity) var2.get(var3);
			if (!var4.isDead && var4.preventEntitySpawning) {
				return false;
			}
		}

		return true;
	}

	public boolean getIsAnyLiquid(AxisAlignedBB var1) {
		int var2 = MathHelper.floor_double(var1.minX);
		int var3 = MathHelper.floor_double(var1.maxX + 1.0D);
		int var4 = MathHelper.floor_double(var1.minY);
		int var5 = MathHelper.floor_double(var1.maxY + 1.0D);
		int var6 = MathHelper.floor_double(var1.minZ);
		int var7 = MathHelper.floor_double(var1.maxZ + 1.0D);
		if (var1.minX < 0.0D) {
			--var2;
		}

		if (var1.minY < 0.0D) {
			--var4;
		}

		if (var1.minZ < 0.0D) {
			--var6;
		}

		for (int var8 = var2; var8 < var3; ++var8) {
			for (int var9 = var4; var9 < var5; ++var9) {
				for (int var10 = var6; var10 < var7; ++var10) {
					Block var11 = Block.blocksList[this.getBlockId(var8, var9, var10)];
					if (var11 != null && var11.blockMaterial.getIsLiquid()) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public boolean isBoundingBoxBurning(AxisAlignedBB var1) {
		int var2 = MathHelper.floor_double(var1.minX);
		int var3 = MathHelper.floor_double(var1.maxX + 1.0D);
		int var4 = MathHelper.floor_double(var1.minY);
		int var5 = MathHelper.floor_double(var1.maxY + 1.0D);
		int var6 = MathHelper.floor_double(var1.minZ);
		int var7 = MathHelper.floor_double(var1.maxZ + 1.0D);
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

	public boolean handleMaterialAcceleration(AxisAlignedBB var1, Material var2, Entity var3) {
		int var4 = MathHelper.floor_double(var1.minX);
		int var5 = MathHelper.floor_double(var1.maxX + 1.0D);
		int var6 = MathHelper.floor_double(var1.minY);
		int var7 = MathHelper.floor_double(var1.maxY + 1.0D);
		int var8 = MathHelper.floor_double(var1.minZ);
		int var9 = MathHelper.floor_double(var1.maxZ + 1.0D);
		if (!this.checkChunksExist(var4, var6, var8, var5, var7, var9)) {
			return false;
		}
		else {
			boolean var10 = false;
			Vec3D var11 = Vec3D.createVector(0.0D, 0.0D, 0.0D);

			for (int var12 = var4; var12 < var5; ++var12) {
				for (int var13 = var6; var13 < var7; ++var13) {
					for (int var14 = var8; var14 < var9; ++var14) {
						Block var15 = Block.blocksList[this.getBlockId(var12, var13, var14)];
						if (var15 != null && var15.blockMaterial == var2) {
							double var16 = (double) ((float) (var13 + 1) - BlockFluid.getFluidHeightPercent(this.getBlockMetadata(var12, var13, var14)));
							if ((double) var7 >= var16) {
								var10 = true;
								var15.velocityToAddToEntity(this, var12, var13, var14, var3, var11);
							}
						}
					}
				}
			}

			if (var11.lengthVector() > 0.0D) {
				var11 = var11.normalize();
				double var18 = 0.014D;
				var3.motionX += var11.xCoord * var18;
				var3.motionY += var11.yCoord * var18;
				var3.motionZ += var11.zCoord * var18;
			}

			return var10;
		}
	}

	public boolean isMaterialInBB(AxisAlignedBB var1, Material var2) {
		int var3 = MathHelper.floor_double(var1.minX);
		int var4 = MathHelper.floor_double(var1.maxX + 1.0D);
		int var5 = MathHelper.floor_double(var1.minY);
		int var6 = MathHelper.floor_double(var1.maxY + 1.0D);
		int var7 = MathHelper.floor_double(var1.minZ);
		int var8 = MathHelper.floor_double(var1.maxZ + 1.0D);

		for (int var9 = var3; var9 < var4; ++var9) {
			for (int var10 = var5; var10 < var6; ++var10) {
				for (int var11 = var7; var11 < var8; ++var11) {
					Block var12 = Block.blocksList[this.getBlockId(var9, var10, var11)];
					if (var12 != null && var12.blockMaterial == var2) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public boolean isAABBInMaterial(AxisAlignedBB var1, Material var2) {
		int var3 = MathHelper.floor_double(var1.minX);
		int var4 = MathHelper.floor_double(var1.maxX + 1.0D);
		int var5 = MathHelper.floor_double(var1.minY);
		int var6 = MathHelper.floor_double(var1.maxY + 1.0D);
		int var7 = MathHelper.floor_double(var1.minZ);
		int var8 = MathHelper.floor_double(var1.maxZ + 1.0D);

		for (int var9 = var3; var9 < var4; ++var9) {
			for (int var10 = var5; var10 < var6; ++var10) {
				for (int var11 = var7; var11 < var8; ++var11) {
					Block var12 = Block.blocksList[this.getBlockId(var9, var10, var11)];
					if (var12 != null && var12.blockMaterial == var2) {
						int var13 = this.getBlockMetadata(var9, var10, var11);
						double var14 = (double) (var10 + 1);
						if (var13 < 8) {
							var14 = (double) (var10 + 1) - (double) var13 / 8.0D;
						}

						if (var14 >= var1.minY) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	public Explosion createExplosion(Entity var1, double var2, double var4, double var6, float var8) {
		return this.newExplosion(var1, var2, var4, var6, var8, false);
	}

	public Explosion newExplosion(Entity var1, double var2, double var4, double var6, float var8, boolean var9) {
		Explosion var10 = new Explosion(this, var1, var2, var4, var6, var8);
		var10.isFlaming = var9;
		var10.doExplosionA();
		var10.doExplosionB(true);
		return var10;
	}

	public float func_675_a(Vec3D var1, AxisAlignedBB var2) {
		double var3 = 1.0D / ((var2.maxX - var2.minX) * 2.0D + 1.0D);
		double var5 = 1.0D / ((var2.maxY - var2.minY) * 2.0D + 1.0D);
		double var7 = 1.0D / ((var2.maxZ - var2.minZ) * 2.0D + 1.0D);
		int var9 = 0;
		int var10 = 0;

		for (float var11 = 0.0F; var11 <= 1.0F; var11 = (float) ((double) var11 + var3)) {
			for (float var12 = 0.0F; var12 <= 1.0F; var12 = (float) ((double) var12 + var5)) {
				for (float var13 = 0.0F; var13 <= 1.0F; var13 = (float) ((double) var13 + var7)) {
					double var14 = var2.minX + (var2.maxX - var2.minX) * (double) var11;
					double var16 = var2.minY + (var2.maxY - var2.minY) * (double) var12;
					double var18 = var2.minZ + (var2.maxZ - var2.minZ) * (double) var13;
					if (this.rayTraceBlocks(Vec3D.createVector(var14, var16, var18), var1) == null) {
						++var9;
					}

					++var10;
				}
			}
		}

		return (float) var9 / (float) var10;
	}

	public void onBlockHit(EntityPlayer var1, int var2, int var3, int var4, int var5) {
		if (var5 == 0) {
			--var3;
		}

		if (var5 == 1) {
			++var3;
		}

		if (var5 == 2) {
			--var4;
		}

		if (var5 == 3) {
			++var4;
		}

		if (var5 == 4) {
			--var2;
		}

		if (var5 == 5) {
			++var2;
		}

		if (this.getBlockId(var2, var3, var4) == Block.fire.blockID) {
			this.playAuxSFXAtEntity(var1, 1004, var2, var3, var4, 0);
			this.setBlockWithNotify(var2, var3, var4, 0);
		}

	}

	public Entity func_4085_a(Class var1) {
		return null;
	}

	public String getDebugLoadedEntities() {
		return "All: " + this.loadedEntityList.size();
	}

	public String func_21119_g() {
		return this.chunkProvider.makeString();
	}

	public TileEntity getBlockTileEntity(int var1, int var2, int var3) {
		Chunk var4 = this.getChunkFromChunkCoords(var1 >> 4, var3 >> 4);
		if (var4 == null) {
			return null;
		}
		else {
			TileEntity var5 = var4.getChunkBlockTileEntity(var1 & 15, var2, var3 & 15);
			if (var5 == null) {
				Iterator var6 = this.addedTileEntityList.iterator();

				while (var6.hasNext()) {
					TileEntity var7 = (TileEntity) var6.next();
					if (!var7.isInvalid() && var7.xCoord == var1 && var7.yCoord == var2 && var7.zCoord == var3) {
						var5 = var7;
						break;
					}
				}
			}

			return var5;
		}
	}

	public void setBlockTileEntity(int var1, int var2, int var3, TileEntity var4) {
		if (var4 != null && !var4.isInvalid()) {
			if (this.scanningTileEntities) {
				var4.xCoord = var1;
				var4.yCoord = var2;
				var4.zCoord = var3;
				this.addedTileEntityList.add(var4);
			}
			else {
				this.loadedTileEntityList.add(var4);
				Chunk var5 = this.getChunkFromChunkCoords(var1 >> 4, var3 >> 4);
				if (var5 != null) {
					var5.setChunkBlockTileEntity(var1 & 15, var2, var3 & 15, var4);
				}
			}
		}

	}

	public void removeBlockTileEntity(int var1, int var2, int var3) {
		TileEntity var4 = this.getBlockTileEntity(var1, var2, var3);
		if (var4 != null && this.scanningTileEntities) {
			var4.invalidate();
			this.addedTileEntityList.remove(var4);
		}
		else {
			if (var4 != null) {
				this.addedTileEntityList.remove(var4);
				this.loadedTileEntityList.remove(var4);
			}

			Chunk var5 = this.getChunkFromChunkCoords(var1 >> 4, var3 >> 4);
			if (var5 != null) {
				var5.removeChunkBlockTileEntity(var1 & 15, var2, var3 & 15);
			}
		}

	}

	public void markEntityForDespawn(TileEntity var1) {
		this.entityRemoval.add(var1);
	}

	public boolean isBlockOpaqueCube(int var1, int var2, int var3) {
		Block var4 = Block.blocksList[this.getBlockId(var1, var2, var3)];
		return var4 == null ? false : var4.isOpaqueCube();
	}

	public boolean isBlockNormalCube(int var1, int var2, int var3) {
		Block var4 = Block.blocksList[this.getBlockId(var1, var2, var3)];
		return var4 == null ? false : var4.blockMaterial.getIsOpaque() && var4.renderAsNormalBlock();
	}

	public boolean func_41082_b(int var1, int var2, int var3, boolean var4) {
		if (var1 >= -30000000 && var3 >= -30000000 && var1 < 30000000 && var3 < 30000000) {
			Chunk var5 = this.chunkProvider.provideChunk(var1 >> 4, var3 >> 4);
			if (var5 != null && !var5.isEmpty()) {
				Block var6 = Block.blocksList[this.getBlockId(var1, var2, var3)];
				return var6 == null ? false : var6.blockMaterial.getIsOpaque() && var6.renderAsNormalBlock();
			}
			else {
				return var4;
			}
		}
		else {
			return var4;
		}
	}

	public void saveWorldIndirectly(IProgressUpdate var1) {
		this.saveWorld(true, var1);

		try {
			ThreadedFileIOBase.threadedIOInstance.waitForFinish();
		}
		catch (InterruptedException var3) {
			var3.printStackTrace();
		}

	}

	public void calculateInitialSkylight() {
		int var1 = this.calculateSkylightSubtracted(1.0F);
		if (var1 != this.skylightSubtracted) {
			this.skylightSubtracted = var1;
		}

	}

	public void setAllowedMobSpawns(boolean var1, boolean var2) {
		this.spawnHostileMobs = var1;
		this.spawnPeacefulMobs = var2;
	}

	public void tick() {
		if (this.getWorldInfo().isHardcoreModeEnabled() && this.difficultySetting < 3) {
			this.difficultySetting = 3;
		}

		this.getWorldChunkManager().cleanupCache();
		this.updateWeather();
		long var2;
		if (this.isAllPlayersFullyAsleep()) {
			boolean var1 = false;
			if (this.spawnHostileMobs && this.difficultySetting >= 1) {
				;
			}

			if (!var1) {
				var2 = this.worldInfo.getWorldTime() + 24000L;
				this.worldInfo.setWorldTime(var2 - var2 % 24000L);
				this.wakeUpAllPlayers();
			}
		}

		Profiler.startSection("mobSpawner");
		SpawnerAnimals.performSpawning(this, this.spawnHostileMobs, this.spawnPeacefulMobs && this.worldInfo.getWorldTime() % 400L == 0L);
		Profiler.endStartSection("chunkSource");
		this.chunkProvider.unload100OldestChunks();
		int var4 = this.calculateSkylightSubtracted(1.0F);
		if (var4 != this.skylightSubtracted) {
			this.skylightSubtracted = var4;
		}

		var2 = this.worldInfo.getWorldTime() + 1L;
		//Spout start
		if(Spoutcraft.getClient().getTick() % autosavePeriod == 0) {
			this.saveWorld(false, (IProgressUpdate)null);
		}
		//Spout end

		this.worldInfo.setWorldTime(var2);
		Profiler.endStartSection("tickPending");
		this.tickUpdates(false);
		Profiler.endStartSection("tickTiles");
		this.updateBlocksAndPlayCaveSounds();
		Profiler.endSection();
	}

	private void calculateInitialWeather() {
		if (this.worldInfo.getIsRaining()) {
			this.rainingStrength = 1.0F;
			if (this.worldInfo.getIsThundering()) {
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
				if (this.worldInfo.getIsThundering()) {
					this.worldInfo.setThunderTime(this.rand.nextInt(12000) + 3600);
				}
				else {
					this.worldInfo.setThunderTime(this.rand.nextInt(168000) + 12000);
				}
			}
			else {
				--var1;
				this.worldInfo.setThunderTime(var1);
				if (var1 <= 0) {
					this.worldInfo.setIsThundering(!this.worldInfo.getIsThundering());
				}
			}

			int var2 = this.worldInfo.getRainTime();
			if (var2 <= 0) {
				if (this.worldInfo.getIsRaining()) {
					this.worldInfo.setRainTime(this.rand.nextInt(12000) + 12000);
				}
				else {
					this.worldInfo.setRainTime(this.rand.nextInt(168000) + 12000);
				}
			}
			else {
				--var2;
				this.worldInfo.setRainTime(var2);
				if (var2 <= 0) {
					this.worldInfo.setIsRaining(!this.worldInfo.getIsRaining());
				}
			}

			this.prevRainingStrength = this.rainingStrength;
			if (this.worldInfo.getIsRaining()) {
				this.rainingStrength = (float) ((double) this.rainingStrength + 0.01D);
			}
			else {
				this.rainingStrength = (float) ((double) this.rainingStrength - 0.01D);
			}

			if (this.rainingStrength < 0.0F) {
				this.rainingStrength = 0.0F;
			}

			if (this.rainingStrength > 1.0F) {
				this.rainingStrength = 1.0F;
			}

			this.prevThunderingStrength = this.thunderingStrength;
			if (this.worldInfo.getIsThundering()) {
				this.thunderingStrength = (float) ((double) this.thunderingStrength + 0.01D);
			}
			else {
				this.thunderingStrength = (float) ((double) this.thunderingStrength - 0.01D);
			}

			if (this.thunderingStrength < 0.0F) {
				this.thunderingStrength = 0.0F;
			}

			if (this.thunderingStrength > 1.0F) {
				this.thunderingStrength = 1.0F;
			}

		}
	}

	private void stopPrecipitation() {
		this.worldInfo.setRainTime(0);
		this.worldInfo.setIsRaining(false);
		this.worldInfo.setThunderTime(0);
		this.worldInfo.setIsThundering(false);
	}
	
	protected void updateBlocksAndPlayCaveSounds() {
		this.positionsToUpdate.clear();
		Profiler.startSection("buildList");

		int var1;
		int var6;
		for (var1 = 0; var1 < this.playerEntities.size(); ++var1) {
			EntityPlayer var2 = (EntityPlayer) this.playerEntities.get(var1);
			int var3 = MathHelper.floor_double(var2.posX / 16.0D);
			int var4 = MathHelper.floor_double(var2.posZ / 16.0D);
			byte var5 = 7;

			for (var6 = -var5; var6 <= var5; ++var6) {
				for (int var7 = -var5; var7 <= var5; ++var7) {
					this.positionsToUpdate.add(new ChunkCoordIntPair(var6 + var3, var7 + var4));
				}
			}
		}

		if (this.soundCounter > 0) {
			--this.soundCounter;
		}

		var1 = 0;
		int var14 = 0;
		Profiler.endSection();
		Iterator var15 = this.positionsToUpdate.iterator();

		while (var15.hasNext()) {
			ChunkCoordIntPair var16 = (ChunkCoordIntPair) var15.next();
			int var17 = var16.chunkXPos * 16;
			var6 = var16.chunkZPos * 16;
			Profiler.startSection("getChunk");
			Chunk var18 = this.getChunkFromChunkCoords(var16.chunkXPos, var16.chunkZPos);
			Profiler.endStartSection("tickChunk");
			var18.updateSkylight();
			Profiler.endStartSection("moodSound");
			int var8;
			int var9;
			int var10;
			int var11;
			int var12;
			if (this.soundCounter == 0) {
				this.updateLCG = this.updateLCG * 3 + 1013904223;
				var8 = this.updateLCG >> 2;
				var9 = var8 & 15;
				var10 = var8 >> 8 & 15;
				var11 = var8 >> 16 & this.worldMaxY;
				var12 = var18.getBlockID(var9, var11, var10);
				var9 += var17;
				var10 += var6;
				if (var12 == 0 && this.getFullBlockLightValue(var9, var11, var10) <= this.rand.nextInt(8) && this.getSavedLightValue(EnumSkyBlock.Sky, var9, var11, var10) <= 0) {
					EntityPlayer var13 = this.getClosestPlayer((double) var9 + 0.5D, (double) var11 + 0.5D, (double) var10 + 0.5D, 8.0D);
					if (var13 != null && var13.getDistanceSq((double) var9 + 0.5D, (double) var11 + 0.5D, (double) var10 + 0.5D) > 4.0D) {
						this.playSoundEffect((double) var9 + 0.5D, (double) var11 + 0.5D, (double) var10 + 0.5D, "ambient.cave.cave", 0.7F, 0.8F + this.rand.nextFloat() * 0.2F);
						this.soundCounter = this.rand.nextInt(12000) + 6000;
					}
				}
			}

			Profiler.endStartSection("thunder");
			if (this.rand.nextInt(100000) == 0 && this.isRaining() && this.getIsThundering()) {
				this.updateLCG = this.updateLCG * 3 + 1013904223;
				var8 = this.updateLCG >> 2;
				var9 = var17 + (var8 & 15);
				var10 = var6 + (var8 >> 8 & 15);
				var11 = this.getPrecipitationHeight(var9, var10);
				if (this.canLightningStrikeAt(var9, var11, var10)) {
					this.addWeatherEffect(new EntityLightningBolt(this, (double) var9, (double) var11, (double) var10));
					this.lastLightningBolt = 2;
				}
			}

			Profiler.endStartSection("iceandsnow");
			if (this.rand.nextInt(16) == 0) {
			this.updateLCG = this.updateLCG * 3 + 1013904223;
			var8 = this.updateLCG >> 2;
			var9 = var8 & 15;
			var10 = var8 >> 8 & 15;
				var11 = this.getPrecipitationHeight(var9 + var17, var10 + var6);
			if (this.func_40481_q(var9 + var17, var11 - 1, var10 + var6)) {
				this.setBlockWithNotify(var9 + var17, var11 - 1, var10 + var6, Block.ice.blockID);
			}

				if (this.isRaining() && this.canSnowAt(var9 + var17, var11, var10 + var6)) {
				this.setBlockWithNotify(var9 + var17, var11, var10 + var6, Block.snow.blockID);
			}
			}

			Profiler.endStartSection("checkLight");
			this.updateAllLightTypes(var17 + this.rand.nextInt(16), this.rand.nextInt(this.worldHeight), var6 + this.rand.nextInt(16));
			Profiler.endStartSection("tickTiles");

			for (var8 = 0; var8 < 20; ++var8) {
				this.updateLCG = this.updateLCG * 3 + 1013904223;
				var9 = this.updateLCG >> 2;
				var10 = var9 & 15;
				var11 = var9 >> 8 & 15;
				var12 = var9 >> 16 & this.worldMaxY;
				int var19 = var18.blocks[var10 << this.xShift | var11 << this.heightShift | var12] & 255;
				++var14;
				if (Block.tickOnLoad[var19]) {
					++var1;
					Block.blocksList[var19].updateTick(this, var10 + var17, var12, var11 + var6, this.rand);
				}
			}

			Profiler.endSection();
		}

	}

	public boolean func_40471_p(int var1, int var2, int var3) {
		return this.isBlockHydrated(var1, var2, var3, false);
	}

	public boolean func_40481_q(int var1, int var2, int var3) {
		return this.isBlockHydrated(var1, var2, var3, true);
	}

	public boolean isBlockHydrated(int var1, int var2, int var3, boolean var4) {
		float var5 = this.getWorldChunkManager().getTemperature(var1, var2, var3);
		if (var5 > 0.15F) {
			return false;
		}
		else {
			if (var2 >= 0 && var2 < this.worldHeight && this.getSavedLightValue(EnumSkyBlock.Block, var1, var2, var3) < 10) {
				int var6 = this.getBlockId(var1, var2, var3);
				if ((var6 == Block.waterStill.blockID || var6 == Block.waterMoving.blockID) && this.getBlockMetadata(var1, var2, var3) == 0) {
					if (!var4) {
						return true;
					}

					boolean var7 = true;
					if (var7 && this.getBlockMaterial(var1 - 1, var2, var3) != Material.water) {
						var7 = false;
					}

					if (var7 && this.getBlockMaterial(var1 + 1, var2, var3) != Material.water) {
						var7 = false;
					}

					if (var7 && this.getBlockMaterial(var1, var2, var3 - 1) != Material.water) {
						var7 = false;
					}

					if (var7 && this.getBlockMaterial(var1, var2, var3 + 1) != Material.water) {
						var7 = false;
					}

					if (!var7) {
						return true;
					}
				}
			}

			return false;
		}
	}

	public boolean canSnowAt(int var1, int var2, int var3) {
		float var4 = this.getWorldChunkManager().getTemperature(var1, var2, var3);
		if (var4 > 0.15F) {
			return false;
		}
		else {
			if (var2 >= 0 && var2 < this.worldHeight && this.getSavedLightValue(EnumSkyBlock.Block, var1, var2, var3) < 10) {
				int var5 = this.getBlockId(var1, var2 - 1, var3);
				int var6 = this.getBlockId(var1, var2, var3);
				if (var6 == 0 && Block.snow.canPlaceBlockAt(this, var1, var2, var3) && var5 != 0 && var5 != Block.ice.blockID && Block.blocksList[var5].blockMaterial.getIsSolid()) {
					return true;
				}
			}

			return false;
		}
	}

	public void updateAllLightTypes(int var1, int var2, int var3) {
		if (!this.worldProvider.hasNoSky) {
			this.updateLightByType(EnumSkyBlock.Sky, var1, var2, var3);
		}

		this.updateLightByType(EnumSkyBlock.Block, var1, var2, var3);
	}

	private int computeSkyLightValue(int var1, int var2, int var3, int var4, int var5, int var6) {
		int var7 = 0;
		if (this.canBlockSeeTheSky(var2, var3, var4)) {
			var7 = 15;
		}
		else {
			if (var6 == 0) {
				var6 = 1;
			}

			for (int var8 = 0; var8 < 6; ++var8) {
				int var9 = var8 % 2 * 2 - 1;
				int var10 = var2 + var8 / 2 % 3 / 2 * var9;
				int var11 = var3 + (var8 / 2 + 1) % 3 / 2 * var9;
				int var12 = var4 + (var8 / 2 + 2) % 3 / 2 * var9;
				int var13 = this.getSavedLightValue(EnumSkyBlock.Sky, var10, var11, var12) - var6;
				if (var13 > var7) {
					var7 = var13;
				}
			}
		}

		return var7;
	}

	private int computeBlockLightValue(int var1, int var2, int var3, int var4, int var5, int var6) {
		//Spout start
		int light = Block.lightValue[var5];
		short customId = Spoutcraft.getWorld().getChunkAt(var2, var3, var4).getCustomBlockId(var2, var3, var4);
		if (customId > 0) {
			CustomBlock block = MaterialData.getCustomBlock(customId);
			if (block != null) {
				light = block.getLightLevel();
			}
		}
		int var7 = light;
		//Spout end
		int var8 = this.getSavedLightValue(EnumSkyBlock.Block, var2 - 1, var3, var4) - var6;
		int var9 = this.getSavedLightValue(EnumSkyBlock.Block, var2 + 1, var3, var4) - var6;
		int var10 = this.getSavedLightValue(EnumSkyBlock.Block, var2, var3 - 1, var4) - var6;
		int var11 = this.getSavedLightValue(EnumSkyBlock.Block, var2, var3 + 1, var4) - var6;
		int var12 = this.getSavedLightValue(EnumSkyBlock.Block, var2, var3, var4 - 1) - var6;
		int var13 = this.getSavedLightValue(EnumSkyBlock.Block, var2, var3, var4 + 1) - var6;
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

	public void updateLightByType(EnumSkyBlock var1, int var2, int var3, int var4) {
		if (this.doChunksNearChunkExist(var2, var3, var4, 17)) {
			int var5 = 0;
			int var6 = 0;
			int var7 = this.getSavedLightValue(var1, var2, var3, var4);
			boolean var8 = false;
			int var10 = this.getBlockId(var2, var3, var4);
			int var11 = Block.lightOpacity[var10];
			if (var11 == 0) {
				var11 = 1;
			}

			boolean var12 = false;
			int var25;
			if (var1 == EnumSkyBlock.Sky) {
				var25 = this.computeSkyLightValue(var7, var2, var3, var4, var10, var11);
			}
			else {
				var25 = this.computeBlockLightValue(var7, var2, var3, var4, var10, var11);
			}

			int var9;
			int var13;
			int var14;
			int var15;
			int var17;
			int var16;
			if (var25 > var7) {
				this.lightUpdateBlockList[var6++] = 133152;
			}
			else if (var25 < var7) {
				if (var1 != EnumSkyBlock.Block) {
					;
				}

				this.lightUpdateBlockList[var6++] = 133152 + (var7 << 18);

				while (var5 < var6) {
					var9 = this.lightUpdateBlockList[var5++];
					var10 = (var9 & 63) - 32 + var2;
					var11 = (var9 >> 6 & 63) - 32 + var3;
					var25 = (var9 >> 12 & 63) - 32 + var4;
					var13 = var9 >> 18 & 15;
					var14 = this.getSavedLightValue(var1, var10, var11, var25);
					if (var14 == var13) {
						this.setLightValue(var1, var10, var11, var25, 0);
						if (var13 > 0) {
							var15 = var10 - var2;
							var16 = var11 - var3;
							var17 = var25 - var4;
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
								for (int var18 = 0; var18 < 6; ++var18) {
									int var19 = var18 % 2 * 2 - 1;
									int var20 = var10 + var18 / 2 % 3 / 2 * var19;
									int var21 = var11 + (var18 / 2 + 1) % 3 / 2 * var19;
									int var22 = var25 + (var18 / 2 + 2) % 3 / 2 * var19;
									var14 = this.getSavedLightValue(var1, var20, var21, var22);
									int var23 = Block.lightOpacity[this.getBlockId(var20, var21, var22)];
									if (var23 == 0) {
										var23 = 1;
									}

									if (var14 == var13 - var23) {
										this.lightUpdateBlockList[var6++] = var20 - var2 + 32 + (var21 - var3 + 32 << 6) + (var22 - var4 + 32 << 12) + (var13 - var23 << 18);
									}
								}
							}
						}
					}
				}

				var5 = 0;
			}

			while (var5 < var6) {
				var7 = this.lightUpdateBlockList[var5++];
				int var24 = (var7 & 63) - 32 + var2;
				var9 = (var7 >> 6 & 63) - 32 + var3;
				var10 = (var7 >> 12 & 63) - 32 + var4;
				var11 = this.getSavedLightValue(var1, var24, var9, var10);
				var25 = this.getBlockId(var24, var9, var10);
				var13 = Block.lightOpacity[var25];
				if (var13 == 0) {
					var13 = 1;
				}

				boolean var26 = false;
				if (var1 == EnumSkyBlock.Sky) {
					var14 = this.computeSkyLightValue(var11, var24, var9, var10, var25, var13);
				}
				else {
					var14 = this.computeBlockLightValue(var11, var24, var9, var10, var25, var13);
				}

				if (var14 != var11) {
					this.setLightValue(var1, var24, var9, var10, var14);
					if (var14 > var11) {
						var15 = var24 - var2;
						var16 = var9 - var3;
						var17 = var10 - var4;
						if (var15 < 0) {
							var15 = -var15;
						}

						if (var16 < 0) {
							var16 = -var16;
						}

						if (var17 < 0) {
							var17 = -var17;
						}

						if (var15 + var16 + var17 < 17 && var6 < this.lightUpdateBlockList.length - 6) {
							if (this.getSavedLightValue(var1, var24 - 1, var9, var10) < var14) {
								this.lightUpdateBlockList[var6++] = var24 - 1 - var2 + 32 + (var9 - var3 + 32 << 6) + (var10 - var4 + 32 << 12);
							}

							if (this.getSavedLightValue(var1, var24 + 1, var9, var10) < var14) {
								this.lightUpdateBlockList[var6++] = var24 + 1 - var2 + 32 + (var9 - var3 + 32 << 6) + (var10 - var4 + 32 << 12);
							}

							if (this.getSavedLightValue(var1, var24, var9 - 1, var10) < var14) {
								this.lightUpdateBlockList[var6++] = var24 - var2 + 32 + (var9 - 1 - var3 + 32 << 6) + (var10 - var4 + 32 << 12);
							}

							if (this.getSavedLightValue(var1, var24, var9 + 1, var10) < var14) {
								this.lightUpdateBlockList[var6++] = var24 - var2 + 32 + (var9 + 1 - var3 + 32 << 6) + (var10 - var4 + 32 << 12);
							}

							if (this.getSavedLightValue(var1, var24, var9, var10 - 1) < var14) {
								this.lightUpdateBlockList[var6++] = var24 - var2 + 32 + (var9 - var3 + 32 << 6) + (var10 - 1 - var4 + 32 << 12);
							}

							if (this.getSavedLightValue(var1, var24, var9, var10 + 1) < var14) {
								this.lightUpdateBlockList[var6++] = var24 - var2 + 32 + (var9 - var3 + 32 << 6) + (var10 + 1 - var4 + 32 << 12);
							}
						}
					}
				}
			}

		}
	}

	public boolean tickUpdates(boolean var1) {
		int var2 = this.scheduledTickTreeSet.size();
		if (var2 != this.scheduledTickSet.size()) {
			throw new IllegalStateException("TickNextTick list out of synch");
		}
		else {
			if (var2 > 1000) {
				var2 = 1000;
			}

			for (int var3 = 0; var3 < var2; ++var3) {
				NextTickListEntry var4 = (NextTickListEntry) this.scheduledTickTreeSet.first();
				if (!var1 && var4.scheduledTime > this.worldInfo.getWorldTime()) {
					break;
				}

				this.scheduledTickTreeSet.remove(var4);
				this.scheduledTickSet.remove(var4);
				byte var5 = 8;
				if (this.checkChunksExist(var4.xCoord - var5, var4.yCoord - var5, var4.zCoord - var5, var4.xCoord + var5, var4.yCoord + var5, var4.zCoord + var5)) {
					int var6 = this.getBlockId(var4.xCoord, var4.yCoord, var4.zCoord);
					if (var6 == var4.blockID && var6 > 0) {
						Block.blocksList[var6].updateTick(this, var4.xCoord, var4.yCoord, var4.zCoord, this.rand);
					}
				}
			}

			return this.scheduledTickTreeSet.size() != 0;
		}
	}

	public List func_41081_a(Chunk var1, boolean var2) {
		ArrayList var3 = null;
		ChunkCoordIntPair var4 = var1.getChunkCoordIntPair();
		int var5 = var4.chunkXPos << 4;
		int var6 = var5 + 16;
		int var7 = var4.chunkZPos << 4;
		int var8 = var7 + 16;
		Iterator var9 = this.scheduledTickSet.iterator();

		while (var9.hasNext()) {
			NextTickListEntry var10 = (NextTickListEntry) var9.next();
			if (var10.xCoord >= var5 && var10.xCoord < var6 && var10.zCoord >= var7 && var10.zCoord < var8) {
				if (var2) {
					this.scheduledTickTreeSet.remove(var10);
					var9.remove();
				}

				if (var3 == null) {
					var3 = new ArrayList();
				}

				var3.add(var10);
			}
		}

		return var3;
	}

	public void randomDisplayUpdates(int var1, int var2, int var3) {
		byte var4 = 16;
		Random var5 = new Random();

		for (int var6 = 0; var6 < 1000; ++var6) {
			int var7 = var1 + this.rand.nextInt(var4) - this.rand.nextInt(var4);
			int var8 = var2 + this.rand.nextInt(var4) - this.rand.nextInt(var4);
			int var9 = var3 + this.rand.nextInt(var4) - this.rand.nextInt(var4);
			int var10 = this.getBlockId(var7, var8, var9);
			if (var10 == 0 && this.rand.nextInt(8) > var8 && this.worldProvider.func_46064_i()) {
				this.spawnParticle("depthsuspend", (double) ((float) var7 + this.rand.nextFloat()), (double) ((float) var8 + this.rand.nextFloat()), (double) ((float) var9 + this.rand.nextFloat()), 0.0D, 0.0D, 0.0D);
			}
			else if (var10 > 0) {
				Block.blocksList[var10].randomDisplayTick(this, var7, var8, var9, var5);
			}
		}

	}

	public List getEntitiesWithinAABBExcludingEntity(Entity var1, AxisAlignedBB var2) {
		this.entitiesWithinAABBExcludingEntity.clear();
		int var3 = MathHelper.floor_double((var2.minX - 2.0D) / 16.0D);
		int var4 = MathHelper.floor_double((var2.maxX + 2.0D) / 16.0D);
		int var5 = MathHelper.floor_double((var2.minZ - 2.0D) / 16.0D);
		int var6 = MathHelper.floor_double((var2.maxZ + 2.0D) / 16.0D);

		for (int var7 = var3; var7 <= var4; ++var7) {
			for (int var8 = var5; var8 <= var6; ++var8) {
				if (this.chunkExists(var7, var8)) {
					this.getChunkFromChunkCoords(var7, var8).getEntitiesWithinAABBForEntity(var1, var2, this.entitiesWithinAABBExcludingEntity);
				}
			}
		}

		return this.entitiesWithinAABBExcludingEntity;
	}

	public List getEntitiesWithinAABB(Class var1, AxisAlignedBB var2) {
		int var3 = MathHelper.floor_double((var2.minX - 2.0D) / 16.0D);
		int var4 = MathHelper.floor_double((var2.maxX + 2.0D) / 16.0D);
		int var5 = MathHelper.floor_double((var2.minZ - 2.0D) / 16.0D);
		int var6 = MathHelper.floor_double((var2.maxZ + 2.0D) / 16.0D);
		ArrayList var7 = new ArrayList();

		for (int var8 = var3; var8 <= var4; ++var8) {
			for (int var9 = var5; var9 <= var6; ++var9) {
				if (this.chunkExists(var8, var9)) {
					this.getChunkFromChunkCoords(var8, var9).getEntitiesOfTypeWithinAAAB(var1, var2, var7);
				}
			}
		}

		return var7;
	}

	public List getLoadedEntityList() {
		return this.loadedEntityList;
	}

	public void updateTileEntityChunkAndDoNothing(int var1, int var2, int var3, TileEntity var4) {
		if (this.blockExists(var1, var2, var3)) {
			this.getChunkFromBlockCoords(var1, var3).setChunkModified();
		}

		for (int var5 = 0; var5 < this.worldAccesses.size(); ++var5) {
			((IWorldAccess) this.worldAccesses.get(var5)).doNothingWithTileEntity(var1, var2, var3, var4);
		}

	}

	public int countEntities(Class var1) {
		int var2 = 0;

		for (int var3 = 0; var3 < this.loadedEntityList.size(); ++var3) {
			Entity var4 = (Entity) this.loadedEntityList.get(var3);
			if (var1.isAssignableFrom(var4.getClass())) {
				++var2;
			}
		}

		return var2;
	}

	public void addLoadedEntities(List var1) {
		this.loadedEntityList.addAll(var1);

		for (int var2 = 0; var2 < var1.size(); ++var2) {
			this.obtainEntitySkin((Entity) var1.get(var2));
		}

	}

	public void unloadEntities(List var1) {
		this.unloadedEntityList.addAll(var1);
	}

	public void dropOldChunks() {
		while (this.chunkProvider.unload100OldestChunks()) {
			;
		}

	}

	public boolean canBlockBePlacedAt(int var1, int var2, int var3, int var4, boolean var5, int var6) {
		int var7 = this.getBlockId(var2, var3, var4);
		Block var8 = Block.blocksList[var7];
		Block var9 = Block.blocksList[var1];
		AxisAlignedBB var10 = var9.getCollisionBoundingBoxFromPool(this, var2, var3, var4);
		if (var5) {
			var10 = null;
		}

		if (var10 != null && !this.checkIfAABBIsClear(var10)) {
			return false;
		}
		else {
			if (var8 == Block.waterMoving || var8 == Block.waterStill || var8 == Block.lavaMoving || var8 == Block.lavaStill || var8 == Block.fire || var8 == Block.snow || var8 == Block.vine) {
				var8 = null;
			}

			return var1 > 0 && var8 == null && var9.canPlaceBlockOnSide(this, var2, var3, var4, var6);
		}
	}

	public PathEntity getPathToEntity(Entity var1, Entity var2, float var3) {
		Profiler.startSection("pathfind");
		int var4 = MathHelper.floor_double(var1.posX);
		int var5 = MathHelper.floor_double(var1.posY);
		int var6 = MathHelper.floor_double(var1.posZ);
		int var7 = (int) (var3 + 16.0F);
		int var8 = var4 - var7;
		int var9 = var5 - var7;
		int var10 = var6 - var7;
		int var11 = var4 + var7;
		int var12 = var5 + var7;
		int var13 = var6 + var7;
		//Spout start
		//ChunkCache var14 = new ChunkCache(this, var8, var9, var10, var11, var12, var13);
		PathEntity var15 = (new PathFinder(this)).createEntityPathTo(var1, var2, var3);
		//Spout end
		Profiler.endSection();
		return var15;
	}

	public PathEntity getEntityPathToXYZ(Entity var1, int var2, int var3, int var4, float var5) {
		Profiler.startSection("pathfind");
		int var6 = MathHelper.floor_double(var1.posX);
		int var7 = MathHelper.floor_double(var1.posY);
		int var8 = MathHelper.floor_double(var1.posZ);
		int var9 = (int) (var5 + 8.0F);
		int var10 = var6 - var9;
		int var11 = var7 - var9;
		int var12 = var8 - var9;
		int var13 = var6 + var9;
		int var14 = var7 + var9;
		int var15 = var8 + var9;
		//Spout start
		//ChunkCache var16 = new ChunkCache(this, var10, var11, var12, var13, var14, var15);
		PathEntity var17 = (new PathFinder(this)).createEntityPathTo(var1, var2, var3, var4, var5);
		//Spout end
		Profiler.endSection();
		return var17;
	}

	public boolean isBlockProvidingPowerTo(int var1, int var2, int var3, int var4) {
		int var5 = this.getBlockId(var1, var2, var3);
		return var5 == 0 ? false : Block.blocksList[var5].isIndirectlyPoweringTo(this, var1, var2, var3, var4);
	}

	public boolean isBlockGettingPowered(int var1, int var2, int var3) {
		return this.isBlockProvidingPowerTo(var1, var2 - 1, var3, 0) ? true : (this.isBlockProvidingPowerTo(var1, var2 + 1, var3, 1) ? true : (this.isBlockProvidingPowerTo(var1, var2, var3 - 1, 2) ? true : (this.isBlockProvidingPowerTo(var1, var2, var3 + 1, 3) ? true : (this.isBlockProvidingPowerTo(var1 - 1, var2, var3, 4) ? true : this.isBlockProvidingPowerTo(var1 + 1, var2, var3, 5)))));
	}

	public boolean isBlockIndirectlyProvidingPowerTo(int var1, int var2, int var3, int var4) {
		if (this.isBlockNormalCube(var1, var2, var3)) {
			return this.isBlockGettingPowered(var1, var2, var3);
		}
		else {
			int var5 = this.getBlockId(var1, var2, var3);
			return var5 == 0 ? false : Block.blocksList[var5].isPoweringTo(this, var1, var2, var3, var4);
		}
	}

	public boolean isBlockIndirectlyGettingPowered(int var1, int var2, int var3) {
		return this.isBlockIndirectlyProvidingPowerTo(var1, var2 - 1, var3, 0) ? true : (this.isBlockIndirectlyProvidingPowerTo(var1, var2 + 1, var3, 1) ? true : (this.isBlockIndirectlyProvidingPowerTo(var1, var2, var3 - 1, 2) ? true : (this.isBlockIndirectlyProvidingPowerTo(var1, var2, var3 + 1, 3) ? true : (this.isBlockIndirectlyProvidingPowerTo(var1 - 1, var2, var3, 4) ? true : this.isBlockIndirectlyProvidingPowerTo(var1 + 1, var2, var3, 5)))));
	}

	public EntityPlayer getClosestPlayerToEntity(Entity var1, double var2) {
		return this.getClosestPlayer(var1.posX, var1.posY, var1.posZ, var2);
	}

	public EntityPlayer getClosestPlayer(double var1, double var3, double var5, double var7) {
		double var9 = -1.0D;
		EntityPlayer var11 = null;

		for (int var12 = 0; var12 < this.playerEntities.size(); ++var12) {
			EntityPlayer var13 = (EntityPlayer) this.playerEntities.get(var12);
			double var14 = var13.getDistanceSq(var1, var3, var5);
			if ((var7 < 0.0D || var14 < var7 * var7) && (var9 == -1.0D || var14 < var9)) {
				var9 = var14;
				var11 = var13;
			}
		}

		return var11;
	}

	public EntityPlayer getClosestVulnerablePlayerToEntity(Entity var1, double var2) {
		return this.getClosestVulnerablePlayer(var1.posX, var1.posY, var1.posZ, var2);
	}

	public EntityPlayer getClosestVulnerablePlayer(double var1, double var3, double var5, double var7) {
		double var9 = -1.0D;
		EntityPlayer var11 = null;

		for (int var12 = 0; var12 < this.playerEntities.size(); ++var12) {
			EntityPlayer var13 = (EntityPlayer) this.playerEntities.get(var12);
			if (!var13.capabilities.disableDamage) {
				double var14 = var13.getDistanceSq(var1, var3, var5);
				if ((var7 < 0.0D || var14 < var7 * var7) && (var9 == -1.0D || var14 < var9)) {
					var9 = var14;
					var11 = var13;
				}
			}
		}

		return var11;
	}

	public EntityPlayer getPlayerEntityByName(String var1) {
		for (int var2 = 0; var2 < this.playerEntities.size(); ++var2) {
			if (var1.equals(((EntityPlayer) this.playerEntities.get(var2)).username)) {
				return (EntityPlayer) this.playerEntities.get(var2);
			}
		}

		return null;
	}

	public void setChunkData(int var1, int var2, int var3, int var4, int var5, int var6, byte[] var7) {
		int var8 = var1 >> 4;
		int var9 = var3 >> 4;
		int var10 = var1 + var4 - 1 >> 4;
		int var11 = var3 + var6 - 1 >> 4;
		int var12 = 0;
		int var13 = var2;
		int var14 = var2 + var5;
		if (var2 < 0) {
			var13 = 0;
		}

		if (var14 > this.worldHeight) {
			var14 = this.worldHeight;
		}

		for (int var15 = var8; var15 <= var10; ++var15) {
			int var16 = var1 - var15 * 16;
			int var17 = var1 + var4 - var15 * 16;
			if (var16 < 0) {
				var16 = 0;
			}

			if (var17 > 16) {
				var17 = 16;
			}

			for (int var18 = var9; var18 <= var11; ++var18) {
				int var19 = var3 - var18 * 16;
				int var20 = var3 + var6 - var18 * 16;
				if (var19 < 0) {
					var19 = 0;
				}

				if (var20 > 16) {
					var20 = 16;
				}

				var12 = this.getChunkFromChunkCoords(var15, var18).setChunkData(var7, var16, var13, var19, var17, var14, var20, var12);
				this.markBlocksDirty(var15 * 16 + var16, var13, var18 * 16 + var19, var15 * 16 + var17, var14, var18 * 16 + var20);
			}
		}

	}

	public void sendQuittingDisconnectingPacket() {}

	public void checkSessionLock() {
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

	public long getWorldSeed() {
		return this.worldInfo.getRandomSeed();
	}

	public long getWorldTime() {
		return this.worldInfo.getWorldTime();
	}

	public ChunkCoordinates getSpawnPoint() {
		return new ChunkCoordinates(this.worldInfo.getSpawnX(), this.worldInfo.getSpawnY(), this.worldInfo.getSpawnZ());
	}

	public void setSpawnPoint(ChunkCoordinates var1) {
		this.worldInfo.setSpawn(var1.posX, var1.posY, var1.posZ);
	}

	public void joinEntityInSurroundings(Entity var1) {
		int var2 = MathHelper.floor_double(var1.posX / 16.0D);
		int var3 = MathHelper.floor_double(var1.posZ / 16.0D);
		byte var4 = 2;

		for (int var5 = var2 - var4; var5 <= var2 + var4; ++var5) {
			for (int var6 = var3 - var4; var6 <= var3 + var4; ++var6) {
				this.getChunkFromChunkCoords(var5, var6);
			}
		}

		if (!this.loadedEntityList.contains(var1)) {
			this.loadedEntityList.add(var1);
		}

	}

	public boolean canMineBlock(EntityPlayer var1, int var2, int var3, int var4) {
		return true;
	}

	public void setEntityState(Entity var1, byte var2) {}

	public void updateEntityList() {
		this.loadedEntityList.removeAll(this.unloadedEntityList);

		int var1;
		Entity var2;
		int var3;
		int var4;
		for (var1 = 0; var1 < this.unloadedEntityList.size(); ++var1) {
			var2 = (Entity) this.unloadedEntityList.get(var1);
			var3 = var2.chunkCoordX;
			var4 = var2.chunkCoordZ;
			if (var2.addedToChunk && this.chunkExists(var3, var4)) {
				this.getChunkFromChunkCoords(var3, var4).removeEntity(var2);
			}
		}

		for (var1 = 0; var1 < this.unloadedEntityList.size(); ++var1) {
			this.releaseEntitySkin((Entity) this.unloadedEntityList.get(var1));
		}

		this.unloadedEntityList.clear();

		for (var1 = 0; var1 < this.loadedEntityList.size(); ++var1) {
			var2 = (Entity) this.loadedEntityList.get(var1);
			if (var2.ridingEntity != null) {
				if (!var2.ridingEntity.isDead && var2.ridingEntity.riddenByEntity == var2) {
					continue;
				}

				var2.ridingEntity.riddenByEntity = null;
				var2.ridingEntity = null;
			}

			if (var2.isDead) {
				var3 = var2.chunkCoordX;
				var4 = var2.chunkCoordZ;
				if (var2.addedToChunk && this.chunkExists(var3, var4)) {
					this.getChunkFromChunkCoords(var3, var4).removeEntity(var2);
				}

				this.loadedEntityList.remove(var1--);
				this.releaseEntitySkin(var2);
			}
		}

	}

	public IChunkProvider getIChunkProvider() {
		return this.chunkProvider;
	}

	public void playNoteAt(int var1, int var2, int var3, int var4, int var5) {
		int var6 = this.getBlockId(var1, var2, var3);
		if (var6 > 0) {
			Block.blocksList[var6].powerBlock(this, var1, var2, var3, var4, var5);
		}

	}

	public ISaveHandler getSaveHandler() {
		return this.saveHandler;
	}

	public WorldInfo getWorldInfo() {
		return this.worldInfo;
	}

	public void updateAllPlayersSleepingFlag() {
		this.allPlayersSleeping = !this.playerEntities.isEmpty();
		Iterator var1 = this.playerEntities.iterator();

		while (var1.hasNext()) {
			EntityPlayer var2 = (EntityPlayer) var1.next();
			if (!var2.isPlayerSleeping()) {
				this.allPlayersSleeping = false;
				break;
			}
		}

	}

	protected void wakeUpAllPlayers() {
		this.allPlayersSleeping = false;
		Iterator var1 = this.playerEntities.iterator();

		while (var1.hasNext()) {
			EntityPlayer var2 = (EntityPlayer) var1.next();
			if (var2.isPlayerSleeping()) {
				var2.wakeUpPlayer(false, false, true);
			}
		}

		this.stopPrecipitation();
	}

	public boolean isAllPlayersFullyAsleep() {
		if (this.allPlayersSleeping && !this.multiplayerWorld) {
			Iterator var1 = this.playerEntities.iterator();

			EntityPlayer var2;
			do {
				if (!var1.hasNext()) {
					return true;
				}

				var2 = (EntityPlayer) var1.next();
			}
			while (var2.isPlayerFullyAsleep());

			return false;
		}
		else {
			return false;
		}
	}

	public float getWeightedThunderStrength(float var1) {
		return (this.prevThunderingStrength + (this.thunderingStrength - this.prevThunderingStrength) * var1) * this.getRainStrength(var1);
	}

	public float getRainStrength(float var1) {
		return this.prevRainingStrength + (this.rainingStrength - this.prevRainingStrength) * var1;
	}

	public void setRainStrength(float var1) {
		this.prevRainingStrength = var1;
		this.rainingStrength = var1;
	}

	public boolean getIsThundering() {
		return (double) this.getWeightedThunderStrength(1.0F) > 0.9D;
	}

	public boolean isRaining() {
		return (double) this.getRainStrength(1.0F) > 0.2D;
	}

	public boolean canLightningStrikeAt(int var1, int var2, int var3) {
		if (!this.isRaining()) {
			return false;
		}
		else if (!this.canBlockSeeTheSky(var1, var2, var3)) {
			return false;
		}
		else if (this.getPrecipitationHeight(var1, var3) > var2) {
			return false;
		}
		else {
			BiomeGenBase var4 = this.getWorldChunkManager().getBiomeGenAt(var1, var3);
			return var4.getEnableSnow() ? false : var4.canSpawnLightningBolt();
		}
	}

	public void setItemData(String var1, WorldSavedData var2) {
		this.mapStorage.setData(var1, var2);
	}

	public WorldSavedData loadItemData(Class var1, String var2) {
		return this.mapStorage.loadData(var1, var2);
	}

	public int getUniqueDataId(String var1) {
		return this.mapStorage.getUniqueDataId(var1);
	}

	public void playAuxSFX(int var1, int var2, int var3, int var4, int var5) {
		this.playAuxSFXAtEntity((EntityPlayer) null, var1, var2, var3, var4, var5);
	}

	public void playAuxSFXAtEntity(EntityPlayer var1, int var2, int var3, int var4, int var5, int var6) {
		for (int var7 = 0; var7 < this.worldAccesses.size(); ++var7) {
			((IWorldAccess) this.worldAccesses.get(var7)).playAuxSFX(var1, var2, var3, var4, var5, var6);
		}

	}

	public int getWorldHeight() {
		return this.worldHeight;
	}

	public Random setRandomSeed(int var1, int var2, int var3) {
		long var4 = (long) var1 * 341873128712L + (long) var2 * 132897987541L + this.getWorldInfo().getRandomSeed() + (long) var3;
		this.rand.setSeed(var4);
		return this.rand;
	}

	public boolean updatingLighting() {
		return false;
	}

	public void scheduleLightingUpdate(EnumSkyBlock var1, int var2, int var3, int var4, int var5, int var6, int var7) {}

	public SpawnListEntry func_40474_a(EnumCreatureType var1, int var2, int var3, int var4) {
		List var5 = this.getIChunkProvider().func_40377_a(var1, var2, var3, var4);
		return var5 != null && !var5.isEmpty() ? (SpawnListEntry) WeightedRandom.func_35733_a(this.rand, var5) : null;
	}

	public ChunkPosition func_40477_b(String var1, int var2, int var3, int var4) {
		return this.getIChunkProvider().func_40376_a(this, var1, var2, var3, var4);
	}

	public double func_46068_G() {
		return this.worldInfo.func_46133_t() == EnumWorldType.FLAT ? 0.0D : 63.0D;
	}	
	//Spout Start
	public void doColorfulStuff() {
		for(int i = 0; i < this.playerEntities.size(); ++i) {
			EntityPlayer ep = (EntityPlayer)this.playerEntities.get(i);
			if (ep != Minecraft.theMinecraft.thePlayer) { 
				ep.doFancyStuff();
			}
		}
	}
	
	//Heights must be a power of 2!
	public void setMapHeight(int height) {
		int shifts = 0;
		int value = height;
		while (value != 1) {
			value = value >> 1;
			shifts++;
		}
		heightShift = shifts;
		this.xShift = this.heightShift + 4;
		this.worldHeight = 1 << this.heightShift;
		this.worldMaxY = this.worldHeight - 1;
		this.seaLevel = this.worldHeight / 2 - 1;
		this.worldInfo.setMapHeight(height);
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
}
