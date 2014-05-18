/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 SpoutcraftDev <http://spoutcraft.org/>
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
package org.spoutcraft.client.packet.builtin;

import java.io.IOException;

import net.minecraft.src.AbstractClientPlayer;

import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.packet.PacketType;
import org.spoutcraft.client.packet.SpoutPacket;

public class PacketSkinURL extends SpoutPacket {
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

	@Override
	public void readData(SpoutInputStream input) throws IOException {
		entityId = input.readInt();
		skinURL = input.readString();
		cloakURL = input.readString();
		release = input.readBoolean();
	}

	@Override
	public void writeData(SpoutOutputStream output) throws IOException {
		output.writeInt(entityId);
		output.writeString(skinURL);
		output.writeString(cloakURL);
		output.writeBoolean(release);
	}

	@Override
	public void run(int PlayerId) {
		AbstractClientPlayer e = SpoutClient.getInstance().getAbstractPlayerFromId(entityId);
		if (e != null) {
			// Check if these are the Minecraft skin/cape, if so, use defaults instead
			String mcSkin = "http://s3.amazonaws.com/MinecraftSkins/" + e.username + ".png";
			String mcCape = "http://s3.amazonaws.com/MinecraftCloaks/" + e.username + ".png";
			if (!"none".equals(this.skinURL)) {
				//System.out.println(e.username + " is going to be sent skinURL: " + skinURL + " from SpoutPlugin's API.");
			}
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
				e.customSkinUrl = this.skinURL;
			}
			if (!"none".equals(this.cloakURL)) {
				e.customCapeUrl = this.cloakURL;
			}
			e.setupCustomSkin();
		}
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketSkinURL;
	}

	@Override
	public int getVersion() {
		return 2;
	}

	@Override
	public void failure(int playerId) {
	}
}
