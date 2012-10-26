package com.pclewis.mcpatcher.mod;

import com.pclewis.mcpatcher.MCPatcherUtils;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;
import org.lwjgl.opengl.GL11;

public class CustomAnimation {
	private static Random rand = new Random();
	private static final ArrayList animations = new ArrayList();
	private final String textureName;
	private final String srcName;
	private final int textureID;
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
		try {
			String var1 = var0.getProperty("to", "");
			String var2 = var0.getProperty("from", "");
			int var3 = Integer.parseInt(var0.getProperty("tiles", "1"));
			int var4 = Integer.parseInt(var0.getProperty("x", ""));
			int var5 = Integer.parseInt(var0.getProperty("y", ""));
			int var6 = Integer.parseInt(var0.getProperty("w", ""));
			int var7 = Integer.parseInt(var0.getProperty("h", ""));

			if (!"".equals(var1) && !"".equals(var2)) {
				add(newStrip(var1, var3, var2, TextureUtils.getResourceAsBufferedImage(var2), var4, var5, var6, var7, var0));
			}
		} catch (IOException var8) {
			;
		} catch (NumberFormatException var9) {
			;
		}
	}

	static void addStripOrTile(String var0, String var1, int var2, int var3, int var4, int var5) {
		if (!addStrip(var0, var1, var2, var3)) {
			add(newTile(var0, var3, var2, var4, var5));
		}
	}

	static boolean addStrip(String var0, String var1, int var2, int var3) {
		String var4 = "/anim/custom_" + var1 + ".png";

		if (TextureUtils.hasResource(var4)) {
			try {
				BufferedImage var5 = TextureUtils.getResourceAsBufferedImage(var4);

				if (var5 != null) {
					add(newStrip(var0, var3, var4, var5, var2 % 16 * TileSize.int_size, var2 / 16 * TileSize.int_size, TileSize.int_size, TileSize.int_size, (Properties)null));
					return true;
				}
			} catch (IOException var6) {
				var6.printStackTrace();
			}
		}

		return false;
	}

	private static void add(CustomAnimation var0) {
		if (var0 != null) {
			animations.add(var0);
		}
	}

	private static CustomAnimation newStrip(String var0, int var1, String var2, BufferedImage var3, int var4, int var5, int var6, int var7, Properties var8) throws IOException {
		if (var4 >= 0 && var5 >= 0 && var6 > 0 && var7 > 0 && var1 > 0) {
			int var9 = MCPatcherUtils.getMinecraft().renderEngine.getTexture(var0);

			if (var9 <= 0) {
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
						return new CustomAnimation(var2, var0, var9, var1, var4, var5, var6, var7, var14, var13 / var7, var8);
					} else {
						return null;
					}
				} else {
					return null;
				}
			}
		} else {
			return null;
		}
	}

	private static CustomAnimation newTile(String var0, int var1, int var2, int var3, int var4) {
		int var5 = var2 % 16 * TileSize.int_size;
		int var6 = var2 / 16 * TileSize.int_size;
		int var7 = TileSize.int_size;
		int var8 = TileSize.int_size;

		if (var5 >= 0 && var6 >= 0 && var7 > 0 && var8 > 0 && var5 + var1 * var7 <= 16 * TileSize.int_size && var6 + var1 * var8 <= 16 * TileSize.int_size) {
			int var9 = MCPatcherUtils.getMinecraft().renderEngine.getTexture(var0);

			if (var9 <= 0) {
				return null;
			} else {
				try {
					return new CustomAnimation(var0, var9, var1, var5, var6, var7, var8, var3, var4);
				} catch (IOException var11) {
					return null;
				}
			}
		} else {
			return null;
		}
	}

	private CustomAnimation(String var1, String var2, int var3, int var4, int var5, int var6, int var7, int var8, ByteBuffer var9, int var10, Properties var11) {
		this.srcName = var1;
		this.textureName = var2;
		this.textureID = var3;
		this.tileCount = var4;
		this.x = var5;
		this.y = var6;
		this.w = var7;
		this.h = var8;
		this.imageData = var9;
		this.numFrames = var10;
		this.currentFrame = -1;
		this.delegate = new CustomAnimation$Strip(this, var11);
	}

	private CustomAnimation(String var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) throws IOException {
		this.srcName = var1;
		this.textureName = var1;
		this.textureID = var2;
		this.tileCount = var3;
		this.x = var4;
		this.y = var5;
		this.w = var6;
		this.h = var7;
		this.imageData = ByteBuffer.allocateDirect(4 * var6 * var7);
		this.numFrames = var7;
		this.currentFrame = -1;
		this.delegate = new CustomAnimation$Tile(this, var8, var9);
	}

	void update() {
		if (--this.currentDelay <= 0) {
			if (++this.currentFrame >= this.numFrames) {
				this.currentFrame = 0;
			}

			for (int var1 = 0; var1 < this.tileCount; ++var1) {
				for (int var2 = 0; var2 < this.tileCount; ++var2) {
					this.delegate.update(var1 * this.w, var2 * this.h);
				}
			}

			this.currentDelay = this.delegate.getDelay();
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
		return var0.textureID;
	}

	static int access$700(CustomAnimation var0) {
		return var0.currentFrame;
	}

	static Random access$800() {
		return rand;
	}

	static int access$900(CustomAnimation var0) {
		return var0.numFrames;
	}

	static String access$1000(CustomAnimation var0) {
		return var0.srcName;
	}

	static int access$902(CustomAnimation var0, int var1) {
		return var0.numFrames = var1;
	}
}
