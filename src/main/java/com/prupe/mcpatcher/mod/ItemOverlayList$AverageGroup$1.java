package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.mod.ItemOverlayList$AverageGroup;
import com.prupe.mcpatcher.mod.ItemOverlayList$Entry;
import java.util.Comparator;

class ItemOverlayList$AverageGroup$1 implements Comparator {
	final ItemOverlayList$AverageGroup this$0;

	ItemOverlayList$AverageGroup$1(ItemOverlayList$AverageGroup var1) {
		this.this$0 = var1;
	}

	public int compare(ItemOverlayList$Entry var1, ItemOverlayList$Entry var2) {
		int var3 = var2.overlay.weight - var1.overlay.weight;
		return var3 != 0 ? var3 : var2.level - var1.level;
	}

	public int compare(Object var1, Object var2) {
		return this.compare((ItemOverlayList$Entry)var1, (ItemOverlayList$Entry)var2);
	}
}
