package com.prupe.mcpatcher.hd;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.TexturePackAPI;

import net.minecraft.src.ResourceLocation;

public class MipmapHelper {
	private static final MCLogger logger = MCLogger.getLogger("Mipmap");
	private static final ResourceLocation MIPMAP_PROPERTIES = TexturePackAPI.newMCPatcherResourceLocation("mipmap.properties");
	private static final int TEX_FORMAT = 32993;
	private static final int TEX_DATA_TYPE = 33639;
	private static final int MIN_ALPHA = 26;
	private static final int MAX_ALPHA = 229;
	private static final boolean mipmapSupported = GLContext.getCapabilities().OpenGL12;
	static final boolean mipmapEnabled = Config.getBoolean("Extended HD", "mipmap", false);
	static final int maxMipmapLevel = Config.getInt("Extended HD", "maxMipmapLevel", 3);
	private static final boolean useMipmap = mipmapSupported && mipmapEnabled && maxMipmapLevel > 0;
	private static final int mipmapAlignment = (1 << Config.getInt("Extended HD", "mipmapAlignment", 3)) - 1;
	private static final boolean anisoSupported = GLContext.getCapabilities().GL_EXT_texture_filter_anisotropic;
	static final int anisoLevel;
	private static final int anisoMax;
	private static final boolean lodSupported;
	private static final int lodBias;
	private static final Map<String, Reference<BufferedImage>> imagePool = new HashMap();
	private static final Map<Integer, Reference<ByteBuffer>> bufferPool = new HashMap();
	private static final Map<String, Boolean> mipmapType = new HashMap();

	private static void setupTexture(int width, int height, boolean blur, boolean clamp, String textureName) {
		int mipmaps = useMipmapsForTexture(textureName) ? getMipmapLevels(width, height, 1) : 0;
		logger.finer("setupTexture(%s) %dx%d %d mipmaps", new Object[] {textureName, Integer.valueOf(width), Integer.valueOf(height), Integer.valueOf(mipmaps)});
		int magFilter = blur ? 9729 : 9728;
		int minFilter = mipmaps > 0 ? 9986 : magFilter;
		int wrap = clamp ? 10496 : 10497;

		if (mipmaps > 0) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, mipmaps);
			checkGLError("%s: set GL_TEXTURE_MAX_LEVEL = %d", new Object[] {textureName, Integer.valueOf(mipmaps)});

