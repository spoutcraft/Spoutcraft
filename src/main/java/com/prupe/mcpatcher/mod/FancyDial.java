package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.BlendMethod;
import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.TexturePackAPI;
import com.prupe.mcpatcher.mod.FancyDial$Layer;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Field;
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
	private static final String ITEMS_PNG = "/gui/items.png";
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
	private static final Map setupInfo = new WeakHashMap();
	private static final Map instances = new WeakHashMap();
	private static final HashSet keysDown = new HashSet();
	private final TextureStitched icon;
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
	private final List layers = new ArrayList();
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
					String var1 = var0.getIconName();
					Properties var2 = TexturePackAPI.getProperties("/misc/" + var1 + ".properties");

					if (var2 != null) {
						setupInfo.put(var0, var2);
					}
				}
			}
		}
	}

	public static boolean update(TextureStitched var0) {
		if (!initialized) {
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
		if (!setupInfo.isEmpty()) {
			ArrayList var0 = new ArrayList();
			var0.addAll(setupInfo.keySet());
			Iterator var1 = var0.iterator();
			while (var1.hasNext()) {
				TextureStitched var2 = (TextureStitched)var1.next();
				getInstance(var2);
			}
		}

		inUpdateAll = true;
		Iterator var3 = instances.values().iterator();
		while (var3.hasNext()) {
			FancyDial var4 = (FancyDial)var3.next();

			if (var4 != null && var4.needExtraUpdate) {
				var4.icon.updateAnimation();
			}
		}

		inUpdateAll = false;
	}

	static void postUpdateAll() {
		if (initialized) {
			Iterator var0 = instances.values().iterator();

			while (var0.hasNext()) {
				FancyDial var1 = (FancyDial)var0.next();

				if (var1 != null) {
					var1.postRender();
				}
			}
		}
	}

	static void refresh() {
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
		this.name = var2.getIconName();
		this.x0 = var2.getOriginX();
		this.y0 = var2.getOriginY();
		this.width = getIconWidth(var2);
		this.height = getIconHeight(var2);
		this.needExtraUpdate = !hasAnimation(var2);

		TexturePackAPI.bindTexture("/gui/items.png");
		this.itemsTexture = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
		int var4;

		if (useScratchTexture) {
			BufferedImage var5 = new BufferedImage(this.width, this.height, 2);
			this.scratchTexture = var1.allocateAndSetupTexture(var5);
			var4 = this.scratchTexture;
			this.scratchTextureBuffer = ByteBuffer.allocateDirect(4 * this.width * this.height);
		} else {
			this.scratchTexture = -1;
			this.scratchTextureBuffer = null;
			var4 = this.itemsTexture;
		}
		int var7 = 0;

		while (true) {
			FancyDial$Layer var6 = this.newLayer("/misc/" + this.name + ".properties", var3, "." + var7);
			if (var6 == null) {
				if (var7 > 0) {
					if (this.layers.size() < 2) {
						return;
					}

					this.outputFrames = MCPatcherUtils.getIntProperty(var3, "outputFrames", 0);
					this.frameBuffer = EXTFramebufferObject.glGenFramebuffersEXT();

					if (this.frameBuffer < 0) {
						return;
					}

					EXTFramebufferObject.glBindFramebufferEXT(36160, this.frameBuffer);
					EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36064, 3553, var4, 0);
					EXTFramebufferObject.glBindFramebufferEXT(36160, 0);
					var7 = GL11.glGetError();

					if (var7 != 0) {
						return;
					}

					this.ok = true;
					return;
				}
			} else {
				this.layers.add(var6);
				this.debug |= var6.debug;
			}
			++var7;
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
				this.lastAngle = Double.MAX_VALUE;
			}

			if (this.outputFrames > 0) {
				try {
					BufferedImage var2 = new BufferedImage(this.width, this.outputFrames * this.height, 2);
					ByteBuffer var3 = this.scratchTextureBuffer == null ? ByteBuffer.allocateDirect(4 * this.width * this.height) : this.scratchTextureBuffer;
					IntBuffer var4 = var3.asIntBuffer();
					int[] var5 = new int[this.width * this.height];
					EXTFramebufferObject.glBindFramebufferEXT(36160, this.frameBuffer);
					File var6 = MCPatcherUtils.getMinecraftPath(new String[] {"custom_" + this.name + ".png"});

					for (int var7 = 0; var7 < this.outputFrames; ++var7) {
						this.render((double)var7 * (360.0D / (double)this.outputFrames), false);

						if (this.scratchTexture < 0) {
							var3.position(0);
							GL11.glReadPixels(this.x0, this.y0, this.width, this.height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, var3);
						}

						var4.position(0);

						for (int var8 = 0; var8 < var5.length; ++var8) {
							var5[var8] = Integer.rotateRight(var4.get(var8), 8);
						}

						var2.setRGB(0, var7 * this.height, this.width, this.height, var5, 0, this.width);
					}

					ImageIO.write(var2, "png", var6);
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

	private boolean render(double var1, boolean var3) {
		if (var1 == this.lastAngle) {
			this.skipPostRender = true;
			return true;
		} else {
			this.skipPostRender = false;
			this.lastAngle = var1;
			GL11.glPushAttrib(glAttributes);

			if (this.scratchTexture >= 0) {
				GL11.glViewport(0, 0, this.width, this.height);
			} else {
				GL11.glViewport(this.x0, this.y0, this.width, this.height);
				GL11.glEnable(GL11.GL_SCISSOR_TEST);
				GL11.glScissor(this.x0, this.y0, this.width, this.height);
			}

			if (var3) {
				EXTFramebufferObject.glBindFramebufferEXT(36160, this.frameBuffer);
			}

			boolean var4 = false;

			if (GLContext.getCapabilities().OpenGL13) {
				GL13.glActiveTexture(GL13.GL_TEXTURE1);
				var4 = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);

				if (var4) {
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
			Iterator var5 = this.layers.iterator();

			while (var5.hasNext()) {
				FancyDial$Layer var6 = (FancyDial$Layer)var5.next();
				var6.blendMethod.applyBlending();
				GL11.glPushMatrix();
				TexturePackAPI.bindTexture(var6.textureName);
				float var7 = var6.offsetX;
				float var8 = var6.offsetY;
				float var9 = var6.scaleX;
				float var10 = var6.scaleY;

				if (var6.debug) {
					var7 += this.offsetXDelta;
					var8 += this.offsetYDelta;
					var9 += this.scaleXDelta;
					var10 += this.scaleYDelta;
				}

				GL11.glTranslatef(var7, var8, 0.0F);
				GL11.glScalef(var9, var10, 1.0F);
				float var11 = (float)(var1 * (double)var6.rotationMultiplier + (double)var6.rotationOffset);
				GL11.glRotatef(var11, 0.0F, 0.0F, 1.0F);
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

			if (var4) {
				GL13.glActiveTexture(GL13.GL_TEXTURE1);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL13.glActiveTexture(GL13.GL_TEXTURE0);
			}

			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			int var12 = GL11.glGetError();

			if (var12 != 0) {
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

	private static int getIconWidth(Icon var0) {
		return Math.round((float)var0.getSheetWidth() * (var0.getMaxU() - var0.getMinU()));
	}

	private static int getIconHeight(Icon var0) {
		return Math.round((float)var0.getSheetHeight() * (var0.getMaxV() - var0.getMinV()));
	}

	private static boolean hasAnimation(Icon var0) {
		if (var0 instanceof TextureStitched && subTexturesField != null) {
			try {
				List var1 = (List)subTexturesField.get(var0);
				return var1 != null && !var1.isEmpty();
			} catch (Throwable var2) {
				var2.printStackTrace();
			}
		}

		return true;
	}

	private static double getAngle(Icon var0) {
		return var0 instanceof TextureCompass ? ((TextureCompass)var0).currentAngle * 180.0D / Math.PI : (var0 instanceof TextureClock ? ((TextureClock)var0).field_94239_h * 360.0D : 0.0D);
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
		} else {
			boolean var5 = MCPatcherUtils.getBooleanProperty(var2, "filter", false);

			if (var5 && !var4.startsWith("%blur%")) {
				var4 = "%blur%" + var4;
			}

			if (!TexturePackAPI.hasResource(var4)) {
				return null;
			} else {
				float var6 = MCPatcherUtils.getFloatProperty(var2, "scaleX" + var3, 1.0F);
				float var7 = MCPatcherUtils.getFloatProperty(var2, "scaleY" + var3, 1.0F);
				float var8 = MCPatcherUtils.getFloatProperty(var2, "offsetX" + var3, 0.0F);
				float var9 = MCPatcherUtils.getFloatProperty(var2, "offsetY" + var3, 0.0F);
				float var10 = MCPatcherUtils.getFloatProperty(var2, "rotationSpeed" + var3, 0.0F);
				float var11 = MCPatcherUtils.getFloatProperty(var2, "rotationOffset" + var3, 0.0F);
				String var12 = MCPatcherUtils.getStringProperty(var2, "blend" + var3, "alpha");
				BlendMethod var13 = BlendMethod.parse(var12);

				if (var13 == null) {
					return null;
				} else {
					boolean var14 = MCPatcherUtils.getBooleanProperty(var2, "debug" + var3, false);
					return new FancyDial$Layer(this, var4, var6, var7, var8, var9, var10, var11, var13, var14);
				}
			}
		}
	}

	static {
		int var0 = 527702;

		if (gl13Supported && useGL13) {
			var0 |= 536870912;
		}

		glAttributes = var0;
		Field var1 = null;

		try {
			Field[] var2 = TextureStitched.class.getDeclaredFields();
			int var3 = var2.length;

			for (int var4 = 0; var4 < var3; ++var4) {
				Field var5 = var2[var4];

				if (List.class.isAssignableFrom(var5.getType())) {
					var5.setAccessible(true);
					var1 = var5;
					break;
				}
			}
		} catch (Throwable var6) {
			var6.printStackTrace();
		}

		subTexturesField = var1;
		GL11.glNewList(drawList, GL11.GL_COMPILE);
		drawBox();
		GL11.glEndList();
	}
}
