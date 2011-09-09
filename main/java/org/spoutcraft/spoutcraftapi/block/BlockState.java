package org.spoutcraft.spoutcraftapi.block;

import org.spoutcraft.spoutcraftapi.NotImplemented;
import org.spoutcraft.spoutcraftapi.World;

@NotImplemented
public interface BlockState {
	
	@NotImplemented
	public Block getBlock();
	
	@NotImplemented
	public Chunk getChunk();
	
	@NotImplemented
	public byte getLightLevel();
	
	@NotImplemented
	public int getTypeId();
	
	@NotImplemented
	public World getWorld();
	
	@NotImplemented
	public int getX();
	
	@NotImplemented
	public int getY();
	
	@NotImplemented
	public int getZ();
	
	@NotImplemented
	public boolean setTypeId(int type);
	
	@NotImplemented
	public boolean update();

}
