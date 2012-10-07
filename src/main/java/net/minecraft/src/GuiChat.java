package net.minecraft.src;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
// Spout Start
import java.io.File;
import org.bukkit.ChatColor;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.config.Configuration;
import org.spoutcraft.client.gui.chat.GuiURLConfirm;
import org.spoutcraft.api.gui.ChatBar;
// Spout End

public class GuiChat extends GuiScreen
{
	/** The chat message. */
	public String message = ""; // Spout protected -> public

	/** Counts the number of screen updates. Used to make the caret flash. */
	private int updateCounter = 0; // Spout
	
	// Spout Improved Chat Start
	public static final String allowedCharacters = ChatAllowedCharacters.allowedCharacters;
	private static String lastChat = "";
	
	public int cursorPosition = 0;
	
	public GuiChat()
	{
		SpoutClient.getInstance().getChatManager().chatScroll = 0;
		SpoutClient.getInstance().getChatManager().commandScroll = 0;
		message = GuiChat.lastChat;
		SpoutClient.getInstance().getChatManager().updateCursor(message.length(), this);
		GuiChat.lastChat = "";
	}
	// Spout Improved Chat End

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui()
	{
		Keyboard.enableRepeatEvents(true);
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	public void onGuiClosed()
	{
		Keyboard.enableRepeatEvents(false);
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen()
	{
		updateCounter++;
	}

	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char par1, int par2)
	{
		// Spout Improved ChatStart
		if (SpoutClient.getInstance().getChatManager().onChatKeyTyped(par1, par2, this)) {
			return;
		}
		// Spout Improved Chat End
		if (par2 == 1)
		{
			mc.displayGuiScreen(null);
			return;
		}

		if (par2 == 28)
		{
			String s = message.trim();

			if (s.length() > 0)
			{
				String var4 = message.trim();

				// Spout Improved Chat Start
				if (var4.startsWith("/")) {
					SpoutClient.getInstance().getChatManager().pastCommands.add(var4);
				}
				else {
					SpoutClient.getInstance().getChatManager().pastMessages.add(var4);
				}
				//if(!this.mc.lineIsCommand(var4)) {
				if (!SpoutClient.getInstance().getChatManager().handleCommand(var4)) {
					SpoutClient.getInstance().getChatManager().sendChat(var4);
					//this.mc.thePlayer.sendChatMessage(var4);
				}
				// Spout Improved Chat End
			}

			mc.displayGuiScreen(null);
			return;
		}

		if (par2 == 14 && message.length() > 0)
		{
			message = message.substring(0, message.length() - 1);
		}

		if (ChatAllowedCharacters.isAllowedCharacter(par1) && message.length() < 100)
		{
			message += par1;
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int var1, int var2, float var3) {
		// Spout Improved Chat Start
		SpoutClient.getInstance().getChatManager().handleMouseWheel();
		String text = message;
		if (cursorPosition >= 0 && cursorPosition < message.length()) {
			text = message.substring(0, cursorPosition) + "|" + message.substring(cursorPosition);
		}
		else {
			text += "_";
		}
		ChatBar chatWidget = SpoutClient.getInstance().getActivePlayer().getMainScreen().getChatBar();
		java.util.List<String> lines = SpoutClient.getInstance().getChatManager().formatChat(text, true);
		chatWidget.setWidth(width - 2);
		drawRect(2, this.height - (int)(lines.size() * chatWidget.getHeight()), this.width - 2, this.height - 2, Integer.MIN_VALUE);
		int size = lines.size();
		int color = chatWidget.getTextColor().toInt();
		for (int k = 0; k < lines.size(); k++) {
			String line = lines.get(k);
			drawString(fontRenderer, line, chatWidget.getCursorX(), chatWidget.getCursorY() - 12 * size--, color);
		}
		
		if (Configuration.isShowChatColors()) {
			for(int c = 0; c < 16; c++) {
				ChatColor value = ChatColor.getByCode(c);
				String name = value.name().toLowerCase();
				boolean lastUnderscore = true;
				String parsedName = "";
				for(int chr = 0; chr < name.length(); chr++) {
					char ch = name.charAt(chr);
					if(lastUnderscore) {
						ch = Character.toUpperCase(ch);
					}
					if(ch == '_') {
						lastUnderscore = true;
						ch = ' ';
					} else {
						lastUnderscore = false;
					}
					parsedName += ch;
				}
				char code = (char) ('0' + c);
				if(c >= 10) {
					code = (char) ('a' + c - 10);
				}
				fontRenderer.drawStringWithShadow("&" + code + " - " + value + parsedName, width - 90, 70 + c * 10, 0xffffffff);
			}
		}
		// Spout Improved Chat End
		super.drawScreen(var1, var2, var3);
	}

	/**
	 * Called when the mouse is clicked.
	 */
	protected void mouseClicked(int par1, int par2, int par3)
	{
		if (par3 != 0)
		{
			return;
		}
		
		ChatClickData var4 = this.mc.ingameGUI.func_50012_a(Mouse.getX(), Mouse.getY());
		if (var4 != null) {
			URI var5 = var4.getURI();
			if (var5 != null) {
				// Spout Start
				//Check if screenshot
				if (var4.getMessage().startsWith("Saved screenshot as")) {
					String fileName = var4.func_78309_f();
					File screenshotsDir = new File(Minecraft.getMinecraftDir(), "screenshots");
					File screenshot = new File(screenshotsDir, fileName);
					try {
						Desktop.getDesktop().open(screenshot);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					this.mc.displayGuiScreen(new GuiURLConfirm(this, var4.func_78309_f(), var5));
				}
				return;
			}
		}

//		if (mc.ingameGUI.field_933_a == null)
//		{
//			super.mouseClicked(par1, par2, par3);
//			return;
//		}
		
		

		// Spout Improved Chat Start
//		if (!(message.length() <= 0 || message.endsWith(" ")))
//		{
//			message += " ";
//		}
		// Spout Improved Chat End

		//message += mc.ingameGUI.field_933_a;
///		byte byte0 = 100;

//		if (message.length() > byte0)
//		{
//			message = message.substring(0, byte0);
//		}
	}
	
	// Spout Start
	public static void interruptChat() {
		if (Minecraft.theMinecraft.currentScreen instanceof GuiChat) {
			if (Configuration.isShowDamageAlerts()) {
				GuiChat chat = (GuiChat) Minecraft.theMinecraft.currentScreen;
				GuiChat.lastChat = chat.message;
				Minecraft.theMinecraft.displayGuiScreen(null);
			}
		}
	}
	// Spout End
}
