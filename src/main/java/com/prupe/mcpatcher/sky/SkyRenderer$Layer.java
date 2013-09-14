package com.prupe.mcpatcher.sky;

import com.prupe.mcpatcher.BlendMethod;
import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.TexturePackAPI;
import java.util.Properties;
import net.minecraft.src.ResourceLocation;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;

class SkyRenderer$Layer {
	private static final int SECS_PER_DAY = 86400;
	private static final int TICKS_PER_DAY = 24000;
	private static final double TOD_OFFSET = -0.25D;
	private static final double SKY_DISTANCE = 100.0D;
	private final ResourceLocation propertiesName;
	private final Properties properties;
	private ResourceLocation texture;
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

	static SkyRenderer$Layer create(ResourceLocation resource) {
		Properties properties = TexturePackAPI.getProperties(resource);
		return properties == null ? null : new SkyRenderer$Layer(resource, properties);
	}

	SkyRenderer$Layer(ResourceLocation propertiesName, Properties properties) {
		this.propertiesName = propertiesName;
		this.properties = properties;
		this.valid = true;
		this.valid = this.readTexture() && this.readRotation() & this.readBlendingMethod() && this.readFadeTimers();
	}

	private boolean readTexture() {
		this.texture = TexturePackAPI.parseResourceLocation(this.propertiesName, this.properties.getProperty("source", this.propertiesName.func_110623_a().replaceFirst("\\.properties$", ".png")));
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

			String value = this.properties.getProperty("axis", "0.0 0.0 1.0").trim().toLowerCase();
			String[] tokens = value.split("\\s+");

			if (tokens.length != 3) {
				return this.addError("invalid rotate value %s", new Object[] {value});
			}

			float x;
			float y;
			float z;

			try {
				x = Float.parseFloat(tokens[0]);
				y = Float.parseFloat(tokens[1]);
				z = Float.parseFloat(tokens[2]);
			} catch (NumberFormatException var7) {
				return this.addError("invalid rotation axis", new Object[0]);
			}

			if (x * x + y * y + z * z == 0.0F) {
				return this.addError("rotation axis cannot be 0", new Object[0]);
			}

			this.axis = new float[] {z, y, -x};
		}

