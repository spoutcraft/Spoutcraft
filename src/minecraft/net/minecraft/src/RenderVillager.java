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
import net.minecraft.src.EntityVillager;
import net.minecraft.src.ModelVillager;
import net.minecraft.src.RenderLiving;
import org.lwjgl.opengl.GL11;

public class RenderVillager extends RenderLiving {

	protected ModelVillager field_40295_c;

	public RenderVillager() {
		super(new ModelVillager(0.0F), 0.5F);
		this.field_40295_c = (ModelVillager)this.mainModel;
	}

	protected int func_40293_a(EntityVillager par1EntityVillager, int par2, float par3) {
		return -1;
	}

	public void renderVillager(EntityVillager par1EntityVillager, double par2, double par4, double par6, float par8, float par9) {
		super.doRenderLiving(par1EntityVillager, par2, par4, par6, par8, par9);
	}

	protected void func_40290_a(EntityVillager par1EntityVillager, double par2, double par4, double par6) {}

	protected void func_40291_a(EntityVillager par1EntityVillager, float par2) {
		super.renderEquippedItems(par1EntityVillager, par2);
	}

	protected void func_40292_b(EntityVillager par1EntityVillager, float par2) {
		float var3 = 0.9375F;
		if (par1EntityVillager.func_48123_at() < 0) {
			var3 = (float)((double)var3 * 0.5D);
			this.shadowSize = 0.25F;
		} else {
			this.shadowSize = 0.5F;
		}

		GL11.glScalef(var3, var3, var3);
	}

	
	
	protected void passSpecialRender(EntityLiving par1EntityLiving, double par2, double par4, double par6) {
		//Spout start
		//this.func_40290_a((EntityVillager)par1EntityLiving, par2, par4, par6);
        super.passSpecialRender(par1EntityLiving, par2, par4, par6);
        //Spout end
	}

	
	
	protected void preRenderCallback(EntityLiving par1EntityLiving, float par2) {
		this.func_40292_b((EntityVillager)par1EntityLiving, par2);
	}

	
	
	protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
		return this.func_40293_a((EntityVillager)par1EntityLiving, par2, par3);
	}

	
	
	protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2) {
		this.func_40291_a((EntityVillager)par1EntityLiving, par2);
	}

	
	
	public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
		this.renderVillager((EntityVillager)par1EntityLiving, par2, par4, par6, par8, par9);
	}

	
	
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		this.renderVillager((EntityVillager)par1Entity, par2, par4, par6, par8, par9);
	}
}
