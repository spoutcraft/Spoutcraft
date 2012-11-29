package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

// Spout Start
import com.pclewis.mcpatcher.mod.TextureUtils;
import com.pclewis.mcpatcher.mod.TileSize;
// Spout End

import net.minecraft.client.Minecraft;

public class TextureCompassFX extends TextureFX {

	/** A reference to the Minecraft object. */
	private Minecraft mc;

	/** Holds the image of the compass from items.png in rgb format. */
	// Spout HD Start
	private int[] compassIconImageData; // Spout HD
	// Spout HD End
	public double field_76868_i;
	public double field_76866_j;
	public static TextureCompassFX field_82391_c;

	public TextureCompassFX(Minecraft par1Minecraft) {
		super(Item.compass.getIconFromDamage(0));
		this.compassIconImageData = new int[TileSize.int_numPixels]; // Spout HD
		this.mc = par1Minecraft;
		this.tileImage = 1;

		try {
			// Spout HD Start
			BufferedImage var2 = TextureUtils.getResourceAsBufferedImage((Object)Minecraft.class, "/gui/items.png");
			int var3 = this.iconIndex % 16 * TileSize.int_size;
			int var4 = this.iconIndex / 16 * TileSize.int_size;
			var2.getRGB(var3, var4, TileSize.int_size, TileSize.int_size, this.compassIconImageData, 0, TileSize.int_size);
			// Spout HD End
		} catch (IOException var5) {
			var5.printStackTrace();
		}

		field_82391_c = this;
	}

	public void onTick() {
		if (this.mc.theWorld != null && this.mc.thePlayer != null) {
			func_82390_a(this.mc.thePlayer.posX, this.mc.thePlayer.posZ, (double)this.mc.thePlayer.rotationYaw, false, false);
		} else {
			func_82390_a(0.0D, 0.0D, 0.0D, true, false);
		}
	}

	public static void func_82390_a(double par0, double par2, double par4, boolean par6, boolean par7) {
		int[] var8 = field_82391_c.compassIconImageData;
		byte[] var9 = field_82391_c.imageData;
		int var17;
		int var16;

			// Spout HD Start
		for (int var10 = 0; var10 < TileSize.int_numPixels; ++var10) {
			// Spout HD End
			int var11 = var8[var10] >> 24 & 255;
			int var12 = var8[var10] >> 16 & 255;
			int var13 = var8[var10] >> 8 & 255;
			int var14 = var8[var10] >> 0 & 255;

			if (field_82391_c.anaglyphEnabled) {
				int var15 = (var12 * 30 + var13 * 59 + var14 * 11) / 100;
				var16 = (var12 * 30 + var13 * 70) / 100;
				var17 = (var12 * 30 + var14 * 70) / 100;
				var12 = var15;
				var13 = var16;
				var14 = var17;
			}

			var9[var10 * 4 + 0] = (byte)var12;
			var9[var10 * 4 + 1] = (byte)var13;
			var9[var10 * 4 + 2] = (byte)var14;
			var9[var10 * 4 + 3] = (byte)var11;
		}

		double var27 = 0.0D;

		if (field_82391_c.mc.theWorld != null && !par6) {
			ChunkCoordinates var29 = field_82391_c.mc.theWorld.getSpawnPoint();
			double var28 = (double)var29.posX - par0;
			double var32 = (double)var29.posZ - par2;
			var27 = (par4 - 90.0D) * Math.PI / 180.0D - Math.atan2(var32, var28);

			if (!field_82391_c.mc.theWorld.provider.isSurfaceWorld()) {
				var27 = Math.random() * Math.PI * 2.0D;
			}
		}

		double var30;

		if (par7) {
			field_82391_c.field_76868_i = var27;
		} else {
			for (var30 = var27 - field_82391_c.field_76868_i; var30 < -Math.PI; var30 += (Math.PI * 2D)) {
				;
			}

			while (var30 >= Math.PI) {
				var30 -= (Math.PI * 2D);
			}

			if (var30 < -1.0D) {
				var30 = -1.0D;
			}

			if (var30 > 1.0D) {
				var30 = 1.0D;
			}

			field_82391_c.field_76866_j += var30 * 0.1D;
			field_82391_c.field_76866_j *= 0.8D;
			field_82391_c.field_76868_i += field_82391_c.field_76866_j;
		}

		var30 = Math.sin(field_82391_c.field_76868_i);
		double var31 = Math.cos(field_82391_c.field_76868_i);
		int var19;
		int var18;
		int var21;
		int var20;
		short var23;
		int var22;
		int var25;
		int var24;
		int var26;

		// Spout HD Start
		for (var16 = TileSize.int_compassCrossMin; var16 <= TileSize.int_compassCrossMax; ++var16) {
			var17 = (int)(TileSize.double_compassCenterMax + var31 * (double)var16 * 0.3D);
			var18 = (int)(TileSize.double_compassCenterMin - var30 * (double)var16 * 0.3D * 0.5D);
			var19 = var18 * TileSize.int_size + var17;
			// Spout HD End
			var20 = 100;
			var21 = 100;
			var22 = 100;
			var23 = 255;

			if (field_82391_c.anaglyphEnabled) {
				var24 = (var20 * 30 + var21 * 59 + var22 * 11) / 100;
				var25 = (var20 * 30 + var21 * 70) / 100;
				var26 = (var20 * 30 + var22 * 70) / 100;
				var20 = var24;
				var21 = var25;
				var22 = var26;
			}

			var9[var19 * 4 + 0] = (byte)var20;
			var9[var19 * 4 + 1] = (byte)var21;
			var9[var19 * 4 + 2] = (byte)var22;
			var9[var19 * 4 + 3] = (byte)var23;
		}

		for (var16 = -8; var16 <= 16; ++var16) {
			var17 = (int)(8.5D + var30 * (double)var16 * 0.3D);
			var18 = (int)(7.5D + var31 * (double)var16 * 0.3D * 0.5D);
			var19 = var18 * 16 + var17;
			var20 = var16 >= 0 ? 255 : 100;
			var21 = var16 >= 0 ? 20 : 100;
			var22 = var16 >= 0 ? 20 : 100;
			var23 = 255;

			if (field_82391_c.anaglyphEnabled) {
				var24 = (var20 * 30 + var21 * 59 + var22 * 11) / 100;
				var25 = (var20 * 30 + var21 * 70) / 100;
				var26 = (var20 * 30 + var22 * 70) / 100;
				var20 = var24;
				var21 = var25;
				var22 = var26;
			}

			var9[var19 * 4 + 0] = (byte)var20;
			var9[var19 * 4 + 1] = (byte)var21;
			var9[var19 * 4 + 2] = (byte)var22;
			var9[var19 * 4 + 3] = (byte)var23;
		}
	}
}
