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
package org.spoutcraft.client.gui.error;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiConnecting;
import net.minecraft.src.GuiScreen;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.gui.Button;
import org.spoutcraft.api.gui.Color;
import org.spoutcraft.api.gui.GenericButton;
import org.spoutcraft.api.gui.GenericLabel;
import org.spoutcraft.api.gui.GenericScrollArea;
import org.spoutcraft.api.gui.GenericTexture;
import org.spoutcraft.api.gui.RenderPriority;
import org.spoutcraft.api.gui.WidgetAnchor;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.gui.MCRenderDelegate;
import org.spoutcraft.client.io.CustomTextureManager;

public class GuiConnectionLost extends GuiScreen {
	public static String lastServerIp;
	public static int lastServerPort;

	private String message;

	public GuiConnectionLost() {
		message = "The connection to the server has been lost!";
	}

	public GuiConnectionLost(String message) {
		this.message = message;
	}

	@Override
	public void initGui() {
		GenericScrollArea screen = new GenericScrollArea();
		screen.setHeight(height - 16 - 24).setWidth(width).setY(16 + 24).setX(0);
		getScreen().attachWidget("Spoutcraft", screen);

		GenericLabel label = new GenericLabel("Connection Lost!");
		int size = Spoutcraft.getMinecraftFont().getTextWidth(label.getText());
		label.setX((int) (width / 2 - size / 2)).setY(16);
		label.setFixed(true).setPriority(RenderPriority.Lowest);
		getScreen().attachWidget("Spoutcraft", label);

		int top = 5;
		Color grey = new Color(0.80F, 0.80F, 0.80F, 0.65F);

		label = new GenericLabel(message);
		size = Spoutcraft.getMinecraftFont().getTextWidth(label.getText());
		label.setX((int) (width / 2 - size / 2)).setY(top);
		label.setTextColor(grey);
		screen.attachWidget("Spoutcraft", label);

		LocalTexture texture = new LocalTexture();
		texture.setUrl("/res/misc/disconnected.png").setX((int) (width / 2 - 64)).setY(top);
		texture.setHeight(128).setWidth(128);
		screen.attachWidget("Spoutcraft", texture);

		top += 116;

		Button button;
		button = new ReconnectButton().setText("Attempt to Reconnect");
		button.setHeight(20).setWidth(200);
		button.setX((int) (width / 2 - button.getWidth() / 2));
		button.setY(top);
		button.setAlign(WidgetAnchor.TOP_CENTER);
		screen.attachWidget("Spoutcraft", button);
		top += 26;

		button = new ReturnToServerList().setText("Return to " + SpoutClient.getInstance().getServerManager().getJoinedFromName());
		button.setHeight(20).setWidth(200);
		button.setX((int) (width / 2 - button.getWidth() / 2));
		button.setY(top);
		button.setAlign(WidgetAnchor.TOP_CENTER);
		screen.attachWidget("Spoutcraft", button);
		top += 26;

		button = new ReturnToMainMenu().setText("Return to Main Menu");
		button.setHeight(20).setWidth(200);
		button.setX((int) (width / 2 - button.getWidth() / 2));
		button.setY(top);
		button.setAlign(WidgetAnchor.TOP_CENTER);
		screen.attachWidget("Spoutcraft", button);
		top += 26;
	}

	@Override
	public void drawScreen(int var1, int var2, float var3) {
		drawDefaultBackground();
	}
}

class ReconnectButton extends GenericButton {
	public void onButtonClick() {
		Minecraft.theMinecraft.displayGuiScreen(new GuiConnecting(Minecraft.theMinecraft, GuiConnectionLost.lastServerIp, GuiConnectionLost.lastServerPort));
	}
}

class ReturnToMainMenu extends GenericButton {
	public void onButtonClick() {
		Minecraft.theMinecraft.displayGuiScreen(new org.spoutcraft.client.gui.mainmenu.MainMenu());
	}
}

class ReturnToServerList extends GenericButton {
	public void onButtonClick() {
		Minecraft.theMinecraft.displayGuiScreen(SpoutClient.getInstance().getServerManager().getJoinedFrom());
	}
}

class LocalTexture extends GenericTexture {
	public void render() {
		Texture texture = CustomTextureManager.getTextureFromJar(getUrl());
		if (texture != null) {
			GL11.glTranslatef((float) getScreenX(), (float) getScreenY(), 0); // Moves texture into place
			((MCRenderDelegate)Spoutcraft.getRenderDelegate()).drawTexture(texture, (int)getWidth(), (int)getHeight(), isDrawingAlphaChannel());
		}
	}
}
