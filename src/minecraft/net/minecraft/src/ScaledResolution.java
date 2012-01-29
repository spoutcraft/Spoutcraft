package net.minecraft.src;

public class ScaledResolution {
	private int scaledWidth;
	private int scaledHeight;
	public double scaledWidthD;
	public double scaledHeightD;
	public int scaleFactor;

	public ScaledResolution(GameSettings gamesettings, int i, int j) {
		scaledWidth = i;
		scaledHeight = j;
		scaleFactor = 1;
		int k = gamesettings.guiScale;
		if (k == 0) {
			k = 1000;
		}
		for (; scaleFactor < k && scaledWidth / (scaleFactor + 1) >= 320 && scaledHeight / (scaleFactor + 1) >= 240; scaleFactor++) { }
		scaledWidthD = (double)scaledWidth / (double)scaleFactor;
		scaledHeightD = (double)scaledHeight / (double)scaleFactor;
		scaledWidth = (int)Math.ceil(scaledWidthD);
		scaledHeight = (int)Math.ceil(scaledHeightD);
	}

	public int getScaledWidth() {
		return scaledWidth;
	}

	public int getScaledHeight() {
		return scaledHeight;
	}
}
