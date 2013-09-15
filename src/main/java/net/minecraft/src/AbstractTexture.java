package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public abstract class AbstractTexture implements TextureObject {
	public int glTextureId = -1;

	public int getGlTextureId() {
		if (this.glTextureId == -1) {
			this.glTextureId = TextureUtil.glGenTextures();
		}

		return this.glTextureId;
	}

	public void unloadGLTexture() {
		if (this.glTextureId >= 0) {
			GL11.glDeleteTextures(this.glTextureId);
			this.glTextureId = -1;
		}
	}

	public void finalize() {
		this.unloadGLTexture();
	}
}
