package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.getspout.spout.item.SpoutItemBlock;

public class PacketCustomBlockOverride implements SpoutPacket {
	
	private int x;
	private int y;
	private int z;
	private int blockId;
	private int metaData;
	
	public PacketCustomBlockOverride() {
	}
	
	public PacketCustomBlockOverride(int x, int y, int z, Integer blockId, Integer metaData) {
		this.x = x;
		this.y = y;
		this.z = z;
		setBlockId(blockId);
		setMetaData(metaData);
	}
	
	private void setBlockId(Integer blockId) {
		if (blockId == null) {
			this.blockId = -1;
		} else {
			this.blockId = blockId;
		}
	}
	
	private void setMetaData(Integer metaData) {
		if (metaData == null) {
			this.metaData = 0;
		} else {
			this.metaData = metaData;
		}
	}
	
	private Integer getBlockId() {
		return blockId == -1 ? null : blockId;
	}
	
	private Integer getMetaData() {
		return blockId == -1 ? null : metaData;
	}
	

	public int getNumBytes() {
		return 12;
	}

	public void readData(DataInputStream input) throws IOException {
		x = input.readInt();
		y = input.readByte() & 0xFF;
		z = input.readInt();
		setBlockId((int)input.readShort());
		setMetaData(input.readByte() & 0xFF);
	}

	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(x);
		output.writeByte(y);
		output.writeInt(z);
		output.writeShort(blockId);
		output.writeByte(metaData);
	}
	

	public void run(int PlayerId) {
		SpoutItemBlock.overrideBlock(x, y, z, getBlockId(), getMetaData());
	}


	public PacketType getPacketType() {
		return PacketType.PacketCustomBlockOverride;
	}
	

	public int getVersion() {
		return 0;
	}


	public void failure(int playerId) {
	}

	
}
