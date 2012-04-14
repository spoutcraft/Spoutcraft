/*
 * This file is part of SpoutcraftAPI (http://wiki.getspout.org/).
 * 
 * SpoutcraftAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SpoutcraftAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.spoutcraftapi.gui;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.lwjgl.opengl.GL11;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.UnsafeClass;
import org.spoutcraft.spoutcraftapi.io.SpoutInputStream;
import org.spoutcraft.spoutcraftapi.io.SpoutOutputStream;
import org.spoutcraft.spoutcraftapi.player.ChatMessage;

@UnsafeClass
public class ChatTextBox extends GenericWidget implements Widget {
	protected int visibleLines = 10;
	protected int visibleChatLines = 20;
	protected int fadeoutTicks = 20 * 5;
	protected static final List<ChatMessage> chatMessages = new LinkedList<ChatMessage>();

	public ChatTextBox() {
		
	}

	public WidgetType getType() {
		return WidgetType.ChatTextBox;
	}

	@Override
	public void readData(SpoutInputStream input) throws IOException {
		super.readData(input);
		setNumVisibleLines(input.readInt());
		setNumVisibleChatLines(input.readInt());
		setFadeoutTicks(input.readInt());
	}

	@Override
	public void writeData(SpoutOutputStream output) throws IOException {
		super.writeData(output);
		output.writeInt(getNumVisibleLines());
		output.writeInt(getNumVisibleChatLines());
		output.writeInt(getFadeoutTicks());
	}

	public UUID getId() {
		return new UUID(0, 3);
	}
	
	private boolean chatOpen = false;
	public void setChatOpen(boolean chat) {
		chatOpen = chat;
	}
	
	public void render() {
		GL11.glEnable(GL11.GL_BLEND);
		if(!isVisible()) {
			return;
		}
		int scroll = Spoutcraft.getChatManager().getScroll();
		MinecraftFont font = Spoutcraft.getMinecraftFont();
		Iterator<ChatMessage> iter = chatMessages.iterator();
		for(int i = 0; i < scroll; i++) {
			if(iter.hasNext()) {
				iter.next();
			}
		}
		int lines = 0;
		int bottom = (int) getScreen().getHeight() - 50;
		while(iter.hasNext()) {
			ChatMessage message = iter.next();
			if(message.isIgnoredPerson() && Spoutcraft.getChatManager().isIgnoringPlayers()) {
				continue;
			}
			if(message.isJoinMessage() && !Spoutcraft.getChatManager().isShowingJoins()) {
				continue;
			}
			double opacity = 1.0D - message.getAge() / getFadeoutTicks();
			if(opacity > 1.0d) {
				opacity = 1.0d;
			} 
			if(opacity < 0.0d) {
				opacity = 0.0d;
			}
			if(message.getAge() > getFadeoutTicks() + 20 && !chatOpen) {
				break;
			}
			if (chatOpen) {
				opacity = 1D;
			}
			//int chatColor =  (chatOpen ? 255 : (int)(255D * opacity));
			int chatColor = 0xffffffff;
			int backgroundColor = 0x80000000;
			if(Spoutcraft.getChatManager().isHighlightingWords() && message.isHighlighted() && !message.isJoinMessage()) {
				backgroundColor = 0x80ff0000;
			}
			RenderUtil.drawRectangle(3, bottom - 1, 3 + 320, bottom + 9, backgroundColor);
			font.drawShadowedString(message.getUnparsedMessage(), 4, bottom, chatColor);
			bottom -= 10;
			lines ++;
			if(!chatOpen && lines > visibleLines) {
				break;
			} else if (chatOpen && lines > visibleChatLines) {
				break;
			}
		}
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	public void increaseAge() {
		Iterator<ChatMessage> iter = chatMessages.iterator();
		while(iter.hasNext()) {
			ChatMessage message = iter.next();
			message.onTick();
			if(message.getAge() > getFadeoutTicks()) {
				break;
			}
		}
	}

	/**
	 * Gets the number of visible lines of chat for the player
	 * 
	 * @return visible chat lines
	 */
	public int getNumVisibleLines() {
		return visibleLines;
	}

	/**
	 * Sets the number of visible lines of chat for the player
	 * 
	 * @param lines to view
	 * @return ChatTextBox
	 */
	public ChatTextBox setNumVisibleLines(int lines) {
		visibleLines = lines;
		return this;
	}

	/**
	 * Gets the number of visible lines of chat for the player, when fully opened
	 * 
	 * @return visible chat lines
	 */
	public int getNumVisibleChatLines() {
		return visibleChatLines;
	}

	/**
	 * Sets the number of visible lines of chat for the player, when fully opened
	 * 
	 * @param lines to view
	 * @return ChatTextBox
	 */
	public ChatTextBox setNumVisibleChatLines(int lines) {
		visibleChatLines = lines;
		return this;
	}

	/**
	 * The number ticks until the text fades out from the main screen
	 * 
	 * @return fadeout ticks
	 */
	public int getFadeoutTicks() {
		return fadeoutTicks;
	}

	/**
	 * Sets the number of ticks until the text fades out from the main screen. 20 ticks is equivelent to one second.
	 * 
	 * @param ticks to set
	 * @return this
	 */
	public ChatTextBox setFadeoutTicks(int ticks) {
		fadeoutTicks = ticks;
		return this;
	}
	
	public static void addChatMessage(ChatMessage message) {
		if(message.getUnparsedMessage().trim().equals("")) {
			return;
		}
		chatMessages.add(0, message);
		while (chatMessages.size() > 3000) {
			chatMessages.remove(chatMessages.size() - 1);
		}
	}
	
	public static void reparse() {
		for(ChatMessage message:chatMessages) {
			message.reparse();
		}
	}
	
	public static void clearChat() {
		chatMessages.clear();
	}
	
	public static int getNumChatMessages() {
		return chatMessages.size();
	}
	
	public static String getChatMessageAt(int pos) {
		if (pos > -1 && pos < chatMessages.size()) {
			return chatMessages.get(pos).getMessage();
		}
		return "";
	}
	
	public static String getPlayerNameAt(int pos) {
		if (pos > -1 && pos < chatMessages.size()) {
			String player = chatMessages.get(pos).getPlayer();
			if (player != null) {
				return player;
			}
		}
		return "";
	}

	@Override
	public int getVersion() {
		return super.getVersion() + 1;
	}
}
