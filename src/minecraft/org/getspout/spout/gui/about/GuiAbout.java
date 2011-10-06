package org.getspout.spout.gui.about;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.io.CustomTextureManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.spoutcraft.spoutcraftapi.gui.Color;
import org.spoutcraft.spoutcraftapi.gui.GenericGradient;
import org.spoutcraft.spoutcraftapi.gui.Gradient;
import org.spoutcraft.spoutcraftapi.gui.RenderUtil;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.Tessellator;

public class GuiAbout extends GuiScreen {
	private float scrolled = 0f;
	private boolean holdingScrollBar = false;
	private Color background1 = new Color(0.06F, 0.06F, 0.06F, 0.65F);
	private Color background2 = new Color(0.06F, 0.06F, 0.06F, 0.72F);
	private Color scrollBarColor = new Color(0.46F, 0.46F, 0.46F, 0.55F);
	private Color scrollBarColor2 = new Color(0.06F, 0.06F, 0.06F, 0.62F);
	private Gradient scrollArea = new GenericGradient();
	private static final int SCREEN_SIZE = 2000;
	private static final int SCREEN_START = 60;
	private static final int SCREEN_END = 94;
	private static final float SCROLL_FACTOR = 10f;
	Texture spoutcraftTexture = CustomTextureManager.getTextureFromJar("/res/spoutcraft.png");
	Texture yourkitLogo = CustomTextureManager.getTextureFromJar("/res/yourkit.png");
	public GuiAbout() {
		
	}
	
	@Override
	public void initGui() {
		this.controlList.clear();
		controlList.add(new GuiButton(1, this.width / 2 - 45, this.height - 25, 100, 20, "Main Menu"));
	}
	
	@Override
	public void actionPerformed(GuiButton button) {
		this.mc.displayGuiScreen(new GuiMainMenu());
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int click) {
		holdingScrollBar = false;
		boolean clickedScrollBar = mouseX >= this.width - 12 && mouseY <= this.width;
		int height = getInvertedScaledHeight(this.height );
		if (clickedScrollBar) {
			//do nothing if we clicked on the bar slider itself
			if (mouseY > height + 16 && mouseY < this.height - 50) {
				setScrolled(getScrolled() + 0.1f);
			}
			else if (mouseY < height && mouseY > 30) {
				setScrolled(getScrolled() - 0.1f);
			}
			else {
				holdingScrollBar = true;
			}
		}
		super.mouseClicked(mouseX, mouseY, click);
	}
	
