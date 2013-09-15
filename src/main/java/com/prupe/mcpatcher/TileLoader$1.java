package com.prupe.mcpatcher;

import java.io.IOException;
import java.util.Iterator;

import net.minecraft.src.Tessellator;
import net.minecraft.src.TextureMap;

final class TileLoader$1 extends TexturePackChangeHandler {
	TileLoader$1(String x0, int x1) {
		super(x0, x1);
	}

	public void initialize() {}

	public void beforeChange() {
		TileLoader.access$002(true);
		TessellatorUtils.clear(Tessellator.instance);
		Iterator i$ = TileLoader.access$100().iterator();

		while (i$.hasNext()) {
			TextureMap textureMap = (TextureMap)i$.next();

			try {
				textureMap.unloadGLTexture();
			} catch (Throwable var4) {
				var4.printStackTrace();
			}
		}

		TileLoader.access$100().clear();
		TileLoader.access$200().clear();
		TileLoader.access$300().clear();
	}

	public void afterChange() {
		while (true) {
			Iterator i$ = TileLoader.access$200().iterator();

			while (true) {
				if (i$.hasNext()) {
					TileLoader loader = (TileLoader)i$.next();

					if (TileLoader.access$400(loader).isEmpty()) {
						continue;
					}

					if (!loader.allowOverflow || TileLoader.access$500() <= 0) {
						loader.subLogger.warning("could not load all %s tiles (%d remaining)", new Object[] {loader.mapName, Integer.valueOf(TileLoader.access$400(loader).size())});
						TileLoader.access$400(loader).clear();
						break;
					}

					TileLoader.access$602(false);
					String mapName = loader.mapName + "_overflow" + TileLoader.access$704(loader);
					TileLoader.access$800().fine("new TextureAtlas(%s)", new Object[] {mapName});
					TextureMap map = new TextureMap(2, mapName);
					try {
						map.func_110551_a(TexturePackAPI.getResourceManager());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (TileLoader.access$600()) {
						TileLoader.access$100().add(map);
						break;
					}

					TileLoader.access$800().severe("TileLoader.registerIcons was never called!  Possible conflict in TextureAtlas.class", new Object[0]);
				}

				TileLoader.access$002(false);
				return;
			}
		}
	}

	public void afterChange2() {
		Iterator i$ = TileLoader.access$200().iterator();

		while (i$.hasNext()) {
			TileLoader loader = (TileLoader)i$.next();
			loader.finish();
		}
	}
}
