package org.spoutcraft.spoutcraftapi.material.block;

import org.spoutcraft.spoutcraftapi.material.SolidBlock;

public class DoubleSlabs extends GenericBlock implements SolidBlock {

	public DoubleSlabs(int id, int data) {
		super(id, data);
	}

	public boolean isFallingBlock() {
		return false;
	}

}
