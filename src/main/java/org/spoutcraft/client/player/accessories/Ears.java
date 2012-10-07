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

public class Ears extends Accessory {
	public ModelRenderer bipedEars2;

	public Ears(ModelBiped mb) {
		super(mb);
		bipedEars2 = new ModelRenderer(mb, 0, 0);
		bipedEars2.addBox(-3F, -6F, -1F, 6, 6, 1);
	}

	@Override
	public void render(EntityPlayer plr, float f) {
		bipedEars2.rotateAngleY = getModel().bipedHead.rotateAngleY;
		bipedEars2.rotateAngleX = getModel().bipedHead.rotateAngleX;
		bipedEars2.rotationPointX = 0.0F;
		bipedEars2.rotationPointY = 0.0F;
		bipedEars2.render(f);
	}

	@Override
	public AccessoryType getType() {
		return AccessoryType.EARS;
	}
}
