package com.prupe.mcpatcher;

import org.lwjgl.opengl.GL11;

public class BlendMethod {
	public static final BlendMethod ALPHA = new BlendMethod("alpha", 770, 771, true, false, true);
	public static final BlendMethod ADD = new BlendMethod("add", 770, 1, true, false, true);
	public static final BlendMethod SUBTRACT = new BlendMethod("subtract", 775, 0, true, true, false);
	public static final BlendMethod MULTIPLY = new BlendMethod("multiply", 774, 771, true, true, true);
	public static final BlendMethod DODGE = new BlendMethod("dodge", 1, 1, true, true, false);
	public static final BlendMethod BURN = new BlendMethod("burn", 0, 769, true, true, false);
	public static final BlendMethod SCREEN = new BlendMethod("screen", 1, 769, true, true, false);
	public static final BlendMethod OVERLAY = new BlendMethod("overlay", 774, 768, true, true, false);
	public static final BlendMethod REPLACE = new BlendMethod("replace", 0, 0, false, false, true);
	private final int srcBlend;
	private final int dstBlend;
	private final String name;
	private final boolean blend;
	private final boolean fadeRGB;
	private final boolean fadeAlpha;

	public static BlendMethod parse(String var0) {
		var0 = var0.toLowerCase().trim();

		if (var0.equals("alpha")) {
			return ALPHA;
		} else if (var0.equals("add")) {
			return ADD;
		} else if (var0.equals("subtract")) {
			return SUBTRACT;
		} else if (var0.equals("multiply")) {
			return MULTIPLY;
		} else if (var0.equals("dodge")) {
			return DODGE;
		} else if (var0.equals("burn")) {
			return BURN;
		} else if (var0.equals("screen")) {
			return SCREEN;
		} else if (!var0.equals("overlay") && !var0.equals("color")) {
			if (var0.equals("replace")) {
				return REPLACE;
			} else {
				String[] var1 = var0.split("\\s+");

				if (var1.length >= 2) {
					try {
						int var2 = Integer.parseInt(var1[0]);
						int var3 = Integer.parseInt(var1[1]);
						return new BlendMethod("custom(" + var2 + "," + var3 + ")", var2, var3, true, true, false);
					} catch (NumberFormatException var4) {
						;
					}
				}

				return null;
			}
		} else {
			return OVERLAY;
		}
	}

	private BlendMethod(String var1, int var2, int var3, boolean var4, boolean var5, boolean var6) {
		this.name = var1;
		this.srcBlend = var2;
		this.dstBlend = var3;
		this.blend = var4;
		this.fadeRGB = var5;
		this.fadeAlpha = var6;
	}

	public String toString() {
		return this.name;
	}

	public void applyFade(float var1) {
		if (this.fadeRGB && this.fadeAlpha) {
			GL11.glColor4f(var1, var1, var1, var1);
		} else if (this.fadeRGB) {
			GL11.glColor4f(var1, var1, var1, 1.0F);
		} else if (this.fadeAlpha) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, var1);
		}
	}

	public void applyAlphaTest() {
		if (this.blend) {
			GL11.glDisable(GL11.GL_ALPHA_TEST);
		} else {
			GL11.glEnable(GL11.GL_ALPHA_TEST);
		}
	}

	public void applyBlending() {
		if (this.blend) {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(this.srcBlend, this.dstBlend);
		} else {
			GL11.glDisable(GL11.GL_BLEND);
		}
	}

	public boolean isColorBased() {
		return this.fadeRGB;
	}
}
