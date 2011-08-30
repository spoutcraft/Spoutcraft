package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.getspout.spout.item.SpoutItemBlock;

public class PacketCustomItem implements SpoutPacket {
	
	
	private int itemId;
	private Integer blockId;
	private Short metaData;
	
	public PacketCustomItem() {
	}
	
	public PacketCustomItem(int itemId, Integer blockId, Short metaData) {
		this.itemId = itemId;
		this.blockId = blockId;
		this.metaData = metaData;
	}
	
	@Override
	public int getNumBytes() {
		return 10;
	}
	
	@Override
	public void readData(DataInputStream input) throws IOException {
		itemId = input.readInt();
		blockId = input.readInt();
		if (blockId == -1) {
			blockId = null;
		}
		metaData = input.readShort();
		if (metaData == -1) {
			metaData = null;
		}
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(itemId);
		output.writeInt(blockId == null ? -1 : blockId);
		output.writeShort(metaData == null ? -1 : metaData);
	}

	@Override
	public void run(int PlayerId) {
		SpoutItemBlock.addItemInfoMap(itemId, blockId, metaData);
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketCustomItem;
	}
	
	@Override
	public int getVersion() {
		return 3;
	}

	@Override
	public void failure(int playerId) {
		
	}

}
