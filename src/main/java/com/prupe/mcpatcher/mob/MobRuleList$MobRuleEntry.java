package com.prupe.mcpatcher.mob;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.WeightedIndex;

class MobRuleList$MobRuleEntry {
	final int[] skins;
	final WeightedIndex weightedIndex;
	private final Set<String> biomes;
	private final int minHeight;
	private final int maxHeight;

	static MobRuleList$MobRuleEntry load(Properties properties, int index, int limit) {
		String skinList = properties.getProperty("skins." + index, "").trim().toLowerCase();
		int[] skins;
		int chooser;

		if (!skinList.equals("*") && !skinList.equals("all") && !skinList.equals("any")) {
			skins = MCPatcherUtils.parseIntegerList(skinList, 1, limit);

			if (skins.length <= 0) {
				return null;
			}

			for (chooser = 0; chooser < skins.length; ++chooser) {
				--skins[chooser];
			}
		} else {
			skins = new int[limit];

			for (chooser = 0; chooser < skins.length; skins[chooser] = chooser++) {
				;
			}
		}

		WeightedIndex var10 = WeightedIndex.create(skins.length, properties.getProperty("weights." + index, ""));

		if (var10 == null) {
			return null;
		} else {
			HashSet biomes = new HashSet();
			String biomeList = properties.getProperty("biomes." + index, "").trim().toLowerCase();

			if (!biomeList.equals("")) {
				Collections.addAll(biomes, biomeList.split("\\s+"));
			}

			if (biomes.isEmpty()) {
				biomes = null;
			}

			int minHeight = MCPatcherUtils.getIntProperty(properties, "minHeight." + index, -1);
			int maxHeight = MCPatcherUtils.getIntProperty(properties, "maxHeight." + index, Integer.MAX_VALUE);

			if (minHeight < 0 || minHeight > maxHeight) {
				minHeight = -1;
				maxHeight = Integer.MAX_VALUE;
			}

			return new MobRuleList$MobRuleEntry(skins, var10, biomes, minHeight, maxHeight);
		}
	}

	MobRuleList$MobRuleEntry(int[] skins, WeightedIndex weightedIndex, HashSet<String> biomes, int minHeight, int maxHeight) {
		this.skins = skins;
		this.weightedIndex = weightedIndex;
		this.biomes = biomes;
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
	}

	boolean match(int i, int j, int k, String biome) {
		return this.biomes != null && !this.biomes.contains(biome) ? false : this.minHeight < 0 || j >= this.minHeight && j <= this.maxHeight;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("skins:");
		int[] i$ = this.skins;
		int s = i$.length;

		for (int i$1 = 0; i$1 < s; ++i$1) {
			int i = i$[i$1];
			sb.append(' ').append(i + 1);
		}

		if (this.biomes != null) {
			sb.append(", biomes:");
			Iterator var6 = this.biomes.iterator();

			while (var6.hasNext()) {
				String var7 = (String)var6.next();
				sb.append(' ').append(var7);
			}
		}

		if (this.minHeight >= 0) {
			sb.append(", height: ").append(this.minHeight).append('-').append(this.maxHeight);
		}

		sb.append(", weights: ").append(this.weightedIndex.toString());
		return sb.toString();
	}
}
