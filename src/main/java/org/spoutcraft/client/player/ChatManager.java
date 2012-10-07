/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
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
package org.spoutcraft.client.player;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.yaml.snakeyaml.Yaml;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ChatAllowedCharacters;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiChat;
import net.minecraft.src.GuiPlayerInfo;

import org.bukkit.ChatColor;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.entity.Player;
import org.spoutcraft.api.gui.ChatTextBox;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.config.Configuration;
import org.spoutcraft.client.io.FileUtil;
import org.spoutcraft.client.packet.PacketCustomBlockChunkOverride;

public class ChatManager implements org.spoutcraft.api.player.ChatManager {
	public int commandScroll = 0;
	public int messageScroll = 0;
	public int chatScroll = 0;
	public String tabHelp = null;
	public List<String> pastCommands = new LinkedList<String>();
	public List<String> pastMessages = new LinkedList<String>();

	public List<String> wordHighlight = new ArrayList<String>();
	public List<String> ignorePeople = new ArrayList<String>();

	public boolean onChatKeyTyped(char character, int key, GuiChat chat) {
		try {
			boolean control = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
			String message = chat.message;
			int cursor = chat.cursorPosition;

			if (message.length() > 99 && message.startsWith("/")) {
				return false; // Block long commands
			}

			if (ChatAllowedCharacters.allowedCharacters.indexOf(character) > -1 || character > 32) {
				if (cursor == 0) {
					message = character + message;
				} else if (cursor > 0 && cursor < message.length()) {
					message = (message.substring(0, cursor) + character + message.substring(cursor));
				} else {
					message += character;
				}
				updateMessage(message, chat);
				updateCursor(++cursor, chat);
			} else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) && cursor > 0) {
				updateCursor(--cursor, chat);
			} else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && cursor < message.length()) {
				updateCursor(++cursor, chat);
			} else if (Keyboard.isKeyDown(Keyboard.KEY_DELETE) && cursor < message.length()) {
				message = message.substring(0, cursor) + message.substring(cursor + 1);
				updateMessage(message, chat);
			} else if (Keyboard.isKeyDown(Keyboard.KEY_PRIOR) && chatScroll < getChatHistorySize()) {
				chatScroll += 20;
				chatScroll = Math.min(getChatHistorySize() - 1, chatScroll);
			} else if (Keyboard.isKeyDown(Keyboard.KEY_NEXT) && chatScroll > 0) {
				chatScroll -= 20;
				chatScroll = Math.max(0, chatScroll);
			} else if (Keyboard.isKeyDown(Keyboard.KEY_HOME)) {
				chatScroll = 0;
			} else if (Keyboard.isKeyDown(Keyboard.KEY_END)) {
				chatScroll = getChatHistorySize() - 2;
				if (chatScroll < 0) {
					chatScroll = 0;
				}
			} else if (control && Keyboard.isKeyDown(Keyboard.KEY_UP)) {
				if (commandScroll < pastCommands.size()) {
					commandScroll++;
					message = updateCommandScroll(chat);
					updateMessage(message, chat);
				}
			} else if (control && Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
				if (commandScroll > 0) {
					commandScroll--;
					message = updateCommandScroll(chat);
					updateMessage(message, chat);
				}
			} else if (Keyboard.isKeyDown(Keyboard.KEY_UP) && messageScroll < pastMessages.size()) {
				messageScroll++;
				message = updateMessageScroll(chat);
				updateMessage(message, chat);
			} else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && messageScroll > 0) {
				messageScroll--;
				message = updateMessageScroll(chat);
				updateMessage(message, chat);
			} else if (control && Keyboard.isKeyDown(Keyboard.KEY_V)) {
				String paste = paste();
				message = (message.substring(0, cursor) + paste + message.substring(Math.min(cursor, message.length())));
				cursor += paste.length();
				updateMessage(message, chat);
				updateCursor(cursor, chat);
			} else if (control && Keyboard.isKeyDown(Keyboard.KEY_C)) {
				copy(message);
			} else if (Keyboard.isKeyDown(Keyboard.KEY_BACK) && message.length() > 0 && cursor > 0) {
				if (message.length() == 1) {
					message = "";
				} else {
					String text = message;
					if (cursor > text.length()) {
						cursor = text.length();
					}
					if (cursor > 1) {
						text = message.substring(0, cursor - 1);
					} else {
						text = "";
					}
					if (cursor > 0) {
						text += message.substring(cursor);
					}
					message = text;
				}
				updateCursor(--cursor, chat);
				updateMessage(message, chat);
			} else if (Keyboard.isKeyDown(Keyboard.KEY_TAB) && tabHelp != null && cursor == message.length()) { 
				message = message + tabHelp + " ";
				cursor += tabHelp.length() + 1;
				updateMessage(message, chat);
				updateCursor(cursor, chat);
			} else { // Not handled
				return false;
			}

			// Handled
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public int getChatHistorySize() {
		return ChatTextBox.getNumChatMessages();
	}

	public int checkCursor(String message, int cursor) {
		if (cursor > message.length()) {
			cursor = message.length();
		} else if (cursor < 0) {
			cursor = 0;
		}
		return cursor;
	}

	public String updateCommandScroll(GuiChat chat) {
		String command;
		if (commandScroll == 0) {
			command = "";
		} else {
			command = (String)pastCommands.get(pastCommands.size() - commandScroll);
		}
		updateMessage(command, chat);
		updateCursor(command.length(), chat);
		return command;
	}

	public String updateMessageScroll(GuiChat chat) {
		String message;
		if (messageScroll == 0) {
			message = "";
		} else {
			message = (String)pastMessages.get(pastMessages.size() - messageScroll);
		}
		updateMessage(message, chat);
		updateCursor(message.length(), chat);
		return message;
	}

	public void updateCursor(int position, GuiChat chat) {
		chat.cursorPosition = checkCursor(chat.message, position);
	}

	public void updateMessage(String message, GuiChat chat) {
		chat.message = message;
		updateCursor(chat.cursorPosition, chat);
	}

	public void sendChat(String message) {
		if (!Spoutcraft.hasPermission("spout.client.chatcolors")) {
			message = message.replaceAll("(&([a-fA-F0-9]))", "");
		}
		List<String> lines = formatChat(message, false);
		for (String chat : lines) {
			SpoutClient.getHandle().thePlayer.sendChatMessage(chat);
		}
	}

	public String formatChatColors(String message) {
		message = message.replaceAll("(&([a-fA-F0-9]))", "\u00A7$2");
		message = message.replaceAll("(&([k-oK-O0-9]))", "\u00A7$2");
		message = message.replaceAll("(&([r|R]))", "\u00A7$2");
		return message;
	}

	public List<String> formatChat(String message, boolean display) {
		final LinkedList<String> lines = new LinkedList<String>();
		final FontRenderer font = Minecraft.theMinecraft.fontRenderer;
		final int width = (int) SpoutClient.getInstance().getActivePlayer().getMainScreen().getChatBar().getWidth() - 6;

		if (message.startsWith("/")) {
			if (display) {
				message = completeNames(message);
			}
			if (message.length() > 99) {
				message = message.substring(0, 100);
			}
			lines.add(message);
			return lines;
		}

		if (display) {
			message = completeNames(message);
		}

		StringBuilder builder = new StringBuilder(100);
		int lineWidth = 0;
		for (int i = 0; i < message.length(); i++) {
			char ch = message.charAt(i);
			int charWidth = font.getStringWidth(String.valueOf(ch));
			if (lineWidth + charWidth < width && (display || builder.length() < 99)) {
				builder.append(ch);
				lineWidth += charWidth;
			} else {
				lines.add(builder.toString());
				builder.delete(0, builder.length());
				builder.append(ch);
				lineWidth = charWidth;
			}
		}

		if (builder.length() > 0) {
			lines.add(builder.toString());
		}

		return lines;
	}

	private String completeNames(String message) {
		tabHelp = null;
		String[] words = message.split(" ");
		if (words.length > 0) {
			String lastWord = words[words.length - 1];
			if (lastWord.endsWith("_")) {
				lastWord = lastWord.substring(0, lastWord.length() - 1);
			}
			if (lastWord.length() > 2) {
				// Check nearby players
				Player p = SpoutClient.getInstance().getPlayer(lastWord);
				String playerName = p != null ? p.getName() : null;

				// Check server player list
				if (playerName == null && SpoutClient.getHandle().isMultiplayerWorld()) {
					int delta = Integer.MAX_VALUE;
					String best = null;
					final List<GuiPlayerInfo> players = ((EntityClientPlayerMP)SpoutClient.getHandle().thePlayer).sendQueue.playerInfoList;
					final String toLower = lastWord.toLowerCase();
					for (GuiPlayerInfo info : players) {
						String name = ChatColor.stripColor(info.name);
						if (name.toLowerCase().startsWith(toLower)) {
							int curDelta = info.name.length() - lastWord.length();
							if (curDelta < delta) {
								best = name;
								delta = curDelta;
							}
							if (curDelta == 0) {
								break;
							}
						}
					}
					if (best != null) {
						playerName = best;
					}
				}

				// Autocomplete
				if (playerName != null && playerName.length() > lastWord.length()) {
					message = message.substring(0, message.length() - 1) + "|" + ChatColor.YELLOW + playerName.substring(lastWord.length()) + ChatColor.RESET;
					tabHelp = playerName.substring(lastWord.length());
				}
			}
		}
		return message;
	}

	public void handleMouseWheel() {
		int wheel = Mouse.getDWheel();
		if (wheel > 0) {
			if (chatScroll < getChatHistorySize()) {
				chatScroll++;
			}
		} else if (wheel < 0) {
			if (chatScroll > 0) {
				chatScroll--;
			}
		}
	}

	public boolean handleCommand(String command) {
		try {
			 if (command.equals("/?") || command.startsWith("/client help")) {
				SpoutClient.getHandle().ingameGUI.addChatMessage(ChatColor.YELLOW.toString() + "Spoutcraft Client Debug Commands:");
				SpoutClient.getHandle().ingameGUI.addChatMessage("/client gc - run the garbage collector");
				return true;
			} else if (command.startsWith("/client gc")) {
				SpoutClient.getHandle().ingameGUI.addChatMessage(ChatColor.YELLOW.toString() + "Starting Garbage Collection...");
				long start = System.currentTimeMillis();
				long mem = Runtime.getRuntime().freeMemory();
				long time = 250;
				if (command.split(" ").length > 2) {
					try {
						time = Long.parseLong(command.split(" ")[2]);
					} catch(Exception ignore) {}
				}
				while ((System.currentTimeMillis() - start) < time) {
					System.gc();
				}
				SpoutClient.getHandle().ingameGUI.addChatMessage(ChatColor.GREEN.toString() + "Garbage Collection Complete!");
				SpoutClient.getHandle().ingameGUI.addChatMessage(ChatColor.GREEN.toString() + (Runtime.getRuntime().freeMemory() - mem) / (1024D * 1024D) + " mb of memory freed");
				return true;
			} else if (command.startsWith("/loadchunks")) {
				int amt = Integer.parseInt(command.split(" ")[1]);
				SpoutClient.getHandle().ingameGUI.addChatMessage(ChatColor.YELLOW.toString() + "Forcing server to load " + amt + " chunks...");
				Random r = new Random();
				for (int i = 0; i < amt; i++) {
					int cx = (r.nextBoolean() ? -1 : 1) * r.nextInt(1000);
					int cz = (r.nextBoolean() ? -1 : 1) * r.nextInt(1000);
					PacketCustomBlockChunkOverride packet = new PacketCustomBlockChunkOverride(cx, cz);
					SpoutClient.getInstance().getPacketManager().sendSpoutPacket(packet);
				}
				SpoutClient.getHandle().ingameGUI.addChatMessage(ChatColor.GREEN.toString() + "Loaded " + amt + " chunks!");
				return true;
			}
		} catch (Exception e) {}
		return false;
	}

	public static void copy(String a) {
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(a), null);
	}

	public static String paste() {
		String str = "";
		Transferable localTransferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
		if (localTransferable != null && localTransferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
			try {
				str = (String)localTransferable.getTransferData(DataFlavor.stringFlavor);
			} catch (Exception e) {}
		}
		return str;
	}

	public static String formatUrl(String message) {
		// TODO Make this work without writing out all the text that follows...
		/*
		int start = -1;
		if (start == -1) {
			start = message.indexOf("http://");
		}
		if (start == -1) {
			start = message.indexOf("https://");
		}
		if (start == -1) {
			start = message.indexOf("ftp://");
		}
		if (start == -1) {
			start = message.indexOf("irc://");
		}
		if (start == -1) {
			start = message.indexOf("www.");
		}
		if (start != -1) {
			char end;
			int endPos = message.length();
			for (int i = start; i < message.length(); i++) {
				end = message.charAt(i);
				endPos = i;
				if (Character.isWhitespace(end)) {
					break;
				}
			}

			String begin = "";
			if (start > 0) {
				begin = message.substring(0, start);
			}
			String ending = "";
			if (endPos < message.length()) {
				ending = message.substring(endPos + 1);
			}
			StringBuffer format = new StringBuffer(begin).append(ChatColor.AQUA.toString()).append(message.substring(start, endPos + 1)).append(ChatColor.WHITE.toString()).append(ending);
			return format.toString();
		}*/
		return message;
	}

	public void load() {
		boolean wasSandboxed = SpoutClient.disableSandbox();
		Yaml yaml = new Yaml();
		try {
			Object l = yaml.load(new FileReader(getFile()));
			HashMap<String, Object> map = (HashMap<String, Object>) l;
			ignorePeople = (List<String>) map.get("ignore");
			wordHighlight = (List<String>) map.get("highlight");
		} catch (FileNotFoundException e) {
			ignorePeople = new ArrayList<String>();
			wordHighlight = new ArrayList<String>();
		}
		SpoutClient.enableSandbox(wasSandboxed);
	}

	public void save() {
		boolean wasSandboxed = SpoutClient.disableSandbox();
		Yaml yaml = new Yaml();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("ignore", ignorePeople);
		map.put("highlight", wordHighlight);
		try {
			yaml.dump(map, new FileWriter(getFile()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		SpoutClient.enableSandbox(wasSandboxed);
	}

	private File getFile() {
		return new File(FileUtil.getConfigDir(), "chatSettings.yml");
	}

	public List<String> getIgnoredPlayers() {
		return ignorePeople;
	}

	public List<String> getWordsToHighlight() {
		return wordHighlight;
	}

	public boolean isHighlightingWords() {
		return Configuration.isHighlightMentions();
	}

	public boolean isIgnoringPlayers() {
		return Configuration.isIgnorePeople();
	}

	public int getScroll() {
		return chatScroll;
	}

	public boolean isShowingJoins() {
		return Configuration.isShowJoinMessages();
	}

	public boolean isUsingRegex() {
		return Configuration.isChatUsesRegex();
	}

	public float getOpacity() {
		return Configuration.getChatOpacity();
	}
}
