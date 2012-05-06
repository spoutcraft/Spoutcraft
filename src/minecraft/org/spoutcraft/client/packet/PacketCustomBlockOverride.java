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

import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.io.SpoutInputStream;
import org.spoutcraft.spoutcraftapi.io.SpoutOutputStream;

public class PacketCustomBlockOverride implements SpoutPacket {
	private int x;
	private short y;
	private int z;
	private short blockId;
	private byte rotation;

	public PacketCustomBlockOverride() {

	}

	public PacketCustomBlockOverride(int x, int y, int z, Integer blockId, Byte rotation) {
		this.x = x;
		this.y = (short) (y & 0xFFFF);
		this.z = z;
		setBlockId(blockId);
		setBlockRotation(rotation);
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

	private void setBlockRotation(Byte rotation) {
		if (rotation == null) {
			this.rotation = -1;
		} else {
			this.rotation = rotation.byteValue();
		}
	}

	protected Byte getBlockRotation() {
		return rotation == -1 ? null : Byte.valueOf(rotation);
	}

	public void readData(SpoutInputStream input) throws IOException {
		x = input.readInt();
		y = input.readShort();
		z = input.readInt();
		setBlockId((int)input.readShort());
		setBlockRotation((byte) input.read());
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeInt(x);
		output.writeShort(y);
		output.writeInt(z);
		output.writeShort(blockId);
		output.write(rotation);
	}

	public void run(int PlayerId) {
		Spoutcraft.getWorld().getChunkAt(x, y, z).setCustomBlockId(x, y, z, blockId);
		Spoutcraft.getWorld().getChunkAt(x, y, z).setCustomBlockRotation(x, y, z, rotation);
	}

	public PacketType getPacketType() {
		return PacketType.PacketCustomBlockOverride;
	}

	public int getVersion() {
		return 3;
	}

	public void failure(int playerId) {
	}
}
