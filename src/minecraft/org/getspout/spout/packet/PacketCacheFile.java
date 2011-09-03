/*
 * This file is part of Spoutcraft (http://wiki.getspout.org/).
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
package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.io.CRCManager;
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
		int read = input.read(this.fileData);
		System.out.println("Read Bytes: " + read + " Expected Bytes " + size);
		if (read != size) {
			input.skipBytes(size - read);
		}
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
		long expectedCRC = CRCManager.getCRC(fileName);
		long calculatedCRC = FileUtil.getCRC(cache, new byte[16384]);
		System.out.println("Downloaded File " + fileName + "'s CRC " + calculatedCRC + ", expected CRC: " + expectedCRC);
		if (expectedCRC != calculatedCRC) {
			System.out.println("WARNING, Downloaded File " + fileName + "'s CRC " + calculatedCRC + " did not match the expected CRC: " + expectedCRC);
			SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketPreCacheFile(plugin, fileName, expectedCRC, false));
			System.out.println("Requesting re-downloaded of File " + fileName);
		}
		else if (cache.exists() && FileUtil.isImageFile(fileName)) {
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
