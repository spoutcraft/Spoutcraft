package com.pclewis.mcpatcher;

import net.minecraft.client.Minecraft;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;

/**
 * Collection of static methods available to mods at runtime.  This class is always injected into
 * the output minecraft jar.
 */
public class MCPatcherUtils {
	private static File minecraftDir = null;
	private static boolean debug = false;
	private static boolean isGame;
	static Config config = null;

	private static Minecraft minecraft;
	private static String minecraftVersion;
	private static String patcherVersion;

	public static final String HD_TEXTURES = "HD Textures";
	public static final String HD_FONT = "HD Font";
	public static final String BETTER_GRASS = "Better Grass";
	public static final String RANDOM_MOBS = "Random Mobs";
	public static final String CUSTOM_COLORS = "Custom Colors";
	public static final String BETTER_GLASS = "Better Glass";
	public static final String GLSL_SHADERS = "GLSL Shaders";

	public static final String UTILS_CLASS = "com.pclewis.mcpatcher.MCPatcherUtils";
	public static final String CONFIG_CLASS = "com.pclewis.mcpatcher.Config";

	public static final String TILE_SIZE_CLASS = "com.pclewis.mcpatcher.mod.TileSize";
	public static final String TEXTURE_UTILS_CLASS = "com.pclewis.mcpatcher.mod.TextureUtils";
	public static final String CUSTOM_ANIMATION_CLASS = "com.pclewis.mcpatcher.mod.CustomAnimation";
	public static final String RANDOM_MOBS_CLASS = "com.pclewis.mcpatcher.mod.MobRandomizer";
	public static final String COLORIZER_CLASS = "com.pclewis.mcpatcher.mod.Colorizer";
	public static final String COLOR_MAP_CLASS = "com.pclewis.mcpatcher.mod.ColorMap";
	public static final String BIOME_HELPER_CLASS = "com.pclewis.mcpatcher.mod.BiomeHelper";
	public static final String SHADERS_CLASS = "com.pclewis.mcpatcher.mod.Shaders";

	private MCPatcherUtils() {
	}

	static {
		isGame = true;
		try {
			if (Class.forName("com.pclewis.mcpatcher.MCPatcher") != null) {
				isGame = false;
			}
		} catch (ClassNotFoundException e) {
		} catch (Throwable e) {
			e.printStackTrace();
		}

		if (isGame) {
			if (setGameDir(new File(".")) || setGameDir(getDefaultGameDir())) {
				System.out.println("MCPatcherUtils initialized. Directory " + minecraftDir.getPath());
			} else {
				System.out.println("MCPatcherUtils initialized. Current directory " + new File(".").getAbsolutePath());
			}
		}
	}

	static File getDefaultGameDir() {
		String os = System.getProperty("os.name").toLowerCase();
		String baseDir = null;
		String subDir = ".minecraft";
		if (os.contains("win")) {
			baseDir = System.getenv("APPDATA");
		} else if (os.contains("mac")) {
			subDir = "Library/Application Support/minecraft";
		}
		if (baseDir == null) {
			baseDir = System.getProperty("user.home");
		}
		return new File(baseDir, subDir);
	}

	static boolean setGameDir(File dir) {
		if (dir != null &&
			dir.isDirectory() &&
			new File(dir, "bin/lwjgl.jar").exists() &&
			new File(dir, "resources").isDirectory()) {
			minecraftDir = dir.getAbsoluteFile();
		} else {
			minecraftDir = null;
		}
		return loadProperties();
	}

	private static boolean loadProperties() {
		config = null;
		if (minecraftDir != null && minecraftDir.exists()) {
			try {
				config = new Config(minecraftDir);
			} catch (Exception e) {
				e.printStackTrace();
			}
			debug = getBoolean(Config.TAG_DEBUG, false);
			return true;
		}
		return false;
	}

	/**
	 * Get the path to a file/directory within the minecraft folder.
	 *
	 * @param subdirs zero or more path components
	 * @return combined path
	 */
	public static File getMinecraftPath(String... subdirs) {
		File f = minecraftDir;
		for (String s : subdirs) {
			f = new File(f, s);
		}
		return f;
	}

	/**
	 * Write a debug message to minecraft standard output.
	 *
	 * @param format printf-style format string
	 * @param params printf-style parameters
	 */
	public static void log(String format, Object... params) {
		if (debug) {
			System.out.printf(format + "\n", params);
		}
	}

	/**
	 * Returns true if running inside game, false if running inside MCPatcher.  Useful for
	 * code shared between mods and runtime classes.
	 *
	 * @return true if in game
	 */
	public static boolean isGame() {
		return isGame;
	}

	/**
	 * Write a warning message to minecraft standard output.
	 *
	 * @param format printf-style format string
	 * @param params printf-style parameters
	 */
	public static void warn(String format, Object... params) {
		System.out.printf("WARNING: " + format + "\n", params);
	}

	/**
	 * Write an error message to minecraft standard output.
	 *
	 * @param format printf-style format string
	 * @param params printf-style parameters
	 */
	public static void error(String format, Object... params) {
		System.out.printf("ERROR: " + format + "\n", params);
	}

	/**
	 * Gets a value from mcpatcher.xml.
	 *
	 * @param mod		  name of mod
	 * @param tag		  property name
	 * @param defaultValue default value if not found in .properties file
	 * @return String value
	 */
	public static String getString(String mod, String tag, Object defaultValue) {
		if (config == null) {
			return defaultValue == null ? null : defaultValue.toString();
		}
		String value = config.getModConfigValue(mod, tag);
		if (value == null && defaultValue != null) {
			value = defaultValue.toString();
			config.setModConfigValue(mod, tag, value);
		}
		return value;
	}

