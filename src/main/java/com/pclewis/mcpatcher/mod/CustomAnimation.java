package com.pclewis.mcpatcher.mod;

import com.pclewis.mcpatcher.MCLogger;
import com.pclewis.mcpatcher.MCPatcherUtils;
import com.pclewis.mcpatcher.TexturePackAPI;
import com.pclewis.mcpatcher.mod.CustomAnimation$Delegate;
import com.pclewis.mcpatcher.mod.CustomAnimation$Strip;
import com.pclewis.mcpatcher.mod.CustomAnimation$Tile;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;
import org.lwjgl.opengl.GL11;

public class CustomAnimation {
	private static final MCLogger logger = MCLogger.getLogger("HD Textures");
	private static final String CLASS_NAME = CustomAnimation.class.getSimpleName();
	private static Random rand = new Random();
	private static final ArrayList animations = new ArrayList();
	private final String textureName;
	private final String srcName;
	private final ByteBuffer imageData;
	private final int tileCount;
	private final int x;
	private final int y;
	private final int w;
	private final int h;
	private int currentFrame;
	private int currentDelay;
	private int numFrames;
	private CustomAnimation$Delegate delegate;

	public static void updateAll() {
		//FancyCompass.update();
		Iterator var0 = animations.iterator();

		while (var0.hasNext()) {
			CustomAnimation var1 = (CustomAnimation)var0.next();
			var1.update();
		}
	}

	static void clear() {
		animations.clear();
	}

	static void addStrip(Properties var0) {
		if (var0 != null) {
			try {
				String var1 = var0.getProperty("to", "");
				String var2 = var0.getProperty("from", "");
				int var3 = Integer.parseInt(var0.getProperty("tiles", "1"));
				int var4 = Integer.parseInt(var0.getProperty("x", ""));
				int var5 = Integer.parseInt(var0.getProperty("y", ""));
				int var6 = Integer.parseInt(var0.getProperty("w", ""));
				int var7 = Integer.parseInt(var0.getProperty("h", ""));

				if (!"".equals(var1) && !"".equals(var2)) {
					add(newStrip(var1, var3, var2, TexturePackAPI.getImage(var2), var4, var5, var6, var7, var0));
				}
			} catch (IOException var8) {
				;
			} catch (NumberFormatException var9) {
				;
			}
		}
	}

	static void addStripOrTile(String var0, String var1, int var2, int var3, int var4, int var5) {
		if (!addStrip(var0, var1, var2, var3)) {
			add(newTile(var0, var3, var2, var4, var5));
		}
	}

	static boolean addStrip(String var0, String var1, int var2, int var3) {
		String var4 = "/anim/custom_" + var1 + ".png";

		if (TexturePackAPI.hasResource(var4)) {
			try {
				BufferedImage var5 = TexturePackAPI.getImage(var4);

				if (var5 != null) {
					int var6 = getTileSize(var0);
					add(newStrip(var0, var3, var4, var5, var2 % 16 * var6, var2 / 16 * var6, var6, var6, (Properties)null));
					return true;
				}
			} catch (IOException var7) {
				var7.printStackTrace();
			}
		}

		return false;
	}

	private static void add(CustomAnimation var0) {
		if (var0 != null) {
			animations.add(var0);
			logger.fine("new %s %s %dx%d -> %s @ %d,%d (%d frames)", new Object[] {CLASS_NAME, var0.srcName, Integer.valueOf(var0.w), Integer.valueOf(var0.h), var0.textureName, Integer.valueOf(var0.x), Integer.valueOf(var0.y), Integer.valueOf(var0.numFrames)});
		}
	}

