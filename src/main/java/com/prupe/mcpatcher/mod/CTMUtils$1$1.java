package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.mod.CTMUtils$1;
import java.util.Comparator;

class CTMUtils$1$1 implements Comparator {
	final CTMUtils$1 this$0;

	CTMUtils$1$1(CTMUtils$1 var1) {
		this.this$0 = var1;
	}

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
