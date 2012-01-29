package net.minecraft.src;

import java.io.*;

public class Packet61DoorChange extends Packet {
	public int sfxID;
	public int auxData;
	public int posX;
	public int posY;
	public int posZ;

	public Packet61DoorChange() {
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		sfxID = datainputstream.readInt();
		posX = datainputstream.readInt();
		posY = datainputstream.readByte();
		posZ = datainputstream.readInt();
		auxData = datainputstream.readInt();
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.writeInt(sfxID);
		dataoutputstream.writeInt(posX);
		dataoutputstream.writeByte(posY);
		dataoutputstream.writeInt(posZ);
		dataoutputstream.writeInt(auxData);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleDoorChange(this);
	}

	public int getPacketSize() {
		return 20;
	}
}
