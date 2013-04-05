/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
 * Spoutcraft is licensed under the GNU Lesser General Public License.
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
package org.spoutcraft.client;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.src.WorldInfo;

import org.spoutcraft.api.util.FastLocation;
import org.spoutcraft.api.util.FixedLocation;
import org.spoutcraft.client.block.SpoutcraftChunk;
import org.spoutcraft.client.entity.CraftEntity;
import org.spoutcraft.client.entity.CraftLivingEntity;
import org.spoutcraft.client.player.SpoutPlayer;

public class SpoutcraftWorld {
	private final net.minecraft.src.World handle;

	public SpoutcraftWorld(net.minecraft.src.World world) {
		handle = world;
	}

	public net.minecraft.src.World getHandle() {
		return handle;
	}

	public boolean isAllowAnimals() {
		return handle.spawnPeacefulMobs;
	}

	public boolean isAllowMonsters() {
		return handle.spawnHostileMobs;
	}

	public int getMixedBrightnessAt(org.spoutcraft.api.material.Block block, int x, int y, int z) {
		net.minecraft.src.Block b = null;
		if (block.getRawId() < net.minecraft.src.Block.blocksList.length) {
			b = net.minecraft.src.Block.blocksList[block.getRawId()];
		}
		if (b == null) {
			b = net.minecraft.src.Block.stone;
		}
		return b.getMixedBrightnessForBlock(handle, x, y, z);
	}

	public boolean isOpaque(int x, int y, int z) {
		return handle.isBlockOpaqueCube(x, y, z);
	}

	public long getFullTime() {
		return handle.getWorldTime();
	}

	public int getHighestBlockYAt(int x, int z) {
		return handle.getFirstUncoveredBlock(x, z);
	}

	public int getMaxHeight() {
		return 256;
	}

	public int getXBitShifts() {
		return 8;
	}

	public int getZBitShifts() {
		return 12;
	}

	public long getSeed() {
		return handle.getSeed();
	}

	public boolean isChunkLoaded(SpoutcraftChunk chunk) {
		return handle.chunkProvider.chunkExists(chunk.getX(), chunk.getZ());
	}

	public boolean isChunkLoaded(int x, int z) {
		return handle.chunkProvider.chunkExists(x, z);
	}

	public void loadChunk(SpoutcraftChunk chunk) {
		handle.chunkProvider.loadChunk(chunk.getX(), chunk.getZ());
	}

	public void loadChunk(int x, int z) {
		handle.chunkProvider.loadChunk(x, z);
	}

	public void save() {
		handle.chunkProvider.saveChunks(false, null);
	}

	public void setFullTime(long time) {
		handle.worldInfo.setWorldTime(time);
	}

	public long getTime() {
		long time = getFullTime() % 24000;
		if (time < 0) {
			time += 24000;
		}
		return time;
	}

	public void setTime(long time) {
		long margin = (time - getFullTime()) % 24000;
		if (margin < 0) {
			margin += 24000;
		}
		setFullTime(getFullTime() + margin);
	}

	public int getBlockTypeIdAt(int x, int y, int z) {
		return handle.getBlockId(x, y, z);
	}

	public int getBlockTypeIdAt(FixedLocation location) {
		return getBlockTypeIdAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}

	public int getBlockDataAt(int x, int y, int z) {
		return handle.getBlockMetadata(x, y, z);
	}

	public int getBlockDataAt(FixedLocation location) {
		return getBlockDataAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}

	public int getHighestBlockYAt(FixedLocation location) {
		return getHighestBlockYAt(location.getBlockX(), location.getBlockZ());
	}

	public List<CraftEntity> getEntities() {
		ArrayList<CraftEntity> ret = new ArrayList<CraftEntity>();
		for (Object mcentity:handle.loadedEntityList) {
			if (mcentity instanceof net.minecraft.src.Entity) {
				ret.add(((net.minecraft.src.Entity)mcentity).spoutEnty);
			}
		}
		return ret;
	}

	public List<CraftLivingEntity> getLivingEntities() {
		ArrayList<CraftLivingEntity> ret = new ArrayList<CraftLivingEntity>();
		for (Object mcentity:handle.loadedEntityList) {
			if (mcentity instanceof net.minecraft.src.EntityLiving) {
				ret.add((CraftLivingEntity) ((net.minecraft.src.EntityLiving)mcentity).spoutEnty);
			}
		}
		return ret;
	}

	public List<SpoutPlayer> getPlayers() {
		ArrayList<SpoutPlayer> ret = new ArrayList<SpoutPlayer>();
		for (Object mcentity:handle.loadedEntityList) {
			if (mcentity instanceof net.minecraft.src.EntityPlayer) {
				ret.add((SpoutPlayer) ((net.minecraft.src.EntityPlayer)mcentity).spoutEnty);
			}
		}
		return ret;
	}

	public String getName() {
		return null;
	}

	public UUID getUID() {
		return null;
	}

	public FixedLocation getSpawnLocation() {
		WorldInfo info = handle.worldInfo;
		return new FastLocation(info.getSpawnX(), info.getSpawnY(), info.getSpawnZ(), 0, 0);
	}

	public boolean setSpawnLocation(int x, int y, int z) {
		handle.worldInfo.setSpawnPosition(x, y, z);
		return true;
	}

	public boolean hasStorm() {
		return handle.worldInfo.isRaining();
	}

	public void setStorm(boolean hasStorm) {
		handle.worldInfo.setRaining(hasStorm);
	}

	public int getWeatherDuration() {
		return handle.worldInfo.getRainTime();
	}

	public void setWeatherDuration(int duration) {
		handle.worldInfo.setRainTime(duration);
	}

	public boolean isThundering() {
		return handle.worldInfo.isThundering();
	}

	public void setThundering(boolean thundering) {
		handle.worldInfo.setThundering(thundering);
	}

	public int getThunderDuration() {
		return handle.worldInfo.getThunderTime();
	}

	public void setThunderDuration(int duration) {
		handle.worldInfo.setThunderTime(duration);
	}

	public boolean createExplosion(double x, double y, double z, float power) {
		return createExplosion(x, y, z, power, false);
	}

	public boolean createExplosion(double x, double y, double z, float power, boolean setFire) {
		return handle.newExplosion(null, x, y, z, power, setFire, true) != null;
	}

	public boolean createExplosion(FixedLocation loc, float power) {
		return createExplosion(loc, power, false);
	}

	public boolean createExplosion(FixedLocation loc, float power, boolean setFire) {
		return createExplosion(loc.getX(), loc.getY(), loc.getZ(), power, setFire);
	}

	public boolean isMultiplayerWorld() {
		return Minecraft.theMinecraft.isMultiplayerWorld();
	}

	public CraftEntity getEntityFromId(int id) {
		net.minecraft.src.Entity e = SpoutClient.getInstance().getEntityFromId(id);
		if (e != null) {
			return e.spoutEnty;
		}
		return null;
	}
}
