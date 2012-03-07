package com.pclewis.mcpatcher.mod;

import com.pclewis.mcpatcher.MCPatcherUtils;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.WorldChunkManager;

abstract class BiomeHelper {
	static BiomeHelper instance;

	IBlockAccess blockAccess;

	BiomeHelper(IBlockAccess blockAccess) {
		this.blockAccess = blockAccess;
	}

	boolean useBlockBlending() {
		return false;
	}

	abstract BiomeGenBase getBiomeGenAt(int i, int j, int k);

	abstract float getTemperature(int i, int j, int k);

	abstract float getRainfall(int i, int j, int k);

	abstract int getWaterColorMultiplier(int i, int j, int k);

	static class Stub extends BiomeHelper {
		Stub() {
			super(null);
		}

		@Override
		BiomeGenBase getBiomeGenAt(int i, int j, int k) {
			return null;
		}

		@Override
		float getTemperature(int i, int j, int k) {
			return 0.5f;
		}

		@Override
		float getRainfall(int i, int j, int k) {
			return 1.0f;
		}

		@Override
		int getWaterColorMultiplier(int i, int j, int k) {
			return 0xffffff;
		}
	}

	static class Old extends BiomeHelper {
		WorldChunkManager chunkManager;

		Old(IBlockAccess blockAccess) {
			super(blockAccess);
			chunkManager = blockAccess.getWorldChunkManager();
		}

		@Override
		BiomeGenBase getBiomeGenAt(int i, int j, int k) {
			return chunkManager.getBiomeGenAt(i, k);
		}

		@Override
		float getTemperature(int i, int j, int k) {
			float[] tmp = new float[1];
			tmp = chunkManager.getTemperatures(tmp, i, k, 1, 1);
			return tmp[0];
		}

		@Override
		float getRainfall(int i, int j, int k) {
			float[] tmp = new float[1];
			tmp = chunkManager.getRainfall(tmp, i, k, 1, 1);
			return tmp[0];
		}

		@Override
		int getWaterColorMultiplier(int i, int j, int k) {
			return getBiomeGenAt(i, j, k).waterColorMultiplier;
		}
	}

	static class New extends BiomeHelper {
		private static boolean logged;

		private BiomeGenBase lastBiome;
		private int lastI;
		private int lastK;

		New(IBlockAccess blockAccess) {
			super(blockAccess);
			if (!logged) {
				logged = true;
				MCPatcherUtils.log("biomes v1.2 detected");
			}
		}

		@Override
		boolean useBlockBlending() {
			return true;
		}

		@Override
		BiomeGenBase getBiomeGenAt(int i, int j, int k) {
			if (lastBiome == null || i != lastI || k != lastK) {
				lastI = i;
				lastK = k;
				lastBiome = blockAccess.getWorldChunkManager().getBiomeGenAt(i, k);
			}
			return lastBiome;
		}

		@Override
		float getTemperature(int i, int j, int k) {
			return getBiomeGenAt(i, j, k).getIntTemperature();
		}

		@Override
		float getRainfall(int i, int j, int k) {
			return getBiomeGenAt(i, j, k).getIntRainfall();
		}

		@Override
		int getWaterColorMultiplier(int i, int j, int k) {
			return getBiomeGenAt(i, j, k).waterColorMultiplier;
		}
	}
}