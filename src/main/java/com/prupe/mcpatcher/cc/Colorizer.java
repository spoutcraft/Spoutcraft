package com.prupe.mcpatcher.cc;

import java.util.Properties;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.TexturePackAPI;
import com.prupe.mcpatcher.TexturePackChangeHandler;

import net.minecraft.src.Potion;
import net.minecraft.src.ResourceLocation;

public class Colorizer {
	private static final MCLogger logger = MCLogger.getLogger("Custom Colors");
	static final ResourceLocation COLOR_PROPERTIES = TexturePackAPI.newMCPatcherResourceLocation("color.properties");
	private static Properties properties;
	static final boolean useWaterColors = Config.getBoolean("Custom Colors", "water", true);
	static final boolean useSwampColors = Config.getBoolean("Custom Colors", "swamp", true);
	static final boolean useTreeColors = Config.getBoolean("Custom Colors", "tree", true);
	static final boolean usePotionColors = Config.getBoolean("Custom Colors", "potion", true);
	static final boolean useParticleColors = Config.getBoolean("Custom Colors", "particle", true);
	static final boolean useFogColors = Config.getBoolean("Custom Colors", "fog", true);
	static final boolean useCloudType = Config.getBoolean("Custom Colors", "clouds", true);
	static final boolean useRedstoneColors = Config.getBoolean("Custom Colors", "redstone", true);
	static final boolean useStemColors = Config.getBoolean("Custom Colors", "stem", true);
	static final boolean useMapColors = Config.getBoolean("Custom Colors", "map", true);
	static final boolean useDyeColors = Config.getBoolean("Custom Colors", "dye", true);
	static final boolean useBlockColors = Config.getBoolean("Custom Colors", "otherBlocks", true);
	static final boolean useTextColors = Config.getBoolean("Custom Colors", "text", true);
	static final boolean useXPOrbColors = Config.getBoolean("Custom Colors", "xporb", true);
	static final boolean useEggColors = Config.getBoolean("Custom Colors", "egg", true);
	public static final int COLOR_MAP_SWAMP_GRASS = 0;
	public static final int COLOR_MAP_SWAMP_FOLIAGE = 1;
	public static final int COLOR_MAP_PINE = 2;
	public static final int COLOR_MAP_BIRCH = 3;
	public static final int COLOR_MAP_FOLIAGE = 4;
	public static final int COLOR_MAP_WATER = 5;
	public static final int COLOR_MAP_UNDERWATER = 6;
	public static final int COLOR_MAP_FOG0 = 7;
	public static final int COLOR_MAP_SKY0 = 8;
	public static final int NUM_FIXED_COLOR_MAPS = 9;
	static final ColorMap[] fixedColorMaps = new ColorMap[9];
	public static final float[] setColor = new float[3];

	public static void setColorF(int color) {
		intToFloat3(color, setColor);
	}

	static void init() {}

	private static void reset() {
		properties = new Properties();
		ColorizeBlock.reset();
		Lightmap.reset();
		ColorizeItem.reset();
		ColorizeWorld.reset();
		ColorizeEntity.reset();
	}

	private static void reloadColorProperties() {
		if (TexturePackAPI.getProperties(COLOR_PROPERTIES, properties)) {
			logger.finer("reloading %s", new Object[] {COLOR_PROPERTIES});
		}
	}

	static String getStringKey(String[] keys, int index) {
		return keys != null && index >= 0 && index < keys.length && keys[index] != null ? keys[index] : "" + index;
	}

	static void loadIntColor(String key, Potion potion) {
		potion.liquidColor = loadIntColor(key, potion.liquidColor);
	}

	static boolean loadIntColor(String key, int[] color, int index) {
		logger.config("%s=%06x", new Object[] {key, Integer.valueOf(color[index])});
		String value = properties.getProperty(key, "");

		if (!value.equals("")) {
			try {
				color[index] = Integer.parseInt(value, 16);
				return true;
			} catch (NumberFormatException var5) {
				;
			}
		}

		return false;
	}

	static int loadIntColor(String key, int color) {
		logger.config("%s=%06x", new Object[] {key, Integer.valueOf(color)});
		String value = properties.getProperty(key, "");

		if (!value.equals("")) {
			try {
				return Integer.parseInt(value, 16);
			} catch (NumberFormatException var4) {
				;
			}
		}

		return color;
	}

	static void loadFloatColor(String key, float[] color) {
		int intColor = float3ToInt(color);
		intToFloat3(loadIntColor(key, intColor), color);
	}

	static void intToFloat3(int rgb, float[] f, int offset) {
		if ((rgb & 16777215) == 16777215) {
			f[offset] = f[offset + 1] = f[offset + 2] = 1.0F;
		} else {
			f[offset] = (float)(rgb & 16711680) / 1.671168E7F;
			f[offset + 1] = (float)(rgb & 65280) / 65280.0F;
			f[offset + 2] = (float)(rgb & 255) / 255.0F;
		}
	}

	static void intToFloat3(int rgb, float[] f) {
		intToFloat3(rgb, f, 0);
	}

	static int float3ToInt(float[] f, int offset) {
		return (int)(255.0F * f[offset]) << 16 | (int)(255.0F * f[offset + 1]) << 8 | (int)(255.0F * f[offset + 2]);
	}

	static int float3ToInt(float[] f) {
		return float3ToInt(f, 0);
	}

	static float clamp(float f) {
		return f < 0.0F ? 0.0F : (f > 1.0F ? 1.0F : f);
	}

	static double clamp(double d) {
		return d < 0.0D ? 0.0D : (d > 1.0D ? 1.0D : d);
	}

	static void clamp(float[] f) {
		for (int i = 0; i < f.length; ++i) {
			f[i] = clamp(f[i]);
		}
	}

	static void access$000() {
		reset();
	}

	static void access$100() {
		reloadColorProperties();
	}

	static Properties access$200() {
		return properties;
	}

	static {
		try {
			reset();
		} catch (Throwable var1) {
			var1.printStackTrace();
		}

		TexturePackChangeHandler.register(new Colorizer$1("Custom Colors", 2));
	}
}
