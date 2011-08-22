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
package org.getspout.spout.packet;

public enum ScreenAction {
	Open(0),
	Close(1),
	;
	
	private final byte id;
	ScreenAction(int id) {
		this.id = (byte)id;
	}
	
	public int getId() {
		return id;
	}
	
	public static ScreenAction getScreenActionFromId(int id) {
		for (ScreenAction action : values()) {
			if (action.getId() == id) {
				return action;
			}
		}
		return null;
	}
}
