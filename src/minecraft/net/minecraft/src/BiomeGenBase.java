// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            Block, BlockGrass, SpawnListEntry, EntitySpider, 
//            EntityZombie, EntitySkeleton, EntityCreeper, EntitySlime, 
//            EntitySheep, EntityPig, EntityChicken, EntityCow, 
//            EntitySquid, WorldGenBigTree, WorldGenTrees, EnumCreatureType, 
//            BiomeGenRainforest, BiomeGenSwamp, BiomeGenForest, BiomeGenDesert, 
//            BiomeGenTaiga, BiomeGenHell, BiomeGenSky, WorldGenerator

public class BiomeGenBase
{

    protected BiomeGenBase()
    {
        topBlock = (byte)Block.grass.blockID;
        fillerBlock = (byte)Block.dirt.blockID;
        field_6502_q = 0x4ee031;
        spawnableMonsterList = new ArrayList();
        spawnableCreatureList = new ArrayList();
        spawnableWaterCreatureList = new ArrayList();
        enableRain = true;
        spawnableMonsterList.add(new SpawnListEntry(net.minecraft.src.EntitySpider.class, 10));
        spawnableMonsterList.add(new SpawnListEntry(net.minecraft.src.EntityZombie.class, 10));
        spawnableMonsterList.add(new SpawnListEntry(net.minecraft.src.EntitySkeleton.class, 10));
        spawnableMonsterList.add(new SpawnListEntry(net.minecraft.src.EntityCreeper.class, 10));
        spawnableMonsterList.add(new SpawnListEntry(net.minecraft.src.EntitySlime.class, 10));
        spawnableCreatureList.add(new SpawnListEntry(net.minecraft.src.EntitySheep.class, 12));
        spawnableCreatureList.add(new SpawnListEntry(net.minecraft.src.EntityPig.class, 10));
        spawnableCreatureList.add(new SpawnListEntry(net.minecraft.src.EntityChicken.class, 10));
        spawnableCreatureList.add(new SpawnListEntry(net.minecraft.src.EntityCow.class, 8));
        spawnableWaterCreatureList.add(new SpawnListEntry(net.minecraft.src.EntitySquid.class, 10));
    }

    private BiomeGenBase setDisableRain()
    {
        enableRain = false;
        return this;
    }

    public static void generateBiomeLookup()
    {
        for(int i = 0; i < 64; i++)
        {
            for(int j = 0; j < 64; j++)
            {
                biomeLookupTable[i + j * 64] = getBiome((float)i / 63F, (float)j / 63F);
            }

        }

        desert.topBlock = desert.fillerBlock = (byte)Block.sand.blockID;
        iceDesert.topBlock = iceDesert.fillerBlock = (byte)Block.sand.blockID;
    }

    public WorldGenerator getRandomWorldGenForTrees(Random random)
    {
        if(random.nextInt(10) == 0)
        {
            return new WorldGenBigTree();
        } else
        {
            return new WorldGenTrees();
        }
    }

