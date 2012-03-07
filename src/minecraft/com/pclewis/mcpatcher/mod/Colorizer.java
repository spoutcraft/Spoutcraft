package com.pclewis.mcpatcher.mod;

import com.pclewis.mcpatcher.MCPatcherUtils;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

public class Colorizer {
	private static final String COLOR_PROPERTIES = "/color.properties";
	private static final String LIGHTMAP_FORMAT = "/environment/lightmap%d.png";
	private static final String REDSTONE_COLORS = "/misc/redstonecolor.png";
	private static final String STEM_COLORS = "/misc/stemcolor.png";
	private static final String LAVA_DROP_COLORS = "/misc/lavadropcolor.png";
	private static final String MYCELIUM_COLORS = "/misc/myceliumparticlecolor.png";

	private static final String PALETTE_BLOCK_KEY = "palette.block.";
	private static final String TEXT_KEY = "text.";
	private static final String TEXT_CODE_KEY = TEXT_KEY + "code.";

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

	private static final String[] MAP_MATERIALS = new String[]{
		"air",
		"grass",
		"sand",
		"cloth",
		"tnt",
		"ice",
		"iron",
		"foliage",
		"snow",
		"clay",
		"dirt",
		"stone",
		"water",
		"wood",
	};

	private static Properties properties;
	private static final ColorMap[] fixedColorMaps = new ColorMap[NUM_FIXED_COLOR_MAPS]; // bitmaps from FIXED_COLOR_MAPS
	private static ColorMap[] blockColorMaps; // bitmaps from palette.block.*
	private static final HashMap<Float, ColorMap> blockMetaColorMaps = new HashMap<Float, ColorMap>(); // bitmaps from palette.block.*
	private static int lilypadColor; // lilypad
	private static float[] waterBaseColor; // particle.water
	private static float[] lavaDropColors; // /misc/lavadropcolor.png
	private static int waterBottleColor; // potion.water
	private static float[][] redstoneColor; // /misc/redstonecolor.png
	private static int[] stemColors; // /misc/stemcolor.png
	private static ArrayList<Potion> potions = new ArrayList<Potion>(); // potion.*
	private static final Random random = new Random(); 

	private static final boolean useWaterColors = MCPatcherUtils.getBoolean(MCPatcherUtils.CUSTOM_COLORS, "water", true);
	private static final boolean useSwampColors = MCPatcherUtils.getBoolean(MCPatcherUtils.CUSTOM_COLORS, "swamp", true);
	private static final boolean useTreeColors = MCPatcherUtils.getBoolean(MCPatcherUtils.CUSTOM_COLORS, "tree", true);
	private static final boolean usePotionColors = MCPatcherUtils.getBoolean(MCPatcherUtils.CUSTOM_COLORS, "potion", true);
	private static final boolean useParticleColors = MCPatcherUtils.getBoolean(MCPatcherUtils.CUSTOM_COLORS, "particle", true);
	private static final boolean useLightmaps = MCPatcherUtils.getBoolean(MCPatcherUtils.CUSTOM_COLORS, "lightmaps", true);
	private static final boolean useFogColors = MCPatcherUtils.getBoolean(MCPatcherUtils.CUSTOM_COLORS, "fog", true);
	private static final boolean useCloudType = MCPatcherUtils.getBoolean(MCPatcherUtils.CUSTOM_COLORS, "clouds", true);
	private static final boolean useRedstoneColors = MCPatcherUtils.getBoolean(MCPatcherUtils.CUSTOM_COLORS, "redstone", true);
	private static final boolean useStemColors = MCPatcherUtils.getBoolean(MCPatcherUtils.CUSTOM_COLORS, "stem", true);
	private static final boolean useEggColors = MCPatcherUtils.getBoolean(MCPatcherUtils.CUSTOM_COLORS, "egg", true);
	private static final boolean useMapColors = MCPatcherUtils.getBoolean(MCPatcherUtils.CUSTOM_COLORS, "map", true);
	private static final boolean useSheepColors = MCPatcherUtils.getBoolean(MCPatcherUtils.CUSTOM_COLORS, "sheep", true);
	private static final boolean useBlockColors = MCPatcherUtils.getBoolean(MCPatcherUtils.CUSTOM_COLORS, "otherBlocks", true);
	private static final boolean useTextColors = MCPatcherUtils.getBoolean(MCPatcherUtils.CUSTOM_COLORS, "text", true);
	private static final int fogBlendRadius = MCPatcherUtils.getInt(MCPatcherUtils.CUSTOM_COLORS, "fogBlendRadius", 7);
	private static final float fogBlendScale = getBlendScale(fogBlendRadius);
	private static final int blockBlendRadius = MCPatcherUtils.getInt(MCPatcherUtils.CUSTOM_COLORS, "blockBlendRadius", 1);
	private static final float blockBlendScale = getBlendScale(blockBlendRadius);

