package org.spoutcraft.spoutcraftapi;

import java.util.Map;
import java.util.logging.Logger;

import org.spoutcraft.spoutcraftapi.Client.Mode;
import org.spoutcraft.spoutcraftapi.addon.AddonManager;
import org.spoutcraft.spoutcraftapi.command.AddonCommand;
import org.spoutcraft.spoutcraftapi.command.CommandSender;
import org.spoutcraft.spoutcraftapi.gl.SafeGL;
import org.spoutcraft.spoutcraftapi.keyboard.KeyBindingManager;

public final class Spoutcraft {
	
	private static Client client = null;
	
	private Spoutcraft() {
	}
	
	public static void setClient(Client argClient) {
		if (client != null) {
			throw new UnsupportedOperationException("Cannot redefine singleton Client");
		}
		client = argClient;
	}
	
	public static Client getClient() {
		return client;
	}
	
	public static String getName() {
		return client.getName();
	}

	public static String getVersion() {
		return client.getVersion();
	}

	public static AddonManager getAddonManager() {
		return client.getAddonManager();
	}

	public static void reload() {
		client.reload();
	}

	public static Logger getLogger() {
		return client.getLogger();
	}

	public static AddonCommand getAddonCommand(String name) {
		return client.getAddonCommand(name);
	}

	public static boolean dispatchCommand(CommandSender sender, String commandLine) {
		return client.dispatchCommand(sender, commandLine);
	}

	public static Map<String, String[]> getCommandAliases() {
		return client.getCommandAliases();
	}

	public static String getUpdateFolder() {
		return client.getUpdateFolder();
	}
	
	public static SafeGL getGLWrapper() {
		return client.getGLWrapper();
	}
	
	public static Mode getMode() {
		return client.getMode();
	}
	
	public static KeyBindingManager getKeyBindingManager() {
		return client.getKeyBindingManager();
	}
}
