package com.prupe.mcpatcher.hd;

import com.prupe.mcpatcher.BlendMethod;
import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.InputHandler;
import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.TexturePackAPI;
import com.prupe.mcpatcher.hd.FancyDial$Layer;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.WeakHashMap;
import javax.imageio.ImageIO;
import net.minecraft.src.Icon;
import net.minecraft.src.ResourceLocation;
import net.minecraft.src.TextureAtlasSprite;
import net.minecraft.src.TextureClock;
import net.minecraft.src.TextureCompass;
import net.minecraft.src.TextureMap;
import net.minecraft.src.TextureObject;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;

public class FancyDial {
	private static final MCLogger logger = MCLogger.getLogger("Custom Animations", "Animation");
	private static final ResourceLocation ITEMS_PNG = new ResourceLocation("textures/atlas/items.png");
	private static final double ANGLE_UNSET = Double.MAX_VALUE;
	private static final int NUM_SCRATCH_TEXTURES = 3;
	private static final boolean fboSupported = GLContext.getCapabilities().GL_EXT_framebuffer_object;
	private static final boolean gl13Supported = GLContext.getCapabilities().OpenGL13;
	private static final boolean enableCompass = Config.getBoolean("Extended HD", "fancyCompass", true);
	private static final boolean enableClock = Config.getBoolean("Extended HD", "fancyClock", true);
	private static final boolean useGL13 = gl13Supported && Config.getBoolean("Extended HD", "useGL13", true);
	private static final boolean useScratchTexture = Config.getBoolean("Extended HD", "useScratchTexture", true);
	private static final int glAttributes;
	private static boolean initialized;
	private static final int drawList = GL11.glGenLists(1);
	private static final Map<TextureAtlasSprite, ResourceLocation> setupInfo = new WeakHashMap();
	private static final Map<TextureAtlasSprite, FancyDial> instances = new WeakHashMap();
	private final TextureAtlasSprite icon;
	private final ResourceLocation resource;
	private final String name;
	private final int x0;
	private final int y0;
	private final int width;
	private final int height;
	private final int itemsTexture;
	private final int[] scratchTexture = new int[4];
	private final ByteBuffer scratchBuffer;
	private final int[] frameBuffer = new int[4];
	private int scratchIndex;
	private Map<Double, ByteBuffer> itemFrames = new TreeMap();
	private int outputFrames;
	private boolean ok;
	private double lastAngle = Double.MAX_VALUE;
	private boolean lastItemFrameRenderer;
	private final List<FancyDial$Layer> layers = new ArrayList();
	private InputHandler keyboard;
	private static final float STEP = 0.01F;
	private float scaleXDelta;
	private float scaleYDelta;
	private float offsetXDelta;
	private float offsetYDelta;

	public static void setup(TextureAtlasSprite icon) {
		if (fboSupported) {
			String name = icon.getIconName();

			if ("compass".equals(icon.getIconName())) {
				if (!enableCompass) {
					return;
				}
			} else {
				if (!"clock".equals(icon.getIconName())) {
					logger.warning("ignoring custom animation for %s not compass or clock", new Object[] {icon.getIconName()});
					return;
				}

				if (!enableClock) {
					return;
				}
			}

			ResourceLocation resource = TexturePackAPI.newMCPatcherResourceLocation("dial/" + name + ".properties");

			if (TexturePackAPI.hasResource(resource)) {
				logger.fine("found custom %s (%s)", new Object[] {name, resource});
				setupInfo.put(icon, resource);
			}
		}
	}

	public static boolean update(TextureAtlasSprite icon, boolean itemFrameRenderer) {
		if (!initialized) {
			logger.finer("deferring %s update until initialization finishes", new Object[] {icon.getIconName()});
			return false;
		} else {
			FancyDial instance = (FancyDial)instances.get(icon);

			if (instance == null) {
				instance = getInstance(icon);

				if (instance == null) {
					return false;
				}
			}

			return instance.render(itemFrameRenderer);
		}
	}

	static void clearAll() {
		logger.finer("FancyDial.clearAll", new Object[0]);
		Iterator i$ = instances.values().iterator();

		while (i$.hasNext()) {
			FancyDial instance = (FancyDial)i$.next();

			if (instance != null) {
				instance.finish();
			}
		}

		instances.clear();
		initialized = true;
	}

