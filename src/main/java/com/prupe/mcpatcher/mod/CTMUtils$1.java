package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.TexturePackAPI;
import com.prupe.mcpatcher.TexturePackChangeHandler;
import com.prupe.mcpatcher.mod.CTMUtils$1$1;
import com.prupe.mcpatcher.mod.TileOverrideImpl$BetterGrass;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import net.minecraft.src.Block;
import net.minecraft.src.Tessellator;
import net.minecraft.src.TextureMap;

final class CTMUtils$1 extends TexturePackChangeHandler {
	CTMUtils$1(String var1, int var2) {
		super(var1, var2);
	}

	public void initialize() {}

	public void beforeChange() {
		CTMUtils.access$002(true);

		try {
			Iterator var1 = CTMUtils.access$100().iterator();

			while (var1.hasNext()) {
				TextureMap var2 = (TextureMap)var1.next();
				var2.getTexture().unloadGLTexture();
			}
		} catch (Throwable var10) {
			var10.printStackTrace();
		}

		CTMUtils.access$100().clear();
		RenderPassAPI.instance.clear();
		TessellatorUtils.clear(Tessellator.instance);
		Arrays.fill(CTMUtils.access$200(), (Object)null);
		CTMUtils.access$300().clear();
		CTMUtils.access$400().clear();
		CTMUtils.access$502(new TileLoader());
		CTMUtils.access$702((TileOverrideImpl$BetterGrass)null);
		CITUtils.refresh();

		if (CTMUtils.access$800() || CTMUtils.access$900()) {
			ArrayList var11 = new ArrayList();
			String[] var12 = TexturePackAPI.listDirectories("/ctm");
			int var3 = var12.length;

			for (int var4 = 0; var4 < var3; ++var4) {
				String var5 = var12[var4];
				String[] var6 = TexturePackAPI.listResources(var5, ".properties");
				int var7 = var6.length;

				for (int var8 = 0; var8 < var7; ++var8) {
					String var9 = var6[var8];
					var11.add(var9);
				}
			}

			Collections.sort(var11, new CTMUtils$1$1(this));
			Iterator var13 = var11.iterator();

			while (var13.hasNext()) {
				String var14 = (String)var13.next();
				CTMUtils.access$1000(TileOverride.create(var14, CTMUtils.access$500()));
			}
		}
	}

	public void afterChange() {
		int var1;

		if (CTMUtils.access$1100() > 0 && (CTMUtils.access$800() || CTMUtils.access$900())) {
			for (var1 = 0; !CTMUtils.access$400().isEmpty(); ++var1) {
				CTMUtils.access$1202(false);
				TextureMap var2 = new TextureMap(2, "ctm" + var1, "not_used", CTMUtils.access$1300());
				var2.refreshTextures();

				if (!CTMUtils.access$1200()) {
					break;
				}

				CTMUtils.access$100().add(var2);
			}
		}

		CTMUtils.access$400().clear();
		CTMUtils.access$500().finish();

		for (var1 = 0; var1 < CTMUtils.access$200().length; ++var1) {
			if (CTMUtils.access$200()[var1] != null && Block.blocksList[var1] != null) {
				ITileOverride[] var6 = CTMUtils.access$200()[var1];
				int var3 = var6.length;

				for (int var4 = 0; var4 < var3; ++var4) {
					ITileOverride var5 = var6[var4];

					if (var5 != null && !var5.isDisabled() && var5.getRenderPass() >= 0) {
						RenderPassAPI.instance.setRenderPassForBlock(Block.blocksList[var1], var5.getRenderPass());
					}
				}
			}
		}

		CTMUtils.access$002(false);
	}
}
