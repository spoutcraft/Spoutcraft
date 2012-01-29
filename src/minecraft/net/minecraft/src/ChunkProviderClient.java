package net.minecraft.src;

import java.util.*;

public class ChunkProviderClient
	implements IChunkProvider {
	private Chunk blankChunk;
	private LongHashMap chunkMapping;
	private List field_889_c;
	private World worldObj;

	public ChunkProviderClient(World world) {
		chunkMapping = new LongHashMap();
		field_889_c = new ArrayList();
		blankChunk = new EmptyChunk(world, new byte[256 * world.worldHeight], 0, 0);
		worldObj = world;
	}

	public boolean chunkExists(int i, int j) {
		if (this != null) {
			return true;
		}
		else {
			return chunkMapping.containsKey(ChunkCoordIntPair.chunkXZ2Int(i, j));
		}
	}

	public void func_539_c(int i, int j) {
		Chunk chunk = provideChunk(i, j);
		if (!chunk.isEmpty()) {
			chunk.onChunkUnload();
		}
		chunkMapping.remove(ChunkCoordIntPair.chunkXZ2Int(i, j));
		field_889_c.remove(chunk);
	}

	public Chunk loadChunk(int i, int j) {
		byte abyte0[] = new byte[256 * worldObj.worldHeight];
		Chunk chunk = new Chunk(worldObj, abyte0, i, j);
		Arrays.fill(chunk.skylightMap.data, (byte) - 1);
		chunkMapping.add(ChunkCoordIntPair.chunkXZ2Int(i, j), chunk);
		chunk.isChunkLoaded = true;
		return chunk;
	}

	public Chunk provideChunk(int i, int j) {
		Chunk chunk = (Chunk)chunkMapping.getValueByKey(ChunkCoordIntPair.chunkXZ2Int(i, j));
		if (chunk == null) {
			return blankChunk;
		}
		else {
			return chunk;
		}
	}

	public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate) {
		return true;
	}

	public boolean unload100OldestChunks() {
		return false;
	}

	public boolean canSave() {
		return false;
	}

	public void populate(IChunkProvider ichunkprovider, int i, int j) {
	}

	public String makeString() {
		return (new StringBuilder()).append("MultiplayerChunkCache: ").append(chunkMapping.getNumHashElements()).toString();
	}

	public List func_40377_a(EnumCreatureType enumcreaturetype, int i, int j, int k) {
		return null;
	}

	public ChunkPosition func_40376_a(World world, String s, int i, int j, int k) {
		return null;
	}
}
