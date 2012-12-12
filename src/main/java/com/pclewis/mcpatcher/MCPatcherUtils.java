package com.pclewis.mcpatcher;

import net.minecraft.client.Minecraft;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.zip.ZipFile;

/**
 * Collection of static methods available to mods at runtime.  This class is always injected into
 * the output minecraft jar.
 */
public class MCPatcherUtils {
    private static File minecraftDir = null;
    private static String directoryStr = "";
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
    public static final String CONNECTED_TEXTURES = "Connected Textures";
    public static final String BETTER_SKIES = "Better Skies";
    public static final String BETTER_GLASS = "Better Glass";
    public static final String GLSL_SHADERS = "GLSL Shaders";

    public static final String UTILS_CLASS = "com.pclewis.mcpatcher.MCPatcherUtils";
    public static final String LOGGER_CLASS = "com.pclewis.mcpatcher.MCLogger";
    public static final String CONFIG_CLASS = "com.pclewis.mcpatcher.Config";
    public static final String TEXTURE_PACK_API_CLASS = "com.pclewis.mcpatcher.TexturePackAPI";
    public static final String WEIGHTED_INDEX_CLASS = "com.pclewis.mcpatcher.WeightedIndex";
    public static final String GL11_CLASS = "org.lwjgl.opengl.GL11";

    public static final String TILE_SIZE_CLASS = "com.pclewis.mcpatcher.mod.TileSize";
    public static final String TEXTURE_UTILS_CLASS = "com.pclewis.mcpatcher.mod.TextureUtils";
    public static final String CUSTOM_ANIMATION_CLASS = "com.pclewis.mcpatcher.mod.CustomAnimation";
    public static final String FANCY_COMPASS_CLASS = "com.pclewis.mcpatcher.mod.FancyCompass";
    public static final String MIPMAP_HELPER_CLASS = "com.pclewis.mcpatcher.mod.MipmapHelper";
    public static final String FONT_UTILS_CLASS = "com.pclewis.mcpatcher.mod.FontUtils";
    public static final String RANDOM_MOBS_CLASS = "com.pclewis.mcpatcher.mod.MobRandomizer";
    public static final String MOB_RULE_LIST_CLASS = "com.pclewis.mcpatcher.mod.MobRuleList";
    public static final String MOB_OVERLAY_CLASS = "com.pclewis.mcpatcher.mod.MobOverlay";
    public static final String COLORIZER_CLASS = "com.pclewis.mcpatcher.mod.Colorizer";
    public static final String COLOR_MAP_CLASS = "com.pclewis.mcpatcher.mod.ColorMap";
    public static final String BIOME_HELPER_CLASS = "com.pclewis.mcpatcher.mod.BiomeHelper";
    public static final String LIGHTMAP_CLASS = "com.pclewis.mcpatcher.mod.Lightmap";
    public static final String CTM_UTILS_CLASS = "com.pclewis.mcpatcher.mod.CTMUtils";
    public static final String SUPER_TESSELLATOR_CLASS = "com.pclewis.mcpatcher.mod.SuperTessellator";
    public static final String TILE_OVERRIDE_CLASS = "com.pclewis.mcpatcher.mod.TileOverride";
    public static final String GLASS_PANE_RENDERER_CLASS = "com.pclewis.mcpatcher.mod.GlassPaneRenderer";
    public static final String RENDER_PASS_CLASS = "com.pclewis.mcpatcher.mod.RenderPass";
    public static final String RENDER_PASS_API_CLASS = "com.pclewis.mcpatcher.mod.RenderPassAPI";
    public static final String SKY_RENDERER_CLASS = "com.pclewis.mcpatcher.mod.SkyRenderer";
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
                directoryStr = " Directory " + minecraftDir.getPath();
            } else {
                directoryStr = " Current directory " + new File(".").getAbsolutePath();
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
     * @see #info(String, Object...)
     * @see #debug(String, Object...)
     * @deprecated
     */
    public static void log(String format, Object... params) {
        debug(format, params);
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
     * Write an informational message to minecraft standard output.
     *
     * @param format printf-style format string
     * @param params printf-style parameters
     */
    public static void info(String format, Object... params) {
        System.out.printf(format + "\n", params);
    }

    /**
     * Write a debug message to minecraft standard output.
     *
     * @param format printf-style format string
     * @param params printf-style parameters
     */
    public static void debug(String format, Object... params) {
        if (debug) {
            System.out.printf(format + "\n", params);
        }
    }

    /**
     * Gets a value from mcpatcher.xml.
     *
     * @param mod          name of mod
     * @param tag          property name
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
     * @param tag          property name
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
     * @param mod          name of mod
     * @param tag          property name
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
     * @param tag          property name
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
     * @param mod          name of mod
     * @param tag          property name
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
     * @param tag          property name
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

    static void setLogLevel(String category, Level level) {
        config.setLogLevel(category, level);
    }

    static Level getLogLevel(String category) {
        return config.getLogLevel(category);
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
        info("MCPatcherUtils v%s initialized.%s", patcherVersion, directoryStr);
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
     * Attempts to read a properties file.  Closes input stream regardless of success or failure.
     *
     * @param input open input stream
     * @return properties object or null
     */
    public static Properties readProperties(InputStream input) {
        Properties properties = new Properties();
        if (readProperties(input, properties)) {
            return properties;
        } else {
            return null;
        }
    }

    /**
     * Attempts to read a properties file.  Closes input stream regardless of success or failure.
     *
     * @param input      open input stream
     * @param properties initial properties object
     * @return true if properties were successfully read
     */
    public static boolean readProperties(InputStream input, Properties properties) {
        if (input != null && properties != null) {
            try {
                properties.load(input);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                close(input);
            }
        }
        return false;
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

    /**
     * Parse a comma-separated list of integers/ranges.
     *
     * @param list     comma-separated list, e.g., 2-4,5,8,12-20
     * @param minValue smallest value allowed in the list
     * @param maxValue largest value allowed in the list
     * @return possibly empty integer array
     */
    public static int[] parseIntegerList(String list, int minValue, int maxValue) {
        ArrayList<Integer> tmpList = new ArrayList<Integer>();
        for (String token : list.replace(',', ' ').split("\\s+")) {
            token = token.trim();
            try {
                if (token.matches("^\\d+$")) {
                    tmpList.add(Integer.parseInt(token));
                } else if (token.matches("^\\d+-\\d+$")) {
                    String[] t = token.split("-");
                    int min = Integer.parseInt(t[0]);
                    int max = Integer.parseInt(t[1]);
                    for (int i = min; i <= max; i++) {
                        tmpList.add(i);
                    }
                }
            } catch (NumberFormatException e) {
            }
        }
        if (minValue <= maxValue) {
            for (int i = 0; i < tmpList.size(); ) {
                if (tmpList.get(i) < minValue || tmpList.get(i) > maxValue) {
                    tmpList.remove(i);
                } else {
                    i++;
                }
            }
        }
        int[] a = new int[tmpList.size()];
        for (int i = 0; i < a.length; i++) {
            a[i] = tmpList.get(i);
        }
        return a;
    }
}
