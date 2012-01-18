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
import java.util.UUID;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.spoutcraftapi.keyboard.KeyBinding;
import org.spoutcraft.spoutcraftapi.packet.PacketUtil;

public class PacketKeyBinding implements SpoutPacket {
	private String id;
	private String plugin;
	private String description;
	private int key;
	private boolean pressed;
	private UUID uniqueId;

	public PacketKeyBinding() {

	}

	public PacketKeyBinding(KeyBinding binding, int key, boolean pressed, int screen) {
		this.key = key;
		this.pressed = pressed;
		this.uniqueId = binding.getUniqueId();
	}

	public int getNumBytes() {
		return 4 + 1 + 16;
	}

	public void readData(DataInputStream input) throws IOException {
		id = PacketUtil.readString(input);
		description = PacketUtil.readString(input);
		plugin = PacketUtil.readString(input);
		key = input.readInt();
		uniqueId = new UUID(input.readLong(), input.readLong());
	}

	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(key);
		output.writeBoolean(pressed);
		output.writeLong(uniqueId.getMostSignificantBits());
		output.writeLong(uniqueId.getLeastSignificantBits());
	}

	public void run(int playerId) {
		KeyBinding binding = new KeyBinding(key, plugin, id, description);
		binding.setUniqueId(uniqueId);
		SpoutClient.getInstance().getKeyBindingManager().registerControl(binding);
	}

	public void failure(int playerId) {}

	public PacketType getPacketType() {
		return PacketType.PacketKeyBinding;
	}

	public int getVersion() {
		return 0;
	}
}
