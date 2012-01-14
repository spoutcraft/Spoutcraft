package com.pclewis.mcpatcher.mod;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityRenderer;
import net.minecraft.src.Potion;
import net.minecraft.src.TexturePackBase;
import net.minecraft.src.World;
import net.minecraft.src.WorldChunkManager;
import org.lwjgl.opengl.GL11;

public class Colorizer {
	private static final String COLOR_PROPERTIES = "/color.properties";
	private static final String LIGHTMAP_FORMAT = "/environment/lightmap%d.png";
	private static final String REDSTONE_COLORS = "/misc/redstonecolor.png";
	private static final String STEM_COLORS = "/misc/stemcolor.png";
	private static final String LAVA_DROP_COLORS = "/misc/lavadropcolor.png";
	public static final int COLOR_MAP_SWAMP_GRASS = 0;
	public static final int COLOR_MAP_SWAMP_FOLIAGE = 1;
	public static final int COLOR_MAP_PINE = 2;
	public static final int COLOR_MAP_BIRCH = 3;
	public static final int COLOR_MAP_FOLIAGE = 4;
	public static final int COLOR_MAP_WATER = 5;
	public static final int COLOR_MAP_UNDERWATER = 6;
	public static final int COLOR_MAP_FOG0 = 7;
	public static final int COLOR_MAP_SKY0 = 8;
	private static final String[] COLOR_MAPS = new String[] {"/misc/swampgrasscolor.png", "/misc/swampfoliagecolor.png", "/misc/pinecolor.png", "/misc/birchcolor.png", "/misc/foliagecolor.png", "/misc/watercolorX.png", "/misc/underwatercolor.png", "/misc/fogcolor0.png", "/misc/skycolor0.png"};
	public static final String PALETTE_BLOCK_KEY = "palette.block.";
	private static Properties properties;
	private static int[][] colorMaps;
	private static int[] colorMapDefault;
	private static int[] lilypadColor;
	private static float[] waterBaseColor;
	private static float[] lavaDropColor;
	private static int[] waterBottleColor;
	private static float[][] redstoneColor;
	private static int[] stemColors;
	private static ArrayList potions = new ArrayList();
	private static TexturePackBase lastTexturePack;
	private static final int LIGHTMAP_SIZE = 16;
	private static final float LIGHTMAP_SCALE = 15.0F;
	private static final int COLORMAP_SIZE = 256;
	private static final float COLORMAP_SCALE = 255.0F;
	private static final boolean useLightmaps = true;
	private static HashMap lightmaps = new HashMap();
	private static final boolean useDropColors = true;
	public static float[] waterColor;
	private static final boolean useEggColors = true;
	private static final HashMap entityNamesByID = new HashMap();
	private static final HashMap spawnerEggShellColors = new HashMap();
	private static final HashMap spawnerEggSpotColors = new HashMap();
	private static final int fogBlendRadius = 7;
	private static final float fogBlendScale = 1.0F / (float)((2 * fogBlendRadius + 1) * (2 * fogBlendRadius + 1));
	private static final int CLOUDS_DEFAULT = 0;
	private static final int CLOUDS_FAST = 1;
	private static final int CLOUDS_FANCY = 2;
	private static int cloudType = 0;
	private static final ArrayList biomes = new ArrayList();
	private static boolean biomesLogged;
	public static float redstoneWireRed;
	public static float redstoneWireGreen;
	public static float redstoneWireBlue;
	public static float lavaDropRed;
	public static float lavaDropGreen;
	public static float lavaDropBlue;
	private static Entity fogCamera;
	private static WorldChunkManager fogChunkManager;
	public static final float[] setColor = new float[3];

	private static boolean colorMapIndexValid(int var0) {
		return var0 >= 0 && var0 < colorMaps.length && colorMaps[var0] != null;
	}

	public static int colorizeBiome(int var0, int var1, double var2, double var4) {
		checkUpdate();
		if (!colorMapIndexValid(var1)) {
			return var0;
		}
		else {
			int var6 = (int)(255.0D * (1.0D - var2));
			int var7 = (int)(255.0D * (1.0D - var4 * var2));
			return colorMaps[var1][256 * var7 + var6];
		}
	}

