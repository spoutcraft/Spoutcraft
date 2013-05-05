package com.prupe.mcpatcher;

import java.util.Iterator;
import net.minecraft.src.Tessellator;
import net.minecraft.src.TextureMap;

final class TileLoader$1 extends TexturePackChangeHandler {
	TileLoader$1(String var1, int var2) {
		super(var1, var2);
	}

	public void initialize() {}

	public void beforeChange() {
		TileLoader.access$002(true);
		TessellatorUtils.clear(Tessellator.instance);
		Iterator var1 = TileLoader.access$100().iterator();

		while (var1.hasNext()) {
			TextureMap var2 = (TextureMap)var1.next();

			try {
				var2.getTexture().unloadGLTexture();
			} catch (Throwable var4) {
				var4.printStackTrace();
			}
		}

		TileLoader.access$100().clear();
		TileLoader.access$200().clear();
	}

	public void afterChange() {
		while (true) {
			Iterator var1 = TileLoader.access$200().iterator();

			while (true) {
				if (var1.hasNext()) {
					TileLoader var2 = (TileLoader)var1.next();

					if (TileLoader.access$300(var2).isEmpty()) {
						continue;
					}

					if (!var2.allowOverflow || TileLoader.access$400() <= 0) {
						TileLoader.access$300(var2).clear();
						break;
					}

					TileLoader.access$502(false);
					String var3 = var2.mapName + "_overflow" + TileLoader.access$604(var2);
					TextureMap var4 = new TextureMap(2, var3, "not_used", TileLoader.access$800());
					var4.refreshTextures();

					if (TileLoader.access$500()) {
						TileLoader.access$100().add(var4);
						break;
					}
				}

				TileLoader.access$002(false);
				return;
			}
		}
	}

	public void afterChange2() {
		Iterator var1 = TileLoader.access$200().iterator();

		while (var1.hasNext()) {
			TileLoader var2 = (TileLoader)var1.next();
			var2.finish();
		}
	}
}
