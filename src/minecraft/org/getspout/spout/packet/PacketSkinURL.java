/*
 * This file is part of Spoutcraft (http://wiki.getspout.org/).
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
package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import org.getspout.spout.client.SpoutClient;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class PacketSkinURL implements SpoutPacket{
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
	public int getNumBytes() {
		return 5 + PacketUtil.getNumBytes(skinURL) + PacketUtil.getNumBytes(cloakURL);
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		entityId = input.readInt();
		skinURL = PacketUtil.readString(input, 256);
		cloakURL = PacketUtil.readString(input, 256);
		release = input.readBoolean();
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(entityId);
		PacketUtil.writeString(output, skinURL);
		PacketUtil.writeString(output, cloakURL);
		output.writeBoolean(release);
	}

	@Override
	public void run(int PlayerId) {
		EntityPlayer e = SpoutClient.getInstance().getPlayerFromId(entityId);
		if (e != null) {
			boolean reobtainSkin = false;
			boolean reobtainCloak = false;
			String oldSkin = e.skinUrl;
			String oldCloak = e.playerCloakUrl;
			if (!this.skinURL.equals("none")) {
				e.skinUrl = this.skinURL;
			}
			if (!this.cloakURL.equals("none")) {
				e.cloakUrl = this.cloakURL;
				e.playerCloakUrl = this.cloakURL;
			}
			if (release) {
				e.worldObj.releaseEntitySkin(e);
				List<EntityPlayer> players = e.worldObj.playerEntities;
				if (oldSkin != null) {
					for (EntityPlayer player : players) {
						if (player.skinUrl != null && player.skinUrl.equals(oldSkin)) {
							reobtainSkin = true;
							break;
						}
					}
				}
				if (oldCloak != null) {
					for (EntityPlayer player : players) {
						if (player.playerCloakUrl != null && player.playerCloakUrl.equals(oldSkin)) {
							reobtainCloak = true;
							break;
						}
					}
				}
				if (reobtainSkin) {
					Minecraft.theMinecraft.renderEngine.obtainImageData(oldSkin, new ImageBufferDownload());
				}
				if (reobtainCloak) {
					Minecraft.theMinecraft.renderEngine.obtainImageData(oldCloak, new ImageBufferDownload());
				}
			}
			e.worldObj.obtainEntitySkin(e);
		}
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketSkinURL;
	}
	
	@Override
	public int getVersion() {
		return 1;
	}

	@Override
	public void failure(int playerId) {
		
	}

}
