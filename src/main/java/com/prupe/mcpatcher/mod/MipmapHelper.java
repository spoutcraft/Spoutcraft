package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.Config;
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
import java.util.Map;
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
	private static final String MIPMAP_PROPERTIES = "/mipmap.properties";
	private static final int MIN_ALPHA = 26;
	private static final int MAX_ALPHA = 229;
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
	private static final Map imagePool = new HashMap();
	private static final Map bufferPool = new HashMap();
	private static int bgColorFix = 4;
	public static int currentLevel;
	private static final Map mipmapType = new HashMap();
	private static Texture currentTexture;
	private static boolean enableTransparencyFix = true;
	private static boolean flippedTextureLogged;

	public static void setupTexture(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, ByteBuffer var8, Texture var9) {
		if (!useMipmapsForTexture(var9.getTextureName())) {
			GL11.glTexImage2D(var0, var1, var2, var3, var4, var5, var6, var7, getDirectByteBuffer(var8, true));
		} else {
			int[] var10 = var6 == 6408 ? new int[] {3, 0, 1, 2}: new int[] {3, 2, 1, 0};
			BufferedImage var11 = getPooledImage(var3, var4, 0);
			byte[] var12 = new byte[4 * var3 * var4];
			int[] var13 = new int[var3 * var4];
			var8.position(0);
			var8.get(var12, 0, var12.length);

			for (int var14 = 0; var14 < var12.length; var14 += 4) {
				var13[var14 / 4] = (var12[var14 + var10[0]] & 255) << 24 | (var12[var14 + var10[1]] & 255) << 16 | (var12[var14 + var10[2]] & 255) << 8 | var12[var14 + var10[3]] & 255;
			}

			var11.setRGB(0, 0, var3, var4, var13, 0, var3);

			try {
				currentTexture = var9;
				enableTransparencyFix = false;
				setupTexture(MCPatcherUtils.getMinecraft().renderEngine, var11, var9.getGlTextureId(), false, false, var9.getTextureName());
			} finally {
				enableTransparencyFix = true;
				currentTexture = null;
			}
		}
	}

	public static ByteBuffer allocateByteBuffer(int var0) {
		return byteBufferAllocation == 0 ? ByteBuffer.allocateDirect(var0) : ByteBuffer.allocate(var0);
	}

	public static void copySubTexture(Texture var0, Texture var1, int var2, int var3, boolean var4) {
		ByteBuffer var5 = var1.getTextureData();
		var5.position(0);

		if (byteBufferAllocation == 1 && !var5.isDirect()) {
			var1.textureData = var5 = getDirectByteBuffer(var5, false);
		}

		TexturePackAPI.bindTexture(var0.getGlTextureId());
		int var6 = var0.mipmapActive ? getMipmapLevels() : 0;
		int var7 = var1.getWidth();
		int var8 = var1.getHeight();

		if (var4 && !flippedTextureLogged) {
			flippedTextureLogged = true;
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
			checkGLError("%s -> %s: glTexSubImage2D(%d, %d, %d, %d, %d)", new Object[] {var1.getTextureName(), var0.getTextureName(), Integer.valueOf(var9), Integer.valueOf(var2), Integer.valueOf(var3), Integer.valueOf(var7), Integer.valueOf(var8)});

			if (var9 >= var6) {
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
		}
	}

	private static ArrayList getMipmapsForTexture(BufferedImage var0, String var1) {
		ArrayList var2 = new ArrayList();
		var2.add(var0);

		if (!useMipmapsForTexture(var1)) {
			return var2;
		} else {
			int var3 = var0.getWidth();
			int var4 = var0.getHeight();

			if (getCustomMipmaps(var2, var1, var3, var4)) {
				return var2;
			} else {
				int var5 = getMipmapLevels(var0.getWidth(), var0.getHeight(), 2);

				if (var5 <= 0) {
					GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, var5);
					return var2;
				} else {
					var0 = convertToARGB(var0);
					var2.set(0, var0);
					int var6 = 1 << bgColorFix;
					int var7 = gcd(var3, var4);

					if (enableTransparencyFix && bgColorFix > 0 && var7 % var6 == 0 && (var7 / var6 & var7 / var6 - 1) == 0) {
						long var8 = System.currentTimeMillis();
						BufferedImage var10;

						for (var10 = (BufferedImage)var2.get(var2.size() - 1); gcd(var10.getWidth(), var10.getHeight()) > var6; var10 = scaleHalf(var10)) {
							;
						}

						long var11 = System.currentTimeMillis();
						setBackgroundColor(var0, var10);
						long var13 = System.currentTimeMillis();
					}

					for (int var15 = 0; var15 < var5; ++var15) {
						var0 = scaleHalf(var0);
						var2.add(var0);
					}

					return var2;
				}
			}
		}
	}

	static BufferedImage fixTransparency(String var0, BufferedImage var1) {
		if (var1 == null) {
			return var1;
		} else {
			long var2 = System.currentTimeMillis();
			var1 = convertToARGB(var1);
			int var4 = var1.getWidth();
			int var5 = var1.getHeight();
			IntBuffer var6 = getARGBAsIntBuffer(var1);
			IntBuffer var7 = var6;
			label33:

			while (var4 % 2 == 0 && var5 % 2 == 0) {
				int var8 = 0;

				while (true) {
					if (var8 >= var7.limit()) {
						break label33;
					}

					if (var7.get(var8) >>> 24 == 0) {
						IntBuffer var9 = getPooledBuffer(var4 * var5).asIntBuffer();
						scaleHalf(var7, var4, var5, var9, 8);
						var7 = var9;
						var4 >>= 1;
						var5 >>= 1;
						break;
					}

					++var8;
				}
			}

			long var12 = System.currentTimeMillis();

			if (var7 != var6) {
				setBackgroundColor(var6, var1.getWidth(), var1.getHeight(), var7, var1.getWidth() / var4);
			}

			long var10 = System.currentTimeMillis();
			return var1;
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
						currentTexture.mipmapActive = true;
						currentTexture.textureFormat = 9986;
						currentTexture.textureMinFilter = 9728;
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
				var0.setupTextureExt(var7, var2, var4, var5);
				checkGLError("setupTexture %s#%d", new Object[] {var3, Integer.valueOf(currentLevel)});
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
		mipmapType.put("terrain", Boolean.valueOf(true));
		mipmapType.put("items", Boolean.valueOf(false));
		Properties var0 = TexturePackAPI.getProperties("/mipmap.properties");

		if (var0 != null) {
			bgColorFix = MCPatcherUtils.getIntProperty(var0, "bgColorFix", 4);
			Iterator var1 = var0.entrySet().iterator();

			while (var1.hasNext()) {
				Entry var2 = (Entry)var1.next();

				if (var2.getKey() instanceof String && var2.getValue() instanceof String) {
					String var3 = ((String)var2.getKey()).trim();
					boolean var4 = Boolean.parseBoolean(((String)var2.getValue()).trim().toLowerCase());

					if (var3.startsWith("/")) {
						mipmapType.put(var3, Boolean.valueOf(var4));
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
					break;
				}

				var0.add(var7);
				var4 = true;
			}
		}

		return var4;
	}

	private static boolean useMipmapsForTexture(String var0) {
		return useMipmap && var0 != null ? (mipmapType.containsKey(var0) ? ((Boolean)mipmapType.get(var0)).booleanValue() : !var0.startsWith("%") && !var0.startsWith("##") && !var0.startsWith("/achievement/") && !var0.startsWith("/environment/") && !var0.startsWith("/font/") && !var0.startsWith("/gui/") && !var0.startsWith("/misc/") && !var0.startsWith("/terrain/") && !var0.startsWith("/title/") && !var0.contains("item")) : false;
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

		var2.order(ByteOrder.BIG_ENDIAN);
		var2.position(0);
		return var2;
	}

	private static ByteBuffer getDirectByteBuffer(ByteBuffer var0, boolean var1) {
		if (var0.isDirect()) {
			return var0;
		} else {
			ByteBuffer var2 = var1 ? getPooledBuffer(var0.capacity()) : ByteBuffer.allocateDirect(var0.capacity());
			var2.order(var0.order());
			var2.put(var0);
			var2.flip();
			return var2;
		}
	}

	private static BufferedImage convertToARGB(BufferedImage var0) {
		if (var0 == null) {
			return null;
		} else if (var0.getType() == 2) {
			return var0;
		} else {
			int var1 = var0.getWidth();
			int var2 = var0.getHeight();
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
		setBackgroundColor(var5, var2, var3, var6, var4);
	}

	private static void setBackgroundColor(IntBuffer var0, int var1, int var2, IntBuffer var3, int var4) {
		for (int var5 = 0; var5 < var1; ++var5) {
			for (int var6 = 0; var6 < var2; ++var6) {
				int var7 = var1 * var6 + var5;
				int var8 = var0.get(var7);

				if ((var8 & -16777216) == 0) {
					var8 = var3.get(var6 / var4 * (var1 / var4) + var5 / var4);
					var0.put(var7, var8 & 16777215);
				}
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
	}
}
