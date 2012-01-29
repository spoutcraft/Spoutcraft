package net.minecraft.src;

import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GLContext;

public class OpenGlCapsChecker {
	private static boolean tryCheckOcclusionCapable = true;

	public OpenGlCapsChecker() {
	}

	public static boolean checkARBOcclusion() {
		return tryCheckOcclusionCapable && GLContext.getCapabilities().GL_ARB_occlusion_query;
	}
}
