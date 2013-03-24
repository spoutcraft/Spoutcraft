package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.TexturePackAPI;
import com.prupe.mcpatcher.TexturePackChangeHandler;
import com.prupe.mcpatcher.mod.Colorizer$1;
import java.util.Properties;
import net.minecraft.src.Potion;

public class Colorizer {
	private static final String COLOR_PROPERTIES = "/color.properties";
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

	public static void setColorF(int var0) {
		intToFloat3(var0, setColor);
	}

	private static void reset() {
		properties = new Properties();
		ColorizeBlock.reset();
		Lightmap.reset();
		ColorizeItem.reset();
		ColorizeWorld.reset();
		ColorizeEntity.reset();
	}

	private static void reloadColorProperties() {
		TexturePackAPI.getProperties("/color.properties", properties);		
	}

	static String getStringKey(String[] var0, int var1) {
		return var0 != null && var1 >= 0 && var1 < var0.length && var0[var1] != null ? var0[var1] : "" + var1;
	}

	static void loadIntColor(String var0, Potion var1) {
		var1.liquidColor = loadIntColor(var0, var1.liquidColor);
	}

	static boolean loadIntColor(String var0, int[] var1, int var2) {
		String var3 = properties.getProperty(var0, "");

		if (!var3.equals("")) {
			try {
				var1[var2] = Integer.parseInt(var3, 16);
				return true;
			} catch (NumberFormatException var5) {
				;
			}
		}

		return false;
	}

	static int loadIntColor(String var0, int var1) {
		String var2 = properties.getProperty(var0, "");

		if (!var2.equals("")) {
			try {
				return Integer.parseInt(var2, 16);
			} catch (NumberFormatException var4) {
				;
			}
		}

		return var1;
	}

	static void loadFloatColor(String var0, float[] var1) {
		int var2 = float3ToInt(var1);
		intToFloat3(loadIntColor(var0, var2), var1);
	}

	static void intToFloat3(int var0, float[] var1, int var2) {
		var1[var2] = (float)(var0 & 16711680) / 1.671168E7F;
		var1[var2 + 1] = (float)(var0 & 65280) / 65280.0F;
		var1[var2 + 2] = (float)(var0 & 255) / 255.0F;
	}

	static void intToFloat3(int var0, float[] var1) {
		intToFloat3(var0, var1, 0);
	}

	static int float3ToInt(float[] var0, int var1) {
		return (int)(255.0F * var0[var1]) << 16 | (int)(255.0F * var0[var1 + 1]) << 8 | (int)(255.0F * var0[var1 + 2]);
	}

	static int float3ToInt(float[] var0) {
		return float3ToInt(var0, 0);
	}

	static float clamp(float var0) {
		return var0 < 0.0F ? 0.0F : (var0 > 1.0F ? 1.0F : var0);
	}

	static double clamp(double var0) {
		return var0 < 0.0D ? 0.0D : (var0 > 1.0D ? 1.0D : var0);
	}

	static void clamp(float[] var0) {
		for (int var1 = 0; var1 < var0.length; ++var1) {
			var0[var1] = clamp(var0[var1]);
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
