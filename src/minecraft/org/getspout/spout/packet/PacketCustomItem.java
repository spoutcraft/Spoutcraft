package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.getspout.spout.item.SpoutItemBlock;

public class PacketCustomItem implements SpoutPacket {
	
	
	private int itemId;
	private Integer blockId;
	
	public PacketCustomItem() {
	}
	
	public PacketCustomItem(int itemId, Integer blockId) {
		this.itemId = itemId;
		this.blockId = blockId;
	}
	
	@Override
	public int getNumBytes() {
		return 8;
	}
	
	@Override
	public void readData(DataInputStream input) throws IOException {
		itemId = input.readInt();
		blockId = input.readInt();
		if (blockId == -1) {
			blockId = null;
		}
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(itemId);
		output.writeInt(blockId == null ? -1 : blockId);
	}

	@Override
	public void run(int PlayerId) {
		SpoutItemBlock.addItemInfoMap(itemId, blockId);
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketCustomItem;
	}
	
	@Override
	public int getVersion() {
		return 2;
	}

	@Override
	public void failure(int playerId) {
		
	}

}
