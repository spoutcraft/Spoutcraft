package net.minecraft.src;

import java.io.*;
import java.util.List;

public class Packet24MobSpawn extends Packet {
	public int entityId;
	public int type;
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public byte yaw;
	public byte pitch;
	private DataWatcher metaData;
	private List receivedMetadata;

	public Packet24MobSpawn() {
	}

	public Packet24MobSpawn(EntityLiving entityliving) {
		entityId = entityliving.entityId;
		type = (byte)EntityList.getEntityID(entityliving);
		xPosition = MathHelper.floor_double(entityliving.posX * 32D);
		yPosition = MathHelper.floor_double(entityliving.posY * 32D);
		zPosition = MathHelper.floor_double(entityliving.posZ * 32D);
		yaw = (byte)(int)((entityliving.rotationYaw * 256F) / 360F);
		pitch = (byte)(int)((entityliving.rotationPitch * 256F) / 360F);
		metaData = entityliving.getDataWatcher();
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		entityId = datainputstream.readInt();
		type = datainputstream.readByte() & 0xff;
		xPosition = datainputstream.readInt();
		yPosition = datainputstream.readInt();
		zPosition = datainputstream.readInt();
		yaw = datainputstream.readByte();
		pitch = datainputstream.readByte();
		receivedMetadata = DataWatcher.readWatchableObjects(datainputstream);
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.writeInt(entityId);
		dataoutputstream.writeByte(type & 0xff);
		dataoutputstream.writeInt(xPosition);
		dataoutputstream.writeInt(yPosition);
		dataoutputstream.writeInt(zPosition);
		dataoutputstream.writeByte(yaw);
		dataoutputstream.writeByte(pitch);
		metaData.writeWatchableObjects(dataoutputstream);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleMobSpawn(this);
	}

	public int getPacketSize() {
		return 20;
	}

	public List getMetadata() {
		return receivedMetadata;
	}
}
