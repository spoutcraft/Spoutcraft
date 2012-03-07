package com.pclewis.mcpatcher.mod;

import com.pclewis.mcpatcher.MCPatcherUtils;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityRenderer;
import net.minecraft.src.EntitySheep;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemDye;
import net.minecraft.src.MapColor;
import net.minecraft.src.Potion;
import net.minecraft.src.TexturePackBase;
import net.minecraft.src.World;
import org.lwjgl.opengl.GL11;

public class Colorizer {
	private static final String COLOR_PROPERTIES = "/color.properties";
	private static final String LIGHTMAP_FORMAT = "/environment/lightmap%d.png";
	private static final String REDSTONE_COLORS = "/misc/redstonecolor.png";
	private static final String STEM_COLORS = "/misc/stemcolor.png";
	private static final String LAVA_DROP_COLORS = "/misc/lavadropcolor.png";
	private static final String MYCELIUM_COLORS = "/misc/myceliumparticlecolor.png";
	private static final String PALETTE_BLOCK_KEY = "palette.block.";
	private static final String TEXT_KEY = "text.";
	private static final String TEXT_CODE_KEY = "text.code.";
	private static final int COLOR_CODE_UNSET = -2;
	public static final int COLOR_MAP_SWAMP_GRASS = 0;
	public static final int COLOR_MAP_SWAMP_FOLIAGE = 1;
	public static final int COLOR_MAP_PINE = 2;
	public static final int COLOR_MAP_BIRCH = 3;
	public static final int COLOR_MAP_FOLIAGE = 4;
	public static final int COLOR_MAP_WATER = 5;
	public static final int COLOR_MAP_UNDERWATER = 6;
	public static final int COLOR_MAP_FOG0 = 7;
	public static final int COLOR_MAP_SKY0 = 8;
	public static final int NUM_FIXED_COLOR_MAPS = 9;
	private static final String[] MAP_MATERIALS = new String[]{"air", "grass", "sand", "cloth", "tnt", "ice", "iron", "foliage", "snow", "clay", "dirt", "stone", "water", "wood"};
	private static Properties properties;
	private static final ColorMap[] fixedColorMaps = new ColorMap[9];
	private static ColorMap[] blockColorMaps;
	private static final HashMap blockMetaColorMaps = new HashMap();
	private static int lilypadColor;
	private static float[] waterBaseColor;
	private static float[] lavaDropColors;
	private static int waterBottleColor;
	private static float[][] redstoneColor;
	private static int[] stemColors;
	private static ArrayList potions = new ArrayList();
	private static final Random random = new Random();
	private static final boolean useWaterColors = true;
	private static final boolean useSwampColors = true;
	private static final boolean useTreeColors = true;
	private static final boolean usePotionColors = true;
	private static final boolean useParticleColors = true;
	private static final boolean useLightmaps = true;
	private static final boolean useFogColors = true;
	private static final boolean useCloudType = true;
	private static final boolean useRedstoneColors = true;
	private static final boolean useStemColors = true;
	private static final boolean useEggColors = true;
	private static final boolean useMapColors = true;
	private static final boolean useSheepColors = true;
	private static final boolean useBlockColors = true;
	private static final boolean useTextColors = true;
	private static final int fogBlendRadius = 7;
	private static final float fogBlendScale = getBlendScale(fogBlendRadius);
	private static final int blockBlendRadius = 1;
	private static final float blockBlendScale = getBlendScale(blockBlendRadius);
	static TexturePackBase lastTexturePack;
	private static final int LIGHTMAP_SIZE = 16;
	private static final float LIGHTMAP_SCALE = 15.0F;
	private static HashMap lightmaps = new HashMap();
	public static final float[] setColor = new float[3];
	public static float[] waterColor;
	public static float[] portalColor = new float[]{1.0F, 0.3F, 0.9F};
	private static final HashMap entityNamesByID = new HashMap();
	private static final HashMap spawnerEggShellColors = new HashMap();
	private static final HashMap spawnerEggSpotColors = new HashMap();
	private static final int CLOUDS_DEFAULT = 0;
	private static final int CLOUDS_FAST = 1;
	private static final int CLOUDS_FANCY = 2;
	private static int cloudType = 0;
	private static final ArrayList biomes = new ArrayList();
	private static boolean biomesLogged;
	private static Entity fogCamera;
	public static float[] netherFogColor;
	public static float[] endFogColor;
	public static int endSkyColor;
	private static int[] myceliumColors;
	private static final HashMap textColorMap = new HashMap();
	private static final int[] textCodeColors = new int[32];

