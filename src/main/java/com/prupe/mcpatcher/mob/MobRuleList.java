package com.prupe.mcpatcher.mob;

import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.TexturePackAPI;
import com.prupe.mcpatcher.mob.MobRuleList$MobRuleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import net.minecraft.src.ResourceLocation;

class MobRuleList {
	private static final MCLogger logger = MCLogger.getLogger("Random Mobs");
	public static final String ALTERNATIVES_REGEX = "_(eyes|overlay|tame|angry|collar|fur|invulnerable|shooting)\\.properties$";
	private static final Map<ResourceLocation, MobRuleList> allRules = new HashMap();
	private final ResourceLocation baseSkin;
	private final List<ResourceLocation> allSkins;
	private final int skinCount;
	private final List<MobRuleList$MobRuleEntry> entries;

	private MobRuleList(ResourceLocation baseSkin) {
		this.baseSkin = baseSkin;
		String newPath = baseSkin.getResourcePath().replaceFirst("^textures/entity/", "mcpatcher/mob/");
		ResourceLocation newSkin = new ResourceLocation(baseSkin.getResourceDomain(), newPath);
		this.allSkins = new ArrayList();
		this.allSkins.add(baseSkin);
		int filename = 2;

		while (true) {
			ResourceLocation altFilename = TexturePackAPI.transformResourceLocation(newSkin, ".png", filename + ".png");

			if (!TexturePackAPI.hasResource(altFilename)) {
				this.skinCount = this.allSkins.size();

				if (this.skinCount <= 1) {
					this.entries = null;
					return;
				} else {
					logger.fine("found %d variations for %s", new Object[] {Integer.valueOf(this.skinCount), baseSkin});
					ResourceLocation var10 = TexturePackAPI.transformResourceLocation(newSkin, ".png", ".properties");
					altFilename = new ResourceLocation(newSkin.getResourceDomain(), var10.getResourcePath().replaceFirst("_(eyes|overlay|tame|angry|collar|fur|invulnerable|shooting)\\.properties$", ".properties"));
					Properties properties = TexturePackAPI.getProperties(var10);

					if (properties == null && !var10.equals(altFilename)) {
						properties = TexturePackAPI.getProperties(altFilename);

						if (properties != null) {
							logger.fine("using %s for %s", new Object[] {altFilename, baseSkin});
						}
					}

					ArrayList tmpEntries = new ArrayList();

					if (properties != null) {
						int i = 0;

						while (true) {
							MobRuleList$MobRuleEntry entry = MobRuleList$MobRuleEntry.load(properties, i, this.skinCount);

							if (entry == null) {
								if (i > 0) {
									break;
								}
							} else {
								logger.fine("  %s", new Object[] {entry.toString()});
								tmpEntries.add(entry);
							}

							++i;
						}
					}

					this.entries = tmpEntries.isEmpty() ? null : tmpEntries;
					return;
				}
			}

			this.allSkins.add(altFilename);
			++filename;
		}
	}

	ResourceLocation getSkin(long key, int i, int j, int k, String biome) {
		if (this.entries == null) {
			int i$1 = (int)(key % (long)this.skinCount);

			if (i$1 < 0) {
				i$1 += this.skinCount;
			}

			return (ResourceLocation)this.allSkins.get(i$1);
		} else {
			Iterator i$ = this.entries.iterator();
			MobRuleList$MobRuleEntry entry;

			do {
				if (!i$.hasNext()) {
					return this.baseSkin;
				}

				entry = (MobRuleList$MobRuleEntry)i$.next();
			} while (!entry.match(i, j, k, biome));

			int index = entry.weightedIndex.choose(key);
			return (ResourceLocation)this.allSkins.get(entry.skins[index]);
		}
	}

	static MobRuleList get(ResourceLocation texture) {
		MobRuleList list = (MobRuleList)allRules.get(texture);

		if (list == null) {
			list = new MobRuleList(texture);
			allRules.put(texture, list);
		}

		return list;
	}

	static void clear() {
		allRules.clear();
	}
}
