package com.prupe.mcpatcher.cc;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.TexturePackAPI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ColorizeBlock {
	private static final MCLogger logger = MCLogger.getLogger("Custom Colors");
	private static final ResourceLocation REDSTONE_COLORS = TexturePackAPI.newMCPatcherResourceLocation("colormap/redstone.png");
	private static final ResourceLocation STEM_COLORS = TexturePackAPI.newMCPatcherResourceLocation("colormap/stem.png");
	private static final ResourceLocation PUMPKIN_STEM_COLORS = TexturePackAPI.newMCPatcherResourceLocation("colormap/pumpkinstem.png");
	private static final ResourceLocation MELON_STEM_COLORS = TexturePackAPI.newMCPatcherResourceLocation("colormap/melonstem.png");
	private static final ResourceLocation SWAMPGRASSCOLOR = TexturePackAPI.newMCPatcherResourceLocation("colormap/swampgrass.png");
	private static final ResourceLocation SWAMPFOLIAGECOLOR = TexturePackAPI.newMCPatcherResourceLocation("colormap/swampfoliage.png");
	private static final ResourceLocation PINECOLOR = TexturePackAPI.newMCPatcherResourceLocation("colormap/pine.png");
	private static final ResourceLocation BIRCHCOLOR = TexturePackAPI.newMCPatcherResourceLocation("colormap/birch.png");
	private static final ResourceLocation FOLIAGECOLOR = TexturePackAPI.newMCPatcherResourceLocation("colormap/foliage.png");
	private static final ResourceLocation WATERCOLOR = TexturePackAPI.newMCPatcherResourceLocation("colormap/water.png");
	private static final ResourceLocation UNDERWATERCOLOR = TexturePackAPI.newMCPatcherResourceLocation("colormap/underwater.png");
	private static final ResourceLocation FOGCOLOR0 = TexturePackAPI.newMCPatcherResourceLocation("colormap/fog0.png");
	private static final ResourceLocation SKYCOLOR0 = TexturePackAPI.newMCPatcherResourceLocation("colormap/sky0.png");
	private static final String PALETTE_BLOCK_KEY = "palette.block.";
	private static final int BLOCK_ID_PUMPKIN_STEM = 104;
	private static final int BLOCK_ID_MELON_STEM = 105;
	private static final ColorMap[] blockColorMaps = new ColorMap[Block.blocksList.length];
	private static final Map<Float, ColorMap> blockMetaColorMaps = new HashMap();
	private static int lilypadColor;
	private static float[][] redstoneColor;
	private static int[] pumpkinStemColors;
	private static int[] melonStemColors;
	private static final int blockBlendRadius = Config.getInt("Custom Colors", "blockBlendRadius", 1);
	private static final float blockBlendScale = (float)Math.pow((double)(2 * blockBlendRadius + 1), -2.0D);
	public static float[] waterColor;

	static void reset() {
		Colorizer.fixedColorMaps[0] = new ColorMap(5131854);
		Colorizer.fixedColorMaps[1] = new ColorMap(5131854);
		Colorizer.fixedColorMaps[2] = new ColorMap(6396257);
		Colorizer.fixedColorMaps[3] = new ColorMap(8431445);
		Colorizer.fixedColorMaps[4] = new ColorMap(4764952);
		Colorizer.fixedColorMaps[5] = new ColorMap(16777215);
		Colorizer.fixedColorMaps[6] = new ColorMap(329011);
		Colorizer.fixedColorMaps[7] = new ColorMap(12638463);
		Colorizer.fixedColorMaps[8] = new ColorMap(16777215);
		Arrays.fill(blockColorMaps, (Object)null);
		blockMetaColorMaps.clear();
		lilypadColor = 2129968;
		waterColor = new float[] {0.2F, 0.3F, 1.0F};
		redstoneColor = (float[][])null;
		pumpkinStemColors = null;
		melonStemColors = null;
	}

	static void reloadColorMaps(Properties properties) {
		Colorizer.fixedColorMaps[0].loadColorMap(Colorizer.useSwampColors, SWAMPGRASSCOLOR);
		Colorizer.fixedColorMaps[1].loadColorMap(Colorizer.useSwampColors, SWAMPFOLIAGECOLOR);
		Colorizer.fixedColorMaps[2].loadColorMap(Colorizer.useTreeColors, PINECOLOR);
		Colorizer.fixedColorMaps[3].loadColorMap(Colorizer.useTreeColors, BIRCHCOLOR);
		Colorizer.fixedColorMaps[4].loadColorMap(Colorizer.useTreeColors, FOLIAGECOLOR);
		Colorizer.fixedColorMaps[4].clear();
		Colorizer.fixedColorMaps[5].loadColorMap(Colorizer.useWaterColors, WATERCOLOR);
		Colorizer.fixedColorMaps[6].loadColorMap(Colorizer.useWaterColors, UNDERWATERCOLOR);
		Colorizer.fixedColorMaps[7].loadColorMap(Colorizer.useFogColors, FOGCOLOR0);
		Colorizer.fixedColorMaps[8].loadColorMap(Colorizer.useFogColors, SKYCOLOR0);
	}

	static void reloadSwampColors(Properties properties) {
		int[] temp = new int[] {lilypadColor};
		Colorizer.loadIntColor("lilypad", temp, 0);
		lilypadColor = temp[0];
	}

	static void reloadBlockColors(Properties properties) {
		Iterator i$ = properties.entrySet().iterator();

		while (i$.hasNext()) {
			Entry entry = (Entry)i$.next();

			if (entry.getKey() instanceof String && entry.getValue() instanceof String) {
				String key = (String)entry.getKey();
				String value = (String)entry.getValue();

				if (key.startsWith("palette.block.")) {
					ResourceLocation address = TexturePackAPI.parseResourceLocation(Colorizer.COLOR_PROPERTIES, key.substring("palette.block.".length()).trim());

					if (address != null) {
						ColorMap colorMap = new ColorMap(16777215);
						colorMap.loadColorMap(true, address);

						if (colorMap.isCustom()) {
							String[] arr$ = value.split("\\s+");
							int len$ = arr$.length;

							for (int i$1 = 0; i$1 < len$; ++i$1) {
								String idString = arr$[i$1];
								String[] tokens = idString.split(":");
								int[] tokensInt = new int[tokens.length];

								try {
									for (int e = 0; e < tokens.length; ++e) {
										tokensInt[e] = Integer.parseInt(tokens[e]);
									}
								} catch (NumberFormatException var14) {
									continue;
								}

								switch (tokensInt.length) {
									case 1:
										if (tokensInt[0] < 0 || tokensInt[0] >= blockColorMaps.length) {
											continue;
										}

										blockColorMaps[tokensInt[0]] = colorMap;
										break;

									case 2:
										blockMetaColorMaps.put(Float.valueOf(ColorMap.getBlockMetaKey(tokensInt[0], tokensInt[1])), colorMap);
										break;

									default:
										continue;
								}

								logger.finer("using %s for block %s, default color %06x", new Object[] {key, idString, Integer.valueOf(colorMap.colorize())});
							}
						}
					}
				}
			}
		}
	}

	static void reloadRedstoneColors(Properties properties) {
		int[] rgb = MCPatcherUtils.getImageRGB(TexturePackAPI.getImage(REDSTONE_COLORS));

		if (rgb != null && rgb.length >= 16) {
			redstoneColor = new float[16][];

			for (int i = 0; i < 16; ++i) {
				float[] f = new float[3];
				Colorizer.intToFloat3(rgb[i], f);
				redstoneColor[i] = f;
			}
		}
	}

	static void reloadStemColors(Properties properties) {
		int[] stemColors = getStemRGB(STEM_COLORS);
		pumpkinStemColors = getStemRGB(PUMPKIN_STEM_COLORS);

		if (pumpkinStemColors == null) {
			pumpkinStemColors = stemColors;
		}

		melonStemColors = getStemRGB(MELON_STEM_COLORS);

		if (melonStemColors == null) {
			melonStemColors = stemColors;
		}
	}

	private static int[] getStemRGB(ResourceLocation resource) {
		int[] rgb = MCPatcherUtils.getImageRGB(TexturePackAPI.getImage(resource));
		return rgb != null && rgb.length >= 8 ? rgb : null;
	}

	public static int colorizeBiome(int defaultColor, int index, double temperature, double rainfall) {
		return Colorizer.fixedColorMaps[index].colorize(defaultColor, temperature, rainfall);
	}

	public static int colorizeBiome(int defaultColor, int index) {
		return Colorizer.fixedColorMaps[index].colorize(defaultColor);
	}

	public static int colorizeBiome(int defaultColor, int index, int i, int j, int k) {
		return Colorizer.fixedColorMaps[index].colorize(defaultColor, i, j, k);
	}

	public static int colorizeBiomeWithBlending(int defaultColor, int index, int i, int j, int k) {
		return colorizeWithBlending(Colorizer.fixedColorMaps[index], defaultColor, i, j, k);
	}

	public static int colorizeWater(Object dummy, int i, int k) {
		return Colorizer.fixedColorMaps[5].colorize(BiomeHelper.getWaterColorMultiplier(i, 64, k), i, 64, k);
	}

	public static int colorizeBlock(Block block, int i, int j, int k, int metadata) {
		ColorMap colorMap = null;

		if (!blockMetaColorMaps.isEmpty()) {
			colorMap = (ColorMap)blockMetaColorMaps.get(Float.valueOf(ColorMap.getBlockMetaKey(block.blockID, metadata)));
		}

		if (colorMap == null && block.blockID >= 0 && block.blockID < blockColorMaps.length) {
			colorMap = blockColorMaps[block.blockID];
		}

		return colorizeWithBlending(colorMap, 16777215, i, j, k);
	}

	private static int colorizeWithBlending(ColorMap colorMap, int defaultColor, int i, int j, int k) {
		if (colorMap != null && colorMap.isCustom() && blockBlendRadius > 0) {
			float[] sum = new float[3];
			float[] sample = new float[3];

			for (int di = -blockBlendRadius; di <= blockBlendRadius; ++di) {
				for (int dk = -blockBlendRadius; dk <= blockBlendRadius; ++dk) {
					int rgb = colorMap.colorize(defaultColor, i + di, j, k + dk);
					Colorizer.intToFloat3(rgb, sample);
					sum[0] += sample[0];
					sum[1] += sample[1];
					sum[2] += sample[2];
				}
			}

			sum[0] *= blockBlendScale;
			sum[1] *= blockBlendScale;
			sum[2] *= blockBlendScale;
			return Colorizer.float3ToInt(sum);
		} else {
			return defaultColor;
		}
	}

	public static int colorizeBlock(Block block) {
		ColorMap colorMap = blockColorMaps[block.blockID];
		return colorMap == null ? 16777215 : colorMap.colorize(16777215);
	}

	public static int colorizeStem(int defaultColor, Block block, int blockMetadata) {
		int[] colors;

		switch (block.blockID) {
			case 104:
				colors = pumpkinStemColors;
				break;

			case 105:
				colors = melonStemColors;
				break;

			default:
				return defaultColor;
		}

		return colors == null ? defaultColor : colors[blockMetadata & 7];
	}

	public static int getLilyPadColor() {
		return lilypadColor;
	}

	public static int getItemColorFromDamage(int defaultColor, int blockID, int damage) {
		return blockID != 8 && blockID != 9 ? defaultColor : colorizeBiome(defaultColor, 5);
	}

	public static boolean computeRedstoneWireColor(int current) {
		if (redstoneColor == null) {
			return false;
		} else {
			System.arraycopy(redstoneColor[Math.max(Math.min(current, 15), 0)], 0, Colorizer.setColor, 0, 3);
			return true;
		}
	}

	public static int colorizeRedstoneWire(IBlockAccess blockAccess, int i, int j, int k, int defaultColor) {
		if (redstoneColor == null) {
			return defaultColor;
		} else {
			int metadata = Math.max(Math.min(blockAccess.getBlockMetadata(i, j, k), 15), 0);
			return Colorizer.float3ToInt(redstoneColor[metadata]);
		}
	}

	public static boolean computeWaterColor(double x, double y, double z) {
		if (Colorizer.useParticleColors && Colorizer.fixedColorMaps[5].isCustom()) {
			int rgb = colorizeBiome(16777215, 5, (int)x, (int)y, (int)z);
			float[] multiplier = new float[3];
			Colorizer.intToFloat3(rgb, multiplier);

			for (int i = 0; i < 3; ++i) {
				waterColor[i] = multiplier[i] * ColorizeEntity.waterBaseColor[i];
			}

			return true;
		} else {
			return false;
		}
	}

	public static void computeWaterColor() {
		int rgb = colorizeBiome(16777215, 5);
		Colorizer.intToFloat3(rgb, waterColor);
	}

	public static void colorizeWaterBlockGL(int blockID) {
		if (blockID == 8 || blockID == 9) {
			computeWaterColor();
			GL11.glColor4f(waterColor[0], waterColor[1], waterColor[2], 1.0F);
		}
	}

	static {
		try {
			reset();
		} catch (Throwable var1) {
			var1.printStackTrace();
		}
	}
}
