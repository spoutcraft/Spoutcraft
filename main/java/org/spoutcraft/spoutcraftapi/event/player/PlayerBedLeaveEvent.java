package org.spoutcraft.spoutcraftapi.event.player;

import org.spoutcraft.spoutcraftapi.block.Block;
import org.spoutcraft.spoutcraftapi.entity.Player;

public class PlayerBedLeaveEvent extends PlayerEvent{
	
	protected Block bed;

	protected PlayerBedLeaveEvent(Player player, Block bed) {
		super(Type.PLAYER_BED_LEAVE, player);
		this.bed = bed;
	}
	
	public Block getBed() {
		return bed;
	}

}
