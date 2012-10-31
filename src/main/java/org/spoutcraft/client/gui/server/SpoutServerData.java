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
package org.spoutcraft.client.gui.server;

import net.minecraft.src.ServerData;

import org.spoutcraft.client.SpoutClient;

/**
 * Extends ServerData to store additional spout server details.
 */
public class SpoutServerData extends ServerData {
	private ServerItem serverItem;

	/**
	 * Construct more closely matches Minecraft ServerData
	 * @param name - Server Name
	 * @param ip - Server ip and port in ip:port format
	 */
	public SpoutServerData(String name, String ip) {
		super(name, ip);
	}

	/**
	 * Construct more closely matches Spoutcraft data from a ServerItem
	 * @param name - Server Name
	 * @param ip - Ip Address
	 * @param port - port
	 * @param uid - databaseId
	 */
	public SpoutServerData(String name, ServerItem serverItem) {
		this(name, serverItem.getIp() + ":" + String.valueOf(serverItem.getPort()));
		setServerItem(serverItem);
		if (serverItem.getAcceptsTextures() != null) setAcceptsTextures(serverItem.getAcceptsTextures());
	}

	@Override
	public void setAcceptsTextures(boolean accepts) {
		this.getServerItem().setAcceptsTextures(accepts);
		super.setAcceptsTextures(accepts);
	}

	/**
	 * Sets the serveritem entry for this server
	 * @param uid
	 */
	public void setServerItem(ServerItem serverItem) {
		this.serverItem = serverItem;
	}

	/**
	 * Gets the serveritem entry for this server
	 * @return
	 */
	public ServerItem getServerItem() {
		return serverItem;
	}
}
