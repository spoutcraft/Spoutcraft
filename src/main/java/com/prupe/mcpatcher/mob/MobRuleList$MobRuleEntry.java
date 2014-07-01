package com.prupe.mcpatcher.mob;

import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.WeightedIndex;
import java.lang.reflect.Method;
import java.util.BitSet;
import java.util.Properties;

class MobRuleList$MobRuleEntry {
	final int[] skins;
	final WeightedIndex weightedIndex;
	private final BitSet biomes;
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

		WeightedIndex var11 = WeightedIndex.create(skins.length, properties.getProperty("weights." + index, ""));

		if (var11 == null) {
			return null;
		} else {
			String biomeList = MCPatcherUtils.getStringProperty(properties, "biomes." + index, "");
			BitSet biomes;

			if (biomeList.isEmpty()) {
				biomes = null;
			} else {
				biomes = new BitSet();

				if (MobRuleList.access$000() != null) {
					try {
						MobRuleList.access$000().invoke((Object)null, new Object[] {biomeList, biomes});
					} catch (Throwable var10) {
						var10.printStackTrace();
						MobRuleList.access$002((Method)null);
					}
				}
			}

			int minHeight = MCPatcherUtils.getIntProperty(properties, "minHeight." + index, -1);
			int maxHeight = MCPatcherUtils.getIntProperty(properties, "maxHeight." + index, Integer.MAX_VALUE);

			if (minHeight < 0 || minHeight > maxHeight) {
				minHeight = -1;
				maxHeight = Integer.MAX_VALUE;
			}

			return new MobRuleList$MobRuleEntry(skins, var11, biomes, minHeight, maxHeight);
		}
	}

	MobRuleList$MobRuleEntry(int[] skins, WeightedIndex weightedIndex, BitSet biomes, int minHeight, int maxHeight) {
		this.skins = skins;
		this.weightedIndex = weightedIndex;
		this.biomes = biomes;
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
	}

	boolean match(int i, int j, int k, Integer biome) {
		return this.biomes != null && (biome == null || !this.biomes.get(biome.intValue())) ? false : this.minHeight < 0 || j >= this.minHeight && j <= this.maxHeight;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("skins:");
		int[] i = this.skins;
		int len$ = i.length;

		for (int i$ = 0; i$ < len$; ++i$) {
			int i1 = i[i$];
			sb.append(' ').append(i1 + 1);
		}

		if (this.biomes != null) {
			sb.append(", biomes:");

			for (int var6 = this.biomes.nextSetBit(0); var6 >= 0; var6 = this.biomes.nextSetBit(var6 + 1)) {
				sb.append(' ').append(var6);
			}
		}

		if (this.minHeight >= 0) {
			sb.append(", height: ").append(this.minHeight).append('-').append(this.maxHeight);
		}

		sb.append(", weights: ").append(this.weightedIndex.toString());
		return sb.toString();
	}
}
