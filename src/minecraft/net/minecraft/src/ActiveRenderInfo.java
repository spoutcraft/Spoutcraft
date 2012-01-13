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

	public static float field_41074_a = 0.0F;
	public static float field_41072_b = 0.0F;
	public static float field_41073_c = 0.0F;
	private static IntBuffer field_41079_i = GLAllocation.createDirectIntBuffer(16);
	private static FloatBuffer field_41076_j = GLAllocation.createDirectFloatBuffer(16);
	private static FloatBuffer field_41077_k = GLAllocation.createDirectFloatBuffer(16);
	private static FloatBuffer field_41075_l = GLAllocation.createDirectFloatBuffer(3);
	public static float field_41070_d;
	public static float field_41071_e;
	public static float field_41068_f;
	public static float field_41069_g;
	public static float field_41078_h;
	
	//Spout start
	public static FloatBuffer modelMatrix;
	public static FloatBuffer projectionMatrix;
	//Spout end	

	public static void func_41067_a(EntityPlayer var0, boolean var1) {
		GL11.glGetFloat(2982 /*GL_MODELVIEW_MATRIX*/, field_41076_j);
		GL11.glGetFloat(2983 /*GL_PROJECTION_MATRIX*/, field_41077_k);
		
		//Spout start
		modelMatrix = field_41076_j.duplicate();
		projectionMatrix = field_41077_k.duplicate();
		//Spout end
		
		GL11.glGetInteger(2978 /*GL_VIEWPORT*/, field_41079_i);
		float var2 = (float)((field_41079_i.get(0) + field_41079_i.get(2)) / 2);
		float var3 = (float)((field_41079_i.get(1) + field_41079_i.get(3)) / 2);
		GLU.gluUnProject(var2, var3, 0.0F, field_41076_j, field_41077_k, field_41079_i, field_41075_l);
		field_41074_a = field_41075_l.get(0);
		field_41072_b = field_41075_l.get(1);
		field_41073_c = field_41075_l.get(2);
		int var4 = var1?1:0;
		float var5 = var0.rotationPitch;
		float var6 = var0.rotationYaw;
		field_41070_d = MathHelper.cos(var6 * 3.1415927F / 180.0F) * (float)(1 - var4 * 2);
		field_41068_f = MathHelper.sin(var6 * 3.1415927F / 180.0F) * (float)(1 - var4 * 2);
		field_41069_g = -field_41068_f * MathHelper.sin(var5 * 3.1415927F / 180.0F) * (float)(1 - var4 * 2);
		field_41078_h = field_41070_d * MathHelper.sin(var5 * 3.1415927F / 180.0F) * (float)(1 - var4 * 2);
		field_41071_e = MathHelper.cos(var5 * 3.1415927F / 180.0F);
	}

	public static Vec3D func_41065_a(EntityLiving var0, double var1) {
		double var3 = var0.prevPosX + (var0.posX - var0.prevPosX) * var1;
		double var5 = var0.prevPosY + (var0.posY - var0.prevPosY) * var1 + (double)var0.getEyeHeight();
		double var7 = var0.prevPosZ + (var0.posZ - var0.prevPosZ) * var1;
		double var9 = var3 + (double)(field_41074_a * 1.0F);
		double var11 = var5 + (double)(field_41072_b * 1.0F);
		double var13 = var7 + (double)(field_41073_c * 1.0F);
		return Vec3D.createVector(var9, var11, var13);
	}

	public static int func_41066_a(World var0, EntityLiving var1, float var2) {
		Vec3D var3 = func_41065_a(var1, (double)var2);
		ChunkPosition var4 = new ChunkPosition(var3);
		int var5 = var0.getBlockId(var4.x, var4.y, var4.z);
		if(var5 != 0 && Block.blocksList[var5].blockMaterial.getIsLiquid()) {
			float var6 = BlockFluid.getFluidHeightPercent(var0.getBlockMetadata(var4.x, var4.y, var4.z)) - 0.11111111F;
			float var7 = (float)(var4.y + 1) - var6;
			if(var3.yCoord >= (double)var7) {
				var5 = var0.getBlockId(var4.x, var4.y + 1, var4.z);
			}
		}

		return var5;
	}

}
