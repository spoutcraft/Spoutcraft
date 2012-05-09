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
import java.nio.ByteBuffer;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.io.SpoutInputStream;
import org.spoutcraft.spoutcraftapi.io.SpoutOutputStream;

public class PacketCustomBlockChunkOverride implements CompressablePacket{
	private int chunkX;
	private int chunkZ;
	private boolean hasData = false;
	private byte[] data;
	private boolean compressed = true;

	public PacketCustomBlockChunkOverride() {

	}

	public PacketCustomBlockChunkOverride(int x, int z) {
		chunkX = x;
		chunkZ = z;
		compressed = false;
	}

	public void readData(SpoutInputStream input) throws IOException {
		chunkX = input.readInt();
		chunkZ = input.readInt();
		hasData = input.readBoolean();
		if (hasData) {
			int size = input.readInt();
			data = new byte[size];
			input.read(data);
		}
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeInt(chunkX);
		output.writeInt(chunkZ);
		output.writeBoolean(hasData);
		if (hasData) {
			output.writeInt(data.length);
			output.write(data);
		}
	}

	public void run(int playerId) {
		if (hasData) {
			ByteBuffer buffer = ByteBuffer.allocate(data.length);
			buffer.put(data);
			short[] customIds = new short[16*16*Spoutcraft.getWorld().getMaxHeight()];
			byte[] customRots = new byte[16*16*Spoutcraft.getWorld().getMaxHeight()];
			for (int i = 0; i < customIds.length; i++) {
				customIds[i] = buffer.getShort(i * 3);
				customRots[i] = buffer.get((i * 3) + 2);
			}
			Spoutcraft.getWorld().getChunkAt(chunkX, chunkZ).setCustomBlockIds(customIds);
			Spoutcraft.getWorld().getChunkAt(chunkX, chunkZ).setCustomBlockRotations(customRots);
		}
	}

	public void failure(int playerId) {

	}

	public PacketType getPacketType() {
		return PacketType.PacketCustomBlockChunkOverride;
	}

	public int getVersion() {
		return 2;
	}

	public void compress() {
		if (!compressed && hasData) {
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
		if (compressed && hasData) {
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