	public static int colorizeBiome(int var0, int var1, double var2, double var4) {
		return fixedColorMaps[var1].colorize(var0, var2, var4);
	}

	public static int colorizeBiome(int var0, int var1) {
		return fixedColorMaps[var1].colorize(var0);
	}

	public static int colorizeBiome(int var0, int var1, int var2, int var3, int var4) {
		return fixedColorMaps[var1].colorize(var0, var2, var3, var4);
	}

	public static int colorizeWater(Object var0, int var1, int var2) {
		return fixedColorMaps[5].colorize(BiomeHelper.instance.getWaterColorMultiplier(var1, 64, var2), var1, 64, var2);
	}

	public static int colorizeBlock(Block var0, int var1, int var2, int var3, int var4) {
		ColorMap var5 = null;
		if (!blockMetaColorMaps.isEmpty()) {
			var5 = (ColorMap)blockMetaColorMaps.get(Float.valueOf(ColorMap.getBlockMetaKey(var0.blockID, var4)));
		}

		if (var5 == null && var0.blockID >= 0 && var0.blockID < blockColorMaps.length) {
			var5 = blockColorMaps[var0.blockID];
		}

		if (var5 != null && var5.isCustom()) {
			if (BiomeHelper.instance.useBlockBlending() && blockBlendRadius != 0) {
				float[] var6 = new float[3];
				float[] var7 = new float[3];

				for (int var8 = -blockBlendRadius; var8 <= blockBlendRadius; ++var8) {
					for (int var9 = -blockBlendRadius; var9 <= blockBlendRadius; ++var9) {
						int var10 = var5.colorize(16777215, var1 + var8, var2, var3 + var9);
						intToFloat3(var10, var7);
						var6[0] += var7[0];
						var6[1] += var7[1];
						var6[2] += var7[2];
					}
				}

				var6[0] *= blockBlendScale;
				var6[1] *= blockBlendScale;
				var6[2] *= blockBlendScale;
				return float3ToInt(var6);
			} else {
				return var5.colorize(16777215, var1, var2, var3);
			}
		} else {
			return 16777215;
		}
	}

	public static int colorizeBlock(Block var0) {
		ColorMap var1 = blockColorMaps[var0.blockID];
		return var1 == null?16777215:var1.colorize(16777215);
	}

	public static int colorizeStem(int var0, int var1) {
		return stemColors == null?var0:stemColors[var1 & 7];
	}

	public static int colorizeSpawnerEgg(int var0, int var1, int var2) {
		if (!useEggColors) {
			return var0;
		} else {
			Integer var3 = null;
			HashMap var4 = var2 == 0?spawnerEggShellColors:spawnerEggSpotColors;
			if (var4.containsKey(Integer.valueOf(var1))) {
				var3 = (Integer)var4.get(Integer.valueOf(var1));
			} else if (entityNamesByID.containsKey(Integer.valueOf(var1))) {
				String var5 = (String)entityNamesByID.get(Integer.valueOf(var1));
				if (var5 != null) {
					int[] var6 = new int[]{var0};
					loadIntColor((var2 == 0?"egg.shell.":"egg.spots.") + var5, var6, 0);
					var4.put(Integer.valueOf(var1), Integer.valueOf(var6[0]));
					var3 = Integer.valueOf(var6[0]);
				}
			}

			return var3 == null?var0:var3.intValue();
		}
	}

	public static int colorizeText(int var0) {
		int var1 = var0 & -16777216;
		var0 &= 16777215;
		Integer var2 = (Integer)textColorMap.get(Integer.valueOf(var0));
		return var2 == null?var1 | var0:var1 | var2.intValue();
	}

	public static int colorizeText(int var0, int var1) {
		return var1 >= 0 && var1 < textCodeColors.length && textCodeColors[var1] != -2?var0 & -16777216 | textCodeColors[var1]:var0;
	}

	public static int getWaterBottleColor() {
		return waterBottleColor;
	}

	public static int getLilyPadColor() {
		return lilypadColor;
	}

