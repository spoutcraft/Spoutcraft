package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.getspout.spout.item.SpoutCustomBlockDesign;
import org.getspout.spout.item.SpoutItemBlock;

public class PacketCustomBlockDesign implements SpoutPacket {
	
	private Integer blockId;
	private Integer metaData;

	private SpoutCustomBlockDesign design;
	
	public PacketCustomBlockDesign() {
	}
	
	public PacketCustomBlockDesign(Integer blockId, Integer metaData, SpoutCustomBlockDesign design) {
		this.design = design;
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
		return 8 + design.getNumBytes();
	}
	
	public void readData(DataInputStream input) throws IOException {
		setBlockId(input.readInt());
		setMetaData(input.readInt());
		design = new SpoutCustomBlockDesign();
		design.read(input);
	}
	
	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(blockId);
		output.writeInt(metaData);
		design.write(output);
	}
	
	public void run(int id) {
		if (blockId != null && blockId >= 0) {
			SpoutItemBlock.setCustomBlockDesign(design, blockId, metaData);
		}
	}

	@Override
	public void failure(int id) {
		
	}

	public PacketType getPacketType() {
		return PacketType.PacketCustomBlockDesign;
	}

	@Override
	public int getVersion() {
		return 0;
	}

}

