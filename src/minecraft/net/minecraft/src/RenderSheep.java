package net.minecraft.src;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntitySheep;
import net.minecraft.src.ModelBase;
import net.minecraft.src.RenderLiving;
import org.lwjgl.opengl.GL11;

//Spout Start
import org.getspout.spout.entity.EntitySkinType;
//Spout End

public class RenderSheep extends RenderLiving {

	public RenderSheep(ModelBase var1, ModelBase var2, float var3) {
		super(var1, var3);
		this.setRenderPassModel(var2);
	}

	protected boolean setWoolColorAndRender(EntitySheep var1, int var2, float var3) {
		if(var2 == 0 && !var1.getSheared()) {
			//Spout Start
			loadTexture(EntitySkinType.getTexture(EntitySkinType.SHEEP_FUR, entitysheep, "/mob/sheep_fur.png"));
			//Spout End
			float var4 = 1.0F;
			int var5 = var1.getFleeceColor();
			GL11.glColor3f(var4 * EntitySheep.fleeceColorTable[var5][0], var4 * EntitySheep.fleeceColorTable[var5][1], var4 * EntitySheep.fleeceColorTable[var5][2]);
			return true;
		} else {
			return false;
		}
	}

	// $FF: synthetic method
	// $FF: bridge method
	protected boolean shouldRenderPass(EntityLiving var1, int var2, float var3) {
		return this.setWoolColorAndRender((EntitySheep)var1, var2, var3);
	}
}
