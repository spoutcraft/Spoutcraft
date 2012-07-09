/*
 * This file is part of Vanilla.
 *
 * Vanilla is licensed under the SpoutDev License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spoutcraft.client.player.accessories;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.ModelRenderer;



public class Sunglasses extends Accessory{
	public ModelRenderer SunglassesFront;
	public ModelRenderer SunglassesFront2;
	public ModelRenderer SunglassesBridge;
	public ModelRenderer RightSunglasses;
	public ModelRenderer LeftSunglasses;
	public ModelRenderer RightSunglassesBridge;
	public ModelRenderer LeftSunglassesBridge;
	
	public Sunglasses(ModelBiped acs) {
		super(acs);
		SunglassesFront = new ModelRenderer(acs, 0, 0);
		SunglassesFront.addBox(-3F, -4F, -5F, 2, 2, 1);
		SunglassesFront2 = new ModelRenderer(acs, 6, 0);
		SunglassesFront2.addBox(1F, -4F, -5F, 2, 2, 1);
		SunglassesBridge = new ModelRenderer(acs, 0, 4);
		SunglassesBridge.addBox(-1F, -4F, -5F, 2, 1, 1);
	}

	@Override
	public void render(EntityPlayer plr, float f) {
		SunglassesFront.rotateAngleY = getModel().bipedHead.rotateAngleY;
		SunglassesFront.rotateAngleX = getModel().bipedHead.rotateAngleX;
		SunglassesFront.render(f);
		SunglassesFront2.rotateAngleY = getModel().bipedHead.rotateAngleY;
		SunglassesFront2.rotateAngleX = getModel().bipedHead.rotateAngleX;
		SunglassesFront2.render(f);
		SunglassesBridge.rotateAngleY = getModel().bipedHead.rotateAngleY;
		SunglassesBridge.rotateAngleX = getModel().bipedHead.rotateAngleX;
		SunglassesBridge.render(f);
		RightSunglasses.rotateAngleY = getModel().bipedHead.rotateAngleY;
		RightSunglasses.rotateAngleX = getModel().bipedHead.rotateAngleX;
		RightSunglasses.render(f);
		LeftSunglasses.rotateAngleY = getModel().bipedHead.rotateAngleY;
		LeftSunglasses.rotateAngleX = getModel().bipedHead.rotateAngleX;
		LeftSunglasses.render(f);
		LeftSunglassesBridge.rotateAngleY = getModel().bipedHead.rotateAngleY;
		LeftSunglassesBridge.rotateAngleX = getModel().bipedHead.rotateAngleX;
		LeftSunglassesBridge.render(f);
		RightSunglassesBridge.rotateAngleY = getModel().bipedHead.rotateAngleY;
		RightSunglassesBridge.rotateAngleX = getModel().bipedHead.rotateAngleX;
		RightSunglassesBridge.render(f);
	}

}
