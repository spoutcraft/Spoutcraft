package net.minecraft.src;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntitySpider;
import net.minecraft.src.ModelSpider;
import net.minecraft.src.RenderLiving;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

//Spout Start
import org.getspout.spout.entity.EntitySkinType;
//Spout End

public class RenderSpider extends RenderLiving {

	public RenderSpider() {
		super(new ModelSpider(), 1.0F);
		this.setRenderPassModel(new ModelSpider());
	}

	protected float setSpiderDeathMaxRotation(EntitySpider var1) {
		return 180.0F;
	}

	protected boolean setSpiderEyeBrightness(EntitySpider var1, int var2, float var3) {
		if(var2 != 0) {
			return false;
		} else if(var2 != 0) {
			return false;
		} else {
			//Spout Start
				loadTexture(EntitySkinType.getTexture(EntitySkinType.SPIDER_EYES, var1, "/mob/spider_eyes.png"));
				//Spout End
			float var4 = 1.0F;
			GL11.glEnable(3042 /*GL_BLEND*/);
			GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
			GL11.glBlendFunc(1, 1);
			char var5 = '\uf0f0';
			int var6 = var5 % 65536;
			int var7 = var5 / 65536;
			GL13.glMultiTexCoord2f('\u84c1', (float)var6 / 1.0F, (float)var7 / 1.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, var4);
			return true;
		}
	}

	protected void func_35446_a(EntitySpider var1, float var2) {
		float var3 = var1.func_35188_k_();
		GL11.glScalef(var3, var3, var3);
	}

	// $FF: synthetic method
	// $FF: bridge method
	protected void preRenderCallback(EntityLiving var1, float var2) {
		this.func_35446_a((EntitySpider)var1, var2);
	}

	// $FF: synthetic method
	// $FF: bridge method
	protected float getDeathMaxRotation(EntityLiving var1) {
		return this.setSpiderDeathMaxRotation((EntitySpider)var1);
	}

	// $FF: synthetic method
	// $FF: bridge method
	protected boolean shouldRenderPass(EntityLiving var1, int var2, float var3) {
		return this.setSpiderEyeBrightness((EntitySpider)var1, var2, var3);
	}
}
