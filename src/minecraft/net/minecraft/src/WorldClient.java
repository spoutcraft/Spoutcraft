package net.minecraft.src;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import net.minecraft.client.Minecraft;

public class WorldClient extends World {

	/** The packets that need to be sent to the server. */
	private NetClientHandler sendQueue;

	/** The ChunkProviderClient instance */
	private ChunkProviderClient clientChunkProvider;

	/**
	 * The hash set of entities handled by this client. Uses the entity's ID as the hash set's key.
	 */
	private IntHashMap entityHashSet = new IntHashMap();

	/** Contains all entities for this client, both spawned and non-spawned. */
	private Set entityList = new HashSet();

	/**
	 * Contains all entities for this client that were not spawned due to a non-present chunk. The game will attempt to
	 * spawn up to 10 pending entities with each subsequent tick until the spawn queue is empty.
	 */
	private Set entitySpawnQueue = new HashSet();
	private final Minecraft field_73037_M = Minecraft.func_71410_x();
	private final Set field_73038_N = new HashSet();

	public WorldClient(NetClientHandler par1NetClientHandler, WorldSettings par2WorldSettings, int par3, int par4, Profiler par5Profiler) {
		super(new SaveHandlerMP(), "MpServer", WorldProvider.getProviderForDimension(par3), par2WorldSettings, par5Profiler);
		this.sendQueue = par1NetClientHandler;
		this.difficultySetting = par4;
		this.func_72950_A(8, 64, 8);
		this.mapStorage = par1NetClientHandler.mapStorage;
	}

	/**
	 * Runs a single tick for the world
	 */
	public void tick() {
		super.tick();
		this.setWorldTime(this.getWorldTime() + 1L);
		this.field_72984_F.startSection("reEntryProcessing");

		for (int var1 = 0; var1 < 10 && !this.entitySpawnQueue.isEmpty(); ++var1) {
			Entity var2 = (Entity)this.entitySpawnQueue.iterator().next();
			this.entitySpawnQueue.remove(var2);

			if (!this.loadedEntityList.contains(var2)) {
				this.spawnEntityInWorld(var2);
			}
		}

		this.field_72984_F.endStartSection("connection");
		this.sendQueue.processReadPackets();
		this.field_72984_F.endStartSection("chunkCache");
		this.clientChunkProvider.unload100OldestChunks();
		this.field_72984_F.endStartSection("tiles");
		this.tickBlocksAndAmbiance();
		this.field_72984_F.endSection();
	}

	/**
	 * Invalidates an AABB region of blocks from the receive queue, in the event that the block has been modified client-
	 * side in the intervening 80 receive ticks.
	 */
	public void invalidateBlockReceiveRegion(int par1, int par2, int par3, int par4, int par5, int par6) {}

	/**
	 * Creates the chunk provider for this world. Called in the constructor. Retrieves provider from worldProvider?
	 */
	protected IChunkProvider createChunkProvider() {
		this.clientChunkProvider = new ChunkProviderClient(this);
		return this.clientChunkProvider;
	}

	/**
	 * plays random cave ambient sounds and runs updateTick on random blocks within each chunk in the vacinity of a player
	 */
	protected void tickBlocksAndAmbiance() {
		super.tickBlocksAndAmbiance();
		this.field_73038_N.retainAll(this.activeChunkSet);

		if (this.field_73038_N.size() == this.activeChunkSet.size()) {
			this.field_73038_N.clear();
		}

		int var1 = 0;
		Iterator var2 = this.activeChunkSet.iterator();

		while (var2.hasNext()) {
			ChunkCoordIntPair var3 = (ChunkCoordIntPair)var2.next();

			if (!this.field_73038_N.contains(var3)) {
				int var4 = var3.chunkXPos * 16;
				int var5 = var3.chunkZPos * 16;
				this.field_72984_F.startSection("getChunk");
				Chunk var6 = this.getChunkFromChunkCoords(var3.chunkXPos, var3.chunkZPos);
				this.func_72941_a(var4, var5, var6);
				this.field_72984_F.endSection();
				this.field_73038_N.add(var3);
				++var1;

				if (var1 >= 10) {
					return;
				}
			}
		}
	}

	public void doPreChunk(int par1, int par2, boolean par3) {
		if (par3) {
			this.clientChunkProvider.loadChunk(par1, par2);
		} else {
			this.clientChunkProvider.unloadChunk(par1, par2);
		}

		if (!par3) {
			this.markBlocksDirty(par1 * 16, 0, par2 * 16, par1 * 16 + 15, 256, par2 * 16 + 15);
		}
	}

	/**
	 * Called to place all entities as part of a world
	 */
	public boolean spawnEntityInWorld(Entity par1Entity) {
		boolean var2 = super.spawnEntityInWorld(par1Entity);
		this.entityList.add(par1Entity);

		if (!var2) {
			this.entitySpawnQueue.add(par1Entity);
		}

		return var2;
	}

	/**
	 * Dismounts the entity (and anything riding the entity), sets the dead flag, and removes the player entity from the
	 * player entity list. Called by the playerLoggedOut function.
	 */
	public void setEntityDead(Entity par1Entity) {
		super.setEntityDead(par1Entity);
		this.entityList.remove(par1Entity);
	}

	/**
	 * Start the skin for this entity downloading, if necessary, and increment its reference counter
	 */
	public void obtainEntitySkin(Entity par1Entity) { // Spout protected -> public
		super.obtainEntitySkin(par1Entity);
		if (this.entitySpawnQueue.contains(par1Entity)) {
			this.entitySpawnQueue.remove(par1Entity);
		}
	}

