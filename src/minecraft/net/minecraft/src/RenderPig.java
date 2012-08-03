package net.minecraft.src;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPig;
import net.minecraft.src.ModelBase;
import net.minecraft.src.RenderLiving;
//Spout Start
import org.spoutcraft.spoutcraftapi.entity.EntitySkinType;
//Spout End

public class RenderPig extends RenderLiving {

	public RenderPig(ModelBase par1ModelBase, ModelBase par2ModelBase, float par3) {
		super(par1ModelBase, par3);
		this.setRenderPassModel(par2ModelBase);
	}

	protected int renderSaddledPig(EntityPig par1EntityPig, int par2, float par3) {
		//Spout Start
		loadTexture(par1EntityPig.getCustomTexture(EntitySkinType.PIG_SADDLE, "/mob/saddle.png"));
		//Spout End
		return par2 == 0 && par1EntityPig.getSaddled() ? 1 : -1;
	}

	public void func_77098_a(EntityPig par1EntityPig, double par2, double par4, double par6, float par8, float par9) {
		super.doRenderLiving(par1EntityPig, par2, par4, par6, par8, par9);
	}

	protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
		return this.renderSaddledPig((EntityPig)par1EntityLiving, par2, par3);
	}

	public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
		this.func_77098_a((EntityPig)par1EntityLiving, par2, par4, par6, par8, par9);
	}

	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		this.func_77098_a((EntityPig)par1Entity, par2, par4, par6, par8, par9);
	}
}
