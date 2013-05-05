package net.minecraft.src;

import org.lwjgl.opengl.GL11;
// MCPatcher Start
import com.prupe.mcpatcher.mod.MobRandomizer;
// MCPatcher End
// Spout Start
import org.spoutcraft.client.config.Configuration;
// Spout End

public class RenderSpider extends RenderLiving {
	public RenderSpider() {
		super(new ModelSpider(), 1.0F);
		this.setRenderPassModel(new ModelSpider());
	}

	protected float setSpiderDeathMaxRotation(EntitySpider par1EntitySpider) {
		return 180.0F;
	}

	/**
	 * Sets the spider's glowing eyes
	 */
	protected int setSpiderEyeBrightness(EntitySpider par1EntitySpider, int par2, float par3) {
		if (par2 != 0) {
			return -1;
		} else {
			// Spout Start
			if (Configuration.isRandomMobTextures()) {
			// MCPatcher Start
			this.loadTexture(MobRandomizer.randomTexture((EntityLiving)par1EntitySpider, "/mob/spider_eyes.png"));
			// MCPatcher End
			} else {
				loadTexture(par1EntitySpider.getCustomTexture(org.spoutcraft.api.entity.EntitySkinType.SPIDER_EYES, "/mob/spider_eyes.png"));
			}
			// Spout End
			float var4 = 1.0F;
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);

			if (par1EntitySpider.isInvisible()) {
				GL11.glDepthMask(false);
			} else {
				GL11.glDepthMask(true);
			}

			char var5 = 61680;
			int var6 = var5 % 65536;
			int var7 = var5 / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var6 / 1.0F, (float)var7 / 1.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, var4);
			return 1;
		}
	}

	protected void scaleSpider(EntitySpider par1EntitySpider, float par2) {
		float var3 = par1EntitySpider.spiderScaleAmount();
		GL11.glScalef(var3, var3, var3);
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args: entityLiving,
	 * partialTickTime
	 */
	protected void preRenderCallback(EntityLiving par1EntityLiving, float par2) {
		this.scaleSpider((EntitySpider)par1EntityLiving, par2);
	}

	protected float getDeathMaxRotation(EntityLiving par1EntityLiving) {
		return this.setSpiderDeathMaxRotation((EntitySpider)par1EntityLiving);
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
		return this.setSpiderEyeBrightness((EntitySpider)par1EntityLiving, par2, par3);
	}
}
