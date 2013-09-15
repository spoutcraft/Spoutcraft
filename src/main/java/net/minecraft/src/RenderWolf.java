package net.minecraft.src;

import org.lwjgl.opengl.GL11;

import com.prupe.mcpatcher.cc.ColorizeEntity;
import com.prupe.mcpatcher.mob.MobRandomizer;

public class RenderWolf extends RenderLiving {
	private static final ResourceLocation wolfTextures = new ResourceLocation("textures/entity/wolf/wolf.png");
	private static final ResourceLocation tamedWolfTextures = new ResourceLocation("textures/entity/wolf/wolf_tame.png");
	private static final ResourceLocation anrgyWolfTextures = new ResourceLocation("textures/entity/wolf/wolf_angry.png");
	private static final ResourceLocation wolfCollarTextures = new ResourceLocation("textures/entity/wolf/wolf_collar.png");

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
			this.bindTexture(wolfTextures);
			GL11.glColor3f(var4, var4, var4);
			return 1;
		} else if (par2 == 1 && par1EntityWolf.isTamed()) {
			// MCPatcher Start
			//ToDo: need spoutcraft update
			this.bindTexture(MobRandomizer.randomTexture((EntityLivingBase)par1EntityWolf, wolfCollarTextures));
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

	protected ResourceLocation func_110914_a(EntityWolf par1EntityWolf) {
		return par1EntityWolf.isTamed() ? tamedWolfTextures : (par1EntityWolf.isAngry() ? anrgyWolfTextures : wolfTextures);
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	protected int shouldRenderPass(EntityLivingBase par1EntityLivingBase, int par2, float par3) {
		return this.func_82447_a((EntityWolf)par1EntityLivingBase, par2, par3);
	}

	/**
	 * Defines what float the third param in setRotationAngles of ModelBase is
	 */
	protected float handleRotationFloat(EntityLivingBase par1EntityLivingBase, float par2) {
		return this.getTailRotation((EntityWolf)par1EntityLivingBase, par2);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(Entity par1Entity) {
		return this.func_110914_a((EntityWolf)par1Entity);
	}
}
