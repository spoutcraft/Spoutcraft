package net.minecraft.src;

// MCPatcher Start
import com.pclewis.mcpatcher.mod.TileSize;
// MCPatcher End

public class TextureFlamesFX extends TextureFX {
	// MCPatcher Start
	protected float[] field_76869_g;
	protected float[] field_76870_h;
	// MCPatcher End

	public TextureFlamesFX(int par1) {
		super(Block.fire.blockIndexInTexture + par1 * 16);
		// MCPatcher Start
		this.field_76869_g = new float[TileSize.int_flameArraySize];
		this.field_76870_h = new float[TileSize.int_flameArraySize];
		// MCPatcher End
	}

	public void onTick() {
		int var3;
		float var4;
		int var6;

		// MCPatcher Start
		for (int var1 = 0; var1 < TileSize.int_size; ++var1) {
			for (int var2 = 0; var2 < TileSize.int_flameHeight; ++var2) {
		// MCPatcher End
				var3 = 18;
				// MCPatcher Start
				var4 = this.field_76869_g[var1 + (var2 + 1) % TileSize.int_flameHeight * TileSize.int_size] * (float)var3;
				// MCPatcher End

				for (int var5 = var1 - 1; var5 <= var1 + 1; ++var5) {
					for (var6 = var2; var6 <= var2 + 1; ++var6) {
						// MCPatcher Start
						if (var5 >= 0 && var6 >= 0 && var5 < TileSize.int_size && var6 < TileSize.int_flameHeight) {
							var4 += this.field_76869_g[var5 + var6 * TileSize.int_size];
						// MCPatcher End
						}

						++var3;
					}
				}

				// MCPatcher Start
				this.field_76870_h[var1 + var2 * TileSize.int_size] = var4 / ((float)var3 * TileSize.float_flameNudge);

				if (var2 >= TileSize.int_flameHeightMinus1) {
					this.field_76870_h[var1 + var2 * TileSize.int_size] = (float)(Math.random() * Math.random() * Math.random() * 4.0D + Math.random() * 0.10000000149011612D + 0.20000000298023224D);
				// MCPatcher End
				}
			}
		}

		float[] var13 = this.field_76870_h;
		this.field_76870_h = this.field_76869_g;
		this.field_76869_g = var13;

		// MCPatcher Start
		for (var3 = 0; var3 < TileSize.int_numPixels; ++var3) {
		// MCPatcher End
			var4 = this.field_76869_g[var3] * 1.8F;

			if (var4 > 1.0F) {
				var4 = 1.0F;
			}

			if (var4 < 0.0F) {
				var4 = 0.0F;
			}

			var6 = (int)(var4 * 155.0F + 100.0F);
			int var7 = (int)(var4 * var4 * 255.0F);
			int var8 = (int)(var4 * var4 * var4 * var4 * var4 * var4 * var4 * var4 * var4 * var4 * 255.0F);
			short var9 = 255;

			if (var4 < 0.5F) {
				var9 = 0;
			}

			float var14 = (var4 - 0.5F) * 2.0F;

			if (this.anaglyphEnabled) {
				int var10 = (var6 * 30 + var7 * 59 + var8 * 11) / 100;
				int var11 = (var6 * 30 + var7 * 70) / 100;
				int var12 = (var6 * 30 + var8 * 70) / 100;
				var6 = var10;
				var7 = var11;
				var8 = var12;
			}

			this.imageData[var3 * 4 + 0] = (byte)var6;
			this.imageData[var3 * 4 + 1] = (byte)var7;
			this.imageData[var3 * 4 + 2] = (byte)var8;
			this.imageData[var3 * 4 + 3] = (byte)var9;
		}
	}
}
