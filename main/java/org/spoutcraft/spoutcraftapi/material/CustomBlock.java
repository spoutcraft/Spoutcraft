package org.spoutcraft.spoutcraftapi.material;

import org.spoutcraft.spoutcraftapi.addon.Addon;

public interface CustomBlock extends Block {
	
	public int getCustomId();
	
	public String getFullName();
	
	public CustomBlock setCustomMetaData(int customMetaData);

	public int getCustomMetaData();
	
	public Addon getAddon();
	
	public CustomItem getBlockItem();
	
	public int getBlockId();
}
