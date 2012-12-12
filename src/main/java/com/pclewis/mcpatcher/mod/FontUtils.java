package com.pclewis.mcpatcher.mod;

import com.pclewis.mcpatcher.MCLogger;
import com.pclewis.mcpatcher.MCPatcherUtils;
import com.pclewis.mcpatcher.TexturePackAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;

import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Properties;

public class FontUtils {
    private static final MCLogger logger = MCLogger.getLogger(MCPatcherUtils.HD_FONT);

    private static final int ROWS = 16;
    private static final int COLS = 16;

    public static final char[] AVERAGE_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123467890".toCharArray();
    public static final int[] SPACERS = new int[]{0x02028bfe, 0x02808080, 0x0dffffff};

    private static final boolean showLines = false;

    static {
        TexturePackAPI.loadFontFromTexturePack = true;

        TexturePackAPI.ChangeHandler.register(new TexturePackAPI.ChangeHandler(MCPatcherUtils.HD_FONT, 2) {
            @Override
            protected void onChange() {
                Minecraft minecraft = MCPatcherUtils.getMinecraft();
                setFontRenderer(minecraft, minecraft.fontRenderer, "/font/default.png");
                if (minecraft.standardGalacticFontRenderer != minecraft.fontRenderer) {
                    setFontRenderer(minecraft, minecraft.standardGalacticFontRenderer, "/font/alternate.png");
                }
            }
        });
    }

    private static void setFontRenderer(Minecraft minecraft, FontRenderer fontRenderer, String filename) {
        if (fontRenderer != null) {
            boolean saveUnicode = fontRenderer.unicodeFlag;
            fontRenderer.initialize(minecraft.gameSettings, filename, minecraft.renderEngine);
            fontRenderer.unicodeFlag = saveUnicode;
        }
    }

    public static float[] computeCharWidths(FontRenderer fontRenderer, String filename, BufferedImage image, int[] rgb, int[] charWidth) {
        if (fontRenderer.FONT_HEIGHT == 0) {
            fontRenderer.FONT_HEIGHT = 8;
        }
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
                            logger.finer("'%c' pixel (%d, %d) = %08x, colIdx = %d", (char) ch, x, y, pixel, colIdx);
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
            } else if (charWidthf[ch] >= 7.99f) {
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
            if (printThis(ch)) {
                logger.finer("charWidth['%c'] = %f", (char) ch, charWidthf[ch]);
            }
        }
        return charWidthf;
    }

    private static float getCharWidthf(FontRenderer fontRenderer, char ch) {
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
            boolean isLink = false;
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                float cWidth = getCharWidthf(fontRenderer, c);
                if (cWidth < 0.0f && i < s.length() - 1) {
                    i++;
                    c = s.charAt(i);
                    if (c == 'l' || c == 'L') {
                        isLink = true;
                    } else if (c == 'r' || c == 'R' || (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F')) {
                        isLink = false;
                    }
                    cWidth = getCharWidthf(fontRenderer, c);
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
        return ((pixel >> 24) & 0xf0) > 0;
    }

    private static boolean printThis(int ch) {
        return "ABCDEF abcdef".indexOf(ch) >= 0;
    }

    private static float defaultSpaceWidth(float[] charWidthf) {
        if (TexturePackAPI.isDefaultTexturePack()) {
            return 4.0f;
        }
        float sum = 0.0f;
        int n = 0;
        for (char ch : AVERAGE_CHARS) {
            if (charWidthf[ch] > 0.0f) {
                sum += charWidthf[ch];
                n++;
            }
        }
        if (n > 0) {
            return sum / (float) n * 7.0f / 12.0f;
        } else {
            return 4.0f;
        }
    }

    private static void getCharWidthOverrides(String font, float[] charWidthf, boolean[] isOverride) {
        String textFile = font.replace(".png", ".properties");
        Properties props = TexturePackAPI.getProperties(textFile);
        if (props == null) {
            return;
        }
        logger.fine("reading character widths from %s", textFile);
        for (Map.Entry entry : props.entrySet()) {
            String key = entry.getKey().toString().trim();
            String value = entry.getValue().toString().trim();
            if (key.matches("^width\\.\\d+$") && !value.equals("")) {
                try {
                    int ch = Integer.parseInt(key.substring(6));
                    float width = Float.parseFloat(value);
                    if (ch >= 0 && ch < charWidthf.length) {
                        logger.finer("setting charWidthf[%d] to %f", ch, width);
                        charWidthf[ch] = width;
                        isOverride[ch] = true;
                    }
                } catch (NumberFormatException e) {
                }
            }
        }
    }
}
