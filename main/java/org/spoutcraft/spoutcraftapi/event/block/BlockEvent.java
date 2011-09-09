package org.spoutcraft.spoutcraftapi.event.block;

import org.spoutcraft.spoutcraftapi.block.Block;
import org.spoutcraft.spoutcraftapi.event.Event;

public abstract class BlockEvent<TEvent extends BlockEvent<TEvent>> extends Event<TEvent> {
	
	protected Block block;

	protected BlockEvent(Block block) {
		this.block = block;
	}

	public Block getBlock() {
		return block;
	}
	
}