	static TexturePackBase lastTexturePack;

	private static final int LIGHTMAP_SIZE = 16;
	private static final float LIGHTMAP_SCALE = LIGHTMAP_SIZE - 1;
	private static HashMap<Integer, BufferedImage> lightmaps = new HashMap<Integer, BufferedImage>();

	public static final float[] setColor = new float[3];
	public static float[] waterColor;
	public static float[] portalColor = new float[]{1.0f, 0.3f, 0.9f};

	private static final HashMap<Integer, String> entityNamesByID = new HashMap<Integer, String>();
	private static final HashMap<Integer, Integer> spawnerEggShellColors = new HashMap<Integer, Integer>(); // egg.shell.*
	private static final HashMap<Integer, Integer> spawnerEggSpotColors = new HashMap<Integer, Integer>(); // egg.spots.*

	private static final int CLOUDS_DEFAULT = 0;
	private static final int CLOUDS_FAST = 1;
	private static final int CLOUDS_FANCY = 2;
	private static int cloudType = CLOUDS_DEFAULT;

	private static final ArrayList<BiomeGenBase> biomes = new ArrayList<BiomeGenBase>();
	private static boolean biomesLogged;

	private static Entity fogCamera;
	
	public static float[] netherFogColor;
	public static float[] endFogColor;
	public static int endSkyColor;
	
	private static int[] myceliumColors;

	private static final HashMap<Integer, Integer> textColorMap = new HashMap<Integer, Integer>();
	private static final int[] textCodeColors = new int[32];

	public static int colorizeBiome(int defaultColor, int index, double temperature, double rainfall) {
		return fixedColorMaps[index].colorize(defaultColor, temperature, rainfall);
	}

	public static int colorizeBiome(int defaultColor, int index) {
		return fixedColorMaps[index].colorize(defaultColor);
	}

	public static int colorizeBiome(int defaultColor, int index, int i, int j, int k) {
		return fixedColorMaps[index].colorize(defaultColor, i, j, k);
	}

	public static int colorizeWater(Object dummy, int i, int k) {
		return fixedColorMaps[COLOR_MAP_WATER].colorize(BiomeHelper.instance.getWaterColorMultiplier(i, 64, k), i, 64, k);
	}

	public static int colorizeBlock(Block block, int i, int j, int k, int metadata) {
		ColorMap colorMap = null;
		if (!blockMetaColorMaps.isEmpty()) {
			colorMap = blockMetaColorMaps.get(ColorMap.getBlockMetaKey(block.blockID, metadata));
		}
		if (colorMap == null && block.blockID >= 0 && block.blockID < blockColorMaps.length) {
			colorMap = blockColorMaps[block.blockID];
		}
		if (colorMap == null || !colorMap.isCustom()) {
			return 0xffffff;
		} else if (!BiomeHelper.instance.useBlockBlending() || blockBlendRadius == 0) {
			return colorMap.colorize(0xffffff, i, j, k);
		} else {
			float[] sum = new float[3];
			float[] sample = new float[3];
			for (int di = -blockBlendRadius; di <= blockBlendRadius; di++) {
				for (int dk = -blockBlendRadius; dk <= blockBlendRadius; dk++) {
					int rgb = colorMap.colorize(0xffffff, i + di, j, k + dk);
					intToFloat3(rgb, sample);
					sum[0] += sample[0];
					sum[1] += sample[1];
					sum[2] += sample[2];
				}
			}
			sum[0] *= blockBlendScale;
			sum[1] *= blockBlendScale;
			sum[2] *= blockBlendScale;
			return float3ToInt(sum);
		}
	}

