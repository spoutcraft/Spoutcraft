package com.prupe.mcpatcher;

import java.util.Comparator;

final class TexturePackChangeHandler$1 implements Comparator<TexturePackChangeHandler> {
	public int compare(TexturePackChangeHandler o1, TexturePackChangeHandler o2) {
		return o1.order - o2.order;
	}
}
