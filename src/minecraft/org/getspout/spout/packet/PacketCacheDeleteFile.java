package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import org.getspout.spout.io.FileUtil;

public class PacketCacheDeleteFile implements SpoutPacket {
	private String plugin;
	private String fileName;
	public PacketCacheDeleteFile() {
		
	}
	
	public PacketCacheDeleteFile(String plugin, String fileName) {
		this.plugin = plugin;
		this.fileName = fileName;
	}

	@Override
	public int getNumBytes() {
		return PacketUtil.getNumBytes(fileName) + PacketUtil.getNumBytes(plugin);
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		fileName = PacketUtil.readString(input);
		plugin = PacketUtil.readString(input);
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		PacketUtil.writeString(output, fileName);
		PacketUtil.writeString(output, plugin);
	}

	@Override
	public void run(int playerId) {
		File file = FileUtil.findAudioFile(plugin, fileName);
		if (file != null) {
			file.delete();
		}
		file = FileUtil.findTextureFile(plugin, fileName);
		if (file != null) {
			file.delete();
		}
	}

	@Override
	public void failure(int playerId) {
		
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketCacheDeleteFile;
	}

	@Override
	public int getVersion() {
		return 0;
	}

}
