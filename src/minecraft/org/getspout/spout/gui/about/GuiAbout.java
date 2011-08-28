package org.getspout.spout.gui.about;

import org.getspout.spout.client.SpoutClient;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiScreen;

public class GuiAbout extends GuiScreen {
	public GuiAbout() {
		
	}
	
	@Override
	public void initGui() {
		this.controlList.clear();
		controlList.add(new GuiButton(1, this.width / 2 + 155, this.height - 241, 60, 16, "Main Menu"));
	}
	
	@Override
	public void actionPerformed(GuiButton button) {
		this.mc.displayGuiScreen(new GuiMainMenu());
	}
	
		@Override
	public void drawScreen(int x, int y, float z) {
		super.drawBackground(0);
		drawString(this.fontRenderer, "Spoutcraft (" + SpoutClient.getClientVersion().toString() + "):", this.width / 2 - 205, this.height - 233, 0x2554C7);
		drawString(this.fontRenderer, "Spoutcraft is a modified Minecraft client that plays exactly like the official", this.width / 2 - 190, this.height - 223, 0x357EC7);
		drawString(this.fontRenderer, "client. Spoutcraft follows the official game rules, but allows other mods and", this.width / 2 - 190, this.height - 213, 0x357EC7);
		drawString(this.fontRenderer, "server plugins to modify the game mechanics more easily and in previously ", this.width / 2 - 190, this.height - 203, 0x357EC7);
		drawString(this.fontRenderer, "impossible ways.", this.width / 2 - 190, this.height - 190, 0x357EC7);
		
		drawString(this.fontRenderer, "Spout:", this.width / 2 - 205, this.height - 163, 0x2554C7);
		drawString(this.fontRenderer, "Spout is a Bukkit server plugin. It does not modify the game mechanics. Instead", this.width / 2 - 190, this.height - 153, 0x357EC7);
		drawString(this.fontRenderer, "it improves the server performance, provides a way for server plugins to", this.width / 2 - 190, this.height - 143, 0x357EC7);
		drawString(this.fontRenderer, "easily modify the game mechanics for Spoutcraft users and provides", this.width / 2 - 190, this.height - 133, 0x357EC7);
		drawString(this.fontRenderer, "useful new developer tools and API.", this.width / 2 - 190, this.height - 120, 0x357EC7);
		
		drawString(this.fontRenderer, "SpoutAPI:", this.width / 2 - 205, this.height - 93, 0x2554C7);
		drawString(this.fontRenderer, "SpoutAPI is the official SpoutAPI for developers to use when writing Spout", this.width / 2 - 190, this.height - 83, 0x357EC7);
		drawString(this.fontRenderer, "plugins. The SpoutAPI is supported both by using a Bukkit server and Spout", this.width / 2 - 190, this.height - 73, 0x357EC7);
		drawString(this.fontRenderer, "plugin or just using the Glowstone server.", this.width / 2 - 190, this.height - 63, 0x357EC7);
		
		drawString(this.fontRenderer, "The Spout Team:", this.width / 2 - 205, this.height - 33, 0x2554C7);
		drawString(this.fontRenderer, "Afforess - Lead Developer, alta189 - Developer, Raphfrk - Developer", this.width / 2 - 190, this.height - 23, 0x357EC7);
		drawString(this.fontRenderer, "narrowtux - Developer, Top_Cat - Developer, Wulfspider - Web Developer", this.width / 2 - 190, this.height - 13, 0x357EC7);
		super.drawScreen(x, x, z);
	}
}