	private static CustomAnimation newStrip(String var0, int var1, String var2, BufferedImage var3, int var4, int var5, int var6, int var7, Properties var8) throws IOException {
		if (var3 == null) {
			logger.severe("%s: image %s not found in texture pack", new Object[] {CLASS_NAME, var2});
			return null;
		} else if (var4 >= 0 && var5 >= 0 && var6 > 0 && var7 > 0 && var1 > 0) {
			int var9 = MCPatcherUtils.getMinecraft().renderEngine.getTexture(var0);

			if (var9 <= 0) {
				logger.severe("%s: invalid id %d for texture %s", new Object[] {CLASS_NAME, Integer.valueOf(var9), var0});
				return null;
			} else {
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, var9);
				int var10 = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
				int var11 = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);

				if (var4 + var1 * var6 <= var10 && var5 + var1 * var7 <= var11) {
					int var12 = var3.getWidth();
					int var13 = var3.getHeight();

					if (var12 != var6) {
						var3 = TextureUtils.resizeImage(var3, var6);
						var12 = var3.getWidth();
						var13 = var3.getHeight();
					}

					if (var12 == var6 && var13 >= var7) {
						ByteBuffer var14 = ByteBuffer.allocateDirect(4 * var12 * var13);
						int[] var15 = new int[var12 * var13];
						byte[] var16 = new byte[4 * var12 * var13];
						var3.getRGB(0, 0, var12, var13, var15, 0, var12);
						ARGBtoRGBA(var15, var16);
						var14.put(var16);
						return new CustomAnimation(var2, var0, var1, var4, var5, var6, var7, var14, var13 / var7, var8);
					} else {
						logger.severe("%s: %s dimensions %dx%d do not match %dx%d", new Object[] {CLASS_NAME, var2, Integer.valueOf(var12), Integer.valueOf(var13), Integer.valueOf(var6), Integer.valueOf(var7)});
						return null;
					}
				} else {
					logger.severe("%s: %s invalid dimensions x=%d,y=%d,w=%d,h=%d,count=%d", new Object[] {CLASS_NAME, var2, Integer.valueOf(var4), Integer.valueOf(var5), Integer.valueOf(var6), Integer.valueOf(var7), Integer.valueOf(var1)});
					return null;
				}
			}
		} else {
			logger.severe("%s: %s invalid dimensions x=%d,y=%d,w=%d,h=%d,count=%d", new Object[] {CLASS_NAME, var2, Integer.valueOf(var4), Integer.valueOf(var5), Integer.valueOf(var6), Integer.valueOf(var7), Integer.valueOf(var1)});
			return null;
		}
	}

	private static CustomAnimation newTile(String var0, int var1, int var2, int var3, int var4) {
		int var5 = getTileSize(var0);
		int var6 = var2 % 16 * var5;
		int var7 = var2 / 16 * var5;
		int var8 = var5;
		int var9 = var5;

		if (var6 >= 0 && var7 >= 0 && var5 > 0 && var5 > 0 && var6 + var1 * var5 <= 16 * var5 && var7 + var1 * var5 <= 16 * var5) {
			try {
				return new CustomAnimation(var0, var1, var6, var7, var8, var9, var3, var4);
			} catch (IOException var11) {
				return null;
			}
		} else {
			logger.severe("%s: %s invalid dimensions x=%d,y=%d,w=%d,h=%d", new Object[] {CLASS_NAME, var0, Integer.valueOf(var6), Integer.valueOf(var7), Integer.valueOf(var5), Integer.valueOf(var5)});
			return null;
		}
	}

	private CustomAnimation(String var1, String var2, int var3, int var4, int var5, int var6, int var7, ByteBuffer var8, int var9, Properties var10) {
		this.srcName = var1;
		this.textureName = var2;
		this.tileCount = var3;
		this.x = var4;
		this.y = var5;
		this.w = var6;
		this.h = var7;
		this.imageData = var8;
		this.numFrames = var9;
		this.currentFrame = -1;
		this.delegate = new CustomAnimation$Strip(this, var10);
	}

	private CustomAnimation(String var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) throws IOException {
		this.srcName = var1;
		this.textureName = var1;
		this.tileCount = var2;
		this.x = var3;
		this.y = var4;
		this.w = var5;
		this.h = var6;
		this.imageData = ByteBuffer.allocateDirect(4 * var5 * var6);
		this.numFrames = var6;
		this.currentFrame = -1;
		this.delegate = new CustomAnimation$Tile(this, var7, var8);
	}

	void update() {
		int var1 = TexturePackAPI.getTextureIfLoaded(this.textureName);

		if (var1 >= 0) {
			if (--this.currentDelay <= 0) {
				if (++this.currentFrame >= this.numFrames) {
					this.currentFrame = 0;
				}

				for (int var2 = 0; var2 < this.tileCount; ++var2) {
					for (int var3 = 0; var3 < this.tileCount; ++var3) {
						this.delegate.update(var1, var2 * this.w, var3 * this.h);
					}
				}

				this.currentDelay = this.delegate.getDelay();
			}
		}
	}

	static void ARGBtoRGBA(int[] var0, byte[] var1) {
		for (int var2 = 0; var2 < var0.length; ++var2) {
			int var3 = var0[var2];
			var1[var2 * 4 + 3] = (byte)(var3 >> 24 & 255);
			var1[var2 * 4 + 0] = (byte)(var3 >> 16 & 255);
			var1[var2 * 4 + 1] = (byte)(var3 >> 8 & 255);
			var1[var2 * 4 + 2] = (byte)(var3 >> 0 & 255);
		}
	}

	static int getTileSize(String var0) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, MCPatcherUtils.getMinecraft().renderEngine.getTexture(var0));
		return GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH) / 16;
	}

	static String access$000(CustomAnimation var0) {
		return var0.textureName;
	}

	static int access$100(CustomAnimation var0) {
		return var0.w;
	}

	static int access$200(CustomAnimation var0) {
		return var0.h;
	}

	static int access$300(CustomAnimation var0) {
		return var0.x;
	}

	static int access$400(CustomAnimation var0) {
		return var0.y;
	}

	static ByteBuffer access$500(CustomAnimation var0) {
		return var0.imageData;
	}

	static int access$600(CustomAnimation var0) {
		return var0.currentFrame;
	}

	static Random access$700() {
		return rand;
	}

	static int access$800(CustomAnimation var0) {
		return var0.numFrames;
	}

	static String access$900(CustomAnimation var0) {
		return var0.srcName;
	}

	static int access$802(CustomAnimation var0, int var1) {
		return var0.numFrames = var1;
	}
}
