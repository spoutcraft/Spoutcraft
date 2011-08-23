// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            RenderLiving, EntityPig, ModelBase, EntityLiving

public class RenderPig extends RenderLiving
{

    public RenderPig(ModelBase modelbase, ModelBase modelbase1, float f)
    {
        super(modelbase, f);
        setRenderPassModel(modelbase1);
    }

    protected boolean renderSaddledPig(EntityPig entitypig, int i, float f)
    {
        loadTexture("/mob/saddle.png");
        return i == 0 && entitypig.getSaddled();
    }

    protected boolean shouldRenderPass(EntityLiving entityliving, int i, float f)
    {
        return renderSaddledPig((EntityPig)entityliving, i, f);
    }
}
