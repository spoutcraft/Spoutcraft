package com.prupe.mcpatcher.cc;

import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.MCPatcherUtils;
import java.lang.reflect.Method;
import java.util.BitSet;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Minecraft;

class BiomeHelper {
	private static final MCLogger logger = MCLogger.getLogger("Custom Colors");
	private static Method getWaterColorMultiplier;
	private static BiomeGenBase lastBiome;
	private static int lastI;
	private static int lastK;

	static void parseBiomeList(String list, BitSet bits) {
		if (!MCPatcherUtils.isNullOrEmpty(list)) {
			String[] arr$ = list.toLowerCase().split("\\s+");
			int len$ = arr$.length;

			for (int i$ = 0; i$ < len$; ++i$) {
				String s = arr$[i$];

				if (!s.isEmpty()) {
					BiomeGenBase[] arr$1 = BiomeGenBase.biomeList;
					int len$1 = arr$1.length;

					for (int i$1 = 0; i$1 < len$1; ++i$1) {
						BiomeGenBase biome = arr$1[i$1];

						if (biome != null && biome.biomeName != null && s.equals(biome.biomeName.toLowerCase().replace(" ", ""))) {
							bits.set(biome.biomeID);
						}
					}
				}
			}
		}
	}

	static int getBiomeIDAt(int i, int j, int k) {
		BiomeGenBase biome = getBiomeGenAt(i, j, k);
		return biome == null ? BiomeGenBase.biomeList.length : biome.biomeID;
	}

	static BiomeGenBase getBiomeGenAt(int i, int j, int k) {
		if (lastBiome == null || i != lastI || k != lastK) {
			lastI = i;
			lastK = k;
			lastBiome = Minecraft.getMinecraft().theWorld.getBiomeGenForCoords(i, k);
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
