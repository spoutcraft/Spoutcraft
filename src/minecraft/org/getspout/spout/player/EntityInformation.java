package org.getspout.spout.player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;

/**
 * A repository of voltaile information that should not be lost even if the entity is removed.
 */
public class EntityInformation {
	private static Map<UUID, EntityInformation> players = new HashMap<UUID, EntityInformation>();
	
	private EntityInformation(UUID id) {
		
	}
	
	public static EntityInformation getInformation(UUID id) {
		EntityInformation info = players.get(id);
		if (info == null) {
			info = new EntityInformation(id);
			players.put(id, info);
		}
		return info;
	}
	
	public static Entity getEntityFromId(UUID id) {
		Entity result = null;
		if (Minecraft.theMinecraft.theWorld != null) {
			List<Entity> entities = Minecraft.theMinecraft.theWorld.loadedEntityList;
			for (Entity e : entities) {
				if (e.uniqueId.equals(id)) {
					result = e;
					break;
				}
			}
		}
		return result;
	}
}
