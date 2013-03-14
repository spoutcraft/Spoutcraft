package com.prupe.mcpatcher.mod;

import java.util.List;
import net.minecraft.src.Texture;
import net.minecraft.src.TextureStitched;

public class BorderedTexture extends TextureStitched {
	private float normalizedX0;
	private float normalizedX1;
	private float normalizedY0;
	private float normalizedY1;
	private float normalizedWidth;
	private float normalizedHeight;

	public BorderedTexture(String var1) {
		super(var1);
	}

	public void func_94218_a(Texture var1, List var2, int var3, int var4, int var5, int var6, boolean var7) {
		super.func_94218_a(var1, var2, var3, var4, var5, var6, var7);
		int var8 = var2 != null && !var2.isEmpty() ? ((Texture)var2.get(0)).border : 0;
		var3 += var8;
		var4 += var8;
		var5 -= 2 * var8;
		var6 -= 2 * var8;
		this.normalizedX0 = (float)var3 / (float)var1.func_94275_d();
		this.normalizedX1 = (float)(var3 + var5) / (float)var1.func_94275_d();
		this.normalizedY0 = (float)var4 / (float)var1.func_94276_e();
		this.normalizedY1 = (float)(var4 + var6) / (float)var1.func_94276_e();
		this.normalizedWidth = this.normalizedX1 - this.normalizedX0;
		this.normalizedHeight = this.normalizedY1 - this.normalizedY0;
		this.normalizedX1 = minusEpsilon(this.normalizedX1);
		this.normalizedY1 = minusEpsilon(this.normalizedY1);
	}

	public float func_94209_e() {
		return this.normalizedX0;
	}

	public float func_94212_f() {
		return this.normalizedX1;
	}

	public float func_94214_a(double var1) {
		return minusEpsilon(this.normalizedX0 + (float)var1 / 16.0F * this.normalizedWidth);
	}

	public float func_94206_g() {
		return this.normalizedY0;
	}

	public float func_94210_h() {
		return this.normalizedY1;
	}

	public float func_94207_b(double var1) {
		return minusEpsilon(this.normalizedY0 + (float)var1 / 16.0F * this.normalizedHeight);
	}

	private static float minusEpsilon(float var0) {
		return var0 > 0.0F ? Float.intBitsToFloat(Float.floatToRawIntBits(var0) - 1) : var0;
	}
}
