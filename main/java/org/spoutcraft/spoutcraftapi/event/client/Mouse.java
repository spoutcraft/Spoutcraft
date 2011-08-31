/*
 * This file is part of Spoutcraft API (http://wiki.getspout.org/).
 * 
 * Spoutcraft API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.spoutcraftapi.event.client;

import java.util.HashMap;
import java.util.Map;

public enum Mouse {
	LEFT_BUTTON(0),
	RIGHT_BUTTON(1),
	MIDDLE_BUTTON(2),
	KEY_UNKNOWN(-1);

	private final int buttonCode;
	private static final Map<Integer, Mouse> lookupButtonCode = new HashMap<Integer, Mouse>();
	Mouse(final int i) {
		this.buttonCode = i;
	}

	public int getButtonCode() {
		return buttonCode;
	}

	public static Mouse getButton(int button) {
		if (lookupButtonCode.containsKey(button)) {
			return lookupButtonCode.get(button);
		}
		//Some computers report signed/unsigned values incorrectly
		if (lookupButtonCode.containsKey(256+button)) {
			return lookupButtonCode.get(256+button);
		}
		return KEY_UNKNOWN;
	}

	static {
		for (Mouse button : values()) {
			lookupButtonCode.put(button.buttonCode, button);
		}
	}
}
