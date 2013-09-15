package com.prupe.mcpatcher.hd;

import java.util.Iterator;
import java.util.Properties;

import com.prupe.mcpatcher.TexturePackAPI;
import com.prupe.mcpatcher.TexturePackChangeHandler;

import net.minecraft.src.ResourceLocation;

final class CustomAnimation$1 extends TexturePackChangeHandler {
	CustomAnimation$1(String x0, int x1) {
		super(x0, x1);
	}

	public void beforeChange() {
		if (!CustomAnimation.access$000().isEmpty()) {
			CustomAnimation.access$100().fine("%d animations were never registered:", new Object[] {Integer.valueOf(CustomAnimation.access$000().size())});
			Iterator i$ = CustomAnimation.access$000().keySet().iterator();

			while (i$.hasNext()) {
				ResourceLocation resource = (ResourceLocation)i$.next();
				CustomAnimation.access$100().fine("  %s", new Object[] {resource});
			}

			CustomAnimation.access$000().clear();
		}

		CustomAnimation.access$200().clear();
		MipmapHelper.reset();
		FancyDial.refresh();
	}

	public void afterChange() {
		if (CustomAnimation.access$300()) {
			Iterator i$ = TexturePackAPI.listResources("mcpatcher/anim", ".properties", true, false, false).iterator();

			while (i$.hasNext()) {
				ResourceLocation resource = (ResourceLocation)i$.next();
				Properties properties = TexturePackAPI.getProperties(resource);

				if (properties != null) {
					CustomAnimation.access$000().put(resource, properties);
				}
			}
		}
	}
}
