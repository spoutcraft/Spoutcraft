package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.getspout.spout.client.SpoutClient;

public class PacketItemName implements SpoutPacket{
	private int id;
	private short data;
	private String name;
	public PacketItemName() {
		
	}
	
	public PacketItemName(int id, short data, String name) {
		this.id = id;
		this.data = data;
		this.name = name;
	}

	@Override
	public int getNumBytes() {
		return 6 + PacketUtil.getNumBytes(name);
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		id = input.readInt();
		data = input.readShort();
		name = PacketUtil.readString(input);
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(id);
		output.writeShort(data);
		PacketUtil.writeString(output, name);
	}

	@Override
	public void run(int PlayerId) {
		if (name != null) {
			if (name.equals("[resetall]")) {
				SpoutClient.getInstance().getItemManager().reset();
			}
			else if (name.equals("[reset]")) {
				SpoutClient.getInstance().getItemManager().resetName(id, data);
			}
			else {
				SpoutClient.getInstance().getItemManager().setItemName(id, data, name);
			}
		}
	}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketItemName;
	}

}
