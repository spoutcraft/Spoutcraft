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
                float cWidth = fontRenderer.getCharWidth(c);
                if (cWidth < 0.0f && i < s.length() - 1) {
                    c = s.charAt(i + 1);
                    if (c == 'l' || c == 'L') {
                        isLink = true;
                    } else if (c == 'r' || c == 'R') {
                        isLink = false;
                    }
                    cWidth = fontRenderer.getCharWidth(c);
                }
                totalWidth += cWidth;
                if (isLink) {
                    totalWidth++;
                }
            }
        }
        return totalWidth;
    }

    private static boolean isOpaque(int pixel) {
        for (int i : SPACERS) {
            if (pixel == i) {
                return false;
            }
        }
        return (pixel & 0xff) > 0;
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