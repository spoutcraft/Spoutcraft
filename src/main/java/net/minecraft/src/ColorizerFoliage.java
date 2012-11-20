package net.minecraft.src;

public class ColorizerFoliage {

	public static int[] foliageBuffer = new int[65536]; // Spout HD private->public

	public static void setFoliageBiomeColorizer(int[] par0ArrayOfInteger) {
		foliageBuffer = par0ArrayOfInteger;
	}

	public static int getFoliageColor(double par0, double par2) {
		par2 *= par0;
		int var4 = (int) ((1.0D - par0) * 255.0D);
		int var5 = (int) ((1.0D - par2) * 255.0D);
		return foliageBuffer[var5 << 8 | var4];
	}

	public static int getFoliageColorPine() {
		return 6396257;
	}

	public static int getFoliageColorBirch() {
		return 8431445;
	}

	public static int getFoliageColorBasic() {
		return 4764952;
	}
}
