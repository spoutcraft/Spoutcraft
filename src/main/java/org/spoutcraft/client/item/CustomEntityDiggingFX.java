/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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
package org.spoutcraft.client.item;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.EntityDiggingFX;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;

import org.spoutcraft.api.block.design.GenericBlockDesign;
import org.spoutcraft.client.SpoutClient;

public class CustomEntityDiggingFX extends EntityDiggingFX {
	private Texture textureBinding = null;
	GenericBlockDesign design;
	public CustomEntityDiggingFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12, Block block, int var15, int var16, Texture textureBinding, GenericBlockDesign design) {
		super(var1, var2, var4, var6, var8, var10, var12, block, var15, var16, Minecraft.theMinecraft.renderEngine);
		this.textureBinding = textureBinding;
		this.design = design;
	}

	public void renderParticle(Tessellator var1, float var2, float var3, float var4, float var5, float var6, float var7) {
		SpoutClient.getHandle().renderEngine.bindTexture(textureBinding.getTextureID());
		Tessellator var10 = Tessellator.instance;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		var10.startDrawingQuads();

		float var12 = 0.1F * this.particleScale;
		float var13 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)var2 - interpPosX);
		float var14 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)var2 - interpPosY);
		float var15 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)var2 - interpPosZ);
		float var16 = 1.0F;
		for (int i = 0; i < design.getX().length; i++) {
			var1.setColorOpaque_F(var16 * this.particleRed, var16 * this.particleGreen, var16 * this.particleBlue);
			var1.addVertexWithUV((double)(var13 - var3 * var12 - var6 * var12), (double)(var14 - var4 * var12), (double)(var15 - var5 * var12 - var7 * var12), design.getTextureXPos()[i][0], design.getTextureYPos()[i][0]);
			var1.addVertexWithUV((double)(var13 - var3 * var12 + var6 * var12), (double)(var14 + var4 * var12), (double)(var15 - var5 * var12 + var7 * var12), design.getTextureXPos()[i][1], design.getTextureYPos()[i][1]);
			var1.addVertexWithUV((double)(var13 + var3 * var12 + var6 * var12), (double)(var14 + var4 * var12), (double)(var15 + var5 * var12 + var7 * var12), design.getTextureXPos()[i][2], design.getTextureYPos()[i][2]);
			var1.addVertexWithUV((double)(var13 + var3 * var12 - var6 * var12), (double)(var14 - var4 * var12), (double)(var15 + var5 * var12 - var7 * var12), design.getTextureXPos()[i][3], design.getTextureYPos()[i][3]);
		}

		var10.draw();
	}
}
