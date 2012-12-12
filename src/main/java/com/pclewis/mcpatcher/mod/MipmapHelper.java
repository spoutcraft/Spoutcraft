package com.pclewis.mcpatcher.mod;

import com.pclewis.mcpatcher.MCLogger;
import com.pclewis.mcpatcher.MCPatcherUtils;
import com.pclewis.mcpatcher.TexturePackAPI;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.TextureFX;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

public class MipmapHelper {
    private static final MCLogger logger = MCLogger.getLogger("Mipmap");

    private static final boolean mipmapSupported;
    private static final boolean mipmapEnabled = MCPatcherUtils.getBoolean(MCPatcherUtils.HD_TEXTURES, "mipmap", true);
    private static final int maxMipmapLevel = MCPatcherUtils.getInt(MCPatcherUtils.HD_TEXTURES, "maxMipmapLevel", 4);
    private static final boolean useMipmap;
    private static final int mipmapAlignment = (1 << MCPatcherUtils.getInt(MCPatcherUtils.HD_TEXTURES, "mipmapAlignment", 3)) - 1;

    private static final boolean anisoSupported;
    private static final int anisoLevel;
    private static final int anisoMax;

    private static final boolean lodSupported;
    private static int lodBias;

    public static int currentLevel;

    private static final HashMap<String, Integer> mipmapType = new HashMap<String, Integer>();
    private static final int MIPMAP_NONE = 0;
    private static final int MIPMAP_BASIC = 1;
    private static final int MIPMAP_ALPHA = 2;

    static {
        mipmapSupported = GLContext.getCapabilities().OpenGL12;
        useMipmap = mipmapSupported && mipmapEnabled && maxMipmapLevel > 0;

        anisoSupported = GLContext.getCapabilities().GL_EXT_texture_filter_anisotropic;
        if (anisoSupported) {
            anisoMax = (int) GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT);
            checkGLError("glGetFloat(GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT)");
            anisoLevel = Math.max(Math.min(MCPatcherUtils.getInt(MCPatcherUtils.HD_TEXTURES, "anisotropicFiltering", 1), anisoMax), 1);
        } else {
            anisoMax = anisoLevel = 1;
        }

        lodSupported = GLContext.getCapabilities().GL_EXT_texture_lod_bias;
        if (lodSupported) {
            lodBias = MCPatcherUtils.getInt(MCPatcherUtils.HD_TEXTURES, "lodBias", 0);
        }

