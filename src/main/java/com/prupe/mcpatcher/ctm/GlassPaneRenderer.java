package com.prupe.mcpatcher.ctm;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.TessellatorUtils;
import java.util.Arrays;
import net.minecraft.src.Block;
import net.minecraft.src.Icon;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;

public class GlassPaneRenderer {
	private static final boolean enable = Config.getBoolean("Connected Textures", "glassPane", true);
	public static boolean active;
	private static final Icon[] icons = new Icon[6];
	private static Tessellator tessellator;
	private static double u0;
	private static double uM;
	private static double u1;
	private static double v0;
	private static double v1;

	public static void render(RenderBlocks renderBlocks, Block blockPane, Icon origIcon, int i, int j, int k, boolean connectNorth, boolean connectSouth, boolean connectWest, boolean connectEast) {
		if (!enable) {
			active = false;
		} else {
			active = true;

			for (int i0 = 2; i0 <= 5; ++i0) {
				icons[i0] = CTMUtils.getTile(renderBlocks, blockPane, i, j, k, i0, origIcon, Tessellator.instance);

				if (icons[i0] == null) {
					active = RenderPassAPI.instance.skipDefaultRendering(blockPane);
					return;
				}
			}

			double var27 = (double)i;
			double iM = var27 + 0.5D;
			double i1 = var27 + 1.0D;
			double j0 = (double)j;
			double j1 = j0 + 1.0D;
			double k0 = (double)k;
			double kM = k0 + 0.5D;
			double k1 = k0 + 1.0D;
			boolean connectAny = connectWest || connectEast || connectNorth || connectSouth;

			if ((!connectEast || !connectWest) && connectAny) {
				if (connectWest && !connectEast) {
					setupTileCoords(3);
					tessellator.addVertexWithUV(var27, j1, kM, uM, v0);
					tessellator.addVertexWithUV(var27, j0, kM, uM, v1);
					tessellator.addVertexWithUV(iM, j0, kM, u1, v1);
					tessellator.addVertexWithUV(iM, j1, kM, u1, v0);
					setupTileCoords(2);
					tessellator.addVertexWithUV(iM, j1, kM, u0, v0);
					tessellator.addVertexWithUV(iM, j0, kM, u0, v1);
					tessellator.addVertexWithUV(var27, j0, kM, uM, v1);
					tessellator.addVertexWithUV(var27, j1, kM, uM, v0);
				} else if (!connectWest && connectEast) {
					setupTileCoords(3);
					tessellator.addVertexWithUV(iM, j1, kM, u0, v0);
					tessellator.addVertexWithUV(iM, j0, kM, u0, v1);
					tessellator.addVertexWithUV(i1, j0, kM, uM, v1);
					tessellator.addVertexWithUV(i1, j1, kM, uM, v0);
					setupTileCoords(2);
					tessellator.addVertexWithUV(i1, j1, kM, uM, v0);
					tessellator.addVertexWithUV(i1, j0, kM, uM, v1);
					tessellator.addVertexWithUV(iM, j0, kM, u1, v1);
					tessellator.addVertexWithUV(iM, j1, kM, u1, v0);
				}
			} else {
				setupTileCoords(3);
				tessellator.addVertexWithUV(var27, j1, kM, u0, v0);
				tessellator.addVertexWithUV(var27, j0, kM, u0, v1);
				tessellator.addVertexWithUV(i1, j0, kM, u1, v1);
				tessellator.addVertexWithUV(i1, j1, kM, u1, v0);
				setupTileCoords(2);
				tessellator.addVertexWithUV(i1, j1, kM, u0, v0);
				tessellator.addVertexWithUV(i1, j0, kM, u0, v1);
				tessellator.addVertexWithUV(var27, j0, kM, u1, v1);
				tessellator.addVertexWithUV(var27, j1, kM, u1, v0);
			}

			if ((!connectNorth || !connectSouth) && connectAny) {
				if (connectNorth && !connectSouth) {
					setupTileCoords(4);
					tessellator.addVertexWithUV(iM, j1, k0, uM, v0);
					tessellator.addVertexWithUV(iM, j0, k0, uM, v1);
					tessellator.addVertexWithUV(iM, j0, kM, u1, v1);
					tessellator.addVertexWithUV(iM, j1, kM, u1, v0);
					setupTileCoords(5);
					tessellator.addVertexWithUV(iM, j1, kM, u0, v0);
					tessellator.addVertexWithUV(iM, j0, kM, u0, v1);
					tessellator.addVertexWithUV(iM, j0, k0, uM, v1);
					tessellator.addVertexWithUV(iM, j1, k0, uM, v0);
				} else if (!connectNorth && connectSouth) {
					setupTileCoords(4);
					tessellator.addVertexWithUV(iM, j1, kM, u0, v0);
					tessellator.addVertexWithUV(iM, j0, kM, u0, v1);
					tessellator.addVertexWithUV(iM, j0, k1, uM, v1);
					tessellator.addVertexWithUV(iM, j1, k1, uM, v0);
					setupTileCoords(5);
					tessellator.addVertexWithUV(iM, j1, k1, uM, v0);
					tessellator.addVertexWithUV(iM, j0, k1, uM, v1);
					tessellator.addVertexWithUV(iM, j0, kM, u1, v1);
					tessellator.addVertexWithUV(iM, j1, kM, u1, v0);
				}
			} else {
				setupTileCoords(4);
				tessellator.addVertexWithUV(iM, j1, k0, u0, v0);
				tessellator.addVertexWithUV(iM, j0, k0, u0, v1);
				tessellator.addVertexWithUV(iM, j0, k1, u1, v1);
				tessellator.addVertexWithUV(iM, j1, k1, u1, v0);
				setupTileCoords(5);
				tessellator.addVertexWithUV(iM, j1, k1, u0, v0);
				tessellator.addVertexWithUV(iM, j0, k1, u0, v1);
				tessellator.addVertexWithUV(iM, j0, k0, u1, v1);
				tessellator.addVertexWithUV(iM, j1, k0, u1, v0);
			}

			Arrays.fill(icons, (Object)null);
			tessellator = null;
		}
	}

	private static void setupTileCoords(int face) {
		Icon icon = icons[face];
		tessellator = TessellatorUtils.getTessellator(Tessellator.instance, icons[face]);
		u0 = (double)icon.getMinU();
		uM = (double)icon.getInterpolatedU(8.0D);
		u1 = (double)icon.getMaxU();
		v0 = (double)icon.getMinV();
		v1 = (double)icon.getMaxV();
	}
}
