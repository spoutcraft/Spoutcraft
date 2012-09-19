/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiScreen;
import org.bukkit.ChatColor;

import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.addon.Addon;
import org.spoutcraft.api.event.screen.ButtonClickEvent;
import org.spoutcraft.api.gui.Button;
import org.spoutcraft.api.gui.Color;
import org.spoutcraft.api.gui.GenericButton;
import org.spoutcraft.api.gui.GenericLabel;
import org.spoutcraft.api.gui.GenericScrollArea;
import org.spoutcraft.api.gui.RenderPriority;
import org.spoutcraft.api.gui.WidgetAnchor;


public class GuiUnexpectedError extends GuiScreen {

	private Throwable caused;
	private GenericLabel hastebinLink;
	
	private String hastebinURL;
	private boolean generated = false;

	public GuiUnexpectedError(Throwable caused) {
		this.caused = caused;
	}

	public void initGui() {
		Addon spoutcraft = Spoutcraft.getAddonManager().getAddon("Spoutcraft");

		GenericScrollArea screen = new GenericScrollArea();
		screen.setHeight(height - 16 - 24).setWidth(width).setY(16 + 24).setX(0);
		getScreen().attachWidget(spoutcraft, screen);

		GenericLabel label = new GenericLabel("Oh Noes! An Error has occured!");
		int size = Spoutcraft.getMinecraftFont().getTextWidth(label.getText());
		label.setX((int) (width / 2 - size / 2)).setY(16);
		label.setFixed(true).setPriority(RenderPriority.Lowest);
		getScreen().attachWidget(spoutcraft, label);

		int top = 60;
		Color grey = new Color(0.80F, 0.80F, 0.80F, 0.65F);

		hastebinLink = new GenericLabel("Generating hastie...");
		hastebinLink.setX(95).setY(top);
		hastebinLink.setTextColor(grey);
		screen.attachWidget(spoutcraft, hastebinLink);
		generateHastie();
		
		Button button = new CopyErrorURL(this).setText("Copy link");
		button.setHeight(20).setWidth(80);
		button.setX((int) (hastebinLink.getWidth()+hastebinLink.getX()+10.0));
		button.setY(top-5);
		button.setAlign(WidgetAnchor.TOP_CENTER);
		screen.attachWidget(spoutcraft, button);
		
		top += 25;

		button = new ReportErrorButton().setText("Report");
		button.setHeight(20).setWidth(70);
		button.setX((int) (width / 2 - button.getWidth() - button.getWidth()/2));
		button.setY(top);
		button.setAlign(WidgetAnchor.TOP_CENTER);
		screen.attachWidget(spoutcraft, button);
		
		button = new IgnoreErrorButton().setText("Ignore");
		button.setHeight(20).setWidth(70);
		button.setX((int) (width / 2 + button.getWidth() /2));
		button.setY(top);
		button.setAlign(WidgetAnchor.TOP_CENTER);
		screen.attachWidget(spoutcraft, button);
		top += 30;
	}

	@Override
	public void drawScreen(int var1, int var2, float var3) {
		drawDefaultBackground();
	}

	private void generateHastie() {
		if(generated) {
			hastebinLink.setText("Error link: "+ChatColor.GREEN+hastebinURL);
			return;
		}
		try {
			String data = "Spoutcraft b"+SpoutClient.getClientVersion()+" error:"+"\n\n";
			data += caused.toString()+"\n";
			for(StackTraceElement ele : caused.getStackTrace()) {
				data+=ele.toString()+"\n";
			}
			
			URL url = new URL("http://www.hastebin.com/documents");
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();

			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = rd.readLine();
			hastebinURL = "hastebin.com/"+line.substring(8, line.length()-2); //get rid of the json stuff
			hastebinLink.setText("Error: "+ChatColor.GREEN+hastebinURL);
			generated = true;
			wr.close();
			rd.close();
		} catch (Exception e) {
			hastebinLink.setText("Connection error!");
		}
	}

	protected void copyErrorToClipboard() {
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(hastebinURL), null);
	}
}
class CopyErrorURL extends GenericButton {
	
	private GuiUnexpectedError error;
	CopyErrorURL(GuiUnexpectedError error) {
		this.error = error;
	}
	
	public void onButtonClick(ButtonClickEvent event) {
		error.copyErrorToClipboard();
	}
}

class IgnoreErrorButton extends GenericButton {

	public void onButtonClick(ButtonClickEvent event) {
		Minecraft.theMinecraft.displayGuiScreen(new org.spoutcraft.client.gui.mainmenu.MainMenu());
	}
}

class ReportErrorButton extends GenericButton {

	public void onButtonClick(ButtonClickEvent event) {
		SpoutClient.disableSandbox();
		try {
			URL url = new URL("http://spout.in/issues");
			Desktop.getDesktop().browse(url.toURI());
		} catch (Exception e) {
		} finally {
			SpoutClient.enableSandbox();
		}
		Minecraft.theMinecraft.displayGuiScreen(new org.spoutcraft.client.gui.mainmenu.MainMenu());
	}
}

class ExitGameButton extends GenericButton {

	public void onButtonClick(ButtonClickEvent event) {
		Minecraft.theMinecraft.shutdownMinecraftApplet();
	}
}
