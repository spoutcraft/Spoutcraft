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

import net.minecraft.src.EntityLiving;

public enum EntitySkinType {
	DEFAULT(0),
	SPIDER_EYES(1),
	SHEEP_FUR(2),
	WOLF_ANGRY(3),
	WOLF_TAMED(4),
	PIG_SADDLE(5),
	GHAST_MOUTH(6),
	ENDERMAN_EYES(7),
	;
	
	private final byte id;
	private EntitySkinType(int id){
		this.id = (byte)id;
	}
	
	public byte getId(){
		return id;
	}
	
	public static String getTexture(EntitySkinType type, EntityLiving entity, String std){
		String texture = entity.getCustomTexture(type.getId());
		if(texture == null)
		{
			texture = std;
		}
		return texture;
	}
}
