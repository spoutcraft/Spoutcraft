package org.spoutcraft.spoutcraftapi.material;

import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.block.design.BlockDesign;

public interface CustomBlock extends Block {
	
	public int getCustomId();
	
	public String getFullName();
	
	public Addon getAddon();
	
	public CustomItem getBlockItem();
	
	public int getBlockId();
	
	public BlockDesign getBlockDesign(int id);
	
	public Block setBlockDesign(BlockDesign design, int id);
}
