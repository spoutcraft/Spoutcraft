package net.minecraft.src;

import java.io.*;

public class Packet8UpdateHealth extends Packet {
	public int healthMP;
	public int food;
	public float foodSaturation;

	public Packet8UpdateHealth() {
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		healthMP = datainputstream.readShort();
		food = datainputstream.readShort();
		foodSaturation = datainputstream.readFloat();
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.writeShort(healthMP);
		dataoutputstream.writeShort(food);
		dataoutputstream.writeFloat(foodSaturation);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleHealth(this);
	}

	public int getPacketSize() {
		return 8;
	}
}
