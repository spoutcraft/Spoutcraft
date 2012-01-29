package net.minecraft.src;

import java.io.*;

public class Packet102WindowClick extends Packet {
	public int window_Id;
	public int inventorySlot;
	public int mouseClick;
	public short action;
	public ItemStack itemStack;
	public boolean holdingShift;

	public Packet102WindowClick() {
	}

	public Packet102WindowClick(int i, int j, int k, boolean flag, ItemStack itemstack, short word0) {
		window_Id = i;
		inventorySlot = j;
		mouseClick = k;
		itemStack = itemstack;
		action = word0;
		holdingShift = flag;
	}

	public void processPacket(NetHandler nethandler) {
		nethandler.handleWindowClick(this);
	}

	public void readPacketData(DataInputStream datainputstream)
	throws IOException {
		window_Id = datainputstream.readByte();
		inventorySlot = datainputstream.readShort();
		mouseClick = datainputstream.readByte();
		action = datainputstream.readShort();
		holdingShift = datainputstream.readBoolean();
		itemStack = func_40187_b(datainputstream);
	}

	public void writePacketData(DataOutputStream dataoutputstream)
	throws IOException {
		dataoutputstream.writeByte(window_Id);
		dataoutputstream.writeShort(inventorySlot);
		dataoutputstream.writeByte(mouseClick);
		dataoutputstream.writeShort(action);
		dataoutputstream.writeBoolean(holdingShift);
		writeItemStack(itemStack, dataoutputstream);
	}

	public int getPacketSize() {
		return 11;
	}
}
