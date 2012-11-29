package net.minecraft.src;

import com.pclewis.mcpatcher.mod.TileSize; // Spout HD

public class TextureWaterFlowFX extends TextureFX {
	// Spout HD Start
	protected float[] field_76880_g;
	protected float[] field_76883_h;
	protected float[] field_76884_i;
	protected float[] field_76881_j;
	private int tickCounter;
	// Spout HD End

	public TextureWaterFlowFX() {
		super(Block.waterMoving.blockIndexInTexture + 1);
		// Spout HD Start
		this.field_76880_g = new float[TileSize.int_numPixels];
		this.field_76883_h = new float[TileSize.int_numPixels];
		this.field_76884_i = new float[TileSize.int_numPixels];
		this.field_76881_j = new float[TileSize.int_numPixels];
		this.tickCounter = 0;
		// Spout HD End
		this.tileSize = 2;
	}

	public void onTick() {
		++this.tickCounter;
		int var1;
		int var2;
		float var3;
		int var5;
		int var6;

		for (var1 = 0; var1 < TileSize.int_size; ++var1) { // Spout HD 
			for (var2 = 0; var2 < TileSize.int_size; ++var2) { // Spout HD 
				var3 = 0.0F;

				for (int var4 = var2 - 2; var4 <= var2; ++var4) {
					// Spout HD Start
					var5 = var1 & TileSize.int_sizeMinus1;
					var6 = var4 & TileSize.int_sizeMinus1;
					var3 += this.field_76880_g[var5 + var6 * TileSize.int_size];
					// Spout HD End
				}

				this.field_76883_h[var1 + var2 * TileSize.int_size] = var3 / 3.2F + this.field_76884_i[var1 + var2 * TileSize.int_size] * 0.8F; // Spout HD 
			}
		}

		// Spout HD 
		for (var1 = 0; var1 < TileSize.int_size; ++var1) {
			for (var2 = 0; var2 < TileSize.int_size; ++var2) {
				this.field_76884_i[var1 + var2 * TileSize.int_size] += this.field_76881_j[var1 + var2 * TileSize.int_size] * 0.05F;

				if (this.field_76884_i[var1 + var2 * TileSize.int_size] < 0.0F) {
					this.field_76884_i[var1 + var2 * TileSize.int_size] = 0.0F;
				}

				this.field_76881_j[var1 + var2 * TileSize.int_size] -= 0.3F;

				if (Math.random() < 0.2D) {
					this.field_76881_j[var1 + var2 * TileSize.int_size] = 0.5F;
					// Spout HD End
				}
			}
		}

		float[] var12 = this.field_76883_h;
		this.field_76883_h = this.field_76880_g;
		this.field_76880_g = var12;

		for (var2 = 0; var2 < TileSize.int_numPixels; ++var2) { // Spout HD 
			var3 = this.field_76880_g[var2 - this.tickCounter * TileSize.int_size & TileSize.int_numPixelsMinus1]; // Spout HD 

			if (var3 > 1.0F) {
				var3 = 1.0F;
			}

			if (var3 < 0.0F) {
				var3 = 0.0F;
			}

			float var13 = var3 * var3;
			var5 = (int)(32.0F + var13 * 32.0F);
			var6 = (int)(50.0F + var13 * 64.0F);
			int var7 = 255;
			int var8 = (int)(146.0F + var13 * 50.0F);

			if (this.anaglyphEnabled) {
				int var9 = (var5 * 30 + var6 * 59 + var7 * 11) / 100;
				int var10 = (var5 * 30 + var6 * 70) / 100;
				int var11 = (var5 * 30 + var7 * 70) / 100;
				var5 = var9;
				var6 = var10;
				var7 = var11;
			}

			this.imageData[var2 * 4 + 0] = (byte)var5;
			this.imageData[var2 * 4 + 1] = (byte)var6;
			this.imageData[var2 * 4 + 2] = (byte)var7;
			this.imageData[var2 * 4 + 3] = (byte)var8;
		}
	}
}
