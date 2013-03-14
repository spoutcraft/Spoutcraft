package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.mod.CustomAnimation$1;
import java.util.Comparator;

class CustomAnimation$1$1 implements Comparator {
	final CustomAnimation$1 this$0;

	CustomAnimation$1$1(CustomAnimation$1 var1) {
		this.this$0 = var1;
	}

	public int compare(CustomAnimation var1, CustomAnimation var2) {
		return CustomAnimation.access$300(var1).compareTo(CustomAnimation.access$300(var2));
	}

	public int compare(Object var1, Object var2) {
		return this.compare((CustomAnimation)var1, (CustomAnimation)var2);
	}
}
