/*
 * This file is part of SpoutAPI (http://wiki.getspout.org/).
 * 
 * SpoutAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SpoutAPI is distributed in the hope that it will be useful,
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
import java.nio.ByteBuffer;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.spoutcraft.spoutcraftapi.Spoutcraft;

public class PacketCustomBlockChunkOverride implements CompressablePacket{
	private int chunkX;
	private int chunkZ;
	private byte[] data;
	private boolean compressed = true;
	
	public PacketCustomBlockChunkOverride() {
		
	}
	
	public PacketCustomBlockChunkOverride(short[] customIds, int x, int z) {
		chunkX = x;
		chunkZ = z;
		ByteBuffer buffer = ByteBuffer.allocate(customIds.length * 2);
		for (int i = 0; i < customIds.length; i++) {
			buffer.putShort(customIds[i]);
		}
		data = buffer.array();
	}

	public int getNumBytes() {
		return 12 + data.length;
	}

	public void readData(DataInputStream input) throws IOException {
		chunkX = input.readInt();
		chunkZ = input.readInt();
		int size = input.readInt();
		data = new byte[size];
		input.readFully(data);
	}

	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(chunkX);
		output.writeInt(chunkZ);
		output.writeInt(data.length);
		output.write(data);
	}

	public void run(int playerId) {
		ByteBuffer buffer = ByteBuffer.allocate(data.length);
		buffer.put(data);
		short[] customIds = new short[16*16*Spoutcraft.getWorld().getMaxHeight()];
		for (int i = 0; i < customIds.length; i++) {
			customIds[i] = buffer.getShort(i * 2);
		}
		Spoutcraft.getWorld().getChunkAt(chunkX, chunkZ).setCustomBlockIds(customIds);
	}

	public void failure(int playerId) {
		
	}

	public PacketType getPacketType() {
		return PacketType.PacketCustomBlockChunkOverride;
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
				while(!deflater.finished())
				{
					int bytesCompressed = deflater.deflate(buffer);
					bos.write(buffer, 0, bytesCompressed);
				}
				try {
					bos.close();
				}
				catch (IOException e) {
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
				}
				catch (DataFormatException e) {
					
				}
			}
			try {
				bos.close();
			}
			catch (IOException e) {
				
			}

			data = bos.toByteArray();
		}
	}

	public boolean isCompressed() {
		return compressed;
	}

}
