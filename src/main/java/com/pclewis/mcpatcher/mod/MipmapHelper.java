package com.pclewis.mcpatcher.mod;

import com.pclewis.mcpatcher.MCLogger;
import com.pclewis.mcpatcher.MCPatcherUtils;
import com.pclewis.mcpatcher.TexturePackAPI;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Hashtable;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.TextureFX;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;

public class MipmapHelper {
	private static final MCLogger logger = MCLogger.getLogger("Mipmap");
	private static final boolean mipmapSupported = GLContext.getCapabilities().OpenGL12;
	private static final boolean mipmapEnabled = MCPatcherUtils.getBoolean("HD Textures", "mipmap", true);
	private static final int maxMipmapLevel = MCPatcherUtils.getInt("HD Textures", "maxMipmapLevel", 4);
	private static final boolean useMipmap = mipmapSupported && mipmapEnabled && maxMipmapLevel > 0;
	private static final int mipmapAlignment = (1 << MCPatcherUtils.getInt("HD Textures", "mipmapAlignment", 3)) - 1;
	private static final boolean anisoSupported = GLContext.getCapabilities().GL_EXT_texture_filter_anisotropic;
	private static final int anisoLevel;
	private static final int anisoMax;
	private static final boolean lodSupported;
	private static int lodBias;
	public static int currentLevel;
	private static final HashMap mipmapType = new HashMap();
	private static final int MIPMAP_NONE = 0;
	private static final int MIPMAP_BASIC = 1;
	private static final int MIPMAP_ALPHA = 2;

	public static void setupTexture(RenderEngine var0, BufferedImage var1, int var2, String var3) {
		currentLevel = 0;

		try {
			if (var2 < 0 || var1 == null) {
				return;
			}

			int var4 = 0;
			BufferedImage var5 = var1;
			int var6 = getMipmapType(var3, var1);

			if (var6 >= 1) {
				var4 = getMipmapLevel(var3, var1);

				if (var4 > 0) {
					logger.fine("generating %d mipmaps for %s, alpha=%s", new Object[] {Integer.valueOf(var4), var3, Boolean.valueOf(var6 >= 2)});
				} else {
					GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, var4);
					var6 = 0;

					if (var3 != null) {
						mipmapType.put(var3, Integer.valueOf(var6));
					}
				}
			}

			while (true) {
				if (currentLevel == 1) {
					GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_LINEAR);
					GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
					GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, var4);
					checkGLError("set GL_TEXTURE_MAX_LEVEL = %d", new Object[] {Integer.valueOf(var4)});

					if (anisoSupported && anisoLevel > 1) {
						GL11.glTexParameterf(GL11.GL_TEXTURE_2D, 34046, (float)anisoLevel);
						checkGLError("set GL_TEXTURE_MAX_ANISOTROPY_EXT = %f", new Object[] {Integer.valueOf(anisoLevel)});
					}

					setLOD(0);
				}

				var0.setupTexture(var1, var2);
				checkGLError("setupTexture %d", new Object[] {Integer.valueOf(currentLevel)});

				if (++currentLevel > var4) {
					break;
				}

				var5 = scaleHalf(var5);

				if (var6 >= 2) {
					var1 = var5;
				} else {
					var1 = new BufferedImage(var5.getColorModel(), var5.copyData((WritableRaster)null), var5.getColorModel().isAlphaPremultiplied(), (Hashtable)null);
					resetOnOffTransparency(var1);
				}

