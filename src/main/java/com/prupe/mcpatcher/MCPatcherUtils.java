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
import net.minecraft.client.Minecraft;

public class MCPatcherUtils {
	private static File minecraftDir = null;
	private static String directoryStr = "";
	private static boolean isGame = true;
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
	public static final String CUSTOM_ANIMATIONS = "Custom Animations";
	public static final String MIPMAP = "Mipmap";
	public static final String UTILS_CLASS = "com.prupe.mcpatcher.MCPatcherUtils";
	public static final String LOGGER_CLASS = "com.prupe.mcpatcher.MCLogger";
	public static final String CONFIG_CLASS = "com.prupe.mcpatcher.Config";
	public static final String TILE_MAPPING_CLASS = "com.prupe.mcpatcher.TileMapping";
	public static final String PROFILER_API_CLASS = "com.prupe.mcpatcher.ProfilerAPI";
	public static final String TEXTURE_PACK_API_CLASS = "com.prupe.mcpatcher.TexturePackAPI";
	public static final String TEXTURE_PACK_CHANGE_HANDLER_CLASS = "com.prupe.mcpatcher.TexturePackChangeHandler";
	public static final String WEIGHTED_INDEX_CLASS = "com.prupe.mcpatcher.WeightedIndex";
	public static final String BLEND_METHOD_CLASS = "com.prupe.mcpatcher.BlendMethod";	
	public static final String GL11_CLASS = "org.lwjgl.opengl.GL11";
	public static final String CUSTOM_ANIMATION_CLASS = "com.prupe.mcpatcher.mod.CustomAnimation";
	public static final String FANCY_DIAL_CLASS = "com.prupe.mcpatcher.mod.FancyDial";
	public static final String MIPMAP_HELPER_CLASS = "com.prupe.mcpatcher.mod.MipmapHelper";
	public static final String AA_HELPER_CLASS = "com.prupe.mcpatcher.mod.AAHelper";
	public static final String BORDERED_TEXTURE_CLASS = "com.prupe.mcpatcher.mod.BorderedTexture";
	public static final String FONT_UTILS_CLASS = "com.prupe.mcpatcher.mod.FontUtils";
	public static final String RANDOM_MOBS_CLASS = "com.prupe.mcpatcher.mod.MobRandomizer";
	public static final String MOB_RULE_LIST_CLASS = "com.prupe.mcpatcher.mod.MobRuleList";
	public static final String MOB_OVERLAY_CLASS = "com.prupe.mcpatcher.mod.MobOverlay";
	public static final String COLORIZER_CLASS = "com.prupe.mcpatcher.mod.Colorizer";
	public static final String COLORIZE_WORLD_CLASS = "com.prupe.mcpatcher.mod.ColorizeWorld";
	public static final String COLORIZE_ITEM_CLASS = "com.prupe.mcpatcher.mod.ColorizeItem";
	public static final String COLORIZE_ENTITY_CLASS = "com.prupe.mcpatcher.mod.ColorizeEntity";
	public static final String COLORIZE_BLOCK_CLASS = "com.prupe.mcpatcher.mod.ColorizeBlock";
	public static final String COLOR_MAP_CLASS = "com.prupe.mcpatcher.mod.ColorMap";
	public static final String BIOME_HELPER_CLASS = "com.prupe.mcpatcher.mod.BiomeHelper";
	public static final String LIGHTMAP_CLASS = "com.prupe.mcpatcher.mod.Lightmap";
	public static final String CTM_UTILS_CLASS = "com.prupe.mcpatcher.mod.CTMUtils";
	public static final String TESSELLATOR_UTILS_CLASS = "com.prupe.mcpatcher.mod.TessellatorUtils";	
	public static final String TILE_OVERRIDE_INTERFACE = "com.prupe.mcpatcher.mod.ITileOverride";
	public static final String TILE_OVERRIDE_CLASS = "com.prupe.mcpatcher.mod.TileOverride";
	public static final String TILE_OVERRIDE_IMPL_CLASS = "com.prupe.mcpatcher.mod.TileOverrideImpl";
	public static final String TILE_LOADER_CLASS = "com.prupe.mcpatcher.mod.TileLoader";
	public static final String GLASS_PANE_RENDERER_CLASS = "com.prupe.mcpatcher.mod.GlassPaneRenderer";
	public static final String RENDER_PASS_CLASS = "com.prupe.mcpatcher.mod.RenderPass";
	public static final String RENDER_PASS_API_CLASS = "com.prupe.mcpatcher.mod.RenderPassAPI";
	public static final String SKY_RENDERER_CLASS = "com.prupe.mcpatcher.mod.SkyRenderer";
	public static final String FIREWORKS_HELPER_CLASS = "com.prupe.mcpatcher.mod.FireworksHelper";
	public static final String CIT_UTILS_CLASS = "com.prupe.mcpatcher.mod.CITUtils";
	public static final String SHADERS_CLASS = "com.prupe.mcpatcher.mod.Shaders";

