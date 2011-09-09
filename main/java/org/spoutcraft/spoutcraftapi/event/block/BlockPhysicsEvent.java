package org.spoutcraft.spoutcraftapi.event.block;

import org.spoutcraft.spoutcraftapi.block.Block;
import org.spoutcraft.spoutcraftapi.event.Cancellable;
import org.spoutcraft.spoutcraftapi.event.HandlerList;

public class BlockPhysicsEvent extends BlockEvent<BlockPhysicsEvent> implements Cancellable {

	protected int changedId;

	protected BlockPhysicsEvent(Block block, int changedId) {
		super(block);
		this.changedId = changedId;
	}
	
	public int getChangedTypeId() {
		return changedId;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
	
	public static final HandlerList<BlockPhysicsEvent> handlers = new HandlerList<BlockPhysicsEvent>();
	
	@Override
	protected HandlerList<BlockPhysicsEvent> getHandlers() {
		return handlers;
	}

	@Override
	protected String getEventName() {
		return "Block Physics Event";
	}

}
