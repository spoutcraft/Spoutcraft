package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.getspout.spout.client.SpoutClient;

public class PacketAllowVisualCheats implements SpoutPacket{
	private boolean cheating = false;
	public PacketAllowVisualCheats() {

	}
	
	public PacketAllowVisualCheats(boolean allow) {
		this.cheating = allow;
	}

	@Override
	public int getNumBytes() {
		return 1;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		cheating = input.readBoolean();
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		output.writeBoolean(cheating);
	}

	@Override
	public void run(int playerId) {
		System.out.println("Server Cheat Mode: " + cheating);
		SpoutClient.getInstance().setCheatMode(cheating);
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketAllowVisualCheats;
	}

}
