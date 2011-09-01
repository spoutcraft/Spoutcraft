package org.spoutcraft.spoutcraftapi;

import java.util.Map;
import java.util.logging.Logger;

import org.spoutcraft.spoutcraftapi.addon.AddonManager;
import org.spoutcraft.spoutcraftapi.command.AddonCommand;
import org.spoutcraft.spoutcraftapi.command.CommandSender;

public abstract interface Spoutcraft {

	public String getName();

	public String getVersion();

	public AddonManager getAddonManager();

	public void reload();

	public Logger getLogger();

	public AddonCommand getAddonCommand(String name);

	public boolean dispatchCommand(CommandSender sender, String commandLine);

	public Map<String, String[]> getCommandAliases();

}
