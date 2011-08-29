package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

//Spout Start
import net.minecraft.client.Minecraft;

import org.getspout.spout.SpoutVersion;
import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.packet.CustomPacket;
import org.getspout.spout.packet.PacketFullVersion;
import org.getspout.spout.packet.PacketRenderDistance;
//Spout End

public class Packet18Animation extends Packet {

	public int entityId;
	public int animate;


	public Packet18Animation() {}

	public Packet18Animation(Entity var1, int var2) {
		this.entityId = var1.entityId;
		this.animate = var2;
	}

	public void readPacketData(DataInputStream var1) throws IOException {
		this.entityId = var1.readInt();
		this.animate = var1.readByte();
	}

	public void writePacketData(DataOutputStream var1) throws IOException {
		var1.writeInt(this.entityId);
		var1.writeByte(this.animate);
	}

	public void processPacket(NetHandler var1) {
		//Spout Start
		if (entityId == -42) {
			SpoutClient.getInstance().setSpoutVersion(new SpoutVersion(1, 0, 5, 0));
			((NetClientHandler)var1).addToSendQueue(this);
			if (SpoutClient.getReloadPacket() != null) {
				((NetClientHandler)var1).addToSendQueue(new CustomPacket(SpoutClient.getReloadPacket()));
				SpoutClient.setReloadPacket(null);
			}
			((NetClientHandler)var1).addToSendQueue(new CustomPacket(new PacketRenderDistance((byte)Minecraft.theMinecraft.gameSettings.renderDistance)));
			((NetClientHandler)var1).addToSendQueue(new CustomPacket(new PacketFullVersion(SpoutClient.getClientVersion().toString())));
			System.out.println("Spout SP Enabled");
		}
		else {
			var1.handleArmAnimation(this);
		}
		//Spout End
	}

	public int getPacketSize() {
		return 5;
	}
}
