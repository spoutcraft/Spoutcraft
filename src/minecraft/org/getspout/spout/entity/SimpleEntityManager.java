package org.getspout.spout.entity;

import java.util.HashMap;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;

public class SimpleEntityManager implements EntityManager {
	private HashMap<Integer,Entity> entities = new HashMap<Integer,Entity>();
	@Override
	public void registerEntity(Entity entity) {
		entities.put(entity.entityId, entity);
	}
	
	@Override
	public boolean unregisterEntity(Entity entity) {
		return entities.remove(entity.entityId) != null;
	}

	@Override
	public Entity getEntityFromId(int id) {
		return entities.get(id);
	}

	@Override
	public void setTexture(int id, String texture) {
		Entity e = getEntityFromId(id);
		if(e instanceof EntityLiving){
			EntityLiving el = (EntityLiving)e;
			el.setCustomTexture(texture);
		}
	}

	@Override
	public void setAlternateTexture(int id, String texture) {
		Entity e = getEntityFromId(id);
		if(e instanceof EntityLiving){
			EntityLiving el = (EntityLiving)e;
			el.setAlternateCustomTexture(texture);
		}
	}

}
