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

import org.spoutcraft.api.addon.Addon;
import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;

public class PacketClientAddons implements SpoutPacket {
	private String[] addons;
	private String[] versions;

	public PacketClientAddons() {
	}

	public PacketClientAddons(Addon[] addons) {
		this.addons = new String[addons.length];
		this.versions = new String[addons.length];
		for (int i = 0; i < addons.length; i++) {
			this.addons[i] = addons[i].getDescription().getName();
			this.versions[i] = addons[i].getDescription().getVersion();
			if (this.addons[i] == null) {
				this.addons[i] = "";
			}
			if (this.versions[i] == null) {
				this.versions[i] = "";
			}
		}
	}

	public void readData(SpoutInputStream input) throws IOException {
		int size = input.readShort();
		addons = new String[size];
		versions = new String[size];
		for (int i = 0; i < size; i++) {
			addons[i] = input.readString();
			versions[i] = input.readString();
		}
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeShort((short) addons.length);
		for (int i = 0; i < addons.length; i++) {
			output.writeString(addons[i]);
			output.writeString(versions[i]);
		}
	}

	public void run(int playerId) {
	}

	public void failure(int playerId) {
	}

	public PacketType getPacketType() {
		return PacketType.PacketClientAddons;
	}

	public int getVersion() {
		return 0;
	}
}
