package net.minecraft.src;

import java.io.*;

public class Packet103SetSlot extends Packet {
	public int windowId;
	public int itemSlot;
	public ItemStack myItemStack;

	public Packet103SetSlot() {
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleSetSlot(this);
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		windowId = datainputstream.readByte();
		itemSlot = datainputstream.readShort();
		myItemStack = func_40187_b(datainputstream);
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.writeByte(windowId);
		dataoutputstream.writeShort(itemSlot);
		writeItemStack(myItemStack, dataoutputstream);
	}

	public int getPacketSize() {
		return 8;
	}
}
