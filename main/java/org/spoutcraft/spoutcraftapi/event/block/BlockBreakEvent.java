package org.spoutcraft.spoutcraftapi.event.block;

import org.spoutcraft.spoutcraftapi.block.Block;
import org.spoutcraft.spoutcraftapi.entity.Player;
import org.spoutcraft.spoutcraftapi.event.Cancellable;

public class BlockBreakEvent extends BlockEvent implements Cancellable {
	protected Player player;
	protected boolean cancel = false;

	protected BlockBreakEvent(Block block, Player player) {
		super(Type.BLOCK_BREAK, block);
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public boolean isCancelled() {
		return cancel;
	}

	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

}
