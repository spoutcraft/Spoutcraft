package org.spoutcraft.spoutcraftapi;

import java.util.Map;
import java.util.logging.Logger;

import org.spoutcraft.spoutcraftapi.addon.AddonManager;
import org.spoutcraft.spoutcraftapi.command.AddonCommand;
import org.spoutcraft.spoutcraftapi.command.CommandSender;

public final class Spoutcraft {

	public String getName() {
		return null;
	}

	public String getVersion() {
		return null;
	}

	public AddonManager getAddonManager(){
		return null;
	}

	public void reload(){
		
	}

	public Logger getLogger(){
		return null;
	}

	public AddonCommand getAddonCommand(String name){
		return null;
	}

	public boolean dispatchCommand(CommandSender sender, String commandLine){
		return false;
	}

	public Map<String, String[]> getCommandAliases(){
		return null;
	}

}
