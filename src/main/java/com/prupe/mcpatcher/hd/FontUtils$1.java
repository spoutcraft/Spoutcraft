package com.prupe.mcpatcher.hd;

import com.prupe.mcpatcher.TexturePackChangeHandler;
import java.util.Iterator;
import net.minecraft.src.FontRenderer;

final class FontUtils$1 extends TexturePackChangeHandler {
	FontUtils$1(String x0, int x1) {
		super(x0, x1);
	}

	public void initialize() {}

	public void beforeChange() {}

	public void afterChange() {
		Iterator i$ = FontUtils.access$000().iterator();

		while (i$.hasNext()) {
			FontRenderer renderer = (FontRenderer)i$.next();
			renderer.readFontTexture();
		}
	}
}
