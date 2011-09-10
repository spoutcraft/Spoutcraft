package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.WorldClient;

public class PacketUniqueId implements SpoutPacket{
	private long lsb;
	private long msb;
	private int entityId;
	
	public PacketUniqueId() {
		
	}
	
	public PacketUniqueId(UUID id, int entityId) {
		lsb = id.getLeastSignificantBits();
		msb = id.getMostSignificantBits();
		this.entityId = entityId;
	}

	public int getNumBytes() {
		return 20;
	}

	public void readData(DataInputStream input) throws IOException {
		lsb = input.readLong();
		msb = input.readLong();
		entityId = input.readInt();
	}

	public void writeData(DataOutputStream output) throws IOException {
		output.writeLong(lsb);
		output.writeLong(msb);
		output.writeInt(entityId);
	}

	public void run(int playerId) {
		if (Minecraft.theMinecraft.theWorld instanceof WorldClient) {
			Entity e = null;
			if (Minecraft.theMinecraft.thePlayer != null && entityId == Minecraft.theMinecraft.thePlayer.entityId) {
				e = Minecraft.theMinecraft.thePlayer;
			}
			else {
				e = ((WorldClient)Minecraft.theMinecraft.theWorld).func_709_b(entityId);
			}
			UUID current = new UUID(msb, lsb);
			e.uniqueId = current;
		}
	}

	public void failure(int playerId) {

	}

	public PacketType getPacketType() {
		return PacketType.PacketUniqueId;
	}

	public int getVersion() {
		return 0;
	}

}
