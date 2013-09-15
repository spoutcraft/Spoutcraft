package com.prupe.mcpatcher.hd;

import com.prupe.mcpatcher.BlendMethod;

import net.minecraft.src.ResourceLocation;

class FancyDial$Layer {
	final ResourceLocation textureName;
	final float scaleX;
	final float scaleY;
	final float offsetX;
	final float offsetY;
	final float rotationMultiplier;
	final float rotationOffset;
	final BlendMethod blendMethod;
	final boolean debug;

	final FancyDial this$0;

	FancyDial$Layer(FancyDial var1, ResourceLocation textureName, float scaleX, float scaleY, float offsetX, float offsetY, float rotationMultiplier, float rotationOffset, BlendMethod blendMethod, boolean debug) {
		this.this$0 = var1;
		this.textureName = textureName;
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.rotationMultiplier = rotationMultiplier;
		this.rotationOffset = rotationOffset;
		this.blendMethod = blendMethod;
		this.debug = debug;
	}

	public String toString() {
		return String.format("Layer{%s %f %f %+f %+f x%f}", new Object[] {this.textureName, Float.valueOf(this.scaleX), Float.valueOf(this.scaleY), Float.valueOf(this.offsetX), Float.valueOf(this.offsetY), Float.valueOf(this.rotationMultiplier)});
	}
}
