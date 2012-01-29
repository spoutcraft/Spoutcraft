package net.minecraft.src;

import java.io.*;

public class Packet255KickDisconnect extends Packet {
	public String reason;

	public Packet255KickDisconnect() {
	}

	public Packet255KickDisconnect(String s) {
		reason = s;
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		reason = readString(datainputstream, 256);
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		writeString(reason, dataoutputstream);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleKickDisconnect(this);
	}

	public int getPacketSize() {
		return reason.length();
	}
}