	public static int getItemColorFromDamage(int var0, int var1, int var2) {
		return var1 != 8 && var1 != 9?var0:colorizeBiome(var0, 5);
	}

	public static boolean computeLightmap(EntityRenderer var0, World var1) {
		if (var1 != null && useLightmaps) {
			int var2 = var1.worldProvider.worldType;
			String var3 = String.format("/environment/lightmap%d.png", new Object[]{Integer.valueOf(var2)});
			BufferedImage var4;
			if (lightmaps.containsKey(Integer.valueOf(var2))) {
				var4 = (BufferedImage)lightmaps.get(Integer.valueOf(var2));
			} else {
				var4 = MCPatcherUtils.readImage(lastTexturePack.getResourceAsStream(var3));
				lightmaps.put(Integer.valueOf(var2), var4);
			}

			if (var4 == null) {
				return false;
			} else {
				int var5 = var4.getWidth();
				int var6 = var4.getHeight();
				if (var6 != 32) {
					lightmaps.put(Integer.valueOf(var2), (Object)null);
					return false;
				} else {
					int[] var7 = new int[var5 * var6];
					var4.getRGB(0, 0, var5, var6, var7, 0, var5);
					int[] var8 = new int[256];
					float var9 = clamp(var1.lightningFlash > 0?1.0F:1.1666666F * (var1.func_35464_b(1.0F) - 0.2F)) * (float)(var5 - 1);
					float var10 = clamp(var0.torchFlickerX + 0.5F) * (float)(var5 - 1);
					float var11 = clamp(Minecraft.theMinecraft.gameSettings.gammaSetting);
					float[] var12 = new float[48];
					float[] var13 = new float[48];
					float[] var14 = new float[3];

					int var15;
					for (var15 = 0; var15 < 16; ++var15) {
						interpolate(var7, var15 * var5, var9, var12, 3 * var15);
						interpolate(var7, (var15 + 16) * var5, var10, var13, 3 * var15);
					}

					for (var15 = 0; var15 < 16; ++var15) {
						for (int var16 = 0; var16 < 16; ++var16) {
							int var17;
							for (var17 = 0; var17 < 3; ++var17) {
								var14[var17] = clamp(var12[3 * var15 + var17] + var13[3 * var16 + var17]);
							}

							if (var11 != 0.0F) {
								for (var17 = 0; var17 < 3; ++var17) {
									float var18 = 1.0F - var14[var17];
									var18 = 1.0F - var18 * var18 * var18 * var18;
									var14[var17] = var11 * var18 + (1.0F - var11) * var14[var17];
								}
							}

							var8[var15 * 16 + var16] = -16777216 | float3ToInt(var14);
						}
					}

					Minecraft.theMinecraft.renderEngine.createTextureFromBytes(var8, 16, 16, var0.lightmapTexture);
					return true;
				}
			}
		} else {
			return false;
		}
	}

	public static boolean computeRedstoneWireColor(int var0) {
		if (redstoneColor == null) {
			return false;
		} else {
			System.arraycopy(redstoneColor[Math.max(Math.min(var0, 15), 0)], 0, setColor, 0, 3);
			return true;
		}
	}

	public static boolean computeWaterColor(double var0, double var2, double var4) {
		if (!useParticleColors) {
			return false;
		} else {
			int var6 = colorizeBiome(16777215, 5, (int)var0, (int)var2, (int)var4);
			float[] var7 = new float[3];
			intToFloat3(var6, var7);

			for (int var8 = 0; var8 < 3; ++var8) {
				waterColor[var8] = var7[var8] * waterBaseColor[var8];
			}

			return true;
		}
	}

	public static void computeWaterColor() {
		int var0 = colorizeBiome(16777215, 5);
		intToFloat3(var0, waterColor);
	}

	public static void colorizeWaterBlockGL(int var0) {
		if (var0 == 8 || var0 == 9) {
			computeWaterColor();
			GL11.glColor4f(waterColor[0], waterColor[1], waterColor[2], 1.0F);
		}
	}

	public static boolean computeLavaDropColor(int var0) {
		if (lavaDropColors == null) {
			return false;
		} else {
			int var1 = 3 * Math.max(Math.min(lavaDropColors.length / 3 - 1, var0), 0);
			System.arraycopy(lavaDropColors, var1, setColor, 0, 3);
			return true;
		}
	}