	static File getDefaultGameDir() {
		String var0 = System.getProperty("os.name").toLowerCase();
		String var1 = null;
		String var2 = ".spoutcraft";

		if (var0.contains("win")) {
			var1 = System.getenv("APPDATA");
		} else if (var0.contains("mac")) {
			var2 = "Library/Application Support/spoutcraft";
		}

		if (var1 == null) {
			var1 = System.getProperty("user.home");
		}

		return new File(var1, var2);
	}

	static boolean setGameDir(File var0) {
		if (var0 != null && var0.isDirectory() && ((new File(var0, "bin/lwjgl.jar")).exists() && (new File(var0, "resources")).isDirectory() || (new File(var0, "libraries")).isDirectory() && (new File(var0, "versions")).isDirectory())) {
			minecraftDir = var0.getAbsoluteFile();
		} else {
			minecraftDir = null;
		}

		return true;
	}

	public static File getMinecraftPath(String ... var0) {
		File var1 = minecraftDir;
		String[] var2 = var0;
		int var3 = var0.length;

		for (int var4 = 0; var4 < var3; ++var4) {
			String var5 = var2[var4];
			var1 = new File(var1, var5);
		}

		return var1;
	}

	public static boolean isGame() {
		return isGame;
	}

	public static String getStringProperty(Properties var0, String var1, String var2) {
		return var0 == null ? var2 : var0.getProperty(var1, var2).trim();
	}

	public static int getIntProperty(Properties var0, String var1, int var2) {
		if (var0 != null) {
			String var3 = var0.getProperty(var1, "").trim();

			if (!var3.equals("")) {
				try {
					return Integer.parseInt(var3);
				} catch (NumberFormatException var5) {
					;
				}
			}
		}

		return var2;
	}

	public static boolean getBooleanProperty(Properties var0, String var1, boolean var2) {
		if (var0 != null) {
			String var3 = var0.getProperty(var1, "").trim().toLowerCase();

			if (!var3.equals("")) {
				return Boolean.parseBoolean(var3);
			}
		}

		return var2;
	}

	public static float getFloatProperty(Properties var0, String var1, float var2) {
		if (var0 != null) {
			String var3 = var0.getProperty(var1, "").trim();

			if (!var3.equals("")) {
				try {
					return Float.parseFloat(var3);
				} catch (NumberFormatException var5) {
					;
				}
			}
		}

		return var2;
	}

	public static void close(Closeable var0) {
		if (var0 != null) {
			try {
				var0.close();
			} catch (IOException var2) {
				var2.printStackTrace();
			}
		}
	}

	public static void close(ZipFile var0) {
		if (var0 != null) {
			try {
				var0.close();
			} catch (IOException var2) {
				var2.printStackTrace();
			}
		}
	}

	public static void setMinecraft(Minecraft var0) {
		minecraft = var0;
	}

