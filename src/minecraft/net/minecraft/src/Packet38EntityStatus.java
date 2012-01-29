package net.minecraft.src;

import java.io.*;

public class Packet38EntityStatus extends Packet {
	public int entityId;
	public byte entityStatus;

	public Packet38EntityStatus() {
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		entityId = datainputstream.readInt();
		entityStatus = datainputstream.readByte();
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.writeInt(entityId);
		dataoutputstream.writeByte(entityStatus);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleEntityStatus(this);
	}

	public int getPacketSize() {
		return 5;
	}
}
