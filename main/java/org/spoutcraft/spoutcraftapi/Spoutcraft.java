/*
 * This file is part of SpoutcraftAPI (http://wiki.getspout.org/).
 * 
 * SpoutcraftAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SpoutcraftAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.spoutcraftapi;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import org.spoutcraft.spoutcraftapi.Client.Mode;
import org.spoutcraft.spoutcraftapi.addon.AddonManager;
import org.spoutcraft.spoutcraftapi.addon.AddonStore;
import org.spoutcraft.spoutcraftapi.command.AddonCommand;
import org.spoutcraft.spoutcraftapi.command.CommandSender;
import org.spoutcraft.spoutcraftapi.entity.ActivePlayer;
import org.spoutcraft.spoutcraftapi.entity.Player;
import org.spoutcraft.spoutcraftapi.gui.MinecraftFont;
import org.spoutcraft.spoutcraftapi.gui.MinecraftTessellator;
import org.spoutcraft.spoutcraftapi.gui.RenderDelegate;
import org.spoutcraft.spoutcraftapi.inventory.MaterialManager;
import org.spoutcraft.spoutcraftapi.io.AddonPacket;
import org.spoutcraft.spoutcraftapi.keyboard.KeyBindingManager;
import org.spoutcraft.spoutcraftapi.player.BiomeManager;
import org.spoutcraft.spoutcraftapi.player.SkyManager;
import org.spoutcraft.spoutcraftapi.util.Location;

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

	/**
	 * Gets the client singleton interface
	 * @return client
	 */
	public static Client getClient() {
		return client;
	}
	
	public static World getWorld() {
		return client.getWorld();
	}

	/**
	 * Gets the name with the version of this client.
	 * 
	 * Example: 'Spoutcraft_555'
	 * @return name
	 */
	public static String getName() {
		return client.getName();
	}

	/**
	 * Gets the build version of the client.
	 * @return version
	 */
	public static long getVersion() {
		return client.getVersion();
	}
	
	/**
	 * Gets the build version of Spout on the server, or -1 if none exists
	 * @return version
	 */
	public static long getServerVersion() {
		return client.getServerVersion();
	}

	/**
	 * Gets the addon manager for interfacing with addons
	 * @return
	 */
	public static AddonManager getAddonManager() {
		return client.getAddonManager();
	}

	/**
	 * Gets a logger that will print logs to the users .spoutcraft/logs folder, or terminal if it is active
	 * @return logger
	 */
	public static Logger getLogger() {
		return client.getLogger();
	}

	public static AddonCommand getAddonCommand(String name) {
		return client.getAddonCommand(name);
	}

	public static boolean dispatchCommand(CommandSender sender, String commandLine) {
		return client.dispatchCommand(sender, commandLine);
	}
	
	public static File getUpdateFolder() {
		return client.getUpdateFolder();
	}

	public static Mode getMode() {
		return client.getMode();
	}
	
	public static SkyManager getSkyManager() {
		return client.getSkyManager();
	}
	
	public static KeyBindingManager getKeyBindingManager() {
		return client.getKeyBindingManager();
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
	
	public static ActivePlayer getActivePlayer() {
		return client.getActivePlayer();
	}
	
	public static BiomeManager getBiomeManager(){
		return client.getBiomeManager();
	}
	
	public static MaterialManager getMaterialManager() {
		return client.getMaterialManager();
	}
	
	public static boolean isSpoutEnabled() {
		return client.isSpoutEnabled();
	}
	
	public static File getAddonFolder() {
		return client.getAddonFolder();
	}
	
	public static File getAudioCache() {
		return client.getAudioCache();
	}
	
	public static File getTemporaryCache() {
		return client.getTemporaryCache();
	}
	
	public static File getTextureCache() {
		return client.getTextureCache();
	}
	
	public static File getTexturePackFolder() {
		return client.getTexturePackFolder();
	}
	
	public static File getStatsFolder() {
		return client.getStatsFolder();
	}
	
	public static File getSelectedTexturePackZip() {
		return client.getSelectedTexturePackZip();
	}
	
	public static long getTick() {
		return client.getTick();
	}
	
	public static Location getCamera() {
		return client.getCamera();
	}
	
	public static void setCamera(Location loc) {
		client.setCamera(loc);
	}
	
	public static void detachCamera(boolean detach) {
		client.detachCamera(detach);
	}
	
	public static boolean isCameraDetached() {
		return client.isCameraDetached();
	}
	
	public static void send(AddonPacket packet) {
		client.send(packet);
	}
	
	/**
	 * Gets a list of all Players
	 *
	 * @return An array of Players
	 */
	public static Player[] getPlayers() {
		return client.getPlayers();
	}
	
	/**
	 * Gets a player object by the given username
	 *
	 * This method may not return objects for offline players
	 *
	 * @param name Name to look up
	 * @return Player if it was found, otherwise null
	 */
	public static Player getPlayer(String name) {
		return client.getPlayer(name);
	}

	/**
	 * Gets the player with the exact given name, case insensitive
	 *
	 * @param name Exact name of the player to retrieve
	 * @return Player object or null if not found
	 */
	public static Player getPlayerExact(String name) {
		return client.getPlayerExact(name);
	}

	/**
	 * Attempts to match any players with the given name, and returns a list
	 * of all possibly matches
	 *
	 * This list is not sorted in any particular order. If an exact match is found,
	 * the returned list will only contain a single result.
	 *
	 * @param name Name to match
	 * @return List of all possible players
	 */
	public static List<Player> matchPlayer(String name) {
		return client.matchPlayer(name);
	}
	
	public static AddonStore getAddonStore() {
		return client.getAddonStore();
	}
}
