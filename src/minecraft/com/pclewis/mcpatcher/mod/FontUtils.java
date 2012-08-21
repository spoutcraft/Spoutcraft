package com.pclewis.mcpatcher.mod;

import java.awt.image.BufferedImage;
import net.minecraft.src.FontRenderer;

public class FontUtils {
	public static final char[] AVERAGE_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123467890".toCharArray();
	public static final int[] SPACERS = new int[] {33721342, 41975936, 234881023};

	public static float[] computeCharWidths(String var0, BufferedImage var1, int[] var2, int[] var3) {
		float[] var4 = new float[var3.length];
		int var5 = var1.getWidth();
		int var6 = var1.getHeight();
		int var7 = var5 / 16;
		int var8 = var6 / 16;
		int var9 = 0;
		int var10;

		while (var9 < var3.length) {
			var10 = var9 / 16;
			int var11 = var9 % 16;
			int var12 = var7 - 1;
			label71:

			while (true) {
				if (var12 >= 0) {
					int var13 = var11 * var7 + var12;
					int var14 = 0;

					while (true) {
						if (var14 >= var8) {
							--var12;
							continue label71;
						}

						int var15 = var10 * var8 + var14;
						int var16 = var2[var13 + var15 * var5];

						if (isOpaque(var16)) {
							var4[var9] = 128.0F * (float)(var12 + 1) / (float)var5 + 1.0F;
							break;
						}

						++var14;
					}
				}

				++var9;
				break;
			}
		}

		for (var9 = 0; var9 < var4.length; ++var9) {
			if (var4[var9] <= 0.0F) {
				var4[var9] = 2.0F;
			} else if (var4[var9] >= 7.99F) {
				var4[var9] = 7.99F;
			}
		}

		boolean[] var18 = new boolean[var3.length];

		if (!var18[32]) {
			var4[32] = defaultSpaceWidth(var4);
		}

		for (var10 = 0; var10 < var3.length; ++var10) {
			var3[var10] = Math.round(var4[var10]);
		}

		return var4;
	}

	public static float getCharWidthf(FontRenderer var0, char var1) {
		float var2 = (float)var0.getCharWidth(var1);
		return var2 >= 0.0F && var1 < var0.charWidthf.length && var1 >= 0 ? var0.charWidthf[var1] : var2;
	}

	public static float getStringWidthf(FontRenderer var0, String var1) {
		float var2 = 0.0F;

		if (var1 != null) {
			boolean var3 = false;

			for (int var4 = 0; var4 < var1.length(); ++var4) {
				char var5 = var1.charAt(var4);
				float var6 = (float)var0.getCharWidth(var5);

				if (var6 < 0.0F && var4 < var1.length() - 1) {
					var5 = var1.charAt(var4 + 1);

					if (var5 != 108 && var5 != 76) {
						if (var5 == 114 || var5 == 82 || var5 >= 48 && var5 <= 57 || var5 >= 97 && var5 <= 102 || var5 >= 65 && var5 <= 70) {
							var3 = false;
						}
					} else {
						var3 = true;
					}

					var6 = (float)var0.getCharWidth(var5);
				}

				var2 += var6;

				if (var3) {
					++var2;
				}
			}
		}

		return var2;
	}

	private static boolean isOpaque(int var0) {
		int[] var1 = SPACERS;
		int var2 = var1.length;

		for (int var3 = 0; var3 < var2; ++var3) {
			int var4 = var1[var3];

			if (var0 == var4) {
				return false;
			}
		}

		return (var0 >> 24 & 240) > 0;
	}

	private static float defaultSpaceWidth(float[] var0) {
		float var1 = 0.0F;
		int var2 = 0;
		char[] var3 = AVERAGE_CHARS;
		int var4 = var3.length;

		for (int var5 = 0; var5 < var4; ++var5) {
			char var6 = var3[var5];

			if (var0[var6] > 0.0F) {
				var1 += var0[var6];
				++var2;
			}
		}

		if (var2 > 0) {
			return var1 / (float)var2 * 0.5F;
		} else {
			return 4.0F;
		}
	}
}
