package com.prupe.mcpatcher.ctm;

import java.util.Properties;

import com.prupe.mcpatcher.BlendMethod;
import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.TexturePackAPI;
import com.prupe.mcpatcher.TexturePackChangeHandler;

final class RenderPass$2 extends TexturePackChangeHandler {
	RenderPass$2(String x0, int x1) {
		super(x0, x1);
	}

	public void beforeChange() {
		RenderPass.access$402(BlendMethod.ALPHA);
		RenderPass.access$502(true);
	}

	public void afterChange() {
		Properties properties = TexturePackAPI.getProperties(RenderPass.access$600());

		if (properties != null) {
			String method = properties.getProperty("blend.3", "alpha").trim().toLowerCase();
			RenderPass.access$402(BlendMethod.parse(method));

			if (RenderPass.access$400() == null) {
				RenderPass.access$700().error("%s: unknown blend method \'%s\'", new Object[] {RenderPass.access$600(), method});
				RenderPass.access$402(BlendMethod.ALPHA);
			}

			RenderPass.access$502(MCPatcherUtils.getBooleanProperty(properties, "enableLightmap.3", !RenderPass.access$400().isColorBased()));
		}
	}
}
