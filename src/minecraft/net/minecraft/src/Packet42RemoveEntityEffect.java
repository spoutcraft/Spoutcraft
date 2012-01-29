package net.minecraft.src;

import java.io.*;

public class Packet42RemoveEntityEffect extends Packet {
	public int entityId;
	public byte effectId;

	public Packet42RemoveEntityEffect() {
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		entityId = datainputstream.readInt();
		effectId = datainputstream.readByte();
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.writeInt(entityId);
		dataoutputstream.writeByte(effectId);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleRemoveEntityEffect(this);
	}

	public int getPacketSize() {
		return 5;
	}
}
