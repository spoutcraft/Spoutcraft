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
package org.spoutcraft.client.gui;

import org.spoutcraft.api.gui.KeyManager;
import org.spoutcraft.api.gui.Keyboard;

public class SimpleKeyManager implements KeyManager {
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
