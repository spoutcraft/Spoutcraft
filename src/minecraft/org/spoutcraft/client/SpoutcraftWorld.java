
package org.spoutcraft.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.src.WorldInfo;

import org.spoutcraft.client.block.SpoutcraftChunk;
import org.spoutcraft.client.entity.CraftEntity;
import org.spoutcraft.spoutcraftapi.BlockChangeDelegate;
import org.spoutcraft.spoutcraftapi.ChunkSnapshot;
import org.spoutcraft.spoutcraftapi.Effect;
import org.spoutcraft.spoutcraftapi.TreeType;
import org.spoutcraft.spoutcraftapi.World;
import org.spoutcraft.spoutcraftapi.block.Biome;
import org.spoutcraft.spoutcraftapi.block.Block;
import org.spoutcraft.spoutcraftapi.block.Chunk;
import org.spoutcraft.spoutcraftapi.entity.Arrow;
import org.spoutcraft.spoutcraftapi.entity.CreatureType;
import org.spoutcraft.spoutcraftapi.entity.Entity;
import org.spoutcraft.spoutcraftapi.entity.Item;
import org.spoutcraft.spoutcraftapi.entity.LightningStrike;
import org.spoutcraft.spoutcraftapi.entity.LivingEntity;
import org.spoutcraft.spoutcraftapi.entity.Player;
import org.spoutcraft.spoutcraftapi.generator.BlockPopulator;
import org.spoutcraft.spoutcraftapi.generator.ChunkGenerator;
import org.spoutcraft.spoutcraftapi.inventory.ItemStack;
import org.spoutcraft.spoutcraftapi.util.FastLocation;
import org.spoutcraft.spoutcraftapi.util.FixedLocation;
import org.spoutcraft.spoutcraftapi.util.MutableLocation;
import org.spoutcraft.spoutcraftapi.util.Vector;

public class SpoutcraftWorld implements World{
	private final net.minecraft.src.World handle;
	private Environment environment;

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

	public int getMixedBrightnessAt(org.spoutcraft.spoutcraftapi.material.Block block, int x, int y, int z) {
		net.minecraft.src.Block b;
		if (block.getRawId() < net.minecraft.src.Block.blocksList.length) {
			b = net.minecraft.src.Block.blocksList[block.getRawId()];
		} else {
			b = net.minecraft.src.Block.stone;
		}
		return b.getMixedBrightnessForBlock(handle, x, y, z);
	}

	public boolean isOpaque(int x, int y, int z) {
		return handle.isBlockOpaqueCube(x, y, z);
	}

	public Block getBlockAt(int x, int y, int z) {
		return getChunkAt(x >> 4, z >> 4).getBlockAt(x & 0xF, y & (getMaxHeight() - 1), z & 0xF);
	}

	public Chunk getChunkAt(Block block) {
		return handle.getChunkFromBlockCoords(block.getX(), block.getZ()).spoutChunk;
	}

	public Chunk getChunkAt(int x, int y) {
		return handle.getChunkFromChunkCoords(x, y).spoutChunk;
	}

	public Chunk getChunkAt(int x, int y, int z) {
		return handle.getChunkFromBlockCoords(x, z).spoutChunk;
	}

	public Chunk getChunkAt(FixedLocation location) {
		return handle.getChunkFromBlockCoords(location.getBlockX(), location.getBlockZ()).spoutChunk;
	}

	public long getFullTime() {
		return handle.getWorldTime();
	}

	public Block getHighestBlockAt(int x, int z) {
		return getBlockAt(x, getHighestBlockYAt(x, z), z);
	}

	public int getHighestBlockYAt(int x, int z) {
		return handle.getFirstUncoveredBlock(x, z);
	}