	/**
	 * Gets a value from mcpatcher.xml.
	 *
	 * @param tag		  property name
	 * @param defaultValue default value if not found in .properties file
	 * @return String value
	 */
	public static String getString(String tag, Object defaultValue) {
		if (config == null) {
			return defaultValue == null ? null : defaultValue.toString();
		}
		String value = config.getConfigValue(tag);
		if (value == null && defaultValue != null) {
			value = defaultValue.toString();
			config.setConfigValue(tag, value);
		}
		return value;
	}

	/**
	 * Gets a value from mcpatcher.xml.
	 *
	 * @param mod		  name of mod
	 * @param tag		  property name
	 * @param defaultValue default value if not found in .properties file
	 * @return int value or 0
	 */
	public static int getInt(String mod, String tag, int defaultValue) {
		int value;
		try {
			value = Integer.parseInt(getString(mod, tag, defaultValue));
		} catch (NumberFormatException e) {
			value = defaultValue;
		}
		return value;
	}

	/**
	 * Gets a value from mcpatcher.xml.
	 *
	 * @param tag		  property name
	 * @param defaultValue default value if not found in .properties file
	 * @return int value or 0
	 */
	public static int getInt(String tag, int defaultValue) {
		int value;
		try {
			value = Integer.parseInt(getString(tag, defaultValue));
		} catch (NumberFormatException e) {
			value = defaultValue;
		}
		return value;
	}

	/**
	 * Gets a value from mcpatcher.xml.
	 *
	 * @param mod		  name of mod
	 * @param tag		  property name
	 * @param defaultValue default value if not found in .properties file
	 * @return boolean value
	 */
	public static boolean getBoolean(String mod, String tag, boolean defaultValue) {
		String value = getString(mod, tag, defaultValue).toLowerCase();
		if (value.equals("false")) {
			return false;
		} else if (value.equals("true")) {
			return true;
		} else {
			return defaultValue;
		}
	}

	/**
	 * Gets a value from mcpatcher.xml.
	 *
	 * @param tag		  property name
	 * @param defaultValue default value if not found in .properties file
	 * @return boolean value
	 */
	public static boolean getBoolean(String tag, boolean defaultValue) {
		String value = getString(tag, defaultValue).toLowerCase();
		if (value.equals("false")) {
			return false;
		} else if (value.equals("true")) {
			return true;
		} else {
			return defaultValue;
		}
	}

	/**
	 * Sets a value in mcpatcher.xml.
	 *
	 * @param mod   name of mod
	 * @param tag   property name
	 * @param value property value (must support toString())
	 */
	public static void set(String mod, String tag, Object value) {
		if (config != null) {
			config.setModConfigValue(mod, tag, value.toString());
		}
	}

	/**
	 * Set a global config value in mcpatcher.xml.
	 *
	 * @param tag   property name
	 * @param value property value (must support toString())
	 */
	static void set(String tag, Object value) {
		if (config != null) {
			config.setConfigValue(tag, value.toString());
		}
	}

	/**
	 * Remove a value from mcpatcher.xml.
	 *
	 * @param mod name of mod
	 * @param tag property name
	 */
	public static void remove(String mod, String tag) {
		if (config != null) {
			config.remove(config.getModConfig(mod, tag));
		}
	}

	/**
	 * Remove a global config value from mcpatcher.xml.
	 *
	 * @param tag property name
	 */
	static void remove(String tag) {
		if (config != null) {
			config.remove(config.getConfig(tag));
		}
	}

	/**
	 * Convenience method to close a stream ignoring exceptions.
	 *
	 * @param closeable closeable object
	 */
	public static void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Convenience method to close a zip file ignoring exceptions.
	 *
	 * @param zip zip file
	 */
	public static void close(ZipFile zip) {
		if (zip != null) {
			try {
				zip.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void setMinecraft(Minecraft minecraft) {
		MCPatcherUtils.minecraft = minecraft;
	}

	public static void setVersions(String minecraftVersion, String patcherVersion) {
		MCPatcherUtils.minecraftVersion = minecraftVersion;
		MCPatcherUtils.patcherVersion = patcherVersion;
	}

	/**
	 * Get minecraft object.
	 *
	 * @return minecraft
	 */
	public static Minecraft getMinecraft() {
		return minecraft;
	}

	/**
	 * Get shortened version of currently running Minecraft, e.g., 1.9pre4.
	 *
	 * @return string
	 */
	public static String getMinecraftVersion() {
		return minecraftVersion;
	}

	/**
	 * Get version of MCPatcher.
	 *
	 * @return string
	 */
	public static String getPatcherVersion() {
		return patcherVersion;
	}

	/**
	 * Attempts to read image.  Closes input stream regardless of success or failure.
	 *
	 * @param input open input stream
	 * @return image or null
	 */
	public static BufferedImage readImage(InputStream input) {
		BufferedImage image = null;
		if (input != null) {
			try {
				image = ImageIO.read(input);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				close(input);
			}
		}
		return image;
	}

	/**
	 * Get array of rgb values from image.
	 *
	 * @param image input image
	 * @return rgb array
	 */
	public static int[] getImageRGB(BufferedImage image) {
		if (image == null) {
			return null;
		} else {
			int width = image.getWidth();
			int height = image.getHeight();
			int[] rgb = new int[width * height];
			image.getRGB(0, 0, width, height, rgb, 0, width);
			return rgb;
		}
	}
}