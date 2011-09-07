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

public interface Block {

	public int getX();

	public int getY();

	public int getZ();

	public float getHardness();

	public void setHardness(float hardness);

	// TODO Implement
	public int getBlockPower();

	// TODO Implement
	public byte getLightLevel();

	// TODO Implement
	public int getTypeId();

	// TODO Implement
	public World getWorld();

	// TODO Implement
	public boolean isEmpty();

	// TODO Implement
	public boolean isLiquid();

	// TODO Implement
	public void setData(byte data);

	// TODO Implement
	public boolean setTypeId(int type);
	
	//TODO Implement
	public Chunk getChunk();

}