	public static void setupBlockAccess(IBlockAccess var0, boolean var1) {
		checkUpdate();
		if (var0 == null) {
			BiomeHelper.instance = new BiomeHelper.Stub();
		} else {
			BiomeHelper.instance = new BiomeHelper.New(var0);
		}
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
		if (var0 >= 0 && var0 < fixedColorMaps.length && fogCamera != null && fixedColorMaps[var0].isCustom()) {
			float[] var1 = new float[3];
			int var2 = (int)fogCamera.posX;
			int var3 = (int)fogCamera.posY;
			int var4 = (int)fogCamera.posZ;
			setColor[0] = 0.0F;
			setColor[1] = 0.0F;
			setColor[2] = 0.0F;

			for (int var5 = -fogBlendRadius; var5 <= fogBlendRadius; ++var5) {
				for (int var6 = -fogBlendRadius; var6 <= fogBlendRadius; ++var6) {
					int var7 = colorizeBiome(16777215, var0, var2 + var5, var3, var4 + var6);
					intToFloat3(var7, var1);
					setColor[0] += var1[0];
					setColor[1] += var1[1];
					setColor[2] += var1[2];
				}
			}

			setColor[0] *= fogBlendScale;
			setColor[1] *= fogBlendScale;
			setColor[2] *= fogBlendScale;
			return true;
		} else {
			return false;
		}
	}

	public static boolean computeFogColor(World var0, float var1) {
		if (var0.worldProvider.worldType == 0 && computeFogColor(7)) {
			computeLightningFlash(var0, var1);
			return true;
		} else {
			return false;
		}
	}

	public static boolean computeSkyColor(World var0, float var1) {
		if (var0.worldProvider.worldType == 0 && computeFogColor(8)) {
			computeLightningFlash(var0, var1);
			return true;
		} else {
			return false;
		}
	}

	private static void computeLightningFlash(World var0, float var1) {
		if (var0.lightningFlash > 0) {
			var1 = 0.45F * clamp((float)var0.lightningFlash - var1);
			setColor[0] = setColor[0] * (1.0F - var1) + 0.8F * var1;
			setColor[1] = setColor[1] * (1.0F - var1) + 0.8F * var1;
			setColor[2] = setColor[2] * (1.0F - var1) + 0.8F * var1;
		}
	}

	public static boolean computeMyceliumParticleColor() {
		if (myceliumColors == null) {
			return false;
		} else {
			setColorF(myceliumColors[random.nextInt(myceliumColors.length)]);
			return true;
		}
	}

	public static void setColorF(int var0) {
		intToFloat3(var0, setColor);
	}

	public static void setupBiome(BiomeGenBase var0) {
		biomes.add(var0);
	}

	public static void setupPotion(Potion var0) {
		var0.origColor = var0.liquidColor;
		potions.add(var0);
	}

	public static void setupSpawnerEgg(String var0, int var1, int var2, int var3) {
		entityNamesByID.put(Integer.valueOf(var1), var0);
	}

	public static boolean drawFancyClouds(boolean var0) {
		switch(cloudType) {
		case 1:
			return false;
		case 2:
			return true;
		default:
			return var0;
		}
	}

	private static void checkUpdate() {
		if (lastTexturePack != Minecraft.theMinecraft.texturePackList.selectedTexturePack) {
			lastTexturePack = Minecraft.theMinecraft.texturePackList.selectedTexturePack;
			reset();
			reloadColorProperties();
			if (useFogColors) {
				reloadFogColors();
			}

			if (usePotionColors) {
				reloadPotionColors();
			}

			if (useSwampColors) {
				reloadSwampColors();
			}

			if (useBlockColors) {
				reloadBlockColors();
			}

			if (useParticleColors) {
				reloadParticleColors();
			}

			if (useRedstoneColors) {
				reloadRedstoneColors();
			}

			if (useStemColors) {
				reloadStemColors();
			}

			if (useCloudType) {
				reloadCloudType();
			}

			if (useMapColors) {
				reloadMapColors();
			}

			if (useSheepColors) {
				reloadSheepColors();
			}

			if (useTextColors) {
				reloadTextColors();
			}
		}
	}

