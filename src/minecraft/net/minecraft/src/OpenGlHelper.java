package net.minecraft.src;

import org.lwjgl.opengl.*;

public class OpenGlHelper {
	public static int lightmapDisabled;
	public static int lightmapEnabled;
	private static boolean useMultitextureARB = false;

	public OpenGlHelper() {
	}

	public static void initializeTextures() {
		useMultitextureARB = GLContext.getCapabilities().GL_ARB_multitexture && !GLContext.getCapabilities().OpenGL13;
		if (useMultitextureARB) {
			lightmapDisabled = 33984 /*GL_TEXTURE0_ARB*/;
			lightmapEnabled = 33985 /*GL_TEXTURE1_ARB*/;
		}
		else {
			lightmapDisabled = 33984 /*GL_TEXTURE0_ARB*/;
			lightmapEnabled = 33985 /*GL_TEXTURE1_ARB*/;
		}
	}

	public static void setActiveTexture(int i) {
		if (useMultitextureARB) {
			ARBMultitexture.glActiveTextureARB(i);
		}
		else {
			GL13.glActiveTexture(i);
		}
	}

	public static void setClientActiveTexture(int i) {
		if (useMultitextureARB) {
			ARBMultitexture.glClientActiveTextureARB(i);
		}
		else {
			GL13.glClientActiveTexture(i);
		}
	}

	public static void setLightmapTextureCoords(int i, float f, float f1) {
		if (useMultitextureARB) {
			ARBMultitexture.glMultiTexCoord2fARB(i, f, f1);
		}
		else {
			GL13.glMultiTexCoord2f(i, f, f1);
		}
	}
}
