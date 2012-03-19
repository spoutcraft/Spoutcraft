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

import net.minecraft.src.EntityPlayer;

import org.spoutcraft.spoutcraftapi.packet.PacketUtil;

import org.spoutcraft.client.SpoutClient;

public class PacketSkinURL implements SpoutPacket {
	public int entityId;
	public String skinURL;
	public String cloakURL;
	public boolean release = true;

	public PacketSkinURL() {

	}

	public PacketSkinURL(int id, String skinURL, String cloakURL) {
		this.entityId = id;
		this.skinURL = skinURL;
		this.cloakURL = cloakURL;
		release = false;
	}

	public PacketSkinURL(int id, String skinURL) {
		this.entityId = id;
		this.skinURL = skinURL;
		this.cloakURL = "none";
	}

	public PacketSkinURL(String cloakURL, int id) {
		this.entityId = id;
		this.skinURL = "none";
		this.cloakURL = cloakURL;
	}

	public int getNumBytes() {
		return 5 + PacketUtil.getNumBytes(skinURL) + PacketUtil.getNumBytes(cloakURL);
	}

	public void readData(DataInputStream input) throws IOException {
		entityId = input.readInt();
		skinURL = PacketUtil.readString(input, 256);
		cloakURL = PacketUtil.readString(input, 256);
		release = input.readBoolean();
	}

	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(entityId);
		PacketUtil.writeString(output, skinURL);
		PacketUtil.writeString(output, cloakURL);
		output.writeBoolean(release);
	}

	public void run(int PlayerId) {
		EntityPlayer e = SpoutClient.getInstance().getPlayerFromId(entityId);
		if (e != null) {
			if (!this.skinURL.equals("none")) {
				e.skinUrl = this.skinURL;
			}
			if (!this.cloakURL.equals("none")) {
				e.updateCloak(cloakURL);
			}
			if (release) {
				e.worldObj.releaseEntitySkin(e);
			}
			e.worldObj.obtainEntitySkin(e);
		}
	}

	public PacketType getPacketType() {
		return PacketType.PacketSkinURL;
	}

	public int getVersion() {
		return 1;
	}

	public void failure(int playerId) {

	}
}
