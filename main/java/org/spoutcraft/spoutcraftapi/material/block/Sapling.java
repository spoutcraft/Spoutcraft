package org.spoutcraft.spoutcraftapi.material.block;

import org.spoutcraft.spoutcraftapi.material.Plant;

public class Sapling extends GenericBlock implements Plant{

	public Sapling(int data) {
		super(6, data);
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
