package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ChatAllowedCharacters;
import net.minecraft.src.GuiScreen;

import org.bukkit.ChatColor;
import org.lwjgl.input.Keyboard;
//Spout Start
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.config.ConfigReader;
import org.spoutcraft.spoutcraftapi.gui.ChatBar;
//Spout End

public class GuiChat extends GuiScreen {
	//Spout Improved Chat Start
	public String message = "";
	public int updateCounter = 0;
	public static final String allowedCharacters = ChatAllowedCharacters.allowedCharacters;
	private static String lastChat = "";
	
	public int cursorPosition = 0;
	public GuiChat() {
		SpoutClient.getInstance().getChatManager().chatScroll = 0;
		SpoutClient.getInstance().getChatManager().commandScroll = 0;
		message = GuiChat.lastChat;
		SpoutClient.getInstance().getChatManager().updateCursor(message.length(), this);
		GuiChat.lastChat = "";
	}
	//Spout Improved Chat End

	public void initGui() {
		Keyboard.enableRepeatEvents(true);
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	public void updateScreen() {
		++this.updateCounter;
	}

	protected void keyTyped(char var1, int var2) {
		//Spout Improved ChatStart
		if (SpoutClient.getInstance().getChatManager().onChatKeyTyped(var1, var2, this)) {
			return;
		}
		//Spout Improved Chat End
		if(var2 == 1) {
			this.mc.displayGuiScreen((GuiScreen)null);
		} else if(var2 == 28) {
			String var3 = this.message.trim();
			if(var3.length() > 0) {
				String var4 = this.message.trim();
				//Spout Improved Chat Start
				if (var4.startsWith("/")) {
					SpoutClient.getInstance().getChatManager().pastCommands.add(var4);
				}
				else {
					SpoutClient.getInstance().getChatManager().pastMessages.add(var4);
				}
				//Spout Improved Chat End
				//if(!this.mc.lineIsCommand(var4)) {
				if (!SpoutClient.getInstance().getChatManager().handleCommand(var4)) {
					//Spout Improved Chat  Start
					SpoutClient.getInstance().getChatManager().sendChat(var4);
					//Spout Improved Chat End
					//this.mc.thePlayer.sendChatMessage(var4);
					
				}
			}

			this.mc.displayGuiScreen((GuiScreen)null);
		} else {
			if(var2 == 14 && this.message.length() > 0) {
				this.message = this.message.substring(0, this.message.length() - 1);
			}

			if((allowedCharacters.indexOf(var1) >= 0 || var1 > 32) && this.message.length() < 100) {
				this.message = this.message + var1;
			}

		}
	}

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

	protected void mouseClicked(int var1, int var2, int var3) {
		if(var3 == 0) {
			if(this.mc.ingameGUI.field_933_a != null) {
				if(this.message.length() > 0 && !this.message.endsWith(" ")) {
					this.message = this.message + " ";
				}

				this.message = this.message + this.mc.ingameGUI.field_933_a;
				//Spout Improved Chat Start
				/*
				byte var4 = 100;
				if(this.message.length() > var4) {
					this.message = this.message.substring(0, var4);
				}
				*/
				super.drawScreen(var1, var2, var3);
				//Spout Improved Chat End
			} else {
				super.mouseClicked(var1, var2, var3);
			}
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
