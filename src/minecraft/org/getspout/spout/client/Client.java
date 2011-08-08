package org.getspout.spout.client;

import org.getspout.spout.SpoutVersion;
import org.getspout.spout.inventory.ItemManager;
import org.getspout.spout.packet.PacketManager;
import org.getspout.spout.player.ActivePlayer;
import org.getspout.spout.player.Player;
import org.getspout.spout.player.SkyManager;

public interface Client {
	
	public ItemManager getItemManager();
	
	public SkyManager getSkyManager();
	
	public PacketManager getPacketManager();
	
	public ActivePlayer getActivePlayer();
	
	public boolean isCheatMode();
	
	public boolean isSpoutEnabled();
	
	public SpoutVersion getServerVersion();
	
	public Player getPlayerFromId(int id);
	
	public long getTick();
}
