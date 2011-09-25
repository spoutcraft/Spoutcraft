/*
 * This file is part of Spoutcraft API (http://wiki.getspout.org/).
 * 
 * Spoutcraft API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.spoutcraftapi;

import java.util.Map;
import java.util.logging.Logger;

import org.spoutcraft.spoutcraftapi.Client.Mode;
import org.spoutcraft.spoutcraftapi.addon.AddonManager;
import org.spoutcraft.spoutcraftapi.command.AddonCommand;
import org.spoutcraft.spoutcraftapi.command.CommandSender;
import org.spoutcraft.spoutcraftapi.gui.MinecraftFont;
import org.spoutcraft.spoutcraftapi.gui.MinecraftTessellator;
import org.spoutcraft.spoutcraftapi.gui.RenderDelegate;

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

	public static Mode getMode() {
		return client.getMode();
	}

	public static RenderDelegate getRenderDelegate() {
		return client.getRenderDelegate();
	}

	public static MinecraftFont getMinecraftFont() {
		return client.getRenderDelegate().getMinecraftFont();
	}

	public static MinecraftTessellator getTessellator() {
		return client.getRenderDelegate().getTessellator();
	}
}
