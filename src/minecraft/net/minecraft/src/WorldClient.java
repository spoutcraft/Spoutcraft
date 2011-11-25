package net.minecraft.src;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.ChunkProviderClient;
import net.minecraft.src.Entity;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.IntHashMap;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.Packet255KickDisconnect;
import net.minecraft.src.SaveHandlerMP;
import net.minecraft.src.World;
import net.minecraft.src.WorldBlockPositionType;
import net.minecraft.src.WorldProvider;
import net.minecraft.src.WorldSettings;

public class WorldClient extends World {

	private LinkedList blocksToReceive = new LinkedList();
	private NetClientHandler sendQueue;
	private ChunkProviderClient field_20915_C;
	private IntHashMap entityHashSet = new IntHashMap();
	private Set entityList = new HashSet();
	private Set entitySpawnQueue = new HashSet();

	public WorldClient(NetClientHandler var1, WorldSettings var2, int var3, int var4) {
		super(new SaveHandlerMP(), "MpServer", WorldProvider.getProviderForDimension(var3), var2);
		this.sendQueue = var1;
		this.difficultySetting = var4;
		this.setSpawnPoint(new ChunkCoordinates(8, 64, 8));
		this.mapStorage = var1.mapStorage;
	}

	public void tick() {
		this.setWorldTime(this.getWorldTime() + 1L);

		int var1;
		for (var1 = 0; var1 < 10 && !this.entitySpawnQueue.isEmpty(); ++var1) {
			Entity var2 = (Entity) this.entitySpawnQueue.iterator().next();
			if (!this.loadedEntityList.contains(var2)) {
				this.entityJoinedWorld(var2);
			}
		}

		this.sendQueue.processReadPackets();

		for (var1 = 0; var1 < this.blocksToReceive.size(); ++var1) {
			WorldBlockPositionType var3 = (WorldBlockPositionType) this.blocksToReceive.get(var1);
			if (--var3.acceptCountdown == 0) {
				super.setBlockAndMetadata(var3.posX, var3.posY, var3.posZ, var3.blockID, var3.metadata);
				super.markBlockNeedsUpdate(var3.posX, var3.posY, var3.posZ);
				this.blocksToReceive.remove(var1--);
			}
		}

	}

	public void invalidateBlockReceiveRegion(int var1, int var2, int var3, int var4, int var5, int var6) {
		for (int var7 = 0; var7 < this.blocksToReceive.size(); ++var7) {
			WorldBlockPositionType var8 = (WorldBlockPositionType) this.blocksToReceive.get(var7);
			if (var8.posX >= var1 && var8.posY >= var2 && var8.posZ >= var3 && var8.posX <= var4 && var8.posY <= var5 && var8.posZ <= var6) {
				this.blocksToReceive.remove(var7--);
			}
		}

	}

	protected IChunkProvider getChunkProvider() {
		this.field_20915_C = new ChunkProviderClient(this);
		return this.field_20915_C;
	}

	public void setSpawnLocation() {
		this.setSpawnPoint(new ChunkCoordinates(8, 64, 8));
	}

	protected void updateBlocksAndPlayCaveSounds() {
	}

	public void scheduleBlockUpdate(int var1, int var2, int var3, int var4, int var5) {
	}

	public boolean tickUpdates(boolean var1) {
		return false;
	}

	public void doPreChunk(int var1, int var2, boolean var3) {
		if (var3) {
			this.field_20915_C.loadChunk(var1, var2);
		} else {
			this.field_20915_C.func_539_c(var1, var2);
		}

		if (!var3) {
			this.markBlocksDirty(var1 * 16, 0, var2 * 16, var1 * 16 + 15, this.field_35472_c, var2 * 16 + 15);
		}

	}

	public boolean entityJoinedWorld(Entity var1) {
		boolean var2 = super.entityJoinedWorld(var1);
		this.entityList.add(var1);
		if (!var2) {
			this.entitySpawnQueue.add(var1);
		}

		return var2;
	}

	public void setEntityDead(Entity var1) {
		super.setEntityDead(var1);
		this.entityList.remove(var1);
	}

	public void obtainEntitySkin(Entity var1) { // Spout protected -> public
		super.obtainEntitySkin(var1);
		if (this.entitySpawnQueue.contains(var1)) {
			this.entitySpawnQueue.remove(var1);
		}

	}

	public void releaseEntitySkin(Entity var1) { // Spout protected -> public
		super.releaseEntitySkin(var1);
		if (this.entityList.contains(var1)) {
			this.entitySpawnQueue.add(var1);
		}

	}

	public void func_712_a(int var1, Entity var2) {
		Entity var3 = this.func_709_b(var1);
		if (var3 != null) {
			this.setEntityDead(var3);
		}

		this.entityList.add(var2);
		var2.entityId = var1;
		if (!this.entityJoinedWorld(var2)) {
			this.entitySpawnQueue.add(var2);
		}

		this.entityHashSet.addKey(var1, var2);
	}

	public Entity func_709_b(int var1) {
		return (Entity) this.entityHashSet.lookup(var1);
	}

	public Entity removeEntityFromWorld(int var1) {
		Entity var2 = (Entity) this.entityHashSet.removeObject(var1);
		if (var2 != null) {
			this.entityList.remove(var2);
			this.setEntityDead(var2);
		}

		return var2;
	}

	public boolean setBlockMetadata(int var1, int var2, int var3, int var4) {
		int var5 = this.getBlockId(var1, var2, var3);
		int var6 = this.getBlockMetadata(var1, var2, var3);
		if (super.setBlockMetadata(var1, var2, var3, var4)) {
			this.blocksToReceive.add(new WorldBlockPositionType(this, var1, var2, var3, var5, var6));
			return true;
		} else {
			return false;
		}
	}

	public boolean setBlockAndMetadata(int var1, int var2, int var3, int var4, int var5) {
		int var6 = this.getBlockId(var1, var2, var3);
		int var7 = this.getBlockMetadata(var1, var2, var3);
		if (super.setBlockAndMetadata(var1, var2, var3, var4, var5)) {
			this.blocksToReceive.add(new WorldBlockPositionType(this, var1, var2, var3, var6, var7));
			return true;
		} else {
			return false;
		}
	}

	public boolean setBlock(int var1, int var2, int var3, int var4) {
		int var5 = this.getBlockId(var1, var2, var3);
		int var6 = this.getBlockMetadata(var1, var2, var3);
		if (super.setBlock(var1, var2, var3, var4)) {
			this.blocksToReceive.add(new WorldBlockPositionType(this, var1, var2, var3, var5, var6));
			return true;
		} else {
			return false;
		}
	}

	public boolean setBlockAndMetadataAndInvalidate(int var1, int var2, int var3, int var4, int var5) {
		this.invalidateBlockReceiveRegion(var1, var2, var3, var1, var2, var3);
		if (super.setBlockAndMetadata(var1, var2, var3, var4, var5)) {
			this.notifyBlockChange(var1, var2, var3, var4);
			return true;
		} else {
			return false;
		}
	}

	public void sendQuittingDisconnectingPacket() {
		this.sendQueue.func_28117_a(new Packet255KickDisconnect("Quitting"));
	}

	protected void updateWeather() {
		if (!this.worldProvider.hasNoSky) {
			if (this.lastLightningBolt > 0) {
				--this.lastLightningBolt;
			}

			this.prevRainingStrength = this.rainingStrength;
			if (this.worldInfo.getIsRaining()) {
				this.rainingStrength = (float) ((double) this.rainingStrength + 0.01D);
			} else {
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
			} else {
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
}
