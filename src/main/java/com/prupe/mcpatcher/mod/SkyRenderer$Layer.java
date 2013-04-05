package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.BlendMethod;
import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.TexturePackAPI;
import java.util.Properties;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;

class SkyRenderer$Layer {
	private static final int SECS_PER_DAY = 86400;
	private static final int TICKS_PER_DAY = 24000;
	private static final double TOD_OFFSET = -0.25D;
	private static final double SKY_DISTANCE = 100.0D;
	private String prefix;
	private Properties properties;
	private String texture;
	private boolean fade;
	private boolean rotate;
	private float[] axis;
	private float speed;
	private BlendMethod blendMethod;
	private double a;
	private double b;
	private double c;
	boolean valid;
	float brightness;

	static SkyRenderer$Layer create(String var0) {
		Properties var1 = TexturePackAPI.getProperties(var0 + ".properties");
		return var1 == null ? null : new SkyRenderer$Layer(var0, var1);
	}

	SkyRenderer$Layer(String var1, Properties var2) {
		this.prefix = var1;
		this.properties = var2;
		this.valid = true;
		this.valid = this.readTexture() && this.readRotation() & this.readBlendingMethod() && this.readFadeTimers();
	}

	private boolean readTexture() {
		this.texture = this.properties.getProperty("source", this.prefix + ".png");
		return TexturePackAPI.hasResource(this.texture) ? true : this.addError("source texture %s not found", new Object[] {this.texture});
	}

	private boolean readRotation() {
		this.rotate = MCPatcherUtils.getBooleanProperty(this.properties, "rotate", true);

		if (this.rotate) {
			try {
				this.speed = Float.parseFloat(this.properties.getProperty("speed", "1.0"));
			} catch (NumberFormatException var8) {
				return this.addError("invalid rotation speed", new Object[0]);
			}

			String var1 = this.properties.getProperty("axis", "0.0 0.0 1.0").trim().toLowerCase();
			String[] var2 = var1.split("\\s+");

			if (var2.length != 3) {
				return this.addError("invalid rotate value %s", new Object[] {var1});
			}

			float var3;
			float var4;
			float var5;

			try {
				var3 = Float.parseFloat(var2[0]);
				var4 = Float.parseFloat(var2[1]);
				var5 = Float.parseFloat(var2[2]);
			} catch (NumberFormatException var7) {
				return this.addError("invalid rotation axis", new Object[0]);
			}

			if (var3 * var3 + var4 * var4 + var5 * var5 == 0.0F) {
				return this.addError("rotation axis cannot be 0", new Object[0]);
			}

			this.axis = new float[] {var5, var4, -var3};
		}

		return true;
	}

	private boolean readBlendingMethod() {
		String var1 = this.properties.getProperty("blend", "add");
		this.blendMethod = BlendMethod.parse(var1);
		return this.blendMethod == null ? this.addError("unknown blend method %s", new Object[] {var1}): true;
	}

	private boolean readFadeTimers() {
		this.fade = Boolean.parseBoolean(this.properties.getProperty("fade", "true"));

		if (!this.fade) {
			return true;
		} else {
			int var1 = this.parseTime(this.properties, "startFadeIn");
			int var2 = this.parseTime(this.properties, "endFadeIn");
			int var3 = this.parseTime(this.properties, "endFadeOut");

			if (!this.valid) {
				return false;
			} else {
				while (var2 <= var1) {
					var2 += 86400;
				}

				while (var3 <= var2) {
					var3 += 86400;
				}

				if (var3 - var1 >= 86400) {
					return this.addError("fade times must fall within a 24 hour period", new Object[0]);
				} else {
					int var4 = var1 + var3 - var2;
					double var5 = normalize((double)var1, 86400, -0.25D);
					double var7 = normalize((double)var2, 86400, -0.25D);
					double var9 = normalize((double)var4, 86400, -0.25D);
					double var11 = normalize((double)var3, 86400, -0.25D);
					double var13 = Math.cos(var5) * Math.sin(var7) + Math.cos(var11) * Math.sin(var5) + Math.cos(var7) * Math.sin(var11) - Math.cos(var5) * Math.sin(var11) - Math.cos(var7) * Math.sin(var5) - Math.cos(var11) * Math.sin(var7);

					if (var13 == 0.0D) {
						return this.addError("determinant is 0", new Object[0]);
					} else {
						this.a = (Math.sin(var11) - Math.sin(var5)) / var13;
						this.b = (Math.cos(var5) - Math.cos(var11)) / var13;
						this.c = (Math.cos(var11) * Math.sin(var5) - Math.cos(var5) * Math.sin(var11)) / var13;
						return true;
					}
				}
			}
		}
	}

