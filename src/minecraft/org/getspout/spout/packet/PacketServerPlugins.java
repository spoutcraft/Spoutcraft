/*
 * This file is part of SpoutAPI (http://wiki.getspout.org/).
 * 
 * SpoutAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SpoutAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.addon.ServerAddon;
import org.spoutcraft.spoutcraftapi.addon.SimpleAddonManager;
import org.spoutcraft.spoutcraftapi.packet.PacketUtil;

public class PacketServerPlugins implements SpoutPacket{
	private String[] plugins;
	private String[] versions;
	public PacketServerPlugins() {
		
	}

	public int getNumBytes() {
		int size = 2;
		for (int i = 0; i < plugins.length; i++) {
			size += PacketUtil.getNumBytes(plugins[i]);
			size += PacketUtil.getNumBytes(versions[i]);
		}
		return size;
	}

	public void readData(DataInputStream input) throws IOException {
		int size = input.readShort();
		plugins = new String[size];
		versions = new String[size];
		for (int i = 0; i < size; i++) {
			plugins[i] = PacketUtil.readString(input);
			versions[i] = PacketUtil.readString(input);
		}
	}

	public void writeData(DataOutputStream output) throws IOException {
		output.writeShort(plugins.length);
		for (int i = 0; i < plugins.length; i++) {
			PacketUtil.writeString(output, plugins[i]);
			PacketUtil.writeString(output, versions[i]);
		}
	}

	public void run(int playerId) {
		SimpleAddonManager addonManager = ((SimpleAddonManager)Spoutcraft.getAddonManager());
		for (int i = 0; i < plugins.length; i++) {
			Addon addon = addonManager.getAddon(plugins[i]);
			if(addon == null) {
				addon = new ServerAddon(plugins[i], versions[i], null);
				addonManager.addFakeAddon((ServerAddon) addon);
			}
			else if(addon instanceof ServerAddon) {
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