	public static int colorizeBiome(int var0, int var1) {
		checkUpdate();
		return colorMapDefault[var1];
	}

	public static int colorizeBiome(int var0, int var1, WorldChunkManager var2, int var3, int var4, int var5) {
		checkUpdate();
		return colorizeBiome(var0, var1, (double)var2.getTemperature(var3, var4, var5), (double)var2.getRainfall(var3, var5));
	}

	public static int colorizeWater(WorldChunkManager var0, int var1, int var2) {
		return colorizeBiome(var0.getBiomeGenAt(var1, var2).waterColorMultiplier, 5, var0, var1, 64, var2);
	}

	public static int colorizeStem(int var0, int var1) {
		checkUpdate();
		return stemColors == null ? var0 : stemColors[var1 & 7];
	}

	public static int colorizeBlock(Block var0, WorldChunkManager var1, int var2, int var3, int var4) {
		return colorizeBiome(16777215, COLOR_MAPS.length + var0.blockID, var1, var2, var3, var4);
	}

	public static int colorizeBlock(Block var0) {
		return colorizeBiome(16777215, COLOR_MAPS.length + var0.blockID);
	}

	public static int colorizeSpawnerEgg(int var0, int var1, int var2) {
		if (!useEggColors) {
			return var0;
		}
		else {
			checkUpdate();
			Integer var3 = null;
			HashMap var4 = var2 == 0 ? spawnerEggShellColors : spawnerEggSpotColors;
			if (var4.containsKey(Integer.valueOf(var1))) {
				var3 = (Integer)var4.get(Integer.valueOf(var1));
			}
			else if (entityNamesByID.containsKey(Integer.valueOf(var1))) {
				String var5 = (String)entityNamesByID.get(Integer.valueOf(var1));
				if (var5 != null) {
					int[] var6 = new int[] {var0};
					loadIntColor((var2 == 0 ? "egg.shell." : "egg.spots.") + var5, var6, 0);
					var4.put(Integer.valueOf(var1), Integer.valueOf(var6[0]));
					var3 = Integer.valueOf(var6[0]);
				}
			}

			return var3 == null ? var0 : var3.intValue();
		}
	}

	public static void setColorF(int var0) {
		intToFloat3(var0, setColor);
	}

	public static int getWaterBottleColor() {
		checkUpdate();
		return waterBottleColor[0];
	}

	public static int getLilyPadColor() {
		checkUpdate();
		return lilypadColor[0];
	}

	public static int getItemColorFromDamage(int var0, int var1, int var2) {
		return var1 != 8 && var1 != 9 ? var0 : colorizeBiome(var0, 5);
	}

	public static boolean computeLightmap(EntityRenderer var0, World var1) {
		if (var1 != null && useLightmaps) {
			checkUpdate();
			int var2 = var1.worldProvider.worldType;
			String var3 = String.format("/environment/lightmap%d.png", new Object[] {Integer.valueOf(var2)});
			BufferedImage var4;
			if (lightmaps.containsKey(Integer.valueOf(var2))) {
				var4 = (BufferedImage)lightmaps.get(Integer.valueOf(var2));
			}
			else {
				var4 = readImage(lastTexturePack.getResourceAsStream(var3));
				lightmaps.put(Integer.valueOf(var2), var4);
				if (var4 == null) {
				}
				else {
				}
			}

			if (var4 == null) {
				return false;
			}
			else {
				int var5 = var4.getWidth();
				int var6 = var4.getHeight();
				if (var6 != 32) {
					System.out.printf("ERROR: %s must be exactly %d pixels high\n", new Object[] {var3, Integer.valueOf(32)});
					lightmaps.put(Integer.valueOf(var2), (Object)null);
					return false;
				}
				else {
					int[] var7 = new int[var5 * var6];
					var4.getRGB(0, 0, var5, var6, var7, 0, var5);
					int[] var8 = new int[256];
					float var9 = clamp(var1.lightningFlash > 0 ? 1.0F : 1.1666666F * (var1.func_35464_b(1.0F) - 0.2F)) * (float)(var5 - 1);
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

					Minecraft.theMinecraft.renderEngine.createTextureFromBytes(var8, 16, 16, var0.emptyTexture);
					return true;
				}
			}
		}
		else {
			return false;
		}
	}

