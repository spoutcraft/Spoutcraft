package org.spoutcraft.spoutcraftapi.addon.java;

import java.io.File;

import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.addon.AddonDescriptionFile;
import org.spoutcraft.spoutcraftapi.addon.AddonLoader;

public abstract class JavaAddon implements Addon{
	
	private boolean enabled = false;
	private AddonDescriptionFile description = null;
	private AddonLoader loader = null;
	
	public AddonDescriptionFile getDescription() {
		return description;
	}

	public abstract void onEnable();

	public abstract void onDisable();

	public AddonLoader getAddonLoader() {
		return loader;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean arg) {
		if (this.enabled != arg) {		
			this.enabled = arg;
			if (this.enabled) {
				this.onEnable();
			} else {
				this.onDisable();
			}
		}
	}

	public void initialize(JavaAddonLoader javaAddonLoader, Spoutcraft spoutcraft, AddonDescriptionFile description2, File dataFolder, File file, AddonClassLoader loader2) {
		// TODO Auto-generated method stub
		
	}

	public AddonClassLoader getClassLoader() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
