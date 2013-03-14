package com.prupe.mcpatcher;

import java.util.Comparator;

final class TexturePackChangeHandler$1 implements Comparator {
	public int compare(TexturePackChangeHandler var1, TexturePackChangeHandler var2) {
		return var1.order - var2.order;
	}

	public int compare(Object var1, Object var2) {
		return this.compare((TexturePackChangeHandler)var1, (TexturePackChangeHandler)var2);
	}
}
