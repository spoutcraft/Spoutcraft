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
import java.io.IOException;
import java.io.File;

import net.minecraft.src.RenderEngine;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.io.FileUtil;
import org.getspout.spout.texture.TexturePackAction;
import org.getspout.spout.io.Download;
import org.getspout.spout.io.FileDownloadThread;

public class PacketTexturePack implements SpoutPacket{
	private static byte[] downloadBuffer = new byte[16384];
	private String url;
	private long expectedCRC;
	
	public PacketTexturePack(){
		
	}
	
	public PacketTexturePack(String url, long expectedCRC) {
		this.url = url;
		this.expectedCRC = expectedCRC;
	}

	@Override
	public int getNumBytes() {
		return PacketUtil.getNumBytes(url) + 8;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		url = PacketUtil.readString(input, 256);
		expectedCRC = input.readLong();
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		PacketUtil.writeString(output, url);
		output.writeLong(expectedCRC);
	}

	@Override
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
			File texturePack = new File(FileUtil.getTexturePackDirectory(), fileName);
			if (FileUtil.getCRC(texturePack, downloadBuffer) != expectedCRC && expectedCRC != 0) {
				texturePack.delete();
			}
			Download download = new Download(fileName, FileUtil.getTexturePackDirectory(), url, new TexturePackAction(fileName, FileUtil.getTexturePackDirectory()));
			FileDownloadThread.getInstance().addToDownloadQueue(download);
		}
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketTexturePack;
	}
	
	@Override
	public int getVersion() {
		return 2;
	}

	@Override
	public void failure(int playerId) {
		
	}

}
