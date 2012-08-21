package com.pclewis.mcpatcher;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.ZipFile;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;

public class MCPatcherUtils {
	private static File minecraftDir = null;
	private static boolean isGame = true;
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
	public static final String CONFIG_CLASS = "com.pclewis.mcpatcher.Config";
	public static final String GL11_CLASS = "org.lwjgl.opengl.GL11";
	public static final String TILE_SIZE_CLASS = "com.pclewis.mcpatcher.mod.TileSize";
	public static final String TEXTURE_UTILS_CLASS = "com.pclewis.mcpatcher.mod.TextureUtils";
	public static final String CUSTOM_ANIMATION_CLASS = "com.pclewis.mcpatcher.mod.CustomAnimation";
	public static final String FONT_UTILS_CLASS = "com.pclewis.mcpatcher.mod.FontUtils";
	public static final String RANDOM_MOBS_CLASS = "com.pclewis.mcpatcher.mod.MobRandomizer";
	public static final String MOB_OVERLAY_CLASS = "com.pclewis.mcpatcher.mod.MobOverlay";
	public static final String COLORIZER_CLASS = "com.pclewis.mcpatcher.mod.Colorizer";
	public static final String COLOR_MAP_CLASS = "com.pclewis.mcpatcher.mod.ColorMap";
	public static final String BIOME_HELPER_CLASS = "com.pclewis.mcpatcher.mod.BiomeHelper";
	public static final String CTM_UTILS_CLASS = "com.pclewis.mcpatcher.mod.CTMUtils";
	public static final String SUPER_TESSELLATOR_CLASS = "com.pclewis.mcpatcher.mod.SuperTessellator";
	public static final String TILE_OVERRIDE_CLASS = "com.pclewis.mcpatcher.mod.TileOverride";
	public static final String SKY_RENDERER_CLASS = "com.pclewis.mcpatcher.mod.SkyRenderer";
	public static final String SHADERS_CLASS = "com.pclewis.mcpatcher.mod.Shaders";

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
		if (var0 != null && var0.isDirectory() && (new File(var0, "bin/lwjgl.jar")).exists() && (new File(var0, "resources")).isDirectory()) {
			minecraftDir = var0.getAbsoluteFile();
		} else {
			minecraftDir = null;
		}

		return loadProperties();
	}

	private static boolean loadProperties() {
		config = null;
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
		String[] var4 = var0.replace(',', ' ').split("\\s+");
		int var5 = var4.length;

		for (int var6 = 0; var6 < var5; ++var6) {
			String var7 = var4[var6];
			var7 = var7.trim();

			try {
				if (var7.matches("^\\d+$")) {
					var3.add(Integer.valueOf(Integer.parseInt(var7)));
				} else if (var7.matches("^\\d+-\\d+$")) {
					String[] var8 = var7.split("-");
					int var9 = Integer.parseInt(var8[0]);
					int var10 = Integer.parseInt(var8[1]);

					for (int var11 = var9; var11 <= var10; ++var11) {
						var3.add(Integer.valueOf(var11));
					}
				}
			} catch (NumberFormatException var12) {
				;
			}
		}

		if (var1 <= var2) {
			int var13 = 0;

			while (var13 < var3.size()) {
				if (((Integer)var3.get(var13)).intValue() >= var1 && ((Integer)var3.get(var13)).intValue() <= var2) {
					++var13;
				} else {
					var3.remove(var13);
				}
			}
		}

		int[] var14 = new int[var3.size()];

		for (var5 = 0; var5 < var14.length; ++var5) {
			var14[var5] = ((Integer)var3.get(var5)).intValue();
		}

		return var14;
	}

	static {
		try {
			if (Class.forName("com.pclewis.mcpatcher.MCPatcher") != null) {
				isGame = false;
			}
		} catch (ClassNotFoundException var1) {
			;
		} catch (Throwable var2) {
			var2.printStackTrace();
		}

		if (isGame) {
			if (!setGameDir(new File(".")) && !setGameDir(getDefaultGameDir())) {
				System.out.println("MCPatcherUtils initialized. Current directory " + (new File(".")).getAbsolutePath());
			} else {
				System.out.println("MCPatcherUtils initialized. Directory " + minecraftDir.getPath());
			}
		}
	}
}
