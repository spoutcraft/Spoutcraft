package net.minecraft.src;

import gnu.trove.map.hash.TLongObjectHashMap;
import gnu.trove.set.hash.TLongHashSet; // Spout
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Chunk;
import net.minecraft.src.ChunkCoordIntPair;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.ChunkPosition;
import net.minecraft.src.EmptyChunk;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumCreatureType;
import net.minecraft.src.IChunkLoader;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.IProgressUpdate;
import net.minecraft.src.LongHashMap;
import net.minecraft.src.World;

public class ChunkProvider implements IChunkProvider {

	private TLongHashSet droppedChunksSet = new TLongHashSet(); //Spout
	private Chunk emptyChunk;
	private IChunkProvider chunkProvider;
	private IChunkLoader chunkLoader;
	private TLongObjectHashMap<Chunk> chunkMap = new TLongObjectHashMap<Chunk>(1000); //Spout
	//private List<Chunk> chunkList = new ArrayList<Chunk>();
	private World worldObj;
	private int field_35392_h;
	//Spout start
	private int lastX = Integer.MAX_VALUE, lastZ = Integer.MAX_VALUE;
	private Chunk last;
	public static long cacheHits = 0;
	public static long cacheMisses = 0L;
	//Spout end

	public ChunkProvider(World par1World, IChunkLoader par2IChunkLoader, IChunkProvider par3IChunkProvider) {
		this.emptyChunk = new EmptyChunk(par1World, 0, 0);
		this.worldObj = par1World;
		this.chunkLoader = par2IChunkLoader;
		this.chunkProvider = par3IChunkProvider;
		chunkMap.setAutoCompactionFactor(0.0F); //Spout
		droppedChunksSet.setAutoCompactionFactor(0.0F); //Spout
	}

	public boolean chunkExists(int par1, int par2) {
		//Spout start
		if (par1 == lastX && par2 == lastZ && Thread.currentThread() == Minecraft.mainThread) {
			cacheHits++;
			return true;
		}
		//Spout end
		return this.chunkMap.containsKey(ChunkCoordIntPair.chunkXZ2Int(par1, par2));
	}

	public void dropChunk(int par1, int par2) {
		ChunkCoordinates var3 = this.worldObj.getSpawnPoint();
		int var4 = par1 * 16 + 8 - var3.posX;
		int var5 = par2 * 16 + 8 - var3.posZ;
		short var6 = 128;
		if (var4 < -var6 || var4 > var6 || var5 < -var6 || var5 > var6) {
			/*Spout start
			this.droppedChunksSet.add(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(par1, par2)));
			*/
			this.droppedChunksSet.add(ChunkCoordIntPair.chunkXZ2Int(par1, par2));
			//Spout end
		}

	}

	public Chunk loadChunk(int par1, int par2) {
		long var3 = ChunkCoordIntPair.chunkXZ2Int(par1, par2);
		/*Spout start
		this.droppedChunksSet.remove(Long.valueOf(var3));
		Chunk var5 = (Chunk)this.chunkMap.getValueByKey(var3);
		*/
		this.droppedChunksSet.remove(var3); 
		Chunk var5 = (Chunk)this.chunkMap.get(var3);
		//Spout end
		
		if (var5 == null) {
			int var6 = 1875004;
			if (par1 < -var6 || par2 < -var6 || par1 >= var6 || par2 >= var6) {
				return this.emptyChunk;
			}

			var5 = this.loadChunkFromFile(par1, par2);
			if (var5 == null) {
				if (this.chunkProvider == null) {
					var5 = this.emptyChunk;
				} else {
					var5 = this.chunkProvider.provideChunk(par1, par2);
				}
			}

			//Spout start
			this.chunkMap.put(var3, var5);
			//this.chunkList.add(var5);
			//Spout end
			if (var5 != null) {
				var5.func_4143_d();
				var5.onChunkLoad();
			}

			var5.populateChunk(this, this, par1, par2);
		}
		
		//Spout start
		if (var5 != null && Thread.currentThread() == Minecraft.mainThread) {
			lastX = par1;
			lastZ = par2;
			last = var5;
			cacheMisses++;
		}
		//Spout end

		return var5;
	}

	public Chunk provideChunk(int var1, int var2) {
		//Spout start
		if (var1 == lastX && var2 == lastZ  && Thread.currentThread() == Minecraft.mainThread) {
			cacheHits++;
			return last;
		}
		Chunk var3 = (Chunk)this.chunkMap.get(ChunkCoordIntPair.chunkXZ2Int(var1, var2));
		
		if (var3 != null  && Thread.currentThread() == Minecraft.mainThread) {
			lastX = var1;
			lastZ = var2;
			last = var3;
			cacheMisses++;
		}
		//Spout end
		return var3 == null ? this.loadChunk(var1, var2) : var3;
	}

