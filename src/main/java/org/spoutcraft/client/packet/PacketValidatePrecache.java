/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 SpoutcraftDev <http://spoutcraft.org//>
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
import org.spoutcraft.client.precache.PrecacheManager;
import org.spoutcraft.client.precache.PrecacheTuple;

public class PacketValidatePrecache implements SpoutPacket {
	int count;
	PrecacheTuple[] plugins;

	@Override
	public void readData(SpoutInputStream input) throws IOException {
		count = input.readInt();
		plugins = new PrecacheTuple[count];
		if (count > 0) {
			for (int i = 0; i<count; i++) {
				String plugin = input.readString();
				String version = input.readString();
				long crc = input.readLong();
				plugins[i] = new PrecacheTuple(plugin, version, crc);
			}
		}
	}

	@Override
	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeInt(count);
		for (int i=0; i<count; i++) {
			output.writeString(plugins[i].getPlugin());
			output.writeString(plugins[i].getVersion());
			output.writeLong(plugins[i].getCrc());
		}
	}

	@Override
	public void run(int playerId) {
		PrecacheManager.reset();
		// Build the precache list
		for (PrecacheTuple plugin : plugins) {
			PrecacheManager.addPlugin(plugin);
		}

		PrecacheManager.doNextCache();
	}

	@Override
	public void failure(int playerId) {
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketValidatePrecache;
	}

	@Override
	public int getVersion() {
		return 0;
	}
}
