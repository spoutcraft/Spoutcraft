package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.WeightedIndex;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;

class MobRuleList$MobRuleEntry {
	final int[] skins;
	final WeightedIndex weightedIndex;
	private final HashSet biomes;
	private final int minHeight;
	private final int maxHeight;

	static MobRuleList$MobRuleEntry load(Properties var0, int var1, int var2) {
		String var3 = var0.getProperty("skins." + var1, "").trim().toLowerCase();
		int[] var4;
		int var5;

		if (!var3.equals("*") && !var3.equals("all") && !var3.equals("any")) {
			var4 = MCPatcherUtils.parseIntegerList(var3, 1, var2);

			if (var4.length <= 0) {
				return null;
			}

			for (var5 = 0; var5 < var4.length; ++var5) {
				--var4[var5];
			}
		} else {
			var4 = new int[var2];

			for (var5 = 0; var5 < var4.length; var4[var5] = var5++) {
				;
			}
		}

		WeightedIndex var10 = WeightedIndex.create(var4.length, var0.getProperty("weights." + var1, ""));

		if (var10 == null) {
			return null;
		} else {
			HashSet var6 = new HashSet();
			String var7 = var0.getProperty("biomes." + var1, "").trim().toLowerCase();

			if (!var7.equals("")) {
				Collections.addAll(var6, var7.split("\\s+"));
			}

			if (var6.isEmpty()) {
				var6 = null;
			}

			int var8 = MCPatcherUtils.getIntProperty(var0, "minHeight." + var1, -1);
			int var9 = MCPatcherUtils.getIntProperty(var0, "maxHeight." + var1, Integer.MAX_VALUE);

			if (var8 < 0 || var8 > var9) {
				var8 = -1;
				var9 = Integer.MAX_VALUE;
			}

			return new MobRuleList$MobRuleEntry(var4, var10, var6, var8, var9);
		}
	}

	MobRuleList$MobRuleEntry(int[] var1, WeightedIndex var2, HashSet var3, int var4, int var5) {
		this.skins = var1;
		this.weightedIndex = var2;
		this.biomes = var3;
		this.minHeight = var4;
		this.maxHeight = var5;
	}

	boolean match(int var1, int var2, int var3, String var4) {
		return this.biomes != null && !this.biomes.contains(var4) ? false : this.minHeight < 0 || var2 >= this.minHeight && var2 <= this.maxHeight;
	}

	public String toString() {
		StringBuilder var1 = new StringBuilder();
		var1.append("skins:");
		int[] var2 = this.skins;
		int var3 = var2.length;

		for (int var4 = 0; var4 < var3; ++var4) {
			int var5 = var2[var4];
			var1.append(' ').append(var5 + 1);
		}

		if (this.biomes != null) {
			var1.append(", biomes:");
			Iterator var6 = this.biomes.iterator();

			while (var6.hasNext()) {
				String var7 = (String)var6.next();
				var1.append(' ').append(var7);
			}
		}

		if (this.minHeight >= 0) {
			var1.append(", height: ").append(this.minHeight).append('-').append(this.maxHeight);
		}

		var1.append(", weights: ").append(this.weightedIndex.toString());
		return var1.toString();
	}
}