	/**
	 * Decrement the reference counter for this entity's skin image data
	 */
	public void releaseEntitySkin(Entity par1Entity) { // Spout protected -> public
		super.releaseEntitySkin(par1Entity);
		if (this.entityList.contains(par1Entity)) {
			if (par1Entity.isEntityAlive()) {
				this.entitySpawnQueue.add(par1Entity);
			} else {
				this.entityList.remove(par1Entity);
			}
		}
	}

	/**
	 * Add an ID to Entity mapping to entityHashSet
	 */
	public void addEntityToWorld(int par1, Entity par2Entity) {
		Entity var3 = this.getEntityByID(par1);

		if (var3 != null) {
			this.setEntityDead(var3);
		}

		this.entityList.add(par2Entity);
		par2Entity.entityId = par1;

		if (!this.spawnEntityInWorld(par2Entity)) {
			this.entitySpawnQueue.add(par2Entity);
		}

		this.entityHashSet.addKey(par1, par2Entity);
	}

	/**
	 * Lookup and return an Entity based on its ID
	 */
	public Entity getEntityByID(int par1) {
		return (Entity)this.entityHashSet.lookup(par1);
	}

	public Entity removeEntityFromWorld(int par1) {
		Entity var2 = (Entity)this.entityHashSet.removeObject(par1);

		if (var2 != null) {
			this.entityList.remove(var2);
			this.setEntityDead(var2);
		}

		return var2;
	}

	public boolean setBlockAndMetadataAndInvalidate(int par1, int par2, int par3, int par4, int par5) {
		this.invalidateBlockReceiveRegion(par1, par2, par3, par1, par2, par3);
		return super.setBlockAndMetadataWithNotify(par1, par2, par3, par4, par5);
	}

	/**
	 * If on MP, sends a quitting packet.
	 */
	public void sendQuittingDisconnectingPacket() {
		this.sendQueue.quitWithPacket(new Packet255KickDisconnect("Quitting"));
	}

	/**
	 * Updates all weather states.
	 */
	protected void updateWeather() {
		if (!this.worldProvider.hasNoSky) {
			if (this.lastLightningBolt > 0) {
				--this.lastLightningBolt;
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

	public void func_73029_E(int par1, int par2, int par3) {
		byte var4 = 16;
		Random var5 = new Random();

		for (int var6 = 0; var6 < 1000; ++var6) {
			int var7 = par1 + this.rand.nextInt(var4) - this.rand.nextInt(var4);
			int var8 = par2 + this.rand.nextInt(var4) - this.rand.nextInt(var4);
			int var9 = par3 + this.rand.nextInt(var4) - this.rand.nextInt(var4);
			int var10 = this.getBlockId(var7, var8, var9);

			if (var10 == 0 && this.rand.nextInt(8) > var8 && this.worldProvider.getWorldHasVoidParticles()) {
				this.spawnParticle("depthsuspend", (double)((float)var7 + this.rand.nextFloat()), (double)((float)var8 + this.rand.nextFloat()), (double)((float)var9 + this.rand.nextFloat()), 0.0D, 0.0D, 0.0D);
			} else if (var10 > 0) {
				Block.blocksList[var10].randomDisplayTick(this, var7, var8, var9, var5);
			}
		}
	}

	public void func_73022_a() {
		this.loadedEntityList.removeAll(this.unloadedEntityList);
		int var1;
		Entity var2;
		int var3;
		int var4;

		for (var1 = 0; var1 < this.unloadedEntityList.size(); ++var1) {
			var2 = (Entity)this.unloadedEntityList.get(var1);
			var3 = var2.chunkCoordX;
			var4 = var2.chunkCoordZ;

			if (var2.addedToChunk && this.chunkExists(var3, var4)) {
				this.getChunkFromChunkCoords(var3, var4).removeEntity(var2);
			}
		}

		for (var1 = 0; var1 < this.unloadedEntityList.size(); ++var1) {
			this.releaseEntitySkin((Entity)this.unloadedEntityList.get(var1));
		}

		this.unloadedEntityList.clear();

		for (var1 = 0; var1 < this.loadedEntityList.size(); ++var1) {
			var2 = (Entity)this.loadedEntityList.get(var1);

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

	public CrashReport func_72914_a(CrashReport par1CrashReport) {
		par1CrashReport = super.func_72914_a(par1CrashReport);
		par1CrashReport.func_71500_a("Forced Entities", new CallableMPL1(this));
		par1CrashReport.func_71500_a("Retry Entities", new CallableMPL2(this));
		return par1CrashReport;
	}

	public void func_72980_b(double par1, double par3, double par5, String par7Str, float par8, float par9) {
		float var10 = 16.0F;

		if (par8 > 1.0F) {
			var10 *= par8;
		}

		if (this.field_73037_M.renderViewEntity.getDistanceSq(par1, par3, par5) < (double)(var10 * var10)) {
			this.field_73037_M.sndManager.playSound(par7Str, (float)par1, (float)par3, (float)par5, par8, par9);
		}
	}

	static Set func_73026_a(WorldClient par0WorldClient) {
		return par0WorldClient.entityList;
	}

	static Set func_73030_b(WorldClient par0WorldClient) {
		return par0WorldClient.entitySpawnQueue;
	}
}
