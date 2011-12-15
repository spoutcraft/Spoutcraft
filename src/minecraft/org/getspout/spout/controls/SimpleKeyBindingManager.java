package org.getspout.spout.controls;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.Packet3Chat;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.io.FileUtil;
import org.getspout.spout.packet.PacketKeyBinding;
import org.lwjgl.input.Keyboard;
import org.spoutcraft.spoutcraftapi.keyboard.KeyBinding;
import org.spoutcraft.spoutcraftapi.keyboard.KeyBindingManager;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

public class SimpleKeyBindingManager implements KeyBindingManager {
	private HashMap<Integer, KeyBinding> bindingsForKey = new HashMap<Integer, KeyBinding>();
	private HashMap<Integer, Shortcut> shortcutsForKey = new HashMap<Integer, Shortcut>();
	private ArrayList<KeyBinding> bindings;
	private ArrayList<Shortcut> shortcuts = new ArrayList<Shortcut>();
	
	public SimpleKeyBindingManager(){
	}
	
	public void registerControl(KeyBinding binding){
		KeyBinding result = null;
		for(KeyBinding check:bindings){
			if(check.getId().equals(binding.getId()) && check.getAddonName().equals(binding.getAddonName())){
				result = check;
			}
		}
		if(result != null){
			result.takeChanges(binding);
		} else {
			bindings.add(binding);
		}
		updateBindings();
		save();
	}
	
	public void registerShortcut(Shortcut shortcut){
		shortcuts.add(shortcut);
		updateShortcuts();
		save();
	}
	
	public void unregisterShortcut(Shortcut shortcut) {
		shortcuts.remove(shortcut);
		shortcutsForKey.remove(shortcut.getKey());
		save();
	}
	
	public void unregisterControl(KeyBinding binding){
		bindings.remove(binding);
		bindingsForKey.remove(binding.getKey());
		save();
	}
	
	public void updateBindings() {
		if(bindings == null){
			bindings = new ArrayList<KeyBinding>();
			return;
		}
		
		bindingsForKey.clear();
		for(KeyBinding binding:bindings){
			bindingsForKey.put(binding.getKey(), binding);
		}
	}
	
	private void updateShortcuts() {
		if(shortcuts == null) {
			shortcuts = new ArrayList<Shortcut>();
			return;
		}
		
		shortcutsForKey.clear();
		for(Shortcut shortcut:shortcuts) {
			shortcutsForKey.put(shortcut.hashCode(), shortcut); //The hash also contains the modifier
		}
	}
	
