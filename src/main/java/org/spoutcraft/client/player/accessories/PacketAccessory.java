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
package org.spoutcraft.client.player.accessories;

import java.io.IOException;
import org.spoutcraft.client.packet.PacketType;
import org.spoutcraft.client.packet.SpoutPacket;
import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;

public class PacketAccessory implements SpoutPacket{
	private AccessoryType type;
	private String url, who;
	private boolean add;

	public PacketAccessory() {
	}

	public PacketAccessory(AccessoryType type, String url) {
		this(type, url, true);
	}

	public PacketAccessory(AccessoryType type, String url, boolean add) {
		this.type = type;
		this.url = url;
		this.add = add;
	}

	@Override
	public void readData(SpoutInputStream input) throws IOException {
		who = input.readString();
		type = AccessoryType.get(input.readInt());
		url = input.readString();
		add = input.readBoolean();
	}

	@Override
	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeString(who);
		output.writeInt(type.getId());
		output.writeString(url);
		output.writeBoolean(add);
	}

	@Override
	public void run(int playerId) {
		if (add) {
			AccessoryHandler.addAccessoryType(who, type, url);
		} else {
			AccessoryHandler.removeAccessoryType(who, type);
		}
	}

	@Override
	public void failure(int playerId) {
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketAccessory;
	}

	@Override
	public int getVersion() {
		return 2;
	}
}
