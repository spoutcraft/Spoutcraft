package com.pclewis.mcpatcher.mod;

import com.pclewis.mcpatcher.MCPatcherUtils;
import net.minecraft.src.FontRenderer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;

public class FontUtils {
	private static final int ROWS = 16;
	private static final int COLS = 16;

	public static final char[] AVERAGE_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123467890".toCharArray();
	public static final int[] SPACERS = new int[]{0x02028bfe, 0x02808080, 0x0dffffff};

	private static final boolean showLines = false;

	private static Method getResource;

	public static float[] computeCharWidths(String filename, BufferedImage image, int[] rgb, int[] charWidth) {
		float[] charWidthf = new float[charWidth.length];
		int imgWidth = image.getWidth();
		int imgHeight = image.getHeight();
		int colWidth = imgWidth / COLS;
		int rowHeight = imgHeight / ROWS;
		for (int ch = 0; ch < charWidth.length; ch++) {
			int Ych = ch / COLS;
			int Xch = ch % COLS;
			int Xfw = colWidth - 1;//Text Alpha - fullwidth
			outer:
			for (int X0 = Xfw; X0 >= 0; X0--) {
				int x = Xch * colWidth + X0;
				for (int rowIdx = 0; rowIdx < rowHeight; rowIdx++) {
					int y = Ych * rowHeight + rowIdx;
					int colIdx = rgb[x + y * imgWidth];
					if (isOpaque(colIdx)) {
						charWidthf[ch] = (128F * (float)(X0+1)) / (float)imgWidth + 1F; // textAlpha - Will still overrun character fullwidth with HD!
						if (showLines) {
							for (int i = 0; i < rowHeight; i++) {
								y = Ych * rowHeight + i;
								for (int j = 0; j < Math.max(colWidth / 16, 1); j++) {
									image.setRGB(x + j, y, (i == rowIdx ? 0xff0000ff : 0xffff0000));
									image.setRGB(Xch * colWidth + j, y, 0xff00ff00);
								}
							}
						}
						break outer;
					}
				}
			}
		}
		for (int ch = 0; ch < charWidthf.length; ch++) {
			if (charWidthf[ch] <= 0.0f) {
				charWidthf[ch] = 2.0f;
			}
			else if (charWidthf[ch] >= 7.99f) { // textAlpha - Characters should not exceed a template's fullwidth unless overridden by the '.properties' file.
				charWidthf[ch] = 7.99f;
			}
		}
		boolean[] isOverride = new boolean[charWidth.length];
		try {
			getCharWidthOverrides(filename, charWidthf, isOverride);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		if (!isOverride[32]) {
			charWidthf[32] = defaultSpaceWidth(charWidthf);
		}
		for (int ch = 0; ch < charWidth.length; ch++) {
			charWidth[ch] = Math.round(charWidthf[ch]);
		}
		return charWidthf;
	}

	public static float getCharWidthf(FontRenderer fontRenderer, char ch) {
		float width = fontRenderer.getCharWidth(ch);
		if (width < 0 || ch >= fontRenderer.charWidthf.length || ch < 0) {
			return width;
		} else {
			return fontRenderer.charWidthf[ch];
		}
	}

	public static float getStringWidthf(FontRenderer fontRenderer, String s) {
		float totalWidth = 0.0f;
		if (s != null) {
			for (int i = 0; i < s.length(); i++) {
				char c = s.charAt(i);
				boolean isLink = false;
				float cWidth = fontRenderer.getCharWidthFloat(c);
				if (cWidth < 0.0f && i < s.length() - 1) {
					c = s.charAt(i + 1);
					if (c == 'l' || c == 'L') {
						isLink = true;
					} else if (c == 'r' || c == 'R' || (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F')) {
						isLink = false;
					}
					cWidth = fontRenderer.getCharWidthFloat(c);
				}
				totalWidth += cWidth;
				if (isLink) {
					totalWidth++;
				}
			}
		}
		return totalWidth;
	}

	private static boolean isOpaque(int colIdx) {
		for (int i : SPACERS) {
			if (colIdx == i) {
				return false;
			}
		}
		colIdx >>= 24; //begin AlphaText - extracts Alpha instead of Green, and declares it opaque if 16 or greater.
		colIdx &= 0xf0;
		return (colIdx > 0); //end AlphaText
	}

	private static float defaultSpaceWidth(float[] charWidthf) {
		float sum = 0.0f;
		int n = 0;
		for (char ch : AVERAGE_CHARS) {
			if (charWidthf[ch] > 0.0f) {
				sum += charWidthf[ch];
				n++;
			}
		}
		if (n > 0) {
			return sum / (float) n * 0.5f;
		} else {
			return 4.0f;
		}
	}

	private static void getCharWidthOverrides(String font, float[] charWidthf, boolean[] isOverride) {
		if (getResource == null) {
			return;
		}
		String textFile = font.replace(".png", ".properties");
		InputStream is;
		try {
			Object o = getResource.invoke(null, textFile);
			if (!(o instanceof InputStream)) {
				return;
			}
			is = (InputStream) o;
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		try {
			Properties props = new Properties();
			props.load(is);
			for (Map.Entry entry : props.entrySet()) {
				String key = entry.getKey().toString().trim();
				String value = entry.getValue().toString().trim();
				if (key.matches("^width\\.\\d+$") && !value.equals("")) {
					try {
						int ch = Integer.parseInt(key.substring(6));
						float width = Float.parseFloat(value);
						if (ch >= 0 && ch < charWidthf.length) {
							charWidthf[ch] = width;
							isOverride[ch] = true;
						}
					} catch (NumberFormatException e) {
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			MCPatcherUtils.close(is);
		}
	}
}
