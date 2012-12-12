package com.pclewis.mcpatcher.mod;

import com.pclewis.mcpatcher.MCLogger;
import com.pclewis.mcpatcher.MCPatcherUtils;
import com.pclewis.mcpatcher.TexturePackAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.TextureCompassFX;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Properties;

import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex3f;

class FancyCompass {
    private static final String ITEMS_PNG = "/gui/items.png";
    private static final String COMPASS_BASE_PNG = "/misc/compass_base.png";
    private static final String COMPASS_DIAL_PNG = "/misc/compass_dial.png";
    private static final String COMPASS_OVERLAY_PNG = "/misc/compass_overlay.png";
    private static final String COMPASS_PROPERTIES = "/misc/compass.properties";

    private static final int COMPASS_TILE_NUM = 54;
    private static final float RELATIVE_X = (COMPASS_TILE_NUM % 16) / 16.0f;
    private static final float RELATIVE_Y = (COMPASS_TILE_NUM / 16) / 16.0f;

    private static final boolean fboSupported = GLContext.getCapabilities().GL_EXT_framebuffer_object;

    private static FancyCompass instance;

    private final TextureCompassFX compass;
    private final float scaleX;
    private final float scaleY;
    private final float offsetX;
    private final float offsetY;
    private final boolean debug;

    private final int baseTexture;
    private final int dialTexture;
    private final int overlayTexture;
    private final int tileSize;
    private final int compassX;
    private final int compassY;
    private final int frameBuffer;

    private static final HashSet<Integer> keysDown = new HashSet<Integer>();

    private static final float STEP = 0.01f;
    private float plusX;
    private float plusY;
    private float plusOX;
    private float plusOY;

