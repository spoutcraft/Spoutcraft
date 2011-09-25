package org.spoutcraft.spoutcraftapi.material.block;

import org.spoutcraft.spoutcraftapi.material.SolidBlock;

public class Wool extends GenericBlock implements SolidBlock {

	public Wool(int id, int data) {
		super(id, data);
	}

	public boolean isFallingBlock() {
		return false;
	}

}