	public Chunk[] getLoadedChunks() {
		Set<SpoutcraftChunk> chunks = SpoutcraftChunk.loadedChunks;
		Chunk[] loaded = new Chunk[chunks.size()];
		Iterator<SpoutcraftChunk> j = chunks.iterator();
		for (int i = 0; i < chunks.size(); i++) {
			loaded[i] = j.next();
		}
		return loaded;
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

	public boolean isChunkLoaded(Chunk chunk) {
		return handle.chunkProvider.chunkExists(chunk.getX(), chunk.getZ());
	}

	public boolean isChunkLoaded(int x, int z) {
		return handle.chunkProvider.chunkExists(x, z);
	}

	public void loadChunk(Chunk chunk) {
		handle.chunkProvider.loadChunk(chunk.getX(), chunk.getZ());
	}

	public void loadChunk(int x, int z) {
		handle.chunkProvider.loadChunk(x, z);
	}

	public boolean loadChunk(int x, int z, boolean generate) {
		// TODO ?
		return false;
	}

	public boolean refreshChunk(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean regenerateChunk(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
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

	public Block getBlockAt(FixedLocation location) {
		return getChunkAt(location.getBlockX() >> 4, location.getBlockZ() >> 4).getBlockAt(location.getBlockX() & 0xF, location.getBlockY() & 0x7F, location.getBlockZ() & 0xF);
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

	public Block getHighestBlockAt(FixedLocation location) {
		return getHighestBlockAt(location.getBlockX(), location.getBlockZ());
	}

	public boolean unloadChunk(Chunk chunk) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean unloadChunk(int x, int z) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean unloadChunk(int x, int z, boolean save) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean unloadChunk(int x, int z, boolean save, boolean safe) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean unloadChunkRequest(int x, int z) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean unloadChunkRequest(int x, int z, boolean safe) {
		// TODO Auto-generated method stub
		return false;
	}

	public Item dropItem(FixedLocation location, ItemStack item) {
		// TODO Auto-generated method stub
		return null;
	}

	public Item dropItemNaturally(FixedLocation location, ItemStack item) {
		// TODO Auto-generated method stub
		return null;
	}

	public Arrow spawnArrow(FixedLocation location, Vector velocity, float speed, float spread) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean generateTree(FixedLocation location, TreeType type) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean generateTree(FixedLocation loc, TreeType type, BlockChangeDelegate delegate) {
		// TODO Auto-generated method stub
		return false;
	}

	public LivingEntity spawnCreature(FixedLocation loc, CreatureType type) {
		// TODO Auto-generated method stub
		return null;
	}

	public LightningStrike strikeLightning(FixedLocation loc) {
		// TODO Auto-generated method stub
		return null;
	}

	public LightningStrike strikeLightningEffect(FixedLocation loc) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Entity> getEntities() {
		ArrayList<Entity> ret = new ArrayList<Entity>();
		for (Object mcentity:handle.loadedEntityList) {
			if (mcentity instanceof net.minecraft.src.Entity) {
				ret.add(((net.minecraft.src.Entity)mcentity).spoutEntity);
			}
		}
		return ret;
	}

	public List<LivingEntity> getLivingEntities() {
		ArrayList<LivingEntity> ret = new ArrayList<LivingEntity>();
		for (Object mcentity:handle.loadedEntityList) {
			if (mcentity instanceof net.minecraft.src.EntityLiving) {
				ret.add((LivingEntity) ((net.minecraft.src.EntityLiving)mcentity).spoutEntity);
			}
		}
		return ret;
	}

	public List<Player> getPlayers() {
		ArrayList<Player> ret = new ArrayList<Player>();
		for (Object mcentity:handle.loadedEntityList) {
			if (mcentity instanceof net.minecraft.src.EntityPlayer) {
				ret.add((Player) ((net.minecraft.src.EntityPlayer)mcentity).spoutEntity);
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
		return new FastLocation(info.getSpawnX(), info.getSpawnY(), info.getSpawnZ(), 0, 0, this);
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
		return handle.newExplosion(null, x, y, z, power, setFire) != null;
	}

	public boolean createExplosion(FixedLocation loc, float power) {
		return createExplosion(loc, power, false);
	}

	public boolean createExplosion(FixedLocation loc, float power, boolean setFire) {
		return createExplosion(loc.getX(), loc.getY(), loc.getZ(), power, setFire);
	}

	public Environment getEnvironment() {
		//TODO: get the environments
		return environment;
	}

	public ChunkGenerator getGenerator() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<BlockPopulator> getPopulators() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T extends Entity> T spawn(FixedLocation location, Class<T> clazz) throws IllegalArgumentException {
		return (T) CraftEntity.spawn(new MutableLocation(location.getWorld(), location.getX(), location.getY(), location.getZ()), (Class<Entity>) clazz);
	}

	public void playEffect(FixedLocation location, Effect effect, int data) {
		// TODO Auto-generated method stub
	}

	public void playEffect(FixedLocation location, Effect effect, int data, int radius) {
		// TODO Auto-generated method stub
	}

	public ChunkSnapshot getEmptyChunkSnapshot(int x, int z, boolean includeBiome, boolean includeBiomeTempRain) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setSpawnFlags(boolean allowMonsters, boolean allowAnimals) {
		// TODO Auto-generated method stub
	}

	public Biome getBiome(int x, int z) {
		// TODO Auto-generated method stub
		return null;
	}

	public double getTemperature(int x, int z) {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getHumidity(int x, int z) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getSeaLevel() {
		return getMaxHeight() / 4;
	}

	public boolean getKeepSpawnInMemory() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setKeepSpawnInMemory(boolean keepLoaded) {
		// TODO Auto-generated method stub
	}

	public boolean isAutoSave() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setAutoSave(boolean value) {
		// TODO Auto-generated method stub
	}

	public boolean isMultiplayerWorld() {
		return Minecraft.theMinecraft.isMultiplayerWorld();
	}

	public Entity getEntityFromId(int id) {
		net.minecraft.src.Entity e = SpoutClient.getInstance().getEntityFromId(id);
		if (e != null) {
			return e.spoutEntity;
		}
		return null;
	}

	public Entity getEntityFromUUID(UUID id) {
		// TODO
		throw new UnsupportedOperationException("Not yet implemented!");
	}
}