	public static int colorizeBlock(Block block) {
		ColorMap colorMap = blockColorMaps[block.blockID];
		if (colorMap == null) {
			return 0xffffff;
		} else {
			return colorMap.colorize(0xffffff);
		}
	}

	public static int colorizeStem(int defaultColor, int blockMetadata) {
		if (stemColors == null) {
			return defaultColor;
		} else {
			return stemColors[blockMetadata & 0x7];
		}
	}

	public static int colorizeSpawnerEgg(int defaultColor, int entityID, int spots) {
		if (!useEggColors) {
			return defaultColor;
		}
		Integer value = null;
		HashMap<Integer, Integer> eggMap = (spots == 0 ? spawnerEggShellColors : spawnerEggSpotColors);
		if (eggMap.containsKey(entityID)) {
			value = eggMap.get(entityID);
		} else if (entityNamesByID.containsKey(entityID)) {
			String name = entityNamesByID.get(entityID);
			if (name != null) {
				int[] tmp = new int[]{defaultColor};
				loadIntColor((spots == 0 ? "egg.shell." : "egg.spots.") + name, tmp, 0);
				eggMap.put(entityID, tmp[0]);
				value = tmp[0];
			}
		}
		return value == null ? defaultColor : value;
	}
	
	public static int colorizeText(int defaultColor) {
		int high = defaultColor & 0xff000000;
		defaultColor &= 0xffffff;
		Integer newColor = textColorMap.get(defaultColor);
		if (newColor == null) {
			return high | defaultColor;
		} else {
			return high | newColor;
		}
	}
	
	public static int colorizeText(int defaultColor, int index) {
		if (index < 0 || index >= textCodeColors.length || textCodeColors[index] == COLOR_CODE_UNSET) {
			return defaultColor;
		} else {
			return (defaultColor & 0xff000000) | textCodeColors[index];
		}
	}

	public static int getWaterBottleColor() {
		return waterBottleColor;
	}

	public static int getLilyPadColor() {
		return lilypadColor;
	}

	public static int getItemColorFromDamage(int defaultColor, int blockID, int damage) {
		if (blockID == 8 || blockID == 9) {
			return colorizeBiome(defaultColor, COLOR_MAP_WATER);
		} else {
			return defaultColor;
		}
	}

	public static boolean computeLightmap(EntityRenderer renderer, World world) {
		if (world == null || !useLightmaps) {
			return false;
		}
		int worldType = world.worldProvider.worldType;
		String name = String.format(LIGHTMAP_FORMAT, worldType);
		BufferedImage image;
		if (lightmaps.containsKey(worldType)) {
			image = lightmaps.get(worldType);
		} else {
			image = MCPatcherUtils.readImage(lastTexturePack.getInputStream(name));
			lightmaps.put(worldType, image);
			if (image == null) {
				MCPatcherUtils.log("using default lighting for world %d", worldType);
			} else {
				MCPatcherUtils.log("using %s", name);
			}
		}
		if (image == null) {
			return false;
		}
		int width = image.getWidth();
		int height = image.getHeight();
		if (height != 2 * LIGHTMAP_SIZE) {
			MCPatcherUtils.error("%s must be exactly %d pixels high", name, 2 * LIGHTMAP_SIZE);
			lightmaps.put(worldType, null);
			return false;
		}
		int[] origMap = new int[width * height];
		image.getRGB(0, 0, width, height, origMap, 0, width);
		int[] newMap = new int[LIGHTMAP_SIZE * LIGHTMAP_SIZE];
		float sun = clamp(world.lightningFlash > 0 ? 1.0f : 7.0f / 6.0f * (world.getSunAngle(1.0f) - 0.2f)) * (width - 1);
		float torch = clamp(renderer.torchFlickerX + 0.5f) * (width - 1);
		float gamma = clamp(MCPatcherUtils.getMinecraft().gameSettings.gammaSetting);
		float[] sunrgb = new float[3 * LIGHTMAP_SIZE];
		float[] torchrgb = new float[3 * LIGHTMAP_SIZE];
		float[] rgb = new float[3];
		for (int i = 0; i < LIGHTMAP_SIZE; i++) {
			interpolate(origMap, i * width, sun, sunrgb, 3 * i);
			interpolate(origMap, (i + LIGHTMAP_SIZE) * width, torch, torchrgb, 3 * i);
		}
		for (int s = 0; s < LIGHTMAP_SIZE; s++) {
			for (int t = 0; t < LIGHTMAP_SIZE; t++) {
				for (int k = 0; k < 3; k++) {
					rgb[k] = clamp(sunrgb[3 * s + k] + torchrgb[3 * t + k]);
				}
				if (gamma != 0.0f) {
					for (int k = 0; k < 3; k++) {
						float tmp = 1.0f - rgb[k];
						tmp = 1.0f - tmp * tmp * tmp * tmp;
						rgb[k] = gamma * tmp + (1.0f - gamma) * rgb[k];
					}
				}
				newMap[s * LIGHTMAP_SIZE + t] = 0xff000000 | float3ToInt(rgb);
			}
		}
		MCPatcherUtils.getMinecraft().renderEngine.createTextureFromBytes(newMap, LIGHTMAP_SIZE, LIGHTMAP_SIZE, renderer.lightmapTexture);
		return true;
	}

