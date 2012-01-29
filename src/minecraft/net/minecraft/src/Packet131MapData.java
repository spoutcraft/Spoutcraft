package net.minecraft.src;

import java.io.*;

public class Packet131MapData extends Packet {
	public short itemID;
	public short uniqueID;
	public byte itemData[];

	public Packet131MapData() {
		isChunkDataPacket = true;
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		itemID = datainputstream.readShort();
		uniqueID = datainputstream.readShort();
		itemData = new byte[datainputstream.readByte() & 0xff];
		datainputstream.readFully(itemData);
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.writeShort(itemID);
		dataoutputstream.writeShort(uniqueID);
		dataoutputstream.writeByte(itemData.length);
		dataoutputstream.write(itemData);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleItemData(this);
	}

	public int getPacketSize() {
		return 4 + itemData.length;
	}
}
