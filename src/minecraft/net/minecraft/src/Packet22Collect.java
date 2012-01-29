package net.minecraft.src;

import java.io.*;

public class Packet22Collect extends Packet {
	public int collectedEntityId;
	public int collectorEntityId;

	public Packet22Collect() {
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		collectedEntityId = datainputstream.readInt();
		collectorEntityId = datainputstream.readInt();
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.writeInt(collectedEntityId);
		dataoutputstream.writeInt(collectorEntityId);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleCollect(this);
	}

	public int getPacketSize() {
		return 8;
	}
}
