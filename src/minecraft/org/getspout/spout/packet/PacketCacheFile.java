package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.getspout.spout.io.CustomTextureManager;
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
		this.fileName = FileUtil.getFileName(this.fileName);
		if (!FileUtil.canCache(fileName)) {
			System.out.println("WARNING, " + plugin + " tried to cache an invalid file type: " + fileName);
			return;
		}
		File directory = new File(FileUtil.getCacheDirectory(), plugin);
		if (!directory.exists()) {
			directory.mkdir();
		}
		File cache = new File(directory, fileName);
		try {
			FileUtils.writeByteArrayToFile(cache, fileData);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (cache.exists() && FileUtil.isImageFile(fileName)) {
			CustomTextureManager.getTextureFromUrl(plugin, fileName);
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
