package com.pclewis.mcpatcher.mod;

import com.pclewis.mcpatcher.MCPatcherUtils;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;
import net.minecraft.src.FontRenderer;

public class FontUtils {
	private static final int ROWS = 16;
	private static final int COLS = 16;
	public static final char[] AVERAGE_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123467890".toCharArray();
	public static final int[] SPACERS = new int[] {33721342, 41975936, 234881023};
	private static final boolean showLines = false;
	private static Method getResource;

	public static float[] computeCharWidths(String var0, BufferedImage var1, int[] var2, int[] var3) {
		MCPatcherUtils.debug("computeCharWidths(%s)", new Object[] {var0});
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
							if (printThis(var9)) {
								MCPatcherUtils.debug("\'%c\' pixel (%d, %d) = %08x, colIdx = %d", new Object[] {Character.valueOf((char)var9), Integer.valueOf(var13), Integer.valueOf(var15), Integer.valueOf(var16), Integer.valueOf(var12)});
							}

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

		try {
			getCharWidthOverrides(var0, var4, var18);
		} catch (Throwable var17) {
			var17.printStackTrace();
		}

		if (!var18[32]) {
			var4[32] = defaultSpaceWidth(var4);
		}

		for (var10 = 0; var10 < var3.length; ++var10) {
			var3[var10] = Math.round(var4[var10]);

			if (printThis(var10)) {
				MCPatcherUtils.debug("charWidth[\'%c\'] = %f", new Object[] {Character.valueOf((char)var10), Float.valueOf(var4[var10])});
			}
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

	private static boolean printThis(int var0) {
		return "ABCDEF abcdef".indexOf(var0) >= 0;
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

	private static void getCharWidthOverrides(String var0, float[] var1, boolean[] var2) {
		if (getResource != null) {
			String var3 = var0.replace(".png", ".properties");
			InputStream var4;

			try {
				Object var5 = getResource.invoke((Object)null, new Object[] {var3});

				if (!(var5 instanceof InputStream)) {
					return;
				}

				var4 = (InputStream)var5;
			} catch (Exception var20) {
				var20.printStackTrace();
				return;
			}

			MCPatcherUtils.debug("reading character widths from %s", new Object[] {var3});

			try {
				Properties var21 = new Properties();
				var21.load(var4);
				Iterator var6 = var21.entrySet().iterator();

				while (var6.hasNext()) {
					Entry var7 = (Entry)var6.next();
					String var8 = var7.getKey().toString().trim();
					String var9 = var7.getValue().toString().trim();

					if (var8.matches("^width\\.\\d+$") && !var9.equals("")) {
						try {
							int var10 = Integer.parseInt(var8.substring(6));
							float var11 = Float.parseFloat(var9);

							if (var10 >= 0 && var10 < var1.length) {
								MCPatcherUtils.debug("    setting charWidthf[%d] to %f", new Object[] {Integer.valueOf(var10), Float.valueOf(var11)});
								var1[var10] = var11;
								var2[var10] = true;
							}
						} catch (NumberFormatException var17) {
							;
						}
					}
				}
			} catch (IOException var18) {
				var18.printStackTrace();
			} finally {
				MCPatcherUtils.close((Closeable)var4);
			}
		}
	}

	static {
		try {
			Class var0 = Class.forName("com.pclewis.mcpatcher.mod.TextureUtils");

			try {
				getResource = var0.getDeclaredMethod("getResourceAsStream", new Class[] {String.class});
			} catch (NoSuchMethodException var2) {
				var2.printStackTrace();
			}
		} catch (ClassNotFoundException var3) {
			;
		}
	}
}
