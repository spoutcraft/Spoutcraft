package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.getspout.spout.client.SpoutClient;

public class PacketWorldSeed implements SpoutPacket{
	public long newSeed;
	
	public PacketWorldSeed() {
	}
	
	public PacketWorldSeed(long newSeed) {
		this.newSeed = newSeed;
	}

	@Override
	public int getNumBytes() {
		return 8;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		this.newSeed = input.readLong();
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		output.writeLong(this.newSeed);
	}

	@Override
	public void run(int id) {
		SpoutClient.getHandle().theWorld.getWorldInfo().setNewSeed(newSeed);
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketWorldSeed;
	}
}
