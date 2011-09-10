package org.spoutcraft.spoutcraftapi.addon;

import java.io.File;

import org.spoutcraft.spoutcraftapi.event.Event;

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

}
