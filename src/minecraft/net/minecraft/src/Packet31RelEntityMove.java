package net.minecraft.src;

import java.io.*;

public class Packet31RelEntityMove extends Packet30Entity {
	public Packet31RelEntityMove() {
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		super.readPacketData(datainputstream);
		xPosition = datainputstream.readByte();
		yPosition = datainputstream.readByte();
		zPosition = datainputstream.readByte();
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		super.writePacketData(dataoutputstream);
		dataoutputstream.writeByte(xPosition);
		dataoutputstream.writeByte(yPosition);
		dataoutputstream.writeByte(zPosition);
	}

	public int getPacketSize() {
		return 7;
	}
}
