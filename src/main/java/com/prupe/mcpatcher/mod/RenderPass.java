package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.BlendMethod;
import com.prupe.mcpatcher.TexturePackChangeHandler;
import com.prupe.mcpatcher.mod.RenderPass$1;
import com.prupe.mcpatcher.mod.RenderPass$2;
import net.minecraft.src.Block;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityRenderer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderGlobal;
import org.lwjgl.opengl.GL11;

public class RenderPass {
	private static final String RENDERPASS_PROPERTIES = "/renderpass.properties";
	private static final int[] baseRenderPass = new int[Block.blocksList.length];
	private static final int[] extraRenderPass = new int[Block.blocksList.length];
	private static BlendMethod blendMethod;
	private static boolean enableLightmap;
	private static int renderPass = -1;
	private static int maxRenderPass = 1;
	private static boolean ambientOcclusion;

	public static void start(int var0) {
		finish();
		renderPass = var0;
	}

	public static void finish() {
		renderPass = -1;
	}

	public static boolean skipAllRenderPasses(boolean[] var0) {
		boolean[] var1 = var0;
		int var2 = var0.length;

		for (int var3 = 0; var3 < var2; ++var3) {
			boolean var4 = var1[var3];

			if (!var4) {
				return false;
			}
		}

		return true;
	}

	public static int getBlockRenderPass(Block var0) {
		return renderPass <= 2 ? baseRenderPass[var0.blockID] : extraRenderPass[var0.blockID];
	}

	public static boolean canRenderInPass(Block var0, int var1, boolean var2) {
		return baseRenderPass[var0.blockID] < 2 && extraRenderPass[var0.blockID] < 2 ? var2 : var1 == getBlockRenderPass(var0);
	}

	public static boolean shouldSideBeRendered(Block var0, IBlockAccess var1, int var2, int var3, int var4, int var5) {
		return var0.shouldSideBeRendered(var1, var2, var3, var4, var5) ? true : extraRenderPass[var1.getBlockId(var2, var3, var4)] >= 0 && extraRenderPass[var0.blockID] < 0;
	}

	public static boolean setAmbientOcclusion(boolean var0) {
		ambientOcclusion = var0;
		return var0;
	}

	public static float getAOBaseMultiplier(float var0) {
		return renderPass > 2 && !enableLightmap ? 1.0F : var0;
	}

	public static void doRenderPass(RenderGlobal var0, EntityLiving var1, int var2, double var3) {
		if (var2 <= maxRenderPass) {
			switch (var2) {
				case 2:
					GL11.glDisable(GL11.GL_CULL_FACE);
					var0.sortAndRender(var1, var2, var3);
					GL11.glEnable(GL11.GL_CULL_FACE);
					break;

				case 3:
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					GL11.glPolygonOffset(-2.0F, -2.0F);
					GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
					GL11.glEnable(GL11.GL_CULL_FACE);

					if (ambientOcclusion) {
						GL11.glShadeModel(GL11.GL_SMOOTH);
					}

					blendMethod.applyBlending();
					var0.sortAndRender(var1, var2, var3);
					GL11.glPolygonOffset(0.0F, 0.0F);
					GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glShadeModel(GL11.GL_FLAT);
			}
		}
	}

	public static void enableDisableLightmap(EntityRenderer var0, double var1, int var3) {
		if (!enableLightmap && var3 == 3) {
			var0.disableLightmap(var1);
		} else {
			var0.enableLightmap(var1);
		}
	}

	static int access$000() {
		return renderPass;
	}

	static int access$102(int var0) {
		maxRenderPass = var0;
		return var0;
	}

	static int[] access$200() {
		return baseRenderPass;
	}

	static int[] access$300() {
		return extraRenderPass;
	}

	static int access$100() {
		return maxRenderPass;
	}

	static BlendMethod access$402(BlendMethod var0) {
		blendMethod = var0;
		return var0;
	}

	static boolean access$502(boolean var0) {
		enableLightmap = var0;
		return var0;
	}

	static BlendMethod access$400() {
		return blendMethod;
	}

	static {
		RenderPassAPI.instance = new RenderPass$1();
		TexturePackChangeHandler.register(new RenderPass$2("Better Glass", 4));
	}
}
