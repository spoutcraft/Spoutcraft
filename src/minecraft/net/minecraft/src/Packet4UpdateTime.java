package net.minecraft.src;

import java.io.*;

public class Packet4UpdateTime extends Packet {
	public long time;

	public Packet4UpdateTime() {
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		time = datainputstream.readLong();
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.writeLong(time);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleUpdateTime(this);
	}

	public int getPacketSize() {
		return 8;
	}
}
