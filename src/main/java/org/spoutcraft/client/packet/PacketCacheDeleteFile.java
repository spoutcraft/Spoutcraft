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

import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;
import org.spoutcraft.client.io.FileUtil;

public class PacketCacheDeleteFile implements SpoutPacket {
	private String plugin;
	private String fileName;
	public PacketCacheDeleteFile() {
	}

	public PacketCacheDeleteFile(String plugin, String fileName) {
		this.plugin = plugin;
		this.fileName = fileName;
	}

	public void readData(SpoutInputStream input) throws IOException {
		fileName = input.readString();
		plugin = input.readString();
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeString(fileName);
		output.writeString(plugin);
	}

	public void run(int playerId) {
		// TODO Fix security vulnerability: http://pastie.org/private/qdmx5veidnood1ectllkcq
		/*File file = FileUtil.findFile(plugin, fileName);
		if (file != null) {
			file.delete();
		}*/
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