	private static void reset() {
		properties = new Properties();
		fixedColorMaps[0] = new ColorMap(useSwampColors, "/misc/swampgrasscolor.png", 5131854);
		fixedColorMaps[1] = new ColorMap(useSwampColors, "/misc/swampfoliagecolor.png", 5131854);
		fixedColorMaps[2] = new ColorMap(useTreeColors, "/misc/pinecolor.png", 6396257);
		fixedColorMaps[3] = new ColorMap(useTreeColors, "/misc/birchcolor.png", 8431445);
		fixedColorMaps[4] = new ColorMap(useTreeColors, "/misc/foliagecolor.png", 4764952);
		fixedColorMaps[4].clear();
		fixedColorMaps[5] = new ColorMap(useWaterColors, "/misc/watercolorX.png", 16777215);
		fixedColorMaps[6] = new ColorMap(useWaterColors, "/misc/underwatercolor.png", 329011);
		fixedColorMaps[7] = new ColorMap(useFogColors, "/misc/fogcolor0.png", 12638463);
		fixedColorMaps[8] = new ColorMap(useFogColors, "/misc/skycolor0.png", 16777215);
		netherFogColor = new float[]{0.2F, 0.03F, 0.03F};
		endFogColor = new float[]{0.075F, 0.075F, 0.094F};
		endSkyColor = 1579032;
		blockColorMaps = new ColorMap[Block.blocksList.length];
		blockMetaColorMaps.clear();
		lilypadColor = 2129968;
		waterBaseColor = new float[]{0.2F, 0.3F, 1.0F};
		waterColor = new float[]{0.2F, 0.3F, 1.0F};
		portalColor = new float[]{1.0F, 0.3F, 0.9F};
		lavaDropColors = null;
		waterBottleColor = 3694022;
		redstoneColor = (float[][])null;
		stemColors = null;
		lightmaps.clear();
		spawnerEggShellColors.clear();
		spawnerEggSpotColors.clear();
		cloudType = 0;

		Potion var1;
		for (Iterator var0 = potions.iterator(); var0.hasNext(); var1.liquidColor = var1.origColor) {
			var1 = (Potion)var0.next();
		}

		MapColor[] var4 = MapColor.mapColorArray;
		int var6 = var4.length;

		for (int var2 = 0; var2 < var6; ++var2) {
			MapColor var3 = var4[var2];
			if (var3 != null) {
				var3.colorValue = var3.origColorValue;
			}
		}

		EntitySheep.fleeceColorTable = (float[][])EntitySheep.origFleeceColorTable.clone();
		myceliumColors = null;
		textColorMap.clear();

		for (int var5 = 0; var5 < textCodeColors.length; ++var5) {
			textCodeColors[var5] = -2;
		}
	}

	private static void reloadColorProperties() {
		InputStream var0 = null;

		try {
			var0 = lastTexturePack.getResourceAsStream("/color.properties");
			if (var0 != null) {
				properties.load(var0);
			}
		} catch (IOException var5) {
			var5.printStackTrace();
		} finally {
			MCPatcherUtils.close((Closeable)var0);
		}
	}

	private static void reloadFogColors() {
		loadFloatColor("fog.nether", netherFogColor);
		loadFloatColor("fog.end", endFogColor);
		endSkyColor = loadIntColor("sky.end", endSkyColor);
	}

	private static void reloadPotionColors() {
		Iterator var0 = potions.iterator();

		while (var0.hasNext()) {
			Potion var1 = (Potion)var0.next();
			loadIntColor(var1.name, var1);
		}

		int[] var2 = new int[]{waterBottleColor};
		loadIntColor("potion.water", var2, 0);
		waterBottleColor = var2[0];
	}

	private static void reloadSwampColors() {
		int[] var0 = new int[]{lilypadColor};
		loadIntColor("lilypad", var0, 0);
		lilypadColor = var0[0];
	}

