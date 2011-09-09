package org.spoutcraft.spoutcraftapi.event.block;

import org.spoutcraft.spoutcraftapi.block.Block;
import org.spoutcraft.spoutcraftapi.event.Event;

public class BlockEvent extends Event{
	protected Block block;

	protected BlockEvent(Type type, Block block) {
		super(type);
		this.block = block;
	}

	public Block getBlock() {
		return block;
	}
}
