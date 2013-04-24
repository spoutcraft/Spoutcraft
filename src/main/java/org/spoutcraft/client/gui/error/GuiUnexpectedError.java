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

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;

import org.lwjgl.opengl.GL11;
import org.lwjgl.Sys;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.TexturePackList;

import org.bukkit.ChatColor;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.gui.Button;
import org.spoutcraft.api.gui.Color;
import org.spoutcraft.api.gui.GenericButton;
import org.spoutcraft.api.gui.GenericLabel;
import org.spoutcraft.api.gui.GenericScrollArea;
import org.spoutcraft.api.gui.RenderPriority;
import org.spoutcraft.api.gui.WidgetAnchor;
import org.spoutcraft.client.SpoutClient;

public class GuiUnexpectedError extends GuiScreen {
	private Throwable caused;
	private GenericLabel hastebinLink;

	private String hastebinURL;
	private boolean generated = false;

	public GuiUnexpectedError(Throwable caused) {
		this.caused = caused;
	}

	public void initGui() {

		GenericScrollArea screen = new GenericScrollArea();
		screen.setHeight(height - 16 - 24).setWidth(width).setY(16 + 24).setX(0);
		getScreen().attachWidget("Spoutcraft", screen);

		GenericLabel label = new GenericLabel("Oh noes! An error has occurred!");
		int size = Spoutcraft.getMinecraftFont().getTextWidth(label.getText());
		label.setX((int) (width / 2 - size / 2)).setY(16);
		label.setFixed(true).setPriority(RenderPriority.Lowest);
		getScreen().attachWidget("Spoutcraft", label);

		int top = 60;
		Color grey = new Color(0.80F, 0.80F, 0.80F, 0.65F);

		hastebinLink = new GenericLabel("Generating hastie...");
		hastebinLink.setX(95).setY(top);
		hastebinLink.setTextColor(grey);
		screen.attachWidget("Spoutcraft", hastebinLink);
		generateHastie();

		Button button = new CopyErrorURL(this).setText("Copy Link");
		button.setHeight(20).setWidth(80);
		button.setX((int) (hastebinLink.getWidth() + hastebinLink.getX() + 10.0));
		button.setY(top-5);
		button.setAlign(WidgetAnchor.TOP_CENTER);
		screen.attachWidget("Spoutcraft", button);

		top += 25;

		button = new ReportErrorButton().setText("Report");
		button.setHeight(20).setWidth(70);
		button.setX((int) (width / 2 - button.getWidth() - button.getWidth() / 2));
		button.setY(top);
		button.setAlign(WidgetAnchor.TOP_CENTER);
		screen.attachWidget("Spoutcraft", button);

		button = new IgnoreErrorButton().setText("Ignore");
		button.setHeight(20).setWidth(70);
		button.setX((int) (width / 2 + button.getWidth() / 2));
		button.setY(top);
		button.setAlign(WidgetAnchor.TOP_CENTER);
		screen.attachWidget("Spoutcraft", button);
		top += 30;
	}

	@Override
	public void drawScreen(int var1, int var2, float var3) {
		drawDefaultBackground();
	}

	private void generateHastie() {
		if (generated) {
			hastebinLink.setText("Error Link: " + ChatColor.GREEN+hastebinURL);
			return;
		}
		try {
			StringBuilder builder = new StringBuilder("Spoutcraft Error Report:\n");
			builder.append("    Build: ").append(SpoutClient.getClientVersion()).append("\n");
			builder.append("-----------------------------------").append("\n");
			builder.append("Stack Trace:").append("\n");
			builder.append("    Exception: ").append(caused.getClass().getSimpleName()).append("\n");
			builder.append("    Message: ").append(caused.getMessage()).append("\n");
			builder.append("    Trace:").append("\n");
			
			StringWriter sw = new StringWriter();
			caused.printStackTrace(new PrintWriter(sw));
			String causeString = sw.toString();
			builder.append("       ").append(sw).append("\n");
			
			builder.append("-----------------------------------").append("\n");
			builder.append("Minecraft Information:\n");
			builder.append("    Texture Pack: ").append(Minecraft.theMinecraft.texturePackList.getSelectedTexturePack().getTexturePackFileName()).append("\n");
			//builder.append("    Texture Pack Res: ").append(TileSize.int_size + "x").append("\n");
			builder.append("    LWJGL Version: ").append(Sys.getVersion()).append("\n");

			builder.append("System Information:\n");
			builder.append("    Operating System: ").append(System.getProperty("os.name")).append("\n");
			builder.append("    Operating System Version: ").append(System.getProperty("os.version")).append("\n");
			builder.append("    Operating System Architecture: ").append(System.getProperty("os.arch")).append("\n");
			builder.append("    Java version: ").append(System.getProperty("java.version")).append(" ").append(System.getProperty("sun.arch.data.model", "32")).append(" bit").append("\n");
			builder.append("    Total Memory: ").append(Runtime.getRuntime().totalMemory() / 1024L / 1024L).append(" MB\n");
			builder.append("    Max Memory: ").append(Runtime.getRuntime().maxMemory() / 1024L / 1024L).append(" MB\n");
			builder.append("    Memory Free: ").append(Runtime.getRuntime().freeMemory() / 1024L / 1024L).append(" MB\n");
			builder.append("    CPU Cores: ").append(Runtime.getRuntime().availableProcessors()).append("\n");
			builder.append("    OpenGL Version: ").append(GL11.glGetString(GL11.GL_VERSION)).append("\n");
			builder.append("    OpenGL Vendor: ").append(GL11.glGetString(GL11.GL_VENDOR)).append("\n");
			String message = builder.toString();

			PasteBinAPI pastebin = new PasteBinAPI("963f01dd506cb3f607a487bc34b60d16");
			String response = pastebin.makePaste(message, "ser_" + System.currentTimeMillis(), "text");
			System.out.println("pastebin response: " + response);
			if (!response.startsWith("http://pastebin.com")) {
				URL url = new URL("http://www.hastebin.com/documents");
				URLConnection conn = url.openConnection();
				conn.setDoOutput(true);
				OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
				wr.write(builder.toString());
				wr.flush();

				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line = rd.readLine();
				hastebinURL = "hastebin.com/" + line.substring(8, line.length() - 2); // Get rid of the JSON stuff
				wr.close();
				rd.close();
			} else {
				hastebinURL = response;
			}
			hastebinLink.setText("Error: " + ChatColor.GREEN+hastebinURL);
			generated = true;
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

	public void onButtonClick() {
		error.copyErrorToClipboard();
	}
}

class IgnoreErrorButton extends GenericButton {
	public void onButtonClick() {
		Minecraft.theMinecraft.displayGuiScreen(new org.spoutcraft.client.gui.mainmenu.MainMenu());
	}
}

class ReportErrorButton extends GenericButton {
	public void onButtonClick() {
		try {
			URL url = new URL("http://spout.in/issues");
			Desktop.getDesktop().browse(url.toURI());
		} catch (Exception e) { }
		Minecraft.theMinecraft.displayGuiScreen(new org.spoutcraft.client.gui.mainmenu.MainMenu());
	}
}

class ExitGameButton extends GenericButton {
	public void onButtonClick() {
		Minecraft.theMinecraft.shutdownMinecraftApplet();
	}
}
