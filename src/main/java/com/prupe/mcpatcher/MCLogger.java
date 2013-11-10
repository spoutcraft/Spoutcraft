package com.prupe.mcpatcher;

import com.prupe.mcpatcher.MCLogger$1;
import com.prupe.mcpatcher.MCLogger$ErrorLevel;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MCLogger {
	private static final HashMap<String, MCLogger> allLoggers = new HashMap();
	public static final Level ERROR = new MCLogger$ErrorLevel();
	private static final long FLOOD_INTERVAL = 1000L;
	private static final long FLOOD_REPORT_INTERVAL = 5000L;
	private static final int FLOOD_LIMIT = 100;
	private static final int FLOOD_LEVEL = Level.CONFIG.intValue();
	private final String logPrefix;
	private final Logger logger;
	private boolean flooding;
	private long lastFloodReport;
	private int floodCount;
	private long lastMessage = System.currentTimeMillis();
	private boolean enabled = false;

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

	private boolean checkFlood() {
		long now = System.currentTimeMillis();
		boolean showFloodMessage = false;

		if (now - this.lastMessage > 1000L) {
			if (this.flooding) {
				this.reportFlooding(now);
				this.flooding = false;
			} else {
				this.floodCount = 0;
			}
		} else if (this.flooding && now - this.lastFloodReport > 5000L) {
			this.reportFlooding(now);
			showFloodMessage = true;
		}

		this.lastMessage = now;
		++this.floodCount;

		if (this.flooding) {
			return showFloodMessage;
		} else if (this.floodCount > 100) {
			this.flooding = true;
			this.lastFloodReport = now;
			this.reportFlooding(now);
			return false;
		} else {
			return true;
		}
	}

	private void reportFlooding(long now) {
		if (this.floodCount > 0) {
			this.logger.log(Level.WARNING, String.format("%d flood messages dropped in the last %ds", new Object[] {Integer.valueOf(this.floodCount), Long.valueOf((now - this.lastFloodReport) / 1000L)}));
		}

		this.floodCount = 0;
		this.lastFloodReport = now;
	}

	public boolean isLoggable(Level level) {
		return this.logger.isLoggable(level);
	}

	public void setLevel(Level level) {
		this.logger.setLevel(level);
	}

	public void log(Level level, String format, Object ... params) {
		if (this.isLoggable(level)) {
			if (level.intValue() >= FLOOD_LEVEL && !this.checkFlood()) {
				return;
			}

			this.logger.log(level, String.format(format, params));
		}
	}

	public void severe(String format, Object ... params) {
		if (enabled) {
			this.log(Level.SEVERE, format, params);
		}
	}

	public void error(String format, Object ... params) {
		if (enabled) {
			this.log(ERROR, format, params);
		}
	}

	public void warning(String format, Object ... params) {
		if (enabled) {
			this.log(Level.WARNING, format, params);
		}
	}

	public void info(String format, Object ... params) {
		if (enabled) {
			this.log(Level.INFO, format, params);
		}
	}

	public void config(String format, Object ... params) {
		if (enabled) {
			this.log(Level.CONFIG, format, params);
		}
	}

	public void fine(String format, Object ... params) {
		if (enabled) {
			this.log(Level.FINE, format, params);
		}
	}

	public void finer(String format, Object ... params) {
		if (enabled) {
			this.log(Level.FINER, format, params);
		}
	}

	public void finest(String format, Object ... params) {
		if (enabled) {
			this.log(Level.FINEST, format, params);
		}
	}

	static String access$000(MCLogger x0) {
		return x0.logPrefix;
	}
}
