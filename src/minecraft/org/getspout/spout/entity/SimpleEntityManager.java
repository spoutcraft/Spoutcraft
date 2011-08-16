package org.getspout.spout.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;

public class SimpleEntityManager implements EntityManager {
	private ArrayList<Entity> newEntities = new ArrayList<Entity>();
	private HashMap<Integer,Entity> entities = new HashMap<Integer,Entity>();
	@Override
	public void registerEntity(Entity entity) {
		// Entity id hasn't been set yet
		newEntities.add(entity);
	}
	
	@Override
	public boolean unregisterEntity(Entity entity) {
		
		return removeEntity(entity) != null;
	}
	
	private Entity removeEntity(Entity entity) {
		
		int id = entity.entityId;
		
		Entity e1 = entities.get(id);
		
		if (e1 == entity) {
			return entities.remove(id);
		}
		
		Iterator<Entity> i = newEntities.iterator();
		while(i.hasNext()) {
			Entity next = i.next();
			if(next == entity) {
				i.remove();
				return next;
			}
		}

		return null;
		
	}

	@Override
	public Entity getEntityFromId(int id) {
		Entity e1 = entities.get(id);
		
		if (e1 != null) {
			return e1;
		}
		
		
		Iterator<Entity> i = newEntities.iterator();
		while(i.hasNext()) {
			Entity next = i.next();
			if (next.entityId == id) {
				i.remove();
				entities.put(id, next);
				return next;
			}
		}
		
		return null;
		
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
