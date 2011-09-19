package net.minecraft.src;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.src.Chunk;
import net.minecraft.src.ChunkCoordIntPair;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.EmptyChunk;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IChunkLoader;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.IProgressUpdate;
import net.minecraft.src.PlayerList;
import net.minecraft.src.World;
//Spout Start
import gnu.trove.TLongHashSet;
import gnu.trove.TLongIterator;
//Spout end

public class ChunkProvider implements IChunkProvider {

	//Spout Start
	private TLongHashSet droppedChunksSet = new TLongHashSet();
	//Spout End
	private Chunk field_28064_b;
	private IChunkProvider chunkProvider;
	private IChunkLoader chunkLoader;
	private PlayerList chunkMap = new PlayerList();
	private List chunkList = new ArrayList();
	private World worldObj;
	private int field_35392_h;


	public ChunkProvider(World var1, IChunkLoader var2, IChunkProvider var3) {
		//EmptyChunk var10001 = new EmptyChunk;
		var1.getClass();
		EmptyChunk var10001 = new EmptyChunk(var1, new byte[256 * 128], 0, 0);
		this.field_28064_b = var10001;
		this.worldObj = var1;
		this.chunkLoader = var2;
		this.chunkProvider = var3;
	}

	public boolean chunkExists(int var1, int var2) {
		return this.chunkMap.func_35575_b(ChunkCoordIntPair.chunkXZ2Int(var1, var2));
	}

	public void func_35391_d(int var1, int var2) {
		ChunkCoordinates var3 = this.worldObj.getSpawnPoint();
		int var4 = var1 * 16 + 8 - var3.posX;
		int var5 = var2 * 16 + 8 - var3.posZ;
		short var6 = 128;
		if(var4 < -var6 || var4 > var6 || var5 < -var6 || var5 > var6) {
			this.droppedChunksSet.add(ChunkCoordIntPair.chunkXZ2Int(var1, var2)); //Spout
		}

	}

	public Chunk loadChunk(int var1, int var2) {
		long var3 = ChunkCoordIntPair.chunkXZ2Int(var1, var2);
		this.droppedChunksSet.remove(var3);
		Chunk var5 = (Chunk)this.chunkMap.func_35578_a(var3);
		if(var5 == null) {
			int var6 = 1875004;
			if(var1 < -var6 || var2 < -var6 || var1 >= var6 || var2 >= var6) {
				return this.field_28064_b;
			}

			var5 = this.loadChunkFromFile(var1, var2);
			if(var5 == null) {
				if(this.chunkProvider == null) {
					var5 = this.field_28064_b;
				} else {
					var5 = this.chunkProvider.provideChunk(var1, var2);
				}
			}

			this.chunkMap.func_35577_a(var3, var5);
			this.chunkList.add(var5);
			if(var5 != null) {
				var5.func_4143_d();
				var5.onChunkLoad();
			}

			var5.func_35843_a(this, this, var1, var2);
		}

		return var5;
	}

	public Chunk provideChunk(int var1, int var2) {
		Chunk var3 = (Chunk)this.chunkMap.func_35578_a(ChunkCoordIntPair.chunkXZ2Int(var1, var2));
		return var3 == null?this.loadChunk(var1, var2):var3;
	}

	private Chunk loadChunkFromFile(int var1, int var2) {
		if(this.chunkLoader == null) {
			return null;
		} else {
			try {
				Chunk var3 = this.chunkLoader.loadChunk(this.worldObj, var1, var2);
				if(var3 != null) {
					var3.lastSaveTime = this.worldObj.getWorldTime();
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
				this.chunkLoader.saveExtraChunkData(this.worldObj, var1);
			} catch (Exception var3) {
				var3.printStackTrace();
			}

		}
	}

	private void func_28062_b(Chunk var1) {
		if(this.chunkLoader != null) {
			try {
				var1.lastSaveTime = this.worldObj.getWorldTime();
				this.chunkLoader.saveChunk(this.worldObj, var1);
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
		int var1;
		//Spout Start
		TLongIterator iterator = this.droppedChunksSet.iterator();
		for(var1 = 0; var1 < 100 && iterator.hasNext(); ++var1) {
			long var2 = iterator.next();
			iterator.remove();
			Chunk var3 = (Chunk)this.chunkMap.func_35578_a(var2);
			var3.onChunkUnload();
			this.func_28062_b(var3);
			this.func_28063_a(var3);
			this.droppedChunksSet.remove(var2);
			this.chunkMap.func_35574_d(var2);
			this.chunkList.remove(var3);
		}
		//Spout End

		for(var1 = 0; var1 < 10; ++var1) {
			if(this.field_35392_h >= this.chunkList.size()) {
				this.field_35392_h = 0;
				break;
			}

			Chunk var4 = (Chunk)this.chunkList.get(this.field_35392_h++);
			EntityPlayer var5 = this.worldObj.getClosestPlayer((double)(var4.xPosition << 4) + 8.0D, 64.0D, (double)(var4.zPosition << 4) + 8.0D, 288.0D);
			if(var5 == null) {
				this.func_35391_d(var4.xPosition, var4.zPosition);
			}
		}

		if(this.chunkLoader != null) {
			this.chunkLoader.func_814_a();
		}

		return this.chunkProvider.unload100OldestChunks();
	}

	public boolean canSave() {
		return true;
	}

	public String makeString() {
		return "ServerChunkCache: " + this.chunkMap.func_35576_a() + " Drop: " + this.droppedChunksSet.size();
	}
}
