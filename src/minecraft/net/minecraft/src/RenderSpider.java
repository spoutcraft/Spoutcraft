// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;
//Spout Start
import org.getspout.spout.entity.EntitySkinType;
//Spout End
import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            RenderLiving, ModelSpider, EntitySpider, EntityLiving

public class RenderSpider extends RenderLiving
{

    public RenderSpider()
    {
        super(new ModelSpider(), 1.0F);
        setRenderPassModel(new ModelSpider());
    }

    protected float setSpiderDeathMaxRotation(EntitySpider entityspider)
    {
        return 180F;
    }

    protected boolean setSpiderEyeBrightness(EntitySpider entityspider, int i, float f)
    {
        if(i != 0)
        {
            return false;
        }
        if(i != 0)
        {
            return false;
        } else
        {
        	//Spout Start
            loadTexture(EntitySkinType.getTexture(EntitySkinType.SPIDER_EYES, entityspider, "/mob/spider_eyes.png"));
            //Spout End
            float f1 = (1.0F - entityspider.getEntityBrightness(1.0F)) * 0.5F;
            GL11.glEnable(3042 /*GL_BLEND*/);
            GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
            GL11.glBlendFunc(770, 771);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, f1);
            return true;
        }
    }

    protected float getDeathMaxRotation(EntityLiving entityliving)
    {
        return setSpiderDeathMaxRotation((EntitySpider)entityliving);
    }

    protected boolean shouldRenderPass(EntityLiving entityliving, int i, float f)
    {
        return setSpiderEyeBrightness((EntitySpider)entityliving, i, f);
    }
}
