package net.minecraft.src;
//Spout HD Start
import com.pclewis.mcpatcher.mod.TileSize;
//Spout HD End
import java.util.Random;

public class TexturePortalFX extends TextureFX {
	private int portalTickCounter = 0;
	//Spout HD Start
	private byte[][] portalTextureData;
	//Spout HD End

	public TexturePortalFX() {
		super(Block.portal.blockIndexInTexture);
		//Spout HD Start
		this.portalTextureData = new byte[32][TileSize.int_numBytes];
		//Spout HD End
		Random var1 = new Random(100L);

		for (int var2 = 0; var2 < 32; ++var2) {
			//Spout HD Start
			for (int var3 = 0; var3 < TileSize.int_size; ++var3) {
				for (int var4 = 0; var4 < TileSize.int_size; ++var4) {
					//Spout HD End
					float var5 = 0.0F;

					int var6;
					for (var6 = 0; var6 < 2; ++var6) {
						//Spout HD Start
						float var7 = (float)(var6 * TileSize.int_size) * 0.5F;
						float var8 = (float)(var6 * TileSize.int_size) * 0.5F;
						float var9 = ((float)var3 - var7) / TileSize.float_size * 2.0F;
						float var10 = ((float)var4 - var8) / TileSize.float_size * 2.0F;
						//Spout HD End
						if (var9 < -1.0F) {
							var9 += 2.0F;
						}

						if (var9 >= 1.0F) {
							var9 -= 2.0F;
						}

						if (var10 < -1.0F) {
							var10 += 2.0F;
						}

						if (var10 >= 1.0F) {
							var10 -= 2.0F;
						}

						float var11 = var9 * var9 + var10 * var10;
						float var12 = (float)Math.atan2((double)var10, (double)var9) + ((float)var2 / 32.0F * (float)Math.PI * 2.0F - var11 * 10.0F + (float)(var6 * 2)) * (float)(var6 * 2 - 1);
						var12 = (MathHelper.sin(var12) + 1.0F) / 2.0F;
						var12 /= var11 + 1.0F;
						var5 += var12 * 0.5F;
					}

					var5 += var1.nextFloat() * 0.1F;
					var6 = (int)(var5 * 100.0F + 155.0F);
					int var13 = (int)(var5 * var5 * 200.0F + 55.0F);
					int var14 = (int)(var5 * var5 * var5 * var5 * 255.0F);
					int var15 = (int)(var5 * 100.0F + 155.0F);
					//Spout HD Start
					int var16 = var4 * TileSize.int_size + var3;
					//Spout HD End
					this.portalTextureData[var2][var16 * 4 + 0] = (byte)var13;
					this.portalTextureData[var2][var16 * 4 + 1] = (byte)var14;
					this.portalTextureData[var2][var16 * 4 + 2] = (byte)var6;
					this.portalTextureData[var2][var16 * 4 + 3] = (byte)var15;
				}
			}
		}

	}

	public void onTick() {
		++this.portalTickCounter;
		byte[] var1 = this.portalTextureData[this.portalTickCounter & 31];
		//Spout HD Start
		for (int var2 = 0; var2 < TileSize.int_numPixels; ++var2) {
			//Spout HD End
			int var3 = var1[var2 * 4 + 0] & 255;
			int var4 = var1[var2 * 4 + 1] & 255;
			int var5 = var1[var2 * 4 + 2] & 255;
			int var6 = var1[var2 * 4 + 3] & 255;
			if (this.anaglyphEnabled) {
				int var7 = (var3 * 30 + var4 * 59 + var5 * 11) / 100;
				int var8 = (var3 * 30 + var4 * 70) / 100;
				int var9 = (var3 * 30 + var5 * 70) / 100;
				var3 = var7;
				var4 = var8;
				var5 = var9;
			}

			this.imageData[var2 * 4 + 0] = (byte)var3;
			this.imageData[var2 * 4 + 1] = (byte)var4;
			this.imageData[var2 * 4 + 2] = (byte)var5;
			this.imageData[var2 * 4 + 3] = (byte)var6;
		}
	}
}
