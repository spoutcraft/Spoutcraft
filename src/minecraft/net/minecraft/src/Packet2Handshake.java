package net.minecraft.src;

import java.io.*;

public class Packet2Handshake extends Packet {
	public String username;

	public Packet2Handshake() {
	}

	public Packet2Handshake(String s) {
		username = s;
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		username = readString(datainputstream, 32);
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		writeString(username, dataoutputstream);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleHandshake(this);
	}

	public int getPacketSize() {
		return 4 + username.length() + 4;
	}
}
