/*
 * This file is part of Spoutcraft (http://www.spout.org/).
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
import java.io.IOException;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.gui.CustomScreen;
import org.spoutcraft.client.io.FileDownloadThread;

public class PacketPreCacheCompleted implements SpoutPacket{
	public PacketPreCacheCompleted() {
	}

	public int getNumBytes() {
		return 0;
	}

	public void readData(DataInputStream input) throws IOException {
	}

	public void writeData(DataOutputStream output) throws IOException {
	}

	public void run(int playerId) {
		FileDownloadThread.preCacheCompleted.set(System.currentTimeMillis());
		SpoutClient.getInstance().getPacketManager().sendSpoutPacket(this);
		if (!(SpoutClient.getHandle().currentScreen instanceof CustomScreen)) {
			//Closes Downloading Terrain
			SpoutClient.getHandle().displayGuiScreen(null, false);
			//Prevent closing a plugin created menu from opening the downloading terrain
			SpoutClient.getHandle().clearPreviousScreen();
		}
	}

	public void failure(int playerId) {
	}

	public PacketType getPacketType() {
		return PacketType.PacketPreCacheCompleted;
	}

	public int getVersion() {
		return 0;
	}
}
