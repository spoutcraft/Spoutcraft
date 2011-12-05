package org.spoutcraft.spoutcraftapi.inventory;

public interface MaterialManager {

	public float getFriction(org.spoutcraft.spoutcraftapi.material.Block block);
	
	public void setFriction(org.spoutcraft.spoutcraftapi.material.Block block, float friction);
	
	public void resetFriction(org.spoutcraft.spoutcraftapi.material.Block block);
	
	public float getHardness(org.spoutcraft.spoutcraftapi.material.Block block);
	
	public void setHardness(org.spoutcraft.spoutcraftapi.material.Block block, float hardness);
	
	public void resetHardness(org.spoutcraft.spoutcraftapi.material.Block block);
	
	public boolean isOpaque(org.spoutcraft.spoutcraftapi.material.Block block);
	
	public void setOpaque(org.spoutcraft.spoutcraftapi.material.Block block, boolean opacity);
	
	public void resetOpacity(org.spoutcraft.spoutcraftapi.material.Block block);
	
	public int getLightLevel(org.spoutcraft.spoutcraftapi.material.Block block);
	
	public void setLightLevel(org.spoutcraft.spoutcraftapi.material.Block block, int level);
	
	public void resetLightLevel(org.spoutcraft.spoutcraftapi.material.Block block);
}
