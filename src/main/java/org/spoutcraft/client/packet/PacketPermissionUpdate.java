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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;
import org.spoutcraft.client.SpoutClient;

public class PacketPermissionUpdate implements SpoutPacket {
private Map<String, Boolean> permissions;

	public PacketPermissionUpdate() {
		permissions = new HashMap<String, Boolean>();
	}

	public PacketPermissionUpdate(Map<String, Boolean> permissions) {
		this.permissions = permissions;
	}

	@Override
	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeInt(permissions.size());
		for (Entry<String, Boolean> perm:permissions.entrySet()) {
			output.writeString(perm.getKey());
			output.writeBoolean(perm.getValue());
		}
	}

	@Override
	public void readData(SpoutInputStream input) throws IOException {
		int num = input.readInt();
		for (int i = 0; i < num; i++) {
			String perm = input.readString();
			boolean allowed = input.readBoolean();
			permissions.put(perm, allowed);
		}
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketPermissionUpdate;
	}

	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public void run(int playerId) {
		for (Entry<String, Boolean> perm:permissions.entrySet()) {
			SpoutClient.getInstance().setPermission(perm.getKey(), perm.getValue());
		}
	}

	@Override
	public void failure(int playerId) {}
}
