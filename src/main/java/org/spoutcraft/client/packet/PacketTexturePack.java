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
import java.io.File;

import net.minecraft.src.RenderEngine;

import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.io.Download;
import org.spoutcraft.client.io.FileDownloadThread;
import org.spoutcraft.client.io.FileUtil;
import org.spoutcraft.client.texture.TexturePackAction;

public class PacketTexturePack implements SpoutPacket {
	private static byte[] downloadBuffer = new byte[16384];
	private String url;
	private long expectedCRC;

	public PacketTexturePack() {
	}

	public PacketTexturePack(String url, long expectedCRC) {
		this.url = url;
		this.expectedCRC = expectedCRC;
	}

	public void readData(SpoutInputStream input) throws IOException {
		url = input.readString();
		expectedCRC = input.readLong();
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeString(url);
		output.writeLong(expectedCRC);
	}

	public void run(int PlayerId) {
		if (url.equals("[none]")) {
			if (SpoutClient.getHandle().renderEngine.oldPack != null) {
				RenderEngine rengine = SpoutClient.getHandle().renderEngine;
				rengine.texturePack.setTexturePack(rengine.oldPack);
				rengine.oldPack = null;
				rengine.refreshTextures();
			}
		} else {
			String fileName = FileUtil.getFileName(url);
			if (!FileUtil.isZippedFile(fileName)) {
				System.out.println("Rejecting Invalid Texture Pack: " + fileName);
				return;
			}
			File texturePack = new File(FileUtil.getTexturePackDir(), fileName);
			if (FileUtil.getCRC(texturePack, downloadBuffer) != expectedCRC && expectedCRC != 0) {
				texturePack.delete();
			}
			Download download = new Download(fileName, FileUtil.getTexturePackDir(), url, new TexturePackAction(fileName, FileUtil.getTexturePackDir()));
			FileDownloadThread.getInstance().addToDownloadQueue(download);
		}
	}

	public PacketType getPacketType() {
		return PacketType.PacketTexturePack;
	}

	public int getVersion() {
		return 2;
	}

	public void failure(int playerId) {
	}
}
