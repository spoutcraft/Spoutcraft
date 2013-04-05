/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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
import org.spoutcraft.api.entity.EntitySkinType;
import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;
import org.spoutcraft.client.entity.CraftEntity;

public class PacketEntitySkin implements SpoutPacket {
	protected String texture = "";
	protected int entityId;
	protected byte textureId = 0;

	public void readData(SpoutInputStream input) throws IOException {
		entityId = input.readInt();
		textureId = (byte) input.read();
		texture = input.readString();
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeInt(entityId);
		output.write(textureId);
		output.writeString(texture);
	}

	public void run(int PlayerId) {
		if (texture.equals("[reset]")) {
			texture = null;
		}
		CraftEntity entity = Spoutcraft.getWorld().getEntityFromId(entityId);
		if (entity != null) {
			entity.setSkin(texture, EntitySkinType.getType(textureId));
		}
	}

	public PacketType getPacketType() {
		return PacketType.PacketEntitySkin;
	}

	public int getVersion() {
		return 1;
	}

	public void failure(int playerId) {
	}
}
