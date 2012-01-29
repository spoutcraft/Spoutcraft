package net.minecraft.src;

import java.io.*;

public class Packet5PlayerInventory extends Packet {
	public int entityID;
	public int slot;
	public int itemID;
	public int itemDamage;

	public Packet5PlayerInventory() {
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		entityID = datainputstream.readInt();
		slot = datainputstream.readShort();
		itemID = datainputstream.readShort();
		itemDamage = datainputstream.readShort();
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.writeInt(entityID);
		dataoutputstream.writeShort(slot);
		dataoutputstream.writeShort(itemID);
		dataoutputstream.writeShort(itemDamage);
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handlePlayerInventory(this);
	}

	public int getPacketSize() {
		return 8;
	}
}
