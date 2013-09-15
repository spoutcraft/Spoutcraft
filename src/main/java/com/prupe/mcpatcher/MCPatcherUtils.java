package com.prupe.mcpatcher;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipFile;
import javax.imageio.ImageIO;

import net.minecraft.src.Minecraft;

public class MCPatcherUtils {
	private static File minecraftDir = null;
	private static boolean isGame;
	private static Minecraft minecraft;
	private static String minecraftVersion;
	private static String patcherVersion;
	public static final String EXTENDED_HD = "Extended HD";
	public static final String HD_FONT = "HD Font";
	public static final String RANDOM_MOBS = "Random Mobs";
	public static final String CUSTOM_COLORS = "Custom Colors";
	public static final String CONNECTED_TEXTURES = "Connected Textures";
	public static final String BETTER_SKIES = "Better Skies";
	public static final String BETTER_GLASS = "Better Glass";
	public static final String CUSTOM_ITEM_TEXTURES = "Custom Item Textures";
	public static final String GLSL_SHADERS = "GLSL Shaders";
	public static final String BASE_MOD = "__Base";
	public static final String BASE_TEXTURE_PACK_MOD = "__TexturePackBase";
	public static final String BASE_TILESHEET_MOD = "__TilesheetBase";
	public static final String CUSTOM_ANIMATIONS = "Custom Animations";
	public static final String MIPMAP = "Mipmap";
	public static final String GL11_CLASS = "org.lwjgl.opengl.GL11";
	public static final String UTILS_CLASS = "com.prupe.mcpatcher.MCPatcherUtils";
	public static final String LOGGER_CLASS = "com.prupe.mcpatcher.MCLogger";
	public static final String CONFIG_CLASS = "com.prupe.mcpatcher.Config";
	public static final String PROFILER_API_CLASS = "com.prupe.mcpatcher.ProfilerAPI";
	public static final String INPUT_HANDLER_CLASS = "com.prupe.mcpatcher.InputHandler";
	public static final String TEXTURE_PACK_API_CLASS = "com.prupe.mcpatcher.TexturePackAPI";
	public static final String TEXTURE_PACK_CHANGE_HANDLER_CLASS = "com.prupe.mcpatcher.TexturePackChangeHandler";
	public static final String WEIGHTED_INDEX_CLASS = "com.prupe.mcpatcher.WeightedIndex";
	public static final String BLEND_METHOD_CLASS = "com.prupe.mcpatcher.BlendMethod";
	public static final String TILE_LOADER_CLASS = "com.prupe.mcpatcher.TileLoader";
	public static final String TESSELLATOR_UTILS_CLASS = "com.prupe.mcpatcher.TessellatorUtils";
	public static final String AA_HELPER_CLASS = "com.prupe.mcpatcher.hd.AAHelper";
	public static final String BORDERED_TEXTURE_CLASS = "com.prupe.mcpatcher.hd.BorderedTexture";
	public static final String CUSTOM_ANIMATION_CLASS = "com.prupe.mcpatcher.hd.CustomAnimation";
	public static final String FANCY_DIAL_CLASS = "com.prupe.mcpatcher.hd.FancyDial";
	public static final String FONT_UTILS_CLASS = "com.prupe.mcpatcher.hd.FontUtils";
	public static final String MIPMAP_HELPER_CLASS = "com.prupe.mcpatcher.hd.MipmapHelper";
	public static final String RANDOM_MOBS_CLASS = "com.prupe.mcpatcher.mob.MobRandomizer";
	public static final String MOB_RULE_LIST_CLASS = "com.prupe.mcpatcher.mob.MobRuleList";
	public static final String MOB_OVERLAY_CLASS = "com.prupe.mcpatcher.mob.MobOverlay";
	public static final String LINE_RENDERER_CLASS = "com.prupe.mcpatcher.mob.LineRenderer";
	public static final String COLORIZER_CLASS = "com.prupe.mcpatcher.cc.Colorizer";
	public static final String COLORIZE_WORLD_CLASS = "com.prupe.mcpatcher.cc.ColorizeWorld";
	public static final String COLORIZE_ITEM_CLASS = "com.prupe.mcpatcher.cc.ColorizeItem";
	public static final String COLORIZE_ENTITY_CLASS = "com.prupe.mcpatcher.cc.ColorizeEntity";
	public static final String COLORIZE_BLOCK_CLASS = "com.prupe.mcpatcher.cc.ColorizeBlock";
	public static final String COLOR_MAP_CLASS = "com.prupe.mcpatcher.cc.ColorMap";
	public static final String BIOME_HELPER_CLASS = "com.prupe.mcpatcher.cc.BiomeHelper";
	public static final String LIGHTMAP_CLASS = "com.prupe.mcpatcher.cc.Lightmap";
	public static final String CTM_UTILS_CLASS = "com.prupe.mcpatcher.ctm.CTMUtils";
	public static final String TILE_OVERRIDE_INTERFACE = "com.prupe.mcpatcher.ctm.ITileOverride";
	public static final String TILE_OVERRIDE_CLASS = "com.prupe.mcpatcher.ctm.TileOverride";
	public static final String TILE_OVERRIDE_IMPL_CLASS = "com.prupe.mcpatcher.ctm.TileOverrideImpl";
	public static final String GLASS_PANE_RENDERER_CLASS = "com.prupe.mcpatcher.ctm.GlassPaneRenderer";
	public static final String RENDER_PASS_CLASS = "com.prupe.mcpatcher.ctm.RenderPass";
	public static final String RENDER_PASS_API_CLASS = "com.prupe.mcpatcher.ctm.RenderPassAPI";
	public static final String SKY_RENDERER_CLASS = "com.prupe.mcpatcher.sky.SkyRenderer";
	public static final String FIREWORKS_HELPER_CLASS = "com.prupe.mcpatcher.sky.FireworksHelper";
	public static final String CIT_UTILS_CLASS = "com.prupe.mcpatcher.cit.CITUtils";
	public static final String OVERRIDE_BASE_CLASS = "com.prupe.mcpatcher.cit.OverrideBase";
	public static final String ITEM_OVERRIDE_CLASS = "com.prupe.mcpatcher.cit.ItemOverride";
	public static final String ENCHANTMENT_CLASS = "com.prupe.mcpatcher.cit.Enchantment";
	public static final String ENCHANTMENT_LIST_CLASS = "com.prupe.mcpatcher.cit.EnchantmentList";
	public static final String ARMOR_OVERRIDE_CLASS = "com.prupe.mcpatcher.cit.ArmorOverride";
	public static final String POTION_REPLACER_CLASS = "com.prupe.mcpatcher.cit.PotionReplacer";
	public static final String SHADERS_CLASS = "com.prupe.mcpatcher.glsl.Shaders";
	public static final String BLANK_PNG = "mcpatcher/blank.png";

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
		if (dir != null && dir.isDirectory() && (new File(dir, "libraries")).isDirectory() && (new File(dir, "versions")).isDirectory()) {
			minecraftDir = dir.getAbsoluteFile();
		} else {
			minecraftDir = null;
		}

