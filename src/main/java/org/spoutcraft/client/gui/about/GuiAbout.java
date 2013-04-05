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
package org.spoutcraft.client.gui.about;

import java.awt.Desktop;
import java.net.URL;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.Tessellator;

import org.spoutcraft.api.gui.Color;
import org.spoutcraft.api.gui.GenericGradient;
import org.spoutcraft.api.gui.Gradient;
import org.spoutcraft.api.gui.RenderUtil;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.io.CustomTextureManager;

public class GuiAbout extends GuiScreen {
	private float scrolled = 0f;
	private boolean holdingScrollBar = false;
	private Color background1 = new Color(0.06F, 0.06F, 0.06F, 0.65F);
	private Color background2 = new Color(0.06F, 0.06F, 0.06F, 0.72F);
	private Color scrollBarColor = new Color(0.46F, 0.46F, 0.46F, 0.55F);
	private Color scrollBarColor2 = new Color(0.06F, 0.06F, 0.06F, 0.62F);
	private Gradient scrollArea = new GenericGradient();
	private static final int SCREEN_SIZE = 230;
	private static final int SCREEN_START = 60;
	private static final int SCREEN_END = 94;
	private static final float SCROLL_FACTOR = 10f;
	Texture spoutcraftTexture = CustomTextureManager.getTextureFromJar("/res/logo/spoutcraft.png");
	Texture beastNodeLogo = CustomTextureManager.getTextureFromJar("/res/logo/beastnode.png");
	Texture minecraftBizLogo = CustomTextureManager.getTextureFromJar("/res/logo/minecraft_biz.png");
	private int sourceY = -1;
	private int sourceWidth = -1;
	private boolean hoveringLink = false;
	private final GuiScreen parent;

	public GuiAbout(GuiScreen parent) {
		this.parent = parent;
	}

	@Override
	public void initGui() {
		this.buttonList.clear();
		buttonList.add(new GuiButton(1, this.width / 2 - 50, this.height - 25, 100, 20, "Main Menu"));
	}

	@Override
	public void actionPerformed(GuiButton button) {
		this.mc.displayGuiScreen(parent);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int click) {
		holdingScrollBar = false;
		int height = getInvertedScaledHeight(this.height );
		String browseUrl = null;
		if (mouseX >= this.width - 12 && mouseY <= this.width) {
			// Do nothing if we clicked on the slider bar itself
			if (mouseY > height + 16 && mouseY < this.height - 50) {
				setScrolled(getScrolled() + 0.1f);
			} else if (mouseY < height && mouseY > 30) {
				setScrolled(getScrolled() - 0.1f);
			} else {
				holdingScrollBar = true;
			}
		} else if (this.isInBoundingRect(this.width / 2 + 30, getScaledHeight(15), 33, 147, mouseX, mouseY)) {
			browseUrl = "http://spout.in/beast";
		} else if (this.isInBoundingRect((int)(0.0325f * this.width), (this.height - 40), (int)(44 * 0.4f), (int)(310 * 0.4f), mouseX, mouseY)) {
			browseUrl = "http://spout.in/minecraft";
		} else if (this.isInBoundingRect((this.width - 140), this.height - 45, 32, 128, mouseX, mouseY)) {
			browseUrl = "http://spout.in/spoutcraft";
		} else if (this.isInBoundingRect(this.width / 2 + 30, getScaledHeight(sourceY), 10, sourceWidth, mouseX, mouseY)) {
			browseUrl = "https://github.com/SpoutDev";
		}
		if (browseUrl != null) {
			try {
				URL url =  new URL(browseUrl);
				Desktop.getDesktop().browse(url.toURI());
			} catch (Exception e) { }
		}

		super.mouseClicked(mouseX, mouseY, click);
	}

