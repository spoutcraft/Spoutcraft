package org.getspout.spout;

import org.spoutcraft.spoutcraftapi.World;
import org.spoutcraft.spoutcraftapi.block.Block;
import org.spoutcraft.spoutcraftapi.block.Chunk;
import org.spoutcraft.spoutcraftapi.util.FixedLocation;

public class SpoutcraftWorld implements World{
	
	private final net.minecraft.src.World handle;
	public SpoutcraftWorld(net.minecraft.src.World world) {
		handle = world;
	}

	public boolean getAllowAnimals() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean getAllowMonsters() {
		// TODO Auto-generated method stub
		return false;
	}

	public Block getBlockAt(int x, int y, int z) {
		return getChunkAt(x >> 4, z >> 4).getBlockAt(x & 0xF, y & 0x7F, z & 0xF);
	}

	public Chunk getChunkAt(Block block) {
		return handle.getChunkFromBlockCoords(block.getX(), block.getZ()).spoutChunk;
	}

	public Chunk getChunkAt(int x, int y) {
		return handle.getChunkFromChunkCoords(x, y).spoutChunk;
	}
	
	public Chunk getChunkAt(FixedLocation location) {
		return handle.getChunkFromBlockCoords(location.getBlockX(), location.getBlockZ()).spoutChunk;
	}

	public long getFullTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Block getHighestBlockAt(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getHighestBlockYAt(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Chunk[] getLoadedChunks() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getMaxHeight() {
		return 127;
	}

	public long getSeed() {
		return handle.getRandomSeed();
	}

	public long getTime() {
		return handle.getWorldTime();
	}

	public boolean isChunkLoaded(Chunk arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isChunkLoaded(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public void loadChunk(Chunk arg0) {
		// TODO Auto-generated method stub
		
	}

	public void loadChunk(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	public boolean loadChunk(int arg0, int arg1, boolean arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean refreshChunk(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean regenerateChunk(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public void save() {
		// TODO Auto-generated method stub
		
	}

	public void setFullTime(long arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setTime(long arg0) {
		// TODO Auto-generated method stub
		
	}
}
