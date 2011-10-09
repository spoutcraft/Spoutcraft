package org.spoutcraft.spoutcraftapi.material;

import org.spoutcraft.spoutcraftapi.addon.Addon;

public interface CustomItem extends Item {
	
	public int getCustomId();
	
	public String getFullName();
	
	public Addon getAddon();
	
	public CustomItem setTexture(String texture);
	
	public String getTexture();
}
