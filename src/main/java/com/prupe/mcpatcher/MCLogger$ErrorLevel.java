package com.prupe.mcpatcher;

import java.util.logging.Level;

class MCLogger$ErrorLevel extends Level {
	protected MCLogger$ErrorLevel() {
		super("ERROR", (Level.WARNING.intValue() + Level.SEVERE.intValue()) / 2);
	}
}
