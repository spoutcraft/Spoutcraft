package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.io.Download;
import org.getspout.spout.io.FileDownloadThread;
import org.getspout.spout.io.FileUtil;

public class PacketPreCacheFile implements SpoutPacket{
	private static byte[] downloadBuffer = new byte[16384];
	private boolean cached = false;
	private boolean url = false;
	private long expectedCRC;
	private String file;
	private String plugin;
	
	public PacketPreCacheFile() {
		
	}
	
	public PacketPreCacheFile(String plugin, String file, long expectedCRC, boolean url) {
		this.file = file;
		this.plugin = plugin;
		this.expectedCRC = expectedCRC;
		this.url = url;
	}

	@Override
	public int getNumBytes() {
		return 10 + PacketUtil.getNumBytes(file) + PacketUtil.getNumBytes(plugin);
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		this.cached = input.readBoolean();
		this.url = input.readBoolean();
		this.expectedCRC = input.readLong();
		this.file = PacketUtil.readString(input);
		this.plugin = PacketUtil.readString(input);
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		output.writeBoolean(this.cached);
		output.writeBoolean(this.url);
		output.writeLong(this.expectedCRC);
		PacketUtil.writeString(output, this.file);
		PacketUtil.writeString(output, this.plugin);
	}

	@Override
	public void run(int playerId) {
		File directory = FileUtil.getCacheDirectory();
		String fileName = FileUtil.getFileName(file);
		File expected = new File(directory, fileName);
		if (expected.exists()) {
			long crc = FileUtil.getCRC(expected, downloadBuffer);
			this.cached = crc == expectedCRC;
			System.out.println("Calculated CRC: " + crc + " Expected CRC: " + expectedCRC);
		}
		if (!cached) {
			if (expected.exists()) {
				expected.delete();
			}
			System.out.println("No cached file found for " + fileName);
			//Request copy of file
			if (!url) {
				SpoutClient.getInstance().getPacketManager().sendSpoutPacket(this);
			}
			//Begin download
			else {
				Download data = new Download(fileName, FileUtil.getCacheDirectory(), file, null);
				FileDownloadThread.getInstance().addToDownloadQueue(data);
			}
		}
		else {
			System.out.println("Found valid cached file found for " + fileName);
		}
	}

	@Override
	public void failure(int playerId) {
		
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketPreCacheFile;
	}

	@Override
	public int getVersion() {
		return 0;
	}

}
