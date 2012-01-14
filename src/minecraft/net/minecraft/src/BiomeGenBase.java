package net.minecraft.src;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.src.BiomeDecorator;
import net.minecraft.src.BiomeGenBeach;
import net.minecraft.src.BiomeGenDesert;
import net.minecraft.src.BiomeGenEnd;
import net.minecraft.src.BiomeGenForest;
import net.minecraft.src.BiomeGenHell;
import net.minecraft.src.BiomeGenHills;
import net.minecraft.src.BiomeGenMushroomIsland;
import net.minecraft.src.BiomeGenOcean;
import net.minecraft.src.BiomeGenPlains;
import net.minecraft.src.BiomeGenRiver;
import net.minecraft.src.BiomeGenSnow;
import net.minecraft.src.BiomeGenSwamp;
import net.minecraft.src.BiomeGenTaiga;
import net.minecraft.src.Block;
import net.minecraft.src.ColorizerFoliage;
import net.minecraft.src.ColorizerGrass;
import net.minecraft.src.EntityChicken;
import net.minecraft.src.EntityCow;
import net.minecraft.src.EntityCreeper;
import net.minecraft.src.EntityEnderman;
import net.minecraft.src.EntityPig;
import net.minecraft.src.EntitySheep;
import net.minecraft.src.EntitySkeleton;
import net.minecraft.src.EntitySlime;
import net.minecraft.src.EntitySpider;
import net.minecraft.src.EntitySquid;
import net.minecraft.src.EntityZombie;
import net.minecraft.src.EnumCreatureType;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.SpawnListEntry;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenBigTree;
import net.minecraft.src.WorldGenForest;
import net.minecraft.src.WorldGenSwamp;
import net.minecraft.src.WorldGenTrees;
import net.minecraft.src.WorldGenerator;

public abstract class BiomeGenBase {

