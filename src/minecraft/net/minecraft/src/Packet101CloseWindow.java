package net.minecraft.src;

import java.io.*;

public class Packet101CloseWindow extends Packet {
	public int windowId;

	public Packet101CloseWindow() {
	}

	public Packet101CloseWindow(int i) {
		windowId = i;
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleCloseWindow(this);
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		windowId = datainputstream.readByte();
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.writeByte(windowId);
	}

	public int getPacketSize() {
		return 1;
	}
}
