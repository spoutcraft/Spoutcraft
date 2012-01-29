package net.minecraft.src;

import java.io.*;

public class Packet39AttachEntity extends Packet {
	public int entityId;
	public int vehicleEntityId;

	public Packet39AttachEntity() {
	}

	public int getPacketSize() {
		return 8;
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		entityId = datainputstream.readInt();
		vehicleEntityId = datainputstream.readInt();
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.writeInt(entityId);
		dataoutputstream.writeInt(vehicleEntityId);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleAttachEntity(this);
	}
}
