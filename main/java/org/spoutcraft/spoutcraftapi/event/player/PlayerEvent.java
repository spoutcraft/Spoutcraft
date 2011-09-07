package org.spoutcraft.spoutcraftapi.event.player;

import org.spoutcraft.spoutcraftapi.entity.Player;
import org.spoutcraft.spoutcraftapi.event.Event;

public class PlayerEvent extends Event{
	protected Player player;

	protected PlayerEvent(Type type, Player player) {
		super(type);
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}

}
