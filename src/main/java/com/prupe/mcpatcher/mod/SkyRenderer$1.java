package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.TexturePackChangeHandler;
import net.minecraft.src.WorldClient;

final class SkyRenderer$1 extends TexturePackChangeHandler {
	SkyRenderer$1(String var1, int var2) {
		super(var1, var2);
	}

	public void beforeChange() {
		SkyRenderer.access$000().clear();
	}

	public void afterChange() {
		if (SkyRenderer.access$100()) {
			WorldClient var1 = MCPatcherUtils.getMinecraft().theWorld;

			if (var1 != null) {
				SkyRenderer.access$200(var1.provider.dimensionId);
			}
		}

		FireworksHelper.reload();
	}
}