        logger.config("mipmap: supported=%s, enabled=%s, level=%d", mipmapSupported, mipmapEnabled, maxMipmapLevel);
        logger.config("anisotropic: supported=%s, level=%d, max=%d", anisoSupported, anisoLevel, anisoMax);
        logger.config("lod bias: supported=%s, bias=%d", lodSupported, lodBias);
    }

    public static void setupTexture(RenderEngine renderEngine, BufferedImage image, int texture, String currentTexture) {
        currentLevel = 0;
        try {
            if (texture < 0 || image == null) {
                return;
            }
            int mipmap = 0;
            BufferedImage origImage = image;
            int type = getMipmapType(currentTexture, image);
            if (type >= MIPMAP_BASIC) {
                mipmap = getMipmapLevel(currentTexture, image);
                if (mipmap > 0) {
                    logger.fine("generating %d mipmaps for %s, alpha=%s", mipmap, currentTexture, type >= MIPMAP_ALPHA);
                } else {
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, mipmap);
                    type = MIPMAP_NONE;
                    if (currentTexture != null) {
                        mipmapType.put(currentTexture, type);
                    }
                }
            }
            while (true) {
                if (currentLevel == 1) {
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_LINEAR);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, mipmap);
                    checkGLError("set GL_TEXTURE_MAX_LEVEL = %d", mipmap);
                    if (anisoSupported && anisoLevel > 1) {
                        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, anisoLevel);
                        checkGLError("set GL_TEXTURE_MAX_ANISOTROPY_EXT = %f", anisoLevel);
                    }
                    setLOD(0);
                }
                renderEngine.setupTexture(image, texture);
                checkGLError("setupTexture %d", currentLevel);
                if (++currentLevel > mipmap) {
                    break;
                }
                origImage = scaleHalf(origImage);
                if (type >= MIPMAP_ALPHA) {
                    image = origImage;
                } else {
                    image = new BufferedImage(origImage.getColorModel(), origImage.copyData(null), origImage.getColorModel().isAlphaPremultiplied(), null);
                    resetOnOffTransparency(image);
                }
                logger.finest("%s mipmap level %d (%dx%d)", currentTexture, currentLevel, image.getWidth(), image.getHeight());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            currentLevel = 0;
        }
    }

    public static void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, ByteBuffer pixels, TextureFX textureFX) {
        GL11.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
        if (textureFX.tileImage == 0) {
            update("/terrain.png", xoffset, yoffset, width, height, pixels);
        }
    }

    static void update(String texture, int x, int y, int w, int h, ByteBuffer imageData) {
        int filter = GL11.glGetTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER);
        if (filter != GL11.GL_NEAREST_MIPMAP_LINEAR && filter != GL11.GL_NEAREST_MIPMAP_NEAREST) {
            return;
        }
        int mipmaps = GL11.glGetTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL);
        for (int i = 1; i <= mipmaps && ((x | y | w | h) & mipmapAlignment) == 0; i++) {
            ByteBuffer newImage = ByteBuffer.allocateDirect(w * h);
            scaleHalf(imageData.asIntBuffer(), w, h, newImage.asIntBuffer());
            x >>= 1;
            y >>= 1;
            w >>= 1;
            h >>= 1;
            int width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, i, GL11.GL_TEXTURE_WIDTH);
            int height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, i, GL11.GL_TEXTURE_HEIGHT);
            if (width < 0 || height < 0) {
                break;
            }
            GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, i, x, y, w, h, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) newImage.position(0));
            checkGLError("glTexSubImage2D(%d, %d, %d, %d, %d, %d), width %d, height %d, mipmaps %d", i, x, y, w, h, newImage.limit(), width, height, mipmaps);
            imageData = newImage;
        }
    }

    static void setLOD(int adj) {
        if (lodSupported) {
            lodBias += adj;
            GL11.glTexEnvi(EXTTextureLODBias.GL_TEXTURE_FILTER_CONTROL_EXT, EXTTextureLODBias.GL_TEXTURE_LOD_BIAS_EXT, lodBias);
            checkGLError("set GL_TEXTURE_LOD_BIAS_EXT = %d", lodBias);
            if (adj != 0) {
                logger.config("new lod bias: %d", lodBias);
            }
        }
    }

    static void reset() {
        mipmapType.clear();
        forceMipmapType("/terrain.png", MIPMAP_BASIC);
    }

    private static int getMipmapType(String texture, BufferedImage image) {
        if (!useMipmap || texture == null) {
            return MIPMAP_NONE;
        } else if (mipmapType.containsKey(texture)) {
            return mipmapType.get(texture);
        } else if (texture.startsWith("%") ||
            texture.startsWith("##") ||
            texture.startsWith("/achievement/") ||
            texture.startsWith("/environment/") ||
            texture.startsWith("/font/") ||
            texture.startsWith("/gui/") ||
            texture.startsWith("/misc/") ||
            texture.startsWith("/terrain/") ||
            texture.startsWith("/title/")) {
            return MIPMAP_NONE;
        } else if (image == null) {
            return MIPMAP_BASIC;
        } else {
            int width = image.getWidth();
            int height = image.getHeight();
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int pixel = image.getRGB(i, j);
                    int alpha = pixel >>> 24;
                    if (alpha > 0x1a && alpha < 0xe5) {
                        logger.finer("%s alpha transparency? yes, by pixel search", texture);
                        mipmapType.put(texture, MIPMAP_ALPHA);
                        return MIPMAP_ALPHA;
                    }
                }
            }
            logger.finer("%s alpha transparency? no, by pixel search", texture);
            mipmapType.put(texture, MIPMAP_BASIC);
            return MIPMAP_BASIC;
        }
    }

    static void forceMipmapType(String texture, int type) {
        if (!useMipmap) {
            return;
        }
        boolean reload = false;
        if (mipmapType.containsKey(texture)) {
            reload = mipmapType.get(texture) != type;
            if (!reload) {
                return;
            }
        }
        mipmapType.put(texture, type);
        if (reload) {
            logger.finer("force %s -> %d (reloading)", texture, type);
            int id = TexturePackAPI.getTextureIfLoaded(texture);
            if (id >= 0) {
                setupTexture(MCPatcherUtils.getMinecraft().renderEngine, TexturePackAPI.getImage(texture), id, texture);
            }
        } else {
            logger.finer("force %s -> %d", texture, type);
        }
    }

    private static int getMipmapLevel(String texture, BufferedImage image) {
        int size = Math.min(image.getWidth(), image.getHeight());
        int minSize = getMinSize(texture);
        int mipmap;
        for (mipmap = 0; size >= minSize && mipmap < maxMipmapLevel; size >>= 1, mipmap++) {
        }
        return mipmap;
    }

    private static int getMinSize(String texture) {
        return texture.equals("/terrain.png") || texture.startsWith("/ctm/") ? 32 : 2;
    }

    private static void resetOnOffTransparency(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int pixel = image.getRGB(i, j);
                int alpha = pixel >>> 24;
                if (alpha <= 0x7f) {
                    pixel &= 0x00ffffff;
                } else {
                    pixel |= 0xff000000;
                }
                image.setRGB(i, j, pixel);
            }
        }
    }

    private static void scaleHalf(IntBuffer in, int w, int h, IntBuffer out) {
        for (int i = 0; i < w / 2; i++) {
            for (int j = 0; j < h / 2; j++) {
                int k = w * 2 * j + 2 * i;
                int pixel00 = in.get(k);
                int pixel01 = in.get(k + 1);
                int pixel10 = in.get(k + w);
                int pixel11 = in.get(k + w + 1);
                out.put(w / 2 * j + i, average4RGBA(pixel00, pixel01, pixel10, pixel11));
            }
        }
    }

    private static BufferedImage scaleHalf(BufferedImage in) {
        int w = in.getWidth();
        int h = in.getHeight();
        BufferedImage out = new BufferedImage(w / 2, h / 2, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < w / 2; i++) {
            for (int j = 0; j < h / 2; j++) {
                int pixel00 = Integer.rotateLeft(in.getRGB(2 * i, 2 * j), 8);
                int pixel01 = Integer.rotateLeft(in.getRGB(2 * i + 1, 2 * j), 8);
                int pixel10 = Integer.rotateLeft(in.getRGB(2 * i, 2 * j + 1), 8);
                int pixel11 = Integer.rotateLeft(in.getRGB(2 * i + 1, 2 * j + 1), 8);
                out.setRGB(i, j, Integer.rotateRight(average4RGBA(pixel00, pixel01, pixel10, pixel11), 8));
            }
        }
        return out;
    }

    private static int average4RGBA(int pixel00, int pixel01, int pixel10, int pixel11) {
        int a00 = pixel00 & 0xff;
        int a01 = pixel01 & 0xff;
        int a10 = pixel10 & 0xff;
        int a11 = pixel11 & 0xff;
        int a = a00 + a01 + a10 + a11;
        if (a == 0 || a == 0x3fc) {
            return average2RGBA(average2RGBA(pixel00, pixel11), average2RGBA(pixel01, pixel10)) | (a >>> 2);
        } else {
            int pixel = a >> 2;
            for (int i = 8; i < 32; i += 8) {
                int average = (a00 * ((pixel00 >> i) & 0xff) + a01 * ((pixel01 >> i) & 0xff) +
                    a10 * ((pixel10 >> i) & 0xff) + a11 * ((pixel11 >> i) & 0xff)) / a;
                pixel |= (average << i);
            }
            return pixel;
        }
    }

    private static int average2RGBA(int a, int b) {
        return ((a & 0xfefefefe) >>> 1) + ((b & 0xfefefefe) >>> 1);
    }

    private static void checkGLError(String format, Object... params) {
        int error = GL11.glGetError();
        if (error != 0) {
            String message = GLU.gluErrorString(error) + ": " + String.format(format, params);
            new RuntimeException(message).printStackTrace();
        }
    }
}
