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
package org.spoutcraft.client.packet;

import java.io.IOException;

import org.spoutcraft.client.gui.minimap.MinimapConfig;
import org.spoutcraft.spoutcraftapi.io.SpoutInputStream;
import org.spoutcraft.spoutcraftapi.io.SpoutOutputStream;

public class PacketWaypoint implements SpoutPacket{
	private double x, y, z;
	private String name;
	
	public PacketWaypoint() { }
	
	public PacketWaypoint(double x, double y, double z, String name) { 
		this.x = x;
		this.y = y;
		this.z = z;
		this.name = name;
	}

	public void readData(SpoutInputStream input) throws IOException { 
		x = input.readDouble();
		y = input.readDouble();
		z = input.readDouble();
		name = input.readString();
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeDouble(x);
		output.writeDouble(y);
		output.writeDouble(z);
		output.writeString(name);
	}

	public void run(int playerId) {
		MinimapConfig.getInstance().addServerWaypoint(x, y, z, name);
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
