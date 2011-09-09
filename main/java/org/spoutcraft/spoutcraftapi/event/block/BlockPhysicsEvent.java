package org.spoutcraft.spoutcraftapi.event.block;

import org.spoutcraft.spoutcraftapi.block.Block;
import org.spoutcraft.spoutcraftapi.event.Cancellable;

public class BlockPhysicsEvent extends BlockEvent implements Cancellable {

	protected boolean cancel = false;
	protected int changedId;

	protected BlockPhysicsEvent(Block block, int changedId) {
		super(Type.BLOCK_PHYSICS, block);
		this.changedId = changedId;
	}
	
	public int getChangedTypeId() {
		return changedId;
	}

	public boolean isCancelled() {
		return cancel;
	}

	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

}
