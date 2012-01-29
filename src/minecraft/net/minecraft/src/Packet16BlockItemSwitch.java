package net.minecraft.src;

import java.io.*;

public class Packet16BlockItemSwitch extends Packet {
	public int id;

	public Packet16BlockItemSwitch() {
	}

	public Packet16BlockItemSwitch(int i) {
		id = i;
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		id = datainputstream.readShort();
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.writeShort(id);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleBlockItemSwitch(this);
	}

	public int getPacketSize() {
		return 2;
	}
}
