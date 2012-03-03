package net.minecraft.src;

import net.minecraft.client.Minecraft;

import org.bukkit.ChatColor;
import org.lwjgl.input.Keyboard;
//Spout Start
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.config.ConfigReader;
import org.spoutcraft.spoutcraftapi.gui.ChatBar;
//Spout End

public class GuiChat extends GuiScreen
{
	/** The chat message. */
	public String message = ""; //Spout protected -> public

	/** Counts the number of screen updates. Used to make the caret flash. */
	private int updateCounter = 0; //Spout
	
	//Spout Improved Chat Start
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
	//Spout Improved Chat End

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
		//Spout Improved ChatStart
		if (SpoutClient.getInstance().getChatManager().onChatKeyTyped(par1, par2, this)) {
			return;
		}
		//Spout Improved Chat End
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

				//Spout Improved Chat Start
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
				//Spout Improved Chat End
			}

			mc.displayGuiScreen(null);
			return;
		}

		if (par2 == 14 && message.length() > 0)
		{
			message = message.substring(0, message.length() - 1);
		}

		if (!(!ChatAllowedCharacters.func_48614_a(par1) || message.length() >= 100))
		{
			message += par1;
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int var1, int var2, float var3) {
		//Spout Improved Chat Start
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
		drawRect((int)chatWidget.getScreenX(), (int)(chatWidget.getScreenY() - lines.size() * chatWidget.getHeight()), (int)chatWidget.getScreenX() + (int)chatWidget.getWidth(), (int)chatWidget.getScreenY(), 0x80000000);
		int size = lines.size();
		int color = chatWidget.getTextColor().toInt();
		for (int k = 0; k < lines.size(); k++) {
			String line = lines.get(k);
			drawString(fontRenderer, line, chatWidget.getCursorX(), chatWidget.getCursorY() - 12 * size--, color);
		}
		
		if (ConfigReader.showChatColors) {
			drawString(fontRenderer, ChatColor.BLACK + "&0 - Black", width - 90, 10, 0xFFFFFFF);
			drawString(fontRenderer, ChatColor.DARK_BLUE + "&1 - Dark Blue", width - 90, 20, 0xFFFFFFF);
			drawString(fontRenderer, ChatColor.DARK_GREEN + "&2 - Dark Green", width - 90, 30, 0xFFFFFFF);
			drawString(fontRenderer, ChatColor.DARK_AQUA + "&3 - Dark Aqua", width - 90, 40, 0xFFFFFFF);
			drawString(fontRenderer, ChatColor.DARK_RED + "&4 - Dark Red", width - 90, 50, 0xFFFFFFF);
			drawString(fontRenderer, ChatColor.DARK_PURPLE + "&5 - Dark Purple", width - 90, 60, 0xFFFFFFF);
			drawString(fontRenderer, ChatColor.GOLD + "&6 - Gold", width - 90, 70, 0xFFFFFFF);
			drawString(fontRenderer, ChatColor.GRAY + "&7 - Grey", width - 90, 80, 0xFFFFFFF);
			drawString(fontRenderer, ChatColor.DARK_GRAY + "&8 - Dark Grey", width - 90, 90, 0xFFFFFFF);
			drawString(fontRenderer, ChatColor.BLUE + "&9 - Blue", width - 90, 100, 0xFFFFFFF);
			drawString(fontRenderer, ChatColor.GREEN + "&a - Green", width - 90, 110, 0xFFFFFFF);
			drawString(fontRenderer, ChatColor.AQUA + "&b - Aqua", width - 90, 120, 0xFFFFFFF);
			drawString(fontRenderer, ChatColor.RED + "&c - Red", width - 90, 130, 0xFFFFFFF);
			drawString(fontRenderer, ChatColor.LIGHT_PURPLE + "&d - Light Purple", width - 90, 140, 0xFFFFFFF);
			drawString(fontRenderer, ChatColor.YELLOW + "&e - Yellow", width - 90, 150, 0xFFFFFFF);
			drawString(fontRenderer, ChatColor.WHITE + "&f - White", width - 90, 160, 0xFFFFFFF);
		}
		//Spout Improved Chat End
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

		if (mc.ingameGUI.field_933_a == null)
		{
			super.mouseClicked(par1, par2, par3);
			return;
		}

		//Spout Improved Chat Start
//		if (!(message.length() <= 0 || message.endsWith(" ")))
//		{
//			message += " ";
//		}
		//Spout Improved Chat End

		message += mc.ingameGUI.field_933_a;
		byte byte0 = 100;

		if (message.length() > byte0)
		{
			message = message.substring(0, byte0);
		}
	}
	
	//Spout start
	public static void interruptChat() {
		if (Minecraft.theMinecraft.currentScreen instanceof GuiChat) {
			if (ConfigReader.showDamageAlerts) {
				GuiChat chat = (GuiChat) Minecraft.theMinecraft.currentScreen;
				GuiChat.lastChat = chat.message;
				Minecraft.theMinecraft.displayGuiScreen(null);
			}
		}
	}
	//Spout end
}
