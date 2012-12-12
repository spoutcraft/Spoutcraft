package com.pclewis.mcpatcher.mod;

import com.pclewis.mcpatcher.MCLogger;
import com.pclewis.mcpatcher.MCPatcherUtils;
import com.pclewis.mcpatcher.TexturePackAPI;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Properties;

public class CTMUtils {
    private static final MCLogger logger = MCLogger.getLogger(MCPatcherUtils.CONNECTED_TEXTURES, "CTM");

    private static final boolean enableGlass = MCPatcherUtils.getBoolean(MCPatcherUtils.CONNECTED_TEXTURES, "glass", true);
    private static final boolean enableGlassPane = MCPatcherUtils.getBoolean(MCPatcherUtils.CONNECTED_TEXTURES, "glassPane", true);
    private static final boolean enableBookshelf = MCPatcherUtils.getBoolean(MCPatcherUtils.CONNECTED_TEXTURES, "bookshelf", true);
    private static final boolean enableSandstone = MCPatcherUtils.getBoolean(MCPatcherUtils.CONNECTED_TEXTURES, "sandstone", true);
    private static final boolean enableStandard = MCPatcherUtils.getBoolean(MCPatcherUtils.CONNECTED_TEXTURES, "standard", true);
    private static final boolean enableNonStandard = MCPatcherUtils.getBoolean(MCPatcherUtils.CONNECTED_TEXTURES, "nonStandard", true);
    private static final boolean enableOutline = MCPatcherUtils.getBoolean(MCPatcherUtils.CONNECTED_TEXTURES, "outline", false);

    static final int BLOCK_ID_LOG = 17;
    static final int BLOCK_ID_GLASS = 20;
    static final int BLOCK_ID_BED = 26;
    static final int BLOCK_ID_GLASS_PANE = 102;
    static final int BLOCK_ID_BOOKSHELF = 47;

    static final int NUM_TILES = 256;

    static final int TILE_NUM_STILL_LAVA = 14 * 16 + 13;
    static final int TILE_NUM_FLOWING_LAVA = 14 * 16 + 14;
    static final int TILE_NUM_STILL_WATER = 12 * 16 + 13;
    static final int TILE_NUM_FLOWING_WATER = 12 * 16 + 14;
    static final int TILE_NUM_FIRE_E_W = 1 * 16 + 15;
    static final int TILE_NUM_FIRE_N_S = 2 * 16 + 15;
    static final int TILE_NUM_PORTAL = 0 * 16 + 14;
    static final int TILE_NUM_SANDSTONE_SIDE = 192;
    static final int TILE_NUM_GLASS = 49;
    static final int TILE_NUM_GLASS_PANE_SIDE = 148;

    static TileOverride lastOverride;
    static int terrainTexture = -1;
    private static final TileOverride blockOverrides[][] = new TileOverride[Block.blocksList.length][];
    private static final TileOverride tileOverrides[][] = new TileOverride[NUM_TILES][];

    static boolean active;

    public static int newTextureIndex;
    public static Tessellator newTessellator;

