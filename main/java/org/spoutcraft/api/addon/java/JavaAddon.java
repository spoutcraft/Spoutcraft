package org.spoutcraft.api.addon.java;

import org.spoutcraft.api.addon.Addon;
import org.spoutcraft.api.addon.AddonDescriptionFile;
import org.spoutcraft.api.addon.AddonLoader;

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
	
}
