package com.pclewis.mcpatcher.mod;

import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.IBlockAccess;

abstract class BiomeHelper {
	static BiomeHelper instance;
	IBlockAccess blockAccess;

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

	static class Stub extends BiomeHelper {
		Stub() {
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

	static class New extends BiomeHelper {
		private BiomeGenBase lastBiome;
		private int lastI;
		private int lastK;

		New(IBlockAccess var1) {
			super(var1);
		}

		boolean useBlockBlending() {
			return true;
		}

		BiomeGenBase getBiomeGenAt(int var1, int var2, int var3) {
			if (this.lastBiome == null || var1 != this.lastI || var3 != this.lastK) {
				this.lastI = var1;
				this.lastK = var3;
				this.lastBiome = this.blockAccess.func_48454_a(var1, var3);
			}

			return this.lastBiome;
		}

		float getTemperature(int var1, int var2, int var3) {
			return this.getBiomeGenAt(var1, var2, var3).func_48411_i();
		}

		float getRainfall(int var1, int var2, int var3) {
			return this.getBiomeGenAt(var1, var2, var3).func_48414_h();
		}

		int getWaterColorMultiplier(int var1, int var2, int var3) {
			return this.getBiomeGenAt(var1, var2, var3).waterColorMultiplier;
		}
	}
}
