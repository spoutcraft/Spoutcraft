package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.TexturePackAPI;
import com.prupe.mcpatcher.TexturePackChangeHandler;
import com.prupe.mcpatcher.mod.CustomAnimation$1;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class CustomAnimation {
	private static final boolean enable = Config.getBoolean("Extended HD", "animations", true);
	private static final ArrayList animations = new ArrayList();
	private final String propertiesName;
	private final String dstName;
	private final String srcName;
	private final int mipmapLevel;
	private final ByteBuffer imageData;
	private final int x;
	private final int y;
	private final int w;
	private final int h;
	private int currentFrame;
	private int currentDelay;
	private int numFrames;
	private int[] tileOrder;
	private int[] tileDelay;
	private final int numTiles;
	private boolean error;

	public static void updateAll() {
		FancyDial.updateAll();
		Iterator var0 = animations.iterator();

		while (var0.hasNext()) {
			CustomAnimation var1 = (CustomAnimation)var0.next();
			var1.update();
		}

		FancyDial.postUpdateAll();
	}

	static void addStrip(String var0) {
		Properties var1 = TexturePackAPI.getProperties(var0);

		if (var1 != null) {
			String var2 = var1.getProperty("to", "");
			String var3 = var1.getProperty("from", "");
			int var4 = MCPatcherUtils.getIntProperty(var1, "x", 0);
			int var5 = MCPatcherUtils.getIntProperty(var1, "y", 0);
			int var6 = MCPatcherUtils.getIntProperty(var1, "w", 0);
			int var7 = MCPatcherUtils.getIntProperty(var1, "h", 0);

			if (!"".equals(var2) && !"".equals(var3)) {
				newStrip(var0, var1, var2, var3, TexturePackAPI.getImage(var3), var4, var5, var6, var7);
			}
		}
	}

	private static void add(CustomAnimation var0) {
		if (var0 != null) {
			animations.add(var0);
		}
	}

	private static void newStrip(String var0, Properties var1, String var2, String var3, BufferedImage var4, int var5, int var6, int var7, int var8) {
		if (!var2.equals("/terrain.png") && !var2.equals("/gui/items.png")) {
			if (var5 >= 0 && var6 >= 0 && var7 > 0 && var8 > 0) {
				TexturePackAPI.bindTexture(var2);
				int var9 = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
				int var10 = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
				int var11 = MipmapHelper.getMipmapLevels();

				if (var5 + var7 <= var9 && var6 + var8 <= var10) {
					int var12 = var4.getWidth();
					int var13 = var4.getHeight();

					if (var12 != var7) {
						var4 = resizeImage(var4, var7);
						var12 = var4.getWidth();
						var13 = var4.getHeight();
					}

					if (var12 == var7 && var13 >= var8) {
						ByteBuffer var14 = ByteBuffer.allocateDirect(4 * var12 * var13);
						int[] var15 = new int[var12 * var13];
						byte[] var16 = new byte[4 * var12 * var13];
						var4.getRGB(0, 0, var12, var13, var15, 0, var12);
						ARGBtoRGBA(var15, var16);
						var14.put(var16).flip();

						for (int var17 = 0; var17 <= var11; ++var17) {
							add(new CustomAnimation(var0, var3, var2, var17, var5, var6, var7, var8, var14, var13 / var8, var1));

							if (((var5 | var6 | var7 | var8) & 1) != 0 || var7 <= 0 || var8 <= 0) {
								break;
							}

							ByteBuffer var18 = ByteBuffer.allocateDirect(var12 * var13);
							MipmapHelper.scaleHalf(var14.asIntBuffer(), var12, var13, var18.asIntBuffer(), 0);
							var14 = var18;
							var12 >>= 1;
							var13 >>= 1;
							var5 >>= 1;
							var6 >>= 1;
							var7 >>= 1;
							var8 >>= 1;
						}
					}
				}
			}
		}
	}

	private CustomAnimation(String var1, String var2, String var3, int var4, int var5, int var6, int var7, int var8, ByteBuffer var9, int var10, Properties var11) {
		this.propertiesName = var1;
		this.srcName = var2;
		this.dstName = var3;
		this.mipmapLevel = var4;
		this.x = var5;
		this.y = var6;
		this.w = var7;
		this.h = var8;
		this.imageData = var9;
		this.numFrames = var10;
		this.currentFrame = -1;
		this.numTiles = var10;

		if (var11 == null) {
			var11 = TexturePackAPI.getProperties(var2.replace(".png", ".properties"));
		}

		this.loadProperties(var11);
	}

	void update() {
		if (!this.error) {
			int var1 = TexturePackAPI.getTextureIfLoaded(this.dstName);

			if (var1 >= 0) {
				if (--this.currentDelay <= 0) {
					if (++this.currentFrame >= this.numFrames) {
						this.currentFrame = 0;
					}

					TexturePackAPI.bindTexture(var1);
					this.update(var1, 0, 0);
					int var2 = GL11.glGetError();

					if (var2 != 0) {
						this.error = true;
					} else {
						this.currentDelay = this.getDelay();
					}
				}
			}
		}
	}

	public String toString() {
		return String.format("CustomAnimation{%s %s %dx%d -> %s%s @ %d,%d (%d frames)}", new Object[] {this.propertiesName, this.srcName, Integer.valueOf(this.w), Integer.valueOf(this.h), this.dstName, this.mipmapLevel > 0 ? "#" + this.mipmapLevel : "", Integer.valueOf(this.x), Integer.valueOf(this.y), Integer.valueOf(this.numFrames)});
	}

	private static void ARGBtoRGBA(int[] var0, byte[] var1) {
		for (int var2 = 0; var2 < var0.length; ++var2) {
			int var3 = var0[var2];
			var1[var2 * 4 + 3] = (byte)(var3 >> 24 & 255);
			var1[var2 * 4 + 0] = (byte)(var3 >> 16 & 255);
			var1[var2 * 4 + 1] = (byte)(var3 >> 8 & 255);
			var1[var2 * 4 + 2] = (byte)(var3 >> 0 & 255);
		}
	}

	private static BufferedImage resizeImage(BufferedImage var0, int var1) {
		if (var1 == var0.getWidth()) {
			return var0;
		} else {
			int var2 = var0.getHeight() * var1 / var0.getWidth();
			BufferedImage var3 = new BufferedImage(var1, var2, 2);
			Graphics2D var4 = var3.createGraphics();
			var4.drawImage(var0, 0, 0, var1, var2, (ImageObserver)null);
			return var3;
		}
	}

	private void loadProperties(Properties var1) {
		this.loadTileOrder(var1);
		int var2;

		if (this.tileOrder == null) {
			this.tileOrder = new int[this.numFrames];

			for (var2 = 0; var2 < this.numFrames; ++var2) {
				this.tileOrder[var2] = var2 % this.numTiles;
			}
		}

		this.tileDelay = new int[this.numFrames];
		this.loadTileDelay(var1);

		for (var2 = 0; var2 < this.numFrames; ++var2) {
			this.tileDelay[var2] = Math.max(this.tileDelay[var2], 1);
		}
	}

	private void loadTileOrder(Properties var1) {
		if (var1 != null) {
			int var2;

			for (var2 = 0; getIntValue(var1, "tile.", var2) != null; ++var2) {
				;
			}

			if (var2 > 0) {
				this.numFrames = var2;
				this.tileOrder = new int[this.numFrames];

				for (var2 = 0; var2 < this.numFrames; ++var2) {
					this.tileOrder[var2] = Math.abs(getIntValue(var1, "tile.", var2).intValue()) % this.numTiles;
				}
			}
		}
	}

	private void loadTileDelay(Properties var1) {
		if (var1 != null) {
			Integer var2 = getIntValue(var1, "duration");

			for (int var3 = 0; var3 < this.numFrames; ++var3) {
				Integer var4 = getIntValue(var1, "duration.", var3);

				if (var4 != null) {
					this.tileDelay[var3] = var4.intValue();
				} else if (var2 != null) {
					this.tileDelay[var3] = var2.intValue();
				}
			}
		}
	}

	private static Integer getIntValue(Properties var0, String var1) {
		try {
			String var2 = var0.getProperty(var1);

			if (var2 != null && var2.matches("^\\d+$")) {
				return Integer.valueOf(Integer.parseInt(var2));
			}
		} catch (NumberFormatException var3) {
			;
		}

		return null;
	}

	private static Integer getIntValue(Properties var0, String var1, int var2) {
		return getIntValue(var0, var1 + var2);
	}

	private void update(int var1, int var2, int var3) {
		GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, this.mipmapLevel, this.x + var2, this.y + var3, this.w, this.h, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)this.imageData.position(4 * this.w * this.h * this.tileOrder[this.currentFrame]));
	}

	private int getDelay() {
		return this.tileDelay[this.currentFrame];
	}

	static ArrayList access$000() {
		return animations;
	}

	static boolean access$100() {
		return enable;
	}

	static String access$300(CustomAnimation var0) {
		return var0.dstName;
	}

	static {
		TexturePackChangeHandler.register(new CustomAnimation$1("Extended HD", 1));
	}
}
