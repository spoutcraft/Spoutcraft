/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 SpoutcraftDev <http://spoutcraft.org/>
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
package org.spoutcraft.client.gui.precache;

import net.minecraft.src.GuiScreen;
import net.minecraft.src.Minecraft;
import net.minecraft.src.Packet0KeepAlive;

import org.lwjgl.opengl.GL11;
import org.bukkit.ChatColor;
import org.spoutcraft.api.gui.GenericLabel;
import org.spoutcraft.api.gui.GenericTexture;
import org.spoutcraft.api.gui.WidgetAnchor;
import org.spoutcraft.api.gui.Texture;
import org.spoutcraft.client.io.FileUtil;

public class GuiPrecache extends GuiScreen {
	public GenericLabel statusText, defaultText;
	Texture background, logo;
	private int updateCounter;

	@Override
	public void initGui() {
		
		logo = new ScaledTexture(FileUtil.getAssetsDir().getPath()+"/logo/spoutcraft.png");
		((ScaledTexture) logo).setScale(Math.min(1F, (width - 135F) / 256F));
		logo.setGeometry((width / 2)-64, (height / 2)-60, 128, 32);
		
		logo.setLocal(true);
		logo.setDrawAlphaChannel(true);
		
		defaultText = new GenericLabel();
		defaultText.setAnchor(WidgetAnchor.CENTER_CENTER);
		defaultText.setAlign(WidgetAnchor.CENTER_CENTER);
		defaultText.setText(ChatColor.WHITE + "Loading terrain and custom resources");
		//  + "\n" + "\n" + ChatColor.MAGIC + "ShowMagic"
		
		statusText = new GenericLabel();
		statusText.setAnchor(WidgetAnchor.CENTER_CENTER);
		statusText.setAlign(WidgetAnchor.CENTER_CENTER);
		statusText.shiftYPos(20);
		statusText.setText("Activating Cached Resources...");
		getScreen().attachWidgets("Spoutcraft", logo, defaultText, statusText);
	}
	
	@Override
	public void updateScreen() {
		++this.updateCounter;

		if (this.updateCounter % 20 == 0) {
			Minecraft.getMinecraft().getNetHandler().addToSendQueue(new Packet0KeepAlive());
		}

		if (Minecraft.getMinecraft().getNetHandler() != null) {
			Minecraft.getMinecraft().getNetHandler().processReadPackets();
		}
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		this.drawBackground(0);
		super.drawScreen(par1, par2, par3);
	}
}

class ScaledTexture extends GenericTexture {
	float scale;

	ScaledTexture(String path) {
		super(path);
	}

	public ScaledTexture setScale(float scale) {
		this.scale = scale;
		return this;
	}

	@Override
	public void render() {
		GL11.glPushMatrix();
		GL11.glScalef(scale, 1F, 1F);
		super.render();
		GL11.glPopMatrix();
	}
}
