package org.spoutcraft.spoutcraftapi.event.addon;

import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.event.HandlerList;

public class AddonDisableEvent extends AddonEvent<AddonDisableEvent> {
	private static final AddonDisableEvent instance = new AddonDisableEvent(null);
	public static final HandlerList<AddonDisableEvent> handlers = new HandlerList<AddonDisableEvent>();
	private Addon addon;

	public AddonDisableEvent(Addon addon) {
		this.addon = addon;
	}
	
	/**
	 * Gets the singleton, updates its state and returns it
	 * @param addon
	 * @return AddonDisableEvent singleton
	 */
	public static AddonDisableEvent getInstance(Addon addon) {
		instance.addon = addon;
		return instance;
	}
	
	/**
	 * Returns the Addon that was disabled
	 * @return Addon
	 */
	public Addon getAddon() {
		return addon;
	}

	@Override
	public HandlerList<AddonDisableEvent> getHandlers() {
		return handlers;
	}

	@Override
	protected String getEventName() {
		return "Addon Enable Event";
	}

}
