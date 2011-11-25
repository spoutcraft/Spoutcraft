package net.minecraft.src;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPig;
import net.minecraft.src.ModelBase;
import net.minecraft.src.RenderLiving;

//Spout Start
import org.getspout.spout.entity.EntitySkinType;
//Spout End

public class RenderPig extends RenderLiving {

	public RenderPig(ModelBase var1, ModelBase var2, float var3) {
		super(var1, var3);
		this.setRenderPassModel(var2);
	}

	protected int renderSaddledPig(EntityPig var1, int var2, float var3) {
		//Spout Start
        loadTexture(EntitySkinType.getTexture(EntitySkinType.PIG_SADDLE, var1, "/mob/saddle.png"));
    	//Spout End
		return var2 == 0 && var1.getSaddled()?1:-1;
	}

	public void func_40286_a(EntityPig var1, double var2, double var4, double var6, float var8, float var9) {
		super.doRenderLiving(var1, var2, var4, var6, var8, var9);
	}

	// $FF: synthetic method
	// $FF: bridge method
	protected int shouldRenderPass(EntityLiving var1, int var2, float var3) {
		return this.renderSaddledPig((EntityPig)var1, var2, var3);
	}

	// $FF: synthetic method
	// $FF: bridge method
	public void doRenderLiving(EntityLiving var1, double var2, double var4, double var6, float var8, float var9) {
		this.func_40286_a((EntityPig)var1, var2, var4, var6, var8, var9);
	}

	// $FF: synthetic method
	// $FF: bridge method
	public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9) {
		this.func_40286_a((EntityPig)var1, var2, var4, var6, var8, var9);
	}
}
