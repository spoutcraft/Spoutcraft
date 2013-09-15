package net.minecraft.src;

public class RenderPig extends RenderLiving {
	private static final ResourceLocation saddledPigTextures = new ResourceLocation("textures/entity/pig/pig_saddle.png");
	private static final ResourceLocation pigTextures = new ResourceLocation("textures/entity/pig/pig.png");
	
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
			this.bindTexture(saddledPigTextures);
			// Spout Endz
			return 1;
		} else {
			return -1;
		}
	}

	public void renderLivingPig(EntityPig par1EntityPig, double par2, double par4, double par6, float par8, float par9) {
		super.doRenderLiving(par1EntityPig, par2, par4, par6, par8, par9);
	}
	
	protected ResourceLocation getPigTextures(EntityPig par1EntityPig) {
		return pigTextures;
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	protected int shouldRenderPass(EntityLivingBase par1EntityLivingBase, int par2, float par3) {
		return this.renderSaddledPig((EntityPig)par1EntityLivingBase, par2, par3);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(Entity par1Entity) {
		return this.getPigTextures((EntityPig)par1Entity);
	}
}
