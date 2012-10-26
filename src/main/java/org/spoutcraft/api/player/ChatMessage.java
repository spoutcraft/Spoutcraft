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
package org.spoutcraft.api.player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.spoutcraft.api.ChatColor;
import org.spoutcraft.api.Spoutcraft;

public class ChatMessage {
	private String unparsedMessage = null;
	private String message = null;
	private boolean highlighted = false;
	private boolean joinMessage = false;
	private boolean ignoredPerson = false;
	private String player = null;
	private int age = 0;

	static final String messageFormats[] = {
		"^<(.*)>\\s{0,1}(.*)$", //Vanilla format
		"^(.*):\\s{0,1}(.*)$",
		"^(.*)\\s{0,1}\\[.*\\]: (.*)$", //Format for teh cool admin folks
		"^\\[(.*)\\] (.*)$", //Bukkit console say command ([Server] hello)
	};

	static final Pattern messagePatterns[];

	static {
		messagePatterns = new Pattern[messageFormats.length];
		int i = 0;
		for (String format:messageFormats) {
			Pattern pattern = Pattern.compile(format);
			messagePatterns[i] = pattern;
			i++;
		}
	}

	public ChatMessage(String unparsed, String message, boolean highlighted, String player) {
		this.unparsedMessage = unparsed;
		this.message = message;
		this.highlighted = highlighted;
		this.player = player;
	}

	public ChatMessage(String unparsed, String message, boolean highlighted) {
		this(unparsed, message, highlighted, null);
	}

	public ChatMessage(String unparsed, String message, String player) {
		this(unparsed, message, false, player);
	}

	public ChatMessage(String unparsed, String message) {
		this(unparsed, message, false, null);
	}

	public ChatMessage(String unparsed) {
		copy(parseMessage(unparsed));
	}

	public ChatMessage(ChatMessage other) {
		copy(other);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isHighlighted() {
		return highlighted;
	}

	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}

	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	public String getUnparsedMessage() {
		return unparsedMessage;
	}

	public void setUnparsedMessage(String unparsedMessage) {
		this.unparsedMessage = unparsedMessage;
	}

	public void copy(ChatMessage other) {
		unparsedMessage = other.unparsedMessage;
		message = other.message;
		highlighted = other.highlighted;
		player = other.player;
		ignoredPerson = other.ignoredPerson;
		joinMessage = other.joinMessage;
	}

	public ChatMessage clone() {
		return new ChatMessage(this);
	}

	public boolean hasPlayer() {
		return player != null;
	}

	public void reparse() {
		copy(parseMessage(unparsedMessage));
	}

	public int getAge() {
		return age;
	}

	public boolean isJoinMessage() {
		return joinMessage;
	}

	public void setJoinMessage(boolean joinMessage) {
		this.joinMessage = joinMessage;
	}

	public boolean isIgnoredPerson() {
		return ignoredPerson;
	}

	public void setIgnoredPerson(boolean ignoredPerson) {
		this.ignoredPerson = ignoredPerson;
	}

	public void onTick() {
		age ++;
	}

	public static ChatMessage parseMessage(String unparsed) {
//		String player = null;
//		boolean highlighted = false;
//		unparsed = Spoutcraft.getChatManager().formatChatColors(unparsed);
//		String message = unparsed;
//		for (Pattern p:messagePatterns) {
//			Matcher m = p.matcher(ChatColor.stripColor(unparsed));
//			if (m.matches()) {
//				player = m.group(1);
//				message = m.group(2);
//				break;
//			}
//		}
//		String playerName = Spoutcraft.getActivePlayer().getName();
//		if (!playerName.equals(player)) { //Don't highlight our own messages
//			if (message.contains(playerName)) {
//				highlighted = true;
//			} else {
//				for (String highlightWord:Spoutcraft.getChatManager().getWordsToHighlight()) {
//					if (Spoutcraft.getChatManager().isUsingRegex()) {
//						try {
//							if (message.matches(".*"+highlightWord+".*")) {
//								highlighted = true;
//								break;
//							}
//						} catch (PatternSyntaxException e) {}
//					} else {
//						if (message.contains(highlightWord)) {
//							highlighted = true;
//							break;
//						}
//					}
//				}
//			}
//		}
//		if (player != null) {
//			player = ChatColor.stripColor(player);
//		}
//		ChatMessage ret = new ChatMessage(unparsed, message, highlighted, player);
//		if (message.contains("has joined")) {
//			ret.setJoinMessage(true);
//		}
//		if (ret.hasPlayer()) {
//			for (String name:Spoutcraft.getChatManager().getIgnoredPlayers()) {
//				if (Spoutcraft.getChatManager().isUsingRegex()) {
//					try {
//						if (ret.getPlayer().matches(".*"+name+".*")) {
//							ret.setIgnoredPerson(true);
//							break;
//						}
//					} catch (PatternSyntaxException e) {}
//				} else {
//					if (ret.getPlayer().equalsIgnoreCase(name)) {
//						ret.setIgnoredPerson(true);
//						break;
//					}
//				}
//			}
//		}
//		return ret;
		return null;
	}
}
