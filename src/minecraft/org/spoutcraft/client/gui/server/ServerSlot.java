/*
 * This file is part of Spoutcraft (http://www.spout.org/).
 *
 * Spoutcraft is licensed under the SpoutDev License Version 1.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
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
