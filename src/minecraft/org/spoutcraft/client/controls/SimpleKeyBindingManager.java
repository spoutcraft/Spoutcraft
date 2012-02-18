/*
 * This file is part of Spoutcraft (http://www.spout.org/).
 *
 * Spoutcraft is licensed under the SpoutDev License Version 1.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spoutcraft.client.controls;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.src.Block;
import net.minecraft.src.BlockWorkbench;
import net.minecraft.src.GuiScreen;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.io.FileUtil;
import org.spoutcraft.client.packet.PacketKeyBinding;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.gui.ScreenType;
import org.spoutcraft.spoutcraftapi.keyboard.AbstractBinding;
import org.spoutcraft.spoutcraftapi.keyboard.KeyBinding;
import org.spoutcraft.spoutcraftapi.keyboard.KeyBindingManager;
import org.spoutcraft.spoutcraftapi.keyboard.KeyBindingPress;
import org.lwjgl.input.Keyboard;

public class SimpleKeyBindingManager implements KeyBindingManager {
	private ArrayList<KeyBinding> bindings;
	private ArrayList<Shortcut> shortcuts = new ArrayList<Shortcut>();
	private HashMap<Integer, ArrayList<AbstractBinding>> bindingsForKey = new HashMap<Integer, ArrayList<AbstractBinding>>();

	public SimpleKeyBindingManager() {
	}

	public void registerControl(KeyBinding binding) {
		KeyBinding result = null;
		for (KeyBinding check:bindings) {
			if (check.getId().equals(binding.getId()) && check.getAddonName().equals(binding.getAddonName())) {
				result = check;
			}
		}
		if (result != null) {
			result.takeChanges(binding);
		} else {
			bindings.add(binding);
		}
		updateBindings();
		save();
	}

	public void registerShortcut(Shortcut shortcut) {
		shortcuts.add(shortcut);
		updateBindings();
		save();
	}

	public void unregisterShortcut(Shortcut shortcut) {
		shortcuts.remove(shortcut);
		bindingsForKey.get(shortcut.getKey()).remove(shortcut);
		save();
	}

	public void unregisterControl(KeyBinding binding) {
		bindings.remove(binding);
		bindingsForKey.get(binding.getKey()).remove(binding);
		save();
	}

	public void updateBindings() {
		if (bindings == null) {
			bindings = new ArrayList<KeyBinding>();
		}
		if(shortcuts == null) {
			shortcuts = new ArrayList<Shortcut>();
		}

		bindingsForKey.clear();
		for (KeyBinding binding:bindings) {
			ArrayList<AbstractBinding> bindings = bindingsForKey.get(binding.getKey());
			if(bindings == null) {
				bindings = new ArrayList<AbstractBinding>();
				bindingsForKey.put(binding.getKey(), bindings);
			}
			bindings.add(binding);
		}
		for (Shortcut binding:shortcuts) {
			ArrayList<AbstractBinding> bindings = bindingsForKey.get(binding.getKey());
			if(bindings == null) {
				bindings = new ArrayList<AbstractBinding>();
				bindingsForKey.put(binding.getKey(), bindings);
			}
			bindings.add(binding);
		}
	}

	public void save() {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		if (wasSandboxed) {
			SpoutClient.disableSandbox();
		}
		Yaml yaml = new Yaml();
		yaml.setBeanAccess(BeanAccess.FIELD); // to ignore transient fields!!
		try {
			//KeyBindings saving
			FileWriter writer = new FileWriter(getBindingsFile());
			ArrayList<Object> kbsave = new ArrayList<Object>();
			for (KeyBinding binding:bindings) {
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
			for (Shortcut sh:shortcuts) {
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
		if (wasSandboxed) {
			SpoutClient.enableSandbox();
		}
	}

	private File getBindingsFile() throws IOException {
		File file = new File(FileUtil.getSpoutcraftDirectory(), "bindings.yml");
		if (!file.exists()) {
			file.createNewFile();
		}
		return file;
	}

	private File getShortcutsFile() throws IOException {
		File file = new File(FileUtil.getSpoutcraftDirectory(), "shortcuts.yml");
		if (!file.exists()) {
			file.createNewFile();
		}
		return file;
	}

	@SuppressWarnings("unchecked")
	public void load() {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		if (wasSandboxed) {
			SpoutClient.disableSandbox();
		}
		Yaml yaml = new Yaml();
		try {
			bindings = new ArrayList<KeyBinding>();
			ArrayList<Object> kbsave = yaml.loadAs(new FileReader(getBindingsFile()), ArrayList.class);
			if (kbsave == null) {
				kbsave = new ArrayList<Object>();
			}
			for (Object obj:kbsave) {
				HashMap<String, Object> item = (HashMap<String, Object>) obj;
				int key = (Integer) item.get("key");
				String id, description, addonName;
				id = (String) item.get("id");
				description = (String) item.get("description");
				if (item.containsKey("addonName")) {
					addonName = (String) item.get("addonName");
				} else if (item.containsKey("plugin")) {
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
			if (shsave == null) {
				shsave = new ArrayList<Object>();
			}
			for (Object obj:shsave) {
				HashMap<String, Object> item = (HashMap<String, Object>) obj;
				Shortcut sh = new Shortcut();
				sh.setTitle((String)item.get("title"));
				sh.setKey((Integer)item.get("key"));
				sh.setCommands((ArrayList<String>)item.get("commands"));
				if (item.containsKey("modifiers")) {
					sh.setRawModifiers((byte)(int)(Integer)item.get("modifiers"));
				}
				shortcuts.add(sh);
			}
		} catch (Exception e) {
			e.printStackTrace();
			shortcuts = new ArrayList<Shortcut>();
		}
		updateBindings();
		if (wasSandboxed) {
			SpoutClient.enableSandbox();
		}
	}

	public void pressKey(int key, boolean keyPressed, int screen) {
		if(SpoutClient.getHandle().currentScreen instanceof GuiAmbigousInput) {
			return;
		}
		if(bindingsForKey.containsKey(key)) {
			ArrayList<AbstractBinding> bindings = bindingsForKey.get(key);
			ArrayList<AbstractBinding> effective = new ArrayList<AbstractBinding>();
			for(AbstractBinding b:bindings) {
				if(b.matches(key)) {
					effective.add(b);
				}
			}
			if(effective.size() == 1) {
				effective.iterator().next().summon(key, !keyPressed, screen);
			} else if(screen == 0) {
				GuiScreen parent = SpoutClient.getHandle().currentScreen;
				SpoutClient.getHandle().displayGuiScreen(new GuiAmbigousInput(effective, parent));
			} else {
				Spoutcraft.getActivePlayer().showAchievement("Multiple Bindings ...", "are assigned to Key "+Keyboard.getKeyName(key), Block.workbench.blockID);
			}
			
		}
	}

	public static boolean isModifierKey(int key) {
		if (key == Keyboard.KEY_LSHIFT
				|| key == Keyboard.KEY_RSHIFT
				|| key == Keyboard.KEY_LMENU
				|| key == Keyboard.KEY_RMENU
				|| key == Keyboard.KEY_LCONTROL
				|| key == Keyboard.KEY_RCONTROL
				|| key == Keyboard.KEY_LMETA
				|| key == Keyboard.KEY_RMETA) {
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
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			sh.setModifier(Shortcut.MOD_SHIFT, true);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
			sh.setModifier(Shortcut.MOD_CTRL, true);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU)) {
			sh.setModifier(Shortcut.MOD_ALT, true);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LMETA) || Keyboard.isKeyDown(Keyboard.KEY_RMETA)) {
			sh.setModifier(Shortcut.MOD_SUPER, true);
		}
	}

	public static byte getPressedModifiers() {
		byte res = 0;
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			res|=Shortcut.MOD_SHIFT;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
			res|=Shortcut.MOD_CTRL;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU)) {
			res|=Shortcut.MOD_ALT;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LMETA) || Keyboard.isKeyDown(Keyboard.KEY_RMETA)) {
			res|=Shortcut.MOD_SUPER;
		}
		return res;
	}
	
	public void summon(KeyBinding binding, int key, boolean keyReleased, int screen) {
		if (binding.getDelegate() == null &&  binding.getUniqueId() != null) { //Server side
			SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketKeyBinding(binding, key, !keyReleased, screen));
		} else if (binding.getDelegate() != null) { //Client side
			KeyBindingPress event = new KeyBindingPress(org.spoutcraft.spoutcraftapi.gui.Keyboard.getKey(key), binding, ScreenType.getType(screen));
			if (!keyReleased) {
				binding.getDelegate().onKeyPress(event);
			} else {
				binding.getDelegate().onKeyRelease(event);
			}
		}
	}
}
