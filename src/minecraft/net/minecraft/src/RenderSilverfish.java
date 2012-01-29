package net.minecraft.src;

public class RenderSilverfish extends RenderLiving {
	public RenderSilverfish() {
		super(new ModelSilverfish(), 0.3F);
	}

	protected float getSilverfishDeathRotation(EntitySilverfish entitysilverfish) {
		return 180F;
	}

	public void renderSilverfish(EntitySilverfish entitysilverfish, double d, double d1, double d2,
	        float f, float f1) {
		super.doRenderLiving(entitysilverfish, d, d1, d2, f, f1);
	}

	protected int shouldSilverfishRenderPass(EntitySilverfish entitysilverfish, int i, float f) {
		return -1;
	}

	protected float getDeathMaxRotation(EntityLiving entityliving) {
		return getSilverfishDeathRotation((EntitySilverfish)entityliving);
	}

	protected int shouldRenderPass(EntityLiving entityliving, int i, float f) {
		return shouldSilverfishRenderPass((EntitySilverfish)entityliving, i, f);
	}

	public void doRenderLiving(EntityLiving entityliving, double d, double d1, double d2,
	        float f, float f1) {
		renderSilverfish((EntitySilverfish)entityliving, d, d1, d2, f, f1);
	}

	public void doRender(Entity entity, double d, double d1, double d2,
	        float f, float f1) {
		renderSilverfish((EntitySilverfish)entity, d, d1, d2, f, f1);
	}
}
