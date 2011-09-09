package org.spoutcraft.spoutcraftapi.addon;

import java.io.File;
import java.util.regex.Pattern;

public abstract interface AddonLoader {

	public abstract Addon loadAddon(File paramFile) throws InvalidAddonException, InvalidAddonException, UnknownDependencyException, InvalidDescriptionException;

	public abstract Addon loadAddon(File paramFile, boolean paramBoolean) throws InvalidAddonException, InvalidAddonException, UnknownDependencyException, InvalidDescriptionException;

	public abstract Pattern[] getAddonFileFilters();

	public abstract void enableAddon(Addon paramAddon);

	public abstract void disableAddon(Addon paramAddon);

}