	public static boolean computeRedstoneWireColor(int var0) {
		checkUpdate();
		if (redstoneColor == null) {
			return false;
		}
		else {
			float[] var1 = redstoneColor[Math.max(Math.min(var0, 15), 0)];
			redstoneWireRed = var1[0];
			redstoneWireGreen = var1[1];
			redstoneWireBlue = var1[2];
			return true;
		}
	}

	public static boolean computeWaterColor(WorldChunkManager var0, double var1, double var3, double var5) {
		checkUpdate();
		if (!useDropColors) {
			return false;
		}
		else {
			int var7 = colorizeBiome(16777215, 5, var0, (int)var1, (int)var3, (int)var5);
			float[] var8 = new float[3];
			intToFloat3(var7, var8);

			for (int var9 = 0; var9 < 3; ++var9) {
				waterColor[var9] = var8[var9] * waterBaseColor[var9];
			}

			return true;
		}
	}

	public static void computeWaterColor() {
		checkUpdate();
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
		checkUpdate();
		if (lavaDropColor == null) {
			return false;
		}
		else {
			int var1 = 3 * Math.max(Math.min(lavaDropColor.length / 3 - 1, var0), 0);
			lavaDropRed = lavaDropColor[var1];
			lavaDropGreen = lavaDropColor[var1 + 1];
			lavaDropBlue = lavaDropColor[var1 + 2];
			return true;
		}
	}

	public static void setupForFog(WorldChunkManager var0, Entity var1) {
		fogChunkManager = var0;
		fogCamera = var1;
		if (!biomesLogged) {
			biomesLogged = true;
			Iterator var2 = biomes.iterator();

			while (var2.hasNext()) {
				BiomeGenBase var3 = (BiomeGenBase)var2.next();
				int var4 = (int)(255.0D * (1.0D - (double)var3.temperature));
				int var5 = (int)(255.0D * (1.0D - (double)(var3.rainfall * var3.temperature)));
			}
		}
	}

	public static boolean computeFogColor(int var0) {
		checkUpdate();
		if (colorMapIndexValid(var0) && fogChunkManager != null && fogCamera != null) {
			float[] var1 = new float[3];
			int var2 = (int)fogCamera.posX;
			int var3 = (int)fogCamera.posY;
			int var4 = (int)fogCamera.posZ;
			setColor[0] = 0.0F;
			setColor[1] = 0.0F;
			setColor[2] = 0.0F;

			for (int var5 = -fogBlendRadius; var5 <= fogBlendRadius; ++var5) {
				for (int var6 = -fogBlendRadius; var6 <= fogBlendRadius; ++var6) {
					int var7 = colorizeBiome(16777215, var0, fogChunkManager, var2 + var5, var3, var4 + var6);
					intToFloat3(var7, var1);
					setColor[0] += var1[0] * fogBlendScale;
					setColor[1] += var1[1] * fogBlendScale;
					setColor[2] += var1[2] * fogBlendScale;
				}
			}

			return true;
		}
		else {
			return false;
		}
	}

	public static boolean computeSkyColor(World var0) {
		return var0.worldProvider.worldType == 0 && computeFogColor(8);
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
		switch (cloudType) {
			case 1:
				return false;
			case 2:
				return true;
			default:
				return var0;
		}
	}

