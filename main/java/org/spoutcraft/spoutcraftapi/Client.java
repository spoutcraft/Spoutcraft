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

	public String getVersion();

	public AddonManager getAddonManager();

	public void reload();

	public Logger getLogger();

	public AddonCommand getAddonCommand(String name);

	public boolean dispatchCommand(CommandSender sender, String commandLine);

	public Map<String, String[]> getCommandAliases();

	public String getUpdateFolder();

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

	public Location getCamera();

	public void setCamera(Location loc);

	public enum Mode {
		Single_Player,
		Multiplayer,
		Menu;
	}

}
