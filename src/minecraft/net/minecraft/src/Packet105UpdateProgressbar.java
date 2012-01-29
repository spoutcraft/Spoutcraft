package net.minecraft.src;

import java.io.*;

public class Packet105UpdateProgressbar extends Packet {
	public int windowId;
	public int progressBar;
	public int progressBarValue;

	public Packet105UpdateProgressbar() {
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleUpdateProgressbar(this);
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		windowId = datainputstream.readByte();
		progressBar = datainputstream.readShort();
		progressBarValue = datainputstream.readShort();
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.writeByte(windowId);
		dataoutputstream.writeShort(progressBar);
		dataoutputstream.writeShort(progressBarValue);
	}

	public int getPacketSize() {
		return 5;
	}
}
