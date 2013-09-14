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
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.WeakHashMap;
import javax.imageio.ImageIO;
import net.minecraft.src.Icon;
import net.minecraft.src.ResourceLocation;
import net.minecraft.src.TextureAtlasSprite;
import net.minecraft.src.TextureClock;
import net.minecraft.src.TextureCompass;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;

public class FancyDial {
	private static final MCLogger logger = MCLogger.getLogger("Custom Animations", "Animation");
	private static final ResourceLocation ITEMS_PNG = new ResourceLocation("textures/atlas/items.png");
	private static final double ANGLE_UNSET = Double.MAX_VALUE;
	private static final boolean fboSupported = GLContext.getCapabilities().GL_EXT_framebuffer_object;
	private static final boolean gl13Supported = GLContext.getCapabilities().OpenGL13;
	private static final boolean enableCompass = Config.getBoolean("Extended HD", "fancyCompass", true);
	private static final boolean enableClock = Config.getBoolean("Extended HD", "fancyClock", true);
	private static final boolean useGL13 = Config.getBoolean("Extended HD", "useGL13", true);
	private static final boolean useScratchTexture = Config.getBoolean("Extended HD", "useScratchTexture", true);
	private static final int glAttributes;
	private static boolean initialized;
	private static boolean inUpdateAll;
	private static final int drawList = GL11.glGenLists(1);
	private static final Field subTexturesField;
	private static final Map<TextureAtlasSprite, ResourceLocation> setupInfo = new WeakHashMap();
	private static final Map<TextureAtlasSprite, FancyDial> instances = new WeakHashMap();
	private final TextureAtlasSprite icon;
	private final String name;
	private final int x0;
	private final int y0;
	private final int width;
	private final int height;
	private final boolean needExtraUpdate;
	private final int itemsTexture;
	private int scratchTexture;
	private final ByteBuffer scratchTextureBuffer;
	private int frameBuffer;
	private int outputFrames;
	private boolean ok;
	private double lastAngle = Double.MAX_VALUE;
	private boolean skipPostRender;
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

