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

public class PacketPluginReload implements SpoutPacket{
	public int activeInventoryX;
	public int activeInventoryY;
	public int activeInventoryZ;
	public String worldName;
	
	public PacketPluginReload() {
		
	}

	@Override
	public int getNumBytes() {
		return 12 + PacketUtil.getNumBytes(worldName);
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		activeInventoryX = input.readInt();
		activeInventoryY = input.readInt();
		activeInventoryZ = input.readInt();
		worldName = PacketUtil.readString(input, 64);
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(activeInventoryX);
		output.writeInt(activeInventoryY);
		output.writeInt(activeInventoryZ);
		PacketUtil.writeString(output, worldName);
	}

	@Override
	public void run(int playerId) {
		SpoutClient.setReloadPacket(this);
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketPluginReload;
	}
	
	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public void failure(int playerId) {
		
	}

}
