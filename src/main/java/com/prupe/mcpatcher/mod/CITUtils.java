package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.TexturePackAPI;
import com.prupe.mcpatcher.mod.CITUtils$ItemOverride;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.src.Icon;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Stitcher;
import net.minecraft.src.TextureMap;

public class CITUtils {
	private static final MCLogger logger = MCLogger.getLogger("Custom Item Textures", "CIT");
	private static TileLoader tileLoader;
	private static CITUtils$ItemOverride[][] overrides = new CITUtils$ItemOverride[Item.itemsList.length][];

	public static Icon getIcon(Icon var0, Item var1, ItemStack var2) {
		int var3 = var1.itemID;

		if (var3 >= 0 && var3 < overrides.length && overrides[var3] != null) {
			CITUtils$ItemOverride[] var4 = overrides[var3];
			int var5 = var4.length;

			for (int var6 = 0; var6 < var5; ++var6) {
				CITUtils$ItemOverride var7 = var4[var6];

				if (var7.match(var0, var3, var2)) {
					return var7.icon;
				}
			}
		}

		return var0;
	}

	static void refresh() {
		tileLoader = new TileLoader(logger);
		Arrays.fill(overrides, (Object)null);
		String[] var0 = TexturePackAPI.listResources("/cit", ".properties");
		int var1 = var0.length;

		for (int var2 = 0; var2 < var1; ++var2) {
			String var3 = var0[var2];
			CITUtils$ItemOverride var4 = CITUtils$ItemOverride.create(var3);

			if (var4 != null) {
				Iterator var5 = var4.itemsIDs.iterator();

				while (var5.hasNext()) {
					int var6 = ((Integer)var5.next()).intValue();
					overrides[var6] = registerOverride(overrides[var6], var4);
					logger.fine("registered %s to item %d (%s)", new Object[] {var4, Integer.valueOf(var6), getItemName(var6)});
				}
			}
		}
	}

	private static CITUtils$ItemOverride[] registerOverride(CITUtils$ItemOverride[] var0, CITUtils$ItemOverride var1) {
		if (var1 != null) {
			if (var0 == null) {
				var0 = new CITUtils$ItemOverride[] {var1};
			} else {
				CITUtils$ItemOverride[] var2 = new CITUtils$ItemOverride[var0.length + 1];
				System.arraycopy(var0, 0, var2, 0, var0.length);
				var2[var0.length] = var1;
				var0 = var2;
			}
		}

		return var0;
	}

	private static String getItemName(int var0) {
		if (var0 >= 0 && var0 < Item.itemsList.length) {
			Item var1 = Item.itemsList[var0];

			if (var1 != null) {
				String var2 = var1.getUnlocalizedName();

				if (var2 != null) {
					return var2;
				}
			}
		}

		return "unknown item " + var0;
	}

	static void registerIcons(TextureMap var0, Stitcher var1, String var2, Map var3) {
		if (var2.equals("items")) {
			CITUtils$ItemOverride[][] var4 = overrides;
			int var5 = var4.length;

			for (int var6 = 0; var6 < var5; ++var6) {
				CITUtils$ItemOverride[] var7 = var4[var6];

				if (var7 != null) {
					CITUtils$ItemOverride[] var8 = var7;
					int var9 = var7.length;

					for (int var10 = 0; var10 < var9; ++var10) {
						CITUtils$ItemOverride var11 = var8[var10];
						var11.registerIcon(var0, var1, var3);
					}
				}
			}

			tileLoader.finish();
		}
	}

	static String access$000(int var0) {
		return getItemName(var0);
	}

	static TileLoader access$100() {
		return tileLoader;
	}

	static MCLogger access$200() {
		return logger;
	}
}