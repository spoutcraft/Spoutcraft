package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import net.minecraft.src.Entity;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;

//Spout Start
import net.minecraft.client.Minecraft;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.packet.PacketFullVersion;
import org.spoutcraft.client.packet.PacketRenderDistance;

public class Packet18Animation extends Packet {

	public int entityId;
	public int animate;

	public Packet18Animation() {}

	public Packet18Animation(Entity par1Entity, int par2) {
		this.entityId = par1Entity.entityId;
		this.animate = par2;
	}

	public void readPacketData(DataInputStream par1DataInputStream) throws IOException {
		this.entityId = par1DataInputStream.readInt();
		this.animate = par1DataInputStream.readByte();
	}

	public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException {
		par1DataOutputStream.writeInt(this.entityId);
		par1DataOutputStream.writeByte(this.animate);
	}

	public void processPacket(NetHandler par1NetHandler) {
		//Spout Start
		if (entityId == -42) {
			SpoutClient.getInstance().setSpoutVersion(1);
			((NetClientHandler) par1NetHandler).addToSendQueue(this);
			SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketRenderDistance((byte)Minecraft.theMinecraft.gameSettings.renderDistance));
			SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketFullVersion(Long.toString(SpoutClient.getClientVersion())));
			System.out.println("Detected Spout server.");
		} else {
			par1NetHandler.handleAnimation(this);
		}
		//Spout End
	}

	public int getPacketSize() {
		return 5;
	}
}
