package org.spoutcraft.spoutcraftapi.material.item;

import org.spoutcraft.spoutcraftapi.material.Tool;

public class GenericTool extends GenericItem implements Tool {

	public GenericTool(String name, int id) {
		super(name, id);
	}

	public int getToolPower() {
		return 0;
	}

	public void setToolPower() {
		
	}

}
