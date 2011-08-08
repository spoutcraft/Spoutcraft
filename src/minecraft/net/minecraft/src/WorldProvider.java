// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.gui.Color; //Spout

// Referenced classes of package net.minecraft.src:
//            WorldChunkManager, ChunkProviderGenerate, World, Block, 
//            MathHelper, Vec3D, WorldProviderHell, WorldProviderSurface, 
//            WorldProviderSky, IChunkProvider

public abstract class WorldProvider
{

    public WorldProvider()
    {
        isNether = false;
        isHellWorld = false;
        hasNoSky = false;
        lightBrightnessTable = new float[16];
        worldType = 0;
        colorsSunriseSunset = new float[4];
    }

    public final void registerWorld(World world)
    {
        worldObj = world;
        registerWorldChunkManager();
        generateLightBrightnessTable();
    }

    protected void generateLightBrightnessTable()
    {
        float f = 0.05F;
        for(int i = 0; i <= 15; i++)
        {
            float f1 = 1.0F - (float)i / 15F;
            lightBrightnessTable[i] = ((1.0F - f1) / (f1 * 3F + 1.0F)) * (1.0F - f) + f;
        }

    }

    protected void registerWorldChunkManager()
    {
        worldChunkMgr = new WorldChunkManager(worldObj);
    }

    public IChunkProvider getChunkProvider()
    {
        return new ChunkProviderGenerate(worldObj, worldObj.getRandomSeed());
    }

    public boolean canCoordinateBeSpawn(int i, int j)
    {
        int k = worldObj.getFirstUncoveredBlock(i, j);
        return k == Block.sand.blockID;
    }

    public float calculateCelestialAngle(long l, float f)
    {
        int i = (int)(l % 24000L);
        float f1 = ((float)i + f) / 24000F - 0.25F;
        if(f1 < 0.0F)
        {
            f1++;
        }
        if(f1 > 1.0F)
        {
            f1--;
        }
        float f2 = f1;
        f1 = 1.0F - (float)((Math.cos((double)f1 * 3.1415926535897931D) + 1.0D) / 2D);
        f1 = f2 + (f1 - f2) / 3F;
        return f1;
    }

    public float[] calcSunriseSunsetColors(float f, float f1)
    {
        float f2 = 0.4F;
        float f3 = MathHelper.cos(f * 3.141593F * 2.0F) - 0.0F;
        float f4 = -0F;
        if(f3 >= f4 - f2 && f3 <= f4 + f2)
        {
            float f5 = ((f3 - f4) / f2) * 0.5F + 0.5F;
            float f6 = 1.0F - (1.0F - MathHelper.sin(f5 * 3.141593F)) * 0.99F;
            f6 *= f6;
            colorsSunriseSunset[0] = f5 * 0.3F + 0.7F;
            colorsSunriseSunset[1] = f5 * f5 * 0.7F + 0.2F;
            colorsSunriseSunset[2] = f5 * f5 * 0.0F + 0.2F;
            colorsSunriseSunset[3] = f6;
            return colorsSunriseSunset;
        } else
        {
            return null;
        }
    }

    public Vec3D func_4096_a(float f, float f1)
    {
        float f2 = MathHelper.cos(f * 3.141593F * 2.0F) * 2.0F + 0.5F;
        if(f2 < 0.0F)
        {
            f2 = 0.0F;
        }
        if(f2 > 1.0F)
        {
            f2 = 1.0F;
        }
        //Spout Start
        
        float f3 = 0.7529412F;
        float f4 = 0.8470588F;
        float f5 = 1.0F;
        Color fogColor = SpoutClient.getInstance().getSkyManager().getFogColor();
        if(fogColor != null){
            f3 = fogColor.getRedF();
            f4 = fogColor.getGreenF();
            f5 = fogColor.getBlueF();
        }
        //Spout End
        f3 *= f2 * 0.94F + 0.06F;
        f4 *= f2 * 0.94F + 0.06F;
        f5 *= f2 * 0.91F + 0.09F;
        return Vec3D.createVector(f3, f4, f5);
    }

    public boolean canRespawnHere()
    {
        return true;
    }

    public static WorldProvider getProviderForDimension(int i)
    {
        if(i == -1)
        {
            return new WorldProviderHell();
        }
        if(i == 0)
        {
            return new WorldProviderSurface();
        }
        if(i == 1)
        {
            return new WorldProviderSky();
        } else
        {
            return null;
        }
    }

    public float getCloudHeight()
    {
        return 108F;
    }

    public boolean func_28112_c()
    {
        return true;
    }

    public World worldObj;
    public WorldChunkManager worldChunkMgr;
    public boolean isNether;
    public boolean isHellWorld;
    public boolean hasNoSky;
    public float lightBrightnessTable[];
    public int worldType;
    private float colorsSunriseSunset[];
}
