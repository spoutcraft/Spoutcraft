package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.Config;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Entity;
import net.minecraft.src.World;

public class ColorizeWorld {
	private static final int fogBlendRadius = Config.getInt("Custom Colors", "fogBlendRadius", 7);
	private static final float fogBlendScale = (float)Math.pow((double)(2 * fogBlendRadius + 1), -2.0D);
	private static final String TEXT_KEY = "text.";
	private static final String TEXT_CODE_KEY = "text.code.";
	private static final int CLOUDS_DEFAULT = 0;
	private static final int CLOUDS_FAST = 1;
	private static final int CLOUDS_FANCY = 2;
	private static int cloudType = 0;
	private static final List biomes = new ArrayList();
	private static boolean biomesLogged;
	private static Entity fogCamera;
	private static final Map textColorMap = new HashMap();
	private static final int[] textCodeColors = new int[32];
	private static final boolean[] textCodeColorSet = new boolean[32];
	private static int signTextColor;
	public static float[] netherFogColor;
	public static float[] endFogColor;
	public static int endSkyColor;

	static void reset() {
		netherFogColor = new float[] {0.2F, 0.03F, 0.03F};
		endFogColor = new float[] {0.075F, 0.075F, 0.094F};
		endSkyColor = 1579032;
		cloudType = 0;
		textColorMap.clear();

		for (int var0 = 0; var0 < textCodeColorSet.length; ++var0) {
			textCodeColorSet[var0] = false;
		}

		signTextColor = 0;
	}

	static void reloadFogColors(Properties var0) {
		Colorizer.loadFloatColor("fog.nether", netherFogColor);
		Colorizer.loadFloatColor("fog.end", endFogColor);
		endSkyColor = Colorizer.loadIntColor("sky.end", endSkyColor);
	}

	static void reloadCloudType(Properties var0) {
		String var1 = var0.getProperty("clouds", "").trim().toLowerCase();

		if (var1.equals("fast")) {
			cloudType = 1;
		} else if (var1.equals("fancy")) {
			cloudType = 2;
		}
	}

	static void reloadTextColors(Properties var0) {
		for (int var1 = 0; var1 < textCodeColors.length; ++var1) {
			textCodeColorSet[var1] = Colorizer.loadIntColor("text.code." + var1, textCodeColors, var1);

			if (textCodeColorSet[var1] && var1 + 16 < textCodeColors.length) {
				textCodeColors[var1 + 16] = (textCodeColors[var1] & 16579836) >> 2;
				textCodeColorSet[var1 + 16] = true;
			}
		}

		Iterator var8 = var0.entrySet().iterator();

		while (var8.hasNext()) {
			Entry var2 = (Entry)var8.next();

			if (var2.getKey() instanceof String && var2.getValue() instanceof String) {
				String var3 = (String)var2.getKey();
				String var4 = (String)var2.getValue();

				if (var3.startsWith("text.") && !var3.startsWith("text.code.")) {
					var3 = var3.substring("text.".length()).trim();

					try {
						int var6;

						if (var3.equals("xpbar")) {
							var6 = 8453920;
						} else if (var3.equals("boss")) {
							var6 = 16711935;
						} else {
							var6 = Integer.parseInt(var3, 16);
						}

						int var5 = Integer.parseInt(var4, 16);
						textColorMap.put(Integer.valueOf(var6), Integer.valueOf(var5));
					} catch (NumberFormatException var7) {
						;
					}
				}
			}
		}

		signTextColor = Colorizer.loadIntColor("text.sign", 0);
	}

	public static void setupBiome(BiomeGenBase var0) {
		biomes.add(var0);
	}

	public static void setupForFog(Entity var0) {
		fogCamera = var0;

		if (!biomesLogged) {
			biomesLogged = true;
			Iterator var1 = biomes.iterator();

			while (var1.hasNext()) {
				BiomeGenBase var2 = (BiomeGenBase)var1.next();
				int var3 = ColorMap.getX((double)var2.temperature, (double)var2.rainfall);
				int var4 = ColorMap.getY((double)var2.temperature, (double)var2.rainfall);
			}
		}
	}

	public static boolean computeFogColor(int var0) {
		if (var0 >= 0 && var0 < Colorizer.fixedColorMaps.length && fogCamera != null && Colorizer.fixedColorMaps[var0].isCustom()) {
			float[] var1 = new float[3];
			int var2 = (int)fogCamera.posX;
			int var3 = (int)fogCamera.posY;
			int var4 = (int)fogCamera.posZ;
			Colorizer.setColor[0] = 0.0F;
			Colorizer.setColor[1] = 0.0F;
			Colorizer.setColor[2] = 0.0F;

			for (int var5 = -fogBlendRadius; var5 <= fogBlendRadius; ++var5) {
				for (int var6 = -fogBlendRadius; var6 <= fogBlendRadius; ++var6) {
					int var7 = ColorizeBlock.colorizeBiome(16777215, var0, var2 + var5, var3, var4 + var6);
					Colorizer.intToFloat3(var7, var1);
					Colorizer.setColor[0] += var1[0];
					Colorizer.setColor[1] += var1[1];
					Colorizer.setColor[2] += var1[2];
				}
			}

			Colorizer.setColor[0] *= fogBlendScale;
			Colorizer.setColor[1] *= fogBlendScale;
			Colorizer.setColor[2] *= fogBlendScale;
			return true;
		} else {
			return false;
		}
	}

	public static boolean computeFogColor(World var0, float var1) {
		if (var0.provider.dimensionId == 0 && computeFogColor(7)) {
			computeLightningFlash(var0, var1);
			return true;
		} else {
			return false;
		}
	}

	public static boolean computeSkyColor(World var0, float var1) {
		if (var0.provider.dimensionId == 0 && computeFogColor(8)) {
			computeLightningFlash(var0, var1);
			return true;
		} else {
			return false;
		}
	}

	private static void computeLightningFlash(World var0, float var1) {
		if (var0.lastLightningBolt > 0) {
			var1 = 0.45F * Colorizer.clamp((float)var0.lastLightningBolt - var1);
			Colorizer.setColor[0] = Colorizer.setColor[0] * (1.0F - var1) + 0.8F * var1;
			Colorizer.setColor[1] = Colorizer.setColor[1] * (1.0F - var1) + 0.8F * var1;
			Colorizer.setColor[2] = Colorizer.setColor[2] * (1.0F - var1) + 0.8F * var1;
		}
	}

	public static boolean drawFancyClouds(boolean var0) {
		switch (cloudType) {
			case 1:
				return false;

			case 2:
				return true;

			default:
				return var0;
		}
	}

	public static int colorizeText(int var0) {
		int var1 = var0 & -16777216;
		var0 &= 16777215;
		Integer var2 = (Integer)textColorMap.get(Integer.valueOf(var0));
		return var2 == null ? var1 | var0 : var1 | var2.intValue();
	}

	public static int colorizeText(int var0, int var1) {
		return var1 >= 0 && var1 < textCodeColors.length && textCodeColorSet[var1] ? var0 & -16777216 | textCodeColors[var1] : var0;
	}

	public static int colorizeSignText() {
		return signTextColor;
	}

	static {
		try {
			reset();
		} catch (Throwable var1) {
			var1.printStackTrace();
		}
	}
}
