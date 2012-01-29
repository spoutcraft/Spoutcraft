package net.minecraft.src;

import java.io.*;

public class Packet52MultiBlockChange extends Packet {
	public int xPosition;
	public int zPosition;
	public short coordinateArray[];
	public byte typeArray[];
	public byte metadataArray[];
	public int size;

	public Packet52MultiBlockChange() {
		isChunkDataPacket = true;
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		xPosition = datainputstream.readInt();
		zPosition = datainputstream.readInt();
		size = datainputstream.readShort() & 0xffff;
		coordinateArray = new short[size];
		typeArray = new byte[size];
		metadataArray = new byte[size];
		for (int i = 0; i < size; i++) {
			coordinateArray[i] = datainputstream.readShort();
		}

		datainputstream.readFully(typeArray);
		datainputstream.readFully(metadataArray);
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.writeInt(xPosition);
		dataoutputstream.writeInt(zPosition);
		dataoutputstream.writeShort((short)size);
		for (int i = 0; i < size; i++) {
			dataoutputstream.writeShort(coordinateArray[i]);
		}

		dataoutputstream.write(typeArray);
		dataoutputstream.write(metadataArray);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleMultiBlockChange(this);
	}

	public int getPacketSize() {
		return 10 + size * 4;
	}
}
