/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Spoutcraft is licensed under the GNU Lesser General Public License.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.api.keyboard;

import org.lwjgl.input.Keyboard;

public abstract class AbstractBinding {
	public static final byte MOD_SHIFT = 1;
	public static final byte MOD_CTRL = 2;
	public static final byte MOD_ALT = 4;
	/**
	 * SUPER is Windows key on Windows, Super on Linux, CMD/Command on Mac OS X
	 * On normal keyboards, this key is located between CTRL and ALT,
	 * on Apple Keyboards (or those which are compatible with Mac OS X), the CMD key is left AND right from the Spacebar.
	 */
	public static final byte MOD_SUPER = 8;

	private int key = -1;

	private byte modifiers = 0;

	public void setKey(int key) {
		this.key = key;
	}

	public int getKey() {
		return key;
	}

	public byte getModifiers() {
		return modifiers;
	}

	public void setRawModifiers(byte mod) {
		this.modifiers = mod;
	}

	public void setModifier(byte mod, boolean holding) {
		if (holding) {
			modifiers |= mod;
		} else {
			modifiers &= ~mod;
		}
	}

	public abstract void summon(int key, boolean keyReleased, int screen);

	public boolean matches(int key, byte modifiers) {
		return key == getKey() && modifiers == getModifiers();
	}

	public abstract String getTitle();

	public boolean hasModifier(byte mod) {
		return (modifiers & mod) != 0;
	}

	@Override
	public String toString() {
		String result = "";
		if (hasModifier(MOD_SHIFT)) {
			result += "SHIFT + ";
		}
		if (hasModifier(MOD_CTRL)) {
			result += "CTRL + ";
		}
		if (hasModifier(MOD_ALT)) {
			result += "ALT + ";
		}
		if (hasModifier(MOD_SUPER)) {
			result += "SUPER + ";
		}
		if (key > 0) {
			result += Keyboard.getKeyName(key);
		} else if (key == 0) {
			result += "No key";
		} else if (key < 0) {
			result += getMouseButtonName(key);
		}

		return result;
	}

	public static String getMouseButtonName(int button) {
		button += 100;
		switch(button) {
			case 0:
				return "Left button";
			case 1:
				return "Right button";
			case 2:
				return "Middle button";
			default:
				return "Button "+button;
		}
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof AbstractBinding) {
			AbstractBinding a = (AbstractBinding) other;
			return a == this;
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + key;
		result = prime * result + modifiers;
		return result;
	}
}
