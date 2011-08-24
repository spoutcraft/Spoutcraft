// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import org.getspout.spout.entity.EntitySkinType;
import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//			RenderLiving, EntitySheep, ModelBase, EntityLiving

public class RenderSheep extends RenderLiving
{

	public RenderSheep(ModelBase modelbase, ModelBase modelbase1, float f)
	{
		super(modelbase, f);
		setRenderPassModel(modelbase1);
	}

	protected boolean setWoolColorAndRender(EntitySheep entitysheep, int i, float f)
	{
		if(i == 0 && !entitysheep.getSheared())
		{
			//Spout Start
			loadTexture(EntitySkinType.getTexture(EntitySkinType.SHEEP_FUR, entitysheep, "/mob/sheep_fur.png"));
			//Spout End
			float f1 = entitysheep.getEntityBrightness(f);
			int j = entitysheep.getFleeceColor();
			GL11.glColor3f(f1 * EntitySheep.fleeceColorTable[j][0], f1 * EntitySheep.fleeceColorTable[j][1], f1 * EntitySheep.fleeceColorTable[j][2]);
			return true;
		} else
		{
			return false;
		}
	}

	protected boolean shouldRenderPass(EntityLiving entityliving, int i, float f)
	{
		return setWoolColorAndRender((EntitySheep)entityliving, i, f);
	}
}