	public void save(){
		boolean wasSandboxed = SpoutClient.isSandboxed();
		if(wasSandboxed) {
			SpoutClient.disableSandbox();
		}
		Yaml yaml = new Yaml();
		yaml.setBeanAccess(BeanAccess.FIELD); // to ignore transient fields!!
		try {
			//KeyBindings saving
			FileWriter writer = new FileWriter(getBindingsFile());
			ArrayList<Object> kbsave = new ArrayList<Object>();
			for(KeyBinding binding:bindings) {
				HashMap<String, Object> item = new HashMap<String, Object>();
				item.put("key", binding.getKey());
				item.put("id", binding.getId());
				item.put("description", binding.getDescription());
				item.put("addonName", binding.getAddonName());
				kbsave.add(item);
			}
			yaml.dump(kbsave, writer);
			
			//Shortcuts saving
			writer = new FileWriter(getShortcutsFile());
			ArrayList<Object> shsave = new ArrayList<Object>();
			for(Shortcut sh:shortcuts){
				HashMap<String, Object> item = new HashMap<String, Object>();
				item.put("title", sh.getTitle());
				item.put("key", sh.getKey());
				item.put("modifiers", sh.getModifiers());
				item.put("commands", sh.getCommands());
				shsave.add(item);
			}
			yaml.dump(shsave, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(wasSandboxed) {
			SpoutClient.enableSandbox();
		}
	}
	
	private File getBindingsFile() {
		return new File(FileUtil.getSpoutcraftDirectory(), "bindings.yml");
	}
	
	private File getShortcutsFile() {
		return new File(FileUtil.getSpoutcraftDirectory(), "shortcuts.yml");
	}

	@SuppressWarnings("unchecked")
	public void load(){
		boolean wasSandboxed = SpoutClient.isSandboxed();
		if(wasSandboxed) {
			SpoutClient.disableSandbox();
		}
		Yaml yaml = new Yaml();
		try {
			bindings = new ArrayList<KeyBinding>();
			ArrayList<Object> kbsave = yaml.loadAs(new FileReader(getBindingsFile()), ArrayList.class);
			for(Object obj:kbsave) {
				HashMap<String, Object> item = (HashMap<String, Object>) obj;
				int key = (Integer) item.get("key");
				String id, description, addonName;
				id = (String) item.get("id");
				description = (String) item.get("description");
				if(item.containsKey("addonName")) {
					addonName = (String) item.get("addonName");
				} else if(item.containsKey("plugin")) {
					addonName = (String) item.get("plugin");
				} else {
					continue; //Invalid item
				}
				KeyBinding binding = new KeyBinding(key, addonName, id, description);
				bindings.add(binding);
			}
		} catch (Exception e) {
			e.printStackTrace();
			bindings = new ArrayList<KeyBinding>();
		}
		updateBindings();
		try {
			shortcuts.clear();
			ArrayList<Object> shsave = yaml.loadAs(new FileReader(getShortcutsFile()), ArrayList.class);
			for(Object obj:shsave) {
				HashMap<String, Object> item = (HashMap<String, Object>) obj;
				Shortcut sh = new Shortcut();
				sh.setTitle((String)item.get("title"));
				sh.setKey((Integer)item.get("key"));
				sh.setCommands((ArrayList<String>)item.get("commands"));
				if(item.containsKey("modifiers")) {
					sh.setRawModifiers((byte)(int)(Integer)item.get("modifiers"));
				}
				shortcuts.add(sh);
			}
		} catch (Exception e) {
			e.printStackTrace();
			shortcuts = new ArrayList<Shortcut>();
		}
		updateShortcuts();
		if(wasSandboxed) {
			SpoutClient.enableSandbox();
		}
	}

	public void pressKey(int key, boolean keyReleased, int screen) {
		KeyBinding binding = bindingsForKey.get(key);
		if(binding!=null){
			if(binding.getDelegate() == null &&  binding.getUniqueId() != null) { //Server side
				SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketKeyBinding(binding, key, keyReleased, screen));
			} else if(binding.getDelegate() != null){ //Client side
				if(!keyReleased) {
					binding.getDelegate().onKeyPress(key, binding);
				} else {
					binding.getDelegate().onKeyRelease(key, binding);
				}
			}
		}
		if(screen == 0) {
			if(!isModifierKey(key)) {
				Shortcut shortcut = shortcutsForKey.get(getPressedShortcut(key).hashCode());
				if(shortcut != null && !keyReleased) {
					//TODO: send to addons!
					for(String cmd:shortcut.getCommands()) {
						if(SpoutClient.getHandle().isMultiplayerWorld()) {
							EntityClientPlayerMP player = (EntityClientPlayerMP)SpoutClient.getHandle().thePlayer;
							player.sendQueue.addToSendQueue(new Packet3Chat(cmd));
						}
					}
				}
			}
		}
	}
	
	public static boolean isModifierKey(int key) {
		if(key == Keyboard.KEY_LSHIFT
				|| key == Keyboard.KEY_RSHIFT
				|| key == Keyboard.KEY_LMENU
				|| key == Keyboard.KEY_RMENU
				|| key == Keyboard.KEY_LCONTROL
				|| key == Keyboard.KEY_RCONTROL
				|| key == Keyboard.KEY_LMETA
				|| key == Keyboard.KEY_RMETA){
			return true;
		}
		return false;
	}

	public List<KeyBinding> getAllBindings() {
		return bindings;
	}

	public List<Shortcut> getAllShortcuts() {
		return Collections.unmodifiableList(shortcuts);
	}
	
	public static Shortcut getPressedShortcut(int key) {
		Shortcut sh = new Shortcut();
		sh.setKey(key);
		setModifiersToShortcut(sh);
		return sh;
	}
	
	public static void setModifiersToShortcut(Shortcut sh) {
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			sh.setModifier(Shortcut.MOD_SHIFT, true);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
			sh.setModifier(Shortcut.MOD_CTRL, true);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU)) {
			sh.setModifier(Shortcut.MOD_ALT, true);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LMETA) || Keyboard.isKeyDown(Keyboard.KEY_RMETA)) {
			sh.setModifier(Shortcut.MOD_SUPER, true);
		}
	}
	
	public static byte getPressedModifiers() {
		byte res = 0;
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			res|=Shortcut.MOD_SHIFT;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
			res|=Shortcut.MOD_CTRL;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU)) {
			res|=Shortcut.MOD_ALT;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LMETA) || Keyboard.isKeyDown(Keyboard.KEY_RMETA)) {
			res|=Shortcut.MOD_SUPER;
		}
		return res;
	}
}
