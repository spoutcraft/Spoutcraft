package org.spoutcraft.spoutcraftapi.keyboard;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.spoutcraft.spoutcraftapi.Spoutcraft;

public class KeyBinding implements Serializable {
	private static final long serialVersionUID = 3241524501740640147L;
	private int key;
	private String plugin;
	private String id;
	private String description;
	
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

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof KeyBinding){
			KeyBinding other = (KeyBinding)obj;
			return (new EqualsBuilder()).append(key, other.key).append(plugin, other.plugin).append(id, other.id).append(description, other.description).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		//no description in here, because that's not necessary!
		return (new HashCodeBuilder()).append(key).append(plugin).append(id).toHashCode();
	}
}
