package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.TexturePackAPI;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;
import javax.imageio.ImageIO;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.RenderEngine;

public class FontUtils {
	public static final boolean enable = Config.getBoolean("Extended HD", "hdFont", true);
	private static final int ROWS = 16;
	private static final int COLS = 16;
	public static final char[] AVERAGE_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123467890".toCharArray();
	public static final int[] SPACERS = new int[] {33721342, 41975936, 234881023};
	private static final boolean showLines = false;

	public static String getFontName(String var0) {
		var0 = var0.replaceFirst("_hd\\.png$", ".png");
		String var1 = var0.replaceFirst("\\.png$", "_hd.png");

		if (enable && TexturePackAPI.hasResource(var1)) {
			return var1;
		} else {
			return var0;
		}
	}

	public static BufferedImage getImage(Object var0, String var1) throws IOException {
		return enable ? TexturePackAPI.getImage(var1) : ImageIO.read(RenderEngine.class.getResourceAsStream(var1));
	}

	public static float[] computeCharWidthsf(FontRenderer var0, String var1, BufferedImage var2, int[] var3, int[] var4) {
		float[] var5 = new float[var4.length];
		int var6;

		if (!enable) {
			for (var6 = 0; var6 < var4.length; ++var6) {
				var5[var6] = (float)var4[var6];
			}

			return var5;
		} else {
			var6 = var2.getWidth();
			int var7 = var2.getHeight();
			int var8 = var6 / 16;
			int var9 = var7 / 16;
			int var10 = 0;
			int var11;

			while (var10 < var4.length) {
				var11 = var10 / 16;
				int var12 = var10 % 16;
				int var13 = var8 - 1;
				label84:

				while (true) {
					if (var13 >= 0) {
						int var14 = var12 * var8 + var13;
						int var15 = 0;

						while (true) {
							if (var15 >= var9) {
								--var13;
								continue label84;
							}

							int var16 = var11 * var9 + var15;
							int var17 = var3[var14 + var16 * var6];

							if (isOpaque(var17)) {
								var5[var10] = 128.0F * (float)(var13 + 1) / (float)var6 + 1.0F;
								break;
							}

							++var15;
						}
					}

					++var10;
					break;
				}
			}

			for (var10 = 0; var10 < var5.length; ++var10) {
				if (var5[var10] <= 0.0F) {
					var5[var10] = 2.0F;
				} else if (var5[var10] >= 7.99F) {
					var5[var10] = 7.99F;
				}
			}

			boolean[] var19 = new boolean[var4.length];

			try {
				getCharWidthOverrides(var1, var5, var19);
			} catch (Throwable var18) {
				var18.printStackTrace();
			}

			if (!var19[32]) {
				var5[32] = defaultSpaceWidth(var5);
			}

			for (var11 = 0; var11 < var4.length; ++var11) {
				var4[var11] = Math.round(var5[var11]);
			}

			return var5;
		}
	}

	private static float getCharWidthf(FontRenderer var0, char var1) {
		float var2 = (float)var0.getCharWidth(var1);
		return var2 >= 0.0F && var0.charWidthf != null && var1 < var0.charWidthf.length && var1 >= 0 ? var0.charWidthf[var1] : var2;
	}

	public static float getCharWidthf(FontRenderer var0, int[] var1, int var2) {
		return enable ? var0.charWidthf[var2] * (float)var0.FONT_HEIGHT / 8.0F : (float)var1[var2];
	}

	public static float getStringWidthf(FontRenderer var0, String var1) {
		float var2 = 0.0F;
		var1 = org.bukkit.ChatColor.stripColor(var1); // Spout - Strip Colors & Formatting Codes to calculate spacing properly.

		if (var1 != null) {
			boolean var3 = false;

			for (int var4 = 0; var4 < var1.length(); ++var4) {
				char var5 = var1.charAt(var4);
				float var6 = getCharWidthf(var0, var5);

				if (var6 < 0.0F && var4 < var1.length() - 1) {
					++var4;
					var5 = var1.charAt(var4);

					if (var5 != 108 && var5 != 76) {
						if (var5 == 114 || var5 == 82) {
							var3 = false;
						}
					} else {
						var3 = true;
					}

					var6 = 0.0F;
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

	private static boolean printThis(int var0) {
		return "ABCDEF abcdef".indexOf(var0) >= 0;
	}

	private static float defaultSpaceWidth(float[] var0) {
		if (TexturePackAPI.isDefaultTexturePack()) {
			return 4.0F;
		} else {
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
				return var1 / (float)var2 * 7.0F / 12.0F;
			} else {
				return 4.0F;
			}
		}
	}

	private static void getCharWidthOverrides(String var0, float[] var1, boolean[] var2) {
		String var3 = var0.replace(".png", ".properties");
		Properties var4 = TexturePackAPI.getProperties(var3);

		if (var4 != null) {
			Iterator var5 = var4.entrySet().iterator();

			while (var5.hasNext()) {
				Entry var6 = (Entry)var5.next();
				String var7 = var6.getKey().toString().trim();
				String var8 = var6.getValue().toString().trim();

				if (var7.matches("^width\\.\\d+$") && !var8.equals("")) {
					try {
						int var9 = Integer.parseInt(var7.substring(6));
						float var10 = Float.parseFloat(var8);

						if (var9 >= 0 && var9 < var1.length) {
							var1[var9] = var10;
							var2[var9] = true;
						}
					} catch (NumberFormatException var11) {
						;
					}
				}
			}
		}
	}
}