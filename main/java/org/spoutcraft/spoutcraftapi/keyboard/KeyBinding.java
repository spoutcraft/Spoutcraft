package org.spoutcraft.spoutcraftapi.keyboard;

import java.io.Serializable;
import java.util.UUID;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;

public final class KeyBinding implements Serializable {
	private static final long serialVersionUID = 3241524501740640147L;
	private int key;
	private transient Addon addon;
	private String id;
	private String description;
	private transient UUID uuid = null;
	private String addonName;
	private BindingExecutionDelegate myDelegate = null;
	
	public KeyBinding() {
	}
	
	public KeyBinding(int key, String name, String id, String description) {
		this.key = key;
		this.addonName = name;
		this.description = description;
		this.id = id;
		this.addon = null;
	}
	
	public KeyBinding(int key, Addon addon, String id, String description) {
		this.key = key;
		this.addon = addon;
		this.description = description;
		this.id = id;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public Addon getAddon() {
		if(addon == null) {
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
		if(obj instanceof KeyBinding){
			KeyBinding other = (KeyBinding)obj;
			if(uuid != null && other.uuid != null) {
				return other.uuid.equals(this.uuid);
			} else {
				return this == obj;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (new HashCodeBuilder()).append(key).append(addon).append(id).append(uuid).toHashCode();
	}

	public void takeChanges(KeyBinding binding) {
		description = binding.description;
		uuid = binding.uuid;
		myDelegate = binding.myDelegate;
	}

	public String getAddonName() {
		if(addon == null) {
			addon = Spoutcraft.getAddonManager().getAddon(addonName);
		}
		if(addon != null) {
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
}