	@Override
	public void mouseMovedOrUp(int mouseX, int mouseY, int click) {
		hoveringLink = false;
		if (click != 0) { // Still dragging
			if (holdingScrollBar) {
				int height = getInvertedScaledHeight(this.height);
				if (mouseY > height + 16) {
					setScrolled(getScrolled() + 0.01f);
				} else if (mouseY < height) {
					setScrolled(getScrolled() - 0.01f);
				}
			}
		} else {
			holdingScrollBar = false;
		}
		if (this.isInBoundingRect(this.width / 2 + 30, getScaledHeight(sourceY), 10, sourceWidth, mouseX, mouseY)) {
			hoveringLink = true;
		}
		super.mouseMovedOrUp(mouseX, mouseY, click);
	}

	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
		int scroll = Mouse.getEventDWheel();
		if (scroll != 0) {
			setScrolled(getScrolled() - (scroll / (SCROLL_FACTOR * SCREEN_SIZE)));
		}
	}

	public float getScrolled() {
		return scrolled;
	}

	public void setScrolled(float f) {
		if (f > 1f) {
			scrolled = 1f;
		} else if (f < 0f) {
			scrolled = 0f;
		} else {
			scrolled = f;
		}
	}

	public int getScaledHeight(int height) {
		return SCREEN_END + height - (int)(SCREEN_SIZE * scrolled) - SCREEN_START;
	}

	public int getInvertedScaledHeight(int height) {
		return (int)((this.height - SCREEN_END) * scrolled) + height - this.height + SCREEN_START / 2;
	}

	public void drawScaledString(String string, int width, int height, int color) {
		drawString(this.fontRenderer, string, width, getScaledHeight(height), color);
	}

	@Override
	public void drawScreen(int x, int y, float z) {
		super.drawBackground(0);
		GL11.glPushMatrix();

		GL11.glDisable(2896 /*GL_LIGHTING*/);
		GL11.glDisable(2912 /*GL_FOG*/);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		bg.setTopColor(background1);
		bg.setBottomColor(background2);
		bg.setY(30);
		bg.setHeight(this.height - 50);
		bg.setX(0);
		bg.setWidth(this.width - 12);
		bg.render();

		scrollArea.setY(30);
		scrollArea.setHeight(this.height - 50);
		scrollArea.setX(this.width - 12);
		scrollArea.setWidth(16);
		scrollArea.setTopColor(scrollBarColor);
		scrollArea.setBottomColor(scrollBarColor2);
		scrollArea.render();

		// Right half
		int top = 0;
		drawScaledString("What is Spoutcraft?", this.width / 2 - 200, top, 0xffffff); top += 10;
		drawScaledString("Spoutcraft is a modification for the", this.width / 2 - 200, top, 0x808080); top += 10;
		drawScaledString("Minecraft client that plays exactly", this.width / 2 - 200, top, 0x808080); top += 10;
		drawScaledString("like the official game. Its goal is", this.width / 2 - 200, top, 0x808080); top += 10;
		drawScaledString("to give developers an easy to use", this.width / 2 - 200, top, 0x808080); top += 10;
		drawScaledString("platform for building and distributing", this.width / 2 - 200, top, 0x808080); top += 10;
		drawScaledString("mods, while providing a rich", this.width / 2 - 200, top, 0x808080); top += 10;
		drawScaledString("gameplay experience for users.", this.width / 2 - 200, top, 0x808080); top += 10;

		top += 20;

		drawScaledString("What is Spout?", this.width / 2 - 200, top, 0xffffff); top += 10;
		drawScaledString("Spout is a Bukkit plugin development", this.width / 2 - 200, top, 0x808080); top += 10;
		drawScaledString("platform that allows for previously", this.width / 2 - 200, top, 0x808080); top += 10;
		drawScaledString("impossible tasks, such as custom items", this.width / 2 - 200, top, 0x808080); top += 10;
		drawScaledString("blocks, mobs, animals, and vehicles. ", this.width / 2 - 200, top, 0x808080); top += 10;

		top += 20;

		drawScaledString("Who is SpoutDev?", this.width / 2 - 200, top, 0xffffff); top += 10;
		drawScaledString("SpoutDev is the team behind Spout, ", this.width / 2 - 200, top, 0x808080); top += 10;
		drawScaledString("SpoutAPI, Spoutcraft,", this.width / 2 - 200, top, 0x808080); top += 10;
		drawScaledString("and Spoutcraft Launcher.", this.width / 2 - 200, top, 0x808080); top += 10;

		top += 20;

		drawScaledString("Contributors", this.width / 2 - 200, top, 0xffffff); top += 10;
		drawScaledString("The Bukkit Team - Bukkit Server API", this.width / 2 - 200, top, 0x808080); top += 10;
		drawScaledString("lahwran - Fast Events Code", this.width / 2 - 200, top, 0x808080); top += 10;
		drawScaledString("lahwran - Minimap Logic", this.width / 2 - 200, top, 0x808080); top += 10;
		drawScaledString("Celtic Minstrel - Code", this.width / 2 - 200, top, 0x808080); top += 10;
		drawScaledString("Zeerix - Threading Code", this.width / 2 - 200, top, 0x808080); top += 10;
		drawScaledString("Karlthepagan - OptiTick Code", this.width / 2 - 200, top, 0x808080); top += 10;
		drawScaledString("Kahr - HD Textures Code", this.width / 2 - 200, top, 0x808080); top += 10;
		drawScaledString("Jeckari - Custom Texture Code", this.width / 2 - 200, top, 0x808080); top += 10;
		drawScaledString("Rycochet - GUI Code", this.width / 2 - 200, top, 0x808080); top += 10;
		drawScaledString("knowbuddy - GUI Optimizations", this.width / 2 - 200, top, 0x808080); top += 10;
		drawScaledString("TomyLobo - Mipmapping Code", this.width / 2 - 200, top, 0x808080); top += 10;
		drawScaledString("Apache Foundation - Code", this.width / 2 - 200, top, 0x808080); top += 10;
		drawScaledString("ThVortex - FontRenderer fixes", this.width / 2 - 200, top, 0x808080); top += 10;

		// Left half
		top = 0;
		drawScaledString("Sponsors", this.width / 2 + 30, top, 0xffffff); top += 10;

		top = 130;

		drawScaledString("Team", this.width / 2 + 30, top, 0xffffff); top += 10;
		drawScaledString("Afforess - Lead Developer", this.width / 2 + 30, top, 0x808080); top += 10;
		drawScaledString("Wulfspider - Co-Lead & Support", this.width / 2 + 30, top, 0x808080); top += 10;
		drawScaledString("alta189 - Co-Lead & Developer", this.width / 2 + 30, top, 0x808080); top += 10;
		drawScaledString("Top_Cat - Developer", this.width / 2 + 30, top, 0x808080); top += 10;
		drawScaledString("raphfrk - Developer", this.width / 2 + 30, top, 0x808080); top += 10;
		drawScaledString("narrowtux - Developer", this.width / 2 + 30, top, 0x808080); top += 10;
		drawScaledString("Olloth - Developer", this.width / 2 + 30, top, 0x808080); top += 10;
		drawScaledString("Rycochet - Developer", this.width / 2 + 30, top, 0x808080); top += 10;
		drawScaledString("RoyAwesome - Developer", this.width / 2 + 30, top, 0x808080); top += 10;
		drawScaledString("zml2008 - Developer", this.width / 2 + 30, top, 0x808080); top += 10;
		drawScaledString("Zidane - Developer", this.width / 2 + 30, top, 0x808080); top += 10;

		top += 20;

		drawScaledString("Contact", this.width / 2 + 30, top, 0xffffff); top += 10;
		drawScaledString("Email: dev@spout.org", this.width / 2 + 30, top, 0x808080); top += 10;
		drawScaledString("Website: spout.org", this.width / 2 + 30, top, 0x808080); top += 10;
		drawScaledString("#spout on irc.esper.net", this.width / 2 + 30, top, 0x808080); top += 10;

		top += 20;

		drawScaledString("Note", this.width / 2 + 30, top, 0xffffff); top += 10;
		drawScaledString("Spoutcraft, Spout, and related", this.width / 2 + 30, top, 0x808080); top += 10;
		drawScaledString("projects are open source. You", this.width / 2 + 30, top, 0x808080); top += 10;
		drawScaledString("can browse the source code at", this.width / 2 + 30, top, 0x808080);  top += 10;
		sourceY = top;
		sourceWidth = this.fontRenderer.getStringWidth("https://github.com/SpoutDev");
		drawScaledString("https://github.com/SpoutDev", this.width / 2 + 30, sourceY, hoveringLink ? 0x65A5D1 : 0x176093); top += 10;

		top += 20;

		int beastNodeX = (this.width / 2 + 30);
		int beastNodeY = getScaledHeight(15);
		if (beastNodeLogo != null) {
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDepthMask(false);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glTranslatef(beastNodeX, beastNodeY, 0); // Moves texture into place
			SpoutClient.getHandle().renderEngine.bindTexture(beastNodeLogo.getTextureID());
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			Tessellator tessellator = Tessellator.instance;
			//GL11.glScalef(0.5f, 0.5f, 0.5f);
			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV(0.0D, 33, -90, 0.0D, 0.0D); // Draw corners
			tessellator.addVertexWithUV(147, 33, -90, beastNodeLogo.getWidth(), 0.0D);
			tessellator.addVertexWithUV(147, 0.0D, -90, beastNodeLogo.getWidth(), beastNodeLogo.getHeight());
			tessellator.addVertexWithUV(0.0D, 0.0D, -90, 0.0D, beastNodeLogo.getHeight());
			tessellator.draw();
			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glPopMatrix();
		}

		int minecraftBizX = (this.width / 2 + 30);
		int minecraftBizY = getScaledHeight(85);
		if (minecraftBizLogo != null) {
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDepthMask(false);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glTranslatef(minecraftBizX, minecraftBizY, 0); // Moves texture into place
			SpoutClient.getHandle().renderEngine.bindTexture(minecraftBizLogo.getTextureID());
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			Tessellator tessellator = Tessellator.instance;
			//GL11.glScalef(0.5f, 0.5f, 0.5f);
			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV(0.0D, 33, -90, 0.0D, 0.0D); // Draw corners
			tessellator.addVertexWithUV(147, 33, -90, minecraftBizLogo.getWidth(), 0.0D);
			tessellator.addVertexWithUV(147, 0.0D, -90, minecraftBizLogo.getWidth(), minecraftBizLogo.getHeight());
			tessellator.addVertexWithUV(0.0D, 0.0D, -90, 0.0D, minecraftBizLogo.getHeight());
			tessellator.draw();
			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glPopMatrix();
		}

		// Draw tooltips
		if (isInBoundingRect(beastNodeX, beastNodeY, 33, 147, x, y)) {
			drawTooltip(
				"BeastNode provides high quality Minecraft and web hosting at affordable\n" +
				"prices and is generously sponsoring the Spout project with its hosting\n" +
				"& server needs. Mine, build, craft, and chat with your own high quality\n" +
				"Minecraft server with FREE mumble voice server and web hosting.", x, y);
		} else if (isInBoundingRect(minecraftBizX, minecraftBizY, 33, 147, x, y)) {
			drawTooltip(
				"Your Minecraft community in a new dimension!", x, y);
		}

		GL11.glDisable(2896 /*GL_LIGHTING*/);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		SpoutClient.getHandle().renderEngine.bindTexture("/gui/gui.png");
		RenderUtil.drawTexturedModalRectangle(this.width - 14, getInvertedScaledHeight(this.height), 0, 208, 16, 16, 0f);

		GL11.glDisable(2912 /*GL_FOG*/);
		GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
		this.overlayBackground(0, 30, 255, 255);
		this.overlayBackground(this.height - 50, this.height, 255, 255);
		drawCenteredString(this.fontRenderer, "About", this.width / 2, 16, 0xffffff);
		SpoutClient.getHandle().renderEngine.bindTexture("/title/mclogo.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPushMatrix();
		GL11.glTranslatef((0.0325f * this.width), (this.height - 40), 0);
		GL11.glScalef(0.4f, 0.4f, 0.4f);
		this.drawTexturedModalRect(0, 0, 0, 0, 155, 44);
		this.drawTexturedModalRect(155, 0, 0, 45, 155, 44);
		GL11.glPopMatrix();

		if (spoutcraftTexture != null) {
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glDepthMask(false);
			GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glTranslatef((this.width - 140), (this.height - 45), 0); // Moves texture into place
			SpoutClient.getHandle().renderEngine.bindTexture(spoutcraftTexture.getTextureID());
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV(0.0D, 32, -90, 0.0D, 0.0D); // Draw corners
			tessellator.addVertexWithUV(128, 32, -90, spoutcraftTexture.getWidth(), 0.0D);
			tessellator.addVertexWithUV(128, 0.0D, -90, spoutcraftTexture.getWidth(), spoutcraftTexture.getHeight());
			tessellator.addVertexWithUV(0.0D, 0.0D, -90, 0.0D, spoutcraftTexture.getHeight());
			tessellator.draw();
			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glPopMatrix();
		}

		drawString(this.fontRenderer, "Unknown Version", (int)(this.width * 0.034f), this.height - 20, 0xffffff);
		drawString(this.fontRenderer, "Copyright Mojang AB", (int)(this.width * 0.034f), this.height - 10, 0x808080);

		String version = SpoutClient.getClientVersion();
		drawString(this.fontRenderer, version, (int)(this.width - 45) - fontRenderer.getStringWidth(version) + fontRenderer.getStringWidth("0"), this.height - 20, 0xffffff);
		drawString(this.fontRenderer, "Licensed under LGPLv3", (int)(this.width - 129), this.height - 10, 0x808080);

		((GuiButton)this.buttonList.get(0)).xPosition = this.width / 2 - 50;
		((GuiButton)this.buttonList.get(0)).yPosition = this.height - 25;
		((GuiButton)this.buttonList.get(0)).drawButton(this.mc, x, y);
		//super.drawScreen(x, x, z);

		// Shadow magic
		GL11.glEnable(3042 /*GL_BLEND*/);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
		GL11.glShadeModel(7425 /*GL_SMOOTH*/);
		GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
		Tessellator var16 = Tessellator.instance;
		byte var19 = 4;
		var16.startDrawingQuads();
		var16.setColorRGBA_I(0, 0);
		var16.addVertexWithUV(0, (double)(30 + var19), 0.0D, 0.0D, 1.0D);
		var16.addVertexWithUV(this.width - 12, (double)(30 + var19), 0.0D, 1.0D, 1.0D);
		var16.setColorRGBA_I(0, 255);
		var16.addVertexWithUV(this.width - 12, 30, 0.0D, 1.0D, 0.0D);
		var16.addVertexWithUV(0, 30, 0.0D, 0.0D, 0.0D);
		var16.draw();
		var16.startDrawingQuads();
		var16.setColorRGBA_I(0, 255);
		var16.addVertexWithUV(0, this.height - 50, 0.0D, 0.0D, 1.0D);
		var16.addVertexWithUV(this.width - 12, this.height - 50, 0.0D, 1.0D, 1.0D);
		var16.setColorRGBA_I(0, 0);
		var16.addVertexWithUV(this.width - 12, (double)(this.height - 50 - var19), 0.0D, 1.0D, 0.0D);
		var16.addVertexWithUV(0, (double)(this.height - 50 - var19), 0.0D, 0.0D, 0.0D);
		var16.draw();

		GL11.glEnable(3008 /*GL_ALPHA_TEST*/);

		GL11.glPopMatrix();
	}

	private void overlayBackground(int var1, int var2, int var3, int var4) {
		Tessellator var5 = Tessellator.instance;
		SpoutClient.getHandle().renderEngine.bindTexture("/gui/background.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float var6 = 32.0F;
		var5.startDrawingQuads();
		var5.setColorRGBA_I(4210752, var4);
		var5.addVertexWithUV(0.0D, (double)var2, 0.0D, 0.0D, (double)((float)var2 / var6));
		var5.addVertexWithUV((double)this.width, (double)var2, 0.0D, (double)((float)this.width / var6), (double)((float)var2 / var6));
		var5.setColorRGBA_I(4210752, var3);
		var5.addVertexWithUV((double)this.width, (double)var1, 0.0D, (double)((float)this.width / var6), (double)((float)var1 / var6));
		var5.addVertexWithUV(0.0D, (double)var1, 0.0D, 0.0D, (double)((float)var1 / var6));
		var5.draw();
	}
}
