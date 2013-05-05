package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
// Spout
import gnu.trove.map.hash.TLongObjectHashMap;
// Spout End

public class ChunkProviderClient implements IChunkProvider {

	/**
	 * The completely empty chunk used by ChunkProviderClient when chunkMapping doesn't contain the requested coordinates.
	 */
	private Chunk blankChunk;

	/**
	 * The mapping between ChunkCoordinates and Chunks that ChunkProviderClient maintains.
	 */
	// Spout Start
	private TLongObjectHashMap<Chunk> chunkMapping = new TLongObjectHashMap<Chunk>(1000);
	// Spout End
	/**
	 * This may have been intended to be an iterable version of all currently loaded chunks (MultiplayerChunkCache), with
	 * identical contents to chunkMapping's values. However it is never actually added to.
	 */
	// Spout Start - Unused
	//private List chunkListing = new ArrayList();
	// Spout End

	/** Reference to the World object. */
	private World worldObj;

	public ChunkProviderClient(World par1World) {
		this.blankChunk = new EmptyChunk(par1World, 0, 0);
		this.worldObj = par1World;
		// Spout Start
		chunkMapping.setAutoCompactionFactor(0.0F);
		// Spout End
	}

	/**
	 * Checks to see if a chunk exists at x, y
	 */
	// Spout Start
	public boolean chunkExists(int var1, int var2) {
		return this != null ? true : this.chunkMapping.containsKey(ChunkCoordIntPair.chunkXZ2Int(var1, var2));
	// Spout End
	}

	/**
	 * Unload chunk from ChunkProviderClient's hashmap. Called in response to a Packet50PreChunk with its mode field set to
	 * false
	 */
	public void unloadChunk(int par1, int par2) {
		Chunk var3 = this.provideChunk(par1, par2);

		if (!var3.isEmpty()) {
			var3.onChunkUnload();
		}

		this.chunkMapping.remove(ChunkCoordIntPair.chunkXZ2Int(par1, par2));
		// Spout Start - Unused
		//this.chunkListing.remove(var3);
		// Spout End
	}

	/**
	 * loads or generates the chunk at the chunk location specified
	 */
	public Chunk loadChunk(int par1, int par2) {
		Chunk var3 = new Chunk(this.worldObj, par1, par2);
		// Spout Start
		this.chunkMapping.put(ChunkCoordIntPair.chunkXZ2Int(par1, par2), var3);
		// Spout End
		var3.isChunkLoaded = true;
		return var3;
	}

	/**
	 * Will return back a chunk, if it doesn't exist and its not a MP client it will generates all the blocks for the
	 * specified chunk from the map seed and chunk seed
	 */
	// Spout Start - public
	public Chunk provideChunk(int var1, int var2) {
		Chunk var3 = (Chunk)this.chunkMapping.get(ChunkCoordIntPair.chunkXZ2Int(var1, var2));
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
	
	public void func_104112_b() {}

	/**
	 * Unloads chunks that are marked to be unloaded. This is not guaranteed to unload every such chunk.
	 */
	public boolean unloadQueuedChunks() {
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
		// Spout Start
		return "MultiplayerChunkCache: " + this.chunkMapping.size();
		// Spout End
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
		// Spout Start - Was this.chunkListing.size();
		return chunkMapping.size();
		// Spout End
	}

	public void recreateStructures(int par1, int par2) {}
}
