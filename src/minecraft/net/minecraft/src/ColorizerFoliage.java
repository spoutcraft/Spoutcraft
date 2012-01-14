package net.minecraft.src;

import com.pclewis.mcpatcher.mod.Colorizer;  //Spout HD

public class ColorizerFoliage {

	public static int[] foliageBuffer = new int[65536]; //Spout HD private->public


	public static void getFoilageBiomeColorizer(int[] var0) {
		foliageBuffer = var0;
	}

	public static int getFoliageColor(double var0, double var2) {
		var2 *= var0;
		int var4 = (int)((1.0D - var0) * 255.0D);
		int var5 = (int)((1.0D - var2) * 255.0D);
		return foliageBuffer[var5 << 8 | var4];
	}

	public static int getFoliageColorPine() {
		return Colorizer.colorizeBiome(6396257, Colorizer.COLOR_MAP_PINE); //Spout HD
	}

	public static int getFoliageColorBirch() {
		return Colorizer.colorizeBiome(8431445, Colorizer.COLOR_MAP_BIRCH); //Spout HD
	}

	public static int getFoliageColorBasic() {
		return Colorizer.colorizeBiome(4764952, Colorizer.COLOR_MAP_FOLIAGE); //Spout HD
	}

}
