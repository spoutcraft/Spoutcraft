package com.pclewis.mcpatcher.mod;

import com.pclewis.mcpatcher.MCLogger;
import com.pclewis.mcpatcher.MCPatcherUtils;
import com.pclewis.mcpatcher.TexturePackAPI;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.TextureFX;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.*;

public class MipmapHelper {
    private static final MCLogger logger = MCLogger.getLogger("Mipmap");

    private static final String MIPMAP_PROPERTIES = "/mipmap.properties";

    private static final boolean mipmapSupported;
    private static final boolean mipmapEnabled = MCPatcherUtils.getBoolean(MCPatcherUtils.HD_TEXTURES, "mipmap", false);
    private static final int maxMipmapLevel = MCPatcherUtils.getInt(MCPatcherUtils.HD_TEXTURES, "maxMipmapLevel", 3);
    private static final boolean useMipmap;
    private static final int mipmapAlignment = (1 << MCPatcherUtils.getInt(MCPatcherUtils.HD_TEXTURES, "mipmapAlignment", 3)) - 1;

    private static final boolean anisoSupported;
    private static final int anisoLevel;
    private static final int anisoMax;

    private static final boolean lodSupported;
    private static int lodBias;

    private static final HashMap<String, Reference<BufferedImage>> imagePool = new HashMap<String, Reference<BufferedImage>>();
    private static final HashMap<Integer, Reference<ByteBuffer>> bufferPool = new HashMap<Integer, Reference<ByteBuffer>>();

    private static int bgColorFix;

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

    public static void setupTexture(RenderEngine renderEngine, BufferedImage image, int texture, String textureName) {
        if (texture < 0 || image == null) {
            return;
        }
        long s1 = System.currentTimeMillis();
        ArrayList<BufferedImage> mipmapImages = getMipmapsForTexture(image, textureName);
        long s2 = System.currentTimeMillis();
        setupTextureMipmaps(renderEngine, mipmapImages, texture, textureName);
        long s3 = System.currentTimeMillis();
        if (mipmapImages.size() > 1) {
            logger.finer("%s: generate %dms, setup %dms, total %dms", textureName, s2 - s1, s3 - s2, s3 - s1);
        }
    }

