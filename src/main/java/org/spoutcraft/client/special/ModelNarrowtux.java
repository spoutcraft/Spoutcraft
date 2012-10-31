/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Spoutcraft is licensed under the GNU Lesser General Public License.
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
package org.spoutcraft.client.special;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModelBase;
import net.minecraft.src.ModelRenderer;

import org.spoutcraft.client.io.CustomTextureManager;

public class ModelNarrowtux extends ModelBase {
	public ModelRenderer waist;
	public ModelRenderer leftFoot, rightFoot;
	public ModelRenderer leftWing, rightWing;
	public ModelRenderer head;

	private final static float MAX_WING_ROTATION = 0.314159265f;
	private final static float MIN_WING_ROTATION = 0.209439510f;

	public ModelNarrowtux(float scale) {
		reload(scale);
	}

	@Override
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
		reload(0.0f);
		this.setRotationAngles(par2, par3, par4, par5, par6, par7);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, CustomTextureManager.getTextureFromPath("/Users/tux/Sites/narrowtux.png").getTextureID());
		waist.render(par7);
		leftFoot.render(par7);
		rightFoot.render(par7);
		leftWing.render(par7);
		rightWing.render(par7);
		head.render(par7);
	}

	@Override
	public void setLivingAnimations(EntityLiving par1EntityLiving, float x, float y, float z) {
	}

	public void reload(float scale) {
		boxList.clear();
		waist = new ModelRenderer(this, 0, 0);
		waist.addBox(-4f, 2f, -2f, 8, 20, 4, scale);

		leftFoot = new ModelRenderer(this, 4, 0);
		leftFoot.setRotationPoint(0f, 22f, 0f);
		leftFoot.addBox(0f, 0f, -1f, 4, 2, 3, scale);
		leftFoot.addBox(0f, 1f, -2f, 4, 1, 1, scale);

		rightFoot = new ModelRenderer(this, 4, 0);
		rightFoot.setRotationPoint(0f, 22f, 0f);
		rightFoot.addBox(-4f, 0f, -1f, 4, 2, 3, scale);
		rightFoot.addBox(-4f, 1f, -2f, 4, 1, 1, scale);
		rightFoot.mirror = true;

		head = new ModelRenderer(this, 30, 0);
		head.addBox(-3f, -4f, -1f, 6, 6, 3, scale);
		head.addBox(-2f, 0f, -4f, 4, 1, 3);

		leftWing = new ModelRenderer(this, 4, 0);
		leftWing.addBox(4f, 2f, -2f, 1, 10, 4);
		leftWing.setRotationPoint(-1f, 1.5f, 0f);
		leftWing.rotateAngleZ = -0.261799388f;

		rightWing = new ModelRenderer(this, 4, 0);
		rightWing.addBox(-4f, 2f, -2f, -1, 10, 4);
		rightWing.setRotationPoint(1f, 1.5f, 0f);
		rightWing.rotateAngleZ = 0.261799388f;
	}

	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6) {
		this.head.rotateAngleY = par4 / (180F / (float) Math.PI);
		this.head.rotateAngleX = par5 / (180F / (float) Math.PI);
		this.rightWing.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float) Math.PI) * 2.0F * par2 * 0.5F;
		this.leftWing.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 2.0F * par2 * 0.5F;
		this.rightFoot.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
		this.leftFoot.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float) Math.PI) * 1.4F * par2;
		this.rightFoot.rotateAngleY = 0.0F;
		this.leftFoot.rotateAngleY = 0.0F;

		this.rightWing.rotateAngleY = 0.0F;
		this.leftWing.rotateAngleY = 0.0F;

		float var7;
		float var8;

		if (this.onGround > -9990.0F) {
			var7 = this.onGround;
			this.waist.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(var7) * (float)Math.PI * 2.0F) * 0.2F;
			this.rightWing.rotateAngleY += this.waist.rotateAngleY;
			this.leftWing.rotateAngleY += this.waist.rotateAngleY;
			this.leftWing.rotateAngleX += this.waist.rotateAngleY;
			var7 = 1.0F - this.onGround;
			var7 *= var7;
			var7 *= var7;
			var7 = 1.0F - var7;
			var8 = MathHelper.sin(var7 * (float)Math.PI);
			float var9 = MathHelper.sin(this.onGround * (float)Math.PI) * -(this.head.rotateAngleX - 0.7F) * 0.75F;
			this.rightWing.rotateAngleX = (float)((double)this.rightWing.rotateAngleX - ((double)var8 * 1.2D + (double)var9));
			this.rightWing.rotateAngleY += this.waist.rotateAngleY * 2.0F;
			//this.rightWing.rotateAngleZ = MathHelper.sin(this.onGround * (float)Math.PI) * -0.4F;
		}
	}
}
