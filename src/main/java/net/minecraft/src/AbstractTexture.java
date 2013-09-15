package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public abstract class AbstractTexture implements TextureObject {
	public int field_110553_a = -1;

	public int func_110552_b() {
		if (this.field_110553_a == -1) {
			this.field_110553_a = TextureUtil.func_110996_a();
		}

		return this.field_110553_a;
	}

	public void unloadGLTexture() {
		if (this.field_110553_a >= 0) {
			GL11.glDeleteTextures(this.field_110553_a);
			this.field_110553_a = -1;
		}
	}

	public void finalize() {
		this.unloadGLTexture();
	}
}
