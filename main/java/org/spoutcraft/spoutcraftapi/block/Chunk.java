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
package org.spoutcraft.spoutcraftapi.block;

import org.spoutcraft.spoutcraftapi.World;
import org.spoutcraft.spoutcraftapi.entity.Entity;

public interface Chunk {
	
	public Block getBlockAt(int x, int y, int z);
	
	//TODO Implement
	public World getWorld();
	
	//TODO Implement
	public int getX();
	
	//TODO Implement
	public int getZ();
	
	//TODO Implement
	public boolean isLoaded();
	
	//TODO Implement
	public boolean load();
	
	//TODO Implement
	public boolean load(boolean generate);
	
	//TODO Implement
	public boolean unload();
	
	//TODO Implement
	public boolean unload(boolean save);
	
	//TODO Implement
	public boolean unload(boolean save, boolean safe);
	
	//TODO Implement
	public Entity[] getEntities();

}
