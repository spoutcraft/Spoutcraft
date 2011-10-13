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
package org.getspout.spout.gui.server;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Map;

import net.minecraft.src.*;

import org.bukkit.ChatColor;
import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.io.CustomTextureManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.yaml.snakeyaml.Yaml;

public class GuiServerInfo extends GuiScreen {

	private ServerSlot info;
	private Texture image;
	private Texture flag;
	private String url = "";
	private String site = "";
	private String forum = "";
	private GuiScreen back;

	public GuiServerInfo(ServerSlot info, GuiScreen back) {
		if (!info.loaded) {
			try {
				URL url = new URL("http://servers.getspout.org/api.php?id=" + info.uniqueid);
				BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
				Yaml yaml = new Yaml();
				ArrayList<Map<String, String>> list = (ArrayList<Map<String, String>>) yaml.load(reader);
				reader.close();
				
				Map<String, String> i = list.get(0);
				info.country = i.get("country");
				info.site = URLDecoder.decode(i.get("site"), "UTF-8");
				info.forum = URLDecoder.decode(i.get("forumurl"), "UTF-8");
				info.description = URLDecoder.decode(i.get("longdescription"), "UTF-8");
				info.loaded = true;
			} catch (IOException e) {}
		}
		
		int maxlen = 210;
		this.info = info;
		this.back = back;
		url = "http://servers.getspout.org/preview/" + info.uniqueid + ".png";
		site = shorten(info.site, maxlen);
		forum = shorten(info.forum, maxlen);
		CustomTextureManager.downloadTexture(url);
		CustomTextureManager.downloadTexture("http://servers.getspout.org/images/flags/" + info.country.toLowerCase() + ".png");
	}
	
	public String shorten(String string, int width) {
		FontRenderer fr = SpoutClient.getHandle().fontRenderer;
		boolean shortened = false;
		while (fr.getStringWidth(string) > width) {
			shortened = true;
			string = string.substring(0, string.length() - 1);
		}
		return string + (shortened ? "..." : "");
	}

	public void updateScreen() {
		
	}

	public void initGui() {
		StringTranslate var1 = StringTranslate.getInstance();
		this.controlList.clear();
		this.controlList.add(new GuiButton(0, this.width / 2 - 30, this.height / 4 + 150, 110, 20, var1.translateKey("Join")));
		this.controlList.add(new GuiButton(1, this.width / 2 + 90, this.height / 4 + 150, 110, 20, var1.translateKey("Back")));
		this.controlList.add(new GuiButton(2, this.width / 2 - 200, this.height / 4 + 150, 160, 20, var1.translateKey("Help promote this server")));
	}

	public void onGuiClosed() {
		
	}

	public void actionPerformed(GuiButton button) {
		if(button.enabled) {
			if (button.id == 0) {
				int port = info.port.length() > 0 ? Integer.parseInt(info.port) : 25565;
				SpoutClient.getHandle().displayGuiScreen(new GuiConnecting(SpoutClient.getHandle(), info.ip, port));
			} else if (button.id == 1) {
				SpoutClient.getHandle().displayGuiScreen(back);
				if (back instanceof GuiMultiplayer) {
					((GuiMultiplayer) back).updateList();
				}
			} else if (button.id == 2) {
				openLink("http://servers.getspout.org/info/" + info.uniqueid + ".php");
			}
		}
	}

	public void keyTyped(char letter, int key) {
		
	}
	
