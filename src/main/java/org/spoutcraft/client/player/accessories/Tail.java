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

import org.lwjgl.opengl.GL11;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModelBiped;
import net.minecraft.src.ModelRenderer;

public class Tail extends Accessory {
	public ModelRenderer Tail;

	public Tail(ModelBiped mb) {
		super(mb);
		Tail = new ModelRenderer(mb, 0, 0);
		Tail.addBox(0.0F, 0.0F, 0.0F, 1, 10, 1);
		Tail.setRotationPoint(-0.5F, 0F, -1F);
	}

	@Override
	public void render(EntityPlayer par1EntityPlayer, float f, float par2) {
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, 0.75F, 0.125F);
		double d = (par1EntityPlayer.field_71091_bM + (par1EntityPlayer.field_71094_bP - par1EntityPlayer.field_71091_bM) * (double) par2) - (par1EntityPlayer.prevPosX + (par1EntityPlayer.posX - par1EntityPlayer.prevPosX) * (double) par2);
		double d1 = (par1EntityPlayer.field_71096_bN + (par1EntityPlayer.field_71095_bQ - par1EntityPlayer.field_71096_bN) * (double) par2) - (par1EntityPlayer.prevPosY + (par1EntityPlayer.posY - par1EntityPlayer.prevPosY) * (double) par2);
		double d2 = (par1EntityPlayer.field_71097_bO + (par1EntityPlayer.field_71085_bR - par1EntityPlayer.field_71097_bO) * (double) par2) - (par1EntityPlayer.prevPosZ + (par1EntityPlayer.posZ - par1EntityPlayer.prevPosZ) * (double) par2);
		float f8 = par1EntityPlayer.prevRenderYawOffset + (par1EntityPlayer.renderYawOffset - par1EntityPlayer.prevRenderYawOffset) * par2;
		double d3 = MathHelper.sin((f8 * 3.141593F) / 180F);
		double d4 = -MathHelper.cos((f8 * 3.141593F) / 180F);
		float f9 = (float) d1 * 10F;
		if (f9 < -6F) {
			f9 = -6F;
		}
		if (f9 > 32F) {
			f9 = 32F;
		}
		float f10 = (float) (d * d3 + d2 * d4) * 100F;
		float f11 = (float) (d * d4 - d2 * d3) * 100F;
		if (f10 < 0.0F) {
			f10 = 0.0F;
		}
		float f12 = par1EntityPlayer.prevCameraYaw + (par1EntityPlayer.cameraYaw - par1EntityPlayer.prevCameraYaw) * par2;
		f9 += MathHelper.sin((par1EntityPlayer.prevDistanceWalkedModified + (par1EntityPlayer.distanceWalkedModified - par1EntityPlayer.prevDistanceWalkedModified) * par2) * 6F) * 32F * f12;
		if (par1EntityPlayer.isSneaking()) {
			f9 += 25F;
		}
		GL11.glRotatef(6F + f10 / 2.0F + f9, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(f11 / 2.0F, 0.0F, 0.0F, 1.0F);
		GL11.glRotatef(-f11 / 2.0F, 0.0F, 0.0F, 0.0F);
		GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
		Tail.render(f);
		GL11.glPopMatrix();
	}

	@Override
	public AccessoryType getType() {
		return AccessoryType.TAIL;
	}
}
