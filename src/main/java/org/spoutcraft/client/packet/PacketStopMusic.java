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

public class PacketStopMusic implements SpoutPacket {
	private boolean resetTimer = false;
	private int fadeTime = -1;
	public PacketStopMusic() {
	}

	public PacketStopMusic(boolean resetTimer, int fadeTime) {
		this.resetTimer = resetTimer;
		this.fadeTime = fadeTime;
	}

	public int getNumBytes() {
		return 5;
	}

	public void readData(SpoutInputStream input) throws IOException {
		resetTimer = input.readBoolean();
		fadeTime = input.readInt();
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeBoolean(resetTimer);
		output.writeInt(fadeTime);
	}

	public void run(int PlayerId) {
		if (fadeTime == -1) {
			SpoutClient.getHandle().sndManager.stopMusic();
		} else {
			SpoutClient.getHandle().sndManager.fadeOut(fadeTime);
		}
		if (resetTimer) {
			SpoutClient.getHandle().sndManager.resetTime();
		}
	}

	public PacketType getPacketType() {
		return PacketType.PacketStopMusic;
	}

	public int getVersion() {
		return 0;
	}

	public void failure(int playerId) {
	}
}
