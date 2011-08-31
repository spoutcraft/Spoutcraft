package org.spoutcraft.spoutcraftapi.addon;

import java.io.File;

import org.spoutcraft.spoutcraftapi.event.Event;
import org.spoutcraft.spoutcraftapi.event.Listener;

public abstract interface AddonManager {

	public abstract Addon getAddon(String paramString);

	public abstract Addon[] getAddons();

	public abstract boolean isAddonEnabled(String paramString);

	public abstract boolean isAddonEnabled(Addon paramAddon);

	public abstract Addon loadAddon(File paramFile) throws InvalidAddonException, InvalidDescriptionException, UnknownDependencyException;

	public abstract Addon[] loadAddons(File paramFile);

	public abstract void disableAddons();

	public abstract void clearAddons();

	public abstract void callEvent(Event paramEvent);

	public abstract void registerEvent(Event.Type paramType, Listener paramListener, Event.Priority paramPriority, Addon paramAddon);

	public abstract void registerEvent(Event.Type paramType, Listener paramListener, EventExecutor paramEventExecutor, Event.Priority paramPriority, Addon paramAddon);

	public abstract void enableAddon(Addon paramAddon);

	public abstract void disableAddon(Addon paramAddon);

}
