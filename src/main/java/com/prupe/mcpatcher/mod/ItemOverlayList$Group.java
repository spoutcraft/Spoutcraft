package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.mod.ItemOverlayList$Entry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

abstract class ItemOverlayList$Group {
	final List entries = new ArrayList();
	final int method;
	final int limit;

	ItemOverlayList$Group(ItemOverlay var1) {
		this.method = var1.applyMethod;
		this.limit = var1.limit;
	}

	ItemOverlayList$Entry add(ItemOverlay var1, int var2) {
		ItemOverlayList$Entry var3 = new ItemOverlayList$Entry(var1, var2);
		this.entries.add(var3);
		return var3;
	}

	void computeIntensities() {
		ItemOverlayList$Entry var2;

		for (Iterator var1 = this.entries.iterator(); var1.hasNext(); var2.intensity = 0.0F) {
			var2 = (ItemOverlayList$Entry)var1.next();
		}
	}
}
