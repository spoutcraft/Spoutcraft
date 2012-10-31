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

import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;
import org.spoutcraft.client.SpoutClient;

public class PacketMusicChange implements SpoutPacket {
	protected int id;
	protected int volumePercent;
	boolean cancel = false;

	public PacketMusicChange() {
	}

	public PacketMusicChange(int music, int volumePercent) {
		this.id = music;
		this.volumePercent = volumePercent;
	}

	public boolean isCancelled() {
		return cancel;
	}

	public void readData(SpoutInputStream input) throws IOException {
		id = input.readInt();
		volumePercent = input.readInt();
		cancel =  input.readBoolean();
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeInt(id);
		output.writeInt(volumePercent);
		output.writeBoolean(cancel);
	}

	public void run(int playerId) {
		if (cancel) {
			SpoutClient.getHandle().sndManager.cancelled = true;
		} else {
			SpoutClient.getHandle().sndManager.allowed = true;
		}
	}

	public PacketType getPacketType() {
		return PacketType.PacketMusicChange;
	}

	public int getVersion() {
		return 0;
	}

	public void failure(int playerId) {
	}
}
