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
package org.spoutcraft.spoutcraftapi;

import org.spoutcraft.spoutcraftapi.block.Block;
import org.spoutcraft.spoutcraftapi.block.Chunk;

public interface World {

	//TODO Implement
	public Block getBlockAt(int x, int y, int z);
	
	//TODO Implement
	public Block getHighestBlockAt(int x, int z);
	
	//TODO Implement
	public int getHighestBlockYAt(int x, int z);
	
	//TODO Implement
	public void setTime(long time);
	
	//TODO Implement
	public long getTime();
	
	//TODO Implement
	public void setFullTime(long time);
	
	//TODO Implement
	public long getFullTime();
	
	//TODO Implement
	public void save();
	
	//TODO Implement
	public int getMaxHeight();
	
	//TODO Implement
	public boolean getAllowAnimals();
	
	//TODO Implement
	public boolean getAllowMonsters();
	
	//TODO Implement
	public Chunk getChunkAt(Block block);
	
	//TODO Implement
	public Chunk getChunkAt(int x, int z);
	
	//TODO Implement
	public Chunk[] getLoadedChunks();
	
	//TODO Implement
	public long getSeed();
	
	//TODO Implement
	public boolean isChunkLoaded(Chunk chunk);
	
	//TODO Implement
	public boolean isChunkLoaded(int x, int z);
	
	//TODO Implement
	public void loadChunk(Chunk chunk);
	
	//TODO Implement
	public void loadChunk(int x, int z);
	
	//TODO Implement
	public boolean loadChunk(int x, int z, boolean generate);
	
	//TODO Implement
	public boolean refreshChunk(int x, int z);
	
	//TODO Implement
	public boolean regenerateChunk(int x, int z);

}
