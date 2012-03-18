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
import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.commons.io.output.ByteArrayOutputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.src.WorldClient;

import org.spoutcraft.spoutcraftapi.entity.Entity;

import org.spoutcraft.client.SpoutClient;

public class PacketEntityInformation implements CompressablePacket {
	private boolean compressed = false;
	private byte[] data = null;

	public PacketEntityInformation() {

	}

	public PacketEntityInformation(List<Entity> entities) {
		ByteBuffer tempbuffer = ByteBuffer.allocate(entities.size() * 4);
		for (Entity e : entities) {
			tempbuffer.putInt(e.getEntityId());
		}
		data = tempbuffer.array();
	}

	public int getNumBytes() {
		return 1 + (data != null ? data.length : 0) + 4;
	}

	public void readData(DataInputStream input) throws IOException {
		int size = input.readInt();
		if (size > 0) {
			data = new byte[size];
			input.readFully(data);
		}
		compressed = input.readBoolean();
	}

	public void writeData(DataOutputStream output) throws IOException {
		if (data != null) {
			output.writeInt(data.length);
			output.write(data);
		} else {
			output.writeInt(0);
		}
		output.writeBoolean(compressed);
	}

	public void run(int playerId) {
		if (Minecraft.theMinecraft.theWorld instanceof WorldClient) {
			ByteBuffer rawData = ByteBuffer.allocate(data.length);
			rawData.put(data);
			for (int i = 0; i < data.length / 20; i++) {
				int index = i * 20;
				long lsb = rawData.getLong(index);
				long msb = rawData.getLong(index + 8);
				int id = rawData.getInt(index + 16);

				net.minecraft.src.Entity e = SpoutClient.getInstance().getEntityFromId(id);
				if (e != null) {
					e.uniqueId = new UUID(msb, lsb);
				}
			}
		}
	}

	public void failure(int playerId) {

	}

	public PacketType getPacketType() {
		return PacketType.PacketEntityInformation;
	}

	public int getVersion() {
		return 0;
	}

	public void compress() {
		if (!compressed) {
			if (data != null) {
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
			}
			compressed = true;
		}
	}

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

	public boolean isCompressed() {
		return compressed;
	}
}
