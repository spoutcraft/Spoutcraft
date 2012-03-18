/*
 * This file is part of Spoutcraft (http://www.spout.org/).
 *
 * Spoutcraft is licensed under the SpoutDev License Version 1.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spoutcraft.client.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.spoutcraft.spoutcraftapi.Spoutcraft;

public class PacketCustomBlockOverride implements SpoutPacket {
	private int x;
	private short y;
	private int z;
	private short blockId;

	public PacketCustomBlockOverride() {
	}

	public PacketCustomBlockOverride(int x, int y, int z, Integer blockId) {
		this.x = x;
		this.y = (short) (y & 0xFFFF);
		this.z = z;
		setBlockId(blockId);
	}

	private void setBlockId(Integer blockId) {
		if (blockId == null) {
			this.blockId = -1;
		} else {
			this.blockId = blockId.shortValue();
		}
	}

	protected Integer getBlockId() {
		return blockId == -1 ? null : Integer.valueOf(blockId);
	}

	public int getNumBytes() {
		return 4 + 4 + 2 + 4;
	}

	public void readData(DataInputStream input) throws IOException {
		x = input.readInt();
		y = input.readShort();
		z = input.readInt();
		setBlockId((int) input.readShort());
	}

	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(x);
		output.writeShort(y);
		output.writeInt(z);
		output.writeShort(blockId);
	}

	public void run(int PlayerId) {
		Spoutcraft.getWorld().getChunkAt(x, y, z).setCustomBlockId(x, y, z, blockId);
	}

	public PacketType getPacketType() {
		return PacketType.PacketCustomBlockOverride;
	}

	public int getVersion() {
		return 2;
	}

	public void failure(int playerId) {
	}
}
