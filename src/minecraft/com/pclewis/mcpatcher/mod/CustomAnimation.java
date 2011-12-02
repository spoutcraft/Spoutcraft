package com.pclewis.mcpatcher.mod;

import com.pclewis.mcpatcher.mod.TextureUtils;
import com.pclewis.mcpatcher.mod.TileSize;
import java.awt.image.BufferedImage;
import java.io.IOException;
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

		try {
         String var9 = "/custom_" + var4 + ".png";
         var7 = TextureUtils.getResourceAsBufferedImage(var9);
         if(var7 != null) {
            var8 = var9;
         }
      } catch (IOException var10) {
			;
		}

		//MCPatcherUtils.log("new CustomAnimation %s, src=%s, buffer size=0x%x, tile=%d", new Object[]{var4, var7 == null?"terrain.png":var8, Integer.valueOf(this.imageData.length), Integer.valueOf(this.iconIndex)});
		if(var7 == null) {
         this.delegate = new CustomAnimation.Tile(var8, var1, var5, var6);
		} else {
         this.delegate = new CustomAnimation.Strip(var7);
		}

	}

	private static void ARGBtoRGBA(int[] var0, byte[] var1) {
		for(int var2 = 0; var2 < var0.length; ++var2) {
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

      private final int oneFrame;
      private final byte[] src;
      private final int numFrames;
      private int currentFrame;


      Strip(BufferedImage var2) {
         this.oneFrame = TileSize.int_size * TileSize.int_size * 4;
         this.numFrames = var2.getHeight() / var2.getWidth();
         int[] var3 = new int[var2.getWidth() * var2.getHeight()];
         var2.getRGB(0, 0, var2.getWidth(), var2.getHeight(), var3, 0, TileSize.int_size);
         this.src = new byte[var3.length * 4];
         CustomAnimation.ARGBtoRGBA(var3, this.src);
      }

      public void onTick() {
         if(++this.currentFrame >= this.numFrames) {
            this.currentFrame = 0;
         }

         System.arraycopy(this.src, this.currentFrame * this.oneFrame, CustomAnimation.this.imageData, 0, this.oneFrame);
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
         if(this.isScrolling) {
            this.temp = new byte[this.oneRow];
         } else {
            this.temp = null;
         }

         BufferedImage var6;
         try {
            var6 = TextureUtils.getResourceAsBufferedImage(var2);
         } catch (IOException var10) {
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
         if(this.isScrolling && (this.maxScrollDelay <= 0 || --this.timer <= 0)) {
			if(this.maxScrollDelay > 0) {
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
