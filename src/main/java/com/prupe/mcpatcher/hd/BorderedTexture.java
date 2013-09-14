package com.prupe.mcpatcher.hd;

import com.prupe.mcpatcher.MCLogger;
import net.minecraft.src.TextureAtlasSprite;

public class BorderedTexture extends TextureAtlasSprite {
	private static final MCLogger logger = MCLogger.getLogger("Mipmap");
	private float minU;
	private float maxU;
	private float minV;
	private float maxV;
	private float scaledWidth;
	private float scaledHeight;
	private int tilesheetWidth;
	private int tilesheetHeight;
	private int x0;
	private int y0;
	private String tilesheet;
	int border;

	public static TextureAtlasSprite create(String tilesheet, String name) {
		return (TextureAtlasSprite)(AAHelper.useAAForTexture(tilesheet) ? new BorderedTexture(tilesheet, name) : new TextureAtlasSprite(name));
	}

	private BorderedTexture(String tilesheet, String name) {
		super(name);
		this.tilesheet = tilesheet;
	}

	public void func_110971_a(int tilesheetWidth, int tilesheetHeight, int x0, int y0, boolean flipped) {
		super.func_110971_a(tilesheetWidth, tilesheetHeight, x0, y0, flipped);
		this.tilesheetWidth = tilesheetWidth;
		this.tilesheetHeight = tilesheetHeight;
		this.x0 = x0;
		this.y0 = y0;
		this.setBorderWidth(this.border);
	}

	/**
	 * Returns the minimum U coordinate to use when rendering with this icon.
	 */
	public float getMinU() {
		return this.minU;
	}

	/**
	 * Returns the maximum U coordinate to use when rendering with this icon.
	 */
	public float getMaxU() {
		return this.maxU;
	}

	/**
	 * Gets a U coordinate on the icon. 0 returns uMin and 16 returns uMax. Other arguments return in-between values.
	 */
	public float getInterpolatedU(double u) {
		return this.border > 0 ? this.minU + (float)u * this.scaledWidth : super.getInterpolatedU(u);
	}

	/**
	 * Returns the minimum V coordinate to use when rendering with this icon.
	 */
	public float getMinV() {
		return this.minV;
	}

	/**
	 * Returns the maximum V coordinate to use when rendering with this icon.
	 */
	public float getMaxV() {
		return this.maxV;
	}

	/**
	 * Gets a V coordinate on the icon. 0 returns vMin and 16 returns vMax. Other arguments return in-between values.
	 */
	public float getInterpolatedV(double v) {
		return this.border > 0 ? this.minV + (float)v * this.scaledHeight : super.getInterpolatedV(v);
	}

	public void copyFrom(TextureAtlasSprite stitched) {
		if (stitched instanceof BorderedTexture) {
			BorderedTexture bordered = (BorderedTexture)stitched;
			this.tilesheetWidth = bordered.tilesheetWidth;
			this.tilesheetHeight = bordered.tilesheetHeight;
			this.x0 = bordered.x0;
			this.y0 = bordered.y0;
			this.tilesheet = bordered.tilesheet;
			this.border = bordered.border;
		}
	}

	void setBorderWidth(int border) {
		this.border = border;
		int width = this.getOriginX();
		int height = this.getOriginY();

		if (width > 0 && height > 0) {
			logger.finer("setBorderWidth(%s, %s, %d): %dx%d -> %dx%d", new Object[] {this.tilesheet, this.getIconName(), Integer.valueOf(border), Integer.valueOf(width - 2 * border), Integer.valueOf(height - 2 * border), Integer.valueOf(width), Integer.valueOf(height)});

			if (border > 0) {
				this.x0 += border;
				this.y0 += border;
				width -= 2 * border;
				height -= 2 * border;
				this.minU = (float)this.x0 / (float)this.tilesheetWidth;
				this.maxU = (float)(this.x0 + width) / (float)this.tilesheetWidth;
				this.minV = (float)this.y0 / (float)this.tilesheetHeight;
				this.maxV = (float)(this.y0 + height) / (float)this.tilesheetHeight;
			} else {
				this.minU = super.getMinU();
				this.maxU = super.getMaxU();
				this.minV = super.getMinV();
				this.maxV = super.getMaxV();
			}

			this.scaledWidth = (this.maxU - this.minU) / 16.0F;
			this.scaledHeight = (this.maxV - this.minV) / 16.0F;
		} else {
			this.x0 = this.y0 = 0;
			this.minU = this.maxU = this.minV = this.maxV = 0.0F;
			this.scaledWidth = this.scaledHeight = 0.0F;
		}
	}
}