	static void registerAnimations() {
		TextureObject texture = TexturePackAPI.getTextureObject(ITEMS_PNG);

		if (texture instanceof TextureMap) {
			List animations = ((TextureMap)texture).listAnimatedSprites;
			Iterator i$ = instances.values().iterator();

			while (i$.hasNext()) {
				FancyDial instance = (FancyDial)i$.next();
				instance.registerAnimation(animations);
			}
		}
	}

	void registerAnimation(List<TextureAtlasSprite> animations) {
		if (!animations.contains(this.icon)) {
			animations.add(this.icon);

			if (this.icon.framesTextureData == null) {
				this.icon.framesTextureData = new ArrayList();
			}

			if (this.icon.framesTextureData.isEmpty()) {
				int[] dummyRGB = new int[this.width * this.height];
				Arrays.fill(dummyRGB, -65281);
				this.icon.framesTextureData.add(dummyRGB);
			}

			logger.fine("registered %s animation", new Object[] {this.name});
		}
	}

	private static FancyDial getInstance(TextureAtlasSprite icon) {
		ResourceLocation resource = (ResourceLocation)setupInfo.get(icon);
		Properties properties = TexturePackAPI.getProperties(resource);
		setupInfo.remove(icon);

		if (properties == null) {
			return null;
		} else {
			try {
				FancyDial e = new FancyDial(icon, resource, properties);

				if (e.ok) {
					instances.put(icon, e);
					return e;
				}

				e.finish();
			} catch (Throwable var4) {
				var4.printStackTrace();
			}

			return null;
		}
	}

	private FancyDial(TextureAtlasSprite icon, ResourceLocation resource, Properties properties) {
		this.icon = icon;
		this.resource = resource;
		this.name = icon.getIconName();
		this.x0 = icon.getOriginX();
		this.y0 = icon.getOriginY();
		this.width = icon.getIconWidth();
		this.height = icon.getIconHeight();
		this.scratchBuffer = ByteBuffer.allocateDirect(4 * this.width * this.height);
		this.itemsTexture = TexturePackAPI.getTextureIfLoaded(ITEMS_PNG);

		if (this.itemsTexture < 0) {
			logger.severe("could not get items texture", new Object[0]);
		} else {
			if (useScratchTexture) {
				logger.fine("rendering %s to %dx%d scratch texture", new Object[] {this.name, Integer.valueOf(this.width), Integer.valueOf(this.height)});
			} else {
				logger.fine("rendering %s directly to %s", new Object[] {this.name, ITEMS_PNG});
			}

			for (int debug = 0; debug < this.scratchTexture.length; ++debug) {
				this.scratchTexture[debug] = debug == 3 ? this.itemsTexture : this.setupScratchTexture(debug);
				this.frameBuffer[debug] = this.setupFrameBuffer(this.scratchTexture[debug]);
			}

			boolean var7 = false;
			int glError = 0;

			while (true) {
				FancyDial$Layer layer = this.newLayer(resource, properties, "." + glError);

				if (layer == null) {
					if (glError > 0) {
						this.keyboard = new InputHandler(this.name, var7);

						if (this.layers.size() < 2) {
							logger.error("custom %s needs at least two layers defined", new Object[] {this.name});
							return;
						}

						this.outputFrames = MCPatcherUtils.getIntProperty(properties, "outputFrames", 0);
						glError = GL11.glGetError();

						if (glError != 0) {
							logger.severe("%s during %s setup", new Object[] {GLU.gluErrorString(glError), this.name});
							return;
						}

						this.ok = true;
						return;
					}
				} else {
					this.layers.add(layer);
					var7 |= layer.debug;
					logger.fine("  new %s", new Object[] {layer});
				}

				++glError;
			}
		}
	}

	private int setupScratchTexture(int i) {
		int targetTexture;

		if (useScratchTexture) {
			targetTexture = GL11.glGenTextures();
			MipmapHelper.setupTexture(targetTexture, this.width, this.height, TexturePackAPI.transformResourceLocation(this.resource, ".properties", "_scratch" + i).getResourcePath());
		} else {
			targetTexture = -1;
		}

		return targetTexture;
	}

