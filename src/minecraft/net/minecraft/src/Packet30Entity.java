package net.minecraft.src;

import java.io.*;

public class Packet30Entity extends Packet {
	public int entityId;
	public byte xPosition;
	public byte yPosition;
	public byte zPosition;
	public byte yaw;
	public byte pitch;
	public boolean rotating;

	public Packet30Entity() {
		rotating = false;
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		entityId = datainputstream.readInt();
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.writeInt(entityId);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleEntity(this);
	}

	public int getPacketSize() {
		return 4;
	}
}
