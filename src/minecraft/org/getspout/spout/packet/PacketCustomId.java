package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.getspout.spout.item.SpoutItem;

public class PacketCustomId implements SpoutPacket {
	
	
	private int itemId;
	private Integer blockId;
	
	public PacketCustomId() {
	}
	
	public PacketCustomId(int itemId, Integer blockId) {
		this.itemId = itemId;
		this.blockId = blockId;
	}
	
	public int getNumBytes() {
		return 8;
	}
	
	public void readData(DataInputStream input) throws IOException {
		itemId = input.readInt();
		blockId = input.readInt();
		if (blockId == -1) {
			blockId = null;
		}
	}

	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(itemId);
		output.writeInt(blockId == null ? -1 : blockId);
	}

	public void run(int PlayerId) {
		SpoutItem.addItemInfoMap(itemId, blockId);
	}

	public PacketType getPacketType() {
		return PacketType.PacketCustomId;
	}
	
	public int getVersion() {
		return 4;
	}

	public void failure(int playerId) {
		
	}
}
