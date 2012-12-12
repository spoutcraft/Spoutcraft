package com.pclewis.mcpatcher.mod;

import com.pclewis.mcpatcher.MCLogger;
import com.pclewis.mcpatcher.MCPatcherUtils;

import java.lang.reflect.Field;

final public class TileSize {
    private static final MCLogger logger = MCLogger.getLogger(MCPatcherUtils.HD_TEXTURES);

    public static int int_size;
    public static int int_sizeMinus1;
    public static int int_sizeHalf;
    public static int int_glBufferSize = 0x10000;
    public static int int_numPixels;
    public static int int_numBytes;
    public static int int_numPixelsMinus1;
    public static int int_compassNeedleMin;
    public static int int_compassNeedleMax;
    public static int int_compassCrossMin;
    public static int int_compassCrossMax;
    public static int int_flameHeight;
    public static int int_flameHeightMinus1;
    public static int int_flameArraySize;

    public static float float_size;
    public static float float_sizeMinus1;
    public static float float_sizeMinus0_01;
    public static float float_sizeHalf;
    public static float float_size16;
    public static float float_reciprocal;
    public static float float_texNudge;
    public static float float_flameNudge;

    public static double double_size;
    public static double double_sizeMinus1;
    public static double double_compassCenterMin;
    public static double double_compassCenterMax;

    static {
        setTileSize(16);
    }

    public static void setTileSize(int size) {
        int_size = size;
        int_sizeMinus1 = size - 1;
        int_sizeHalf = size / 2;
        int_glBufferSize = Math.max(int_glBufferSize, 1024 * size * size);
        int_numPixels = size * size;
        int_numBytes = 4 * int_numPixels;
        int_numPixelsMinus1 = int_numPixels - 1;
        int_compassNeedleMin = size / -2;
        int_compassNeedleMax = size;
        int_compassCrossMin = size / -4;
        int_compassCrossMax = size / 4;
        int_flameHeight = size + 4;
        int_flameHeightMinus1 = int_flameHeight - 1;
        int_flameArraySize = size * int_flameHeight;

        float_size = (float) int_size;
        float_sizeMinus1 = float_size - 1.0F;
        float_sizeMinus0_01 = float_size - 0.01F;
        float_sizeHalf = float_size / 2.0F;
        float_size16 = float_size * 16.0F;
        float_reciprocal = 1.0F / float_size;
        float_texNudge = 1.0F / (float_size * float_size * 2.0F);
        if (size < 64) {
            float_flameNudge = 1.0F + 0.96F / float_size;
        } else {
            float_flameNudge = 1.0F + 1.28F / float_size;
        }

        double_size = (double) int_size;
        double_sizeMinus1 = double_size - 1.0;
        double_compassCenterMin = double_size / 2.0 - 0.5;
        double_compassCenterMax = double_size / 2.0 + 0.5;

        dump();
    }

    private static void dump() {
        for (Field f : TileSize.class.getDeclaredFields()) {
            if (f.getName().contains("_")) {
                try {
                    logger.finer("%s = %s", f.getName(), f.get(null));
                } catch (Exception e) {
                    logger.finer("%s: %s", f.getName(), e.toString());
                }
            }
        }
    }
}
