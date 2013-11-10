package com.prupe.mcpatcher;

import com.prupe.mcpatcher.Config$FileEntry;
import java.util.List;

class Config$ModEntry {
	String type;
	boolean enabled;
	String path;
	String className;
	List<Config$FileEntry> files;
}