    protected BiomeGenBase setEnableSnow()
    {
        enableSnow = true;
        return this;
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

    protected BiomeGenBase setBiomeName(String s)
    {
        biomeName = s;
        return this;
    }

    protected BiomeGenBase func_4124_a(int i)
    {
        field_6502_q = i;
        return this;
    }

    protected BiomeGenBase setColor(int i)
    {
        color = i;
        return this;
    }

    public static BiomeGenBase getBiomeFromLookup(double d, double d1)
    {
        int i = (int)(d * 63D);
        int j = (int)(d1 * 63D);
        return biomeLookupTable[i + j * 64];
    }

    public static BiomeGenBase getBiome(float f, float f1)
    {
        f1 *= f;
        if(f < 0.1F)
        {
            return tundra;
        }
        if(f1 < 0.2F)
        {
            if(f < 0.5F)
            {
                return tundra;
            }
            if(f < 0.95F)
            {
                return savanna;
            } else
            {
                return desert;
            }
        }
        if(f1 > 0.5F && f < 0.7F)
        {
            return swampland;
        }
        if(f < 0.5F)
        {
            return taiga;
        }
        if(f < 0.97F)
        {
            if(f1 < 0.35F)
            {
                return shrubland;
            } else
            {
                return forest;
            }
        }
        if(f1 < 0.45F)
        {
            return plains;
        }
        if(f1 < 0.9F)
        {
            return seasonalForest;
        } else
        {
            return rainforest;
        }
    }

    public int getSkyColorByTemp(float f)
    {
        f /= 3F;
        if(f < -1F)
        {
            f = -1F;
        }
        if(f > 1.0F)
        {
            f = 1.0F;
        }
        return java.awt.Color.getHSBColor(0.6222222F - f * 0.05F, 0.5F + f * 0.1F, 1.0F).getRGB();
    }

    public List getSpawnableList(EnumCreatureType enumcreaturetype)
    {
        if(enumcreaturetype == EnumCreatureType.monster)
        {
            return spawnableMonsterList;
        }
        if(enumcreaturetype == EnumCreatureType.creature)
        {
            return spawnableCreatureList;
        }
        if(enumcreaturetype == EnumCreatureType.waterCreature)
        {
            return spawnableWaterCreatureList;
        } else
        {
            return null;
        }
    }

    public boolean getEnableSnow()
    {
        return enableSnow;
    }

    public boolean canSpawnLightningBolt()
    {
        if(enableSnow)
        {
            return false;
        } else
        {
            return enableRain;
        }
    }

    public static final BiomeGenBase rainforest = (new BiomeGenRainforest()).setColor(0x8fa36).setBiomeName("Rainforest").func_4124_a(0x1ff458);
    public static final BiomeGenBase swampland = (new BiomeGenSwamp()).setColor(0x7f9b2).setBiomeName("Swampland").func_4124_a(0x8baf48);
    public static final BiomeGenBase seasonalForest = (new BiomeGenBase()).setColor(0x9be023).setBiomeName("Seasonal Forest");
    public static final BiomeGenBase forest = (new BiomeGenForest()).setColor(0x56621).setBiomeName("Forest").func_4124_a(0x4eba31);
    public static final BiomeGenBase savanna = (new BiomeGenDesert()).setColor(0xd9e023).setBiomeName("Savanna");
    public static final BiomeGenBase shrubland = (new BiomeGenBase()).setColor(0xa1ad20).setBiomeName("Shrubland");
    public static final BiomeGenBase taiga = (new BiomeGenTaiga()).setColor(0x2eb153).setBiomeName("Taiga").setEnableSnow().func_4124_a(0x7bb731);
    public static final BiomeGenBase desert = (new BiomeGenDesert()).setColor(0xfa9418).setBiomeName("Desert").setDisableRain();
    public static final BiomeGenBase plains = (new BiomeGenDesert()).setColor(0xffd910).setBiomeName("Plains");
    public static final BiomeGenBase iceDesert = (new BiomeGenDesert()).setColor(0xffed93).setBiomeName("Ice Desert").setEnableSnow().setDisableRain().func_4124_a(0xc4d339);
    public static final BiomeGenBase tundra = (new BiomeGenBase()).setColor(0x57ebf9).setBiomeName("Tundra").setEnableSnow().func_4124_a(0xc4d339);
    public static final BiomeGenBase hell = (new BiomeGenHell()).setColor(0xff0000).setBiomeName("Hell").setDisableRain();
    public static final BiomeGenBase sky = (new BiomeGenSky()).setColor(0x8080ff).setBiomeName("Sky").setDisableRain();
    public String biomeName;
    public int color;
    public byte topBlock;
    public byte fillerBlock;
    public int field_6502_q;
    protected List spawnableMonsterList;
    protected List spawnableCreatureList;
    protected List spawnableWaterCreatureList;
    private boolean enableSnow;
    private boolean enableRain;
    private static BiomeGenBase biomeLookupTable[] = new BiomeGenBase[4096];

    static 
    {
        generateBiomeLookup();
    }
}
