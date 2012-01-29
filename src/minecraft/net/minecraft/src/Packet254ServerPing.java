package net.minecraft.src;

import java.io.*;

public class Packet254ServerPing extends Packet {
	public Packet254ServerPing() {
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleServerPing(this);
	}

	public int getPacketSize() {
		return 0;
	}
}
