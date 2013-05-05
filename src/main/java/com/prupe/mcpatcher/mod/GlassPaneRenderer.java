package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.TessellatorUtils;
import java.util.Arrays;
import net.minecraft.src.Block;
import net.minecraft.src.Icon;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
// Spout Start
import org.spoutcraft.client.config.Configuration;
// Spout End

public class GlassPaneRenderer {
	// Spout Start
	private static final boolean enable = Configuration.isConnectedTextures();
	// Spout End
	public static boolean active;
	private static final Icon[] icons = new Icon[6];
	private static Tessellator tessellator;
	private static double u0;
	private static double uM;
	private static double u1;
	private static double v0;
	private static double v1;

	public static void render(RenderBlocks var0, Block var1, Icon var2, int var3, int var4, int var5, boolean var6, boolean var7, boolean var8, boolean var9) {
		if (!enable) {
			active = false;
		} else {
			active = true;

			for (int var10 = 2; var10 <= 5; ++var10) {
				icons[var10] = CTMUtils.getTile(var0, var1, var3, var4, var5, var10, var2, Tessellator.instance);

				if (icons[var10] == null) {
					active = RenderPassAPI.instance.skipDefaultRendering(var1);
					return;
				}
			}

			double var27 = (double)var3;
			double var12 = var27 + 0.5D;
			double var14 = var27 + 1.0D;
			double var16 = (double)var4;
			double var18 = var16 + 1.0D;
			double var20 = (double)var5;
			double var22 = var20 + 0.5D;
			double var24 = var20 + 1.0D;
			boolean var26 = var8 || var9 || var6 || var7;

			if ((!var9 || !var8) && var26) {
				if (var8 && !var9) {
					setupTileCoords(3);
					tessellator.addVertexWithUV(var27, var18, var22, uM, v0);
					tessellator.addVertexWithUV(var27, var16, var22, uM, v1);
					tessellator.addVertexWithUV(var12, var16, var22, u1, v1);
					tessellator.addVertexWithUV(var12, var18, var22, u1, v0);
					setupTileCoords(2);
					tessellator.addVertexWithUV(var12, var18, var22, u0, v0);
					tessellator.addVertexWithUV(var12, var16, var22, u0, v1);
					tessellator.addVertexWithUV(var27, var16, var22, uM, v1);
					tessellator.addVertexWithUV(var27, var18, var22, uM, v0);
				} else if (!var8 && var9) {
					setupTileCoords(3);
					tessellator.addVertexWithUV(var12, var18, var22, u0, v0);
					tessellator.addVertexWithUV(var12, var16, var22, u0, v1);
					tessellator.addVertexWithUV(var14, var16, var22, uM, v1);
					tessellator.addVertexWithUV(var14, var18, var22, uM, v0);
					setupTileCoords(2);
					tessellator.addVertexWithUV(var14, var18, var22, uM, v0);
					tessellator.addVertexWithUV(var14, var16, var22, uM, v1);
					tessellator.addVertexWithUV(var12, var16, var22, u1, v1);
					tessellator.addVertexWithUV(var12, var18, var22, u1, v0);
				}
			} else {
				setupTileCoords(3);
				tessellator.addVertexWithUV(var27, var18, var22, u0, v0);
				tessellator.addVertexWithUV(var27, var16, var22, u0, v1);
				tessellator.addVertexWithUV(var14, var16, var22, u1, v1);
				tessellator.addVertexWithUV(var14, var18, var22, u1, v0);
				setupTileCoords(2);
				tessellator.addVertexWithUV(var14, var18, var22, u0, v0);
				tessellator.addVertexWithUV(var14, var16, var22, u0, v1);
				tessellator.addVertexWithUV(var27, var16, var22, u1, v1);
				tessellator.addVertexWithUV(var27, var18, var22, u1, v0);
			}

			if ((!var6 || !var7) && var26) {
				if (var6 && !var7) {
					setupTileCoords(4);
					tessellator.addVertexWithUV(var12, var18, var20, uM, v0);
					tessellator.addVertexWithUV(var12, var16, var20, uM, v1);
					tessellator.addVertexWithUV(var12, var16, var22, u1, v1);
					tessellator.addVertexWithUV(var12, var18, var22, u1, v0);
					setupTileCoords(5);
					tessellator.addVertexWithUV(var12, var18, var22, u0, v0);
					tessellator.addVertexWithUV(var12, var16, var22, u0, v1);
					tessellator.addVertexWithUV(var12, var16, var20, uM, v1);
					tessellator.addVertexWithUV(var12, var18, var20, uM, v0);
				} else if (!var6 && var7) {
					setupTileCoords(4);
					tessellator.addVertexWithUV(var12, var18, var22, u0, v0);
					tessellator.addVertexWithUV(var12, var16, var22, u0, v1);
					tessellator.addVertexWithUV(var12, var16, var24, uM, v1);
					tessellator.addVertexWithUV(var12, var18, var24, uM, v0);
					setupTileCoords(5);
					tessellator.addVertexWithUV(var12, var18, var24, uM, v0);
					tessellator.addVertexWithUV(var12, var16, var24, uM, v1);
					tessellator.addVertexWithUV(var12, var16, var22, u1, v1);
					tessellator.addVertexWithUV(var12, var18, var22, u1, v0);
				}
			} else {
				setupTileCoords(4);
				tessellator.addVertexWithUV(var12, var18, var20, u0, v0);
				tessellator.addVertexWithUV(var12, var16, var20, u0, v1);
				tessellator.addVertexWithUV(var12, var16, var24, u1, v1);
				tessellator.addVertexWithUV(var12, var18, var24, u1, v0);
				setupTileCoords(5);
				tessellator.addVertexWithUV(var12, var18, var24, u0, v0);
				tessellator.addVertexWithUV(var12, var16, var24, u0, v1);
				tessellator.addVertexWithUV(var12, var16, var20, u1, v1);
				tessellator.addVertexWithUV(var12, var18, var20, u1, v0);
			}

			Arrays.fill(icons, (Object)null);
			tessellator = null;
		}
	}

	private static void setupTileCoords(int var0) {
		Icon var1 = icons[var0];
		tessellator = TessellatorUtils.getTessellator(Tessellator.instance, icons[var0]);
		u0 = (double)var1.getMinU();
		uM = (double)var1.getInterpolatedU(8.0D);
		u1 = (double)var1.getMaxU();
		v0 = (double)var1.getMinV();
		v1 = (double)var1.getMaxV();
	}
}
