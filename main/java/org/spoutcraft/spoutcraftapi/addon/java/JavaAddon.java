package org.spoutcraft.spoutcraftapi.addon.java;

import java.io.File;

import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.addon.AddonDescriptionFile;
import org.spoutcraft.spoutcraftapi.addon.AddonLoader;

public abstract class JavaAddon implements Addon{

    private boolean initialized = false;
    private AddonLoader loader = null;
    private Spoutcraft spoutcraft = null;
    private File file = null;
    private File dataFolder = null;
    private AddonClassLoader classLoader = null;
	private boolean enabled = false;
	private AddonDescriptionFile description = null;
	private boolean naggable = false;
	
	public AddonDescriptionFile getDescription() {
		return description;
	}
	
	public void initialize(JavaAddonLoader loader, Spoutcraft spoutcraft, AddonDescriptionFile description, File dataFolder, File file, AddonClassLoader classLoader) {
		if (!initialized) {
            this.initialized = true;
            this.loader = loader;
            this.spoutcraft = spoutcraft;
            this.file = file;
            this.description = description;
            this.dataFolder = dataFolder;
            this.classLoader = classLoader;
        }
	}

	public abstract void onEnable();

	public abstract void onDisable();
	
	public File getFile() {
		return file;
	}
	
	public File getDataFolder() {
		return dataFolder;
	}
	
	public Spoutcraft getSpoutcraft() {
		return spoutcraft;
	}

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
	
	public final boolean isNaggable() {
        return naggable;
    }

    public final void setNaggable(boolean canNag) {
        this.naggable = canNag;
    }

	

	public AddonClassLoader getClassLoader() {
		return classLoader;
	}
	
}
