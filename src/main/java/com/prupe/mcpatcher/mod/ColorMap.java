package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.TexturePackAPI;

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

	ColorMap(int var1) {
		this.mapDefault = var1;
	}

	void loadColorMap(boolean var1, String var2) {
		if (var1) {
			this.map = MCPatcherUtils.getImageRGB(TexturePackAPI.getImage(var2));

			if (this.map != null) {
				if (this.map.length != 65536) {
					this.map = null;
				} else {
					this.mapDefault = this.colorize(16777215, 0.5D, 1.0D);
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
		return this.colorize(var1, (double)BiomeHelper.getTemperature(var2, var3, var4), (double)BiomeHelper.getRainfall(var2, var3, var4));
	}
}
