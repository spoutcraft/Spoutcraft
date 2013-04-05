package net.minecraft.src;

// MCPatcher Start
import com.prupe.mcpatcher.mod.ColorizeBlock;
import com.prupe.mcpatcher.mod.Colorizer;
// MCPatcher End

public class ColorizerFoliage {

	/** Color buffer for foliage */
	// MCPatcher Start - private to public
	public static int[] foliageBuffer = new int[65536];
	// MCPatcher End

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
		// MCPatcher Start
		return ColorizeBlock.colorizeBiome(6396257, Colorizer.COLOR_MAP_PINE);
		// MCPatcher End
	}

	/**
	 * Gets the foliage color for birch type (metadata 2) trees
	 */
	public static int getFoliageColorBirch() {
		// MCPatcher Start
		return ColorizeBlock.colorizeBiome(8431445, Colorizer.COLOR_MAP_BIRCH);
		// MCPatcher End
	}

	public static int getFoliageColorBasic() {
		// MCPatcher Start
		return ColorizeBlock.colorizeBiome(4764952, Colorizer.COLOR_MAP_FOLIAGE);
		// MCPatcher End
	}
}
