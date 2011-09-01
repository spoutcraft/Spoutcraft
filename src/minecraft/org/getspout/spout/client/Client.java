/*
 * This file is part of Spoutcraft (http://wiki.getspout.org/).
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
package org.getspout.spout.client;

import net.minecraft.src.EntityPlayer;

import org.getspout.spout.SpoutVersion;
import org.getspout.spout.entity.EntityManager;
import org.getspout.spout.packet.PacketManager;
import org.getspout.spout.player.ActivePlayer;
import org.getspout.spout.player.BiomeManager;
import org.getspout.spout.player.SkyManager;
import org.spoutcraft.spoutcraftapi.inventory.ItemManager;

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
