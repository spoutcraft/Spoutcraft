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

import java.awt.Desktop;
import java.net.URL;

import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.event.screen.ButtonClickEvent;
import org.spoutcraft.spoutcraftapi.gui.Button;
import org.spoutcraft.spoutcraftapi.gui.Color;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;
import org.spoutcraft.spoutcraftapi.gui.GenericLabel;
import org.spoutcraft.spoutcraftapi.gui.GenericScrollArea;
import org.spoutcraft.spoutcraftapi.gui.RenderPriority;
import org.spoutcraft.spoutcraftapi.gui.WidgetAnchor;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiScreen;

public class GuiUnexpectedError extends GuiScreen{
	
	public GuiUnexpectedError() {
		
	}
	
	public void initGui() {
		Addon spoutcraft = Spoutcraft.getAddonManager().getAddon("Spoutcraft");
		
		GenericScrollArea screen = new GenericScrollArea();
		screen.setHeight(height - 16 - 24).setWidth(width).setY(16+24).setX(0);
		getScreen().attachWidget(spoutcraft, screen);
		
		GenericLabel label = new GenericLabel("Oh Noes!");
		int size = Spoutcraft.getMinecraftFont().getTextWidth(label.getText());
		label.setX((int) (width / 2 - size / 2)).setY(16);
		label.setFixed(true).setPriority(RenderPriority.Lowest);
		getScreen().attachWidget(spoutcraft, label);
		
		int top = 5;
		Color grey = new Color(0.80F, 0.80F, 0.80F, 0.65F);
		
		label = new GenericLabel("Spoutcraft has encounted an unexpected error! How shall we proceed?");
		size = Spoutcraft.getMinecraftFont().getTextWidth(label.getText());
		label.setX((int) (width / 2 - size / 2)).setY(top);
		label.setTextColor(grey);
		screen.attachWidget(spoutcraft, label);
		top += 22;
		
		label = new GenericLabel("1.) It's just a fluke. I'm a good person, errors don't happen to me. \nAnyway, Even if an error did happen, I'm sure it was just a cosmic mistake.\n" +
		"This error was just the result of Bill Gates/Steve Jobs/Linus Torvalds\nsummoning a forbidden spirit, and is unlikely to occur more than once,\n" +
		"maybe twice in a blue moon. But that's all superstious mumbo-jumbo anyway.\nThe point is that this will never happen again, so let's just move past it\n"+
		"and forgive and forget.\n\nWhat were we talking about again?");
		label.setX(10).setY(top);
		label.setTextColor(grey);
		screen.attachWidget(spoutcraft, label);
		top += 11 * 10;
		
		Button button = new IgnoreErrorButton().setText("Ignore & Return To Main Menu");
		button.setHeight(20).setWidth(200);
		button.setX((int) (width / 2 - button.getWidth() / 2));
		button.setY(top);
		button.setAlign(WidgetAnchor.TOP_CENTER);
		screen.attachWidget(spoutcraft, button);
		top += 32;
		
		label = new GenericLabel("2.) Oh dear, an error. I should report this right away so it can get fixed!");
		size = Spoutcraft.getMinecraftFont().getTextWidth(label.getText());
		label.setX(10).setY(top);
		label.setTextColor(grey);
		screen.attachWidget(spoutcraft, label);
		top += 22;
		
		button = new ReportErrorButton().setText("Report & Return To Main Menu");
		button.setHeight(20).setWidth(200);
		button.setX((int) (width / 2 - button.getWidth() / 2));
		button.setY(top);
		button.setAlign(WidgetAnchor.TOP_CENTER);
		screen.attachWidget(spoutcraft, button);
		top += 32;
		
		label = new GenericLabel("3.) An error? Fits this crummy mod like a glove. Get me out of here!");
		size = Spoutcraft.getMinecraftFont().getTextWidth(label.getText());
		label.setX(10).setY(top);
		label.setTextColor(grey);
		screen.attachWidget(spoutcraft, label);
		top += 22;
		
		button = new ExitGameButton().setText("Exit This Crummy Game");
		button.setHeight(20).setWidth(200);
		button.setX((int) (width / 2 - button.getWidth() / 2));
		button.setY(top);
		button.setAlign(WidgetAnchor.TOP_CENTER);
		screen.attachWidget(spoutcraft, button);
		top += 32;
	}
	
	@Override
	public void drawScreen(int var1, int var2, float var3) {
		drawDefaultBackground();
	}
	
	

}

class IgnoreErrorButton extends GenericButton {
	public void onButtonClick(ButtonClickEvent event) {
		Minecraft.theMinecraft.displayGuiScreen(new GuiMainMenu());
	}
}

class ReportErrorButton extends GenericButton {
	public void onButtonClick(ButtonClickEvent event) {
		try {
			URL url =  new URL("https://github.com/SpoutDev/Spout/issues/new");
			Desktop.getDesktop().browse(url.toURI());
		}
		catch (Exception e) { }
		Minecraft.theMinecraft.displayGuiScreen(new GuiMainMenu());
	}
}

class ExitGameButton extends GenericButton {
	public void onButtonClick(ButtonClickEvent event) {
		Minecraft.theMinecraft.shutdownMinecraftApplet();
	}
}
