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
package org.getspout.spout.block;

import gnu.trove.map.hash.TIntFloatHashMap;
import gnu.trove.map.hash.TIntIntHashMap;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.spoutcraft.spoutcraftapi.World;
import org.spoutcraft.spoutcraftapi.block.Block;
import org.spoutcraft.spoutcraftapi.block.Chunk;
import org.spoutcraft.spoutcraftapi.entity.Entity;

import com.google.common.collect.MapMaker;

public class SpoutcraftChunk implements Chunk{
	public static final Set<SpoutcraftChunk> loadedChunks = new HashSet<SpoutcraftChunk>();
	
	private WeakReference<net.minecraft.src.Chunk> weakChunk;
	private net.minecraft.src.World world;
	private final Map<Integer, Block> cache = new MapMaker().softValues().makeMap();
	private int x;
	private int z;
	public final TIntIntHashMap powerOverrides = new TIntIntHashMap();
	public final TIntFloatHashMap hardnessOverrides = new TIntFloatHashMap();
	
	public SpoutcraftChunk(net.minecraft.src.Chunk chunk) {
		this.weakChunk = new WeakReference<net.minecraft.src.Chunk>(chunk);
		world = getHandle().worldObj;
		x = getHandle().xPosition;
		z = getHandle().zPosition;
	}
	
	public net.minecraft.src.Chunk getHandle() {
		net.minecraft.src.Chunk c = weakChunk.get();
		if (c == null) {
			c = world.getChunkFromChunkCoords(x, z);
			weakChunk = new WeakReference<net.minecraft.src.Chunk>(c);
		}
		return c;
	}
	
	public Block getBlockAt(int x, int y, int z) {
		int pos = (x & 0xF) << 11 | (z & 0xF) << 7 | (y & 0x7F);
		Block block = this.cache.get(pos);
		if (block == null) {
			Block newBlock = new SpoutcraftBlock(this, (getX() << 4) | (x & 0xF), y & 0x7F, (getZ() << 4) | (z & 0xF));
			Block oldBlock = this.cache.put(pos, newBlock);
			if (oldBlock == null) {
				block = newBlock;
			} else {
				block = oldBlock;
			}
		}
		return block;
	}

	public World getWorld() {
		return world.world;
	}

	public int getX() {
		return x;
	}

	public int getZ() {
		return z;
	}

	public boolean isLoaded() {
		return getWorld().isChunkLoaded(this);
	}

	public boolean load() {
		return getWorld().loadChunk(getX(), getZ(), true);
	}

	public boolean load(boolean generate) {
		return getWorld().loadChunk(getX(), getZ(), generate);
	}

	public boolean unload() {
		return getWorld().unloadChunk(getX(), getX());
	}

	public boolean unload(boolean save) {
		return getWorld().unloadChunk(getX(), getZ(), save);
	}

	public boolean unload(boolean save, boolean safe) {
		return getWorld().unloadChunk(getX(), getZ(), save, safe);
	}

	public Entity[] getEntities() {
		int count = 0, index = 0;
		net.minecraft.src.Chunk chunk = getHandle();
		for (int i = 0; i < 8; i++) {
			count += chunk.entities[i].size();
		}

		Entity[] entities = new Entity[count];
		for (int i = 0; i < 8; i++) {
			for (Object obj: chunk.entities[i].toArray()) {
				if (!(obj instanceof net.minecraft.src.Entity)) {
					continue;
				}
				entities[index++] = ((net.minecraft.src.Entity) obj).spoutEntity;
			}
		}
		return entities;
	}
	
	public int hashCode() {
		return new HashCodeBuilder().append(x).append(z).hashCode();
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof SpoutcraftChunk) {
			SpoutcraftChunk other = (SpoutcraftChunk)obj;
			return x == other.x && z == other.z;
		}
		return false;
	}
	
	public String toString() {
		return "SpoutcraftChunk (" + x + ", " + z +") ";
	}
	
}
