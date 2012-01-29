package net.minecraft.src;

import java.io.*;

public class Packet29DestroyEntity extends Packet {
	public int entityId;

	public Packet29DestroyEntity() {
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
		nethandler.handleDestroyEntity(this);
	}

	public int getPacketSize() {
		return 4;
	}
}
