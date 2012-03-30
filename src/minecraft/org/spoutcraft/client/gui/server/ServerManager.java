/*
 * This file is part of Spoutcraft (http://www.spout.org/).
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
package org.spoutcraft.client.gui.server;

import net.minecraft.src.GuiConnecting;
import net.minecraft.src.GuiScreen;

import org.spoutcraft.client.SpoutClient;

public class ServerManager {
	private FavoritesModel favorites;
	private ServerListModel serverList;
	private LANModel lanModel;
	private GuiScreen joinedFrom;
	private String screenName;

	public void init() {
		favorites = new FavoritesModel();
		favorites.load();
		serverList = new ServerListModel();
		lanModel = new LANModel();
	}

	public FavoritesModel getFavorites() {
		return favorites;
	}

	public ServerListModel getServerList() {
		return serverList;
	}
	
	public LANModel getLANModel() {
		return lanModel;
	}

	/**
	 * Joins the server given by the serverItem
	 * @param serverItem
	 */
	public void join(ServerItem serverItem, GuiScreen from, String name) {
		join(serverItem.getIp(), serverItem.getPort(), from, name);
	}

	/**
	 * Joins the server on the given ip:port
	 * @param ip
	 * @param port
	 */
	public void join(String ip, int port, GuiScreen from, String name) {
		this.joinedFrom = from;
		this.screenName = name;
		SpoutClient.getHandle().displayGuiScreen(new GuiConnecting(SpoutClient.getHandle(), ip, port));
	}

	public GuiScreen getJoinedFrom() {
		return joinedFrom;
	}

	public String getJoinedFromName() {
		return screenName;
	}
}