	public static void checkUpdate() {
		if (lastTexturePack != Minecraft.theMinecraft.texturePackList.selectedTexturePack) {
			lastTexturePack = Minecraft.theMinecraft.texturePackList.selectedTexturePack;
			properties = new Properties();
			colorMaps = new int[COLOR_MAPS.length + Block.blocksList.length][];
			colorMapDefault = new int[colorMaps.length];

			for (int var0 = 0; var0 < colorMapDefault.length; ++var0) {
				colorMapDefault[var0] = 16777215;
			}

			colorMapDefault[0] = 5131854;
			colorMapDefault[1] = 5131854;
			colorMapDefault[2] = 6396257;
			colorMapDefault[3] = 8431445;
			colorMapDefault[4] = 4764952;
			colorMapDefault[5] = 16777215;
			colorMapDefault[6] = 329011;
			colorMapDefault[7] = 12638463;
			colorMapDefault[8] = 16777215;
			lilypadColor = new int[] {2129968};
			waterBaseColor = new float[] {0.2F, 0.3F, 1.0F};
			waterColor = new float[] {0.2F, 0.3F, 1.0F};
			lavaDropColor = null;
			waterBottleColor = new int[] {3694022};
			redstoneColor = (float[][])null;
			stemColors = null;
			lightmaps.clear();
			spawnerEggShellColors.clear();
			spawnerEggSpotColors.clear();
			cloudType = 0;

			Potion var1;
			for (Iterator var18 = potions.iterator(); var18.hasNext(); var1.liquidColor = var1.origColor) {
				var1 = (Potion)var18.next();
			}

			InputStream var19 = null;

			try {
				var19 = lastTexturePack.getResourceAsStream("/color.properties");
				if (var19 != null) {
					properties.load(var19);
				}
			}
			catch (IOException var16) {
				var16.printStackTrace();
			}
			finally {
				if (var19 != null) {
					try {
						var19.close();
					}
					catch (Exception e) { }
				}
			}

			//if (MCPatcherUtils.getBoolean("Custom Colors", "water", true)) {
				loadColorMap(5);
				loadColorMap(6);
			//}

			//if (MCPatcherUtils.getBoolean("Custom Colors", "fog", true)) {
				loadColorMap(7);
				loadColorMap(8);
			//}

			Iterator var20;
			//if (MCPatcherUtils.getBoolean("Custom Colors", "potion", true)) {
				var20 = potions.iterator();

				while (var20.hasNext()) {
					Potion var2 = (Potion)var20.next();
					loadIntColor(var2.name, var2);
				}

				loadIntColor("potion.water", waterBottleColor, 0);
			//}

			//if (MCPatcherUtils.getBoolean("Custom Colors", "swamp", true)) {
				loadColorMap(0);
				loadColorMap(1);
				loadIntColor("lilypad", lilypadColor, 0);
			//}

			//if (MCPatcherUtils.getBoolean("Custom Colors", "tree", true)) {
				loadColorMap(2);
				loadColorMap(3);
				loadColorMap(4);
				colorMaps[4] = null;
			//}

			//if (MCPatcherUtils.getBoolean("Custom Colors", "otherBlocks", true)) {
				var20 = properties.entrySet().iterator();

				while (var20.hasNext()) {
					Entry var22 = (Entry)var20.next();
					if (var22.getKey() instanceof String && var22.getValue() instanceof String) {
						String var3 = (String)var22.getKey();
						String var4 = (String)var22.getValue();
						if (var3.startsWith("palette.block.")) {
							var3 = var3.substring("palette.block.".length()).trim();
							int[] var5 = loadColorMap(var3);
							if (var5 != null) {
								String[] var6 = var4.split("\\s+");
								int var7 = var6.length;

								for (int var8 = 0; var8 < var7; ++var8) {
									String var9 = var6[var8];

									try {
										int var10 = Integer.parseInt(var9);
										if (var10 >= 0 && var10 < colorMaps.length - COLOR_MAPS.length) {
											int var11 = COLOR_MAPS.length + var10;
											colorMaps[var11] = var5;
											colorMapDefault[var11] = colorizeBiome(colorMapDefault[var11], var11, 0.5D, 1.0D);
										}
									}
									catch (NumberFormatException var15) {
										;
									}
								}
							}
						}
					}
				}
		//	}

			int[] var21;
			int var23;
			if (useDropColors) {
				loadFloatColor("drop.water", waterBaseColor);
				var21 = getImageRGB(readImage(lastTexturePack.getResourceAsStream("/misc/lavadropcolor.png")));
				if (var21 != null) {
					lavaDropColor = new float[3 * var21.length];

					for (var23 = 0; var23 < var21.length; ++var23) {
						intToFloat3(var21[var23], lavaDropColor, 3 * var23);
					}
				}
			}

			//if (MCPatcherUtils.getBoolean("Custom Colors", "redstone", true)) {
				var21 = getImageRGB(readImage(lastTexturePack.getResourceAsStream("/misc/redstonecolor.png")));
				if (var21 != null && var21.length >= 16) {
					redstoneColor = new float[16][];

					for (var23 = 0; var23 < 16; ++var23) {
						float[] var25 = new float[3];
						intToFloat3(var21[var23], var25);
						redstoneColor[var23] = var25;
					}
				}
			//}

			//if (MCPatcherUtils.getBoolean("Custom Colors", "stem", true)) {
				var21 = getImageRGB(readImage(lastTexturePack.getResourceAsStream("/misc/stemcolor.png")));
				if (var21 != null && var21.length >= 8) {
					stemColors = var21;
				}
			//}

			//if (MCPatcherUtils.getBoolean("Custom Colors", "clouds", true)) {
				String var24 = properties.getProperty("clouds", "").trim().toLowerCase();
				if (var24.equals("fast")) {
					cloudType = 1;
				}
				else if (var24.equals("fancy")) {
					cloudType = 2;
				}
			//}
		}
	}

