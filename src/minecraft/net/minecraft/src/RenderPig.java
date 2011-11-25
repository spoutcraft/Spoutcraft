package net.minecraft.src;

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

	protected boolean renderSaddledPig(EntityPig var1, int var2, float var3) {
		//Spout Start
		  loadTexture(EntitySkinType.getTexture(EntitySkinType.PIG_SADDLE, var1, "/mob/saddle.png"));
	 	//Spout End
		return var2 == 0 && var1.getSaddled();
	}

	// $FF: synthetic method
	// $FF: bridge method
	protected boolean shouldRenderPass(EntityLiving var1, int var2, float var3) {
		return this.renderSaddledPig((EntityPig)var1, var2, var3);
	}
}
