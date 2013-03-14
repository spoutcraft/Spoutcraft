package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.TexturePackAPI;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.Texture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;

public class MipmapHelper {
	private static final MCLogger logger = MCLogger.getLogger("Mipmap");
	private static final String MIPMAP_PROPERTIES = "/mipmap.properties";
	private static final boolean mipmapSupported = GLContext.getCapabilities().OpenGL12;
	static final boolean mipmapEnabled = Config.getBoolean("Extended HD", "mipmap", false);
	static final int maxMipmapLevel = Config.getInt("Extended HD", "maxMipmapLevel", 3);
	private static final boolean useMipmap = mipmapSupported && mipmapEnabled && maxMipmapLevel > 0;
	private static final int mipmapAlignment = (1 << Config.getInt("Extended HD", "mipmapAlignment", 3)) - 1;
	private static final int byteBufferAllocation = Config.getInt("Extended HD", "byteBufferAllocation", 1);
	private static final boolean anisoSupported = GLContext.getCapabilities().GL_EXT_texture_filter_anisotropic;
	static final int anisoLevel;
	private static final int anisoMax;
	private static final boolean lodSupported;
	private static int lodBias;
	private static final HashMap imagePool = new HashMap();
	private static final HashMap bufferPool = new HashMap();
	private static int bgColorFix = 4;
	public static int currentLevel;
	private static final HashMap mipmapType = new HashMap();
	private static final int MIPMAP_NONE = 0;
	private static final int MIPMAP_BASIC = 1;
	private static final int MIPMAP_ALPHA = 2;
	private static Texture currentTexture;
	private static boolean flippedTextureLogged;

	public static void setupTexture(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, ByteBuffer var8, Texture var9) {
		int[] var10 = var6 == 6408 ? new int[] {3, 0, 1, 2}: new int[] {3, 2, 1, 0};
		BufferedImage var11 = new BufferedImage(var3, var4, 2);
		byte[] var12 = new byte[4 * var3 * var4];
		int[] var13 = new int[var3 * var4];
		var8.position(0);
		var8.get(var12, 0, var12.length);

		for (int var14 = 0; var14 < var12.length; var14 += 4) {
			var13[var14 / 4] = (var12[var14 + var10[0]] & 255) << 24 | (var12[var14 + var10[1]] & 255) << 16 | (var12[var14 + var10[2]] & 255) << 8 | var12[var14 + var10[3]] & 255;
		}

		var11.setRGB(0, 0, var3, var4, var13, 0, var3);
		currentTexture = var9;
		setupTexture(MCPatcherUtils.getMinecraft().renderEngine, var11, var9.func_94282_c(), false, false, var9.func_94280_f());
		currentTexture = null;
	}

	public static ByteBuffer allocateByteBuffer(int var0) {
		return byteBufferAllocation == 0 ? ByteBuffer.allocateDirect(var0) : ByteBuffer.allocate(var0);
	}

