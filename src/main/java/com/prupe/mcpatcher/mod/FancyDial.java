package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.BlendMethod;
import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.TexturePackAPI;
import com.prupe.mcpatcher.mod.FancyDial$Layer;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.WeakHashMap;
import javax.imageio.ImageIO;
import net.minecraft.src.Icon;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.TextureClock;
import net.minecraft.src.TextureCompass;
import net.minecraft.src.TextureStitched;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;

public class FancyDial {
	private static final MCLogger logger = MCLogger.getLogger("Custom Animations", "Animation");
	private static final String ITEMS_PNG = "/gui/items.png";
	private static final boolean fboSupported = GLContext.getCapabilities().GL_EXT_framebuffer_object;
	private static final boolean gl13Supported = GLContext.getCapabilities().OpenGL13;
	private static final boolean enableCompass = Config.getBoolean("Extended HD", "fancyCompass", true);
	private static final boolean enableClock = Config.getBoolean("Extended HD", "fancyClock", true);
	private static final boolean useGL13 = Config.getBoolean("Extended HD", "useGL13", true);
	private static boolean initialized;
	private static final int drawList = GL11.glGenLists(1);
	private static final Map setupInfo = new WeakHashMap();
	private static final Map instances = new WeakHashMap();
	private static final HashSet keysDown = new HashSet();
	private final TextureStitched icon;
	private final String name;
	private boolean needExtraUpdate;
	private boolean ok;
	private int outputFrames;
	private final List layers = new ArrayList();
	private int frameBuffer = -1;
	private boolean debug;
	private static final float STEP = 0.01F;
	private float scaleXDelta;
	private float scaleYDelta;
	private float offsetXDelta;
	private float offsetYDelta;

	public static void setup(TextureStitched var0) {
		if (fboSupported) {
			if (!(var0 instanceof TextureCompass) || enableCompass) {
				if (!(var0 instanceof TextureClock) || enableClock) {
					String var1 = var0.func_94215_i();
					Properties var2 = TexturePackAPI.getProperties("/misc/" + var1 + ".properties");

					if (var2 != null) {
						logger.fine("found custom %s", new Object[] {var1});
						setupInfo.put(var0, var2);
					}
				}
			}
		}
	}

	public static boolean update(TextureStitched var0) {
		if (!initialized) {
			logger.finer("deferring %s update until initialization finishes", new Object[] {var0.func_94215_i()});
			return false;
		} else {
			FancyDial var1 = (FancyDial)instances.get(var0);

			if (var1 == null) {
				var1 = getInstance(var0);

				if (var1 == null) {
					return false;
				}
			}

			return var1.render();
		}
	}

	static void updateAll() {
		if (!initialized) {
			logger.finer("deferring %s update until initialization finishes", new Object[] {FancyDial.class.getSimpleName()});
		} else {
			if (!setupInfo.isEmpty()) {
				ArrayList var0 = new ArrayList();
				var0.addAll(setupInfo.keySet());
				Iterator var1 = var0.iterator();

				while (var1.hasNext()) {
					TextureStitched var2 = (TextureStitched)var1.next();
					getInstance(var2);
				}
			}

			Iterator var3 = instances.values().iterator();

			while (var3.hasNext()) {
				FancyDial var4 = (FancyDial)var3.next();

				if (var4 != null && var4.needExtraUpdate) {
					var4.icon.func_94219_l();
				}
			}
		}
	}

	static void refresh() {
		logger.finer("FancyDial.refresh", new Object[0]);
		Iterator var0 = instances.values().iterator();

		while (var0.hasNext()) {
			FancyDial var1 = (FancyDial)var0.next();

			if (var1 != null) {
				var1.finish();
			}
		}

		instances.clear();
		initialized = true;
	}

	private static FancyDial getInstance(TextureStitched var0) {
		Properties var1 = (Properties)setupInfo.get(var0);

		if (var1 == null) {
			return null;
		} else {
			setupInfo.remove(var0);
			RenderEngine var2 = MCPatcherUtils.getMinecraft().renderEngine;

			try {
				FancyDial var3 = new FancyDial(var2, var0, var1);

				if (var3.ok) {
					instances.put(var0, var3);
					return var3;
				}

				var3.finish();
			} catch (Throwable var4) {
				var4.printStackTrace();
			}

			return null;
		}
	}

