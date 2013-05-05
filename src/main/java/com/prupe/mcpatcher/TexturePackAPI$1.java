package com.prupe.mcpatcher;

import java.util.Comparator;

final class TexturePackAPI$1 implements Comparator {
	public int compare(String var1, String var2) {
		String var3 = var1.replaceAll(".*/", "").replaceFirst("\\.properties", "");
		String var4 = var2.replaceAll(".*/", "").replaceFirst("\\.properties", "");
		int var5 = var3.compareTo(var4);
		return var5 != 0 ? var5 : var1.compareTo(var2);
	}

	public int compare(Object var1, Object var2) {
		return this.compare((String)var1, (String)var2);
	}
}
