package org.getspout.spout.client;

import net.minecraft.src.EntityPlayer;

import org.getspout.spout.SpoutVersion;
import org.getspout.spout.entity.EntityManager;
import org.getspout.spout.inventory.ItemManager;
import org.getspout.spout.packet.PacketManager;
import org.getspout.spout.player.ActivePlayer;
import org.getspout.spout.player.BiomeManager;
import org.getspout.spout.player.SkyManager;

public interface Client {
	
	public ItemManager getItemManager();
	
	public SkyManager getSkyManager();
	
	public PacketManager getPacketManager();
	
	public ActivePlayer getActivePlayer();
	
	public EntityManager getEntityManager();
	
	public BiomeManager getBiomeManager();
	
	public boolean isCheatMode();
	
	public boolean isSpoutEnabled();
	
	public SpoutVersion getServerVersion();
	
	public EntityPlayer getPlayerFromId(int id);
	
	public long getTick();
}
