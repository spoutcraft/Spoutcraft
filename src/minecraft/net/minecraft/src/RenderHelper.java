package net.minecraft.src;

import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL11;

public class RenderHelper {
	private static FloatBuffer colorBuffer = GLAllocation.createDirectFloatBuffer(16);

	public RenderHelper() {
	}

	public static void disableStandardItemLighting() {
		GL11.glDisable(2896 /*GL_LIGHTING*/);
		GL11.glDisable(16384 /*GL_LIGHT0*/);
		GL11.glDisable(16385 /*GL_LIGHT1*/);
		GL11.glDisable(2903 /*GL_COLOR_MATERIAL*/);
	}

	public static void enableStandardItemLighting() {
		GL11.glEnable(2896 /*GL_LIGHTING*/);
		GL11.glEnable(16384 /*GL_LIGHT0*/);
		GL11.glEnable(16385 /*GL_LIGHT1*/);
		GL11.glEnable(2903 /*GL_COLOR_MATERIAL*/);
		GL11.glColorMaterial(1032 /*GL_FRONT_AND_BACK*/, 5634 /*GL_AMBIENT_AND_DIFFUSE*/);
		float f = 0.4F;
		float f1 = 0.6F;
		float f2 = 0.0F;
		Vec3D vec3d = Vec3D.createVector(0.20000000298023224D, 1.0D, -0.69999998807907104D).normalize();
		GL11.glLight(16384 /*GL_LIGHT0*/, 4611 /*GL_POSITION*/, setColorBuffer(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord, 0.0D));
		GL11.glLight(16384 /*GL_LIGHT0*/, 4609 /*GL_DIFFUSE*/, setColorBuffer(f1, f1, f1, 1.0F));
		GL11.glLight(16384 /*GL_LIGHT0*/, 4608 /*GL_AMBIENT*/, setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
		GL11.glLight(16384 /*GL_LIGHT0*/, 4610 /*GL_SPECULAR*/, setColorBuffer(f2, f2, f2, 1.0F));
		vec3d = Vec3D.createVector(-0.20000000298023224D, 1.0D, 0.69999998807907104D).normalize();
		GL11.glLight(16385 /*GL_LIGHT1*/, 4611 /*GL_POSITION*/, setColorBuffer(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord, 0.0D));
		GL11.glLight(16385 /*GL_LIGHT1*/, 4609 /*GL_DIFFUSE*/, setColorBuffer(f1, f1, f1, 1.0F));
		GL11.glLight(16385 /*GL_LIGHT1*/, 4608 /*GL_AMBIENT*/, setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
		GL11.glLight(16385 /*GL_LIGHT1*/, 4610 /*GL_SPECULAR*/, setColorBuffer(f2, f2, f2, 1.0F));
		GL11.glShadeModel(7424 /*GL_FLAT*/);
		GL11.glLightModel(2899 /*GL_LIGHT_MODEL_AMBIENT*/, setColorBuffer(f, f, f, 1.0F));
	}

	private static FloatBuffer setColorBuffer(double d, double d1, double d2, double d3) {
		return setColorBuffer((float)d, (float)d1, (float)d2, (float)d3);
	}

	private static FloatBuffer setColorBuffer(float f, float f1, float f2, float f3) {
		colorBuffer.clear();
		colorBuffer.put(f).put(f1).put(f2).put(f3);
		colorBuffer.flip();
		return colorBuffer;
	}

	public static void func_41089_c() {
		GL11.glPushMatrix();
		GL11.glRotatef(-30F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(165F, 1.0F, 0.0F, 0.0F);
		enableStandardItemLighting();
		GL11.glPopMatrix();
	}
}
