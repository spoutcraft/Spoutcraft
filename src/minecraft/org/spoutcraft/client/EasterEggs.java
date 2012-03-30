/*
 * This file is part of Spoutcraft (http://www.spout.org/).
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.client.Minecraft;

import org.bukkit.ChatColor;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

public final class EasterEggs {
	private static final Yaml yaml = new Yaml(new SafeConstructor());
	private static final File file = new File(Minecraft.getMinecraftDir(), "spoutcraft" + File.separator + "special.yml");
	private static final List<EasterEgg> eggs = new ArrayList<EasterEgg>();
	private static Map<String, Object> map = null;
	private static long colorUpdate = 0;
	private static int titleColor = -1;
	private static String lastColor = "WHITE";

	@SuppressWarnings("unchecked")
	private static Map<String, Object> getMap() {
		if (!file.exists())
			return null;
		Map<String, Object> result = null;
		try {
			result = (Map<String, Object>) yaml.load(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private static void buildEasterEggs() {
		if (map != null) {
			for (String key : map.keySet()) {
				EasterEgg egg = new EasterEgg(key);
				Map<String, Object> eggmap = null;
				try {
					eggmap = (Map<String, Object>) map.get(key);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

				if (eggmap.containsKey("start")) {
					try {
						long start = (Long) eggmap.get("start");
						egg.setStart(start);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}

				if (eggmap.containsKey("end")) {
					try {
						long end = (Long) eggmap.get("end");
						egg.setEnd(end);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}

				if (eggmap.containsKey("titlecolor")) {
					try {
						List<String> titlecolor = (ArrayList<String>) eggmap.get("titlecolor");
						egg.setTitlecolor(titlecolor);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}

				if (eggmap.containsKey("splash")) {
					try {
						String splash = (String) eggmap.get("splash");
						egg.setSplash(splash);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}

				if (eggmap.containsKey("skin")) {
					try {
						String skin = (String) eggmap.get("skin");
						egg.setSkin(skin);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}

				if (eggmap.containsKey("cape")) {
					try {
						String cape = (String) eggmap.get("cape");
						egg.setCape(cape);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}

				eggs.add(egg);
			}
		}
	}

	public static String getEasterEggTitle(String user) {
		if (user.equalsIgnoreCase("Afforess")) {
			return ChatColor.DARK_BLUE + "Afforess";
		}
		
		if (user.equalsIgnoreCase("Wulfspider")) {
			return ChatColor.BLUE + "Wulfspider";
		}
		
		if (user.equalsIgnoreCase("alta189")) {
			return ChatColor.DARK_GREEN + "alta189";
		}
		
		if (user.equalsIgnoreCase("Raphfrk")) {
			return ChatColor.GREEN + "Raphfrk";
		}
		
		if (user.equalsIgnoreCase("narrowtux")) {
			return ChatColor.GOLD + "narrowtux";
		}
		
		if (user.equalsIgnoreCase("Top_Cat")) {
			return ChatColor.RED + "T" + ChatColor.DARK_RED + "o" + ChatColor.YELLOW + "p" + ChatColor.GREEN + "_" + ChatColor.DARK_GREEN + "C" + ChatColor.BLUE + "a" + ChatColor.LIGHT_PURPLE + "t";
		}
		
		if (user.equalsIgnoreCase("Olloth")) {
			return ChatColor.DARK_RED + "Olloth";
		}
		
		if (user.equalsIgnoreCase("Kylegar")) {
			return ChatColor.AQUA + "Roy" + ChatColor.GREEN + "Awesome";
		}

		if (user.equalsIgnoreCase("zml2008")) {
			return ChatColor.DARK_PURPLE + "zml2008";
		}
		
		if (user.equalsIgnoreCase("NinjaZidane")) {
			return ChatColor.BLACK + "Ninja" + ChatColor.BLUE + "Zidane";
		}
		return null;
	}

	public static int getEasterEggTitleColor() {
		if (map == null) {
			map = getMap();
			buildEasterEggs();
		}
		long current = System.currentTimeMillis();
		for (EasterEgg egg : eggs) {
			if (egg.getStart() <= current && egg.getEnd() >= current) {
				if (egg.getTitlecolor() != null && !egg.getTitlecolor().isEmpty()) {
					if (current >= colorUpdate) {
						titleColor = getNewTitleColor(egg);
						colorUpdate = current + 300000;
					}
					return titleColor;

				}
			}
		}
		return -1;
	}

	private static int getNewTitleColor(EasterEgg egg) {
		String result = "";
		int tries = 0;
		boolean stop = false;
		while (!stop) {
			List<String> colors = egg.getTitlecolor();
			if (colors.size() > 1) {
				int index = new Random().nextInt(colors.size());
				result = colors.get(index);
			} else {
				result = colors.get(0);
			}
			if (!result.equals(lastColor) || tries > 10) {
				stop = true;
			}
			tries++;
		}
		lastColor = result;
		try {
			ChatColor c = ChatColor.valueOf(result);
			titleColor = convert(c);
		} catch (Exception e) {
			titleColor = -1;
		}

		return titleColor;
	}

	public static String getSplashTextEasterEgg() {
		if (map == null) {
			map = getMap();
			buildEasterEggs();
		}
		long current = System.currentTimeMillis();
		for (EasterEgg egg : eggs) {
			if (egg.getStart() <= current && egg.getEnd() >= current) {
				if (egg.getSplash() != null) {
					return egg.getSplash();
				}
			}
		}
		return null;
	}

	public static String getEasterEggCape() {
		if (map == null) {
			map = getMap();
			buildEasterEggs();
		}
		long current = System.currentTimeMillis();
		for (EasterEgg egg : eggs) {
			if (egg.getStart() <= current && egg.getEnd() >= current) {
				if (egg.getCape() != null) {
					return egg.getCape();
				}
			}
		}
		return null;
	}

	public static String getEasterEggSkin() {
		if (map == null) {
			map = getMap();
			buildEasterEggs();
		}
		long current = System.currentTimeMillis();
		for (EasterEgg egg : eggs) {
			if ((egg.getStart() <= current && egg.getEnd() >= current) || (egg.getStart() == 0 && egg.getEnd() == 0)) {
				if (egg.getSkin() != null) {
					return egg.getSkin();
				}
			}

		}
		return null;
	}

	private static int convert(ChatColor c) {
		switch (c) {
			case AQUA:
				return 0x55FFFF;
			case BLACK:
				return 0x000000;
			case BLUE:
				return 0x5555FF;
			case DARK_AQUA:
				return 0x00AAAA;
			case DARK_BLUE:
				return 0x0000AA;
			case DARK_GRAY:
				return 0x555555;
			case DARK_GREEN:
				return 0x00AA00;
			case DARK_PURPLE:
				return 0xAA00AA;
			case DARK_RED:
				return 0xAA0000;
			case GOLD:
				return 0xFFAA00;
			case GRAY:
				return 0xAAAAAA;
			case GREEN:
				return 0x55FF55;
			case LIGHT_PURPLE:
				return 0xFF55FF;
			case RED:
				return 0xFF5555;
			case WHITE:
				return 0xFFFFFF;
			case YELLOW:
				return 0xFFFF55;
		}
		return -1;
	}
}
