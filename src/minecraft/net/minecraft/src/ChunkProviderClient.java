package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.src.Chunk;
import net.minecraft.src.ChunkCoordIntPair;
import net.minecraft.src.ChunkPosition;
import net.minecraft.src.EmptyChunk;
import net.minecraft.src.EnumCreatureType;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.IProgressUpdate;
import net.minecraft.src.LongHashMap;
import net.minecraft.src.World;

//Spout start
import java.util.Arrays;

import gnu.trove.map.hash.TLongObjectHashMap;

import net.minecraft.client.Minecraft;
//Spout end

public class ChunkProviderClient implements IChunkProvider {

	private Chunk blankChunk;
	private TLongObjectHashMap<Chunk> chunkMapping = new TLongObjectHashMap<Chunk>(1000); //Spout
	private List field_889_c = new ArrayList();
	private World worldObj;
	//Spout start
	private int lastX = Integer.MAX_VALUE, lastZ = Integer.MAX_VALUE;
	private Chunk last;
	public static long cacheHits = 0;
	public static long cacheMisses = 0L;
	//Spout end

	public ChunkProviderClient(World par1World) {
		this.blankChunk = new EmptyChunk(par1World, 0, 0);
		this.worldObj = par1World;
		chunkMapping.setAutoCompactionFactor(0.0F); //Spout
	}

	public boolean chunkExists(int var1, int var2) {
		//Spout start
		if (var1 == lastX && var2 == lastZ && Thread.currentThread() == Minecraft.mainThread) {
			cacheHits++;
			return true;
		}
		//Spout end
		return this != null ? true : this.chunkMapping.containsKey(ChunkCoordIntPair.chunkXZ2Int(var1, var2)); //Spout
	}

	public void func_539_c(int par1, int par2) {
		Chunk var3 = this.provideChunk(par1, par2);
		if (!var3.isEmpty()) {
			var3.onChunkUnload();
		}

		this.chunkMapping.remove(ChunkCoordIntPair.chunkXZ2Int(par1, par2));
		this.field_889_c.remove(var3);
	}

	public Chunk loadChunk(int par1, int par2) {
		Chunk var3 = new Chunk(this.worldObj, par1, par2);
		this.chunkMapping.put(ChunkCoordIntPair.chunkXZ2Int(par1, par2), var3); //Spout
		var3.isChunkLoaded = true;
		return var3;
	}

	public Chunk provideChunk(int var1, int var2) {
		//Spout start
		if (var1 == lastX && var2 == lastZ && Thread.currentThread() == Minecraft.mainThread) {
			cacheHits++;
			return last;
		}
		Chunk var3 = (Chunk)this.chunkMapping.get(ChunkCoordIntPair.chunkXZ2Int(var1, var2));
		
		if (var3 != null && Thread.currentThread() == Minecraft.mainThread) {
			lastX = var1;
			lastZ = var2;
			last = var3;
			cacheMisses++;
		}
		//Spout end
		return var3 == null ? this.blankChunk : var3;
	}

	public boolean saveChunks(boolean par1, IProgressUpdate par2IProgressUpdate) {
		return true;
	}

	public boolean unload100OldestChunks() {
		return false;
	}

	public boolean canSave() {
		return false;
	}

	public void populate(IChunkProvider par1IChunkProvider, int par2, int par3) {}

	public String makeString() {
		return "MultiplayerChunkCache: " + this.chunkMapping.size(); //Spout
	}

	public List getPossibleCreatures(EnumCreatureType par1EnumCreatureType, int par2, int par3, int par4) {
		return null;
	}

	public ChunkPosition findClosestStructure(World par1World, String par2Str, int par3, int par4, int par5) {
		return null;
	}
}
