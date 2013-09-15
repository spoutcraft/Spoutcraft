package com.prupe.mcpatcher;

import java.util.BitSet;

import org.lwjgl.input.Keyboard;

public class InputHandler {
	private final BitSet keysDown = new BitSet();
	private final String name;
	private final boolean enabled;

	public InputHandler(String name, boolean enabled) {
		this.name = name;
		this.enabled = enabled;
	}

	public String getName() {
		return this.name;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public boolean isKeyPressed(int key) {
		if (this.enabled) {
			if (Keyboard.isKeyDown(key)) {
				if (!this.keysDown.get(key)) {
					this.keysDown.set(key);
					return true;
				}
			} else {
				this.keysDown.clear(key);
			}
		}

		return false;
	}

	public boolean isKeyDown(int key) {
		return this.enabled && Keyboard.isKeyDown(key);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("InputUtils{").append(this.name).append(':');

		for (int i = this.keysDown.nextSetBit(0); i >= 0; i = this.keysDown.nextSetBit(i + 1)) {
			sb.append(' ').append(Keyboard.getKeyName(i));
		}

		sb.append('}');
		return sb.toString();
	}
}
