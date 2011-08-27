package org.spoutcraft.api.addon;

import java.io.File;
import java.util.regex.Pattern;

import org.spoutcraft.api.event.Event;
import org.spoutcraft.api.event.Listener;

public abstract interface AddonLoader {
	
	  public abstract Addon loadAddon(File paramFile) throws InvalidAddonException, InvalidAddonException, UnknownDependencyException;

	  public abstract Addon loadAddon(File paramFile, boolean paramBoolean) throws InvalidAddonException, InvalidAddonException, UnknownDependencyException;

	  public abstract Pattern[] getAddonFileFilters();

	  public abstract EventExecutor createExecutor(Event.Type paramType, Listener paramListener);

	  public abstract void enableAddon(Addon paramAddon);

	  public abstract void disableAddon(Addon paramAddon);
	  
}
