package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.player.Player;
import org.getspout.spout.player.SpoutPlayer;

import net.minecraft.src.*;

public class PacketSkinURL implements SpoutPacket{
	
	public PacketSkinURL() {
	}
	
	public PacketSkinURL(int id, String skinURL, String cloakURL) {
		this.entityId = id;
		this.skinURL = skinURL;
		this.cloakURL = cloakURL;
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
	public int entityId;
	public String skinURL;
	public String cloakURL;

	@Override
	public int getNumBytes() {
		return 4 + PacketUtil.getNumBytes(skinURL) + PacketUtil.getNumBytes(cloakURL);
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		entityId = input.readInt();
		skinURL = PacketUtil.readString(input, 256);
		cloakURL = PacketUtil.readString(input, 256);
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(entityId);
		PacketUtil.writeString(output, skinURL);
		PacketUtil.writeString(output, cloakURL);
	}

	@Override
	public void run(int PlayerId) {
		Player p = SpoutClient.getInstance().getPlayerFromId(entityId);
		if (p != null) {
			EntityPlayer e = ((SpoutPlayer)p).getHandle();
			if (e != null) {
				if (!this.skinURL.equals("none")) {
					e.skinUrl = this.skinURL;
				}
				if (!this.cloakURL.equals("none")) {
					e.cloakUrl = this.cloakURL;
					e.playerCloakUrl = this.cloakURL;
				}
				e.worldObj.releaseEntitySkin(e);
				e.worldObj.obtainEntitySkin(e);
			}
		}
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketSkinURL;
	}
	
	@Override
	public int getVersion() {
		return 0;
	}

}