	public static boolean update(TextureAtlasSprite icon) {
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

			return instance.render();
		}
	}

	static void updateAll() {
		if (!initialized) {
			logger.finer("deferring %s update until initialization finishes", new Object[] {FancyDial.class.getSimpleName()});
		} else {
			if (!setupInfo.isEmpty()) {
				ArrayList i$ = new ArrayList();
				i$.addAll(setupInfo.keySet());
				Iterator instance = i$.iterator();

				while (instance.hasNext()) {
					TextureAtlasSprite icon = (TextureAtlasSprite)instance.next();
					getInstance(icon);
				}
			}

			inUpdateAll = true;
			Iterator i$1 = instances.values().iterator();

			while (i$1.hasNext()) {
				FancyDial instance1 = (FancyDial)i$1.next();

				if (instance1 != null && instance1.needExtraUpdate) {
					instance1.icon.updateAnimation();
				}
			}

			inUpdateAll = false;
		}
	}

	static void postUpdateAll() {
		if (initialized) {
			Iterator i$ = instances.values().iterator();

			while (i$.hasNext()) {
				FancyDial instance = (FancyDial)i$.next();

				if (instance != null) {
					instance.postRender();
				}
			}
		}
	}

	static void refresh() {
		logger.finer("FancyDial.refresh", new Object[0]);
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
		this.name = icon.getIconName();
		this.x0 = icon.func_130010_a();
		this.y0 = icon.func_110967_i();
		this.width = icon.getOriginX();
		this.height = icon.getOriginY();
		this.needExtraUpdate = !hasAnimation(icon);

		if (this.needExtraUpdate) {
			logger.fine("%s needs direct .update() call", new Object[] {icon.getIconName()});
		}

		TexturePackAPI.bindTexture(ITEMS_PNG);
		this.itemsTexture = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
		int targetTexture;

		if (useScratchTexture) {
			BufferedImage debug = new BufferedImage(this.width, this.height, 2);
			this.scratchTexture = GL11.glGenTextures();
			MipmapHelper.setupTexture(this.scratchTexture, debug, false, false, TexturePackAPI.transformResourceLocation(resource, ".properties", "_scratch"));
			targetTexture = this.scratchTexture;
			this.scratchTextureBuffer = ByteBuffer.allocateDirect(4 * this.width * this.height);
			logger.fine("rendering %s to %dx%d scratch texture %d", new Object[] {this.name, Integer.valueOf(this.width), Integer.valueOf(this.height), Integer.valueOf(this.scratchTexture)});
		} else {
			this.scratchTexture = -1;
			this.scratchTextureBuffer = null;
			logger.fine("rendering %s directly to %s", new Object[] {this.name, ITEMS_PNG});
			targetTexture = this.itemsTexture;
		}

		if (this.itemsTexture < 0) {
			logger.severe("could not get items texture", new Object[0]);
		} else {
			logger.fine("setting up %s", new Object[] {this});
			boolean var8 = false;
			int glError = 0;

			while (true) {
				FancyDial$Layer layer = this.newLayer(resource, properties, "." + glError);

				if (layer == null) {
					if (glError > 0) {
						this.keyboard = new InputHandler(this.name, var8);

						if (this.layers.size() < 2) {
							logger.error("custom %s needs at least two layers defined", new Object[] {this.name});
							return;
						}

						this.outputFrames = MCPatcherUtils.getIntProperty(properties, "outputFrames", 0);
						this.frameBuffer = EXTFramebufferObject.glGenFramebuffersEXT();

						if (this.frameBuffer < 0) {
							logger.severe("could not get framebuffer object", new Object[0]);
							return;
						}

						EXTFramebufferObject.glBindFramebufferEXT(36160, this.frameBuffer);
						EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36064, 3553, targetTexture, 0);
						EXTFramebufferObject.glBindFramebufferEXT(36160, 0);
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
					var8 |= layer.debug;
					logger.fine("  new %s", new Object[] {layer});
				}

				++glError;
			}
		}
	}

	private boolean render() {
		if (!this.ok) {
			return false;
		} else {
			boolean changed = true;

			if (!this.keyboard.isEnabled()) {
				changed = false;
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
				changed = false;
			}

			if (changed) {
				logger.info("", new Object[0]);
				logger.info("scaleX  %+f", new Object[] {Float.valueOf(this.scaleXDelta)});
				logger.info("scaleY  %+f", new Object[] {Float.valueOf(this.scaleYDelta)});
				logger.info("offsetX %+f", new Object[] {Float.valueOf(this.offsetXDelta)});
				logger.info("offsetY %+f", new Object[] {Float.valueOf(this.offsetYDelta)});
				this.lastAngle = Double.MAX_VALUE;
			}

			if (this.outputFrames > 0) {
				try {
					BufferedImage e = new BufferedImage(this.width, this.outputFrames * this.height, 2);
					ByteBuffer byteBuffer = this.scratchTextureBuffer == null ? ByteBuffer.allocateDirect(4 * this.width * this.height) : this.scratchTextureBuffer;
					IntBuffer intBuffer = byteBuffer.asIntBuffer();
					int[] argb = new int[this.width * this.height];
					EXTFramebufferObject.glBindFramebufferEXT(36160, this.frameBuffer);
					File path = MCPatcherUtils.getMinecraftPath(new String[] {"custom_" + this.name + ".png"});
					logger.info("generating %d %s frames", new Object[] {Integer.valueOf(this.outputFrames), this.name});

					for (int i = 0; i < this.outputFrames; ++i) {
						this.render((double)i * (360.0D / (double)this.outputFrames), false);

						if (this.scratchTexture < 0) {
							byteBuffer.position(0);
							GL11.glReadPixels(this.x0, this.y0, this.width, this.height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, byteBuffer);
						}

						intBuffer.position(0);

						for (int j = 0; j < argb.length; ++j) {
							argb[j] = Integer.rotateRight(intBuffer.get(j), 8);
						}

						e.setRGB(0, i * this.height, this.width, this.height, argb, 0, this.width);
					}

					ImageIO.write(e, "png", path);
					logger.info("wrote %dx%d %s", new Object[] {Integer.valueOf(e.getWidth()), Integer.valueOf(e.getHeight()), path.getPath()});
				} catch (Throwable var12) {
					var12.printStackTrace();
				} finally {
					EXTFramebufferObject.glBindFramebufferEXT(36160, 0);
				}

				this.outputFrames = 0;
			}

			return this.render(getAngle(this.icon), true);
		}
	}

	private boolean render(double angle, boolean bindFB) {
		if (angle == this.lastAngle) {
			this.skipPostRender = true;
			return true;
		} else {
			this.skipPostRender = false;
			this.lastAngle = angle;
			GL11.glPushAttrib(glAttributes);

			if (this.scratchTexture >= 0) {
				GL11.glViewport(0, 0, this.width, this.height);
			} else {
				GL11.glViewport(this.x0, this.y0, this.width, this.height);
				GL11.glEnable(GL11.GL_SCISSOR_TEST);
				GL11.glScissor(this.x0, this.y0, this.width, this.height);
			}

			if (bindFB) {
				EXTFramebufferObject.glBindFramebufferEXT(36160, this.frameBuffer);
			}

			boolean lightmapEnabled = false;

			if (GLContext.getCapabilities().OpenGL13) {
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

			if (gl13Supported && useGL13) {
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
			Iterator glError = this.layers.iterator();

			while (glError.hasNext()) {
				FancyDial$Layer layer = (FancyDial$Layer)glError.next();
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

			if (bindFB) {
				EXTFramebufferObject.glBindFramebufferEXT(36160, 0);
			}

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
			int glError1 = GL11.glGetError();

			if (glError1 != 0) {
				logger.severe("%s during %s update", new Object[] {GLU.gluErrorString(glError1), this.icon.getIconName()});
				this.ok = false;
			} else if (!inUpdateAll) {
				this.postRender();
			}

			return this.ok;
		}
	}

	private void postRender() {
		if (this.ok && !this.skipPostRender && this.scratchTexture >= 0) {
			TexturePackAPI.bindTexture(this.scratchTexture);
			this.scratchTextureBuffer.position(0);
			GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.scratchTextureBuffer);
			this.scratchTextureBuffer.position(0);
			TexturePackAPI.bindTexture(this.itemsTexture);
			GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, this.x0, this.y0, this.width, this.height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.scratchTextureBuffer);
		}
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
		if (this.frameBuffer >= 0) {
			EXTFramebufferObject.glDeleteFramebuffersEXT(this.frameBuffer);
			this.frameBuffer = -1;
		}

		if (this.scratchTexture >= 0) {
			TexturePackAPI.deleteTexture(this.scratchTexture);
			this.scratchTexture = -1;
		}

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

	private static boolean hasAnimation(Icon icon) {
		if (icon instanceof TextureAtlasSprite && subTexturesField != null) {
			try {
				List e = (List)subTexturesField.get(icon);
				return e != null && e.size() > 1;
			} catch (Throwable var2) {
				var2.printStackTrace();
			}
		}

		return true;
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

		if (gl13Supported && useGL13) {
			bits |= 536870912;
		}

		glAttributes = bits;
		Field field = null;

		try {
			Field[] e = TextureAtlasSprite.class.getDeclaredFields();
			int len$ = e.length;

			for (int i$ = 0; i$ < len$; ++i$) {
				Field f = e[i$];

				if (List.class.isAssignableFrom(f.getType())) {
					f.setAccessible(true);
					field = f;
					break;
				}
			}
		} catch (Throwable var6) {
			var6.printStackTrace();
		}

		subTexturesField = field;
		GL11.glNewList(drawList, GL11.GL_COMPILE);
		drawBox();
		GL11.glEndList();
	}
}
