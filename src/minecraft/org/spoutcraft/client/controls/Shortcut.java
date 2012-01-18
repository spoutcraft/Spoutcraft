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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

public class Shortcut implements Serializable {
	private static final long serialVersionUID = 4365592803468257957L;
	private int key = -1;
	private byte modifierKeys = 0;

	public static final byte MOD_SHIFT = 1;
	public static final byte MOD_CTRL = 2;
	public static final byte MOD_ALT = 4;
	/**
	 * SUPER is Windows key on Windows, Super on Linux, CMD/Command on Mac OS X
	 * On normal keyboards, this key is located between CTRL and ALT,
	 * on Apple Keyboards (or those which are compatible with Mac OS X), the CMD key is left AND right from the Spacebar.
	 */
	public static final byte MOD_SUPER = 8;

	private String title = "";
	private ArrayList<String> commands = new ArrayList<String>();

	public Shortcut() {
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int getKey() {
		return key;
	}

	public void addCommand(String cmd) {
		commands.add(cmd);
	}

	public List<String> getCommands() {
		return commands;
	}

	public void clear() {
		commands.clear();
	}

	public void removeCommand(int i) {
		commands.remove(i);
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setModifier(byte mod, boolean holding) {
		if (holding) {
			modifierKeys |= mod;
		} else {
			modifierKeys &= ~mod;
		}
	}

	public boolean hasModifier(byte mod) {
		return (modifierKeys & mod) != 0;
	}

	public void setCommands(ArrayList<String> commands) {
		this.commands = commands;
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
		result += Keyboard.getKeyName(key);

		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + key;
		result = prime * result + modifierKeys;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Shortcut other = (Shortcut) obj;
		if (key != other.key) {
			return false;
		}
		if (modifierKeys != other.modifierKeys) {
			return false;
		}
		return true;
	}

	public byte getModifiers() {
		return modifierKeys;
	}

	public void setRawModifiers(byte mod) {
		this.modifierKeys = mod;
	}
}
