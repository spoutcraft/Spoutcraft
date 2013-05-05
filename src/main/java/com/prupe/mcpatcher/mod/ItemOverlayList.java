package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.mod.ItemOverlayList$AverageGroup;
import com.prupe.mcpatcher.mod.ItemOverlayList$CycleGroup;
import com.prupe.mcpatcher.mod.ItemOverlayList$Entry;
import com.prupe.mcpatcher.mod.ItemOverlayList$Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.src.ItemStack;

class ItemOverlayList {
	private static final float PI = (float)Math.PI;
	private final Map groups = new HashMap();
	private final List entries = new ArrayList();

	ItemOverlayList(ItemOverride[][] var1, ItemStack var2) {
		int[] var3 = CITUtils.getEnchantmentLevels(var2.stackTagCompound);
		int var4 = var2.itemID;

		if (var4 >= 0 && var4 < var1.length && var1[var4] != null) {
			ItemOverride[] var5 = var1[var4];
			int var6 = var5.length;

			for (int var7 = 0; var7 < var6; ++var7) {
				ItemOverride var8 = var5[var7];

				if (var8.match(var4, var2, var3)) {
					int var9 = Math.max(var8.lastEnchantmentLevel, 1);
					ItemOverlayList$Entry var10 = this.getGroup(var8.overlay).add(var8.overlay, var9);
					this.entries.add(var10);
				}
			}
		}

		Iterator var11 = this.groups.values().iterator();

		while (var11.hasNext()) {
			ItemOverlayList$Group var12 = (ItemOverlayList$Group)var11.next();
			var12.computeIntensities();
		}
	}

	boolean isEmpty() {
		return this.groups.isEmpty();
	}

	int size() {
		return this.entries.size();
	}

	ItemOverlay getOverlay(int var1) {
		return ((ItemOverlayList$Entry)this.entries.get(var1)).overlay;
	}

	float getIntensity(int var1) {
		return ((ItemOverlayList$Entry)this.entries.get(var1)).intensity;
	}

	private ItemOverlayList$Group getGroup(ItemOverlay var1) {
		Object var2 = (ItemOverlayList$Group)this.groups.get(Integer.valueOf(var1.groupID));

		if (var2 == null) {
			switch (var1.applyMethod) {
				case 0:
				case 2:
					var2 = new ItemOverlayList$AverageGroup(var1);
					break;

				case 1:
					var2 = new ItemOverlayList$CycleGroup(var1);
			}

			this.groups.put(Integer.valueOf(var1.groupID), var2);
		}

		return (ItemOverlayList$Group)var2;
	}
}
