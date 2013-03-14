package net.minecraft.src;

import org.lwjgl.opengl.GL11;
// MCPatcher Start
import com.prupe.mcpatcher.mod.MobRandomizer;
// MCPatcher End
// Spout Start
import org.spoutcraft.client.config.Configuration;
// Spout End

public class RenderSheep extends RenderLiving {
	public RenderSheep(ModelBase par1ModelBase, ModelBase par2ModelBase, float par3) {
		super(par1ModelBase, par3);
		this.setRenderPassModel(par2ModelBase);
	}

	protected int setWoolColorAndRender(EntitySheep par1EntitySheep, int par2, float par3) {
		if (par2 == 0 && !par1EntitySheep.getSheared()) {
			// Spout Start
			if (Configuration.isRandomMobTextures()) {
				this.loadTexture(MobRandomizer.randomTexture((Object)par1EntitySheep, "/mob/sheep_fur.png"));
			} else {
				loadTexture(par1EntitySheep.getCustomTexture(org.spoutcraft.api.entity.EntitySkinType.SHEEP_FUR, "/mob/sheep_fur.png"));
			}
			// Spout End
			this.loadTexture("/mob/sheep_fur.png");
			float var4 = 1.0F;
			int var5 = par1EntitySheep.getFleeceColor();
			GL11.glColor3f(var4 * EntitySheep.fleeceColorTable[var5][0], var4 * EntitySheep.fleeceColorTable[var5][1], var4 * EntitySheep.fleeceColorTable[var5][2]);
			return 1;
		} else {
			return -1;
		}
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
		return this.setWoolColorAndRender((EntitySheep)par1EntityLiving, par2, par3);
	}
}
