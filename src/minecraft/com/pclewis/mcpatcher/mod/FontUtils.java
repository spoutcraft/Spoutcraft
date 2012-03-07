package com.pclewis.mcpatcher.mod;

import com.pclewis.mcpatcher.MCPatcherUtils;

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

	static {
		Class<?> utils;
		try {
			utils = Class.forName(MCPatcherUtils.TEXTURE_UTILS_CLASS);
			try {
				getResource = utils.getDeclaredMethod("getResourceAsStream", String.class);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
		}
	}

	public static float[] computeCharWidths(String filename, BufferedImage image, int[] rgb, int[] charWidth) {
		MCPatcherUtils.log("computeCharWidths(%s)", filename);
		float[] charWidthf = new float[charWidth.length];
		int width = image.getWidth();
		int height = image.getHeight();
		int colWidth = width / COLS;
		int rowHeight = height / ROWS;
		for (int ch = 0; ch < charWidth.length; ch++) {
			int row = ch / COLS;
			int col = ch % COLS;
			outer:
			for (int colIdx = colWidth - 1; colIdx >= 0; colIdx--) {
				int x = col * colWidth + colIdx;
				for (int rowIdx = 0; rowIdx < rowHeight; rowIdx++) {
					int y = row * rowHeight + rowIdx;
					int pixel = rgb[x + y * width];
					if (isOpaque(pixel)) {
						if (printThis(ch)) {
							MCPatcherUtils.log("'%c' pixel (%d, %d) = %08x, colIdx = %d", (char) ch, x, y, pixel, colIdx);
						}
						charWidthf[ch] = (128.0f * (float) (colIdx + 1)) / (float) width + 1.0f;
						if (showLines) {
							for (int i = 0; i < rowHeight; i++) {
								y = row * rowHeight + i;
								for (int j = 0; j < Math.max(colWidth / 16, 1); j++) {
									image.setRGB(x + j, y, (i == rowIdx ? 0xff0000ff : 0xffff0000));
									image.setRGB(col * colWidth + j, y, 0xff00ff00);
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
			if (printThis(ch)) {
				MCPatcherUtils.log("charWidth['%c'] = %f", (char) ch, charWidthf[ch]);
			}
		}
		return charWidthf;
	}

	private static boolean isOpaque(int pixel) {
		for (int i : SPACERS) {
			if (pixel == i) {
				return false;
			}
		}
		return (pixel & 0xff) > 0;
	}

	private static boolean printThis(int ch) {
		return "ABCDEF abcdef".indexOf(ch) >= 0;
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

		MCPatcherUtils.log("reading character widths from %s", textFile);
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
							MCPatcherUtils.log("	setting charWidthf[%d] to %f", ch, width);
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