	private int setupFrameBuffer(int texture) {
		if (texture < 0) {
			return -1;
		} else {
			int frameBuffer = EXTFramebufferObject.glGenFramebuffersEXT();

			if (frameBuffer < 0) {
				logger.severe("could not get framebuffer object", new Object[0]);
				this.ok = false;
				return -1;
			} else {
				EXTFramebufferObject.glBindFramebufferEXT(36160, frameBuffer);
				EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36064, 3553, texture, 0);
				EXTFramebufferObject.glBindFramebufferEXT(36160, 0);
				return frameBuffer;
			}
		}
	}

	private boolean render(boolean itemFrameRenderer) {
		if (!this.ok) {
			return false;
		} else {
			if (!itemFrameRenderer) {
				boolean angle = true;

				if (!this.keyboard.isEnabled()) {
					angle = false;
				} else if (this.keyboard.isKeyPressed(80)) {
					this.scaleYDelta -= 0.01F;
				} else if (this.keyboard.isKeyPressed(72)) {
					this.scaleYDelta += 0.01F;
				} else if (this.keyboard.isKeyPressed(75)) {
					this.scaleXDelta -= 0.01F;
				} else if (this.keyboard.isKeyPressed(77)) {
					this.scaleXDelta += 0.01F;
				} else if (this.keyboard.isKeyPressed(208)) {
					this.offsetYDelta += 0.01F;
				} else if (this.keyboard.isKeyPressed(200)) {
					this.offsetYDelta -= 0.01F;
				} else if (this.keyboard.isKeyPressed(203)) {
					this.offsetXDelta -= 0.01F;
				} else if (this.keyboard.isKeyPressed(205)) {
					this.offsetXDelta += 0.01F;
				} else if (this.keyboard.isKeyPressed(55)) {
					this.scaleXDelta = this.scaleYDelta = this.offsetXDelta = this.offsetYDelta = 0.0F;
				} else {
					angle = false;
				}

				if (angle) {
					logger.info("", new Object[0]);
					logger.info("scaleX  %+f", new Object[] {Float.valueOf(this.scaleXDelta)});
					logger.info("scaleY  %+f", new Object[] {Float.valueOf(this.scaleYDelta)});
					logger.info("offsetX %+f", new Object[] {Float.valueOf(this.offsetXDelta)});
					logger.info("offsetY %+f", new Object[] {Float.valueOf(this.offsetYDelta)});
					this.lastAngle = Double.MAX_VALUE;
				}

				if (this.outputFrames > 0) {
					this.writeCustomImage();
					this.outputFrames = 0;
				}
			}

			double var5 = getAngle(this.icon);
			int var6;

			if (!useScratchTexture) {
				if (var5 != this.lastAngle) {
					this.renderToItems(var5);
					this.lastAngle = var5;
				}
			} else if (itemFrameRenderer) {
				ByteBuffer glError = (ByteBuffer)this.itemFrames.get(Double.valueOf(var5));

				if (glError == null) {
					logger.fine("rendering %s at angle %f for item frame", new Object[] {this.name, Double.valueOf(var5)});
					glError = ByteBuffer.allocateDirect(this.width * this.height * 4);
					this.renderToItems(var5);
					this.readTextureToBuffer(glError);
					this.itemFrames.put(Double.valueOf(var5), glError);
				} else {
					this.copyBufferToItemsTexture(glError);
				}

				this.lastItemFrameRenderer = true;
			} else if (this.lastAngle == Double.MAX_VALUE) {
				for (var6 = 0; var6 < 3; ++var6) {
					this.renderToFB(var5, this.frameBuffer[var6]);
				}

				this.readTextureToBuffer(this.scratchTexture[0], this.scratchBuffer);
				this.copyBufferToItemsTexture(this.scratchBuffer);
				this.lastAngle = var5;
				this.scratchIndex = 0;
			} else if (this.lastItemFrameRenderer || var5 != this.lastAngle) {
				var6 = (this.scratchIndex + 1) % 3;

				if (var5 != this.lastAngle) {
					this.renderToFB(var5, this.frameBuffer[var6]);
					this.readTextureToBuffer(this.scratchTexture[this.scratchIndex], this.scratchBuffer);
				}

				this.copyBufferToItemsTexture(this.scratchBuffer);
				this.lastAngle = var5;
				this.scratchIndex = var6;
				this.lastItemFrameRenderer = false;
			}

			var6 = GL11.glGetError();

			if (var6 != 0) {
				logger.severe("%s during %s update", new Object[] {GLU.gluErrorString(var6), this.name});
				this.ok = false;
			}

			return this.ok;
		}
	}

	private void writeCustomImage() {
		try {
			BufferedImage e = new BufferedImage(this.width, this.outputFrames * this.height, 2);
			IntBuffer intBuffer = this.scratchBuffer.asIntBuffer();
			int[] argb = new int[this.width * this.height];
			File path = MCPatcherUtils.getGamePath(new String[] {"custom_" + this.name + ".png"});
			logger.info("generating %d %s frames", new Object[] {Integer.valueOf(this.outputFrames), this.name});

			for (int i = 0; i < this.outputFrames; ++i) {
				this.renderToItems((double)i * (360.0D / (double)this.outputFrames));
				this.readTextureToBuffer(this.scratchBuffer);
				intBuffer.position(0);

				for (int j = 0; j < argb.length; ++j) {
					argb[j] = Integer.rotateRight(intBuffer.get(j), 8);
				}

				e.setRGB(0, i * this.height, this.width, this.height, argb, 0, this.width);
			}

			ImageIO.write(e, "png", path);
			logger.info("wrote %dx%d %s", new Object[] {Integer.valueOf(e.getWidth()), Integer.valueOf(e.getHeight()), path.getPath()});
		} catch (Throwable var7) {
			var7.printStackTrace();
		}
	}

	private void renderToItems(double angle) {
		GL11.glPushAttrib(glAttributes);
		GL11.glViewport(this.x0, this.y0, this.width, this.height);
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		GL11.glScissor(this.x0, this.y0, this.width, this.height);
		EXTFramebufferObject.glBindFramebufferEXT(36160, this.frameBuffer[3]);
		this.renderImpl(angle);
	}

	private void renderToFB(double angle, int bindFB) {
		GL11.glPushAttrib(glAttributes);
		GL11.glViewport(0, 0, this.width, this.height);
		EXTFramebufferObject.glBindFramebufferEXT(36160, bindFB);
		this.renderImpl(angle);
	}

	private void renderImpl(double angle) {
		boolean lightmapEnabled = false;

		if (gl13Supported) {
			GL13.glActiveTexture(GL13.GL_TEXTURE1);
			lightmapEnabled = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);

			if (lightmapEnabled) {
				GL11.glDisable(GL11.GL_TEXTURE_2D);
			}

			GL13.glActiveTexture(GL13.GL_TEXTURE0);
		}

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_LIGHTING);

		if (useGL13) {
			GL11.glDisable(GL13.GL_MULTISAMPLE);
		}

		GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
		GL11.glClear(16384);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glOrtho(-1.0D, 1.0D, -1.0D, 1.0D, -1.0D, 1.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		Iterator i$ = this.layers.iterator();

		while (i$.hasNext()) {
			FancyDial$Layer layer = (FancyDial$Layer)i$.next();
			layer.blendMethod.applyBlending();
			GL11.glPushMatrix();
			TexturePackAPI.bindTexture(layer.textureName);
			float offsetX = layer.offsetX;
			float offsetY = layer.offsetY;
			float scaleX = layer.scaleX;
			float scaleY = layer.scaleY;

			if (layer.debug) {
				offsetX += this.offsetXDelta;
				offsetY += this.offsetYDelta;
				scaleX += this.scaleXDelta;
				scaleY += this.scaleYDelta;
			}

			GL11.glTranslatef(offsetX, offsetY, 0.0F);
			GL11.glScalef(scaleX, scaleY, 1.0F);
			float layerAngle = (float)(angle * (double)layer.rotationMultiplier + (double)layer.rotationOffset);
			GL11.glRotatef(layerAngle, 0.0F, 0.0F, 1.0F);
			GL11.glCallList(drawList);
			GL11.glPopMatrix();
		}

		EXTFramebufferObject.glBindFramebufferEXT(36160, 0);
		GL11.glPopAttrib();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPopMatrix();

		if (lightmapEnabled) {
			GL13.glActiveTexture(GL13.GL_TEXTURE1);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
		}

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	private void readTextureToBuffer(int texture, ByteBuffer buffer) {
		TexturePackAPI.bindTexture(texture);
		buffer.position(0);
		GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
	}

	private void readTextureToBuffer(ByteBuffer buffer) {
		EXTFramebufferObject.glBindFramebufferEXT(36160, this.frameBuffer[3]);
		buffer.position(0);
		GL11.glReadPixels(this.x0, this.y0, this.width, this.height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
		EXTFramebufferObject.glBindFramebufferEXT(36160, 0);
	}

	private void copyBufferToItemsTexture(ByteBuffer buffer) {
		TexturePackAPI.bindTexture(this.itemsTexture);
		buffer.position(0);
		GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, this.x0, this.y0, this.width, this.height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
	}

	private static void drawBox() {
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
		for (int i = 0; i < this.frameBuffer.length; ++i) {
			if (this.frameBuffer[i] >= 0) {
				EXTFramebufferObject.glDeleteFramebuffersEXT(this.frameBuffer[i]);
				this.frameBuffer[i] = -1;
			}

			if (i < 3 && this.scratchTexture[i] >= 0) {
				TexturePackAPI.deleteTexture(this.scratchTexture[i]);
				this.scratchTexture[i] = -1;
			}
		}

		this.itemFrames.clear();
		this.layers.clear();
		this.ok = false;
	}

	public String toString() {
		return String.format("FancyDial{%s, %dx%d @ %d,%d}", new Object[] {this.name, Integer.valueOf(this.width), Integer.valueOf(this.height), Integer.valueOf(this.x0), Integer.valueOf(this.y0)});
	}

	protected void finalize() throws Throwable {
		this.finish();
		super.finalize();
	}

	private static double getAngle(Icon icon) {
		return icon instanceof TextureCompass ? ((TextureCompass)icon).currentAngle * 180.0D / Math.PI : (icon instanceof TextureClock ? ((TextureClock)icon).field_94239_h * 360.0D : 0.0D);
	}

	FancyDial$Layer newLayer(ResourceLocation resource, Properties properties, String suffix) {
		String textureName = MCPatcherUtils.getStringProperty(properties, "source" + suffix, "");

		if (textureName.isEmpty()) {
			return null;
		} else {
			ResourceLocation textureResource = TexturePackAPI.parseResourceLocation(resource, textureName);

			if (textureResource == null) {
				return null;
			} else if (!TexturePackAPI.hasResource(textureResource)) {
				logger.error("%s: could not read %s", new Object[] {resource, textureResource});
				return null;
			} else {
				float scaleX = MCPatcherUtils.getFloatProperty(properties, "scaleX" + suffix, 1.0F);
				float scaleY = MCPatcherUtils.getFloatProperty(properties, "scaleY" + suffix, 1.0F);
				float offsetX = MCPatcherUtils.getFloatProperty(properties, "offsetX" + suffix, 0.0F);
				float offsetY = MCPatcherUtils.getFloatProperty(properties, "offsetY" + suffix, 0.0F);
				float angleMultiplier = MCPatcherUtils.getFloatProperty(properties, "rotationSpeed" + suffix, 0.0F);
				float angleOffset = MCPatcherUtils.getFloatProperty(properties, "rotationOffset" + suffix, 0.0F);
				String blend = MCPatcherUtils.getStringProperty(properties, "blend" + suffix, "alpha");
				BlendMethod blendMethod = BlendMethod.parse(blend);

				if (blendMethod == null) {
					logger.error("%s: unknown blend method %s", new Object[] {resource, blend});
					return null;
				} else {
					boolean debug = MCPatcherUtils.getBooleanProperty(properties, "debug" + suffix, false);
					return new FancyDial$Layer(this, textureResource, scaleX, scaleY, offsetX, offsetY, angleMultiplier, angleOffset, blendMethod, debug);
				}
			}
		}
	}

	static {
		logger.config("fbo: supported=%s", new Object[] {Boolean.valueOf(fboSupported)});
		logger.config("GL13: supported=%s, enabled=%s", new Object[] {Boolean.valueOf(gl13Supported), Boolean.valueOf(useGL13)});
		int bits = 527702;

		if (useGL13) {
			bits |= 536870912;
		}

		glAttributes = bits;
		GL11.glNewList(drawList, GL11.GL_COMPILE);
		drawBox();
		GL11.glEndList();
	}
}
