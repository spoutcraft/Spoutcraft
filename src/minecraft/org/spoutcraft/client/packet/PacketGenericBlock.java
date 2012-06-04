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

import org.spoutcraft.spoutcraftapi.inventory.ItemStack;
import org.spoutcraft.spoutcraftapi.io.SpoutInputStream;
import org.spoutcraft.spoutcraftapi.io.SpoutOutputStream;
import org.spoutcraft.spoutcraftapi.material.block.GenericCustomBlock;

public class PacketGenericBlock implements SpoutPacket {
	GenericCustomBlock block = new GenericCustomBlock();
	public PacketGenericBlock() {
	}

	public void readData(SpoutInputStream input) throws IOException {
		block.readData(input);
	}

	public void writeData(SpoutOutputStream output) throws IOException {
	}

	public void run(int playerId) {
		block.setItemDrop(new ItemStack(block, 1));
	}

	public void failure(int playerId) {
	}

	public PacketType getPacketType() {
		return PacketType.PacketGenericBlock;
	}

	public int getVersion() {
		return block.getVersion();
	}
}
