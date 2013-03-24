package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.BlendMethod;
import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.TexturePackAPI;
import com.prupe.mcpatcher.TexturePackChangeHandler;
import java.util.Properties;

final class RenderPass$2 extends TexturePackChangeHandler {
	RenderPass$2(String var1, int var2) {
		super(var1, var2);
	}

	public void beforeChange() {
		RenderPass.access$402(BlendMethod.ALPHA);
		RenderPass.access$502(true);
	}

	public void afterChange() {
		Properties var1 = TexturePackAPI.getProperties("/renderpass.properties");

		if (var1 != null) {
			String var2 = var1.getProperty("blend.3", "alpha").trim().toLowerCase();
			RenderPass.access$402(BlendMethod.parse(var2));

			if (RenderPass.access$400() == null) {
				RenderPass.access$402(BlendMethod.ALPHA);
			}

			RenderPass.access$502(MCPatcherUtils.getBooleanProperty(var1, "enableLightmap.3", !RenderPass.access$400().isColorBased()));
		}
	}
}
