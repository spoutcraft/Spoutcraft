package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.TexturePackAPI;
import com.prupe.mcpatcher.TexturePackChangeHandler;
import com.prupe.mcpatcher.mod.CustomAnimation$1$1;
import java.awt.image.BufferedImage;
import java.util.Collections;
import net.minecraft.src.ColorizerFoliage;
import net.minecraft.src.ColorizerGrass;

final class CustomAnimation$1 extends TexturePackChangeHandler {
	CustomAnimation$1(String var1, int var2) {
		super(var1, var2);
	}

	public void beforeChange() {
		CustomAnimation.access$000().clear();
		MipmapHelper.reset();
		AAHelper.reset();
		FancyDial.refresh();
	}

	public void afterChange() {
		this.refreshColorizer(ColorizerGrass.grassBuffer, "/misc/grasscolor.png");
		this.refreshColorizer(ColorizerFoliage.foliageBuffer, "/misc/foliagecolor.png");

		if (CustomAnimation.access$100()) {
			String[] var1 = TexturePackAPI.listResources("/anim/", ".properties");
			int var2 = var1.length;

			for (int var3 = 0; var3 < var2; ++var3) {
				String var4 = var1[var3];
				CustomAnimation.addStrip(var4);
			}

			Collections.sort(CustomAnimation.access$000(), new CustomAnimation$1$1(this));
		}
	}

	private void refreshColorizer(int[] var1, String var2) {
		BufferedImage var3 = TexturePackAPI.getImage(var2);

		if (var3.getWidth() == 256 && var3.getHeight() == 256) {
			var3.getRGB(0, 0, 256, 256, var1, 0, 256);
		}
	}

	private boolean isCustomTerrainItemResource(String var1) {
		var1 = var1.replaceFirst("^/anim", "").replaceFirst("\\.(png|properties)$", "");
		return var1.equals("/custom_lava_still") || var1.equals("/custom_lava_flowing") || var1.equals("/custom_water_still") || var1.equals("/custom_water_flowing") || var1.equals("/custom_fire_n_s") || var1.equals("/custom_fire_e_w") || var1.equals("/custom_portal") || var1.matches("/custom_(terrain|item)_\\d+");
	}
}
