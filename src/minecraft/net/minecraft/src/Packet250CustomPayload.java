package net.minecraft.src;

import java.io.*;

public class Packet250CustomPayload extends Packet {
	public String field_44012_a;
	public int field_44010_b;
	public byte field_44011_c[];

	public Packet250CustomPayload() {
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		field_44012_a = readString(datainputstream, 16);
		field_44010_b = datainputstream.readShort();
		if (field_44010_b > 0 && field_44010_b < 32767) {
			field_44011_c = new byte[field_44010_b];
			datainputstream.read(field_44011_c);
		}
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		writeString(field_44012_a, dataoutputstream);
		dataoutputstream.writeShort((short)field_44010_b);
		if (field_44011_c != null) {
			dataoutputstream.write(field_44011_c);
		}
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.func_44028_a(this);
	}

	public int getPacketSize() {
		return 2 + field_44012_a.length() * 2 + 2 + field_44010_b;
	}
}
