package com.pclewis.mcpatcher.mod;

import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.IBlockAccess;

class BiomeHelper$New extends BiomeHelper {
	private BiomeGenBase lastBiome;
	private int lastI;
	private int lastK;

	BiomeHelper$New(IBlockAccess var1) {
		super(var1);
	}

	boolean useBlockBlending() {
		return true;
	}

	BiomeGenBase getBiomeGenAt(int var1, int var2, int var3) {
		if (this.lastBiome == null || var1 != this.lastI || var3 != this.lastK) {
			this.lastI = var1;
			this.lastK = var3;
			this.lastBiome = this.blockAccess.getBiomeGenForCoords(var1, var3);
		}

		return this.lastBiome;
	}

	float getTemperature(int var1, int var2, int var3) {
		return this.getBiomeGenAt(var1, var2, var3).getFloatTemperature();
	}

	float getRainfall(int var1, int var2, int var3) {
		return this.getBiomeGenAt(var1, var2, var3).getFloatRainfall();
	}

	int getWaterColorMultiplier(int var1, int var2, int var3) {
		return this.getBiomeGenAt(var1, var2, var3).waterColorMultiplier;
	}
}
