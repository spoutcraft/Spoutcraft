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
import org.spoutcraft.spoutcraftapi.entity.Entity;
import org.spoutcraft.spoutcraftapi.entity.EntitySkinType;
import org.spoutcraft.spoutcraftapi.packet.PacketUtil;

public class PacketEntitySkin implements SpoutPacket {
	protected String texture = "";
	protected int entityId;
	protected byte textureId = 0;

	public int getNumBytes() {
		return PacketUtil.getNumBytes(texture) + 4 + 1;
	}

	public void readData(DataInputStream input) throws IOException {
		entityId = input.readInt();
		textureId = input.readByte();
		texture = PacketUtil.readString(input);
	}

	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(entityId);
		output.writeByte(textureId);
		PacketUtil.writeString(output, texture);
	}

	public void run(int PlayerId) {
		if (texture.equals("[reset]")) {
			texture = null;
		}
		Entity entity = Spoutcraft.getWorld().getEntityFromId(entityId);
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
