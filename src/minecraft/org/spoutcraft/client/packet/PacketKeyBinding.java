package org.spoutcraft.client.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.spoutcraft.client.client.SpoutClient;
import org.spoutcraft.spoutcraftapi.keyboard.KeyBinding;
import org.spoutcraft.spoutcraftapi.packet.PacketUtil;

public class PacketKeyBinding implements SpoutPacket {

	private String id;
	private String plugin;
	private String description;
	private int key;
	private boolean pressed;
	private UUID uniqueId;
	
	public PacketKeyBinding(){
		
	}
	
	public PacketKeyBinding(KeyBinding binding, int key, boolean pressed, int screen){
		this.key = key;
		this.pressed = pressed;
		this.uniqueId = binding.getUniqueId();
	}
	
	public int getNumBytes() {
		return 4 + 1 + 16;
	}

	public void readData(DataInputStream input) throws IOException {
		id = PacketUtil.readString(input);
		description = PacketUtil.readString(input);
		plugin = PacketUtil.readString(input);
		key = input.readInt();
		uniqueId = new UUID(input.readLong(), input.readLong());
	}

	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(key);
		output.writeBoolean(pressed);
		output.writeLong(uniqueId.getMostSignificantBits());
		output.writeLong(uniqueId.getLeastSignificantBits());
	}

	public void run(int playerId) {
		KeyBinding binding = new KeyBinding(key, plugin, id, description);
		binding.setUniqueId(uniqueId);
		SpoutClient.getInstance().getKeyBindingManager().registerControl(binding);
	}

	public void failure(int playerId) {}

	public PacketType getPacketType() {
		return PacketType.PacketKeyBinding;
	}

	public int getVersion() {
		return 0;
	}
}
