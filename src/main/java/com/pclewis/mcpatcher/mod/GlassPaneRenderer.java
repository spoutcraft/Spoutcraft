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

    private static double u0; // left edge
    private static double uM; // left-right midpoint 
    private static double u1; // right edge
    private static double v0; // top edge
    private static double v1; // bottom edge

    public static void render(RenderBlocks renderBlocks, int overrideBlockTexture, Block blockPane, int i, int j, int k,
                              boolean connectNorth, boolean connectSouth, boolean connectWest, boolean connectEast) {
        if (!CTMUtils.active || overrideBlockTexture >= 0 || !(Tessellator.instance instanceof SuperTessellator) ||
            !CTMUtils.getConnectedTexture(renderBlocks, renderBlocks.blockAccess, blockPane, CTMUtils.TILE_NUM_GLASS_PANE_SIDE, i, j, k, 0)) {
            active = false;
            GlassPaneRenderer.renderBlocks = null;
            GlassPaneRenderer.blockPane = null;
            return;
        }

        GlassPaneRenderer.renderBlocks = renderBlocks;
        GlassPaneRenderer.blockPane = blockPane;
        GlassPaneRenderer.i = i;
        GlassPaneRenderer.j = j;
        GlassPaneRenderer.k = k;

        double i0 = i;
        double iM = i0 + 0.5;
        double i1 = i0 + 1.0;
        double j0 = j;
        double j1 = j0 + 1.0;
        double k0 = k;
        double kM = k0 + 0.5;
        double k1 = k0 + 1.0;

        Tessellator tessellator = CTMUtils.newTessellator;
        active = true;

        boolean connectAny = connectWest || connectEast || connectNorth || connectSouth;

        if ((connectEast && connectWest) || !connectAny) {
            // full west-east pane
            setupTileCoords(TileOverride.SOUTH_FACE);
            tessellator.addVertexWithUV(i0, j1, kM, u0, v0);
            tessellator.addVertexWithUV(i0, j0, kM, u0, v1);
            tessellator.addVertexWithUV(i1, j0, kM, u1, v1);
            tessellator.addVertexWithUV(i1, j1, kM, u1, v0);

            setupTileCoords(TileOverride.NORTH_FACE);
            tessellator.addVertexWithUV(i1, j1, kM, u0, v0);
            tessellator.addVertexWithUV(i1, j0, kM, u0, v1);
            tessellator.addVertexWithUV(i0, j0, kM, u1, v1);
            tessellator.addVertexWithUV(i0, j1, kM, u1, v0);
        } else if (connectWest && !connectEast) {
            // west half-pane
            setupTileCoords(TileOverride.SOUTH_FACE);
            tessellator.addVertexWithUV(i0, j1, kM, u0, v0);
            tessellator.addVertexWithUV(i0, j0, kM, u0, v1);
            tessellator.addVertexWithUV(iM, j0, kM, uM, v1);
            tessellator.addVertexWithUV(iM, j1, kM, uM, v0);

            setupTileCoords(TileOverride.NORTH_FACE);
            tessellator.addVertexWithUV(iM, j1, kM, uM, v0);
            tessellator.addVertexWithUV(iM, j0, kM, uM, v1);
            tessellator.addVertexWithUV(i0, j0, kM, u1, v1);
            tessellator.addVertexWithUV(i0, j1, kM, u1, v0);
        } else if (!connectWest && connectEast) {
            // east half-pane
            setupTileCoords(TileOverride.SOUTH_FACE);
            tessellator.addVertexWithUV(iM, j1, kM, uM, v0);
            tessellator.addVertexWithUV(iM, j0, kM, uM, v1);
            tessellator.addVertexWithUV(i1, j0, kM, u1, v1);
            tessellator.addVertexWithUV(i1, j1, kM, u1, v0);

            setupTileCoords(TileOverride.NORTH_FACE);
            tessellator.addVertexWithUV(i1, j1, kM, u0, v0);
            tessellator.addVertexWithUV(i1, j0, kM, u0, v1);
            tessellator.addVertexWithUV(iM, j0, kM, uM, v1);
            tessellator.addVertexWithUV(iM, j1, kM, uM, v0);
        }

        if ((connectNorth && connectSouth) || !connectAny) {
            // full north-south pane
            setupTileCoords(TileOverride.WEST_FACE);
            tessellator.addVertexWithUV(iM, j1, k0, u0, v0);
            tessellator.addVertexWithUV(iM, j0, k0, u0, v1);
            tessellator.addVertexWithUV(iM, j0, k1, u1, v1);
            tessellator.addVertexWithUV(iM, j1, k1, u1, v0);

            setupTileCoords(TileOverride.EAST_FACE);
            tessellator.addVertexWithUV(iM, j1, k1, u0, v0);
            tessellator.addVertexWithUV(iM, j0, k1, u0, v1);
            tessellator.addVertexWithUV(iM, j0, k0, u1, v1);
            tessellator.addVertexWithUV(iM, j1, k0, u1, v0);
        } else if (connectNorth && !connectSouth) {
            // north half-pane
            setupTileCoords(TileOverride.WEST_FACE);
            tessellator.addVertexWithUV(iM, j1, k0, u0, v0);
            tessellator.addVertexWithUV(iM, j0, k0, u0, v1);
            tessellator.addVertexWithUV(iM, j0, kM, uM, v1);
            tessellator.addVertexWithUV(iM, j1, kM, uM, v0);

            setupTileCoords(TileOverride.EAST_FACE);
            tessellator.addVertexWithUV(iM, j1, kM, uM, v0);
            tessellator.addVertexWithUV(iM, j0, kM, uM, v1);
            tessellator.addVertexWithUV(iM, j0, k0, u1, v1);
            tessellator.addVertexWithUV(iM, j1, k0, u1, v0);
        } else if (!connectNorth && connectSouth) {
            // south half-pane
            setupTileCoords(TileOverride.WEST_FACE);
            tessellator.addVertexWithUV(iM, j1, kM, uM, v0);
            tessellator.addVertexWithUV(iM, j0, kM, uM, v1);
            tessellator.addVertexWithUV(iM, j0, k1, u1, v1);
            tessellator.addVertexWithUV(iM, j1, k1, u1, v0);

            setupTileCoords(TileOverride.EAST_FACE);
            tessellator.addVertexWithUV(iM, j1, k1, uM, v0);
            tessellator.addVertexWithUV(iM, j0, k1, uM, v1);
            tessellator.addVertexWithUV(iM, j0, kM, u1, v1);
            tessellator.addVertexWithUV(iM, j1, kM, u1, v0);
        }
    }

    private static void setupTileCoords(int face) {
        int tile = CTMUtils.lastOverride.getTile(renderBlocks, renderBlocks.blockAccess, blockPane, CTMUtils.TILE_NUM_GLASS, i, j, k, face);
        int tileU = (tile & 0x0f) << 4;
        int tileV = tile & 0xf0;
        u0 = tileU / 256.0f;
        uM = (tileU + 7.99f) / 256.0f;
        u1 = (tileU + 15.99f) / 256.0f;
        v0 = tileV / 256.0f;
        v1 = (tileV + 15.99f) / 256.0f;
    }
}
