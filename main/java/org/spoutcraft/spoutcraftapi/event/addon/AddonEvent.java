package org.spoutcraft.spoutcraftapi.event.addon;

import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.event.Event;

public abstract class AddonEvent <TEvent extends AddonEvent<TEvent>> extends Event<TEvent>{
	protected Addon addon;
	
	public abstract Addon getAddon();
}
