package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.getspout.spout.item.ServerCustomBlock;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.material.CustomItem;
import org.spoutcraft.spoutcraftapi.material.Material;
import org.spoutcraft.spoutcraftapi.material.block.GenericCustomBlock;
import org.spoutcraft.spoutcraftapi.material.item.GenericCustomItem;
import org.spoutcraft.spoutcraftapi.packet.PacketUtil;

public class PacketCustomMaterial implements SpoutPacket {
	private Addon addon;
	private String name;
	private int id;
	private int data;
	private byte type;
	private boolean isItem = false;
	private boolean isOpaque = false;

	public PacketCustomMaterial() {

	}

	public int getNumBytes() {
		return PacketUtil.getNumBytes(addon.getDescription().getName()) + PacketUtil.getNumBytes(name);
	}

	public void readData(DataInputStream input) throws IOException {
		addon = Spoutcraft.getAddonManager().getAddon(PacketUtil.readString(input));
		name = PacketUtil.readString(input);
		id = input.readInt();
		data = input.readInt();
		type = input.readByte();
		isItem = type == 2 ? true : false;
		isOpaque = type == 1 ? true : false;
	}

	public void writeData(DataOutputStream output) throws IOException {
		PacketUtil.writeString(output, addon.getDescription().getName());
		PacketUtil.writeString(output, name);
		output.writeInt(id);
		output.writeInt(data);
		output.writeByte(isItem ? 2 : isOpaque ? 1 : 0);
	}

	public void run(int playerId) {
		CustomItem item = new GenericCustomItem(addon, name, id);
		if(!isItem) {
			Material block = new ServerCustomBlock(addon, name, isOpaque, item);
		}
	}

	public void failure(int playerId) {
	}

	public PacketType getPacketType() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

}
