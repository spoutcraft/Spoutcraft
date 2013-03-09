package net.minecraft.src;

import org.lwjgl.opengl.GL11;
// MCPatcher Start
import com.prupe.mcpatcher.mod.ColorizeEntity;
import com.prupe.mcpatcher.mod.MobRandomizer;
// MCPatcher End

public class RenderWolf extends RenderLiving {
	public RenderWolf(ModelBase par1ModelBase, ModelBase par2ModelBase, float par3) {
		super(par1ModelBase, par3);
		this.setRenderPassModel(par2ModelBase);
	}

	protected float getTailRotation(EntityWolf par1EntityWolf, float par2) {
		return par1EntityWolf.getTailRotation();
	}

	protected int func_82447_a(EntityWolf par1EntityWolf, int par2, float par3) {
		float var4;

		if (par2 == 0 && par1EntityWolf.getWolfShaking()) {
			var4 = par1EntityWolf.getBrightness(par3) * par1EntityWolf.getShadingWhileShaking(par3);
			this.loadTexture(par1EntityWolf.getTexture());
			GL11.glColor3f(var4, var4, var4);
			return 1;
		} else if (par2 == 1 && par1EntityWolf.isTamed()) {
			// MCPatcher Start
			this.loadTexture(MobRandomizer.randomTexture((Object)par1EntityWolf, "/mob/wolf_collar.png"));
			// MCPatcher End
			var4 = 1.0F;
			int var5 = par1EntityWolf.getCollarColor();
			// MCPatcher Start
			GL11.glColor3f(var4 * ColorizeEntity.collarColors[var5][0], var4 * ColorizeEntity.collarColors[var5][1], var4 * ColorizeEntity.collarColors[var5][2]);
			// MCPatcher End
			return 1;
		} else {
			return -1;
		}
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
		return this.func_82447_a((EntityWolf)par1EntityLiving, par2, par3);
	}

	/**
	 * Defines what float the third param in setRotationAngles of ModelBase is
	 */
	protected float handleRotationFloat(EntityLiving par1EntityLiving, float par2) {
		return this.getTailRotation((EntityWolf)par1EntityLiving, par2);
	}
}
