package com.prupe.mcpatcher.cc;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCLogger;
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
	private static final MCLogger logger = MCLogger.getLogger("Custom Colors");
	private static final int fogBlendRadius = Config.getInt("Custom Colors", "fogBlendRadius", 7);
	private static final String TEXT_KEY = "text.";
	private static final String TEXT_CODE_KEY = "text.code.";
	private static final int CLOUDS_DEFAULT = 0;
	private static final int CLOUDS_FAST = 1;
	private static final int CLOUDS_FANCY = 2;
	private static int cloudType = 0;
	private static final List<BiomeGenBase> biomes = new ArrayList();
	private static boolean biomesLogged;
	private static Entity fogCamera;
	private static final Map<Integer, Integer> textColorMap = new HashMap();
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

		for (int i = 0; i < textCodeColorSet.length; ++i) {
			textCodeColorSet[i] = false;
		}

		signTextColor = 0;
	}

	static void reloadFogColors(Properties properties) {
		Colorizer.loadFloatColor("fog.nether", netherFogColor);
		Colorizer.loadFloatColor("fog.end", endFogColor);
		endSkyColor = Colorizer.loadIntColor("sky.end", endSkyColor);
	}

	static void reloadCloudType(Properties properties) {
		String value = properties.getProperty("clouds", "").trim().toLowerCase();

		if (value.equals("fast")) {
			cloudType = 1;
		} else if (value.equals("fancy")) {
			cloudType = 2;
		}
	}

	static void reloadTextColors(Properties properties) {
		for (int i$ = 0; i$ < textCodeColors.length; ++i$) {
			textCodeColorSet[i$] = Colorizer.loadIntColor("text.code." + i$, textCodeColors, i$);

			if (textCodeColorSet[i$] && i$ + 16 < textCodeColors.length) {
				textCodeColors[i$ + 16] = (textCodeColors[i$] & 16579836) >> 2;
				textCodeColorSet[i$ + 16] = true;
			}
		}

		Iterator var8 = properties.entrySet().iterator();

		while (var8.hasNext()) {
			Entry entry = (Entry)var8.next();

			if (entry.getKey() instanceof String && entry.getValue() instanceof String) {
				String key = (String)entry.getKey();
				String value = (String)entry.getValue();

				if (key.startsWith("text.") && !key.startsWith("text.code.")) {
					key = key.substring("text.".length()).trim();

					try {
						int oldColor;

						if (key.equals("xpbar")) {
							oldColor = 8453920;
						} else if (key.equals("boss")) {
							oldColor = 16711935;
						} else {
							oldColor = Integer.parseInt(key, 16);
						}

						int e = Integer.parseInt(value, 16);
						textColorMap.put(Integer.valueOf(oldColor), Integer.valueOf(e));
					} catch (NumberFormatException var7) {
						;
					}
				}
			}
		}

		signTextColor = Colorizer.loadIntColor("text.sign", 0);
	}

	public static void setupBiome(BiomeGenBase biome) {
		biomes.add(biome);
	}

	public static void setupForFog(Entity entity) {
		fogCamera = entity;

		if (!biomesLogged) {
			biomesLogged = true;
			Iterator i$ = biomes.iterator();

			while (i$.hasNext()) {
				BiomeGenBase biome = (BiomeGenBase)i$.next();
				int x = ColorMap.getX((double)biome.temperature, (double)biome.rainfall);
				int y = ColorMap.getY((double)biome.temperature, (double)biome.rainfall);
				logger.finer("setupBiome #%d \"%s\" %06x (%d,%d)", new Object[] {Integer.valueOf(biome.biomeID), biome.biomeName, Integer.valueOf(biome.waterColorMultiplier), Integer.valueOf(x), Integer.valueOf(y)});
			}
		}
	}

	public static boolean computeFogColor(int index) {
		if (index >= 0 && index < Colorizer.fixedColorMaps.length && fogCamera != null && Colorizer.fixedColorMaps[index].isCustom()) {
			int x = (int)fogCamera.posX;
			int y = (int)fogCamera.posY;
			int z = (int)fogCamera.posZ;
			Colorizer.fixedColorMaps[index].colorizeWithBlending(x, y, z, fogBlendRadius, Colorizer.setColor);
			return true;
		} else {
			return false;
		}
	}

	public static boolean computeFogColor(World world, float f) {
		if (world.provider.dimensionId == 0 && computeFogColor(7)) {
			computeLightningFlash(world, f);
			return true;
		} else {
			return false;
		}
	}

	public static boolean computeSkyColor(World world, float f) {
		if (world.provider.dimensionId == 0 && computeFogColor(8)) {
			computeLightningFlash(world, f);
			return true;
		} else {
			return false;
		}
	}

	private static void computeLightningFlash(World world, float f) {
		if (world.lastLightningBolt > 0) {
			f = 0.45F * Colorizer.clamp((float)world.lastLightningBolt - f);
			Colorizer.setColor[0] = Colorizer.setColor[0] * (1.0F - f) + 0.8F * f;
			Colorizer.setColor[1] = Colorizer.setColor[1] * (1.0F - f) + 0.8F * f;
			Colorizer.setColor[2] = Colorizer.setColor[2] * (1.0F - f) + 0.8F * f;
		}
	}

	public static boolean drawFancyClouds(boolean fancyGraphics) {
		switch (cloudType) {
			case 1:
				return false;

			case 2:
				return true;

			default:
				return fancyGraphics;
		}
	}

	public static int colorizeText(int defaultColor) {
		int high = defaultColor & -16777216;
		defaultColor &= 16777215;
		Integer newColor = (Integer)textColorMap.get(Integer.valueOf(defaultColor));
		return newColor == null ? high | defaultColor : high | newColor.intValue();
	}

	public static int colorizeText(int defaultColor, int index) {
		return index >= 0 && index < textCodeColors.length && textCodeColorSet[index] ? defaultColor & -16777216 | textCodeColors[index] : defaultColor;
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
