/*
 * This file is part of Spoutcraft (http://wiki.getspout.org/).
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
package org.getspout.spout.gui.error;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiConnecting;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiScreen;

import org.getspout.spout.gui.MCRenderDelegate;
import org.getspout.spout.gui.server.GuiFavorites2;
import org.getspout.spout.io.CustomTextureManager;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.gui.Button;
import org.spoutcraft.spoutcraftapi.gui.Color;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;
import org.spoutcraft.spoutcraftapi.gui.GenericLabel;
import org.spoutcraft.spoutcraftapi.gui.GenericScrollArea;
import org.spoutcraft.spoutcraftapi.gui.GenericTexture;
import org.spoutcraft.spoutcraftapi.gui.RenderPriority;
import org.spoutcraft.spoutcraftapi.gui.WidgetAnchor;

public class GuiConnectionLost extends GuiScreen{
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
		Addon spoutcraft = Spoutcraft.getAddonManager().getAddon("Spoutcraft");
		
		GenericScrollArea screen = new GenericScrollArea();
		screen.setHeight(height - 16 - 24).setWidth(width).setY(16+24).setX(0);
		getScreen().attachWidget(spoutcraft, screen);
		
		GenericLabel label = new GenericLabel("Connection Lost!");
		int size = Spoutcraft.getMinecraftFont().getTextWidth(label.getText());
		label.setX((int) (width / 2 - size / 2)).setY(16);
		label.setFixed(true).setPriority(RenderPriority.Lowest);
		getScreen().attachWidget(spoutcraft, label);
		
		int top = 5;
		Color grey = new Color(0.80F, 0.80F, 0.80F, 0.65F);
		
		label = new GenericLabel(message);
		size = Spoutcraft.getMinecraftFont().getTextWidth(label.getText());
		label.setX((int) (width / 2 - size / 2)).setY(top);
		label.setTextColor(grey);
		screen.attachWidget(spoutcraft, label);
		
		LocalTexture texture = new LocalTexture();
		texture.setUrl("/res/disconnected.png").setX((int) (width / 2 - 64)).setY(top);
		texture.setHeight(128).setWidth(128);
		screen.attachWidget(spoutcraft, texture);
		
		top += 116;
		
		Button button;
		button = new ReconnectButton().setText("Attempt to Reconnect");
		button.setHeight(20).setWidth(200);
		button.setX((int) (width / 2 - button.getWidth() / 2));
		button.setY(top);
		button.setAlign(WidgetAnchor.TOP_CENTER);
		screen.attachWidget(spoutcraft, button);
		top += 26;
		
		button = new ReturnToServerList().setText("Return to Server List");
		button.setHeight(20).setWidth(200);
		button.setX((int) (width / 2 - button.getWidth() / 2));
		button.setY(top);
		button.setAlign(WidgetAnchor.TOP_CENTER);
		screen.attachWidget(spoutcraft, button);
		top += 26;
		
		button = new ReturnToMainMenu().setText("Return to Main Menu");
		button.setHeight(20).setWidth(200);
		button.setX((int) (width / 2 - button.getWidth() / 2));
		button.setY(top);
		button.setAlign(WidgetAnchor.TOP_CENTER);
		screen.attachWidget(spoutcraft, button);
		top += 26;
	}
	
	@Override
	public void drawScreen(int var1, int var2, float var3) {
		drawDefaultBackground();
	}
}

class ReconnectButton extends GenericButton {
	public void onButtonClick(ButtonClickEvent event) {
		Minecraft.theMinecraft.displayGuiScreen(new GuiConnecting(Minecraft.theMinecraft, GuiConnectionLost.lastServerIp, GuiConnectionLost.lastServerPort));
	}
}

class ReturnToMainMenu extends GenericButton {
	public void onButtonClick(ButtonClickEvent event) {
		Minecraft.theMinecraft.displayGuiScreen(new GuiMainMenu());
	}
}

class ReturnToServerList extends GenericButton {
	public void onButtonClick(ButtonClickEvent event) {
		Minecraft.theMinecraft.displayGuiScreen(new GuiFavorites2(new GuiMainMenu()));
	}
}

class LocalTexture extends GenericTexture {
	public void render() {
		Texture texture = CustomTextureManager.getTextureFromJar(getUrl());
		if (texture != null) {
			GL11.glTranslatef((float) getScreenX(), (float) getScreenY(), 0); // moves texture into place
			((MCRenderDelegate)Spoutcraft.getRenderDelegate()).drawTexture(texture, (int)getWidth(), (int)getHeight(), isDrawingAlphaChannel());
		}
	}
}

