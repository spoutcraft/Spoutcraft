package com.pclewis.mcpatcher.mod;

import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.IBlockAccess;

abstract class BiomeHelper {
	static BiomeHelper instance;
	IBlockAccess blockAccess;

	static String getBiomeNameAt(int var0, int var1, int var2) {
		if (instance == null) {
			return null;
		} else {
			BiomeGenBase var3 = instance.getBiomeGenAt(var0, var1, var2);
			return var3 == null ? null : var3.biomeName;
		}
	}

	BiomeHelper(IBlockAccess var1) {
		this.blockAccess = var1;
	}

	boolean useBlockBlending() {
		return false;
	}

	abstract BiomeGenBase getBiomeGenAt(int var1, int var2, int var3);

	abstract float getTemperature(int var1, int var2, int var3);

	abstract float getRainfall(int var1, int var2, int var3);

	abstract int getWaterColorMultiplier(int var1, int var2, int var3);
}
