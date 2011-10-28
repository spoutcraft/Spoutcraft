/*
 * This file is part of SpoutcraftAPI (http://wiki.getspout.org/).
 * 
 * SpoutcraftAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SpoutcraftAPI is distributed in the hope that it will be useful,
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

	/**
	 * Gets a block from this chunk
	 *
	 * @param x 0-15
	 * @param y 0-127
	 * @param z 0-15
	 * @return the Block
	 */
	public Block getBlockAt(int x, int y, int z);

	/**
	 * Gets the world containing this chunk
	 *
	 * @return Parent World
	 */
	public World getWorld();

	/**
	 * Gets the X-coordinate of this chunk
	 *
	 * @return X-coordinate
	 */
	public int getX();

	/**
	 * Gets the Z-coordinate of this chunk
	 *
	 * @return Z-coordinate
	 */
	public int getZ();

	/**
	 * Checks if the chunk is loaded.
	 *
	 * @return True if it is loaded.
	 */
	public boolean isLoaded();

	/**
	 * Loads the chunk.
	 *
	 * @return true if the chunk has loaded successfully, otherwise false
	 */
	public boolean load();


	/**
	 * Loads the chunk.
	 *
	 * @param generate Whether or not to generate a chunk if it doesn't already exist
	 * @return true if the chunk has loaded successfully, otherwise false
	 */
	public boolean load(boolean generate);

	/**
	 * Unloads and optionally saves the Chunk
	 *
	 * @return true if the chunk has unloaded successfully, otherwise false
	 */	 
	public boolean unload();

	/**
	 * Unloads and optionally saves the Chunk
	 *
	 * @param save Controls whether the chunk is saved
	 * @return true if the chunk has unloaded successfully, otherwise false
	 */
	public boolean unload(boolean save);

	/**
	 * Unloads and optionally saves the Chunk
	 *
	 * @param save Controls whether the chunk is saved
	 * @param safe Controls whether to unload the chunk when players are nearby
	 * @return true if the chunk has unloaded successfully, otherwise false
	 */
	public boolean unload(boolean save, boolean safe);

	/**
	 * Get a list of all entities in the chunk.
	 * @return The entities.
	 */
	public Entity[] getEntities();

}
