package net.minecraft.src;

import net.minecraft.src.Entity;
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

	protected int setWoolColorAndRender(EntitySheep var1, int var2, float var3) {
		if(var2 == 0 && !var1.getSheared()) {
			//Spout Start
			loadTexture(EntitySkinType.getTexture(EntitySkinType.SHEEP_FUR, var1, "/mob/sheep_fur.png"));
			//Spout End
			float var4 = 1.0F;
			int var5 = var1.getFleeceColor();
			GL11.glColor3f(var4 * EntitySheep.fleeceColorTable[var5][0], var4 * EntitySheep.fleeceColorTable[var5][1], var4 * EntitySheep.fleeceColorTable[var5][2]);
			return 1;
		} else {
			return -1;
		}
	}

	public void func_40271_a(EntitySheep var1, double var2, double var4, double var6, float var8, float var9) {
		super.doRenderLiving(var1, var2, var4, var6, var8, var9);
	}

	// $FF: synthetic method
	// $FF: bridge method
	protected int shouldRenderPass(EntityLiving var1, int var2, float var3) {
		return this.setWoolColorAndRender((EntitySheep)var1, var2, var3);
	}

	// $FF: synthetic method
	// $FF: bridge method
	public void doRenderLiving(EntityLiving var1, double var2, double var4, double var6, float var8, float var9) {
		this.func_40271_a((EntitySheep)var1, var2, var4, var6, var8, var9);
	}

	// $FF: synthetic method
	// $FF: bridge method
	public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9) {
		this.func_40271_a((EntitySheep)var1, var2, var4, var6, var8, var9);
	}
}
