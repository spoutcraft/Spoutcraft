package com.prupe.mcpatcher;

import com.prupe.mcpatcher.Config$VersionEntry;
import java.util.LinkedHashMap;

class Config$ProfileEntry {
	String original;
	String version;
	LinkedHashMap<String, LinkedHashMap<String, String>> config = new LinkedHashMap();
	LinkedHashMap<String, Config$VersionEntry> versions = new LinkedHashMap();

	private LinkedHashMap<String, String> getModConfig(String mod) {
		LinkedHashMap map = (LinkedHashMap)this.config.get(mod);

		if (map == null) {
			map = new LinkedHashMap();
			this.config.put(mod, map);
		}

		return map;
	}

	static LinkedHashMap access$000(Config$ProfileEntry x0, String x1) {
		return x0.getModConfig(x1);
	}
}
