package org.getspout.spout.entity;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;

public class SimpleEntityManager implements EntityManager {
	private ArrayList<Entity> newEntities = new ArrayList<Entity>();
	private HashMap<Integer,Entity> entities = new HashMap<Integer,Entity>();
	@Override
	public void registerEntity(Entity entity) {
		// Entity id hasn't been set yet so has to be placed in temp storage
		newEntities.add(entity);
	}
	
	@Override
	public boolean unregisterEntity(Entity entity) {
		transferEntitiesToHashMap();
		return entities.remove(entity.entityId) != null;
	}
	
	@Override
	public Entity getEntityFromId(int id) {
		transferEntitiesToHashMap();
		return entities.get(id);
	}
	
	private void transferEntitiesToHashMap() {
		for (Entity e : newEntities) {
			entities.put(e.entityId, e);
		}
		newEntities.clear();
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
