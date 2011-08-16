package org.getspout.spout.entity;

import net.minecraft.src.Entity;

public interface EntityManager {
	public void registerEntity(Entity entity);
	
	public boolean unregisterEntity(Entity entity);
	
	public Entity getEntityFromId(int id);
	
	public void setTexture(int id, String texture);

	public void setAlternateTexture(int id, String texture);
}
