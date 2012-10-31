/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Spoutcraft is licensed under the GNU Lesser General Public License.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.api;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import org.spoutcraft.api.addon.AddonManager;
import org.spoutcraft.api.addon.AddonStore;
import org.spoutcraft.api.command.AddonCommand;
import org.spoutcraft.api.command.CommandSender;
import org.spoutcraft.api.entity.ActivePlayer;
import org.spoutcraft.api.entity.CameraEntity;
import org.spoutcraft.api.entity.Player;
import org.spoutcraft.api.gui.RenderDelegate;
import org.spoutcraft.api.gui.WidgetManager;
import org.spoutcraft.api.inventory.MaterialManager;
import org.spoutcraft.api.io.AddonPacket;
import org.spoutcraft.api.keyboard.KeyBindingManager;
import org.spoutcraft.api.player.BiomeManager;
import org.spoutcraft.api.player.ChatManager;
import org.spoutcraft.api.player.SkyManager;
import org.spoutcraft.api.property.PropertyInterface;
import org.spoutcraft.api.util.FixedLocation;

public interface Client extends PropertyInterface {
	public String getName();

	public String getVersion();

	public World getWorld();

	public AddonManager getAddonManager();

	public Logger getLogger();

	public AddonCommand getAddonCommand(String name);

	public boolean dispatchCommand(CommandSender sender, String commandLine);

	public File getUpdateFolder();

	public SkyManager getSkyManager();

	public KeyBindingManager getKeyBindingManager();

	public BiomeManager getBiomeManager();

	public MaterialManager getMaterialManager();

	public boolean isSpoutEnabled();

	public long getServerVersion();

	public File getAddonFolder();

	public File getAudioCache();

	public File getTemporaryCache();

	public File getTextureCache();

	public File getTexturePackFolder();

	public File getSelectedTexturePackZip();

	public File getStatsFolder();

	public long getTick();

	public Mode getMode();

	public RenderDelegate getRenderDelegate();

	public void send(AddonPacket packet);

	public ActivePlayer getActivePlayer();

	public AddonStore getAddonStore();

	/**
	 * Gets a list of all Players
	 *
	 * @return An array of Players
	 */
	public Player[] getPlayers();

	/**
	 * Gets a player object by the given username
	 *
	 * This method may not return objects for offline players
	 *
	 * @param name Name to look up
	 * @return Player if it was found, otherwise null
	 */
	public Player getPlayer(String name);

	/**
	 * Gets the player with the exact given name, case insensitive
	 *
	 * @param name Exact name of the player to retrieve
	 * @return Player object or null if not found
	 */
	public Player getPlayerExact(String name);

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
	public List<Player> matchPlayer(String name);

	/**
	 * The camera property holds the position and view of the camera. You can set it to a new location to influence it and to provide camera cutscenes.
	 * @return the location and view of the camera
	 */
	public CameraEntity getCamera();

	/**
	 * The camera property holds the position and view of the camera. You can set it to a new location to influence it and to provide camera cutscenes.
	 * Detaching the camera is mandatory before doing a cut scene, otherwise, the players movement will override your cutscene.
	 * @see detachCamera(boolean)
	 * @param loc the location and view of the camera
	 */
	public void setCamera(FixedLocation loc);

	/**
	 * The detach property decides if player movements will influence the camera or not. If the camera is detached, player movements will be ignored.
	 * @param detach if the camera should be detached
	 */
	public void detachCamera(boolean detach);

	/**
	 * The detach property decides if player movements will influence the camera or not. If the camera is detached, player movements will be ignored.
	 * @return if the camera is detached
	 */
	public boolean isCameraDetached();

	public enum Mode {
		Single_Player,
		Multiplayer,
		Menu;
	}

	public WidgetManager getWidgetManager();

	public boolean hasPermission(String permission);
}
