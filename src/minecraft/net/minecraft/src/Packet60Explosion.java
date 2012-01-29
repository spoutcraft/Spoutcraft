package net.minecraft.src;

import java.io.*;
import java.util.*;

public class Packet60Explosion extends Packet {
	public double explosionX;
	public double explosionY;
	public double explosionZ;
	public float explosionSize;
	public Set destroyedBlockPositions;

	public Packet60Explosion() {
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		explosionX = datainputstream.readDouble();
		explosionY = datainputstream.readDouble();
		explosionZ = datainputstream.readDouble();
		explosionSize = datainputstream.readFloat();
		int i = datainputstream.readInt();
		destroyedBlockPositions = new HashSet();
		int j = (int)explosionX;
		int k = (int)explosionY;
		int l = (int)explosionZ;
		for (int i1 = 0; i1 < i; i1++) {
			int j1 = datainputstream.readByte() + j;
			int k1 = datainputstream.readByte() + k;
			int l1 = datainputstream.readByte() + l;
			destroyedBlockPositions.add(new ChunkPosition(j1, k1, l1));
		}
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.writeDouble(explosionX);
		dataoutputstream.writeDouble(explosionY);
		dataoutputstream.writeDouble(explosionZ);
		dataoutputstream.writeFloat(explosionSize);
		dataoutputstream.writeInt(destroyedBlockPositions.size());
		int i = (int)explosionX;
		int j = (int)explosionY;
		int k = (int)explosionZ;
		int j1;
		for (Iterator iterator = destroyedBlockPositions.iterator(); iterator.hasNext(); dataoutputstream.writeByte(j1)) {
			ChunkPosition chunkposition = (ChunkPosition)iterator.next();
			int l = chunkposition.x - i;
			int i1 = chunkposition.y - j;
			j1 = chunkposition.z - k;
			dataoutputstream.writeByte(l);
			dataoutputstream.writeByte(i1);
		}
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleExplosion(this);
	}

	public int getPacketSize() {
		return 32 + destroyedBlockPositions.size() * 3;
	}
}
