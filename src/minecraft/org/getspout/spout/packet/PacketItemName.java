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
import org.spoutcraft.spoutcraftapi.packet.PacketUtil;

public class PacketItemName implements SpoutPacket{
	private int id;
	private short data;
	private String name;
	public PacketItemName() {
		
	}
	
	public PacketItemName(int id, short data, String name) {
		this.id = id;
		this.data = data;
		this.name = name;
	}

	public int getNumBytes() {
		return 6 + PacketUtil.getNumBytes(name);
	}

	public void readData(DataInputStream input) throws IOException {
		id = input.readInt();
		data = input.readShort();
		name = PacketUtil.readString(input);
	}

	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(id);
		output.writeShort(data);
		PacketUtil.writeString(output, name);
	}

	public void run(int PlayerId) {
		if (name != null) {
			if (name.equals("[resetall]")) {
				SpoutClient.getInstance().getItemManager().reset();
			}
			else if (name.equals("[reset]")) {
				SpoutClient.getInstance().getItemManager().resetName(id, data);
			}
			else {
				SpoutClient.getInstance().getItemManager().setItemName(id, data, name);
			}
		}
	}

	public PacketType getPacketType() {
		return PacketType.PacketItemName;
	}

	public int getVersion() {
		return 0;
	}

	public void failure(int playerId) {
		
	}
}
