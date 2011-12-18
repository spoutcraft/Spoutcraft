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
package org.spoutcraft.client.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import org.spoutcraft.client.io.FileUtil;
import org.spoutcraft.spoutcraftapi.packet.PacketUtil;

public class PacketCacheDeleteFile implements SpoutPacket {
	private String plugin;
	private String fileName;
	public PacketCacheDeleteFile() {
		
	}
	
	public PacketCacheDeleteFile(String plugin, String fileName) {
		this.plugin = plugin;
		this.fileName = fileName;
	}

	public int getNumBytes() {
		return PacketUtil.getNumBytes(fileName) + PacketUtil.getNumBytes(plugin);
	}

	public void readData(DataInputStream input) throws IOException {
		fileName = PacketUtil.readString(input);
		plugin = PacketUtil.readString(input);
	}

	public void writeData(DataOutputStream output) throws IOException {
		PacketUtil.writeString(output, fileName);
		PacketUtil.writeString(output, plugin);
	}

	public void run(int playerId) {
		File file = FileUtil.findFile(plugin, fileName);
		if (file != null) {
			file.delete();
		}
	}

	public void failure(int playerId) {
		
	}

	public PacketType getPacketType() {
		return PacketType.PacketCacheDeleteFile;
	}

	public int getVersion() {
		return 0;
	}

}
