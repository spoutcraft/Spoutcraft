package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.src.Entity;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;

//Spout Start
import net.minecraft.client.Minecraft;

import org.getspout.spout.client.SpoutClient;
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
		System.out.println("Packet 18: " + entityId);
		if (entityId == -42) {
			SpoutClient.getInstance().setSpoutVersion(1);
			((NetClientHandler)var1).addToSendQueue(this);
			SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketRenderDistance((byte)Minecraft.theMinecraft.gameSettings.renderDistance));
			SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketFullVersion(Long.toString(SpoutClient.getClientVersion())));
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
