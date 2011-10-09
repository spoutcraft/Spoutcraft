package org.spoutcraft.spoutcraftapi.material;

import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.block.design.BlockDesign;


public interface CustomBlock extends Block {

	public BlockDesign getBlockDesign();
	
	public CustomBlock setBlockDesign(BlockDesign design);
	
	public int getCustomId();
	
	public String getFullName();
	
	public CustomBlock setCustomMetaData(int customMetaData);

	public int getCustomMetaData();
	
	public Addon getAddon();
	
	public CustomItem getBlockItem();
	
	public int getBlockId();
}
