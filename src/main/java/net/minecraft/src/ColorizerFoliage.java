package net.minecraft.src;

import com.pclewis.mcpatcher.mod.Colorizer; // Spout

public class ColorizerFoliage {

	/** Color buffer for foliage */
	public static int[] foliageBuffer = new int[65536];// Spout private -> public

	public static void setFoliageBiomeColorizer(int[] par0ArrayOfInteger) {
		foliageBuffer = par0ArrayOfInteger;
	}

	/**
	 * Gets foliage color from temperature and humidity. Args: temperature, humidity
	 */
	public static int getFoliageColor(double par0, double par2) {
		par2 *= par0;
		int var4 = (int)((1.0D - par0) * 255.0D);
		int var5 = (int)((1.0D - par2) * 255.0D);
		return foliageBuffer[var5 << 8 | var4];
	}

	/**
	 * Gets the foliage color for pine type (metadata 1) trees
	 */
	public static int getFoliageColorPine() {
		return Colorizer.colorizeBiome(6396257, Colorizer.COLOR_MAP_PINE); // Spout
	}

	/**
	 * Gets the foliage color for birch type (metadata 2) trees
	 */
	public static int getFoliageColorBirch() {
		return Colorizer.colorizeBiome(8431445, Colorizer.COLOR_MAP_BIRCH); // Spout
	}

	public static int getFoliageColorBasic() {
		return Colorizer.colorizeBiome(4764952, Colorizer.COLOR_MAP_FOLIAGE); // Spout
	}
}
