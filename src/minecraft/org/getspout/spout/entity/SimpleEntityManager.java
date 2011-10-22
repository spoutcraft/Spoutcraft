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

import java.util.HashMap;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.WorldClient;

public class SimpleEntityManager implements EntityManager {
	private HashMap<UUID, EntityData> entityData = new HashMap<UUID, EntityData>(1000);
	private final EntityData generic = new EmptyEntityData();
	public Entity getEntityFromId(int id) {
		return ((WorldClient) Minecraft.theMinecraft.theWorld).func_709_b(id);
	}

	public void setTexture(int id, String texture, byte textureId) {
		Entity e = getEntityFromId(id);
		if(e instanceof EntityLiving){
			EntityLiving el = (EntityLiving)e;
			el.setCustomTexture(texture, textureId);
		}
	}
	
	public EntityData getData(UUID id) {
		EntityData storage = entityData.get(id);
		if (storage != null) {
			return storage;
		}
		storage = new EntityData();
		entityData.put(id, storage);
		return storage;
	}
	
	public EntityData getGenericData() {
		return generic;
	}
	
	public void removeData(UUID id) {
		entityData.remove(id);
	}
	
	public void clearData() {
		entityData.clear();
		System.out.println("Cleared Data");
	}
}
