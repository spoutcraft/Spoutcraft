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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.lwjgl.opengl.GL11;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.UnsafeClass;
import org.spoutcraft.spoutcraftapi.player.ChatMessage;

@UnsafeClass
public class ChatTextBox extends GenericWidget implements Widget {
	protected int visibleLines = 10;
	protected int visibleChatLines = 15;
	protected int fadeoutTicks = 20 * 5;
	protected List<ChatMessage> chatMessages = new LinkedList<ChatMessage>();

	public ChatTextBox() {
		
	}

	public WidgetType getType() {
		return WidgetType.ChatTextBox;
	}

	@Override
	public int getNumBytes() {
		return super.getNumBytes() + 12;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		super.readData(input);
		setNumVisibleLines(input.readInt());
		setNumVisibleChatLines(input.readInt());
		setFadeoutTicks(input.readInt());
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		super.writeData(output);
		output.writeInt(getNumVisibleLines());
		output.writeInt(getNumVisibleChatLines());
		output.writeInt(getFadeoutTicks());
	}

	public UUID getId() {
		return new UUID(0, 3);
	}
	
	public void render() {
		if(!isVisible()) {
			return;
		}
		int scroll = Spoutcraft.getChatManager().getScroll();
		boolean chatOpen = false;
		if(Spoutcraft.getActivePlayer() != null && Spoutcraft.getActivePlayer().getCurrentScreen() != null && Spoutcraft.getActivePlayer().getCurrentScreen().getScreenType() == ScreenType.CHAT_SCREEN) {
			chatOpen = true;
		}
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
			if(message.isJoinMessage() && !Spoutcraft.getChatManager().isShowingJoins()) {
				continue;
			}
			if(message.getAge() > getFadeoutTicks() && !chatOpen) {
				break;
			}
			//TODO Animation
			int color = 0x80000000;
			if(Spoutcraft.getChatManager().isHighlightingWords() && message.isHighlighted() && !message.isJoinMessage()) {
				color = 0x60ff0000;
			}
			RenderUtil.drawRectangle(3, bottom - 2, 3 + 320, bottom + 9, color);
			font.drawShadowedString(message.getUnparsedMessage(), 4, bottom, 0xffffff);
			bottom -= 11;
			lines ++;
			if(!chatOpen && lines > visibleLines) {
				break;
			} else if (chatOpen && lines > visibleChatLines) {
				break;
			}
		}
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
	
	public void addChatMessage(ChatMessage message) {
		if(message.getUnparsedMessage().trim().isEmpty()) {
			return;
		}
		chatMessages.add(0, message);
		while (chatMessages.size() > 3000) {
			chatMessages.remove(chatMessages.size() - 1);
		}
	}

	@Override
	public int getVersion() {
		return super.getVersion() + 1;
	}
}
