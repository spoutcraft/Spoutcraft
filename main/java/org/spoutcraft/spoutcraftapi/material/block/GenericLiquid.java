package org.spoutcraft.spoutcraftapi.material.block;

import org.spoutcraft.spoutcraftapi.material.Liquid;

public class GenericLiquid extends GenericBlock implements Liquid{
	private final boolean flowing;
	public GenericLiquid(int id, boolean flowing) {
		super(id);
		this.flowing = flowing;
	}

	public boolean isFlowing() {
		return flowing;
	}

}
