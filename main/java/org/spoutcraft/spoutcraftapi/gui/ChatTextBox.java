/*
 * This file is part of Spoutcraft (http://wiki.getspout.org/).
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
package org.spoutcraft.spoutcraftapi.gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class ChatTextBox extends GenericWidget implements Widget{
	protected int visibleLines = 10;
	protected int visibleChatLines = 20;
	protected int fadeoutTicks = 250;
	public ChatTextBox() {

	}
	
	@Override
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
	 
	}
	
	/**
	 * Gets the number of visible lines of chat for the player
	 * @return visible chat lines
	 */
	public int getNumVisibleLines() {
		return visibleLines;
	}
	
	/**
	 * Sets the number of visible lines of chat for the player
	 * @param lines to view
	 * @return ChatTextBox
	 */
	public ChatTextBox setNumVisibleLines(int lines) {
		visibleLines = lines;
		return this;
	}
	
	/**
	 * Gets the number of visible lines of chat for the player, when fully opened
	 * @return visible chat lines
	 */
	public int getNumVisibleChatLines() {
		return visibleChatLines;
	}
	
	/**
	 * Sets the number of visible lines of chat for the player, when fully opened
	 * @param lines to view
	 * @return ChatTextBox
	 */
	public ChatTextBox setNumVisibleChatLines(int lines) {
		visibleChatLines = lines;
		return this;
	}
	
	/**
	 * The number ticks until the text fades out from the main screen
	 * @return fadeout ticks
	 */
	public int getFadeoutTicks() {
		return fadeoutTicks;
	}
	
	/**
	 * Sets the number of ticks until the text fades out from the main screen.
	 * 20 ticks is equivelent to one second.
	 * @param ticks to set
	 * @return this
	 */
	public ChatTextBox setFadeoutTicks(int ticks) {
		fadeoutTicks = ticks;
		return this;
	}
	
	@Override
	public int getVersion() {
		return super.getVersion() + 1;
	}

}
