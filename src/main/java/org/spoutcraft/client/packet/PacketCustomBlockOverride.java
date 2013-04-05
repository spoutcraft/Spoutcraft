/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;
import org.spoutcraft.client.SpoutClient;

public class PacketCustomBlockOverride implements SpoutPacket {
	private int x;
	private short y;
	private int z;
	private short blockId;
	private byte data;

	public PacketCustomBlockOverride() {
	}

	public PacketCustomBlockOverride(int x, int y, int z, Integer blockId, Byte data) {
		this.x = x;
		this.y = (short) (y & 0xFFFF);
		this.z = z;
		setBlockId(blockId);
		setBlockData(data);
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

	private void setBlockData(Byte data) {
		if (data == null) {
			this.data = -1;
		} else {
			this.data = data.byteValue();
		}
	}

	protected Byte getBlockDatas() {
		return data == -1 ? null : Byte.valueOf(data);
	}

	public void readData(SpoutInputStream input) throws IOException {
		x = input.readInt();
		y = input.readShort();
		z = input.readInt();
		setBlockId((int)input.readShort());
		setBlockData((byte) input.read());
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeInt(x);
		output.writeShort(y);
		output.writeInt(z);
		output.writeShort(blockId);
		output.write(data);
	}

	public void run(int PlayerId) {
		Spoutcraft.getChunkAt(SpoutClient.getInstance().getRawWorld(), x, y, z).setCustomBlockId(x, y, z, blockId);
		Spoutcraft.getChunkAt(SpoutClient.getInstance().getRawWorld(), x, y, z).setCustomBlockData(x, y, z, data);
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
