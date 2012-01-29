package net.minecraft.src;

import java.io.*;

public class Packet3Chat extends Packet {
	public String message;

	public Packet3Chat() {
	}

	public Packet3Chat(String s) {
		if (s.length() > 119) {
			s = s.substring(0, 119);
		}
		message = s;
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		message = readString(datainputstream, 119);
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		writeString(message, dataoutputstream);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleChat(this);
	}

	public int getPacketSize() {
		return 2 + message.length() * 2;
	}
}
