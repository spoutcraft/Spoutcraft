package com.prupe.mcpatcher.mod;

import java.util.Arrays;
import net.minecraft.src.Block;

final class RenderPass$1 extends RenderPassAPI {
	boolean skipDefaultRendering(Block var1) {
		return RenderPass.access$000() > 1;
	}

	boolean skipThisRenderPass(Block var1, int var2) {
		if (var2 < 0) {
			var2 = var1.getRenderBlockPass();
		}

		return var2 != RenderPass.access$000();
	}

	void clear() {
		RenderPass.access$102(1);
		Arrays.fill(RenderPass.access$200(), -1);
		Arrays.fill(RenderPass.access$300(), -1);

		for (int var1 = 0; var1 < Block.blocksList.length; ++var1) {
			Block var2 = Block.blocksList[var1];

			if (var2 != null) {
				RenderPass.access$200()[var1] = var2.getRenderBlockPass();
			}
		}
	}

	void setRenderPassForBlock(Block var1, int var2) {
		if (var2 >= 0) {
			if (var2 <= 2) {
				RenderPass.access$200()[var1.blockID] = var2;
			} else {
				RenderPass.access$300()[var1.blockID] = var2;
			}

			RenderPass.access$102(Math.max(RenderPass.access$100(), var2));
		}
	}

	void finish() {
		RenderPass.finish();
	}
}
