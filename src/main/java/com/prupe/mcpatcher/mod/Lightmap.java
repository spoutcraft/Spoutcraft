package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.TexturePackAPI;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import net.minecraft.src.EntityRenderer;
import net.minecraft.src.World;

public final class Lightmap {
	private static final String LIGHTMAP_FORMAT = "/environment/lightmap%d.png";
	private static final int LIGHTMAP_SIZE = 16;
	private static final int HEIGHT_WITHOUT_NIGHTVISION = 32;
	private static final int HEIGHT_WITH_NIGHTVISION = 64;
	private static final boolean useLightmaps = Config.getBoolean("Custom Colors", "lightmaps", true);
	private static final HashMap lightmaps = new HashMap();
	private final int width;
	private final boolean customNightvision;
	private final int[] origMap;
	private final boolean valid;
	private final int[] newMap = new int[256];
	private final float[] sunrgb = new float[48];
	private final float[] torchrgb = new float[48];
	private final float[] sunrgbnv = new float[48];
	private final float[] torchrgbnv = new float[48];
	private final float[] rgb = new float[3];

	static void reset() {
		lightmaps.clear();
	}

	public static boolean computeLightmap(EntityRenderer var0, World var1, float var2) {
		if (var1 != null && useLightmaps) {
			Lightmap var3 = null;
			int var4 = var1.provider.dimensionId;

			if (lightmaps.containsKey(Integer.valueOf(var4))) {
				var3 = (Lightmap)lightmaps.get(Integer.valueOf(var4));
			} else {
				String var5 = String.format("/environment/lightmap%d.png", new Object[] {Integer.valueOf(var4)});
				BufferedImage var6 = TexturePackAPI.getImage(var5);

				if (var6 != null) {
					var3 = new Lightmap(var5, var6);

					if (!var3.valid) {
						var3 = null;
					}
				}

				lightmaps.put(Integer.valueOf(var4), var3);
			}

			return var3 != null && var3.compute(var0, var1, var2);
		} else {
			return false;
		}
	}

	private Lightmap(String var1, BufferedImage var2) {
		this.width = var2.getWidth();
		int var3 = var2.getHeight();
		this.customNightvision = var3 == 64;
		this.origMap = new int[this.width * var3];
		var2.getRGB(0, 0, this.width, var3, this.origMap, 0, this.width);
		this.valid = var3 == 32 || var3 == 64;
	}

	private boolean compute(EntityRenderer var1, World var2, float var3) {
		float var4 = Colorizer.clamp(var2.lastLightningBolt > 0 ? 1.0F : 1.1666666F * (var2.getSunBrightness(1.0F) - 0.2F)) * (float)(this.width - 1);
		float var5 = Colorizer.clamp(var1.torchFlickerX + 0.5F) * (float)(this.width - 1);
		float var6 = var1.getNightVisionStrength(var3);
		float var7 = Colorizer.clamp(MCPatcherUtils.getMinecraft().gameSettings.gammaSetting);
		int var8;

		for (var8 = 0; var8 < 16; ++var8) {
			interpolate(this.origMap, var8 * this.width, var4, this.sunrgb, 3 * var8);
			interpolate(this.origMap, (var8 + 16) * this.width, var5, this.torchrgb, 3 * var8);

			if (this.customNightvision && var6 > 0.0F) {
				interpolate(this.origMap, (var8 + 32) * this.width, var4, this.sunrgbnv, 3 * var8);
				interpolate(this.origMap, (var8 + 48) * this.width, var5, this.torchrgbnv, 3 * var8);
			}
		}

		for (var8 = 0; var8 < 16; ++var8) {
			for (int var9 = 0; var9 < 16; ++var9) {
				int var10;

				for (var10 = 0; var10 < 3; ++var10) {
					this.rgb[var10] = Colorizer.clamp(this.sunrgb[3 * var8 + var10] + this.torchrgb[3 * var9 + var10]);
				}

				if (var6 > 0.0F) {
					if (this.customNightvision) {
						for (var10 = 0; var10 < 3; ++var10) {
							this.rgb[var10] = Colorizer.clamp((1.0F - var6) * this.rgb[var10] + var6 * (this.sunrgbnv[3 * var8 + var10] + this.torchrgbnv[3 * var9 + var10]));
						}
					} else {
						float var12 = Math.max(Math.max(this.rgb[0], this.rgb[1]), this.rgb[2]);

						if (var12 > 0.0F) {
							var12 = 1.0F - var6 + var6 / var12;

							for (int var11 = 0; var11 < 3; ++var11) {
								this.rgb[var11] = Colorizer.clamp(this.rgb[var11] * var12);
							}
						}
					}
				}

				if (var7 != 0.0F) {
					for (var10 = 0; var10 < 3; ++var10) {
						float var13 = 1.0F - this.rgb[var10];
						var13 = 1.0F - var13 * var13 * var13 * var13;
						this.rgb[var10] = var7 * var13 + (1.0F - var7) * this.rgb[var10];
					}
				}

				this.newMap[var8 * 16 + var9] = -16777216 | Colorizer.float3ToInt(this.rgb);
			}
		}

		MCPatcherUtils.getMinecraft().renderEngine.createTextureFromBytes(this.newMap, 16, 16, var1.lightmapTexture);
		return true;
	}

	private static void interpolate(int[] var0, int var1, float var2, float[] var3, int var4) {
		int var5 = (int)Math.floor((double)var2);
		int var6 = (int)Math.ceil((double)var2);

		if (var5 == var6) {
			Colorizer.intToFloat3(var0[var1 + var5], var3, var4);
		} else {
			float var7 = var2 - (float)var5;
			float var8 = 1.0F - var7;
			float[] var9 = new float[3];
			float[] var10 = new float[3];
			Colorizer.intToFloat3(var0[var1 + var5], var9);
			Colorizer.intToFloat3(var0[var1 + var6], var10);

			for (int var11 = 0; var11 < 3; ++var11) {
				var3[var4 + var11] = var8 * var9[var11] + var7 * var10[var11];
			}
		}
	}
}
