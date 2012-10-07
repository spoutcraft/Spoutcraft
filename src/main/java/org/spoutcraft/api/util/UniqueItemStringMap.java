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
package org.spoutcraft.api.util;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class UniqueItemStringMap {
	private static final ConcurrentHashMap<Integer,String> reverse = new ConcurrentHashMap<Integer,String>();
	private static final ConcurrentHashMap<Integer,String> reverseStable = new ConcurrentHashMap<Integer,String>();
	private static final ConcurrentHashMap<String,Integer> forward = new ConcurrentHashMap<String,Integer>();

	private static final AtomicInteger idCounter = new AtomicInteger(1024);

	/*private static Configuration config;

	public static void setConfigFile(Configuration config) {
		UniqueItemStringMap.config = config;
		List<String> keys = config.getKeys();

		for (String key : keys) {
			Integer id = getIdFromFile(decodeKey(key));
			if (id != null) {
				forward.put(key, id);
				reverse.put(id, key);
				reverseStable.put(id, key);
			}
		}
	}

	private static Integer getIdFromFile(String key) {
		synchronized(config) {
			key = encodeKey(key);
			if (config.getProperty(key) == null) {
				return null;
			} else {
				int id = config.getInt(key, -1);
				if (id == -1) {
					config.removeProperty(key);
					return null;
				} else {
					return id;
				}
			}
		}
	}

	private static void setIdInFile(String key, int id) {
		synchronized(config) {
			key = encodeKey(key);
			config.setProperty(key, id);
			config.save();
		}
	}*/

	private static String encodeKey(String key) {
		key = key.replace("*", "-*-");
		return key.replace(".", "**");
	}

	private static String decodeKey(String key) {
		key = key.replace("**", ".");
		return key.replace("-*-", "*");
	}

	public static Set<Integer> getIds() {
		return reverse.keySet();
	}

	/**
	 * Associates a unique id for each string
	 *
	 * These associations persist over reloads and server restarts
	 *
	 * @param string the string to be associated
	 * @return the id associated with the string.
	 */
	public static int getId(String string) {
		string = encodeKey(string);

		Integer id = null;

		boolean success = false;
		int testId = idCounter.incrementAndGet() & 0x0FFFF;

		while (!success || id == null) {
			id = forward.get(string);
			if (id != null) {
				return id;
			}

			if (reverse.containsKey(testId)) { // ID already in use
				testId = idCounter.incrementAndGet() & 0x0FFFF;
				if (testId == 65535 || testId < 1024) {
					throw new RuntimeException("[Spout] Out of custom item ids");
				}
				continue;
			}

			String oldString = reverse.putIfAbsent(testId, string);

			if (oldString == null) { // Reverse link success
				Integer oldId = forward.putIfAbsent(string, testId);
				if (oldId != null) { // Forward link failed
					reverse.remove(testId, string); // Remove reverse link
					continue;
				}
				id = testId;
			} else { // Reverse link failed
				continue;
			}

			reverseStable.put(testId, string);

			//setIdInFile(decodeKey(string), id);

			success = true;

		}

		return id;
	}

	/**
	 * Returns the id associated with a string
	 *
	 * These associations persist over reloads and server restarts
	 *
	 * Note: . characters are replaced with * characters
	 *
	 * @param id the id
	 * @return the string associated with the id, or null if no string is associated
	 */
	public static String getString(int id) {
		return decodeKey(reverseStable.get(id));
	}
}
