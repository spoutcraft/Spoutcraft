package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiOptions;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSelectWorld;
import net.minecraft.src.GuiTexturePacks;
import net.minecraft.src.MathHelper;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.Tessellator;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class GuiMainMenu extends GuiScreen {

	private static final Random rand = new Random();
	private float updateCounter = 0.0F;
	private String splashText = "missingno";
	private GuiButton multiplayerButton;
	private int field_35357_f = 0;
	private int field_35358_g;

	public GuiMainMenu() {
		try {
			ArrayList var1 = new ArrayList();
			BufferedReader var2 = new BufferedReader(new InputStreamReader(GuiMainMenu.class.getResourceAsStream("/title/splashes.txt"), Charset.forName("UTF-8")));
			String var3 = "";

			while((var3 = var2.readLine()) != null) {
				var3 = var3.trim();
				if(var3.length() > 0) {
					var1.add(var3);
				}
			}

			do {
				this.splashText = (String)var1.get(rand.nextInt(var1.size()));
			} while(this.splashText.hashCode() == 125780783);
		} catch (Exception var4) {
			;
		}
	}

	public void updateScreen() {
		++this.updateCounter;
		++this.field_35357_f;
	}

	public boolean doesGuiPauseGame() {
		return false;
	}

	protected void keyTyped(char var1, int var2) {}

	public void initGui() {
		this.field_35358_g = this.mc.renderEngine.allocateAndSetupTexture(new BufferedImage(256, 256, 2));
		Calendar var1 = Calendar.getInstance();
		var1.setTime(new Date());
		if(var1.get(2) + 1 == 11 && var1.get(5) == 9) {
			this.splashText = "Happy birthday, ez!";
		} else if(var1.get(2) + 1 == 6 && var1.get(5) == 1) {
			this.splashText = "Happy birthday, Notch!";
		} else if(var1.get(2) + 1 == 12 && var1.get(5) == 24) {
			this.splashText = "Merry X-mas!";
		} else if(var1.get(2) + 1 == 1 && var1.get(5) == 1) {
			this.splashText = "Happy new year!";
		}
		//Spout start
		else {
			String text = org.getspout.spout.EasterEggs.getSplashTextEasterEgg();
			if (text != null) {
				this.splashText = text;
			}
		}
		//Spout end

		StringTranslate var2 = StringTranslate.getInstance();
		int var4 = this.height / 4 + 48;
		this.controlList.add(new GuiButton(1, this.width / 2 - 100, var4, var2.translateKey("menu.singleplayer")));
		this.controlList.add(this.multiplayerButton = new GuiButton(2, this.width / 2 - 100, var4 + 24, var2.translateKey("menu.multiplayer")));
		this.controlList.add(new GuiButton(3, this.width / 2 - 100, var4 + 48, "Texture Packs")); //Spout
		if(this.mc.hideQuitButton) {
			this.controlList.add(new GuiButton(0, this.width / 2 - 100, var4 + 72, var2.translateKey("menu.options")));
		} else {
		//Spout Start
			this.controlList.add(new GuiButton(0, this.width / 2 - 100, var4 + 72, 98, 20, var2.translateKey("menu.options")));
			this.controlList.add(new GuiButton(4, this.width / 2 + 2, var4 + 72, 98, 20, var2.translateKey("menu.quit")));
		}
		this.controlList.add(new GuiButton(5, this.width / 2 - 100, var4 + 96, "About"));
		//Spout End

		if(this.mc.session == null) {
			this.multiplayerButton.enabled = false;
		}

	}

	protected void actionPerformed(GuiButton var1) {
		if(var1.id == 0) {
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
		}

		if(var1.id == 1) {
			this.mc.displayGuiScreen(new GuiSelectWorld(this));
		}

		if(var1.id == 2) {
			this.mc.displayGuiScreen(new org.getspout.spout.gui.server.GuiFavorites(this)); //Spout
		}

		if(var1.id == 3) {
			this.mc.displayGuiScreen(new GuiTexturePacks(this));
		}

		if(var1.id == 4) {
			this.mc.shutdown();
		}
		//Spout Start
		if(var1.id == 5) {
			this.mc.displayGuiScreen(new org.getspout.spout.gui.about.GuiAbout());
		}
		//Spout End

	}

	private void func_35355_b(int var1, int var2, float var3) {
		Tessellator var4 = Tessellator.instance;
		GL11.glMatrixMode(5889 /*GL_PROJECTION*/);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GLU.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
		GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		GL11.glEnable(3042 /*GL_BLEND*/);
		GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
		GL11.glDisable(2884 /*GL_CULL_FACE*/);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(770, 771);
		byte var5 = 8;

		for(int var6 = 0; var6 < var5 * var5; ++var6) {
			GL11.glPushMatrix();
			float var7 = ((float)(var6 % var5) / (float)var5 - 0.5F) / 64.0F;
			float var8 = ((float)(var6 / var5) / (float)var5 - 0.5F) / 64.0F;
			float var9 = 0.0F;
			GL11.glTranslatef(var7, var8, var9);
			GL11.glRotatef(MathHelper.sin(((float)this.field_35357_f + var3) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(-((float)this.field_35357_f + var3) * 0.1F, 0.0F, 1.0F, 0.0F);

			for(int var10 = 0; var10 < 6; ++var10) {
				GL11.glPushMatrix();
				if(var10 == 1) {
					GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
				}

				if(var10 == 2) {
					GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
				}

				if(var10 == 3) {
					GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
				}

				if(var10 == 4) {
					GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
				}

				if(var10 == 5) {
					GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				}

				GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.mc.renderEngine.getTexture("/title/bg/panorama" + var10 + ".png"));
				var4.startDrawingQuads();
				var4.setColorRGBA_I(16777215, 255 / (var6 + 1));
				float var11 = 0.0F;
				var4.addVertexWithUV(-1.0D, -1.0D, 1.0D, (double)(0.0F + var11), (double)(0.0F + var11));
				var4.addVertexWithUV(1.0D, -1.0D, 1.0D, (double)(1.0F - var11), (double)(0.0F + var11));
				var4.addVertexWithUV(1.0D, 1.0D, 1.0D, (double)(1.0F - var11), (double)(1.0F - var11));
				var4.addVertexWithUV(-1.0D, 1.0D, 1.0D, (double)(0.0F + var11), (double)(1.0F - var11));
				var4.draw();
				GL11.glPopMatrix();
			}

			GL11.glPopMatrix();
			GL11.glColorMask(true, true, true, false);
		}

		var4.setTranslationD(0.0D, 0.0D, 0.0D);
		GL11.glColorMask(true, true, true, true);
		GL11.glMatrixMode(5889 /*GL_PROJECTION*/);
		GL11.glPopMatrix();
		GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/);
		GL11.glPopMatrix();
		GL11.glDepthMask(true);
		GL11.glEnable(2884 /*GL_CULL_FACE*/);
		GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
		GL11.glEnable(2929 /*GL_DEPTH_TEST*/);
	}

	private void func_35354_a(float var1) {
		GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.field_35358_g);
		GL11.glCopyTexSubImage2D(3553 /*GL_TEXTURE_2D*/, 0, 0, 0, 0, 0, 256, 256);
		GL11.glEnable(3042 /*GL_BLEND*/);
		GL11.glBlendFunc(770, 771);
		GL11.glColorMask(true, true, true, false);
		Tessellator var2 = Tessellator.instance;
		var2.startDrawingQuads();
		byte var3 = 3;

		for(int var4 = 0; var4 < var3; ++var4) {
			var2.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F / (float)(var4 + 1));
			int var5 = this.width;
			int var6 = this.height;
			float var7 = (float)(var4 - var3 / 2) / 256.0F;
			var2.addVertexWithUV((double)var5, (double)var6, (double)this.zLevel, (double)(0.0F + var7), 0.0D);
			var2.addVertexWithUV((double)var5, 0.0D, (double)this.zLevel, (double)(1.0F + var7), 0.0D);
			var2.addVertexWithUV(0.0D, 0.0D, (double)this.zLevel, (double)(1.0F + var7), 1.0D);
			var2.addVertexWithUV(0.0D, (double)var6, (double)this.zLevel, (double)(0.0F + var7), 1.0D);
		}

		var2.draw();
		GL11.glColorMask(true, true, true, true);
	}

	private void func_35356_c(int var1, int var2, float var3) {
		GL11.glViewport(0, 0, 256, 256);
		this.func_35355_b(var1, var2, var3);
		this.func_35354_a(var3);
		this.func_35354_a(var3);
		this.func_35354_a(var3);
		this.func_35354_a(var3);
		this.func_35354_a(var3);
		this.func_35354_a(var3);
		this.func_35354_a(var3);
		this.func_35354_a(var3);
		GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
		GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10241 /*GL_TEXTURE_MIN_FILTER*/, 9729 /*GL_LINEAR*/);
		GL11.glTexParameteri(3553 /*GL_TEXTURE_2D*/, 10240 /*GL_TEXTURE_MAG_FILTER*/, 9729 /*GL_LINEAR*/);
		Tessellator var4 = Tessellator.instance;
		var4.startDrawingQuads();
		float var5 = this.width > this.height?120.0F / (float)this.width:120.0F / (float)this.height;
		float var6 = (float)this.height * var5 / 256.0F;
		float var7 = (float)this.width * var5 / 256.0F;
		var4.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
		int var8 = this.width;
		int var9 = this.height;
		var4.addVertexWithUV(0.0D, (double)var9, (double)this.zLevel, (double)(0.5F - var6), (double)(0.5F + var7));
		var4.addVertexWithUV((double)var8, (double)var9, (double)this.zLevel, (double)(0.5F - var6), (double)(0.5F - var7));
		var4.addVertexWithUV((double)var8, 0.0D, (double)this.zLevel, (double)(0.5F + var6), (double)(0.5F - var7));
		var4.addVertexWithUV(0.0D, 0.0D, (double)this.zLevel, (double)(0.5F + var6), (double)(0.5F + var7));
		var4.draw();
	}

	public void drawScreen(int var1, int var2, float var3) {
		this.func_35356_c(var1, var2, var3);
		Tessellator var4 = Tessellator.instance;
		short var5 = 274;
		int var6 = this.width / 2 - var5 / 2;
		byte var7 = 30;
		this.drawGradientRect(0, 0, this.width, this.height, -1426063361, 16777215);
		this.drawGradientRect(0, 0, this.width, this.height, 0, -1442840576);
		GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, this.mc.renderEngine.getTexture("/title/mclogo.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.drawTexturedModalRect(var6 + 0, var7 + 0, 0, 0, 155, 44);
		this.drawTexturedModalRect(var6 + 155, var7 + 0, 0, 45, 155, 44);
		var4.setColorOpaque_I(16777215);
		GL11.glPushMatrix();
		GL11.glTranslatef((float)(this.width / 2 + 90), 70.0F, 0.0F);
		GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
		float var8 = 1.8F - MathHelper.abs(MathHelper.sin((float)(System.currentTimeMillis() % 1000L) / 1000.0F * 3.1415927F * 2.0F) * 0.1F);
		var8 = var8 * 100.0F / (float)(this.fontRenderer.getStringWidth(this.splashText) + 32);
		GL11.glScalef(var8, var8, var8);
		this.drawCenteredString(this.fontRenderer, this.splashText, 0, -8, 16776960);
		GL11.glPopMatrix();

		//Spout Start
		//this.drawString(this.fontRenderer, "Minecraft Beta 1.8.1", 2, this.height - 10, 16777215); //Spout
		//String var9 = "Copyright Mojang AB. Do not distribute!";
		//this.drawString(this.fontRenderer, var9, this.width - this.fontRenderer.getStringWidth(var9) - 2, this.height - 10, 16777215); //Spout
		
		
		//var9 = "Spoutcraft " + SpoutClient.getClientVersion().toString() + " - Minecraft Beta 1.8.1" + " - " + var9;
		//this.drawCenteredString(this.fontRenderer, var9, this.width / 2, this.height - 10, 0x1F3C8E);
		//Spout End
		super.drawScreen(var1, var2, var3);
	}

}
