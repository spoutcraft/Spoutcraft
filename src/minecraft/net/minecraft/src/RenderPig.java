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

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPig;
import net.minecraft.src.ModelBase;
import net.minecraft.src.RenderLiving;
//Spout Start
import org.spoutcraft.spoutcraftapi.entity.EntitySkinType;
//Spout End

public class RenderPig extends RenderLiving {

	public RenderPig(ModelBase par1ModelBase, ModelBase par2ModelBase, float par3) {
		super(par1ModelBase, par3);
		this.setRenderPassModel(par2ModelBase);
	}

	protected int renderSaddledPig(EntityPig par1EntityPig, int par2, float par3) {
		//Spout Start
		loadTexture(par1EntityPig.getCustomTexture(EntitySkinType.PIG_SADDLE, "/mob/saddle.png"));
		//Spout End
		return par2 == 0 && par1EntityPig.getSaddled()?1:-1;
	}

	public void func_40286_a(EntityPig par1EntityPig, double par2, double par4, double par6, float par8, float par9) {
		super.doRenderLiving(par1EntityPig, par2, par4, par6, par8, par9);
	}



	protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
		return this.renderSaddledPig((EntityPig)par1EntityLiving, par2, par3);
	}



	public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
		this.func_40286_a((EntityPig)par1EntityLiving, par2, par4, par6, par8, par9);
	}



	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		this.func_40286_a((EntityPig)par1Entity, par2, par4, par6, par8, par9);
	}
}
