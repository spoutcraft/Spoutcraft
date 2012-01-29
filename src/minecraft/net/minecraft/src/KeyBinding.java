package net.minecraft.src;

import java.util.*;

public class KeyBinding {
	public static List keybindArray = new ArrayList();
	public static IntHashMap hash = new IntHashMap();
	public String keyDescription;
	public int keyCode;
	public boolean pressed;
	public int pressTime;

	public static void onTick(int i) {
		KeyBinding keybinding = (KeyBinding)hash.lookup(i);
		if (keybinding != null) {
			keybinding.pressTime++;
		}
	}

	public static void setKeyBindState(int i, boolean flag) {
		KeyBinding keybinding = (KeyBinding)hash.lookup(i);
		if (keybinding != null) {
			keybinding.pressed = flag;
		}
	}

	public static void unPressAllKeys() {
		KeyBinding keybinding;
		for (Iterator iterator = keybindArray.iterator(); iterator.hasNext(); keybinding.unpressKey()) {
			keybinding = (KeyBinding)iterator.next();
		}
	}

	public static void resetKeyBindingArrayAndHash() {
		hash.clearMap();
		KeyBinding keybinding;
		for (Iterator iterator = keybindArray.iterator(); iterator.hasNext(); hash.addKey(keybinding.keyCode, keybinding)) {
			keybinding = (KeyBinding)iterator.next();
		}
	}

	public KeyBinding(String s, int i) {
		pressTime = 0;
		keyDescription = s;
		keyCode = i;
		keybindArray.add(this);
		hash.addKey(i, this);
	}

	public boolean isPressed() {
		if (pressTime == 0) {
			return false;
		}
		else {
			pressTime--;
			return true;
		}
	}

	private void unpressKey() {
		pressTime = 0;
		pressed = false;
	}
}
