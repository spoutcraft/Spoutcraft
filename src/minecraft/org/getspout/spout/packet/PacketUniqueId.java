package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.getspout.spout.client.SpoutClient;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.WorldClient;


public class PacketUniqueId implements CompressablePacket{
	private boolean alive = false;
	private boolean compressed = false;
	private byte[] data = null;
	
	public PacketUniqueId() {
		
	}
	
	public int getNumBytes() {
		return 2 + (data != null ? data.length : 0) + 4;
	}

	public void readData(DataInputStream input) throws IOException {
		int size = input.readInt();
		if (size > 0){
			data = new byte[size];
			input.readFully(data);
		}
		alive = input.readBoolean();
		compressed = input.readBoolean();
	}

	public void writeData(DataOutputStream output) throws IOException {
		if (data != null) {
			output.writeInt(data.length);
			output.write(data);
		}
		else {
			output.writeInt(0);
		}
		output.writeBoolean(alive);
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
				
				Entity e = null;
				if (Minecraft.theMinecraft.thePlayer != null && id == Minecraft.theMinecraft.thePlayer.entityId) {
					e = Minecraft.theMinecraft.thePlayer;
				}
				else {
					e = ((WorldClient)Minecraft.theMinecraft.theWorld).func_709_b(id);
				}
				UUID current = new UUID(msb, lsb);
				if (e != null) {
					e.uniqueId = current;
					e.uuidValid = alive;
				}
				if (!alive) {
					SpoutClient.getInstance().getEntityManager().removeData(current);
				}
			}
		}
	}

	public void failure(int playerId) {

	}

	public PacketType getPacketType() {
		return PacketType.PacketUniqueId;
	}

	public int getVersion() {
		return 2;
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