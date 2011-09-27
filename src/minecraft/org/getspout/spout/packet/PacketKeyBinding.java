package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.getspout.spout.client.SpoutClient;
import org.spoutcraft.spoutcraftapi.keyboard.KeyBinding;

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
	
	@Override
	public int getNumBytes() {
		return 4 + 1 + 16;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		id = PacketUtil.readString(input);
		description = PacketUtil.readString(input);
		plugin = PacketUtil.readString(input);
		key = input.readInt();
		uniqueId = new UUID(input.readLong(), input.readLong());
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		output.writeInt(key);
		output.writeBoolean(pressed);
		output.writeLong(uniqueId.getMostSignificantBits());
		output.writeLong(uniqueId.getLeastSignificantBits());
	}

	@Override
	public void run(int playerId) {
		KeyBinding binding = new KeyBinding(key, plugin, id, description);
		binding.setUniqueId(uniqueId);
		SpoutClient.getInstance().getKeyBindingManager().registerControl(binding);
	}

	@Override
	public void failure(int playerId) {}

	@Override
	public PacketType getPacketType() {
		return PacketType.PacketKeyBinding;
	}

	@Override
	public int getVersion() {
		return 0;
	}
}
