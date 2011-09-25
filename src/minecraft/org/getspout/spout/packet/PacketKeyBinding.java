package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.getspout.spout.client.SpoutClient;
import org.spoutcraft.spoutcraftapi.gui.ScreenType;
import org.spoutcraft.spoutcraftapi.keyboard.KeyBinding;

public class PacketKeyBinding implements SpoutPacket {

	private String id;
	private String plugin;
	private String description;
	private int key;
	private boolean pressed;
	private int screen;
	
	public PacketKeyBinding(){
		
	}
	
	public PacketKeyBinding(KeyBinding binding, int key, boolean pressed, int screen){
		id = binding.getId();
		plugin = binding.getPlugin();
		this.key = key;
		this.pressed = pressed;
		this.screen = screen;
	}
	
	@Override
	public int getNumBytes() {
		return PacketUtil.getNumBytes(id) + PacketUtil.getNumBytes(plugin) + 4 + 1 + 4;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		id = PacketUtil.readString(input);
		description = PacketUtil.readString(input);
		plugin = PacketUtil.readString(input);
		key = input.readInt();
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		PacketUtil.writeString(output, id);
		PacketUtil.writeString(output, plugin);
		output.writeInt(key);
		output.writeBoolean(pressed);
		output.writeInt(screen);
	}

	@Override
	public void run(int playerId) {
		KeyBinding binding = new KeyBinding(key, plugin, id, description);
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
