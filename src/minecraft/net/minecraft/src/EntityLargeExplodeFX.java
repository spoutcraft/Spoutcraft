// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.Random;
import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            EntityFX, RenderEngine, RenderHelper, Tessellator, 
//            World

public class EntityLargeExplodeFX extends EntityFX
{

    public EntityLargeExplodeFX(RenderEngine renderengine, World world, double d, double d1, double d2, double d3, double d4, double d5)
    {
        super(world, d, d1, d2, 0.0D, 0.0D, 0.0D);
        field_35130_a = 0;
        field_35129_ay = 0;
        field_35128_az = renderengine;
        field_35129_ay = 6 + rand.nextInt(4);
        particleRed = particleGreen = particleBlue = rand.nextFloat() * 0.6F + 0.4F;
        field_35131_aA = 1.0F - (float)d3 * 0.5F;
    }

    public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5)
    {
        int i = (int)((((float)field_35130_a + f) * 15F) / (float)field_35129_ay);
        if(i > 15)
        {
            return;
        } else
        {
            field_35128_az.bindTexture(field_35128_az.getTexture("/misc/explosion.png"));
            float f6 = (float)(i % 4) / 4F;
            float f7 = f6 + 0.24975F;
            float f8 = (float)(i / 4) / 4F;
            float f9 = f8 + 0.24975F;
            float f10 = 2.0F * field_35131_aA;
            float f11 = (float)((prevPosX + (posX - prevPosX) * (double)f) - interpPosX);
            float f12 = (float)((prevPosY + (posY - prevPosY) * (double)f) - interpPosY);
            float f13 = (float)((prevPosZ + (posZ - prevPosZ) * (double)f) - interpPosZ);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(2896 /*GL_LIGHTING*/);
            RenderHelper.disableStandardItemLighting();
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_F(particleRed, particleGreen, particleBlue, 1.0F);
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            tessellator.setBlendTexture(240);
            tessellator.addVertexWithUV(f11 - f1 * f10 - f4 * f10, f12 - f2 * f10, f13 - f3 * f10 - f5 * f10, f7, f9);
            tessellator.addVertexWithUV((f11 - f1 * f10) + f4 * f10, f12 + f2 * f10, (f13 - f3 * f10) + f5 * f10, f7, f8);
            tessellator.addVertexWithUV(f11 + f1 * f10 + f4 * f10, f12 + f2 * f10, f13 + f3 * f10 + f5 * f10, f6, f8);
            tessellator.addVertexWithUV((f11 + f1 * f10) - f4 * f10, f12 - f2 * f10, (f13 + f3 * f10) - f5 * f10, f6, f9);
            tessellator.draw();
            GL11.glPolygonOffset(0.0F, 0.0F);
            GL11.glEnable(2896 /*GL_LIGHTING*/);
            return;
        }
    }

    public int func_35115_a(float f)
    {
        return 61680;
    }

    public void onUpdate()
    {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        field_35130_a++;
        if(field_35130_a == field_35129_ay)
        {
            setEntityDead();
        }
    }

    public int getFXLayer()
    {
        return 3;
    }

    private int field_35130_a;
    private int field_35129_ay;
    private RenderEngine field_35128_az;
    private float field_35131_aA;
}
