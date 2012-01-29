package net.minecraft.src;

import java.io.*;

public class Packet6SpawnPosition extends Packet {
	public int xPosition;
	public int yPosition;
	public int zPosition;

	public Packet6SpawnPosition() {
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		xPosition = datainputstream.readInt();
		yPosition = datainputstream.readInt();
		zPosition = datainputstream.readInt();
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.writeInt(xPosition);
		dataoutputstream.writeInt(yPosition);
		dataoutputstream.writeInt(zPosition);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleSpawnPosition(this);
	}

	public int getPacketSize() {
		return 12;
	}
}
