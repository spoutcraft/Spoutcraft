/*
 * This file is part of Spoutcraft (http://wiki.getspout.org/).
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
package org.spoutcraft.spoutcraftapi.gui;

import java.util.HashMap;

public enum WidgetAnchor {
	TOP_LEFT(0),
	TOP_CENTER(1),
	TOP_RIGHT(2),
	CENTER_LEFT(3),
	CENTER_CENTER(4),
	CENTER_RIGHT(5),
	BOTTOM_LEFT(6),
	BOTTOM_CENTER(7),
	BOTTOM_RIGHT(8),
	SCALE(9),
	;

	private final int id;
	WidgetAnchor(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	private static final HashMap<Integer, WidgetAnchor> lookupId = new HashMap<Integer, WidgetAnchor>();

	static {
		for (WidgetAnchor t : values()) {
			lookupId.put(t.getId(), t);
		}
	}

	public static WidgetAnchor getAnchorFromId(int id) {
		return lookupId.get(id);
	}
}
