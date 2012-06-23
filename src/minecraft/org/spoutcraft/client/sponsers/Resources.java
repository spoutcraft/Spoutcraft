package org.spoutcraft.client.sponsers;

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
	public static VIP getVIP(String username) {
		if (vips == null) {
			vips = new ConcurrentHashMap<String, VIP>();
			Configuration config = VIP.getYAML();
			for (String key : config.getKeys()) {
				try {
					@SuppressWarnings("unchecked")
					Map<String, Object> values = (Map<String, Object>) config.getProperty(key);
					key = key.toLowerCase();
					String title = (String) values.get("title");
					title = SpoutClient.getInstance().getChatManager().formatChatColors(title);
					String cape = (String) values.get("cape");
					String particle = (String) values.get("particle");
					VIP vip = new VIP(key, title, cape, particle);
					vips.put(key, vip);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return vips.get(username.toLowerCase());
	}
}