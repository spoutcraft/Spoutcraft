package net.minecraft.src;

import java.io.*;

public class Packet17Sleep extends Packet {
	public int entityID;
	public int bedX;
	public int bedY;
	public int bedZ;
	public int field_22046_e;

	public Packet17Sleep() {
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		entityID = datainputstream.readInt();
		field_22046_e = datainputstream.readByte();
		bedX = datainputstream.readInt();
		bedY = datainputstream.readByte();
		bedZ = datainputstream.readInt();
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.writeInt(entityID);
		dataoutputstream.writeByte(field_22046_e);
		dataoutputstream.writeInt(bedX);
		dataoutputstream.writeByte(bedY);
		dataoutputstream.writeInt(bedZ);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleSleep(this);
	}

	public int getPacketSize() {
		return 14;
	}
}
