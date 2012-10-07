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
package org.spoutcraft.api.event.addon;

import org.spoutcraft.api.addon.Addon;
import org.spoutcraft.api.event.HandlerList;

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
