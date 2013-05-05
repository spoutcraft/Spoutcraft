package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.mod.ItemOverlayList$Entry;
import com.prupe.mcpatcher.mod.ItemOverlayList$Group;
import java.util.Iterator;

class ItemOverlayList$CycleGroup extends ItemOverlayList$Group {
	float total;

	ItemOverlayList$CycleGroup(ItemOverlay var1) {
		super(var1);
	}

	ItemOverlayList$Entry add(ItemOverlay var1, int var2) {
		ItemOverlayList$Entry var3 = super.add(var1, var2);
		var3.start = this.total;
		this.total += var1.duration;
		return var3;
	}

	void computeIntensities() {
		if (this.total > 0.0F) {
			float var1 = (float)((double)System.currentTimeMillis() / 1000.0D % (double)this.total);
			float var4;

			for (Iterator var2 = this.entries.iterator(); var2.hasNext(); var1 -= var4) {
				ItemOverlayList$Entry var3 = (ItemOverlayList$Entry)var2.next();

				if (var1 <= 0.0F) {
					break;
				}

				var4 = var3.overlay.duration;

				if (var1 < var4) {
					var3.intensity = (float)Math.sin((double)((float)Math.PI * var1 / var4));
				}
			}
		}
	}
}
