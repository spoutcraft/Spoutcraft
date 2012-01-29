package net.minecraft.src;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import net.minecraft.src.Block;
import net.minecraft.src.BlockFluid;
import net.minecraft.src.ChunkPosition;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GLAllocation;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Vec3D;
import net.minecraft.src.World;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class ActiveRenderInfo {

	public static float objectX = 0.0F;
	public static float objectY = 0.0F;
	public static float objectZ = 0.0F;
	private static IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
	private static FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
	private static FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
	private static FloatBuffer objectCoords = GLAllocation.createDirectFloatBuffer(3);
	public static float rotationX;
	public static float rotationXZ;
	public static float rotationZ;
	public static float rotationYZ;
	public static float rotationXY;

	//Spout start
	public static FloatBuffer modelMatrix;
	public static FloatBuffer projectionMatrix;
	//Spout end	

	public static void updateRenderInfo(EntityPlayer var0, boolean var1) {
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelview);
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projection);

		//Spout start
		modelMatrix = modelview.duplicate();
		projectionMatrix = projection.duplicate();
		//Spout end


		GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);
		float var2 = (float)((viewport.get(0) + viewport.get(2)) / 2);
		float var3 = (float)((viewport.get(1) + viewport.get(3)) / 2);
		GLU.gluUnProject(var2, var3, 0.0F, modelview, projection, viewport, objectCoords);
		objectX = objectCoords.get(0);
		objectY = objectCoords.get(1);
		objectZ = objectCoords.get(2);
		int var4 = var1 ? 1 : 0;
		float var5 = var0.rotationPitch;
		float var6 = var0.rotationYaw;
		rotationX = MathHelper.cos(var6 * 3.1415927F / 180.0F) * (float)(1 - var4 * 2);
		rotationZ = MathHelper.sin(var6 * 3.1415927F / 180.0F) * (float)(1 - var4 * 2);
		rotationYZ = -rotationZ * MathHelper.sin(var5 * 3.1415927F / 180.0F) * (float)(1 - var4 * 2);
		rotationXY = rotationX * MathHelper.sin(var5 * 3.1415927F / 180.0F) * (float)(1 - var4 * 2);
		rotationXZ = MathHelper.cos(var5 * 3.1415927F / 180.0F);
	}

	public static Vec3D projectViewFromEntity(EntityLiving var0, double var1) {
		double var3 = var0.prevPosX + (var0.posX - var0.prevPosX) * var1;
		double var5 = var0.prevPosY + (var0.posY - var0.prevPosY) * var1 + (double)var0.getEyeHeight();
		double var7 = var0.prevPosZ + (var0.posZ - var0.prevPosZ) * var1;
		double var9 = var3 + (double)(objectX * 1.0F);
		double var11 = var5 + (double)(objectY * 1.0F);
		double var13 = var7 + (double)(objectZ * 1.0F);
		return Vec3D.createVector(var9, var11, var13);
	}

	public static int getBlockIdAtEntityViewpoint(World var0, EntityLiving var1, float var2) {
		Vec3D var3 = projectViewFromEntity(var1, (double)var2);
		ChunkPosition var4 = new ChunkPosition(var3);
		int var5 = var0.getBlockId(var4.x, var4.y, var4.z);
		if (var5 != 0 && Block.blocksList[var5].blockMaterial.getIsLiquid()) {
			float var6 = BlockFluid.getFluidHeightPercent(var0.getBlockMetadata(var4.x, var4.y, var4.z)) - 0.11111111F;
			float var7 = (float)(var4.y + 1) - var6;
			if (var3.yCoord >= (double)var7) {
				var5 = var0.getBlockId(var4.x, var4.y + 1, var4.z);
			}
		}

		return var5;
	}

}
