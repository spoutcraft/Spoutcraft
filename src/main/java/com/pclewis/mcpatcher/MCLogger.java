package com.pclewis.mcpatcher;

import java.util.HashMap;
import java.util.logging.*;

public class MCLogger {
	private static final HashMap<String, MCLogger> allLoggers = new HashMap<String, MCLogger>();

	private final String logPrefix;
	private final Logger logger;

	public static MCLogger getLogger(String category) {
		return getLogger(category, category);
	}

	public static synchronized MCLogger getLogger(String category, String logPrefix) {
		MCLogger logger = allLoggers.get(category);
		if (logger == null) {
			logger = new MCLogger(category, logPrefix);
			allLoggers.put(category, logger);
		}
		return logger;
	}

	private MCLogger(String category, String logPrefix) {
		this.logPrefix = logPrefix;
		logger = Logger.getLogger(category);
		logger.setLevel(MCPatcherUtils.getLogLevel(category));
		logger.setUseParentHandlers(false);
		logger.addHandler(new Handler() {
			private final Formatter formatter = new Formatter() {
				@Override
				public String format(LogRecord record) {
					Level level = record.getLevel();
					if (level == Level.CONFIG) {
						return record.getMessage();
					} else {
						String message = record.getMessage();
						String prefix = "";
						while (message.startsWith("\n")) {
							prefix += "\n";
							message = message.substring(1);
						}
						return prefix + "[" + MCLogger.this.logPrefix + "] " + level.toString() + ": " + message;
					}
				}
			};

			@Override
			public void publish(LogRecord record) {
				System.out.println(formatter.format(record));
			}

			@Override
			public void flush() {
			}

			@Override
			public void close() throws SecurityException {
			}
		});
	}

	public boolean isLoggable(Level level) {
		return logger.isLoggable(level);
	}

	public void setLevel(Level level) {
		logger.setLevel(level);
	}

	public void log(Level level, String format, Object... params) {
		if (isLoggable(level)) {
			logger.log(level, String.format(format, params));
		}
	}

	public void config(String format, Object... params) {
		log(Level.CONFIG, format, params);
	}

	public void fine(String format, Object... params) {
		log(Level.FINE, format, params);
	}

	public void finer(String format, Object... params) {
		log(Level.FINER, format, params);
	}

	public void finest(String format, Object... params) {
		log(Level.FINEST, format, params);
	}

	public void info(String format, Object... params) {
		log(Level.INFO, format, params);
	}

	public void severe(String format, Object... params) {
		log(Level.SEVERE, format, params);
	}

	public void warning(String format, Object... params) {
		log(Level.WARNING, format, params);
	}
}