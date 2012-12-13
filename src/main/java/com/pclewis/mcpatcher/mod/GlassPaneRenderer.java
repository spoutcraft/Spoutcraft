package com.pclewis.mcpatcher.mod;

import net.minecraft.src.Block;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;

public class GlassPaneRenderer {
	public static boolean active;
	private static RenderBlocks renderBlocks;
	private static Block blockPane;
	private static int i;
	private static int j;
	private static int k;
	private static double u0;
	private static double uM;
	private static double u1;
	private static double v0;
	private static double v1;

	public static void render(RenderBlocks var0, int var1, Block var2, int var3, int var4, int var5, boolean var6, boolean var7, boolean var8, boolean var9) {
		if (CTMUtils.active && var1 < 0 && Tessellator.instance instanceof SuperTessellator && CTMUtils.getConnectedTexture(var0, var0.blockAccess, var2, 148, var3, var4, var5, 0)) {
			renderBlocks = var0;
			blockPane = var2;
			i = var3;
			j = var4;
			k = var5;
			double var10 = (double)var3;
			double var12 = var10 + 0.5D;
			double var14 = var10 + 1.0D;
			double var16 = (double)var4;
			double var18 = var16 + 1.0D;
			double var20 = (double)var5;
			double var22 = var20 + 0.5D;
			double var24 = var20 + 1.0D;
			Tessellator var26 = CTMUtils.newTessellator;
			active = true;
			boolean var27 = var8 || var9 || var6 || var7;

			if ((!var9 || !var8) && var27) {
				if (var8 && !var9) {
					setupTileCoords(3);
					var26.addVertexWithUV(var10, var18, var22, u0, v0);
					var26.addVertexWithUV(var10, var16, var22, u0, v1);
					var26.addVertexWithUV(var12, var16, var22, uM, v1);
					var26.addVertexWithUV(var12, var18, var22, uM, v0);
					setupTileCoords(2);
					var26.addVertexWithUV(var12, var18, var22, uM, v0);
					var26.addVertexWithUV(var12, var16, var22, uM, v1);
					var26.addVertexWithUV(var10, var16, var22, u1, v1);
					var26.addVertexWithUV(var10, var18, var22, u1, v0);
				} else if (!var8 && var9) {
					setupTileCoords(3);
					var26.addVertexWithUV(var12, var18, var22, uM, v0);
					var26.addVertexWithUV(var12, var16, var22, uM, v1);
					var26.addVertexWithUV(var14, var16, var22, u1, v1);
					var26.addVertexWithUV(var14, var18, var22, u1, v0);
					setupTileCoords(2);
					var26.addVertexWithUV(var14, var18, var22, u0, v0);
					var26.addVertexWithUV(var14, var16, var22, u0, v1);
					var26.addVertexWithUV(var12, var16, var22, uM, v1);
					var26.addVertexWithUV(var12, var18, var22, uM, v0);
				}
			} else {
				setupTileCoords(3);
				var26.addVertexWithUV(var10, var18, var22, u0, v0);
				var26.addVertexWithUV(var10, var16, var22, u0, v1);
				var26.addVertexWithUV(var14, var16, var22, u1, v1);
				var26.addVertexWithUV(var14, var18, var22, u1, v0);
				setupTileCoords(2);
				var26.addVertexWithUV(var14, var18, var22, u0, v0);
				var26.addVertexWithUV(var14, var16, var22, u0, v1);
				var26.addVertexWithUV(var10, var16, var22, u1, v1);
				var26.addVertexWithUV(var10, var18, var22, u1, v0);
			}

			if ((!var6 || !var7) && var27) {
				if (var6 && !var7) {
					setupTileCoords(4);
					var26.addVertexWithUV(var12, var18, var20, u0, v0);
					var26.addVertexWithUV(var12, var16, var20, u0, v1);
					var26.addVertexWithUV(var12, var16, var22, uM, v1);
					var26.addVertexWithUV(var12, var18, var22, uM, v0);
					setupTileCoords(5);
					var26.addVertexWithUV(var12, var18, var22, uM, v0);
					var26.addVertexWithUV(var12, var16, var22, uM, v1);
					var26.addVertexWithUV(var12, var16, var20, u1, v1);
					var26.addVertexWithUV(var12, var18, var20, u1, v0);
				} else if (!var6 && var7) {
					setupTileCoords(4);
					var26.addVertexWithUV(var12, var18, var22, uM, v0);
					var26.addVertexWithUV(var12, var16, var22, uM, v1);
					var26.addVertexWithUV(var12, var16, var24, u1, v1);
					var26.addVertexWithUV(var12, var18, var24, u1, v0);
					setupTileCoords(5);
					var26.addVertexWithUV(var12, var18, var24, uM, v0);
					var26.addVertexWithUV(var12, var16, var24, uM, v1);
					var26.addVertexWithUV(var12, var16, var22, u1, v1);
					var26.addVertexWithUV(var12, var18, var22, u1, v0);
				}
			} else {
				setupTileCoords(4);
				var26.addVertexWithUV(var12, var18, var20, u0, v0);
				var26.addVertexWithUV(var12, var16, var20, u0, v1);
				var26.addVertexWithUV(var12, var16, var24, u1, v1);
				var26.addVertexWithUV(var12, var18, var24, u1, v0);
				setupTileCoords(5);
				var26.addVertexWithUV(var12, var18, var24, u0, v0);
				var26.addVertexWithUV(var12, var16, var24, u0, v1);
				var26.addVertexWithUV(var12, var16, var20, u1, v1);
				var26.addVertexWithUV(var12, var18, var20, u1, v0);
			}
		} else {
			active = false;
			renderBlocks = null;
			blockPane = null;
		}
	}

	private static void setupTileCoords(int var0) {
		int var1 = CTMUtils.lastOverride.getTile(renderBlocks, renderBlocks.blockAccess, blockPane, 49, i, j, k, var0);
		int var2 = (var1 & 15) << 4;
		int var3 = var1 & 240;
		u0 = (double)((float)var2 / 256.0F);
		uM = (double)(((float)var2 + 7.99F) / 256.0F);
		u1 = (double)(((float)var2 + 15.99F) / 256.0F);
		v0 = (double)((float)var3 / 256.0F);
		v1 = (double)(((float)var3 + 15.99F) / 256.0F);
	}
}
