package com.pclewis.mcpatcher.mod;

import com.pclewis.mcpatcher.MCLogger;
import com.pclewis.mcpatcher.MCPatcherUtils;
import com.pclewis.mcpatcher.TexturePackAPI;
import com.pclewis.mcpatcher.WeightedIndex;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;

import java.awt.image.BufferedImage;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class TileOverride {
    private static final MCLogger logger = MCLogger.getLogger(MCPatcherUtils.CONNECTED_TEXTURES, "CTM");

    static final int BOTTOM_FACE = 0; // 0, -1, 0
    static final int TOP_FACE = 1; // 0, 1, 0
    static final int NORTH_FACE = 2; // 0, 0, -1
    static final int SOUTH_FACE = 3; // 0, 0, 1
    static final int WEST_FACE = 4; // -1, 0, 0
    static final int EAST_FACE = 5; // 1, 0, 0

    private static final int[][] ROTATE_UV_MAP = new int[][]{
        {WEST_FACE, EAST_FACE, NORTH_FACE, SOUTH_FACE, TOP_FACE, BOTTOM_FACE, 2, -2, 2, -2, 0, 0},
        {NORTH_FACE, SOUTH_FACE, TOP_FACE, BOTTOM_FACE, WEST_FACE, EAST_FACE, 0, 0, 0, 0, -2, 2},
    };

    private static final int[] GO_DOWN = new int[]{0, -1, 0};
    private static final int[] GO_UP = new int[]{0, 1, 0};
    private static final int[] GO_NORTH = new int[]{0, 0, -1};
    private static final int[] GO_SOUTH = new int[]{0, 0, 1};
    private static final int[] GO_WEST = new int[]{-1, 0, 0};
    private static final int[] GO_EAST = new int[]{1, 0, 0};

    // NEIGHBOR_OFFSETS[a][b][c] = offset from starting block
    // a: face 0-5
    // b: neighbor 0-7
    //    7   6   5
    //    0   *   4
    //    1   2   3
    // c: coordinate (x,y,z) 0-2
    private static final int[][][] NEIGHBOR_OFFSET = new int[][][]{
        // BOTTOM_FACE
        {
            GO_WEST,
            add(GO_WEST, GO_SOUTH),
            GO_SOUTH,
            add(GO_EAST, GO_SOUTH),
            GO_EAST,
            add(GO_EAST, GO_NORTH),
            GO_NORTH,
            add(GO_WEST, GO_NORTH),
        },
        // TOP_FACE
        {
            GO_WEST,
            add(GO_WEST, GO_SOUTH),
            GO_SOUTH,
            add(GO_EAST, GO_SOUTH),
            GO_EAST,
            add(GO_EAST, GO_NORTH),
            GO_NORTH,
            add(GO_WEST, GO_NORTH),
        },
        // NORTH_FACE
        {
            GO_EAST,
            add(GO_EAST, GO_DOWN),
            GO_DOWN,
            add(GO_WEST, GO_DOWN),
            GO_WEST,
            add(GO_WEST, GO_UP),
            GO_UP,
            add(GO_EAST, GO_UP),
        },
        // SOUTH_FACE
        {
            GO_WEST,
            add(GO_WEST, GO_DOWN),
            GO_DOWN,
            add(GO_EAST, GO_DOWN),
            GO_EAST,
            add(GO_EAST, GO_UP),
            GO_UP,
            add(GO_WEST, GO_UP),
        },
        // WEST_FACE
        {
            GO_NORTH,
            add(GO_NORTH, GO_DOWN),
            GO_DOWN,
            add(GO_SOUTH, GO_DOWN),
            GO_SOUTH,
            add(GO_SOUTH, GO_UP),
            GO_UP,
            add(GO_NORTH, GO_UP),
        },
        // EAST_FACE
        {
            GO_SOUTH,
            add(GO_SOUTH, GO_DOWN),
            GO_DOWN,
            add(GO_NORTH, GO_DOWN),
            GO_NORTH,
            add(GO_NORTH, GO_UP),
            GO_UP,
            add(GO_SOUTH, GO_UP),
        },
    };

    private static final int CONNECT_BY_BLOCK = 0;
    private static final int CONNECT_BY_TILE = 1;
    private static final int CONNECT_BY_MATERIAL = 2;

    private static final Method forceMipmapType;

    final String filePrefix;
    final String textureName;
    final int texture;
    final int renderPass;
    final int[] blockIDs;
    final int[] tileIDs;
    final int faces;
    final int metadata;
    final int connectType;
    final int[] tileMap;

    boolean disabled;
    int[] reorient;
    int metamask;
    int rotateUV;
    boolean rotateTop;

    static {
        Method method = null;
        try {
            Class<?> cl = Class.forName(MCPatcherUtils.MIPMAP_HELPER_CLASS);
            method = cl.getDeclaredMethod("forceMipmapType", String.class, Integer.TYPE);
        } catch (ClassNotFoundException e) {
        } catch (NoSuchMethodException e) {
        }
        forceMipmapType = method;
    }

    static TileOverride create(String filePrefix, Properties properties) {
        if (filePrefix == null) {
            return null;
        }
        if (properties == null) {
            properties = TexturePackAPI.getProperties(filePrefix + ".properties");
        }
        if (properties == null) {
            return null;
        }

        String method = properties.getProperty("method", "default").trim().toLowerCase();
        TileOverride override = null;

        if (method.equals("default") || method.equals("glass") || method.equals("ctm")) {
            override = new CTM(filePrefix, properties);
        } else if (method.equals("random")) {
            override = new Random1(filePrefix, properties);
        } else if (method.equals("fixed") || method.equals("static")) {
            override = new Fixed(filePrefix, properties);
        } else if (method.equals("bookshelf") || method.equals("horizontal")) {
            override = new Horizontal(filePrefix, properties);
        } else if (method.equals("vertical")) {
            override = new Vertical(filePrefix, properties);
        } else if (method.equals("sandstone") || method.equals("top")) {
            override = new Top(filePrefix, properties);
        } else if (method.equals("repeat") || method.equals("pattern")) {
            override = new Repeat(filePrefix, properties);
        } else {
            logger.severe("%s.properties: unknown method \"%s\"", filePrefix, method);
        }

        if (override == null || override.disabled) {
        } else if (override.tileMap == null || override.tileMap.length == 0) {
            override.error("no tile map given");
        } else {
            String status = override.checkTileMap();
            if (status != null) {
                override.error("invalid %s tile map: %s", override.getMethod(), status);
            }
        }

        return override == null || override.disabled ? null : override;
    }

    static TileOverride create(BufferedImage image, int tileID) {
        TileOverride override = new CTM(image, tileID);
        return override.disabled ? null : override;
    }

    private TileOverride(BufferedImage image, int tileID) {
        filePrefix = null;
        textureName = null;
        texture = MCPatcherUtils.getMinecraft().renderEngine.allocateAndSetupTexture(image);
        renderPass = -1;
        blockIDs = new int[0];
        tileIDs = new int[]{tileID};
        faces = -1;
        metadata = -1;
        connectType = CONNECT_BY_MATERIAL;
        tileMap = null;
    }

    private TileOverride(String filePrefix, Properties properties) {
        this.filePrefix = filePrefix;
        textureName = properties.getProperty("source", filePrefix + ".png");

        int pass = 0;
        try {
            pass = Integer.parseInt(properties.getProperty("renderPass", "-1"));
        } catch (NumberFormatException e) {
        }
        renderPass = pass;
        if (forceMipmapType != null) {
            try {
                forceMipmapType.invoke(null, textureName, renderPass > 2 ? 2 : 1);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        texture = CTMUtils.getTexture(textureName);
        if (texture < 0) {
            if (properties.contains("source")) {
                error("source texture %s not found", textureName);
            } else {
                disabled = true;
            }
        }

        blockIDs = getIDList(properties, "blockIDs", "block", Block.blocksList.length - 1);
        tileIDs = getIDList(properties, "tileIDs", "terrain", CTMUtils.NUM_TILES - 1);
        if (blockIDs.length == 0 && tileIDs.length == 0) {
            error("no block or tile IDs matched");
        }

        int flags = 0;
        for (String val : properties.getProperty("faces", "all").trim().toLowerCase().split("\\s+")) {
            if (val.equals("bottom")) {
                flags |= (1 << BOTTOM_FACE);
            } else if (val.equals("top")) {
                flags |= (1 << TOP_FACE);
            } else if (val.equals("north")) {
                flags |= (1 << NORTH_FACE);
            } else if (val.equals("south")) {
                flags |= (1 << SOUTH_FACE);
            } else if (val.equals("east")) {
                flags |= (1 << EAST_FACE);
            } else if (val.equals("west")) {
                flags |= (1 << WEST_FACE);
            } else if (val.equals("side") || val.equals("sides")) {
                flags |= (1 << NORTH_FACE) | (1 << SOUTH_FACE) | (1 << EAST_FACE) | (1 << WEST_FACE);
            } else if (val.equals("all")) {
                flags = -1;
            }
        }
        faces = flags;

        int meta = 0;
        for (int i : MCPatcherUtils.parseIntegerList(properties.getProperty("metadata", "0-31"), 0, 31)) {
            meta |= (1 << i);
        }
        metadata = meta;

        String connectType1 = properties.getProperty("connect", "").trim().toLowerCase();
        if (connectType1.equals("")) {
            connectType = tileIDs.length > 0 ? CONNECT_BY_TILE : CONNECT_BY_BLOCK;
        } else if (connectType1.equals("block")) {
            connectType = CONNECT_BY_BLOCK;
        } else if (connectType1.equals("tile")) {
            connectType = CONNECT_BY_TILE;
        } else if (connectType1.equals("material")) {
            connectType = CONNECT_BY_MATERIAL;
        } else {
            error("invalid connect type %s", connectType1);
            connectType = CONNECT_BY_BLOCK;
        }

        String tileList = properties.getProperty("tiles", "");
        if (tileList.equals("")) {
            tileMap = getDefaultTileMap();
        } else {
            tileMap = MCPatcherUtils.parseIntegerList(tileList, 0, 255);
        }

        if (renderPass > 3) {
            error("renderPass must be 0-3");
        } else if (renderPass >= 0 && tileIDs.length > 0) {
            error("renderPass=%d must be block-based not tile-based", renderPass);
        }
    }

    private int[] getIDList(Properties properties, String key, String type, int maxID) {
        int[] list = MCPatcherUtils.parseIntegerList(properties.getProperty(key, "").trim(), 0, maxID);
        if (list.length > 0) {
            return list;
        }
        Matcher m = Pattern.compile("/" + type + "(\\d+)").matcher(filePrefix);
        if (m.find()) {
            try {
                list = new int[]{Integer.parseInt(m.group(1))};
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private static int[] add(int[] a, int[] b) {
        if (a.length != b.length) {
            throw new RuntimeException("arrays to add are not same length");
        }
        int[] c = new int[a.length];
        for (int i = 0; i < c.length; i++) {
            c[i] = a[i] + b[i];
        }
        return c;
    }

    int[] getDefaultTileMap() {
        return null;
    }

    String checkTileMap() {
        return null;
    }

    boolean requiresFace() {
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s[%s]", getMethod(), textureName);
    }

    final void error(String format, Object... params) {
        if (filePrefix != null && !filePrefix.equals("/ctm")) {
            logger.severe(filePrefix + ".properties: " + format, params);
        }
        disabled = true;
    }

    final boolean shouldConnect(IBlockAccess blockAccess, Block block, int tileNum, int i, int j, int k, int face, int[] offset) {
        int meta = blockAccess.getBlockMetadata(i, j, k);
        i += offset[0];
        j += offset[1];
        k += offset[2];
        int neighborID = blockAccess.getBlockId(i, j, k);
        Block neighbor = Block.blocksList[neighborID];
        if (exclude(blockAccess, neighbor, tileNum, i, j, k, face)) {
            return false;
        } else if (metamask != -1 && (blockAccess.getBlockMetadata(i, j, k) & ~metamask) != (meta & ~metamask)) {
            return false;
        }
        switch (connectType) {
            case CONNECT_BY_BLOCK:
                return neighborID == block.blockID;

            case CONNECT_BY_TILE:
                return neighbor.getBlockTexture(blockAccess, i, j, k, face) == tileNum;

            case CONNECT_BY_MATERIAL:
                return block.blockMaterial == neighbor.blockMaterial;

            default:
                return false;
        }
    }

    final int reorient(int face) {
        if (face < 0 || face > 5 || reorient == null) {
            return face;
        } else {
            return reorient[face];
        }
    }

    final boolean exclude(IBlockAccess blockAccess, Block block, int origTexture, int i, int j, int k, int face) {
        if (block == null) {
            return true;
        } else if (RenderPassAPI.instance.skipThisRenderPass(block, renderPass)) {
            return true;
        } else if ((faces & (1 << reorient(face))) == 0) {
            return true;
        } else if (metadata != -1) {
            int meta = blockAccess.getBlockMetadata(i, j, k);
            if (meta >= 0 && meta < 32 && (metadata & ((1 << meta) | (1 << (meta & metamask)))) == 0) {
                return true;
            }
        }
        return false;
    }

    final int getTile(RenderBlocks renderBlocks, IBlockAccess blockAccess, Block block, int origTexture, int i, int j, int k, int face) {
        if (face < 0) {
            if (requiresFace()) {
                error("method=%s is not supported for non-standard blocks", getMethod());
                return -1;
            }
        }
        reorient = null;
        metamask = -1;
        rotateUV = 0;
        rotateTop = false;
        if (block.blockID == CTMUtils.BLOCK_ID_LOG) {
            metamask = ~0xc;
            int orientation = blockAccess.getBlockMetadata(i, j, k) & 0xc;
            if (orientation == 4) { // east/west cut
                reorient = ROTATE_UV_MAP[0];
                rotateUV = ROTATE_UV_MAP[0][face + 6];
                rotateTop = true;
            } else if (orientation == 8) { // north/south cut
                reorient = ROTATE_UV_MAP[1];
                rotateUV = ROTATE_UV_MAP[1][face + 6];
            }
        }
        if (exclude(blockAccess, block, origTexture, i, j, k, face)) {
            return -1;
        } else {
            return getTileImpl(blockAccess, block, origTexture, i, j, k, face);
        }
    }

    final int rotateUV(int neighbor) {
        return (neighbor + rotateUV) & 7;
    }

    private static int[] compose(int[] map1, int[] map2) {
        int[] newMap = new int[map2.length];
        for (int i = 0; i < map2.length; i++) {
            newMap[i] = map1[map2[i]];
        }
        return newMap;
    }

    abstract String getMethod();

    abstract int getTileImpl(IBlockAccess blockAccess, Block block, int origTexture, int i, int j, int k, int face);

    final static class CTM extends TileOverride {
        private static final int[] defaultTileMap = new int[]{
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
            16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27,
            32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43,
            48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59,
        };

        // Index into this array is formed from these bit values:
        // 128 64  32
        // 1   *   16
        // 2   4   8
        private static final int[] neighborMap = new int[]{
            0, 3, 0, 3, 12, 5, 12, 15, 0, 3, 0, 3, 12, 5, 12, 15,
            1, 2, 1, 2, 4, 7, 4, 29, 1, 2, 1, 2, 13, 31, 13, 14,
            0, 3, 0, 3, 12, 5, 12, 15, 0, 3, 0, 3, 12, 5, 12, 15,
            1, 2, 1, 2, 4, 7, 4, 29, 1, 2, 1, 2, 13, 31, 13, 14,
            36, 17, 36, 17, 24, 19, 24, 43, 36, 17, 36, 17, 24, 19, 24, 43,
            16, 18, 16, 18, 6, 46, 6, 21, 16, 18, 16, 18, 28, 9, 28, 22,
            36, 17, 36, 17, 24, 19, 24, 43, 36, 17, 36, 17, 24, 19, 24, 43,
            37, 40, 37, 40, 30, 8, 30, 34, 37, 40, 37, 40, 25, 23, 25, 45,
            0, 3, 0, 3, 12, 5, 12, 15, 0, 3, 0, 3, 12, 5, 12, 15,
            1, 2, 1, 2, 4, 7, 4, 29, 1, 2, 1, 2, 13, 31, 13, 14,
            0, 3, 0, 3, 12, 5, 12, 15, 0, 3, 0, 3, 12, 5, 12, 15,
            1, 2, 1, 2, 4, 7, 4, 29, 1, 2, 1, 2, 13, 31, 13, 14,
            36, 39, 36, 39, 24, 41, 24, 27, 36, 39, 36, 39, 24, 41, 24, 27,
            16, 42, 16, 42, 6, 20, 6, 10, 16, 42, 16, 42, 28, 35, 28, 44,
            36, 39, 36, 39, 24, 41, 24, 27, 36, 39, 36, 39, 24, 41, 24, 27,
            37, 38, 37, 38, 30, 11, 30, 32, 37, 38, 37, 38, 25, 33, 25, 26,
        };

        private final int[] neighborTileMap;

        private CTM(BufferedImage image, int tileID) {
            super(image, tileID);
            neighborTileMap = compose(defaultTileMap, neighborMap);
        }

        private CTM(String filePrefix, Properties properties) {
            super(filePrefix, properties);
            neighborTileMap = compose(tileMap, neighborMap);
        }

        @Override
        String getMethod() {
            return "ctm";
        }

        @Override
        int[] getDefaultTileMap() {
            return defaultTileMap;
        }

        @Override
        String checkTileMap() {
            if (tileMap.length >= 47) {
                return null;
            } else {
                return "requires at least 47 tiles";
            }
        }

        @Override
        boolean requiresFace() {
            return true;
        }

        @Override
        int getTileImpl(IBlockAccess blockAccess, Block block, int origTexture, int i, int j, int k, int face) {
            int[][] offsets = NEIGHBOR_OFFSET[face];
            int neighborBits = 0;
            for (int bit = 0; bit < 8; bit++) {
                if (shouldConnect(blockAccess, block, origTexture, i, j, k, face, offsets[bit])) {
                    neighborBits |= (1 << bit);
                }
            }
            return neighborTileMap[neighborBits];
        }
    }

    final static class Horizontal extends TileOverride {
        private static final int[] defaultTileMap = new int[]{
            12, 13, 14, 15,
        };

        // Index into this array is formed from these bit values:
        // 1   *   2
        private static final int[] neighborMap = new int[]{
            3, 2, 0, 1,
        };

        private final int[] neighborTileMap;

        private Horizontal(String filePrefix, Properties properties) {
            super(filePrefix, properties);
            neighborTileMap = compose(tileMap, neighborMap);
        }

        @Override
        String getMethod() {
            return "horizontal";
        }

        @Override
        int[] getDefaultTileMap() {
            return defaultTileMap;
        }

        @Override
        String checkTileMap() {
            if (tileMap.length == 4) {
                return null;
            } else {
                return "requires exactly 4 tiles";
            }
        }

        @Override
        int getTileImpl(IBlockAccess blockAccess, Block block, int origTexture, int i, int j, int k, int face) {
            if (face < 0) {
                face = NORTH_FACE;
            } else if (reorient(face) <= TOP_FACE) {
                return -1;
            }
            int[][] offsets = NEIGHBOR_OFFSET[face];
            int neighborBits = 0;
            if (shouldConnect(blockAccess, block, origTexture, i, j, k, face, offsets[rotateUV(0)])) {
                neighborBits |= 1;
            }
            if (shouldConnect(blockAccess, block, origTexture, i, j, k, face, offsets[rotateUV(4)])) {
                neighborBits |= 2;
            }
            return neighborTileMap[neighborBits];
        }
    }

    final static class Vertical extends TileOverride {
        private static final int[] defaultTileMap = new int[]{
            48, 32, 16, 0,
        };

        // Index into this array is formed from these bit values:
        // 2
        // *
        // 1
        private static final int[] neighborMap = new int[]{
            3, 2, 0, 1,
        };

        private final int[] neighborTileMap;

        private Vertical(String filePrefix, Properties properties) {
            super(filePrefix, properties);
            neighborTileMap = compose(tileMap, neighborMap);
        }

        @Override
        String getMethod() {
            return "vertical";
        }

        @Override
        int[] getDefaultTileMap() {
            return defaultTileMap;
        }

        @Override
        String checkTileMap() {
            if (tileMap.length == 4) {
                return null;
            } else {
                return "requires exactly 4 tiles";
            }
        }

        @Override
        int getTileImpl(IBlockAccess blockAccess, Block block, int origTexture, int i, int j, int k, int face) {
            if (face < 0) {
                face = NORTH_FACE;
            } else if (reorient(face) <= TOP_FACE) {
                return -1;
            }
            int[][] offsets = NEIGHBOR_OFFSET[face];
            int neighborBits = 0;
            if (shouldConnect(blockAccess, block, origTexture, i, j, k, face, offsets[rotateUV(2)])) {
                neighborBits |= 1;
            }
            if (shouldConnect(blockAccess, block, origTexture, i, j, k, face, offsets[rotateUV(6)])) {
                neighborBits |= 2;
            }
            return neighborTileMap[neighborBits];
        }
    }

    final static class Top extends TileOverride {
        private static final int[] defaultTileMap = new int[]{
            66,
        };

        private Top(String filePrefix, Properties properties) {
            super(filePrefix, properties);
        }

        @Override
        String getMethod() {
            return "top";
        }

        @Override
        int[] getDefaultTileMap() {
            return defaultTileMap;
        }

        @Override
        String checkTileMap() {
            if (tileMap.length == 1) {
                return null;
            } else {
                return "requires exactly 1 tile";
            }
        }

        @Override
        int getTileImpl(IBlockAccess blockAccess, Block block, int origTexture, int i, int j, int k, int face) {
            if (face < 0) {
                face = NORTH_FACE;
            } else if (reorient(face) <= TOP_FACE) {
                return -1;
            }
            int[][] offsets = NEIGHBOR_OFFSET[face];
            if (shouldConnect(blockAccess, block, origTexture, i, j, k, face, offsets[rotateUV(6)])) {
                return tileMap[0];
            }
            return -1;
        }
    }

    final static class Random1 extends TileOverride {
        private static final long P1 = 0x1c3764a30115L;
        private static final long P2 = 0x227c1adccd1dL;
        private static final long P3 = 0xe0d251c03ba5L;
        private static final long P4 = 0xa2fb1377aeb3L;
        private static final long MULTIPLIER = 0x5deece66dL;
        private static final long ADDEND = 0xbL;

        private final int symmetry;
        private final WeightedIndex chooser;

        private Random1(String filePrefix, Properties properties) {
            super(filePrefix, properties);

            String sym = properties.getProperty("symmetry", "none");
            if (sym.equals("all")) {
                symmetry = 6;
            } else if (sym.equals("opposite")) {
                symmetry = 2;
            } else {
                symmetry = 1;
            }

            chooser = WeightedIndex.create(tileMap.length, properties.getProperty("weights", ""));
            if (chooser == null) {
                error("invalid weights");
            }
        }

        @Override
        String getMethod() {
            return "random";
        }

        @Override
        int getTileImpl(IBlockAccess blockAccess, Block block, int origTexture, int i, int j, int k, int face) {
            if (tileMap.length == 1) {
                return tileMap[0];
            }
            if (face < 0) {
                face = 0;
            }
            face = reorient(face) / symmetry;
            long n = P1 * i * (i + ADDEND) + P2 * j * (j + ADDEND) + P3 * k * (k + ADDEND) + P4 * face * (face + ADDEND);
            n = MULTIPLIER * (n + i + j + k + face) + ADDEND;
            int index = chooser.choose(n);
            return tileMap[index];
        }
    }

    final static class Repeat extends TileOverride {
        private final int width;
        private final int height;
        private final int symmetry;

        Repeat(String filePrefix, Properties properties) {
            super(filePrefix, properties);
            int w = 0;
            int h = 0;
            try {
                w = Integer.parseInt(properties.getProperty("width", "0"));
                h = Integer.parseInt(properties.getProperty("height", "0"));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            width = w;
            height = h;
            if (width <= 0 || height <= 0 || width * height > CTMUtils.NUM_TILES) {
                error("invalid width and height (%dx%d)", width, height);
            }

            String sym = properties.getProperty("symmetry", "none");
            if (sym.equals("opposite")) {
                symmetry = ~1;
            } else {
                symmetry = -1;
            }
        }

        @Override
        String getMethod() {
            return "repeat";
        }

        @Override
        String checkTileMap() {
            if (tileMap.length == width * height) {
                return null;
            } else {
                return String.format("requires exactly %dx%d tiles", width, height);
            }
        }

        @Override
        int getTileImpl(IBlockAccess blockAccess, Block block, int origTexture, int i, int j, int k, int face) {
            if (face < 0) {
                face = 0;
            }
            face &= symmetry;
            int x;
            int y;
            switch (face) {
                case TOP_FACE:
                case BOTTOM_FACE:
                    if (rotateTop) {
                        x = k;
                        y = i;
                    } else {
                        x = i;
                        y = k;
                    }
                    break;

                case NORTH_FACE:
                    x = -i - 1;
                    y = -j;
                    break;

                case SOUTH_FACE:
                    x = i;
                    y = -j;
                    break;

                case WEST_FACE:
                    x = k;
                    y = -j;
                    break;

                case EAST_FACE:
                    x = -k - 1;
                    y = -j;
                    break;

                default:
                    return -1;
            }
            x %= width;
            if (x < 0) {
                x += width;
            }
            y %= height;
            if (y < 0) {
                y += height;
            }
            return tileMap[width * y + x];
        }
    }

    final static class Fixed extends TileOverride {
        private Fixed(String filePrefix, Properties properties) {
            super(filePrefix, properties);
        }

        @Override
        String getMethod() {
            return "fixed";
        }

        @Override
        String checkTileMap() {
            if (tileMap.length == 1) {
                return null;
            } else {
                return "requires exactly 1 tile";
            }
        }

        @Override
        int getTileImpl(IBlockAccess blockAccess, Block block, int origTexture, int i, int j, int k, int face) {
            return tileMap[0];
        }
    }
}
