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

	public String format(LogRecord var1) {
		Level var2 = var1.getLevel();

		if (var2 == Level.CONFIG) {
			return var1.getMessage();
		} else {
			String var3 = var1.getMessage();
			String var4;

			for (var4 = ""; var3.startsWith("\n"); var3 = var3.substring(1)) {
				var4 = var4 + "\n";
			}

			return var4 + "[" + MCLogger.access$000(this.this$1.this$0) + "] " + var2.toString() + ": " + var3;
		}
	}
}
