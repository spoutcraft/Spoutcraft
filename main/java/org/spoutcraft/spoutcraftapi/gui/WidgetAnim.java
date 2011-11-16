/*
 * This file is part of Spout API (http://wiki.getspout.org/).
 *
 * Spout API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spout API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.spoutcraftapi.gui;

import java.util.HashMap;

/**
 * Types of animation, only one animation is permitted at a time, and note that
 * some types are limited to certain widget types...
 */
public enum WidgetAnim {

	/**
	 * No animation (default).
	 */
	NONE(0),
	/**
	 * Change the X or Y by "value" pixels (any Widget).
	 */
	POSITION(1),
	/**
	 * Change the Width or Height by "value" pixels (any Widget).
	 */
	SIZE(2),
	/**
	 * Change the Top or Left offset by "value" pixels (Texture only).
	 */
	OFFSET(3);
	private final int id;

	WidgetAnim(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	private static final HashMap<Integer, WidgetAnim> lookupId = new HashMap<Integer, WidgetAnim>();

	static {
		for (WidgetAnim t : values()) {
			lookupId.put(t.getId(), t);
		}
	}

	public static WidgetAnim getAnimationFromId(int id) {
		return lookupId.get(id);
	}

	public boolean check(Widget widget) {
		switch (this) {
			case POSITION:
			case SIZE:
				return true;
			case OFFSET:
				if (widget instanceof Texture) {
					return true;
				}
		}
		return false;
	}
}
