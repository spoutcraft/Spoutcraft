package com.prupe.mcpatcher.mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import net.minecraft.src.MapColor;
import net.minecraft.src.Potion;

public class ColorizeItem {
	private static final Map entityNamesByID = new HashMap();
	private static final Map spawnerEggShellColors = new HashMap();
	private static final Map spawnerEggSpotColors = new HashMap();
	private static int waterBottleColor;
	private static List potions = new ArrayList();
	private static final String[] MAP_MATERIALS = new String[] {"air", "grass", "sand", "cloth", "tnt", "ice", "iron", "foliage", "snow", "clay", "dirt", "stone", "water", "wood"};

	static void reset() {
		spawnerEggShellColors.clear();
		spawnerEggSpotColors.clear();
		waterBottleColor = 3694022;
		Potion var1;

		for (Iterator var0 = potions.iterator(); var0.hasNext(); var1.liquidColor = var1.origColor) {
			var1 = (Potion)var0.next();
		}

		MapColor[] var4 = MapColor.mapColorArray;
		int var5 = var4.length;

		for (int var2 = 0; var2 < var5; ++var2) {
			MapColor var3 = var4[var2];

			if (var3 != null) {
				var3.colorValue = var3.origColorValue;
			}
		}
	}

	static void reloadPotionColors(Properties var0) {
		Iterator var1 = potions.iterator();

		while (var1.hasNext()) {
			Potion var2 = (Potion)var1.next();
			Colorizer.loadIntColor(var2.name, var2);
		}

		int[] var3 = new int[] {waterBottleColor};
		Colorizer.loadIntColor("potion.water", var3, 0);
		waterBottleColor = var3[0];
	}

	static void reloadMapColors(Properties var0) {
		for (int var1 = 0; var1 < MapColor.mapColorArray.length; ++var1) {
			if (MapColor.mapColorArray[var1] != null) {
				int[] var2 = new int[] {MapColor.mapColorArray[var1].origColorValue};
				Colorizer.loadIntColor("map." + Colorizer.getStringKey(MAP_MATERIALS, var1), var2, 0);
				MapColor.mapColorArray[var1].colorValue = var2[0];
			}
		}
	}

	public static void setupSpawnerEgg(String var0, int var1, int var2, int var3) {
		entityNamesByID.put(Integer.valueOf(var1), var0);
	}

	public static void setupPotion(Potion var0) {
		var0.origColor = var0.liquidColor;
		potions.add(var0);
	}

	public static int colorizeSpawnerEgg(int var0, int var1, int var2) {
		if (!Colorizer.useEggColors) {
			return var0;
		} else {
			Integer var3 = null;
			Map var4 = var2 == 0 ? spawnerEggShellColors : spawnerEggSpotColors;

			if (var4.containsKey(Integer.valueOf(var1))) {
				var3 = (Integer)var4.get(Integer.valueOf(var1));
			} else if (entityNamesByID.containsKey(Integer.valueOf(var1))) {
				String var5 = (String)entityNamesByID.get(Integer.valueOf(var1));

				if (var5 != null) {
					int[] var6 = new int[] {var0};
					Colorizer.loadIntColor((var2 == 0 ? "egg.shell." : "egg.spots.") + var5, var6, 0);
					var4.put(Integer.valueOf(var1), Integer.valueOf(var6[0]));
					var3 = Integer.valueOf(var6[0]);
				}
			}

			return var3 == null ? var0 : var3.intValue();
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
