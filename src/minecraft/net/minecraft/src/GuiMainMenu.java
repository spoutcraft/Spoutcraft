package net.minecraft.src;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;

import org.bukkit.ChatColor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.opengl.Texture;
import org.spoutcraft.client.config.ConfigReader;
import org.spoutcraft.client.gui.MCRenderDelegate;
import org.spoutcraft.client.gui.addon.GuiAddonsLocal;
import org.spoutcraft.client.gui.settings.GameSettingsScreen;
import org.spoutcraft.client.io.CustomTextureManager;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.gui.Button;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;
import org.spoutcraft.spoutcraftapi.gui.Screen;

public class GuiMainMenu extends GuiScreen
{
	/** The RNG used by the Main Menu Screen. */
	private static final Random rand = new Random();

	/** Counts the number of screen updates. */
	private float updateCounter;

	/** The splash message. */
	private String splashText;
	private GuiButton multiplayerButton;
	private int field_35357_f;

	// Spout Start
	private Button buttonSinglePlayer, buttonMultiPlayer, buttonAddons, buttonTextures, buttonOptions, buttonAbout, buttonQuit, buttonFastLogin;

	// Spout End

	/**
	 * Texture allocated for the current viewport of the main menu's panorama background.
	 */
	private int viewportTexture;

	public GuiMainMenu()
	{
		updateCounter = 0.0F;
		field_35357_f = 0;
		splashText = "missingno";

		try
		{
			ArrayList arraylist = new ArrayList();
			BufferedReader bufferedreader = new BufferedReader(new InputStreamReader((net.minecraft.src.GuiMainMenu.class).getResourceAsStream("/title/splashes.txt"), Charset.forName("UTF-8")));
			String s = "";

			do
			{
				String s1;

				if ((s1 = bufferedreader.readLine()) == null)
				{
					break;
				}

				s1 = s1.trim();

				if (s1.length() > 0)
				{
					arraylist.add(s1);
				}
			}
			while (true);

			do
			{
				splashText = (String)arraylist.get(rand.nextInt(arraylist.size()));
			}
			while (splashText.hashCode() == 0x77f432f);
		}
		catch (Exception exception) { }

		updateCounter = rand.nextFloat();
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen()
	{
		field_35357_f++;
	}

	/**
	 * Returns true if this GUI should pause the game when it is displayed in single-player
	 */
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char c, int i)
	{
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui()
	{
		viewportTexture = mc.renderEngine.allocateAndSetupTexture(new java.awt.image.BufferedImage(256, 256, 2));
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		if (calendar.get(2) + 1 == 11 && calendar.get(5) == 9)
		{
			splashText = "Happy birthday, ez!";
		}
		else if (calendar.get(2) + 1 == 6 && calendar.get(5) == 1)
		{
			splashText = "Happy birthday, Notch!";
		}
		else if (calendar.get(2) + 1 == 12 && calendar.get(5) == 24)
		{
			splashText = "Merry X-mas!";
		}
		else if (calendar.get(2) + 1 == 1 && calendar.get(5) == 1)
		{
			splashText = "Happy new year!";
		}

		// Spout start
		String text = org.spoutcraft.client.EasterEggs.getSplashTextEasterEgg();
		if (text != null) {
			this.splashText = text;
		}
		StringTranslate tr = StringTranslate.getInstance();

		buttonSinglePlayer = new GenericButton(tr.translateKey("menu.singleplayer"));
		buttonMultiPlayer = new GenericButton(tr.translateKey("menu.multiplayer"));
		buttonAddons = new GenericButton("Addons");
		buttonTextures = new GenericButton("Textures");
		buttonOptions = new GenericButton(tr.translateKey("menu.options"));
		buttonAbout = new GenericButton("About");
		buttonQuit = new GenericButton(tr.translateKey("menu.quit"));
		buttonFastLogin = new GenericButton(ChatColor.GREEN + "Fast Login");
		if (!ConfigReader.fastLogin) {
			buttonFastLogin.setEnabled(false).setVisible(false);
		}

		int left = 5;
		int bottom = height - 25;
		int widthl = 100;
		int widthr = 75;
		int right = this.width - 5 - widthr;
		buttonTextures.setX(left).setY(bottom).setWidth(widthl).setHeight(20);
		buttonQuit.setX(right).setY(bottom).setWidth(widthr).setHeight(20);

		bottom -= 25;
		buttonAddons.setX(left).setY(bottom).setWidth(widthl).setHeight(20);
		buttonAbout.setX(right).setY(bottom).setWidth(widthr).setHeight(20);

		bottom -= 25;
		buttonMultiPlayer.setX(left).setY(bottom).setWidth(widthl).setHeight(20);
		buttonOptions.setX(right).setY(bottom).setWidth(widthr).setHeight(20);

		bottom -= 25;
		buttonSinglePlayer.setX(left).setY(bottom).setWidth(widthl).setHeight(20);
		buttonFastLogin.setX(right).setY(bottom).setWidth(widthr).setHeight(20);

		Addon spoutcraft = Spoutcraft.getAddonManager().getAddon("Spoutcraft");
		Screen s = getScreen();
		s.attachWidgets(spoutcraft, buttonTextures, buttonQuit, buttonAddons, buttonAbout, buttonMultiPlayer, buttonOptions, buttonSinglePlayer, buttonFastLogin);

		if (this.mc.session == null) {
			buttonMultiPlayer.setEnabled(false);
		}
		// Spout End
	}

	// Spout Start
	@Override
	protected void buttonClicked(Button btn) {
		if (btn == buttonSinglePlayer) {
			mc.displayGuiScreen(new org.spoutcraft.client.gui.singleplayer.GuiWorldSelection(this));
		}
		if (btn == buttonMultiPlayer) {
			mc.displayGuiScreen(new org.spoutcraft.client.gui.server.GuiFavorites(this));
		}
		if (btn == buttonAddons) {
			this.mc.displayGuiScreen(new GuiAddonsLocal());
		}
		if (btn == buttonTextures) {
			mc.displayGuiScreen(new org.spoutcraft.client.gui.texturepacks.GuiTexturePacks());
		}
		if (btn == buttonOptions) {
			mc.displayGuiScreen(new GameSettingsScreen(this));
		}
		if (btn == buttonAbout) {
			this.mc.displayGuiScreen(new org.spoutcraft.client.gui.about.GuiAbout());
		}
		if (btn == buttonQuit) {
			mc.shutdown();
		}
		if (btn == buttonFastLogin) {
			ConfigReader.fastLogin = !ConfigReader.fastLogin;
			ConfigReader.write();
			buttonFastLogin.setText((ConfigReader.fastLogin ? ChatColor.GREEN : ChatColor.RED) + "Fast Login");
		}
	}

	// Spout End

	/**
	 * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
	 */
	//Spout removed method because our own Gui API does that.
	//	protected void actionPerformed(GuiButton par1GuiButton)
	//	{
	//		if (par1GuiButton.id == 0)
	//		{
	//			mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
	//		}
	//
	//		if (par1GuiButton.id == 5)
	//		{
	//			mc.displayGuiScreen(new GuiLanguage(this, mc.gameSettings));
	//		}
	//
	//		if (par1GuiButton.id == 1)
	//		{
	//			mc.displayGuiScreen(new GuiSelectWorld(this));
	//		}
	//
	//		if (par1GuiButton.id == 2)
	//		{
	//			mc.displayGuiScreen(new GuiMultiplayer(this));
	//		}
	//
	//		if (par1GuiButton.id == 3)
	//		{
	//			mc.displayGuiScreen(new GuiTexturePacks(this));
	//		}
	//
	//		if (par1GuiButton.id == 4)
	//		{
	//			mc.shutdown();
	//		}
	//	}

	/**
	 * Draws the main menu panorama
	 */
	private void drawPanorama(int par1, int par2, float par3)
	{
		Tessellator tessellator = Tessellator.instance;
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GLU.gluPerspective(120F, 1.0F, 0.05F, 10F);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glRotatef(180F, 1.0F, 0.0F, 0.0F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		int i = 8;

		for (int j = 0; j < i * i; j++)
		{
			GL11.glPushMatrix();
			float f = ((float)(j % i) / (float)i - 0.5F) / 64F;
			float f1 = ((float)(j / i) / (float)i - 0.5F) / 64F;
			float f2 = 0.0F;
			GL11.glTranslatef(f, f1, f2);
			GL11.glRotatef(MathHelper.sin(((float)field_35357_f + par3) / 400F) * 25F + 20F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(-((float)field_35357_f + par3) * 0.1F, 0.0F, 1.0F, 0.0F);

			for (int k = 0; k < 6; k++)
			{
				GL11.glPushMatrix();

				if (k == 1)
				{
					GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
				}

				if (k == 2)
				{
					GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
				}

				if (k == 3)
				{
					GL11.glRotatef(-90F, 0.0F, 1.0F, 0.0F);
				}

				if (k == 4)
				{
					GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
				}

				if (k == 5)
				{
					GL11.glRotatef(-90F, 1.0F, 0.0F, 0.0F);
				}

				GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture((new StringBuilder()).append("/title/bg/panorama").append(k).append(".png").toString()));
				tessellator.startDrawingQuads();
				tessellator.setColorRGBA_I(0xffffff, 255 / (j + 1));
				float f3 = 0.0F;
				tessellator.addVertexWithUV(-1D, -1D, 1.0D, 0.0F + f3, 0.0F + f3);
				tessellator.addVertexWithUV(1.0D, -1D, 1.0D, 1.0F - f3, 0.0F + f3);
				tessellator.addVertexWithUV(1.0D, 1.0D, 1.0D, 1.0F - f3, 1.0F - f3);
				tessellator.addVertexWithUV(-1D, 1.0D, 1.0D, 0.0F + f3, 1.0F - f3);
				tessellator.draw();
				GL11.glPopMatrix();
			}

			GL11.glPopMatrix();
			GL11.glColorMask(true, true, true, false);
		}

		tessellator.setTranslationD(0.0D, 0.0D, 0.0D);
		GL11.glColorMask(true, true, true, true);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPopMatrix();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	/**
	 * Rotate and blurs the skybox view in the main menu
	 */
	private void rotateAndBlurSkybox(float par1)
	{
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, viewportTexture);
		GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColorMask(true, true, true, false);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		byte byte0 = 3;

		for (int i = 0; i < byte0; i++)
		{
			tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F / (float)(i + 1));
			int j = width;
			int k = height;
			float f = (float)(i - byte0 / 2) / 256F;
			tessellator.addVertexWithUV(j, k, zLevel, 0.0F + f, 0.0D);
			tessellator.addVertexWithUV(j, 0.0D, zLevel, 1.0F + f, 0.0D);
			tessellator.addVertexWithUV(0.0D, 0.0D, zLevel, 1.0F + f, 1.0D);
			tessellator.addVertexWithUV(0.0D, k, zLevel, 0.0F + f, 1.0D);
		}

		tessellator.draw();
		GL11.glColorMask(true, true, true, true);
	}

	/**
	 * Renders the skybox in the main menu
	 */
	private void renderSkybox(int par1, int par2, float par3)
	{
		GL11.glViewport(0, 0, 256, 256);
		drawPanorama(par1, par2, par3);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		rotateAndBlurSkybox(par3);
		rotateAndBlurSkybox(par3);
		rotateAndBlurSkybox(par3);
		rotateAndBlurSkybox(par3);
		rotateAndBlurSkybox(par3);
		rotateAndBlurSkybox(par3);
		rotateAndBlurSkybox(par3);
		rotateAndBlurSkybox(par3);
		GL11.glViewport(0, 0, mc.displayWidth, mc.displayHeight);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		float f = width <= height ? 120F / (float)height : 120F / (float)width;
		float f1 = ((float)height * f) / 256F;
		float f2 = ((float)width * f) / 256F;
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
		int i = width;
		int j = height;
		tessellator.addVertexWithUV(0.0D, j, zLevel, 0.5F - f1, 0.5F + f2);
		tessellator.addVertexWithUV(i, j, zLevel, 0.5F - f1, 0.5F - f2);
		tessellator.addVertexWithUV(i, 0.0D, zLevel, 0.5F + f1, 0.5F - f2);
		tessellator.addVertexWithUV(0.0D, 0.0D, zLevel, 0.5F + f1, 0.5F + f2);
		tessellator.draw();
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3)
	{
		renderSkybox(par1, par2, par3);
		Tessellator tessellator = Tessellator.instance;
		char c = 274;
		int i = width / 2 - c / 2;
		byte byte0 = 30;
		drawGradientRect(0, 0, width, height, 0x80ffffff, 0xffffff);
		drawGradientRect(0, 0, width, height, 0, 0x80000000);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/title/mclogo.png"));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		if ((double)updateCounter < 0.0001D)
		{
			drawTexturedModalRect(i + 0, byte0 + 0, 0, 0, 99, 44);
			drawTexturedModalRect(i + 99, byte0 + 0, 129, 0, 27, 44);
			drawTexturedModalRect(i + 99 + 26, byte0 + 0, 126, 0, 3, 44);
			drawTexturedModalRect(i + 99 + 26 + 3, byte0 + 0, 99, 0, 26, 44);
			drawTexturedModalRect(i + 155, byte0 + 0, 0, 45, 155, 44);
		}
		else
		{
			drawTexturedModalRect(i + 0, byte0 + 0, 0, 0, 155, 44);
			drawTexturedModalRect(i + 155, byte0 + 0, 0, 45, 155, 44);
		}

		tessellator.setColorOpaque_I(0xffffff);
		GL11.glPushMatrix();
		GL11.glTranslatef(10, 50.0F, 0.0F); //Spout adjusted position
		//GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F); //Spout removed rotation
		float f = 1.8F - MathHelper.abs(MathHelper.sin(((float)(System.currentTimeMillis() % 1000L) / 1000F) * (float)Math.PI * 2.0F) * 0.1F);
		f = (f * 100F) / (float)(fontRenderer.getStringWidth(splashText) + 32);
		GL11.glScalef(f, f, f);
		drawCenteredString(fontRenderer, splashText, 0, -8, 0xffff00);
		GL11.glPopMatrix();

		//Spout Start
		String powered = "Powered by";
		int stringWidth = fontRenderer.getStringWidth(powered);
		drawString(this.fontRenderer, powered, width - stringWidth - 10, 3, 0xffbadfe6);
		GL11.glPushMatrix();
		GL11.glTranslated(width - 256*0.75 - 10, 8, 0);
		GL11.glScaled(0.75, 0.75, 0.75);
		Texture spoutcraftLogo = CustomTextureManager.getTextureFromJar("/res/spoutcraft.png");
		MCRenderDelegate r = (MCRenderDelegate) Spoutcraft.getRenderDelegate();
		r.drawTexture(spoutcraftLogo, 256, 64);

		GL11.glPopMatrix();

		if (Keyboard.isKeyDown(Keyboard.KEY_M)) {
			mc.displayGuiScreen(new org.spoutcraft.client.gui.server.GuiFavorites(this));
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			mc.displayGuiScreen(new GuiSelectWorld(new GuiMainMenu()));
		} else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			mc.displayGuiScreen(new GuiAddonsLocal());
		} else if (Keyboard.isKeyDown(Keyboard.KEY_T)) {
			mc.displayGuiScreen(new org.spoutcraft.client.gui.texturepacks.GuiTexturePacks());
		} else if (Keyboard.isKeyDown(Keyboard.KEY_O)) {
			mc.displayGuiScreen(new GameSettingsScreen(new GuiMainMenu()));
		}
		//Spout End

		// Spout Start
		// this.drawString(this.fontRenderer, "Minecraft 1.1", 2, this.height - 10, 16777215); //Spout
		// String var9 = "Copyright Mojang AB. Do not distribute!";
		// this.drawString(this.fontRenderer, var9, this.width - this.fontRenderer.getStringWidth(var9) - 2, this.height - 10, 16777215); //Spout

		// var9 = "Spoutcraft " + SpoutClient.getClientVersion().toString() + " - Minecraft Beta 1.8.1" + " - " + var9;
		// this.drawCenteredString(this.fontRenderer, var9, this.width / 2, this.height - 10, 0x1F3C8E);
		// Spout End- 2, height - 10, 0xffffff);
		super.drawScreen(par1, par2, par3);
	}
}
