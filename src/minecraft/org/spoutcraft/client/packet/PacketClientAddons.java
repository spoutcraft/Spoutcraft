/*
 * This file is part of Spoutcraft (http://www.spout.org/).
 *
 * Spoutcraft is licensed under the SpoutDev License Version 1.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spoutcraft.client.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.addon.ServerAddon;
import org.spoutcraft.spoutcraftapi.addon.SimpleAddonManager;
import org.spoutcraft.spoutcraftapi.packet.PacketUtil;

public class PacketClientAddons implements SpoutPacket{
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

	public int getNumBytes() {
		int size = 2;
		for (int i = 0; i < addons.length; i++) {
			size += PacketUtil.getNumBytes(addons[i]);
			size += PacketUtil.getNumBytes(versions[i]);
		}
		return size;
	}

	public void readData(DataInputStream input) throws IOException {
		int size = input.readShort();
		addons = new String[size];
		versions = new String[size];
		for (int i = 0; i < size; i++) {
			addons[i] = PacketUtil.readString(input);
			versions[i] = PacketUtil.readString(input);
		}
	}

	public void writeData(DataOutputStream output) throws IOException {
		output.writeShort(addons.length);
		for (int i = 0; i < addons.length; i++) {
			PacketUtil.writeString(output, addons[i]);
			PacketUtil.writeString(output, versions[i]);
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
