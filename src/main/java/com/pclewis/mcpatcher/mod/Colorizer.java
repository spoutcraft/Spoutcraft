package com.pclewis.mcpatcher.mod;

import com.pclewis.mcpatcher.MCLogger;
import com.pclewis.mcpatcher.MCPatcherUtils;
import com.pclewis.mcpatcher.TexturePackAPI;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class Colorizer {
	private static final MCLogger logger = MCLogger.getLogger(MCPatcherUtils.CUSTOM_COLORS);

	private static final String COLOR_PROPERTIES = "/color.properties";
	private static final String REDSTONE_COLORS = "/misc/redstonecolor.png";
	private static final String STEM_COLORS = "/misc/stemcolor.png";
	private static final String LAVA_DROP_COLORS = "/misc/lavadropcolor.png";
	private static final String MYCELIUM_COLORS = "/misc/myceliumparticlecolor.png";
	private static final String XPORB_COLORS = "/misc/xporbcolor.png";

	private static final String PALETTE_BLOCK_KEY = "palette.block.";
	private static final String TEXT_KEY = "text.";
	private static final String TEXT_CODE_KEY = TEXT_KEY + "code.";

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
	private static final boolean useFogColors = MCPatcherUtils.getBoolean(MCPatcherUtils.CUSTOM_COLORS, "fog", true);
	private static final boolean useCloudType = MCPatcherUtils.getBoolean(MCPatcherUtils.CUSTOM_COLORS, "clouds", true);
	private static final boolean useRedstoneColors = MCPatcherUtils.getBoolean(MCPatcherUtils.CUSTOM_COLORS, "redstone", true);
	private static final boolean useStemColors = MCPatcherUtils.getBoolean(MCPatcherUtils.CUSTOM_COLORS, "stem", true);
	private static final boolean useEggColors = MCPatcherUtils.getBoolean(MCPatcherUtils.CUSTOM_COLORS, "egg", true);
	private static final boolean useMapColors = MCPatcherUtils.getBoolean(MCPatcherUtils.CUSTOM_COLORS, "map", true);
	private static final boolean useDyeColors = MCPatcherUtils.getBoolean(MCPatcherUtils.CUSTOM_COLORS, "dye", true);
	private static final boolean useBlockColors = MCPatcherUtils.getBoolean(MCPatcherUtils.CUSTOM_COLORS, "otherBlocks", true);
	private static final boolean useTextColors = MCPatcherUtils.getBoolean(MCPatcherUtils.CUSTOM_COLORS, "text", true);
	private static final boolean useXPOrbColors = MCPatcherUtils.getBoolean(MCPatcherUtils.CUSTOM_COLORS, "xporb", true);
	private static final int fogBlendRadius = MCPatcherUtils.getInt(MCPatcherUtils.CUSTOM_COLORS, "fogBlendRadius", 7);
	private static final float fogBlendScale = getBlendScale(fogBlendRadius);
	private static final int blockBlendRadius = MCPatcherUtils.getInt(MCPatcherUtils.CUSTOM_COLORS, "blockBlendRadius", 1);
	private static final float blockBlendScale = getBlendScale(blockBlendRadius);

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

	private static int[] xpOrbColors;

	private static final HashMap<Integer, Integer> textColorMap = new HashMap<Integer, Integer>(); // text.*
	private static final int[] textCodeColors = new int[32]; // text.code.*
	private static final boolean[] textCodeColorSet = new boolean[32];
	private static int signTextColor; // text.sign

	private static final int[] origDyeColors = ItemDye.dyeColors.clone(); // dye.*
	private static final float[][] origFleeceColors = new float[EntitySheep.fleeceColorTable.length][]; // sheep.*

	public static final float[][] armorColors = new float[EntitySheep.fleeceColorTable.length][]; // armor.*
	public static int undyedLeatherColor; // armor.default

	public static final float[][] collarColors = new float[EntitySheep.fleeceColorTable.length][]; // collar.*

	static {
		try {
			for (int i = 0; i < EntitySheep.fleeceColorTable.length; i++) {
				origFleeceColors[i] = EntitySheep.fleeceColorTable[i].clone();
				armorColors[i] = EntitySheep.fleeceColorTable[i].clone();
				collarColors[i] = EntitySheep.fleeceColorTable[i].clone();
			}
			reset();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		TexturePackAPI.ChangeHandler.register(new TexturePackAPI.ChangeHandler(MCPatcherUtils.CUSTOM_COLORS, 2) {
			@Override
			protected void onChange() {
				reset();
				reloadColorProperties();
				reloadColorMaps();
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
				if (useDyeColors) {
					reloadDyeColors();
				}
				if (useTextColors) {
					reloadTextColors();
				}
				if (useXPOrbColors) {
					reloadXPOrbColors();
				}
			}
		});
	}

	public static int colorizeBiome(int defaultColor, int index, double temperature, double rainfall) {
		return fixedColorMaps[index].colorize(defaultColor, temperature, rainfall);
	}

	public static int colorizeBiome(int defaultColor, int index) {
		return fixedColorMaps[index].colorize(defaultColor);
	}

	public static int colorizeBiome(int defaultColor, int index, int i, int j, int k) {
		return fixedColorMaps[index].colorize(defaultColor, i, j, k);
	}

	public static int colorizeBiomeWithBlending(int defaultColor, int index, int i, int j, int k) {
		return colorizeWithBlending(fixedColorMaps[index], defaultColor, i, j, k);
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
		return colorizeWithBlending(colorMap, 0xffffff, i, j, k);
	}

	private static int colorizeWithBlending(ColorMap colorMap, int defaultColor, int i, int j, int k) {
		if (colorMap == null || !colorMap.isCustom() || !BiomeHelper.instance.useBlockBlending() || blockBlendRadius <= 0) {
			return defaultColor;
		}
		float[] sum = new float[3];
		float[] sample = new float[3];
		for (int di = -blockBlendRadius; di <= blockBlendRadius; di++) {
			for (int dk = -blockBlendRadius; dk <= blockBlendRadius; dk++) {
				int rgb = colorMap.colorize(defaultColor, i + di, j, k + dk);
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
		if (index < 0 || index >= textCodeColors.length || !textCodeColorSet[index]) {
			return defaultColor;
		} else {
			return (defaultColor & 0xff000000) | textCodeColors[index];
		}
	}

	public static int colorizeSignText() {
		return signTextColor;
	}

	public static int colorizeXPOrb(int origColor, float timer) {
		if (xpOrbColors == null || xpOrbColors.length == 0) {
			return origColor;
		} else {
			return xpOrbColors[(int) ((Math.sin(timer / 4.0) + 1.0) * (xpOrbColors.length - 1) / 2.0)];
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

	public static boolean computeRedstoneWireColor(int current) {
		if (redstoneColor == null) {
			return false;
		} else {
			System.arraycopy(redstoneColor[Math.max(Math.min(current, 15), 0)], 0, setColor, 0, 3);
			return true;
		}
	}

	public static int colorizeRedstoneWire(IBlockAccess blockAccess, int i, int j, int k, int defaultColor) {
		if (redstoneColor == null) {
			return defaultColor;
		} else {
			int metadata = Math.max(Math.min(blockAccess.getBlockMetadata(i, j, k), 15), 0);
			return float3ToInt(redstoneColor[metadata]);
		}
	}

	public static boolean computeWaterColor(double x, double y, double z) {
		if (useParticleColors && fixedColorMaps[COLOR_MAP_WATER].isCustom()) {
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
			int offset = 3 * Math.max(Math.min(lavaDropColors.length / 3 - 1, age - 20), 0);
			System.arraycopy(lavaDropColors, offset, setColor, 0, 3);
			return true;
		}
	}

	public static void setupBlockAccess(IBlockAccess blockAccess, boolean newBiomes) {
		if (blockAccess == null) {
			BiomeHelper.instance = new BiomeHelper.Stub();
		} else {
			BiomeHelper.instance = new BiomeHelper.New(blockAccess);
		}
	}

	public static void setupForFog(Entity entity) {
		fogCamera = entity;
		if (!biomesLogged) {
			biomesLogged = true;
			for (BiomeGenBase biome : biomes) {
				int x = ColorMap.getX(biome.temperature, biome.rainfall);
				int y = ColorMap.getY(biome.temperature, biome.rainfall);
				logger.finer("setupBiome #%d \"%s\" %06x (%d,%d)", biome.biomeID, biome.biomeName, biome.waterColorMultiplier, x, y);
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
		if (world.provider.dimensionId == 0 && computeFogColor(COLOR_MAP_FOG0)) {
			computeLightningFlash(world, f);
			return true;
		} else {
			return false;
		}
	}

	public static boolean computeSkyColor(World world, float f) {
		if (world.provider.dimensionId == 0 && computeFogColor(COLOR_MAP_SKY0)) {
			computeLightningFlash(world, f);
			return true;
		} else {
			return false;
		}
	}

	private static void computeLightningFlash(World world, float f) {
		if (world.lightningFlash > 0) {
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
		potion.origColor = potion.liquidColor;
		potions.add(potion);
	}

	public static void setupSpawnerEgg(String entityName, int entityID, int defaultShellColor, int defaultSpotColor) {
		logger.config("egg.shell.%s=%06x", entityName, defaultShellColor);
		logger.config("egg.spots.%s=%06x", entityName, defaultSpotColor);
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

	private static void reset() {
		properties = new Properties();

		fixedColorMaps[COLOR_MAP_SWAMP_GRASS] = new ColorMap(0x4e4e4e);
		fixedColorMaps[COLOR_MAP_SWAMP_FOLIAGE] = new ColorMap(0x4e4e4e);
		fixedColorMaps[COLOR_MAP_PINE] = new ColorMap(0x619961);
		fixedColorMaps[COLOR_MAP_BIRCH] = new ColorMap(0x80a755);
		fixedColorMaps[COLOR_MAP_FOLIAGE] = new ColorMap(0x48b518);
		fixedColorMaps[COLOR_MAP_WATER] = new ColorMap(0xffffff);
		fixedColorMaps[COLOR_MAP_UNDERWATER] = new ColorMap(0x050533);
		fixedColorMaps[COLOR_MAP_FOG0] = new ColorMap(0xc0d8ff);
		fixedColorMaps[COLOR_MAP_SKY0] = new ColorMap(0xffffff);

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
		Lightmap.clear();
		spawnerEggShellColors.clear();
		spawnerEggSpotColors.clear();
		cloudType = CLOUDS_DEFAULT;
		for (Potion potion : potions) {
			potion.liquidColor = potion.origColor;
		}
		for (MapColor mapColor : MapColor.mapColorArray) {
			if (mapColor != null) {
				mapColor.colorValue = mapColor.origColorValue;
			}
		}
		System.arraycopy(origDyeColors, 0, ItemDye.dyeColors, 0, origDyeColors.length);
		for (int i = 0; i < origFleeceColors.length; i++) {
			EntitySheep.fleeceColorTable[i] = origFleeceColors[i].clone();
			armorColors[i] = origFleeceColors[i].clone();
			collarColors[i] = origFleeceColors[i].clone();
		}
		undyedLeatherColor = 0xa06540;
		myceliumColors = null;
		xpOrbColors = null;
		textColorMap.clear();
		for (int i = 0; i < textCodeColorSet.length; i++) {
			textCodeColorSet[i] = false;
		}
		signTextColor = 0;
	}

	private static void reloadColorProperties() {
		if (TexturePackAPI.getProperties(COLOR_PROPERTIES, properties)) {
			logger.finer("reloading %s", COLOR_PROPERTIES);
		}
	}

	private static void reloadColorMaps() {
		fixedColorMaps[COLOR_MAP_SWAMP_GRASS].loadColorMap(useSwampColors, "/misc/swampgrasscolor.png");
		fixedColorMaps[COLOR_MAP_SWAMP_FOLIAGE].loadColorMap(useSwampColors, "/misc/swampfoliagecolor.png");
		fixedColorMaps[COLOR_MAP_PINE].loadColorMap(useTreeColors, "/misc/pinecolor.png");
		fixedColorMaps[COLOR_MAP_BIRCH].loadColorMap(useTreeColors, "/misc/birchcolor.png");
		fixedColorMaps[COLOR_MAP_FOLIAGE].loadColorMap(useTreeColors, "/misc/foliagecolor.png");
		fixedColorMaps[COLOR_MAP_FOLIAGE].clear();
		fixedColorMaps[COLOR_MAP_WATER].loadColorMap(useWaterColors, "/misc/watercolorX.png");
		fixedColorMaps[COLOR_MAP_UNDERWATER].loadColorMap(useWaterColors, "/misc/underwatercolor.png");
		fixedColorMaps[COLOR_MAP_FOG0].loadColorMap(useFogColors, "/misc/fogcolor0.png");
		fixedColorMaps[COLOR_MAP_SKY0].loadColorMap(useFogColors, "/misc/skycolor0.png");
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
			ColorMap colorMap = new ColorMap(0xffffff);
			colorMap.loadColorMap(true, key);
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
				logger.finer("using %s for block %s, default color %06x", key, idString, colorMap.colorize());
			}
		}
	}

	private static void reloadParticleColors() {
		loadFloatColor("drop.water", waterBaseColor);
		loadFloatColor("particle.water", waterBaseColor);
		loadFloatColor("particle.portal", portalColor);
		int[] rgb = MCPatcherUtils.getImageRGB(TexturePackAPI.getImage(LAVA_DROP_COLORS));
		if (rgb != null) {
			lavaDropColors = new float[3 * rgb.length];
			for (int i = 0; i < rgb.length; i++) {
				intToFloat3(rgb[i], lavaDropColors, 3 * i);
			}
		}
		myceliumColors = MCPatcherUtils.getImageRGB(TexturePackAPI.getImage(MYCELIUM_COLORS));
	}

	private static void reloadRedstoneColors() {
		int[] rgb = MCPatcherUtils.getImageRGB(TexturePackAPI.getImage(REDSTONE_COLORS));
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
		int[] rgb = MCPatcherUtils.getImageRGB(TexturePackAPI.getImage(STEM_COLORS));
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

	private static void reloadDyeColors() {
		for (int i = 0; i < ItemDye.dyeColors.length; i++) {
			loadIntColor("dye." + getStringKey(ItemDye.dyeColorNames, i), ItemDye.dyeColors, i);
		}
		for (int i = 0; i < EntitySheep.fleeceColorTable.length; i++) {
			String key = getStringKey(ItemDye.dyeColorNames, EntitySheep.fleeceColorTable.length - 1 - i);
			loadFloatColor("sheep." + key, EntitySheep.fleeceColorTable[i]);
			loadFloatColor("armor." + key, armorColors[i]);
			loadFloatColor("collar." + key, collarColors[i]);
		}
		undyedLeatherColor = loadIntColor("armor.default", undyedLeatherColor);
	}

	private static void reloadTextColors() {
		for (int i = 0; i < textCodeColors.length; i++) {
			textCodeColorSet[i] = loadIntColor(TEXT_CODE_KEY + i, textCodeColors, i);
			if (textCodeColorSet[i] && i + 16 < textCodeColors.length) {
				textCodeColors[i + 16] = (textCodeColors[i] & 0xfcfcfc) >> 2;
				textCodeColorSet[i + 16] = true;
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
		signTextColor = loadIntColor("text.sign", 0);
	}

	private static void reloadXPOrbColors() {
		xpOrbColors = MCPatcherUtils.getImageRGB(TexturePackAPI.getImage(XPORB_COLORS));
	}

	private static String getStringKey(String[] keys, int index) {
		if (keys != null && index >= 0 && index < keys.length && keys[index] != null) {
			return keys[index];
		} else {
			return "" + index;
		}
	}

	private static void loadIntColor(String key, Potion potion) {
		logger.config("%s=%06x", key, potion.liquidColor);
		String value = properties.getProperty(key, "");
		if (!value.equals("")) {
			try {
				potion.liquidColor = Integer.parseInt(value, 16);
			} catch (NumberFormatException e) {
			}
		}
	}

	private static boolean loadIntColor(String key, int[] color, int index) {
		logger.config("%s=%06x", key, color[index]);
		String value = properties.getProperty(key, "");
		if (!value.equals("")) {
			try {
				color[index] = Integer.parseInt(value, 16);
				return true;
			} catch (NumberFormatException e) {
			}
		}
		return false;
	}

	private static int loadIntColor(String key, int color) {
		logger.config("%s=%06x", key, color);
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
		logger.config("%s=%06x", key, float3ToInt(color));
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

	static void intToFloat3(int rgb, float[] f, int offset) {
		f[offset] = (float) (rgb & 0xff0000) / (float) 0xff0000;
		f[offset + 1] = (float) (rgb & 0xff00) / (float) 0xff00;
		f[offset + 2] = (float) (rgb & 0xff) / (float) 0xff;
	}

	static void intToFloat3(int rgb, float[] f) {
		intToFloat3(rgb, f, 0);
	}

	static int float3ToInt(float[] f, int offset) {
		return ((int) (255.0f * f[offset])) << 16 | ((int) (255.0f * f[offset + 1])) << 8 | (int) (255.0f * f[offset + 2]);
	}

	static int float3ToInt(float[] f) {
		return float3ToInt(f, 0);
	}

	static float clamp(float f) {
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

	static void clamp(float[] f) {
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