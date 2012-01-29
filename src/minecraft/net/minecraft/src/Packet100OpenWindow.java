package net.minecraft.src;

import java.io.*;

public class Packet100OpenWindow extends Packet {
	public int windowId;
	public int inventoryType;
	public String windowTitle;
	public int slotsCount;

	public Packet100OpenWindow() {
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleOpenWindow(this);
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		windowId = datainputstream.readByte() & 0xff;
		inventoryType = datainputstream.readByte() & 0xff;
		windowTitle = readString(datainputstream, 16);
		slotsCount = datainputstream.readByte() & 0xff;
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.writeByte(windowId & 0xff);
		dataoutputstream.writeByte(inventoryType & 0xff);
		writeString(windowTitle, dataoutputstream);
		dataoutputstream.writeByte(slotsCount & 0xff);
	}

	public int getPacketSize() {
		return 3 + windowTitle.length();
	}
}
