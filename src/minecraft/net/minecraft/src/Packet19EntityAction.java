package net.minecraft.src;

import java.io.*;

public class Packet19EntityAction extends Packet {
	public int entityId;
	public int state;

	public Packet19EntityAction() {
	}

	public Packet19EntityAction(Entity entity, int i) {
		entityId = entity.entityId;
		state = i;
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		entityId = datainputstream.readInt();
		state = datainputstream.readByte();
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.writeInt(entityId);
		dataoutputstream.writeByte(state);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleEntityAction(this);
	}

	public int getPacketSize() {
		return 5;
	}
}
