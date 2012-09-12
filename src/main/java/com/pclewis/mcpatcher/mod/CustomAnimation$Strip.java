package com.pclewis.mcpatcher.mod;

import com.pclewis.mcpatcher.MCPatcherUtils;
import com.pclewis.mcpatcher.mod.CustomAnimation$Delegate;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Properties;
import org.lwjgl.opengl.GL11;

class CustomAnimation$Strip implements CustomAnimation$Delegate {
	private int[] tileOrder;
	private int[] tileDelay;
	private final int numTiles;

	final CustomAnimation this$0;

	CustomAnimation$Strip(CustomAnimation var1, Properties var2) {
		this.this$0 = var1;
		this.numTiles = CustomAnimation.access$900(var1);
		InputStream var3 = null;

		if (var2 == null) {
			try {
				var3 = TextureUtils.getResourceAsStream(CustomAnimation.access$1000(var1).replaceFirst("\\.png$", ".properties"));

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
			this.tileOrder = new int[CustomAnimation.access$900(this.this$0)];

			for (var2 = 0; var2 < CustomAnimation.access$900(this.this$0); ++var2) {
				this.tileOrder[var2] = var2 % this.numTiles;
			}
		}

		this.tileDelay = new int[CustomAnimation.access$900(this.this$0)];
		this.loadTileDelay(var1);

		for (var2 = 0; var2 < CustomAnimation.access$900(this.this$0); ++var2) {
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
				CustomAnimation.access$902(this.this$0, var2);
				this.tileOrder = new int[CustomAnimation.access$900(this.this$0)];

				for (var2 = 0; var2 < CustomAnimation.access$900(this.this$0); ++var2) {
					this.tileOrder[var2] = Math.abs(this.getIntValue(var1, "tile.", var2).intValue()) % this.numTiles;
				}
			}
		}
	}

	private void loadTileDelay(Properties var1) {
		if (var1 != null) {
			Integer var2 = this.getIntValue(var1, "duration");

			for (int var3 = 0; var3 < CustomAnimation.access$900(this.this$0); ++var3) {
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
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, CustomAnimation.access$600(this.this$0));
		GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, CustomAnimation.access$300(this.this$0) + var1, CustomAnimation.access$400(this.this$0) + var2, CustomAnimation.access$100(this.this$0), CustomAnimation.access$200(this.this$0), GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)CustomAnimation.access$500(this.this$0).position(4 * CustomAnimation.access$100(this.this$0) * CustomAnimation.access$200(this.this$0) * this.tileOrder[CustomAnimation.access$700(this.this$0)]));
	}

	public int getDelay() {
		return this.tileDelay[CustomAnimation.access$700(this.this$0)];
	}
}
