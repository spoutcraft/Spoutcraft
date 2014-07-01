package com.prupe.mcpatcher.hd;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.TexturePackAPI;
import com.prupe.mcpatcher.TexturePackChangeHandler;
import com.prupe.mcpatcher.hd.FontUtils$1;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.ResourceLocation;

public class FontUtils {
	private static final MCLogger logger = MCLogger.getLogger("HD Font");
	private static final boolean enable = Config.getBoolean("Extended HD", "hdFont", true);
	private static final boolean enableNonHD = Config.getBoolean("Extended HD", "nonHDFontWidth", false);
	private static final int ROWS = 16;
	private static final int COLS = 16;
	public static final char[] AVERAGE_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123467890".toCharArray();
	public static final int[] SPACERS = new int[] {33721342, 41975936, 234881023};
	private static final boolean showLines = false;
	private static final Set<FontRenderer> allRenderers = new HashSet();

	static void init() {}

	public static ResourceLocation getFontName(FontRenderer fontRenderer, ResourceLocation font) {
		if (fontRenderer.defaultFont == null) {
			fontRenderer.defaultFont = font;
		}

		if (fontRenderer.hdFont == null) {
			String newFont = fontRenderer.defaultFont.getResourceDomain();
			String name = fontRenderer.defaultFont.getResourcePath().replaceAll(".*/", "");
			fontRenderer.hdFont = new ResourceLocation(newFont, "mcpatcher/font/" + name);
		}

		ResourceLocation newFont1;

		if (enable && TexturePackAPI.hasResource(fontRenderer.hdFont)) {
			logger.fine("using %s instead of %s", new Object[] {fontRenderer.hdFont, fontRenderer.defaultFont});
			fontRenderer.isHD = true;
			newFont1 = fontRenderer.hdFont;
		} else {
			logger.fine("using default %s", new Object[] {fontRenderer.defaultFont});
			fontRenderer.isHD = enable && enableNonHD;
			newFont1 = fontRenderer.defaultFont;
		}

		fontRenderer.fontAdj = fontRenderer.isHD ? 0.0F : 1.0F;
		return newFont1;
	}

	public static float[] computeCharWidthsf(FontRenderer fontRenderer, ResourceLocation filename, BufferedImage image, int[] rgb, int[] charWidth, float fontAdj) {
		float[] charWidthf = new float[charWidth.length];
		int width;

		if (!fontRenderer.isHD) {
			fontRenderer.fontAdj = fontAdj;

			for (width = 0; width < charWidth.length; ++width) {
				charWidthf[width] = (float)charWidth[width];
			}

			charWidthf[32] = 4.0F;
			return charWidthf;
		} else {
			allRenderers.add(fontRenderer);
			width = image.getWidth();
			int height = image.getHeight();
			int colWidth = width / 16;
			int rowHeight = height / 16;
			int isOverride = 0;
			int ch;

			while (isOverride < charWidth.length) {
				ch = isOverride / 16;
				int col = isOverride % 16;
				int colIdx = colWidth - 1;
				label84:

				while (true) {
					if (colIdx >= 0) {
						int x = col * colWidth + colIdx;
						int rowIdx = 0;

						while (true) {
							if (rowIdx >= rowHeight) {
								--colIdx;
								continue label84;
							}

							int y = ch * rowHeight + rowIdx;
							int pixel = rgb[x + y * width];

							if (isOpaque(pixel)) {
								if (printThis(isOverride)) {
									logger.finer("\'%c\' pixel (%d, %d) = %08x, colIdx = %d", new Object[] {Character.valueOf((char)isOverride), Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(pixel), Integer.valueOf(colIdx)});
								}

								charWidthf[isOverride] = 128.0F * (float)(colIdx + 1) / (float)width + 1.0F;
								break;
							}

							++rowIdx;
						}
					}

					++isOverride;
					break;
				}
			}

			for (isOverride = 0; isOverride < charWidthf.length; ++isOverride) {
				if (charWidthf[isOverride] <= 0.0F) {
					charWidthf[isOverride] = 2.0F;
				} else if (charWidthf[isOverride] >= 7.99F) {
					charWidthf[isOverride] = 7.99F;
				}
			}

			boolean[] var20 = new boolean[charWidth.length];

			try {
				getCharWidthOverrides(filename, charWidthf, var20);
			} catch (Throwable var19) {
				var19.printStackTrace();
			}

			if (!var20[32]) {
				charWidthf[32] = defaultSpaceWidth(charWidthf);
			}

			for (ch = 0; ch < charWidth.length; ++ch) {
				charWidth[ch] = Math.round(charWidthf[ch]);

				if (printThis(ch)) {
					logger.finer("charWidth[\'%c\'] = %f", new Object[] {Character.valueOf((char)ch), Float.valueOf(charWidthf[ch])});
				}
			}

			return charWidthf;
		}
	}

