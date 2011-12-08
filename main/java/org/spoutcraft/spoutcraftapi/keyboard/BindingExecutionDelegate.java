package org.spoutcraft.spoutcraftapi.keyboard;

public abstract class BindingExecutionDelegate {
	public abstract void onKeyPress(int key, KeyBinding binding);
	public abstract void onKeyRelease(int key, KeyBinding binding);
}
