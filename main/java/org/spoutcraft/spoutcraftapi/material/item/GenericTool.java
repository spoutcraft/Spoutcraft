package org.spoutcraft.spoutcraftapi.material.item;

import org.spoutcraft.spoutcraftapi.material.Tool;

public class GenericTool extends GenericItem implements Tool {

	public GenericTool(int id) {
		super(id);
	}

	public int getToolPower() {
		return 0;
	}

	public void setToolPower() {
		
	}

}
