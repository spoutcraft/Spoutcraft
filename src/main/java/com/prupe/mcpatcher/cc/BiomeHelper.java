package com.prupe.mcpatcher.cc;

import java.lang.reflect.Method;

import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.MCPatcherUtils;

import net.minecraft.src.BiomeGenBase;

class BiomeHelper {
	private static final MCLogger logger = MCLogger.getLogger("Custom Colors");
	private static Method getWaterColorMultiplier;
	private static BiomeGenBase lastBiome;
	private static int lastI;
	private static int lastK;

	static String getBiomeNameAt(int i, int j, int k) {
		BiomeGenBase biome = getBiomeGenAt(i, j, k);

		if (biome == null) {
			return "";
		} else {
			String biomeName = biome.biomeName;
			return biomeName == null ? "" : biomeName.toLowerCase().replace(" ", "");
		}
	}

	static BiomeGenBase getBiomeGenAt(int i, int j, int k) {
		if (lastBiome == null || i != lastI || k != lastK) {
			lastI = i;
			lastK = k;
			lastBiome = MCPatcherUtils.getMinecraft().theWorld.getBiomeGenForCoords(i, k);
		}

		return lastBiome;
	}

	static float getTemperature(int i, int j, int k) {
		return getBiomeGenAt(i, j, k).getFloatTemperature();
	}

	static float getRainfall(int i, int j, int k) {
		return getBiomeGenAt(i, j, k).getFloatRainfall();
	}

	static int getWaterColorMultiplier(int i, int j, int k) {
		BiomeGenBase biome = getBiomeGenAt(i, j, k);

		if (getWaterColorMultiplier != null) {
			try {
				return ((Integer)getWaterColorMultiplier.invoke(biome, new Object[0])).intValue();
			} catch (Throwable var5) {
				var5.printStackTrace();
				getWaterColorMultiplier = null;
			}
		}

		return biome.waterColorMultiplier;
	}

	static {
		try {
			getWaterColorMultiplier = BiomeGenBase.class.getDeclaredMethod("getWaterColorMultiplier", new Class[0]);
			getWaterColorMultiplier.setAccessible(true);
			logger.config("forge getWaterColorMultiplier detected", new Object[0]);
		} catch (NoSuchMethodException var1) {
			;
		}
	}
}
