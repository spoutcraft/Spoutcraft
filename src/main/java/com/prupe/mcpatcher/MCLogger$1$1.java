package com.prupe.mcpatcher;

import com.prupe.mcpatcher.MCLogger$1;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

class MCLogger$1$1 extends Formatter {
	final MCLogger$1 this$1;

	MCLogger$1$1(MCLogger$1 var1) {
		this.this$1 = var1;
	}

	public String format(LogRecord record) {
		Level level = record.getLevel();

		if (level == Level.CONFIG) {
			return record.getMessage();
		} else {
			String message = record.getMessage();
			String prefix;

			for (prefix = ""; message.startsWith("\n"); message = message.substring(1)) {
				prefix = prefix + "\n";
			}

			return prefix + "[" + MCLogger.access$000(this.this$1.this$0) + "] " + level.toString() + ": " + message;
		}
	}
}
