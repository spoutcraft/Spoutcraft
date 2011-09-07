package org.spoutcraft.spoutcraftapi.event.player;

import org.spoutcraft.spoutcraftapi.block.Block;
import org.spoutcraft.spoutcraftapi.entity.Player;
import org.spoutcraft.spoutcraftapi.event.Cancellable;

public class PlayerBedEnterEvent extends PlayerEvent implements Cancellable {
	
	protected boolean cancel = false;
	protected Block bed;

	protected PlayerBedEnterEvent(Player player, Block bed) {
		super(Type.PLAYER_BED_ENTER, player);
		this.bed = bed;

	}
	
	public Block getBed() {
		return bed;
	}

	public boolean isCancelled() {
		return cancel;
	}

	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

}
