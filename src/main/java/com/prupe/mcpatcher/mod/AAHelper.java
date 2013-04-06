package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.TexturePackAPI;
import java.awt.image.BufferedImage;
import org.lwjgl.opengl.PixelFormat;

// Spout Start
import org.spoutcraft.client.config.Configuration;
// Spout End

public class AAHelper {
	private static final int debugColor = Config.getBoolean("Extended HD", "debugBorder", false) ? -16776961 : 0;
	private static final int aaSamples = Configuration.getAASampling();
	public static int border;

	static void reset() {
		TexturePackAPI.enableTextureBorder = false;
	}

	public static PixelFormat setupPixelFormat(PixelFormat var0) {
		if (aaSamples > 1) {
			return var0.withSamples(aaSamples);
		} else {
			return var0;
		}
	}

	public static BufferedImage addBorder(String var0, BufferedImage var1, boolean var2) {
		if (var1 != null && TexturePackAPI.enableTextureBorder) {
			var1 = MipmapHelper.fixTransparency(var0, var1);
			int var3 = var1.getWidth();
			int var4 = var1.getHeight();
			int var5;

			if (var2 && var4 % var3 == 0) {
				var5 = var4 / var3;
				var4 = var3;
			} else {
				var5 = 1;
			}

			setupBorder(var1, var3, var4);

			if (border <= 0) {
				return var1;
			} else {
				int var6 = var3 + 2 * border;
				int var7 = var4 + 2 * border;
				BufferedImage var8 = new BufferedImage(var6, var5 * var7, 2);

				for (int var9 = 0; var9 < var5; ++var9) {
					int var10 = var9 * var4;
					int var11 = var9 * var7;
					copyRegion(var1, 0, var10, var8, 0, var11, border, border, true, true);
					copyRegion(var1, 0, var10, var8, border, var11, var3, border, false, true);
					copyRegion(var1, var3 - border, var10, var8, var3 + border, var11, border, border, true, true);
					copyRegion(var1, 0, var10, var8, 0, var11 + border, border, var3, true, false);
					copyRegion(var1, 0, var10, var8, border, var11 + border, var3, var4, false, false);
					copyRegion(var1, var3 - border, var10, var8, var3 + border, var11 + border, border, var3, true, false);
					copyRegion(var1, 0, var10 + var4 - border, var8, 0, var11 + var4 + border, border, border, true, true);
					copyRegion(var1, 0, var10 + var4 - border, var8, border, var11 + var4 + border, var3, border, false, true);
					copyRegion(var1, var3 - border, var10 + var4 - border, var8, var3 + border, var11 + var4 + border, border, border, true, true);
					addDebugOutline(var8, var11, var3, var4);
				}

				return var8;
			}
		} else {
			border = 0;
			return var1;
		}
	}

	private static void setupBorder(BufferedImage var0, int var1, int var2) {
		if (aaSamples <= 1 && MipmapHelper.anisoLevel <= 1) {
			border = 0;
		} else if (MipmapHelper.mipmapEnabled && MipmapHelper.maxMipmapLevel > 0) {
			border = 1 << Math.max(Math.min(MipmapHelper.maxMipmapLevel, 4), 0);
		} else {
			border = 2;
		}

		border = Math.min(border, Math.min(var1, var2));
	}

	private static void copyRegion(BufferedImage var0, int var1, int var2, BufferedImage var3, int var4, int var5, int var6, int var7, boolean var8, boolean var9) {
		int[] var10 = new int[var6 * var7];
		var0.getRGB(var1, var2, var6, var7, var10, 0, var6);

		if (!var8 && !var9) {
			var3.setRGB(var4, var5, var6, var7, var10, 0, var6);
		} else {
			int[] var11 = new int[var6 * var7];

			for (int var12 = 0; var12 < var6; ++var12) {
				for (int var13 = 0; var13 < var7; ++var13) {
					var11[var6 * var13 + var12] = var10[var6 * (var9 ? var7 - 1 - var13 : var13) + (var8 ? var6 - 1 - var12 : var12)];
				}
			}

			var3.setRGB(var4, var5, var6, var7, var11, 0, var6);
		}
	}

	private static void addDebugOutline(BufferedImage var0, int var1, int var2, int var3) {
		if (debugColor != 0) {
			int var4;

			for (var4 = 0; var4 < var2; ++var4) {
				var0.setRGB(var4 + border, var1 + border, debugColor);
				var0.setRGB(var4 + border, var1 + var3 + border, debugColor);
			}

			for (var4 = 0; var4 < var3; ++var4) {
				var0.setRGB(border, var1 + var4 + border, debugColor);
				var0.setRGB(var3 + border, var1 + var4 + border, debugColor);
			}
		}
	}
}
