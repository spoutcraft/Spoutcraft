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
import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.commons.io.output.ByteArrayOutputStream;

import org.spoutcraft.api.io.AddonPacket;
import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;
import org.spoutcraft.client.SpoutClient;

public class PacketAddonData implements CompressablePacket {
	private AddonPacket packet = null;
	private boolean needsCompression;
	private boolean compressed = false;
	private byte[] data;
	public PacketAddonData() {
	}

	public PacketAddonData(AddonPacket packet) {
		this.packet = packet;
		SpoutOutputStream stream = new SpoutOutputStream();

		boolean sandboxed = SpoutClient.isSandboxed();
		SpoutClient.enableSandbox();
		try {
			packet.write(stream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!sandboxed) {
			SpoutClient.disableSandbox();
		}
		ByteBuffer buffer = stream.getRawBuffer();
		data = new byte[buffer.capacity() - buffer.remaining()];
		System.arraycopy(buffer.array(), 0, data, 0, data.length);
		needsCompression = data.length > 512;
	}

	@SuppressWarnings("unchecked")
	public void readData(SpoutInputStream input) throws IOException {
		String packetName = input.readString();

		boolean sandboxed = SpoutClient.isSandboxed();
		SpoutClient.enableSandbox();

		try {
			Class<? extends AddonPacket> packetClass = AddonPacket.getPacketFromId(packetName);
			Constructor<? extends AddonPacket> constructor = null;
			Constructor<? extends AddonPacket>[] constructors = (Constructor<? extends AddonPacket>[]) packetClass.getConstructors();
			for (Constructor<? extends AddonPacket> c : constructors) {
				if (c.getGenericParameterTypes().length == 0) {
					constructor = c;
					break;
				}
			}
			packet = constructor.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!sandboxed) {
			SpoutClient.disableSandbox();
		}

		int size = input.readInt();
		compressed = input.readBoolean();
		data = new byte[size];
		input.read(data);
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeString(AddonPacket.getPacketId(packet.getClass()));
		output.writeInt(data.length);
		output.writeBoolean(compressed);
		output.write(data);
	}

	public void run(int playerId) {
		if (packet != null) {
			SpoutInputStream stream = new SpoutInputStream(ByteBuffer.wrap(data));

			boolean sandboxed = SpoutClient.isSandboxed();
			SpoutClient.enableSandbox();
			try {
				packet.read(stream);
				packet.run();
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (!sandboxed) {
				SpoutClient.disableSandbox();
			}
		}
	}

	public void failure(int playerId) {
	}

	public PacketType getPacketType() {
		return PacketType.PacketAddonData;
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
			compressed = false;
		}
	}

	public boolean isCompressed() {
		return !needsCompression || compressed;
	}
}
