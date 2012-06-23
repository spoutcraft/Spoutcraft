package org.spoutcraft.client.sponsers;

import java.io.File;

import org.bukkit.util.config.Configuration;

public class BaseYAMLResource implements YAMLResource {
	private Configuration cached = null;
	private final File localCache;
	public BaseYAMLResource(File file) {
		this.localCache = file;
	}

	public synchronized Configuration getYAML() {
		updateYAML();
		return cached;
	}

	public synchronized boolean updateYAML() {
		if (cached == null) {
			//Setup cached processor
			cached = new Configuration(localCache);
			cached.load();
			return true;
		}
		return false;
	}
}
