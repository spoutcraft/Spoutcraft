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
package org.spoutcraft.client.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketChunkRefresh implements SpoutPacket {

	private int cx;
	private int cz;
	
	public PacketChunkRefresh() {
	}

	public PacketChunkRefresh(int cx, int cz) {
		this.cx = cx;
		this.cz = cz;
	}

	public int getNumBytes() {
		return 8;
	}

	public void readData(DataInputStream input) throws IOException {
		this.cx = input.readInt();
		this.cz = input.readInt();
	}

	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(this.cx);
		output.writeInt(this.cz);
	}

	public void run(int id) {
	}

	public PacketType getPacketType() {
		return PacketType.PacketChunkRefresh;
	}

	public int getVersion() {
		return 0;
	}

	public void failure(int playerId) {
		
	}
}
