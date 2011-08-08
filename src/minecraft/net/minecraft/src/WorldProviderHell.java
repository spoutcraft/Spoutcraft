// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.gui.Color; //Spout

// Referenced classes of package net.minecraft.src:
//            WorldProvider, WorldChunkManagerHell, BiomeGenBase, Vec3D, 
//            ChunkProviderHell, World, Block, IChunkProvider

public class WorldProviderHell extends WorldProvider
{

    public WorldProviderHell()
    {
    }

    public void registerWorldChunkManager()
    {
        worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.hell, 1.0D, 0.0D);
        isNether = true;
        isHellWorld = true;
        hasNoSky = true;
        worldType = -1;
    }

    public Vec3D func_4096_a(float f, float f1)
    {
        //Spout Start
        Color fogColor = SpoutClient.getInstance().getSkyManager().getFogColor();
        if(fogColor != null) {
            return Vec3D.createVector(fogColor.getRedF(), fogColor.getGreenF(), fogColor.getBlueF());
        } else {
            return Vec3D.createVector(0.20000000298023224D, 0.029999999329447746D, 0.029999999329447746D);
        }
        //Spout End
    }

    protected void generateLightBrightnessTable()
    {
        float f = 0.1F;
        for(int i = 0; i <= 15; i++)
        {
            float f1 = 1.0F - (float)i / 15F;
            lightBrightnessTable[i] = ((1.0F - f1) / (f1 * 3F + 1.0F)) * (1.0F - f) + f;
        }

    }

    public IChunkProvider getChunkProvider()
    {
        return new ChunkProviderHell(worldObj, worldObj.getRandomSeed());
    }

    public boolean canCoordinateBeSpawn(int i, int j)
    {
        int k = worldObj.getFirstUncoveredBlock(i, j);
        if(k == Block.bedrock.blockID)
        {
            return false;
        }
        if(k == 0)
        {
            return false;
        }
        return Block.opaqueCubeLookup[k];
    }

    public float calculateCelestialAngle(long l, float f)
    {
        return 0.5F;
    }

    public boolean canRespawnHere()
    {
        return false;
    }
}
