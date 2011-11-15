package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.getspout.spout.item.ServerCustomBlock;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.material.CustomBlock;
import org.spoutcraft.spoutcraftapi.material.CustomItem;
import org.spoutcraft.spoutcraftapi.material.item.GenericCustomItem;
import org.spoutcraft.spoutcraftapi.packet.PacketUtil;

public class PacketCustomMaterial implements SpoutPacket {
	private Addon addon;
	private String name;
	private int id;
	private byte type;
	private boolean isItem = false;
	private boolean isOpaque = false;
	private float hardness;
	private float friction;
	private int lightLevel;

	public PacketCustomMaterial() {

	}

	public int getNumBytes() {
		return PacketUtil.getNumBytes(addon.getDescription().getName()) + PacketUtil.getNumBytes(name) + 4 + 1 + 1 + 1 + 4 + 4 + 4;
	}

	public void readData(DataInputStream input) throws IOException {
		addon = Spoutcraft.getAddonManager().getOrCreateAddon(PacketUtil.readString(input));
		name = PacketUtil.readString(input);
		id = input.readInt();
		type = input.readByte();
		isItem = type == 2 ? true : false;
		isOpaque = type == 1 ? true : false;
		hardness = input.readFloat();
		friction = input.readFloat();
		lightLevel = input.readInt();
	}

	public void writeData(DataOutputStream output) throws IOException {
		PacketUtil.writeString(output, addon.getDescription().getName());
		PacketUtil.writeString(output, name);
		output.writeInt(id);
		output.writeByte(isItem ? 2 : isOpaque ? 1 : 0);
		output.writeFloat(hardness);
		output.writeFloat(friction);
		output.writeInt(lightLevel);
	}

	public void run(int playerId) {
		CustomItem item = new GenericCustomItem(Spoutcraft.getClient().getMaterialManager(), addon, name, id);
		if(!isItem) {
			CustomBlock block = new ServerCustomBlock(addon, name, isOpaque, item);
			block.setHardness(hardness).setFriction(friction).setLightLevel(lightLevel);
			
		}
	}

	public void failure(int playerId) {
	}

	public PacketType getPacketType() {
		return PacketType.PacketCustomMaterial;
	}

	public int getVersion() {
		return 2;
	}

}