	private Chunk loadChunkFromFile(int par1, int par2) {
		if (this.chunkLoader == null) {
			return null;
		} else {
			try {
				Chunk var3 = this.chunkLoader.loadChunk(this.worldObj, par1, par2);
				if (var3 != null) {
					var3.lastSaveTime = this.worldObj.getWorldTime();
				}

				return var3;
			} catch (Exception var4) {
				var4.printStackTrace();
				return null;
			}
		}
	}

	private void saveChunkExtraData(Chunk par1Chunk) {
		if (this.chunkLoader != null) {
			try {
				this.chunkLoader.saveExtraChunkData(this.worldObj, par1Chunk);
			} catch (Exception var3) {
				var3.printStackTrace();
			}

		}
	}

	private void saveChunkData(Chunk par1Chunk) {
		if (this.chunkLoader != null) {
			try {
				par1Chunk.lastSaveTime = this.worldObj.getWorldTime();
				this.chunkLoader.saveChunk(this.worldObj, par1Chunk);
			} catch (IOException var3) {
				var3.printStackTrace();
			}

		}
	}

	public void populate(IChunkProvider par1IChunkProvider, int par2, int par3) {
		Chunk var4 = this.provideChunk(par2, par3);
		if (!var4.isTerrainPopulated) {
			var4.isTerrainPopulated = true;
			if (this.chunkProvider != null) {
				this.chunkProvider.populate(par1IChunkProvider, par2, par3);
				var4.setChunkModified();
			}
		}

	}

	public boolean saveChunks(boolean par1, IProgressUpdate par2IProgressUpdate) {
		int var3 = 0;

		//Spout start
		for (Chunk var5 : chunkMap.values(new Chunk[chunkMap.size()])) {
			//Chunk var5 = (Chunk)this.chunkList.get(var4);
		//Spout end
			if (par1) {
				this.saveChunkExtraData(var5);
			}

			if (var5.needsSaving(par1)) {
				this.saveChunkData(var5);
				var5.isModified = false;
				++var3;
				if (var3 == 24 && !par1) {
					return false;
				}
			}
		}

		if (par1) {
			if (this.chunkLoader == null) {
				return true;
			}

			this.chunkLoader.saveExtraData();
		}

		return true;
	}

	public boolean unload100OldestChunks() {
		int var1;
		for (var1 = 0; var1 < 100; ++var1) {
			if (!this.droppedChunksSet.isEmpty()) {
				/*Spout start
				Long var2 = (Long)this.droppedChunksSet.iterator().next();
				Chunk var3 = (Chunk)this.chunkMap.func_35578_a(var2.longValue());
				*/
				long var2 = this.droppedChunksSet.iterator().next();
				Chunk var3 = (Chunk)this.chunkMap.get(var2);
				//Spout end
				if (var3 != null) {
					var3.onChunkUnload();
					this.saveChunkData(var3);
					this.saveChunkExtraData(var3);
					this.droppedChunksSet.remove(var2);
					this.chunkMap.remove(var2); //Spout var2.longValue() to var2
					//this.chunkList.remove(var3);
				}
			}
		}

		//Spout start
		Chunk[] chunks = chunkMap.values(new Chunk[chunkMap.size()]);
		for (var1 = 0; var1 < 10; ++var1) {
			if (this.field_35392_h >= chunks.length) {
				//Spout end
				this.field_35392_h = 0;
				break;
			}

			Chunk var4 = chunks[this.field_35392_h++]; //Spout
			EntityPlayer var5 = this.worldObj.func_48456_a((double)(var4.xPosition << 4) + 8.0D, (double)(var4.zPosition << 4) + 8.0D, 288.0D);
			if (var5 == null) {
				this.dropChunk(var4.xPosition, var4.zPosition);
			}
		}

		if (this.chunkLoader != null) {
			this.chunkLoader.chunkTick();
		}

		return this.chunkProvider.unload100OldestChunks();
	}

	public boolean canSave() {
		return true;
	}

	public String makeString() {
		return "ServerChunkCache: " + this.chunkMap.size() + " Drop: " + this.droppedChunksSet.size(); //Spout
	}

	public List getPossibleCreatures(EnumCreatureType par1EnumCreatureType, int par2, int par3, int par4) {
		return this.chunkProvider.getPossibleCreatures(par1EnumCreatureType, par2, par3, par4);
	}

	public ChunkPosition findClosestStructure(World par1World, String par2Str, int par3, int par4, int par5) {
		return this.chunkProvider.findClosestStructure(par1World, par2Str, par3, par4, par5);
	}
}
