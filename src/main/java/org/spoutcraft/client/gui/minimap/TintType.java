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
package org.spoutcraft.client.gui.minimap;

import java.util.HashMap;

/**
 * Represents the possible types of tint available for block colors
 */
public enum TintType {
	// Redstone is actually based on the metadata...
	NONE, GRASS, TALL_GRASS, FOLIAGE, PINE, BIRCH, REDSTONE, GLASS, WATER, COLORMULT;
	public static final HashMap<String, TintType> map;

	public static TintType get(String name) {
		return map.get(name);
	}

	static {
		map = new HashMap<String, TintType>();
		for (TintType t : TintType.values()) {
			map.put(t.name(), t);
		}
	}
}