	public static boolean computeRedstoneWireColor(int current) {
		if (redstoneColor == null) {
			return false;
		} else {
			System.arraycopy(redstoneColor[Math.max(Math.min(current, 15), 0)], 0, setColor, 0, 3);
			return true;
		}
	}

	public static boolean computeWaterColor(double x, double y, double z) {
		if (useParticleColors) {
			int rgb = colorizeBiome(0xffffff, COLOR_MAP_WATER, (int) x, (int) y, (int) z);
			float[] multiplier = new float[3];
			intToFloat3(rgb, multiplier);
			for (int i = 0; i < 3; i++) {
				waterColor[i] = multiplier[i] * waterBaseColor[i];
			}
			return true;
		} else {
			return false;
		}
	}

	public static void computeWaterColor() {
		int rgb = colorizeBiome(0xffffff, COLOR_MAP_WATER);
		intToFloat3(rgb, waterColor);
	}

	public static void colorizeWaterBlockGL(int blockID) {
		if (blockID == 8 || blockID == 9) {
			computeWaterColor();
			GL11.glColor4f(waterColor[0], waterColor[1], waterColor[2], 1.0f);
		}
	}

	public static boolean computeLavaDropColor(int age) {
		if (lavaDropColors == null) {
			return false;
		} else {
			int offset = 3 * Math.max(Math.min(lavaDropColors.length / 3 - 1, age), 0);
			System.arraycopy(lavaDropColors, offset, setColor, 0, 3);
			return true;
		}
	}

	public static void setupBlockAccess(IBlockAccess blockAccess, boolean newBiomes) {
		checkUpdate();
		if (blockAccess == null) {
			BiomeHelper.instance = new BiomeHelper.Stub();
		} else if (newBiomes) {
			BiomeHelper.instance = new BiomeHelper.New(blockAccess);
		} else {
			BiomeHelper.instance = new BiomeHelper.Old(blockAccess);
		}
	}

	public static void setupForFog(Entity entity) {
		fogCamera = entity;
		if (!biomesLogged) {
			biomesLogged = true;
			for (BiomeGenBase biome : biomes) {
				int x = ColorMap.getX(biome.temperature, biome.rainfall);
				int y = ColorMap.getY(biome.temperature, biome.rainfall);
				MCPatcherUtils.log("setupBiome #%d \"%s\" %06x (%d,%d)", biome.biomeID, biome.biomeName, biome.waterColorMultiplier, x, y);
			}
		}
	}

