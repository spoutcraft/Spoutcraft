package com.pclewis.mcpatcher.mod;

import com.pclewis.mcpatcher.MCLogger;
import com.pclewis.mcpatcher.MCPatcherUtils;
import com.pclewis.mcpatcher.TexturePackAPI;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.HashSet;
import java.util.Properties;
import net.minecraft.client.Minecraft;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.TextureCompassFX;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

class FancyCompass {
	private static final MCLogger logger = MCLogger.getLogger("HD Textures");
	private static final String ITEMS_PNG = "/gui/items.png";
	private static final String COMPASS_BASE_PNG = "/misc/compass_base.png";
	private static final String COMPASS_DIAL_PNG = "/misc/compass_dial.png";
	private static final String COMPASS_OVERLAY_PNG = "/misc/compass_overlay.png";
	private static final String COMPASS_PROPERTIES = "/misc/compass.properties";
	private static final int COMPASS_TILE_NUM = 54;
	private static final float RELATIVE_X = 0.375F;
	private static final float RELATIVE_Y = 0.1875F;
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
	private static final HashSet keysDown = new HashSet();
	private static final float STEP = 0.01F;
	private float plusX;
	private float plusY;
	private float plusOX;
	private float plusOY;

	private FancyCompass() {
		Minecraft var1 = MCPatcherUtils.getMinecraft();
		RenderEngine var2 = var1.renderEngine;
		this.compass = new TextureCompassFX(var1);
		int var3 = var2.getTexture("/gui/items.png");
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, var3);
		this.tileSize = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH) / 16;
		this.compassX = (int)(0.375F * (float)this.tileSize * 16.0F);
		this.compassY = (int)(0.1875F * (float)this.tileSize * 16.0F);
		Properties var4 = TexturePackAPI.getProperties("/misc/compass.properties");
		this.scaleX = getFloatProperty(var4, "scaleX", 1.0F);
		this.scaleY = getFloatProperty(var4, "scaleY", 0.5F);
		this.offsetX = getFloatProperty(var4, "offsetX", 1.0F / (float)(2 * this.tileSize));
		this.offsetY = getFloatProperty(var4, "offsetY", -1.0F / (float)(2 * this.tileSize));
		var2.blurTexture = getBooleanProperty(var4, "filter", false);
		this.debug = getBooleanProperty(var4, "debug", false);
		BufferedImage var5 = TexturePackAPI.getImage("/misc/compass_base.png");

		if (var5 == null) {
			var5 = new BufferedImage(this.tileSize, this.tileSize, 2);
			BufferedImage var6 = TexturePackAPI.getImage("/gui/items.png");
			Graphics2D var7 = var5.createGraphics();
			int var8 = (int)((float)var6.getWidth() * 0.375F);
			int var9 = (int)((float)var6.getHeight() * 0.1875F);
			var7.drawImage(var6, 0, 0, var5.getWidth(), var5.getHeight(), var8, var9, var8 + var6.getWidth() / 16, var9 + var6.getHeight() / 16, (ImageObserver)null);
		}

		this.baseTexture = var2.allocateAndSetupTexture(var5);
		var5 = TexturePackAPI.getImage("/misc/compass_dial.png");
		this.dialTexture = var2.allocateAndSetupTexture(var5);
		var5 = TexturePackAPI.getImage("/misc/compass_overlay.png");

		if (var5 == null) {
			this.overlayTexture = -1;
		} else {
			this.overlayTexture = var2.allocateAndSetupTexture(var5);
		}

		this.frameBuffer = EXTFramebufferObject.glGenFramebuffersEXT();
		EXTFramebufferObject.glBindFramebufferEXT(36160, this.frameBuffer);
		EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36064, 3553, var3, 0);
		EXTFramebufferObject.glBindFramebufferEXT(36160, 0);
	}

	private void onTick() {
		this.compass.onTick();
		boolean var1 = false;

		if (!this.debug) {
			var1 = true;
		} else if (tap(80)) {
			this.plusY -= 0.01F;
		} else if (tap(72)) {
			this.plusY += 0.01F;
		} else if (tap(75)) {
			this.plusX -= 0.01F;
		} else if (tap(77)) {
			this.plusX += 0.01F;
		} else if (tap(208)) {
			this.plusOY += 0.01F;
		} else if (tap(200)) {
			this.plusOY -= 0.01F;
		} else if (tap(203)) {
			this.plusOX -= 0.01F;
		} else if (tap(205)) {
			this.plusOX += 0.01F;
		} else if (tap(55)) {
			this.plusX = this.plusY = this.plusOX = this.plusOY = 0.0F;
		} else {
			var1 = true;
		}

		if (!var1) {
			logger.info("", new Object[0]);
			logger.info("scaleX = %f", new Object[] {Float.valueOf(this.scaleX + this.plusX)});
			logger.info("scaleY = %f", new Object[] {Float.valueOf(this.scaleY + this.plusY)});
			logger.info("offsetX = %f", new Object[] {Float.valueOf(this.offsetX + this.plusOX)});
			logger.info("offsetY = %f", new Object[] {Float.valueOf(this.offsetY + this.plusOY)});
		}

		GL11.glPushAttrib(527702);
		GL11.glViewport(this.compassX, this.compassY, this.tileSize, this.tileSize);
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		GL11.glScissor(this.compassX, this.compassY, this.tileSize, this.tileSize);
		EXTFramebufferObject.glBindFramebufferEXT(36160, this.frameBuffer);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
		GL11.glClear(16384);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(-1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 1.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.baseTexture);
		this.drawBox();
		GL11.glPushMatrix();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.dialTexture);
		float var2 = (float)(180.0D - this.compass.field_76868_i * 180.0D / Math.PI);
		GL11.glTranslatef(this.offsetX + this.plusOX, this.offsetY + this.plusOY, 0.0F);
		GL11.glScalef(this.scaleX + this.plusX, this.scaleY + this.plusY, 1.0F);
		GL11.glRotatef(var2, 0.0F, 0.0F, 1.0F);
		this.drawBox();
		GL11.glPopMatrix();

		if (this.overlayTexture >= 0) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.overlayTexture);
			this.drawBox();
		}

		EXTFramebufferObject.glBindFramebufferEXT(36160, 0);
		GL11.glPopAttrib();
	}

	private void drawBox() {
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0.0F, 0.0F);
		GL11.glVertex3f(-1.0F, -1.0F, 0.0F);
		GL11.glTexCoord2f(1.0F, 0.0F);
		GL11.glVertex3f(1.0F, -1.0F, 0.0F);
		GL11.glTexCoord2f(1.0F, 1.0F);
		GL11.glVertex3f(1.0F, 1.0F, 0.0F);
		GL11.glTexCoord2f(0.0F, 1.0F);
		GL11.glVertex3f(-1.0F, 1.0F, 0.0F);
		GL11.glEnd();
	}

	private void finish() {
		EXTFramebufferObject.glDeleteFramebuffersEXT(this.frameBuffer);
	}

	private static boolean getBooleanProperty(Properties var0, String var1, boolean var2) {
		if (var0 != null) {
			var2 = Boolean.parseBoolean(var0.getProperty(var1, "" + var2));
		}

		return var2;
	}

	private static float getFloatProperty(Properties var0, String var1, float var2) {
		if (var0 != null) {
			String var3 = var0.getProperty(var1, "").trim();

			if (!var3.equals("")) {
				try {
					return Float.parseFloat(var3);
				} catch (NumberFormatException var5) {
					logger.severe("%s: invalid value %s for %s", new Object[] {"/misc/compass.properties", var3, var1});
				}
			}
		}

		return var2;
	}

	private static boolean tap(int var0) {
		if (Keyboard.isKeyDown(var0)) {
			if (!keysDown.contains(Integer.valueOf(var0))) {
				keysDown.add(Integer.valueOf(var0));
				return true;
			}
		} else {
			keysDown.remove(Integer.valueOf(var0));
		}

		return false;
	}

	static boolean refresh() {
		if (instance != null) {
			instance.finish();
			instance = null;
		}

		if (fboSupported && TexturePackAPI.hasResource("/misc/compass_dial.png")) {
			try {
				instance = new FancyCompass();
			} catch (Throwable var4) {
				var4.printStackTrace();
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
