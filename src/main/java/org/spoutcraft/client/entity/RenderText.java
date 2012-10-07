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
package org.spoutcraft.client.entity;

import org.lwjgl.opengl.GL11;

import net.minecraft.src.Entity;
import net.minecraft.src.RenderEntity;
import net.minecraft.src.RenderHelper;

import org.spoutcraft.client.SpoutClient;

public class RenderText extends RenderEntity {
	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float pitch) {
		EntityText entitytext = (EntityText)entity;
		String text = entitytext.getText();
		yaw = entitytext.isRotateWithPlayer()?this.renderManager.playerViewY:yaw;
		RenderHelper.disableStandardItemLighting();
		GL11.glPushMatrix();
		float scale = entitytext.getScale() * 0.124f;
		GL11.glTranslated(x, y + 1, z);
		GL11.glScalef(scale, scale, scale);
		GL11.glRotatef(180f, 0, 0f, 1f);
		int stringwidth = SpoutClient.getInstance().getRenderDelegate().getMinecraftFont().getTextWidth(text);
		GL11.glRotatef(yaw, 0, 1f, 0);
		GL11.glTranslated(-stringwidth / 2d, 0, 0);
		SpoutClient.getHandle().fontRenderer.drawString(text, 0, 0, 0xffffffff);
		GL11.glPopMatrix();
		RenderHelper.enableStandardItemLighting();
	}
}
