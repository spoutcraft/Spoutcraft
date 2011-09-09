package org.spoutcraft.spoutcraftapi.event.block;

import org.spoutcraft.spoutcraftapi.block.Block;
import org.spoutcraft.spoutcraftapi.entity.Player;
import org.spoutcraft.spoutcraftapi.event.Cancellable;
import org.spoutcraft.spoutcraftapi.event.HandlerList;

public class BlockBreakEvent extends BlockEvent<BlockBreakEvent> implements Cancellable {
	
	protected Player player;

	protected BlockBreakEvent(Block block, Player player) {
		super(block);
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	public static final HandlerList<BlockBreakEvent> handlers = new HandlerList<BlockBreakEvent>();
	
	@Override
	protected HandlerList<BlockBreakEvent> getHandlers() {
		return handlers;
	}

	@Override
	protected String getEventName() {
		return "Block Damage Event";
	}

}
