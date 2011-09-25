package org.getspout.spout.controls;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.io.FileUtil;
import org.getspout.spout.packet.PacketKeyBinding;
import org.spoutcraft.spoutcraftapi.keyboard.KeyBindingManager;
import org.spoutcraft.spoutcraftapi.keyboard.KeyBinding;
import org.yaml.snakeyaml.Yaml;

public class SimpleKeyBindingManager implements KeyBindingManager {
	private HashMap<Integer, KeyBinding> bindings = new HashMap<Integer, KeyBinding>();
	
	public SimpleKeyBindingManager(){
		load();
	}
	
	public void registerControl(KeyBinding binding){
		bindings.put(binding.getKey(), binding);
	}
	
	public void unregisterControl(KeyBinding binding){
		bindings.remove(binding.getKey());
	}
	
	public void updateBinding(KeyBinding binding, int newKey){
		unregisterControl(binding);
		binding.setKey(newKey);
		registerControl(binding);
	}
	
	@Override
	public void save(){
		Yaml yaml = new Yaml();
		File file = new File(FileUtil.getSpoutcraftDirectory(), "bindings.yml");
		try {
			FileWriter writer = new FileWriter(file);
			yaml.dump(bindings, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void load(){
		Yaml yaml = new Yaml();
	}

	@Override
	public void pressKey(int key, boolean keyReleased, int screen) {
		
		KeyBinding binding = bindings.get(key);
		if(binding==null){
			System.out.println("No binding found!");
			return;
		}	
		SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketKeyBinding(binding, key, keyReleased, screen));
	}
}
