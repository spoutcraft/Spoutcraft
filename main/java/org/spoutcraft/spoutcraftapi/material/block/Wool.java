package org.spoutcraft.spoutcraftapi.material.block;

import org.spoutcraft.spoutcraftapi.material.SolidBlock;

public class Wool extends GenericBlock implements SolidBlock {

	public Wool(String name, int id, int data) {
		super(name, id, data);
	}

	public boolean isFallingBlock() {
		return false;
	}

}
