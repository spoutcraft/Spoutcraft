package net.minecraft.src;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.src.BiomeDecorator;
import net.minecraft.src.BiomeGenDesert;
import net.minecraft.src.BiomeGenForest;
import net.minecraft.src.BiomeGenHell;
import net.minecraft.src.BiomeGenHills;
import net.minecraft.src.BiomeGenOcean;
import net.minecraft.src.BiomeGenPlains;
import net.minecraft.src.BiomeGenRiver;
import net.minecraft.src.BiomeGenSky;
import net.minecraft.src.BiomeGenSwamp;
import net.minecraft.src.BiomeGenTaiga;
import net.minecraft.src.Block;
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
import net.minecraft.src.SpawnListEntry;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenBigTree;
import net.minecraft.src.WorldGenForest;
import net.minecraft.src.WorldGenSwamp;
import net.minecraft.src.WorldGenTrees;
import net.minecraft.src.WorldGenerator;

public abstract class BiomeGenBase {

	public static final BiomeGenBase[] field_35486_a = new BiomeGenBase[256];
	public static final BiomeGenBase field_35484_b = (new BiomeGenOcean(0)).setColor(112).setBiomeName("Ocean").func_35479_b(-1.0F, 0.5F);
	public static final BiomeGenBase field_35485_c = (new BiomeGenPlains(1)).setColor(9286496).setBiomeName("Plains").func_35478_a(0.8F, 0.4F);
	public static final BiomeGenBase desert = (new BiomeGenDesert(2)).setColor(16421912).setBiomeName("Desert").setDisableRain().func_35478_a(2.0F, 0.0F).func_35479_b(0.1F, 0.2F);
	public static final BiomeGenBase field_35483_e = (new BiomeGenHills(3)).setColor(6316128).setBiomeName("Extreme Hills").func_35479_b(0.2F, 1.8F).func_35478_a(0.2F, 0.3F);
	public static final BiomeGenBase forest = (new BiomeGenForest(4)).setColor(353825).setBiomeName("Forest").func_4124_a(5159473).func_35478_a(0.7F, 0.8F);
	public static final BiomeGenBase taiga = (new BiomeGenTaiga(5)).setColor(747097).setBiomeName("Taiga").func_4124_a(5159473).func_35478_a(0.3F, 0.8F).func_35479_b(0.1F, 0.4F);
	public static final BiomeGenBase swampland = (new BiomeGenSwamp(6)).setColor(522674).setBiomeName("Swampland").func_4124_a(9154376).func_35479_b(-0.2F, 0.1F).func_35478_a(0.8F, 0.9F);
	public static final BiomeGenBase field_35487_i = (new BiomeGenRiver(7)).setColor(255).setBiomeName("River").func_35479_b(-0.5F, 0.0F);
	public static final BiomeGenBase hell = (new BiomeGenHell(8)).setColor(16711680).setBiomeName("Hell").setDisableRain();
	public static final BiomeGenBase sky = (new BiomeGenSky(9)).setColor(8421631).setBiomeName("Sky").setDisableRain();
	public String biomeName;
	public int color;
	public byte topBlock;
	public byte fillerBlock;
	public int field_6502_q;
	public float field_35492_q;
	public float field_35491_r;
	public float field_35490_s;
	public float field_35489_t;
	public BiomeDecorator field_35488_u;
	protected List spawnableMonsterList;
	protected List spawnableCreatureList;
	protected List spawnableWaterCreatureList;
	private boolean enableSnow;
	private boolean enableRain;
	public final int field_35494_y;
	protected WorldGenTrees field_35493_z;
	protected WorldGenBigTree field_35480_A;
	protected WorldGenForest field_35481_B;
	protected WorldGenSwamp field_35482_C;


	protected BiomeGenBase(int var1) {
		this.topBlock = (byte)Block.grass.blockID;
		this.fillerBlock = (byte)Block.dirt.blockID;
		this.field_6502_q = 5169201;
		this.field_35492_q = 0.1F;
		this.field_35491_r = 0.3F;
		this.field_35490_s = 0.5F;
		this.field_35489_t = 0.5F;
		this.spawnableMonsterList = new ArrayList();
		this.spawnableCreatureList = new ArrayList();
		this.spawnableWaterCreatureList = new ArrayList();
		this.enableRain = true;
		this.field_35493_z = new WorldGenTrees();
		this.field_35480_A = new WorldGenBigTree();
		this.field_35481_B = new WorldGenForest();
		this.field_35482_C = new WorldGenSwamp();
		this.field_35494_y = var1;
		field_35486_a[var1] = this;
		this.field_35488_u = this.func_35475_a();
		this.spawnableCreatureList.add(new SpawnListEntry(EntitySheep.class, 12, 4, 4));
		this.spawnableCreatureList.add(new SpawnListEntry(EntityPig.class, 10, 4, 4));
		this.spawnableCreatureList.add(new SpawnListEntry(EntityChicken.class, 10, 4, 4));
		this.spawnableCreatureList.add(new SpawnListEntry(EntityCow.class, 8, 4, 4));
		this.spawnableMonsterList.add(new SpawnListEntry(EntitySpider.class, 10, 4, 4));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityZombie.class, 10, 4, 4));
		this.spawnableMonsterList.add(new SpawnListEntry(EntitySkeleton.class, 10, 4, 4));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityCreeper.class, 10, 4, 4));
		this.spawnableMonsterList.add(new SpawnListEntry(EntitySlime.class, 10, 4, 4));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityEnderman.class, 2, 4, 4));
		this.spawnableWaterCreatureList.add(new SpawnListEntry(EntitySquid.class, 10, 4, 4));
	}

	protected BiomeDecorator func_35475_a() {
		return new BiomeDecorator(this);
	}

	private BiomeGenBase func_35478_a(float var1, float var2) {
		this.field_35490_s = var1;
		this.field_35489_t = var2;
		return this;
	}

	private BiomeGenBase func_35479_b(float var1, float var2) {
		this.field_35492_q = var1;
		this.field_35491_r = var2;
		return this;
	}

	private BiomeGenBase setDisableRain() {
		this.enableRain = false;
		return this;
	}

	public WorldGenerator getRandomWorldGenForTrees(Random var1) {
		return (WorldGenerator)(var1.nextInt(10) == 0?this.field_35480_A:this.field_35493_z);
	}

	//Spout start
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
	//Spout end

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
		if(var1 < -1.0F) {
			var1 = -1.0F;
		}

		if(var1 > 1.0F) {
			var1 = 1.0F;
		}

		return Color.getHSBColor(0.62222224F - var1 * 0.05F, 0.5F + var1 * 0.1F, 1.0F).getRGB();
	}

	public List getSpawnableList(EnumCreatureType var1) {
		return var1 == EnumCreatureType.monster?this.spawnableMonsterList:(var1 == EnumCreatureType.creature?this.spawnableCreatureList:(var1 == EnumCreatureType.waterCreature?this.spawnableWaterCreatureList:null));
	}

	public boolean getEnableSnow() {
		return this.enableSnow;
	}

	public boolean canSpawnLightningBolt() {
		return this.enableSnow?false:this.enableRain;
	}

	public float getBiome() {
		return 0.1F;
	}

	public final int func_35476_e() {
		return (int)(this.field_35489_t * 65536.0F);
	}

	public final int func_35474_f() {
		return (int)(this.field_35490_s * 65536.0F);
	}

	public void func_35477_a(World var1, Random var2, int var3, int var4) {
		this.field_35488_u.func_35881_a(var1, var2, var3, var4);
	}

}
