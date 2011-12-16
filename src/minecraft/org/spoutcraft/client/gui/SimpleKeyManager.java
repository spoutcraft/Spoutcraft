package org.spoutcraft.client.gui;

import org.spoutcraft.spoutcraftapi.gui.KeyManager;
import org.spoutcraft.spoutcraftapi.gui.Keyboard;

public class SimpleKeyManager implements KeyManager{

	public boolean isKeyDown(Keyboard key) {
		return org.lwjgl.input.Keyboard.isKeyDown(key.getKeyCode());
	}

	public boolean isRepeatingEvents() {
		return org.lwjgl.input.Keyboard.isRepeatEvent();
	}

	public void setRepeatingEvents(boolean repeat) {
		org.lwjgl.input.Keyboard.enableRepeatEvents(repeat);
	}
}
