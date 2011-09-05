package org.spoutcraft.spoutcraftapi;

import java.util.Map;
import java.util.logging.Logger;

import org.spoutcraft.spoutcraftapi.addon.AddonManager;
import org.spoutcraft.spoutcraftapi.command.AddonCommand;
import org.spoutcraft.spoutcraftapi.command.CommandSender;
import org.spoutcraft.spoutcraftapi.entity.EntityManager;
import org.spoutcraft.spoutcraftapi.gl.SafeGL;
import org.spoutcraft.spoutcraftapi.inventory.ItemManager;
import org.spoutcraft.spoutcraftapi.player.BiomeManager;
import org.spoutcraft.spoutcraftapi.player.SkyManager;

public interface Client {

	public String getName();

	public String getVersion();

	public AddonManager getAddonManager();

	public void reload();

	public Logger getLogger();

	public AddonCommand getAddonCommand(String name);

	public boolean dispatchCommand(CommandSender sender, String commandLine);

	public Map<String, String[]> getCommandAliases();

	public String getUpdateFolder();
	
	public SafeGL getGLWrapper();
	
	public ItemManager getItemManager();
	
	public SkyManager getSkyManager();
	
	//public PacketManager getPacketManager();
	
	//public ActivePlayer getActivePlayer();
	
	public EntityManager getEntityManager();
	
	public BiomeManager getBiomeManager();
	
	public boolean isCheatMode();
	
	public boolean isSpoutEnabled();
	
	//public SpoutVersion getServerVersion();
	
	//public EntityPlayer getPlayerFromId(int id);
	
	public long getTick();
	
	public Mode getMode();
	
	
	
	public enum Mode{
		Single_Player, Multiplayer, Menu;
	}

}
