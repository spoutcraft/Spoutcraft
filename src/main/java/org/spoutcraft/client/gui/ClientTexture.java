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
package org.spoutcraft.client.gui;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.gui.GenericTexture;
import org.spoutcraft.client.io.CustomTextureManager;

public class ClientTexture extends GenericTexture {
	public ClientTexture(String path) {
		super(path);
	}

	public ClientTexture() {
		super();
	}

	@Override
	public void render() {
		GL11.glTranslatef(getX(), getY(), 0);
		Texture t = CustomTextureManager.getTextureFromJar(getUrl());
		if (t != null) {
			MCRenderDelegate d = (MCRenderDelegate) Spoutcraft.getRenderDelegate();
			d.drawTexture(t, (int) getWidth(), (int) getHeight(), isDrawingAlphaChannel());
		}
	}

	@Override
	public org.spoutcraft.api.gui.Texture setUrl(String url) {
		Texture t = CustomTextureManager.getTextureFromJar(url);
		if (t != null) {
			setOriginalHeight(t.getImageHeight());
			setOriginalWidth(t.getImageWidth());
		}
		return super.setUrl(url);
	}
}
