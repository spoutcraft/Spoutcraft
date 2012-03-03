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

import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntitySpider;
import net.minecraft.src.ModelSpider;
import net.minecraft.src.OpenGlHelper;
import net.minecraft.src.RenderLiving;
import org.lwjgl.opengl.GL11;

//Spout Start
import org.spoutcraft.spoutcraftapi.entity.EntitySkinType;
//Spout End

public class RenderSpider extends RenderLiving {

	public RenderSpider() {
		super(new ModelSpider(), 1.0F);
		this.setRenderPassModel(new ModelSpider());
	}

	protected float setSpiderDeathMaxRotation(EntitySpider par1EntitySpider) {
		return 180.0F;
	}

	protected int setSpiderEyeBrightness(EntitySpider par1EntitySpider, int par2, float par3) {
		if (par2 != 0) {
			return -1;
		} else {
			//Spout Start
            loadTexture(par1EntitySpider.getCustomTexture(EntitySkinType.SPIDER_EYES, "/mob/spider_eyes.png"));
            //Spout End
			float var4 = 1.0F;
			GL11.glEnable(3042);
			GL11.glDisable(3008);
			GL11.glBlendFunc(1, 1);
			char var5 = '\uf0f0';
			int var6 = var5 % 65536;
			int var7 = var5 / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapEnabled, (float)var6 / 1.0F, (float)var7 / 1.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, var4);
			return 1;
		}
	}

	protected void scaleSpider(EntitySpider par1EntitySpider, float par2) {
		float var3 = par1EntitySpider.spiderScaleAmount();
		GL11.glScalef(var3, var3, var3);
	}

	
	
	protected void preRenderCallback(EntityLiving par1EntityLiving, float par2) {
		this.scaleSpider((EntitySpider)par1EntityLiving, par2);
	}

	
	
	protected float getDeathMaxRotation(EntityLiving par1EntityLiving) {
		return this.setSpiderDeathMaxRotation((EntitySpider)par1EntityLiving);
	}

	
	
	protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
		return this.setSpiderEyeBrightness((EntitySpider)par1EntityLiving, par2, par3);
	}
}
