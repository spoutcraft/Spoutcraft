package com.prupe.mcpatcher.mob;

import java.util.LinkedHashMap;

import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.TexturePackChangeHandler;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLivingBase;
import net.minecraft.src.ResourceLocation;

public class MobRandomizer {
	private static final MCLogger logger = MCLogger.getLogger("Random Mobs");
	private static final LinkedHashMap<String, ResourceLocation> cache = new LinkedHashMap();

	static void init() {}

	public static ResourceLocation randomTexture(EntityLivingBase entity, ResourceLocation texture) {
		if (texture != null && texture.func_110623_a().endsWith(".png")) {
			String key = texture.toString() + ":" + entity.entityId;
			ResourceLocation newTexture = (ResourceLocation)cache.get(key);

			if (newTexture == null) {
				MobRandomizer$ExtraInfo info = MobRandomizer$ExtraInfo.getInfo(entity);
				MobRuleList list = MobRuleList.get(texture);
				newTexture = list.getSkin(MobRandomizer$ExtraInfo.access$100(info), MobRandomizer$ExtraInfo.access$200(info), MobRandomizer$ExtraInfo.access$300(info), MobRandomizer$ExtraInfo.access$400(info), MobRandomizer$ExtraInfo.access$500(info));
				cache.put(key, newTexture);
				logger.finer("entity %s using %s (cache: %d)", new Object[] {entity, newTexture, Integer.valueOf(cache.size())});

				if (cache.size() > 250) {
					while (cache.size() > 200) {
						cache.remove(cache.keySet().iterator().next());
					}
				}
			}

			return newTexture;
		} else {
			return texture;
		}
	}

	public static ResourceLocation randomTexture(Entity entity, ResourceLocation texture) {
		return entity instanceof EntityLivingBase ? randomTexture((EntityLivingBase)entity, texture) : texture;
	}

	static LinkedHashMap access$000() {
		return cache;
	}

	static MCLogger access$600() {
		return logger;
	}

	static {
		TexturePackChangeHandler.register(new MobRandomizer$1("Random Mobs", 2));
	}
}
