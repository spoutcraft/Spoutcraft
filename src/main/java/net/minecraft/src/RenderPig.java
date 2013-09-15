package net.minecraft.src;

public class RenderPig extends RenderLiving {
	private static final ResourceLocation field_110888_a = new ResourceLocation("textures/entity/pig/pig_saddle.png");
	private static final ResourceLocation field_110887_f = new ResourceLocation("textures/entity/pig/pig.png");
	public RenderPig(ModelBase par1ModelBase, ModelBase par2ModelBase, float par3) {
		super(par1ModelBase, par3);
		this.setRenderPassModel(par2ModelBase);
	}

	protected int renderSaddledPig(EntityPig par1EntityPig, int par2, float par3) {
		// Spout Start
		//ToDO: EntitySkinType's need to be fixed 
		//loadTexture(par1EntityPig.getCustomTexture(EntitySkinType.PIG_SADDLE, "/mob/saddle.png"));
		
		// Spout End
		if (par2 == 0 && par1EntityPig.getSaddled()) {
			// Spout Start - Unused
			//this.loadTexture("/mob/saddle.png");
			this.func_110776_a(field_110888_a);
			// Spout Endz
			return 1;
		} else {
			return -1;
		}
	}

	public void renderLivingPig(EntityPig par1EntityPig, double par2, double par4, double par6, float par8, float par9) {
		super.doRenderLiving(par1EntityPig, par2, par4, par6, par8, par9);
	}
	
	protected ResourceLocation func_110886_a(EntityPig par1EntityPig) {
		return field_110887_f;
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	protected int shouldRenderPass(EntityLivingBase par1EntityLivingBase, int par2, float par3) {
		return this.renderSaddledPig((EntityPig)par1EntityLivingBase, par2, par3);
	}

	protected ResourceLocation func_110775_a(Entity par1Entity) {
		return this.func_110886_a((EntityPig)par1Entity);
	}
}
