package org.spoutcraft.spoutcraftapi.keyboard;

import java.io.Serializable;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.spoutcraft.spoutcraftapi.Spoutcraft;

public class KeyBinding implements Serializable {
	private static final long serialVersionUID = 3241524501740640147L;
	private int key;
	private String plugin;
	private String id;
	private String description;
	private transient UUID uuid;
	
	public KeyBinding() {
		
	}
	
	public KeyBinding(int key, String plugin, String id, String description) {
		this.key = key;
		this.plugin = plugin;
		this.description = description;
		this.id = id;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getPlugin() {
		return plugin;
	}

	public void setPlugin(String plugin) {
		this.plugin = plugin;
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
			return other.uuid.equals(this.uuid);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (new HashCodeBuilder()).append(key).append(plugin).append(id).append(uuid).toHashCode();
	}

	public void takeChanges(KeyBinding binding) {
		description = binding.description;
		uuid = binding.uuid;
	}
}
