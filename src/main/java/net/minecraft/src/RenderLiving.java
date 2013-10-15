package net.minecraft.src;

import org.lwjgl.opengl.GL11;

import com.prupe.mcpatcher.mob.LineRenderer;

public abstract class RenderLiving extends RendererLivingEntity {
	public RenderLiving(ModelBase par1ModelBase, float par2) {
		super(par1ModelBase, par2);
	}

	protected boolean func_130007_b(EntityLiving par1EntityLiving) {
		return super.specialRender(par1EntityLiving) && (par1EntityLiving.getAlwaysRenderNameTagForRender() || par1EntityLiving.hasCustomNameTag() && par1EntityLiving == this.renderManager.field_96451_i);
	}

	public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
		super.doRenderLiving(par1EntityLiving, par2, par4, par6, par8, par9);
		this.func_110827_b(par1EntityLiving, par2, par4, par6, par8, par9);
	}

	private double func_110828_a(double par1, double par3, double par5) {
		return par1 + (par3 - par1) * par5;
	}

	protected void func_110827_b(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
		Entity var10 = par1EntityLiving.getLeashedToEntity();

		if (var10 != null) {
			par4 -= (1.6D - (double)par1EntityLiving.height) * 0.5D;
			Tessellator var11 = Tessellator.instance;
			double var12 = this.func_110828_a((double)var10.prevRotationYaw, (double)var10.rotationYaw, (double)(par9 * 0.5F)) * 0.01745329238474369D;
			double var14 = this.func_110828_a((double)var10.prevRotationPitch, (double)var10.rotationPitch, (double)(par9 * 0.5F)) * 0.01745329238474369D;
			double var16 = Math.cos(var12);
			double var18 = Math.sin(var12);
			double var20 = Math.sin(var14);

			if (var10 instanceof EntityHanging) {
				var16 = 0.0D;
				var18 = 0.0D;
				var20 = -1.0D;
			}

			double var22 = Math.cos(var14);
			double var24 = this.func_110828_a(var10.prevPosX, var10.posX, (double)par9) - var16 * 0.7D - var18 * 0.5D * var22;
			double var26 = this.func_110828_a(var10.prevPosY + (double)var10.getEyeHeight() * 0.7D, var10.posY + (double)var10.getEyeHeight() * 0.7D, (double)par9) - var20 * 0.5D - 0.25D;
			double var28 = this.func_110828_a(var10.prevPosZ, var10.posZ, (double)par9) - var18 * 0.7D + var16 * 0.5D * var22;
			double var30 = this.func_110828_a((double)par1EntityLiving.prevRenderYawOffset, (double)par1EntityLiving.renderYawOffset, (double)par9) * 0.01745329238474369D + (Math.PI / 2D);
			var16 = Math.cos(var30) * (double)par1EntityLiving.width * 0.4D;
			var18 = Math.sin(var30) * (double)par1EntityLiving.width * 0.4D;
			double var32 = this.func_110828_a(par1EntityLiving.prevPosX, par1EntityLiving.posX, (double)par9) + var16;
			double var34 = this.func_110828_a(par1EntityLiving.prevPosY, par1EntityLiving.posY, (double)par9);
			double var36 = this.func_110828_a(par1EntityLiving.prevPosZ, par1EntityLiving.posZ, (double)par9) + var18;
			par2 += var16;
			par6 += var18;
			double var38 = (double)((float)(var24 - var32));
			double var40 = (double)((float)(var26 - var34));
			double var42 = (double)((float)(var28 - var36));

			if (!LineRenderer.renderLine(1, par2, par4, par6, var38, var40, var42)) {
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_CULL_FACE);
				boolean var44 = true;
				double var45 = 0.025D;
				var11.startDrawing(5);
				int var47;
				float var48;

				for (var47 = 0; var47 <= 24; ++var47) {
					if (var47 % 2 == 0) {
						var11.setColorRGBA_F(0.5F, 0.4F, 0.3F, 1.0F);
					} else {
						var11.setColorRGBA_F(0.35F, 0.28F, 0.21000001F, 1.0F);
					}

					var48 = (float)var47 / 24.0F;
					var11.addVertex(par2 + var38 * (double)var48 + 0.0D, par4 + var40 * (double)(var48 * var48 + var48) * 0.5D + (double)((24.0F - (float)var47) / 18.0F + 0.125F), par6 + var42 * (double)var48);
					var11.addVertex(par2 + var38 * (double)var48 + 0.025D, par4 + var40 * (double)(var48 * var48 + var48) * 0.5D + (double)((24.0F - (float)var47) / 18.0F + 0.125F) + 0.025D, par6 + var42 * (double)var48);
				}

				var11.draw();
				var11.startDrawing(5);

				for (var47 = 0; var47 <= 24; ++var47) {
					if (var47 % 2 == 0) {
						var11.setColorRGBA_F(0.5F, 0.4F, 0.3F, 1.0F);
					} else {
						var11.setColorRGBA_F(0.35F, 0.28F, 0.21000001F, 1.0F);
					}

					var48 = (float)var47 / 24.0F;
					var11.addVertex(par2 + var38 * (double)var48 + 0.0D, par4 + var40 * (double)(var48 * var48 + var48) * 0.5D + (double)((24.0F - (float)var47) / 18.0F + 0.125F) + 0.025D, par6 + var42 * (double)var48);
					var11.addVertex(par2 + var38 * (double)var48 + 0.025D, par4 + var40 * (double)(var48 * var48 + var48) * 0.5D + (double)((24.0F - (float)var47) / 18.0F + 0.125F), par6 + var42 * (double)var48 + 0.025D);
				}

				var11.draw();
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}

			GL11.glEnable(GL11.GL_CULL_FACE);
		}
	}

	public void renderPlayer(EntityLivingBase par1EntityLivingBase, double par2, double par4, double par6, float par8, float par9) {
		this.doRenderLiving((EntityLiving)par1EntityLivingBase, par2, par4, par6, par8, par9);
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
	 * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
	 * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
	 * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
	 */
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		this.doRenderLiving((EntityLiving)par1Entity, par2, par4, par6, par8, par9);
	}
}
