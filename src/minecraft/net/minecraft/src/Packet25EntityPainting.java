package net.minecraft.src;

import java.io.*;

public class Packet25EntityPainting extends Packet {
	public int entityId;
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public int direction;
	public String title;

	public Packet25EntityPainting() {
	}

	public Packet25EntityPainting(EntityPainting entitypainting) {
		entityId = entitypainting.entityId;
		xPosition = entitypainting.xPosition;
		yPosition = entitypainting.yPosition;
		zPosition = entitypainting.zPosition;
		direction = entitypainting.direction;
		title = entitypainting.art.title;
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		entityId = datainputstream.readInt();
		title = readString(datainputstream, EnumArt.maxArtTitleLength);
		xPosition = datainputstream.readInt();
		yPosition = datainputstream.readInt();
		zPosition = datainputstream.readInt();
		direction = datainputstream.readInt();
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.writeInt(entityId);
		writeString(title, dataoutputstream);
		dataoutputstream.writeInt(xPosition);
		dataoutputstream.writeInt(yPosition);
		dataoutputstream.writeInt(zPosition);
		dataoutputstream.writeInt(direction);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleEntityPainting(this);
	}

	public int getPacketSize() {
		return 24;
	}
}
