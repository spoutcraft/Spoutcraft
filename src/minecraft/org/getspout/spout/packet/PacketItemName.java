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

import org.spoutcraft.spoutcraftapi.material.Material;
import org.spoutcraft.spoutcraftapi.material.MaterialData;
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
		Material material = MaterialData.getOrCreateMaterial(id, data);
		if (material == null) {
			System.out.println("Failed to create material for : " + id + ", " + data);
		}
		if (name.equals("[resetall]")) {
			MaterialData.reset();
		}
		else if (name.equals("[reset]")) {
			material.setName(material.getNotchianName());
		}
		else {
			
			material.setName(name);
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
