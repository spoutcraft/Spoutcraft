package org.spoutcraft.client.packet;

import java.io.File;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.Packet0KeepAlive;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;
import org.spoutcraft.client.io.CustomTextureManager;
import org.spoutcraft.client.io.FileUtil;

public class PacketSendPrecache implements CompressablePacket {
	
	private byte[] fileData;
	private String serverName;
	private boolean compressed = false;
	
	public PacketSendPrecache() {
	}
	
	public PacketSendPrecache(File file) {
		try {
			this.fileData = FileUtils.readFileToByteArray(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//this.serverName = FileUtil.getFileName(file.getPath());
	}
	
	// TODO move to separate thread?
	public void compress() {
		if (!compressed) {
			Deflater deflater = new Deflater();
			deflater.setInput(fileData);
			deflater.setLevel(Deflater.BEST_COMPRESSION);
			deflater.finish();
			ByteArrayOutputStream bos = new ByteArrayOutputStream(fileData.length);
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
			fileData = bos.toByteArray();
			compressed = true;
		}
	}
	
	public boolean isCompressed() {
		return compressed;
	}
	
	public void decompress() {
		if (compressed) {
			Inflater decompressor = new Inflater();
			decompressor.setInput(fileData);

			ByteArrayOutputStream bos = new ByteArrayOutputStream(fileData.length);

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

			fileData = bos.toByteArray();
		}
	}
	
	public void readData(SpoutInputStream input) throws IOException {
		this.serverName = input.readString();
		compressed = input.readBoolean();
		int size = input.readInt();
		this.fileData = new byte[size];
		input.read(fileData);
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeString(serverName);
		output.writeBoolean(compressed);
		output.writeInt(fileData.length);
		output.write(fileData);
	}
	
	public void failure(int playerId) {
		// TODO Auto-generated method stub
	}

	public PacketType getPacketType() {
		return PacketType.PacketSendPrecache;
	}

	public int getVersion() {
		return 0;
	}
	
	public void run(int playerId) {
		
		String zName;
		if (serverName==null || serverName.isEmpty()) {
			zName = "unnamedserver.zip";
		} else {
			zName = serverName.replaceAll("[^a-zA-Z0-9]", "").trim() + ".zip";
		}
		
		File cachedZip = new File(FileUtil.getCacheDir(), zName);
		
		System.out.println("Received Precache File");
		
		if (cachedZip.exists()) {
			cachedZip.delete();
		}
		
		try {
			FileUtils.writeByteArrayToFile(cachedZip, fileData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//unzip
		FileUtil.loadPrecache(cachedZip);
		
		
		((EntityClientPlayerMP)Minecraft.theMinecraft.thePlayer).sendQueue.addToSendQueue(new Packet0KeepAlive());
	}
}
