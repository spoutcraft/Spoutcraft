/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 SpoutcraftDev <http://spoutcraft.org/>
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
package org.spoutcraft.client.packet.builtin;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.commons.io.output.ByteArrayOutputStream;

import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;
import org.spoutcraft.api.material.Block;
import org.spoutcraft.api.material.MaterialData;
import org.spoutcraft.client.packet.CompressiblePacket;
import org.spoutcraft.client.packet.PacketType;

public class PacketBlockData extends CompressiblePacket {
	byte[] data;
	boolean compressed = false;
	public PacketBlockData() {
	}

	@Override
	public void compress() {
		if (!compressed) {
			Deflater deflater = new Deflater();
			deflater.setInput(data);
			deflater.setLevel(Deflater.BEST_COMPRESSION);
			deflater.finish();
			ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
			byte[] buffer = new byte[1024];
			while (!deflater.finished()) {
				int bytesCompressed = deflater.deflate(buffer);
				bos.write(buffer, 0, bytesCompressed);
			}
			try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			data = bos.toByteArray();
			compressed = true;
		}
	}

	@Override
	public void decompress() {
		if (compressed) {
			Inflater decompressor = new Inflater();
			decompressor.setInput(data);

			ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);

			byte[] buf = new byte[1024];
			while (!decompressor.finished()) {
				try {
					int count = decompressor.inflate(buf);
					bos.write(buf, 0, count);
				} catch (DataFormatException e) {
				}
			}
			try {
				bos.close();
			} catch (IOException e) {
			}

			data = bos.toByteArray();
		}
	}

	@Override
	public boolean isCompressed() {
		return compressed;
	}

	@Override
	public void readData(SpoutInputStream input) throws IOException {
		int size = input.readInt();
		compressed = input.readBoolean();
		if (size > 0) {
			data = new byte[size];
			input.read(data);
		}
	}

	@Override
	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeInt(data == null ? 0 : data.length);
		output.writeBoolean(compressed);
		if (data != null) {
			output.write(data);
		}
	}

	@Override
	public void run(int playerId) {
		if (data != null) {
			ByteBuffer result = ByteBuffer.allocate(data.length).put(data);
			for (int i = 0; i < data.length / 15; i++) {
				int index = i * 15;
				int id = result.get(index);
				short rawData = result.get(index+1);
				Block block = MaterialData.getBlock(id, rawData);
				if (block != null) {
					block.setHardness(result.getFloat(index+2));
					block.setLightLevel(result.getInt(index+6));
					block.setFriction(result.getFloat(index+10));
					block.setOpaque(result.get(index+14) != 0);
				}
			}
		}
	}

	@Override
	public void failure(int playerId) {
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketBlockData;
	}

	@Override
	public int getVersion() {
		return 1;
	}
}