    static {
        TexturePackAPI.ChangeHandler.register(new TexturePackAPI.ChangeHandler(MCPatcherUtils.CONNECTED_TEXTURES, 2) {
            @Override
            protected void onChange() {
                terrainTexture = getTexture("/terrain.png");
                SuperTessellator.instance.clearTessellators();

                Arrays.fill(blockOverrides, null);
                Arrays.fill(tileOverrides, null);

                if (enableStandard || enableNonStandard) {
                    for (String s : TexturePackAPI.listResources("/ctm", ".properties")) {
                        registerOverride(TileOverride.create(s.replace(".properties", ""), null));
                    }
                }

                Properties properties = new Properties();

                if (enableGlass) {
                    properties.clear();
                    properties.setProperty("method", "glass");
                    properties.setProperty("connect", "block");
                    properties.setProperty("blockIDs", "" + BLOCK_ID_GLASS);
                    registerOverride(TileOverride.create("/ctm", properties));
                }

                if (enableGlassPane) {
                    properties.clear();
                    properties.setProperty("method", "glass");
                    properties.setProperty("connect", "block");
                    properties.setProperty("blockIDs", "" + BLOCK_ID_GLASS_PANE);
                    registerOverride(TileOverride.create("/ctm", properties));
                }

                if (enableBookshelf) {
                    properties.clear();
                    properties.setProperty("method", "bookshelf");
                    properties.setProperty("connect", "block");
                    properties.setProperty("blockIDs", "" + BLOCK_ID_BOOKSHELF);
                    registerOverride(TileOverride.create("/ctm", properties));
                }

                if (enableSandstone) {
                    properties.clear();
                    properties.setProperty("method", "sandstone");
                    properties.setProperty("connect", "tile");
                    properties.setProperty("tileIDs", "" + TILE_NUM_SANDSTONE_SIDE);
                    properties.setProperty("metadata", "0");
                    registerOverride(TileOverride.create("/ctm", properties));
                }

                if (enableOutline) {
                    setupOutline();
                }

                RenderPassAPI.instance.clear();
                for (int i = 0; i < blockOverrides.length; i++) {
                    if (blockOverrides[i] != null && Block.blocksList[i] != null) {
                        for (TileOverride override : blockOverrides[i]) {
                            if (override != null && !override.disabled && override.renderPass >= 0) {
                                RenderPassAPI.instance.setRenderPassForBlock(Block.blocksList[i], override.renderPass);
                            }
                        }
                    }
                }

                GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrainTexture);
            }
        });
    }

    public static void start() {
        lastOverride = null;
        if (terrainTexture >= 0) {
            SuperTessellator.instance.texture = terrainTexture;
            active = true;
        } else {
            SuperTessellator.instance.texture = -1;
            active = false;
        }
    }

    private static boolean check(IBlockAccess blockAccess, int blockID) {
        return active && blockAccess != null && blockID != BLOCK_ID_BED && blockID != BLOCK_ID_GLASS_PANE && Tessellator.instance instanceof SuperTessellator;
    }

    public static boolean setup(RenderBlocks renderBlocks, Block block, int i, int j, int k, int face, int origTexture) {
        IBlockAccess blockAccess = renderBlocks.blockAccess;
        if (!enableStandard || !check(blockAccess, block.blockID) || face < 0 || face > 5) {
            return false;
        } else if (getConnectedTexture(renderBlocks, blockAccess, block, origTexture, i, j, k, face)) {
            return true;
        } else {
            reset();
            return false;
        }
    }

    public static boolean setup(RenderBlocks renderBlocks, Block block, int i, int j, int k, int origTexture) {
        IBlockAccess blockAccess = renderBlocks.blockAccess;
        if (!enableNonStandard || !check(blockAccess, block.blockID)) {
            return false;
        } else if (getConnectedTexture(renderBlocks, blockAccess, block, origTexture, i, j, k, -1)) {
            return true;
        } else {
            reset();
            return false;
        }
    }

    public static void reset() {
    }

    public static void finish() {
        reset();
        RenderPassAPI.instance.finish();
        SuperTessellator.instance.texture = -1;
        lastOverride = null;
        active = false;
    }

    public static boolean skipDefaultRendering(Block block) {
        return RenderPassAPI.instance.skipDefaultRendering(block);
    }

    static boolean getConnectedTexture(RenderBlocks renderBlocks, IBlockAccess blockAccess, Block block, int origTexture, int i, int j, int k, int face) {
        lastOverride = null;
        return getConnectedTexture(renderBlocks, blockAccess, block, origTexture, i, j, k, face, tileOverrides, origTexture) ||
            getConnectedTexture(renderBlocks, blockAccess, block, origTexture, i, j, k, face, blockOverrides, block.blockID);
    }

    private static boolean getConnectedTexture(RenderBlocks renderBlocks, IBlockAccess blockAccess, Block block, int origTexture, int i, int j, int k, int face, TileOverride[][] allOverrides, int index) {
        if (index < 0 || index >= allOverrides.length) {
            return false;
        }
        TileOverride[] overrides = allOverrides[index];
        if (overrides == null) {
            return false;
        }
        for (int n = 0; n < overrides.length; n++) {
            TileOverride override = overrides[n];
            if (override == null) {
                continue;
            }
            if (override.disabled) {
                overrides[n] = null;
                continue;
            }
            lastOverride = override;
            newTextureIndex = override.getTile(renderBlocks, blockAccess, block, origTexture, i, j, k, face);
            if (newTextureIndex >= 0) {
                newTessellator = SuperTessellator.instance.getTessellator(override.texture);
                return true;
            }
        }
        return false;
    }

    private static void registerOverride(TileOverride override) {
        if (override != null) {
            registerOverride(override, override.blockIDs, blockOverrides, "block");
            registerOverride(override, override.tileIDs, tileOverrides, "tile");
        }
    }

    private static void registerOverride(TileOverride override, int[] ids, TileOverride[][] allOverrides, String type) {
        if (override == null || ids == null || allOverrides == null) {
            return;
        }
        for (int index : ids) {
            TileOverride[] oldList = allOverrides[index];
            if (oldList == null) {
                allOverrides[index] = new TileOverride[]{override};
            } else {
                TileOverride[] newList = new TileOverride[oldList.length + 1];
                System.arraycopy(oldList, 0, newList, 0, oldList.length);
                newList[oldList.length] = override;
                allOverrides[index] = newList;
            }
            logger.fine("using %s for %s %d", override.toString(), type, index);
        }
    }

    private static void setupOutline() {
        BufferedImage terrain = TexturePackAPI.getImage("/terrain.png");
        if (terrain == null) {
            return;
        }
        BufferedImage template = TexturePackAPI.getImage("/ctm/template.png");
        if (template == null) {
            return;
        }

        int width = terrain.getWidth();
        int height = terrain.getHeight();
        if (template.getWidth() != width) {
            BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = newImage.createGraphics();
            graphics2D.drawImage(template, 0, 0, width, height, null);
            template = newImage;
        }

        for (int i = 0; i < tileOverrides.length; i++) {
            TileOverride override = setupOutline(i, terrain, template);
            if (override != null) {
                TileOverride[] oldList = tileOverrides[i];
                if (oldList == null) {
                    tileOverrides[i] = new TileOverride[]{override};
                } else {
                    TileOverride[] newList = new TileOverride[oldList.length + 1];
                    System.arraycopy(oldList, 0, newList, 0, oldList.length);
                    newList[oldList.length] = override;
                    tileOverrides[i] = newList;
                }
            }
        }
    }

    private static TileOverride setupOutline(int tileNum, BufferedImage terrain, BufferedImage template) {
        switch (tileNum) {
            case TILE_NUM_STILL_LAVA: // still lava
            case TILE_NUM_FLOWING_LAVA: // flowing lava
            case TILE_NUM_STILL_WATER: // still water
            case TILE_NUM_FLOWING_WATER: // flowing water
            case TILE_NUM_FIRE_E_W: // fire east-west
            case TILE_NUM_FIRE_N_S: // fire north-south
            case TILE_NUM_PORTAL: // portal
                return null;

            default:
                break;
        }

        int tileSize = terrain.getWidth() / 16;
        int tileX = (tileNum % 16) * tileSize;
        int tileY = (tileNum / 16) * tileSize;
        BufferedImage newImage = new BufferedImage(template.getWidth(), template.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < template.getWidth(); x++) {
            for (int y = 0; y < template.getHeight(); y++) {
                int rgb = template.getRGB(x, y);
                if ((rgb & 0xff000000) == 0) {
                    rgb = terrain.getRGB(tileX + (x % tileSize), tileY + (y % tileSize));
                }
                newImage.setRGB(x, y, rgb);
            }
        }

        return TileOverride.create(newImage, tileNum);
    }

    static int getTexture(String name) {
        if (name == null) {
            return -1;
        }
        if (TexturePackAPI.hasResource(name)) {
            return MCPatcherUtils.getMinecraft().renderEngine.getTexture(name);
        } else {
            return -1;
        }
    }
}
