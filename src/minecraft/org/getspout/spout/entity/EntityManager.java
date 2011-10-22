/*
 * This file is part of Spoutcraft API (http://wiki.getspout.org/).
 * 
 * Spoutcraft API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.getspout.spout.entity;

import java.util.UUID;

import net.minecraft.src.Entity;

public interface EntityManager {
	
	public Entity getEntityFromId(int id);
	
	public void setTexture(int id, String texture, byte textureId);
	
	public EntityData getData(UUID id);
	
	public EntityData getGenericData();
	
	public void removeData(UUID id);
	
	public void clearData();
}
