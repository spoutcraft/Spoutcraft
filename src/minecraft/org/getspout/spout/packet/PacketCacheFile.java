package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.getspout.spout.io.FileUtil;

public class PacketCacheFile implements SpoutPacket {
	private String plugin;
	private byte[] fileData;
	private String fileName;
	
	public PacketCacheFile() {
		
	}
	
	public PacketCacheFile(String plugin, File file) {
		this.plugin = plugin;
		try {
			this.fileData = FileUtils.readFileToByteArray(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.fileName = FileUtil.getFileName(file.getPath());
	}

	@Override
	public int getNumBytes() {
		return PacketUtil.getNumBytes(fileName) + PacketUtil.getNumBytes(plugin) + fileData.length + 4;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		this.fileName = PacketUtil.readString(input);
		this.plugin = PacketUtil.readString(input);
		int size = input.readInt();
		this.fileData = new byte[size];
		input.read(this.fileData);
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		PacketUtil.writeString(output, fileName);
		PacketUtil.writeString(output, plugin);
		output.writeInt(fileData.length);
		output.write(fileData);
	}

	@Override
	public void run(int playerId) {
		File directory = FileUtil.getCacheDirectory();
		File cache = new File(directory, fileName);
		try {
			FileUtils.writeByteArrayToFile(cache, fileData);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void failure(int playerId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketCacheFile;
	}

	@Override
	public int getVersion() {
		return 0;
	}

}
