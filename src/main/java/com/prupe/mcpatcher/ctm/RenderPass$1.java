package com.prupe.mcpatcher.ctm;

import java.util.Arrays;

import net.minecraft.src.Block;

final class RenderPass$1 extends RenderPassAPI {
	boolean skipDefaultRendering(Block block) {
		return RenderPass.access$000() > 1;
	}

	boolean skipThisRenderPass(Block block, int pass) {
		if (pass < 0) {
			pass = block.getRenderBlockPass();
		}

		return pass != RenderPass.access$000();
	}

	void clear() {
		RenderPass.access$102(1);
		Arrays.fill(RenderPass.access$200(), -1);
		Arrays.fill(RenderPass.access$300(), -1);

		for (int i = 0; i < Block.blocksList.length; ++i) {
			Block block = Block.blocksList[i];

			if (block != null) {
				RenderPass.access$200()[i] = block.getRenderBlockPass();
			}
		}
	}

	void setRenderPassForBlock(Block block, int pass) {
		if (pass >= 0) {
			if (pass <= 2) {
				RenderPass.access$200()[block.blockID] = pass;
			} else {
				RenderPass.access$300()[block.blockID] = pass;
			}

			RenderPass.access$102(Math.max(RenderPass.access$100(), pass));
		}
	}

	void finish() {
		RenderPass.finish();
	}
}
