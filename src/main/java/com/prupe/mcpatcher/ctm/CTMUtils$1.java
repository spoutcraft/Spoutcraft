package com.prupe.mcpatcher.ctm;

import java.util.Arrays;
import java.util.Iterator;

import com.prupe.mcpatcher.TexturePackAPI;
import com.prupe.mcpatcher.TexturePackChangeHandler;
import com.prupe.mcpatcher.TileLoader;

import net.minecraft.src.Block;
import net.minecraft.src.ResourceLocation;

final class CTMUtils$1 extends TexturePackChangeHandler {
	CTMUtils$1(String x0, int x1) {
		super(x0, x1);
	}

	public void initialize() {}

	public void beforeChange() {
		RenderPassAPI.instance.clear();
		CTMUtils.access$000().clear();
		Arrays.fill(CTMUtils.access$100(), (Object)null);
		CTMUtils.access$200().clear();
		CTMUtils.access$302(new TileLoader("textures/blocks", true, CTMUtils.access$400()));
		CTMUtils.access$502((TileOverrideImpl$BetterGrass)null);

		if (CTMUtils.access$600() || CTMUtils.access$700()) {
			Iterator i$ = TexturePackAPI.listResources("mcpatcher/ctm", ".properties", true, false, true).iterator();

			while (i$.hasNext()) {
				ResourceLocation resource = (ResourceLocation)i$.next();
				CTMUtils.access$800(TileOverride.create(resource, CTMUtils.access$300()));
			}
		}
	}

	public void afterChange() {
		if (CTMUtils.access$900()) {
			CTMUtils.access$800(CTMUtils.access$502(new TileOverrideImpl$BetterGrass(CTMUtils.access$300(), 2, "grass")));
			CTMUtils.access$800(new TileOverrideImpl$BetterGrass(CTMUtils.access$300(), 110, "mycel"));
		}

		Iterator i$ = CTMUtils.access$000().iterator();

		while (i$.hasNext()) {
			ITileOverride overrides = (ITileOverride)i$.next();
			overrides.registerIcons();
		}

		int i$1;
		ITileOverride[] var8;

		for (int var6 = 0; var6 < CTMUtils.access$100().length; ++var6) {
			if (CTMUtils.access$100()[var6] != null && Block.blocksList[var6] != null) {
				var8 = CTMUtils.access$100()[var6];
				i$1 = var8.length;

				for (int overrides1 = 0; overrides1 < i$1; ++overrides1) {
					ITileOverride override = var8[overrides1];

					if (override != null && !override.isDisabled() && override.getRenderPass() >= 0) {
						RenderPassAPI.instance.setRenderPassForBlock(Block.blocksList[var6], override.getRenderPass());
					}
				}
			}
		}

		ITileOverride[][] var7 = CTMUtils.access$100();
		int var9 = var7.length;

		for (i$1 = 0; i$1 < var9; ++i$1) {
			ITileOverride[] var10 = var7[i$1];
			this.sortOverrides(var10);
		}

		i$ = CTMUtils.access$200().values().iterator();

		while (i$.hasNext()) {
			var8 = (ITileOverride[])i$.next();
			this.sortOverrides(var8);
		}
	}

	private void sortOverrides(ITileOverride[] overrides) {
		if (overrides != null) {
			Arrays.sort(overrides);
		}
	}
}
