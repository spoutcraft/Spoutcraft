package com.prupe.mcpatcher.cc;

import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.TexturePackAPI;
import java.util.Arrays;
import net.minecraft.src.ResourceLocation;

final class ColorMap {
	private static final MCLogger logger = MCLogger.getLogger("Custom Colors");
	private static final int COLORMAP_SIZE = 256;
	private static final float COLORMAP_SCALE = 255.0F;
	private int[] map;
	private int mapDefault;
	private int lastBlendI = Integer.MIN_VALUE;
	private int lastBlendK = Integer.MAX_VALUE;
	private final float[] lastBlendResult = new float[3];

	static int getX(double temperature, double rainfall) {
		return (int)(255.0D * (1.0D - Colorizer.clamp(temperature)));
	}

	static int getY(double temperature, double rainfall) {
		return (int)(255.0D * (1.0D - Colorizer.clamp(rainfall) * Colorizer.clamp(temperature)));
	}

	static float getBlockMetaKey(int blockID, int metadata) {
		return (float)blockID + (float)(metadata & 255) / 256.0F;
	}

	ColorMap(int defaultColor) {
		this.mapDefault = defaultColor;
	}

	void loadColorMap(boolean useCustom, ResourceLocation resource) {
		if (useCustom) {
			this.map = MCPatcherUtils.getImageRGB(TexturePackAPI.getImage(resource));

			if (this.map != null) {
				if (this.map.length != 65536) {
					logger.error("%s must be %dx%d", new Object[] {resource, Integer.valueOf(256), Integer.valueOf(256)});
					this.map = null;
				} else {
					this.mapDefault = this.colorize(16777215, 0.5D, 1.0D);
					logger.fine("using %s, default color %06x", new Object[] {resource, Integer.valueOf(this.mapDefault)});
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

	int colorize(int defaultColor) {
		return this.map == null ? defaultColor : this.mapDefault;
	}

	int colorize(int defaultColor, double temperature, double rainfall) {
		return this.map == null ? defaultColor : this.map[256 * getY(temperature, rainfall) + getX(temperature, rainfall)];
	}

	int colorize(int defaultColor, int i, int j, int k) {
		return this.colorize(defaultColor, (double)BiomeHelper.getTemperature(i, j, k), (double)BiomeHelper.getRainfall(i, j, k));
	}

	void colorizeWithBlending(int i, int j, int k, int radius, float[] result) {
		if (i == this.lastBlendI && k == this.lastBlendK) {
			System.arraycopy(this.lastBlendResult, 0, result, 0, 3);
		} else {
			Arrays.fill(result, 0.0F);
			int diameter;

			for (diameter = -radius; diameter <= radius; ++diameter) {
				for (int scale = -radius; scale <= radius; ++scale) {
					int rgb = this.colorize(this.mapDefault, i + diameter, j, k + scale);
					Colorizer.intToFloat3(rgb, this.lastBlendResult);
					result[0] += this.lastBlendResult[0];
					result[1] += this.lastBlendResult[1];
					result[2] += this.lastBlendResult[2];
				}
			}

			diameter = 2 * radius + 1;
			float var9 = 1.0F / (float)(diameter * diameter);
			result[0] *= var9;
			result[1] *= var9;
			result[2] *= var9;
			System.arraycopy(result, 0, this.lastBlendResult, 0, 3);
			this.lastBlendI = i;
			this.lastBlendK = k;
		}
	}
}
