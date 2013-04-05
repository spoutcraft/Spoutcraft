package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.BlendMethod;

class FancyDial$Layer {
	final String textureName;
	final float scaleX;
	final float scaleY;
	final float offsetX;
	final float offsetY;
	final float rotationMultiplier;
	final float rotationOffset;
	final BlendMethod blendMethod;
	final boolean debug;

	final FancyDial this$0;

	FancyDial$Layer(FancyDial var1, String var2, float var3, float var4, float var5, float var6, float var7, float var8, BlendMethod var9, boolean var10) {
		this.this$0 = var1;
		this.textureName = var2;
		this.scaleX = var3;
		this.scaleY = var4;
		this.offsetX = var5;
		this.offsetY = var6;
		this.rotationMultiplier = var7;
		this.rotationOffset = var8;
		this.blendMethod = var9;
		this.debug = var10;
	}

	public String toString() {
		return String.format("Layer{%s %f %f %+f %+f x%f}", new Object[] {this.textureName, Float.valueOf(this.scaleX), Float.valueOf(this.scaleY), Float.valueOf(this.offsetX), Float.valueOf(this.offsetY), Float.valueOf(this.rotationMultiplier)});
	}
}
