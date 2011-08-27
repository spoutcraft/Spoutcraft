package org.bukkit;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({ "rawtypes", "unchecked" })
public enum ChatColor {
	BLACK(0),

	DARK_BLUE(1),

	DARK_GREEN(2),

	DARK_AQUA(3),

	DARK_RED(4),

	DARK_PURPLE(5),

	GOLD(6),

	GRAY(7),

	DARK_GRAY(8),

	BLUE(9),

	GREEN(10),

	AQUA(11),

	RED(12),

	LIGHT_PURPLE(13),

	YELLOW(14),

	WHITE(15);

	private final int code;
	private static final Map<Integer, ChatColor> colors;

	private ChatColor(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}

	public String toString() {
		return String.format("§%x", new Object[] { Integer.valueOf(this.code) });
	}

	public static ChatColor getByCode(int code) {
		return (ChatColor) colors.get(Integer.valueOf(code));
	}

	public static String stripColor(String input) {
		if (input == null) {
			return null;
		}

		return input.replaceAll("(?i)§[0-F]", "");
	}

	static {
		colors = new HashMap();

		for (ChatColor color : values())
			colors.put(Integer.valueOf(color.getCode()), color);
	}
}