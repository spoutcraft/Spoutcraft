package net.minecraft.src;

import java.io.*;

public class Packet41EntityEffect extends Packet {
	public int entityId;
	public byte effectId;
	public byte effectAmp;
	public short duration;

	public Packet41EntityEffect() {
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		entityId = datainputstream.readInt();
		effectId = datainputstream.readByte();
		effectAmp = datainputstream.readByte();
		duration = datainputstream.readShort();
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.writeInt(entityId);
		dataoutputstream.writeByte(effectId);
		dataoutputstream.writeByte(effectAmp);
		dataoutputstream.writeShort(duration);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleEntityEffect(this);
	}

	public int getPacketSize() {
		return 8;
	}
}
