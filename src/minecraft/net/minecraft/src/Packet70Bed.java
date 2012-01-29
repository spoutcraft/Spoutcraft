package net.minecraft.src;

import java.io.*;

public class Packet70Bed extends Packet {
	public static final String bedChat[] = {
		"tile.bed.notValid", null, null, "gameMode.changed"
	};
	public int bedState;
	public int gameMode;

	public Packet70Bed() {
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		bedState = datainputstream.readByte();
		gameMode = datainputstream.readByte();
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.writeByte(bedState);
		dataoutputstream.writeByte(gameMode);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleBed(this);
	}

	public int getPacketSize() {
		return 2;
	}
}
