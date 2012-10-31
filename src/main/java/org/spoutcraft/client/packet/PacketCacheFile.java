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

import java.io.File;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

import net.minecraft.src.*;
import net.minecraft.client.Minecraft;

import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.io.CRCManager;
import org.spoutcraft.client.io.CustomTextureManager;
import org.spoutcraft.client.io.FileUtil;

public class PacketCacheFile implements CompressablePacket {
	private String plugin;
	private byte[] fileData;
	private String fileName;
	private boolean compressed = false;

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
		this.fileName = input.readString();
		this.plugin = input.readString();
		compressed = input.readBoolean();
		int size = input.readInt();
		this.fileData = new byte[size];
		input.read(fileData);
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeString(fileName);
		output.writeString(plugin);
		output.writeBoolean(compressed);
		output.writeInt(fileData.length);
		output.write(fileData);
	}

	public void run(int playerId) {
		this.fileName = FileUtil.getFileName(this.fileName);
		if (!FileUtil.canCache(fileName)) {
			System.out.println("WARNING, " + plugin + " tried to cache an invalid file type: " + fileName);
			return;
		}
		File directory = new File(FileUtil.getCacheDir(), plugin);
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
		((EntityClientPlayerMP)Minecraft.theMinecraft.thePlayer).sendQueue.addToSendQueue(new Packet0KeepAlive());
	}

	public void failure(int playerId) {
		// TODO Auto-generated method stub
	}

	public PacketType getPacketType() {
		return PacketType.PacketCacheFile;
	}

	public int getVersion() {
		return 1;
	}
}
