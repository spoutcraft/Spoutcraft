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
package org.spoutcraft.client.entity;

import org.lwjgl.opengl.GL11;

import net.minecraft.src.Entity;
import net.minecraft.src.RenderEntity;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.Tessellator;

import org.spoutcraft.client.io.CustomTextureManager;
import org.spoutcraft.client.SpoutClient;

public class RenderTexture extends RenderEntity {
	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float pitch) {
		EntityTexture entityT = (EntityTexture) entity;
		if (entityT.texture == null) {
			entityT.texture = CustomTextureManager.getTextureFromUrl(entityT.getUrl());
			if (entityT.texture == null) {
				return;
			}
		}
		GL11.glPushMatrix();
		RenderHelper.disableStandardItemLighting();
		yaw = entityT.isRotateWithPlayer()?-this.renderManager.playerViewY:yaw;
		GL11.glTranslated(x, y, z);
		GL11.glRotatef(yaw, 0, 1.0F, 0);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		SpoutClient.getHandle().renderEngine.bindTexture(entityT.texture.getTextureID());
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		double h = entityT.height * 0.014;
		double w = entityT.width * 0.014;
		tessellator.addVertexWithUV(w/2, -h/2, 0, 0.0D, 0.0D); // Draw corners
		tessellator.addVertexWithUV(-w/2, -h/2, 0, entityT.texture.getWidth(), 0.0D);
		tessellator.addVertexWithUV(-w/2, h/2, 0, entityT.texture.getWidth(), entityT.texture.getHeight());
		tessellator.addVertexWithUV(w/2, h/2, 0, 0.0D, entityT.texture.getHeight());
		tessellator.draw();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();
	}
}