    private FancyCompass() {
        Minecraft minecraft = MCPatcherUtils.getMinecraft();
        RenderEngine renderEngine = minecraft.renderEngine;
        compass = new TextureCompassFX(minecraft);

        int targetTexture = renderEngine.getTexture(ITEMS_PNG);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, targetTexture);
        tileSize = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH) / 16;
        compassX = (int) (RELATIVE_X * tileSize * 16);
        compassY = (int) (RELATIVE_Y * tileSize * 16);

        Properties properties = TexturePackAPI.getProperties(COMPASS_PROPERTIES);
        scaleX = getFloatProperty(properties, "scaleX", 1.0f);
        scaleY = getFloatProperty(properties, "scaleY", 0.5f);
        offsetX = getFloatProperty(properties, "offsetX", 1.0f / (2 * tileSize));
        offsetY = getFloatProperty(properties, "offsetY", -1.0f / (2 * tileSize));
        renderEngine.blurTexture = getBooleanProperty(properties, "filter", false);
        debug = getBooleanProperty(properties, "debug", false);

        BufferedImage image = TexturePackAPI.getImage(COMPASS_BASE_PNG);
        if (image == null) {
            image = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
            BufferedImage items = TexturePackAPI.getImage(ITEMS_PNG);
            Graphics2D graphics2D = image.createGraphics();
            int sx = (int) (items.getWidth() * RELATIVE_X);
            int sy = (int) (items.getHeight() * RELATIVE_Y);
            graphics2D.drawImage(items,
                0, 0, image.getWidth(), image.getHeight(),
                sx, sy, sx + items.getWidth() / 16, sy + items.getHeight() / 16,
                null
            );
        }
        baseTexture = renderEngine.allocateAndSetupTexture(image);

        image = TexturePackAPI.getImage(COMPASS_DIAL_PNG);
        dialTexture = renderEngine.allocateAndSetupTexture(image);

        image = TexturePackAPI.getImage(COMPASS_OVERLAY_PNG);
        if (image == null) {
            overlayTexture = -1;
        } else {
            overlayTexture = renderEngine.allocateAndSetupTexture(image);
        }

        frameBuffer = glGenFramebuffersEXT();
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, frameBuffer);
        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL11.GL_TEXTURE_2D, targetTexture, 0);
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
    }

    private void onTick() {
        compass.onTick();

        boolean f = false;
        if (!debug) {
            f = true;
        } else if (tap(Keyboard.KEY_NUMPAD2)) {
            plusY -= STEP;
        } else if (tap(Keyboard.KEY_NUMPAD8)) {
            plusY += STEP;
        } else if (tap(Keyboard.KEY_NUMPAD4)) {
            plusX -= STEP;
        } else if (tap(Keyboard.KEY_NUMPAD6)) {
            plusX += STEP;
        } else if (tap(Keyboard.KEY_DOWN)) {
            plusOY += STEP;
        } else if (tap(Keyboard.KEY_UP)) {
            plusOY -= STEP;
        } else if (tap(Keyboard.KEY_LEFT)) {
            plusOX -= STEP;
        } else if (tap(Keyboard.KEY_RIGHT)) {
            plusOX += STEP;
        } else if (tap(Keyboard.KEY_MULTIPLY)) {
            plusX = plusY = plusOX = plusOY = 0.0f;
        } else {
            f = true;
        }

        GL11.glPushAttrib(GL11.GL_VIEWPORT_BIT | GL11.GL_SCISSOR_BIT | GL11.GL_DEPTH_BITS);
        GL11.glViewport(compassX, compassY, tileSize, tileSize);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(compassX, compassY, tileSize, tileSize);

        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, frameBuffer);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(-1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f);

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, baseTexture);
        drawBox();

        GL11.glPushMatrix();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, dialTexture);
        float angle = (float) (180.0 - compass.field_76866_j * 180.0 / Math.PI);
        GL11.glTranslatef(offsetX + plusOX, offsetY + plusOY, 0.0f);
        GL11.glScalef(scaleX + plusX, scaleY + plusY, 1.0f);
        GL11.glRotatef(angle, 0.0f, 0.0f, 1.0f);
        drawBox();
        GL11.glPopMatrix();

        if (overlayTexture >= 0) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, overlayTexture);
            drawBox();
        }

        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
        GL11.glPopAttrib();
    }

    private void drawBox() {
        GL11.glBegin(GL11.GL_QUADS);
        glTexCoord2f(0.0f, 0.0f);
        glVertex3f(-1.0f, -1.0f, 0.0f);
        glTexCoord2f(1.0f, 0.0f);
        glVertex3f(1.0f, -1.0f, 0.0f);
        glTexCoord2f(1.0f, 1.0f);
        glVertex3f(1.0f, 1.0f, 0.0f);
        glTexCoord2f(0.0f, 1.0f);
        glVertex3f(-1.0f, 1.0f, 0.0f);
        GL11.glEnd();
    }

    private void finish() {
        glDeleteFramebuffersEXT(frameBuffer);
    }

    private static boolean getBooleanProperty(Properties properties, String key, boolean defaultValue) {
        if (properties != null) {
            defaultValue = Boolean.parseBoolean(properties.getProperty(key, "" + defaultValue));
        }
        return defaultValue;
    }


    private static float getFloatProperty(Properties properties, String key, float defaultValue) {
        if (properties != null) {
            String value = properties.getProperty(key, "").trim();
            if (!value.equals("")) {
                try {
                    return Float.parseFloat(value);
                } catch (NumberFormatException ignore) {}
            }
        }
        return defaultValue;
    }

    private static boolean tap(int key) {
        if (Keyboard.isKeyDown(key)) {
            if (!keysDown.contains(key)) {
                keysDown.add(key);
                return true;
            }
        } else {
            keysDown.remove(key);
        }
        return false;
    }

    static boolean refresh() {
        if (instance != null) {
            instance.finish();
            instance = null;
        }
        if (fboSupported && TexturePackAPI.hasResource(COMPASS_DIAL_PNG)) {
            try {
                instance = new FancyCompass();
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                MCPatcherUtils.getMinecraft().renderEngine.blurTexture = false;
            }
        }
        return instance != null;
    }

    static void update() {
        if (instance != null) {
            instance.onTick();
        }
    }
}
