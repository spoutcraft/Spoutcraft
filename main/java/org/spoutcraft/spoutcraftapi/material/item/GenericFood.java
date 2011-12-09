package org.spoutcraft.spoutcraftapi.material.item;

import org.spoutcraft.spoutcraftapi.material.Food;

public class GenericFood extends GenericItem implements Food {
	private final int hunger;
	public GenericFood(String name, int id, int hunger) {
		super(name, id);
		this.hunger = hunger;
	}

	public int getHungerRestored() {
		return hunger;
	}

}
