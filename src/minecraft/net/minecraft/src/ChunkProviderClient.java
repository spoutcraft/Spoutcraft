package net.minecraft.src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.spoutcraft.client.ChunkComparator;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.config.ConfigReader;

//Spout
import gnu.trove.map.hash.TLongObjectHashMap;

import net.minecraft.client.Minecraft;
//Spout end

public class ChunkProviderClient implements IChunkProvider {

	private Chunk blankChunk;
	private TLongObjectHashMap<Chunk> chunkMapping = new TLongObjectHashMap<Chunk>(1000); //Spout
	//private List field_889_c = new ArrayList();
	private World worldObj;
	//Spout start
	private int lastX = Integer.MAX_VALUE, lastZ = Integer.MAX_VALUE;
	private Chunk last = null;
	private int unloadableCounter = 0;
	//Spout end

	public ChunkProviderClient(World par1World) {
		this.blankChunk = new EmptyChunk(par1World, 0, 0);
		this.worldObj = par1World;
		chunkMapping.setAutoCompactionFactor(0.0F); //Spout
	}

	public boolean chunkExists(int var1, int var2) {
		/*Spout start
		if (var1 == lastX && var2 == lastZ && Thread.currentThread() == Minecraft.mainThread) {
			return last != null;
		}
		//Spout end
		return this != null ? true : this.chunkMapping.containsKey(ChunkCoordIntPair.chunkXZ2Int(var1, var2)); //Spout*/
		return true;
	}

	public void unloadChunk(int par1, int par2) {
		Chunk var3 = this.provideChunk(par1, par2);
		//Spout start
		if (!var3.isEmpty() && SpoutClient.hasAvailableRAM()) {
			var3.canBeUnloaded = true;
			return;
		}
		
		if (!var3.isEmpty()) {
			var3.onChunkUnload();
		}

		this.chunkMapping.remove(ChunkCoordIntPair.chunkXZ2Int(par1, par2));
		//this.field_889_c.remove(var3); //Spout
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
			return last;
		}
		Chunk var3 = (Chunk)this.chunkMapping.get(ChunkCoordIntPair.chunkXZ2Int(var1, var2));
		
		if (var3 != null && Thread.currentThread() == Minecraft.mainThread) {
			lastX = var1;
			lastZ = var2;
			last = var3;
		}
		//Spout end
		return var3 == null ? this.blankChunk : var3;
	}

	public boolean saveChunks(boolean par1, IProgressUpdate par2IProgressUpdate) {
		return true;
	}

	public boolean unload100OldestChunks() {
		//Spout start
		if (!SpoutClient.hasAvailableRAM()) {
			return false;
		}
		int desiredChunks;
		int range;
		switch(Minecraft.theMinecraft.gameSettings.renderDistance) {
			case 0: desiredChunks = 600; range = 10; break;
			case 1: desiredChunks = 400; range = 8; break;
			case 2: desiredChunks = 200; range = 7; break;
			case 3: desiredChunks = 100; range = 5; break;
			default: desiredChunks = 200; range = 7; break;
		}
		if (ConfigReader.farView){
			desiredChunks *= 2;
		}
		
		if (chunkMapping.size() > desiredChunks) {
			unloadableCounter++;
			final int delay = 40;
			if (unloadableCounter > delay) {
				ArrayList<Chunk> chunks = new ArrayList<Chunk>(chunkMapping.size());
				chunks.addAll(chunkMapping.valueCollection());
				Collections.sort(chunks, new ChunkComparator());
				
				for (int i = 0; i < 20; i++) {
					Chunk c = chunks.get(i);
					if (c.canBeUnloaded) {
						c.onChunkUnload();
						chunkMapping.remove(ChunkCoordIntPair.chunkXZ2Int(c.xPosition, c.zPosition));
					}
				}
				unloadableCounter = 0;
			}
		}
		//Spout end
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

	public int func_73152_e() {
		return chunkMapping.size(); // Spout - this.chunkListing.size();
	}
}
