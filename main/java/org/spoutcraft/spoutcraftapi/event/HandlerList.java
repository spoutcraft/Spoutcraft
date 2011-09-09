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

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map.Entry;

/**
 * @author lahwran
 * @param <TEvent> Event type
 *
 */
@SuppressWarnings("unchecked")
public class HandlerList<TEvent extends Event<TEvent>> {
	/**
	 * handler array. this field being an array is the key to this system's speed.
	 * 
	 * is initialized in bake().
	 */
	Listener<TEvent>[][] handlers;

	/**
	 * Int array same length as handlers. each value in this array is the index
	 * of an Order slot, corossponding to the equivalent value in handlers.
	 * 
	 * is initialized in bake().
	 */
	int[] handlerids;

	/**
	 * Dynamic handler lists. These are changed using register() and
	 * unregister() and are automatically baked to the handlers array any
	 * time they have changed.
	 */
	private final EnumMap<Order, ArrayList<Listener<TEvent>>> handlerslots;

	/**
	 * Whether the current handlerslist has been fully baked. When this is set
	 * to false, the Map<Order, List<Listener>> will be baked to Listener[][]
	 * next time the event is called.
	 * 
	 * @see EventManager.callEvent
	 */
	private boolean baked = false;

	/**
	 * List of all handlerlists which have been created, for use in bakeall()
	 */
	private static ArrayList<HandlerList> alllists = new ArrayList<HandlerList>();

	/**
	 * Bake all handler lists. Best used just after all normal event
	 * registration is complete, ie just after all plugins are loaded if
	 * you're using fevents in a plugin system.
	 */
	public static void bakeall() {
		for (HandlerList h : alllists) {
			h.bake();
		}
	}

	/**
	 * Create a new handler list and initialize using EventManager.Order
	 * handlerlist is then added to meta-list for use in bakeall()
	 */
	public HandlerList() {
		handlerslots = new EnumMap<Order, ArrayList<Listener<TEvent>>>(Order.class);
		for (Order o : Order.values()) {
			handlerslots.put(o, new ArrayList<Listener<TEvent>>());
		}
		alllists.add(this);
	}

	/**
	 * Register a new listener in this handler list
	 * @param listener listener to register
	 * @param order order location at which to call provided listener
	 */
	public void register(Listener<TEvent> listener, Order order) {
		if (handlerslots.get(order).contains(listener))
			throw new IllegalStateException("This listener is already registered to order "+order.toString());
		baked = false;
		handlerslots.get(order).add(listener);
	}

	/**
	 * Remove a listener from all order slots
	 * @param listener listener to purge
	 */
	public void unregister(Listener<TEvent> listener) {
		for (Order o : Order.values()) {
			unregister(listener, o);
		}
	}

	/**
	 * Remove a listener from a specific order slot
	 * @param listener listener to remove
	 * @param order order from which to remove listener
	 */
	public void unregister(Listener<TEvent> listener, Order order) {
		if (handlerslots.get(order).contains(listener)) {
			baked = false;
			handlerslots.get(order).remove(listener);
		}
	}

	/**
	 * Bake HashMap and ArrayLists to 2d array - does nothing if not necessary
	 */
	void bake() {
		if (baked)
			return; // don't re-bake when still valid

		ArrayList<Listener[]> handlerslist = new ArrayList<Listener[]>();
		ArrayList<Integer> handleridslist = new ArrayList<Integer>();
		for (Entry<Order, ArrayList<Listener<TEvent>>> entry : handlerslots.entrySet()) {
			Order orderslot = entry.getKey();

			ArrayList<Listener<TEvent>> list = entry.getValue();

			int ord = orderslot.getIndex();
			handlerslist.add(list.toArray(new Listener[list.size()]));
			handleridslist.add(ord);
		}
		handlers = handlerslist.toArray(new Listener[handlerslist.size()][]);
		handlerids = new int[handleridslist.size()];
		for (int i=0; i<handleridslist.size(); i++) {
			handlerids[i] = handleridslist.get(i);
		}
		baked = true;
	}
}
