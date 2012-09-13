package net.minecraft.src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.spoutcraft.client.ChunkComparator;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.config.ConfigReader;

// Spout
import gnu.trove.map.hash.TLongObjectHashMap;

import net.minecraft.client.Minecraft;
// Spout End

public class ChunkProviderClient implements IChunkProvider {

	/**
	 * The completely empty chunk used by ChunkProviderClient when chunkMapping doesn't contain the requested coordinates.
	 */
	private Chunk blankChunk;

	/**
	 * The mapping between ChunkCoordinates and Chunks that ChunkProviderClient maintains.
	 */
	private TLongObjectHashMap<Chunk> chunkMapping = new TLongObjectHashMap<Chunk>(1000); // Spout
	//private List field_889_c = new ArrayList();

	/** Reference to the World object. */
	private World worldObj;
	// Spout Start
	private int lastX = Integer.MAX_VALUE, lastZ = Integer.MAX_VALUE;
	private Chunk last = null;
	private int unloadableCounter = 0;
	// Spout End

	public ChunkProviderClient(World par1World) {
		this.blankChunk = new EmptyChunk(par1World, 0, 0);
		this.worldObj = par1World;
		chunkMapping.setAutoCompactionFactor(0.0F); // Spout
	}

	/**
	 * Checks to see if a chunk exists at x, y
	 */
	public boolean chunkExists(int var1, int var2) {
		/*Spout start
		if (var1 == lastX && var2 == lastZ && Thread.currentThread() == Minecraft.mainThread) {
			return last != null;
		}
		// Spout End
		return this != null ? true : this.chunkMapping.containsKey(ChunkCoordIntPair.chunkXZ2Int(var1, var2)); // Spout*/
		return true;
	}

	/**
	 * Unload chunk from ChunkProviderClient's hashmap. Called in response to a Packet50PreChunk with its mode field set to
	 * false
	 */
	public void unloadChunk(int par1, int par2) {
		Chunk var3 = this.provideChunk(par1, par2);
		// Spout Start
		if (!var3.isEmpty() && SpoutClient.hasAvailableRAM()) {
			var3.canBeUnloaded = true;
			return;
		}
		
		if (!var3.isEmpty()) {
			var3.onChunkUnload();
		}

		this.chunkMapping.remove(ChunkCoordIntPair.chunkXZ2Int(par1, par2));
		//this.field_889_c.remove(var3); // Spout
	}

	/**
	 * loads or generates the chunk at the chunk location specified
	 */
	public Chunk loadChunk(int par1, int par2) {
		Chunk var3 = new Chunk(this.worldObj, par1, par2);
		this.chunkMapping.put(ChunkCoordIntPair.chunkXZ2Int(par1, par2), var3); // Spout
		var3.isChunkLoaded = true;
		return var3;
	}

	/**
	 * Will return back a chunk, if it doesn't exist and its not a MP client it will generates all the blocks for the
	 * specified chunk from the map seed and chunk seed
	 */
	public Chunk provideChunk(int var1, int var2) {
		// Spout Start
		if (var1 == lastX && var2 == lastZ && Thread.currentThread() == Minecraft.mainThread) {
			return last;
		}
		Chunk var3 = (Chunk)this.chunkMapping.get(ChunkCoordIntPair.chunkXZ2Int(var1, var2));
		
		if (var3 != null && Thread.currentThread() == Minecraft.mainThread) {
			lastX = var1;
			lastZ = var2;
			last = var3;
		}
		// Spout End
		return var3 == null ? this.blankChunk : var3;
	}

	/**
	 * Two modes of operation: if passed true, save all Chunks in one go.  If passed false, save up to two chunks.  Return
	 * true if all chunks have been saved.
	 */
	public boolean saveChunks(boolean par1, IProgressUpdate par2IProgressUpdate) {
		return true;
	}

	/**
	 * Unloads the 100 oldest chunks from memory, due to a bug with chunkSet.add() never being called it thinks the list is
	 * always empty and will not remove any chunks.
	 */
	public boolean unload100OldestChunks() {
		// Spout Start
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
		// Spout End
		return false;
	}

	/**
	 * Returns if the IChunkProvider supports saving.
	 */
	public boolean canSave() {
		return false;
	}

	/**
	 * Populates chunk with ores etc etc
	 */
	public void populate(IChunkProvider par1IChunkProvider, int par2, int par3) {}

	/**
	 * Converts the instance data to a readable string.
	 */
	public String makeString() {
		return "MultiplayerChunkCache: " + this.chunkMapping.size(); // Spout
	}

	/**
	 * Returns a list of creatures of the specified type that can spawn at the given location.
	 */
	public List getPossibleCreatures(EnumCreatureType par1EnumCreatureType, int par2, int par3, int par4) {
		return null;
	}

	/**
	 * Returns the location of the closest structure of the specified type. If not found returns null.
	 */
	public ChunkPosition findClosestStructure(World par1World, String par2Str, int par3, int par4, int par5) {
		return null;
	}

	public int getLoadedChunkCount() {
		return chunkMapping.size(); // Spout - this.chunkListing.size();
	}
}
