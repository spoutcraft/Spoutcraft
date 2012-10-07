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

public class Wings extends Accessory{
	ModelRenderer LeftWingPart1;
	ModelRenderer LeftWingPart2;
	ModelRenderer LeftWingPart3;
	ModelRenderer LeftWingPart4;
	ModelRenderer LeftWingPart5;
	ModelRenderer LeftWingPart6;
	ModelRenderer LeftWingPart7;
	ModelRenderer LeftWingPart8;
	ModelRenderer LeftWingPart0;
	ModelRenderer RightWingPart0;
	ModelRenderer RightWingPart1;
	ModelRenderer RightWingPart2;
	ModelRenderer RightWingPart3;
	ModelRenderer RightWingPart4;
	ModelRenderer RightWingPart5;
	ModelRenderer RightWingPart6;
	ModelRenderer RightWingPart7;
	ModelRenderer RightWingPart8;

	public Wings(ModelBiped mb) {
		super(mb);
		LeftWingPart1 = new ModelRenderer(mb, 56, 0);
		LeftWingPart1.addBox(-1F, 1F, 3F, 1, 10, 1);
		LeftWingPart1.setRotationPoint(0F, 0F, 0F);
		LeftWingPart1.setTextureSize(64, 32);
		LeftWingPart1.mirror = true;
		setRotation(LeftWingPart1, 0F, 0.5007752F, 0.0174533F);
		LeftWingPart2 = new ModelRenderer(mb, 50, 0);
		LeftWingPart2.addBox(-1F, 0F, 4F, 1, 10, 2);
		LeftWingPart2.setRotationPoint(0F, 0F, 0F);
		LeftWingPart2.setTextureSize(64, 32);
		LeftWingPart2.mirror = true;
		setRotation(LeftWingPart2, 0F, 0.5182285F, 0.0349066F);
		LeftWingPart3 = new ModelRenderer(mb, 46, 0);
		LeftWingPart3.addBox(-1F, -1F, 6F, 1, 10, 1);
		LeftWingPart3.setRotationPoint(0F, 0F, 0F);
		LeftWingPart3.setTextureSize(64, 32);
		LeftWingPart3.mirror = true;
		setRotation(LeftWingPart3, 0F, 0.5356818F, 0.0523599F);
		LeftWingPart4 = new ModelRenderer(mb, 38, 0);
		LeftWingPart4.addBox(-1F, -2F, 7F, 1, 10, 3);
		LeftWingPart4.setRotationPoint(0F, 0F, 0F);
		LeftWingPart4.setTextureSize(64, 32);
		LeftWingPart4.mirror = true;
		setRotation(LeftWingPart4, 0F, 0.5531351F, 0.0698132F);
		LeftWingPart5 = new ModelRenderer(mb, 34, 0);
		LeftWingPart5.addBox(-1F, -1F, 10F, 1, 10, 1);
		LeftWingPart5.setRotationPoint(0F, 0F, 0F);
		LeftWingPart5.setTextureSize(64, 32);
		LeftWingPart5.mirror = true;
		setRotation(LeftWingPart5, 0F, 0.5531351F, 0.0523599F);
		LeftWingPart6 = new ModelRenderer(mb, 30, 0);
		LeftWingPart6.addBox(-1F, 0F, 11F, 1, 10, 1);
		LeftWingPart6.setRotationPoint(0F, 0F, 0F);
		LeftWingPart6.setTextureSize(64, 32);
		LeftWingPart6.mirror = true;
		setRotation(LeftWingPart6, 0F, 0.5705884F, 0.0349066F);
		LeftWingPart7 = new ModelRenderer(mb, 26, 0);
		LeftWingPart7.addBox(-1F, 1F, 12F, 1, 10, 1);
		LeftWingPart7.setRotationPoint(0F, 0F, 0F);
		LeftWingPart7.setTextureSize(64, 32);
		LeftWingPart7.mirror = true;
		setRotation(LeftWingPart7, 0F, 0.5880417F, 0.0174533F);
		LeftWingPart8 = new ModelRenderer(mb, 22, 0);
		LeftWingPart8.addBox(-1F, 3F, 13F, 1, 10, 1);
		LeftWingPart8.setRotationPoint(0F, 0F, 0F);
		LeftWingPart8.setTextureSize(64, 32);
		LeftWingPart8.mirror = true;
		setRotation(LeftWingPart8, 0F, 0.5880417F, 0F);
		LeftWingPart0 = new ModelRenderer(mb, 60, 0);
		LeftWingPart0.addBox(-1F, 2F, 2F, 1, 10, 1);
		LeftWingPart0.setRotationPoint(0F, 0F, 0F);
		LeftWingPart0.setTextureSize(64, 32);
		LeftWingPart0.mirror = true;
		setRotation(LeftWingPart0, 0F, 0.4833219F, 0F);
		RightWingPart0 = new ModelRenderer(mb, 60, 21);
		RightWingPart0.addBox(0F, 2F, 2F, 1, 10, 1);
		RightWingPart0.setRotationPoint(0F, 0F, 0F);
		RightWingPart0.setTextureSize(64, 32);
		RightWingPart0.mirror = true;
		setRotation(RightWingPart0, 0F, -0.4833166F, 0F);
		RightWingPart1 = new ModelRenderer(mb, 56, 21);
		RightWingPart1.addBox(0F, 1F, 3F, 1, 10, 1);
		RightWingPart1.setRotationPoint(0F, 0F, 0F);
		RightWingPart1.setTextureSize(64, 32);
		RightWingPart1.mirror = true;
		setRotation(RightWingPart1, 0F, -0.5007699F, -0.0174533F);
		RightWingPart2 = new ModelRenderer(mb, 50, 20);
		RightWingPart2.addBox(0F, 0F, 4F, 1, 10, 2);
		RightWingPart2.setRotationPoint(0F, 0F, 0F);
		RightWingPart2.setTextureSize(64, 32);
		RightWingPart2.mirror = true;
		setRotation(RightWingPart2, 0F, -0.5182232F, -0.0349066F);
		RightWingPart3 = new ModelRenderer(mb, 46, 21);
		RightWingPart3.addBox(0F, -1F, 6F, 1, 10, 1);
		RightWingPart3.setRotationPoint(0F, 0F, 0F);
		RightWingPart3.setTextureSize(64, 32);
		RightWingPart3.mirror = true;
		setRotation(RightWingPart3, 0.0174533F, -0.5356765F, -0.0523599F);
		RightWingPart4 = new ModelRenderer(mb, 38, 19);
		RightWingPart4.addBox(0F, -2F, 7F, 1, 10, 3);
		RightWingPart4.setRotationPoint(0F, 0F, 0F);
		RightWingPart4.setTextureSize(64, 32);
		RightWingPart4.mirror = true;
		setRotation(RightWingPart4, 0.0174533F, -0.5531297F, -0.0698132F);
		RightWingPart5 = new ModelRenderer(mb, 34, 21);
		RightWingPart5.addBox(0F, -1F, 10F, 1, 10, 1);
		RightWingPart5.setRotationPoint(0F, 0F, 0F);
		RightWingPart5.setTextureSize(64, 32);
		RightWingPart5.mirror = true;
		setRotation(RightWingPart5, 0.0174533F, -0.570583F, -0.0523599F);
		RightWingPart6 = new ModelRenderer(mb, 30, 21);
		RightWingPart6.addBox(0F, 0F, 11F, 1, 10, 1);
		RightWingPart6.setRotationPoint(0F, 0F, 0F);
		RightWingPart6.setTextureSize(64, 32);
		RightWingPart6.mirror = true;
		setRotation(RightWingPart6, 0.0174533F, -0.5880363F, -0.0349066F);
		RightWingPart7 = new ModelRenderer(mb, 26, 21);
		RightWingPart7.addBox(0F, 1F, 12F, 1, 10, 1);
		RightWingPart7.setRotationPoint(0F, 0F, 0F);
		RightWingPart7.setTextureSize(64, 32);
		RightWingPart7.mirror = true;
		setRotation(RightWingPart7, 0.0174533F, -0.6054896F, -0.0174533F);
		RightWingPart8 = new ModelRenderer(mb, 22, 21);
		RightWingPart8.addBox(0F, 3F, 13F, 1, 10, 1);
		RightWingPart8.setRotationPoint(0F, 0F, 0F);
		RightWingPart8.setTextureSize(64, 32);
		RightWingPart8.mirror = true;
		setRotation(RightWingPart8, 0.0174533F, -0.6229429F, 0F);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
	  model.rotateAngleX = x;
	  model.rotateAngleY = y;
	  model.rotateAngleZ = z;
	}

	@Override
	public void render(EntityPlayer plr, float f) {
		LeftWingPart1.render(f);
		LeftWingPart2.render(f);
		LeftWingPart3.render(f);
		LeftWingPart4.render(f);
		LeftWingPart5.render(f);
		LeftWingPart6.render(f);
		LeftWingPart7.render(f);
		LeftWingPart8.render(f);
		LeftWingPart0.render(f);
		RightWingPart0.render(f);
		RightWingPart1.render(f);
		RightWingPart2.render(f);
		RightWingPart3.render(f);
		RightWingPart4.render(f);
		RightWingPart5.render(f);
		RightWingPart6.render(f);
		RightWingPart7.render(f);
		RightWingPart8.render(f);
	}

	@Override
	public AccessoryType getType() {
		return AccessoryType.WINGS;
	}
}
