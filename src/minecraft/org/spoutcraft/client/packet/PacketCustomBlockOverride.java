package org.spoutcraft.client.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.spoutcraft.spoutcraftapi.Spoutcraft;

public class PacketCustomBlockOverride implements SpoutPacket {
	
	private int x;
	private short y;
	private int z;
	private short blockId;
	
	public PacketCustomBlockOverride() {
	}
	
	public PacketCustomBlockOverride(int x, int y, int z, Integer blockId) {
		this.x = x;
		this.y = (short) (y & 0xFFFF);
		this.z = z;
		setBlockId(blockId);
	}
	
	private void setBlockId(Integer blockId) {
		if (blockId == null) {
			this.blockId = -1;
		} else {
			this.blockId = blockId.shortValue();
		}
	}
	
	protected Integer getBlockId() {
		return blockId == -1 ? null : Integer.valueOf(blockId);
	}

	public int getNumBytes() {
		return 4 + 4 + 2 + 4;
	}

	public void readData(DataInputStream input) throws IOException {
		x = input.readInt();
		y = input.readShort();
		z = input.readInt();
		setBlockId((int)input.readShort());
	}
	
	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(x);
		output.writeShort(y);
		output.writeInt(z);
		output.writeShort(blockId);
	}
	

	public void run(int PlayerId) {
		Spoutcraft.getWorld().getChunkAt(x, y, z).setCustomBlockId(x, y, z, blockId);
	}

	public PacketType getPacketType() {
		return PacketType.PacketCustomBlockOverride;
	}
	
	public int getVersion() {
		return 2;
	}

	public void failure(int playerId) {
	}
}
