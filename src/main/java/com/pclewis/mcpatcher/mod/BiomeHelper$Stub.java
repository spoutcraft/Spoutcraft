package com.pclewis.mcpatcher.mod;

import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.IBlockAccess;

class BiomeHelper$Stub extends BiomeHelper {
	BiomeHelper$Stub() {
		super((IBlockAccess)null);
	}

	BiomeGenBase getBiomeGenAt(int var1, int var2, int var3) {
		return null;
	}

	float getTemperature(int var1, int var2, int var3) {
		return 0.5F;
	}

	float getRainfall(int var1, int var2, int var3) {
		return 1.0F;
	}

	int getWaterColorMultiplier(int var1, int var2, int var3) {
		return 16777215;
	}
}
