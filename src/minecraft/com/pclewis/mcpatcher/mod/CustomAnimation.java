package com.pclewis.mcpatcher.mod;

import com.pclewis.mcpatcher.mod.TextureUtils;
import com.pclewis.mcpatcher.mod.TileSize;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;
import net.minecraft.src.TextureFX;

public class CustomAnimation extends TextureFX {
	private CustomAnimation.Delegate delegate;
	private static Random rand = new Random();

	public CustomAnimation(int var1, int var2, int var3, String var4, int var5, int var6) {
		super(var1);
		this.iconIndex = var1;
		this.tileImage = var2;
		this.tileSize = var3;
		BufferedImage var7 = null;
		String var8 = var2 == 0?"/terrain.png":"/gui/items.png";
		String var9 = "/anim/custom_" + var4 + ".png";

		try {
			var7 = TextureUtils.getResourceAsBufferedImage(var9);
			if (var7 != null) {
				var8 = var9;
			}
		} catch (IOException var19) {
			;
		}

		//MCPatcherUtils.log("new CustomAnimation %s, src=%s, buffer size=0x%x, tile=%d", new Object[]{var4, var7 == null?"terrain.png":var8, Integer.valueOf(this.imageData.length), Integer.valueOf(this.iconIndex)});
		if (var7 == null) {
			this.delegate = new CustomAnimation.Tile(var8, var1, var5, var6);
		} else {
			Properties var10 = null;
			InputStream var11 = null;

			try {
				var11 = TextureUtils.getResourceAsStream(var9.replaceFirst("\\.png$", ".properties"));
				if (var11 != null) {
					var10 = new Properties();
					var10.load(var11);
				}
			} catch (IOException var17) {
				var17.printStackTrace();
			}
			finally {
				try {
					if (var11 != null) {
						var11.close();
					}
				} catch(IOException e) { }
			}

			this.delegate = new CustomAnimation.Strip(var7, var10);
		}
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

	public void onTick() {
		this.delegate.onTick();
	}

	private class Strip implements CustomAnimation.Delegate {
		private final int oneTile;
		private final byte[] src;
		private final int numTiles;
		private int[] tileOrder;
		private int[] tileDelay;
		private int numFrames;
		private int currentFrame;
		private int currentDelay;

		Strip(BufferedImage var2, Properties var3) {
			this.oneTile = TileSize.int_size * TileSize.int_size * 4;
			this.numFrames = this.numTiles = var2.getHeight() / var2.getWidth();
			int[] var4 = new int[var2.getWidth() * var2.getHeight()];
			var2.getRGB(0, 0, var2.getWidth(), var2.getHeight(), var4, 0, TileSize.int_size);
			this.src = new byte[var4.length * 4];
			CustomAnimation.ARGBtoRGBA(var4, this.src);
			this.loadProperties(var3);
			this.currentFrame = -1;
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

			for (var2 = 0; var2 < this.numFrames; ++var2) {
				this.tileDelay[var2] = 1;
			}

			this.loadTileDelay(var1);
		}

		private void loadTileOrder(Properties var1) {
			if (var1 != null) {
				int var2;
				for (var2 = 0; this.getIntValue(var1, "tile.", var2) != null; ++var2) {
					;
				}

				if (var2 > 0) {
					this.numFrames = var2;
					this.tileOrder = new int[this.numFrames];

					for (var2 = 0; var2 < this.numFrames; ++var2) {
						this.tileOrder[var2] = Math.abs(this.getIntValue(var1, "tile.", var2).intValue()) % this.numTiles;
					}
				}
			}
		}

		private void loadTileDelay(Properties var1) {
			if (var1 != null) {
				for (int var2 = 0; var2 < this.numFrames; ++var2) {
					Integer var3 = this.getIntValue(var1, "duration.", var2);
					if (var3 != null) {
						this.tileDelay[var2] = Math.max(var3.intValue(), 1);
					}
				}
			}
		}

		private Integer getIntValue(Properties var1, String var2, int var3) {
			try {
				String var4 = var1.getProperty(var2 + var3);
				if (var4 != null && var4.matches("^\\d+$")) {
					return Integer.valueOf(Integer.parseInt(var4));
				}
			} catch (NumberFormatException var5) {
				;
			}

			return null;
		}

		public void onTick() {
			if (--this.currentDelay <= 0) {
				if (++this.currentFrame >= this.numFrames) {
					this.currentFrame = 0;
				}

				System.arraycopy(this.src, this.tileOrder[this.currentFrame] * this.oneTile, CustomAnimation.this.imageData, 0, this.oneTile);
				this.currentDelay = this.tileDelay[this.currentFrame];
			}
		}
	}

	private class Tile implements CustomAnimation.Delegate {
		private final int allButOneRow;
		private final int oneRow;
		private final int minScrollDelay;
		private final int maxScrollDelay;
		private final boolean isScrolling;
		private final byte[] temp;
		private int timer;

		Tile(String var2, int var3, int var4, int var5) {
			this.oneRow = TileSize.int_size * 4;
			this.allButOneRow = (TileSize.int_size - 1) * this.oneRow;
			this.minScrollDelay = var4;
			this.maxScrollDelay = var5;
			this.isScrolling = this.minScrollDelay >= 0;
			if (this.isScrolling) {
				this.temp = new byte[this.oneRow];
			} else {
				this.temp = null;
			}

			BufferedImage var6;
			try {
				var6 = TextureUtils.getResourceAsBufferedImage(var2);
				}
				catch (IOException var10) {
				var10.printStackTrace();
				return;
			}

			int var7 = var3 % 16 * TileSize.int_size;
			int var8 = var3 / 16 * TileSize.int_size;
			int[] var9 = new int[TileSize.int_numPixels];
			var6.getRGB(var7, var8, TileSize.int_size, TileSize.int_size, var9, 0, TileSize.int_size);
			CustomAnimation.ARGBtoRGBA(var9, CustomAnimation.this.imageData);
		}

		public void onTick() {
			if (this.isScrolling && (this.maxScrollDelay <= 0 || --this.timer <= 0)) {
				if (this.maxScrollDelay > 0) {
					this.timer = CustomAnimation.rand.nextInt(this.maxScrollDelay - this.minScrollDelay + 1) + this.minScrollDelay;
				}

				System.arraycopy(CustomAnimation.this.imageData, this.allButOneRow, this.temp, 0, this.oneRow);
				System.arraycopy(CustomAnimation.this.imageData, 0, CustomAnimation.this.imageData, this.oneRow, this.allButOneRow);
				System.arraycopy(this.temp, 0, CustomAnimation.this.imageData, 0, this.oneRow);
			}
		}
	}

	private interface Delegate {
		void onTick();
	}
}
