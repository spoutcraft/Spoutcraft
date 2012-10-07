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
package org.spoutcraft.client.player.accessories;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.ModelRenderer;

public class TopHat extends Accessory{
	public ModelRenderer bipedBottomHat;
	public ModelRenderer bipedTopHat;

	public TopHat(ModelBiped model) {
		super(model);
		bipedBottomHat = new ModelRenderer(model, 0, 0);
		bipedBottomHat.addBox(-5.5F, -9F, -5.5F, 11, 2, 11);
		bipedTopHat = new ModelRenderer(model, 0, 13);
		bipedTopHat.addBox(-3.5F, -17F, -3.5F, 7, 8, 7);
	}

	@Override
	public void render(EntityPlayer plr, float f) {
		bipedBottomHat.rotateAngleY = getModel().bipedHead.rotateAngleY;
		bipedBottomHat.rotateAngleX = getModel().bipedHead.rotateAngleX;
		bipedBottomHat.rotationPointX = 0.0F;
		bipedBottomHat.rotationPointY = 0.0F;
		bipedBottomHat.render(f);
		bipedTopHat.rotateAngleY = getModel().bipedHead.rotateAngleY;
		bipedTopHat.rotateAngleX = getModel().bipedHead.rotateAngleX;
		bipedTopHat.rotationPointX = 0.0F;
		bipedTopHat.rotationPointY = 0.0F;
		bipedTopHat.render(f);
	}

	@Override
	public AccessoryType getType() {
		return AccessoryType.TOPHAT;
	}
}
