package net.minecraft.src;

import java.io.*;

public class Packet106Transaction extends Packet {
	public int windowId;
	public short shortWindowId;
	public boolean accepted;

	public Packet106Transaction() {
	}

	public Packet106Transaction(int i, short word0, boolean flag) {
		windowId = i;
		shortWindowId = word0;
		accepted = flag;
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleContainerTransaction(this);
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		windowId = datainputstream.readByte();
		shortWindowId = datainputstream.readShort();
		accepted = datainputstream.readByte() != 0;
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.writeByte(windowId);
		dataoutputstream.writeShort(shortWindowId);
		dataoutputstream.writeByte(accepted ? 1 : 0);
	}

	public int getPacketSize() {
		return 4;
	}
}
