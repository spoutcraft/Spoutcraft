package org.spoutcraft.spoutcraftapi.material;

import org.spoutcraft.spoutcraftapi.block.design.BlockDesign;

public interface Block extends Material{
	
	public float getFriction();
	
	public Block setFriction(float slip);
	
	public float getHardness();
	
	public Block setHardness(float hardness);
	
	public boolean isOpaque();
	
	public Block setOpaque(boolean opaque);
	
	public int getLightLevel();
	
	public Block setLightLevel(int level);
	
	public BlockDesign getBlockDesign();
	
	public Block setBlockDesign(BlockDesign design);

}
