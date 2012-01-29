package net.minecraft.src;

import java.io.*;

public class Packet14BlockDig extends Packet {
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public int face;
	public int status;

	public Packet14BlockDig() {
	}

	public Packet14BlockDig(int i, int j, int k, int l, int i1) {
		status = i;
		xPosition = j;
		yPosition = k;
		zPosition = l;
		face = i1;
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		status = datainputstream.read();
		xPosition = datainputstream.readInt();
		yPosition = datainputstream.read();
		zPosition = datainputstream.readInt();
		face = datainputstream.read();
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.write(status);
		dataoutputstream.writeInt(xPosition);
		dataoutputstream.write(yPosition);
		dataoutputstream.writeInt(zPosition);
		dataoutputstream.write(face);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleBlockDig(this);
	}

	public int getPacketSize() {
		return 11;
	}
}
