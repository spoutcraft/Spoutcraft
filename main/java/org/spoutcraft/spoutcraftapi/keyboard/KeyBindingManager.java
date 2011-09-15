package org.spoutcraft.spoutcraftapi.keyboard;

import org.spoutcraft.spoutcraftapi.gui.ScreenType;

public interface KeyBindingManager {

	public void registerControl(KeyBinding binding);

	public void unregisterControl(KeyBinding binding);

	public abstract void load();

	public abstract void save();

	public void pressKey(int key, boolean keyReleased, int screen);

}