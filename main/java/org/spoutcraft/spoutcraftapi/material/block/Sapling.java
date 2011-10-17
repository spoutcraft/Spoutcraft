package org.spoutcraft.spoutcraftapi.material.block;

import org.spoutcraft.spoutcraftapi.material.Plant;

public class Sapling extends GenericBlock implements Plant{

	public Sapling(String name, int data) {
		super(name, 6, data);
	}

	public boolean isHasGrowthStages() {
		return true;
	}

	public int getNumGrowthStages() {
		return 3;
	}

	public int getMinimumLightToGrow() {
		return 8;
	}

}
