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

public class NotchHat extends Accessory{
	public ModelRenderer NotchHatTop;
	public ModelRenderer NotchHatBottom;

	public NotchHat(ModelBiped mb) {
		super(mb);
		NotchHatTop = new ModelRenderer(mb, 0, 0);
		NotchHatTop.addBox(-5F, -9F, -5F, 10, 1, 10);
		NotchHatBottom = new ModelRenderer(mb, 0, 11);
		NotchHatBottom.addBox(-4F, -13F, -4F, 8, 4, 8);
	}

	@Override
	public void render(EntityPlayer plr, float f) {
		NotchHatTop.rotateAngleY = getModel().bipedHead.rotateAngleY;
		NotchHatTop.rotateAngleX = getModel().bipedHead.rotateAngleX;
		NotchHatTop.rotationPointX = 0.0F;
		NotchHatTop.rotationPointY = 0.0F;
		NotchHatTop.render(f);
		NotchHatBottom.rotateAngleY = getModel().bipedHead.rotateAngleY;
		NotchHatBottom.rotateAngleX = getModel().bipedHead.rotateAngleX;
		NotchHatBottom.rotationPointX = 0.0F;
		NotchHatBottom.rotationPointY = 0.0F;
		NotchHatBottom.render(f);
	}

	@Override
	public AccessoryType getType() {
		return AccessoryType.NOTCHHAT;
	}
}