	private FancyDial(RenderEngine var1, TextureStitched var2, Properties var3) {
		this.icon = var2;
		this.name = var2.func_94215_i();
		String var4 = "/textures/items/" + this.name + ".png";
		BufferedImage var5 = TexturePackAPI.getImage(var4);

		if (var5 == null) {
			logger.error("could not get %s", new Object[] {var4});
		} else {
			this.needExtraUpdate = var5.getHeight() % var5.getWidth() != 0 || var5.getHeight() / var5.getWidth() <= 1;

			if (this.needExtraUpdate) {
				logger.fine("%s needs direct .update() call", new Object[] {this.name});
			}

			TexturePackAPI.bindTexture("/gui/items.png");
			int var6 = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);

			if (var6 < 0) {
				logger.severe("could not get items texture", new Object[0]);
			} else {
				logger.fine("setting up %s", new Object[] {this});
				int var7 = 0;

				while (true) {
					FancyDial$Layer var8 = this.newLayer("/misc/" + this.name + ".properties", var3, "." + var7);

					if (var8 == null) {
						if (var7 > 0) {
							if (this.layers.size() < 2) {
								logger.error("custom %s needs at least two layers defined", new Object[] {this.name});
								return;
							}

							this.outputFrames = MCPatcherUtils.getIntProperty(var3, "outputFrames", 0);
							this.frameBuffer = EXTFramebufferObject.glGenFramebuffersEXT();

							if (this.frameBuffer < 0) {
								logger.severe("could not get framebuffer object", new Object[0]);
								return;
							}

							EXTFramebufferObject.glBindFramebufferEXT(36160, this.frameBuffer);
							EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36064, 3553, var6, 0);
							EXTFramebufferObject.glBindFramebufferEXT(36160, 0);
							var7 = GL11.glGetError();

							if (var7 != 0) {
								logger.severe("%s during %s setup", new Object[] {GLU.gluErrorString(var7), this.name});
								return;
							}

							this.ok = true;
							return;
						}
					} else {
						this.layers.add(var8);
						this.debug |= var8.debug;
						logger.fine("  new %s", new Object[] {var8});
					}

					++var7;
				}
			}
		}
	}

	private boolean render() {
		if (!this.ok) {
			return false;
		} else {
			boolean var1 = true;

			if (!this.debug) {
				var1 = false;
			} else if (tap(80)) {
				this.scaleYDelta -= 0.01F;
			} else if (tap(72)) {
				this.scaleYDelta += 0.01F;
			} else if (tap(75)) {
				this.scaleXDelta -= 0.01F;
			} else if (tap(77)) {
				this.scaleXDelta += 0.01F;
			} else if (tap(208)) {
				this.offsetYDelta += 0.01F;
			} else if (tap(200)) {
				this.offsetYDelta -= 0.01F;
			} else if (tap(203)) {
				this.offsetXDelta -= 0.01F;
			} else if (tap(205)) {
				this.offsetXDelta += 0.01F;
			} else if (tap(55)) {
				this.scaleXDelta = this.scaleYDelta = this.offsetXDelta = this.offsetYDelta = 0.0F;
			} else {
				var1 = false;
			}

			if (var1) {
				logger.info("", new Object[0]);
				logger.info("scaleX  %+f", new Object[] {Float.valueOf(this.scaleXDelta)});
				logger.info("scaleY  %+f", new Object[] {Float.valueOf(this.scaleYDelta)});
				logger.info("offsetX %+f", new Object[] {Float.valueOf(this.offsetXDelta)});
				logger.info("offsetY %+f", new Object[] {Float.valueOf(this.offsetYDelta)});
			}

			if (this.outputFrames > 0) {
				try {
					int var2 = getIconWidth(this.icon);
					int var3 = getIconHeight(this.icon);
					BufferedImage var4 = new BufferedImage(var2, this.outputFrames * var3, 2);
					ByteBuffer var5 = ByteBuffer.allocateDirect(4 * var2 * var3);
					IntBuffer var6 = var5.asIntBuffer();
					int[] var7 = new int[var2 * var3];
					EXTFramebufferObject.glBindFramebufferEXT(36160, this.frameBuffer);
					File var8 = MCPatcherUtils.getMinecraftPath(new String[] {"custom_" + this.name + ".png"});
					logger.info("generating %d %s frames", new Object[] {Integer.valueOf(this.outputFrames), this.name});

					for (int var9 = 0; var9 < this.outputFrames; ++var9) {
						this.render((double)var9 * (360.0D / (double)this.outputFrames), false);
						var5.position(0);
						GL11.glReadPixels(this.icon.func_94211_a(), this.icon.func_94216_b(), var2, var3, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, var5);
						var6.position(0);

						for (int var10 = 0; var10 < var7.length; ++var10) {
							var7[var10] = Integer.rotateRight(var6.get(var10), 8);
						}

						var4.setRGB(0, var9 * var3, var2, var3, var7, 0, var2);
					}

					ImageIO.write(var4, "png", var8);
					logger.info("wrote %dx%d %s", new Object[] {Integer.valueOf(var4.getWidth()), Integer.valueOf(var4.getHeight()), var8.getPath()});
				} catch (Throwable var14) {
					var14.printStackTrace();
				} finally {
					EXTFramebufferObject.glBindFramebufferEXT(36160, 0);
				}

				this.outputFrames = 0;
			}

			return this.render(getAngle(this.icon), true);
		}
	}

	private boolean render(double var1, boolean var3) {
		int var4 = 527702;

		if (gl13Supported && useGL13) {
			var4 |= 536870912;
		}

		GL11.glPushAttrib(var4);
		int var5 = this.icon.func_94211_a();
		int var6 = this.icon.func_94216_b();
		int var7 = getIconWidth(this.icon);
		int var8 = getIconHeight(this.icon);
		GL11.glViewport(var5, var6, var7, var8);
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		GL11.glScissor(var5, var6, var7, var8);

		if (var3) {
			EXTFramebufferObject.glBindFramebufferEXT(36160, this.frameBuffer);
		}

		boolean var9 = false;

		if (GLContext.getCapabilities().OpenGL13) {
			GL13.glActiveTexture(GL13.GL_TEXTURE1);
			var9 = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);

			if (var9) {
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
		Iterator var10 = this.layers.iterator();

		while (var10.hasNext()) {
			FancyDial$Layer var11 = (FancyDial$Layer)var10.next();
			var11.blendMethod.applyBlending();
			GL11.glPushMatrix();
			TexturePackAPI.bindTexture(var11.textureName);
			float var12 = var11.offsetX;
			float var13 = var11.offsetY;
			float var14 = var11.scaleX;
			float var15 = var11.scaleY;

			if (var11.debug) {
				var12 += this.offsetXDelta;
				var13 += this.offsetYDelta;
				var14 += this.scaleXDelta;
				var15 += this.scaleYDelta;
			}

			GL11.glTranslatef(var12, var13, 0.0F);
			GL11.glScalef(var14, var15, 1.0F);
			float var16 = (float)(var1 * (double)var11.rotationMultiplier + (double)var11.rotationOffset);
			GL11.glRotatef(var16, 0.0F, 0.0F, 1.0F);
			GL11.glCallList(drawList);
			GL11.glPopMatrix();
		}

		if (var3) {
			EXTFramebufferObject.glBindFramebufferEXT(36160, 0);
		}

		GL11.glPopAttrib();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPopMatrix();

		if (var9) {
			GL13.glActiveTexture(GL13.GL_TEXTURE1);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
		}

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		int var17 = GL11.glGetError();

		if (var17 != 0) {
			logger.severe("%s during %s update", new Object[] {GLU.gluErrorString(var17), this.icon.func_94215_i()});
			this.ok = false;
		}

		return this.ok;
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

		this.layers.clear();
		this.ok = false;
	}

	public String toString() {
		return String.format("FancyDial{%s, %dx%d @ %d,%d}", new Object[] {this.name, Integer.valueOf(getIconWidth(this.icon)), Integer.valueOf(getIconHeight(this.icon)), Integer.valueOf(this.icon.func_94211_a()), Integer.valueOf(this.icon.func_94216_b())});
	}

	protected void finalize() throws Throwable {
		this.finish();
		super.finalize();
	}

	private static int getIconWidth(Icon var0) {
		return Math.round((float)var0.func_94213_j() * (var0.func_94212_f() - var0.func_94209_e()));
	}

	private static int getIconHeight(Icon var0) {
		return Math.round((float)var0.func_94208_k() * (var0.func_94210_h() - var0.func_94206_g()));
	}

	private static double getAngle(Icon var0) {
		return var0 instanceof TextureCompass ? ((TextureCompass)var0).field_94244_i * 180.0D / Math.PI : (var0 instanceof TextureClock ? ((TextureClock)var0).field_94239_h * 360.0D : 0.0D);
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

	FancyDial$Layer newLayer(String var1, Properties var2, String var3) {
		String var4 = MCPatcherUtils.getStringProperty(var2, "source" + var3, "");

		if (var4.equals("")) {
			return null;
		} else if (!TexturePackAPI.hasResource(var4)) {
			logger.error("%s: could not read %s", new Object[] {var1, var4});
			return null;
		} else {
			float var5 = MCPatcherUtils.getFloatProperty(var2, "scaleX" + var3, 1.0F);
			float var6 = MCPatcherUtils.getFloatProperty(var2, "scaleY" + var3, 1.0F);
			float var7 = MCPatcherUtils.getFloatProperty(var2, "offsetX" + var3, 0.0F);
			float var8 = MCPatcherUtils.getFloatProperty(var2, "offsetY" + var3, 0.0F);
			float var9 = MCPatcherUtils.getFloatProperty(var2, "rotationSpeed" + var3, 0.0F);
			float var10 = MCPatcherUtils.getFloatProperty(var2, "rotationOffset" + var3, 0.0F);
			String var11 = MCPatcherUtils.getStringProperty(var2, "blend" + var3, "alpha");
			BlendMethod var12 = BlendMethod.parse(var11);

			if (var12 == null) {
				logger.error("%s: unknown blend method %s", new Object[] {var11});
				return null;
			} else {
				boolean var13 = MCPatcherUtils.getBooleanProperty(var2, "debug" + var3, false);
				return new FancyDial$Layer(this, var4, var5, var6, var7, var8, var9, var10, var12, var13);
			}
		}
	}

	static {
		logger.config("fbo: supported=%s", new Object[] {Boolean.valueOf(fboSupported)});
		logger.config("GL13: supported=%s, enabled=%s", new Object[] {Boolean.valueOf(gl13Supported), Boolean.valueOf(useGL13)});
		GL11.glNewList(drawList, GL11.GL_COMPILE);
		drawBox();
		GL11.glEndList();
	}
}
