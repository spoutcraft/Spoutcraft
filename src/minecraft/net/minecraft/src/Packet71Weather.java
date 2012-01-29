package net.minecraft.src;

import java.io.*;

public class Packet71Weather extends Packet {
	public int entityID;
	public int posX;
	public int posY;
	public int posZ;
	public int isLightningBolt;

	public Packet71Weather() {
	}

	public Packet71Weather(Entity entity) {
		entityID = entity.entityId;
		posX = MathHelper.floor_double(entity.posX * 32D);
		posY = MathHelper.floor_double(entity.posY * 32D);
		posZ = MathHelper.floor_double(entity.posZ * 32D);
		if (entity instanceof EntityLightningBolt) {
			isLightningBolt = 1;
		}
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		entityID = datainputstream.readInt();
		isLightningBolt = datainputstream.readByte();
		posX = datainputstream.readInt();
		posY = datainputstream.readInt();
		posZ = datainputstream.readInt();
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.writeInt(entityID);
		dataoutputstream.writeByte(isLightningBolt);
		dataoutputstream.writeInt(posX);
		dataoutputstream.writeInt(posY);
		dataoutputstream.writeInt(posZ);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleWeather(this);
	}

	public int getPacketSize() {
		return 17;
	}
}
