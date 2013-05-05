package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.mod.ItemOverlayList$AverageGroup$1;
import com.prupe.mcpatcher.mod.ItemOverlayList$Entry;
import com.prupe.mcpatcher.mod.ItemOverlayList$Group;
import java.util.Collections;
import java.util.Iterator;

class ItemOverlayList$AverageGroup extends ItemOverlayList$Group {
	int total;

	ItemOverlayList$AverageGroup(ItemOverlay var1) {
		super(var1);
	}

	void computeIntensities() {
		if (this.limit < this.entries.size()) {
			Collections.sort(this.entries, new ItemOverlayList$AverageGroup$1(this));

			while (this.entries.size() > this.limit) {
				this.entries.remove(this.limit);
			}
		}

		this.total = 0;
		Iterator var1 = this.entries.iterator();
		ItemOverlayList$Entry var2;

		while (var1.hasNext()) {
			var2 = (ItemOverlayList$Entry)var1.next();

			if (this.method == 0) {
				this.total += var2.level;
			} else {
				this.total = Math.max(this.total, var2.level);
			}
		}

		if (this.total > 0) {
			for (var1 = this.entries.iterator(); var1.hasNext(); var2.intensity = (float)var2.level / (float)this.total) {
				var2 = (ItemOverlayList$Entry)var1.next();
			}
		}
	}
}
