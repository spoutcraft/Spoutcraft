package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.TexturePackAPI;
import com.prupe.mcpatcher.TexturePackChangeHandler;
import com.prupe.mcpatcher.TileLoader;
import com.prupe.mcpatcher.mod.TileOverrideImpl$BetterGrass;
import java.util.Arrays;
import java.util.Iterator;
import net.minecraft.src.Block;

final class CTMUtils$1 extends TexturePackChangeHandler {
	CTMUtils$1(String var1, int var2) {
		super(var1, var2);
	}

	public void initialize() {}

	public void beforeChange() {
		RenderPassAPI.instance.clear();
		CTMUtils.access$000().clear();
		Arrays.fill(CTMUtils.access$100(), (Object)null);
		CTMUtils.access$200().clear();
		CTMUtils.access$302(new TileLoader("terrain", true));
		CTMUtils.access$502((TileOverrideImpl$BetterGrass)null);

		if (CTMUtils.access$600() || CTMUtils.access$700()) {
			Iterator var1 = TexturePackAPI.listResources("/ctm", ".properties", true, false, true).iterator();

			while (var1.hasNext()) {
				String var2 = (String)var1.next();
				CTMUtils.access$800(TileOverride.create(var2, CTMUtils.access$300()));
			}
		}
	}

	public void afterChange() {
		if (CTMUtils.access$900()) {
			CTMUtils.access$800(CTMUtils.access$502(new TileOverrideImpl$BetterGrass(CTMUtils.access$300(), 2, "grass")));
			CTMUtils.access$800(new TileOverrideImpl$BetterGrass(CTMUtils.access$300(), 110, "mycel"));
		}

		Iterator var1 = CTMUtils.access$000().iterator();

		while (var1.hasNext()) {
			ITileOverride var2 = (ITileOverride)var1.next();
			var2.registerIcons();
		}

		for (int var6 = 0; var6 < CTMUtils.access$100().length; ++var6) {
			if (CTMUtils.access$100()[var6] != null && Block.blocksList[var6] != null) {
				ITileOverride[] var7 = CTMUtils.access$100()[var6];
				int var3 = var7.length;

				for (int var4 = 0; var4 < var3; ++var4) {
					ITileOverride var5 = var7[var4];

					if (var5 != null && !var5.isDisabled() && var5.getRenderPass() >= 0) {
						RenderPassAPI.instance.setRenderPassForBlock(Block.blocksList[var6], var5.getRenderPass());
					}
				}
			}
		}
	}
}