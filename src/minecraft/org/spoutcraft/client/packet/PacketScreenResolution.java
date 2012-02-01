/*
 * This file is part of SpoutAPI (http://wiki.getspout.org/).
 * 
 * SpoutAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SpoutAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.client.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class PacketScreenResolution implements SpoutPacket{
	private int x, y;
	
	public PacketScreenResolution() {
		
	}
	
	public PacketScreenResolution(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getNumBytes() {
		return 8;
	}

	public void readData(DataInputStream input) throws IOException {
		x = input.readInt();
		y = input.readInt();
	}

	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(x);
		output.writeInt(y);
	}

	public void run(int playerId) {

	}

	public void failure(int playerId) {
	}

	public PacketType getPacketType() {
		return PacketType.PacketScreenResolution;
	}

	public int getVersion() {
		return 0;
	}
}
