/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
package org.spoutcraft.client.player.accessories;

import java.util.HashMap;
import java.util.Map;

public enum AccessoryType {

	TOPHAT(1), NOTCHHAT(2), BRACELET(3), WINGS(4), EARS(5), SUNGLASSES(6), TAIL(7);
	
	private static Map<Integer, AccessoryType> types = new HashMap<Integer, AccessoryType>();
	private int id;
	private AccessoryType(int id) {
		this.id = id;
		addID(id, this);
	}
	
	private static void addID(int id, AccessoryType type) {
		types.put(id, type);
	}
	
	public int getId() {
		return id;
	}
	
	public static AccessoryType byId(int id) {
		return types.get(id);
	}
}