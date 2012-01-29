package net.minecraft.src;

import java.io.*;

public class Packet28EntityVelocity extends Packet {
	public int entityId;
	public int motionX;
	public int motionY;
	public int motionZ;

	public Packet28EntityVelocity() {
	}

	public Packet28EntityVelocity(Entity entity) {
		this(entity.entityId, entity.motionX, entity.motionY, entity.motionZ);
	}

	public Packet28EntityVelocity(int i, double d, double d1, double d2) {
		entityId = i;
		double d3 = 3.8999999999999999D;
		if (d < -d3) {
			d = -d3;
		}
		if (d1 < -d3) {
			d1 = -d3;
		}
		if (d2 < -d3) {
			d2 = -d3;
		}
		if (d > d3) {
			d = d3;
		}
		if (d1 > d3) {
			d1 = d3;
		}
		if (d2 > d3) {
			d2 = d3;
		}
		motionX = (int)(d * 8000D);
		motionY = (int)(d1 * 8000D);
		motionZ = (int)(d2 * 8000D);
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		entityId = datainputstream.readInt();
		motionX = datainputstream.readShort();
		motionY = datainputstream.readShort();
		motionZ = datainputstream.readShort();
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.writeInt(entityId);
		dataoutputstream.writeShort(motionX);
		dataoutputstream.writeShort(motionY);
		dataoutputstream.writeShort(motionZ);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleEntityVelocity(this);
	}

	public int getPacketSize() {
		return 10;
	}
}
