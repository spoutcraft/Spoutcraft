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
 * This class doesn't actually need to exist, but it feels wrong to have this
 * part of the event call logic inside Event
 * @author lahwran
 */
public class EventManager {
	/**
	 * Call an event.
	 * 
	 * @param <TEvent> Event subclass
	 * @param event Event to handle
	 */
	public <TEvent extends Event<TEvent>> void callEvent(TEvent event) {
		HandlerList<TEvent> handlerlist = event.getHandlers();
		handlerlist.bake();

		Listener<TEvent>[][] handlers = handlerlist.handlers;
		int[] handlerids = handlerlist.handlerids;

		for (int arrayidx=0; arrayidx<handlers.length; arrayidx++) {

			// if the order slot is even and the event has stopped propogating
			if (event.isCancelled() && (handlerids[arrayidx] & 1) == 0)
				continue; // then don't call this order slot

			for (int handler = 0; handler < handlers[arrayidx].length; handler++) {
				try {
					handlers[arrayidx][handler].onEvent(event);
				} catch (Throwable t) {
					System.err.println("Error while passing event "+event);
					t.printStackTrace();
				}
			}
		}
	}
}
