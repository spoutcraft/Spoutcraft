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

import java.io.File;
import java.util.logging.Logger;

import org.spoutcraft.spoutcraftapi.addon.AddonManager;
import org.spoutcraft.spoutcraftapi.command.AddonCommand;
import org.spoutcraft.spoutcraftapi.command.CommandSender;
import org.spoutcraft.spoutcraftapi.entity.ActivePlayer;
import org.spoutcraft.spoutcraftapi.gui.RenderDelegate;
import org.spoutcraft.spoutcraftapi.inventory.ItemManager;
import org.spoutcraft.spoutcraftapi.keyboard.KeyBindingManager;
import org.spoutcraft.spoutcraftapi.player.BiomeManager;
import org.spoutcraft.spoutcraftapi.player.SkyManager;
import org.spoutcraft.spoutcraftapi.property.PropertyInterface;
import org.spoutcraft.spoutcraftapi.util.Location;

public interface Client extends PropertyInterface {

	public String getName();

	public SpoutVersion getVersion();

	public AddonManager getAddonManager();

	public Logger getLogger();

	public AddonCommand getAddonCommand(String name);

	public boolean dispatchCommand(CommandSender sender, String commandLine);

	public File getUpdateFolder();

	public ItemManager getItemManager();

	public SkyManager getSkyManager();
	
	public KeyBindingManager getKeyBindingManager();
	
	//public PacketManager getPacketManager();
	
	public ActivePlayer getActivePlayer();

	public BiomeManager getBiomeManager();

	public boolean isCheatMode();

	public boolean isSpoutEnabled();

	public SpoutVersion getServerVersion();

	// public EntityPlayer getPlayerFromId(int id);

	public long getTick();

	public Mode getMode();

	public RenderDelegate getRenderDelegate();

	/**
	 * The camera property holds the position and view of the camera. You can set it to a new location to influence it and to provide camera cutscenes.
	 * @return the location and view of the camera
	 */
	public Location getCamera();

	/**
	 * The camera property holds the position and view of the camera. You can set it to a new location to influence it and to provide camera cutscenes.
	 * Detaching the camera is mandatory before doing a cut scene, otherwise, the players movement will override your cutscene.
	 * @see detachCamera(boolean)
	 * @param loc the location and view of the camera
	 */
	public void setCamera(Location loc);
	
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

}
