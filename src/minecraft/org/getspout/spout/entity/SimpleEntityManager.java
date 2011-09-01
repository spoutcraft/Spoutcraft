/*
 * This file is part of Spoutcraft (http://wiki.getspout.org/).
 * 
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
	public void setTexture(int id, String texture, byte textureId) {
		Entity e = getEntityFromId(id);
		if(e instanceof EntityLiving){
			EntityLiving el = (EntityLiving)e;
			el.setCustomTexture(texture, textureId);
		}
	}

}
