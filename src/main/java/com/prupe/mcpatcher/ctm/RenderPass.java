package com.prupe.mcpatcher.ctm;

import com.prupe.mcpatcher.BlendMethod;
import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.TexturePackAPI;
import com.prupe.mcpatcher.TexturePackChangeHandler;
import com.prupe.mcpatcher.ctm.RenderPass$1;
import com.prupe.mcpatcher.ctm.RenderPass$2;
import net.minecraft.src.Block;
import net.minecraft.src.EntityLivingBase;
import net.minecraft.src.EntityRenderer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderGlobal;
import net.minecraft.src.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderPass {
	private static final MCLogger logger = MCLogger.getLogger("Better Glass");
	private static final ResourceLocation RENDERPASS_PROPERTIES = TexturePackAPI.newMCPatcherResourceLocation("renderpass.properties");
	private static final int[] baseRenderPass = new int[Block.blocksList.length];
	private static final int[] extraRenderPass = new int[Block.blocksList.length];
	private static BlendMethod blendMethod;
	private static boolean enableLightmap;
	private static int renderPass = -1;
	private static int maxRenderPass = 1;
	private static boolean ambientOcclusion;

	public static void start(int pass) {
		finish();
		renderPass = pass;
	}

	public static void finish() {
		renderPass = -1;
	}

	public static boolean skipAllRenderPasses(boolean[] skipRenderPass) {
		boolean[] arr$ = skipRenderPass;
		int len$ = skipRenderPass.length;

		for (int i$ = 0; i$ < len$; ++i$) {
			boolean b = arr$[i$];

			if (!b) {
				return false;
			}
		}

		return true;
	}

	public static int getBlockRenderPass(Block block) {
		return renderPass <= 2 ? baseRenderPass[block.blockID] : extraRenderPass[block.blockID];
	}

	public static boolean canRenderInPass(Block block, int pass, boolean renderThis) {
		return baseRenderPass[block.blockID] < 2 && extraRenderPass[block.blockID] < 2 ? renderThis : pass == getBlockRenderPass(block);
	}

	public static boolean shouldSideBeRendered(Block block, IBlockAccess blockAccess, int i, int j, int k, int face) {
		return block.shouldSideBeRendered(blockAccess, i, j, k, face) ? true : extraRenderPass[blockAccess.getBlockId(i, j, k)] >= 0 && extraRenderPass[block.blockID] < 0;
	}

	public static boolean setAmbientOcclusion(boolean ambientOcclusion) {
		ambientOcclusion = ambientOcclusion;
		return ambientOcclusion;
	}

	public static float getAOBaseMultiplier(float multiplier) {
		return renderPass > 2 && !enableLightmap ? 1.0F : multiplier;
	}

	public static void doRenderPass(RenderGlobal renderer, EntityLivingBase camera, int pass, double partialTick) {
		if (pass <= maxRenderPass) {
			switch (pass) {
				case 2:
					GL11.glDisable(GL11.GL_CULL_FACE);
					renderer.sortAndRender(camera, pass, partialTick);
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
					renderer.sortAndRender(camera, pass, partialTick);
					GL11.glPolygonOffset(0.0F, 0.0F);
					GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glShadeModel(GL11.GL_FLAT);
			}
		}
	}

	public static void enableDisableLightmap(EntityRenderer renderer, double partialTick, int pass) {
		if (!enableLightmap && pass == 3) {
			renderer.disableLightmap(partialTick);
		} else {
			renderer.enableLightmap(partialTick);
		}
	}

	static int access$000() {
		return renderPass;
	}

	static int access$102(int x0) {
		maxRenderPass = x0;
		return x0;
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

	static BlendMethod access$402(BlendMethod x0) {
		blendMethod = x0;
		return x0;
	}

	static boolean access$502(boolean x0) {
		enableLightmap = x0;
		return x0;
	}

	static ResourceLocation access$600() {
		return RENDERPASS_PROPERTIES;
	}

	static BlendMethod access$400() {
		return blendMethod;
	}

	static MCLogger access$700() {
		return logger;
	}

	static {
		RenderPassAPI.instance = new RenderPass$1();
		TexturePackChangeHandler.register(new RenderPass$2("Better Glass", 4));
	}
}
