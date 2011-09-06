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

import org.spoutcraft.spoutcraftapi.packet.PacketUtil;

public class ChatBar extends GenericWidget implements Widget{
	private int cursorX = 4, cursorY = 240;
	protected Color textColor = new Color((short)255, (short)255, (short)255, (short)0);
	public ChatBar() {
		super();
		setX(2);
		setY(-2);
		setWidth(425);
		setHeight(12);
		setAnchor(WidgetAnchor.BOTTOM_LEFT);
	}
	
	public int getNumBytes() {
		return super.getNumBytes() + 8 + 16;
	}
	
	@Override
	public void readData(DataInputStream input) throws IOException {
		super.readData(input);
		setCursorX(input.readInt());
		setCursorY(input.readInt());
		setTextColor(PacketUtil.readColor(input));
	}
	
	@Override
	public void writeData(DataOutputStream output) throws IOException {
		super.writeData(output);
		output.writeInt(getCursorX());
		output.writeInt(getCursorY());
		PacketUtil.writeColor(output, getTextColor());
	}
	
	public WidgetType getType() {
		return WidgetType.ChatBar;
	}
	
	@Override
	public UUID getId() {
		return new UUID(0, 2);
	}
	
	public int getCursorX() {
		return cursorX;
	}
	
	public ChatBar setCursorX(int x) {
		cursorX = x;
		return this;
	}
	
	public int getCursorY() {
		int diff = 240 - cursorY;
		return (int) (getScreen() != null ? getScreen().getHeight() - diff : cursorY);
	}
	
	public ChatBar setCursorY(int y){
		cursorY = y;
		return this;
	}
	
	public Color getTextColor() {
		return textColor;
	}
	
	public ChatBar setTextColor(Color color) {
		textColor = color;
		return this;
	}
	
	public void render() {
		
	}
	
	@Override
	public int getVersion() {
		return super.getVersion() + 1;
	}

}
