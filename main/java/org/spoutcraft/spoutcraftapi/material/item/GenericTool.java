package org.spoutcraft.spoutcraftapi.material.item;

import org.spoutcraft.spoutcraftapi.material.Block;
import org.spoutcraft.spoutcraftapi.material.Tool;

public class GenericTool extends GenericItem implements Tool {

	public GenericTool(String name, int id) {
		super(name, id);
	}

	public short getDurability() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Tool setDurability(short durability) {
		// TODO Auto-generated method stub
		return this;
	}

	public float getStrengthModifier(Block block) {
		// TODO Auto-generated method stub
		return 0;
	}
		
	public Tool setStrengthModifier(Block block, float modifier) {
		// TODO Auto-generated method stub
		return this;
	}

	public Block[] getStrengthModifiedBlocks() {
		// TODO Auto-generated method stub
		return null;
	}

}