	@Override
	public void mouseMovedOrUp(int mouseX, int mouseY, int click) {
		if (click != 0) { //still dragging
			if (holdingScrollBar) {
				int height = getInvertedScaledHeight(this.height);
				if (mouseY > height + 16) {
					setScrolled(getScrolled() + 0.01f);
				}
				else if (mouseY < height) {
					setScrolled(getScrolled() - 0.01f);
				}
			}
		}
		else {
			holdingScrollBar = false;
		}
		super.mouseMovedOrUp(mouseX, mouseY, click);
	}
	
	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
		int scroll = Mouse.getEventDWheel();
		if (scroll != 0) {
			setScrolled(getScrolled() + (scroll / (SCROLL_FACTOR * SCREEN_SIZE)));
		}
	}
	
	public float getScrolled() {
		return scrolled;
	}
	
	public void setScrolled(float f) {
		if (f > 1f) {
			scrolled = 1f;
		}
		else if (f < 0f) {
			scrolled = 0f;
		}
		else {
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
		
		drawScaledString("What is Spoutcraft?", this.width / 2 - 200, 0, 0xffffff);
		drawScaledString("Spoutcraft is a modification for the", this.width / 2 - 200, 10, 0x808080);
		drawScaledString("Minecraft client that plays exactly like", this.width / 2 - 200, 20, 0x808080);
		drawScaledString(" the official game. Its goal is to give mod", this.width / 2 - 200, 30, 0x808080);
		drawScaledString("developers an easy to use platform for", this.width / 2 - 200, 40, 0x808080);
		drawScaledString("building and distributing mods, while", this.width / 2 - 200, 50, 0x808080);
		drawScaledString("providing a rich user experience.", this.width / 2 - 200, 60, 0x808080);
		
		drawScaledString("What is Spout?", this.width / 2 - 200, 90, 0xffffff);
		drawScaledString("Spout is a Bukkit plugin development", this.width / 2 - 200, 100, 0x808080);
		drawScaledString("platform that allows for previously", this.width / 2 - 200, 110, 0x808080);
		drawScaledString("impossible tasks, such as custom items", this.width / 2 - 200, 120, 0x808080);
		drawScaledString("blocks, mobs, animals, and vehicles. ", this.width / 2 - 200, 130, 0x808080);
		
		drawScaledString("Who is SpoutDev?", this.width / 2 - 200, 160, 0xffffff);
		drawScaledString("SpoutDev is the team behind Spout, ", this.width / 2 - 200, 170, 0x808080);
		drawScaledString("SpoutAPI, Spoutcraft, SpoutcraftAPI,", this.width / 2 - 200, 180, 0x808080);
		drawScaledString("and the Spoutcraft Launcher.", this.width / 2 - 200, 190, 0x808080);
		
		drawScaledString("Sponsors", this.width / 2 + 30, 0, 0xffffff);
		
		int yourkitX = (this.width / 2 + 30);
		int yourkitY = getScaledHeight(60);
		if (yourkitLogo != null) {
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDepthMask(false);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glTranslatef(yourkitX, yourkitY, 0); // moves texture into place
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, yourkitLogo.getTextureID());
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			Tessellator tessellator = Tessellator.instance;
			//GL11.glScalef(0.5f, 0.5f, 0.5f);
			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV(0.0D, 15, -90, 0.0D, 0.0D); // draw corners
			tessellator.addVertexWithUV(55, 15, -90, yourkitLogo.getWidth(), 0.0D);
			tessellator.addVertexWithUV(55, 0.0D, -90, yourkitLogo.getWidth(), yourkitLogo.getHeight());
			tessellator.addVertexWithUV(0.0D, 0.0D, -90, 0.0D, yourkitLogo.getHeight());
			tessellator.draw();
			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glPopMatrix();
		}
		if (isInBoundingRect(yourkitX, yourkitY, 15, 55, x, y)) {
			drawTooltip("YourKit, LLC is the creator of innovative tools  ", x, y);
			drawTooltip("for profiling Java and .NET applications.          ", x, y+11);
			drawTooltip("Take a look at their products at www.yourkit.com", x, y+22);
		}
		
		drawScaledString("Team", this.width / 2 + 30, 90, 0xffffff);
		drawScaledString("Afforess - Lead Developer", this.width / 2 + 30, 100, 0x808080);
		drawScaledString("Wulfspider - Co-Lead & Support", this.width / 2 + 30, 110, 0x808080);
		drawScaledString("alta189 - Co-Lead & Developer", this.width / 2 + 30, 120, 0x808080);
		drawScaledString("Top_Cat - Developer", this.width / 2 + 30, 130, 0x808080);
		drawScaledString("Raphfrk - Developer", this.width / 2 + 30, 140, 0x808080);
		drawScaledString("narrowtux - Developer", this.width / 2 + 30, 150, 0x808080);
		drawScaledString("Olloth - Developer", this.width / 2 + 30, 160, 0x808080);
		drawScaledString("Email: dev@getspout.org", this.width / 2 + 30, 170, 0x808080);
		drawScaledString("Website: getspout.org", this.width / 2 + 30, 180, 0x808080);
		drawScaledString("IRC: #spout in Esper.net", this.width / 2 + 30, 190, 0x808080);
		
		drawScaledString("Contributors", this.width / 2 - 200, 220, 0xffffff);
		drawScaledString("The Bukkit Team - Bukkit Server API", this.width / 2 - 200, 230, 0x808080);
		drawScaledString("lahwran - Fast Events Code", this.width / 2 - 200, 240, 0x808080);
		drawScaledString("Celtic Minstrel - Code", this.width / 2 - 200, 250, 0x808080);
		drawScaledString("Zeerix - Threading Code", this.width / 2 - 200, 260, 0x808080);
		drawScaledString("Karlthepagan - Optick Mod", this.width / 2 - 200, 270, 0x808080);
		drawScaledString("Kahr - HD Textures Code", this.width / 2 - 200, 280, 0x808080);
		drawScaledString("Jeckari - Custom Texture Code", this.width / 2 - 200, 290, 0x808080);
		drawScaledString("Rychochet - GUI Code", this.width / 2 - 200, 300, 0x808080);
		drawScaledString("knowbuddy - GUI optimizations", this.width / 2 - 200, 310, 0x808080);
		drawScaledString("Apache Foundation - Code", this.width / 2 - 200, 320, 0x808080);
		
		
		drawScaledString("Move along, nothing to see here.", 40, 1750, 0x808080);
		
		GL11.glDisable(2896 /*GL_LIGHTING*/);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int var11 = this.mc.renderEngine.getTexture("/gui/allitems.png");
		this.mc.renderEngine.bindTexture(var11);
		RenderUtil.drawTexturedModalRectangle(this.width - 14, getInvertedScaledHeight(this.height), 0, 208, 16, 16, 0f);
		
		GL11.glDisable(2912 /*GL_FOG*/);
		GL11.glDisable(2929 /*GL_DEPTH_TEST*/);
		this.overlayBackground(0, 30, 255, 255);
		this.overlayBackground(this.height - 50, this.height, 255, 255);
		
		drawCenteredString(this.fontRenderer, "About", this.width / 2, 16, 0xffffff);
		
		GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.mc.renderEngine.getTexture("/title/mclogo.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPushMatrix();
		GL11.glTranslatef((0.0075f * this.width), (this.height - 45), 0);
		GL11.glScalef(0.4f, 0.4f, 0.4f);
		this.drawTexturedModalRect(0, 0, 0, 0, 155, 44);
		this.drawTexturedModalRect(155, 0, 0, 45, 155, 44);
		GL11.glPopMatrix();
		
		if (spoutcraftTexture != null) {
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glDepthMask(false);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glTranslatef((this.width - 120), (this.height - 45), 0); // moves texture into place
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, spoutcraftTexture.getTextureID());
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV(0.0D, 32, -90, 0.0D, 0.0D); // draw corners
			tessellator.addVertexWithUV(128, 32, -90, spoutcraftTexture.getWidth(), 0.0D);
			tessellator.addVertexWithUV(128, 0.0D, -90, spoutcraftTexture.getWidth(), spoutcraftTexture.getHeight());
			tessellator.addVertexWithUV(0.0D, 0.0D, -90, 0.0D, spoutcraftTexture.getHeight());
			tessellator.draw();
			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glPopMatrix();
		}
				
		drawString(this.fontRenderer, "Beta 1.8.1", (int)(this.width * 0.0075f), this.height - 20, 0xffffff);
		drawString(this.fontRenderer, "Copyright Mojang AB", (int)(this.width * 0.0075f), this.height - 10, 0x808080);
		
		drawString(this.fontRenderer, SpoutClient.getClientVersion().toString(), (int)(this.width - 44), this.height - 20, 0xffffff);
		drawString(this.fontRenderer, "Licensed under LGPLv3", (int)(this.width - 120), this.height - 10, 0x808080);
		
		getControlList().get(0).xPosition = this.width / 2 - 45;
		getControlList().get(0).yPosition = this.height - 25;
		getControlList().get(0).drawButton(this.mc, x, y);
		//super.drawScreen(x, x, z);
		
		//Shadow magic
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
		
		
	}

	private void overlayBackground(int var1, int var2, int var3, int var4) {
		Tessellator var5 = Tessellator.instance;
		GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.mc.renderEngine.getTexture("/gui/background.png"));
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