		return Config.load(minecraftDir);
	}

	public static File getMinecraftPath(String ... subdirs) {
		File f = minecraftDir;
		String[] arr$ = subdirs;
		int len$ = subdirs.length;

		for (int i$ = 0; i$ < len$; ++i$) {
			String s = arr$[i$];
			f = new File(f, s);
		}

		return f;
	}

	public static boolean isGame() {
		return isGame;
	}

	public static String getStringProperty(Properties properties, String key, String defaultValue) {
		return properties == null ? defaultValue : properties.getProperty(key, defaultValue).trim();
	}

	public static int getIntProperty(Properties properties, String key, int defaultValue) {
		if (properties != null) {
			String value = properties.getProperty(key, "").trim();

			if (!value.equals("")) {
				try {
					return Integer.parseInt(value);
				} catch (NumberFormatException var5) {
					;
				}
			}
		}

		return defaultValue;
	}

	public static boolean getBooleanProperty(Properties properties, String key, boolean defaultValue) {
		if (properties != null) {
			String value = properties.getProperty(key, "").trim().toLowerCase();

			if (!value.equals("")) {
				return Boolean.parseBoolean(value);
			}
		}

		return defaultValue;
	}

	public static float getFloatProperty(Properties properties, String key, float defaultValue) {
		if (properties != null) {
			String value = properties.getProperty(key, "").trim();

			if (!value.equals("")) {
				try {
					return Float.parseFloat(value);
				} catch (NumberFormatException var5) {
					;
				}
			}
		}

		return defaultValue;
	}

	public static double getDoubleProperty(Properties properties, String key, double defaultValue) {
		if (properties != null) {
			String value = properties.getProperty(key, "").trim();

			if (!value.equals("")) {
				try {
					return Double.parseDouble(value);
				} catch (NumberFormatException var6) {
					;
				}
			}
		}

		return defaultValue;
	}

	public static void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException var2) {
				var2.printStackTrace();
			}
		}
	}

	public static void close(ZipFile zip) {
		if (zip != null) {
			try {
				zip.close();
			} catch (IOException var2) {
				var2.printStackTrace();
			}
		}
	}

	public static void setMinecraft(Minecraft minecraft1, File minecraftDir, String minecraftVersion1, String patcherVersion1) {
		isGame = true;
		minecraft = minecraft1;
		minecraftDir = minecraftDir.getAbsoluteFile();
		minecraftVersion = minecraftVersion1;
		patcherVersion = patcherVersion1;
		System.out.println();
		System.out.printf("MCPatcherUtils initialized:\n", new Object[0]);
		System.out.printf("Game directory:    %s\n", new Object[] {minecraftDir});
		System.out.printf("Minecraft version: %s\n", new Object[] {minecraftVersion});
		System.out.printf("MCPatcher version: %s\n", new Object[] {patcherVersion});
		System.out.printf("Max heap memory:   %.1fMB\n", new Object[] {Float.valueOf((float)Runtime.getRuntime().maxMemory() / 1048576.0F)});

		try {
			Class e = Class.forName("sun.misc.VM");
			Method method = e.getDeclaredMethod("maxDirectMemory", new Class[0]);
			long memory = ((Long)method.invoke((Object)null, new Object[0])).longValue();
			System.out.printf("Max direct memory: %.1fMB\n", new Object[] {Float.valueOf((float)memory / 1048576.0F)});
		} catch (Throwable var8) {
			var8.printStackTrace();
		}

		System.out.println();
		Config.load(minecraftDir);
	}

	public static Minecraft getMinecraft() {
		if (minecraft == null) {
			System.out.println("Getting Minecraft, returned Error because it was null");
		}
		return minecraft;
	}

	public static String getMinecraftVersion() {
		return minecraftVersion;
	}

	public static String getPatcherVersion() {
		return patcherVersion;
	}

	public static BufferedImage readImage(InputStream input) {
		BufferedImage image = null;

		if (input != null) {
			try {
				image = ImageIO.read(input);
			} catch (IOException var6) {
				var6.printStackTrace();
			} finally {
				close((Closeable)input);
			}
		}

		return image;
	}

	public static Properties readProperties(InputStream input) {
		Properties properties = new Properties();
		return readProperties(input, properties) ? properties : null;
	}

	public static boolean readProperties(InputStream input, Properties properties) {
		if (input != null && properties != null) {
			boolean e;

			try {
				properties.load(input);
				e = true;
			} catch (IOException var6) {
				var6.printStackTrace();
				return false;
			} finally {
				close((Closeable)input);
			}

			return e;
		} else {
			return false;
		}
	}

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

	public static int[] parseIntegerList(String list, int minValue, int maxValue) {
		ArrayList tmpList = new ArrayList();
		Pattern p = Pattern.compile("(\\d*)-(\\d*)");
		String[] a = list.replace(',', ' ').split("\\s+");
		int i = a.length;

		for (int i$ = 0; i$ < i; ++i$) {
			String token = a[i$];

			try {
				if (token.matches("\\d+")) {
					tmpList.add(Integer.valueOf(Integer.parseInt(token)));
				} else {
					Matcher e = p.matcher(token);

					if (e.matches()) {
						String a1 = e.group(1);
						String b = e.group(2);
						int min = a1.equals("") ? minValue : Integer.parseInt(a1);
						int max = b.equals("") ? maxValue : Integer.parseInt(b);

						for (int i1 = min; i1 <= max; ++i1) {
							tmpList.add(Integer.valueOf(i1));
						}
					}
				}
			} catch (NumberFormatException var15) {
				;
			}
		}

		if (minValue <= maxValue) {
			int var16 = 0;

			while (var16 < tmpList.size()) {
				if (((Integer)tmpList.get(var16)).intValue() >= minValue && ((Integer)tmpList.get(var16)).intValue() <= maxValue) {
					++var16;
				} else {
					tmpList.remove(var16);
				}
			}
		}

		int[] var17 = new int[tmpList.size()];

		for (i = 0; i < var17.length; ++i) {
			var17[i] = ((Integer)tmpList.get(i)).intValue();
		}

		return var17;
	}
}