			if (anisoSupported && anisoLevel > 1) {
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, 34046, (float)anisoLevel);
				checkGLError("%s: set GL_TEXTURE_MAX_ANISOTROPY_EXT = %f", new Object[] {textureName, Integer.valueOf(anisoLevel)});
			}

			if (lodSupported) {
				GL11.glTexEnvi(GL14.GL_TEXTURE_FILTER_CONTROL, GL14.GL_TEXTURE_LOD_BIAS, lodBias);
				checkGLError("%s: set GL_TEXTURE_LOD_BIAS_EXT = %d", new Object[] {textureName, Integer.valueOf(lodBias)});
			}
		}

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, minFilter);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, magFilter);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, wrap);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, wrap);

		for (int level = 0; level <= mipmaps; ++level) {
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, level, GL11.GL_RGBA, width, height, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, (IntBuffer)null);
			checkGLError("%s: glTexImage2D %dx%d level %d", new Object[] {textureName, Integer.valueOf(width), Integer.valueOf(height), Integer.valueOf(level)});
			width >>= 1;
			height >>= 1;
		}
	}

	public static void setupTexture(int[] rgb, int width, int height, int x, int y, boolean blur, boolean clamp, String textureName) {
		setupTexture(width, height, blur, clamp, textureName);
		copySubTexture(rgb, width, height, x, y, textureName);
	}

	public static int setupTexture(int glTexture, BufferedImage image, boolean blur, boolean clamp, ResourceLocation textureName) {
		int width = image.getWidth();
		int height = image.getHeight();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, glTexture);
		logger.finer("setupTexture(%s, %d, %dx%d, %s, %s)", new Object[] {textureName, Integer.valueOf(glTexture), Integer.valueOf(width), Integer.valueOf(height), Boolean.valueOf(blur), Boolean.valueOf(clamp)});
		int[] rgb = new int[width * height];
		image.getRGB(0, 0, width, height, rgb, 0, width);
		setupTexture(rgb, width, height, 0, 0, blur, clamp, textureName.func_110623_a());
		return glTexture;
	}

	public static void setupTexture(int glTexture, int width, int height, String textureName) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, glTexture);
		logger.finer("setupTexture(tilesheet %s, %d, %dx%d)", new Object[] {textureName, Integer.valueOf(glTexture), Integer.valueOf(width), Integer.valueOf(height)});
		setupTexture(width, height, false, false, textureName);
	}

	public static void copySubTexture(int[] rgb, int width, int height, int x, int y, String textureName) {
		IntBuffer buffer = getPooledBuffer(width * height * 4).asIntBuffer();
		buffer.put(rgb).position(0);
		int mipmaps = getMipmapLevelsForCurrentTexture();
		logger.finest("copySubTexture %s %d,%d %dx%d %d mipmaps", new Object[] {textureName, Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(width), Integer.valueOf(height), Integer.valueOf(mipmaps)});

		for (int level = 0; width > 0 && height > 0; height >>= 1) {
			GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, level, x, y, width, height, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, buffer);
			checkGLError("%s: glTexSubImage2D(%d, %d, %d, %d, %d)", new Object[] {textureName, Integer.valueOf(level), Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(width), Integer.valueOf(height)});

			if (level >= mipmaps) {
				break;
			}

			IntBuffer newBuffer = getPooledBuffer(width * height).asIntBuffer();
			scaleHalf(buffer, width, height, newBuffer, 0);
			buffer = newBuffer;
			++level;
			x >>= 1;
			y >>= 1;
			width >>= 1;
		}
	}

	static BufferedImage fixTransparency(ResourceLocation name, BufferedImage image) {
		if (image == null) {
			return image;
		} else {
			long s1 = System.currentTimeMillis();
			image = convertToARGB(image);
			int width = image.getWidth();
			int height = image.getHeight();
			IntBuffer buffer = getImageAsARGBIntBuffer(image);
			IntBuffer scaledBuffer = buffer;
			label33:

			while (width % 2 == 0 && height % 2 == 0) {
				int s2 = 0;

				while (true) {
					if (s2 >= scaledBuffer.limit()) {
						break label33;
					}

					if (scaledBuffer.get(s2) >>> 24 == 0) {
						IntBuffer newBuffer = getPooledBuffer(width * height).asIntBuffer();
						scaleHalf(scaledBuffer, width, height, newBuffer, 8);
						scaledBuffer = newBuffer;
						width >>= 1;
						height >>= 1;
						break;
					}

					++s2;
				}
			}

			long var12 = System.currentTimeMillis();

			if (scaledBuffer != buffer) {
				setBackgroundColor(buffer, image.getWidth(), image.getHeight(), scaledBuffer, image.getWidth() / width);
			}

			long s3 = System.currentTimeMillis();
			logger.finer("bg fix (tile %s): scaling %dms, setbg %dms", new Object[] {name, Long.valueOf(var12 - s1), Long.valueOf(s3 - var12)});
			return image;
		}
	}

	static void reset() {
		mipmapType.clear();
		mipmapType.put("terrain", Boolean.valueOf(true));
		mipmapType.put("items", Boolean.valueOf(false));
		Properties properties = TexturePackAPI.getProperties(MIPMAP_PROPERTIES);

		if (properties != null) {
			Iterator i$ = properties.entrySet().iterator();

			while (i$.hasNext()) {
				Entry entry = (Entry)i$.next();

				if (entry.getKey() instanceof String && entry.getValue() instanceof String) {
					String key = ((String)entry.getKey()).trim();
					boolean value = Boolean.parseBoolean(((String)entry.getValue()).trim().toLowerCase());

					if (key.endsWith(".png")) {
						mipmapType.put(key, Boolean.valueOf(value));
					}
				}
			}
		}
	}

	static boolean useMipmapsForTexture(String texture) {
		return useMipmap && texture != null ? (mipmapType.containsKey(texture) ? ((Boolean)mipmapType.get(texture)).booleanValue() : !texture.contains("item") && !texture.startsWith("textures/colormap/") && !texture.startsWith("textures/environment/") && !texture.startsWith("textures/font/") && !texture.startsWith("textures/gui/") && !texture.startsWith("textures/map/") && !texture.startsWith("textures/misc/") && !texture.startsWith("mcpatcher/colormap/") && !texture.startsWith("mcpatcher/cit/") && !texture.startsWith("mcpatcher/dial/") && !texture.startsWith("mcpatcher/font/") && !texture.startsWith("mcpatcher/lightmap/") && !texture.startsWith("mcpatcher/sky/")) : false;
	}

	static int getMipmapLevelsForCurrentTexture() {
		int filter = GL11.glGetTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER);
		return filter != 9986 && filter != 9984 ? 0 : Math.min(maxMipmapLevel, GL11.glGetTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL));
	}

	private static int gcd(int a, int b) {
		return BigInteger.valueOf((long)a).gcd(BigInteger.valueOf((long)b)).intValue();
	}

	private static int getMipmapLevels(int width, int height, int minSize) {
		int size = gcd(width, height);
		int mipmap;

		for (mipmap = 0; size >= minSize && (size & 1) == 0 && mipmap < maxMipmapLevel; ++mipmap) {
			size >>= 1;
		}

		return mipmap;
	}

	private static BufferedImage getPooledImage(int width, int height, int index) {
		String key = String.format("%dx%d#%d", new Object[] {Integer.valueOf(width), Integer.valueOf(height), Integer.valueOf(index)});
		Reference ref = (Reference)imagePool.get(key);
		BufferedImage image = ref == null ? null : (BufferedImage)ref.get();

		if (image == null) {
			image = new BufferedImage(width, height, 2);
			imagePool.put(key, new SoftReference(image));
		}

		return image;
	}

	private static ByteBuffer getPooledBuffer(int size) {
		Reference ref = (Reference)bufferPool.get(Integer.valueOf(size));
		ByteBuffer buffer = ref == null ? null : (ByteBuffer)ref.get();

		if (buffer == null) {
			buffer = ByteBuffer.allocateDirect(size);
			bufferPool.put(Integer.valueOf(size), new SoftReference(buffer));
		}

		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.position(0);
		return buffer;
	}

	private static BufferedImage convertToARGB(BufferedImage image) {
		if (image == null) {
			return null;
		} else if (image.getType() == 2) {
			return image;
		} else {
			int width = image.getWidth();
			int height = image.getHeight();
			logger.finest("converting %dx%d image to ARGB", new Object[] {Integer.valueOf(width), Integer.valueOf(height)});
			BufferedImage newImage = getPooledImage(width, height, 0);
			Graphics2D graphics = newImage.createGraphics();
			Arrays.fill(getImageAsARGBIntBuffer(newImage).array(), 0);
			graphics.drawImage(image, 0, 0, (ImageObserver)null);
			return newImage;
		}
	}

	private static IntBuffer getImageAsARGBIntBuffer(BufferedImage image) {
		DataBuffer buffer = image.getRaster().getDataBuffer();

		if (buffer instanceof DataBufferInt) {
			return IntBuffer.wrap(((DataBufferInt)buffer).getData());
		} else if (buffer instanceof DataBufferByte) {
			return ByteBuffer.wrap(((DataBufferByte)buffer).getData()).order(ByteOrder.BIG_ENDIAN).asIntBuffer();
		} else {
			int width = image.getWidth();
			int height = image.getHeight();
			int[] pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
			return IntBuffer.wrap(pixels);
		}
	}

	private static void setBackgroundColor(IntBuffer buffer, int width, int height, IntBuffer scaledBuffer, int scale) {
		for (int i = 0; i < width; ++i) {
			for (int j = 0; j < height; ++j) {
				int k = width * j + i;
				int pixel = buffer.get(k);

				if ((pixel & -16777216) == 0) {
					pixel = scaledBuffer.get(j / scale * (width / scale) + i / scale);
					buffer.put(k, pixel & 16777215);
				}
			}
		}
	}

	static void scaleHalf(IntBuffer in, int w, int h, IntBuffer out, int rotate) {
		for (int i = 0; i < w / 2; ++i) {
			for (int j = 0; j < h / 2; ++j) {
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

	private static int average4RGBA(int pixel00, int pixel01, int pixel10, int pixel11) {
		int a00 = pixel00 & 255;
		int a01 = pixel01 & 255;
		int a10 = pixel10 & 255;
		int a11 = pixel11 & 255;

		switch (a00 << 24 | a01 << 16 | a10 << 8 | a11) {
			case -16777216:
				return pixel00;

			case -16776961:
				return average2RGBA(pixel00, pixel11);

			case -16711936:
				return average2RGBA(pixel00, pixel10);

			case -65536:
				return average2RGBA(pixel00, pixel01);

			case -1:
			case 0:
				return average2RGBA(average2RGBA(pixel00, pixel11), average2RGBA(pixel01, pixel10));

			case 255:
				return pixel11;

			case 65280:
				return pixel10;

			case 65535:
				return average2RGBA(pixel10, pixel11);

			case 16711680:
				return pixel01;

			case 16711935:
				return average2RGBA(pixel01, pixel11);

			case 16776960:
				return average2RGBA(pixel01, pixel10);

			default:
				int a = a00 + a01 + a10 + a11;
				int pixel = a >> 2;

				for (int i = 8; i < 32; i += 8) {
					int average = (a00 * (pixel00 >> i & 255) + a01 * (pixel01 >> i & 255) + a10 * (pixel10 >> i & 255) + a11 * (pixel11 >> i & 255)) / a;
					pixel |= average << i;
				}

				return pixel;
		}
	}

	private static int average2RGBA(int a, int b) {
		return ((a & -16843010) >>> 1) + ((b & -16843010) >>> 1) | a & b & 16843009;
	}

	private static void checkGLError(String format, Object ... params) {
		int error = GL11.glGetError();

		if (error != 0) {
			String message = GLU.gluErrorString(error) + ": " + String.format(format, params);
			(new RuntimeException(message)).printStackTrace();
		}
	}

	static {
		if (anisoSupported) {
			anisoMax = (int)GL11.glGetFloat(34047);
			checkGLError("glGetFloat(GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT)", new Object[0]);
			anisoLevel = Math.max(Math.min(Config.getInt("Extended HD", "anisotropicFiltering", 1), anisoMax), 1);
		} else {
			anisoLevel = 1;
			anisoMax = 1;
		}

		lodSupported = GLContext.getCapabilities().GL_EXT_texture_lod_bias;

		if (lodSupported) {
			lodBias = Config.getInt("Extended HD", "lodBias", 0);
		} else {
			lodBias = 0;
		}

		logger.config("mipmap: supported=%s, enabled=%s, level=%d", new Object[] {Boolean.valueOf(mipmapSupported), Boolean.valueOf(mipmapEnabled), Integer.valueOf(maxMipmapLevel)});
		logger.config("anisotropic: supported=%s, level=%d, max=%d", new Object[] {Boolean.valueOf(anisoSupported), Integer.valueOf(anisoLevel), Integer.valueOf(anisoMax)});
		logger.config("lod bias: supported=%s, bias=%d", new Object[] {Boolean.valueOf(lodSupported), Integer.valueOf(lodBias)});
	}
}
