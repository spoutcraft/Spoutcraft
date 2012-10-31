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

import net.minecraft.src.*;

import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;
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

	public void readData(SpoutInputStream input) throws IOException {
		entityId = input.readInt();
		skinURL = input.readString();
		cloakURL = input.readString();
		release = input.readBoolean();
	}

	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeInt(entityId);
		output.writeString(skinURL);
		output.writeString(cloakURL);
		output.writeBoolean(release);
	}

	public void run(int PlayerId) {
		EntityPlayer e = SpoutClient.getInstance().getPlayerFromId(entityId);
		if (e != null) {
			// Check if these are the Minecraft skin/cape, if so, use defaults instead
			String mcSkin = "http://s3.amazonaws.com/MinecraftSkins/" + e.username + ".png";
			String mcCape = "http://s3.amazonaws.com/MinecraftCloaks/" + e.username + ".png";
			System.out.println(e.username + " is going to be sent skinURL: " + skinURL);
			if (this.skinURL.equalsIgnoreCase(mcSkin)) {
				this.skinURL = "http://cdn.spout.org/game/vanilla/skin/" + e.username + ".png";
			}
			if (this.cloakURL.equalsIgnoreCase(mcCape)) {
				if (e.vip != null && e.vip.getCape() != null) {
					this.cloakURL = e.vip.getCape();
				} else {
					this.cloakURL = "http://cdn.spout.org/game/vanilla/cape/" + e.username + ".png";
				}
			}

			if (!"none".equals(this.skinURL)) {
				e.skinUrl = this.skinURL;
			}
			if (!"none".equals(this.cloakURL)) {
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
