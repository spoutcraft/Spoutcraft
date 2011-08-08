package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.getspout.spout.client.SpoutClient;

public class PacketPluginReload implements SpoutPacket{
	public int activeInventoryX;
	public int activeInventoryY;
	public int activeInventoryZ;
	public String worldName;
	
	public PacketPluginReload() {
		
	}

	@Override
	public int getNumBytes() {
		return 12 + PacketUtil.getNumBytes(worldName);
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		activeInventoryX = input.readInt();
		activeInventoryY = input.readInt();
		activeInventoryZ = input.readInt();
		worldName = PacketUtil.readString(input, 64);
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(activeInventoryX);
		output.writeInt(activeInventoryY);
		output.writeInt(activeInventoryZ);
		PacketUtil.writeString(output, worldName);
	}

	@Override
	public void run(int playerId) {
		SpoutClient.setReloadPacket(this);
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketPluginReload;
	}

}
