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

	public static BlendMethod parse(String text) {
		text = text.toLowerCase().trim();

		if (text.equals("alpha")) {
			return ALPHA;
		} else if (text.equals("add")) {
			return ADD;
		} else if (text.equals("subtract")) {
			return SUBTRACT;
		} else if (text.equals("multiply")) {
			return MULTIPLY;
		} else if (text.equals("dodge")) {
			return DODGE;
		} else if (text.equals("burn")) {
			return BURN;
		} else if (text.equals("screen")) {
			return SCREEN;
		} else if (!text.equals("overlay") && !text.equals("color")) {
			if (text.equals("replace")) {
				return REPLACE;
			} else {
				String[] tokens = text.split("\\s+");

				if (tokens.length >= 2) {
					try {
						int e = Integer.parseInt(tokens[0]);
						int dstBlend = Integer.parseInt(tokens[1]);
						return new BlendMethod("custom(" + e + "," + dstBlend + ")", e, dstBlend, true, true, false);
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

	private BlendMethod(String name, int srcBlend, int dstBlend, boolean blend, boolean fadeRGB, boolean fadeAlpha) {
		this.name = name;
		this.srcBlend = srcBlend;
		this.dstBlend = dstBlend;
		this.blend = blend;
		this.fadeRGB = fadeRGB;
		this.fadeAlpha = fadeAlpha;
	}

	public String toString() {
		return this.name;
	}

	public void applyFade(float fade) {
		if (this.fadeRGB && this.fadeAlpha) {
			GL11.glColor4f(fade, fade, fade, fade);
		} else if (this.fadeRGB) {
			GL11.glColor4f(fade, fade, fade, 1.0F);
		} else if (this.fadeAlpha) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, fade);
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

	public boolean canFade() {
		return this.blend && (this.fadeAlpha || this.fadeRGB);
	}
}
