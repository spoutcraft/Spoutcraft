package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.MCPatcherUtils;
import java.lang.reflect.Method;
import net.minecraft.src.BiomeGenBase;

class BiomeHelper {
	private static Method getWaterColorMultiplier;
	private static BiomeGenBase lastBiome;
	private static int lastI;
	private static int lastK;

	static String getBiomeNameAt(int var0, int var1, int var2) {
		BiomeGenBase var3 = getBiomeGenAt(var0, var1, var2);

		if (var3 == null) {
			return "";
		} else {
			String var4 = var3.biomeName;
			return var4 == null ? "" : var4.toLowerCase().replace(" ", "");
		}
	}

	static BiomeGenBase getBiomeGenAt(int var0, int var1, int var2) {
		if (lastBiome == null || var0 != lastI || var2 != lastK) {
			lastI = var0;
			lastK = var2;
			lastBiome = MCPatcherUtils.getMinecraft().theWorld.getBiomeGenForCoords(var0, var2);
		}

		return lastBiome;
	}

	static float getTemperature(int var0, int var1, int var2) {
		return getBiomeGenAt(var0, var1, var2).getFloatTemperature();
	}

	static float getRainfall(int var0, int var1, int var2) {
		return getBiomeGenAt(var0, var1, var2).getFloatRainfall();
	}

	static int getWaterColorMultiplier(int var0, int var1, int var2) {
		BiomeGenBase var3 = getBiomeGenAt(var0, var1, var2);

		if (getWaterColorMultiplier != null) {
			try {
				return ((Integer)getWaterColorMultiplier.invoke(var3, new Object[0])).intValue();
			} catch (Throwable var5) {
				var5.printStackTrace();
				getWaterColorMultiplier = null;
			}
		}

		return var3.waterColorMultiplier;
	}

	static {
		try {
			getWaterColorMultiplier = BiomeGenBase.class.getDeclaredMethod("getWaterColorMultiplier", new Class[0]);
			getWaterColorMultiplier.setAccessible(true);
		} catch (NoSuchMethodException var1) {
			;
		}
	}
}
