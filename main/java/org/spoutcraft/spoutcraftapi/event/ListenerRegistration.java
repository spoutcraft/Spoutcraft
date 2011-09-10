package org.spoutcraft.spoutcraftapi.event;

import org.spoutcraft.spoutcraftapi.addon.Addon;

/**
 * @author lahwran
 * @param <TEvent> Event class
 */
public class ListenerRegistration<TEvent extends Event<TEvent>> {
	private final Listener<TEvent> listener;
	private final Order orderslot;
	private final Addon addon;

	/**
	 * 
	 * @param listener Listener this registration represents
	 * @param orderslot Order position this registration is in
	 * @param addon addon that created this registration
	 */
	public ListenerRegistration(final Listener<TEvent> listener, final Order orderslot, final Addon addon) {
		this.listener = listener;
		this.orderslot = orderslot;
		this.addon = addon;
	}

	/**
	 * Gets the listener for this registration
	 * @return Registered Listener
	 */
	public Listener<TEvent> getListener() {
		return listener;
	}

	/**
	 * Gets the Addon for this registration
	 * @return Registered Addon
	 */
	public Addon getAddon() {
		return addon;
	}

	/**
	 * Gets the order slot for this registration
	 * @return Registered order
	 */
	public Order getOrder() {
		return orderslot;
	}
}
