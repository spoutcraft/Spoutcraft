package org.spoutcraft.client.sponsers;

import org.bukkit.util.config.Configuration;

public interface YAMLResource {
	public Configuration getYAML();
	public boolean updateYAML();
}
