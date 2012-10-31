/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Spoutcraft is licensed under the GNU Lesser General Public License.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.client;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

class SpoutcraftLogger extends Logger {
	private Logger log = Logger.getLogger(SpoutClient.class.getName());
	protected SpoutcraftLogger() {
		super(SpoutClient.class.getName(), null);
	}

	@Override
	public void config(String msg) {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		if (wasSandboxed) {
			SpoutClient.disableSandbox();
		}
		try {
			log.config(msg);
		}
		finally {
			if (wasSandboxed) {
				SpoutClient.enableSandbox();
			}
		}
	}

	@Override
	public void entering(String sourceClass, String sourceMethod) {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		if (wasSandboxed) {
			SpoutClient.disableSandbox();
		}
		try {
			log.entering(sourceClass, sourceMethod);
		}
		finally {
			if (wasSandboxed) {
				SpoutClient.enableSandbox();
			}
		}
	}

	@Override
	public void entering(String sourceClass, String sourceMethod, Object param1) {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		if (wasSandboxed) {
			SpoutClient.disableSandbox();
		}
		try {
			log.entering(sourceClass, sourceMethod, param1);
		}
		finally {
			if (wasSandboxed) {
				SpoutClient.enableSandbox();
			}
		}
	}

	@Override
	public void entering(String sourceClass, String sourceMethod, Object[] params) {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		if (wasSandboxed) {
			SpoutClient.disableSandbox();
		}
		try {
			log.entering(sourceClass, sourceMethod, params);
		}
		finally {
			if (wasSandboxed) {
				SpoutClient.enableSandbox();
			}
		}
	}

	@Override
	public void exiting(String sourceClass, String sourceMethod) {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		if (wasSandboxed) {
			SpoutClient.disableSandbox();
		}
		try {
			log.exiting(sourceClass, sourceMethod);
		}
		finally {
			if (wasSandboxed) {
				SpoutClient.enableSandbox();
			}
		}
	}

	@Override
	public void exiting(String sourceClass, String sourceMethod, Object result) {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		if (wasSandboxed) {
			SpoutClient.disableSandbox();
		}
		try {
			log.exiting(sourceClass, sourceMethod, result);
		}
		finally {
			if (wasSandboxed) {
				SpoutClient.enableSandbox();
			}
		}
	}

	@Override
	public void fine(String msg) {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		if (wasSandboxed) {
			SpoutClient.disableSandbox();
		}
		try {
			log.fine(msg);
		}
		finally {
			if (wasSandboxed) {
				SpoutClient.enableSandbox();
			}
		}
	}

	@Override
	public void finer(String msg) {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		if (wasSandboxed) {
			SpoutClient.disableSandbox();
		}
		try {
			log.finer(msg);
		}
		finally {
			if (wasSandboxed) {
				SpoutClient.enableSandbox();
			}
		}
	}

	@Override
	public void finest(String msg) {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		if (wasSandboxed) {
			SpoutClient.disableSandbox();
		}
		try {
			log.finest(msg);
		}
		finally {
			if (wasSandboxed) {
				SpoutClient.enableSandbox();
			}
		}
	}

	@Override
	public void info(String msg) {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		if (wasSandboxed) {
			SpoutClient.disableSandbox();
		}
		try {
			log.info(msg);
		}
		finally {
			if (wasSandboxed) {
				SpoutClient.enableSandbox();
			}
		}
	}

	@Override
	public void log(Level level, String msg) {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		if (wasSandboxed) {
			SpoutClient.disableSandbox();
		}
		try {
			log.log(level, msg);
		}
		finally {
			if (wasSandboxed) {
				SpoutClient.enableSandbox();
			}
		}
	}

	@Override
	public void log(Level level, String msg, Object param1) {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		if (wasSandboxed) {
			SpoutClient.disableSandbox();
		}
		try {
			log.log(level, msg, param1);
		}
		finally {
			if (wasSandboxed) {
				SpoutClient.enableSandbox();
			}
		}
	}

	@Override
	public void log(Level level, String msg, Object[] params) {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		if (wasSandboxed) {
			SpoutClient.disableSandbox();
		}
		try {
			log.log(level, msg, params);
		}
		finally {
			if (wasSandboxed) {
				SpoutClient.enableSandbox();
			}
		}
	}

	@Override
	public void log(Level level, String msg, Throwable thrown) {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		if (wasSandboxed) {
			SpoutClient.disableSandbox();
		}
		try {
			log.log(level, msg, thrown);
		}
		finally {
			if (wasSandboxed) {
				SpoutClient.enableSandbox();
			}
		}
	}

	@Override
	public void log(LogRecord record) {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		if (wasSandboxed) {
			SpoutClient.disableSandbox();
		}
		try {
			log.log(record);
		}
		finally {
			if (wasSandboxed) {
				SpoutClient.enableSandbox();
			}
		}
	}

	@Override
	public void severe(String msg) {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		if (wasSandboxed) {
			SpoutClient.disableSandbox();
		}
		try {
			log.severe(msg);
		}
		finally {
			if (wasSandboxed) {
				SpoutClient.enableSandbox();
			}
		}
	}

	@Override
	public void warning(String msg) {
		boolean wasSandboxed = SpoutClient.isSandboxed();
		if (wasSandboxed) {
			SpoutClient.disableSandbox();
		}
		try {
			log.warning(msg);
		}
		finally {
			if (wasSandboxed) {
				SpoutClient.enableSandbox();
			}
		}
	}
}
