package com.prupe.mcpatcher.mod;

import java.util.List;
import net.minecraft.src.Texture;
import net.minecraft.src.TextureStitched;

public class BorderedTexture extends TextureStitched {
	private float minU;
	private float maxU;
	private float minV;
	private float maxV;
	private float scaledWidth;
	private float scaledHeight;
	private int border;

	public BorderedTexture(String var1) {
		super(var1);
	}

	public void init(Texture var1, List var2, int var3, int var4, int var5, int var6, boolean var7) {
		super.init(var1, var2, var3, var4, var5, var6, var7);
		this.border = var2 != null && !var2.isEmpty() ? ((Texture)var2.get(0)).border : 0;

		if (this.border > 0) {
			var3 += this.border;
			var4 += this.border;
			var5 -= 2 * this.border;
			var6 -= 2 * this.border;
			this.minU = (float)var3 / (float)var1.getWidth();
			this.maxU = (float)(var3 + var5) / (float)var1.getWidth();
			this.minV = (float)var4 / (float)var1.getHeight();
			this.maxV = (float)(var4 + var6) / (float)var1.getHeight();
		} else {
			this.minU = super.getMinU();
			this.maxU = super.getMaxU();
			this.minV = super.getMinV();
			this.maxV = super.getMaxV();
		}

		this.scaledWidth = (this.maxU - this.minU) / 16.0F;
		this.scaledHeight = (this.maxV - this.minV) / 16.0F;
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
	public float getInterpolatedU(double var1) {
		return this.border > 0 ? this.minU + (float)var1 * this.scaledWidth : super.getInterpolatedU(var1);
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
	public float getInterpolatedV(double var1) {
		return this.border > 0 ? this.minV + (float)var1 * this.scaledHeight : super.getInterpolatedV(var1);
	}
}