	private boolean addError(String var1, Object ... var2) {
		this.valid = false;
		return false;
	}

	private int parseTime(Properties var1, String var2) {
		String var3 = var1.getProperty(var2, "").trim();

		if ("".equals(var3)) {
			this.addError("missing value for %s", new Object[] {var2});
			return -1;
		} else {
			String[] var4 = var3.split(":");

			if (var4.length >= 2) {
				try {
					int var5 = Integer.parseInt(var4[0].trim());
					int var6 = Integer.parseInt(var4[1].trim());
					int var7;

					if (var4.length >= 3) {
						var7 = Integer.parseInt(var4[2].trim());
					} else {
						var7 = 0;
					}

					return (3600 * var5 + 60 * var6 + var7) % 86400;
				} catch (NumberFormatException var8) {
					;
				}
			}

			this.addError("invalid %s time %s", new Object[] {var2, var3});
			return -1;
		}
	}

	private static double normalize(double var0, int var2, double var3) {
		return (Math.PI * 2D) * (var0 / (double)var2 + var3);
	}

	private double f(double var1) {
		return this.a * Math.cos(var1) + this.b * Math.sin(var1) + this.c;
	}

	boolean prepare() {
		this.brightness = SkyRenderer.access$500();

		if (this.fade) {
			double var1 = normalize(SkyRenderer.access$600(), 24000, 0.0D);
			this.brightness *= (float)this.f(var1);
		}

		if (this.brightness <= 0.0F) {
			return false;
		} else {
			if (this.brightness > 1.0F) {
				this.brightness = 1.0F;
			}

			return true;
		}
	}

	boolean render(Tessellator var1) {
		TexturePackAPI.bindTexture(this.texture);
		this.setBlendingMethod(this.brightness);
		GL11.glPushMatrix();

		if (this.rotate) {
			GL11.glRotatef(SkyRenderer.access$700() * 360.0F * this.speed, this.axis[0], this.axis[1], this.axis[2]);
		}

		GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
		drawTile(var1, 4);
		GL11.glPushMatrix();
		GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
		drawTile(var1, 1);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
		drawTile(var1, 0);
		GL11.glPopMatrix();
		GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
		drawTile(var1, 5);
		GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
		drawTile(var1, 2);
		GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
		drawTile(var1, 3);
		GL11.glPopMatrix();
		return true;
	}

	private static void drawTile(Tessellator var0, int var1) {
		double var2 = (double)(var1 % 3) / 3.0D;
		double var4 = (double)(var1 / 3) / 2.0D;
		var0.startDrawingQuads();
		var0.addVertexWithUV(-100.0D, -100.0D, -100.0D, var2, var4);
		var0.addVertexWithUV(-100.0D, -100.0D, 100.0D, var2, var4 + 0.5D);
		var0.addVertexWithUV(100.0D, -100.0D, 100.0D, var2 + 0.3333333333333333D, var4 + 0.5D);
		var0.addVertexWithUV(100.0D, -100.0D, -100.0D, var2 + 0.3333333333333333D, var4);
		var0.draw();
	}

	void setBlendingMethod(float var1) {
		this.blendMethod.applyFade(var1);
		this.blendMethod.applyAlphaTest();
		this.blendMethod.applyBlending();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	static void clearBlendingMethod() {
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, SkyRenderer.access$500());
	}

	static String access$300(SkyRenderer$Layer var0) {
		return var0.texture;
	}
}
