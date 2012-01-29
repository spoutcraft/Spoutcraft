package net.minecraft.src;

import java.io.IOException;
import java.util.List;

public class ChunkProviderLoadOrGenerate
	implements IChunkProvider {
	private Chunk blankChunk;
	private IChunkProvider chunkProvider;
	private IChunkLoader chunkLoader;
	private Chunk chunks[];
	private World worldObj;
	int lastQueriedChunkXPos;
	int lastQueriedChunkZPos;
	private Chunk lastQueriedChunk;
	private int curChunkX;
	private int curChunkY;

	public void setCurrentChunkOver(int i, int j) {
		curChunkX = i;
		curChunkY = j;
	}

	public boolean canChunkExist(int i, int j) {
		byte byte0 = 15;
		return i >= curChunkX - byte0 && j >= curChunkY - byte0 && i <= curChunkX + byte0 && j <= curChunkY + byte0;
	}

	public boolean chunkExists(int i, int j) {
		if (!canChunkExist(i, j)) {
			return false;
		}
		if (i == lastQueriedChunkXPos && j == lastQueriedChunkZPos && lastQueriedChunk != null) {
			return true;
		}
		else {
			int k = i & 0x1f;
			int l = j & 0x1f;
			int i1 = k + l * 32;
			return chunks[i1] != null && (chunks[i1] == blankChunk || chunks[i1].isAtLocation(i, j));
		}
	}

	public Chunk loadChunk(int i, int j) {
		return provideChunk(i, j);
	}

	public Chunk provideChunk(int i, int j) {
		if (i == lastQueriedChunkXPos && j == lastQueriedChunkZPos && lastQueriedChunk != null) {
			return lastQueriedChunk;
		}
		if (!worldObj.findingSpawnPoint && !canChunkExist(i, j)) {
			return blankChunk;
		}
		int k = i & 0x1f;
		int l = j & 0x1f;
		int i1 = k + l * 32;
		if (!chunkExists(i, j)) {
			if (chunks[i1] != null) {
				chunks[i1].onChunkUnload();
				saveChunk(chunks[i1]);
				saveExtraChunkData(chunks[i1]);
			}
			Chunk chunk = func_542_c(i, j);
			if (chunk == null) {
				if (chunkProvider == null) {
					chunk = blankChunk;
				}
				else {
					chunk = chunkProvider.provideChunk(i, j);
					chunk.removeUnknownBlocks();
				}
			}
			chunks[i1] = chunk;
			chunk.func_4143_d();
			if (chunks[i1] != null) {
				chunks[i1].onChunkLoad();
			}
			chunks[i1].populateChunk(this, this, i, j);
		}
		lastQueriedChunkXPos = i;
		lastQueriedChunkZPos = j;
		lastQueriedChunk = chunks[i1];
		return chunks[i1];
	}

	private Chunk func_542_c(int i, int j) {
		if (chunkLoader == null) {
			return blankChunk;
		}
		try {
			Chunk chunk = chunkLoader.loadChunk(worldObj, i, j);
			if (chunk != null) {
				chunk.lastSaveTime = worldObj.getWorldTime();
			}
			return chunk;
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
		return blankChunk;
	}

	private void saveExtraChunkData(Chunk chunk) {
		if (chunkLoader == null) {
			return;
		}
		try {
			chunkLoader.saveExtraChunkData(worldObj, chunk);
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private void saveChunk(Chunk chunk) {
		if (chunkLoader == null) {
			return;
		}
		try {
			chunk.lastSaveTime = worldObj.getWorldTime();
			chunkLoader.saveChunk(worldObj, chunk);
		}
		catch (IOException ioexception) {
			ioexception.printStackTrace();
		}
	}

	public void populate(IChunkProvider ichunkprovider, int i, int j) {
		Chunk chunk = provideChunk(i, j);
		if (!chunk.isTerrainPopulated) {
			chunk.isTerrainPopulated = true;
			if (chunkProvider != null) {
				chunkProvider.populate(ichunkprovider, i, j);
				chunk.setChunkModified();
			}
		}
	}

	public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate) {
		int i = 0;
		int j = 0;
		if (iprogressupdate != null) {
			for (int k = 0; k < chunks.length; k++) {
				if (chunks[k] != null && chunks[k].needsSaving(flag)) {
					j++;
				}
			}
		}
		int l = 0;
		for (int i1 = 0; i1 < chunks.length; i1++) {
			if (chunks[i1] == null) {
				continue;
			}
			if (flag && !chunks[i1].neverSave) {
				saveExtraChunkData(chunks[i1]);
			}
			if (!chunks[i1].needsSaving(flag)) {
				continue;
			}
			saveChunk(chunks[i1]);
			chunks[i1].isModified = false;
			if (++i == 2 && !flag) {
				return false;
			}
			if (iprogressupdate != null && ++l % 10 == 0) {
				iprogressupdate.setLoadingProgress((l * 100) / j);
			}
		}

		if (flag) {
			if (chunkLoader == null) {
				return true;
			}
			chunkLoader.saveExtraData();
		}
		return true;
	}

	public boolean unload100OldestChunks() {
		if (chunkLoader != null) {
			chunkLoader.func_814_a();
		}
		return chunkProvider.unload100OldestChunks();
	}

	public boolean canSave() {
		return true;
	}

	public String makeString() {
		return (new StringBuilder()).append("ChunkCache: ").append(chunks.length).toString();
	}

	public List func_40377_a(EnumCreatureType enumcreaturetype, int i, int j, int k) {
		return chunkProvider.func_40377_a(enumcreaturetype, i, j, k);
	}

	public ChunkPosition func_40376_a(World world, String s, int i, int j, int k) {
		return chunkProvider.func_40376_a(world, s, i, j, k);
	}
}