	public void openLink(String url) {
		try {
			java.net.URI uri = new java.net.URI(url);
			Desktop desktop = Desktop.getDesktop();
			desktop.browse(uri);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public void mouseClicked(int var1, int var2, int var3) {
		if (var1 >= this.width / 2 - 10 && var1 <= this.width / 2 - 10 + SpoutClient.getHandle().fontRenderer.getStringWidth(site) && var2 >= this.height / 2 - 10 && var2 <= this.height / 2 - 2) {
			openLink(info.site);
		} else if (var1 >= this.width / 2 - 10 && var1 <= this.width / 2 - 10 + SpoutClient.getHandle().fontRenderer.getStringWidth(forum) && var2 >= this.height / 2 + 15 && var2 <= this.height / 2 + 23) {
			openLink(info.forum);
		}
		super.mouseClicked(var1, var2, var3);
	}

	public void drawScreen(int var1, int var2, float var3) {
		this.drawDefaultBackground();
		
		if (flag != null) {
			GL11.glPushMatrix();
			GL11.glTranslatef(this.width / 2 + 150, this.height / 2 - 84, 0);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDepthMask(false);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, flag.getTextureID());
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV(0.0D, 6, -90, 0.0D, 0.0D); // draw corners
			tessellator.addVertexWithUV(9, 6, -90, flag.getWidth(), 0.0D);
			tessellator.addVertexWithUV(9, 0.0D, -90, flag.getWidth(), flag.getHeight());
			tessellator.addVertexWithUV(0.0D, 0.0D, -90, 0.0D, flag.getHeight());
			tessellator.draw();
			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glPopMatrix();
		} else {
			flag = CustomTextureManager.getTextureFromUrl("http://servers.getspout.org/images/flags/" + info.country.toLowerCase() + ".png");
		}
		
		if (image != null) {
			GL11.glPushMatrix();
			GL11.glTranslatef(this.width / 2 - 110 - (image.getImageWidth() / 4), this.height / 2 - 20 - (image.getImageHeight() / 4), 0);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDepthMask(false);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, image.getTextureID());
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV(0.0D, image.getImageHeight() / 2, -90, 0.0D, 0.0D); // draw corners
			tessellator.addVertexWithUV(image.getImageWidth() / 2, image.getImageHeight() / 2, -90, image.getWidth(), 0.0D);
			tessellator.addVertexWithUV(image.getImageWidth() / 2, 0.0D, -90, image.getWidth(), image.getHeight());
			tessellator.addVertexWithUV(0.0D, 0.0D, -90, 0.0D, image.getHeight());
			tessellator.draw();
			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glPopMatrix();
		} else {
			image = CustomTextureManager.getTextureFromUrl(url);
		}
		
		this.drawString(SpoutClient.getHandle().fontRenderer, "Server IP:Port", this.width / 2 - 20, this.height / 2 - 95, 0xFFFFFF);
		this.drawString(SpoutClient.getHandle().fontRenderer, info.ip + (info.port.length() > 0 ? ":" : "") + info.port, this.width / 2 - 10, this.height / 2 - 85, 0xA0A0A0);
		
		this.drawString(SpoutClient.getHandle().fontRenderer, "Name", this.width / 2 - 20, this.height / 2 - 70, 0xFFFFFF);
		this.drawString(SpoutClient.getHandle().fontRenderer, info.name, this.width / 2 - 10, this.height / 2 - 60, 0xA0A0A0);
		
		this.drawString(SpoutClient.getHandle().fontRenderer, "Country", this.width / 2 + 120, this.height / 2 - 95, 0xFFFFFF);
		this.drawString(SpoutClient.getHandle().fontRenderer, info.country, this.width / 2 + 130, this.height / 2 - 85, 0xA0A0A0);
		
		this.drawString(SpoutClient.getHandle().fontRenderer, "Players", this.width / 2 + 120, this.height / 2 - 70, 0xFFFFFF);
		this.drawString(SpoutClient.getHandle().fontRenderer, info.players + "/" + info.maxPlayers, this.width / 2 + 130, this.height / 2 - 60, 0xA0A0A0);
		
		this.drawString(SpoutClient.getHandle().fontRenderer, "Response", this.width / 2 - 20, this.height / 2 - 45, 0xFFFFFF);
		this.drawString(SpoutClient.getHandle().fontRenderer, info.msg + ChatColor.GRAY + " (" + info.ping+"ms)", this.width / 2 - 10, this.height / 2 - 35, 0xA0A0A0);
		
		this.drawString(SpoutClient.getHandle().fontRenderer, "Website", this.width / 2 - 20, this.height / 2 - 20, 0xFFFFFF);
		this.drawString(SpoutClient.getHandle().fontRenderer, site, this.width / 2 - 10, this.height / 2 - 10, 0x40FFFF);
		
		this.drawString(SpoutClient.getHandle().fontRenderer, "Forum Post", this.width / 2 - 20, this.height / 2 + 5, 0xFFFFFF);
		this.drawString(SpoutClient.getHandle().fontRenderer, forum, this.width / 2 - 10, this.height / 2 + 15, 0x40FFFF);
		
		this.drawString(SpoutClient.getHandle().fontRenderer, "Description", this.width / 2 - 20, this.height / 2 + 30, 0xFFFFFF);
		this.drawString(SpoutClient.getHandle().fontRenderer, info.description, this.width / 2 - 10, this.height / 2 + 40, 0xA0A0A0);
		
		super.drawScreen(var1, var2, var3);
	}
}