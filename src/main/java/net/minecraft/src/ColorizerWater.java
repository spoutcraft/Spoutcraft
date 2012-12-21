package net.minecraft.src;

public class ColorizerWater {
	// MCPatcher Start - private to public
	public static int[] waterBuffer = new int[65536];
	// MCPatcher End

	public static void setWaterBiomeColorizer(int[] par0ArrayOfInteger) {
		waterBuffer = par0ArrayOfInteger;
	}
}