	public static final BiomeGenBase[] biomeList = new BiomeGenBase[256];
	public static final BiomeGenBase ocean = (new BiomeGenOcean(0)).setColor(112).setBiomeName("Ocean").setMinMaxHeight(-1.0F, 0.4F);
	public static final BiomeGenBase plains = (new BiomeGenPlains(1)).setColor(9286496).setBiomeName("Plains").setTemperatureRainfall(0.8F, 0.4F);
	public static final BiomeGenBase desert = (new BiomeGenDesert(2)).setColor(16421912).setBiomeName("Desert").setDisableRain().setTemperatureRainfall(2.0F, 0.0F).setMinMaxHeight(0.1F, 0.2F);
	public static final BiomeGenBase extremeHills = (new BiomeGenHills(3)).setColor(6316128).setBiomeName("Extreme Hills").setMinMaxHeight(0.2F, 1.3F).setTemperatureRainfall(0.2F, 0.3F);
	public static final BiomeGenBase forest = (new BiomeGenForest(4)).setColor(353825).setBiomeName("Forest").func_4124_a(5159473).setTemperatureRainfall(0.7F, 0.8F);
	public static final BiomeGenBase taiga = (new BiomeGenTaiga(5)).setColor(747097).setBiomeName("Taiga").func_4124_a(5159473).setTemperatureRainfall(0.05F, 0.8F).setMinMaxHeight(0.1F, 0.4F);
	public static final BiomeGenBase swampland = (new BiomeGenSwamp(6)).setColor(522674).setBiomeName("Swampland").func_4124_a(9154376).setMinMaxHeight(-0.2F, 0.1F).setTemperatureRainfall(0.8F, 0.9F);
	public static final BiomeGenBase river = (new BiomeGenRiver(7)).setColor(255).setBiomeName("River").setMinMaxHeight(-0.5F, 0.0F);
	public static final BiomeGenBase hell = (new BiomeGenHell(8)).setColor(16711680).setBiomeName("Hell").setDisableRain().setTemperatureRainfall(2.0F, 0.0F);
	public static final BiomeGenBase sky = (new BiomeGenEnd(9)).setColor(8421631).setBiomeName("Sky").setDisableRain();
	public static final BiomeGenBase frozenOcean = (new BiomeGenOcean(10)).setColor(9474208).setBiomeName("FrozenOcean").setMinMaxHeight(-1.0F, 0.5F).setTemperatureRainfall(0.0F, 0.5F);
	public static final BiomeGenBase frozenRiver = (new BiomeGenRiver(11)).setColor(10526975).setBiomeName("FrozenRiver").setMinMaxHeight(-0.5F, 0.0F).setTemperatureRainfall(0.0F, 0.5F);
	public static final BiomeGenBase icePlains = (new BiomeGenSnow(12)).setColor(16777215).setBiomeName("Ice Plains").setTemperatureRainfall(0.0F, 0.5F);
	public static final BiomeGenBase iceMountains = (new BiomeGenSnow(13)).setColor(10526880).setBiomeName("Ice Mountains").setMinMaxHeight(0.2F, 1.2F).setTemperatureRainfall(0.0F, 0.5F);
	public static final BiomeGenBase mushroomIsland = (new BiomeGenMushroomIsland(14)).setColor(16711935).setBiomeName("MushroomIsland").setTemperatureRainfall(0.9F, 1.0F).setMinMaxHeight(0.2F, 1.0F);
	public static final BiomeGenBase mushroomIslandShore = (new BiomeGenMushroomIsland(15)).setColor(10486015).setBiomeName("MushroomIslandShore").setTemperatureRainfall(0.9F, 1.0F).setMinMaxHeight(-1.0F, 0.1F);
	public static final BiomeGenBase field_46050_r = (new BiomeGenBeach(16)).setColor(16440917).setBiomeName("Beach").setTemperatureRainfall(0.8F, 0.4F).setMinMaxHeight(0.0F, 0.1F);
	public static final BiomeGenBase field_46049_s = (new BiomeGenDesert(17)).setColor(13786898).setBiomeName("DesertHills").setDisableRain().setTemperatureRainfall(2.0F, 0.0F).setMinMaxHeight(0.2F, 0.7F);
	public static final BiomeGenBase field_46048_t = (new BiomeGenForest(18)).setColor(2250012).setBiomeName("ForestHills").func_4124_a(5159473).setTemperatureRainfall(0.7F, 0.8F).setMinMaxHeight(0.2F, 0.6F);
	public static final BiomeGenBase field_46047_u = (new BiomeGenTaiga(19)).setColor(1456435).setBiomeName("TaigaHills").func_4124_a(5159473).setTemperatureRainfall(0.05F, 0.8F).setMinMaxHeight(0.2F, 0.7F);
	public static final BiomeGenBase field_46046_v = (new BiomeGenHills(20)).setColor(7501978).setBiomeName("Extreme Hills Edge").setMinMaxHeight(0.2F, 0.8F).setTemperatureRainfall(0.2F, 0.3F);
	public String biomeName;
	public int color;
	public byte topBlock;
	public byte fillerBlock;
	public int field_6502_q;
	public float minHeight;
	public float maxHeight;
	public float temperature;
	public float rainfall;
	public int waterColorMultiplier;
	public BiomeDecorator biomeDecorator;
	protected List spawnableMonsterList;
	protected List spawnableCreatureList;
	protected List spawnableWaterCreatureList;
	private boolean enableSnow;
	private boolean enableRain;
	public final int biomeID;
	protected WorldGenTrees worldGenTrees;
	protected WorldGenBigTree worldGenBigTree;
	protected WorldGenForest worldGenForest;
	protected WorldGenSwamp worldGenSwamp;

