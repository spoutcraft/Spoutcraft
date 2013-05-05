package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.TexturePackAPI;
import com.prupe.mcpatcher.TexturePackChangeHandler;
import com.prupe.mcpatcher.TileLoader;
import java.util.Arrays;
import java.util.Iterator;
import net.minecraft.src.Icon;
import net.minecraft.src.Item;

final class CITUtils$1 extends TexturePackChangeHandler {
	CITUtils$1(String var1, int var2) {
		super(var1, var2);
	}

	public void beforeChange() {
		CITUtils.tileLoader = new TileLoader("items", false);
		Arrays.fill(CITUtils.access$100(), (Object)null);
		Arrays.fill(CITUtils.access$200(), (Object)null);
		Arrays.fill(CITUtils.access$300(), (Object)null);
		CITUtils.access$402((Icon)null);
		CITUtils.itemNameMap.clear();

		for (CITUtils.LOWEST_ITEM_ID = 256; CITUtils.LOWEST_ITEM_ID < CITUtils.MAX_ITEMS && Item.itemsList[CITUtils.LOWEST_ITEM_ID] == null; ++CITUtils.LOWEST_ITEM_ID) {
			;
		}

		for (CITUtils.HIGHEST_ITEM_ID = CITUtils.MAX_ITEMS - 1; CITUtils.HIGHEST_ITEM_ID >= 0 && Item.itemsList[CITUtils.HIGHEST_ITEM_ID] == null; --CITUtils.HIGHEST_ITEM_ID) {
			;
		}

		if (CITUtils.LOWEST_ITEM_ID <= CITUtils.HIGHEST_ITEM_ID) {
			for (int var1 = CITUtils.LOWEST_ITEM_ID; var1 <= CITUtils.HIGHEST_ITEM_ID; ++var1) {
				Item var2 = Item.itemsList[var1];

				if (var2 != null) {
					String var3 = var2.getUnlocalizedName();

					if (var3 != null) {
						CITUtils.itemNameMap.put(var3, Integer.valueOf(var1));
					}
				}
			}
		}

		if (CITUtils.access$500() || CITUtils.access$600() || CITUtils.access$700()) {
			Iterator var6 = TexturePackAPI.listResources("/cit", ".properties", true, false, true).iterator();

			while (var6.hasNext()) {
				String var7 = (String)var6.next();
				ItemOverride var8 = ItemOverride.create(var7);

				if (var8 != null) {
					ItemOverride[][] var4;

					switch (var8.type) {
						case 0:
							var8.preload(CITUtils.tileLoader);
							var4 = CITUtils.access$100();
							break;

						case 1:
							var4 = CITUtils.access$200();
							break;

						case 2:
							var4 = CITUtils.access$300();
							break;

						default:
							continue;
					}

					int var5;

					if (var8.itemsIDs == null) {
						for (var5 = CITUtils.LOWEST_ITEM_ID; var5 <= CITUtils.HIGHEST_ITEM_ID; ++var5) {
							this.registerOverride(var4, var5, var8);
						}
					} else {
						for (var5 = var8.itemsIDs.nextSetBit(0); var5 >= 0; var5 = var8.itemsIDs.nextSetBit(var5 + 1)) {
							this.registerOverride(var4, var5, var8);
						}
					}
				}
			}
		}
	}

	public void afterChange() {
		ItemOverride[][] var1 = CITUtils.access$100();
		int var2 = var1.length;

		for (int var3 = 0; var3 < var2; ++var3) {
			ItemOverride[] var4 = var1[var3];

			if (var4 != null) {
				ItemOverride[] var5 = var4;
				int var6 = var4.length;

				for (int var7 = 0; var7 < var6; ++var7) {
					ItemOverride var8 = var5[var7];
					var8.registerIcon(CITUtils.tileLoader);
				}
			}
		}
	}

	private void registerOverride(ItemOverride[][] var1, int var2, ItemOverride var3) {
		if (Item.itemsList[var2] != null) {
			var1[var2] = this.registerOverride(var1[var2], var3);
		}
	}

	private ItemOverride[] registerOverride(ItemOverride[] var1, ItemOverride var2) {
		if (var2 != null) {
			if (var1 == null) {
				var1 = new ItemOverride[] {var2};
			} else {
				ItemOverride[] var3 = new ItemOverride[var1.length + 1];
				System.arraycopy(var1, 0, var3, 0, var1.length);
				var3[var1.length] = var2;
				var1 = var3;
			}
		}

		return var1;
	}
}
