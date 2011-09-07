package org.spoutcraft.spoutcraftapi.addon.java;

import java.io.File;

import org.spoutcraft.spoutcraftapi.Client;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.addon.AddonDescriptionFile;
import org.spoutcraft.spoutcraftapi.addon.AddonLoader;
import org.spoutcraft.spoutcraftapi.command.Command;
import org.spoutcraft.spoutcraftapi.command.CommandSender;

public abstract class JavaAddon implements Addon {

	private boolean initialized = false;
	private AddonLoader loader = null;
	private Client client = null;
	private File file = null;
	private File dataFolder = null;
	private AddonClassLoader classLoader = null;
	private boolean enabled = false;
	private AddonDescriptionFile description = null;
	private boolean naggable = false;

	public AddonDescriptionFile getDescription() {
		return description;
	}

	public void initialize(JavaAddonLoader loader, Client client, AddonDescriptionFile description, File dataFolder, File file, AddonClassLoader classLoader) {
		if (!initialized) {
			this.initialized = true;
			this.loader = loader;
			this.client = client;
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

	public Client getClient() {
		return client;
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
	
	public boolean onCommand(CommandSender paramCommandSender, Command paramCommand, String paramString, String[] paramArrayOfString) {
		return false;
	}

}
