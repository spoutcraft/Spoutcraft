// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.*;
import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            TileEntitySign, TileEntitySignRenderer, TileEntityMobSpawner, TileEntityMobSpawnerRenderer, 
//            TileEntityPiston, TileEntityRendererPiston, TileEntitySpecialRenderer, TileEntity, 
//            EntityLiving, World, FontRenderer, RenderEngine

public class TileEntityRenderer
{

    private TileEntityRenderer()
    {
        specialRendererMap = new HashMap();
        specialRendererMap.put(net.minecraft.src.TileEntitySign.class, new TileEntitySignRenderer());
        specialRendererMap.put(net.minecraft.src.TileEntityMobSpawner.class, new TileEntityMobSpawnerRenderer());
        specialRendererMap.put(net.minecraft.src.TileEntityPiston.class, new TileEntityRendererPiston());
        TileEntitySpecialRenderer tileentityspecialrenderer;
        for(Iterator iterator = specialRendererMap.values().iterator(); iterator.hasNext(); tileentityspecialrenderer.setTileEntityRenderer(this))
        {
            tileentityspecialrenderer = (TileEntitySpecialRenderer)iterator.next();
        }

    }

    public TileEntitySpecialRenderer getSpecialRendererForClass(Class class1)
    {
        TileEntitySpecialRenderer tileentityspecialrenderer = (TileEntitySpecialRenderer)specialRendererMap.get(class1);
        if(tileentityspecialrenderer == null && class1 != (net.minecraft.src.TileEntity.class))
        {
            tileentityspecialrenderer = getSpecialRendererForClass(class1.getSuperclass());
            specialRendererMap.put(class1, tileentityspecialrenderer);
        }
        return tileentityspecialrenderer;
    }

    public boolean hasSpecialRenderer(TileEntity tileentity)
    {
        return getSpecialRendererForEntity(tileentity) != null;
    }

    public TileEntitySpecialRenderer getSpecialRendererForEntity(TileEntity tileentity)
    {
        if(tileentity == null)
        {
            return null;
        } else
        {
            return getSpecialRendererForClass(tileentity.getClass());
        }
    }

    public void cacheActiveRenderInfo(World world, RenderEngine renderengine, FontRenderer fontrenderer, EntityLiving entityliving, float f)
    {
        if(worldObj != world)
        {
            func_31072_a(world);
        }
        renderEngine = renderengine;
        entityLivingPlayer = entityliving;
        fontRenderer = fontrenderer;
        playerYaw = entityliving.prevRotationYaw + (entityliving.rotationYaw - entityliving.prevRotationYaw) * f;
        playerPitch = entityliving.prevRotationPitch + (entityliving.rotationPitch - entityliving.prevRotationPitch) * f;
        playerX = entityliving.lastTickPosX + (entityliving.posX - entityliving.lastTickPosX) * (double)f;
        playerY = entityliving.lastTickPosY + (entityliving.posY - entityliving.lastTickPosY) * (double)f;
        playerZ = entityliving.lastTickPosZ + (entityliving.posZ - entityliving.lastTickPosZ) * (double)f;
    }

    public void renderTileEntity(TileEntity tileentity, float f)
    {
        if(tileentity.getDistanceFrom(playerX, playerY, playerZ) < 4096D)
        {
            float f1 = worldObj.getLightBrightness(tileentity.xCoord, tileentity.yCoord, tileentity.zCoord);
            GL11.glColor3f(f1, f1, f1);
            renderTileEntityAt(tileentity, (double)tileentity.xCoord - staticPlayerX, (double)tileentity.yCoord - staticPlayerY, (double)tileentity.zCoord - staticPlayerZ, f);
        }
    }

    public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, 
            float f)
    {
        TileEntitySpecialRenderer tileentityspecialrenderer = getSpecialRendererForEntity(tileentity);
        if(tileentityspecialrenderer != null)
        {
            tileentityspecialrenderer.renderTileEntityAt(tileentity, d, d1, d2, f);
        }
    }

    public void func_31072_a(World world)
    {
        worldObj = world;
        Iterator iterator = specialRendererMap.values().iterator();
        do
        {
            if(!iterator.hasNext())
            {
                break;
            }
            TileEntitySpecialRenderer tileentityspecialrenderer = (TileEntitySpecialRenderer)iterator.next();
            if(tileentityspecialrenderer != null)
            {
                tileentityspecialrenderer.func_31069_a(world);
            }
        } while(true);
    }

    public FontRenderer getFontRenderer()
    {
        return fontRenderer;
    }

    private Map specialRendererMap;
    public static TileEntityRenderer instance = new TileEntityRenderer();
    private FontRenderer fontRenderer;
    public static double staticPlayerX;
    public static double staticPlayerY;
    public static double staticPlayerZ;
    public RenderEngine renderEngine;
    public World worldObj;
    public EntityLiving entityLivingPlayer;
    public float playerYaw;
    public float playerPitch;
    public double playerX;
    public double playerY;
    public double playerZ;

}
