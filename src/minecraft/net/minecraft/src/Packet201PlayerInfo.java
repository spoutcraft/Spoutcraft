package net.minecraft.src;

import java.io.*;

public class Packet201PlayerInfo extends Packet {
	public String playerName;
	public boolean isConnected;
	public int ping;

	public Packet201PlayerInfo() {
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		playerName = readString(datainputstream, 16);
		isConnected = datainputstream.readByte() != 0;
		ping = datainputstream.readShort();
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		writeString(playerName, dataoutputstream);
		dataoutputstream.writeByte(isConnected ? 1 : 0);
		dataoutputstream.writeShort(ping);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handlePlayerInfo(this);
	}

	public int getPacketSize() {
		return playerName.length() + 2 + 1 + 2;
	}
}
