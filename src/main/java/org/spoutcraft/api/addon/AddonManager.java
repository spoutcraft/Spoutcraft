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
package org.spoutcraft.api.addon;

import java.io.File;

import org.spoutcraft.api.event.Event;

public abstract interface AddonManager {
	public abstract Addon getAddon(String paramString);

	public abstract Addon[] getAddons();

	public abstract boolean isAddonEnabled(String paramString);

	public abstract boolean isAddonEnabled(Addon paramAddon);

	public abstract Addon loadAddon(File paramFile) throws InvalidAddonException, InvalidDescriptionException, UnknownDependencyException;

	public abstract Addon[] loadAddons(File paramFile);

	public abstract void disableAddons();

	public abstract void clearAddons();

	public abstract <TEvent extends Event<TEvent>> void callEvent(TEvent event);

	public abstract void enableAddon(Addon paramAddon);

	public abstract void disableAddon(Addon paramAddon);

	public abstract Addon getOrCreateAddon(String readString);

	public abstract ThreadGroup getSecurityThreadGroup();
}
