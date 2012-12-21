package com.pclewis.mcpatcher.mod;

import com.pclewis.mcpatcher.MCLogger;
import com.pclewis.mcpatcher.MCPatcherUtils;
import com.pclewis.mcpatcher.TexturePackAPI;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;

public class FontUtils {
	private static final MCLogger logger = MCLogger.getLogger("HD Font");
	private static final int ROWS = 16;
	private static final int COLS = 16;
	public static final char[] AVERAGE_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123467890".toCharArray();
	public static final int[] SPACERS = new int[] {33721342, 41975936, 234881023};
	private static final boolean showLines = false;

	private static void setFontRenderer(Minecraft var0, FontRenderer var1, String var2) {
		if (var1 != null) {
			boolean var3 = var1.unicodeFlag;
			var1.initialize(var0.gameSettings, var2, var0.renderEngine);
			var1.unicodeFlag = var3;
		}
	}

	public static float[] computeCharWidths(FontRenderer var0, String var1, BufferedImage var2, int[] var3, int[] var4) {
		if (var0.FONT_HEIGHT == 0) {
			var0.FONT_HEIGHT = 8;
		}

		float[] var5 = new float[var4.length];
		int var6 = var2.getWidth();
		int var7 = var2.getHeight();
		int var8 = var6 / 16;
		int var9 = var7 / 16;
		int var10 = 0;
		int var11;

		while (var10 < var4.length) {
			var11 = var10 / 16;
			int var12 = var10 % 16;
			int var13 = var8 - 1;
			label75:

			while (true) {
				if (var13 >= 0) {
					int var14 = var12 * var8 + var13;
					int var15 = 0;

					while (true) {
						if (var15 >= var9) {
							--var13;
							continue label75;
						}

						int var16 = var11 * var9 + var15;
						int var17 = var3[var14 + var16 * var6];

						if (isOpaque(var17)) {
							if (printThis(var10)) {
								logger.finer("\'%c\' pixel (%d, %d) = %08x, colIdx = %d", new Object[] {Character.valueOf((char)var10), Integer.valueOf(var14), Integer.valueOf(var16), Integer.valueOf(var17), Integer.valueOf(var13)});
							}

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

			if (printThis(var11)) {
				logger.finer("charWidth[\'%c\'] = %f", new Object[] {Character.valueOf((char)var11), Float.valueOf(var5[var11])});
			}
		}

		return var5;
	}

	public static float getCharWidthf(FontRenderer var0, char var1) { // Spout
		float var2 = (float)var0.getCharWidth(var1);
		return var2 >= 0.0F && var1 < var0.charWidthf.length && var1 >= 0 ? var0.charWidthf[var1] : var2;
	}

	public static float getStringWidthf(FontRenderer var0, String var1) {
		float var2 = 0.0F;

		if (var1 != null) {
			boolean var3 = false;

			for (int var4 = 0; var4 < var1.length(); ++var4) {
				char var5 = var1.charAt(var4);
				float var6 = getCharWidthf(var0, var5);

				if (var6 < 0.0F && var4 < var1.length() - 1) {
					++var4;
					var5 = var1.charAt(var4);

					if (var5 != 108 && var5 != 76) {
						if (var5 == 114 || var5 == 82 || var5 >= 48 && var5 <= 57 || var5 >= 97 && var5 <= 102 || var5 >= 65 && var5 <= 70) {
							var3 = false;
						}
					} else {
						var3 = true;
					}

					var6 = getCharWidthf(var0, var5);
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
			logger.fine("reading character widths from %s", new Object[] {var3});
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
							logger.finer("setting charWidthf[%d] to %f", new Object[] {Integer.valueOf(var9), Float.valueOf(var10)});
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

	static void access$000(Minecraft var0, FontRenderer var1, String var2) {
		setFontRenderer(var0, var1, var2);
	}

	static {
		TexturePackAPI.loadFontFromTexturePack = true;

		TexturePackAPI.ChangeHandler.register(new TexturePackAPI.ChangeHandler(MCPatcherUtils.HD_FONT, 2) {
			@Override
			protected void onChange() {
				Minecraft var1 = MCPatcherUtils.getMinecraft();
				FontUtils.access$000(var1, var1.fontRenderer, "/font/default.png");

				if (var1.standardGalacticFontRenderer != var1.fontRenderer) {
					FontUtils.access$000(var1, var1.standardGalacticFontRenderer, "/font/alternate.png");
				}
			}
		});
	}
}
