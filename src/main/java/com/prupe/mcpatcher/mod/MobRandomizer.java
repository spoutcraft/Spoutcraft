package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.TexturePackChangeHandler;
import com.prupe.mcpatcher.mod.MobRandomizer$1;
import com.prupe.mcpatcher.mod.MobRandomizer$ExtraInfo;
import java.util.LinkedHashMap;
import net.minecraft.src.EntityLiving;

public class MobRandomizer {
	private static final LinkedHashMap cache = new LinkedHashMap();

	public static String randomTexture(EntityLiving var0) {
		return randomTexture(var0, var0.getTexture());
	}

	public static String randomTexture(EntityLiving var0, String var1) {
		if (var1 != null && var1.startsWith("/mob/") && var1.endsWith(".png")) {
			String var2 = var1 + ":" + var0.entityId;
			String var3 = (String)cache.get(var2);

			if (var3 == null) {
				MobRandomizer$ExtraInfo var4 = MobRandomizer$ExtraInfo.getInfo(var0);
				MobRuleList var5 = MobRuleList.get(var1);
				var3 = var5.getSkin(MobRandomizer$ExtraInfo.access$100(var4), MobRandomizer$ExtraInfo.access$200(var4), MobRandomizer$ExtraInfo.access$300(var4), MobRandomizer$ExtraInfo.access$400(var4), MobRandomizer$ExtraInfo.access$500(var4));
				cache.put(var2, var3);

				if (cache.size() > 250) {
					while (cache.size() > 200) {
						cache.remove(cache.keySet().iterator().next());
					}
				}
			}

			return var3;
		} else {
			return var1;
		}
	}

	public static String randomTexture(Object var0, String var1) {
		return var0 instanceof EntityLiving ? randomTexture((EntityLiving)var0, var1) : var1;
	}

	static LinkedHashMap access$000() {
		return cache;
	}

	static {
		TexturePackChangeHandler.register(new MobRandomizer$1("Random Mobs", 2));
	}
}
