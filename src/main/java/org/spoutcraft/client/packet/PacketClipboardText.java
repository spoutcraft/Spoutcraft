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

public class PacketClipboardText implements SpoutPacket {
	protected String text;
	public PacketClipboardText() {
	}

	public PacketClipboardText(String text) {
		this.text = text;
	}

	public void readData(SpoutInputStream input) throws IOException {
		text = input.readString();
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		if (text.length() > Short.MAX_VALUE) {
			text = text.substring(0, Short.MAX_VALUE - 1);
		}
		output.writeString(text);
	}

	public void run(int playerId) {
	}

	public PacketType getPacketType() {
		return PacketType.PacketClipboardText;
	}

	public int getVersion() {
		return 0;
	}

	public void failure(int playerId) {
	}
}
