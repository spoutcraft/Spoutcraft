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
package org.spoutcraft.api.addon.java;

import java.io.File;
import java.util.logging.Logger;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import org.spoutcraft.api.Client;
import org.spoutcraft.api.UnsafeMethod;
import org.spoutcraft.api.addon.Addon;
import org.spoutcraft.api.addon.AddonDescriptionFile;
import org.spoutcraft.api.addon.AddonLoader;
import org.spoutcraft.api.command.Command;
import org.spoutcraft.api.command.CommandSender;
import org.spoutcraft.api.gui.ScrollArea;

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

	@UnsafeMethod
	public JavaAddon() {
	}

	public final AddonDescriptionFile getDescription() {
		return description;
	}

	public final void initialize(JavaAddonLoader loader, Client client, AddonDescriptionFile description, File dataFolder, File file, AddonClassLoader classLoader) {
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

	@UnsafeMethod
	public abstract void onEnable();

	@UnsafeMethod
	public abstract void onDisable();

	public final File getFile() {
		return file;
	}

	public final File getDataFolder() {
		return dataFolder;
	}

	public final Client getClient() {
		return client;
	}

	public final AddonLoader getAddonLoader() {
		return loader;
	}

	public final boolean isEnabled() {
		return enabled;
	}

	public final Logger getLogger() {
		return client.getLogger();
	}

	@UnsafeMethod
	public void onLoad() {
	}

	@UnsafeMethod
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

	public final AddonClassLoader getClassLoader() {
		return classLoader;
	}

	@UnsafeMethod
	public boolean onCommand(CommandSender paramCommandSender, Command paramCommand, String paramString, String[] paramArrayOfString) {
		return false;
	}

	@Override
	public final int hashCode() {
		return (new HashCodeBuilder().append(file).append(dataFolder).append(description!=null?description.getName():"").toHashCode());
	}

	public boolean hasConfigurationGUI() {
		return false;
	}

	public void setupConfigurationGUI(ScrollArea screen) {
	}
}
