/*
 * This file is part of Spoutcraft (http://www.spout.org/).
 *
 * Spoutcraft is licensed under the SpoutDev License Version 1.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spoutcraft.client.block;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.MapMaker;
import gnu.trove.map.hash.TIntFloatHashMap;

import net.minecraft.client.Minecraft;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.spoutcraft.client.SpoutcraftWorld;
import org.spoutcraft.spoutcraftapi.block.Block;
import org.spoutcraft.spoutcraftapi.block.Chunk;
import org.spoutcraft.spoutcraftapi.entity.Entity;
import org.spoutcraft.spoutcraftapi.material.CustomBlock;
import org.spoutcraft.spoutcraftapi.material.MaterialData;

public class SpoutcraftChunk implements Chunk{
	public static final Set<SpoutcraftChunk> loadedChunks = new HashSet<SpoutcraftChunk>();

	private WeakReference<net.minecraft.src.Chunk> weakChunk;
	private net.minecraft.src.World world;
	private final Map<Integer, Block> cache = new MapMaker().weakValues().makeMap();
	private int x;
	private int z;
	//public final TIntIntHashMap powerOverrides = new TIntIntHashMap();
	public final TIntFloatHashMap hardnessOverrides = new TIntFloatHashMap();
	private short[] customBlockIds = null;

	transient private final int worldHeight;
	transient private final int worldHeightMinusOne;
	transient private final int xBitShifts;
	transient private final int zBitShifts;

	public SpoutcraftChunk(net.minecraft.src.Chunk chunk) {
		this.weakChunk = new WeakReference<net.minecraft.src.Chunk>(chunk);
		world = chunk.worldObj;
		x = chunk.xPosition;
		z = chunk.zPosition;

		this.worldHeight = 256;
		this.xBitShifts = 8;
		this.zBitShifts = 12;
		worldHeightMinusOne = worldHeight - 1;
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
		int pos = ((x & 0xF) << xBitShifts) | ((z & 0xF) << zBitShifts) | (y & worldHeightMinusOne);
		Block block = this.cache.get(pos);
		if (block == null) {
			Block newBlock = new SpoutcraftBlock(this, (getX() << 4) | (x & 0xF), y & worldHeightMinusOne, (getZ() << 4) | (z & 0xF));
			Block oldBlock = this.cache.put(pos, newBlock);
			if (oldBlock == null) {
				block = newBlock;
			} else {
				block = oldBlock;
			}
		}
		return block;
	}

	public SpoutcraftWorld getWorld() {
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
	
	public byte[] getRawBlockIds() {
		//TODO: fix
		return null;
	}

	public Entity[] getEntities() {
		int count = 0, index = 0;
		net.minecraft.src.Chunk chunk = getHandle();
		for (int i = 0; i < chunk.field_48502_j.length; i++) {
			count += chunk.field_48502_j[i].size();
		}

		Entity[] entities = new Entity[count];
		for (int i = 0; i < chunk.field_48502_j.length; i++) {
			for (Object obj: chunk.field_48502_j[i].toArray()) {
				if (!(obj instanceof net.minecraft.src.Entity)) {
					continue;
				}
				entities[index++] = ((net.minecraft.src.Entity) obj).spoutEntity;
			}
		}
		return entities;
	}

	public short getCustomBlockId(int x, int y, int z) {
		if (customBlockIds != null) {
			int key = ((x & 0xF) << xBitShifts) | ((z & 0xF) << zBitShifts) | (y & worldHeightMinusOne);
			return customBlockIds[key];
		}
		return 0;
	}

	public short setCustomBlockId(int x, int y, int z, short id) {
		if (customBlockIds == null) {
			customBlockIds = new short[16*16*worldHeight];
		}
		if (id < 0) id = 0;
		int key = ((x & 0xF) << xBitShifts) | ((z & 0xF) << zBitShifts) | (y & worldHeightMinusOne);
		short old = customBlockIds[key];
		customBlockIds[key] = id;
		Minecraft.theMinecraft.theWorld.markBlockNeedsUpdate(x, y, z);
		return old;
	}

	public short[] getCustomBlockIds() {
		return customBlockIds;
	}

	public void setCustomBlockIds(short[] ids) {
		customBlockIds = ids;
		Minecraft.theMinecraft.theWorld.markBlocksDirty(x * 16, 0, z * 16, x * 16 + 15, worldHeightMinusOne, z * 16 + 15);
	}

	public CustomBlock setCustomBlockId(int x, int y, int z, CustomBlock block) {
		if (block == null) {
			throw new NullPointerException("Custom Block can not be null!");
		}
		short old = setCustomBlockId(x, y, z, (short) block.getCustomId());
		return MaterialData.getCustomBlock(old);
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
