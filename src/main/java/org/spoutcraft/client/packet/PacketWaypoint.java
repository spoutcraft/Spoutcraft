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
package org.spoutcraft.client.packet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;
import org.spoutcraft.client.gui.minimap.MinimapConfig;
import org.spoutcraft.client.gui.minimap.Waypoint;

public class PacketWaypoint implements SpoutPacket {
	private double x, y, z;
	private String name;
	private boolean death = false;

	public PacketWaypoint() { }

	public void readData(SpoutInputStream input) throws IOException { 
		x = input.readDouble();
		y = input.readDouble();
		z = input.readDouble();
		name = input.readString();
		death = input.readBoolean();
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeDouble(x);
		output.writeDouble(y);
		output.writeDouble(z);
		output.writeString(name);
		output.writeBoolean(death);
	}

	public void run(int playerId) {
		if (!death) {
			MinimapConfig.getInstance().addServerWaypoint(x, y, z, name);
		} else {
			Waypoint point = new Waypoint("Death " + new SimpleDateFormat("dd-MM-yyyy").format(new Date()), (int)x, (int)y, (int)z, true);
			point.deathpoint = true;
			MinimapConfig.getInstance().addWaypoint(point);
		}
	}

	public void failure(int playerId) {
	}

	public PacketType getPacketType() {
		return PacketType.PacketWaypoint;
	}

	public int getVersion() {
		return 0;
	}
}
