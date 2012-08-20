package com.pclewis.mcpatcher.mod;

import com.pclewis.mcpatcher.MCPatcherUtils;

final class ColorMap {
	private static final int COLORMAP_SIZE = 256;
	private static final float COLORMAP_SCALE = 255.0F;
	private int[] map;
	private int mapDefault;

	static int getX(double var0, double var2) {
		return (int)(255.0D * (1.0D - Colorizer.clamp(var0)));
	}

	static int getY(double var0, double var2) {
		return (int)(255.0D * (1.0D - Colorizer.clamp(var2) * Colorizer.clamp(var0)));
	}

	static float getBlockMetaKey(int var0, int var1) {
		return (float)var0 + (float)(var1 & 255) / 256.0F;
	}

	ColorMap(boolean var1, String var2, int var3) {
		this.mapDefault = var3;

		if (var1 && Colorizer.lastTexturePack != null) {
			this.map = MCPatcherUtils.getImageRGB(MCPatcherUtils.readImage(Colorizer.lastTexturePack.getResourceAsStream(var2)));

			if (this.map != null) {
				if (this.map.length != 65536) {
					MCPatcherUtils.error("%s must be %dx%d", new Object[] {var2, Integer.valueOf(256), Integer.valueOf(256)});
					this.map = null;
				} else {
					this.mapDefault = this.colorize(16777215, 0.5D, 1.0D);
					MCPatcherUtils.debug("using %s, default color %06x", new Object[] {var2, Integer.valueOf(this.mapDefault)});
				}
			}
		}
	}

	boolean isCustom() {
		return this.map != null;
	}

	void clear() {
		this.map = null;
	}

	int colorize() {
		return this.mapDefault;
	}

	int colorize(int var1) {
		return this.map == null ? var1 : this.mapDefault;
	}

	int colorize(int var1, double var2, double var4) {
		return this.map == null ? var1 : this.map[256 * getY(var2, var4) + getX(var2, var4)];
	}

	int colorize(int var1, int var2, int var3, int var4) {
		return this.colorize(var1, (double)BiomeHelper.instance.getTemperature(var2, var3, var4), (double)BiomeHelper.instance.getRainfall(var2, var3, var4));
	}
}
