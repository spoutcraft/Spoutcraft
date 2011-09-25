package org.spoutcraft.spoutcraftapi.material.block;

import org.spoutcraft.spoutcraftapi.material.SolidBlock;

public class Slab extends GenericBlock implements SolidBlock{

	public Slab(int id, int data) {
		super(id, data);
	}

	public boolean isFallingBlock() {
		return false;
	}

}