				logger.finest("%s mipmap level %d (%dx%d)", new Object[] {var3, Integer.valueOf(currentLevel), Integer.valueOf(var1.getWidth()), Integer.valueOf(var1.getHeight())});
			}
		} catch (Throwable var10) {
			var10.printStackTrace();
		} finally {
			currentLevel = 0;
		}
	}

	public static void glTexSubImage2D(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, ByteBuffer var8, TextureFX var9) {
		GL11.glTexSubImage2D(var0, var1, var2, var3, var4, var5, var6, var7, var8);

		if (var9.tileImage == 0) {
			update("/terrain.png", var2, var3, var4, var5, var8);
		}
	}

	static void update(String var0, int var1, int var2, int var3, int var4, ByteBuffer var5) {
		int var6 = GL11.glGetTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER);

		if (var6 == 9986 || var6 == 9984) {
			int var7 = GL11.glGetTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL);

			for (int var8 = 1; var8 <= var7 && ((var1 | var2 | var3 | var4) & mipmapAlignment) == 0; ++var8) {
				ByteBuffer var9 = ByteBuffer.allocateDirect(var3 * var4);
				scaleHalf(var5.asIntBuffer(), var3, var4, var9.asIntBuffer());
				var1 >>= 1;
				var2 >>= 1;
				var3 >>= 1;
				var4 >>= 1;
				int var10 = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, var8, GL11.GL_TEXTURE_WIDTH);
				int var11 = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, var8, GL11.GL_TEXTURE_HEIGHT);

				if (var10 < 0 || var11 < 0) {
					break;
				}

				GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, var8, var1, var2, var3, var4, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)var9.position(0));
				checkGLError("glTexSubImage2D(%d, %d, %d, %d, %d, %d), width %d, height %d, mipmaps %d", new Object[] {Integer.valueOf(var8), Integer.valueOf(var1), Integer.valueOf(var2), Integer.valueOf(var3), Integer.valueOf(var4), Integer.valueOf(var9.limit()), Integer.valueOf(var10), Integer.valueOf(var11), Integer.valueOf(var7)});
				var5 = var9;
			}
		}
	}

	static void setLOD(int var0) {
		if (lodSupported) {
			lodBias += var0;
			GL11.glTexEnvi(GL14.GL_TEXTURE_FILTER_CONTROL, GL14.GL_TEXTURE_LOD_BIAS, lodBias);
			checkGLError("set GL_TEXTURE_LOD_BIAS_EXT = %d", new Object[] {Integer.valueOf(lodBias)});

			if (var0 != 0) {
				logger.config("new lod bias: %d", new Object[] {Integer.valueOf(lodBias)});
			}
		}
	}

	static void reset() {
		mipmapType.clear();
		forceMipmapType("/terrain.png", 1);
	}

	private static int getMipmapType(String var0, BufferedImage var1) {
		if (useMipmap && var0 != null) {
			if (mipmapType.containsKey(var0)) {
				return ((Integer)mipmapType.get(var0)).intValue();
			} else if (!var0.startsWith("%") && !var0.startsWith("##") && !var0.startsWith("/achievement/") && !var0.startsWith("/environment/") && !var0.startsWith("/font/") && !var0.startsWith("/gui/") && !var0.startsWith("/misc/") && !var0.startsWith("/terrain/") && !var0.startsWith("/title/")) {
				if (var1 == null) {
					return 1;
				} else {
					int var2 = var1.getWidth();
					int var3 = var1.getHeight();

					for (int var4 = 0; var4 < var2; ++var4) {
						for (int var5 = 0; var5 < var3; ++var5) {
							int var6 = var1.getRGB(var4, var5);
							int var7 = var6 >>> 24;

							if (var7 > 26 && var7 < 229) {
								logger.finer("%s alpha transparency? yes, by pixel search", new Object[] {var0});
								mipmapType.put(var0, Integer.valueOf(2));
								return 2;
							}
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
					setupTexture(MCPatcherUtils.getMinecraft().renderEngine, TexturePackAPI.getImage(var0), var3, var0);
				}
			} else {
				logger.finer("force %s -> %d", new Object[] {var0, Integer.valueOf(var1)});
			}
		}
	}

	private static int getMipmapLevel(String var0, BufferedImage var1) {
		int var2 = Math.min(var1.getWidth(), var1.getHeight());
		int var3 = getMinSize(var0);
		int var4;

		for (var4 = 0; var2 >= var3 && var4 < maxMipmapLevel; ++var4) {
			var2 >>= 1;
		}

		return var4;
	}

	private static int getMinSize(String var0) {
		return !var0.equals("/terrain.png") && !var0.startsWith("/ctm/") ? 2 : 32;
	}

	private static void resetOnOffTransparency(BufferedImage var0) {
		int var1 = var0.getWidth();
		int var2 = var0.getHeight();

		for (int var3 = 0; var3 < var1; ++var3) {
			for (int var4 = 0; var4 < var2; ++var4) {
				int var5 = var0.getRGB(var3, var4);
				int var6 = var5 >>> 24;

				if (var6 <= 127) {
					var5 &= 16777215;
				} else {
					var5 |= -16777216;
				}

				var0.setRGB(var3, var4, var5);
			}
		}
	}

	private static void scaleHalf(IntBuffer var0, int var1, int var2, IntBuffer var3) {
		for (int var4 = 0; var4 < var1 / 2; ++var4) {
			for (int var5 = 0; var5 < var2 / 2; ++var5) {
				int var6 = var1 * 2 * var5 + 2 * var4;
				int var7 = var0.get(var6);
				int var8 = var0.get(var6 + 1);
				int var9 = var0.get(var6 + var1);
				int var10 = var0.get(var6 + var1 + 1);
				var3.put(var1 / 2 * var5 + var4, average4RGBA(var7, var8, var9, var10));
			}
		}
	}

	private static BufferedImage scaleHalf(BufferedImage var0) {
		int var1 = var0.getWidth();
		int var2 = var0.getHeight();
		BufferedImage var3 = new BufferedImage(var1 / 2, var2 / 2, 2);

		for (int var4 = 0; var4 < var1 / 2; ++var4) {
			for (int var5 = 0; var5 < var2 / 2; ++var5) {
				int var6 = Integer.rotateLeft(var0.getRGB(2 * var4, 2 * var5), 8);
				int var7 = Integer.rotateLeft(var0.getRGB(2 * var4 + 1, 2 * var5), 8);
				int var8 = Integer.rotateLeft(var0.getRGB(2 * var4, 2 * var5 + 1), 8);
				int var9 = Integer.rotateLeft(var0.getRGB(2 * var4 + 1, 2 * var5 + 1), 8);
				var3.setRGB(var4, var5, Integer.rotateRight(average4RGBA(var6, var7, var8, var9), 8));
			}
		}

		return var3;
	}

	private static int average4RGBA(int var0, int var1, int var2, int var3) {
		int var4 = var0 & 255;
		int var5 = var1 & 255;
		int var6 = var2 & 255;
		int var7 = var3 & 255;
		int var8 = var4 + var5 + var6 + var7;

		if (var8 != 0 && var8 != 1020) {
			int var9 = var8 >> 2;

			for (int var10 = 8; var10 < 32; var10 += 8) {
				int var11 = (var4 * (var0 >> var10 & 255) + var5 * (var1 >> var10 & 255) + var6 * (var2 >> var10 & 255) + var7 * (var3 >> var10 & 255)) / var8;
				var9 |= var11 << var10;
			}

			return var9;
		} else {
			return average2RGBA(average2RGBA(var0, var3), average2RGBA(var1, var2)) | var8 >>> 2;
		}
	}

	private static int average2RGBA(int var0, int var1) {
		return ((var0 & -16843010) >>> 1) + ((var1 & -16843010) >>> 1);
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
			anisoLevel = Math.max(Math.min(MCPatcherUtils.getInt("HD Textures", "anisotropicFiltering", 1), anisoMax), 1);
		} else {
			anisoLevel = 1;
			anisoMax = 1;
		}

		lodSupported = GLContext.getCapabilities().GL_EXT_texture_lod_bias;

		if (lodSupported) {
			lodBias = MCPatcherUtils.getInt("HD Textures", "lodBias", 0);
		}

		logger.config("mipmap: supported=%s, enabled=%s, level=%d", new Object[] {Boolean.valueOf(mipmapSupported), Boolean.valueOf(mipmapEnabled), Integer.valueOf(maxMipmapLevel)});
		logger.config("anisotropic: supported=%s, level=%d, max=%d", new Object[] {Boolean.valueOf(anisoSupported), Integer.valueOf(anisoLevel), Integer.valueOf(anisoMax)});
		logger.config("lod bias: supported=%s, bias=%d", new Object[] {Boolean.valueOf(lodSupported), Integer.valueOf(lodBias)});
	}
}
