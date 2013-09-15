package com.prupe.mcpatcher;

import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

class MCLogger$1 extends Handler {
	private final Formatter formatter;

	final MCLogger this$0;

	MCLogger$1(MCLogger var1) {
		this.this$0 = var1;
		this.formatter = new MCLogger$1$1(this);
	}

	public void publish(LogRecord record) {
		System.out.println(this.formatter.format(record));
	}

	public void flush() {}

	public void close() throws SecurityException {}
}