	public static void copySubTexture(Texture var0, Texture var1, int var2, int var3, boolean var4) {
		ByteBuffer var5 = var1.func_94273_h();
		var5.position(0);

		if (byteBufferAllocation == 1 && !var5.isDirect()) {
			logger.finer("creating %d direct byte buffer for texture %s", new Object[] {Integer.valueOf(var5.capacity()), var1.func_94280_f()});
			ByteBuffer var6 = ByteBuffer.allocateDirect(var5.capacity()).order(var5.order());
			var6.put(var5).flip();
			var5 = var6;
			var1.field_94302_r = var6;
		}

		TexturePackAPI.bindTexture(var0.func_94282_c());
		int var11 = var0.field_94296_l ? getMipmapLevels() : 0;
		int var7 = var1.func_94275_d();
		int var8 = var1.func_94276_e();

		if (var4 && !flippedTextureLogged) {
			flippedTextureLogged = true;
			logger.warning("copySubTexture(%s, %s, %d, %d, %s): flipped texture not yet supported", new Object[] {var0.func_94280_f(), var1.func_94280_f(), Integer.valueOf(var2), Integer.valueOf(var3), Boolean.valueOf(var4)});
		}

		int var9 = 0;

		while (true) {
			ByteBuffer var10;

			if (byteBufferAllocation == 2 && !var5.isDirect()) {
				var10 = getPooledBuffer(var5.capacity());
				var10.put(var5).flip();
				var5 = var10;
			}

			GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, var9, var2, var3, var7, var8, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, var5);
			checkGLError("%s -> %s: glTexSubImage2D(%d, %d, %d, %d, %d)", new Object[] {var1.func_94280_f(), var0.func_94280_f(), Integer.valueOf(var9), Integer.valueOf(var2), Integer.valueOf(var3), Integer.valueOf(var7), Integer.valueOf(var8)});

			if (var9 >= var11) {
				return;
			}

			var10 = getPooledBuffer(var7 * var8);
			scaleHalf(var5.asIntBuffer(), var7, var8, var10.asIntBuffer(), 0);
			var7 >>= 1;
			var8 >>= 1;
			var2 >>= 1;
			var3 >>= 1;
			var5 = var10;
			++var9;
		}
	}

	public static void setupTexture(RenderEngine var0, BufferedImage var1, int var2, boolean var3, boolean var4, String var5) {
		if (var2 >= 0 && var1 != null) {
			long var6 = System.currentTimeMillis();
			ArrayList var8 = getMipmapsForTexture(var1, var5);
			long var9 = System.currentTimeMillis();
			setupTextureMipmaps(var0, var8, var2, var5, var3, var4);
			long var11 = System.currentTimeMillis();

			if (var8.size() > 1) {
				logger.finer("%s: generate %dms, setup %dms, total %dms", new Object[] {var5, Long.valueOf(var9 - var6), Long.valueOf(var11 - var9), Long.valueOf(var11 - var6)});
			}
		}
	}

	private static ArrayList getMipmapsForTexture(BufferedImage var0, String var1) {
		ArrayList var2 = new ArrayList();
		var2.add(var0);
		int var3 = getMipmapType(var1, var2);

		if (var3 < 1) {
			return var2;
		} else {
			int var4 = var0.getWidth();
			int var5 = var0.getHeight();

			if (getCustomMipmaps(var2, var1, var4, var5)) {
				logger.fine("using %d custom mipmaps for %s", new Object[] {Integer.valueOf(var2.size() - 1), var1});
				return var2;
			} else {
				int var6 = getMipmapLevels(var0.getWidth(), var0.getHeight(), 2);

				if (var6 <= 0) {
					GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, var6);
					byte var16 = 0;

					if (var1 != null) {
						mipmapType.put(var1, Integer.valueOf(var16));
					}

					return var2;
				} else {
					logger.fine("generating %d mipmaps for %s, alpha=%s", new Object[] {Integer.valueOf(var6), var1, Boolean.valueOf(var3 >= 2)});
					var0 = convertToARGB(var0);
					var2.set(0, var0);
					int var7 = 1 << bgColorFix;
					int var8 = gcd(var4, var5);

					if (bgColorFix > 0 && var8 % var7 == 0 && (var8 / var7 & var8 / var7 - 1) == 0) {
						long var9 = System.currentTimeMillis();
						BufferedImage var11;

						for (var11 = (BufferedImage)var2.get(var2.size() - 1); gcd(var11.getWidth(), var11.getHeight()) > var7; var11 = scaleHalf(var11)) {
							;
						}

						long var12 = System.currentTimeMillis();
						setBackgroundColor(var0, var11);
						long var14 = System.currentTimeMillis();
						logger.finer("bg fix: scaling %dms, setbg %dms", new Object[] {Long.valueOf(var12 - var9), Long.valueOf(var14 - var12)});
					}

					BufferedImage var17 = var0;

					for (int var10 = 0; var10 < var6; ++var10) {
						var17 = scaleHalf(var17);

						if (var3 >= 2) {
							var0 = var17;
						} else {
							var0 = getPooledImage(var17.getWidth(), var17.getHeight(), 1);
							var17.copyData(var0.getRaster());
							resetOnOffTransparency(var0);
						}

						var2.add(var0);
					}

					return var2;
				}
			}
		}
	}

	private static void setupTextureMipmaps(RenderEngine var0, ArrayList var1, int var2, String var3, boolean var4, boolean var5) {
		try {
			int var6 = var1.size() - 1;

			for (currentLevel = 0; currentLevel <= var6; ++currentLevel) {
				if (currentLevel == 1) {
					GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_LINEAR);
					GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
					GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, var6);

					if (currentTexture != null) {
						currentTexture.field_94296_l = true;
						currentTexture.field_94288_g = 9986;
						currentTexture.field_94301_i = 9728;
					}

					checkGLError("set GL_TEXTURE_MAX_LEVEL = %d", new Object[] {Integer.valueOf(var6)});

					if (anisoSupported && anisoLevel > 1) {
						GL11.glTexParameterf(GL11.GL_TEXTURE_2D, 34046, (float)anisoLevel);
						checkGLError("set GL_TEXTURE_MAX_ANISOTROPY_EXT = %f", new Object[] {Integer.valueOf(anisoLevel)});
					}

					if (lodSupported) {
						GL11.glTexEnvi(GL14.GL_TEXTURE_FILTER_CONTROL, GL14.GL_TEXTURE_LOD_BIAS, lodBias);
						checkGLError("set GL_TEXTURE_LOD_BIAS_EXT = %d", new Object[] {Integer.valueOf(lodBias)});
					}
				}

				BufferedImage var7 = (BufferedImage)var1.get(currentLevel);
				var0.func_98184_a(var7, var2, var4, var5);
				checkGLError("setupTexture %s#%d", new Object[] {var3, Integer.valueOf(currentLevel)});

				if (currentLevel > 0) {
					logger.finest("%s mipmap level %d (%dx%d)", new Object[] {var3, Integer.valueOf(currentLevel), Integer.valueOf(var7.getWidth()), Integer.valueOf(var7.getHeight())});
				}
			}
		} catch (Throwable var11) {
			var11.printStackTrace();
		} finally {
			currentLevel = 0;
		}
	}

	static void reset() {
		bgColorFix = 4;
		mipmapType.clear();
		forceMipmapType("terrain", 1);
		forceMipmapType("items", 0);
		Properties var0 = TexturePackAPI.getProperties("/mipmap.properties");

		if (var0 != null) {
			bgColorFix = MCPatcherUtils.getIntProperty(var0, "bgColorFix", 4);
			Iterator var1 = var0.entrySet().iterator();

			while (var1.hasNext()) {
				Entry var2 = (Entry)var1.next();

				if (var2.getKey() instanceof String && var2.getValue() instanceof String) {
					String var3 = ((String)var2.getKey()).trim();
					String var4 = ((String)var2.getValue()).trim().toLowerCase();

					if (var3.startsWith("/")) {
						if (var4.equals("none")) {
							mipmapType.put(var3, Integer.valueOf(0));
						} else if (!var4.equals("basic") && !var4.equals("opaque")) {
							if (var4.equals("alpha")) {
								mipmapType.put(var3, Integer.valueOf(2));
							} else {
								logger.error("%s: unknown value \'%s\' for %s", new Object[] {"/mipmap.properties", var4, var3});
							}
						} else {
							mipmapType.put(var3, Integer.valueOf(1));
						}
					}
				}
			}
		}
	}

	private static boolean getCustomMipmaps(ArrayList var0, String var1, int var2, int var3) {
		boolean var4 = false;

		if (useMipmap && var1 != null && var1.startsWith("/")) {
			for (int var5 = 1; var2 > 0 && var3 > 0 && var5 <= maxMipmapLevel; ++var5) {
				var2 >>>= 1;
				var3 >>>= 1;
				String var6 = var1.replace(".png", "-mipmap" + var5 + ".png");
				BufferedImage var7 = TexturePackAPI.getImage(var6);

				if (var7 == null) {
					break;
				}

				int var8 = var7.getWidth();
				int var9 = var7.getHeight();

				if (var8 != var2 || var9 != var3) {
					logger.error("%s has wrong size %dx%d (expecting %dx%d)", new Object[] {var6, Integer.valueOf(var8), Integer.valueOf(var9), Integer.valueOf(var2), Integer.valueOf(var3)});
					break;
				}

				var0.add(var7);
				var4 = true;
			}
		}

		return var4;
	}

	private static int getMipmapType(String var0, ArrayList var1) {
		BufferedImage var2 = var1 == null ? null : (BufferedImage)var1.get(0);

		if (useMipmap && var0 != null) {
			if (mipmapType.containsKey(var0)) {
				return ((Integer)mipmapType.get(var0)).intValue();
			} else if (!var0.startsWith("%") && !var0.startsWith("##") && !var0.startsWith("/achievement/") && !var0.startsWith("/environment/") && !var0.startsWith("/font/") && !var0.startsWith("/gui/") && !var0.startsWith("/misc/") && !var0.startsWith("/terrain/") && !var0.startsWith("/title/") && !var0.contains("item")) {
				if (var2 == null) {
					return 1;
				} else {
					var2 = convertToARGB(var2);
					var1.set(0, var2);
					IntBuffer var3 = getARGBAsIntBuffer(var2);

					for (int var4 = 0; var4 < var3.limit(); ++var4) {
						int var5 = var3.get(var4) >>> 24;

						if (var5 > 26 && var5 < 229) {
							logger.finer("%s alpha transparency? yes, by pixel search", new Object[] {var0});
							mipmapType.put(var0, Integer.valueOf(2));
							return 2;
						}
					}

					logger.finer("%s alpha transparency? no, by pixel search", new Object[] {var0});
					mipmapType.put(var0, Integer.valueOf(1));
					return 1;
				}
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}

	static void forceMipmapType(String var0, int var1) {
		if (useMipmap) {
			boolean var2 = false;

			if (mipmapType.containsKey(var0)) {
				var2 = ((Integer)mipmapType.get(var0)).intValue() != var1;

				if (!var2) {
					return;
				}
			}

			mipmapType.put(var0, Integer.valueOf(var1));

			if (var2) {
				logger.finer("force %s -> %d (reloading)", new Object[] {var0, Integer.valueOf(var1)});
				int var3 = TexturePackAPI.getTextureIfLoaded(var0);

				if (var3 >= 0) {
					setupTexture(MCPatcherUtils.getMinecraft().renderEngine, TexturePackAPI.getImage(var0), var3, false, false, var0);
				}
			} else {
				logger.finer("force %s -> %d", new Object[] {var0, Integer.valueOf(var1)});
			}
		}
	}

	static int getMipmapLevels() {
		int var0 = GL11.glGetTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER);
		return var0 != 9986 && var0 != 9984 ? 0 : GL11.glGetTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL);
	}

	private static int gcd(int var0, int var1) {
		return BigInteger.valueOf((long)var0).gcd(BigInteger.valueOf((long)var1)).intValue();
	}

	private static int getMipmapLevels(int var0, int var1, int var2) {
		int var3 = gcd(var0, var1);
		int var4;

		for (var4 = 0; var3 >= var2 && (var3 & 1) == 0 && var4 < maxMipmapLevel; ++var4) {
			var3 >>= 1;
		}

		return var4;
	}

	private static BufferedImage getPooledImage(int var0, int var1, int var2) {
		String var3 = String.format("%dx%d#%d", new Object[] {Integer.valueOf(var0), Integer.valueOf(var1), Integer.valueOf(var2)});
		Reference var4 = (Reference)imagePool.get(var3);
		BufferedImage var5 = var4 == null ? null : (BufferedImage)var4.get();

		if (var5 == null) {
			var5 = new BufferedImage(var0, var1, 2);
			imagePool.put(var3, new SoftReference(var5));
		}

		return var5;
	}

	private static ByteBuffer getPooledBuffer(int var0) {
		Reference var1 = (Reference)bufferPool.get(Integer.valueOf(var0));
		ByteBuffer var2 = var1 == null ? null : (ByteBuffer)var1.get();

		if (var2 == null) {
			var2 = ByteBuffer.allocateDirect(var0);
			bufferPool.put(Integer.valueOf(var0), new SoftReference(var2));
		}

		var2.position(0);
		return var2;
	}

	private static BufferedImage convertToARGB(BufferedImage var0) {
		if (var0 == null) {
			return null;
		} else if (var0.getType() == 2) {
			return var0;
		} else {
			int var1 = var0.getWidth();
			int var2 = var0.getHeight();
			logger.fine("converting %dx%d image to ARGB", new Object[] {Integer.valueOf(var1), Integer.valueOf(var2)});
			BufferedImage var3 = getPooledImage(var1, var2, 0);
			Graphics2D var4 = var3.createGraphics();
			Arrays.fill(getARGBAsIntBuffer(var3).array(), 0);
			var4.drawImage(var0, 0, 0, (ImageObserver)null);
			return var3;
		}
	}

	private static IntBuffer getARGBAsIntBuffer(BufferedImage var0) {
		DataBuffer var1 = var0.getRaster().getDataBuffer();

		if (var1 instanceof DataBufferInt) {
			return IntBuffer.wrap(((DataBufferInt)var1).getData());
		} else if (var1 instanceof DataBufferByte) {
			return ByteBuffer.wrap(((DataBufferByte)var1).getData()).order(ByteOrder.BIG_ENDIAN).asIntBuffer();
		} else {
			int var2 = var0.getWidth();
			int var3 = var0.getHeight();
			int[] var4 = new int[var2 * var3];
			var0.getRGB(0, 0, var2, var3, var4, 0, var2);
			return IntBuffer.wrap(var4);
		}
	}

	private static void setBackgroundColor(BufferedImage var0, BufferedImage var1) {
		int var2 = var0.getWidth();
		int var3 = var0.getHeight();
		int var4 = var2 / var1.getWidth();
		IntBuffer var5 = getARGBAsIntBuffer(var0);
		IntBuffer var6 = getARGBAsIntBuffer(var1);

		for (int var7 = 0; var7 < var2; ++var7) {
			for (int var8 = 0; var8 < var3; ++var8) {
				int var9 = var2 * var8 + var7;
				int var10 = var5.get(var9);

				if ((var10 & -16777216) == 0) {
					var10 = var6.get(var8 / var4 * (var2 / var4) + var7 / var4);
					var5.put(var9, var10 & 16777215);
				}
			}
		}
	}

	private static void resetOnOffTransparency(BufferedImage var0) {
		IntBuffer var1 = getARGBAsIntBuffer(var0);

		for (int var2 = 0; var2 < var1.limit(); ++var2) {
			int var3 = var1.get(var2);

			if (var3 >>> 24 < 127) {
				var1.put(var2, var3 & 16777215);
			} else {
				var1.put(var2, var3 | -16777216);
			}
		}
	}

	static void scaleHalf(IntBuffer var0, int var1, int var2, IntBuffer var3, int var4) {
		for (int var5 = 0; var5 < var1 / 2; ++var5) {
			for (int var6 = 0; var6 < var2 / 2; ++var6) {
				int var7 = var1 * 2 * var6 + 2 * var5;
				int var8 = var0.get(var7);
				int var9 = var0.get(var7 + 1);
				int var10 = var0.get(var7 + var1);
				int var11 = var0.get(var7 + var1 + 1);

				if (var4 != 0) {
					var8 = Integer.rotateLeft(var8, var4);
					var9 = Integer.rotateLeft(var9, var4);
					var10 = Integer.rotateLeft(var10, var4);
					var11 = Integer.rotateLeft(var11, var4);
				}

				int var12 = average4RGBA(var8, var9, var10, var11);

				if (var4 != 0) {
					var12 = Integer.rotateRight(var12, var4);
				}

				var3.put(var1 / 2 * var6 + var5, var12);
			}
		}
	}

	private static BufferedImage scaleHalf(BufferedImage var0) {
		int var1 = var0.getWidth();
		int var2 = var0.getHeight();
		BufferedImage var3 = getPooledImage(var1 / 2, var2 / 2, 0);
		scaleHalf(getARGBAsIntBuffer(var0), var1, var2, getARGBAsIntBuffer(var3), 8);
		return var3;
	}

	private static int average4RGBA(int var0, int var1, int var2, int var3) {
		int var4 = var0 & 255;
		int var5 = var1 & 255;
		int var6 = var2 & 255;
		int var7 = var3 & 255;

		switch (var4 << 24 | var5 << 16 | var6 << 8 | var7) {
			case -16777216:
				return var0;

			case -16776961:
				return average2RGBA(var0, var3);

			case -16711936:
				return average2RGBA(var0, var2);

			case -65536:
				return average2RGBA(var0, var1);

			case -1:
			case 0:
				return average2RGBA(average2RGBA(var0, var3), average2RGBA(var1, var2));

			case 255:
				return var3;

			case 65280:
				return var2;

			case 65535:
				return average2RGBA(var2, var3);

			case 16711680:
				return var1;

			case 16711935:
				return average2RGBA(var1, var3);

			case 16776960:
				return average2RGBA(var1, var2);

			default:
				int var8 = var4 + var5 + var6 + var7;
				int var9 = var8 >> 2;

				for (int var10 = 8; var10 < 32; var10 += 8) {
					int var11 = (var4 * (var0 >> var10 & 255) + var5 * (var1 >> var10 & 255) + var6 * (var2 >> var10 & 255) + var7 * (var3 >> var10 & 255)) / var8;
					var9 |= var11 << var10;
				}

				return var9;
		}
	}

	private static int average2RGBA(int var0, int var1) {
		return ((var0 & -16843010) >>> 1) + ((var1 & -16843010) >>> 1) | var0 & var1 & 16843009;
	}

	private static void checkGLError(String var0, Object ... var1) {
		int var2 = GL11.glGetError();

		if (var2 != 0) {
			String var3 = GLU.gluErrorString(var2) + ": " + String.format(var0, var1);
			(new RuntimeException(var3)).printStackTrace();
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
		}

		logger.config("mipmap: supported=%s, enabled=%s, level=%d", new Object[] {Boolean.valueOf(mipmapSupported), Boolean.valueOf(mipmapEnabled), Integer.valueOf(maxMipmapLevel)});
		logger.config("anisotropic: supported=%s, level=%d, max=%d", new Object[] {Boolean.valueOf(anisoSupported), Integer.valueOf(anisoLevel), Integer.valueOf(anisoMax)});
		logger.config("lod bias: supported=%s, bias=%d", new Object[] {Boolean.valueOf(lodSupported), Integer.valueOf(lodBias)});
	}
}
