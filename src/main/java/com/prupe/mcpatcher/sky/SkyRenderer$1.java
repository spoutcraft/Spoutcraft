package com.prupe.mcpatcher.sky;

import com.prupe.mcpatcher.TexturePackChangeHandler;
import net.minecraft.src.Minecraft;
import net.minecraft.src.WorldClient;

final class SkyRenderer$1 extends TexturePackChangeHandler {
	SkyRenderer$1(String x0, int x1) {
		super(x0, x1);
	}

	public void beforeChange() {
		SkyRenderer.access$000().clear();
	}

	public void afterChange() {
		if (SkyRenderer.access$100()) {
			WorldClient world = Minecraft.getMinecraft().theWorld;

			if (world != null) {
				SkyRenderer.access$200(world.provider.dimensionId);
			}
		}

		FireworksHelper.reload();
	}
}
