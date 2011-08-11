package org.getspout.spout.entity;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;

public class SimpleEntityManager implements EntityManager {
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	@Override
	public void registerEntity(Entity entity) {
		entities.add(entity);
	}

	@Override
	public Entity getEntityFromId(int id) {
		for(Entity e: entities)
		{
			if(e.entityId == id){
				return e;
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