	public static boolean computeFogColor(int index) {
		if (index < 0 || index >= fixedColorMaps.length || fogCamera == null || !fixedColorMaps[index].isCustom()) {
			return false;
		}
		float[] f = new float[3];
		int x = (int) fogCamera.posX;
		int y = (int) fogCamera.posY;
		int z = (int) fogCamera.posZ;
		setColor[0] = 0.0f;
		setColor[1] = 0.0f;
		setColor[2] = 0.0f;
		for (int i = -fogBlendRadius; i <= fogBlendRadius; i++) {
			for (int j = -fogBlendRadius; j <= fogBlendRadius; j++) {
				int rgb = colorizeBiome(0xffffff, index, x + i, y, z + j);
				intToFloat3(rgb, f);
				setColor[0] += f[0];
				setColor[1] += f[1];
				setColor[2] += f[2];
			}
		}
		setColor[0] *= fogBlendScale;
		setColor[1] *= fogBlendScale;
		setColor[2] *= fogBlendScale;
		return true;
	}
	
	public static boolean computeFogColor(World world, float f) {
		if (world.worldProvider.worldType == 0 && computeFogColor(COLOR_MAP_FOG0)) {
			computeLightningFlash(world, f);
			return true;
		} else {
			return false;
		}
	}

	public static boolean computeSkyColor(World world, float f) {
		if (world.worldProvider.worldType == 0 && computeFogColor(COLOR_MAP_SKY0)) {
			computeLightningFlash(world, f);
			return true;
		} else {
			return false;
		}
	}
	
