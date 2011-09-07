package org.spoutcraft.spoutcraftapi.event.player;

import org.spoutcraft.spoutcraftapi.entity.Player;
import org.spoutcraft.spoutcraftapi.event.Cancellable;
import org.spoutcraft.spoutcraftapi.util.Location;

public class PlayerMoveEvent extends PlayerEvent implements Cancellable {

	private boolean cancel = false;
	private Location from;
	private Location to;

	protected PlayerMoveEvent(Player player, Location from, Location to) {
		super(Type.PLAYER_MOVE, player);
		this.from = from;
		this.to = to;
	}

	public Location getFrom() {
		return from;
	}

	public void setFrom(Location from) {
		this.from = from;
	}

	public Location getTo() {
		return to;
	}

	public void setTo(Location to) {
		this.to = to;
	}

	public boolean isCancelled() {
		return cancel;
	}

	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

}
