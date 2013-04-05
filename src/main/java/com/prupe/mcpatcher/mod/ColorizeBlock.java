package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.Config;
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
import org.lwjgl.opengl.GL11;

public class ColorizeBlock {
	private static final String REDSTONE_COLORS = "/misc/redstonecolor.png";
	private static final String STEM_COLORS = "/misc/stemcolor.png";
	private static final String PALETTE_BLOCK_KEY = "palette.block.";
	private static final ColorMap[] blockColorMaps = new ColorMap[Block.blocksList.length];
	private static final Map blockMetaColorMaps = new HashMap();
	private static int lilypadColor;
	private static float[][] redstoneColor;
	private static int[] stemColors;
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
		stemColors = null;
	}

	static void reloadColorMaps(Properties var0) {
		Colorizer.fixedColorMaps[0].loadColorMap(Colorizer.useSwampColors, "/misc/swampgrasscolor.png");
		Colorizer.fixedColorMaps[1].loadColorMap(Colorizer.useSwampColors, "/misc/swampfoliagecolor.png");
		Colorizer.fixedColorMaps[2].loadColorMap(Colorizer.useTreeColors, "/misc/pinecolor.png");
		Colorizer.fixedColorMaps[3].loadColorMap(Colorizer.useTreeColors, "/misc/birchcolor.png");
		Colorizer.fixedColorMaps[4].loadColorMap(Colorizer.useTreeColors, "/misc/foliagecolor.png");
		Colorizer.fixedColorMaps[4].clear();
		Colorizer.fixedColorMaps[5].loadColorMap(Colorizer.useWaterColors, "/misc/watercolorX.png");
		Colorizer.fixedColorMaps[6].loadColorMap(Colorizer.useWaterColors, "/misc/underwatercolor.png");
		Colorizer.fixedColorMaps[7].loadColorMap(Colorizer.useFogColors, "/misc/fogcolor0.png");
		Colorizer.fixedColorMaps[8].loadColorMap(Colorizer.useFogColors, "/misc/skycolor0.png");
	}

	static void reloadSwampColors(Properties var0) {
		int[] var1 = new int[] {lilypadColor};
		Colorizer.loadIntColor("lilypad", var1, 0);
		lilypadColor = var1[0];
	}

	static void reloadBlockColors(Properties var0) {
		Iterator var1 = var0.entrySet().iterator();

		while (var1.hasNext()) {
			Entry var2 = (Entry)var1.next();

			if (var2.getKey() instanceof String && var2.getValue() instanceof String) {
				String var3 = (String)var2.getKey();
				String var4 = (String)var2.getValue();

				if (var3.startsWith("palette.block.")) {
					var3 = var3.substring("palette.block.".length()).trim();
					ColorMap var5 = new ColorMap(16777215);
					var5.loadColorMap(true, var3);

					if (var5.isCustom()) {
						String[] var6 = var4.split("\\s+");
						int var7 = var6.length;

						for (int var8 = 0; var8 < var7; ++var8) {
							String var9 = var6[var8];
							String[] var10 = var9.split(":");
							int[] var11 = new int[var10.length];

							try {
								for (int var12 = 0; var12 < var10.length; ++var12) {
									var11[var12] = Integer.parseInt(var10[var12]);
								}
							} catch (NumberFormatException var13) {
								continue;
							}

							switch (var11.length) {
								case 1:
									if (var11[0] < 0 || var11[0] >= blockColorMaps.length) {
										continue;
									}

									blockColorMaps[var11[0]] = var5;
									break;

								case 2:
									blockMetaColorMaps.put(Float.valueOf(ColorMap.getBlockMetaKey(var11[0], var11[1])), var5);
									break;

								default:
									continue;
							}
						}
					}
				}
			}
		}
	}

	static void reloadRedstoneColors(Properties var0) {
		int[] var1 = MCPatcherUtils.getImageRGB(TexturePackAPI.getImage("/misc/redstonecolor.png"));

		if (var1 != null && var1.length >= 16) {
			redstoneColor = new float[16][];

			for (int var2 = 0; var2 < 16; ++var2) {
				float[] var3 = new float[3];
				Colorizer.intToFloat3(var1[var2], var3);
				redstoneColor[var2] = var3;
			}
		}
	}

	static void reloadStemColors(Properties var0) {
		int[] var1 = MCPatcherUtils.getImageRGB(TexturePackAPI.getImage("/misc/stemcolor.png"));

		if (var1 != null && var1.length >= 8) {
			stemColors = var1;
		}
	}

	public static int colorizeBiome(int var0, int var1, double var2, double var4) {
		return Colorizer.fixedColorMaps[var1].colorize(var0, var2, var4);
	}

	public static int colorizeBiome(int var0, int var1) {
		return Colorizer.fixedColorMaps[var1].colorize(var0);
	}

	public static int colorizeBiome(int var0, int var1, int var2, int var3, int var4) {
		return Colorizer.fixedColorMaps[var1].colorize(var0, var2, var3, var4);
	}

	public static int colorizeBiomeWithBlending(int var0, int var1, int var2, int var3, int var4) {
		return colorizeWithBlending(Colorizer.fixedColorMaps[var1], var0, var2, var3, var4);
	}

	public static int colorizeWater(Object var0, int var1, int var2) {
		return Colorizer.fixedColorMaps[5].colorize(BiomeHelper.getWaterColorMultiplier(var1, 64, var2), var1, 64, var2);
	}

	public static int colorizeBlock(Block var0, int var1, int var2, int var3, int var4) {
		ColorMap var5 = null;

		if (!blockMetaColorMaps.isEmpty()) {
			var5 = (ColorMap)blockMetaColorMaps.get(Float.valueOf(ColorMap.getBlockMetaKey(var0.blockID, var4)));
		}

		if (var5 == null && var0.blockID >= 0 && var0.blockID < blockColorMaps.length) {
			var5 = blockColorMaps[var0.blockID];
		}

		return colorizeWithBlending(var5, 16777215, var1, var2, var3);
	}

	private static int colorizeWithBlending(ColorMap var0, int var1, int var2, int var3, int var4) {
		if (var0 != null && var0.isCustom() && blockBlendRadius > 0) {
			float[] var5 = new float[3];
			float[] var6 = new float[3];

			for (int var7 = -blockBlendRadius; var7 <= blockBlendRadius; ++var7) {
				for (int var8 = -blockBlendRadius; var8 <= blockBlendRadius; ++var8) {
					int var9 = var0.colorize(var1, var2 + var7, var3, var4 + var8);
					Colorizer.intToFloat3(var9, var6);
					var5[0] += var6[0];
					var5[1] += var6[1];
					var5[2] += var6[2];
				}
			}

			var5[0] *= blockBlendScale;
			var5[1] *= blockBlendScale;
			var5[2] *= blockBlendScale;
			return Colorizer.float3ToInt(var5);
		} else {
			return var1;
		}
	}

	public static int colorizeBlock(Block var0) {
		ColorMap var1 = blockColorMaps[var0.blockID];
		return var1 == null ? 16777215 : var1.colorize(16777215);
	}

	public static int colorizeStem(int var0, int var1) {
		return stemColors == null ? var0 : stemColors[var1 & 7];
	}

	public static int getLilyPadColor() {
		return lilypadColor;
	}

	public static int getItemColorFromDamage(int var0, int var1, int var2) {
		return var1 != 8 && var1 != 9 ? var0 : colorizeBiome(var0, 5);
	}

	public static boolean computeRedstoneWireColor(int var0) {
		if (redstoneColor == null) {
			return false;
		} else {
			System.arraycopy(redstoneColor[Math.max(Math.min(var0, 15), 0)], 0, Colorizer.setColor, 0, 3);
			return true;
		}
	}

	public static int colorizeRedstoneWire(IBlockAccess var0, int var1, int var2, int var3, int var4) {
		if (redstoneColor == null) {
			return var4;
		} else {
			int var5 = Math.max(Math.min(var0.getBlockMetadata(var1, var2, var3), 15), 0);
			return Colorizer.float3ToInt(redstoneColor[var5]);
		}
	}

	public static boolean computeWaterColor(double var0, double var2, double var4) {
		if (Colorizer.useParticleColors && Colorizer.fixedColorMaps[5].isCustom()) {
			int var6 = colorizeBiome(16777215, 5, (int)var0, (int)var2, (int)var4);
			float[] var7 = new float[3];
			Colorizer.intToFloat3(var6, var7);

			for (int var8 = 0; var8 < 3; ++var8) {
				waterColor[var8] = var7[var8] * ColorizeEntity.waterBaseColor[var8];
			}

			return true;
		} else {
			return false;
		}
	}

	public static void computeWaterColor() {
		int var0 = colorizeBiome(16777215, 5);
		Colorizer.intToFloat3(var0, waterColor);
	}

	public static void colorizeWaterBlockGL(int var0) {
		if (var0 == 8 || var0 == 9) {
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
