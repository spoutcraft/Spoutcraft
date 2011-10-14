/*
 * This file is part of Bukkit (http://bukkit.org/).
 * 
 * Bukkit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Bukkit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
package org.spoutcraft.spoutcraftapi.generator;

import java.util.Random;

import org.spoutcraft.spoutcraftapi.World;
import org.spoutcraft.spoutcraftapi.block.Chunk;

/**
 * A block populator is responsible for generating a small area of blocks.
 * For example, generating glowstone inside the nether or generating dungeons full of treasure
 */
public abstract class BlockPopulator {
	/**
	 * Populates an area of blocks at or around the given chunk.
	 *
	 * The chunks on each side of the specified chunk must already exist; that is,
	 * there must be one north, east, south and west of the specified chunk.
	 * The "corner" chunks may not exist, in which scenario the populator should
	 * record any changes required for those chunks and perform the changes when
	 * they are ready.
	 *
	 * @param world The world to generate in
	 * @param random The random generator to use
	 * @param source The chunk to generate for
	 */
	public abstract void populate(World world, Random random, Chunk source);
}
