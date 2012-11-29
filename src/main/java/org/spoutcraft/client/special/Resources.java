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
package org.spoutcraft.client.special;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.util.config.Configuration;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.io.FileUtil;

public enum Resources implements YAMLResource {
	Special (new File(FileUtil.getSpoutcraftBaseDir(), "config" + File.separator + "special.yml")),

	VIP (new File(FileUtil.getSpoutcraftBaseDir(), "config" + File.separator + "vip.yml")),
	;

	final BaseYAMLResource resource;
	private Resources(File directory) {
		this.resource = new BaseYAMLResource(directory);
	}

	public Configuration getYAML() {
		return resource.getYAML();
	}

	public boolean updateYAML() {
		return resource.updateYAML();
	}

	private static List<Holiday> holidays = null;
	public static synchronized Holiday getHoliday() {
		if (holidays == null) {
			holidays = new ArrayList<Holiday>();
			try {
				Configuration config = Special.getYAML();
				for (String key : config.getKeys()) {
					try {
						@SuppressWarnings("unchecked")
						Map<String, Object> values = (Map<String, Object>) config.getProperty(key);
						long start = (Long) values.get("start");
						long end = (Long) values.get("end");
						String cape = (String) values.get("cape");
						String splash = (String) values.get("splash");
						boolean particles = (Boolean) values.get("particles");
						Holiday holiday = new Holiday(key, start, end, cape, splash, particles);
						holidays.add(holiday);
					} catch (Exception e) {
						//e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		long time = System.currentTimeMillis() / 1000;
		for (Holiday holiday : holidays) {
			if (time > holiday.getStart() && time < holiday.getEnd()) {
				return holiday;
			}
		}
		return null;
	}

	private static Map<String, VIP> vips;
	@SuppressWarnings("unchecked")
	public static VIP getVIP(String username) {
		if (vips == null) {
			vips = new ConcurrentHashMap<String, VIP>();
			Configuration config = VIP.getYAML();
			for (String key : config.getKeys()) {
				try {
					Map<String, Object> values = (Map<String, Object>) config.getProperty(key);
					key = key.toLowerCase();
					String title = (String) values.get("title");
					title = formatChatColors(title);
					String cape = (String) values.get("cape");
					String armor = (String) values.get("armor");
					float scale = 1f;
					if (values.containsKey("scale")) {
						scale = ((Number) values.get("scale")).floatValue();
					}
					Map<String, Integer> particles = (Map<String, Integer>) values.get("particles");
					Map<String, String> acs = (Map<String, String>) values.get("accessories");
					VIP vip = new VIP(key, title, cape, particles, acs, armor, scale);
					vips.put(key, vip);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return vips.get(username.toLowerCase());
	}
	
	public static String formatChatColors(String message) {
		message = message.replaceAll("(&([a-fA-F0-9]))", "\u00A7$2");
		message = message.replaceAll("(&([k-oK-O0-9]))", "\u00A7$2");
		message = message.replaceAll("(&([r|R]))", "\u00A7$2");
		return message;
	}

}
