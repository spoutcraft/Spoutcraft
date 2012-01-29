package net.minecraft.src;

import java.io.*;

public class Packet7UseEntity extends Packet {
	public int playerEntityId;
	public int targetEntity;
	public int isLeftClick;

	public Packet7UseEntity() {
	}

	public Packet7UseEntity(int i, int j, int k) {
		playerEntityId = i;
		targetEntity = j;
		isLeftClick = k;
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		playerEntityId = datainputstream.readInt();
		targetEntity = datainputstream.readInt();
		isLeftClick = datainputstream.readByte();
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.writeInt(playerEntityId);
		dataoutputstream.writeInt(targetEntity);
		dataoutputstream.writeByte(isLeftClick);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleUseEntity(this);
	}

	public int getPacketSize() {
		return 9;
	}
}
