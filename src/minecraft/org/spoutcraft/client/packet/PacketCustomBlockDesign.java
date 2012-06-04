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

import org.spoutcraft.spoutcraftapi.block.design.GenericBlockDesign;
import org.spoutcraft.spoutcraftapi.io.SpoutInputStream;
import org.spoutcraft.spoutcraftapi.io.SpoutOutputStream;
import org.spoutcraft.spoutcraftapi.material.CustomBlock;
import org.spoutcraft.spoutcraftapi.material.MaterialData;

public class PacketCustomBlockDesign implements SpoutPacket {
	private short customId;
	private byte data;
	private GenericBlockDesign design;

	public PacketCustomBlockDesign() {
	}

	public void readData(SpoutInputStream input) throws IOException {
		customId = input.readShort();
		data = (byte) input.read();
		design = new GenericBlockDesign();
		design.read(input);
		if (design.isReset()) {
			design = null;
		}
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeShort(customId);
	}

	public void run(int id) {
		CustomBlock block = MaterialData.getCustomBlock(customId);
		if (block != null) {
			block.setBlockDesign(design, data);
		}
	}

	public void failure(int id) {
	}

	public PacketType getPacketType() {
		return PacketType.PacketCustomBlockDesign;
	}

	public int getVersion() {
		return new GenericBlockDesign().getVersion() + 3;
	}
}
