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

public class PacketNotification extends PacketAlert {
	protected int time;
	protected short data;
	public PacketNotification() {
	}

	public PacketNotification(String title, String message, int itemId, short data, int time) {
		super(title, message, itemId);
		this.time = time;
		this.data = data;
	}

	public void readData(SpoutInputStream input) throws IOException {
		super.readData(input);
		this.data = input.readShort();
		this.time = input.readInt();
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		super.writeData(output);
		output.writeShort(data);
		output.writeInt(time);
	}

	public PacketType getPacketType() {
		return PacketType.PacketNotification;
	}

	public void run(int PlayerId) {
		SpoutClient.getInstance().getActivePlayer().showAchievement(title, message, itemId, data, time);
	}

	public int getVersion() {
		return 0;
	}
}
