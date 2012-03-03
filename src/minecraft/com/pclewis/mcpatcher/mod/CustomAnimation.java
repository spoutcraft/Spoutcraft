package com.pclewis.mcpatcher.mod;

import com.pclewis.mcpatcher.MCPatcherUtils;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

public class CustomAnimation {
	private static final String CLASS_NAME = CustomAnimation.class.getSimpleName();
	private static Random rand = new Random();
	private static final ArrayList animations = new ArrayList();
	private final String textureName;
	private final String srcName;
	private final int textureID;
	private final ByteBuffer f;
	private final int tileCount;
	private final int x;
	private final int y;
	private final int w;
	private final int h;
	private int currentFrame;
	private int currentDelay;
	private int numFrames;
	private CustomAnimation.Delegate delegate;

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
			int var9 = Minecraft.theMinecraft.renderEngine.getTexture(var0);
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

					if (var12 == var6 && var13 % var7 == 0) {
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
			int var9 = Minecraft.theMinecraft.renderEngine.getTexture(var0);
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
		this.f = var9;
		this.numFrames = var10;
		this.currentFrame = -1;
		this.delegate = new CustomAnimation.Strip(var11);
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
		this.f = ByteBuffer.allocateDirect(4 * var6 * var7);
		this.numFrames = var7;
		this.currentFrame = -1;
		this.delegate = new CustomAnimation.Tile(var8, var9);
	}

	void update() {
		if (--this.currentDelay <= 0) {
			if (++this.currentFrame >= this.numFrames) {
				this.currentFrame = 0;
			}

			for (int var1 = 0; var1 < this.tileCount; ++var1) {
				for (int var2 = 0; var2 < this.tileCount; ++var2) {
					this.delegate.update(var1 * TileSize.int_size, var2 * TileSize.int_size);
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

	private class Strip implements CustomAnimation.Delegate {
		private int[] tileOrder;
		private int[] tileDelay;
		private final int numTiles;

		Strip(Properties var2) {
			this.numTiles = CustomAnimation.this.numFrames;
			InputStream var3 = null;
			if (var2 == null) {
				try {
					var3 = TextureUtils.getResourceAsStream(CustomAnimation.this.srcName.replaceFirst("\\.png$", ".properties"));
					if (var3 != null) {
						var2 = new Properties();
						var2.load(var3);
					}
				} catch (IOException var8) {
					var8.printStackTrace();
				} finally {
					MCPatcherUtils.close((Closeable)var3);
				}
			}

			this.loadProperties(var2);
		}

		private void loadProperties(Properties var1) {
			this.loadTileOrder(var1);
			int var2;
			if (this.tileOrder == null) {
				this.tileOrder = new int[CustomAnimation.this.numFrames];

				for (var2 = 0; var2 < CustomAnimation.this.numFrames; ++var2) {
					this.tileOrder[var2] = var2 % this.numTiles;
				}
			}

			this.tileDelay = new int[CustomAnimation.this.numFrames];
			this.loadTileDelay(var1);

			for (var2 = 0; var2 < CustomAnimation.this.numFrames; ++var2) {
				this.tileDelay[var2] = Math.max(this.tileDelay[var2], 1);
			}
		}

		private void loadTileOrder(Properties var1) {
			if (var1 != null) {
				int var2;
				for (var2 = 0; this.getIntValue(var1, "tile.", var2) != null; ++var2) {
					;
				}

				if (var2 > 0) {
					CustomAnimation.this.numFrames = var2;
					this.tileOrder = new int[CustomAnimation.this.numFrames];

					for (var2 = 0; var2 < CustomAnimation.this.numFrames; ++var2) {
						this.tileOrder[var2] = Math.abs(this.getIntValue(var1, "tile.", var2).intValue()) % this.numTiles;
					}
				}
			}
		}

		private void loadTileDelay(Properties var1) {
			if (var1 != null) {
				Integer var2 = this.getIntValue(var1, "duration");

				for (int var3 = 0; var3 < CustomAnimation.this.numFrames; ++var3) {
					Integer var4 = this.getIntValue(var1, "duration.", var3);
					if (var4 != null) {
						this.tileDelay[var3] = var4.intValue();
					} else if (var2 != null) {
						this.tileDelay[var3] = var2.intValue();
					}
				}
			}
		}

		private Integer getIntValue(Properties var1, String var2) {
			try {
				String var3 = var1.getProperty(var2);
				if (var3 != null && var3.matches("^\\d+$")) {
					return Integer.valueOf(Integer.parseInt(var3));
				}
			} catch (NumberFormatException var4) {
				;
			}

			return null;
		}

		private Integer getIntValue(Properties var1, String var2, int var3) {
			return this.getIntValue(var1, var2 + var3);
		}

		public void update(int var1, int var2) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, CustomAnimation.this.textureID);
			GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, CustomAnimation.this.x + var1, CustomAnimation.this.y + var2, CustomAnimation.this.w, CustomAnimation.this.h, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)CustomAnimation.this.f.position(4 * CustomAnimation.this.w * CustomAnimation.this.h * this.tileOrder[CustomAnimation.this.currentFrame]));
		}

		public int getDelay() {
			return this.tileDelay[CustomAnimation.this.currentFrame];
		}
	}

	private class Tile implements CustomAnimation.Delegate {
		private final int minScrollDelay;
		private final int maxScrollDelay;
		private final boolean isScrolling;

		Tile(int var2, int var3) throws IOException {
			this.minScrollDelay = var2;
			this.maxScrollDelay = var3;
			this.isScrolling = this.minScrollDelay >= 0;
			BufferedImage var4 = TextureUtils.getResourceAsBufferedImage(CustomAnimation.this.textureName);
			int[] var5 = new int[CustomAnimation.this.w * CustomAnimation.this.h];
			byte[] var6 = new byte[4 * CustomAnimation.this.w * CustomAnimation.this.h];
			var4.getRGB(CustomAnimation.this.x, CustomAnimation.this.y, CustomAnimation.this.w, CustomAnimation.this.h, var5, 0, CustomAnimation.this.w);
			CustomAnimation.ARGBtoRGBA(var5, var6);
			CustomAnimation.this.f.put(var6);
		}

		public void update(int var1, int var2) {
			if (this.isScrolling) {
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, CustomAnimation.this.textureID);
				int var3 = CustomAnimation.this.h - CustomAnimation.this.currentFrame;
				GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, CustomAnimation.this.x + var1, CustomAnimation.this.y + var2 + CustomAnimation.this.h - var3, CustomAnimation.this.w, var3, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)CustomAnimation.this.f.position(0));
				GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, CustomAnimation.this.x + var1, CustomAnimation.this.y + var2, CustomAnimation.this.w, CustomAnimation.this.h - var3, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)CustomAnimation.this.f.position(4 * CustomAnimation.this.w * var3));
			}
		}

		public int getDelay() {
			return this.maxScrollDelay > 0?CustomAnimation.rand.nextInt(this.maxScrollDelay - this.minScrollDelay + 1) + this.minScrollDelay:0;
		}
	}

	private interface Delegate {
		void update(int var1, int var2);

		int getDelay();
	}
}
