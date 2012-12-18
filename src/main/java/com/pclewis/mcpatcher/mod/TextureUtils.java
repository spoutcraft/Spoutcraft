package com.pclewis.mcpatcher.mod;

import com.pclewis.mcpatcher.MCLogger;
import com.pclewis.mcpatcher.MCPatcherUtils;
import com.pclewis.mcpatcher.TexturePackAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.opengl.PixelFormat;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TextureUtils {
    private static final MCLogger logger = MCLogger.getLogger(MCPatcherUtils.HD_TEXTURES);

    private static final boolean animatedFire = MCPatcherUtils.getBoolean(MCPatcherUtils.HD_TEXTURES, "animatedFire", true);
    private static final boolean animatedLava = MCPatcherUtils.getBoolean(MCPatcherUtils.HD_TEXTURES, "animatedLava", true);
    private static final boolean animatedWater = MCPatcherUtils.getBoolean(MCPatcherUtils.HD_TEXTURES, "animatedWater", true);
    private static final boolean animatedPortal = MCPatcherUtils.getBoolean(MCPatcherUtils.HD_TEXTURES, "animatedPortal", true);
    private static final boolean customFire = MCPatcherUtils.getBoolean(MCPatcherUtils.HD_TEXTURES, "customFire", true);
    private static final boolean customLava = MCPatcherUtils.getBoolean(MCPatcherUtils.HD_TEXTURES, "customLava", true);
    private static final boolean customWater = MCPatcherUtils.getBoolean(MCPatcherUtils.HD_TEXTURES, "customWater", true);
    private static final boolean customPortal = MCPatcherUtils.getBoolean(MCPatcherUtils.HD_TEXTURES, "customPortal", true);
    private static final boolean customOther = MCPatcherUtils.getBoolean(MCPatcherUtils.HD_TEXTURES, "customOther", true);
    private static final boolean fancyCompass = MCPatcherUtils.getBoolean(MCPatcherUtils.HD_TEXTURES, "fancyCompass", true);

    private static final boolean useTextureCache = false;
    private static final boolean reclaimGLMemory = false;
    private static final boolean useZombieHack = MCPatcherUtils.getBoolean(MCPatcherUtils.HD_TEXTURES, "zombieHack", true);
    private static final int zombieAspectRatio = getAspectRatio("/mob/zombie.png");
    private static final int pigZombieAspectRatio = getAspectRatio("/mob/pigzombie.png");

    private static final int aaSamples = MCPatcherUtils.getInt(MCPatcherUtils.HD_TEXTURES, "antiAliasing", 0);

    private static final int LAVA_STILL_TEXTURE_INDEX = 14 * 16 + 13;  // Block.lavaStill.blockIndexInTexture
    private static final int LAVA_FLOWING_TEXTURE_INDEX = LAVA_STILL_TEXTURE_INDEX + 1; // Block.lavaMoving.blockIndexInTexture
    private static final int WATER_STILL_TEXTURE_INDEX = 12 * 16 + 13; // Block.waterStill.blockIndexInTexture
    private static final int WATER_FLOWING_TEXTURE_INDEX = WATER_STILL_TEXTURE_INDEX + 1; // Block.waterMoving.blockIndexInTexture
    private static final int FIRE_E_W_TEXTURE_INDEX = 1 * 16 + 15; // Block.fire.blockIndexInTexture;
    private static final int FIRE_N_S_TEXTURE_INDEX = FIRE_E_W_TEXTURE_INDEX + 16;
    private static final int PORTAL_TEXTURE_INDEX = 0 * 16 + 14; // Block.portal.blockIndexInTexture

    private static final String ALL_ITEMS = "/gui/allitems.png";
    private static final String ALL_ITEMSX = "/gui/allitemsx.png";

    private static boolean enableResizing = true;
    private static final HashMap<String, Integer> expectedColumns = new HashMap<String, Integer>();

    private static final HashMap<String, BufferedImage> imageCache = new HashMap<String, BufferedImage>();

    private static boolean bindImageReentry;

    public static boolean oldCreativeGui;

    static {
        expectedColumns.put("/terrain.png", 16);
        expectedColumns.put("/gui/items.png", 16);
        expectedColumns.put("/misc/dial.png", 1);

        logger.fine("/mob/zombie.png aspect ratio is %d:1", zombieAspectRatio);
        logger.fine("/mob/pigzombie.png aspect ratio is %d:1", pigZombieAspectRatio);

        TexturePackAPI.instance = new TexturePackAPI() {
            @Override
            protected InputStream getInputStreamImpl(String s) {
                InputStream input;
                if (oldCreativeGui && s.equals(ALL_ITEMS)) {
                    input = super.getInputStreamImpl(ALL_ITEMSX);
                    if (input != null) {
                        return input;
                    }
                }
                input = super.getInputStreamImpl(s);
                if (input == null && s.startsWith("/anim/custom_")) {
                    input = super.getInputStreamImpl(s.substring(5));
                }
                return input;
            }

            @Override
            protected BufferedImage getImageImpl(String s) {
                BufferedImage image;
                if (useTextureCache && enableResizing) {
                    image = imageCache.get(s);
                    if (image != null) {
                        return image;
                    }
                }

                image = super.getImageImpl(s);
                if (image == null) {
                    return null;
                }
                int width = image.getWidth();
                int height = image.getHeight();
                logger.finer("opened %s %dx%d", s, width, height);

                if (enableResizing) {
                    Integer i = expectedColumns.get(s);
                    if (i != null && width != i * TileSize.int_size) {
                        image = resizeImage(image, i * TileSize.int_size);
                    }
                }

                if (s.matches("^/mob/.*_eyes\\d*\\.png$")) {
                    int p = 0;
                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {
                            int argb = image.getRGB(x, y);
                            if ((argb & 0xff000000) == 0 && argb != 0) {
                                image.setRGB(x, y, 0);
                                p++;
                            }
                        }
                    }
                    if (p > 0) {
                        logger.finest("  fixed %d transparent pixels", p, s);
                    }
                }

                image = resizeMobTexture("zombie", zombieAspectRatio, s, image);
                image = resizeMobTexture("pigzombie", pigZombieAspectRatio, s, image);

                if (useTextureCache && enableResizing) {
                    imageCache.put(s, image);
                }
                return image;
            }

            private BufferedImage resizeMobTexture(String mob, int aspectRatio, String texture, BufferedImage image) {
                if (aspectRatio != 0 && texture.matches("/mob/" + mob + "\\d*\\.png")) {
                    int width = image.getWidth();
                    int height = image.getHeight();
                    if (aspectRatio == 1 && width == 2 * height) {
                        logger.fine("resizing %s to %dx%d", texture, width, 2 * height);
                        BufferedImage newImage = new BufferedImage(width, 2 * height, BufferedImage.TYPE_INT_ARGB);
                        Graphics2D graphics2D = newImage.createGraphics();
                        graphics2D.drawImage(image, 0, 0, width, height, 0, 0, width, height, null);
                        image = newImage;
                    } else if (aspectRatio == 2 && width == height) {
                        logger.fine("resizing %s to %dx%d", texture, width, height / 2);
                        BufferedImage newImage = new BufferedImage(width, height / 2, BufferedImage.TYPE_INT_ARGB);
                        Graphics2D graphics2D = newImage.createGraphics();
                        graphics2D.drawImage(image, 0, 0, width, height / 2, 0, 0, width, height / 2, null);
                        image = newImage;
                    }
                }
                return image;
            }
        };

        TexturePackAPI.ChangeHandler.register(new TexturePackAPI.ChangeHandler(MCPatcherUtils.HD_TEXTURES, 1) {
            @Override
            protected void onChange() {
                imageCache.clear();
                MipmapHelper.reset();
                setTileSize();
                Minecraft minecraft = MCPatcherUtils.getMinecraft();
                minecraft.renderEngine.reloadTextures(minecraft);
                if (ColorizerWater.waterBuffer != ColorizerFoliage.foliageBuffer) {
                    refreshColorizer(ColorizerWater.waterBuffer, "/misc/watercolor.png");
                }
                refreshColorizer(ColorizerGrass.grassBuffer, "/misc/grasscolor.png");
                refreshColorizer(ColorizerFoliage.foliageBuffer, "/misc/foliagecolor.png");
            }
        });
    }

    private static int getAspectRatio(String texture) {
        int ratio = 0;
        if (useZombieHack) {
            BufferedImage image = MCPatcherUtils.readImage(TextureUtils.class.getResourceAsStream(texture));
            if (image != null) {
                int width = image.getWidth();
                int height = image.getHeight();
                if (width == 2 * height) {
                    ratio = 2;
                } else if (width == height) {
                    ratio = 1;
                }
            }
        }
        return ratio;
    }

    public static void registerTextureFX(java.util.List<TextureFX> textureList, TextureFX textureFX) {
        TextureFX fx = refreshTextureFX(textureFX);
        if (fx != null) {
            logger.finer("registering new TextureFX class %s", textureFX.getClass().getName());
            textureList.add(fx);
            fx.onTick();
        }
    }

    private static TextureFX refreshTextureFX(TextureFX textureFX) {
        if (textureFX instanceof TextureCompassFX ||
            textureFX instanceof TextureWatchFX ||
            textureFX instanceof TextureLavaFX ||
            textureFX instanceof TextureLavaFlowFX ||
            textureFX instanceof TextureWaterFX ||
            textureFX instanceof TextureWaterFlowFX ||
            textureFX instanceof TextureFlamesFX ||
            textureFX instanceof TexturePortalFX) {
            return null;
        }
        logger.info("attempting to refresh unknown animation %s", textureFX.getClass().getName());
        Minecraft minecraft = MCPatcherUtils.getMinecraft();
        Class<? extends TextureFX> textureFXClass = textureFX.getClass();
        for (int i = 0; i < 3; i++) {
            Constructor<? extends TextureFX> constructor;
            try {
                switch (i) {
                    case 0:
                        constructor = textureFXClass.getConstructor(Minecraft.class, Integer.TYPE);
                        return constructor.newInstance(minecraft, TileSize.int_size);

                    case 1:
                        constructor = textureFXClass.getConstructor(Minecraft.class);
                        return constructor.newInstance(minecraft);

                    case 2:
                        constructor = textureFXClass.getConstructor();
                        return constructor.newInstance();

                    default:
                        break;
                }
            } catch (NoSuchMethodException e) {
            } catch (IllegalAccessException e) {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (textureFX.imageData.length < TileSize.int_numBytes) {
            logger.finer("resizing %s buffer from %d to %d bytes",
                textureFXClass.getName(), textureFX.imageData.length, TileSize.int_numBytes
            );
            textureFX.imageData = new byte[TileSize.int_numBytes];
        }
        return textureFX;
    }

    public static void refreshTextureFX(java.util.List<TextureFX> textureList) {
        ArrayList<TextureFX> savedTextureFX = new ArrayList<TextureFX>();
        for (TextureFX t : textureList) {
            TextureFX fx = refreshTextureFX(t);
            if (fx != null) {
                savedTextureFX.add(fx);
            }
        }
        textureList.clear();
        CustomAnimation.clear();

        Minecraft minecraft = MCPatcherUtils.getMinecraft();
        //if (!fancyCompass || !FancyCompass.refresh()) { //Spout Removed
        //     textureList.add(new TextureCompassFX(minecraft)); //Spout Removed
        //} //Spout Removed
        textureList.add(new TextureWatchFX(minecraft));

        boolean isDefault = TexturePackAPI.isDefaultTexturePack();

        if (!isDefault && customLava) {
            CustomAnimation.addStripOrTile("/terrain.png", "lava_still", LAVA_STILL_TEXTURE_INDEX, 1, -1, -1);
            CustomAnimation.addStripOrTile("/terrain.png", "lava_flowing", LAVA_FLOWING_TEXTURE_INDEX, 2, 3, 6);
        } else if (animatedLava) {
            textureList.add(new TextureLavaFX());
            textureList.add(new TextureLavaFlowFX());
        }

        if (!isDefault && customWater) {
            CustomAnimation.addStripOrTile("/terrain.png", "water_still", WATER_STILL_TEXTURE_INDEX, 1, -1, -1);
            CustomAnimation.addStripOrTile("/terrain.png", "water_flowing", WATER_FLOWING_TEXTURE_INDEX, 2, 0, 0);
        } else if (animatedWater) {
            textureList.add(new TextureWaterFX());
            textureList.add(new TextureWaterFlowFX());
        }

        if (!isDefault && customFire && TexturePackAPI.hasResource("/anim/custom_fire_e_w.png") && TexturePackAPI.hasResource("/anim/custom_fire_n_s.png")) {
            CustomAnimation.addStrip("/terrain.png", "fire_n_s", FIRE_N_S_TEXTURE_INDEX, 1);
            CustomAnimation.addStrip("/terrain.png", "fire_e_w", FIRE_E_W_TEXTURE_INDEX, 1);
        } else if (animatedFire) {
            textureList.add(new TextureFlamesFX(0));
            textureList.add(new TextureFlamesFX(1));
        }

        if (!isDefault && customPortal && TexturePackAPI.hasResource("/anim/custom_portal.png")) {
            CustomAnimation.addStrip("/terrain.png", "portal", PORTAL_TEXTURE_INDEX, 1);
        } else if (animatedPortal) {
            textureList.add(new TexturePortalFX());
        }

        if (customOther) {
            addOtherTextureFX("/terrain.png", "terrain");
            addOtherTextureFX("/gui/items.png", "item");
            for (String name : TexturePackAPI.listResources("/anim/", ".properties")) {
                if (!isCustomTerrainItemResource(name)) {
                    CustomAnimation.addStrip(TexturePackAPI.getProperties(name));
                }
            }
        }

        for (TextureFX t : savedTextureFX) {
            textureList.add(t);
        }

        for (TextureFX t : textureList) {
            t.onTick();
        }

        CustomAnimation.updateAll();
    }

    private static void addOtherTextureFX(String textureName, String imageName) {
        for (int tileNum = 0; tileNum < 256; tileNum++) {
            String resource = "/anim/custom_" + imageName + "_" + tileNum + ".png";
            if (TexturePackAPI.hasResource(resource)) {
                CustomAnimation.addStrip(textureName, imageName + "_" + tileNum, tileNum, 1);
            }
        }
    }

    public static ByteBuffer getByteBuffer(ByteBuffer buffer, byte[] data) {
        buffer.clear();
        final int have = buffer.capacity();
        final int needed = data.length;
        if (needed > have || (reclaimGLMemory && have >= 4 * needed)) {
            logger.finest("resizing gl buffer from 0x%x to 0x%x", have, needed);
            buffer = GLAllocation.createDirectByteBuffer(needed);
        }
        buffer.put(data);
        buffer.position(0).limit(needed);
        TileSize.int_glBufferSize = needed;
        return buffer;
    }

    private static boolean isCustomTerrainItemResource(String resource) {
        resource = resource.replaceFirst("^/anim", "").replaceFirst("\\.(png|properties)$", "");
        return resource.equals("/custom_lava_still") ||
            resource.equals("/custom_lava_flowing") ||
            resource.equals("/custom_water_still") ||
            resource.equals("/custom_water_flowing") ||
            resource.equals("/custom_fire_n_s") ||
            resource.equals("/custom_fire_e_w") ||
            resource.equals("/custom_portal") ||
            resource.matches("^/custom_(terrain|item)_\\d+$");
    }

    public static BufferedImage getImage(Object o1, Object o2, String resource) throws IOException {
        return TexturePackAPI.getImage(resource);
    }

    public static boolean setTileSize() {  //Spout Private > Public
        int size = getTileSize();
        if (size == TileSize.int_size) {
            logger.fine("tile size %d unchanged", size);
            return false;
        } else {
            logger.fine("setting tile size to %d (was %d)", size, TileSize.int_size);
            TileSize.setTileSize(size);
            return true;
        }
    }

    private static int getTileSize() {
        int size = 0;
        enableResizing = false;
        for (Map.Entry<String, Integer> entry : expectedColumns.entrySet()) {
            BufferedImage image = TexturePackAPI.getImage(entry.getKey());
            if (image != null) {
                int newSize = image.getWidth() / entry.getValue();
                logger.fine("%s tile size is %d", entry.getKey(), newSize);
                size = Math.max(size, newSize);
            }
        }
        enableResizing = true;
        return size > 0 ? size : 16;
    }

    static BufferedImage resizeImage(BufferedImage image, int width) {
        if (width == image.getWidth()) {
            return image;
        }
        int height = image.getHeight() * width / image.getWidth();
        logger.finer("resizing to %dx%d", width, height);
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = newImage.createGraphics();
        graphics2D.drawImage(image, 0, 0, width, height, null);
        return newImage;
    }

    private static void refreshColorizer(int[] colorBuffer, String resource) {
        BufferedImage image = TexturePackAPI.getImage(resource);
        logger.fine("reloading %s", resource);
        if (image != null) {
            image.getRGB(0, 0, 256, 256, colorBuffer, 0, 256);
        }
    }

    public static boolean bindImageBegin() {
        if (bindImageReentry) {
            logger.warning("caught TextureFX.bindImage recursion");
            return false;
        }
        bindImageReentry = true;
        return true;
    }

    public static void bindImageEnd() {
        bindImageReentry = false;
    }

    public static PixelFormat getAAPixelFormat(PixelFormat format) {
        return aaSamples > 0 ? format.withSamples(aaSamples) : format;
    }
    
    // Spout HD Start
   // public void setTileSize(Minecraft var1) {
   // 	this.imageData = GLAllocation.createDirectByteBuffer(TileSize.int_glBufferSize);
   // 	this.refreshTextures();
   // 	TextureUtils.refreshTextureFX(this.textureList);
    //}
    // Spout HD End

}