		return true;
	}

	private boolean readBlendingMethod() {
		String value = this.properties.getProperty("blend", "add");
		this.blendMethod = BlendMethod.parse(value);
		return this.blendMethod == null ? this.addError("unknown blend method %s", new Object[] {value}): true;
	}

	private boolean readFadeTimers() {
		this.fade = Boolean.parseBoolean(this.properties.getProperty("fade", "true"));

		if (!this.fade) {
			return true;
		} else {
			int startFadeIn = this.parseTime(this.properties, "startFadeIn");
			int endFadeIn = this.parseTime(this.properties, "endFadeIn");
			int endFadeOut = this.parseTime(this.properties, "endFadeOut");

			if (!this.valid) {
				return false;
			} else {
				while (endFadeIn <= startFadeIn) {
					endFadeIn += 86400;
				}

				while (endFadeOut <= endFadeIn) {
					endFadeOut += 86400;
				}

				if (endFadeOut - startFadeIn >= 86400) {
					return this.addError("fade times must fall within a 24 hour period", new Object[0]);
				} else {
					int startFadeOut = startFadeIn + endFadeOut - endFadeIn;
					double s0 = normalize((double)startFadeIn, 86400, -0.25D);
					double s1 = normalize((double)endFadeIn, 86400, -0.25D);
					double e0 = normalize((double)startFadeOut, 86400, -0.25D);
					double e1 = normalize((double)endFadeOut, 86400, -0.25D);
					double det = Math.cos(s0) * Math.sin(s1) + Math.cos(e1) * Math.sin(s0) + Math.cos(s1) * Math.sin(e1) - Math.cos(s0) * Math.sin(e1) - Math.cos(s1) * Math.sin(s0) - Math.cos(e1) * Math.sin(s1);

					if (det == 0.0D) {
						return this.addError("determinant is 0", new Object[0]);
					} else {
						this.a = (Math.sin(e1) - Math.sin(s0)) / det;
						this.b = (Math.cos(s0) - Math.cos(e1)) / det;
						this.c = (Math.cos(e1) * Math.sin(s0) - Math.cos(s0) * Math.sin(e1)) / det;
						SkyRenderer.access$400().finer("%s: y = %f cos x + %f sin x + %f", new Object[] {this.propertiesName, Double.valueOf(this.a), Double.valueOf(this.b), Double.valueOf(this.c)});
						SkyRenderer.access$400().finer("  at %f: %f", new Object[] {Double.valueOf(s0), Double.valueOf(this.f(s0))});
						SkyRenderer.access$400().finer("  at %f: %f", new Object[] {Double.valueOf(s1), Double.valueOf(this.f(s1))});
						SkyRenderer.access$400().finer("  at %f: %f", new Object[] {Double.valueOf(e0), Double.valueOf(this.f(e0))});
						SkyRenderer.access$400().finer("  at %f: %f", new Object[] {Double.valueOf(e1), Double.valueOf(this.f(e1))});
						return true;
					}
				}
			}
		}
	}

	private boolean addError(String format, Object ... params) {
		SkyRenderer.access$400().error("" + this.propertiesName + ": " + format, params);
		this.valid = false;
		return false;
	}

	private int parseTime(Properties properties, String key) {
		String s = properties.getProperty(key, "").trim();

		if ("".equals(s)) {
			this.addError("missing value for %s", new Object[] {key});
			return -1;
		} else {
			String[] t = s.split(":");

			if (t.length >= 2) {
				try {
					int e = Integer.parseInt(t[0].trim());
					int mm = Integer.parseInt(t[1].trim());
					int ss;

					if (t.length >= 3) {
						ss = Integer.parseInt(t[2].trim());
					} else {
						ss = 0;
					}

					return (3600 * e + 60 * mm + ss) % 86400;
				} catch (NumberFormatException var8) {
					;
				}
			}

			this.addError("invalid %s time %s", new Object[] {key, s});
			return -1;
		}
	}

	private static double normalize(double time, int period, double offset) {
		return (Math.PI * 2D) * (time / (double)period + offset);
	}

	private double f(double x) {
		return this.a * Math.cos(x) + this.b * Math.sin(x) + this.c;
	}

	boolean prepare() {
		this.brightness = SkyRenderer.access$500();

		if (this.fade) {
			double x = normalize(SkyRenderer.access$600(), 24000, 0.0D);
			this.brightness *= (float)this.f(x);
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

	boolean render(Tessellator tessellator) {
		TexturePackAPI.bindTexture(this.texture);
		this.setBlendingMethod(this.brightness);
		GL11.glPushMatrix();

		if (this.rotate) {
			GL11.glRotatef(SkyRenderer.access$700() * 360.0F * this.speed, this.axis[0], this.axis[1], this.axis[2]);
		}

		GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
		drawTile(tessellator, 4);
		GL11.glPushMatrix();
		GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
		drawTile(tessellator, 1);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
		drawTile(tessellator, 0);
		GL11.glPopMatrix();
		GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
		drawTile(tessellator, 5);
		GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
		drawTile(tessellator, 2);
		GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
		drawTile(tessellator, 3);
		GL11.glPopMatrix();
		return true;
	}

	private static void drawTile(Tessellator tessellator, int tile) {
		double tileX = (double)(tile % 3) / 3.0D;
		double tileY = (double)(tile / 3) / 2.0D;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(-100.0D, -100.0D, -100.0D, tileX, tileY);
		tessellator.addVertexWithUV(-100.0D, -100.0D, 100.0D, tileX, tileY + 0.5D);
		tessellator.addVertexWithUV(100.0D, -100.0D, 100.0D, tileX + 0.3333333333333333D, tileY + 0.5D);
		tessellator.addVertexWithUV(100.0D, -100.0D, -100.0D, tileX + 0.3333333333333333D, tileY);
		tessellator.draw();
	}

	void setBlendingMethod(float brightness) {
		this.blendMethod.applyFade(brightness);
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

	static ResourceLocation access$300(SkyRenderer$Layer x0) {
		return x0.texture;
	}
}