	private static void computeLightningFlash(World world, float f) {
		if (world.lightningFlash > 0)
		{
			f = 0.45f * clamp(world.lightningFlash - f);
			setColor[0] = setColor[0] * (1.0f - f) + 0.8f * f;
			setColor[1] = setColor[1] * (1.0f - f) + 0.8f * f;
			setColor[2] = setColor[2] * (1.0f - f) + 0.8f * f;
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

	public static void setColorF(int color) {
		intToFloat3(color, setColor);
	}

	public static void setupBiome(BiomeGenBase biome) {
		biomes.add(biome);
	}

	public static void setupPotion(Potion potion) {
		//System.out.printf("potion.%s=%06x\n", potion.name, potion.color);
		MCPatcherUtils.log("setupPotion #%d \"%s\" %06x", potion.id, potion.name, potion.color);
		potion.origColor = potion.color;
		potions.add(potion);
	}

	public static void setupSpawnerEgg(String entityName, int entityID, int defaultShellColor, int defaultSpotColor) {
		//System.out.printf("egg.shell.%s=%06x\n", entityName, defaultShellColor);
		//System.out.printf("egg.spots.%s=%06x\n", entityName, defaultSpotColor);
		MCPatcherUtils.log("setupSpawnerEgg #%d \"%s\" %06x %06x", entityID, entityName, defaultShellColor, defaultSpotColor);
		entityNamesByID.put(entityID, entityName);
	}

	public static boolean drawFancyClouds(boolean fancyGraphics) {
		switch (cloudType) {
			case CLOUDS_FAST:
				return false;

			case CLOUDS_FANCY:
				return true;

			default:
				return fancyGraphics;
		}
	}

	private static void checkUpdate() {
		if (lastTexturePack == MCPatcherUtils.getMinecraft().texturePackList.selectedTexturePack) {
			return;
		}
		lastTexturePack = MCPatcherUtils.getMinecraft().texturePackList.selectedTexturePack;

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

	private static void reset() {
		properties = new Properties();

		fixedColorMaps[COLOR_MAP_SWAMP_GRASS] = new ColorMap(useSwampColors, "/misc/swampgrasscolor.png", 0x4e4e4e);
		fixedColorMaps[COLOR_MAP_SWAMP_FOLIAGE] = new ColorMap(useSwampColors, "/misc/swampfoliagecolor.png", 0x4e4e4e);
		fixedColorMaps[COLOR_MAP_PINE] = new ColorMap(useTreeColors, "/misc/pinecolor.png", 0x619961);
		fixedColorMaps[COLOR_MAP_BIRCH] = new ColorMap(useTreeColors, "/misc/birchcolor.png", 0x80a755);
		fixedColorMaps[COLOR_MAP_FOLIAGE] = new ColorMap(useTreeColors, "/misc/foliagecolor.png", 0x48b518);
		fixedColorMaps[COLOR_MAP_FOLIAGE].clear();
		fixedColorMaps[COLOR_MAP_WATER] = new ColorMap(useWaterColors, "/misc/watercolorX.png", 0xffffff);
		fixedColorMaps[COLOR_MAP_UNDERWATER] = new ColorMap(useWaterColors, "/misc/underwatercolor.png", 0x050533);
		fixedColorMaps[COLOR_MAP_FOG0] = new ColorMap(useFogColors, "/misc/fogcolor0.png", 0xc0d8ff);
		fixedColorMaps[COLOR_MAP_SKY0] = new ColorMap(useFogColors, "/misc/skycolor0.png", 0xffffff);
		
		netherFogColor = new float[]{0.2f, 0.03f, 0.03f};
		endFogColor = new float[]{0.075f, 0.075f, 0.094f};
		endSkyColor = 0x181818;

		blockColorMaps = new ColorMap[Block.blocksList.length];
		blockMetaColorMaps.clear();

		lilypadColor = 0x208030;
		waterBaseColor = new float[]{0.2f, 0.3f, 1.0f};
		waterColor = new float[]{0.2f, 0.3f, 1.0f};
		portalColor = new float[]{1.0f, 0.3f, 0.9f};
		lavaDropColors = null;
		waterBottleColor = 0x385dc6;
		redstoneColor = null;
		stemColors = null;
		lightmaps.clear();
		spawnerEggShellColors.clear();
		spawnerEggSpotColors.clear();
		cloudType = CLOUDS_DEFAULT;
		for (Potion potion : potions) {
			potion.color = potion.origColor;
		}
		for (MapColor mapColor : MapColor.mapColorArray) {
			if (mapColor != null) {
				mapColor.colorValue = mapColor.origColorValue;
			}
		}
		EntitySheep.fleeceColorTable = EntitySheep.origFleeceColorTable.clone();
		myceliumColors = null;
		textColorMap.clear();
		for (int i = 0; i < textCodeColors.length; i++) {
			textCodeColors[i] = COLOR_CODE_UNSET;
		}
	}

	private static void reloadColorProperties() {
		InputStream inputStream = null;
		try {
			inputStream = lastTexturePack.getInputStream(COLOR_PROPERTIES);
			if (inputStream != null) {
				MCPatcherUtils.log("reloading %s", COLOR_PROPERTIES);
				properties.load(inputStream);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			MCPatcherUtils.close(inputStream);
		}
	}
	
	private static void reloadFogColors() {
		loadFloatColor("fog.nether", netherFogColor);
		loadFloatColor("fog.end", endFogColor);
		endSkyColor = loadIntColor("sky.end", endSkyColor);
	}

	private static void reloadPotionColors() {
		for (Potion potion : potions) {
			loadIntColor(potion.name, potion);
		}
		int[] temp = new int[]{waterBottleColor};
		loadIntColor("potion.water", temp, 0);
		waterBottleColor = temp[0];
	}

	private static void reloadSwampColors() {
		int[] temp = new int[]{lilypadColor};
		loadIntColor("lilypad", temp, 0);
		lilypadColor = temp[0];
	}

	private static void reloadBlockColors() {
		for (Map.Entry<Object, Object> entry : properties.entrySet()) {
			if (!(entry.getKey() instanceof String) || !(entry.getValue() instanceof String)) {
				continue;
			}
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			if (!key.startsWith(PALETTE_BLOCK_KEY)) {
				continue;
			}
			key = key.substring(PALETTE_BLOCK_KEY.length()).trim();
			ColorMap colorMap = new ColorMap(true, key, 0xffffff);
			if (!colorMap.isCustom()) {
				continue;
			}
			for (String idString : value.split("\\s+")) {
				String[] tokens = idString.split(":");
				int[] tokensInt = new int[tokens.length];
				try {
					for (int i = 0; i < tokens.length; i++) {
						tokensInt[i] = Integer.parseInt(tokens[i]);
					}
				} catch (NumberFormatException e) {
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
						blockMetaColorMaps.put(ColorMap.getBlockMetaKey(tokensInt[0], tokensInt[1]), colorMap);
						break;

					default:
						continue;
				}
				MCPatcherUtils.log("using %s for block %s, default color %06x", key, idString, colorMap.colorize());
			}
		}
	}

	private static void reloadParticleColors() {
		loadFloatColor("drop.water", waterBaseColor);
		loadFloatColor("particle.water", waterBaseColor);
		loadFloatColor("particle.portal", portalColor);
		int[] rgb = MCPatcherUtils.getImageRGB(MCPatcherUtils.readImage(lastTexturePack.getInputStream(LAVA_DROP_COLORS)));
		if (rgb != null) {
			lavaDropColors = new float[3 * rgb.length];
			for (int i = 0; i < rgb.length; i++) {
				intToFloat3(rgb[i], lavaDropColors, 3 * i);
			}
		}
		myceliumColors = MCPatcherUtils.getImageRGB(MCPatcherUtils.readImage(lastTexturePack.getInputStream(MYCELIUM_COLORS)));
	}

	private static void reloadRedstoneColors() {
		int[] rgb = MCPatcherUtils.getImageRGB(MCPatcherUtils.readImage(lastTexturePack.getInputStream(REDSTONE_COLORS)));
		if (rgb != null && rgb.length >= 16) {
			redstoneColor = new float[16][];
			for (int i = 0; i < 16; i++) {
				float[] f = new float[3];
				intToFloat3(rgb[i], f);
				redstoneColor[i] = f;
			}
		}
	}

	private static void reloadStemColors() {
		int[] rgb = MCPatcherUtils.getImageRGB(MCPatcherUtils.readImage(lastTexturePack.getInputStream(STEM_COLORS)));
		if (rgb != null && rgb.length >= 8) {
			stemColors = rgb;
		}
	}

	private static void reloadCloudType() {
		String value = properties.getProperty("clouds", "").trim().toLowerCase();
		if (value.equals("fast")) {
			cloudType = CLOUDS_FAST;
		} else if (value.equals("fancy")) {
			cloudType = CLOUDS_FANCY;
		}
	}

	private static void reloadMapColors() {
		for (int i = 0; i < MapColor.mapColorArray.length; i++) {
			if (MapColor.mapColorArray[i] != null) {
				int[] rgb = new int[]{MapColor.mapColorArray[i].origColorValue};
				loadIntColor("map." + getStringKey(MAP_MATERIALS, i), rgb, 0);
				MapColor.mapColorArray[i].colorValue = rgb[0];
			}
		}
	}

	private static void reloadSheepColors() {
		for (int i = 0; i < EntitySheep.fleeceColorTable.length; i++) {
			loadFloatColor("sheep." + getStringKey(ItemDye.dyeColorNames, EntitySheep.fleeceColorTable.length - 1 - i), EntitySheep.fleeceColorTable[i]);
		}
	}
	
	private static void reloadTextColors() {
		for (int i = 0; i < textCodeColors.length; i++) {
			loadIntColor(TEXT_CODE_KEY + i, textCodeColors, i);
			if (textCodeColors[i] != COLOR_CODE_UNSET && i + 16 < textCodeColors.length) {
				textCodeColors[i + 16] = (textCodeColors[i] & 0xfcfcfc) >> 2;
			}
		}
		for (Map.Entry<Object, Object> entry : properties.entrySet()) {
			if (!(entry.getKey() instanceof String) || !(entry.getValue() instanceof String)) {
				continue;
			}
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			if (!key.startsWith(TEXT_KEY) || key.startsWith(TEXT_CODE_KEY)) {
				continue;
			}
			key = key.substring(TEXT_KEY.length()).trim();
			try {
				int newColor;
				int oldColor;
				if (key.equals("xpbar")) {
					oldColor = 0x80ff20;
				} else if (key.equals("boss")) {
					oldColor = 0xff00ff;
				} else {
					oldColor = Integer.parseInt(key, 16);
				}
				newColor = Integer.parseInt(value, 16);
				textColorMap.put(oldColor, newColor);
			} catch (NumberFormatException e) {
			}
		}
	}

	private static String getStringKey(String[] keys, int index) {
		if (keys != null && index >= 0 && index < keys.length && keys[index] != null) {
			return keys[index];
		} else {
			return "" + index;
		}
	}

	private static void loadIntColor(String key, Potion potion) {
		//System.out.printf("%s=%06x\n", key, potion.color);
		String value = properties.getProperty(key, "");
		if (!value.equals("")) {
			try {
				potion.color = Integer.parseInt(value, 16);
			} catch (NumberFormatException e) {
			}
		}
	}

	private static void loadIntColor(String key, int[] color, int index) {
		//System.out.printf("%s=%06x\n", key, color[index]);
		String value = properties.getProperty(key, "");
		if (!value.equals("")) {
			try {
				color[index] = Integer.parseInt(value, 16);
			} catch (NumberFormatException e) {
			}
		}
	}
	
	private static int loadIntColor(String key, int color) {
		//System.out.printf("%s=%06x\n", key, color);
		String value = properties.getProperty(key, "");
		if (!value.equals("")) {
			try {
				color = Integer.parseInt(value, 16);
			} catch (NumberFormatException e) {
			}
		}
		return color;
	}

	private static void loadFloatColor(String key, float[] color) {
		//System.out.printf("%s=%06x\n", key, float3ToInt(color));
		String value = properties.getProperty(key, "");
		if (!value.equals("")) {
			try {
				intToFloat3(Integer.parseInt(value, 16), color);
			} catch (NumberFormatException e) {
			}
		}
	}
	
	private static float[] loadFloatColor(String key) {
		String value = properties.getProperty(key, "");
		if (!value.equals("")) {
			try {
				float[] color = new float[3];
				intToFloat3(Integer.parseInt(value, 16), color);
				return color;
			} catch (NumberFormatException e) {
			}
		}
		return null;
	}

	private static void intToFloat3(int rgb, float[] f, int offset) {
		f[offset] = (float) (rgb & 0xff0000) / (float) 0xff0000;
		f[offset + 1] = (float) (rgb & 0xff00) / (float) 0xff00;
		f[offset + 2] = (float) (rgb & 0xff) / (float) 0xff;
	}

	private static void intToFloat3(int rgb, float[] f) {
		intToFloat3(rgb, f, 0);
	}

	private static int float3ToInt(float[] f, int offset) {
		return ((int) (255.0f * f[offset])) << 16 | ((int) (255.0f * f[offset + 1])) << 8 | (int) (255.0f * f[offset + 2]);
	}

	private static int float3ToInt(float[] f) {
		return float3ToInt(f, 0);
	}

	private static float clamp(float f) {
		if (f < 0.0f) {
			return 0.0f;
		} else if (f > 1.0f) {
			return 1.0f;
		} else {
			return f;
		}
	}

	static double clamp(double d) {
		if (d < 0.0) {
			return 0.0;
		} else if (d > 1.0) {
			return 1.0;
		} else {
			return d;
		}
	}

	private static void clamp(float[] f) {
		for (int i = 0; i < f.length; i++) {
			f[i] = clamp(f[i]);
		}
	}

	private static void interpolate(int[] map, int offset1, float x, float[] rgb, int offset2) {
		int x0 = (int) Math.floor(x);
		int x1 = (int) Math.ceil(x);
		if (x0 == x1) {
			intToFloat3(map[offset1 + x0], rgb, offset2);
		} else {
			float xf = x - x0;
			float xg = 1.0f - xf;
			float[] rgb0 = new float[3];
			float[] rgb1 = new float[3];
			intToFloat3(map[offset1 + x0], rgb0);
			intToFloat3(map[offset1 + x1], rgb1);
			for (int i = 0; i < 3; i++) {
				rgb[offset2 + i] = xg * rgb0[i] + xf * rgb1[i];
			}
		}
	}

	private static float getBlendScale(int radius) {
		return 1.0f / ((2 * radius + 1) * (2 * radius + 1));
	}
}