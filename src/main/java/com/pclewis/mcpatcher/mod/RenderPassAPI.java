package com.pclewis.mcpatcher.mod;

import net.minecraft.src.Block;

class RenderPassAPI {
    static RenderPassAPI instance = new RenderPassAPI();

    boolean skipDefaultRendering(Block block) {
        return false;
    }

    boolean skipThisRenderPass(Block block, int pass) {
        return pass > 2;
    }

    void clear() {
    }

    void setRenderPassForBlock(Block block, int pass) {
    }

    void finish() {
    }
}
