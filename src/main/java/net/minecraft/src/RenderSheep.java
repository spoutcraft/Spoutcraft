package net.minecraft.src;

import org.lwjgl.opengl.GL11;

import com.prupe.mcpatcher.mob.MobRandomizer;

import org.spoutcraft.client.config.Configuration;
public class RenderSheep extends RenderLiving {
	private static final ResourceLocation field_110885_a = new ResourceLocation("textures/entity/sheep/sheep_fur.png");
	private static final ResourceLocation field_110884_f = new ResourceLocation("textures/entity/sheep/sheep.png");
	public RenderSheep(ModelBase par1ModelBase, ModelBase par2ModelBase, float par3) {
		super(par1ModelBase, par3);
		this.setRenderPassModel(par2ModelBase);
	}

	protected int setWoolColorAndRender(EntitySheep par1EntitySheep, int par2, float par3) {
		if (par2 == 0 && !par1EntitySheep.getSheared()) {
			// Spout Start
			if (Configuration.isRandomMobTextures()) {
				this.func_110776_a(MobRandomizer.randomTexture((EntityLivingBase)par1EntitySheep, field_110885_a));
			} else {
				//ToDo: Spoutcraft API borked
				//loadTexture(par1EntitySheep.getCustomTexture(org.spoutcraft.api.entity.EntitySkinType.SHEEP_FUR, "/mob/sheep_fur.png"));
			}
			// Spout End
			
			float var4 = 1.0F;
			int var5 = par1EntitySheep.getFleeceColor();
			GL11.glColor3f(var4 * EntitySheep.fleeceColorTable[var5][0], var4 * EntitySheep.fleeceColorTable[var5][1], var4 * EntitySheep.fleeceColorTable[var5][2]);
			return 1;
		} else {
			return -1;
		}
	}
	
	protected ResourceLocation func_110883_a(EntitySheep par1EntitySheep) {
		return field_110884_f;
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	protected int shouldRenderPass(EntityLivingBase par1EntityLivingBase, int par2, float par3) {
		return this.setWoolColorAndRender((EntitySheep)par1EntityLivingBase, par2, par3);
	}

	protected ResourceLocation func_110775_a(Entity par1Entity) {
		return this.func_110883_a((EntitySheep)par1Entity);
	}
}
