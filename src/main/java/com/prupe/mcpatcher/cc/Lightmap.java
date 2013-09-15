package com.prupe.mcpatcher.cc;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.TexturePackAPI;

import net.minecraft.src.EntityRenderer;
import net.minecraft.src.ResourceLocation;
import net.minecraft.src.World;

public final class Lightmap {
	private static final MCLogger logger = MCLogger.getLogger("Custom Colors");
	private static final String LIGHTMAP_FORMAT = "lightmap/world%d.png";
	private static final int LIGHTMAP_SIZE = 16;
	private static final int HEIGHT_WITHOUT_NIGHTVISION = 32;
	private static final int HEIGHT_WITH_NIGHTVISION = 64;
	private static final boolean useLightmaps = Config.getBoolean("Custom Colors", "lightmaps", true);
	private static final HashMap<Integer, Lightmap> lightmaps = new HashMap();
	private final int width;
	private final boolean customNightvision;
	private final int[] origMap;
	private final boolean valid;
	private final float[] sunrgb = new float[48];
	private final float[] torchrgb = new float[48];
	private final float[] sunrgbnv = new float[48];
	private final float[] torchrgbnv = new float[48];
	private final float[] rgb = new float[3];

	static void reset() {
		lightmaps.clear();
	}

	public static boolean computeLightmap(EntityRenderer renderer, World world, int[] mapRGB, float partialTick) {
		if (world != null && useLightmaps) {
			Lightmap lightmap = null;
			int worldType = world.provider.dimensionId;

			if (lightmaps.containsKey(Integer.valueOf(worldType))) {
				lightmap = (Lightmap)lightmaps.get(Integer.valueOf(worldType));
			} else {
				ResourceLocation resource = TexturePackAPI.newMCPatcherResourceLocation(String.format("lightmap/world%d.png", new Object[] {Integer.valueOf(worldType)}));
				BufferedImage image = TexturePackAPI.getImage(resource);

				if (image != null) {
					lightmap = new Lightmap(resource, image);

					if (!lightmap.valid) {
						lightmap = null;
					}
				}

				lightmaps.put(Integer.valueOf(worldType), lightmap);
			}

			return lightmap != null && lightmap.compute(renderer, world, mapRGB, partialTick);
		} else {
			return false;
		}
	}

	private Lightmap(ResourceLocation resource, BufferedImage image) {
		this.width = image.getWidth();
		int height = image.getHeight();
		this.customNightvision = height == 64;
		this.origMap = new int[this.width * height];
		image.getRGB(0, 0, this.width, height, this.origMap, 0, this.width);
		this.valid = height == 32 || height == 64;

		if (!this.valid) {
			logger.error("%s must be exactly %d or %d pixels high", new Object[] {resource, Integer.valueOf(32), Integer.valueOf(64)});
		}
	}

	private boolean compute(EntityRenderer renderer, World world, int[] mapRGB, float partialTick) {
		float sun = Colorizer.clamp(world.lastLightningBolt > 0 ? 1.0F : 1.1666666F * (world.getSunBrightness(1.0F) - 0.2F)) * (float)(this.width - 1);
		float torch = Colorizer.clamp(renderer.torchFlickerX + 0.5F) * (float)(this.width - 1);
		float nightVisionStrength = renderer.getNightVisionStrength(partialTick);
		float gamma = Colorizer.clamp(MCPatcherUtils.getMinecraft().gameSettings.gammaSetting);
		int s;

		for (s = 0; s < 16; ++s) {
			interpolate(this.origMap, s * this.width, sun, this.sunrgb, 3 * s);
			interpolate(this.origMap, (s + 16) * this.width, torch, this.torchrgb, 3 * s);

			if (this.customNightvision && nightVisionStrength > 0.0F) {
				interpolate(this.origMap, (s + 32) * this.width, sun, this.sunrgbnv, 3 * s);
				interpolate(this.origMap, (s + 48) * this.width, torch, this.torchrgbnv, 3 * s);
			}
		}

		for (s = 0; s < 16; ++s) {
			for (int t = 0; t < 16; ++t) {
				int k;

				for (k = 0; k < 3; ++k) {
					this.rgb[k] = Colorizer.clamp(this.sunrgb[3 * s + k] + this.torchrgb[3 * t + k]);
				}

				if (nightVisionStrength > 0.0F) {
					if (this.customNightvision) {
						for (k = 0; k < 3; ++k) {
							this.rgb[k] = Colorizer.clamp((1.0F - nightVisionStrength) * this.rgb[k] + nightVisionStrength * (this.sunrgbnv[3 * s + k] + this.torchrgbnv[3 * t + k]));
						}
					} else {
						float var13 = Math.max(Math.max(this.rgb[0], this.rgb[1]), this.rgb[2]);

						if (var13 > 0.0F) {
							var13 = 1.0F - nightVisionStrength + nightVisionStrength / var13;

							for (int tmp = 0; tmp < 3; ++tmp) {
								this.rgb[tmp] = Colorizer.clamp(this.rgb[tmp] * var13);
							}
						}
					}
				}

				if (gamma != 0.0F) {
					for (k = 0; k < 3; ++k) {
						float var14 = 1.0F - this.rgb[k];
						var14 = 1.0F - var14 * var14 * var14 * var14;
						this.rgb[k] = gamma * var14 + (1.0F - gamma) * this.rgb[k];
					}
				}

				mapRGB[s * 16 + t] = -16777216 | Colorizer.float3ToInt(this.rgb);
			}
		}

		return true;
	}

	private static void interpolate(int[] map, int offset1, float x, float[] rgb, int offset2) {
		int x0 = (int)Math.floor((double)x);
		int x1 = (int)Math.ceil((double)x);

		if (x0 == x1) {
			Colorizer.intToFloat3(map[offset1 + x0], rgb, offset2);
		} else {
			float xf = x - (float)x0;
			float xg = 1.0F - xf;
			float[] rgb0 = new float[3];
			float[] rgb1 = new float[3];
			Colorizer.intToFloat3(map[offset1 + x0], rgb0);
			Colorizer.intToFloat3(map[offset1 + x1], rgb1);

			for (int i = 0; i < 3; ++i) {
				rgb[offset2 + i] = xg * rgb0[i] + xf * rgb1[i];
			}
		}
	}
}
