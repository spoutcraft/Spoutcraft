package com.prupe.mcpatcher.mod;

import net.minecraft.src.Block;

class RenderPassAPI {
	static RenderPassAPI instance = new RenderPassAPI();

	boolean skipDefaultRendering(Block var1) {
		return false;
	}

	boolean skipThisRenderPass(Block var1, int var2) {
		return var2 > 2;
	}

	void clear() {}

	void setRenderPassForBlock(Block var1, int var2) {}

	void finish() {}
}
