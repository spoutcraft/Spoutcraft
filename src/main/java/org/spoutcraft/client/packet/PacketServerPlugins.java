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

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.addon.Addon;
import org.spoutcraft.api.addon.ServerAddon;
import org.spoutcraft.api.addon.SimpleAddonManager;
import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;

public class PacketServerPlugins implements SpoutPacket {
	private String[] plugins;
	private String[] versions;
	public PacketServerPlugins() {
	}

	public void readData(SpoutInputStream input) throws IOException {
		int size = input.readShort();
		plugins = new String[size];
		versions = new String[size];
		for (int i = 0; i < size; i++) {
			plugins[i] = input.readString();
			versions[i] = input.readString();
		}
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeShort((short) plugins.length);
		for (int i = 0; i < plugins.length; i++) {
			output.writeString(plugins[i]);
			output.writeString(versions[i]);
		}
	}

	public void run(int playerId) {
		SimpleAddonManager addonManager = ((SimpleAddonManager)Spoutcraft.getAddonManager());
		for (int i = 0; i < plugins.length; i++) {
			Addon addon = addonManager.getAddon(plugins[i]);
			if (addon == null || addon instanceof ServerAddon) {
				addonManager.addFakeAddon(new ServerAddon(plugins[i], versions[i], null));
			}
		}
	}

	public void failure(int playerId) {
	}

	public PacketType getPacketType() {
		return PacketType.PacketServerPlugins;
	}

	public int getVersion() {
		return 0;
	}
}
