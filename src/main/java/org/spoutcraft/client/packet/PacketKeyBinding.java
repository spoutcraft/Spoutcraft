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
import java.util.UUID;

import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;
import org.spoutcraft.api.keyboard.KeyBinding;
import org.spoutcraft.client.SpoutClient;

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

	public void readData(SpoutInputStream input) throws IOException {
		id = input.readString();
		description = input.readString();
		plugin = input.readString();
		key = input.readInt();
		uniqueId = new UUID(input.readLong(), input.readLong());
	}

	public void writeData(SpoutOutputStream output) throws IOException {
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
