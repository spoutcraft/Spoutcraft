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
package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.getspout.spout.client.SpoutClient;

public class PacketWorldSeed implements SpoutPacket{
	public long newSeed;
	
	public PacketWorldSeed() {
	}
	
	public PacketWorldSeed(long newSeed) {
		this.newSeed = newSeed;
	}

	public int getNumBytes() {
		return 8;
	}

	public void readData(DataInputStream input) throws IOException {
		this.newSeed = input.readLong();
	}

	public void writeData(DataOutputStream output) throws IOException {
		output.writeLong(this.newSeed);
	}

	public void run(int id) {
		SpoutClient.getHandle().theWorld.getWorldInfo().setNewSeed(newSeed);
	}

	public PacketType getPacketType() {
		return PacketType.PacketWorldSeed;
	}

	public int getVersion() {
		return 0;
	}

	public void failure(int playerId) {
		
	}
}
