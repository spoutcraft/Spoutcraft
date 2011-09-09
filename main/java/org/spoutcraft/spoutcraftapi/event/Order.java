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
package org.spoutcraft.spoutcraftapi.event;

/**
 * Order of event listener calls.
 * 
 * Odd-numbered slots are called even when
 * events are marked "not propogating". If an event stops propogating partway
 * through an even slot, that slot will not cease execution, but future even
 * slots will not be called.
 * 
 * @author lahwran
 */
public enum Order {
	/**
	 * Called before all other handlers.
	 * Should be used for high-priority event canceling.
	 */
	Earlist(0),

	/**
	 * Called after "Earliest" handlers and before "Early" handlers. Is called
	 * even when event has been canceled. Should generally be used to uncancel
	 * events canceled in Earliest.
	 */
	Early_IgnoreCancelled(1),

	/**
	 * Called after "Earliest" handlers. Should generally be used for low
	 * priority event canceling.
	 */
	Early(2),

	/**
	 * Called after "Early" handlers and before "Default" handlers. Is called
	 * even when event has been canceled. This is for general-purpose
	 * always-run events.
	 */
	Default_IgnoreCancelled(3),
	/**
	 * Default call, for general purpose handlers
	 */
	Default(4),

	/**
	 * Called after "Default" handlers and before "Late" handlers. Is called
	 * even when event has been canceled.
	 */
	Late_IgnoreCancelled(5),

	/**
	 * Called after "Default" handlers. 
	 */
	Late(6),

	Latest_IgnoreCancelled(7),
	Latest(8),
	Monitor(9);
	private int index;
	Order(int index) {
		this.index = index;
	}
	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
}