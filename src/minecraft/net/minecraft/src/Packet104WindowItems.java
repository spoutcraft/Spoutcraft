package net.minecraft.src;

import java.io.*;

public class Packet104WindowItems extends Packet {
	public int windowId;
	public ItemStack itemStack[];

	public Packet104WindowItems() {
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		windowId = datainputstream.readByte();
		short word0 = datainputstream.readShort();
		itemStack = new ItemStack[word0];
		for (int i = 0; i < word0; i++) {
			itemStack[i] = func_40187_b(datainputstream);
		}
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.writeByte(windowId);
		dataoutputstream.writeShort(itemStack.length);
		for (int i = 0; i < itemStack.length; i++) {
			writeItemStack(itemStack[i], dataoutputstream);
		}
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleWindowItems(this);
	}

	public int getPacketSize() {
		return 3 + itemStack.length * 5;
	}
}
