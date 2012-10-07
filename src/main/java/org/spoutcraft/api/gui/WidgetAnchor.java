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
package org.spoutcraft.api.gui;

import java.util.HashMap;

public enum WidgetAnchor {
	/**
	 * Widget anchors allow you to place widgets that
	 * stick or "anchor" to a point on the screen
	 *
	 * A widget's coordinates refer to its top left
	 * corner and anchors change the point they are
	 * relative to on the screen
	 *
	 * You can choose any of 9 points to anchor to,
	 * note that if anchoring to the bottom or right
	 * the widget will likely be offscreen until you
	 * set a negative y or x value
	 *
	 * The only exception is scale which assumes the
	 * screen to always be 427x240 and moves/scales
	 * widgets to conform
	 */
	TOP_LEFT(0), // x, y
	TOP_CENTER(1), // screenwidth/2 + x, y
	TOP_RIGHT(2), // screenwidth + x, y
	CENTER_LEFT(3), // x, screenheight/2 + y
	CENTER_CENTER(4), // screenwidth/2 + x, screenheight/2 + y
	CENTER_RIGHT(5), // screenwidth + x, screenheight/2 + y
	BOTTOM_LEFT(6), // x, screenheight + y
	BOTTOM_CENTER(7), // screenwidth/2 + x, screenheight + y
	BOTTOM_RIGHT(8), // screenwidth + x, screenheight + y
	SCALE(9), // Scales
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
