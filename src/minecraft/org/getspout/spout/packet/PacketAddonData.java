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
import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.getspout.spout.client.SpoutClient;
import org.spoutcraft.spoutcraftapi.io.AddonPacket;
import org.spoutcraft.spoutcraftapi.io.SpoutInputStream;
import org.spoutcraft.spoutcraftapi.io.SpoutOutputStream;
import org.spoutcraft.spoutcraftapi.packet.PacketUtil;

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
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		if (!sandboxed) {
			SpoutClient.disableSandbox();
		}
		
		ByteBuffer buffer = stream.getRawBuffer();
		byte[] raw = new byte[buffer.capacity() - buffer.remaining()];
		for (int i = 0; i < raw.length; i++) {
			raw[i] = buffer.get(i);
		}
		data = raw;
		needsCompression = data.length > 512;
	}

	public int getNumBytes() {
		return data.length + 4 + 1 + PacketUtil.getNumBytes(AddonPacket.getPacketId(packet.getClass()));
	}

	@SuppressWarnings("unchecked")
	public void readData(DataInputStream input) throws IOException {
		String id = PacketUtil.readString(input);
		
		boolean sandboxed = SpoutClient.isSandboxed();
		SpoutClient.enableSandbox();
		
		try {
			Class<? extends AddonPacket> packetClass = AddonPacket.getPacketFromId(id);
			Constructor<? extends AddonPacket> constructor = null;
			Constructor<? extends AddonPacket>[] constructors = (Constructor<? extends AddonPacket>[]) packetClass.getConstructors();
			for (Constructor<? extends AddonPacket> c : constructors) {
				if (c.getGenericParameterTypes().length == 0) {
					constructor = c;
					break;
				}
			}
			packet = constructor.newInstance();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		if (!sandboxed) {
			SpoutClient.disableSandbox();
		}
		
		int size = input.readInt();
		compressed = input.readBoolean();
		data = new byte[size];
		input.readFully(data);
	}

	public void writeData(DataOutputStream output) throws IOException {
		PacketUtil.writeString(output, (AddonPacket.getPacketId(packet.getClass())));
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
			}
			catch (Exception e) {
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
			compressed = false;
		}
	}

	public boolean isCompressed() {
		return !needsCompression || compressed;
	}

}