	private static float getCharWidthf(FontRenderer fontRenderer, char ch) {
		float width = (float)fontRenderer.getCharWidth(ch);
		return width >= 0.0F && fontRenderer.charWidthf != null && ch < fontRenderer.charWidthf.length && ch >= 0 ? fontRenderer.charWidthf[ch] : width;
	}

	public static float getCharWidthf(FontRenderer fontRenderer, int[] charWidth, int ch) {
		return fontRenderer.isHD ? fontRenderer.charWidthf[ch] * (float)fontRenderer.FONT_HEIGHT / 8.0F : (float)charWidth[ch];
	}

	public static float getStringWidthf(FontRenderer fontRenderer, String s) {
		float totalWidth = 0.0F;

		if (s != null) {
			boolean isLink = false;

			for (int i = 0; i < s.length(); ++i) {
				char c = s.charAt(i);
				float cWidth = getCharWidthf(fontRenderer, c);

				if (cWidth < 0.0F && i < s.length() - 1) {
					++i;
					c = s.charAt(i);

					if (c != 108 && c != 76) {
						if (c == 114 || c == 82) {
							isLink = false;
						}
					} else {
						isLink = true;
					}

					cWidth = 0.0F;
				}

				totalWidth += cWidth;

				if (isLink) {
					++totalWidth;
				}
			}
		}

		return totalWidth;
	}

	public static ResourceLocation getUnicodePage(ResourceLocation resource) {
		if (enable && resource != null) {
			ResourceLocation newResource = new ResourceLocation(resource.getResourceDomain(), resource.getResourcePath().replaceFirst("^textures/", "mcpatcher/"));

			if (TexturePackAPI.hasResource(newResource)) {
				logger.fine("using %s instead of %s", new Object[] {newResource, resource});
				return newResource;
			}
		}

		return resource;
	}

	private static boolean isOpaque(int pixel) {
		int[] arr$ = SPACERS;
		int len$ = arr$.length;

		for (int i$ = 0; i$ < len$; ++i$) {
			int i = arr$[i$];

			if (pixel == i) {
				return false;
			}
		}

		return (pixel >> 24 & 240) > 0;
	}

	private static boolean printThis(int ch) {
		return "ABCDEF abcdef".indexOf(ch) >= 0;
	}

	private static float defaultSpaceWidth(float[] charWidthf) {
		if (TexturePackAPI.isDefaultTexturePack()) {
			return 4.0F;
		} else {
			float sum = 0.0F;
			int n = 0;
			char[] arr$ = AVERAGE_CHARS;
			int len$ = arr$.length;

			for (int i$ = 0; i$ < len$; ++i$) {
				char ch = arr$[i$];

				if (charWidthf[ch] > 0.0F) {
					sum += charWidthf[ch];
					++n;
				}
			}

			if (n > 0) {
				return sum / (float)n * 7.0F / 12.0F;
			} else {
				return 4.0F;
			}
		}
	}

	private static void getCharWidthOverrides(ResourceLocation font, float[] charWidthf, boolean[] isOverride) {
		ResourceLocation textFile = TexturePackAPI.transformResourceLocation(font, ".png", ".properties");
		Properties props = TexturePackAPI.getProperties(textFile);

		if (props != null) {
			logger.fine("reading character widths from %s", new Object[] {textFile});
			Iterator i$ = props.entrySet().iterator();

			while (i$.hasNext()) {
				Entry entry = (Entry)i$.next();
				String key = entry.getKey().toString().trim();
				String value = entry.getValue().toString().trim();

				if (key.matches("^width\\.\\d+$") && !value.equals("")) {
					try {
						int e = Integer.parseInt(key.substring(6));
						float width = Float.parseFloat(value);

						if (e >= 0 && e < charWidthf.length) {
							logger.finer("setting charWidthf[%d] to %f", new Object[] {Integer.valueOf(e), Float.valueOf(width)});
							charWidthf[e] = width;
							isOverride[e] = true;
						}
					} catch (NumberFormatException var11) {
						;
					}
				}
			}
		}
	}

	static Set access$000() {
		return allRenderers;
	}

	static {
		TexturePackChangeHandler.register(new FontUtils$1("HD Font", 1));
	}
}
