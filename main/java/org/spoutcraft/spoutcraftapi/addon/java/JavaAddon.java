/*
 * This file is part of Spoutcraft API (http://wiki.getspout.org/).
 * 
 * Spoutcraft API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
			this.loader = loader;
			this.client = client;
			this.file = file;
			this.description = description;
			this.dataFolder = dataFolder;
			this.classLoader = classLoader;
			this.initialized = true;
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
	
	public void onLoad() {
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

	public final void setNaggable(boolean naggable) {
		this.naggable = naggable;
	}

	public AddonClassLoader getClassLoader() {
		return classLoader;
	}

	public boolean onCommand(CommandSender paramCommandSender, Command paramCommand, String paramString, String[] paramArrayOfString) {
		return false;
	}

}
