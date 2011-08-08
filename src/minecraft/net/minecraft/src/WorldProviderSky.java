// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

//Spout Start
import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.gui.Color;
//Spout End

// Referenced classes of package net.minecraft.src:
//            WorldProvider, WorldChunkManagerHell, BiomeGenBase, ChunkProviderSky, 
//            World, MathHelper, Vec3D, Block, 
//            Material, IChunkProvider

public class WorldProviderSky extends WorldProvider
{

    public WorldProviderSky()
    {
    }

    public void registerWorldChunkManager()
    {
        worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.sky, 0.5D, 0.0D);
        worldType = 1;
    }

    public IChunkProvider getChunkProvider()
    {
        return new ChunkProviderSky(worldObj, worldObj.getRandomSeed());
    }

    public float calculateCelestialAngle(long l, float f)
    {
        return 0.0F;
    }

    public float[] calcSunriseSunsetColors(float f, float f1)
    {
        return null;
    }

    public Vec3D func_4096_a(float f, float f1)
    {
        int i = 0x8080a0;
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
        float f3 = (float)(i >> 16 & 0xff) / 255F;
        float f4 = (float)(i >> 8 & 0xff) / 255F;
        float f5 = (float)(i & 0xff) / 255F;
        Color fog = SpoutClient.getInstance().getSkyManager().getFogColor();
        if(fog!=null){
            f3 = fog.getRedF();
            f4 = fog.getGreenF();
            f5 = fog.getBlueF();
        }
        //Spout End
        f3 *= f2 * 0.94F + 0.06F;
        f4 *= f2 * 0.94F + 0.06F;
        f5 *= f2 * 0.91F + 0.09F;
        return Vec3D.createVector(f3, f4, f5);
    }

    public boolean func_28112_c()
    {
        return false;
    }

    public float getCloudHeight()
    {
        return 8F;
    }

    public boolean canCoordinateBeSpawn(int i, int j)
    {
        int k = worldObj.getFirstUncoveredBlock(i, j);
        if(k == 0)
        {
            return false;
        } else
        {
            return Block.blocksList[k].blockMaterial.getIsSolid();
        }
    }
}
