package net.minecraft.src;

import com.pclewis.mcpatcher.mod.Colorizer;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
	public static final BiomeGenBase beach = (new BiomeGenBeach(16)).setColor(16440917).setBiomeName("Beach").setTemperatureRainfall(0.8F, 0.4F).setMinMaxHeight(0.0F, 0.1F);
	public static final BiomeGenBase desertHills = (new BiomeGenDesert(17)).setColor(13786898).setBiomeName("DesertHills").setDisableRain().setTemperatureRainfall(2.0F, 0.0F).setMinMaxHeight(0.2F, 0.7F);
	public static final BiomeGenBase forestHills = (new BiomeGenForest(18)).setColor(2250012).setBiomeName("ForestHills").func_4124_a(5159473).setTemperatureRainfall(0.7F, 0.8F).setMinMaxHeight(0.2F, 0.6F);
	public static final BiomeGenBase taigaHills = (new BiomeGenTaiga(19)).setColor(1456435).setBiomeName("TaigaHills").func_4124_a(5159473).setTemperatureRainfall(0.05F, 0.8F).setMinMaxHeight(0.2F, 0.7F);
	public static final BiomeGenBase extremeHillsEdge = (new BiomeGenHills(20)).setColor(7501978).setBiomeName("Extreme Hills Edge").setMinMaxHeight(0.2F, 0.8F).setTemperatureRainfall(0.2F, 0.3F);
	public static final BiomeGenBase field_48416_w = (new BiomeGenJungle(21)).setColor(5470985).setBiomeName("Jungle").func_4124_a(5470985).setTemperatureRainfall(1.2F, 0.9F).setMinMaxHeight(0.2F, 0.4F);
	public static final BiomeGenBase field_48417_x = (new BiomeGenJungle(22)).setColor(2900485).setBiomeName("JungleHills").func_4124_a(5470985).setTemperatureRainfall(1.2F, 0.9F).setMinMaxHeight(1.8F, 0.2F);
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

	protected BiomeGenBase(int par1) {
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
		this.biomeID = par1;
		biomeList[par1] = this;
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

	private BiomeGenBase setTemperatureRainfall(float par1, float par2) {
		if (par1 > 0.1F && par1 < 0.2F) {
			throw new IllegalArgumentException("Please avoid temperatures in the range 0.1 - 0.2 because of snow");
		} else {
			this.temperature = par1;
			this.rainfall = par2;
			return this;
		}
	}

	private BiomeGenBase setMinMaxHeight(float par1, float par2) {
		this.minHeight = par1;
		this.maxHeight = par2;
		return this;
	}

	private BiomeGenBase setDisableRain() {
		this.enableRain = false;
		return this;
	}

	public WorldGenerator getRandomWorldGenForTrees(Random par1Random) {
		return (WorldGenerator)(par1Random.nextInt(10) == 0?this.worldGenBigTree:this.worldGenTrees);
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

	public WorldGenerator func_48410_b(Random par1Random) {
		return new WorldGenTallGrass(Block.tallGrass.blockID, 1);
	}

	protected BiomeGenBase setBiomeName(String par1Str) {
		this.biomeName = par1Str;
		Colorizer.setupBiome(this); //Spout HD
		return this;
	}

	protected BiomeGenBase func_4124_a(int par1) {
		this.field_6502_q = par1;
		return this;
	}

	protected BiomeGenBase setColor(int par1) {
		this.color = par1;
		return this;
	}

	public int getSkyColorByTemp(float par1) {
		par1 /= 3.0F;
		if (par1 < -1.0F) {
			par1 = -1.0F;
		}

		if (par1 > 1.0F) {
			par1 = 1.0F;
		}

		return Color.getHSBColor(0.62222224F - par1 * 0.05F, 0.5F + par1 * 0.1F, 1.0F).getRGB();
	}

	public List getSpawnableList(EnumCreatureType par1EnumCreatureType) {
		return par1EnumCreatureType == EnumCreatureType.monster?this.spawnableMonsterList:(par1EnumCreatureType == EnumCreatureType.creature?this.spawnableCreatureList:(par1EnumCreatureType == EnumCreatureType.waterCreature?this.spawnableWaterCreatureList:null));
	}

	public boolean getEnableSnow() {
		return this.enableSnow;
	}

	public boolean canSpawnLightningBolt() {
		return this.enableSnow?false:this.enableRain;
	}

	public boolean func_48413_d() {
		return this.rainfall > 0.85F;
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

	public final float func_48414_h() {
		return this.rainfall;
	}

	public final float func_48411_i() {
		return this.temperature;
	}

	public void decorate(World par1World, Random par2Random, int par3, int par4) {
		this.biomeDecorator.decorate(par1World, par2Random, par3, par4);
	}

	public int func_48415_j() {
		double var1 = (double)MathHelper.func_48442_a(this.func_48411_i(), 0.0F, 1.0F);
		double var3 = (double)MathHelper.func_48442_a(this.func_48414_h(), 0.0F, 1.0F);
		return ColorizerGrass.getGrassColor(var1, var3);
	}

	public int func_48412_k() {
		double var1 = (double)MathHelper.func_48442_a(this.func_48411_i(), 0.0F, 1.0F);
		double var3 = (double)MathHelper.func_48442_a(this.func_48414_h(), 0.0F, 1.0F);
		return ColorizerFoliage.getFoliageColor(var1, var3);
	}
}
