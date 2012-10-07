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

public class Bracelet extends Accessory{
	public ModelRenderer bipedFrontBracelet;
	public ModelRenderer bipedLeftBracelet;
	public ModelRenderer bipedBackBracelet;
	public ModelRenderer bipedRightBracelet;

	public Bracelet(ModelBiped mb) {
		super(mb);
		bipedFrontBracelet = new ModelRenderer(mb, 0, 0);
		bipedFrontBracelet.addBox(4F, 9F, -3F, 4, 1, 1);
		bipedLeftBracelet = new ModelRenderer(mb, 10, 0);
		bipedLeftBracelet.addBox(3F, 9F, -3F, 1, 1, 6);
		bipedBackBracelet = new ModelRenderer(mb, 0, 2);
		bipedBackBracelet.addBox(4F, 9F, 2F, 4, 1, 1);
		bipedBackBracelet.setRotationPoint(0F, 0F, 0F);
		bipedRightBracelet = new ModelRenderer(mb, 0, 4);
		bipedRightBracelet.addBox(8F, 9F, -3F, 1, 1, 6);
		bipedRightBracelet.setRotationPoint(0F, 0F, 0F);
	}

	@Override
	public void render(EntityPlayer plr, float f) {
		bipedFrontBracelet.rotateAngleY = getModel().bipedLeftArm.rotateAngleY;
		bipedFrontBracelet.rotateAngleX = getModel().bipedLeftArm.rotateAngleX;
		bipedFrontBracelet.rotationPointX = 0.0F;
		bipedFrontBracelet.rotationPointY = 0.0F;
		bipedFrontBracelet.render(f);
		bipedLeftBracelet.rotateAngleY = getModel().bipedLeftArm.rotateAngleY;
		bipedLeftBracelet.rotateAngleX = getModel().bipedLeftArm.rotateAngleX;
		bipedLeftBracelet.rotationPointX = 0.0F;
		bipedLeftBracelet.rotationPointY = 0.0F;
		bipedLeftBracelet.render(f);
		bipedBackBracelet.rotateAngleY = getModel().bipedLeftArm.rotateAngleY;
		bipedBackBracelet.rotateAngleX = getModel().bipedLeftArm.rotateAngleX;
		bipedBackBracelet.rotationPointX = 0.0F;
		bipedBackBracelet.rotationPointY = 0.0F;
		bipedBackBracelet.render(f);
		bipedRightBracelet.rotateAngleY = getModel().bipedLeftArm.rotateAngleY;
		bipedRightBracelet.rotateAngleX = getModel().bipedLeftArm.rotateAngleX;
		bipedRightBracelet.rotationPointX = 0.0F;
		bipedRightBracelet.rotationPointY = 0.0F;
		bipedRightBracelet.render(f);
	}

	@Override
	public AccessoryType getType() {
		return AccessoryType.BRACELET;
	}
}
