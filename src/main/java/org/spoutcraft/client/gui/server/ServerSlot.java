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

public class ServerSlot implements Comparable {
	public int ID = 0;
	public int uniqueid = 0;
	public String ip = "";
	public String port = "";
	public String name = "";
	public int players = 0;
	public int maxPlayers = 0;
	public String country = "";
	public boolean pinging = false;
	public long ping = -2L;
	public String status = "";
	public String msg = "";
	public String description = "";
	public String site = "";
	public String forum = "";
	public int loaded = 0;

	public ServerSlot(int ID) {
		this.ID = ID;
	}

	public int compareTo(ServerSlot other) {
		return other.players - this.players;
	}

	public String getFullIp() {
		return ip + (port.length() > 0 ? ":" : "") + port;
	}

	// $FF: synthetic method
	// $FF: bridge method
	public int compareTo(Object other) {
		return this.compareTo((ServerSlot)other);
	}
}
