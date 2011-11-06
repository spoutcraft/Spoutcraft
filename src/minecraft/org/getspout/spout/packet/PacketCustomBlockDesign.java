package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.getspout.spout.item.ServerCustomBlock;
import org.getspout.spout.item.SpoutCustomBlockDesign;
import org.getspout.spout.item.SpoutItem;
import org.spoutcraft.spoutcraftapi.block.design.GenericBlockDesign;
import org.spoutcraft.spoutcraftapi.material.MaterialData;
import org.spoutcraft.spoutcraftapi.packet.PacketUtil;

public class PacketCustomBlockDesign implements SpoutPacket {
	
	private Integer blockId;
	private Integer metaData;
	private String name;
	private boolean opaque;
	private GenericBlockDesign design;
	
	public PacketCustomBlockDesign() {
		
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
	
	@SuppressWarnings("unused")
	private Integer getBlockId() {
		return blockId == -1 ? null : blockId;
	}
	
	@SuppressWarnings("unused")
	private Integer getMetaData() {
		return blockId == -1 ? null : metaData;
	}
	
	public int getNumBytes() {
		int designBytes = (design == null) ?  (new GenericBlockDesign().getResetNumBytes()) : design.getNumBytes();
		return 8 + designBytes + PacketUtil.getNumBytes(name) + 1;
	}
	
	public void readData(DataInputStream input) throws IOException {
		setBlockId(input.readInt());
		setMetaData(input.readInt());
		name = PacketUtil.readString(input);
		opaque = input.readBoolean();
		design = new GenericBlockDesign();
		design.read(input);
		if (design.getReset()) {
			design = null;
		}
	}
	
	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(blockId == null ? -1 : blockId);
		output.writeInt(metaData == null ? 0 : metaData);
		PacketUtil.writeString(output, name);
		output.writeBoolean(opaque);
		if (design != null) {
			design.write(output);
		} else {
			new GenericBlockDesign().writeReset(output);
		}
	}
	
	public void run(int id) {
		if (MaterialData.getBlock(blockId, metaData.shortValue()) != null) {
			MaterialData.getBlock(blockId, metaData.shortValue()).setBlockDesign(design);
		}
	}


	public void failure(int id) {
		
	}

	public PacketType getPacketType() {
		return PacketType.PacketCustomBlockDesign;
	}


	public int getVersion() {
		return new GenericBlockDesign().getVersion() + 1;
	}

}

