package com.prupe.mcpatcher;

import com.prupe.mcpatcher.MCLogger$1;
import com.prupe.mcpatcher.MCLogger$ErrorLevel;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MCLogger {
	private static final HashMap allLoggers = new HashMap();
	public static final Level ERROR = new MCLogger$ErrorLevel();
	private final String logPrefix;
	private final Logger logger;

	public static MCLogger getLogger(String var0) {
		return getLogger(var0, var0);
	}

	public static synchronized MCLogger getLogger(String var0, String var1) {
		MCLogger var2 = (MCLogger)allLoggers.get(var0);

		if (var2 == null) {
			var2 = new MCLogger(var0, var1);
			allLoggers.put(var0, var2);
		}

		return var2;
	}

	private MCLogger(String var1, String var2) {
		this.logPrefix = var2;
		this.logger = Logger.getLogger(var1);
		this.logger.setLevel(Config.getLogLevel(var1));
		this.logger.setUseParentHandlers(false);
		this.logger.addHandler(new MCLogger$1(this));
	}

	public boolean isLoggable(Level var1) {
		return this.logger.isLoggable(var1);
	}

	public void setLevel(Level var1) {
		this.logger.setLevel(var1);
	}

	public void log(Level var1, String var2, Object ... var3) {
		if (this.isLoggable(var1)) {
			this.logger.log(var1, String.format(var2, var3));
		}
	}

	public void severe(String var1, Object ... var2) {
		this.log(Level.SEVERE, var1, var2);
	}

	public void error(String var1, Object ... var2) {
		this.log(ERROR, var1, var2);
	}

	public void warning(String var1, Object ... var2) {
		this.log(Level.WARNING, var1, var2);
	}

	public void info(String var1, Object ... var2) {
		this.log(Level.INFO, var1, var2);
	}

	public void config(String var1, Object ... var2) {
		this.log(Level.CONFIG, var1, var2);
	}

	public void fine(String var1, Object ... var2) {
		this.log(Level.FINE, var1, var2);
	}

	public void finer(String var1, Object ... var2) {
		this.log(Level.FINER, var1, var2);
	}

	public void finest(String var1, Object ... var2) {
		this.log(Level.FINEST, var1, var2);
	}

	static String access$000(MCLogger var0) {
		return var0.logPrefix;
	}
}
