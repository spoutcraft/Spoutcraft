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
package org.spoutcraft.client.gui.database;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import net.minecraft.src.FontRenderer;

import org.spoutcraft.api.gui.Color;
import org.spoutcraft.api.gui.GenericRadioButton;
import org.spoutcraft.api.gui.RadioButton;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.gui.MCRenderDelegate;
import org.spoutcraft.client.io.CustomTextureManager;

public class SortButton extends GenericRadioButton implements UrlElement {
	boolean topdown = true;
	boolean preferredOrder = true;
	boolean allowSorting = true;
	boolean firstClick = false;

	protected AbstractAPIModel model;
	String url;

	public SortButton(String text, String urlPart, AbstractAPIModel model) {
		super(text);
		url = urlPart;
		this.model = model;
	}

	public SortButton(String text, String baseUrl, boolean preferredOrder, AbstractAPIModel model) {
		super(text);
		url = baseUrl;
		this.preferredOrder = preferredOrder;
		this.model = model;
	}

	@Override
	public void onButtonClick() {
		if (isSelected() && !firstClick) {
			topdown = !topdown;
		}
		updateUrl();
		firstClick = false;
	}

	@Override
	public RadioButton setSelected(boolean b) {
		if (!isSelected() && b) {
			topdown = preferredOrder;
			firstClick = true;
		}
		super.setSelected(b);
		return this;
	}

	private void updateUrl() {
		model.updateUrl();
	}

	public void setAllowSorting(boolean b) {
		allowSorting = b;
	}

	@Override
	public void render() {
		if (!allowSorting) {
			super.render();
		} else {
			MCRenderDelegate r = (MCRenderDelegate) SpoutClient.getInstance().getRenderDelegate();
			String texture = "";
			if (isSelected() && topdown || !isSelected() && preferredOrder) {
				texture = "ui/box_ascending.png";
			} else {
				texture = "ui/box_descending.png";
			}
			Texture direction = CustomTextureManager.getTextureFromJar("/res/" + texture);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glTranslatef((float) getScreenX(), (float) getScreenY(), 0);
			r.renderBaseBox(this, true);
			FontRenderer font = SpoutClient.getHandle().fontRenderer;
			Color color = r.getColor(this);
			if (!isSelected()) {
				color.setAlpha(0.2F);
			}
			r.drawTexture(direction, 20, 20, color, true);
			font.drawString(getText(), 22, 7, r.getColor(this).toInt());
		}
	}

	public boolean isActive() {
		return isSelected();
	}

	public String getUrlPart() {
		String dir = topdown ? "asc" : "desc";
		String surl = url + (allowSorting ? "&order=" + dir : "");
		return surl;
	}

	public void clear() {
		setSelected(false);
	}
}
