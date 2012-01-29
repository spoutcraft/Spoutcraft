package net.minecraft.src;

public class RenderBlaze extends RenderLiving {
	private int field_40278_c;

	public RenderBlaze() {
		super(new ModelBlaze(), 0.5F);
		field_40278_c = ((ModelBlaze)mainModel).func_40321_a();
	}

	public void renderBlaze(EntityBlaze entityblaze, double d, double d1, double d2,
	        float f, float f1) {
		int i = ((ModelBlaze)mainModel).func_40321_a();
		if (i != field_40278_c) {
			field_40278_c = i;
			mainModel = new ModelBlaze();
		}
		super.doRenderLiving(entityblaze, d, d1, d2, f, f1);
	}

	public void doRenderLiving(EntityLiving entityliving, double d, double d1, double d2,
	        float f, float f1) {
		renderBlaze((EntityBlaze)entityliving, d, d1, d2, f, f1);
	}

	public void doRender(Entity entity, double d, double d1, double d2,
	        float f, float f1) {
		renderBlaze((EntityBlaze)entity, d, d1, d2, f, f1);
	}
}
