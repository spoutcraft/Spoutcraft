package com.pclewis.mcpatcher.mod;

import com.pclewis.mcpatcher.MCLogger;
import com.pclewis.mcpatcher.MCPatcherUtils;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.WorldChunkManager;

abstract class BiomeHelper {
    static BiomeHelper instance;

    IBlockAccess blockAccess;

    static String getBiomeNameAt(int i, int j, int k) {
        if (instance == null) {
            return null;
        } else {
            BiomeGenBase biome = instance.getBiomeGenAt(i, j, k);
            return biome == null ? null : biome.biomeName;
        }
    }

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

    static class New extends BiomeHelper {
        private static boolean logged;

        private BiomeGenBase lastBiome;
        private int lastI;
        private int lastK;

        New(IBlockAccess blockAccess) {
            super(blockAccess);
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
                lastBiome = blockAccess.getBiomeGenForCoords(i, k);
            }
            return lastBiome;
        }

        @Override
        float getTemperature(int i, int j, int k) {
            return getBiomeGenAt(i, j, k).getFloatTemperature();
        }

        @Override
        float getRainfall(int i, int j, int k) {
            return getBiomeGenAt(i, j, k).getFloatRainfall();
        }

        @Override
        int getWaterColorMultiplier(int i, int j, int k) {
            return getBiomeGenAt(i, j, k).waterColorMultiplier;
        }
    }
}
