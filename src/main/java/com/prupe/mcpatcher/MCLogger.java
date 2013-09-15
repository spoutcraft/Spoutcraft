package com.prupe.mcpatcher;

import com.prupe.mcpatcher.MCLogger$1;
import com.prupe.mcpatcher.MCLogger$ErrorLevel;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MCLogger {
	private static final HashMap<String, MCLogger> allLoggers = new HashMap();
	public static final Level ERROR = new MCLogger$ErrorLevel();
	private final String logPrefix;
	private final Logger logger;

	public static MCLogger getLogger(String category) {
		return getLogger(category, category);
	}

	public static synchronized MCLogger getLogger(String category, String logPrefix) {
		MCLogger logger = (MCLogger)allLoggers.get(category);

		if (logger == null) {
			logger = new MCLogger(category, logPrefix);
			allLoggers.put(category, logger);
		}

		return logger;
	}

	private MCLogger(String category, String logPrefix) {
		this.logPrefix = logPrefix;
		this.logger = Logger.getLogger(category);
		this.logger.setLevel(Config.getLogLevel(category));
		this.logger.setUseParentHandlers(false);
		this.logger.addHandler(new MCLogger$1(this));
	}

	public boolean isLoggable(Level level) {
		return this.logger.isLoggable(level);
	}

	public void setLevel(Level level) {
		this.logger.setLevel(level);
	}

	public void log(Level level, String format, Object ... params) {
		if (this.isLoggable(level)) {
			this.logger.log(level, String.format(format, params));
		}
	}

	public void severe(String format, Object ... params) {
		this.log(Level.SEVERE, format, params);
	}

	public void error(String format, Object ... params) {
		this.log(ERROR, format, params);
	}

	public void warning(String format, Object ... params) {
		this.log(Level.WARNING, format, params);
	}

	public void info(String format, Object ... params) {
		this.log(Level.INFO, format, params);
	}

	public void config(String format, Object ... params) {
		this.log(Level.CONFIG, format, params);
	}

	public void fine(String format, Object ... params) {
		this.log(Level.FINE, format, params);
	}

	public void finer(String format, Object ... params) {
		this.log(Level.FINER, format, params);
	}

	public void finest(String format, Object ... params) {
		this.log(Level.FINEST, format, params);
	}

	static String access$000(MCLogger x0) {
		return x0.logPrefix;
	}
}
