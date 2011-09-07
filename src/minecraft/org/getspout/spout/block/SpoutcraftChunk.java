package org.getspout.spout.block;

import java.lang.ref.WeakReference;

import net.minecraft.src.WorldClient;

import org.spoutcraft.spoutcraftapi.World;
import org.spoutcraft.spoutcraftapi.block.Block;
import org.spoutcraft.spoutcraftapi.block.Chunk;
import org.spoutcraft.spoutcraftapi.entity.Entity;

public class SpoutcraftChunk implements Chunk{
    private WeakReference<net.minecraft.src.Chunk> weakChunk;
    private WorldClient worldClient;
	private int x;
    private int z;
    
    public SpoutcraftChunk(net.minecraft.src.Chunk chunk) {
    	this.weakChunk = new WeakReference<net.minecraft.src.Chunk>(chunk);
    	worldClient = (WorldClient) getHandle().worldObj;
    	x = getHandle().xPosition;
    	z = getHandle().zPosition;
	}
    
    public net.minecraft.src.Chunk getHandle() {
    	net.minecraft.src.Chunk c = weakChunk.get();
        if (c == null) {
            c = worldClient.getChunkFromChunkCoords(x, z);
            weakChunk = new WeakReference<net.minecraft.src.Chunk>(c);
        }
        return c;
    }
    
	public Block getBlockAt(int x, int y, int z) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public World getWorld() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getZ() {
		return z;
	}

	@Override
	public boolean isLoaded() {
		return getWorld().isChunkLoaded(this);
	}

	@Override
	public boolean load() {
		return getWorld().loadChunk(getX(), getZ(), true);
	}

	@Override
	public boolean load(boolean generate) {
		return getWorld().loadChunk(getX(), getZ(), generate);
	}

	@Override
	public boolean unload() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unload(boolean save) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unload(boolean save, boolean safe) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Entity[] getEntities() {
		// TODO Auto-generated method stub
		return null;
	}

}
