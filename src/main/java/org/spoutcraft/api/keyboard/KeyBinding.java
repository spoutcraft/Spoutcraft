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
package org.spoutcraft.api.keyboard;

import java.io.Serializable;
import java.util.UUID;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.addon.Addon;

public final class KeyBinding extends AbstractBinding implements Serializable {
	private static final long serialVersionUID = 3241524501740640147L;
	private transient Addon addon;
	private String id;
	private String description;
	private transient UUID uuid = null;
	private String addonName;
	private transient BindingExecutionDelegate myDelegate = null;

	public KeyBinding() {
	}

	public KeyBinding(int key, String name, String id, String description) {
		setKey(key);
		this.addonName = name;
		this.description = description;
		this.id = id;
		this.addon = null;
	}

	public KeyBinding(int key, Addon addon, String id, String description) {
		setKey(key);
		this.addon = addon;
		this.addonName = addon.getDescription().getName();
		this.description = description;
		this.id = id;
	}

	public Addon getAddon() {
		if (addon == null) {
			addon = Spoutcraft.getAddonManager().getAddon(addonName);
		}
		return addon;
	}

	public void setAddon(Addon addon) {
		this.addon = addon;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public String getTitle() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setUniqueId(UUID uuid) {
		this.uuid = uuid;
	}

	public UUID getUniqueId() {
		return uuid;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof KeyBinding) {
			KeyBinding other = (KeyBinding)obj;
			if (uuid != null && other.uuid != null) {
				return other.uuid.equals(this.uuid);
			} else {
				return this == obj;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (new HashCodeBuilder()).append(getKey()).append(addon).append(id).append(uuid).toHashCode();
	}

	public void takeChanges(KeyBinding binding) {
		description = binding.description;
		uuid = binding.uuid;
		myDelegate = binding.myDelegate;
	}

	public String getAddonName() {
		if (addon == null) {
			addon = Spoutcraft.getAddonManager().getAddon(addonName);
		}
		if (addon != null) {
			return addon.getDescription().getName();
		} else {
			return addonName;
		}
	}

	public void setDelegate(BindingExecutionDelegate myDelegate) {
		this.myDelegate = myDelegate;
	}

	public BindingExecutionDelegate getDelegate() {
		return myDelegate;
	}

	public void summon(int key, boolean keyReleased, int screen) {
		Spoutcraft.getKeyBindingManager().summon(this, key, keyReleased, screen);
	}

	@Override
	public boolean matches(int key, byte modifiers) {
		if (myDelegate == null && uuid == null) {
			return false;
		}
		return super.matches(key, modifiers);
	}
}
