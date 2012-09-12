package net.minecraft.src;

public class ColorizerWater {

	public static int[] waterBuffer = new int[65536]; // Spout HD private -> public

	// Spout start
	public ColorizerWater() {
	}

	public static int getWaterColor(double d, double d1) {
		d1 *= d;
		int i = (int) ((1.0D - d) * 255D);
		int j = (int) ((1.0D - d1) * 255D);
		return waterBuffer[j << 8 | i];
	}
	// Spout end
	
	public static void setWaterBiomeColorizer(int[] par0ArrayOfInteger) {
		waterBuffer = par0ArrayOfInteger;
	}

}
