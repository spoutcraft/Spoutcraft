package org.getspout.spout.block;

import org.spoutcraft.spoutcraftapi.World;
import org.spoutcraft.spoutcraftapi.block.Block;
import org.spoutcraft.spoutcraftapi.block.Chunk;

public class SpoutcraftBlock implements Block{
	private final SpoutcraftChunk chunk;
	private final int x;
	private final int y;
	private final int z;
	
	public SpoutcraftBlock(SpoutcraftChunk chunk, int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.chunk = chunk;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public float getHardness() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setHardness(float hardness) {
		// TODO Auto-generated method stub
		
	}

	public int getBlockPower() {
		// TODO Auto-generated method stub
		return 0;
	}

	public byte getLightLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getTypeId() {
		// TODO Auto-generated method stub
		return 0;
	}

	public World getWorld() {
		return chunk.getWorld();
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isLiquid() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setData(byte data) {
		// TODO Auto-generated method stub
		
	}

	public boolean setTypeId(int type) {
		// TODO Auto-generated method stub
		return false;
	}

	public Chunk getChunk() {
		return chunk;
	}

}
