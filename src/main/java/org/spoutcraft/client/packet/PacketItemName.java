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

import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;
import org.spoutcraft.api.material.Material;
import org.spoutcraft.api.material.MaterialData;

public class PacketItemName implements SpoutPacket {
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

	public void readData(SpoutInputStream input) throws IOException {
		id = input.readInt();
		data = input.readShort();
		name = input.readString();
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeInt(id);
		output.writeShort(data);
		output.writeString(name);
	}

	public void run(int PlayerId) {
		Material material = MaterialData.getOrCreateMaterial(id, data);
		if (material == null) {
			material = MaterialData.getCustomItem(data);
		}
		if (name.equals("[resetall]")) {
			MaterialData.reset();
		} 
		if (material != null) {
			if (name.equals("[reset]")) {
				material.setName(material.getNotchianName());
			} else {
				material.setName(name);
			}
		} else {
			//System.out.println("Tried to set item name to [" + name + "] for unknown material (" + id + ", " + data + ")");
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
