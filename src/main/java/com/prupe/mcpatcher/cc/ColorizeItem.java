package com.prupe.mcpatcher.cc;

import com.prupe.mcpatcher.MCLogger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import net.minecraft.src.MapColor;
import net.minecraft.src.Potion;

public class ColorizeItem {
	private static final MCLogger logger = MCLogger.getLogger("Custom Colors");
	private static final Map<Integer, String> entityNamesByID = new HashMap();
	private static final Map<Integer, Integer> spawnerEggShellColors = new HashMap();
	private static final Map<Integer, Integer> spawnerEggSpotColors = new HashMap();
	private static int waterBottleColor;
	private static final List<Potion> potions = new ArrayList();
	private static final String[] MAP_MATERIALS = new String[] {"air", "grass", "sand", "cloth", "tnt", "ice", "iron", "foliage", "snow", "clay", "dirt", "stone", "water", "wood"};

	static void reset() {
		spawnerEggShellColors.clear();
		spawnerEggSpotColors.clear();
		waterBottleColor = 3694022;
		Potion len$;

		for (Iterator arr$ = potions.iterator(); arr$.hasNext(); len$.liquidColor = len$.origColor) {
			len$ = (Potion)arr$.next();
		}

		MapColor[] var4 = MapColor.mapColorArray;
		int var5 = var4.length;

		for (int i$ = 0; i$ < var5; ++i$) {
			MapColor mapColor = var4[i$];

			if (mapColor != null) {
				mapColor.colorValue = mapColor.origColorValue;
			}
		}
	}

	static void reloadPotionColors(Properties properties) {
		Iterator temp = potions.iterator();

		while (temp.hasNext()) {
			Potion potion = (Potion)temp.next();
			Colorizer.loadIntColor(potion.name, potion);
		}

		int[] temp1 = new int[] {waterBottleColor};
		Colorizer.loadIntColor("potion.water", temp1, 0);
		waterBottleColor = temp1[0];
	}

	static void reloadMapColors(Properties properties) {
		for (int i = 0; i < MapColor.mapColorArray.length; ++i) {
			if (MapColor.mapColorArray[i] != null) {
				int[] rgb = new int[] {MapColor.mapColorArray[i].origColorValue};
				Colorizer.loadIntColor("map." + Colorizer.getStringKey(MAP_MATERIALS, i), rgb, 0);
				MapColor.mapColorArray[i].colorValue = rgb[0];
			}
		}
	}

	public static void setupSpawnerEgg(String entityName, int entityID, int defaultShellColor, int defaultSpotColor) {
		logger.config("egg.shell.%s=%06x", new Object[] {entityName, Integer.valueOf(defaultShellColor)});
		logger.config("egg.spots.%s=%06x", new Object[] {entityName, Integer.valueOf(defaultSpotColor)});
		entityNamesByID.put(Integer.valueOf(entityID), entityName);
	}

	public static void setupPotion(Potion potion) {
		potion.origColor = potion.liquidColor;
		potions.add(potion);
	}

	public static int colorizeSpawnerEgg(int defaultColor, int entityID, int spots) {
		if (!Colorizer.useEggColors) {
			return defaultColor;
		} else {
			Integer value = null;
			Map eggMap = spots == 0 ? spawnerEggShellColors : spawnerEggSpotColors;

			if (eggMap.containsKey(Integer.valueOf(entityID))) {
				value = (Integer)eggMap.get(Integer.valueOf(entityID));
			} else if (entityNamesByID.containsKey(Integer.valueOf(entityID))) {
				String name = (String)entityNamesByID.get(Integer.valueOf(entityID));

				if (name != null) {
					int[] tmp = new int[] {defaultColor};
					Colorizer.loadIntColor((spots == 0 ? "egg.shell." : "egg.spots.") + name, tmp, 0);
					eggMap.put(Integer.valueOf(entityID), Integer.valueOf(tmp[0]));
					value = Integer.valueOf(tmp[0]);
				}
			}

			return value == null ? defaultColor : value.intValue();
		}
	}

	public static int getWaterBottleColor() {
		return waterBottleColor;
	}

	static {
		try {
			reset();
		} catch (Throwable var1) {
			var1.printStackTrace();
		}
	}
}
