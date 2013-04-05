package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.TexturePackAPI;
import com.prupe.mcpatcher.mod.MobRuleList$MobRuleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

class MobRuleList {
	public static final String ALTERNATIVES_REGEX = "_(eyes|overlay|tame|angry|collar|fur|invul)\\.properties$";
	private static final HashMap allRules = new HashMap();
	private final String baseSkin;
	private final ArrayList allSkins;
	private final int skinCount;
	private final ArrayList entries;

	MobRuleList(String var1) {
		this.baseSkin = var1;
		this.allSkins = new ArrayList();
		this.allSkins.add(var1);
		int var2 = 2;

		while (true) {
			String var3 = var1.replace(".png", "" + var2 + ".png");

			if (!TexturePackAPI.hasResource(var3)) {
				this.skinCount = this.allSkins.size();

				if (this.skinCount <= 1) {
					this.entries = null;
					return;
				} else {
					String var8 = var1.replace(".png", ".properties");
					var3 = var8.replaceFirst("_(eyes|overlay|tame|angry|collar|fur|invul)\\.properties$", ".properties");
					Properties var4 = TexturePackAPI.getProperties(var8);

					if (var4 == null && !var8.equals(var3)) {
						var4 = TexturePackAPI.getProperties(var3);
					}

					ArrayList var5 = new ArrayList();

					if (var4 != null) {
						int var6 = 0;

						while (true) {
							MobRuleList$MobRuleEntry var7 = MobRuleList$MobRuleEntry.load(var4, var6, this.skinCount);

							if (var7 == null) {
								if (var6 > 0) {
									break;
								}
							} else {
								var5.add(var7);
							}

							++var6;
						}
					}

					this.entries = var5.isEmpty() ? null : var5;
					return;
				}
			}

			this.allSkins.add(var3);
			++var2;
		}
	}

	String getSkin(long var1, int var3, int var4, int var5, String var6) {
		if (this.entries == null) {
			int var10 = (int)(var1 % (long)this.skinCount);

			if (var10 < 0) {
				var10 += this.skinCount;
			}

			return (String)this.allSkins.get(var10);
		} else {
			Iterator var7 = this.entries.iterator();
			MobRuleList$MobRuleEntry var8;

			do {
				if (!var7.hasNext()) {
					return this.baseSkin;
				}

				var8 = (MobRuleList$MobRuleEntry)var7.next();
			} while (!var8.match(var3, var4, var5, var6));

			int var9 = var8.weightedIndex.choose(var1);
			return (String)this.allSkins.get(var8.skins[var9]);
		}
	}

	static MobRuleList get(String var0) {
		MobRuleList var1 = (MobRuleList)allRules.get(var0);

		if (var1 == null) {
			var1 = new MobRuleList(var0);
			allRules.put(var0, var1);
		}

		return var1;
	}

	static void clear() {
		allRules.clear();
	}
}