	public static void setVersions(String var0, String var1) {
		minecraftVersion = var0;
		patcherVersion = var1;

		try {
			Class var2 = Class.forName("sun.misc.VM");
			Method var3 = var2.getDeclaredMethod("maxDirectMemory", new Class[0]);
			long var4 = ((Long)var3.invoke((Object)null, new Object[0])).longValue();
		} catch (Throwable var6) {
			var6.printStackTrace();
		}
	}

	public static Minecraft getMinecraft() {
		return minecraft;
	}

	public static String getMinecraftVersion() {
		return minecraftVersion;
	}

	public static String getPatcherVersion() {
		return patcherVersion;
	}

	public static BufferedImage readImage(InputStream var0) {
		BufferedImage var1 = null;

		if (var0 != null) {
			try {
				var1 = ImageIO.read(var0);
			} catch (IOException var6) {
				var6.printStackTrace();
			} finally {
				close((Closeable)var0);
			}
		}

		return var1;
	}

	public static Properties readProperties(InputStream var0) {
		Properties var1 = new Properties();
		return readProperties(var0, var1) ? var1 : null;
	}

	public static boolean readProperties(InputStream var0, Properties var1) {
		if (var0 != null && var1 != null) {
			boolean var2;

			try {
				var1.load(var0);
				var2 = true;
			} catch (IOException var6) {
				var6.printStackTrace();
				return false;
			} finally {
				close((Closeable)var0);
			}

			return var2;
		} else {
			return false;
		}
	}

	public static int[] getImageRGB(BufferedImage var0) {
		if (var0 == null) {
			return null;
		} else {
			int var1 = var0.getWidth();
			int var2 = var0.getHeight();
			int[] var3 = new int[var1 * var2];
			var0.getRGB(0, 0, var1, var2, var3, 0, var1);
			return var3;
		}
	}

	public static int[] parseIntegerList(String var0, int var1, int var2) {
		ArrayList var3 = new ArrayList();
		Pattern var4 = Pattern.compile("(\\d*)-(\\d*)");
		String[] var5 = var0.replace(',', ' ').split("\\s+");
		int var6 = var5.length;

		for (int var7 = 0; var7 < var6; ++var7) {
			String var8 = var5[var7];

			try {
				if (var8.matches("\\d+")) {
					var3.add(Integer.valueOf(Integer.parseInt(var8)));
				} else {
					Matcher var9 = var4.matcher(var8);

					if (var9.matches()) {
						String var10 = var9.group(1);
						String var11 = var9.group(2);
						int var12 = var10.equals("") ? var1 : Integer.parseInt(var10);
						int var13 = var11.equals("") ? var2 : Integer.parseInt(var11);

						for (int var14 = var12; var14 <= var13; ++var14) {
							var3.add(Integer.valueOf(var14));
						}
					}
				}
			} catch (NumberFormatException var15) {
				;
			}
		}

		if (var1 <= var2) {
			int var16 = 0;

			while (var16 < var3.size()) {
				if (((Integer)var3.get(var16)).intValue() >= var1 && ((Integer)var3.get(var16)).intValue() <= var2) {
					++var16;
				} else {
					var3.remove(var16);
				}
			}
		}

		int[] var17 = new int[var3.size()];

		for (var6 = 0; var6 < var17.length; ++var6) {
			var17[var6] = ((Integer)var3.get(var6)).intValue();
		}

		return var17;
	}

	static {
		try {
			if (Class.forName("com.prupe.mcpatcher.MCPatcher") != null) {
				isGame = false;
			}
		} catch (ClassNotFoundException var1) {
			;
		} catch (Throwable var2) {
			var2.printStackTrace();
		}

		if (isGame) {
			if (!setGameDir(new File(".")) && !setGameDir(getDefaultGameDir())) {
				//directoryStr = String.format("Current directory: %s", new Object[] {(new File(".")).getAbsolutePath()});
			} else {
				//directoryStr = String.format("Game directory:    %s", new Object[] {minecraftDir.getPath()});
			}
		}
	}
}
