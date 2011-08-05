package net.minecraft.src;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.src.Chunk;
import net.minecraft.src.ChunkCoordIntPair;
import net.minecraft.src.EmptyChunk;
import net.minecraft.src.IChunkLoader;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.IProgressUpdate;
import net.minecraft.src.World;
//Spout Start
import it.unimi.dsi.fastutil.ints.*;
//Spout End

public class ChunkProvider implements IChunkProvider {

	//Spout Start
	private IntSet droppedChunksSet = new IntOpenHashSet();
	//Spout End
	private Chunk field_28064_b;
	private IChunkProvider chunkProvider;
	private IChunkLoader chunkLoader;
	private Map chunkMap = new HashMap();
	private List chunkList = new ArrayList();
	private World field_28066_g;


	public ChunkProvider(World var1, IChunkLoader var2, IChunkProvider var3) {
		this.field_28064_b = new EmptyChunk(var1, new byte['\u8000'], 0, 0);
		this.field_28066_g = var1;
		this.chunkLoader = var2;
		this.chunkProvider = var3;
	}

	public boolean chunkExists(int var1, int var2) {
		return this.chunkMap.containsKey(Integer.valueOf(ChunkCoordIntPair.chunkXZ2Int(var1, var2)));
	}

	public Chunk prepareChunk(int var1, int var2) {
		int var3 = ChunkCoordIntPair.chunkXZ2Int(var1, var2);
		this.droppedChunksSet.remove(Integer.valueOf(var3));
		Chunk var4 = (Chunk)this.chunkMap.get(Integer.valueOf(var3));
		if(var4 == null) {
			var4 = this.loadChunkFromFile(var1, var2);
			if(var4 == null) {
				if(this.chunkProvider == null) {
					var4 = this.field_28064_b;
				} else {
					var4 = this.chunkProvider.provideChunk(var1, var2);
				}
			}

			this.chunkMap.put(Integer.valueOf(var3), var4);
			this.chunkList.add(var4);
			if(var4 != null) {
				var4.func_4143_d();
				var4.onChunkLoad();
			}

			if(!var4.isTerrainPopulated && this.chunkExists(var1 + 1, var2 + 1) && this.chunkExists(var1, var2 + 1) && this.chunkExists(var1 + 1, var2)) {
				this.populate(this, var1, var2);
			}

			if(this.chunkExists(var1 - 1, var2) && !this.provideChunk(var1 - 1, var2).isTerrainPopulated && this.chunkExists(var1 - 1, var2 + 1) && this.chunkExists(var1, var2 + 1) && this.chunkExists(var1 - 1, var2)) {
				this.populate(this, var1 - 1, var2);
			}

			if(this.chunkExists(var1, var2 - 1) && !this.provideChunk(var1, var2 - 1).isTerrainPopulated && this.chunkExists(var1 + 1, var2 - 1) && this.chunkExists(var1, var2 - 1) && this.chunkExists(var1 + 1, var2)) {
				this.populate(this, var1, var2 - 1);
			}

			if(this.chunkExists(var1 - 1, var2 - 1) && !this.provideChunk(var1 - 1, var2 - 1).isTerrainPopulated && this.chunkExists(var1 - 1, var2 - 1) && this.chunkExists(var1, var2 - 1) && this.chunkExists(var1 - 1, var2)) {
				this.populate(this, var1 - 1, var2 - 1);
			}
		}

		return var4;
	}

	public Chunk provideChunk(int var1, int var2) {
		Chunk var3 = (Chunk)this.chunkMap.get(Integer.valueOf(ChunkCoordIntPair.chunkXZ2Int(var1, var2)));
		return var3 == null?this.prepareChunk(var1, var2):var3;
	}

	private Chunk loadChunkFromFile(int var1, int var2) {
		if(this.chunkLoader == null) {
			return null;
		} else {
			try {
				Chunk var3 = this.chunkLoader.loadChunk(this.field_28066_g, var1, var2);
				if(var3 != null) {
					var3.lastSaveTime = this.field_28066_g.getWorldTime();
				}

				return var3;
			} catch (Exception var4) {
				var4.printStackTrace();
				return null;
			}
		}
	}

	private void func_28063_a(Chunk var1) {
		if(this.chunkLoader != null) {
			try {
				this.chunkLoader.saveExtraChunkData(this.field_28066_g, var1);
			} catch (Exception var3) {
				var3.printStackTrace();
			}

		}
	}

	private void func_28062_b(Chunk var1) {
		if(this.chunkLoader != null) {
			try {
				var1.lastSaveTime = this.field_28066_g.getWorldTime();
				this.chunkLoader.saveChunk(this.field_28066_g, var1);
			} catch (IOException var3) {
				var3.printStackTrace();
			}

		}
	}

	public void populate(IChunkProvider var1, int var2, int var3) {
		Chunk var4 = this.provideChunk(var2, var3);
		if(!var4.isTerrainPopulated) {
			var4.isTerrainPopulated = true;
			if(this.chunkProvider != null) {
				this.chunkProvider.populate(var1, var2, var3);
				var4.setChunkModified();
			}
		}

	}

	public boolean saveChunks(boolean var1, IProgressUpdate var2) {
		int var3 = 0;

		for(int var4 = 0; var4 < this.chunkList.size(); ++var4) {
			Chunk var5 = (Chunk)this.chunkList.get(var4);
			if(var1 && !var5.neverSave) {
				this.func_28063_a(var5);
			}

			if(var5.needsSaving(var1)) {
				this.func_28062_b(var5);
				var5.isModified = false;
				++var3;
				if(var3 == 24 && !var1) {
					return false;
				}
			}
		}

		if(var1) {
			if(this.chunkLoader == null) {
				return true;
			}

			this.chunkLoader.saveExtraData();
		}

		return true;
	}

	public boolean unload100OldestChunks() {
		//Spout Start
		IntIterator iterator = this.droppedChunksSet.iterator();
		
		for(int var1 = 0; var1 < 100 && iterator.hasNext(); ++var1) {
			Integer var2 = iterator.nextInt();
			Chunk var3 = (Chunk)this.chunkMap.get(var2);
			var3.onChunkUnload();
			this.func_28062_b(var3);
			this.func_28063_a(var3);
			this.droppedChunksSet.remove(var2);
			this.chunkMap.remove(var2);
			this.chunkList.remove(var3);
		}
		//Spout End
		if(this.chunkLoader != null) {
			this.chunkLoader.func_814_a();
		}

		return this.chunkProvider.unload100OldestChunks();
	}

	public boolean canSave() {
		return true;
	}

	public String makeString() {
		return "ServerChunkCache: " + this.chunkMap.size() + " Drop: " + this.droppedChunksSet.size();
	}
}
