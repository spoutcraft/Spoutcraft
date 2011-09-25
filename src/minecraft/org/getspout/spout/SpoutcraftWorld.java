package org.getspout.spout;

import java.util.List;
import java.util.UUID;

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
import org.spoutcraft.spoutcraftapi.util.FixedLocation;
import org.spoutcraft.spoutcraftapi.util.Vector;

public class SpoutcraftWorld implements World{
	
	private final net.minecraft.src.World handle;
	public SpoutcraftWorld(net.minecraft.src.World world) {
		handle = world;
	}
	
	public net.minecraft.src.World getHandle() {
		return handle;
	}

	public boolean getAllowAnimals() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean getAllowMonsters() {
		// TODO Auto-generated method stub
		return false;
	}

	public Block getBlockAt(int x, int y, int z) {
		return getChunkAt(x >> 4, z >> 4).getBlockAt(x & 0xF, y & 0x7F, z & 0xF);
	}

	public Chunk getChunkAt(Block block) {
		return handle.getChunkFromBlockCoords(block.getX(), block.getZ()).spoutChunk;
	}

	public Chunk getChunkAt(int x, int y) {
		return handle.getChunkFromChunkCoords(x, y).spoutChunk;
	}
	
	public Chunk getChunkAt(FixedLocation location) {
		return handle.getChunkFromBlockCoords(location.getBlockX(), location.getBlockZ()).spoutChunk;
	}

	public long getFullTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Block getHighestBlockAt(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getHighestBlockYAt(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Chunk[] getLoadedChunks() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getMaxHeight() {
		return 127;
	}

	public long getSeed() {
		return handle.getRandomSeed();
	}

	public long getTime() {
		return handle.getWorldTime();
	}

	public boolean isChunkLoaded(Chunk arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isChunkLoaded(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public void loadChunk(Chunk arg0) {
		// TODO Auto-generated method stub
		
	}

	public void loadChunk(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	public boolean loadChunk(int arg0, int arg1, boolean arg2) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		
	}

	public void setFullTime(long arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setTime(long arg0) {
		// TODO Auto-generated method stub
		
	}

	public Block getBlockAt(FixedLocation location) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getBlockTypeIdAt(int x, int y, int z) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getBlockTypeIdAt(FixedLocation location) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getHighestBlockYAt(FixedLocation location) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Block getHighestBlockAt(FixedLocation location) {
		// TODO Auto-generated method stub
		return null;
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

	public Arrow spawnArrow(FixedLocation location, Vector velocity,
			float speed, float spread) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean generateTree(FixedLocation location, TreeType type) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean generateTree(FixedLocation loc, TreeType type,
			BlockChangeDelegate delegate) {
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
		// TODO Auto-generated method stub
		return null;
	}

	public List<LivingEntity> getLivingEntities() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Player> getPlayers() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public UUID getUID() {
		// TODO Auto-generated method stub
		return null;
	}

	public FixedLocation getSpawnLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean setSpawnLocation(int x, int y, int z) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean hasStorm() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setStorm(boolean hasStorm) {
		// TODO Auto-generated method stub
		
	}

	public int getWeatherDuration() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setWeatherDuration(int duration) {
		// TODO Auto-generated method stub
		
	}

	public boolean isThundering() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setThundering(boolean thundering) {
		// TODO Auto-generated method stub
		
	}

	public int getThunderDuration() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setThunderDuration(int duration) {
		// TODO Auto-generated method stub
		
	}

	public boolean createExplosion(double x, double y, double z, float power) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean createExplosion(double x, double y, double z, float power,
			boolean setFire) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean createExplosion(FixedLocation loc, float power) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean createExplosion(FixedLocation loc, float power,
			boolean setFire) {
		// TODO Auto-generated method stub
		return false;
	}

	public Environment getEnvironment() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean getPVP() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setPVP(boolean pvp) {
		// TODO Auto-generated method stub
		
	}

	public ChunkGenerator getGenerator() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<BlockPopulator> getPopulators() {
		// TODO Auto-generated method stub
		return null;
	}

	public <T extends Entity> T spawn(FixedLocation location, Class<T> clazz)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	public void playEffect(FixedLocation location, Effect effect, int data) {
		// TODO Auto-generated method stub
		
	}

	public void playEffect(FixedLocation location, Effect effect, int data,
			int radius) {
		// TODO Auto-generated method stub
		
	}

	public ChunkSnapshot getEmptyChunkSnapshot(int x, int z,
			boolean includeBiome, boolean includeBiomeTempRain) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setSpawnFlags(boolean allowMonsters, boolean allowAnimals) {
		// TODO Auto-generated method stub
		
	}

	public boolean isAllowAnimals() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isAllowMonsters() {
		// TODO Auto-generated method stub
		return false;
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
		// TODO Auto-generated method stub
		return 0;
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
}
