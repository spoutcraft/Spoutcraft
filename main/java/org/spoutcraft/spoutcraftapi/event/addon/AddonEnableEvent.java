package org.spoutcraft.spoutcraftapi.event.addon;

import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.event.HandlerList;

public class AddonEnableEvent extends AddonEvent<AddonEnableEvent> {
	private static final AddonEnableEvent instance = new AddonEnableEvent(null);
	public static final HandlerList<AddonEnableEvent> handlers = new HandlerList<AddonEnableEvent>();
	private Addon addon;

	public AddonEnableEvent(Addon addon) {
		this.addon = addon;
	}
	
	/**
	 * Gets the singleton, updates its state and returns it
	 * @param addon
	 * @return AddonDisableEvent singleton
	 */
	public static AddonEnableEvent getInstance(Addon addon) {
		instance.addon = addon;
		return instance;
	}
	
	/**
	 * Returns the Addon that was enabled
	 * @return Addon
	 */
	public Addon getAddon() {
		return addon;
	}

	@Override
	public HandlerList<AddonEnableEvent> getHandlers() {
		return handlers;
	}

	@Override
	protected String getEventName() {
		return "Addon Enable Event";
	}

}