	private static void loadIntColor(String var0, Potion var1) {
		String var2 = properties.getProperty(var0, "");
		if (!var2.equals("")) {
			try {
				var1.liquidColor = Integer.parseInt(var2, 16);
			}
			catch (NumberFormatException var4) {
				;
			}
		}
	}

	private static void loadIntColor(String var0, int[] var1, int var2) {
		String var3 = properties.getProperty(var0, "");
		if (!var3.equals("")) {
			try {
				var1[var2] = Integer.parseInt(var3, 16);
			}
			catch (NumberFormatException var5) {
				;
			}
		}
	}

	private static void loadFloatColor(String var0, float[] var1) {
		String var2 = properties.getProperty(var0, "");
		if (!var2.equals("")) {
			try {
				intToFloat3(Integer.parseInt(var2, 16), var1);
			}
			catch (NumberFormatException var4) {
				;
			}
		}
	}

	private static int[] loadColorMap(String var0) {
		int[] var1 = getImageRGB(readImage(lastTexturePack.getResourceAsStream(var0)));
		if (var1 == null) {
			return null;
		}
		else if (var1.length != 65536) {
			System.out.printf("ERROR: %s must be %dx%d\n", new Object[] {var0, Integer.valueOf(256), Integer.valueOf(256)});
			return null;
		}
		else {
			return var1;
		}
	}

	private static void loadColorMap(int var0) {
		int[] var1 = loadColorMap(COLOR_MAPS[var0]);
		if (var1 != null) {
			colorMaps[var0] = var1;
			colorMapDefault[var0] = colorizeBiome(colorMapDefault[var0], var0, 0.5D, 1.0D);
		}
	}

	private static void intToFloat3(int var0, float[] var1, int var2) {
		var1[var2] = (float)(var0 & 16711680) / 1.671168E7F;
		var1[var2 + 1] = (float)(var0 & '\uff00') / 65280.0F;
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
		return var0 < 0.0F ? 0.0F : (var0 > 1.0F ? 1.0F : var0);
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
		}
		else {
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
	
	public static int[] getImageRGB(BufferedImage var0) {
		if (var0 == null) {
			return null;
		}
		else {
			int var1 = var0.getWidth();
			int var2 = var0.getHeight();
			int[] var3 = new int[var1 * var2];
			var0.getRGB(0, 0, var1, var2, var3, 0, var1);
			return var3;
		}
	}
	
	public static BufferedImage readImage(InputStream var0) {
		BufferedImage var1 = null;
		if (var0 != null) {
			try {
				var1 = ImageIO.read(var0);
			}
			catch (IOException var6) {
				var6.printStackTrace();
			}
			finally {
				if (var0 != null) {
					try {
						var0.close();
					}
					catch (Exception e) { }
				}
			}
		}

		return var1;
	}
}
