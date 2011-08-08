package org.getspout.spout.player;

import net.minecraft.src.EntityPlayer;

public class SpoutPlayer implements Player{
	private final EntityPlayer player;
	public SpoutPlayer(EntityPlayer player) {
		this.player = player;
	}
	
	public EntityPlayer getHandle() {
		return player;
	}
}
