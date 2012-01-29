package net.minecraft.src;

public class RenderCow extends RenderLiving {
	public RenderCow(ModelBase modelbase, float f) {
		super(modelbase, f);
	}

	public void renderCow(EntityCow entitycow, double d, double d1, double d2,
	        float f, float f1) {
		super.doRenderLiving(entitycow, d, d1, d2, f, f1);
	}

	public void doRenderLiving(EntityLiving entityliving, double d, double d1, double d2,
	        float f, float f1) {
		renderCow((EntityCow)entityliving, d, d1, d2, f, f1);
	}

	public void doRender(Entity entity, double d, double d1, double d2,
	        float f, float f1) {
		renderCow((EntityCow)entity, d, d1, d2, f, f1);
	}
}