    private static ArrayList<BufferedImage> getMipmapsForTexture(BufferedImage image, String textureName) {
        ArrayList<BufferedImage> mipmapImages = new ArrayList<BufferedImage>();
        mipmapImages.add(image);
        int type = getMipmapType(textureName, mipmapImages);
        if (type < MIPMAP_BASIC) {
            return mipmapImages;
        }
        int width = image.getWidth();
        int height = image.getHeight();
        if (getCustomMipmaps(mipmapImages, textureName, width, height)) {
            logger.fine("using %d custom mipmaps for %s", mipmapImages.size() - 1, textureName);
            return mipmapImages;
        }
        int mipmaps = getMipmapLevels(textureName, image);
        if (mipmaps <= 0) {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, mipmaps);
            type = MIPMAP_NONE;
            if (textureName != null) {
                mipmapType.put(textureName, type);
            }
            return mipmapImages;
        }
        logger.fine("generating %d mipmaps for %s, alpha=%s", mipmaps, textureName, type >= MIPMAP_ALPHA);
        image = convertToARGB(image);
        mipmapImages.set(0, image);
        int scale = 1 << bgColorFix;
        int gcd = gcd(width, height);
        if (bgColorFix > 0 && gcd % scale == 0 && ((gcd / scale) & (gcd / scale - 1)) == 0) {
            long s1 = System.currentTimeMillis();
            BufferedImage scaledImage = mipmapImages.get(mipmapImages.size() - 1);
            while (gcd(scaledImage.getWidth(), scaledImage.getHeight()) > scale) {
                scaledImage = scaleHalf(scaledImage);
            }
            long s2 = System.currentTimeMillis();
            setBackgroundColor(image, scaledImage);
            long s3 = System.currentTimeMillis();
            logger.finer("bg fix: scaling %dms, setbg %dms", s2 - s1, s3 - s2);
        }
        BufferedImage origImage = image;
        for (int i = 0; i < mipmaps; i++) {
            origImage = scaleHalf(origImage);
            if (type >= MIPMAP_ALPHA) {
                image = origImage;
            } else {
                image = getPooledImage(origImage.getWidth(), origImage.getHeight(), 1);
                origImage.copyData(image.getRaster());
                resetOnOffTransparency(image);
            }
            mipmapImages.add(image);
        }
        return mipmapImages;
    }

    private static void setupTextureMipmaps(RenderEngine renderEngine, ArrayList<BufferedImage> mipmapImages, int texture, String textureName) {
        try {
            int mipmaps = mipmapImages.size() - 1;
            for (currentLevel = 0; currentLevel <= mipmaps; currentLevel++) {
                if (currentLevel == 1) {
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_LINEAR);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, mipmaps);
                    checkGLError("set GL_TEXTURE_MAX_LEVEL = %d", mipmaps);
                    if (anisoSupported && anisoLevel > 1) {
                        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, anisoLevel);
                        checkGLError("set GL_TEXTURE_MAX_ANISOTROPY_EXT = %f", anisoLevel);
                    }
                    if (lodSupported) {
                        GL11.glTexEnvi(EXTTextureLODBias.GL_TEXTURE_FILTER_CONTROL_EXT, EXTTextureLODBias.GL_TEXTURE_LOD_BIAS_EXT, lodBias);
                        checkGLError("set GL_TEXTURE_LOD_BIAS_EXT = %d", lodBias);
                    }
                }
                BufferedImage image = mipmapImages.get(currentLevel);
                renderEngine.setupTexture(image, texture);
                checkGLError("setupTexture %s#%d", textureName, currentLevel);
                if (currentLevel > 0) {
                    logger.finest("%s mipmap level %d (%dx%d)", textureName, currentLevel, image.getWidth(), image.getHeight());
                }
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
            ByteOrder saveOrder = pixels.order();
            pixels.order(ByteOrder.BIG_ENDIAN);
            update("/terrain.png", xoffset, yoffset, width, height, pixels);
            pixels.order(saveOrder);
        }
    }

    public static void update(String texture, int x, int y, int w, int h, ByteBuffer imageData) {
        int mipmaps = getMipmapLevels();
        for (int i = 1; i <= mipmaps && ((x | y | w | h) & mipmapAlignment) == 0; i++) {
            ByteBuffer newImage = getPooledBuffer(w * h);
            scaleHalf(imageData.asIntBuffer(), w, h, newImage.asIntBuffer(), 0);
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

    static void reset() {
        bgColorFix = 4;
        mipmapType.clear();
        forceMipmapType("/terrain.png", MIPMAP_BASIC);
        Properties properties = TexturePackAPI.getProperties(MIPMAP_PROPERTIES);
        if (properties != null) {
            bgColorFix = MCPatcherUtils.getIntProperty(properties, "bgColorFix", 4);
            for (Map.Entry entry : properties.entrySet()) {
                if (entry.getKey() instanceof String && entry.getValue() instanceof String) {
                    String key = ((String) entry.getKey()).trim();
                    String value = ((String) entry.getValue()).trim().toLowerCase();
                    if (key.startsWith("/")) {
                        if (value.equals("none")) {
                            mipmapType.put(key, MIPMAP_NONE);
                        } else if (value.equals("basic") || value.equals("opaque")) {
                            mipmapType.put(key, MIPMAP_BASIC);
                        } else if (value.equals("alpha")) {
                            mipmapType.put(key, MIPMAP_ALPHA);
                        } else {
                            logger.warning("%s: unknown value '%s' for %s", MIPMAP_PROPERTIES, value, key);
                        }
                    }
                }
            }
        }
    }

    private static boolean getCustomMipmaps(ArrayList<BufferedImage> mipmaps, String texture, int baseWidth, int baseHeight) {
        boolean added = false;
        if (useMipmap && texture != null) {
            for (int i = 1; baseWidth > 0 && baseHeight > 0 && i <= maxMipmapLevel; i++) {
                baseWidth >>>= 1;
                baseHeight >>>= 1;
                String name = texture.replace(".png", "-mipmap" + i + ".png");
                BufferedImage image = TexturePackAPI.getImage(name);
                if (image == null) {
                    break;
                }
                int width = image.getWidth();
                int height = image.getHeight();
                if (width == baseWidth && height == baseHeight) {
                    mipmaps.add(image);
                    added = true;
                } else {
                    logger.warning("%s has wrong size %dx%d (expecting %dx%d)", name, width, height, baseWidth, baseHeight);
                    break;
                }
            }
        }
        return added;
    }

    private static int getMipmapType(String texture, ArrayList<BufferedImage> mipmapImages) {
        BufferedImage image = mipmapImages.get(0);
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
            image = convertToARGB(image);
            mipmapImages.set(0, image);
            IntBuffer buffer = getARGBAsIntBuffer(image);
            for (int i = 0; i < buffer.limit(); i++) {
                int alpha = buffer.get(i) >>> 24;
                if (alpha > 0x1a && alpha < 0xe5) {
                    logger.finer("%s alpha transparency? yes, by pixel search", texture);
                    mipmapType.put(texture, MIPMAP_ALPHA);
                    return MIPMAP_ALPHA;
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

    static int getMipmapLevels() {
        int filter = GL11.glGetTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER);
        if (filter != GL11.GL_NEAREST_MIPMAP_LINEAR && filter != GL11.GL_NEAREST_MIPMAP_NEAREST) {
            return 0;
        }
        return GL11.glGetTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL);
    }

    private static int gcd(int a, int b) {
        return BigInteger.valueOf(a).gcd(BigInteger.valueOf(b)).intValue();
    }

    private static int getMipmapLevels(String texture, BufferedImage image) {
        return getMipmapLevels(texture, image.getWidth(), image.getHeight());
    }

    private static int getMipmapLevels(String texture, int width, int height) {
        int size = gcd(width, height);
        int minSize = getMinSize(texture);
        int mipmap;
        for (mipmap = 0; size >= minSize && ((size & 1) == 0) && mipmap < maxMipmapLevel; size >>= 1, mipmap++) {
        }
        return mipmap;
    }

    private static int getMinSize(String texture) {
        return texture.equals("/terrain.png") || texture.startsWith("/ctm/") ? 32 : 2;
    }

    private static BufferedImage getPooledImage(int width, int height, int index) {
        String key = String.format("%dx%d#%d", width, height, index);
        Reference<BufferedImage> ref = imagePool.get(key);
        BufferedImage image = (ref == null ? null : ref.get());
        if (image == null) {
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            imagePool.put(key, new SoftReference<BufferedImage>(image));
        }
        return image;
    }

    private static ByteBuffer getPooledBuffer(int size) {
        Reference<ByteBuffer> ref = bufferPool.get(size);
        ByteBuffer buffer = (ref == null ? null : ref.get());
        if (buffer == null) {
            buffer = ByteBuffer.allocateDirect(size);
            bufferPool.put(size, new SoftReference<ByteBuffer>(buffer));
        }
        buffer.position(0);
        return buffer;
    }

    private static BufferedImage convertToARGB(BufferedImage image) {
        if (image == null) {
            return null;
        } else if (image.getType() == BufferedImage.TYPE_INT_ARGB) {
            return image;
        } else {
            int width = image.getWidth();
            int height = image.getHeight();
            logger.fine("converting %dx%d image to ARGB", width, height);
            BufferedImage newImage = getPooledImage(width, height, 0);
            Graphics2D graphics = newImage.createGraphics();
            Arrays.fill(getARGBAsIntBuffer(newImage).array(), 0);
            graphics.drawImage(image, 0, 0, null);
            return newImage;
        }
    }

    private static IntBuffer getARGBAsIntBuffer(BufferedImage image) {
        DataBuffer buffer = image.getRaster().getDataBuffer();
        if (buffer instanceof DataBufferInt) {
            return IntBuffer.wrap(((DataBufferInt) buffer).getData());
        } else if (buffer instanceof DataBufferByte) {
            return ByteBuffer.wrap(((DataBufferByte) buffer).getData()).order(ByteOrder.BIG_ENDIAN).asIntBuffer();
        } else {
            int width = image.getWidth();
            int height = image.getHeight();
            int[] pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);
            return IntBuffer.wrap(pixels);
        }
    }

    private static void setBackgroundColor(BufferedImage image, BufferedImage scaledImage) {
        int width = image.getWidth();
        int height = image.getHeight();
        int scale = width / scaledImage.getWidth();
        IntBuffer buffer = getARGBAsIntBuffer(image);
        IntBuffer scaledBuffer = getARGBAsIntBuffer(scaledImage);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int k = width * j + i;
                int pixel = buffer.get(k);
                if ((pixel & 0xff000000) == 0) {
                    pixel = scaledBuffer.get((j / scale) * (width / scale) + i / scale);
                    buffer.put(k, pixel & 0x00ffffff);
                }
            }
        }
    }

    private static void resetOnOffTransparency(BufferedImage image) {
        IntBuffer rgb = getARGBAsIntBuffer(image);
        for (int i = 0; i < rgb.limit(); i++) {
            int pixel = rgb.get(i);
            if (pixel >>> 24 < 0x7f) {
                rgb.put(i, pixel & 0x00ffffff);
            } else {
                rgb.put(i, pixel | 0xff000000);
            }
        }
    }

    static void scaleHalf(IntBuffer in, int w, int h, IntBuffer out, int rotate) {
        for (int i = 0; i < w / 2; i++) {
            for (int j = 0; j < h / 2; j++) {
                int k = w * 2 * j + 2 * i;
                int pixel00 = in.get(k);
                int pixel01 = in.get(k + 1);
                int pixel10 = in.get(k + w);
                int pixel11 = in.get(k + w + 1);
                if (rotate != 0) {
                    pixel00 = Integer.rotateLeft(pixel00, rotate);
                    pixel01 = Integer.rotateLeft(pixel01, rotate);
                    pixel10 = Integer.rotateLeft(pixel10, rotate);
                    pixel11 = Integer.rotateLeft(pixel11, rotate);
                }
                int pixel = average4RGBA(pixel00, pixel01, pixel10, pixel11);
                if (rotate != 0) {
                    pixel = Integer.rotateRight(pixel, rotate);
                }
                out.put(w / 2 * j + i, pixel);
            }
        }
    }

    private static BufferedImage scaleHalf(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage scaledImage = getPooledImage(width / 2, height / 2, 0);
        scaleHalf(getARGBAsIntBuffer(image), width, height, getARGBAsIntBuffer(scaledImage), 8);
        return scaledImage;
    }

    private static int average4RGBA(int pixel00, int pixel01, int pixel10, int pixel11) {
        int a00 = pixel00 & 0xff;
        int a01 = pixel01 & 0xff;
        int a10 = pixel10 & 0xff;
        int a11 = pixel11 & 0xff;
        switch ((a00 << 24) | (a01 << 16) | (a10 << 8) | a11) {
            case 0xff000000:
                return pixel00;

            case 0x00ff0000:
                return pixel01;

            case 0x0000ff00:
                return pixel10;

            case 0x000000ff:
                return pixel11;

            case 0xffff0000:
                return average2RGBA(pixel00, pixel01);

            case 0xff00ff00:
                return average2RGBA(pixel00, pixel10);

            case 0xff0000ff:
                return average2RGBA(pixel00, pixel11);

            case 0x00ffff00:
                return average2RGBA(pixel01, pixel10);

            case 0x00ff00ff:
                return average2RGBA(pixel01, pixel11);

            case 0x0000ffff:
                return average2RGBA(pixel10, pixel11);

            case 0x00000000:
            case 0xffffffff:
                return average2RGBA(average2RGBA(pixel00, pixel11), average2RGBA(pixel01, pixel10));

            default:
                int a = a00 + a01 + a10 + a11;
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
        return (((a & 0xfefefefe) >>> 1) + ((b & 0xfefefefe) >>> 1)) | (a & b & 0x01010101);
    }

    private static void checkGLError(String format, Object... params) {
        int error = GL11.glGetError();
        if (error != 0) {
            String message = GLU.gluErrorString(error) + ": " + String.format(format, params);
            new RuntimeException(message).printStackTrace();
        }
    }
}
