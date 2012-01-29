package net.minecraft.src;

import java.io.*;

public class Packet23VehicleSpawn extends Packet {
	public int entityId;
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public int speedX;
	public int speedY;
	public int speedZ;
	public int type;
	public int throwerEntityId;

	public Packet23VehicleSpawn() {
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		entityId = datainputstream.readInt();
		type = datainputstream.readByte();
		xPosition = datainputstream.readInt();
		yPosition = datainputstream.readInt();
		zPosition = datainputstream.readInt();
		throwerEntityId = datainputstream.readInt();
		if (throwerEntityId > 0) {
			speedX = datainputstream.readShort();
			speedY = datainputstream.readShort();
			speedZ = datainputstream.readShort();
		}
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.writeInt(entityId);
		dataoutputstream.writeByte(type);
		dataoutputstream.writeInt(xPosition);
		dataoutputstream.writeInt(yPosition);
		dataoutputstream.writeInt(zPosition);
		dataoutputstream.writeInt(throwerEntityId);
		if (throwerEntityId > 0) {
			dataoutputstream.writeShort(speedX);
			dataoutputstream.writeShort(speedY);
			dataoutputstream.writeShort(speedZ);
		}
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleVehicleSpawn(this);
	}

	public int getPacketSize() {
		return 21 + throwerEntityId <= 0 ? 0 : 6;
	}
}
