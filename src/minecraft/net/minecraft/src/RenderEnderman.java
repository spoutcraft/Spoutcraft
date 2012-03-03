/*
 * This file is part of Spoutcraft (http://wiki.getspout.org/).
 * 
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.minecraft.src;

import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityEnderman;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.ModelEnderman;
import net.minecraft.src.OpenGlHelper;
import net.minecraft.src.RenderLiving;
import org.lwjgl.opengl.GL11;

//Spout start
import org.spoutcraft.spoutcraftapi.entity.EntitySkinType;
//Spout end

public class RenderEnderman extends RenderLiving {

	private ModelEnderman endermanModel;
	private Random rnd = new Random();

	public RenderEnderman() {
		super(new ModelEnderman(), 0.5F);
		this.endermanModel = (ModelEnderman)super.mainModel;
		this.setRenderPassModel(this.endermanModel);
	}

	public void renderEnderman(EntityEnderman par1EntityEnderman, double par2, double par4, double par6, float par8, float par9) {
		this.endermanModel.isCarrying = par1EntityEnderman.getCarried() > 0;
		this.endermanModel.isAttacking = par1EntityEnderman.isAttacking;
		if (par1EntityEnderman.isAttacking) {
			double var10 = 0.02D;
			par2 += this.rnd.nextGaussian() * var10;
			par6 += this.rnd.nextGaussian() * var10;
		}

		super.doRenderLiving(par1EntityEnderman, par2, par4, par6, par8, par9);
	}

	protected void renderCarrying(EntityEnderman par1EntityEnderman, float par2) {
		super.renderEquippedItems(par1EntityEnderman, par2);
		if (par1EntityEnderman.getCarried() > 0) {
			GL11.glEnable('\u803a');
			GL11.glPushMatrix();
			float var3 = 0.5F;
			GL11.glTranslatef(0.0F, 0.6875F, -0.75F);
			var3 *= 1.0F;
			GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			GL11.glScalef(var3, -var3, var3);
			int var4 = par1EntityEnderman.getEntityBrightnessForRender(par2);
			int var5 = var4 % 65536;
			int var6 = var4 / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapEnabled, (float)var5 / 1.0F, (float)var6 / 1.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.loadTexture("/terrain.png");
			this.renderBlocks.renderBlockAsItem(Block.blocksList[par1EntityEnderman.getCarried()], par1EntityEnderman.getCarryingData(), 1.0F);
			GL11.glPopMatrix();
			GL11.glDisable('\u803a');
		}

	}

	protected int renderEyes(EntityEnderman par1EntityEnderman, int par2, float par3) {
		if (par2 != 0) {
			return -1;
		} else {
			// Spout Start
			loadTexture(par1EntityEnderman.getCustomTexture(EntitySkinType.ENDERMAN_EYES, "/mob/enderman_eyes.png"));
			// Spout End
			float var4 = 1.0F;
			GL11.glEnable(3042);
			GL11.glDisable(3008);
			GL11.glBlendFunc(1, 1);
			GL11.glDisable(2896);
			char var5 = '\uf0f0';
			int var6 = var5 % 65536;
			int var7 = var5 / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapEnabled, (float)var6 / 1.0F, (float)var7 / 1.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glEnable(2896);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, var4);
			return 1;
		}
	}



	protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
		return this.renderEyes((EntityEnderman)par1EntityLiving, par2, par3);
	}



	protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2) {
		this.renderCarrying((EntityEnderman)par1EntityLiving, par2);
	}



	public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
		this.renderEnderman((EntityEnderman)par1EntityLiving, par2, par4, par6, par8, par9);
	}



	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		this.renderEnderman((EntityEnderman)par1Entity, par2, par4, par6, par8, par9);
	}
}