	private static void reloadBlockColors() {
		Iterator var0 = properties.entrySet().iterator();

		while (var0.hasNext()) {
			Entry var1 = (Entry)var0.next();
			if (var1.getKey() instanceof String && var1.getValue() instanceof String) {
				String var2 = (String)var1.getKey();
				String var3 = (String)var1.getValue();
				if (var2.startsWith("palette.block.")) {
					var2 = var2.substring("palette.block.".length()).trim();
					ColorMap var4 = new ColorMap(true, var2, 16777215);
					if (var4.isCustom()) {
						String[] var5 = var3.split("\\s+");
						int var6 = var5.length;

						for (int var7 = 0; var7 < var6; ++var7) {
							String var8 = var5[var7];
							String[] var9 = var8.split(":");
							int[] var10 = new int[var9.length];

							try {
								for (int var11 = 0; var11 < var9.length; ++var11) {
									var10[var11] = Integer.parseInt(var9[var11]);
								}
							} catch (NumberFormatException var12) {
								continue;
							}

							switch(var10.length) {
							case 1:
								if (var10[0] < 0 || var10[0] >= blockColorMaps.length) {
									continue;
								}

								blockColorMaps[var10[0]] = var4;
								break;
							case 2:
								blockMetaColorMaps.put(Float.valueOf(ColorMap.getBlockMetaKey(var10[0], var10[1])), var4);
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

	private static void reloadParticleColors() {
		loadFloatColor("drop.water", waterBaseColor);
		loadFloatColor("particle.water", waterBaseColor);
		loadFloatColor("particle.portal", portalColor);
		int[] var0 = MCPatcherUtils.getImageRGB(MCPatcherUtils.readImage(lastTexturePack.getResourceAsStream("/misc/lavadropcolor.png")));
		if (var0 != null) {
			lavaDropColors = new float[3 * var0.length];

			for (int var1 = 0; var1 < var0.length; ++var1) {
				intToFloat3(var0[var1], lavaDropColors, 3 * var1);
			}
		}

		myceliumColors = MCPatcherUtils.getImageRGB(MCPatcherUtils.readImage(lastTexturePack.getResourceAsStream("/misc/myceliumparticlecolor.png")));
	}

	private static void reloadRedstoneColors() {
		int[] var0 = MCPatcherUtils.getImageRGB(MCPatcherUtils.readImage(lastTexturePack.getResourceAsStream("/misc/redstonecolor.png")));
		if (var0 != null && var0.length >= 16) {
			redstoneColor = new float[16][];

			for (int var1 = 0; var1 < 16; ++var1) {
				float[] var2 = new float[3];
				intToFloat3(var0[var1], var2);
				redstoneColor[var1] = var2;
			}
		}
	}

	private static void reloadStemColors() {
		int[] var0 = MCPatcherUtils.getImageRGB(MCPatcherUtils.readImage(lastTexturePack.getResourceAsStream("/misc/stemcolor.png")));
		if (var0 != null && var0.length >= 8) {
			stemColors = var0;
		}
	}

	private static void reloadCloudType() {
		String var0 = properties.getProperty("clouds", "").trim().toLowerCase();
		if (var0.equals("fast")) {
			cloudType = 1;
		} else if (var0.equals("fancy")) {
			cloudType = 2;
		}
	}

	private static void reloadMapColors() {
		for (int var0 = 0; var0 < MapColor.mapColorArray.length; ++var0) {
			if (MapColor.mapColorArray[var0] != null) {
				int[] var1 = new int[]{MapColor.mapColorArray[var0].origColorValue};
				loadIntColor("map." + getStringKey(MAP_MATERIALS, var0), var1, 0);
				MapColor.mapColorArray[var0].colorValue = var1[0];
			}
		}
	}

	private static void reloadSheepColors() {
		for (int var0 = 0; var0 < EntitySheep.fleeceColorTable.length; ++var0) {
			loadFloatColor("sheep." + getStringKey(ItemDye.dyeColorNames, EntitySheep.fleeceColorTable.length - 1 - var0), EntitySheep.fleeceColorTable[var0]);
		}
	}

	private static void reloadTextColors() {
		for (int var0 = 0; var0 < textCodeColors.length; ++var0) {
			loadIntColor("text.code." + var0, textCodeColors, var0);
			if (textCodeColors[var0] != -2 && var0 + 16 < textCodeColors.length) {
				textCodeColors[var0 + 16] = (textCodeColors[var0] & 16579836) >> 2;
			}
		}

		Iterator var7 = properties.entrySet().iterator();

		while (var7.hasNext()) {
			Entry var1 = (Entry)var7.next();
			if (var1.getKey() instanceof String && var1.getValue() instanceof String) {
				String var2 = (String)var1.getKey();
				String var3 = (String)var1.getValue();
				if (var2.startsWith("text.") && !var2.startsWith("text.code.")) {
					var2 = var2.substring("text.".length()).trim();

					try {
						int var5;
						if (var2.equals("xpbar")) {
							var5 = 8453920;
						} else if (var2.equals("boss")) {
							var5 = 16711935;
						} else {
							var5 = Integer.parseInt(var2, 16);
						}

						int var4 = Integer.parseInt(var3, 16);
						textColorMap.put(Integer.valueOf(var5), Integer.valueOf(var4));
					} catch (NumberFormatException var6) {
						;
					}
				}
			}
		}
	}

	private static String getStringKey(String[] var0, int var1) {
		return var0 != null && var1 >= 0 && var1 < var0.length && var0[var1] != null?var0[var1]:"" + var1;
	}

	private static void loadIntColor(String var0, Potion var1) {
		String var2 = properties.getProperty(var0, "");
		if (!var2.equals("")) {
			try {
				var1.liquidColor = Integer.parseInt(var2, 16);
			} catch (NumberFormatException var4) {
				;
			}
		}
	}

	private static void loadIntColor(String var0, int[] var1, int var2) {
		String var3 = properties.getProperty(var0, "");
		if (!var3.equals("")) {
			try {
				var1[var2] = Integer.parseInt(var3, 16);
			} catch (NumberFormatException var5) {
				;
			}
		}
	}

	private static int loadIntColor(String var0, int var1) {
		String var2 = properties.getProperty(var0, "");
		if (!var2.equals("")) {
			try {
				var1 = Integer.parseInt(var2, 16);
			} catch (NumberFormatException var4) {
				;
			}
		}

		return var1;
	}

	private static void loadFloatColor(String var0, float[] var1) {
		String var2 = properties.getProperty(var0, "");
		if (!var2.equals("")) {
			try {
				intToFloat3(Integer.parseInt(var2, 16), var1);
			} catch (NumberFormatException var4) {
				;
			}
		}
	}

	private static float[] loadFloatColor(String var0) {
		String var1 = properties.getProperty(var0, "");
		if (!var1.equals("")) {
			try {
				float[] var2 = new float[3];
				intToFloat3(Integer.parseInt(var1, 16), var2);
				return var2;
			} catch (NumberFormatException var3) {
				;
			}
		}

		return null;
	}

	private static void intToFloat3(int var0, float[] var1, int var2) {
		var1[var2] = (float)(var0 & 16711680) / 1.671168E7F;
		var1[var2 + 1] = (float)(var0 & 65280) / 65280.0F;
		var1[var2 + 2] = (float)(var0 & 255) / 255.0F;
	}

	private static void intToFloat3(int var0, float[] var1) {
		intToFloat3(var0, var1, 0);
	}

	private static int float3ToInt(float[] var0, int var1) {
		return (int)(255.0F * var0[var1]) << 16 | (int)(255.0F * var0[var1 + 1]) << 8 | (int)(255.0F * var0[var1 + 2]);
	}

	private static int float3ToInt(float[] var0) {
		return float3ToInt(var0, 0);
	}

	private static float clamp(float var0) {
		return var0 < 0.0F?0.0F:(var0 > 1.0F?1.0F:var0);
	}

	static double clamp(double var0) {
		return var0 < 0.0D?0.0D:(var0 > 1.0D?1.0D:var0);
	}

	private static void clamp(float[] var0) {
		for (int var1 = 0; var1 < var0.length; ++var1) {
			var0[var1] = clamp(var0[var1]);
		}
	}

	private static void interpolate(int[] var0, int var1, float var2, float[] var3, int var4) {
		int var5 = (int)Math.floor((double)var2);
		int var6 = (int)Math.ceil((double)var2);
		if (var5 == var6) {
			intToFloat3(var0[var1 + var5], var3, var4);
		} else {
			float var7 = var2 - (float)var5;
			float var8 = 1.0F - var7;
			float[] var9 = new float[3];
			float[] var10 = new float[3];
			intToFloat3(var0[var1 + var5], var9);
			intToFloat3(var0[var1 + var6], var10);

			for (int var11 = 0; var11 < 3; ++var11) {
				var3[var4 + var11] = var8 * var9[var11] + var7 * var10[var11];
			}
		}
	}

	private static float getBlendScale(int var0) {
		return 1.0F / (float)((2 * var0 + 1) * (2 * var0 + 1));
	}
}
