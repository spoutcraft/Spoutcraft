package org.spoutcraft.spoutcraftapi.material;

public interface Plant extends Block{
	
	public boolean isHasGrowthStages();
	
	public int getNumGrowthStages();
		
	public int getMinimumLightToGrow();

}