	protected BiomeGenBase(int var1) {
		this.topBlock = (byte)Block.grass.blockID;
		this.fillerBlock = (byte)Block.dirt.blockID;
		this.field_6502_q = 5169201;
		this.minHeight = 0.1F;
		this.maxHeight = 0.3F;
		this.temperature = 0.5F;
		this.rainfall = 0.5F;
		this.waterColorMultiplier = 16777215;
		this.spawnableMonsterList = new ArrayList();
		this.spawnableCreatureList = new ArrayList();
		this.spawnableWaterCreatureList = new ArrayList();
		this.enableRain = true;
		this.worldGenTrees = new WorldGenTrees(false);
		this.worldGenBigTree = new WorldGenBigTree(false);
		this.worldGenForest = new WorldGenForest(false);
		this.worldGenSwamp = new WorldGenSwamp();
		this.biomeID = var1;
		biomeList[var1] = this;
		this.biomeDecorator = this.createBiomeDecorator();
		this.spawnableCreatureList.add(new SpawnListEntry(EntitySheep.class, 12, 4, 4));
		this.spawnableCreatureList.add(new SpawnListEntry(EntityPig.class, 10, 4, 4));
		this.spawnableCreatureList.add(new SpawnListEntry(EntityChicken.class, 10, 4, 4));
		this.spawnableCreatureList.add(new SpawnListEntry(EntityCow.class, 8, 4, 4));
		this.spawnableMonsterList.add(new SpawnListEntry(EntitySpider.class, 10, 4, 4));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityZombie.class, 10, 4, 4));
		this.spawnableMonsterList.add(new SpawnListEntry(EntitySkeleton.class, 10, 4, 4));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityCreeper.class, 10, 4, 4));
		this.spawnableMonsterList.add(new SpawnListEntry(EntitySlime.class, 10, 4, 4));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityEnderman.class, 1, 1, 4));
		this.spawnableWaterCreatureList.add(new SpawnListEntry(EntitySquid.class, 10, 4, 4));
	}

	protected BiomeDecorator createBiomeDecorator() {
		return new BiomeDecorator(this);
	}

	private BiomeGenBase setTemperatureRainfall(float var1, float var2) {
		if (var1 > 0.1F && var1 < 0.2F) {
			throw new IllegalArgumentException("Please avoid temperatures in the range 0.1 - 0.2 because of snow");
		}
		else {
			this.temperature = var1;
			this.rainfall = var2;
			return this;
		}
	}

	private BiomeGenBase setMinMaxHeight(float var1, float var2) {
		this.minHeight = var1;
		this.maxHeight = var2;
		return this;
	}

	private BiomeGenBase setDisableRain() {
		this.enableRain = false;
		return this;
	}

	public WorldGenerator getRandomWorldGenForTrees(Random var1) {
		return (WorldGenerator)(var1.nextInt(10) == 0 ? this.worldGenBigTree : this.worldGenTrees);
	}

	// Spout start
	protected BiomeGenBase setEnableSnow(boolean bool) {
		enableSnow = bool;
		return this;
	}

	protected BiomeGenBase setEnableRain(boolean bool) {
		enableRain = bool;
		return this;
	}

	protected String getBiomeName() {
		return biomeName;
	}
	// Spout end

	protected BiomeGenBase setBiomeName(String var1) {
		this.biomeName = var1;
		return this;
	}

	protected BiomeGenBase func_4124_a(int var1) {
		this.field_6502_q = var1;
		return this;
	}

	protected BiomeGenBase setColor(int var1) {
		this.color = var1;
		return this;
	}

	public int getSkyColorByTemp(float var1) {
		var1 /= 3.0F;
		if (var1 < -1.0F) {
			var1 = -1.0F;
		}

		if (var1 > 1.0F) {
			var1 = 1.0F;
		}

		return Color.getHSBColor(0.62222224F - var1 * 0.05F, 0.5F + var1 * 0.1F, 1.0F).getRGB();
	}

	public List getSpawnableList(EnumCreatureType var1) {
		return var1 == EnumCreatureType.monster ? this.spawnableMonsterList : (var1 == EnumCreatureType.creature ? this.spawnableCreatureList : (var1 == EnumCreatureType.waterCreature ? this.spawnableWaterCreatureList : null));
	}

	public boolean getEnableSnow() {
		return this.enableSnow;
	}

	public boolean canSpawnLightningBolt() {
		return this.enableSnow ? false : this.enableRain;
	}

	public float getSpawningChance() {
		return 0.1F;
	}

	public final int getIntRainfall() {
		return (int)(this.rainfall * 65536.0F);
	}

	public final int getIntTemperature() {
		return (int)(this.temperature * 65536.0F);
	}

	public void func_35477_a(World var1, Random var2, int var3, int var4) {
		this.biomeDecorator.decorate(var1, var2, var3, var4);
	}

	public int getGrassColorAtCoords(IBlockAccess var1, int var2, int var3, int var4) {
		double var5 = (double)var1.getWorldChunkManager().getTemperature(var2, var3, var4);
		double var7 = (double)var1.getWorldChunkManager().getRainfall(var2, var4);
		return ColorizerGrass.getGrassColor(var5, var7);
	}

	public int getFoliageColorAtCoords(IBlockAccess var1, int var2, int var3, int var4) {
		double var5 = (double)var1.getWorldChunkManager().getTemperature(var2, var3, var4);
		double var7 = (double)var1.getWorldChunkManager().getRainfall(var2, var4);
		return ColorizerFoliage.getFoliageColor(var5, var7);
	}

}
